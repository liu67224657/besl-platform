package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-13
 * Time: 上午10:35
 * To change this template use File | Settings | File Templates.
 */
public class UserDayPoint implements Serializable {

    private long userDayPointId;
    private PointActionType actionType;
    private String userNo;
    private int pointValue;
    private Date pointDate;

    private String profileId;

    public long getUserDayPointId() {
        return userDayPointId;
    }

    public void setUserDayPointId(long userDayPointId) {
        this.userDayPointId = userDayPointId;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public int getPointValue() {
        return pointValue;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    public Date getPointDate() {
        return pointDate;
    }

    public void setPointDate(Date pointDate) {
        this.pointDate = pointDate;
    }

    public PointActionType getActionType() {
        return actionType;
    }

    public void setActionType(PointActionType actionType) {
        this.actionType = actionType;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
