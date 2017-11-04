package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: liangtang
 * Date: 13-5-29
 * Time: 下午1:51
 * To change this template use File | Settings | File Templates.
 */
public class UserPoint implements Serializable{
    private long userPointId;
    private String userNo;
    private int userPoint;
    private int consumeAmount;
    private int consumeExchange;
    private int extInt1;
    private int extInt2;
    private String profileId;
    private String pointKey;
    private int prestige=0;//声望

    private int worshipNum;//被膜拜次数  存在redis


    public long getUserPointId() {
        return userPointId;
    }

    public void setUserPointId(long userPointId) {
        this.userPointId = userPointId;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
       this.userNo = userNo;
    }

    public int getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(int userPoint) {
        this.userPoint = userPoint;
    }

    public int getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(int consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    public int getConsumeExchange() {
        return consumeExchange;
    }

    public void setConsumeExchange(int consumeExchange) {
        this.consumeExchange = consumeExchange;
    }

    public int getExtInt1() {
        return extInt1;
    }

    public void setExtInt1(int extInt1) {
        this.extInt1 = extInt1;
    }

    public int getExtInt2() {
        return extInt2;
    }

    public void setExtInt2(int extInt2) {
        this.extInt2 = extInt2;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getPointKey() {
        return pointKey;
    }

    public void setPointKey(String pointKey) {
        this.pointKey = pointKey;
    }

    public int getPrestige() {
        return prestige;
    }

    public void setPrestige(int prestige) {
        this.prestige = prestige;
    }

    public int getWorshipNum() {
        return worshipNum;
    }

    public void setWorshipNum(int worshipNum) {
        this.worshipNum = worshipNum;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
