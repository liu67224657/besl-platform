package com.enjoyf.platform.service.tools;



import java.io.Serializable;
import java.util.Date;

/**
 * Author: zhaoxin
 * Date: 11-10-27
 * Time: 下午5:49
 * Desc:  后台工具-审核文章-pojo
 */
public class AuditContent implements Serializable {

    private Long auditId;

    private String uno;

    private String auditContentId;
    private ContentType contentType;

    private String auditUserId;
    private String auditIp;
    private AuditStatus auditStatus;
    private Date auditDate;

    private AuditResultSet auditResultSet;


    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getAuditContentId() {
        return auditContentId;
    }

    public void setAuditContentId(String auditContentId) {
        this.auditContentId = auditContentId;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(String auditUserId) {
        this.auditUserId = auditUserId;
    }

    public String getAuditIp() {
        return auditIp;
    }

    public void setAuditIp(String auditIp) {
        this.auditIp = auditIp;
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

    public AuditResultSet getAuditResultSet() {
        return auditResultSet;
    }

    public void setAuditResultSet(AuditResultSet auditResultSet) {
        this.auditResultSet = auditResultSet;
    }
}
