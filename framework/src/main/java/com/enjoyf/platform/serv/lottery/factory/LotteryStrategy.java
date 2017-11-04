package com.enjoyf.platform.serv.lottery.factory;

import com.enjoyf.platform.service.lottery.Lottery;
import com.enjoyf.platform.service.lottery.LotteryAwardItem;
import com.enjoyf.platform.service.service.ServiceException;

import java.util.Date;

/**
 * Created by zhimingli on 2016/6/30 0030.  lotteryStragy
 */
public interface LotteryStrategy {

    /**
     * 抽奖的逻辑
     * @param lottery
     * @param profileId
     * @param date
     * @param ip
     * @return
     * @throws ServiceException
     */
    LotteryAwardItem lottery(Lottery lottery, String profileId, Date date, String ip) throws ServiceException;


    /**
     * 是否有机会
     * @param lottery
     * @param curDate
     * @param profileId
     * @return
     * @throws ServiceException
     */
    boolean hasUserLotteryChance(Lottery lottery, Date curDate, String profileId) throws ServiceException;


    /**
     * 增加抽奖机会
     * @param lottery
     * @param date
     * @param profileid
     * @param value
     */
    void incrUserLotteryChance(Lottery lottery, Date date, String profileid, int value);
}
