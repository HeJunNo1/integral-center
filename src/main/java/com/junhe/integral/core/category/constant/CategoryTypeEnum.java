package com.junhe.integral.core.category.constant;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分分类类型枚举类
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/21
 */
@Getter
public enum CategoryTypeEnum {

    /**
     * 系统类型
     */
    SystemCategory(1, "系统类型"),
    /**
     * 自定义类型
     */
    CustomCategory(2, "自定义类型");

    /**
     * 类型编号
     */
    private Integer code;
    /**
     * 描述
     */
    private String desc;

    CategoryTypeEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static List<Map<String, Object>> typeItems() {
        List<Map<String, Object>> items = new ArrayList<>();
        for (CategoryTypeEnum typeEnum : CategoryTypeEnum.values()) {
            Map<String, Object> item = new HashMap<>();
            item.put("value", typeEnum.getCode());
            item.put("label", typeEnum.getDesc());
            items.add(item);
        }
        return items;
    }
}
