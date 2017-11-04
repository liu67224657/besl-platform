package com.enjoyf.webapps.joyme.dto.joymeapp;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-10-21
 * Time: 下午8:20
 * To change this template use File | Settings | File Templates.
 */
public class ShakeGameDTO {
    private String game_name;
    private String icon;
    private String reta;
    private String rec_reason;
    private String rec_reason2;
    private String ios_url;
    private String android_url;
    private String gameid;

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getReta() {
        return reta;
    }

    public void setReta(String reta) {
        this.reta = reta;
    }

    public String getRec_reason() {
        return rec_reason;
    }

    public void setRec_reason(String rec_reason) {
        this.rec_reason = rec_reason;
    }

    public String getRec_reason2() {
        return rec_reason2;
    }

    public void setRec_reason2(String rec_reason2) {
        this.rec_reason2 = rec_reason2;
    }

    public String getIos_url() {
        return ios_url;
    }

    public void setIos_url(String ios_url) {
        this.ios_url = ios_url;
    }

    public String getAndroid_url() {
        return android_url;
    }

    public void setAndroid_url(String android_url) {
        this.android_url = android_url;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }
}
