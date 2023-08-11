package com.junhe.integral.core.inteEvent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.junhe.integral.common.PageData;
import com.junhe.integral.core.category.dao.CategoryDao;
import com.junhe.integral.core.category.entity.CategoryEntity;
import com.junhe.integral.core.inteEvent.dao.EventDao;
import com.junhe.integral.core.inteEvent.dto.IntegralEventDTO;
import com.junhe.integral.core.inteEvent.entity.IntegralEventEntity;
import com.junhe.integral.core.inteEvent.service.EventService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 事件管理服务实现类
 * @author HEJUN
 * @since 1.0
 * @date 2023/7/7
 */
@Service
public class EventServiceImpl implements EventService {

    @Resource
    private EventDao eventDao;
    @Resource
    private CategoryDao categoryDao;

    @Override
    public void add(IntegralEventDTO dto) {
        dto.setCode(createCode(dto));
        IntegralEventEntity integralEventEntity = new IntegralEventEntity();
        BeanUtils.copyProperties(dto, integralEventEntity);
        eventDao.insert(integralEventEntity);
    }

    public String createCode(IntegralEventDTO dto){
        Long category = dto.getCategory();
        CategoryEntity categoryEntity = categoryDao.selectById(category);
        String code = eventDao.getMaxCodeByCategory(category);
        if (code == null) {
            return categoryEntity.getCode() + "001";
        }

        int i = Integer.parseInt(code.substring(3, 6));
        return code.substring(0, 3) + String.format("%03d", i + 1);
    }

    @Override
    public IntegralEventDTO get(Long id) {
        IntegralEventEntity integralEventEntity = eventDao.selectById(id);
        IntegralEventDTO dto = new IntegralEventDTO();
        BeanUtils.copyProperties(integralEventEntity, dto);
        return dto;
    }

    @Override
    public void update(IntegralEventDTO dto) {
        IntegralEventEntity integralEventEntity = new IntegralEventEntity();
        BeanUtils.copyProperties(dto, integralEventEntity);
        eventDao.updateById(integralEventEntity);
    }

    @Override
    public void delete(IntegralEventDTO dto) {
        UpdateWrapper<IntegralEventEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(IntegralEventEntity::getIsDelete, 1)
                .eq(IntegralEventEntity::getId, dto.getId());
        eventDao.update(null, updateWrapper);
    }

    @Override
    public PageData<IntegralEventDTO> page(int pageNo, int limit, IntegralEventDTO params) {
        IPage page = new Page(pageNo, limit);
        QueryWrapper<IntegralEventEntity> wrapper = new QueryWrapper<IntegralEventEntity>();
        wrapper.lambda().eq(!ObjectUtils.isEmpty(params.getCategory()), IntegralEventEntity::getCategory, params.getCategory())
                .like(!ObjectUtils.isEmpty(params.getName()), IntegralEventEntity::getName, params.getName())
                .eq(!ObjectUtils.isEmpty(params.getStatus()), IntegralEventEntity::getStatus, params.getStatus());
        IPage pageData = eventDao.selectPage(page, wrapper);
        List<IntegralEventDTO> list = new ArrayList<IntegralEventDTO>();
        if (CollectionUtils.isNotEmpty(pageData.getRecords())) {
            pageData.getRecords().forEach(record -> {
                IntegralEventDTO dto = new IntegralEventDTO();
                BeanUtils.copyProperties(record, dto);
                list.add(dto);
            });
        }
        return new PageData<>(list, pageData.getTotal());
    }

    @Override
    public List<IntegralEventDTO> allEvent() {
        List<IntegralEventEntity> eventEntityList = eventDao.selectList(new QueryWrapper<>());
        List<IntegralEventDTO> list = new ArrayList<IntegralEventDTO>();
        if (CollectionUtils.isNotEmpty(eventEntityList)) {
            eventEntityList.forEach(event -> {
                IntegralEventDTO dto = new IntegralEventDTO();
                BeanUtils.copyProperties(event, dto);
                list.add(dto);
            });
        }
        return list;
    }

    @Override
    public IntegralEventDTO getByCode(String code) {
        QueryWrapper<IntegralEventEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IntegralEventEntity::getCode, code).last(" limit 1");
        IntegralEventEntity integralEventEntity = eventDao.selectOne(wrapper);
        if (integralEventEntity == null) {
            return null;
        }
        IntegralEventDTO dto = new IntegralEventDTO();
        BeanUtils.copyProperties(integralEventEntity, dto);
        return dto;
    }
}
