package com.enjoyf.platform.service.notice;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/23
 */
public abstract class AbstractUserNoticeBody implements Serializable {
    private int bodyType;//回答 接受 过期

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static <T extends AbstractUserNoticeBody> T fromJson(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }

    public int getBodyType() {
        return bodyType;
    }

    public void setBodyType(int bodyType) {
        this.bodyType = bodyType;
    }
}
