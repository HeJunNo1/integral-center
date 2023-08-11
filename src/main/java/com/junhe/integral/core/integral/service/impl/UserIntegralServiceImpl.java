package com.junhe.integral.core.integral.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Lists;
import com.junhe.integral.api.params.UserIntegralLog;
import com.junhe.integral.api.params.UserIntegralTotalQueryParam;
import com.junhe.integral.common.PageData;
import com.junhe.integral.common.RedisUtil;
import com.junhe.integral.constant.IntegralStatusEnum;
import com.junhe.integral.constant.IntegralType;
import com.junhe.integral.constant.RedisKeys;
import com.junhe.integral.core.inteEvent.dto.IntegralEventDTO;
import com.junhe.integral.core.inteEvent.service.EventRedisCacheService;
import com.junhe.integral.core.integral.dao.UserIntegralDao;
import com.junhe.integral.core.integral.dto.IntegralDecreaseLogDTO;
import com.junhe.integral.core.integral.dto.IntegralDetailDTO;
import com.junhe.integral.core.integral.entity.IntegralDecreaseLogEntity;
import com.junhe.integral.core.integral.entity.IntegralDetailEntity;
import com.junhe.integral.core.integral.entity.UserIntegralEntity;
import com.junhe.integral.core.integral.service.IntegralDecreaseLogService;
import com.junhe.integral.core.integral.service.UserIntegralDetailService;
import com.junhe.integral.core.integral.service.UserIntegralService;
import com.junhe.integral.exception.SystemException;
import com.junhe.integral.util.IntegralExpireTimeUtil;
import com.junhe.integral.util.PointsCalculator;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户积分服务类
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/26
 */
@Service
@Slf4j
public class UserIntegralServiceImpl implements UserIntegralService {

    @Resource
    private UserIntegralDao userIntegralDao;
    @Resource
    private UserIntegralDetailService userIntegralDetailService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private PointsCalculator pointsCalculator;
    @Resource
    private EventRedisCacheService eventRedisCacheService;
    @Resource
    private IntegralDecreaseLogService integralDecreaseLogService;

    @Override
    public PageData usersPage(UserIntegralTotalQueryParam param) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addIntegral(IntegralDetailDTO integralDetailDTO) {
        this.userIntegralChange(integralDetailDTO);
    }

