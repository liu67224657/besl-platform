package com.enjoyf.platform.service.lottery;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-18
 * Time: 下午3:30
 * To change this template use File | Settings | File Templates.
 */
public class UserLotteryProperty implements Serializable {
    private String uno;
    private long lotteryId;

    private int drawLotteryTimes;

    private Date lotteryDate;
    private Date lotteryTime;

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public long getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(long lotteryId) {
        this.lotteryId = lotteryId;
    }

    public int getDrawLotteryTimes() {
        return drawLotteryTimes;
    }

    public void setDrawLotteryTimes(int drawLotteryTimes) {
        this.drawLotteryTimes = drawLotteryTimes;
    }

    public Date getLotteryDate() {
        return lotteryDate;
    }

    public void setLotteryDate(Date lotteryDate) {
        this.lotteryDate = lotteryDate;
    }

    public Date getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(Date lotteryTime) {
        this.lotteryTime = lotteryTime;
    }
}
