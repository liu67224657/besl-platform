package com.enjoyf.platform.service.lottery;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-24
 * Time: 上午11:48
 * To change this template use File | Settings | File Templates.
 */
public class LotteryServiceImpl implements LotteryService {

    private ReqProcessor reqProcessor = null;

    public LotteryServiceImpl(ServiceConfigNaming scfg) {
        if (scfg == null) {
            throw new RuntimeException("ContentServiceImpl.ctor: ServiceConfig is null!");
        }

        //
        reqProcessor = scfg.getReqProcessor();
    }

    @Override
    public Lottery createLottery(Lottery lottery) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(lottery);

        Request req = new Request(LotteryConstants.CREATE_LOTTERY, wp);

        RPacket rp = reqProcessor.process(req);

        return (Lottery) rp.readSerializable();
    }

    @Override
    public List<Lottery> queryLottery(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request req = new Request(LotteryConstants.QUERY_LOTTERY, wp);

        RPacket rp = reqProcessor.process(req);

        return (List<Lottery>) rp.readSerializable();
    }

    @Override
    public PageRows<Lottery> queryLotteryByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);

        Request req = new Request(LotteryConstants.QUERY_LOTTERY_BY_PAGE, wp);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<Lottery>) rp.readSerializable();
    }

    @Override
    public Lottery getLotteryById(long lotteryId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(lotteryId);

        Request req = new Request(LotteryConstants.GET_LOTTERY_BY_ID, wp);

        RPacket rp = reqProcessor.process(req);

        return (Lottery) rp.readSerializable();
    }

    @Override
    public boolean modifyLotteryById(UpdateExpress updateExpress, long lotteryId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeLongNx(lotteryId);

        Request req = new Request(LotteryConstants.MODIFY_LOTTERY_BY_ID, wp);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public LotteryAward createLotteryAward(LotteryAward lotteryAward) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(lotteryAward);

        Request req = new Request(LotteryConstants.CREATE_LOTTERY_AWARD, wp);

        RPacket rp = reqProcessor.process(req);

        return (LotteryAward) rp.readSerializable();
    }

    @Override
    public List<LotteryAward> queryLotteryAward(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request req = new Request(LotteryConstants.QUERY_LOTTERY_AWARD, wp);

        RPacket rp = reqProcessor.process(req);

        return (List<LotteryAward>) rp.readSerializable();
    }

    @Override
    public PageRows<LotteryAward> queryLotteryAwardByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);

        Request req = new Request(LotteryConstants.QUERY_LOTTERY_AWARD_BY_PAGE, wp);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<LotteryAward>) rp.readSerializable();
    }

    @Override
    public LotteryAward getLotteryAwardById(long lotteryAwardId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(lotteryAwardId);

        Request req = new Request(LotteryConstants.GET_LOTTERY_AWARD_BY_ID, wp);

        RPacket rp = reqProcessor.process(req);

        return (LotteryAward) rp.readSerializable();
    }

    @Override
    public LotteryAward getLotteryAwardByRate(long lotteryId, int randomNum) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(lotteryId);
        wp.writeIntNx(randomNum);

        Request req = new Request(LotteryConstants.GET_LOTTERY_AWARD_BY_RATE, wp);

        RPacket rp = reqProcessor.process(req);

        return (LotteryAward) rp.readSerializable();
    }

    @Override
    public LotteryAward getLotteryAwardByLevel(int awardLevel, long lotteryId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeIntNx(awardLevel);
        wp.writeLongNx(lotteryId);

        Request req = new Request(LotteryConstants.GET_LOTTERY_AWARD_BY_LEVEL, wp);

        RPacket rp = reqProcessor.process(req);

        return (LotteryAward) rp.readSerializable();
    }

    @Override
    public boolean modifyLotteryAwardById(UpdateExpress updateExpress, long lotteryAwardId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeLongNx(lotteryAwardId);

        Request req = new Request(LotteryConstants.MODIFY_LOTTERY_AWARD_BY_ID, wp);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean createLotteryAwardItem(long awardId, List<LotteryAwardItem> lotteryAwardItemList) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(awardId);
        wp.writeSerializable((Serializable) lotteryAwardItemList);

        Request req = new Request(LotteryConstants.CREATE_LOTTERY_AWARD_ITEM, wp);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public PageRows<LotteryAwardItem> queryByLotteryAwardIdPage(long lotteryAwardId, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(lotteryAwardId);
        wp.writeSerializable(pagination);

        Request req = new Request(LotteryConstants.QUERY_LOTTERY_AWARD_ITEM_BY_PAGE, wp);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<LotteryAwardItem>) rp.readSerializable();
    }

    @Override
    public List<LotteryAwardItem> queryLotteryAwardItem(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request req = new Request(LotteryConstants.QUERY_LOTTERY_AWARD_ITEM, wp);

        RPacket rp = reqProcessor.process(req);

        return (List<LotteryAwardItem>) rp.readSerializable();
    }

    @Override
    public LotteryAwardItem getLotteryAwardItemById(long LotteryAwardItemId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(LotteryAwardItemId);

        Request req = new Request(LotteryConstants.GET_LOTTERY_AWARD_ITEM_BY_ID, wp);

        RPacket rp = reqProcessor.process(req);

        return (LotteryAwardItem) rp.readSerializable();
    }

    @Override
    public boolean modifyLotteryAwardItemById(UpdateExpress updateExpress, long LotteryAwardItemId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeLongNx(LotteryAwardItemId);

        Request req = new Request(LotteryConstants.MODIFY_LOTTERY_AWARD_ITEM_BY_ID, wp);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public PageRows<UserDayLottery> queryUserDayLottery(long lotteryId, Date date, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(lotteryId);
        wp.writeSerializable(date);
        wp.writeSerializable(pagination);

        Request req = new Request(LotteryConstants.QUERY_USER_DAY_LOTTERY, wp);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<UserDayLottery>) rp.readSerializable();
    }

    @Override
    public UserDayLottery getUserDayLottery(long lotteryId, String uno, Date date) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(lotteryId);
        wp.writeStringUTF(uno);
        wp.writeSerializable(date);

        Request req = new Request(LotteryConstants.GET_USER_DAY_LOTTERY, wp);

        RPacket rp = reqProcessor.process(req);

        return (UserDayLottery) rp.readSerializable();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public UserLotteryLog createUserLotteryLog(UserLotteryLog userLotteryLog) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(userLotteryLog);

        Request req = new Request(LotteryConstants.CREATE_USER_LOTTERY_LOG, wp);

        RPacket rp = reqProcessor.process(req);

        return (UserLotteryLog) rp.readSerializable();
    }

    @Override
    public PageRows<UserLotteryLog> queryUserLotteryLogByPage(long lotteryId, String uno, Date from, Date to, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(lotteryId);
        wp.writeStringUTF(uno);
        wp.writeSerializable(from);
        wp.writeSerializable(to);
        wp.writeSerializable(pagination);

        Request req = new Request(LotteryConstants.QUERY_USER_LOTTERY_LOG_BY_PAGE, wp);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<UserLotteryLog>) rp.readSerializable();
    }

    @Override
    public List<UserLotteryLog> queryUserLotteryLog(long lotteryId, String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(lotteryId);
        wp.writeStringUTF(uno);

        Request req = new Request(LotteryConstants.QUERY_USER_LOTTERY_LOG, wp);

        RPacket rp = reqProcessor.process(req);

        return (List<UserLotteryLog>) rp.readSerializable();
    }

    @Override
    public UserLotteryLog getUserLotteryLog(long lotteryId, String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(lotteryId);
        wp.writeStringUTF(uno);

        Request req = new Request(LotteryConstants.GET_USER_LOTTERY_LOG, wp);

        RPacket rp = reqProcessor.process(req);

        return (UserLotteryLog) rp.readSerializable();
    }

    @Override
    public List<UserLotteryLog> queryLastestUserLotteryLog(long lotteryid) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(lotteryid);

        Request req = new Request(LotteryConstants.QUERY_LASTEST_USERLOTTERY_LOG, wp);

        RPacket rp = reqProcessor.process(req);

        return (List<UserLotteryLog>) rp.readSerializable();
    }

    @Override
    public UserLotteryLog userLotteryAward(String uno, String screenName, String ip, Date lotteryDate, long lotteryId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        wp.writeStringUTF(screenName);
        wp.writeStringUTF(ip);
        wp.writeSerializable(lotteryDate);
        wp.writeLongNx(lotteryId);
        Request req = new Request(LotteryConstants.USER_LOTTERY_AWARD, wp);
        RPacket rp = reqProcessor.process(req);
        return (UserLotteryLog) rp.readSerializable();
    }

    @Override
    public AwardEntry userLotteryAwardEntry(String uno, String screenName, String ip, Date lotteryDate, long lotteryId, AwardEntry awardEntry, int baseRate, LotteryType lotteryType) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        wp.writeStringUTF(screenName);
        wp.writeStringUTF(ip);
        wp.writeSerializable(lotteryDate);
        wp.writeLongNx(lotteryId);
        wp.writeSerializable(awardEntry);
        wp.writeIntNx(baseRate);
        wp.writeSerializable(lotteryType);
        Request req = new Request(LotteryConstants.USER_LOTTERY_AWARD_ENTRY, wp);
        RPacket rp = reqProcessor.process(req);
        return (AwardEntry) rp.readSerializable();
    }


