package com.enjoyf.platform.service.tools;

import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 上午10:43
 * Desc:
 */
public class AuditContentQueryEntity implements Serializable{

    //如果是回复贴，需要指出对应的文章ID，通过它进行分表
    private String relationId;//回复中记录文章ID
    private String auditContentId;
    private ContentType contentType;
    private Long auditId;
    private String uno;

    public AuditContentQueryEntity(String auditContentId) {
        this.auditContentId = auditContentId;
    }

    public AuditContentQueryEntity(String relationId, String auditContentId) {
        this.relationId = relationId;
        this.auditContentId = auditContentId;
    }

    public AuditContentQueryEntity(String relationId, String auditContentId, Long auditId) {
        this.relationId = relationId;
        this.auditContentId = auditContentId;
        this.auditId = auditId;
    }

    public AuditContentQueryEntity(String auditContentId, ContentType contentType) {
        this.auditContentId = auditContentId;
        this.contentType = contentType;
    }

    public AuditContentQueryEntity(String relationId, String auditContentId, ContentType contentType) {
        this.relationId = relationId;
        this.auditContentId = auditContentId;
        this.contentType = contentType;
    }

    public AuditContentQueryEntity(String relationId, String auditContentId, ContentType contentType, Long auditId) {
        this.relationId = relationId;
        this.auditContentId = auditContentId;
        this.contentType = contentType;
        this.auditId = auditId;
    }

    public AuditContentQueryEntity(String relationId, String auditContentId, ContentType contentType, Long auditId, String uno) {
        this.relationId = relationId;
        this.auditContentId = auditContentId;
        this.contentType = contentType;
        this.auditId = auditId;
        this.uno = uno;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getAuditContentId() {
        return auditContentId;
    }

    public void setAuditContentId(String auditContentId) {
        this.auditContentId = auditContentId;
    }

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    @Override
    public int hashCode() {
        if(relationId!=null){
            return relationId != null ? relationId.hashCode() : 0;
        }
        return auditContentId != null ? auditContentId.hashCode() : 0;
    }


    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
