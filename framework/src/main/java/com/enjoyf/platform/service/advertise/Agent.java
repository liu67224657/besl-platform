package com.enjoyf.platform.service.advertise;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by ericliu on 16/3/4.
 */
public class Agent  implements Serializable {

    private String agentId;//Md5(agentCode)
    private String agentCode;
    private String agentName;
    private String agentDesc;
    private String modifyTime;
    private String modifyUserId;
    private String modifyIp;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentDesc() {
        return agentDesc;
    }

    public void setAgentDesc(String agentDesc) {
        this.agentDesc = agentDesc;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public String getModifyIp() {
        return modifyIp;
    }

    public void setModifyIp(String modifyIp) {
        this.modifyIp = modifyIp;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
