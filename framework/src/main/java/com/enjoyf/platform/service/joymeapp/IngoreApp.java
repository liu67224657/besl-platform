package com.enjoyf.platform.service.joymeapp;


import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-19
 * Time: 下午7:37
 * To change this template use File | Settings | File Templates.
 */
@JsonIgnoreProperties(value = {"ingore_app_id"})
public class IngoreApp implements Serializable {
    private int ingore_app_id;
    private String app_name;
    private String pkg_name;
    private int platform;

    public int getIngore_app_id() {
        return ingore_app_id;
    }

    public void setIngore_app_id(int ingore_app_id) {
        this.ingore_app_id = ingore_app_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getPkg_name() {
        return pkg_name;
    }

    public void setPkg_name(String pkg_name) {
        this.pkg_name = pkg_name;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
