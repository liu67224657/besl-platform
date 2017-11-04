package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-4-17
 * Time: 上午9:28
 * To change this template use File | Settings | File Templates.
 */
public class SocialHotContentEvent extends SystemEvent{

    private long contentId;
    private String uno;

    public SocialHotContentEvent() {
        super(SystemEventType.SOCIAL_CONTENT_HOT_INSERT);
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return (int) contentId;
    }
}
