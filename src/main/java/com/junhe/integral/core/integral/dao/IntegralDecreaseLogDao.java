package com.junhe.integral.core.integral.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.junhe.integral.core.integral.entity.IntegralDecreaseLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 积分消费日志记录持久化接口
 * @author HEJUN
 * @since 1.0
 * @date 2023/8/9
 */
@Mapper
public interface IntegralDecreaseLogDao extends BaseMapper<IntegralDecreaseLogEntity> {
}
