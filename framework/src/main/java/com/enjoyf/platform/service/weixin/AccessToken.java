package com.enjoyf.platform.service.weixin;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-5-13
 * Time: 下午12:18
 * To change this template use File | Settings | File Templates.
 * 微信通用接口凭证
 */
public class AccessToken implements Serializable {
    //获得的凭证
    private String token;
    //有效期，单位：秒
    private int expiresIn;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
