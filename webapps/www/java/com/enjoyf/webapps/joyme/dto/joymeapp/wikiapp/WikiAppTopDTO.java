package com.enjoyf.webapps.joyme.dto.joymeapp.wikiapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pengxu on 2015/4/8.
 */

public class WikiAppTopDTO implements Serializable {
    /**
     * @param wikikey.
     * @param name.  wiki名称
     * @param picurl. wiki图片url
     * @param hotsum. 热度
     * @param follow. 关注 1：已经关注  0：为关注
     * @param ji.   URL
     * @param jt. 跳转类型
     * @param category. 标签
     */
    private String wikikey;
    private String name;
    private String picurl;
    private String hotsum;
    private String follow;
    private String ji;
    private String jt;
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWikikey() {
        return wikikey;
    }

    public void setWikikey(String wikikey) {
        this.wikikey = wikikey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getHotsum() {
        return hotsum;
    }

    public void setHotsum(String hotsum) {
        this.hotsum = hotsum;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getJi() {
        return ji;
    }

    public void setJi(String ji) {
        this.ji = ji;
    }

    public String getJt() {
        return jt;
    }

    public void setJt(String jt) {
        this.jt = jt;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
