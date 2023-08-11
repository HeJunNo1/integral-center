package com.junhe.integral;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/21
 */
@SpringBootApplication
@MapperScan("com.junhe.integral.**.dao")
public class IntegralApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntegralApplication.class, args);
    }
}
