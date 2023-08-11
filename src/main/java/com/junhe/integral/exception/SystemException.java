package com.junhe.integral.exception;

import lombok.Data;

/**
 * 系统异常
 * @author HEJUN
 * @since 1.0
 * @date 2023/7/28
 */
@Data
public class SystemException extends RuntimeException {

    private String message;

    private Throwable throwable;

    public SystemException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public SystemException(String message) {
        super(message);
        this.message = message;
        this.throwable = throwable;
    }
}
