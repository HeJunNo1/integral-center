package com.junhe.integral.core.integral.service;

import com.junhe.integral.core.integral.dto.IntegralDecreaseLogDTO;
import com.junhe.integral.core.integral.entity.IntegralDecreaseLogEntity;

import java.util.List;

/**
 * 积分消费日志记录
 * @author HEJUN
 * @since 1.0
 * @date 2023/8/9
 */
public interface IntegralDecreaseLogService {

    void batchInsert(List<IntegralDecreaseLogEntity> list);

    List<IntegralDecreaseLogDTO> queryByOrderId(String orderId);
}