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
public class AppVersionInfo implements Serializable {
    private String app_key;
    private String version;
    private String version_url;
    private String version_info;
    private int update_type = 0;//0-非强制更新 1-强制更新

    public AppVersionInfo(String appKey, String appVersion, String version_url, String version_info) {
        this.app_key = appKey;
        this.version = appVersion;
        this.version_url = version_url;
        this.version_info = version_info;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }


    public String getVersion_url() {
        return version_url;
    }

    public void setVersion_url(String version_url) {
        this.version_url = version_url;
    }

    public int getUpdate_type() {
        return update_type;
    }

    public void setUpdate_type(int update_type) {
        this.update_type = update_type;
    }

    public String getVersion_info() {
        return version_info;
    }

    public void setVersion_info(String version_info) {
        this.version_info = version_info;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
