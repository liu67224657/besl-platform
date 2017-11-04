package com.enjoyf.webapps.joyme.dto.joymeapp.anime;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-10-27
 * Time: 下午5:46
 * To change this template use File | Settings | File Templates.
 */
public class AnimeSpecialDTO {
    private String title;      //标题
    private String picurl;     //PICurl
    private String tips;  //角标
    private String desc; //描述
    private String tipscolor;// 1-红色 2-绿色 3-蓝色 4-橙色
    private String ji;     //JoymeInfo
    private String jt;  //跳转类型 详见AnimeRedirectType 类

    private String readnum = "";  //阅读数
    private String displaytype = "";  //展示类型
    private String replynum = "";//评论数

    public String getReplynum() {
        return replynum;
    }

    public void setReplynum(String replynum) {
        this.replynum = replynum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }


    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getTipscolor() {
        return tipscolor;
    }

    public void setTipscolor(String tipscolor) {
        this.tipscolor = tipscolor;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getReadnum() {
        return readnum;
    }

    public void setReadnum(String readnum) {
        this.readnum = readnum;
    }

    public String getDisplaytype() {
        return displaytype;
    }

    public void setDisplaytype(String displaytype) {
        this.displaytype = displaytype;
    }
}
