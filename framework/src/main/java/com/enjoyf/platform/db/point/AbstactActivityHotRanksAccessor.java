package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.point.ActivityHotRanks;
import com.enjoyf.platform.service.content.ActivityType;
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
 * User: ericliu
 * Date: 13-7-12
 * Time: 下午4:15
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstactActivityHotRanksAccessor extends AbstractBaseTableAccessor<ActivityHotRanks> implements ActivityHotRanksAccessor {

    Logger logger = LoggerFactory.getLogger(AbstactActivityHotRanksAccessor.class);

    private static final String KEY_TABLE_NAME = "activity_hot_ranks";

    @Override
    public ActivityHotRanks insert(ActivityHotRanks activityHotRanks, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, activityHotRanks.getGoodsId());
            pstmt.setInt(2, activityHotRanks.getExchange_num());
            pstmt.setInt(3, activityHotRanks.getActivityType().getCode());
            pstmt.setLong(4, activityHotRanks.getActivityId());
            pstmt.setString(5, activityHotRanks.getActivityName());
            pstmt.setString(6, activityHotRanks.getPic());
            pstmt.setTimestamp(7, new Timestamp(activityHotRanks.getLastExchangeTime().getTime()));
            pstmt.setString(8, activityHotRanks.getRemoveStatus().getCode());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                activityHotRanks.setActivityHotRanksId(rs.getLong(1));
            }
            return activityHotRanks;
        } catch (SQLException e) {
            GAlerter.lab("On insert Content, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME
                + " (goods_id,exchange_num,activity_type,activity_id,activity_name,pic,last_exchange_time,remove_status) " + "VALUES (?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ActivityHotRanks INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    @Override
    protected ActivityHotRanks rsToObject(ResultSet rs) throws SQLException {
        ActivityHotRanks returnObj = new ActivityHotRanks();
        returnObj.setActivityHotRanksId(rs.getLong("activity_hot_ranks_id"));
        returnObj.setGoodsId(rs.getLong("goods_id"));
        returnObj.setExchange_num(rs.getInt("exchange_num"));
        returnObj.setActivityType(ActivityType.getByCode(rs.getInt("activity_type")));
        returnObj.setActivityId(rs.getLong("activity_id"));
        returnObj.setActivityName(rs.getString("activity_name"));
        returnObj.setPic(URLUtils.getJoymeDnUrl(rs.getString("pic")));
        returnObj.setLastExchangeTime(rs.getTimestamp("last_exchange_time"));
        returnObj.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));
        return returnObj;
    }

    @Override
    public ActivityHotRanks get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ActivityHotRanks> select(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ActivityHotRanks> select(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int modify(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }
}
