package com.junhe.integral.util;

import java.util.Date;

/**
 * 事件周期
 * @author HEJUN
 * @since 1.0
 * @date 2023/7/28
 */
public class EventCycle {

    private Date startTime;

    private Date endTime;

    public EventCycle(Date startTime, Date endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime(){
        return DateUtil.format(this.startTime, "yyyy-MM-dd 00:00:00");
    }

    public String getEndTime() {
        return DateUtil.format(this.endTime, "yyyy-MM-dd 23:59:59");
    }
}
