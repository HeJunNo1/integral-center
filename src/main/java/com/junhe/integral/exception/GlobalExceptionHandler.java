package com.junhe.integral.exception;

import com.junhe.integral.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * @author HEJUN
 * @since 1.0
 * @date 2023/8/9
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler  {

    @ExceptionHandler(value = SystemException.class)
    public Result exceptionHandler(SystemException exception){
        exception.printStackTrace();
        log.error(exception.getMessage(), exception);
        return new Result().error(500, exception.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception exception){
        log.error(exception.getMessage(), exception);
        return new Result().error(500, exception.getMessage());
    }
}
