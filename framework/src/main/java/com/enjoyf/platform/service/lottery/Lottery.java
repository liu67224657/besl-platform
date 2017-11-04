package com.enjoyf.platform.service.lottery;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-24
 * Time: 上午10:35
 * To change this template use File | Settings | File Templates.
 */
public class Lottery implements Serializable {
    private long lotteryId;      //活动ID
    private String lotteryName;  //活动名称
    private String lotteryDesc;  //活动描述

    private int baseRate;//1000/10000/100000  //抽奖基数
    private int awardLevelCount;  //总共几等奖


    private ValidStatus validStatus = ValidStatus.INVALID; //是否有效

    private Date createDate; //创建日期
    private String createIp; //创建ip
    private Date lastModifyDate;//修改日期
    private String lastModifyIp;//修改ip

    @Deprecated
    private LotteryTimesType lotteryTimesType = LotteryTimesType.ONLY_ONE;
    @Deprecated
    private LotteryType lotteryType = LotteryType.LOTTERY_TYPE_COMMON;
    @Deprecated
    private long shareId;


    private Date startDate;//活动开始时间
    private Date endDate;//活动结束时间

    private LotteryRule lotteryRule;


    public long getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(long lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getLotteryName() {
        return lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public String getLotteryDesc() {
        return lotteryDesc;
    }

    public void setLotteryDesc(String lotteryDesc) {
        this.lotteryDesc = lotteryDesc;
    }

    public int getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(int baseRate) {
        this.baseRate = baseRate;
    }

    public int getAwardLevelCount() {
        return awardLevelCount;
    }

    public void setAwardLevelCount(int awardLevelCount) {
        this.awardLevelCount = awardLevelCount;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getLastModifyIp() {
        return lastModifyIp;
    }

    public void setLastModifyIp(String lastModifyIp) {
        this.lastModifyIp = lastModifyIp;
    }

    public LotteryTimesType getLotteryTimesType() {
        return lotteryTimesType;
    }

    public void setLotteryTimesType(LotteryTimesType lotteryTimesType) {
        this.lotteryTimesType = lotteryTimesType;
    }

    @Deprecated
    public long getShareId() {
        return shareId;
    }

    @Deprecated
    public void setShareId(long shareId) {
        this.shareId = shareId;
    }

    public LotteryType getLotteryType() {
        return lotteryType;
    }

    public void setLotteryType(LotteryType lotteryType) {
        this.lotteryType = lotteryType;
    }

    public LotteryRule getLotteryRule() {
        return lotteryRule;
    }

    public void setLotteryRule(LotteryRule lotteryRule) {
        this.lotteryRule = lotteryRule;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
