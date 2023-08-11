package com.junhe.integral.util;

import com.junhe.integral.api.params.UserIntegralLog;
import com.junhe.integral.constant.EventPointIncrTypeEnum;
import com.junhe.integral.core.inteEvent.EventStatusEnum;
import com.junhe.integral.core.inteEvent.dto.IntegralEventDTO;
import com.junhe.integral.core.inteEvent.service.EventRedisCacheService;
import com.junhe.integral.core.integral.service.UserIntegralDetailService;
import com.junhe.integral.exception.SystemException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 积分计算器 通过
 * @author HEJUN
 * @since 1.0
 * @date 2023/7/28
 */
@Component
public class PointsCalculator {

    @Resource
    private EventRedisCacheService eventRedisCacheService;
    @Resource
    private UserIntegralDetailService userIntegralDetailService;

    /**
     * 根据积分事件计算积分
     * @param log 用户传递的积分获取日志
     * @return int
     * @author HEJUN
     * @date 2023/7/31
     */
    public int getPoints(UserIntegralLog log) {
        IntegralEventDTO integralEvent = eventRedisCacheService.getEventByCode(log.getEventCode());
        eventRedisCacheService.validateEventEffective(integralEvent);
        if (!integralEvent.getIsCycleEvent()) {
            return getEventPoints(log.getUserId(), integralEvent);
        } else {
            return getCycleEventPoints(log.getUserId(), integralEvent);
        }
    }

    /**
     * 非周期性积分事件积分计算
     * @param userId 用户ID
     * @param integralEvent 积分事件
     * @return int
     * @author HEJUN
     * @date 2023/7/31
     */
    private int getEventPoints(String userId, IntegralEventDTO integralEvent) {
        Integer maxTime = integralEvent.getMaxTime();
        if (maxTime != null) {
            int getTimes = userIntegralDetailService.getUserGetTimes(userId, integralEvent);
            if (maxTime != null && getTimes > maxTime) {
                throw new SystemException("超过最大调用次数");
            }
        }
        return integralEvent.getPoints();
    }

    /**
     * 周期性积分事件积分计算
     * @param userId 用户ID
     * @param integralEvent 积分事件
     * @return int
     * @author HEJUN
     * @date 2023/7/31
     */
    private int getCycleEventPoints(String userId, IntegralEventDTO integralEvent) {
        Integer maxTime = integralEvent.getMaxTime();
        int getTimes = userIntegralDetailService.getUserGetTimesInCycle(userId, integralEvent);
        if (maxTime != null && getTimes >= maxTime) {
            throw new SystemException("超过最大调用次数");
        }
        int points = 0;
        Integer cycleEventPointIncrType = integralEvent.getCycleEventPointIncrType();
        if (EventPointIncrTypeEnum.REGULAR.getCode().equals(cycleEventPointIncrType)) {
            return integralEvent.getPoints();
        } else if (EventPointIncrTypeEnum.POINT.getCode().equals(cycleEventPointIncrType)) {
            points = integralEvent.getPoints() + Double.valueOf(Math.floor(integralEvent.getCycleEventPointIncrValue() * getTimes)).intValue();
        } else if (EventPointIncrTypeEnum.PROPORTION.getCode().equals(cycleEventPointIncrType)){
            points = integralEvent.getPoints() + Double.valueOf(Math.floor(integralEvent.getCycleEventPointIncrValue() * getTimes * points)).intValue();
        } else {
            throw new IllegalArgumentException("周期事件积分增长策略错误");
        }
        Integer pointsMax = integralEvent.getPointsMax();
        return (pointsMax != null && points > pointsMax) ? pointsMax : points;
    }
}
