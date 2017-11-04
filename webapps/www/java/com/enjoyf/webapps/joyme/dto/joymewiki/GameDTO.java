package com.enjoyf.webapps.joyme.dto.joymewiki;

/**
 * Created by pengxu on 2017/3/28.
 */
public class GameDTO {
    private String picurl;
    private String gameid;
    private String gamename;
    private String focusnum;
    private String jt;
    private String ji;
    private long time;
    private int recommend;
    private int follow;

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }

    public String getFocusnum() {
        return focusnum;
    }

    public void setFocusnum(String focusnum) {
        this.focusnum = focusnum;
    }

    public String getJt() {
        return jt;
    }

    public void setJt(String jt) {
        this.jt = jt;
    }

    public String getJi() {
        return ji;
    }

    public void setJi(String ji) {
        this.ji = ji;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }
}
