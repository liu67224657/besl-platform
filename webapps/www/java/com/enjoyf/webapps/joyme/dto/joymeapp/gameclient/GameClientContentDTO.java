package com.enjoyf.webapps.joyme.dto.joymeapp.gameclient;

/**
 * Created by zhimingli
 * Date: 2014/12/26
 * Time: 15:22
 */
public class GameClientContentDTO {
    private String title;
    private String picurl;
    private String jt;
    private String ji;
    private String desc;
    private String iconurl;
    private String tagname;
    private String agreenum;
    private String readnum="";
    private String publishtime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
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

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public String getAgreenum() {
        return agreenum;
    }

    public void setAgreenum(String agreenum) {
        this.agreenum = agreenum;
    }

    public String getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(String publishtime) {
        this.publishtime = publishtime;
    }

    public String getReadnum() {
        return readnum;
    }

    public void setReadnum(String readnum) {
        this.readnum = readnum;
    }
}
