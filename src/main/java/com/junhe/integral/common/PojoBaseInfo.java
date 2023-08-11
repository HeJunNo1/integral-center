package com.junhe.integral.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体对象通用字段
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/21
 */
@Data
public class PojoBaseInfo implements Serializable {

    protected Date createTime;

    protected Date updateTime;

    protected String creator;

    protected String updater;

    protected Integer isDelete;
}
