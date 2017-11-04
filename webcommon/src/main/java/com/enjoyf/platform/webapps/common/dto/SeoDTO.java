package com.enjoyf.platform.webapps.common.dto;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-10-16
 * Time: 下午2:40
 * To change this template use File | Settings | File Templates.
 */
public class SeoDTO {
    private String title;
    private String keywords;
    private String desc;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
