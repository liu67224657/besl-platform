package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-9-18
 * Time: 下午4:50
 * To change this template use File | Settings | File Templates.
 */
public class AppPushCustom implements Serializable {

    private String jt;
    private String ji;

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

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
