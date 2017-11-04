package com.enjoyf.platform.service.advertise;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ericliu on 16/3/7.
 */
public class DeviceEvent implements Serializable {
    private String deviceId;
    private String profileId;
    private String agentId;
    private AgentCode agentCode;
    private Date createTime;
    private String createIp;
    private String eventType;//是否要创建新的类
    private int eventValue;


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getEventValue() {
        return eventValue;
    }

    public void setEventValue(int eventValue) {
        this.eventValue = eventValue;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public AgentCode getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(AgentCode agentCode) {
        this.agentCode = agentCode;
    }
}
