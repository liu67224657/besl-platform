package com.enjoyf.webapps.joyme.dto.Wanba;

/**
 * Created by zhimingli on 2016/9/18 0018.
 */
public class ActivityTopMenuDTO {
    private String name;//名称
    private String desc;//描述
    private String picurl;//图片地址
    private String ji;
    private String jt;
    private String pictype;//大图小图

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getPictype() {
        return pictype;
    }

    public void setPictype(String pictype) {
        this.pictype = pictype;
    }
}
