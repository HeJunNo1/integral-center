package com.junhe.integral.constant;

/**
 * 积分类型
 * @author HEJUN
 * @since 1.0
 * @date 2023/7/31
 */
public enum IntegralType {

    INCREMENT(1, "增加"),
    DECREMENT(2, "减少");


    private Integer code;

    private String description;

    IntegralType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }
}
