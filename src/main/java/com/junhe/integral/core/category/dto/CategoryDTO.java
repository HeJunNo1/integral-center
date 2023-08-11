package com.junhe.integral.core.category.dto;

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
public class CategoryDTO extends PojoBaseInfo {

    /**
     * ID 主键
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类编号
     */
    private String code;

    /**
     * 分类类型
     */
    private Integer type;
}
