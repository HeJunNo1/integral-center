package com.junhe.integral.core.inteEvent;

import com.junhe.integral.api.params.UserIntegralLog;
import com.junhe.integral.core.inteEvent.dto.IntegralEventDTO;

/**
 * 事件状态枚举类
 * @author HEJUN
 * @since 1.0
 * @date 2023/7/26
 */
public enum EventStatusEnum {

    DISABLE(0, "禁用"),
    ENABLE(1, "启用");

    private int code;

    private String name;

    EventStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }
}
