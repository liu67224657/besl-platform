package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Created by zhitaoshi on 2015/4/14.
 */
public class JoymeWikiParam implements Serializable {
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static JoymeWikiParam parse(String jsonStr) {
        JoymeWikiParam param = null;
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                param = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<JoymeWikiParam>() {
                });
            } catch (IOException e) {
                GAlerter.lab("JoymeWikiParam parse error, jsonStr:" + jsonStr, e);
            }
        }
        return param;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
