package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-4-18
 * Time: 上午9:16
 * To change this template use File | Settings | File Templates.
 */
public class ProfilePlayingGames implements Serializable{

    private long primaryKeyId;
    private String uno;
    private long gameId;
    private String description;
    private Date createDate;
    private Date modifyDate;
    private ActStatus actStatus = ActStatus.UNACT;

    public long getPrimaryKeyId() {
        return primaryKeyId;
    }

    public void setPrimaryKeyId(long primaryKeyId) {
        this.primaryKeyId = primaryKeyId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public ActStatus getActStatus() {
        return actStatus;
    }

    public void setActStatus(ActStatus actStatus) {
        this.actStatus = actStatus;
    }

    @Override
    public int hashCode() {
        return (int) primaryKeyId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
