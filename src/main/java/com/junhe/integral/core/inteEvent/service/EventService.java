package com.junhe.integral.core.inteEvent.service;

import com.junhe.integral.common.PageData;
import com.junhe.integral.core.inteEvent.dto.IntegralEventDTO;

import java.util.List;

/**
 * 事件管理服务接口
 * @author HEJUN
 * @since 1.0
 * @date 2023/7/7
 */
public interface EventService {
    /**
     * 新增积分事件
     * @param dto 积分事件详情信息
     * @author HEJUN
     * @date 2023/7/7
     */
    void add(IntegralEventDTO dto);

    /**
     * 查询事件详情
     * @param id 事件ID
     * @return com.junhe.integral.core.inteEvent.dto.IntegralEventDTO
     * @author HEJUN
     * @date 2023/7/7
     */
    IntegralEventDTO get(Long id);

    /**
     * 修改积分事件
     * @param dto 积分事件信息
     * @return void
     * @author HEJUN
     * @date 2023/7/25
     */
    void update(IntegralEventDTO dto);

    /**
     * 删除积分事件
     * @param dto 积分事件信息
     * @return void
     * @author HEJUN
     * @date 2023/7/25
     */
    void delete(IntegralEventDTO dto);

    /**
     * 分页查询积分事件
     *
     * @param page 页码
     * @param limit 每页记录数
     * @param params 查询条件
     * @return com.junhe.integral.common.PageData<com.junhe.integral.core.inteEvent.dto.IntegralEventDTO>
     * @author HEJUN
     * @date 2023/7/25
     */
    PageData<IntegralEventDTO> page(int page, int limit, IntegralEventDTO params);

    /**
     * 查询所有积分事件
     * @return java.util.List<com.junhe.integral.core.inteEvent.dto.IntegralEventDTO>
     * @author HEJUN
     * @date 2023/7/27
     */
    List<IntegralEventDTO> allEvent();

    /**
     * 通过事件编码查询积分事件
     * @param code
     * @return com.junhe.integral.core.inteEvent.dto.IntegralEventDTO
     * @author HEJUN
     * @date 2023/7/27
     */
    IntegralEventDTO getByCode(String code);
}
