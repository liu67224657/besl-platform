package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-1
 * Time: 下午6:10
 * To change this template use File | Settings | File Templates.
 */
public class ProfileMobileDevice implements Serializable {
    //
    private long pmdId;

    //
    private String mdSerial;

    //
    private ProfileMobileDeviceClientType mdClientType;

    //
    private String mdHdType;

    //'
    private String mdOsVersion;

    //
    private String uno;

    //
    private long pushMsgId;

    //
    private ActStatus pushStatus = ActStatus.UNACT;

    //
    private ValidStatus validStatus = ValidStatus.VALID;

    //
    private Date createDate;

    public ProfileMobileDevice() {

    }

    // getter and setter


    public long getPmdId() {
        return pmdId;
    }

    public void setPmdId(long pmdId) {
        this.pmdId = pmdId;
    }

    public String getMdSerial() {
        return mdSerial;
    }

    public void setMdSerial(String mdSerial) {
        this.mdSerial = mdSerial;
    }

    public ProfileMobileDeviceClientType getMdClientType() {
        return mdClientType;
    }

    public void setMdClientType(ProfileMobileDeviceClientType mdClientType) {
        this.mdClientType = mdClientType;
    }

    public String getMdHdType() {
        return mdHdType;
    }

    public void setMdHdType(String mdHdType) {
        this.mdHdType = mdHdType;
    }

    public String getMdOsVersion() {
        return mdOsVersion;
    }

    public void setMdOsVersion(String mdOsVersion) {
        this.mdOsVersion = mdOsVersion;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public long getPushMsgId() {
        return pushMsgId;
    }

    public void setPushMsgId(long pushMsgId) {
        this.pushMsgId = pushMsgId;
    }

    public ActStatus getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(ActStatus pushStatus) {
        this.pushStatus = pushStatus;
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
}
