package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.point.ConsumeType;
import com.enjoyf.platform.service.point.GoodsType;
import com.enjoyf.platform.service.point.UserConsumeLog;
import com.enjoyf.platform.service.point.UserConsumeLogField;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-17
 * Time: 下午5:14
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractUserConsumeLogAccessor extends AbstractSequenceBaseTableAccessor<UserConsumeLog> implements UserConsumeLogAccessor {
    private Logger logger = LoggerFactory.getLogger(AbstractUserConsumeLogAccessor.class);

    private static final String KEY_TABLE_NAME = "user_consume_log";
    private static final String CONSUME_ORDER = "CONSUME_ORDER";

    @Override
    public UserConsumeLog insert(UserConsumeLog userConsumeLog, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            userConsumeLog.setConsumeOrder(getSeqNo(CONSUME_ORDER, conn));
            long consumeTimestamp = userConsumeLog.getConsumeDate() == null ? System.currentTimeMillis() : userConsumeLog.getConsumeDate().getTime();
            //user_consume_log_id,user_no,goods_id,goods_name,goods_pic,goods_desc,goods_item_id,goods_type,consume_date,consume_time,consume_ip
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, userConsumeLog.getUserNo());
            pstmt.setLong(2, userConsumeLog.getGoodsId());
            pstmt.setString(3, userConsumeLog.getGoodsName());
            pstmt.setString(4, userConsumeLog.getGoodsPic());
            pstmt.setString(5, userConsumeLog.getGoodsDesc());
            pstmt.setLong(6, userConsumeLog.getGoodsItemId());
            pstmt.setInt(7, userConsumeLog.getGoodsType().getCode());

            pstmt.setTimestamp(8, new Timestamp(consumeTimestamp));
            pstmt.setDate(9, new java.sql.Date(consumeTimestamp));
            pstmt.setString(10, userConsumeLog.getConsumeIp());
            pstmt.setInt(11, userConsumeLog.getConsumeAmount());
            pstmt.setString(12, userConsumeLog.getConsumeType().getCode());
            pstmt.setString(13, userConsumeLog.getProfileId());
            pstmt.setInt(14, userConsumeLog.getGoodsActionType().getCode());
            pstmt.setString(15, userConsumeLog.getAppkey());
            pstmt.setString(16, userConsumeLog.getValidStatus().getCode());
            pstmt.setString(17, userConsumeLog.getAddress() == null ? "" : userConsumeLog.getAddress().toJsonStr());
            pstmt.setLong(18, userConsumeLog.getConsumeOrder());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                userConsumeLog.setUserConsumeLogId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + "On insert , a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return userConsumeLog;
    }

    @Override
    public List<UserConsumeLog> queryByUser(String profileId, Date from, Date to, Pagination pagination, Connection conn) throws DbException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(UserConsumeLogField.PROFILEID, profileId));
        if (from != null) {
            queryExpress.add(QueryCriterions.geq(UserConsumeLogField.CONSUME_TIME, from));
        }
        if (to != null) {
            queryExpress.add(QueryCriterions.leq(UserConsumeLogField.CONSUME_TIME, to));
        }
        queryExpress.add(QuerySort.add(UserConsumeLogField.CONSUME_DATE, QuerySortOrder.DESC));
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public List<UserConsumeLog> queryByUserGoodsIdConsumeTime(String profileId, long goodsId, Date consumeTime, Connection conn) throws DbException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(UserConsumeLogField.PROFILEID, profileId));
        queryExpress.add(QueryCriterions.eq(UserConsumeLogField.GOODS_ID, goodsId));
        queryExpress.add(QueryCriterions.eq(UserConsumeLogField.CONSUME_TIME, consumeTime));
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<UserConsumeLog> queryByUserGoodsId(String profileId, long goodsId, String consumeOrder, Connection conn) throws DbException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(UserConsumeLogField.PROFILEID, profileId));
        queryExpress.add(QueryCriterions.eq(UserConsumeLogField.GOODS_ID, goodsId));
        if (!StringUtil.isEmpty(consumeOrder)) {
            queryExpress.add(QueryCriterions.eq(UserConsumeLogField.CONSUMEORDER, Long.parseLong(consumeOrder)));
        }
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<UserConsumeLog> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<UserConsumeLog> queryUserConsumeLog(QueryExpress queryExpress, Pagination pagination, GoodsActionType type, Connection conn) throws DbException {
        if (pagination == null) {
            return query(KEY_TABLE_NAME, queryExpress, conn);
        }
        List<UserConsumeLog> returnValue = new ArrayList<UserConsumeLog>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "";

        if (!type.equals(GoodsActionType.GIFT)) {
            sql = "SELECT * FROM "
                    + KEY_TABLE_NAME + "  " + ObjectFieldUtil.generateQueryClause(queryExpress, true) + "  LIMIT ?, ?";
        } else {
            sql = "SELECT DISTINCT(goods_id) FROM "
                    + KEY_TABLE_NAME + "  " + ObjectFieldUtil.generateQueryClause(queryExpress, true) + "  LIMIT ?, ?";
        }


        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            //
            if (type.equals(GoodsActionType.GIFT)) {
                pagination.setTotalRows(queryDistinctRowSize(queryExpress, conn));
            } else {
                pagination.setTotalRows(queryRowSize(KEY_TABLE_NAME, queryExpress, conn));
            }

            //
            pstmt = conn.prepareStatement(sql);

            int index = 1;
            index = ObjectFieldUtil.setStmtParams(pstmt, 1, queryExpress);
            pstmt.setInt(index++, pagination.getStartRowIdx());
            pstmt.setInt(index++, pagination.getPageSize());

            rs = pstmt.executeQuery();

            if (type.equals(GoodsActionType.MIDOU)) {
                while (rs.next()) {
                    returnValue.add(rsToObject(rs));
                }
            } else {
                while (rs.next()) {
                    UserConsumeLog userConsumeLog = new UserConsumeLog();
                    userConsumeLog.setGoodsId(rs.getLong("goods_id"));
                    returnValue.add(userConsumeLog);
                }
            }

        } catch (SQLException e) {
            GAlerter.lab("On query, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }


        return returnValue;
    }

    private int queryDistinctRowSize(QueryExpress queryExpress, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(DISTINCT(goods_id)) FROM " + KEY_TABLE_NAME + "  " + ObjectFieldUtil.generateQueryClause(queryExpress, true);

        if (logger.isDebugEnabled()) {
            logger.debug("The queryRowSize sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);
            ObjectFieldUtil.setStmtParams(pstmt, 1, queryExpress);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(user_no,goods_id,goods_name,goods_pic,goods_desc,goods_item_id,goods_type,consume_date,consume_time,consume_ip,consume_amount,consume_type,profileid,goods_action_type,appkey,valid_status,address,consume_order) VALUES(?,?,?,?,?, ?,? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("UserConsumeLog insert sql:" + sql);
        }

        return sql;
    }

    @Override
    protected UserConsumeLog rsToObject(ResultSet rs) throws SQLException {
        UserConsumeLog userConsumeLog = new UserConsumeLog();

        userConsumeLog.setUserConsumeLogId(rs.getLong("user_consume_log_id"));
        userConsumeLog.setUserNo(rs.getString("user_no"));

        userConsumeLog.setGoodsId(rs.getLong("goods_id"));
        userConsumeLog.setGoodsName(rs.getString("goods_name"));
        userConsumeLog.setGoodsDesc(rs.getString("goods_desc"));
        userConsumeLog.setGoodsPic(rs.getString("goods_pic"));
        userConsumeLog.setGoodsType(GoodsType.getByCode(rs.getInt("goods_type")));

        userConsumeLog.setGoodsItemId(rs.getLong("goods_item_id"));

        userConsumeLog.setConsumeAmount(rs.getInt("consume_amount"));
        userConsumeLog.setConsumeType(ConsumeType.getByCode(rs.getString("consume_type")));
        userConsumeLog.setConsumeDate(rs.getTimestamp("consume_date"));
        userConsumeLog.setConsumeTime(rs.getDate("consume_time"));
        userConsumeLog.setConsumeIp(rs.getString("consume_ip"));
        userConsumeLog.setProfileId(rs.getString("profileid"));
        userConsumeLog.setAppkey(rs.getString("appkey"));
        userConsumeLog.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        userConsumeLog.setGoodsActionType(GoodsActionType.getByCode(rs.getInt("goods_action_type")));
        userConsumeLog.setAddress(UserConsumeLog.parse(rs.getString("address")));
        userConsumeLog.setConsumeOrder(rs.getLong("consume_order"));
        return userConsumeLog;
    }
}
