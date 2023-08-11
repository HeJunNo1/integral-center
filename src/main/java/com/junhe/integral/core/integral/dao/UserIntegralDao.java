package com.junhe.integral.core.integral.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.junhe.integral.core.integral.entity.UserIntegralEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户积分持久化接口
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/26
 */
@Mapper
public interface UserIntegralDao extends BaseMapper<UserIntegralEntity> {
}
