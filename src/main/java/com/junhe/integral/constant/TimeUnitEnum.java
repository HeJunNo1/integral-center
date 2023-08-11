package com.junhe.integral.constant;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * 事件单位枚举类
 * @author HEJUN
 * @since 1.0
 * @date 2023/7/27
 */
public enum TimeUnitEnum {

    DAY(1, "DAY"),
    WEEK(2, "WEEK"),
    MONTH(3, "MONTH"),
    YEAR(4, "YEAR");

    private Integer code;

    private String unit;

    TimeUnitEnum(Integer code, String unit) {
        this.code = code;
        this.unit = unit;
    }

    public Integer getCode() {
        return code;
    }
}
