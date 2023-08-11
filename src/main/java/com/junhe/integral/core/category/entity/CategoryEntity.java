package com.junhe.integral.core.category.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.junhe.integral.common.PojoBaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 积分事件分类
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("integral_category")
public class CategoryEntity extends PojoBaseInfo {

    @TableId(type = IdType.ID_WORKER)
    private Long id;

    private String name;

    private String code;

    private Integer type;
}
