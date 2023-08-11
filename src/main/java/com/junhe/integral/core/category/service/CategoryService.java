package com.junhe.integral.core.category.service;

import com.junhe.integral.core.category.dto.CategoryDTO;

/**
 * 积分分类服务接口
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/21
 */
public interface CategoryService {

    /**
     * 新增分类信息
     * @param dto 分类信息
     */
    void add(CategoryDTO dto);

    /**
     * 修改分类信息
     * @param dto 分类信息
     */
    void update(CategoryDTO dto);

    /**
     * 删除分类
     * @param id
     */
    void delete(Long id);

    /**
     * 查询分类详情
     * @param id
     * @return
     */
    CategoryDTO getById(Long id);
}
