package com.junhe.integral.core.integral.dto;

import com.junhe.integral.common.PojoBaseInfo;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 积分详情
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntegralDetailDTO extends PojoBaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
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
     * 积分状态 1：可使用 2：已过期 3:已使用 4:已退回
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
     * 积分类型（1: 增加 2：消费）
     */
    private Integer type;

    /**
     * 积分消费时的订单号
     */
    private String orderId;

}
