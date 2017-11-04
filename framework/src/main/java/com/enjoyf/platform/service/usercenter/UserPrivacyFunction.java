package com.enjoyf.platform.service.usercenter;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by ericliu on 14/10/22.
 * 功能设置
 */
public class UserPrivacyFunction implements Serializable {
    private String acceptFollow;
    private String chat;

    public String getAcceptFollow() {
        return acceptFollow;
    }

    public void setAcceptFollow(String acceptFollow) {
        this.acceptFollow = acceptFollow;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public static UserPrivacyFunction getByJson(String json) {
        return new Gson().fromJson(json, UserPrivacyFunction.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
