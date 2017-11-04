package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.db.point.pointwall.PointwallAppAccessor;
import com.enjoyf.platform.db.point.pointwall.PointwallTasklogAccessor;
import com.enjoyf.platform.db.point.pointwall.PointwallWallAccessor;
import com.enjoyf.platform.db.point.pointwall.PointwallWallAppAccessor;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.serv.point.PointCache;
import com.enjoyf.platform.serv.point.PointConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.HotActivityEvent;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.point.pointwall.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Address;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: liangtang
 * Date: 13-5-29
 * Time: 下午2:12
 * To change this template use File | Settings | File Templates.
 */
public class PointHandler {

    private TemplateHotdeployConfig templateHotdeployConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

    private DataBaseType dataBaseType;
    private String dataSourceName;

    private UserPointAccessor userPointAccessor;
    private PointActionHistoryAccessor pointActionHistoryAccessor;
    private UserDayPointAccessor userDayPointAccessor;

    //
    private GoodsItemAccessor goodsItemAccessor;
    private UserConsumeLogAccessor userConsumeLogAccessor;
    private GoodsAccessor goodsAccessor;

    private ExchangeGoodsAccessor exchangeGoodsAccessor;
    private ExchangeGoodsItemAccessor exchangeGoodsItemAccessor;
    private UserExchangeLogAccessor userExchangeLogAccessor;

    private GiftReserveAccessor giftReserveAccessor;

    private PointwallWallAccessor pointwallWallAccessor;
    private PointwallAppAccessor pointwallAppAccessor;

    private PointwallWallAppAccessor pointwallWallAppAccessor;
    private PointwallTasklogAccessor pointwallTasklogAccessor;
    private ActivityHotRanksAccessor activityHotRanksAccessor;

    private ActivityGoodsAccessor activityGoodsAccessor;
    private GoodsSeckillAccessor goodsSeckillAccessor;

    private GiftLotteryAccessor giftLotteryAccessor;
    private UserLotteryLogAccessor lotteryLogAccessor;

    private PointCache pointCache;
    private PointConfig pointConfig;

    public PointHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the datasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        userPointAccessor = TableAccessorFactory.get().factoryAccessor(UserPointAccessor.class, dataBaseType);
        pointActionHistoryAccessor = TableAccessorFactory.get().factoryAccessor(PointActionHistoryAccessor.class, dataBaseType);
        userDayPointAccessor = TableAccessorFactory.get().factoryAccessor(UserDayPointAccessor.class, dataBaseType);

        goodsItemAccessor = TableAccessorFactory.get().factoryAccessor(GoodsItemAccessor.class, dataBaseType);
        userConsumeLogAccessor = TableAccessorFactory.get().factoryAccessor(UserConsumeLogAccessor.class, dataBaseType);
        goodsAccessor = TableAccessorFactory.get().factoryAccessor(GoodsAccessor.class, dataBaseType);
        activityHotRanksAccessor = TableAccessorFactory.get().factoryAccessor(ActivityHotRanksAccessor.class, dataBaseType);

        //
        exchangeGoodsAccessor = TableAccessorFactory.get().factoryAccessor(ExchangeGoodsAccessor.class, dataBaseType);
        exchangeGoodsItemAccessor = TableAccessorFactory.get().factoryAccessor(ExchangeGoodsItemAccessor.class, dataBaseType);
        userExchangeLogAccessor = TableAccessorFactory.get().factoryAccessor(UserExchangeLogAccessor.class, dataBaseType);
        giftReserveAccessor = TableAccessorFactory.get().factoryAccessor(GiftReserveAccessor.class, dataBaseType);
        pointwallWallAccessor = TableAccessorFactory.get().factoryAccessor(PointwallWallAccessor.class, dataBaseType);
        pointwallAppAccessor = TableAccessorFactory.get().factoryAccessor(PointwallAppAccessor.class, dataBaseType);
        pointwallWallAppAccessor = TableAccessorFactory.get().factoryAccessor(PointwallWallAppAccessor.class, dataBaseType);
        pointwallTasklogAccessor = TableAccessorFactory.get().factoryAccessor(PointwallTasklogAccessor.class, dataBaseType);
        activityGoodsAccessor = TableAccessorFactory.get().factoryAccessor(ActivityGoodsAccessor.class, dataBaseType);
        goodsSeckillAccessor = TableAccessorFactory.get().factoryAccessor(GoodsSeckillAccessor.class, dataBaseType);
        giftLotteryAccessor = TableAccessorFactory.get().factoryAccessor(GiftLotteryAccessor.class, dataBaseType);
        lotteryLogAccessor = TableAccessorFactory.get().factoryAccessor(UserLotteryLogAccessor.class, dataBaseType);





