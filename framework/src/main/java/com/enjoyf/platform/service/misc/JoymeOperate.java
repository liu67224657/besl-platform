package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-3 下午3:53
 * Description:
 */
public class JoymeOperate implements Serializable {
    private long operateId;
    private JoymeOperateType operateType;
    private String content;
    private String serverId;
    private Date createTime;
    private String createUserId;

    public long getOperateId() {
        return operateId;
    }

    public void setOperateId(long operateId) {
        this.operateId = operateId;
    }

    public JoymeOperateType getOperateType() {
        return operateType;
    }

    public void setOperateType(JoymeOperateType operateType) {
        this.operateType = operateType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
