package com.junhe.integral.core.integral.dto;

import lombok.Data;

import java.util.Date;

/**
 * 积分消费日志展示类
 * @author HEJUN
 * @since 1.0
 * @date 2023/8/10
 */
@Data
public class IntegralDecreaseLogDTO {

    private Long id;

    private String orderId;

    private Long integralId;

    private int usePoints;

    private Date createTime;
}
