package com.enjoyf.webapps.joyme.dto.usercenter;

/**
 * Created by ericliu on 14/10/22.
 */
public class ProfileDTO {
    private long uid;
    private String uno;
    private String nick;
    private String iconurl;
    private String desc;
    private long noticetime;
    private String logindomain;

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

    public long getNoticetime() {
        return noticetime;
    }

    public void setNoticetime(long noticetime) {
        this.noticetime = noticetime;
    }

    public String getLogindomain() {
        return logindomain;
    }

    public void setLogindomain(String logindomain) {
        this.logindomain = logindomain;
    }
}
