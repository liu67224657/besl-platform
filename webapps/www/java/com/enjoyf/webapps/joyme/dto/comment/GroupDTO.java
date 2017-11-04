package com.enjoyf.webapps.joyme.dto.comment;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 15-8-31
 * Time: 下午4:21
 * To change this template use File | Settings | File Templates.
 */
public class GroupDTO {
    private String ji;  //跳转类型
    private String jt;  //跳转参数
    private String type;   /* type 1圈子 2广告v2.2不做*/
    private String name;
    private String desc;
    private String picurl;
    private String pepolenum;   //g关注人数

    public String getPepolenum() {
        return pepolenum;
    }

    public void setPepolenum(String pepolenum) {
        this.pepolenum = pepolenum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
