package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.Serializable;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/12
 */
public class AskVoice implements Serializable {
    private String url = "";
    private long time;

    public AskVoice() {
    }

    public AskVoice(String url, long time) {
        this.url = url;
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static AskVoice toObject(String jsonStr) {
        try {
            return new Gson().fromJson(jsonStr, AskVoice.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
