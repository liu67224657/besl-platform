package com.enjoyf.platform.service.lottery;/**
 * Created by ericliu on 16/6/20.
 */

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/6/20
 */
public class LotteryAwardRule implements Serializable {
    private List<Integer> rangeList;//中奖的code list


    //按天 按小时 自定义 无限制
    private LotteryAwardRuleCountType lotteryAwardRuleCountType;//day hour custom


    private List<LotteryAwardRuleCustom> countRule;//day-->int hour--->int


    //活动类型是【概率或时间戳】有效
    private String min;
    private String max;
    private String text;//记录填写的次数


    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LotteryAwardRuleCountType getLotteryAwardRuleCountType() {
        return lotteryAwardRuleCountType;
    }

    public void setLotteryAwardRuleCountType(LotteryAwardRuleCountType lotteryAwardRuleCountType) {
        this.lotteryAwardRuleCountType = lotteryAwardRuleCountType;
    }

    public List<LotteryAwardRuleCustom> getCountRule() {
        return countRule;
    }

    public void setCountRule(List<LotteryAwardRuleCustom> countRule) {
        this.countRule = countRule;
    }

    public List<Integer> getRangeList() {
        return rangeList;
    }

    public void setRangeList(List<Integer> rangeList) {
        this.rangeList = rangeList;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static LotteryAwardRule fromJson(String jsonStr) {
        return new Gson().fromJson(jsonStr, LotteryAwardRule.class);
    }
}