//    @Override
//    public List<Ticket> queryTicketList(QueryExpress queryExpress) throws ServiceException {
//        WPacket wp = new WPacket();
//        wp.writeSerializable(queryExpress);
//        Request req = new Request(LotteryConstants.QUERY_TICKET_LIST, wp);
//        RPacket rp = reqProcessor.process(req);
//
//        return (List<Ticket>) rp.readSerializable();
//    }
//
//    @Override
//    public PageRows<Ticket> queryTicketPageRows(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
//        WPacket wp = new WPacket();
//        wp.writeSerializable(queryExpress);
//        wp.writeSerializable(pagination);
//
//        Request req = new Request(LotteryConstants.QUERY_TICKET_PAGEROWS, wp);
//        RPacket rp = reqProcessor.process(req);
//
//        return (PageRows<Ticket>) rp.readSerializable();
//    }
//
//
//    @Override
//    public boolean modifyTicket(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
//        WPacket wp = new WPacket();
//        wp.writeSerializable(updateExpress);
//        wp.writeSerializable(queryExpress);
//
//        Request req = new Request(LotteryConstants.MODIFY_TICKET, wp);
//        RPacket rp = reqProcessor.process(req);
//
//        return rp.readBooleanNx();
//    }
//
//    @Override
//    public Ticket createTicket(Ticket ticket) throws ServiceException {
//
//        WPacket wp = new WPacket();
//        wp.writeSerializable(ticket);
//
//        Request req = new Request(LotteryConstants.CREATE_TICKET, wp);
//        RPacket rp = reqProcessor.process(req);
//        return (Ticket) rp.readSerializable();  //To change body of implemented methods use File | Settings | File Templates.
//    }

