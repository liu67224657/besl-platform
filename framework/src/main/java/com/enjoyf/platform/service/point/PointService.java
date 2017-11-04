package com.enjoyf.platform.service.point;

import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.point.pointwall.PointwallApp;
import com.enjoyf.platform.service.point.pointwall.PointwallTasklog;
import com.enjoyf.platform.service.point.pointwall.PointwallWall;
import com.enjoyf.platform.service.point.pointwall.PointwallWallApp;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: liangtang
 * Date: 13-5-29
 * Time: 下午1:48
 * To change this template use File | Settings | File Templates.
 */
public interface PointService extends EventReceiver {

    /**
     * 增加一个用户的userpoing，暂时用不到
     *
     * @param userPoint
     * @return
     * @throws ServiceException
     */
//    @Deprecated
    public UserPoint addUserPoint(UserPoint userPoint) throws ServiceException;

    /**
     * 分页查询用户的UserPoint
     *
     * @param queryExpress
     * @param pagination
     * @return
     * @throws ServiceException
     */
    public PageRows<UserPoint> queryUserPointByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    /**
     * 通过查询条件得到用户接口
     *
     * @param queryExpress
     * @return
     * @throws ServiceException
     */
    public UserPoint getUserPoint(QueryExpress queryExpress) throws ServiceException;

    /**
     * 增加或减少一个用户的积分
     *
     * @param pointActionHistory
     * @return
     * @throws ServiceException
     */
    public boolean increasePointActionHistory(PointActionHistory pointActionHistory, PointKeyType pointKeyType) throws ServiceException;


    /**
     * 分页查询得到 pointActionHistory
     *
     * @param queryExpress
     * @param pagination
     * @return
     * @throws ServiceException
     */
    public PageRows<PointActionHistory> queryPointActionHistoryByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    /**
     * 查询得到 pointActionHistory
     *
     * @param queryExpress
     * @return
     * @throws ServiceException
     */
    public List<PointActionHistory> queryPointActionHistory(QueryExpress queryExpress) throws ServiceException;


    /**
     * 得到pointActionHistory by key
     *
     * @param queryExpress
     * @return
     * @throws ServiceException
     */
    public PointActionHistory getPointActionHistory(QueryExpress queryExpress) throws ServiceException;

    /**
     * @param actionHistoryId
     * @param updateExpress
     * @return
     * @throws ServiceException
     */
    public boolean modifyPointActionHistory(long actionHistoryId, UpdateExpress updateExpress) throws ServiceException;

    /**
     * 得到UserDayPoint
     *
     * @param queryExpress
     * @param date
     * @return
     * @throws ServiceException
     */
    public UserDayPoint getUserDayPoint(QueryExpress queryExpress, Date date) throws ServiceException;

    public List<UserDayPoint> queryUserDayPoint(QueryExpress queryExpress, Date date) throws ServiceException;

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 添加商品
     *
     * @param goods
     * @return
     * @throws ServiceException
     */
    public Goods addGoods(Goods goods) throws ServiceException;

    /**
     * 分页查询
     *
     * @param queryExpress
     * @param pagination
     * @return
     * @throws ServiceException
     */
    public PageRows<Goods> queryGoodsByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    /**
     * 非分页查询
     *
     * @param queryExpress
     * @return
     * @throws ServiceException
     */
    public List<Goods> queryGoods(QueryExpress queryExpress) throws ServiceException;

    /**
     * 通过 goods id 查询
     *
     * @param goodsId
     * @return
     * @throws ServiceException
     */
    public Goods getGoodsById(long goodsId) throws ServiceException;

    /**
     * 通过 id 修改
     *
     * @param updateExpress
     * @param goodsId
     * @return
     * @throws ServiceException
     */
    public boolean modifyGoodsById(UpdateExpress updateExpress, long goodsId) throws ServiceException;

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 批量添加
     *
     * @param goodsItem
     * @return
     * @throws ServiceException
     */
    public int addGoodsItem(List<GoodsItem> goodsItem) throws ServiceException;

    /**
     * 分页查询
     *
     * @param queryExpress
     * @param pagination
     * @return
     * @throws ServiceException
     */
    public PageRows<GoodsItem> queryGoodsItemByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public List<GoodsItem> queryGoodsItem(QueryExpress queryExpress) throws ServiceException;

    /**
     * 通过id查询，暂时没用到
     *
     * @param goodsItemId
     * @return
     * @throws ServiceException
     */
    public GoodsItem getGoodsItemById(long goodsItemId) throws ServiceException;

