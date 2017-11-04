package com.enjoyf.platform.service.lottery;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-24
 * Time: 上午10:35
 * To change this template use File | Settings | File Templates.
 */
public class LotteryAwardItem implements Serializable {

    private long lotteryAwardItemId;
    private long lotteryAwardId;

    private String name1;
    private String value1;
    private String name2;
    private String value2;
    private String name3;
    private String value3;

    private ValidStatus lotteryStatus = ValidStatus.VALID;
    private String ownUserNo;
    private Date lotteryDate;
    private Date createDate;
    private long lotteryId;

    public long getLotteryAwardItemId() {
        return lotteryAwardItemId;
    }

    public void setLotteryAwardItemId(long lotteryAwardItemId) {
        this.lotteryAwardItemId = lotteryAwardItemId;
    }

    public long getLotteryAwardId() {
        return lotteryAwardId;
    }

    public void setLotteryAwardId(long lotteryAwardId) {
        this.lotteryAwardId = lotteryAwardId;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public ValidStatus getLotteryStatus() {
        return lotteryStatus;
    }

    public void setLotteryStatus(ValidStatus lotteryStatus) {
        this.lotteryStatus = lotteryStatus;
    }

    public String getOwnUserNo() {
        return ownUserNo;
    }

    public void setOwnUserNo(String ownUserNo) {
        this.ownUserNo = ownUserNo;
    }

    public Date getLotteryDate() {
        return lotteryDate;
    }

    public void setLotteryDate(Date lotteryDate) {
        this.lotteryDate = lotteryDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(long lotteryId) {
        this.lotteryId = lotteryId;
    }

    @Override
    public String toString() {
        return "LotteryAwardItem{" +
                "lotteryAwardItemId=" + lotteryAwardItemId +
                ", lotteryAwardId=" + lotteryAwardId +
                ", name1='" + name1 + '\'' +
                ", value1='" + value1 + '\'' +
                ", name2='" + name2 + '\'' +
                ", value2='" + value2 + '\'' +
                ", name3='" + name3 + '\'' +
                ", value3='" + value3 + '\'' +
                ", lotteryStatus=" + lotteryStatus +
                ", ownUserNo='" + ownUserNo + '\'' +
                ", lotteryDate=" + lotteryDate +
                ", createDate=" + createDate +
                ", lotteryId=" + lotteryId +
                '}';
    }
}
