/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.point;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.point.pointwall.PointwallApp;
import com.enjoyf.platform.service.point.pointwall.PointwallTasklog;
import com.enjoyf.platform.service.point.pointwall.PointwallWall;
import com.enjoyf.platform.service.point.pointwall.PointwallWallApp;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
class PointPacketDecoder extends PacketDecoder {
    private PointLogic pointLogic;

    /**
     * Constructor takes the UserPropsLogic object that we're going to use
     * to logicProcess the packets.
     *
     * @param logic our logical friend
     */
    PointPacketDecoder(PointLogic logic) {
        pointLogic = logic;

        setTransContainer(PointConstants.getTransContainer());
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
            case PointConstants.ADD_USER_POINT:
                wp.writeSerializable(pointLogic.addUserPoint((UserPoint) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_USER_POION_BY_PAGE:
                wp.writeSerializable(pointLogic.queryUserPointByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.GET_USER_POION:
                wp.writeSerializable(pointLogic.getUserPoint((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.ADD_POINT_ACTION_HISTORY:
                wp.writeBooleanNx(pointLogic.increasePointActionHistory((PointActionHistory) rPacket.readSerializable(), (PointKeyType) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_POINT_ACTION_HISTORY_BY_PAGE:
                wp.writeSerializable(pointLogic.queryPointActionHistoryByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.GET_POINT_ACTION_HISTORY:
                wp.writeSerializable(pointLogic.getPointActionHistory((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.MODIFY_POINT_ACTION_HISTORY:
                wp.writeBooleanNx(pointLogic.modifyPointActionHistory(rPacket.readLongNx(), (UpdateExpress) rPacket.readSerializable()));
                break;
            case PointConstants.GET_USER_DAY_POINT:
                wp.writeSerializable(pointLogic.getUserDayPoint((QueryExpress) rPacket.readSerializable(), (Date) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_POINT_ACTION_HISTORY:
                wp.writeSerializable((Serializable) pointLogic.queryPointActionHistory((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.ADD_GOODS:
                wp.writeSerializable(pointLogic.addGoods((Goods) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_GOODS_BY_PAGE:
                wp.writeSerializable(pointLogic.queryGoodsByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_GOODS:
                wp.writeSerializable((Serializable) pointLogic.queryGoods((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.GET_GOODS_BY_ID:
                wp.writeSerializable(pointLogic.getGoodsById((Long) rPacket.readSerializable()));
                break;
            case PointConstants.MODIFY_GOODS_BY_ID:
                wp.writeSerializable(pointLogic.modifyGoodsById((UpdateExpress) rPacket.readSerializable(), (Long) rPacket.readSerializable()));
                break;
            case PointConstants.ADD_GOODS_ITEM:
                wp.writeIntNx(pointLogic.addGoodsItem((List<GoodsItem>) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_GOODS_ITEM_BY_PAGE:
                wp.writeSerializable(pointLogic.queryGoodsItemByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.GET_GOODS_ITEM_BY_ID:
                wp.writeSerializable(pointLogic.getGoodsItemById((Long) rPacket.readSerializable()));
                break;
            case PointConstants.MODIFY_GOODS_ITEM_BY_ID:
                wp.writeSerializable(pointLogic.modifyGoodsItemById((UpdateExpress) rPacket.readSerializable(), (Long) rPacket.readSerializable()));
                break;
            case PointConstants.USER_CONSUME:
                wp.writeSerializable(pointLogic.consumeGoods(rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readLongNx(), (Date) rPacket.readSerializable(), rPacket.readStringUTF(), (GoodsActionType) rPacket.readSerializable(), rPacket.readStringUTF()));
                break;
            case PointConstants.QUERY_USER_CONSUME_LOG_BY_PAGE:
                wp.writeSerializable(pointLogic.queryConsumeLog(rPacket.readStringUTF(), (Date) rPacket.readSerializable(), (Date) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_USER_CONSUME_LOG_BY_UNO_GOODSID_COONSUMETIME:
                wp.writeSerializable((Serializable) pointLogic.queryConsumeLogByGoodsIdConsumeTime(rPacket.readStringUTF(), rPacket.readLongNx(), (Date) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_USER_CONSUME_LOG_BY_UNO_GOODSID:
                wp.writeSerializable((Serializable) pointLogic.queryConsumeLogByGoodsId(rPacket.readStringUTF(), rPacket.readLongNx(), rPacket.readStringUTF()));
                break;
            case PointConstants.QUERY_USER_DAY_POINT:
                wp.writeSerializable((Serializable) pointLogic.queryUserDayPoint((QueryExpress) rPacket.readSerializable(), (Date) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_EXCHANGE_GOODS_BY_PAGE:
                wp.writeSerializable(pointLogic.queryExchangeGoodsByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.GET_EXCHANGE_GOODS:
                wp.writeSerializable(pointLogic.getExchangeGoodS(rPacket.readLongNx()));
                break;
            case PointConstants.QUERY_EXCHANGE_GOODS_LIST:
                wp.writeSerializable((Serializable) pointLogic.listExchangeGoods((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.INSERT_EXCHANGE_GOODS:
                wp.writeSerializable(pointLogic.insertExchangeGoods((ExchangeGoods) rPacket.readSerializable()));
                break;
            case PointConstants.UPDATE_EXCHANGE_GOODS:
                wp.writeSerializable(pointLogic.modifyExchangeGoods((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.TAO_EXGOODS_ITEM:
                wp.writeSerializable((Serializable) pointLogic.taoExchangeGoodsItemByGoodsId(rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readLongNx(), (Date) rPacket.readSerializable(), rPacket.readStringUTF()));
                break;
            case PointConstants.EXCHNAGE_EXGOODS_ITEM:
                wp.writeSerializable(pointLogic.exchangeGoodsItem(rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readLongNx(), (Date) rPacket.readSerializable(), rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readBooleanNx()));
                break;
            case PointConstants.QUERY_EXCHANGE_GOODS_ITEM_BY_PAGE:
                wp.writeSerializable(pointLogic.queryExchangeGoodsItemByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.ADD_EXCHANGE_GOODS_ITEM:
                wp.writeIntNx(pointLogic.addExchangeGoodsItem((List<ExchangeGoodsItem>) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_USER_EXCHANGE_LOG_BY_PAGE:
                wp.writeSerializable(pointLogic.queryByUser(rPacket.readStringUTF(), (Date) rPacket.readSerializable(), (Date) rPacket.readSerializable(), (Pagination) rPacket.readSerializable(), rPacket.readStringUTF()));
                break;
            case PointConstants.QUERY_USER_EXCHANGE_LOG_BY_UNOPAGE:
                wp.writeSerializable(pointLogic.queryUserExchangeByUno(rPacket.readStringUTF(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_USER_EXCHANEG_LOG_BY_QUERY:
                wp.writeSerializable(pointLogic.queryByUserExchangePage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_USER_EXCHANGE_LOG_BY_QUERYEXPRESS:
                wp.writeSerializable((Serializable) pointLogic.queryUserExchangeByQueryExpress((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_USER_RECENT_LOG:
                wp.writeSerializable((Serializable) pointLogic.queryUserRecentLog());
                break;
            case PointConstants.QUERY_USER_EXCHANGE_LOG_BY_DATE:
                wp.writeIntNx(pointLogic.queryUserExchangeByDate((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.INCREASE_SMS_COUNT_EXCHANGE_LOG:
                wp.writeBooleanNx(pointLogic.increaseSmsCountExchangLog((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.UPDATE_EXCHANGE_ITEM_GOODS:
                wp.writeSerializable(pointLogic.modifyExchangeItemGoods((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_GIFT_RESERVE_BY_PAGE:
                wp.writeSerializable(pointLogic.queryGiftReserveByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_GIFT_RESERVE_BY_LIST:
                wp.writeSerializable((Serializable) pointLogic.queryGiftReserveByList((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.MODIFY_GIFT_RESERVE:
                wp.writeSerializable(pointLogic.modifyGiftReserve((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.GET_GIFT_RESERVE:
                wp.writeSerializable(pointLogic.getGiftReserve((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.CREATE_GIFT_RESERVE:
                wp.writeSerializable(pointLogic.createGiftReserve((GiftReserve) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_POINTWALL_WALL:
                wp.writeSerializable((Serializable) pointLogic.queryPointwallWallByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.UPDATE_POINTWALL_WALL:
                wp.writeSerializable(pointLogic.updatePointwallWall((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.INSERT_POINTWALL_WALL:
                wp.writeSerializable(pointLogic.insertPointwallWall((PointwallWall) rPacket.readSerializable()));
                break;
            case PointConstants.DELETE_POINTWALL_WALL:
                wp.writeSerializable(pointLogic.deletePointwallWall((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.GET_POINTWALL_WALL:
                wp.writeSerializable(pointLogic.getPointwallWall((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_POINTWALL_APP:
                wp.writeSerializable((Serializable) pointLogic.queryPointwallAppByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.UPDATE_POINTWALL_APP:
                wp.writeSerializable(pointLogic.updatePointwallApp((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.INSERT_POINTWALL_APP:
                wp.writeSerializable(pointLogic.insertPointwallApp((PointwallApp) rPacket.readSerializable()));
                break;
            case PointConstants.DELETE_POINTWALL_APP:
                wp.writeSerializable(pointLogic.deletePointwallApp((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.GET_POINTWALL_APP:
                wp.writeSerializable(pointLogic.getPointwallApp((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_POINTWALL_WALL_APP:
                wp.writeSerializable((Serializable) pointLogic.queryPointwallWallAppByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_POINTWALL_WALL_APP_NOT_BY_PAGE:
                wp.writeSerializable((Serializable) pointLogic.queryPointwallWallAppAll((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.UPDATE_POINTWALL_WALL_APP:
                wp.writeSerializable(pointLogic.updatePointwallWallApp((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.INSERT_POINTWALL_WALL_APP:
                wp.writeSerializable(pointLogic.insertPointwallWallApp((PointwallWallApp) rPacket.readSerializable()));
                break;
            case PointConstants.DELETE_POINTWALL_WALL_APP:
                wp.writeSerializable(pointLogic.deletePointwallWallApp((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.GET_POINTWALL_WALL_APP:
                wp.writeSerializable(pointLogic.getPointwallWallApp((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.COUNT_TOTAL_POINTWALL_WALL_APP:
                wp.writeIntNx(pointLogic.countPointwallWallAppTotalOfApps((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.COUNT_POINTWALL_WALL_APP:
                wp.writeIntNx(pointLogic.countPointwallWallAppOfOneWall((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_POINTWALL_TASKLOG:
                wp.writeSerializable((Serializable) pointLogic.queryPointwallTasklogByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.INSERT_POINTWALL_TASKLOG:
                wp.writeSerializable(pointLogic.insertPointwallTasklog((PointwallTasklog) rPacket.readSerializable()));
                break;
            case PointConstants.GET_POINTWALL_TASKLOG:
                wp.writeSerializable(pointLogic.getPointwallTasklog((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.COUNT_POINTWALL_TASKLOG_ALL:
                wp.writeIntNx(pointLogic.countPointwallTasklog((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_POINTWALL_TASKLOG_ALL:
                wp.writeSerializable((Serializable) pointLogic.queryPointwallTasklogAll((QueryExpress) rPacket.readSerializable(), rPacket.readIntNx(), rPacket.readIntNx()));
                break;
            case PointConstants.RECIEVE_EVENT:
                wp.writeBooleanNx(pointLogic.receiveEvent((Event) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_CONSUMELOG_BY_PAGE:
                wp.writeSerializable(pointLogic.queryConsumeLogByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable(), (GoodsActionType) rPacket.readSerializable()));
                break;
            case PointConstants.COUNT_POINTWALL_WALL:
                wp.writeIntNx(pointLogic.countPointwallWall((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_POINTWALL_WALL_ALL:
                wp.writeSerializable((Serializable) pointLogic.queryPointwallWall((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.CHECK_GIFT_RESERVER:
                wp.writeSerializable((Serializable) pointLogic.checkGiftReserve(rPacket.readStringUTF(), rPacket.readStringUTF(), (java.util.Set<Long>) rPacket.readSerializable()));
                break;
            case PointConstants.INSERT_ACTIVITY_GOODS:
                wp.writeSerializable(pointLogic.createActivityGoods((ActivityGoods) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_ACTIVITY_GOODS_BY_PAGE:
                wp.writeSerializable(pointLogic.queryActivityGoodsByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.GET_ACTIVITY_GOODS:
                wp.writeSerializable(pointLogic.getActivityGoods(rPacket.readLongNx()));
                break;
            case PointConstants.MODIFY_ACTIVITY_GOODS:
                wp.writeSerializable(pointLogic.modifyActivityGoods(rPacket.readLongNx(), (UpdateExpress) rPacket.readSerializable(), (Map<String, Object>) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_ACTIVITY_GOODS:
                wp.writeSerializable((Serializable) pointLogic.listActivityGoods((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_GIFTMARKET_INDEX:
                wp.writeSerializable((Serializable) pointLogic.queryGiftmarketIndexList());
                break;
            case PointConstants.QUERY_ACTIVITY_GOODS_BY_LETTER:
                wp.writeSerializable((Serializable) pointLogic.queryActivityGoodsByLetter((ActivityType) rPacket.readSerializable(), rPacket.readStringUTF(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.COUNT_ACTIVITY_GOODS:
                wp.writeSerializable(pointLogic.queryActivityGoodsCount((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_ACTIVITY_HOT:
                wp.writeSerializable((Serializable) pointLogic.queryActivityHot((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.GET_POINT_KEY_TYPE:
                wp.writeSerializable((Serializable) pointLogic.getPointKeyType(rPacket.readStringUTF()));
                break;
            case PointConstants.MODIFY_ACTIVITY_HOT_RANKS:
                wp.writeSerializable(pointLogic.modifyActivityHotRanks((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_ACTIVITY_GOODS_BY_GAME_ID:
                wp.writeSerializable((Serializable) pointLogic.queryActivityGoodsByGameId(rPacket.readLongNx()));
                break;
            case PointConstants.QUERY_USER_CONSUME_LOG_LIST:
                wp.writeSerializable((Serializable) pointLogic.queryConsumeLogList((QueryExpress) rPacket.readSerializable(), (GoodsActionType) rPacket.readSerializable()));
                break;

            case PointConstants.CREATE_GOODS_SECKILL:
                wp.writeSerializable(pointLogic.createGoodsSeckill((GoodsSeckill) rPacket.readSerializable()));
                break;
            case PointConstants.GET_GOODS_SECKILL:
                wp.writeSerializable(pointLogic.getGoodsSeckill((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_GOODS_SECKILL:
                wp.writeSerializable((Serializable) pointLogic.queryGoodsSeckill((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_GOODS_SECKILL_BY_PAGE:
                wp.writeSerializable(pointLogic.queryGoodsSeckillByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.MODIFY_GOODS_SECKILL:
                wp.writeSerializable(pointLogic.modifyGoodsSeckill(rPacket.readLongNx(), (UpdateExpress) rPacket.readSerializable()));
                break;
            case PointConstants.GET_GOODS_SECKILL_BY_ID:
                wp.writeSerializable(pointLogic.getGoodsSeckillById(rPacket.readLongNx()));
                break;
            case PointConstants.SECKILL_GOODS:
                wp.writeSerializable(pointLogic.seckillGoods(rPacket.readLongNx(), rPacket.readLongNx(), rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF(), (Date) rPacket.readSerializable(), rPacket.readStringUTF(), (GoodsActionType) rPacket.readSerializable(), rPacket.readStringUTF()));
                break;
            case PointConstants.QUERY_ACTIVITY_GOODS_IDSET:
                wp.writeSerializable((Serializable) pointLogic.queryActivityGoodsIdSet((Set<Long>) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_GOODS_SECKILL_BY_CACHE:
                wp.writeSerializable((Serializable) pointLogic.queryGoodsSeckillByCache((GoodsActionType) rPacket.readSerializable(), rPacket.readLongNx(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_GOODS_ITEM:
                wp.writeSerializable((Serializable) pointLogic.queryGoodsItem((QueryExpress) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_ACTIVITY_GOODS_BY_CACHE:
                wp.writeSerializable(pointLogic.queryActivityGoodsAllListByCache((ActivityType) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.GET_USER_EXCHANGE_LOG:
                wp.writeSerializable(pointLogic.getUserExchangeLog(rPacket.readStringUTF(), rPacket.readLongNx(), rPacket.readLongNx()));
                break;
            case PointConstants.INCR_POINT_RULE:
                wp.writeLongNx(pointLogic.incrPointRule(rPacket.readStringUTF(), rPacket.readLongNx()));
                break;
            case PointConstants.GET_WANBA_QUESTION_POINT:
                wp.writeLongNx(pointLogic.getWanbaQuestionPoint(rPacket.readStringUTF()));
                break;
            case PointConstants.QUERY_RANK_LIST_BY_TYPE:
                wp.writeSerializable((Serializable) pointLogic.queryRankListByType(rPacket.readStringUTF(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.OPEN_GIFT_LOTTERY:
                wp.writeSerializable(pointLogic.openGiftLottery(rPacket.readStringUTF()));
                break;
            case PointConstants.QUERY_GIFT_LOTTERY_BY_PAGE:
                wp.writeSerializable((Serializable) pointLogic.queryGiftLotteryByCache());
                break;
            case PointConstants.GET_GIFT_BOX_NUM:
                wp.writeIntNx(pointLogic.getGiftBoxNum(rPacket.readStringUTF()));
                break;
            case PointConstants.EXCHANGE_GIFT_BOX:
                wp.writeIntNx(pointLogic.exchangeGiftBox(rPacket.readIntNx(), rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case PointConstants.QUERY_USER_LOTTERY_LOG_BY_CACHE:
                wp.writeSerializable((Serializable) pointLogic.queryUserLotteryLogByCache(rPacket.readStringUTF()));
                break;
            case PointConstants.CHOOSE_LOTTERY:
                wp.writeSerializable(pointLogic.chooseLottery(rPacket.readStringUTF(), rPacket.readLongNx(), rPacket.readBooleanNx()));
                break;
            case PointConstants.GET_USER_CHOOSE_LOTTERY:
                wp.writeSerializable((Serializable) pointLogic.getChooseLottery(rPacket.readStringUTF()));
                break;
            case PointConstants.QUERY_USER_CHOOSE_LOTTERY:
                wp.writeSerializable((Serializable) pointLogic.queryChooseLottery((Set<String>) rPacket.readSerializable()));
                break;
            case PointConstants.GET_USER_POINT_BY_DAY:
                wp.writeIntNx(pointLogic.getUserPointByDay(rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case PointConstants.QUERY_WORSHIP:
                wp.writeSerializable(pointLogic.queryWorship(rPacket.readStringUTF(), (Pagination) rPacket.readSerializable()));
                break;
            case PointConstants.QUERY_MY_POINT_LIST_BY_CACHE:
                wp.writeSerializable(pointLogic.queryMyPointByCache(rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF(), (Pagination) rPacket.readSerializable()));
                break;


            default:
                GAlerter.lab("ProfilePacketDecoder.logicProcess: Unrecognized type: " + type);
                throw new ServiceException(ServiceException.BAD_PACKET);
        }

        return wp;
    }
}
