package com.enjoyf.platform.service.lottery;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-24
 * Time: 上午11:48
 * To change this template use File | Settings | File Templates.
 */
public interface LotteryService {

    public Lottery createLottery(Lottery lottery) throws ServiceException;

    public List<Lottery> queryLottery(QueryExpress queryExpress) throws ServiceException;

    public PageRows<Lottery> queryLotteryByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public Lottery getLotteryById(long lotteryId) throws ServiceException;

    public boolean modifyLotteryById(UpdateExpress updateExpress, long lotteryId) throws ServiceException;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public LotteryAward createLotteryAward(LotteryAward lotteryAward) throws ServiceException;

    public List<LotteryAward> queryLotteryAward(QueryExpress queryExpress) throws ServiceException;

    public PageRows<LotteryAward> queryLotteryAwardByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public LotteryAward getLotteryAwardById(long lotteryAwardId) throws ServiceException;

    public LotteryAward getLotteryAwardByRate(long lotteryId, int randomNum) throws ServiceException;

    public LotteryAward getLotteryAwardByLevel(int awardLevel, long lotteryId) throws ServiceException;

    public boolean modifyLotteryAwardById(UpdateExpress updateExpress, long lotteryAwardId) throws ServiceException;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 异步增加奖品只有全部加入才能改为可用状态
     *
     * @param awardId
     * @param lotteryAwardItemList
     * @return
     * @throws ServiceException
     */
    public boolean createLotteryAwardItem(long awardId, List<LotteryAwardItem> lotteryAwardItemList) throws ServiceException;

    public PageRows<LotteryAwardItem> queryByLotteryAwardIdPage(long lotteryAwardId, Pagination pagination) throws ServiceException;

    public List<LotteryAwardItem> queryLotteryAwardItem(QueryExpress queryExpress) throws ServiceException;

    public LotteryAwardItem getLotteryAwardItemById(long LotteryAwardItemId) throws ServiceException;

    public boolean modifyLotteryAwardItemById(UpdateExpress updateExpress, long LotteryAwardItemId) throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public PageRows<UserDayLottery> queryUserDayLottery(long lotteryId, Date date, Pagination pagination) throws ServiceException;

    public UserDayLottery getUserDayLottery(long lotteryId, String uno, Date date) throws ServiceException;


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public UserLotteryLog createUserLotteryLog(UserLotteryLog userLotteryLog) throws ServiceException;

    public PageRows<UserLotteryLog> queryUserLotteryLogByPage(long lotteryId, String uno, Date from, Date to, Pagination pagination) throws ServiceException;

    public List<UserLotteryLog> queryUserLotteryLog(long lotteryId, String uno) throws ServiceException;

    public UserLotteryLog getUserLotteryLog(long lotteryId, String uno) throws ServiceException;

    /**
     * 查询最近中奖记录
     *
     * @param lotteryid
     * @return
     * @throws ServiceException
     */
    public List<UserLotteryLog> queryLastestUserLotteryLog(long lotteryid) throws ServiceException;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public AwardEntry userLotteryAwardEntry(String uno, String screenName, String ip, Date lotteryDate, long lotteryId, AwardEntry awardEntry, int baseRate, LotteryType lotteryType) throws ServiceException;

    public UserLotteryLog userLotteryAward(String uno, String screenName, String ip, Date lotteryDate, long lotteryId) throws ServiceException;

    ////////////////////////////////////////////////////////////////////////
//    public List<Ticket> queryTicketList(QueryExpress queryExpress) throws ServiceException;
//
//    public PageRows<Ticket> queryTicketPageRows(QueryExpress queryExpress, Pagination pagination) throws ServiceException;
//
//    public boolean modifyTicket(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;
//
//    public Ticket createTicket(Ticket ticket) throws ServiceException;
//
//    public Ticket getTicketById(QueryExpress queryExpress) throws ServiceException;
//
//    public List<TicketAward> queryTicketAwardList(QueryExpress queryExpress) throws ServiceException;
//
//    public PageRows<TicketAward> queryTicketAwardPageRows(QueryExpress queryExpress, Pagination pagination) throws ServiceException;
//
//    public TicketAward getTicketAwardById(QueryExpress queryExpress) throws ServiceException;
//
//    public boolean modifyTicketAward(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;
//
//    public TicketAward createTicketAward(TicketAward ticketAward) throws ServiceException;

    public List<LotteryAward> queryLotteryAwardByCache(long lotteryId) throws ServiceException;

    public LotteryAward userLottery(Long lotteryId, String clientid, String ip, Profile profile, String code) throws ServiceException;

    public boolean setUserLotteryShareTime(String clientid, String date_short) throws ServiceException;

    public boolean addOrIncrTime(String key) throws ServiceException;

    public void putShuang12Cache(Profile profile, String words) throws ServiceException;

    public PageRows<String> getShuang12Cache(Pagination pagination) throws ServiceException;

    public void incrShuang12ShareTime(Profile profile) throws ServiceException;


    /**
     * @param lotteryId
     * @param profileId
     * @param ip
     * @param date
     * @param nick
     * @return
     * @throws ServiceException
     */
    public LotteryAwardItem lottery(long lotteryId, String profileId, String ip, Date date, String nick) throws ServiceException;

    public boolean addChance(long lotteryId, String profileId, int value, Date date) throws ServiceException;


    public boolean modifyLotteryAddressByUid(LotteryAddress lotteryAddress, String profileid) throws ServiceException;


    public LotteryItemResult takelottery(long lotteryId, String profileId, String ip, Date date, String nick) throws ServiceException;


    public boolean addLotteryChance(long lotteryId, String profileId, int value, Date date) throws ServiceException;
}
