package com.enjoyf.platform.service.lottery;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhimingli on 2016/6/29 0029.
 */
public class LotteryAwardRuleCustom implements Serializable {
    private Date startTime;
    private Date endTime;
    private int times;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static LotteryAwardRuleCustom fromJson(String jsonStr) {
        return new Gson().fromJson(jsonStr, LotteryAwardRuleCustom.class);
    }
}
