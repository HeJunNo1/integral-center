package com.junhe.integral.api.params;

import lombok.Data;

@Data
public class UserIntegralLog {

    /**
     * 用户标识
     */
    private String userId;
    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 积分值
     */
    private Integer points;

    /**
     * 备注
     */
    private String remark;

    /**
     * 积分类型 1、新增 2、消费
     */
    private Integer type;

    /**
     * 消费时的订单ID
     */
    private String orderId;
}
