package com.junhe.integral.core.integral.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.junhe.integral.api.params.UserIntegralDetailQueryParam;
import com.junhe.integral.common.PageData;
import com.junhe.integral.core.inteEvent.dto.IntegralEventDTO;
import com.junhe.integral.core.integral.dao.UserIntegralDetailDao;
import com.junhe.integral.core.integral.dto.IntegralDetailDTO;
import com.junhe.integral.core.integral.entity.IntegralDetailEntity;
import com.junhe.integral.core.integral.service.UserIntegralDetailService;
import com.junhe.integral.util.EventCycle;
import com.junhe.integral.util.EventCycleCalculator;
import com.junhe.integral.util.IntegralExpireTimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户积分详情服务类
 * @author HEJUN
 * @since 1.0
 * @date 2023/7/28
 */
@Service
public class UserIntegralDetailServiceImpl extends ServiceImpl<UserIntegralDetailDao, IntegralDetailEntity> implements UserIntegralDetailService {

    @Resource
    private UserIntegralDetailDao userIntegralDetailDao;

    @Override
    public int getUserGetTimesInCycle(String userId, IntegralEventDTO event) {
        EventCycle cycle = EventCycleCalculator.getEventCycle(event);

        QueryWrapper<IntegralDetailEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IntegralDetailEntity::getUserId, userId)
                .eq(IntegralDetailEntity::getEventCode, event.getCode())
                .ge(IntegralDetailEntity::getCreateTime, cycle.getStartTime())
                .le(IntegralDetailEntity::getCreateTime, cycle.getEndTime());
        return userIntegralDetailDao.selectCount(wrapper);
    }

    @Override
    public int getUserGetTimes(String userId, IntegralEventDTO event) {
        QueryWrapper<IntegralDetailEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IntegralDetailEntity::getUserId, userId)
                .eq(IntegralDetailEntity::getEventCode, event.getCode());
        return userIntegralDetailDao.selectCount(wrapper);
    }

    @Override
    public void insert(IntegralDetailDTO integralDetailDTO) {
        IntegralDetailEntity entity = new IntegralDetailEntity();
        BeanUtils.copyProperties(integralDetailDTO, entity);
        userIntegralDetailDao.insert(entity);
    }

    @Override
    public List<IntegralDetailDTO> getExpireIntegralByUserId(String userId, Integer expire, Integer timeUnit) {
        Date expireTime = null;
        if (expire != null && expire > 0) {
            expireTime = IntegralExpireTimeUtil.getExpireTime(new Date(), expire, timeUnit);
        }

        QueryWrapper<IntegralDetailEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IntegralDetailEntity::getUserId, userId)
                .eq(IntegralDetailEntity::getStatus, 1)
                .ge(IntegralDetailEntity::getBalancePoints, 1)
                .ne(expireTime != null, IntegralDetailEntity::getExpireTime, expireTime)
                .isNotNull(expireTime == null, IntegralDetailEntity::getExpireTime)
                .orderByAsc(IntegralDetailEntity::getCreateTime);
        List<IntegralDetailEntity> integralDetailEntities = userIntegralDetailDao.selectList(wrapper);
        List<IntegralDetailDTO> detailDTOList = new ArrayList<>(integralDetailEntities.size());
        integralDetailEntities.forEach(e -> {
            IntegralDetailDTO dto = new IntegralDetailDTO();
            BeanUtils.copyProperties(e, dto);
            detailDTOList.add(dto);
        });
        return detailDTOList;
    }

    @Override
    public List<IntegralDetailDTO> getNoExpireIntegralByUserId(String userId) {
        QueryWrapper<IntegralDetailEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IntegralDetailEntity::getUserId, userId)
                .eq(IntegralDetailEntity::getStatus, 1)
                .ge(IntegralDetailEntity::getBalancePoints, 1)
                .isNull(IntegralDetailEntity::getExpireTime)
                .orderByAsc(IntegralDetailEntity::getCreateTime);
        List<IntegralDetailEntity> integralDetailEntities = userIntegralDetailDao.selectList(wrapper);
        List<IntegralDetailDTO> detailDTOList = new ArrayList<>(integralDetailEntities.size());
        integralDetailEntities.forEach(e -> {
            IntegralDetailDTO dto = new IntegralDetailDTO();
            BeanUtils.copyProperties(e, dto);
            detailDTOList.add(dto);
        });
        return detailDTOList;
    }

    @Override
    public void batchUpdate(List<IntegralDetailDTO> list) {
        List<IntegralDetailEntity> entities = new ArrayList<>(list.size());
        list.forEach(d -> {
            IntegralDetailEntity e = new IntegralDetailEntity();
            BeanUtils.copyProperties(d, e);
            entities.add(e);
        });
        super.updateBatchById(entities, 1000);
    }

    @Override
    public IntegralDetailDTO getByOrderId(String orderId) {
        QueryWrapper<IntegralDetailEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IntegralDetailEntity::getOrderId, orderId);
        IntegralDetailEntity entity = userIntegralDetailDao.selectOne(wrapper);

        IntegralDetailDTO dto = new IntegralDetailDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public List<IntegralDetailDTO> getByIds(ArrayList<Long> ids) {
        List<IntegralDetailEntity> entities = baseMapper.selectBatchIds(ids);

        List<IntegralDetailDTO> list = new ArrayList<>();
        entities.forEach(e -> {
            IntegralDetailDTO d = new IntegralDetailDTO();
            BeanUtils.copyProperties(e, d);
            list.add(d);
        });
        return list;
    }

    @Override
    public PageData queryByPage(UserIntegralDetailQueryParam param) {
        IPage page = new Page(param.getPage(), param.getLimit());
        QueryWrapper<IntegralDetailEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(IntegralDetailEntity::getUserId, param.getUserId())
                .eq(!ObjectUtils.isEmpty(param.getEventCode()), IntegralDetailEntity::getEventCode, param.getEventCode())
                .eq(!ObjectUtils.isEmpty(param.getType()), IntegralDetailEntity::getType, param.getType())
                .ge(!ObjectUtils.isEmpty(param.getStartTime()), IntegralDetailEntity::getCreateTime, param.getStartTimeFormat())
                .le(!ObjectUtils.isEmpty(param.getEndTime()), IntegralDetailEntity::getCreateTime, param.getEndTimeFormat())
                .orderByDesc(IntegralDetailEntity::getCreateTime);
        IPage data = baseMapper.selectPage(page, queryWrapper);

        List<IntegralDetailDTO> dtoList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(data.getRecords())) {
            data.getRecords().forEach(record -> {
                IntegralDetailDTO dto = new IntegralDetailDTO();
                BeanUtils.copyProperties(record, dto);
                dtoList.add(dto);
            });
        }
        return new PageData(dtoList, data.getTotal());
    }
}
