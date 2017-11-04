package com.enjoyf.webapps.joyme.dto.joymeapp.client;

import com.enjoyf.platform.service.joymeapp.ArchiveTag;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-12-12
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */
public class ClientHomeNewsMenuDTO implements Serializable {
    private Long menuId;
    private String picUrl;
    private String menuName;
    private String url;
    private int menuType;
    private String menuDesc;
//    private boolean isNew;
//    private boolean isHot;
    private int display_order;

    private List<ArchiveTag> tagList;

    private Date date;

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMenuType() {
        return menuType;
    }

    public void setMenuType(int menuType) {
        this.menuType = menuType;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

//    public boolean isNew() {
//        return isNew;
//    }
//
//    public void setNew(boolean aNew) {
//        isNew = aNew;
//    }
//
//    public boolean isHot() {
//        return isHot;
//    }
//
//    public void setHot(boolean hot) {
//        isHot = hot;
//    }

    public int getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(int display_order) {
        this.display_order = display_order;
    }

    public List<ArchiveTag> getTagList() {
        return tagList;
    }

    public void setTagList(List<ArchiveTag> tagList) {
        this.tagList = tagList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
