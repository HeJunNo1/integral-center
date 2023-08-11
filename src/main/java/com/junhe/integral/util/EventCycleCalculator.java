package com.junhe.integral.util;

import com.junhe.integral.constant.TimeUnitEnum;
import com.junhe.integral.core.inteEvent.dto.IntegralEventDTO;

import java.util.Calendar;

/**
 * 事件周期计算器
 * @author HEJUN
 * @since 1.0
 * @date 2023/7/28
 */
public class EventCycleCalculator {

    public static EventCycle getEventCycle(IntegralEventDTO eventDto) {
        Integer cycle = eventDto.getCycle();
        Integer cycleUnit = eventDto.getCycleUnit();
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();

        if (TimeUnitEnum.DAY.getCode().equals(cycleUnit)) {
            endTime.set(Calendar.HOUR_OF_DAY, 23);
            endTime.set(Calendar.MINUTE, 59);
            endTime.set(Calendar.SECOND, 59);

            startTime.add(Calendar.DATE, 1 - cycle);
            startTime.set(Calendar.HOUR_OF_DAY, 0);
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.SECOND, 0);
        } else if (TimeUnitEnum.WEEK.getCode().equals(cycleUnit)) {
            int firstDayOfWeek = endTime.getFirstDayOfWeek();
            endTime.set(Calendar.DATE, firstDayOfWeek);
            endTime.add(Calendar.DATE, 6);
            endTime.set(Calendar.HOUR_OF_DAY, 23);
            endTime.set(Calendar.MINUTE, 59);
            endTime.set(Calendar.SECOND, 59);

            startTime.set(Calendar.DATE, firstDayOfWeek);
            startTime.set(Calendar.HOUR_OF_DAY, 0);
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.SECOND, 0);
            startTime.add(Calendar.DATE, 0 - 7 * (cycle - 1));
        } else if (TimeUnitEnum.MONTH.getCode().equals(cycleUnit)) {
            startTime.set(Calendar.DATE, 1);
            startTime.set(Calendar.HOUR_OF_DAY, 0);
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.SECOND, 0);
            startTime.add(Calendar.MONTH, 1 - cycle);

            endTime.set(Calendar.DATE, endTime.getActualMaximum(Calendar.DATE));
            endTime.set(Calendar.HOUR_OF_DAY, 23);
            endTime.set(Calendar.MINUTE, 59);
            endTime.set(Calendar.SECOND, 59);
        } else if (TimeUnitEnum.YEAR.getCode().equals(cycleUnit)) {
            startTime.set(Calendar.MONTH, 0);
            startTime.set(Calendar.DATE, 1);
            startTime.set(Calendar.HOUR_OF_DAY, 0);
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.SECOND, 0);
            startTime.add(Calendar.YEAR, 1 - cycle);

            startTime.set(Calendar.MONTH, 11);
            endTime.set(Calendar.DATE, 31);
            endTime.set(Calendar.HOUR_OF_DAY, 23);
            endTime.set(Calendar.MINUTE, 59);
            endTime.set(Calendar.SECOND, 59);
        } else {
            throw new IllegalArgumentException("非法的周期单位");
        }

        return new EventCycle(startTime.getTime(), endTime.getTime());
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.DATE, 1);
        //calendar.add(Calendar.MONTH, -1);
        System.out.println(calendar.getActualMaximum(Calendar.DATE));
    }
}
