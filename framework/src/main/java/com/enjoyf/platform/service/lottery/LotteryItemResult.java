package com.enjoyf.platform.service.lottery;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by zhimingli on 2016/6/13 0013.
 */
public class LotteryItemResult implements Serializable {
    private LotteryItemResultType lotteryItemResultType;

    private LotteryAwardItem lotteryAwardItem;

    public LotteryItemResultType getLotteryItemResultType() {
        return lotteryItemResultType;
    }

    public void setLotteryItemResultType(LotteryItemResultType lotteryItemResultType) {
        this.lotteryItemResultType = lotteryItemResultType;
    }

    public LotteryAwardItem getLotteryAwardItem() {
        return lotteryAwardItem;
    }

    public void setLotteryAwardItem(LotteryAwardItem lotteryAwardItem) {
        this.lotteryAwardItem = lotteryAwardItem;
    }

    public String toJson() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}

