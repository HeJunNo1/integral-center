package com.junhe.integral.core.integral.dto;

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
public class UserIntegralDTO extends PojoBaseInfo implements Serializable {

    private String userId;

    private String username;

    private String nickname;

    private Integer points;

    private Date createTime;

    private Date updateTime;
}
