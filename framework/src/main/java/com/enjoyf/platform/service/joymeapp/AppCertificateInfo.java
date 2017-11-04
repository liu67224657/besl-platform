package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-24
 * Time: 上午10:20
 * To change this template use File | Settings | File Templates.
 */
public class AppCertificateInfo implements Serializable {
    private String app_key;
    private String path;
    private String password;
    private boolean isProduction;

    public AppCertificateInfo(String appKey, String path, String password,boolean isProduction) {
        this.app_key = appKey;
        this.path = path;
        this.password = password;
        this.isProduction=isProduction;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isProduction() {
        return isProduction;
    }

    public void setProduction(boolean production) {
        isProduction = production;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