    /**
     * 通过id修改 暂时没用到
     *
     * @param updateExpress
     * @param goodsItemId
     * @return
     * @throws ServiceException
     */
    public boolean modifyGoodsItemById(UpdateExpress updateExpress, long goodsItemId) throws ServiceException;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 通过uno id date ip查询，暂时没用到
     *
     * @param uno
     * @param goodsId
     * @return
     * @throws ServiceException
     */
    public UserConsumeLog consumeGoods(String uno, String profileId, String appKey, long goodsId, Date consumeDate, String consumeIp, GoodsActionType goodsActionType, String address) throws ServiceException;

    /**
     * 通过uno 时间段 查询
     *
     * @param profileId
     * @param startDate
     * @param endDate
     * @param pagination
     * @return
     * @throws ServiceException
     */
    public PageRows<UserConsumeLog> queryConsumeLog(String profileId, Date startDate, Date endDate, Pagination pagination) throws ServiceException;

    public List<UserConsumeLog> queryConsumeLogList(QueryExpress queryExpress, GoodsActionType gift) throws ServiceException;

    public PageRows<UserConsumeLog> queryConsumeLogByPage(QueryExpress queryExpress, Pagination pagination, GoodsActionType type) throws ServiceException;

    public List<UserConsumeLog> queryConsumeLogByGoodsIdConsumeTime(String profileId, long goodsId, Date consumeTime) throws ServiceException;

    public List<UserConsumeLog> queryConsumeLogByGoodsId(String profileId, long goodsId, String consumeOrder) throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * ExchangeGoods Method   !
     */
    public PageRows<ExchangeGoods> queryExchangeGoodsByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public ExchangeGoods getExchangeGoodS(Long goodsIdS) throws ServiceException;

    public List<ExchangeGoods> listExchangeGoods(QueryExpress queryExpress) throws ServiceException;

    public ExchangeGoods insertExchangeGoods(ExchangeGoods exchangeGoods) throws ServiceException;

