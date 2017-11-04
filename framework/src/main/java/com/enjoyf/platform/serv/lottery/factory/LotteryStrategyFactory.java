package com.enjoyf.platform.serv.lottery.factory;

import com.enjoyf.platform.serv.lottery.LotteryLogic;
import com.enjoyf.platform.service.lottery.LotteryRuleActionType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2016/6/29 0029.
 */
public class LotteryStrategyFactory {
    private static LotteryStrategyFactory instance;
    private static Map<Integer, AbstractLotteryStrategy> map = new HashMap<Integer, AbstractLotteryStrategy>();

    //在get中传递logic和redis是否更好一些,factory可以只穿戴rule
    public static LotteryStrategyFactory get() {
        if (instance == null) {
            synchronized (LotteryStrategyFactory.class) {
                if (instance == null) {
                    instance = new LotteryStrategyFactory();
                }
            }
        }
        return instance;
    }

    //todo 命名
    public AbstractLotteryStrategy factory(LotteryLogic lotteryLogic, LotteryAwardRedis lotteryAwardRedis, LotteryRuleActionType lotteryRuleActionType) {
        if (lotteryRuleActionType == null) {
            return null;
        }
        AbstractLotteryStrategy returnProcessor = map.containsKey(lotteryRuleActionType.getCode()) ? map.get(lotteryRuleActionType.getCode()) : null;
        if (returnProcessor == null) {
            synchronized (map) {
                if (LotteryRuleActionType.RANDOM.equals(lotteryRuleActionType)) {//随机
                    returnProcessor = new RandomLotteryStrategy(lotteryLogic, lotteryAwardRedis);
                } else if (LotteryRuleActionType.TIMESTAMP.equals(lotteryRuleActionType)) {//时间戳
                    returnProcessor = new TimestampLotteryStrategy(lotteryLogic, lotteryAwardRedis);
                } else if (LotteryRuleActionType.TIMES.equals(lotteryRuleActionType)) {//次数
                    returnProcessor = new TimesLotteryStrategy(lotteryLogic, lotteryAwardRedis);
                }
            }
            if (returnProcessor != null) {
                map.put(lotteryRuleActionType.getCode(), returnProcessor);
            }
        }

        return returnProcessor;
    }
}
