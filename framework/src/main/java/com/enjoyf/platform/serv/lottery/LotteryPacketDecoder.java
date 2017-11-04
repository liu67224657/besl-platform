/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.lottery;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */

/**
 * This interface receives packets from the remote clients. It
 * translates them into method calls into the business logic.
 * <p/>
 * The logicProcess() method may be called reentrantly, so it and any
 * handlers must be thread-safe.
 */
class LotteryPacketDecoder extends PacketDecoder {
    private LotteryLogic lotteryLogic;

    /**
     * Constructor takes the UserPropsLogic object that we're going to use
     * to logicProcess the packets.
     *
     * @param logic our logical friend
     */
    LotteryPacketDecoder(LotteryLogic logic) {
        lotteryLogic = logic;

        setTransContainer(LotteryConstants.getTransContainer());
    }

    /**
     * Called when ThreadSampleInfo packet arrives. This routine will
     * just forward the call to the logic object which
     * will take care of actually decoding the packet.
     */
    protected WPacket logicProcess(ConnThreadBase conn, RPacket rPacket) throws ServiceException {
        byte type = rPacket.getType();

        WPacket wp = new WPacket();
        wp.writeByteNx(ServiceConstants.OK);

        switch (type) {
            case LotteryConstants.CREATE_LOTTERY:
                wp.writeSerializable(lotteryLogic.createLottery((Lottery) rPacket.readSerializable()));
                break;
            case LotteryConstants.QUERY_LOTTERY:
                wp.writeSerializable((Serializable) lotteryLogic.queryLottery((QueryExpress) rPacket.readSerializable()));
                break;
            case LotteryConstants.QUERY_LOTTERY_BY_PAGE:
                wp.writeSerializable(lotteryLogic.queryLotteryByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case LotteryConstants.GET_LOTTERY_BY_ID:
                wp.writeSerializable(lotteryLogic.getLotteryById(rPacket.readLongNx()));
                break;
            case LotteryConstants.MODIFY_LOTTERY_BY_ID:
                wp.writeBooleanNx(lotteryLogic.modifyLotteryById((UpdateExpress) rPacket.readSerializable(), rPacket.readLongNx()));
                break;

            case LotteryConstants.CREATE_LOTTERY_AWARD:
                wp.writeSerializable(lotteryLogic.createLotteryAward((LotteryAward) rPacket.readSerializable()));
                break;
            case LotteryConstants.QUERY_LOTTERY_AWARD:
                wp.writeSerializable((Serializable) lotteryLogic.queryLotteryAward((QueryExpress) rPacket.readSerializable()));
                break;
            case LotteryConstants.QUERY_LOTTERY_AWARD_BY_PAGE:
                wp.writeSerializable(lotteryLogic.queryLotteryAwardByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case LotteryConstants.GET_LOTTERY_AWARD_BY_ID:
                wp.writeSerializable(lotteryLogic.getLotteryAwardById(rPacket.readLongNx()));
                break;
            case LotteryConstants.MODIFY_LOTTERY_AWARD_BY_ID:
                wp.writeBooleanNx(lotteryLogic.modifyLotteryAwardById((UpdateExpress) rPacket.readSerializable(), rPacket.readLongNx()));
                break;

            case LotteryConstants.CREATE_LOTTERY_AWARD_ITEM:
                wp.writeBooleanNx(lotteryLogic.createLotteryAwardItem(rPacket.readLongNx(), (List<LotteryAwardItem>) rPacket.readSerializable()));
                break;
            case LotteryConstants.QUERY_LOTTERY_AWARD_ITEM:
                wp.writeSerializable((Serializable) lotteryLogic.queryLotteryAwardItem((QueryExpress) rPacket.readSerializable()));
                break;
            case LotteryConstants.QUERY_LOTTERY_AWARD_ITEM_BY_PAGE:
                wp.writeSerializable(lotteryLogic.queryByLotteryAwardIdPage(rPacket.readLongNx(), (Pagination) rPacket.readSerializable()));
                break;
            case LotteryConstants.GET_LOTTERY_AWARD_ITEM_BY_ID:
                wp.writeSerializable(lotteryLogic.getLotteryAwardItemById(rPacket.readLongNx()));
                break;
            case LotteryConstants.MODIFY_LOTTERY_AWARD_ITEM_BY_ID:
                wp.writeBooleanNx(lotteryLogic.modifyLotteryAwardItemById((UpdateExpress) rPacket.readSerializable(), rPacket.readLongNx()));
                break;

            case LotteryConstants.CREATE_USER_LOTTERY_LOG:
                wp.writeSerializable(lotteryLogic.createUserLotteryLog((UserLotteryLog) rPacket.readSerializable()));
                break;
            case LotteryConstants.QUERY_USER_LOTTERY_LOG_BY_PAGE:
                wp.writeSerializable(lotteryLogic.queryUserLotteryLogByPage(rPacket.readLongNx(), rPacket.readStringUTF(), (Date) rPacket.readSerializable(), (Date) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case LotteryConstants.QUERY_USER_DAY_LOTTERY:
                wp.writeSerializable(lotteryLogic.queryUserDayLottery(rPacket.readLongNx(), (Date) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case LotteryConstants.USER_LOTTERY_AWARD:
                wp.writeSerializable(lotteryLogic.userLotteryAward(rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF(), (Date) rPacket.readSerializable(), rPacket.readLongNx()));
                break;
            case LotteryConstants.USER_LOTTERY_AWARD_ENTRY:
                wp.writeSerializable(lotteryLogic.userLotteryAwardEntry(rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF(), (Date) rPacket.readSerializable(), rPacket.readLongNx(), (AwardEntry) rPacket.readSerializable(), rPacket.readIntNx(), (LotteryType) rPacket.readSerializable()));
                break;
            case LotteryConstants.GET_LOTTERY_AWARD_BY_LEVEL:
                wp.writeSerializable(lotteryLogic.getLotteryAwardByLevel(rPacket.readIntNx(), rPacket.readLongNx()));
                break;
            case LotteryConstants.GET_LOTTERY_AWARD_BY_RATE:
                wp.writeSerializable(lotteryLogic.getLotteryAwardByRate(rPacket.readLongNx(), rPacket.readIntNx()));
                break;
            case LotteryConstants.QUERY_USER_LOTTERY_LOG:
                wp.writeSerializable((Serializable) lotteryLogic.queryUserLotteryLog(rPacket.readLongNx(), rPacket.readStringUTF()));
                break;
            case LotteryConstants.GET_USER_DAY_LOTTERY:
                wp.writeSerializable(lotteryLogic.getUserDayLottery(rPacket.readLongNx(), rPacket.readStringUTF(), (Date) rPacket.readSerializable()));
                break;
            case LotteryConstants.GET_USER_LOTTERY_LOG:
                wp.writeSerializable(lotteryLogic.getUserLotteryLog(rPacket.readLongNx(), rPacket.readStringUTF()));
                break;
            case LotteryConstants.QUERY_LASTEST_USERLOTTERY_LOG:
                wp.writeSerializable((Serializable) lotteryLogic.queryLastestUserLotteryLog(rPacket.readLongNx()));
                break;

//            case LotteryConstants.QUERY_TICKET_LIST:
//                wp.writeSerializable((Serializable) lotteryLogic.queryTicketList((QueryExpress) rPacket.readSerializable()));
//                break;
//            case LotteryConstants.QUERY_TICKET_PAGEROWS:
//                wp.writeSerializable(lotteryLogic.queryTicketPageRows((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//            case LotteryConstants.MODIFY_TICKET:
//                wp.writeBooleanNx(lotteryLogic.modifyTicket((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
//                break;
//            case LotteryConstants.CREATE_TICKET:
//                wp.writeSerializable(lotteryLogic.createTicket((Ticket) rPacket.readSerializable()));
//                break;
//            case LotteryConstants.GET_TICKET_BY_ID:
//                wp.writeSerializable(lotteryLogic.getTicketById((QueryExpress) rPacket.readSerializable()));
//                break;
//            case LotteryConstants.GET_TICKETAWARD_BY_ID:
//                wp.writeSerializable(lotteryLogic.getTicketAwardById((QueryExpress) rPacket.readSerializable()));
//                break;
//            case LotteryConstants.QUERY_TICKETAWARD_LIST:
//                wp.writeSerializable((Serializable) lotteryLogic.queryTicketList((QueryExpress) rPacket.readSerializable()));
//                break;
//            case LotteryConstants.QUERY_TICKETAWARD_PAGEROWS:
//                wp.writeSerializable(lotteryLogic.queryTicketAwardPageRows((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//            case LotteryConstants.CREATE_TICKETAWARD:
//                wp.writeSerializable(lotteryLogic.createTicketAward((TicketAward) rPacket.readSerializable()));
//                break;
//            case LotteryConstants.MODIFY_TICKETAWARD:
//                wp.writeBooleanNx(lotteryLogic.modifyTicketAward((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
//                break;
            case LotteryConstants.QUERY_LOTTERY_AWARD_BY_CACHE:
                wp.writeSerializable((Serializable) lotteryLogic.queryLotteryAwardByCache(rPacket.readLongNx()));
                break;
            case LotteryConstants.USER_LOTTERY_AWARD_FIVE:
                wp.writeSerializable((Serializable) lotteryLogic.userLottery(rPacket.readLongNx(), rPacket.readStringUTF(), rPacket.readStringUTF(), (Profile) rPacket.readSerializable(), rPacket.readStringUTF()));
                break;
            case LotteryConstants.USER_LOTTERY_AWARD_FIVE_ADD_SHARE_TIME:
                wp.writeSerializable((Serializable) lotteryLogic.setUserLotteryShareTime(rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case LotteryConstants.ADD_ORIN_CRTIME:
                wp.writeSerializable((Serializable) lotteryLogic.addOrIncrTime(rPacket.readStringUTF()));
                break;

            case LotteryConstants.PUT_S12_CACHE:
                lotteryLogic.putShuang12Cache((Profile) rPacket.readSerializable(), rPacket.readStringUTF());
                break;
            case LotteryConstants.GET_S12_CACHE:
                wp.writeSerializable(lotteryLogic.getShuang12Cache((Pagination) rPacket.readSerializable()));
                break;
            case LotteryConstants.INCR_S12_SHARE_TIME:
                lotteryLogic.incrShuang12ShareTime((Profile) rPacket.readSerializable());
                break;

            case LotteryConstants.LOTTERY:
                wp.writeSerializable(lotteryLogic.lottery(rPacket.readLongNx(), rPacket.readStringUTF(), rPacket.readStringUTF(), (Date) rPacket.readSerializable(), rPacket.readStringUTF()));
                break;

            case LotteryConstants.ADD_CHANCE:
                wp.writeBooleanNx(lotteryLogic.addChance(rPacket.readLongNx(), rPacket.readStringUTF(), rPacket.readIntNx(), (Date) rPacket.readSerializable()));
                break;

            case LotteryConstants.MODIFY_LOTTERY_ADDRESS:
                wp.writeBooleanNx(lotteryLogic.modifyLotteryAddressByUid((LotteryAddress) rPacket.readSerializable(), rPacket.readStringUTF()));
                break;
            case LotteryConstants.TAKE_LOTTERY:
                wp.writeSerializable(lotteryLogic.takelottery(rPacket.readLongNx(), rPacket.readStringUTF(), rPacket.readStringUTF(), (Date) rPacket.readSerializable(), rPacket.readStringUTF()));
                break;
            case LotteryConstants.ADD_LOTTERY_CHANCE:
                wp.writeBooleanNx(lotteryLogic.addLotteryChance(rPacket.readLongNx(), rPacket.readStringUTF(), rPacket.readIntNx(), (Date) rPacket.readSerializable()));
                break;

            default:
                GAlerter.lab("ProfilePacketDecoder.logicProcess: Unrecognized type: " + type);
                throw new ServiceException(ServiceException.BAD_PACKET);
        }

        return wp;
    }
}
