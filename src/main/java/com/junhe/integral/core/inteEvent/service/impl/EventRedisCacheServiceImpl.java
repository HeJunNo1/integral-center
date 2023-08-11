package com.junhe.integral.core.inteEvent.service.impl;

import com.junhe.integral.common.RedisUtil;
import com.junhe.integral.constant.RedisKeys;
import com.junhe.integral.core.inteEvent.EventStatusEnum;
import com.junhe.integral.core.inteEvent.dto.IntegralEventDTO;
import com.junhe.integral.core.inteEvent.service.EventRedisCacheService;
import com.junhe.integral.core.inteEvent.service.EventService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分事件缓存服务实现类
 * @author HEJUN
 * @since 1.0
 * @date 2023/7/26
 */
@Service
public class EventRedisCacheServiceImpl implements EventRedisCacheService {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private EventService eventService;

    @Override
    public void refreshEventCache() {
        List<IntegralEventDTO> eventList = eventService.allEvent();
        Map<String, Object> eventsMap = new HashMap(0);

        if (CollectionUtils.isNotEmpty(eventList)) {
            eventList.forEach(e -> eventsMap.put(e.getCode(), e));
        }
        redisUtil.del(RedisKeys.getIntegralEventCache());
        redisUtil.hSetAll(RedisKeys.getIntegralEventCache(), eventsMap);
    }

    @Override
    public IntegralEventDTO getEventByCode(String code) {
        IntegralEventDTO eventDTO = (IntegralEventDTO) redisUtil.hget(RedisKeys.getIntegralEventCache(), code);
        if (eventDTO == null) {
            eventDTO = eventService.getByCode(code);
            if (eventDTO != null) {
                redisUtil.hset(RedisKeys.getIntegralEventCache(), code, eventDTO);
            }
        }
        return eventDTO;
    }

    @Override
    public void validateEventEffective(String code){
        IntegralEventDTO event = this.getEventByCode(code);
        this.validateEventEffective(event);
    }

    /**
     * 验证事件的有效性
     * 1、验证事件是否存在
     * 2、验证事件是否开启
     * @param integralEvent 待验证事件
     * @author HEJUN
     * @date 2023/7/28
     */
    @Override
    public void validateEventEffective(IntegralEventDTO integralEvent) {
        if (integralEvent == null) {
            throw new IllegalArgumentException("积分事件不存在");
        }

        Integer status = integralEvent.getStatus();
        if (EventStatusEnum.ENABLE.getCode() != status) {
            throw new IllegalArgumentException("积分事件" + integralEvent.getCode()+"已被禁用");
        }
    }
}
