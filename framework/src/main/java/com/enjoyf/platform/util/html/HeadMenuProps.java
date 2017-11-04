package com.enjoyf.platform.util.html;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-8-16
 * Time: 下午1:54
 * To change this template use File | Settings | File Templates.
 */
public class HeadMenuProps {
    private String code;
    private String name;
    private String displayType;
    private String url;
    private String seoKeyWords;
    private String seoDescription;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSeoKeyWords() {
        return seoKeyWords;
    }

    public void setSeoKeyWords(String seoKeyWords) {
        this.seoKeyWords = seoKeyWords;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
