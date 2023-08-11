package com.junhe.integral.core.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.junhe.integral.core.category.dao.CategoryDao;
import com.junhe.integral.core.category.dto.CategoryDTO;
import com.junhe.integral.core.category.entity.CategoryEntity;
import com.junhe.integral.core.category.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 积分分类服务类
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/21
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryDao categoryDao;

    @Override
    public void add(CategoryDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setCode(createCode());
        entity.setCreateTime(new Date());
        entity.setIsDelete(0);
        categoryDao.insert(entity);
    }

    /**
     * 创建分类code,递增并补全为3位数值字符串
     * @return String 分类code
     */
    private String createCode(){
        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().select(CategoryEntity::getCode).orderByDesc(CategoryEntity::getId).last("limit 1");
        List<CategoryEntity> list = categoryDao.selectList(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return "001";
        }

        int code = Integer.parseInt(list.get(0).getCode());
        return String.format("%03d", code + 1);
    }

    @Override
    public void update(CategoryDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setUpdateTime(new Date());
        categoryDao.updateById(entity);
    }

    @Override
    public void delete(Long id) {
        UpdateWrapper<CategoryEntity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(CategoryEntity::getIsDelete, 1)
                .set(CategoryEntity::getUpdateTime, new Date())
                .eq(CategoryEntity::getId, id);
        categoryDao.update(null, wrapper);
    }

    @Override
    public CategoryDTO getById(Long id) {
        CategoryEntity categoryEntity = categoryDao.selectById(id);
        CategoryDTO dto = new CategoryDTO();
        BeanUtils.copyProperties(categoryEntity, dto);
        return dto;
    }
}
