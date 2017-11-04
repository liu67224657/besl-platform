package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by zhimingli on 2015/3/20.
 */
public class AppSecret implements Serializable {
    private String ios;
    private String android;


    public static String toJson(AppSecret appSecret) {
        return JsonBinder.buildNonDefaultBinder().toJson(appSecret);
    }

    public static AppSecret fromJson(String jsonString) {
        AppSecret param = new AppSecret();
        try {
            if (!StringUtil.isEmpty(jsonString)) {
                param = JsonBinder.buildNonNullBinder().getMapper().readValue(jsonString, new TypeReference<AppSecret>() {
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return param;
    }

    public String toJson() {
        return JsonBinder.buildNonNullBinder().toJson(this);
    }


    public String getIos() {
        return ios;
    }

    public void setIos(String ios) {
        this.ios = ios;
    }

    public String getAndroid() {
        return android;
    }

    public void setAndroid(String android) {
        this.android = android;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
