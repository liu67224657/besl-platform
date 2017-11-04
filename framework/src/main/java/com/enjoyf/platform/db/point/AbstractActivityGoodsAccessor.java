package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.content.MobileExclusive;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.text.TextJsonItem;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午4:38
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractActivityGoodsAccessor extends AbstractBaseTableAccessor<ActivityGoods> implements ActivityGoodsAccessor {

    private Logger logger = LoggerFactory.getLogger(AbstractActivityGoodsAccessor.class);

    protected static final String KEY_TABLE_NAME = "activity_goods";


    @Override
    public ActivityGoods insert(ActivityGoods goods, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, goods.getGameDbId());
            pstmt.setString(2, goods.getActivitySubject());
            pstmt.setString(3, goods.getActivityDesc());
            pstmt.setString(4, TextJsonItem.toJson(goods.getTextJsonItemsList()));
            pstmt.setString(5, goods.getActivityPicUrl());
            pstmt.setInt(6, goods.getActivityType().getCode());
            pstmt.setInt(7, goods.getChannelType().getCode());
            pstmt.setTimestamp(8, new Timestamp(goods.getStartTime() == null ? System.currentTimeMillis() : goods.getStartTime().getTime()));
            pstmt.setTimestamp(9, new Timestamp(goods.getEndTime() == null ? System.currentTimeMillis() : goods.getEndTime().getTime()));
            pstmt.setLong(10, goods.getPassiveShareId());
            pstmt.setString(11, goods.getFirstLetter());
            pstmt.setInt(12, goods.getHotActivity().getCode());
            pstmt.setInt(13, goods.getReserveType());
            pstmt.setInt(14, goods.getActivitygoodsType().getCode());
            pstmt.setInt(15, goods.getGoodsAmount());
            pstmt.setInt(16, goods.getGoodsResetAmount());
            pstmt.setInt(17, goods.getGoodsPoint());
            pstmt.setInt(18, goods.getTimeType().getCode());
            pstmt.setInt(19, goods.getTaoTimesType().getCode());
            pstmt.setInt(20, goods.getDisplayType());
            pstmt.setInt(21, goods.getDisplayOrder());
            pstmt.setString(22, goods.getActStatus().getCode());
            pstmt.setString(23, goods.getCreateUserId());
            pstmt.setString(24, goods.getCreateIp());
            pstmt.setTimestamp(25, new Timestamp(goods.getCreateTime() == null ? System.currentTimeMillis() : goods.getCreateTime().getTime()));
            pstmt.setLong(26, goods.getActivityGoodsId());
            pstmt.setString(27, goods.getSubDesc());
            pstmt.setInt(28, goods.getGoodsActionType().getCode());
            pstmt.setString(29, goods.getBgPic());
            pstmt.setInt(30, goods.getPlatform().getCode());
            pstmt.setInt(31, goods.getSeckilltype().getCode());
            pstmt.setString(32, goods.getSecKill() == null ? null : goods.getSecKill().toJson());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                goods.setActivityGoodsId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert activityGoods, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return goods;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<ActivityGoods> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        if (pagination == null) {
            return super.query(KEY_TABLE_NAME, queryExpress, conn);
        } else {
            return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
        }
    }

    @Override
    public List<ActivityGoods> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public ActivityGoods get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int queryCount(QueryExpress queryExpress, Connection conn) throws DbException {
        return queryRowSize(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected ActivityGoods rsToObject(ResultSet rs) throws SQLException {
        ActivityGoods activityGoods = new ActivityGoods();

        activityGoods.setActivityGoodsId(rs.getLong("activity_goods_id"));
        activityGoods.setActivitySubject(rs.getString("activity_subject"));
        activityGoods.setGameDbId(rs.getLong("game_db_id"));
        activityGoods.setActivityDesc(rs.getString("activity_desc"));
        activityGoods.setTextJsonItemsList(TextJsonItem.fromJson(rs.getString("activity_descjson")));
        activityGoods.setActivityPicUrl(URLUtils.getJoymeDnUrl(rs.getString("activity_picurl")));
        activityGoods.setActivityType(ActivityType.getByCode(rs.getInt("activity_type")));
        activityGoods.setChannelType(MobileExclusive.getByCode(rs.getInt("channel_type")));
        activityGoods.setStartTime(rs.getTimestamp("start_time"));
        activityGoods.setEndTime(rs.getTimestamp("end_time"));
        activityGoods.setPassiveShareId(rs.getLong("passive_share_id"));
        activityGoods.setFirstLetter(rs.getString("first_letter"));
        activityGoods.setHotActivity(ChooseType.getByCode(rs.getInt("hot_activity")));
        activityGoods.setReserveType(rs.getInt("reserve_type"));
        activityGoods.setActivitygoodsType(GoodsType.getByCode(rs.getInt("activity_goods_type")));
        activityGoods.setGoodsAmount(rs.getInt("goods_amount"));
        activityGoods.setGoodsResetAmount(rs.getInt("goods_resetamount"));
        activityGoods.setGoodsPoint(rs.getInt("goods_point"));
        activityGoods.setTimeType(ConsumeTimesType.getByCode(rs.getInt("time_type")));
        activityGoods.setTaoTimesType(ConsumeTimesType.getByCode(rs.getInt("tao_time_type")));
        activityGoods.setDisplayType(rs.getInt("display_type"));
        activityGoods.setDisplayOrder(rs.getInt("display_order"));
        activityGoods.setActStatus(ActStatus.getByCode(rs.getString("remove_status")));
        activityGoods.setCreateUserId(rs.getString("createuserid"));
        activityGoods.setCreateIp(rs.getString("createip"));
        activityGoods.setCreateTime(rs.getTimestamp("createdate"));
        activityGoods.setSubDesc(rs.getString("sub_desc"));
        activityGoods.setGoodsActionType(GoodsActionType.getByCode(rs.getInt("goods_action_type")));
        activityGoods.setBgPic(rs.getString("bg_pic"));
        activityGoods.setPlatform(AppPlatform.getByCode(rs.getInt("platform")));
        activityGoods.setSeckilltype(ChooseType.getByCode(rs.getInt("seckill_type")));
        activityGoods.setSecKill(SecKill.fromJson(rs.getString("seckill_info")));
        return activityGoods;
    }

    public String getInsertSql() {
        String sql = "insert into " + KEY_TABLE_NAME + "(game_db_id,activity_subject,activity_desc,activity_descjson,activity_picurl,activity_type,channel_type,start_time,end_time,passive_share_id" +
                ",first_letter,hot_activity,reserve_type,activity_goods_type,goods_amount,goods_resetamount,goods_point,time_type,tao_time_type,display_type" +
                ",display_order,remove_status,createuserid,createip,createdate,activity_goods_id,sub_desc,goods_action_type,bg_pic,platform,seckill_type,seckill_info) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ActivityGoods insert sql:" + sql);
        }
        return sql;
    }

}