        pointConfig = new PointConfig(props);
        pointCache = new PointCache(pointConfig.getMemCachedConfig());
    }

    /**
     * DB中添加
     *
     * @param userPoint
     * @return
     * @throws DbException
     */
    public UserPoint insertUserPoint(UserPoint userPoint) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userPointAccessor.insert(userPoint, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /**
     * DB中 分页查询
     *
     * @param queryExpress
     * @param pagination
     * @return
     * @throws DbException
     */
    public PageRows<UserPoint> queryUserPoint(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<UserPoint> pageRows = null;
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<UserPoint> list = userPointAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<UserPoint>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /**
     * DB 中得到与主键key对应的数据
     *
     * @param queryExpress
     * @return
     * @throws DbException
     */
    public UserPoint getUserPoint(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userPointAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /**
     * DB中修改 暂时没用到
     *
     * @param updateExpress
     * @param queryExpress
     * @return
     * @throws DbException
     */
    public boolean updateUserPoint(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userPointAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////

    /**
     * DB中添加
     *
     * @param pointActionHistory
     * @return
     * @throws DbException
     */
    public PointActionHistory insertPointActionHistory(PointActionHistory pointActionHistory) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return pointActionHistoryAccessor.insert(pointActionHistory, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /**
     * DB中 分页查询
     *
     * @param queryExpress
     * @param pagination
     * @return
     * @throws DbException
     */
    public PageRows<PointActionHistory> queryPointActionHistory(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<PointActionHistory> pageRows = null;
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<PointActionHistory> list = pointActionHistoryAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<PointActionHistory>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /**
     * DB中 非分页查询
     *
     * @param queryExpress
     * @return
     * @throws DbException
     */
    public List<PointActionHistory> queryPointActionHistory(QueryExpress queryExpress) throws DbException {
        PageRows<PointActionHistory> pageRows = null;
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return pointActionHistoryAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /**
     * DB 中得到与主键key对应的数据
     *
     * @param queryExpress
     * @return
     * @throws DbException
     */
    public PointActionHistory getPointActionHistory(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointActionHistoryAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /**
     * DB中修改 暂时没用到
     *
     * @param updateExpress
     * @param queryExpress
     * @return
     * @throws DbException
     */
    public boolean updatePointActionHistory(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointActionHistoryAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public UserDayPoint getUserDayPoint(QueryExpress queryExpress, Date date) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userDayPointAccessor.get(queryExpress, date, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserDayPoint insertUserDayPoint(UserDayPoint point) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userDayPointAccessor.insert(point, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateUserDayPoint(UpdateExpress updateExpress, QueryExpress queryExpress, Date date) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userDayPointAccessor.update(updateExpress, queryExpress, date, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<UserDayPoint> queryUserDayPoint(QueryExpress queryExpress, Date date) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userDayPointAccessor.query(queryExpress, date, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 批量插入 goodsItemList
     *
     * @param goodsItemList
     * @return
     * @throws DbException
     */
    public boolean batchInsertGoodsItem(List<GoodsItem> goodsItemList) throws ServiceException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);

            goodsItemAccessor.batchInsert(goodsItemList, conn);

            conn.commit();
            return true;
        } catch (SQLException e) {
            DataBaseUtil.rollbackConnection(conn);
            GAlerter.lab("On commit batchInsertGoodsItem occured SQLException. ", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    public Goods insertGoods(Goods goods) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return goodsAccessor.insert(goods, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<Goods> queryGoodsByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<Goods> pageRows = null;
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<Goods> list = goodsAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<Goods>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);

            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public Goods getGoodsById(long goodsId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return goodsAccessor.get(goodsId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateGoodsById(UpdateExpress updateExpress, long goodsId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return goodsAccessor.update(updateExpress, goodsId, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public int insertGoodsItem(List<GoodsItem> goodsItem) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return goodsItemAccessor.batchInsert(goodsItem, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<GoodsItem> queryGoodsItemByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<GoodsItem> pageRows = null;
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<GoodsItem> list = goodsItemAccessor.queryByGoodsIdExchangeStatus(queryExpress, pagination, conn);
            pageRows = new PageRows<GoodsItem>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GoodsItem getGoodsItemById(long goodsItemId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return goodsItemAccessor.get(goodsItemId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateGoodsItemById(UpdateExpress updateExpress, long goodsItemId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return goodsItemAccessor.update(updateExpress, goodsItemId, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /**
     * 用户消费商城的事务，查询商品，判断商品是否为空，是否还有rest_amount
     * 修改商品的rest_amount
     * 如果是虚拟物品，判断虚拟物品是否还存在，如果存在修改goods_item的状态和所有人
     * 写入消费记录。
     *
     * @param uno
     * @param goodsId
     * @param consumeDate
     * @param consumeIp
     * @return UserConsumeLog
     * @throws ServiceException
     */
    public UserConsumeLog consumeGoods(String uno, String profileId, String appkey, long goodsId, Date consumeDate, String consumeIp, GoodsActionType goodsActionType, String address) throws ServiceException {
        UserConsumeLog reutnObj = null;
        Connection conn = null;

        GoodsItem goodsItem = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            //fetch goodsitem
            conn.setAutoCommit(false);
            ActivityGoods activityGoods = activityGoodsAccessor.get(new QueryExpress().add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_GOODS_ID, goodsId)), conn);
            if (activityGoods == null) {
                GAlerter.lab(this.getClass().getName() + " get goods null.uno:" + uno + ",goodsId:" + goodsId);
                throw new ServiceException(PointServiceException.GOODS_NOT_EXISTS, "goods not exists.goodsId" + goodsId);
            }

            if (GoodsType.VIRTUAL.equals(activityGoods.getActivitygoodsType())) {
                goodsItem = lockGoods(uno, profileId, goodsId, consumeDate, conn);
//                conn.commit();
                conn.setAutoCommit(false);
                if (goodsItem == null) {
                    GAlerter.lab(this.getClass().getName() + " get goods item null.uno:" + uno + ",goodsId:" + goodsId);
                    throw new ServiceException(PointServiceException.GOODS_ITEM_GET_FAILED, "goodsitem get failed.goodsId: " + goodsId);
                }
            }

            conn.setAutoCommit(false);
            if (activityGoods.getGoodsResetAmount() <= 0) {
                GAlerter.lab(this.getClass().getName() + " goods rest amount < 0.uno:" + uno + ",goodsId:" + goodsId);
                throw new ServiceException(PointServiceException.GOODS_OUTOF_RESTAMMOUNT, "goods out of rest amount.goodsId" + goodsId);
            }

            // 获得用户积分类型
            PointwallWall wall = getPointwallWall(new QueryExpress().add(QueryCriterions.eq(PointwallWallAppField.APPKEY, appkey)));
            PointKeyType pointKeyType = null;
            if (wall != null && !StringUtil.isEmpty(wall.getPointKey())) {
                pointKeyType = PointKeyType.getByCode(wall.getPointKey());
            }
            if (pointKeyType == null) {
                pointKeyType = PointKeyType.getByCode(appkey);
                if (pointKeyType == null) {
                    pointKeyType = PointKeyType.DEFAULT;
                }

            }

            //判断是否有用户积分信息
            UserPoint userPoint = userPointAccessor.get(new QueryExpress().add(QueryCriterions.eq(UserPointField.PROFILEID, profileId))
                    .add(QueryCriterions.eq(UserPointField.POINTKEY, pointKeyType.getValue())), conn);
            if (userPoint == null) {
                GAlerter.lab(this.getClass().getName() + " user point null.uno:" + uno + ",goodsId:" + goodsId);
                throw new ServiceException(PointServiceException.USER_POINT_GET_FAILED, "user point null.uno" + uno);
            }
            if (userPoint.getUserPoint() < activityGoods.getGoodsPoint()) {
                GAlerter.lab(this.getClass().getName() + " user point not enough.uno:" + uno + ",goodsId:" + goodsId);
                throw new ServiceException(PointServiceException.USER_POINT_NOT_ENOUGH, "user point null.uno" + uno);
            }

            UserConsumeLog userConsumeLog = new UserConsumeLog();
            userConsumeLog.setUserNo(uno);
            userConsumeLog.setProfileId(profileId);
            userConsumeLog.setGoodsId(goodsId);
            userConsumeLog.setGoodsName(activityGoods.getActivitySubject());
            userConsumeLog.setGoodsDesc(activityGoods.getSubDesc());
            userConsumeLog.setGoodsPic(activityGoods.getActivityPicUrl());
            userConsumeLog.setGoodsType(activityGoods.getActivitygoodsType());
            if (goodsItem != null) {
                userConsumeLog.setGoodsItemId(goodsItem.getGoodsItemId());
            }
            userConsumeLog.setConsumeTime(consumeDate);
            userConsumeLog.setConsumeIp(consumeIp);
            userConsumeLog.setConsumeDate(consumeDate);
            userConsumeLog.setConsumeAmount(activityGoods.getGoodsPoint());
            userConsumeLog.setConsumeType(ConsumeType.POINT);
            userConsumeLog.setGoodsActionType(goodsActionType);
            userConsumeLog.setAppkey(appkey);
            userConsumeLog.setValidStatus(ValidStatus.INVALID);
            userConsumeLog.setAddress(Address.parse(address));

            reutnObj = userConsumeLogAccessor.insert(userConsumeLog, conn);

            if (activityGoodsAccessor.update(new UpdateExpress().increase(ActivityGoodsField.GOODS_RESETAMOUNT, -1), new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_GOODS_ID, goodsId))
                    .add(QueryCriterions.gt(ActivityGoodsField.GOODS_RESETAMOUNT, 0)), conn) <= 0) {
                GAlerter.lab(this.getClass().getName() + " goods rest amount -1 failed.uno:" + uno + ",goodsId:" + goodsId + ",goodsItemId" + goodsItem.getGoodsItemId());
                throw new ServiceException(PointServiceException.GOODS_GET_FAILED, "goods get failed.goodsId:" + goodsId);
            }

            //用户积分 减 操作，总消费 加 操作， 兑换消费 加 操作。
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.increase(UserPointField.USERPOINT, -activityGoods.getGoodsPoint());
            updateExpress.increase(UserPointField.CONSUME_AMOUNT, activityGoods.getGoodsPoint());
            updateExpress.increase(UserPointField.CONSUME_EXCHANGE, activityGoods.getGoodsPoint());
            GAlerter.lan(this.getClass().getName() + "updatePoint profileId=" + profileId + " pointkey" + pointKeyType.getValue());
            if (userPointAccessor.update(updateExpress, new QueryExpress().add(QueryCriterions.eq(UserPointField.PROFILEID, profileId))
                    .add(QueryCriterions.eq(UserPointField.POINTKEY, pointKeyType.getValue())), conn) <= 0) {
                GAlerter.lab(this.getClass().getName() + " user point reduce failed.uno:" + uno + ",goodsId:" + goodsId + ",goodsItemId" + goodsItem.getGoodsItemId());
                throw new ServiceException(PointServiceException.USER_POINT_UPDATE_FAILED, "user point update failed.userPointId" + userPoint.getUserPointId());
            }

            //积分明细  记录
            Map<String, String> paramMap = new HashMap<String, String>();
            String descriptionTemplate = templateHotdeployConfig.getExchangePointActionHistoryTemplate();

            paramMap.put("goodsname", activityGoods.getActivitySubject());
            String descriptionBody = NamedTemplate.parse(descriptionTemplate).format(paramMap);

            PointActionHistory pointActionHistory = new PointActionHistory();
            pointActionHistory.setUserNo(uno);
            pointActionHistory.setProfileId(profileId);
            pointActionHistory.setCreateDate(consumeDate);
            pointActionHistory.setActionDate(consumeDate);
            pointActionHistory.setActionDescription(descriptionBody);
            pointActionHistory.setActionType(PointActionType.EXCHANGE_GIFT);
            pointActionHistory.setPointValue(-activityGoods.getGoodsPoint());
            pointActionHistory.setDestId(String.valueOf(goodsId));

            PointActionHistory history = pointActionHistoryAccessor.insert(pointActionHistory, conn);
            if (history == null) {
                GAlerter.lab(this.getClass().getName() + " user point action insert failed.uno:" + uno + ",goodsId:" + goodsId + ",goodsItemId" + goodsItem.getGoodsItemId());
                throw new ServiceException(PointServiceException.POINT_ACTION_HISTORY_INSERT_FAILED, "point action history insert failed.uno" + uno);
            }


            QueryExpress userDayPointExpress = new QueryExpress();
            userDayPointExpress.add(QueryCriterions.eq(UserDayPointField.PROFILEID, pointActionHistory.getProfileId()));
            userDayPointExpress.add(QueryCriterions.eq(UserDayPointField.POINTACTIONTYPE, pointActionHistory.getActionType().getCode()));
            userDayPointExpress.add(QueryCriterions.eq(UserDayPointField.POINTDATE, pointActionHistory.getActionDate()));

            UserDayPoint userDayPoint = userDayPointAccessor.get(userDayPointExpress, pointActionHistory.getActionDate(), conn);
            if (userDayPoint == null) {
                userDayPoint = new UserDayPoint();
                userDayPoint.setUserNo(uno);
                userDayPoint.setProfileId(profileId);
                userDayPoint.setPointValue(-activityGoods.getGoodsPoint());
                userDayPoint.setActionType(PointActionType.EXCHANGE_GIFT);
                userDayPoint.setPointDate(consumeDate);

                if (userDayPointAccessor.insert(userDayPoint, conn) == null) {
                    GAlerter.lab(this.getClass().getName() + " user day point insert failed.uno:" + uno + ",goodsId:" + goodsId + ",goodsItemId" + goodsItem.getGoodsItemId());
                    throw new ServiceException(PointServiceException.USER_DAY_POINT_INSERT_FAILED, "user day point insert failed.uno" + uno);
                }
            } else {
                UpdateExpress increaseUserDayPointExpress = new UpdateExpress();
                increaseUserDayPointExpress.increase(UserDayPointField.POINTVALUE, -activityGoods.getGoodsPoint());
                if (userDayPointAccessor.update(increaseUserDayPointExpress, userDayPointExpress, pointActionHistory.getActionDate(), conn) <= 0) {
                    GAlerter.lab(this.getClass().getName() + " user day point increase failed.uno:" + uno + ",goodsId:" + goodsId + ",goodsItemId" + goodsItem.getGoodsItemId());
                    throw new ServiceException(PointServiceException.USER_DAY_POINT_UPDATE_FAILED, "user day point update failed.uno" + uno);
                }
            }

            try {
                insertOrUpdateActivityHotRanks(goodsId, ActivityType.GOODS.getCode());
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " insertOrUpdateActivityHotRanks occured error.e: ", e);
            }

            conn.commit();
        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + " occoured SQLException.e:", e + "pointkey=" + profileId);
            DataBaseUtil.rollbackConnection(conn);
            resetGoodsItem(conn, goodsItem);
            try {
                conn.commit();
            } catch (SQLException e1) {

                GAlerter.lab(this.getClass().getName() + " occoured SQLException.e:", e);
                throw new DbException(e1);
            }
            throw new DbException(e);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occoured ServiceException.e:", e);
            DataBaseUtil.rollbackConnection(conn);
            resetGoodsItem(conn, goodsItem);
            try {
                conn.commit();
            } catch (SQLException e1) {
                GAlerter.lab(this.getClass().getName() + " occoured SQLException.e:", e);
                throw new DbException(e1);
            }
            throw e;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return reutnObj;
    }

    private GoodsItem lockGoods(String uno, String profileId, long goodsId, Date exchangeDate, Connection conn) throws DbException {
        GoodsItem goodsItem = goodsItemAccessor.getExchangeByStatus(goodsId, ActStatus.UNACT, conn);
        if (goodsItem == null) {
            return null;
        }
        boolean itemSuccess = goodsItemAccessor.update(new UpdateExpress()
                .set(GoodsItemField.EXCHANGE_STATUS, ActStatus.ACTED.getCode())
                .set(GoodsItemField.EXCHANGE_DATE, exchangeDate)
                .set(GoodsItemField.OWN_UNO, uno).set(GoodsItemField.PROFILEID, profileId), goodsItem.getGoodsItemId(), conn) > 0;
        if (itemSuccess) {
            return goodsItem;
        } else {
            return null;
        }
    }

    private boolean resetGoodsItem(Connection conn, GoodsItem goodsItem) throws DbException {
        if (goodsItem == null) {
            return false;
        }

        return goodsItemAccessor.update(new UpdateExpress()
                .set(GoodsItemField.EXCHANGE_STATUS, ActStatus.UNACT.getCode())
                .set(GoodsItemField.EXCHANGE_DATE, null)
                .set(GoodsItemField.PROFILEID, "")
                .set(GoodsItemField.OWN_UNO, ""), goodsItem.getGoodsItemId(), conn) > 0;
    }

    public PageRows<UserConsumeLog> queryConsumeLog(String profileId, Date startDate, Date endDate, Pagination pagination) throws DbException {
        PageRows<UserConsumeLog> pageRows = null;
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<UserConsumeLog> list = userConsumeLogAccessor.queryByUser(profileId, startDate, endDate, pagination, conn);
            pageRows = new PageRows<UserConsumeLog>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<Goods> queryGoods(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return goodsAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<UserConsumeLog> queryConsumeLogByGoodsIdConsumeTime(String profileId, long goodsId, Date consumeTime) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return userConsumeLogAccessor.queryByUserGoodsIdConsumeTime(profileId, goodsId, consumeTime, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<UserConsumeLog> queryConsumeLogByGoodsId(String profileId, long goodsId, String consumeOrder) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return userConsumeLogAccessor.queryByUserGoodsId(profileId, goodsId, consumeOrder, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public List<ExchangeGoods> queryExchangeGoods(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return exchangeGoodsAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ExchangeGoods> queryExchangeGoodsByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<ExchangeGoods> pageRows = new PageRows<ExchangeGoods>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<ExchangeGoods> list = exchangeGoodsAccessor.query(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateExchangeGoods(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return exchangeGoodsAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ExchangeGoods insertExchangeGoods(ExchangeGoods exchangeGoods) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return exchangeGoodsAccessor.insert(exchangeGoods, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ExchangeGoods getExchangeGoodsById(long goodsId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return exchangeGoodsAccessor.get(goodsId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
    /////////////////////////////\

    public List<ExchangeGoodsItem> queryExchangeGoodsItem(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return exchangeGoodsItemAccessor.queryExchangeGoodsItem(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ExchangeGoodsItem> queryExchangeGoodsItemByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<ExchangeGoodsItem> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<ExchangeGoodsItem> list = exchangeGoodsItemAccessor.queryByGoodsIdExchangeStatus(queryExpress, pagination, conn);
            pageRows = new PageRows<ExchangeGoodsItem>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateExchangeGoodsItem(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return exchangeGoodsItemAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int insertExchangeGoodsItem(List<ExchangeGoodsItem> exchangeGoodsItemList) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return exchangeGoodsItemAccessor.batchInsert(exchangeGoodsItemList, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ExchangeGoodsItem getExchangeGoodsItem(long goodsItemId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return exchangeGoodsItemAccessor.get(goodsItemId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<UserExchangeLog> queryUserExchangeLog(String profileId, Date from, Date to, Pagination pagination, String appkey) throws DbException {
        Connection conn = null;
        PageRows<UserExchangeLog> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<UserExchangeLog> list = userExchangeLogAccessor.queryByUser(profileId, from, to, pagination, appkey, conn);
            pageRows = new PageRows<UserExchangeLog>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int queryUserExchangeLogByDate(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userExchangeLogAccessor.queryByDate(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserExchangeLog exhangeGoodsItem(String uno, String profileId, String appKey, long goodsId, Date exchangeDate, String exchangeIp, String userExchangeDomain, boolean isFree) throws ServiceException {
        Connection conn = null;
        UserExchangeLog exhangeLog = null;
        ExchangeGoodsItem exchangeGoodsItem = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            //判断商品是否存在
            ActivityGoods activityGoods = activityGoodsAccessor.get(new QueryExpress().add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_GOODS_ID, goodsId)), conn);
            if (activityGoods == null) {
                return exhangeLog;
            }
            conn.setAutoCommit(false);
            //礼包价格
            int giftPoint = activityGoods.getGoodsPoint();

            //从商品池获取商品
            exchangeGoodsItem = lockExchangeGoodsItem(uno, profileId, appKey, goodsId, exchangeDate, conn);
            if (exchangeGoodsItem == null) {
                GAlerter.lan(this.getClass().getName() + " lockExchangeGoodsItem faild.goodsId: " + goodsId);
                return exhangeLog;
            }
            conn.commit();
            if (!isFree) {
                if (giftPoint > 0) {
                    // 获得用户积分类型
                    PointwallWall wall = getPointwallWall(new QueryExpress().add(QueryCriterions.eq(PointwallWallAppField.APPKEY, appKey)));
                    PointKeyType pointKeyType = null;
                    if (wall != null && !StringUtil.isEmpty(wall.getPointKey())) {
                        pointKeyType = PointKeyType.getByCode(wall.getPointKey());
                    }
                    if (pointKeyType == null) {
                        pointKeyType = PointKeyType.getByCode(appKey);
                        if (pointKeyType == null) {
                            pointKeyType = PointKeyType.DEFAULT;
                        }

                    }

                    //判断是否有用户积分信息
                    UserPoint userPoint = userPointAccessor.get(new QueryExpress().add(QueryCriterions.eq(UserPointField.PROFILEID, profileId))
                            .add(QueryCriterions.eq(UserPointField.POINTKEY, pointKeyType.getValue())), conn);
                    if (userPoint == null) {
                        GAlerter.lab(this.getClass().getName() + " user point null.uno:" + uno + ",goodsId:" + goodsId);
                        throw new ServiceException(PointServiceException.USER_POINT_GET_FAILED, "user point null.uno" + uno);
                    }
                    if (userPoint.getUserPoint() < giftPoint) {
                        GAlerter.lab(this.getClass().getName() + " user point not enough.uno:" + uno + ",goodsId:" + goodsId);
                        throw new ServiceException(PointServiceException.USER_POINT_NOT_ENOUGH, "user point null.uno" + uno);
                    }


                    //用户积分 减 操作，总消费 加 操作， 兑换消费 加 操作。
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.increase(UserPointField.USERPOINT, -giftPoint);
                    updateExpress.increase(UserPointField.CONSUME_AMOUNT, giftPoint);
                    updateExpress.increase(UserPointField.CONSUME_EXCHANGE, giftPoint);

                    if (userPointAccessor.update(updateExpress, new QueryExpress().add(QueryCriterions.eq(UserPointField.PROFILEID, profileId))
                            .add(QueryCriterions.eq(UserPointField.POINTKEY, pointKeyType.getValue())), conn) <= 0) {
                        GAlerter.lab(this.getClass().getName() + " user point reduce failed.uno:" + uno + ",goodsId:" + goodsId + ",goodsItemId" + activityGoods.getActivityGoodsId());
                        throw new ServiceException(PointServiceException.USER_POINT_UPDATE_FAILED, "user point update failed.userPointId" + userPoint.getUserPointId());
                    }

                    //积分明细  记录
                    Map<String, String> paramMap = new HashMap<String, String>();
                    String descriptionTemplate = templateHotdeployConfig.getExchangePointActionHistoryTemplate();

                    paramMap.put("goodsname", activityGoods.getActivitySubject());
                    String descriptionBody = NamedTemplate.parse(descriptionTemplate).format(paramMap);

                    PointActionHistory pointActionHistory = new PointActionHistory();
                    pointActionHistory.setUserNo(uno);
                    pointActionHistory.setProfileId(profileId);
                    pointActionHistory.setCreateDate(new Date());
                    pointActionHistory.setActionDate(new Date());
                    pointActionHistory.setActionDescription(descriptionBody);
                    pointActionHistory.setActionType(PointActionType.EXCHANGE_GIFT);
                    pointActionHistory.setPointValue(-giftPoint);
                    pointActionHistory.setDestId(String.valueOf(goodsId));

                    PointActionHistory history = pointActionHistoryAccessor.insert(pointActionHistory, conn);
                    if (history == null) {
                        GAlerter.lab(this.getClass().getName() + " user point action insert failed.uno:" + uno + ",goodsId:" + goodsId + ",goodsItemId" + exchangeGoodsItem.getGoodsItemId());
                        throw new ServiceException(PointServiceException.POINT_ACTION_HISTORY_INSERT_FAILED, "point action history insert failed.uno" + uno);
                    }


                    QueryExpress userDayPointExpress = new QueryExpress();
                    userDayPointExpress.add(QueryCriterions.eq(UserDayPointField.PROFILEID, pointActionHistory.getProfileId()));
                    userDayPointExpress.add(QueryCriterions.eq(UserDayPointField.POINTACTIONTYPE, pointActionHistory.getActionType().getCode()));
                    userDayPointExpress.add(QueryCriterions.eq(UserDayPointField.POINTDATE, pointActionHistory.getActionDate()));

                    UserDayPoint userDayPoint = userDayPointAccessor.get(userDayPointExpress, pointActionHistory.getActionDate(), conn);
                    if (userDayPoint == null) {
                        userDayPoint = new UserDayPoint();
                        userDayPoint.setUserNo(uno);
                        userDayPoint.setProfileId(profileId);
                        userDayPoint.setPointValue(-giftPoint);
                        userDayPoint.setActionType(PointActionType.EXCHANGE_GIFT);
                        userDayPoint.setPointDate(new Date());

                        if (userDayPointAccessor.insert(userDayPoint, conn) == null) {
                            GAlerter.lab(this.getClass().getName() + " user day point insert failed.uno:" + uno + ",goodsId:" + goodsId + ",goodsItemId" + exchangeGoodsItem.getGoodsItemId());
                            throw new ServiceException(PointServiceException.USER_DAY_POINT_INSERT_FAILED, "user day point insert failed.uno" + uno);
                        }
                    } else {
                        UpdateExpress increaseUserDayPointExpress = new UpdateExpress();
                        increaseUserDayPointExpress.increase(UserDayPointField.POINTVALUE, -giftPoint);
                        if (userDayPointAccessor.update(increaseUserDayPointExpress, userDayPointExpress, pointActionHistory.getActionDate(), conn) <= 0) {
                            GAlerter.lab(this.getClass().getName() + " user day point increase failed.uno:" + uno + ",goodsId:" + goodsId + ",goodsItemId" + exchangeGoodsItem.getGoodsItemId());
                            throw new ServiceException(PointServiceException.USER_DAY_POINT_UPDATE_FAILED, "user day point update failed.uno" + uno);
                        }
                    }
                }
            }
            //
            if (activityGoodsAccessor.update(new UpdateExpress().increase(ActivityGoodsField.GOODS_RESETAMOUNT, -1),
                    new QueryExpress()
                            .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_GOODS_ID, goodsId))
                            .add(QueryCriterions.gt(ActivityGoodsField.GOODS_RESETAMOUNT, 0)), conn) == 1) {
                exhangeLog = new UserExchangeLog();
                exhangeLog.setUserNo(uno);
                exhangeLog.setProfileId(profileId);

                exhangeLog.setGoodsId(goodsId);
                exhangeLog.setGoodsName(activityGoods.getActivitySubject());
                exhangeLog.setGoodsDesc(activityGoods.getSubDesc());
                exhangeLog.setGoodsPic(activityGoods.getActivityPicUrl());
                exhangeLog.setGoodsType(activityGoods.getActivitygoodsType());

                exhangeLog.setGoodsItemId(exchangeGoodsItem.getGoodsItemId());
                exhangeLog.setSnName1(exchangeGoodsItem.getSnName1());
                exhangeLog.setSnName2(exchangeGoodsItem.getSnName2());
                exhangeLog.setSnValue1(exchangeGoodsItem.getSnValue1());
                exhangeLog.setSnValue2(exchangeGoodsItem.getSnValue2());
                exhangeLog.setExchangeType(UserExchangeType.GET_CODE);
                exhangeLog.setAppkey(appKey);

                exhangeLog.setExhangeTime(exchangeDate);
                exhangeLog.setExchangeIp(exchangeIp);
                exhangeLog.setExchangeDate(exchangeDate);
                exhangeLog.setExchangeDomain(UserExchangeDomain.getByCode(userExchangeDomain));
                if (!isFree) {
                    exhangeLog.setExchangePoint(giftPoint);
                } else {
                    exhangeLog.setExchangePoint(0);
                }
                exhangeLog = userExchangeLogAccessor.insert(exhangeLog, conn);


                try {
                    insertOrUpdateActivityHotRanks(goodsId, 1);
                } catch (Exception e) {
                    GAlerter.lab(this.getClass().getName() + " insertOrUpdateActivityHotRanks occured error.e: ", e);
                }

            } else {
                resetExchangeGoodsItem(conn, exchangeGoodsItem);

                //如果领号失败delete cache
                pointCache.deleteUserExchangeLog(profileId, goodsId);

                throw new ServiceException(PointServiceException.GOODS_GET_FAILED, " exchange reduce restamount falied");
            }

            conn.commit();

        } catch (SQLException e) {

            if (exchangeGoodsItem != null) {
                resetExchangeGoodsItem(conn, exchangeGoodsItem);
                try {
                    conn.commit();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            DataBaseUtil.rollbackConnection(conn);
            throw new DbException(e);
        } catch (ServiceException e) {
            DataBaseUtil.rollbackConnection(conn);
            if (exchangeGoodsItem != null) {
                resetExchangeGoodsItem(conn, exchangeGoodsItem);
                try {
                    conn.commit();
                } catch (SQLException e1) {
                    throw new DbException(e1);
                }
            }
            throw e;
        } finally {
            pointCache.deleteUserExchangeLog(profileId, goodsId);
            DataBaseUtil.closeConnection(conn);
        }
        return exhangeLog;
    }

    private void insertOrUpdateActivityHotRanks(long goodsId, int activityType) {
        HotActivityEvent hotActivityEvent = new HotActivityEvent();
        hotActivityEvent.setActivityType(activityType);
        hotActivityEvent.setGoodsId(goodsId);
        try {
            EventDispatchServiceSngl.get().dispatch(hotActivityEvent);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occoured ServiceException.e:", e);
        }
    }

//    private void insertOrUpdateActivityHotRanks(long goodsId, int activityType) {
//        try {
//            ActivityHotRanks activityHotRanks = ContentServiceSngl.get().getActivityHotRanks(new QueryExpress()
//                    .add(QueryCriterions.eq(ActivityHotRanksField.GOODS_ID, goodsId))
//                    .add(QueryCriterions.eq(ActivityHotRanksField.ACTIVITY_TYPE, activityType)));
//            if (activityHotRanks == null) {
//                activityHotRanks = new ActivityHotRanks();
//
//                ActivityRelation activityRelation = ContentServiceSngl.get().getActivityRelation(new QueryExpress().add(QueryCriterions.eq(ActivityRelationField.ACTIVITY_TYPE, activityType))
//                        .add(QueryCriterions.eq(ActivityRelationField.ACTIVITY_DIRECTID, goodsId)));
//
//                Activity activity = ContentServiceSngl.get().getActivityById(activityRelation.getActivityId());
//                activityHotRanks.setGoodsId(goodsId);
//                activityHotRanks.setLastExchangeTime(new Date());
//                activityHotRanks.setExchange_num(1);
//                activityHotRanks.setActivityType(ActivityType.getByCode(activityType));
//                activityHotRanks.setActivityId(activity.getActivityId());
//                activityHotRanks.setActivityName(activity.getActivitySubject());
//                activityHotRanks.setPic(activity.getActivityPicUrl());
//                activityHotRanks.setRemoveStatus(ActStatus.UNACT);
//                ContentServiceSngl.get().insertActivityHotRanks(activityHotRanks);
//            } else {
//                long now = System.currentTimeMillis();
//                long sevenDays = 60 * 60 * 24 * 7 * 1000;
//                long lastExchangeTime = activityHotRanks.getLastExchangeTime().getTime();
//                if ((sevenDays + lastExchangeTime) > now) {
//                    ContentServiceSngl.get().modifyActivityHotRanks(new UpdateExpress().increase(ActivityHotRanksField.EXCHANGE_NUM, 1)
//                            .set(ActivityHotRanksField.LAST_EXCHANGE_TIME, new Date())
//                            .set(ActivityHotRanksField.REMOVE_STATUS, ActStatus.UNACT.getCode()),
//                            new QueryExpress().add(QueryCriterions.eq(ActivityHotRanksField.GOODS_ID, goodsId))
//                                    .add(QueryCriterions.eq(ActivityHotRanksField.ACTIVITY_TYPE, activityType)));
//                } else {
//                    ContentServiceSngl.get().modifyActivityHotRanks(new UpdateExpress()
//                            .set(ActivityHotRanksField.EXCHANGE_NUM, 1)
//                            .set(ActivityHotRanksField.LAST_EXCHANGE_TIME, new Date())
//                            .set(ActivityHotRanksField.REMOVE_STATUS, ActStatus.UNACT.getCode()),
//                            new QueryExpress().add(QueryCriterions.eq(ActivityHotRanksField.GOODS_ID, goodsId))
//                                    .add(QueryCriterions.eq(ActivityHotRanksField.ACTIVITY_TYPE, activityType)));
//                }
//            }
//        } catch (ServiceException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//    }

    //
    private ExchangeGoodsItem lockExchangeGoodsItem(String uno, String profileId, String appKey, long goodsId, Date exchangeDate, Connection conn) throws DbException {
        ExchangeGoodsItem exchangeGoodsItem = exchangeGoodsItemAccessor.getExchangeByStatus(goodsId, ActStatus.UNACT, conn);
        if (exchangeGoodsItem == null) {
            return null;
        }
        boolean itemSuccess = exchangeGoodsItemAccessor.update(new UpdateExpress()
                .set(ExchangeGoodsItemField.EXCHANGE_STATUS, ActStatus.ACTED.getCode())
                .set(ExchangeGoodsItemField.EXCHANGE_TIME, exchangeDate)
                .set(ExchangeGoodsItemField.PROFILEID, profileId)
                .set(ExchangeGoodsItemField.OWN_USER_NO, uno), new QueryExpress().add(QueryCriterions.eq(ExchangeGoodsItemField.EXCHANGE_GOODS_ITEM_ID, exchangeGoodsItem.getGoodsItemId())), conn) > 0;
//        if(itemSuccess){
//            return exchangeGoodsItem;
//        }

        return exchangeGoodsItem;
    }

    private boolean resetExchangeGoodsItem(Connection conn, ExchangeGoodsItem exchangeGoodsItem) throws DbException {
        return exchangeGoodsItemAccessor.update(new UpdateExpress()
                .set(ExchangeGoodsItemField.EXCHANGE_STATUS, ActStatus.UNACT.getCode())
                .set(ExchangeGoodsItemField.EXCHANGE_TIME, null)
                .set(ExchangeGoodsItemField.PROFILEID, "")
                .set(ExchangeGoodsItemField.OWN_USER_NO, ""), new QueryExpress().add(QueryCriterions.eq(ExchangeGoodsItemField.EXCHANGE_GOODS_ITEM_ID, exchangeGoodsItem.getGoodsItemId())), conn) > 0;
    }

    public UserExchangeLog insertExchangeLog(UserExchangeLog userExchangeLog) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return userExchangeLogAccessor.insert(userExchangeLog, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<UserExchangeLog> queryUserExchangeLogByUno(String uno, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<UserExchangeLog> page = new PageRows<UserExchangeLog>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<UserExchangeLog> list = userExchangeLogAccessor.queryByUno(uno, pagination, conn);
            page.setPage(pagination);
            page.setRows(list);
            return page;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public PageRows<UserExchangeLog> queryUserExchangeLogByPageRows(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<UserExchangeLog> page = new PageRows<UserExchangeLog>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<UserExchangeLog> list = userExchangeLogAccessor.query(queryExpress, pagination, conn);
            page.setPage(pagination);
            page.setRows(list);
            return page;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<UserExchangeLog> queryUserExchangeByQueryExress(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userExchangeLogAccessor.queryByQueryExpress(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<UserConsumeLog> queryConsumeLogByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userConsumeLogAccessor.query(queryExpress, pagination, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<UserConsumeLog> queryConsumeByPage(QueryExpress queryExpress, Pagination pagination, GoodsActionType type) throws DbException {
        Connection conn = null;
        PageRows<UserConsumeLog> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<UserConsumeLog> list = userConsumeLogAccessor.queryUserConsumeLog(queryExpress, pagination, type, conn);
            pageRows = new PageRows<UserConsumeLog>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return pageRows;
    }

    public List<UserExchangeLog> queryUserExchangeByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userExchangeLogAccessor.query(queryExpress, pagination, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserExchangeLog getUserExchangeLog(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userExchangeLogAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    @Deprecated
    public Connection getConn() throws DbException {
        return DbConnFactory.factory(dataSourceName);
    }

    public boolean increaseSmsCountExchangLog(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userExchangeLogAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /**
     * 礼包预定 Start
     */

    public List<GiftReserve> queryGiftReserveByList(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return giftReserveAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<GiftReserve> queryGiftReserveByPaga(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<GiftReserve> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<GiftReserve> list = giftReserveAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<GiftReserve>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return pageRows;
    }

    public GiftReserve createGiftReserve(GiftReserve giftReserve) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return giftReserveAccessor.insert(giftReserve, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyGiftReserve(UpdateExpress updateExpress, QueryExpress queryExperss) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return giftReserveAccessor.update(updateExpress, queryExperss, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GiftReserve getGiftReserve(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return giftReserveAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    //####################################积分墙 积份墙列表 start
    public PageRows<PointwallWall> queryPointwallWallByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<PointwallWall> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<PointwallWall> list = pointwallWallAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<PointwallWall>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return pageRows;
    }


    public List<PointwallWall> queryPointwallWall(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        List<PointwallWall> list = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            list = pointwallWallAccessor.queryAll(queryExpress, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return list;
    }

    public int countPointwallWall(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointwallWallAccessor.queryTotalWalls(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public PointwallWall insertPointwallWall(PointwallWall pointwallWall) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointwallWallAccessor.insert(pointwallWall, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updatePointwallWall(UpdateExpress updateExpress, QueryExpress queryExperss) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointwallWallAccessor.update(updateExpress, queryExperss, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int deletePointwallWall(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointwallWallAccessor.delete(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PointwallWall getPointwallWall(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointwallWallAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //#################################### 积分墙列表维护 结束

    /**
     * 积分墙 app管理
     */

    public PageRows<PointwallApp> queryPointwallAppByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<PointwallApp> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<PointwallApp> list = pointwallAppAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<PointwallApp>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return pageRows;
    }

    public PointwallApp insertPointwallApp(PointwallApp pointwallApp) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointwallAppAccessor.insert(pointwallApp, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updatePointwallApp(UpdateExpress updateExpress, QueryExpress queryExperss) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointwallAppAccessor.update(updateExpress, queryExperss, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int deletePointwallApp(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointwallAppAccessor.delete(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PointwallApp getPointwallApp(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointwallAppAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //pengxu-laptop-joyme-platform/framework/src/main/java/com/enjoyf/platform/db/point/PointHandler.java
    /*
     积分墙app管理结束
 */


    //####################################单个积份墙app列表管理 start
    public int countPointwallWallAppTotalOfApps(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointwallWallAppAccessor.queryTotalOfApps(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public int countPointwallWallAppOfOneWall(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointwallWallAppAccessor.queryTotalOfAppsOfOneWall(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<PointwallWallApp> queryPointwallWallAppByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<PointwallWallApp> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<PointwallWallApp> list = pointwallWallAppAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<PointwallWallApp>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return pageRows;
    }


    public List<PointwallWallApp> queryPointwallWallAppAll(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        List<PointwallWallApp> list = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            list = pointwallWallAppAccessor.queryAll(queryExpress, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return list;
    }

    public PointwallWallApp insertPointwallWallApp(PointwallWallApp pointwallWallApp) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointwallWallAppAccessor.insert(pointwallWallApp, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updatePointwallWallApp(UpdateExpress updateExpress, QueryExpress queryExperss) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointwallWallAppAccessor.update(updateExpress, queryExperss, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int deletePointwallWallApp(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointwallWallAppAccessor.delete(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PointwallWallApp getPointwallWallApp(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointwallWallAppAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
    //####################################单个积份墙app列表管理    end

    //#################################point.pw_tasklog管理    start
    public PageRows<PointwallTasklog> queryPointwallTasklogByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<PointwallTasklog> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<PointwallTasklog> list = pointwallTasklogAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<PointwallTasklog>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return pageRows;
    }

    //查询符合查询条件的所有记录的数量
    public int countPointwallTasklog(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointwallTasklogAccessor.countTotal(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    //查询一个符合一定条件的所有记录,非分页,用于数据导出
    public List<PointwallTasklog> queryPointwallTasklogAll(QueryExpress queryExpress, int startIndex, int size) throws DbException {
        Connection conn = null;
        List<PointwallTasklog> list = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            list = pointwallTasklogAccessor.queryAll(queryExpress, startIndex, size, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return list;
    }


    public PointwallTasklog insertPointwallTasklog(PointwallTasklog pointwallTasklog) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointwallTasklogAccessor.insert(pointwallTasklog, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PointwallTasklog getPointwallTasklog(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pointwallTasklogAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateExchangeLog(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userExchangeLogAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateConsumeLog(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userConsumeLogAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateGoodsItem(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return goodsItemAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public List<GoodsItem> queryGoodsItem(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return goodsItemAccessor.queryGoodsItem(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public PageRows<ActivityGoods> queryActivityGoodsByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<ActivityGoods> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<ActivityGoods> list = activityGoodsAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<ActivityGoods>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return pageRows;
    }

    public List<ActivityGoods> queryActivityGoods(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityGoodsAccessor.query(queryExpress, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ActivityGoods getActivityGoods(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityGoodsAccessor.get(queryExpress, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyActivityGoods(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityGoodsAccessor.update(updateExpress, queryExpress, conn) > 0;

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ActivityGoods createActivity(ActivityGoods activityGoods) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityGoodsAccessor.insert(activityGoods, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int countActivitySum(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityGoodsAccessor.queryCount(queryExpress, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ActivityHotRanks insertActivityHotRanks(ActivityHotRanks activityHotRanks) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityHotRanksAccessor.insert(activityHotRanks, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyActivityHotRanks(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityHotRanksAccessor.modify(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ActivityHotRanks> activityHotRanksList(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityHotRanksAccessor.select(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ActivityHotRanks> activityHotRanksPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<ActivityHotRanks> pageRows = new PageRows<ActivityHotRanks>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<ActivityHotRanks> list = activityHotRanksAccessor.select(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return pageRows;
    }

    public ActivityHotRanks getActivityHotRanks(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityHotRanksAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<UserConsumeLog> queryConsumeLogList(QueryExpress queryExpress, GoodsActionType type) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userConsumeLogAccessor.queryUserConsumeLog(queryExpress, null, type, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GoodsSeckill createGoodsSeckill(GoodsSeckill goodsSeckill) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return goodsSeckillAccessor.insert(goodsSeckill, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GoodsSeckill getGoodsSeckill(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return goodsSeckillAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<GoodsSeckill> queryGoodsSeckill(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return goodsSeckillAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<GoodsSeckill> queryGoodsSeckillByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<GoodsSeckill> list = goodsSeckillAccessor.query(queryExpress, pagination, conn);
            PageRows<GoodsSeckill> pageRows = new PageRows<GoodsSeckill>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyGoodsSeckill(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return goodsSeckillAccessor.update(queryExpress, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean modifyGiftLottery(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return giftLotteryAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GiftLottery insertLottery(GiftLottery giftLottery) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return giftLotteryAccessor.insert(giftLottery, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GiftLottery getGiftLottery(QueryExpress queryExpres) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return giftLotteryAccessor.get(queryExpres, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<GiftLottery> queryGiftLottery(QueryExpress queryExpres) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return giftLotteryAccessor.query(queryExpres, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<GiftLottery> queryGiftLotteryByPage(QueryExpress queryExpres,Pagination pagination) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            PageRows<GiftLottery> pageRows=new PageRows<GiftLottery>();
            pageRows.setRows(giftLotteryAccessor.query(queryExpres, conn));
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public UserLotteryLog insertUserLotteryLog(UserLotteryLog userLotteryLog) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryLogAccessor.insert(userLotteryLog, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserLotteryLog getUserLotteryLog(QueryExpress queryExpres) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryLogAccessor.get(queryExpres, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<UserLotteryLog> queryUserLotteryLog(QueryExpress queryExpres) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryLogAccessor.query(queryExpres, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<UserLotteryLog> queryUserLotteryLogByPage(QueryExpress queryExpres,Pagination pagination) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            PageRows<UserLotteryLog> pageRows=new PageRows<UserLotteryLog>();
            pageRows.setRows(lotteryLogAccessor.query(queryExpres, conn));
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

}