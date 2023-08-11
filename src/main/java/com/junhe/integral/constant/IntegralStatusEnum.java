package com.junhe.integral.constant;

/**
 * 积分状态
 * @author HEJUN
 * @since 1.0
 * @date 2023/8/10
 */
public enum IntegralStatusEnum {

    USABLE(1, "可使用"),
    EXPIRED(2, "已过期"),
    USED(3, "已使用"),
    GIVE_BACK(4, "已退回"),;

    private int code;

    private String description;

    IntegralStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return this.code;
    }
}
