package com.junhe.integral.core.integral.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.junhe.integral.core.integral.dao.IntegralDecreaseLogDao;
import com.junhe.integral.core.integral.dto.IntegralDecreaseLogDTO;
import com.junhe.integral.core.integral.entity.IntegralDecreaseLogEntity;
import com.junhe.integral.core.integral.service.IntegralDecreaseLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IntegralDecreaseLogServiceImpl extends ServiceImpl<IntegralDecreaseLogDao, IntegralDecreaseLogEntity> implements IntegralDecreaseLogService {

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void batchInsert(List<IntegralDecreaseLogEntity> list) {
        this.saveBatch(list, 1000);
    }

    @Override
    public List<IntegralDecreaseLogDTO> queryByOrderId(String orderId) {
        QueryWrapper<IntegralDecreaseLogEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IntegralDecreaseLogEntity::getOrderId, orderId);
        List<IntegralDecreaseLogEntity> entities = baseMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        }
        List<IntegralDecreaseLogDTO> list = entities.stream().map(e -> {
            IntegralDecreaseLogDTO dto = new IntegralDecreaseLogDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        }).collect(Collectors.toList());
        return list;
    }
}
