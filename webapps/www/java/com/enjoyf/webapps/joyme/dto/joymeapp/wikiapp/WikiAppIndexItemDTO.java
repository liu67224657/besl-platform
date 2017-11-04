package com.enjoyf.webapps.joyme.dto.joymeapp.wikiapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by zhitaoshi on 2015/4/8.
 */
public class WikiAppIndexItemDTO implements Serializable{

    private String wikikey;
    private String title;
    private String picurl;
    private String jt;
    private String ji;
    private String hotsum;
    private String follow;
    private String cat;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getJt() {
        return jt;
    }

    public void setJt(String jt) {
        this.jt = jt;
    }

    public String getJi() {
        return ji;
    }

    public void setJi(String ji) {
        this.ji = ji;
    }

    public String getWikikey() {
        return wikikey;
    }

    public void setWikikey(String wikikey) {
        this.wikikey = wikikey;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
