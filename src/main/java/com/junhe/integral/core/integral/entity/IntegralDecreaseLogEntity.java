package com.junhe.integral.core.integral.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@TableName("integral_decrease_log")
@Builder
public class IntegralDecreaseLogEntity {

    @TableId(type = IdType.ID_WORKER)
    private Long id;

    private String orderId;

    private Long integralId;

    private int usePoints;

    private Date createTime;
}
