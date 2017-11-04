package com.enjoyf.platform.service.tools;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: zhaoxin
 * Date: 11-10-27
 * Time: 下午5:49
 * Desc: 后台工具-审核用户-pojo
 */
public class AuditUser implements Serializable{

    private Long auditId;

    private String uno;

    private AuditStatus auditStatus;

    private Date auditDate;
    private String auditUserId;
    private String auditIp;

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
}
