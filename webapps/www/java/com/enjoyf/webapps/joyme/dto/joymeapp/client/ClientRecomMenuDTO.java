package com.enjoyf.webapps.joyme.dto.joymeapp.client;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-12-12
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */
public class ClientRecomMenuDTO implements Serializable {
    private Long menuId;
    private String picurl;
    private String title;
    private String value;
    private int type;
    private String desc;
//    private boolean isNew;
//    private boolean isHot;
//    private int display_order;

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
