package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.content.ContentRelationType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-1-8
 * Time: 上午8:30
 * To change this template use File | Settings | File Templates.
 */
public class ContentRelationRemoveEvent extends SystemEvent {
    private String contentId;
    private String relationId;
    private ContentRelationType contentRelationType;

    public ContentRelationRemoveEvent() {
        super(SystemEventType.CONTENT_RELATION_REMOVE);
    }

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

    public ContentRelationType getContentRelationType() {
        return contentRelationType;
    }

    public void setContentRelationType(ContentRelationType contentRelationType) {
        this.contentRelationType = contentRelationType;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return contentId.hashCode();
    }
}
