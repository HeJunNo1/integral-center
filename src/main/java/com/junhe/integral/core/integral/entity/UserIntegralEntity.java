package com.junhe.integral.core.integral.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.junhe.integral.common.PojoBaseInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户总积分
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/25
 */
@Data
@TableName("integral_user_total")
public class UserIntegralEntity {

    @TableId
    private String userId;

    private String username;

    private String nickname;

    private Integer points;

    private Date createTime;

    private Date updateTime;
}
