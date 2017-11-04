package com.enjoyf.platform.service.point;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.point.pointwall.PointwallApp;
import com.enjoyf.platform.service.point.pointwall.PointwallTasklog;
import com.enjoyf.platform.service.point.pointwall.PointwallWall;
import com.enjoyf.platform.service.point.pointwall.PointwallWallApp;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: liangtang
 * Date: 13-5-29
 * Time: 下午1:49
 * To change this template use File | Settings | File Templates.
 */
public class PointServiceImpl implements PointService {


    private ReqProcessor reqProcessor = null;

    /**
     * get service
     *
     * @param scfg
     */
    public PointServiceImpl(ServiceConfig scfg) {
        if (scfg == null) {
            throw new RuntimeException("ContentServiceImpl.ctor: ServiceConfig is null!");
        }

        //
        reqProcessor = scfg.getReqProcessor();
    }


    @Override
    public UserPoint addUserPoint(UserPoint userPoint) throws ServiceException {
        //序列化 写
        WPacket wp = new WPacket();
        wp.writeSerializable(userPoint);

        Request req = new Request(PointConstants.ADD_USER_POINT, wp);

        RPacket rp = reqProcessor.process(req);
        //序列化 读
        return (UserPoint) rp.readSerializable();
    }

    @Override
    public PageRows<UserPoint> queryUserPointByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);

        Request req = new Request(PointConstants.QUERY_USER_POION_BY_PAGE, wp);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<UserPoint>) rp.readSerializable();
    }

    @Override
    public UserPoint getUserPoint(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request req = new Request(PointConstants.GET_USER_POION, wp);

        RPacket rp = reqProcessor.process(req);

        return (UserPoint) rp.readSerializable();
    }

    @Override
    public boolean increasePointActionHistory(PointActionHistory pointActionHistory, PointKeyType pointKeyType) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(pointActionHistory);
        wp.writeSerializable(pointKeyType);
        Request req = new Request(PointConstants.ADD_POINT_ACTION_HISTORY, wp);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public PageRows<PointActionHistory> queryPointActionHistoryByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);

        Request req = new Request(PointConstants.QUERY_POINT_ACTION_HISTORY_BY_PAGE, wp);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<PointActionHistory>) rp.readSerializable();
    }

    @Override
    public PointActionHistory getPointActionHistory(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request req = new Request(PointConstants.GET_POINT_ACTION_HISTORY, wp);

        RPacket rp = reqProcessor.process(req);

        return (PointActionHistory) rp.readSerializable();
    }

    @Override
    public boolean modifyPointActionHistory(long actionHistoryId, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(actionHistoryId);
        wp.writeSerializable(updateExpress);

        Request req = new Request(PointConstants.MODIFY_POINT_ACTION_HISTORY, wp);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }


    @Override
    public UserDayPoint getUserDayPoint(QueryExpress queryExpress, Date date) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(date);

        Request req = new Request(PointConstants.GET_USER_DAY_POINT, wp);

        RPacket rp = reqProcessor.process(req);

        return (UserDayPoint) rp.readSerializable();
    }

    @Override
    public List<UserDayPoint> queryUserDayPoint(QueryExpress queryExpress, Date date) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(date);

        Request req = new Request(PointConstants.QUERY_USER_DAY_POINT, wp);

        RPacket rp = reqProcessor.process(req);

        return (List<UserDayPoint>) rp.readSerializable();
    }

    @Override
    public Goods addGoods(Goods goods) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(goods);

        Request req = new Request(PointConstants.ADD_GOODS, wp);

        RPacket rp = reqProcessor.process(req);

        return (Goods) rp.readSerializable();
    }

    @Override
    public List<Goods> queryGoods(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request req = new Request(PointConstants.QUERY_GOODS, wp);

        RPacket rp = reqProcessor.process(req);

        return (List<Goods>) rp.readSerializable();
    }

    @Override
    public PageRows<Goods> queryGoodsByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);

        Request req = new Request(PointConstants.QUERY_GOODS_BY_PAGE, wp);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<Goods>) rp.readSerializable();
    }

    @Override
    public Goods getGoodsById(long goodsId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(goodsId);

        Request req = new Request(PointConstants.GET_GOODS_BY_ID, wp);

        RPacket rp = reqProcessor.process(req);

        return (Goods) rp.readSerializable();
    }

    @Override
    public boolean modifyGoodsById(UpdateExpress updateExpress, long goodsId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeSerializable(goodsId);

        Request req = new Request(PointConstants.MODIFY_GOODS_BY_ID, wp);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public int addGoodsItem(List<GoodsItem> goodsItem) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) goodsItem);
        Request req = new Request(PointConstants.ADD_GOODS_ITEM, wp);

        RPacket rp = reqProcessor.process(req);

        return rp.readIntNx();
    }

    @Override
    public PageRows<GoodsItem> queryGoodsItemByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);

        Request req = new Request(PointConstants.QUERY_GOODS_ITEM_BY_PAGE, wp);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<GoodsItem>) rp.readSerializable();
    }

    @Override
    public List<GoodsItem> queryGoodsItem(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.QUERY_GOODS_ITEM, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<GoodsItem>) rp.readSerializable();
    }

    @Override
    public GoodsItem getGoodsItemById(long goodsItemId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(goodsItemId);

        Request req = new Request(PointConstants.GET_GOODS_ITEM_BY_ID, wp);

        RPacket rp = reqProcessor.process(req);

        return (GoodsItem) rp.readSerializable();
    }

    @Override
    public boolean modifyGoodsItemById(UpdateExpress updateExpress, long goodsItemId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeSerializable(goodsItemId);

        Request req = new Request(PointConstants.MODIFY_GOODS_ITEM_BY_ID, wp);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public List<PointActionHistory> queryPointActionHistory(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request req = new Request(PointConstants.QUERY_POINT_ACTION_HISTORY, wp);

        RPacket rp = reqProcessor.process(req);

        return (List<PointActionHistory>) rp.readSerializable();
    }

    @Override
    public UserConsumeLog consumeGoods(String uno, String ProfileId, String appKey, long goodsId, Date consumeDate, String consumeIp, GoodsActionType goodsActionType, String address) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeStringUTF(uno);
        wp.writeStringUTF(ProfileId);
        wp.writeStringUTF(appKey);
        wp.writeLongNx(goodsId);
        wp.writeSerializable(consumeDate);
        wp.writeStringUTF(consumeIp);
        wp.writeSerializable(goodsActionType);
        wp.writeStringUTF(address);

        Request req = new Request(PointConstants.USER_CONSUME, wp);

        RPacket rp = reqProcessor.process(req);

        return (UserConsumeLog) rp.readSerializable();
    }

    @Override
    public PageRows<UserConsumeLog> queryConsumeLog(String profileId, Date startDate, Date endDate, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeStringUTF(profileId);
        wp.writeSerializable(startDate);
        wp.writeSerializable(endDate);
        wp.writeSerializable(pagination);

        Request req = new Request(PointConstants.QUERY_USER_CONSUME_LOG_BY_PAGE, wp);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<UserConsumeLog>) rp.readSerializable();
    }

    @Override
    public List<UserConsumeLog> queryConsumeLogList(QueryExpress queryExpress, GoodsActionType gift) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(gift);
        Request req = new Request(PointConstants.QUERY_USER_CONSUME_LOG_LIST, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<UserConsumeLog>) rp.readSerializable();
    }

    @Override
    public PageRows<UserConsumeLog> queryConsumeLogByPage(QueryExpress queryExpress, Pagination pagination, GoodsActionType type) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        wp.writeSerializable(type);

        Request req = new Request(PointConstants.QUERY_CONSUMELOG_BY_PAGE, wp);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<UserConsumeLog>) rp.readSerializable();
    }

    @Override
    public List<UserConsumeLog> queryConsumeLogByGoodsIdConsumeTime(String uno, long goodsId, Date consumeTime) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeStringUTF(uno);
        wp.writeLongNx(goodsId);
        wp.writeSerializable(consumeTime);

        Request req = new Request(PointConstants.QUERY_USER_CONSUME_LOG_BY_UNO_GOODSID_COONSUMETIME, wp);

        RPacket rp = reqProcessor.process(req);

        return (List<UserConsumeLog>) rp.readSerializable();
    }

    @Override
    public List<UserConsumeLog> queryConsumeLogByGoodsId(String uno, long goodsId, String consumeOrder) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeStringUTF(uno);
        wp.writeLongNx(goodsId);
        wp.writeString(consumeOrder);

        Request req = new Request(PointConstants.QUERY_USER_CONSUME_LOG_BY_UNO_GOODSID, wp);

        RPacket rp = reqProcessor.process(req);

        return (List<UserConsumeLog>) rp.readSerializable();
    }

    @Override
    public PageRows<ExchangeGoods> queryExchangeGoodsByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(PointConstants.QUERY_EXCHANGE_GOODS_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<ExchangeGoods>) rp.readSerializable();
    }

    @Override
    public ExchangeGoods getExchangeGoodS(Long goodsIdS) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(goodsIdS);
        Request req = new Request(PointConstants.GET_EXCHANGE_GOODS, wp);
        RPacket rp = reqProcessor.process(req);
        return (ExchangeGoods) rp.readSerializable();
    }

    @Override
    public List<ExchangeGoods> listExchangeGoods(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.QUERY_EXCHANGE_GOODS_LIST, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<ExchangeGoods>) rp.readSerializable();
    }

    @Override
    public ExchangeGoods insertExchangeGoods(ExchangeGoods exchangeGoods) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(exchangeGoods);
        Request req = new Request(PointConstants.INSERT_EXCHANGE_GOODS, wp);
        RPacket rp = reqProcessor.process(req);
        return (ExchangeGoods) rp.readSerializable();
    }

    @Override
    public boolean modifyExchangeGoods(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.UPDATE_EXCHANGE_GOODS, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public PageRows<ExchangeGoodsItem> queryExchangeGoodsItemByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(PointConstants.QUERY_EXCHANGE_GOODS_ITEM_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<ExchangeGoodsItem>) rp.readSerializable();
    }

    @Override
    public int addExchangeGoodsItem(List<ExchangeGoodsItem> list) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) list);
        Request req = new Request(PointConstants.ADD_EXCHANGE_GOODS_ITEM, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readIntNx();
    }

    @Override
    public boolean modifyExchangeItemGoods(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.UPDATE_EXCHANGE_ITEM_GOODS, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public UserExchangeLog exchangeGoodsItem(String uno, String profileId, String appKey, long goodsId, Date exchangeDate, String exchangeIp, String userExchangeDomain, boolean isFree) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeStringUTF(uno);
        wp.writeStringUTF(profileId);
        wp.writeStringUTF(appKey);
        wp.writeLongNx(goodsId);
        wp.writeSerializable(exchangeDate);
        wp.writeStringUTF(exchangeIp);
        wp.writeStringUTF(userExchangeDomain);
        wp.writeBooleanNx(isFree);
        Request req = new Request(PointConstants.EXCHNAGE_EXGOODS_ITEM, wp);
        RPacket rp = reqProcessor.process(req);

        return (UserExchangeLog) rp.readSerializable();
    }

    @Override
    public List<ExchangeGoodsItem> taoExchangeGoodsItemByGoodsId(String uno, String profileId, String appKey, long goodsId, Date taoDate, String taoIp) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        wp.writeStringUTF(profileId);
        wp.writeStringUTF(appKey);
        wp.writeLongNx(goodsId);
        wp.writeSerializable(taoDate);
        wp.writeStringUTF(taoIp);

        Request req = new Request(PointConstants.TAO_EXGOODS_ITEM, wp);
        RPacket rp = reqProcessor.process(req);

        return (List<ExchangeGoodsItem>) rp.readSerializable();
    }

    @Override
    public PageRows<UserExchangeLog> queryByUser(String profileId, Date startTime, Date endTime, Pagination pagination, String appkey) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeStringUTF(profileId);
        wp.writeSerializable(startTime);
        wp.writeSerializable(endTime);
        wp.writeSerializable(pagination);
        wp.writeStringUTF(appkey);
        Request req = new Request(PointConstants.QUERY_USER_EXCHANGE_LOG_BY_PAGE, wp);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<UserExchangeLog>) rp.readSerializable();
    }

    @Override
    public PageRows<UserExchangeLog> queryUserExchangeByUno(String uno, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeStringUTF(uno);
        wp.writeSerializable(pagination);

        Request req = new Request(PointConstants.QUERY_USER_EXCHANGE_LOG_BY_UNOPAGE, wp);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<UserExchangeLog>) rp.readSerializable();
    }

    @Override
    public PageRows<UserExchangeLog> queryByUserExchangePage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);

        Request req = new Request(PointConstants.QUERY_USER_EXCHANEG_LOG_BY_QUERY, wp);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<UserExchangeLog>) rp.readSerializable();
    }

    @Override
    public List<UserExchangeLog> queryUserExchangeByQueryExpress(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(queryExpress);

        Request req = new Request(PointConstants.QUERY_USER_EXCHANGE_LOG_BY_QUERYEXPRESS, wp);

        RPacket rp = reqProcessor.process(req);

        return (List<UserExchangeLog>) rp.readSerializable();

    }

    @Override
    public int queryUserExchangeByDate(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(queryExpress);

        Request req = new Request(PointConstants.QUERY_USER_EXCHANGE_LOG_BY_DATE, wp);

        RPacket rp = reqProcessor.process(req);

        return rp.readIntNx();
    }

    @Override
    public List<UserRecentLogEntry> queryUserRecentLog() throws ServiceException {
        WPacket wp = new WPacket();
        Request req = new Request(PointConstants.QUERY_USER_RECENT_LOG, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<UserRecentLogEntry>) rp.readSerializable();
    }

    @Override
    public boolean increaseSmsCountExchangLog(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.INCREASE_SMS_COUNT_EXCHANGE_LOG, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public PageRows<GiftReserve> queryGiftReserveByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(PointConstants.QUERY_GIFT_RESERVE_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);

        return (PageRows<GiftReserve>) rp.readSerializable();
    }

    @Override
    public List<GiftReserve> queryGiftReserveByList(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.QUERY_GIFT_RESERVE_BY_LIST, wp);
        RPacket rp = reqProcessor.process(req);

        return (List<GiftReserve>) rp.readSerializable();
    }

    @Override
    public Map<Long, GiftReserve> checkGiftReserve(String porfileId, String appkey, Set<Long> giftReserveIds) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(porfileId);
        wp.writeStringUTF(appkey);
        wp.writeSerializable((Serializable) giftReserveIds);

        Request req = new Request(PointConstants.CHECK_GIFT_RESERVER, wp);
        RPacket rp = reqProcessor.process(req);

        return (Map<Long, GiftReserve>) rp.readSerializable();
    }

    @Override
    public GiftReserve getGiftReserve(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.GET_GIFT_RESERVE, wp);
        RPacket rp = reqProcessor.process(req);
        return (GiftReserve) rp.readSerializable();
    }

    @Override
    public GiftReserve createGiftReserve(GiftReserve giftReserve) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(giftReserve);
        Request req = new Request(PointConstants.CREATE_GIFT_RESERVE, wp);
        RPacket rp = reqProcessor.process(req);
        return (GiftReserve) rp.readSerializable();
    }

    @Override
    public boolean modifyGiftReserve(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.MODIFY_GIFT_RESERVE, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    /*
    积分墙列表维护 start
     */

    @Override
    public PageRows<PointwallWall> queryPointwallWallByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(PointConstants.QUERY_POINTWALL_WALL, wp);
        RPacket rp = reqProcessor.process(req);

        return (PageRows<PointwallWall>) rp.readSerializable();
    }

    @Override
    public List<PointwallWall> queryPointwallWall(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.QUERY_POINTWALL_WALL_ALL, wp);
        RPacket rp = reqProcessor.process(req);

        return (List<PointwallWall>) rp.readSerializable();
    }


    @Override
    public int countPointwallWall(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.COUNT_POINTWALL_WALL, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readIntNx();

    }


    @Override
    public boolean updatePointwallWall(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.UPDATE_POINTWALL_WALL, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public PointwallWall insertPointwallWall(PointwallWall pointwallWall) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(pointwallWall);
        Request req = new Request(PointConstants.INSERT_POINTWALL_WALL, wp);
        RPacket rp = reqProcessor.process(req);
        return (PointwallWall) rp.readSerializable();
    }

    @Override
    public int deletePointwallWall(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.DELETE_POINTWALL_WALL, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readIntNx();
    }

    @Override
    public PointwallWall getPointwallWall(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.GET_POINTWALL_WALL, wp);
        RPacket rp = reqProcessor.process(req);
        return (PointwallWall) rp.readSerializable();

    }

    /*
   积分墙列表维护 end
    */

    /*
   积分墙  app管理 start
    */

    @Override
    public PageRows<PointwallApp> queryPointwallAppByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(PointConstants.QUERY_POINTWALL_APP, wp);
        RPacket rp = reqProcessor.process(req);

        return (PageRows<PointwallApp>) rp.readSerializable();
    }

    @Override
    public boolean updatePointwallApp(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.UPDATE_POINTWALL_APP, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public PointwallApp insertPointwallApp(PointwallApp pointwallApp) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(pointwallApp);
        Request req = new Request(PointConstants.INSERT_POINTWALL_APP, wp);
        RPacket rp = reqProcessor.process(req);
        return (PointwallApp) rp.readSerializable();
    }

    @Override
    public int deletePointwallApp(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.DELETE_POINTWALL_APP, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readIntNx();
    }

    @Override
    public PointwallApp getPointwallApp(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.GET_POINTWALL_APP, wp);
        RPacket rp = reqProcessor.process(req);
        return (PointwallApp) rp.readSerializable();

    }

    /*
   积分墙 app管理 end
    */


    /*
        单个积份墙app列表管理 start
         */

    @Override
    public PageRows<PointwallWallApp> queryPointwallWallAppByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(PointConstants.QUERY_POINTWALL_WALL_APP, wp);
        RPacket rp = reqProcessor.process(req);

        return (PageRows<PointwallWallApp>) rp.readSerializable();
    }

    @Override
    public List<PointwallWallApp> queryPointwallWallAppAll(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.QUERY_POINTWALL_WALL_APP_NOT_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);

        return (List<PointwallWallApp>) rp.readSerializable();
    }

    @Override
    public boolean updatePointwallWallApp(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.UPDATE_POINTWALL_WALL_APP, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public PointwallWallApp insertPointwallWallApp(PointwallWallApp pointwallWallApp) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(pointwallWallApp);
        Request req = new Request(PointConstants.INSERT_POINTWALL_WALL_APP, wp);
        RPacket rp = reqProcessor.process(req);
        return (PointwallWallApp) rp.readSerializable();
    }

    @Override
    public int deletePointwallWallApp(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.DELETE_POINTWALL_WALL_APP, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readIntNx();
    }

    @Override
    public PointwallWallApp getPointwallWallApp(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.GET_POINTWALL_WALL_APP, wp);
        RPacket rp = reqProcessor.process(req);
        return (PointwallWallApp) rp.readSerializable();

    }

    @Override
    public int countPointwallWallAppTotalOfApps(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.COUNT_TOTAL_POINTWALL_WALL_APP, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readIntNx();
    }

    @Override
    public int countPointwallWallAppOfOneWall(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.COUNT_POINTWALL_WALL_APP, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readIntNx();
    }


    /*
  单个积份墙app列表管理 end
    */

    /*
       point.pw_tasklog管理    start
    */
    @Override
    public PageRows<PointwallTasklog> queryPointwallTasklogByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(PointConstants.QUERY_POINTWALL_TASKLOG, wp);
        RPacket rp = reqProcessor.process(req);

        return (PageRows<PointwallTasklog>) rp.readSerializable();
    }

    //查询一个符合一定条件的所有记录,非分页,用于数据导出
    @Override
    public int countPointwallTasklog(QueryExpress queryExpress) throws ServiceException {

        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.COUNT_POINTWALL_TASKLOG_ALL, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readIntNx();

    }

    //查询一个符合一定条件的所有记录,非分页,用于数据导出
    @Override
    public List<PointwallTasklog> queryPointwallTasklogAll(QueryExpress queryExpress, int startIndex, int size) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeIntNx(startIndex);
        wp.writeIntNx(size);
        Request req = new Request(PointConstants.QUERY_POINTWALL_TASKLOG_ALL, wp);
        RPacket rp = reqProcessor.process(req);

        return (List<PointwallTasklog>) rp.readSerializable();


    }

    @Override
    public PointwallTasklog insertPointwallTasklog(PointwallTasklog pointwallTasklog) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(pointwallTasklog);
        Request req = new Request(PointConstants.INSERT_POINTWALL_TASKLOG, wp);
        RPacket rp = reqProcessor.process(req);
        return (PointwallTasklog) rp.readSerializable();

    }

    @Override
    public PointwallTasklog getPointwallTasklog(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.GET_POINTWALL_TASKLOG, wp);
        RPacket rp = reqProcessor.process(req);
        return (PointwallTasklog) rp.readSerializable();

    }
    /*
       point.pw_tasklog管理    end
    */


    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(event);
        Request req = new Request(PointConstants.RECIEVE_EVENT, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }


    @Override
    public PageRows<ActivityGoods> queryActivityGoodsByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(PointConstants.QUERY_ACTIVITY_GOODS_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<ActivityGoods>) rp.readSerializable();
    }

    @Override
    public ActivityGoods createActivityGoods(ActivityGoods activityGoods) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(activityGoods);
        Request req = new Request(PointConstants.INSERT_ACTIVITY_GOODS, wp);
        RPacket rp = reqProcessor.process(req);
        return (ActivityGoods) rp.readSerializable();
    }

    @Override
    public ActivityGoods getActivityGoods(long goodsId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(goodsId);
        Request req = new Request(PointConstants.GET_ACTIVITY_GOODS, wp);
        RPacket rp = reqProcessor.process(req);
        return (ActivityGoods) rp.readSerializable();
    }

    @Override
    public boolean modifyActivityGoods(long activityGoodsId, UpdateExpress updateExpress, Map<String, Object> paramMap) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(activityGoodsId);
        wp.writeSerializable(updateExpress);
        wp.writeSerializable((Serializable) paramMap);
        Request req = new Request(PointConstants.MODIFY_ACTIVITY_GOODS, wp);
        RPacket rp = reqProcessor.process(req);
        return (Boolean) rp.readSerializable();
    }

    @Override
    public List<ActivityGoods> listActivityGoods(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.QUERY_ACTIVITY_GOODS, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<ActivityGoods>) rp.readSerializable();
    }

    @Override
    public int queryActivityGoodsCount(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.COUNT_ACTIVITY_GOODS, wp);
        RPacket rp = reqProcessor.process(req);
        return (Integer) rp.readSerializable();
    }

    @Override
    public Map<String, List<ActivityGoods>> queryGiftmarketIndexList() throws ServiceException {
        WPacket wp = new WPacket();
        Request req = new Request(PointConstants.QUERY_GIFTMARKET_INDEX, wp);
        RPacket rp = reqProcessor.process(req);
        return (Map<String, List<ActivityGoods>>) rp.readSerializable();
    }

    @Override
    public PageRows<ActivityGoods> queryActivityGoodsByLetter(ActivityType activityType, String letter, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(activityType);
        wp.writeStringUTF(letter);
        wp.writeSerializable(pagination);
        Request req = new Request(PointConstants.QUERY_ACTIVITY_GOODS_BY_LETTER, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<ActivityGoods>) rp.readSerializable();
    }

    //根据appkey 获取pointkey added by tony
    public PointKeyType getPointKeyType(String appkey) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(appkey);
        Request req = new Request(PointConstants.GET_POINT_KEY_TYPE, wp);
        RPacket rp = reqProcessor.process(req);

        return (PointKeyType) rp.readSerializable();

    }

    @Override
    public List<ActivityHotRanks> queryActivityHot(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.QUERY_ACTIVITY_HOT, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<ActivityHotRanks>) rp.readSerializable();
    }

    @Override
    public boolean modifyActivityHotRanks(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.MODIFY_ACTIVITY_HOT_RANKS, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public List<ActivityGoods> queryActivityGoodsByGameId(long gameDbId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(gameDbId);
        Request req = new Request(PointConstants.QUERY_ACTIVITY_GOODS_BY_GAME_ID, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<ActivityGoods>) rp.readSerializable();
    }

    @Override
    public GoodsSeckill createGoodsSeckill(GoodsSeckill goodsSeckill) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(goodsSeckill);
        Request req = new Request(PointConstants.CREATE_GOODS_SECKILL, wp);
        RPacket rp = reqProcessor.process(req);
        return (GoodsSeckill) rp.readSerializable();
    }

    @Override
    public GoodsSeckill getGoodsSeckill(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.GET_GOODS_SECKILL, wp);
        RPacket rp = reqProcessor.process(req);
        return (GoodsSeckill) rp.readSerializable();
    }

    @Override
    public List<GoodsSeckill> queryGoodsSeckill(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(PointConstants.QUERY_GOODS_SECKILL, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<GoodsSeckill>) rp.readSerializable();
    }

    @Override
    public PageRows<GoodsSeckill> queryGoodsSeckillByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(PointConstants.QUERY_GOODS_SECKILL_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<GoodsSeckill>) rp.readSerializable();
    }

    @Override
    public List<GoodsSeckill> queryGoodsSeckillByCache(GoodsActionType goodsActionType, long goodsId, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(goodsActionType);
        wp.writeLongNx(goodsId);
        wp.writeSerializable(pagination);
        Request req = new Request(PointConstants.QUERY_GOODS_SECKILL_BY_CACHE, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<GoodsSeckill>) rp.readSerializable();
    }

    @Override
    public boolean modifyGoodsSeckill(long seckillId, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(seckillId);
        wp.writeSerializable(updateExpress);
        Request req = new Request(PointConstants.MODIFY_GOODS_SECKILL, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public GoodsSeckill getGoodsSeckillById(long seckillId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(seckillId);
        Request req = new Request(PointConstants.GET_GOODS_SECKILL_BY_ID, wp);
        RPacket rp = reqProcessor.process(req);
        return (GoodsSeckill) rp.readSerializable();
    }

    @Override
    public ActivityGoodsDTO seckillGoods(long seckillId, long goodsId, String profileId, String appKey, String uno, Date consumeDate, String consumeIp, GoodsActionType goodsActionType, String address) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(seckillId);
        wp.writeLongNx(goodsId);
        wp.writeStringUTF(profileId);
        wp.writeStringUTF(appKey);
        wp.writeStringUTF(uno);
        wp.writeSerializable(consumeDate);
        wp.writeStringUTF(consumeIp);
        wp.writeSerializable(goodsActionType);
        wp.writeStringUTF(address);
        Request req = new Request(PointConstants.SECKILL_GOODS, wp);
        RPacket rp = reqProcessor.process(req);
        return (ActivityGoodsDTO) rp.readSerializable();
    }

    @Override
    public Map<Long, ActivityGoods> queryActivityGoodsIdSet(Set<Long> goodsIdSet) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) goodsIdSet);
        Request req = new Request(PointConstants.QUERY_ACTIVITY_GOODS_IDSET, wp);
        RPacket rp = reqProcessor.process(req);
        return (Map<Long, ActivityGoods>) rp.readSerializable();
    }

    @Override
    public PageRows<ActivityGoods> queryActivityGoodsAllListByCache(ActivityType activityType, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(activityType);
        wp.writeSerializable(page);
        Request req = new Request(PointConstants.QUERY_ACTIVITY_GOODS_BY_CACHE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<ActivityGoods>) rp.readSerializable();
    }

    @Override
    public UserExchangeLog getUserExchangeLog(String profileId, long aid, long logId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(profileId);
        wp.writeLongNx(aid);
        wp.writeLongNx(logId);
        Request req = new Request(PointConstants.GET_USER_EXCHANGE_LOG, wp);
        RPacket rp = reqProcessor.process(req);
        return (UserExchangeLog) rp.readSerializable();
    }

    @Override
    public long incrPointRule(String key, Long value) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(key);
        wp.writeLongNx(value);
        Request req = new Request(PointConstants.INCR_POINT_RULE, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readLongNx();
    }

    @Override
    public long getWanbaQuestionPoint(String profileId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(profileId);
        Request req = new Request(PointConstants.GET_WANBA_QUESTION_POINT, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readLongNx();
    }

    @Override
    public Map<String, Integer> queryRankListByType(String type, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(type);
        wp.writeSerializable(pagination);
        Request req = new Request(PointConstants.QUERY_RANK_LIST_BY_TYPE, wp);
        RPacket rp = reqProcessor.process(req);
        return (Map<String, Integer>) rp.readSerializable();
    }

    @Override
    public UserLotteryLog openGiftLottery(String profileId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(profileId);
        Request req = new Request(PointConstants.OPEN_GIFT_LOTTERY, wp);
        RPacket rp = reqProcessor.process(req);
        return (UserLotteryLog) rp.readSerializable();
    }

    @Override
    public List<GiftLottery> queryGiftLotteryByCache() throws ServiceException {
        WPacket wp = new WPacket();
        Request req = new Request(PointConstants.QUERY_GIFT_LOTTERY_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<GiftLottery>) rp.readSerializable();
    }


    @Override
    public int getGiftBoxNum(String profileId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(profileId);
        Request req = new Request(PointConstants.GET_GIFT_BOX_NUM, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readIntNx();
    }

    @Override
    public int exchangeGiftBox(int num, String profileId, String appkey) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeIntNx(num);
        wp.writeString(profileId);
        wp.writeString(appkey);
        Request req = new Request(PointConstants.EXCHANGE_GIFT_BOX, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readIntNx();
    }

    @Override
    public Map<Long, UserLotteryLog> queryUserLotteryLogByCache(String profileId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(profileId);
        Request req = new Request(PointConstants.QUERY_USER_LOTTERY_LOG_BY_CACHE, wp);
        RPacket rp = reqProcessor.process(req);
        return (Map<Long, UserLotteryLog>) rp.readSerializable();
    }

    @Override
    public boolean chooseLottery(String profileId, long lotteryId, boolean bool) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(profileId);
        wp.writeLongNx(lotteryId);
        wp.writeBooleanNx(bool);
        Request req = new Request(PointConstants.CHOOSE_LOTTERY, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public Map<String, String> getChooseLottery(String profileId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(profileId);
        Request req = new Request(PointConstants.GET_USER_CHOOSE_LOTTERY, wp);
        RPacket rp = reqProcessor.process(req);
        return (Map<String, String>) rp.readSerializable();
    }

    @Override
    public Map<String, Map<String, String>> queryChooseLottery(Set<String> profileIds) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) profileIds);
        Request req = new Request(PointConstants.QUERY_USER_CHOOSE_LOTTERY, wp);
        RPacket rp = reqProcessor.process(req);
        return (Map<String, Map<String, String>>) rp.readSerializable();
    }

    @Override
    public int getUserPointByDay(String profileId, String flag) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(profileId);
        wp.writeStringUTF(flag);
        Request req = new Request(PointConstants.GET_USER_POINT_BY_DAY, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readIntNx();
    }

    @Override
    public PageRows<String> queryWorship(String profileId, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(profileId);
        wp.writeSerializable(pagination);
        Request req = new Request(PointConstants.QUERY_WORSHIP, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<String>) rp.readSerializable();
    }

    @Override
    public PageRows<PointActionHistory> queryMyPointByCache(String profileId, String type,String pointkey, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(profileId);
        wp.writeStringUTF(type);
        wp.writeStringUTF(pointkey);
        wp.writeSerializable(pagination);
        Request req = new Request(PointConstants.QUERY_MY_POINT_LIST_BY_CACHE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<PointActionHistory>) rp.readSerializable();
    }
}
