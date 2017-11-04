/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.content.ContentField;
import com.enjoyf.platform.service.content.ContentInteraction;
import com.enjoyf.platform.service.content.ContentInteractionField;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.ObjectField;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:15
 * Description:
 */
public class ContentInteractionSumIncreaseEvent extends SystemEvent {
    //
    private String contentUno;
    private String contentId;
    private String contentInteractionId;
    private ObjectField field;
    private int count;

    //
    public ContentInteractionSumIncreaseEvent() {
        super(SystemEventType.CONTENT_INTERACTION_SUM_INCREASE);
    }

    public String getContentUno() {
        return contentUno;
    }

    public void setContentUno(String contentUno) {
        this.contentUno = contentUno;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public ObjectField getField() {
        return field;
    }

    public void setField(ContentInteractionField field) {
        this.field = field;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getContentInteractionId() {
        return contentInteractionId;
    }

    public void setContentInteractionId(String contentInteractionId) {
        this.contentInteractionId = contentInteractionId;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return contentUno.hashCode();
    }
}
