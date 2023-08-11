package com.junhe.integral.core.integral.service;

import com.junhe.integral.api.params.UserIntegralLog;
import com.junhe.integral.api.params.UserIntegralTotalQueryParam;
import com.junhe.integral.common.PageData;
import com.junhe.integral.common.Result;
import com.junhe.integral.core.integral.dto.IntegralDetailDTO;

import java.util.Map;

public interface UserIntegralService {
    void addIntegral(IntegralDetailDTO integralDetailDTO);

    void asyncPersistenceIntegral(IntegralDetailDTO integralDetailDTO);

    /**
     * 查询用户总积分
     * @param userId 用户ID
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author HEJUN
     * @date 2023/6/26
     */
    Map<String, Object> getUserIntegralTotal(String userId);;

    /**
     * 积分新增
     * @param log 积分获取日志
     * @author HEJUN
     * @date 2023/7/31
     */
    void saveUserIntegral(UserIntegralLog log);

    /**
     * 积分消费
     * @param log 积分消费日志
     * @author HEJUN
     * @date 2023/7/31
     */
    void integralConsume(UserIntegralLog log);

    /**
     * 积分回退
     * @param log 积分回退信息
     * @author HEJUN
     * @date 2023/8/10
     */
    void giveBack(UserIntegralLog log);

    /**
     * 分页查询用户积分
     * @param param
     * @return com.junhe.integral.common.PageData
     * @author HEJUN
     * @date 2023/8/10
     */
    PageData usersPage(UserIntegralTotalQueryParam param);
}
