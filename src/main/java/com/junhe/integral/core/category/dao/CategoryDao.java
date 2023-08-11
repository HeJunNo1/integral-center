package com.junhe.integral.core.category.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.junhe.integral.core.category.entity.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 积分分类持久化接口
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/21
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
}
