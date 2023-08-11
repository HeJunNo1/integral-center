package com.junhe.integral.core.integral.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.junhe.integral.common.PojoBaseInfo;
import lombok.Data;
import java.util.Date;

/**
 * 积分详情
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/25
 */
@Data
@TableName("integral_detail_log")
public class IntegralDetailEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ID_WORKER)
    private Long id;

    /**
     * 事件编号
     */
    private String eventCode;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 积分值
     */
    private Integer points;

    /**
     * 积分状态 1：有效 0：失效
     */
    private Integer status;

    /**
     * 剩余积分值
     */
    private Integer balancePoints;

    /**
     * 已使用积分值
     */
    private Integer usedPoints;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 积分类型 （1:增加 2：消费）
     */
    private Integer type;

    /**
     * 积分消费时对应的订单ID
     */
    private String orderId;
}