    public boolean modifyExchangeGoods(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    /*
   * ExchangeGoodsItem Method
   * */
    public PageRows<ExchangeGoodsItem> queryExchangeGoodsItemByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;


    public int addExchangeGoodsItem(List<ExchangeGoodsItem> list) throws ServiceException;

    public boolean modifyExchangeItemGoods(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;


    /**
     * 兑换商品，兑换后写入userExchangeLog,兑换类型gcode
     *
     * @param uno
     * @param profileId
     * @param appKey
     * @param goodsId
     * @param exchangeDate
     * @param exchangeIp
     * @param userExchangeDomain
     * @param isFree             根据需求 玩霸的礼包需要收费  传参true为正常其他礼包，false为玩霸的接口单独逻辑
     * @return
     * @throws ServiceException
     */
    public UserExchangeLog exchangeGoodsItem(String uno, String profileId, String appKey, long goodsId, Date exchangeDate, String exchangeIp, String userExchangeDomain, boolean isFree) throws ServiceException;

    /**
     * 淘号码，从已经被领取过的号码挑选5个，兑换后写入userExchangeLog,兑换类型tcode
     *
     * @param goodsId
     * @return
     * @throws ServiceException
     */
    public List<ExchangeGoodsItem> taoExchangeGoodsItemByGoodsId(String uno, String profileId, String appkey, long goodsId, Date taoDate, String taoIp) throws ServiceException;

    /**
     * 已领取激活码记录
     *
     * @param startTime
     * @param endTime
     */
    public PageRows<UserExchangeLog> queryByUser(String profileId, Date startTime, Date endTime, Pagination pagination, String appkey) throws ServiceException;

    public PageRows<UserExchangeLog> queryByUserExchangePage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    /**
     * 已领取激活码记录
     *
     * @param uno
     */
    public PageRows<UserExchangeLog> queryUserExchangeByUno(String uno, Pagination pagination) throws ServiceException;


    /**
     * 根据uno和goodsid查询
     */
    public List<UserExchangeLog> queryUserExchangeByQueryExpress(QueryExpress queryExpress) throws ServiceException;

    /**
     * @return
     * @throws ServiceException
     */
    public int queryUserExchangeByDate(QueryExpress queryExpress) throws ServiceException;

    public List<UserRecentLogEntry> queryUserRecentLog() throws ServiceException;

    public boolean increaseSmsCountExchangLog(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    /**
     * 礼包预定接口 start
     */

    public PageRows<GiftReserve> queryGiftReserveByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public List<GiftReserve> queryGiftReserveByList(QueryExpress queryExpress) throws ServiceException;

    public Map<Long, GiftReserve> checkGiftReserve(String porfileId, String appkey, Set<Long> giftReserveIds) throws ServiceException;

    public GiftReserve getGiftReserve(QueryExpress queryExpress) throws ServiceException;

    public GiftReserve createGiftReserve(GiftReserve giftReserve) throws ServiceException;

    public boolean modifyGiftReserve(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;
    /**
     * 礼包预定接口 end
     */


    /**
     * 积分墙 积分墙列表 start
     */

    public PageRows<PointwallWall> queryPointwallWallByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public List<PointwallWall> queryPointwallWall(QueryExpress queryExpress) throws ServiceException;

    public int countPointwallWall(QueryExpress queryExpress) throws ServiceException;

    public boolean updatePointwallWall(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    public PointwallWall insertPointwallWall(PointwallWall pointwallWall) throws ServiceException;

    public int deletePointwallWall(QueryExpress queryExpress) throws ServiceException;

    public PointwallWall getPointwallWall(QueryExpress queryExpress) throws ServiceException;
    /**
     * 积分墙 积分墙列表 end
     */


    /**
     * 积分墙 app管理 start
     */

    public PageRows<PointwallApp> queryPointwallAppByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public boolean updatePointwallApp(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    public PointwallApp insertPointwallApp(PointwallApp pointwallApp) throws ServiceException;

    public int deletePointwallApp(QueryExpress queryExpress) throws ServiceException;

    public PointwallApp getPointwallApp(QueryExpress queryExpress) throws ServiceException;

    /**
     * 积分墙 app管理 end
     */


    /**
     * 积分墙 单积分墙app列表管理 start
     */

    public PageRows<PointwallWallApp> queryPointwallWallAppByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public List<PointwallWallApp> queryPointwallWallAppAll(QueryExpress queryExpress) throws ServiceException;

    public boolean updatePointwallWallApp(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    public PointwallWallApp insertPointwallWallApp(PointwallWallApp pointwallWallApp) throws ServiceException;

    public int deletePointwallWallApp(QueryExpress queryExpress) throws ServiceException;

    public PointwallWallApp getPointwallWallApp(QueryExpress queryExpress) throws ServiceException;

    public int countPointwallWallAppTotalOfApps(QueryExpress queryExpress) throws ServiceException;

    public int countPointwallWallAppOfOneWall(QueryExpress queryExpress) throws ServiceException;

    /**
     * 积分墙 单积分墙app列表管理 end
     */

    /*
       point.pw_tasklog管理    start
    */
    public PageRows<PointwallTasklog> queryPointwallTasklogByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public int countPointwallTasklog(QueryExpress queryExpress) throws ServiceException;

    public List<PointwallTasklog> queryPointwallTasklogAll(QueryExpress queryExpress, int startIndex, int size) throws ServiceException;

    public PointwallTasklog insertPointwallTasklog(PointwallTasklog pointwallTasklog) throws ServiceException;

    public PointwallTasklog getPointwallTasklog(QueryExpress queryExpress) throws ServiceException;

    /*
      point.pw_tasklog管理    end
    */

    /**
     * 礼包中心合并后的方法
     *
     * @param queryExpress
     * @param pagination
     * @return
     * @throws ServiceException
     */
    public PageRows<ActivityGoods> queryActivityGoodsByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public ActivityGoods createActivityGoods(ActivityGoods activityGoods) throws ServiceException;

    public ActivityGoods getActivityGoods(long goodsId) throws ServiceException;

//    public ActivityGoods getActivityGoodsByCache(long goodsId) throws ServiceException;

    public boolean modifyActivityGoods(long activityGoodsId, UpdateExpress updateExpress, Map<String, Object> paramMap) throws ServiceException;

    public List<ActivityGoods> listActivityGoods(QueryExpress queryExpress) throws ServiceException;

    public int queryActivityGoodsCount(QueryExpress queryExpress) throws ServiceException;

    public Map<String, List<ActivityGoods>> queryGiftmarketIndexList() throws ServiceException;

    public PageRows<ActivityGoods> queryActivityGoodsByLetter(ActivityType activityType, String letter, Pagination pagination) throws ServiceException;

    //根据appkey 获取pointkey added by tony
    public PointKeyType getPointKeyType(String appkey) throws ServiceException;

    public List<ActivityHotRanks> queryActivityHot(QueryExpress queryExpress) throws ServiceException;

    public boolean modifyActivityHotRanks(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    public List<ActivityGoods> queryActivityGoodsByGameId(long gameDbId) throws ServiceException;

    //SecKill
    public GoodsSeckill createGoodsSeckill(GoodsSeckill goodsSeckill) throws ServiceException;

    public GoodsSeckill getGoodsSeckill(QueryExpress queryExpress) throws ServiceException;

    public GoodsSeckill getGoodsSeckillById(long seckillId) throws ServiceException;

    public List<GoodsSeckill> queryGoodsSeckill(QueryExpress queryExpress) throws ServiceException;

    public PageRows<GoodsSeckill> queryGoodsSeckillByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    //todo 没用的方法
    public List<GoodsSeckill> queryGoodsSeckillByCache(GoodsActionType goodsActionType, long goodsId, Pagination pagination) throws ServiceException;

    public boolean modifyGoodsSeckill(long seckillId, UpdateExpress updateExpress) throws ServiceException;

    /**
     * 秒杀
     *
     * @param seckillId
     * @param goodsId
     * @param profileId
     * @param appKey
     * @param uno
     * @param consumeDate
     * @param consumeIp
     * @param goodsActionType
     * @param address
     * @return
     * @throws ServiceException
     */
    public ActivityGoodsDTO seckillGoods(long seckillId, long goodsId, String profileId, String appKey, String uno, Date consumeDate, String consumeIp, GoodsActionType goodsActionType, String address) throws ServiceException;

    public Map<Long, ActivityGoods> queryActivityGoodsIdSet(Set<Long> goodsIdSet) throws ServiceException;

    public PageRows<ActivityGoods> queryActivityGoodsAllListByCache(ActivityType activityType, Pagination page) throws ServiceException;

    public UserExchangeLog getUserExchangeLog(String uid, long aid, long logId) throws ServiceException;

    /**
     * 玩霸3.0积分规则
     *
     * @param key
     * @param value
     * @return
     * @throws ServiceException
     */
    public long incrPointRule(String key, Long value) throws ServiceException;


    /**
     * 玩霸问答获得的积分
     *
     * @param profileId
     * @return
     * @throws ServiceException
     */
    public long getWanbaQuestionPoint(String profileId) throws ServiceException;

    /**
     * 排行榜
     *
     * @param type       排行榜的类型 point_xx_x=积分 prestige_xx_x=声望 consumption_xx_x=消费
     * @param pagination
     * @return
     * @throws ServiceException
     */
    public Map<String, Integer> queryRankListByType(String type, Pagination pagination) throws ServiceException;


    /**
     * 打开宝箱抽奖
     *
     * @param profileId
     * @return
     * @throws ServiceException
     */
    public UserLotteryLog openGiftLottery(String profileId) throws ServiceException;

    /**
     * 查询宝箱数量
     *
     * @param profileId
     * @return
     * @throws ServiceException
     */
    public int getGiftBoxNum(String profileId) throws ServiceException;


    /**
     * 兑换宝箱
     *
     * @param num
     * @param profileId
     * @throws ServiceException return 0=兑换失败 -1=积分不足
     */
    public int exchangeGiftBox(int num, String profileId, String appkey) throws ServiceException;

    public List<GiftLottery> queryGiftLotteryByCache() throws ServiceException;

    /**
     * 用户获得道具列表
     *
     * @param profileId
     * @return
     * @throws ServiceException
     */
    public Map<Long, UserLotteryLog> queryUserLotteryLogByCache(String profileId) throws ServiceException;

    /**
     * 选择道具
     *
     * @param profileId
     * @param lotteryId
     * @param bool      true=选择道具  false=取消选择
     * @return
     * @throws ServiceException
     */
    public boolean chooseLottery(String profileId, long lotteryId, boolean bool) throws ServiceException;

    /**
     * 查询用户选择的道具
     *
     * @param profileId
     * @return
     * @throws ServiceException
     */
    public Map<String, String> getChooseLottery(String profileId) throws ServiceException;

    /**
     * 批量查询用户选择的道具
     *
     * @param profileIds
     * @return
     * @throws ServiceException
     */
    public Map<String, Map<String, String>> queryChooseLottery(Set<String> profileIds) throws ServiceException;

    /**
     * 今天获得积分/查询是否签到
     *
     * @param profileId
     * @param flag      为空则查询今天获得的积分，不为空则查询今天的签到
     * @return
     * @throws ServiceException
     */
    public int getUserPointByDay(String profileId, String flag) throws ServiceException;


    /**
     * 查询某用户被哪些人膜拜
     *
     * @param profileId
     * @param pagination
     * @return
     * @throws ServiceException
     */
    public PageRows<String> queryWorship(String profileId, Pagination pagination) throws ServiceException;


    public PageRows<PointActionHistory> queryMyPointByCache(String profileId, String type,String pointkey, Pagination pagination) throws ServiceException;


}
