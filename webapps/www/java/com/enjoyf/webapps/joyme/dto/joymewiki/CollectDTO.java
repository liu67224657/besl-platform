package com.enjoyf.webapps.joyme.dto.joymewiki;

/**
 * Created by pengxu on 2017/3/28.
 */
public class CollectDTO {
    private long id;
    private long time;//发布时间
    private String gamename;//标题
    private String discussion;//所属的wiki/讨论区/游戏
    private String ctype;//类型//1=CMS文章 2=wiki文章
    private String ji;//跳转类型
    private String jt;//

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }

    public String getDiscussion() {
        return discussion;
    }

    public void setDiscussion(String discussion) {
        this.discussion = discussion;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getJi() {
        return ji;
    }

    public void setJi(String ji) {
        this.ji = ji;
    }

    public String getJt() {
        return jt;
    }

    public void setJt(String jt) {
        this.jt = jt;
    }
}
