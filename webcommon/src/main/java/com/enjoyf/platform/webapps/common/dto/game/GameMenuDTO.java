package com.enjoyf.platform.webapps.common.dto.game;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-1-14
 * Time: 下午4:35
 * To change this template use File | Settings | File Templates.
 */
public class GameMenuDTO {
    private long itemId;
    private String menuName;
    private String archPointName;
    private String archPoint;
    private String linkUrl;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getArchPoint() {
        return archPoint;
    }

    public void setArchPoint(String archPoint) {
        this.archPoint = archPoint;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getArchPointName() {
        return archPointName;
    }

    public void setArchPointName(String archPointName) {
        this.archPointName = archPointName;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
