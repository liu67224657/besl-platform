package com.enjoyf.webapps.joyme.dto.joymeapp.anime;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-10-27
 * Time: 下午5:46
 * To change this template use File | Settings | File Templates.
 */
public class AnimeIndexDTO {
    private String title;      //标题
    private String navtitle;  // 首页名称
    private String wikinum;    //wiki数量
    private String tips;  //角标
    private String ji;     //JoymeInfo
    private String jt;  //跳转类型 详见AnimeRedirectType 类
    private String picurl;     //PICurl
    private String desc;     //图片下显示文字


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWikinum() {
        return wikinum;
    }

    public void setWikinum(String wikinum) {
        this.wikinum = wikinum;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNavtitle() {
        return navtitle;
    }

    public void setNavtitle(String navtitle) {
        this.navtitle = navtitle;
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
}
