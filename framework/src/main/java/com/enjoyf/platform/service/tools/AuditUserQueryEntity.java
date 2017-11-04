package com.enjoyf.platform.service.tools;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 上午10:12
 * Desc:
 */
public class AuditUserQueryEntity  implements Serializable {

    private String uno;
    private Long auditId;


    public AuditUserQueryEntity(String uno) {
        this.uno = uno;
    }

    public AuditUserQueryEntity(String uno, Long auditId) {
        this.uno = uno;
        this.auditId = auditId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }



    @Override
    public int hashCode() {
        return auditId != null ? auditId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
