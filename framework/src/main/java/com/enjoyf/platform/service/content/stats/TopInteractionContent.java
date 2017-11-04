/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content.stats;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Description: 文章交互统计
 */
public class TopInteractionContent implements Serializable {
    //content info
    private String contentId;
    private String contentUno;

    //
    private int value;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentUno() {
        return contentUno;
    }

    public void setContentUno(String contentUno) {
        this.contentUno = contentUno;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
