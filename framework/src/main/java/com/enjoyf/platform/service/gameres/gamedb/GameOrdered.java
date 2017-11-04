package com.enjoyf.platform.service.gameres.gamedb;

import net.sf.json.JSONObject;

import java.io.Serializable;

/**
 * Created by zhitaoshi on 2016/5/11.
 */
public class GameOrdered implements Serializable{

    private long gameOrderedId;
    private long gameId;
    private String name;
    private String weiXin;
    private String qq;
    private String mobile;

    public long getGameOrderedId() {
        return gameOrderedId;
    }

    public void setGameOrderedId(long gameOrderedId) {
        this.gameOrderedId = gameOrderedId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeiXin() {
        return weiXin;
    }

    public void setWeiXin(String weiXin) {
        this.weiXin = weiXin;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = JSONObject.fromObject(this);
        return jsonObject.toString();
    }
}
