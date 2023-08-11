package com.junhe.integral.core.integral.service;

import com.junhe.integral.api.params.UserIntegralDetailQueryParam;
import com.junhe.integral.api.params.UserIntegralTotalQueryParam;
import com.junhe.integral.common.PageData;
import com.junhe.integral.core.inteEvent.dto.IntegralEventDTO;
import com.junhe.integral.core.integral.dto.IntegralDetailDTO;

import java.util.ArrayList;
import java.util.List;

public interface UserIntegralDetailService {

    /**
     * 用户周期内获取次数
     * @return int
     * @author HEJUN
     * @date 2023/7/28
     */
    int getUserGetTimesInCycle(String userId, IntegralEventDTO event);

    /**
     * 查询用户对应积分事件积分获取次数
     * @param userId
     * @param integralEvent
     * @return int
     * @author HEJUN
     * @date 2023/7/28
     */
    int getUserGetTimes(String userId, IntegralEventDTO integralEvent);

    /**
     * 插入积分变更日志
     * @param integralDetailDTO 积分详情记录
     * @return void
     * @author HEJUN
     * @date 2023/8/1
     */
    void insert(IntegralDetailDTO integralDetailDTO);

    /**
     * 查询expire timeUnit时间范围内要过期的积分
     * @param userId 用户ID
     * @param expire 过期时间
     * @param timeUnit 单位
     * @return java.util.List<com.junhe.integral.core.integral.dto.IntegralDetailDTO>
     * @author HEJUN
     * @date 2023/8/1
     */
    List<IntegralDetailDTO> getExpireIntegralByUserId(String userId, Integer expire, Integer timeUnit);

    /**
     * 查询不过期的积分
     * @param userId 用户ID
     * @return java.util.List<com.junhe.integral.core.integral.dto.IntegralDetailDTO>
     * @author HEJUN
     * @date 2023/8/1
     */
    List<IntegralDetailDTO> getNoExpireIntegralByUserId(String userId);

    /**
     * 批量更新
     * @param list 待更新列表
     * @author HEJUN
     * @date 2023/8/1
     */
    void batchUpdate(List<IntegralDetailDTO> list);

    /**
     * 通过orderId查询消费日志
     * @param orderId
     * @return com.junhe.integral.core.integral.dto.IntegralDetailDTO
     * @author HEJUN
     * @date 2023/8/10
     */
    IntegralDetailDTO getByOrderId(String orderId);

    /**
     * 通过积分详情ID查询详情信息
     * @return java.util.List<com.junhe.integral.core.integral.dto.IntegralDetailDTO>
     * @author HEJUN
     * @date 2023/8/10
     * @return 详情信息列表
     */
    List<IntegralDetailDTO> getByIds(ArrayList<Long> newArrayList);

    /**
     * 分页查询用户详情
     * @param param
     * @return com.junhe.integral.common.PageData
     * @author HEJUN
     * @date 2023/8/10
     */
    PageData queryByPage(UserIntegralDetailQueryParam param);
}
