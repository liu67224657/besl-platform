package com.enjoyf.webapps.joyme.weblogic.entity;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * 用户个人博客实体类
 */
public class UserBlogwebsite implements java.io.Serializable {

    private Integer blogwebsiteid;
    private String uno;
    private String blogname;
    private String blogdomain;
    private String headimg;
    private String introduce;
    private Integer templateid;
    private Integer selfdefine;
    private String selftemplatedata;

    public UserBlogwebsite() {
    }

    public UserBlogwebsite(String uno, String blogdomain) {
        this.uno = uno;
        this.blogdomain = blogdomain;
    }

    public UserBlogwebsite(String uno, String blogname, String blogdomain,
                           String headimg, String introduce, Integer templateid,
                           Integer selfdefine, String selftemplatedata) {
        this.uno = uno;
        this.blogname = blogname;
        this.blogdomain = blogdomain;
        this.headimg = headimg;
        this.introduce = introduce;
        this.templateid = templateid;
        this.selfdefine = selfdefine;
        this.selftemplatedata = selftemplatedata;
    }

    public Integer getBlogwebsiteid() {
        return this.blogwebsiteid;
    }

    public void setBlogwebsiteid(Integer blogwebsiteid) {
        this.blogwebsiteid = blogwebsiteid;
    }

    public String getUno() {
        return this.uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getBlogname() {
        return this.blogname;
    }

    public void setBlogname(String blogname) {
        this.blogname = blogname;
    }

    public String getBlogdomain() {
        return this.blogdomain;
    }

    public void setBlogdomain(String blogdomain) {
        this.blogdomain = blogdomain;
    }

    public String getHeadimg() {
        return this.headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getIntroduce() {
        return this.introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Integer getTemplateid() {
        return this.templateid;
    }

    public void setTemplateid(Integer templateid) {
        this.templateid = templateid;
    }

    public Integer getSelfdefine() {
        return this.selfdefine;
    }

    public void setSelfdefine(Integer selfdefine) {
        this.selfdefine = selfdefine;
    }

    public String getSelftemplatedata() {
        return this.selftemplatedata;
    }

    public void setSelftemplatedata(String selftemplatedata) {
        this.selftemplatedata = selftemplatedata;
    }


    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