//    @Override
//    public Ticket getTicketById(QueryExpress queryExpress) throws ServiceException {
//        WPacket wp = new WPacket();
//        wp.writeSerializable(queryExpress);
//
//        Request req = new Request(LotteryConstants.GET_TICKET_BY_ID, wp);
//        RPacket rp = reqProcessor.process(req);
//        return (Ticket) rp.readSerializable();
//    }
//
//    @Override
//    public List<TicketAward> queryTicketAwardList(QueryExpress queryExpress) throws ServiceException {
//        WPacket wp = new WPacket();
//        wp.writeSerializable(queryExpress);
//
//        Request req = new Request(LotteryConstants.QUERY_TICKETAWARD_LIST, wp);
//        RPacket rp = reqProcessor.process(req);
//        return (List<TicketAward>) rp.readSerializable();
//    }
//
//    @Override
//    public PageRows<TicketAward> queryTicketAwardPageRows(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
//        WPacket wp = new WPacket();
//        wp.writeSerializable(queryExpress);
//        wp.writeSerializable(pagination);
//        Request req = new Request(LotteryConstants.QUERY_TICKETAWARD_PAGEROWS, wp);
//        RPacket rp = reqProcessor.process(req);
//        return (PageRows<TicketAward>) rp.readSerializable();
//    }
//
//
//    @Override
//    public TicketAward getTicketAwardById(QueryExpress queryExpress) throws ServiceException {
//        WPacket wp = new WPacket();
//        wp.writeSerializable(queryExpress);
//        Request req = new Request(LotteryConstants.GET_TICKETAWARD_BY_ID, wp);
//        RPacket rp = reqProcessor.process(req);
//        return (TicketAward) rp.readSerializable();
//    }
//
//    @Override
//    public boolean modifyTicketAward(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
//        WPacket wp = new WPacket();
//        wp.writeSerializable(updateExpress);
//        wp.writeSerializable(queryExpress);
//        Request req = new Request(LotteryConstants.MODIFY_TICKETAWARD, wp);
//        RPacket rp = reqProcessor.process(req);
//        return rp.readBooleanNx();
//    }
//
//    @Override
//    public TicketAward createTicketAward(TicketAward ticketAward) throws ServiceException {
//        WPacket wp = new WPacket();
//        wp.writeSerializable(ticketAward);
//        Request req = new Request(LotteryConstants.CREATE_TICKETAWARD, wp);
//        RPacket rp = reqProcessor.process(req);
//        return (TicketAward) rp.readSerializable();
//    }

    @Override
    public List<LotteryAward> queryLotteryAwardByCache(long lotteryId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(lotteryId);
        Request req = new Request(LotteryConstants.QUERY_LOTTERY_AWARD_BY_CACHE, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<LotteryAward>) rp.readSerializable();
    }

    @Override
    public LotteryAward userLottery(Long lotteryId, String clientid, String ip, Profile profile, String code) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(lotteryId);
        wp.writeStringUTF(clientid);
        wp.writeStringUTF(ip);
        wp.writeSerializable(profile);
        wp.writeStringUTF(code);
        Request req = new Request(LotteryConstants.USER_LOTTERY_AWARD_FIVE, wp);
        RPacket rp = reqProcessor.process(req);
        return (LotteryAward) rp.readSerializable();
    }

    @Override
    public boolean setUserLotteryShareTime(String clientid, String date_short) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(clientid);
        wp.writeStringUTF(date_short);
        Request req = new Request(LotteryConstants.USER_LOTTERY_AWARD_FIVE_ADD_SHARE_TIME, wp);
        RPacket rp = reqProcessor.process(req);
        return (Boolean) rp.readSerializable();
    }

    @Override
    public boolean addOrIncrTime(String key) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(key);
        Request req = new Request(LotteryConstants.ADD_ORIN_CRTIME, wp);
        RPacket rp = reqProcessor.process(req);
        return (Boolean) rp.readSerializable();
    }

    @Override
    public void putShuang12Cache(Profile profile, String words) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(profile);
        wp.writeStringUTF(words);
        Request req = new Request(LotteryConstants.PUT_S12_CACHE, wp);
        RPacket rp = reqProcessor.process(req);
        return;
    }

    @Override
    public PageRows<String> getShuang12Cache(Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(pagination);
        Request req = new Request(LotteryConstants.GET_S12_CACHE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<String>) rp.readSerializable();
    }

    @Override
    public void incrShuang12ShareTime(Profile profile) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(profile);
        Request req = new Request(LotteryConstants.INCR_S12_SHARE_TIME, wp);
        RPacket rp = reqProcessor.process(req);
        return;
    }

    @Override
    public LotteryAwardItem lottery(long lotteryId, String profileId, String ip, Date date, String nick) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(lotteryId);
        wp.writeStringUTF(profileId);
        wp.writeStringUTF(ip);
        wp.writeSerializable(date);
        wp.writeStringUTF(nick);
        Request req = new Request(LotteryConstants.LOTTERY, wp);
        RPacket rp = reqProcessor.process(req);

        return (LotteryAwardItem) rp.readSerializable();
    }

    @Override
    public boolean addChance(long lotteryId, String profileId, int value, Date date) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(lotteryId);
        wp.writeStringUTF(profileId);
        wp.writeIntNx(value);
        wp.writeSerializable(date);
        Request req = new Request(LotteryConstants.ADD_CHANCE, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public boolean modifyLotteryAddressByUid(LotteryAddress lotteryAddress, String profileid) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(lotteryAddress);
        wp.writeStringUTF(profileid);
        Request req = new Request(LotteryConstants.MODIFY_LOTTERY_ADDRESS, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public LotteryItemResult takelottery(long lotteryId, String profileId, String ip, Date date, String nick) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(lotteryId);
        wp.writeStringUTF(profileId);
        wp.writeStringUTF(ip);
        wp.writeSerializable(date);
        wp.writeStringUTF(nick);
        Request req = new Request(LotteryConstants.TAKE_LOTTERY, wp);
        RPacket rp = reqProcessor.process(req);
        return (LotteryItemResult) rp.readSerializable();
    }

    @Override
    public boolean addLotteryChance(long lotteryId, String profileId, int value, Date date) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(lotteryId);
        wp.writeStringUTF(profileId);
        wp.writeIntNx(value);
        wp.writeSerializable(date);
        Request req = new Request(LotteryConstants.ADD_LOTTERY_CHANCE, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }


}
