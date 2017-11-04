package com.enjoyf.platform.service.lottery;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-25
 * Time: 上午9:14
 * To change this template use File | Settings | File Templates.
 */
public class UserDayLottery implements Serializable{

    private long userDayLotteryId;
    private String userNo;
    private long lotteryId;
    private int lotteryTimes;
    private Date lotteryDate;

    public long getUserDayLotteryId() {
        return userDayLotteryId;
    }

    public void setUserDayLotteryId(long userDayLotteryId) {
        this.userDayLotteryId = userDayLotteryId;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public long getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(long lotteryId) {
        this.lotteryId = lotteryId;
    }

    public int getLotteryTimes() {
        return lotteryTimes;
    }

    public void setLotteryTimes(int lotteryTimes) {
        this.lotteryTimes = lotteryTimes;
    }

    public Date getLotteryDate() {
        return lotteryDate;
    }

    public void setLotteryDate(Date lotteryDate) {
        this.lotteryDate = lotteryDate;
    }
}
