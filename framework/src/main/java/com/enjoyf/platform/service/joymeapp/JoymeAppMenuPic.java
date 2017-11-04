package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by zhitaoshi on 2015/3/24.
 */
public class JoymeAppMenuPic implements Serializable{

    private String iosPic;
    private String androidPic;

    public String getIosPic() {
        return iosPic;
    }

    public void setIosPic(String iosPic) {
        this.iosPic = iosPic;
    }

    public String getAndroidPic() {
        return androidPic;
    }

    public void setAndroidPic(String androidPic) {
        this.androidPic = androidPic;
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static JoymeAppMenuPic parse(String jsonStr) {
        JoymeAppMenuPic appMenuPic = null;
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                appMenuPic = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<JoymeAppMenuPic>() {
                });
            } catch (IOException e) {
                GAlerter.lab("JoymeAppMenuPic parse error, jsonStr:" + jsonStr, e);
            }
        }
        return appMenuPic;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
