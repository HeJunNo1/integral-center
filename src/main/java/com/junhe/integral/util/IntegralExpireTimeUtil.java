package com.junhe.integral.util;

import com.junhe.integral.constant.TimeUnitEnum;
import com.junhe.integral.core.inteEvent.dto.IntegralEventDTO;

import java.util.Calendar;
import java.util.Date;

/**
 * 积分过期时间计算
 * @author HEJUN
 * @since 1.0
 * @date 2023/7/31
 */
public class IntegralExpireTimeUtil {

    public static Date getExpireTime(Date date, IntegralEventDTO event) {
        return getExpireTime(date, event.getEffectiveCycle(), event.getEffectiveCycleUnit());
    }

    public static Date getExpireTime(Date date, int expire, Integer timeUnit) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        if (TimeUnitEnum.DAY.getCode().equals(timeUnit)) {
            cal.add(Calendar.DATE, expire);
        } else if (TimeUnitEnum.WEEK.getCode().equals(timeUnit)) {
            cal.add(Calendar.DATE, expire * 7);
        } else if (TimeUnitEnum.MONTH.getCode().equals(timeUnit)) {
            cal.add(Calendar.MONTH, expire);
        } else if (TimeUnitEnum.YEAR.getCode().equals(timeUnit)) {
            cal.add(Calendar.YEAR, expire);
        }
        return cal.getTime();
    }
}
