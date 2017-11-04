package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-1-13
 * Time: 上午11:19
 * To change this template use File | Settings | File Templates.
 */
public class ClientLineFlag implements Serializable{

    private long flagId;
    private String flagDesc;

    private Long lineId;
    private String lineCode;

    private Long maxItemId;

    private ClientLineFlagType clientLineFlagType;

    private ValidStatus validStatus = ValidStatus.VALID;

    private Date createDate;
    private String createIp;
    private String createUserId;

    private Date modifyDate;
    private String modifyIp;
    private String modifyUserId;

    public long getFlagId() {
        return flagId;
    }

    public void setFlagId(long flagId) {
        this.flagId = flagId;
    }

    public String getFlagDesc() {
        return flagDesc;
    }

    public void setFlagDesc(String flagDesc) {
        this.flagDesc = flagDesc;
    }

    public long getLineId() {
        return lineId;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public Long getMaxItemId() {
        return maxItemId;
    }

    public void setMaxItemId(Long maxItemId) {
        this.maxItemId = maxItemId;
    }

    public ClientLineFlagType getClientLineFlagType() {
        return clientLineFlagType;
    }

    public void setClientLineFlagType(ClientLineFlagType clientLineFlagType) {
        this.clientLineFlagType = clientLineFlagType;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyIp() {
        return modifyIp;
    }

    public void setModifyIp(String modifyIp) {
        this.modifyIp = modifyIp;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