    /**
     * 用户积分变更，使用lua脚本保证原子性
     * @param integralDetailDTO 积分变更信息
     * @author HEJUN
     * @date 2023/7/31
     */
    public void userIntegralChange(IntegralDetailDTO integralDetailDTO){
        RLock lock = redissonClient.getLock(RedisKeys.getUserLockKey(integralDetailDTO.getUserId()));
        lock.lock();

        try {
            //TODO 执行lua脚本
            //1、在缓存中进行积分添加
            //2、将积分变更详情放入redis消息队列，异步入库
            ArrayList<String> keys = Lists.newArrayList(
                    RedisKeys.getUserIntegralTotalKey(integralDetailDTO.getUserId()),
                    RedisKeys.INTEGRAL_STREAM);
            redisUtil.execute("integralChangeScript", keys, integralDetailDTO.getPoints(), integralDetailDTO);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 异步持久化积分到数据库
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void asyncPersistenceIntegral(IntegralDetailDTO integralDetailDTO){
        if (IntegralType.INCREMENT.getCode().equals(integralDetailDTO.getType())) {
            this.addIntegralDetail(integralDetailDTO);
        } else if (IntegralType.DECREMENT.getCode().equals(integralDetailDTO.getType())) {
            this.decrementIntegralDetail(integralDetailDTO);
        }

        RLock lock = redissonClient.getLock(RedisKeys.getUserLockKey(integralDetailDTO.getUserId()));
        lock.lock();
        try {
            updateIntegralTotal(integralDetailDTO);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 修改用户总积分
     * @author HEJUN
     * @date 2023/8/1
     */
    private void updateIntegralTotal(IntegralDetailDTO integralDetailDTO) {
        UserIntegralEntity userIntegralEntity = new UserIntegralEntity();
        userIntegralEntity.setUserId(integralDetailDTO.getUserId());
        userIntegralEntity.setNickname(null);
        userIntegralEntity.setUsername(null);
        userIntegralEntity.setPoints(integralDetailDTO.getPoints());

        if (hasUserAccount(integralDetailDTO.getUserId())) {
            this.update(userIntegralEntity);
        } else {
            this.add(userIntegralEntity);
        }
    }

    /**
     * 检查user积分账户是否存在
     * @param userId 用户ID
     * @return boolean
     */
    private boolean hasUserAccount(String userId) {
        Integer count = userIntegralDao.selectCount(new QueryWrapper<UserIntegralEntity>()
                .lambda().eq(UserIntegralEntity::getUserId, userId));
        return count != 0;
    }

    /**
     * 插入积分变更日志
     * @param integralDetailDTO 积分变更信息详情
     * @author HEJUN
     * @date 2023/7/3
     */
    private void addIntegralDetail(IntegralDetailDTO integralDetailDTO) {
        userIntegralDetailService.insert(integralDetailDTO);
    }

    /**
     * 修改用户积分信息，通常异步执行，避免用户积分频繁修改导致数据库达到瓶颈
     * @author HEJUN
     * @date 2023/7/3
     */
    private void update(UserIntegralEntity entity) {
        userIntegralDao.update(null,
                new UpdateWrapper<UserIntegralEntity>()
                        .lambda()
                        .setSql(" points = points + " + entity.getPoints())
                        .eq(UserIntegralEntity::getUserId, entity.getUserId())
        );
    }

    /**
     * 像用户积分汇总表中插入积分信息，当用户积分汇总表中没有用户积分信息时插入
     * 正常情况下为用户第一次使用时调用
     * @author HEJUN
     * @date 2023/7/3
     */
    private void add(UserIntegralEntity entity) {
        userIntegralDao.insert(entity);
    }

    /**
     * 通过userId获取用户总积分
     * 当缓存中存在用户总积分，则使用缓存中的信息
     * 若缓存中不存在用户积分信息，则从数据库中查询，并将结果放到缓存中
     * @param userId 用户唯一标识
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author HEJUN
     * @date 2023/7/3
     */
    @Override
    public Map<String, Object> getUserIntegralTotal(String userId) {
        Map<String, Object> ret = new HashMap<>(1);
        RLock lock = redissonClient.getLock(RedisKeys.getUserLockKey(userId));
        lock.lock();

        try {
            boolean existsKey = redisUtil.existsKey(RedisKeys.getUserIntegralTotalKey(userId));
            if (existsKey) {
                Integer totalIntegral = (Integer) redisUtil.get(RedisKeys.getUserIntegralTotalKey(userId));
                ret.put("total", totalIntegral);
            } else {
                UserIntegralEntity integralEntity = userIntegralDao.selectOne(new QueryWrapper<UserIntegralEntity>()
                        .lambda().select(UserIntegralEntity::getPoints).eq(UserIntegralEntity::getUserId, userId));
                ret.put("total", integralEntity.getPoints());
                redisUtil.set(RedisKeys.getUserIntegralTotalKey(userId), integralEntity.getPoints(), 1, TimeUnit.HOURS);
            }
        }finally {
            lock.unlock();
        }
        return ret;
    }

    /**
     * 用户积分新增
     * 调用积分新增接口，根据积分事件的积分新增策略添加积分
     * @param log
     * @return void
     * @author HEJUN
     * @date 2023/7/31
     */
    @Override
    public void saveUserIntegral(UserIntegralLog log) {
        //计算新增积分
        int points = pointsCalculator.getPoints(log);
        IntegralEventDTO eventDTO = (IntegralEventDTO) redisUtil.hget(RedisKeys.getIntegralEventCache(), log.getEventCode());
        IntegralDetailDTO detailDTO = new IntegralDetailDTO();

        detailDTO.setUserId(log.getUserId());
        detailDTO.setPoints(points);
        detailDTO.setBalancePoints(points);
        detailDTO.setUsedPoints(0);
        detailDTO.setType(log.getType());
        detailDTO.setEventCode(log.getEventCode());
        detailDTO.setStatus(IntegralStatusEnum.USABLE.getCode());
        //设置积分过期时间
        if (eventDTO.isSettingExpire()) {
            if (eventDTO.getExpireTime() != null) {
                detailDTO.setExpireTime(eventDTO.getExpireTime());
            } else {
                detailDTO.setExpireTime(IntegralExpireTimeUtil.getExpireTime(new Date(), eventDTO));
            }
        }
        this.addIntegral(detailDTO);
    }

    @Override
    public void integralConsume(UserIntegralLog log) {
        eventRedisCacheService.validateEventEffective(log.getEventCode());

        if (!checkBalance(log)) {
            throw new SystemException("积分余额不足");
        }

        RLock lock = redissonClient.getLock(RedisKeys.getUserLockKey(log.getUserId()));
        lock.lock();
        try {
            if (!checkBalance(log)) {
                throw new SystemException("积分余额不足");
            }

            IntegralDetailDTO integralDetailDTO = IntegralDetailDTO.builder()
                    .userId(log.getUserId())
                    .points(0 - log.getPoints())
                    .type(IntegralType.DECREMENT.getCode())
                    .eventCode(log.getEventCode())
                    .balancePoints(0)
                    .usedPoints(0)
                    .status(IntegralStatusEnum.USED.getCode())
                    .orderId(log.getOrderId())
                    .createTime(new Date()).build();
            userIntegralChange(integralDetailDTO);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 校验余额是否充足
     * @param log 积分消费日志
     * @return boolean
     * @author HEJUN
     * @date 2023/7/31
     */
    private boolean checkBalance(UserIntegralLog log) {
        Map<String, Object> userIntegralTotal = getUserIntegralTotal(log.getUserId());
        Integer total = (Integer) userIntegralTotal.get("total");
        if (total == null || log.getPoints() > total) {
            return false;
        }
        return true;
    }

    /**
     * 积分扣减功能实现
     * 优先扣减快要过期的积分，若没有要过期的积分，则优先扣减先获取的积分
     * @param integralDetailDTO 积分扣减详情信息
     * @return void
     * @author HEJUN
     * @date 2023/7/31
     */
    private void decrementIntegralDetail(IntegralDetailDTO integralDetailDTO) {
        userIntegralDetailService.insert(integralDetailDTO);
        List<IntegralDetailDTO> expireIntegral = userIntegralDetailService.getExpireIntegralByUserId(integralDetailDTO.getUserId(), null, null);

        List<IntegralDetailDTO> list = new ArrayList<>();
        List<IntegralDecreaseLogEntity> decreaseLogEntities = new ArrayList<>();
        Integer points = decrease(integralDetailDTO.getPoints(), expireIntegral, list, decreaseLogEntities);

        if (points < 0) {
            List<IntegralDetailDTO> integralDetailDTOS = userIntegralDetailService.getNoExpireIntegralByUserId(integralDetailDTO.getUserId());
            decrease(points, integralDetailDTOS, list, decreaseLogEntities);
        }

        userIntegralDetailService.batchUpdate(list);
        decreaseLogEntities.forEach(e -> e.setOrderId(integralDetailDTO.getOrderId()));
        integralDecreaseLogService.batchInsert(decreaseLogEntities);
    }

    private Integer decrease(Integer points, List<IntegralDetailDTO> integralDetailDTOS, List<IntegralDetailDTO> list, List<IntegralDecreaseLogEntity> decreaseLogEntities) {
        for (IntegralDetailDTO detail : integralDetailDTOS) {
            if (detail.getBalancePoints() + points <= 0) {
                IntegralDecreaseLogEntity e = IntegralDecreaseLogEntity.builder()
                        .integralId(detail.getId())
                        .usePoints(detail.getBalancePoints())
                        .build();
                decreaseLogEntities.add(e);

                points += detail.getBalancePoints();
                detail.setBalancePoints(0);
                detail.setUsedPoints(detail.getPoints());
                detail.setStatus(IntegralStatusEnum.USED.getCode());
                list.add(detail);

            } else if (detail.getBalancePoints() + points > 0) {
                IntegralDecreaseLogEntity e = IntegralDecreaseLogEntity.builder()
                        .integralId(detail.getId())
                        .usePoints(0 - points)
                        .build();
                decreaseLogEntities.add(e);

                detail.setBalancePoints(detail.getBalancePoints() + points);
                detail.setUsedPoints(0 - points);
                list.add(detail);
                points = 0;
            }

            if (points == 0) {
                break;
            }
        }
        return points;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void giveBack(UserIntegralLog log) {
        //积分消费明细退回
        List<IntegralDecreaseLogDTO> logDTOS = integralDecreaseLogService.queryByOrderId(log.getOrderId());
        Map<Long, Integer> map = new HashMap<>();
        logDTOS.forEach(d -> map.put(d.getIntegralId(), d.getUsePoints()));
        logDTOS = null;
        List<IntegralDetailDTO> list = userIntegralDetailService.getByIds(Lists.newArrayList(map.keySet().iterator()));
        list.stream().forEach(d -> {
            d.setBalancePoints(d.getBalancePoints() + map.get(d.getId()));
            d.setUsedPoints(d.getUsedPoints() - map.get(d.getId()));
            if (d.getExpireTime() != null && d.getExpireTime().before(new Date())) {
                d.setStatus(IntegralStatusEnum.EXPIRED.getCode());
            } else {
                d.setStatus(IntegralStatusEnum.USABLE.getCode());
            }
        });
        //消费记录状态修改
        IntegralDetailDTO decreaseDTO = userIntegralDetailService.getByOrderId(log.getOrderId());
        decreaseDTO.setStatus(IntegralStatusEnum.GIVE_BACK.getCode());
        list.add(decreaseDTO);
        userIntegralDetailService.batchUpdate(list);

        //总积分退回
        UserIntegralEntity userIntegralEntity = new UserIntegralEntity();
        userIntegralEntity.setPoints(Math.abs(decreaseDTO.getPoints()));
        userIntegralEntity.setUserId(decreaseDTO.getUserId());
        this.update(userIntegralEntity);
        //缓存修改
        redisUtil.increment(RedisKeys.getUserIntegralTotalKey(decreaseDTO.getUserId()), Math.abs(decreaseDTO.getPoints()));
    }
}
