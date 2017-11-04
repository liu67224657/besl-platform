package com.enjoyf.platform.service.advertise;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ericliu on 16/3/4.
 */
public class ActivityDevice implements Serializable {

    private String deviceId;
//    private IntValidStatus status = IntValidStatus.VALIDING;//ing--is init valid--is actvity device
    private String agentId;
    private AgentCode agentCode;
    private Date createTime;
    private String createIp;
    private Date modifyTime;
    private String modifyIp;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

//    public IntValidStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(IntValidStatus status) {
//        this.status = status;
//    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyIp() {
        return modifyIp;
    }

    public void setModifyIp(String modifyIp) {
        this.modifyIp = modifyIp;
    }

    public AgentCode getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(AgentCode agentCode) {
        this.agentCode = agentCode;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
