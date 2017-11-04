/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="ericliu@enjoyfound.com">Liu Hao</a>
 * Create time: 12-5-04 下午5:34
 * Description:
 */
public class ContentInteractionQueryEntry implements Serializable {
    private String uno;
    private String contentId;
    private String interactionId;

    public ContentInteractionQueryEntry(String uno, String contentId, String interactionId) {
        this.uno = uno;
        this.contentId = contentId;
        this.interactionId = interactionId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(String interactionId) {
        this.interactionId = interactionId;
    }

    @Override
    public int hashCode() {
        return interactionId != null ? interactionId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
