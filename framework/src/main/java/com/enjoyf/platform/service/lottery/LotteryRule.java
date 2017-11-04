package com.enjoyf.platform.service.lottery;/**
 * Created by ericliu on 16/6/20.
 */

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Map;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/6/20
 */
public class LotteryRule implements Serializable {
    private LotteryRuleChanceType ruleChanceType;
    private int maxChance;//最大机会


    private int initChance;//每个人初始的机会
    private LotteryRuleActionType lotteryRuleActionType;//code timestamp times

    public LotteryRuleChanceType getRuleChanceType() {
        return ruleChanceType;
    }

    public void setRuleChanceType(LotteryRuleChanceType ruleChanceType) {
        this.ruleChanceType = ruleChanceType;
    }

    public int getMaxChance() {
        return maxChance;
    }

    public void setMaxChance(int maxChance) {
        this.maxChance = maxChance;
    }

    public LotteryRuleActionType getLotteryRuleActionType() {
        return lotteryRuleActionType;
    }

    public void setLotteryRuleActionType(LotteryRuleActionType lotteryRuleActionType) {
        this.lotteryRuleActionType = lotteryRuleActionType;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public int getInitChance() {
        return initChance;
    }

    public void setInitChance(int initChance) {
        this.initChance = initChance;
    }

    public static LotteryRule fromJson(String jsonStr) {
        return new Gson().fromJson(jsonStr, LotteryRule.class);
    }


}
