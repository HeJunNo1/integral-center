package com.junhe.integral.core.inteEvent.service;

import com.junhe.integral.core.inteEvent.dto.IntegralEventDTO;

/**
 * 积分事件缓存服务类
 * @author HEJUN
 * @since 1.0
 * @date 2023/7/26
 */
public interface EventRedisCacheService {

    /**
     * 刷新积分事件缓存信息
     * @author HEJUN
     * @date 2023/7/26
     */
    void refreshEventCache();

    /**
     * 通过事件编码获取积分事件
     * @param code
     * @return
     */
    IntegralEventDTO getEventByCode(String code);

    /**
     * 校验事件是否有效
     * @param code
     */
    void validateEventEffective(String code);

    /**
     * 校验事件是否有效
     * @param integralEvent
     */
    void validateEventEffective(IntegralEventDTO integralEvent);
}
