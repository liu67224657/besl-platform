package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-7
 * Time: 下午1:17
 * To change this template use File | Settings | File Templates.
 */
public class PointActionHistory implements Serializable {
    private long actionHistoryId;
    private String userNo;
    private String profileId;
    private PointActionType actionType;
    private String actionDescription;
    private String destId;
    private String destUno;
    private Date createDate;
    private Date actionDate;
    private int pointValue;
    private String appkey;
    private String pointkey;
    private String logindomain;
    private int prestige;    //声望
    private int worshipNum;

    private HistoryActionType historyActionType = HistoryActionType.POINT;    //历史记录类型 0=积分操作 1=声望操作

    public long getActionHistoryId() {
        return actionHistoryId;
    }

    public void setActionHistoryId(long actionHistoryId) {
        this.actionHistoryId = actionHistoryId;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public PointActionType getActionType() {
        return actionType;
    }

    public void setActionType(PointActionType actionType) {
        this.actionType = actionType;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public int getPointValue() {
        return pointValue;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public String getDestUno() {
        return destUno;
    }

    public void setDestUno(String destUno) {
        this.destUno = destUno;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getLogindomain() {
        return logindomain;
    }

    public void setLogindomain(String logindomain) {
        this.logindomain = logindomain;
    }

    public int getPrestige() {
        return prestige;
    }

    public void setPrestige(int prestige) {
        this.prestige = prestige;
    }

    public HistoryActionType getHistoryActionType() {
        return historyActionType;
    }

    public int getWorshipNum() {
        return worshipNum;
    }

    public void setWorshipNum(int worshipNum) {
        this.worshipNum = worshipNum;
    }

    public void setHistoryActionType(HistoryActionType historyActionType) {
        this.historyActionType = historyActionType;
    }

    public String getPointkey() {
        return pointkey;
    }

    public void setPointkey(String pointkey) {
        this.pointkey = pointkey;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
