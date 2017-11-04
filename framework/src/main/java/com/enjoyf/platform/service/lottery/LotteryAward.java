package com.enjoyf.platform.service.lottery;

import com.enjoyf.platform.service.ValidStatus;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-24
 * Time: 上午10:35
 * To change this template use File | Settings | File Templates.
 */
public class LotteryAward implements Serializable {
    private long lotteryAwardId; //奖品id
    private long lotteryId;    //活动id
    private int lotteryAwardLevel;//几等奖

    private String lotteryAwardName;//奖品名称
    private String lotteryAwardDesc;//奖品描述

    @Deprecated
    private String lotteryAwardPic;//奖品图片

    private int lotteryAwardAmount;//奖品总数

    @Deprecated
    private int lotteryAwardRestAmount;//奖品剩余总数

    @Deprecated
    private int lotteryAwardMinRate;
    @Deprecated
    private int lotteryAwardMaxRate;


    @Deprecated
    private Set<Integer> lotteryCode;//not use

    private Date createDate;//活动开始时间
    private String createIp;

    private Date lastModifyDate;//活动结束时间
    private String lastModifyIp;

    private LotteryAwardType lotteryAwardType;
    private ValidStatus validStatus;

    private LotteryAwardRule awardRule;

    public long getLotteryAwardId() {
        return lotteryAwardId;
    }

    public void setLotteryAwardId(long lotteryAwardId) {
        this.lotteryAwardId = lotteryAwardId;
    }

    public long getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(long lotteryId) {
        this.lotteryId = lotteryId;
    }

    public int getLotteryAwardLevel() {
        return lotteryAwardLevel;
    }

    public void setLotteryAwardLevel(int lotteryAwardLevel) {
        this.lotteryAwardLevel = lotteryAwardLevel;
    }

    public String getLotteryAwardName() {
        return lotteryAwardName;
    }

    public void setLotteryAwardName(String lotteryAwardName) {
        this.lotteryAwardName = lotteryAwardName;
    }

    public String getLotteryAwardDesc() {
        return lotteryAwardDesc;
    }

    public void setLotteryAwardDesc(String lotteryAwardDesc) {
        this.lotteryAwardDesc = lotteryAwardDesc;
    }

    public String getLotteryAwardPic() {
        return lotteryAwardPic;
    }

    public void setLotteryAwardPic(String lotteryAwardPic) {
        this.lotteryAwardPic = lotteryAwardPic;
    }

    public int getLotteryAwardAmount() {
        return lotteryAwardAmount;
    }

    public void setLotteryAwardAmount(int lotteryAwardAmount) {
        this.lotteryAwardAmount = lotteryAwardAmount;
    }

    public int getLotteryAwardRestAmount() {
        return lotteryAwardRestAmount;
    }

    public void setLotteryAwardRestAmount(int lotteryAwardRestAmount) {
        this.lotteryAwardRestAmount = lotteryAwardRestAmount;
    }

    public int getLotteryAwardMinRate() {
        return lotteryAwardMinRate;
    }

    public void setLotteryAwardMinRate(int lotteryAwardMinRate) {
        this.lotteryAwardMinRate = lotteryAwardMinRate;
    }

    public int getLotteryAwardMaxRate() {
        return lotteryAwardMaxRate;
    }

    public void setLotteryAwardMaxRate(int lotteryAwardMaxRate) {
        this.lotteryAwardMaxRate = lotteryAwardMaxRate;
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

    public LotteryAwardType getLotteryAwardType() {
        return lotteryAwardType;
    }

    public void setLotteryAwardType(LotteryAwardType lotteryAwardType) {
        this.lotteryAwardType = lotteryAwardType;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public Set<Integer> getLotteryCode() {
        return lotteryCode;
    }

    public void setLotteryCode(Set<Integer> lotteryCode) {
        this.lotteryCode = lotteryCode;
    }

    public LotteryAwardRule getAwardRule() {
        return awardRule;
    }

    public void setAwardRule(LotteryAwardRule awardRule) {
        this.awardRule = awardRule;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
