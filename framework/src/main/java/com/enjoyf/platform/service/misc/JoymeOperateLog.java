package com.enjoyf.platform.service.misc;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-3 下午3:53
 * Description:
 */
public class JoymeOperateLog implements Serializable {
    private long operateLogId;
    private JoymeOperateType operateType;
    private String operateServerId;
    private long operateId;
    private Date operateTime;

    public long getOperateLogId() {
        return operateLogId;
    }

    public void setOperateLogId(long operateLogId) {
        this.operateLogId = operateLogId;
    }

    public JoymeOperateType getOperateType() {
        return operateType;
    }

    public void setOperateType(JoymeOperateType operateType) {
        this.operateType = operateType;
    }

    public String getOperateServerId() {
        return operateServerId;
    }

    public void setOperateServerId(String operateServerId) {
        this.operateServerId = operateServerId;
    }

    public long getOperateId() {
        return operateId;
    }

    public void setOperateId(long operateId) {
        this.operateId = operateId;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
}
