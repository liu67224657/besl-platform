package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.advertise.AgentCode;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * Created by zhimingli on 2015/9/25 0025.
 */
public class AdvertiseDeviceClickEvent extends SystemEvent {

    public AdvertiseDeviceClickEvent() {
        super(SystemEventType.ADVERTISE_DEVICE_CLICK);
    }

    private String deviceId;
    private String agentId;
    private Date createTime;
    private String createIp;
    private IntValidStatus status = IntValidStatus.VALIDING;
    private String appkey;
    private String thirdAppKey;
    private AgentCode agentCode;

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

    public IntValidStatus getStatus() {
        return status;
    }

    public void setStatus(IntValidStatus status) {
        this.status = status;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getThirdAppKey() {
        return thirdAppKey;
    }

    public void setThirdAppKey(String thirdAppKey) {
        this.thirdAppKey = thirdAppKey;
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
