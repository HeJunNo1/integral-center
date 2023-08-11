package com.junhe.integral.core.inteEvent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.junhe.integral.common.PojoBaseInfo;
import lombok.Data;

import java.util.Date;

/**
 * 积分事件实体类
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/21
 */
@Data
@TableName("integral_event")
public class IntegralEventEntity extends PojoBaseInfo {

    @TableId(type = IdType.ID_WORKER)
    private Long id;

    /**
     * 事件分类
     */
    private Long category;

    /**
     * 事件编码 6为数字  3位分类编码 + 3位事件编号 （自动生成）
     */
    private String code;

    /**
     * 事件名称
     */
    private String name;

    /**
     * 事件描述
     */
    private String description;

    /**
     * 事件状态 0：停用 1：启用
     */
    private Integer status;

    /**
     * 是否周期性事件
     */
    private Boolean isCycleEvent;

    /**
     * 事件周期
     */
    private Integer cycle;

    /**
     * 事件周期单位
     */
    private Integer cycleUnit;

    /**
     * 非周期事件 或 周期事件周期内 最大获取次数
     */
    private Integer maxTime;

    /**
     * 周期性事件积分增长策略
     */
    private Integer cycleEventPointIncrType;

    /**
     * 积分增长步长
     */
    private Double cycleEventPointIncrValue;

    /**
     * 积分值
     */
    private Integer points;

    /**
     * 事件最高积分值
     */
    private Integer pointsMax;

    /**
     * 是否设置过期
     */
    private boolean isSettingExpire;

    /**
     * 有效期
     */
    private Integer effectiveCycle;

    /**
     * 有效期单位
     */
    private Integer effectiveCycleUnit;

    /**
     * 过期日期
     */
    private Date expireTime;


}
