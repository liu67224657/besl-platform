package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-28
 * Time: 上午11:04
 * To change this template use File | Settings | File Templates.
 */
public class InterFlowAccount implements Serializable {

    private String interflowId;
    private String name;
    private String account;
    private String lord;
    private InterFlowType type;
    private String duty;
    private String userNumber;
    private String level;
    private String manufacturer;
    private String gameName;
    private String gameCategory;
    private String gameType;
    private String platform;
    private String theme;
    private String publishArea;
    private String lastPostDate;

    private Date createDate;
    private String createUser;
    private Date modifyDate;
    private String modifyUser;
    private ValidStatus removeStatus = ValidStatus.VALID;

    public String getInterflowId() {
        return interflowId;
    }

    public void setInterflowId(String interflowId) {
        this.interflowId = interflowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getLord() {
        return lord;
    }

    public void setLord(String lord) {
        this.lord = lord;
    }

    public InterFlowType getType() {
        return type;
    }

    public void setType(InterFlowType type) {
        this.type = type;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameCategory() {
        return gameCategory;
    }

    public void setGameCategory(String gameCategory) {
        this.gameCategory = gameCategory;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getPublishArea() {
        return publishArea;
    }

    public void setPublishArea(String publishArea) {
        this.publishArea = publishArea;
    }

    public String getLastPostDate() {
        return lastPostDate;
    }

    public void setLastPostDate(String lastPostDate) {
        this.lastPostDate = lastPostDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public ValidStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ValidStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    @Override
    public int hashCode() {
        return this.interflowId.hashCode();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
