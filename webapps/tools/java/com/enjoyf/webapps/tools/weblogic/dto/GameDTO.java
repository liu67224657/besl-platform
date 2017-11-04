package com.enjoyf.webapps.tools.weblogic.dto;

import java.util.Date;

/**
 * Created by pengxu on 2017/4/6.
 */
public class GameDTO {
    private String icon;
    private String gameName;
    private Date  createTime;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
