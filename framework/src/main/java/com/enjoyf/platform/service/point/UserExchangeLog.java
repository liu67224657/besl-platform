package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-18
 * Time: 下午3:30
 * To change this template use File | Settings | File Templates.
 */
public class UserExchangeLog implements Serializable {
    private long userExchangeLogId;
    private String userNo;
    private long goodsId;
    private String goodsName;
    private String goodsPic;
    private String goodsDesc;
    private GoodsType goodsType;

    private long goodsItemId;
    private String snName1;
    private String snValue1;
    private String snName2;
    private String snValue2;
    private UserExchangeType exchangeType = UserExchangeType.GET_CODE;

    private Date exchangeDate;
    private Date exhangeTime;
    private String exchangeIp;
    private int exchangePoint;
    private String profileId;
    private String appkey;
    private UserExchangeDomain exchangeDomain;

    private int smsCount;

    public long getUserExchangeLogId() {
        return userExchangeLogId;
    }

    public void setUserExchangeLogId(long userExchangeLogId) {
        this.userExchangeLogId = userExchangeLogId;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public long getGoodsItemId() {
        return goodsItemId;
    }

    public void setGoodsItemId(long goodsItemId) {
        this.goodsItemId = goodsItemId;
    }

    public GoodsType getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(GoodsType goodsType) {
        this.goodsType = goodsType;
    }

    public Date getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(Date exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public Date getExhangeTime() {
        return exhangeTime;
    }

    public void setExhangeTime(Date exhangeTime) {
        this.exhangeTime = exhangeTime;
    }

    public String getExchangeIp() {
        return exchangeIp;
    }

    public void setExchangeIp(String exchangeIp) {
        this.exchangeIp = exchangeIp;
    }

    public UserExchangeType getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(UserExchangeType exchangeType) {
        this.exchangeType = exchangeType;
    }

    public String getSnName1() {
        return snName1;
    }

    public void setSnName1(String snName1) {
        this.snName1 = snName1;
    }

    public String getSnValue1() {
        return snValue1;
    }

    public void setSnValue1(String snValue1) {
        this.snValue1 = snValue1;
    }

    public String getSnName2() {
        return snName2;
    }

    public void setSnName2(String snName2) {
        this.snName2 = snName2;
    }

    public String getSnValue2() {
        return snValue2;
    }

    public void setSnValue2(String snValue2) {
        this.snValue2 = snValue2;
    }

    public UserExchangeDomain getExchangeDomain() {
        return exchangeDomain;
    }

    public void setExchangeDomain(UserExchangeDomain exchangeDomain) {
        this.exchangeDomain = exchangeDomain;
    }

    public int getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(int smsCount) {
        this.smsCount = smsCount;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public int getExchangePoint() {
        return exchangePoint;
    }

    public void setExchangePoint(int exchangePoint) {
        this.exchangePoint = exchangePoint;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
