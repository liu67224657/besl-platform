package com.enjoyf.platform.service.tools;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * Author: zhaoxin
 * Date: 11-10-31
 * Time: 下午8:35
 * Desc:
 */
public class ToolsLoginLog implements java.io.Serializable {

    private long lid;
    private String userId;
    private String passWord;
    private ActStatus isSuccess;
    private Date loginTime;
    private String accIp;

    private Date startTime;
    private Date endTime;

    public ToolsLoginLog() {
    }

    public long getLid() {
        return lid;
    }

    public void setLid(long lid) {
        this.lid = lid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public ActStatus getSuccess() {
        return isSuccess;
    }

    public void setSuccess(ActStatus success) {
        isSuccess = success;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getAccIp() {
        return accIp;
    }

    public void setAccIp(String accIp) {
        this.accIp = accIp;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
