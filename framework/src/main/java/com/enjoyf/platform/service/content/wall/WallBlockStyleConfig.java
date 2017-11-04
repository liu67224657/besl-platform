package com.enjoyf.platform.service.content.wall;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-3-13
 * Time: 下午4:36
 * To change this template use File | Settings | File Templates.
 */
public class WallBlockStyleConfig implements Serializable{
//    s:0 c:0 br:0 w:141 h:225 img:m
    private int subjectLen = 10;
    private int contentLen = 20;
    private int brLen = 1;
    private int width = 188;
    private int height = 141;
    private String img = "m";

    public WallBlockStyleConfig(){
    }

    public int getSubjectLen() {
        return subjectLen;
    }

    public void setSubjectLen(int subjectLen) {
        this.subjectLen = subjectLen;
    }

    public int getContentLen() {
        return contentLen;
    }

    public void setContentLen(int contentLen) {
        this.contentLen = contentLen;
    }

    public int getBrLen() {
        return brLen;
    }

    public void setBrLen(int brLen) {
        this.brLen = brLen;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
