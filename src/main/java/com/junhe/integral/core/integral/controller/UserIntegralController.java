package com.junhe.integral.core.integral.controller;

import com.junhe.integral.api.params.UserIntegralLog;
import com.junhe.integral.api.params.UserIntegralDetailQueryParam;
import com.junhe.integral.api.params.UserIntegralTotalQueryParam;
import com.junhe.integral.common.PageData;
import com.junhe.integral.common.Result;
import com.junhe.integral.core.integral.service.UserIntegralDetailService;
import com.junhe.integral.core.integral.service.UserIntegralService;
import com.junhe.integral.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户积分控制器
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/25
 */
@RestController
@RequestMapping("/userIntegral")
@Slf4j
public class UserIntegralController {

    @Resource
    private UserIntegralService userIntegralService;
    @Resource
    private UserIntegralDetailService userIntegralDetailService;

    @GetMapping("/usersPage")
    public Result usersPage(UserIntegralTotalQueryParam param){
        PageData data = userIntegralService.usersPage(param);
        return new Result().ok(data);
    }

    @GetMapping("/total/{userId}")
    public Result getUserIntegralTotal(@PathVariable("userId") String userId) {
        Map<String, Object> map = userIntegralService.getUserIntegralTotal(userId);
        return new Result().ok(map);
    }

    @PostMapping("/save")
    public Result saveIntegral(@RequestBody UserIntegralLog integralLog) {
        try {
            userIntegralService.saveUserIntegral(integralLog);
        } catch (SystemException e) {
            return new Result().error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result().error("系统错误");
        }

        return new Result();
    }

    @PostMapping("/integralConsume")
    public Result integralConsume(@RequestBody UserIntegralLog log) {
        userIntegralService.integralConsume(log);
        return new Result();
    }

    @PostMapping("/giveBack")
    public Result giveBack(@RequestBody UserIntegralLog log) {
        userIntegralService.giveBack(log);
        return new Result();
    }

    @GetMapping("/detail/page")
    public Result queryUserIntegralLog(UserIntegralDetailQueryParam param){
        PageData data = userIntegralDetailService.queryByPage(param);
        return new Result().ok(data);
    }
}
