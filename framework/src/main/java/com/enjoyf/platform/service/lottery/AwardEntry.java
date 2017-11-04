package com.enjoyf.platform.service.lottery;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-7-11
 * Time: 上午9:58
 * To change this template use File | Settings | File Templates.
 */
public class AwardEntry implements Serializable {
    private LotteryAwardItem awardItem;
    private LotteryAward award;
    private UserDayLottery dayLottery;
    private UserLotteryLog lotteryLog;
    private int increaseAwardRest;
    private String updateAwardItemStatus;

    public AwardEntry() {
    }

    public AwardEntry(LotteryAwardItem awardItem, LotteryAward award) {
        this.awardItem = awardItem;
        this.award = award;
    }

    public LotteryAwardItem getAwardItem() {
        return awardItem;
    }

    public void setAwardItem(LotteryAwardItem awardItem) {
        this.awardItem = awardItem;
    }

    public LotteryAward getAward() {
        return award;
    }

    public void setAward(LotteryAward award) {
        this.award = award;
    }

    public UserDayLottery getDayLottery() {
        return dayLottery;
    }

    public void setDayLottery(UserDayLottery dayLottery) {
        this.dayLottery = dayLottery;
    }

    public UserLotteryLog getLotteryLog() {
        return lotteryLog;
    }

    public void setLotteryLog(UserLotteryLog lotteryLog) {
        this.lotteryLog = lotteryLog;
    }

    public int getIncreaseAwardRest() {
        return increaseAwardRest;
    }

    public void setIncreaseAwardRest(int increaseAwardRest) {
        this.increaseAwardRest = increaseAwardRest;
    }

    public String getUpdateAwardItemStatus() {
        return updateAwardItemStatus;
    }

    public void setUpdateAwardItemStatus(String updateAwardItemStatus) {
        this.updateAwardItemStatus = updateAwardItemStatus;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
