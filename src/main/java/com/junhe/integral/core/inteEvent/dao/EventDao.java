package com.junhe.integral.core.inteEvent.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.junhe.integral.core.inteEvent.entity.IntegralEventEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 积分事件持久化接口
 * @author HEJUN
 * @since 1.0
 * @date 2023/7/7
 */
@Mapper
public interface EventDao extends BaseMapper<IntegralEventEntity> {

    /**
     * 查询同分类事件最大编码
     * @param category 事件分类
     * @return java.lang.String
     * @author HEJUN
     * @date 2023/7/7
     */
    @Select("select code from integral_event where category = #{category} order by code DESC limit 1")
    String getMaxCodeByCategory(Long category);
}
