package com.enjoyf.platform.service.content;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.tools.AuditStatus;
import com.enjoyf.platform.service.tools.ContentAuditStatus;
import com.enjoyf.platform.service.tools.ContentReplyAuditStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: zhaoxin
 * Date: 11-11-2
 * Time: 下午3:41
 * Desc: 用于tools查询条件
 */
public class ContentQueryParam implements Serializable{

    private String uno;
    private String loginName;

    private Date startDate;
    private Date endDate;

    private String key;

    private ContentType contentType;

     //伪删除标记
    private ActStatus removeStatus = ActStatus.UNACT;

    private ContentAuditStatus auditStatus;
    private ContentReplyAuditStatus auditReplyStatus;

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public ContentAuditStatus getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(ContentAuditStatus auditStatus) {
        this.auditStatus = auditStatus;
    }

    public ContentReplyAuditStatus getAuditReplyStatus() {
        return auditReplyStatus;
    }

    public void setAuditReplyStatus(ContentReplyAuditStatus auditReplyStatus) {
        this.auditReplyStatus = auditReplyStatus;
    }
}
