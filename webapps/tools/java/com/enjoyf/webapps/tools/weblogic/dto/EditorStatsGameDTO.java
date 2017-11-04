package com.enjoyf.webapps.tools.weblogic.dto;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-4-1
 * Time: 下午1:48
 * To change this template use File | Settings | File Templates.
 */
public class EditorStatsGameDTO {
    private String gameId;
    private String linkurl;
    private Date createDate;

    private long pv;
    private long uv;

    public EditorStatsGameDTO(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getPv() {
        return pv;
    }

    public void setPv(long pv) {
        this.pv = pv;
    }

    public long getUv() {
        return uv;
    }

    public void setUv(long uv) {
        this.uv = uv;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
