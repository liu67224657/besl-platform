package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-23
 * Time: 下午5:04
 * To change this template use File | Settings | File Templates.
 */
public class NewReleasePic implements Serializable{

    private String picUrl;
    private boolean display;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public boolean getDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
