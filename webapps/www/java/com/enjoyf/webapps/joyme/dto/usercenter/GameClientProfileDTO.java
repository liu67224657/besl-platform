package com.enjoyf.webapps.joyme.dto.usercenter;

import java.io.Serializable;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/17
 * Description:
 */
public class GameClientProfileDTO implements Serializable {
    private String profileid;
    private long uid;
    private String uno;
    private String nick;
    private String iconurl;
    private String desc;
    private int likesum;
    private int likedsum;
    private int picsum;
    private long messagenoticetime;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getLikesum() {
        return likesum;
    }

    public void setLikesum(int likesum) {
        this.likesum = likesum;
    }

    public int getLikedsum() {
        return likedsum;
    }

    public void setLikedsum(int likedsum) {
        this.likedsum = likedsum;
    }

    public int getPicsum() {
        return picsum;
    }

    public void setPicsum(int picsum) {
        this.picsum = picsum;
    }

    public long getMessagenoticetime() {
        return messagenoticetime;
    }

    public void setMessagenoticetime(long messagenoticetime) {
        this.messagenoticetime = messagenoticetime;
    }

    public String getProfileid() {
        return profileid;
    }

    public void setProfileid(String profileid) {
        this.profileid = profileid;
    }
}
