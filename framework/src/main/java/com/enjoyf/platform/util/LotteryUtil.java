package com.enjoyf.platform.util;

import com.enjoyf.platform.service.point.GiftLottery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pengxu on 2016/11/30.
 */
public class LotteryUtil {
    /**
     * 抽奖
     *
     * @param giftLotteries 原始的概率列表，保证顺序和实际物品对应
     * @return
     */

    public static GiftLottery lottery(List<GiftLottery> giftLotteries) {
        if (CollectionUtil.isEmpty(giftLotteries)) {
            return null;
        }

        // 计算总概率;
        double sumRate = 0d;
        for (GiftLottery giftLottery : giftLotteries) {
            sumRate += giftLottery.getProbability();
        }

        // 计算每个物品在总概率的基础下的概率情况
        List<Double> sortOrignalRates = new ArrayList<Double>();
        Double tempSumRate = 0d;
        for (GiftLottery giftLottery : giftLotteries) {
            tempSumRate += giftLottery.getProbability();
            sortOrignalRates.add(tempSumRate / sumRate);
        }

        // 根据区块值来获取抽取到的物品索引
        double nextDouble = Math.random();
        sortOrignalRates.add(nextDouble);
        Collections.sort(sortOrignalRates);

        int index = sortOrignalRates.indexOf(nextDouble);
        return giftLotteries.get(index);
    }
}
