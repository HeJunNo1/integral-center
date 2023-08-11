package com.junhe.integral.constant;

public enum EventPointIncrTypeEnum {

    REGULAR(1, "固定值"),
    POINT(2, "积分值增长"),
    PROPORTION(3, "百分比增长");

    private Integer code;

    private String description;

    EventPointIncrTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return this.code;
    }
}
