package com.enjoyf.platform.service.content;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章关系实体类
 */
public class ContentRelation implements Serializable {
    private String contentId;
    private String relationId;
    private String relationValue;
    private ContentRelationType relationType;
    private ActStatus removeStatus = ActStatus.UNACT;
    private Date createDate;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public ContentRelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(ContentRelationType relationType) {
        this.relationType = relationType;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRelationValue() {
        return relationValue;
    }

    public void setRelationValue(String relationValue) {
        this.relationValue = relationValue;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
