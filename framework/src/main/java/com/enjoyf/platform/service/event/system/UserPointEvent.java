package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.point.HistoryActionType;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-7-14
 * Time: 上午10:22
 * To change this template use File | Settings | File Templates.
 */
public class UserPointEvent extends SystemEvent {

    private String uno;
    private String profileId;
    private String objectId;
    private int point = 0;
    private String description;
    private PointActionType pointActionType;
    private Date actionDate;
    private int prestige;//声望
    private HistoryActionType historyActionType = HistoryActionType.POINT;    //历史记录类型 0=积分操作 1=声望操作

    //  private String profileKey;

    private String appkey;       //added by tony 此类里面存放appkey ,在PointLogic里再把appkey转换成pointkey,此类里不再存放pointkey这个字符串  ，也不存profileKey

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public UserPointEvent() {
        super(SystemEventType.USER_POINT_INCREASE);
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PointActionType getPointActionType() {
        return pointActionType;
    }

    public void setPointActionType(PointActionType pointActionType) {
        this.pointActionType = pointActionType;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
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

    public void setHistoryActionType(HistoryActionType historyActionType) {
        this.historyActionType = historyActionType;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return profileId.hashCode();
    }
}
