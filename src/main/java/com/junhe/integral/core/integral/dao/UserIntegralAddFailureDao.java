package com.junhe.integral.core.integral.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户积分变更持久化数据失败记录接口
 * @author HEJUN
 * @since 1.0
 * @date 2023/7/3
 */
@Mapper
public interface UserIntegralAddFailureDao {

    @Insert("insert into user_integral_add_failure_log(record) values(#{record})")
    int insert(@Param("record") String record);
}
