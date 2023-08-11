package com.junhe.integral.api.params;

import com.junhe.integral.util.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户积分查询参数
 * @author HEJUN
 * @since 1.0
 * @date 2023/8/10
 */
@Data
public class UserIntegralDetailQueryParam implements Serializable {

    private String userId;

    private Integer type;

    private String eventCode;

    private Date startTime;

    private Date endTime;

    private int page = 1;

    private int limit = 10;

    public String getStartTimeFormat(){
        if (this.startTime == null) {
            return null;
        }
        return DateUtil.format(this.startTime, "yyyy-MM-dd 00:00:00");
    }

    public String getEndTimeFormat(){
        if (this.endTime == null) {
            return null;
        }
        return DateUtil.format(this.endTime, "yyyy-MM-dd 23:59:59");
    }
}
