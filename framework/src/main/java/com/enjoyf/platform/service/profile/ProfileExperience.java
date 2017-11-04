package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.service.tools.AuditStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-12-16
 * Time: 上午9:59
 * To change this template use File | Settings | File Templates.
 */
public class ProfileExperience implements Serializable{
    private Long expid;
    private String uno;
    private String expName;
    private ProfileExperienceType experienceType;
    private String metaInfo;
    private Date expStartDate;
    private Date expEndDate;
    private Date createDate;
    private Date updateDate;
    private AuditStatus auditStatus;
    private Date auditDate;

    public ProfileExperience(){
    }
    ///////////////////

    public Long getExpid() {
        return expid;
    }

    public void setExpid(Long expid) {
        this.expid = expid;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    public ProfileExperienceType getExperienceType() {
        return experienceType;
    }

    public void setExperienceType(ProfileExperienceType experienceType) {
        this.experienceType = experienceType;
    }

    public String getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(String metaInfo) {
        this.metaInfo = metaInfo;
    }

    public Date getExpStartDate() {
        return expStartDate;
    }

    public void setExpStartDate(Date expStartDate) {
        this.expStartDate = expStartDate;
    }

    public Date getExpEndDate() {
        return expEndDate;
    }

    public void setExpEndDate(Date expEndDate) {
        this.expEndDate = expEndDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public AuditStatus getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(AuditStatus auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    @Override
    public int hashCode(){
        return expid != null ? expid.hashCode() : 0;
    }

    @Override
    public String toString(){
        return ReflectUtil.fieldsToString(this);
    }
}
