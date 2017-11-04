package com.enjoyf.platform.cloud.contentservice.game;

import com.enjoyf.platform.cloud.contentservice.game.enumeration.GameOperStatus;
import com.enjoyf.platform.cloud.contentservice.game.enumeration.GameType;
import com.enjoyf.platform.cloud.enumeration.ValidStatus;

import java.io.Serializable;
import java.util.Date;

public class Game implements Serializable {

    private Long id;
    private String name;//游戏名称
    private String aliasName;//游戏别名
    private String gameTag;//游戏标签
    private GameType gameType;//游戏类型
    private GameOperStatus operStatus = GameOperStatus.UNKNOWN;//运营状态
    private GameExtJson extJson;
    private ValidStatus validStatus = ValidStatus.VALID;
    private Date createTime;
    private boolean android = false; //
    private boolean ios = false; //
    private boolean pc = false; //

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Game name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getGameTag() {
        return gameTag;
    }

    public void setGameTag(String gameTag) {
        this.gameTag = gameTag;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public GameOperStatus getOperStatus() {
        return operStatus;
    }

    public void setOperStatus(GameOperStatus operStatus) {
        this.operStatus = operStatus;
    }

    public GameExtJson getExtJson() {
        return extJson;
    }

    public void setExtJson(GameExtJson extJson) {
        this.extJson = extJson;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isAndroid() {
        return android;
    }

    public void setAndroid(boolean android) {
        this.android = android;
    }

    public boolean isIos() {
        return ios;
    }

    public void setIos(boolean ios) {
        this.ios = ios;
    }

    public boolean isPc() {
        return pc;
    }

    public void setPc(boolean pc) {
        this.pc = pc;
    }



    @Override
    public String toString() {
        return "Game{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", aliasName='" + getAliasName() + "'" +
                ", gameTag='" + getGameTag() + "'" +
                ", extJson='" + getExtJson() + "'" +
                ", validStatus='" + getValidStatus() + "'" +
                ", createTime='" + getCreateTime() + "'" +
                "}";
    }
}
