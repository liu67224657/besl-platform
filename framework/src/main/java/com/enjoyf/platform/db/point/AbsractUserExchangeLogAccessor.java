package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.*;
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
 * User: zhitaoshi
 * Date: 13-6-7
 * Time: 下午2:05
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbsractUserExchangeLogAccessor extends AbstractBaseTableAccessor<UserExchangeLog> implements UserExchangeLogAccessor {
    private Logger logger = LoggerFactory.getLogger(AbsractUserExchangeLogAccessor.class);

    private static final String KEY_TABLE_NAME = "user_exchange_log";

    @Override
    public UserExchangeLog insert(UserExchangeLog userExchangeLog, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            long consumeTimestamp = userExchangeLog.getExchangeDate() == null ? System.currentTimeMillis() : userExchangeLog.getExchangeDate().getTime();
            //user_no,exchange_goods_id, exchange_goods_name,exchange_goods_desc, exchange_goods_pic,exchange_goods_type,
            // exchange_goods_item_id, sn_name1, sn_value1,sn_name2,sn_value2,exchange_type,exchange_date, exchange_time, exchange_ip
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, userExchangeLog.getUserNo());
            pstmt.setLong(2, userExchangeLog.getGoodsId());
            pstmt.setString(3, userExchangeLog.getGoodsName());
            pstmt.setString(4, userExchangeLog.getGoodsDesc());
            pstmt.setString(5, userExchangeLog.getGoodsPic());
            pstmt.setInt(6, userExchangeLog.getGoodsType().getCode());
            pstmt.setLong(7, userExchangeLog.getGoodsItemId());
            pstmt.setString(8, userExchangeLog.getSnName1());
            pstmt.setString(9, userExchangeLog.getSnValue1());
            pstmt.setString(10, userExchangeLog.getSnName2());
            pstmt.setString(11, userExchangeLog.getSnValue2());
            pstmt.setString(12, userExchangeLog.getExchangeType().getCode());
            pstmt.setDate(13, new java.sql.Date(consumeTimestamp));
            pstmt.setTimestamp(14, new Timestamp(consumeTimestamp));
            pstmt.setString(15, userExchangeLog.getExchangeIp());
            pstmt.setString(16, userExchangeLog.getExchangeDomain().getCode());
            pstmt.setString(17, userExchangeLog.getProfileId());
            pstmt.setInt(18, userExchangeLog.getExchangePoint());
            pstmt.setString(19, userExchangeLog.getAppkey());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                userExchangeLog.setUserExchangeLogId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.error(this.getClass().getName() + "On insert , a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return userExchangeLog;
    }

    @Override
    public List<UserExchangeLog> queryByUser(String profileId, Date from, Date to, Pagination pagination, String appkey, Connection conn) throws DbException {
        QueryExpress queryExpress = new QueryExpress();
        if (!StringUtil.isEmpty(appkey)) {
            queryExpress.add(QueryCriterions.eq(UserExchangeLogField.APPKEY, appkey));
        }
        queryExpress.add(QueryCriterions.eq(UserExchangeLogField.PROFILEID, profileId));
        if (from != null) {
            queryExpress.add(QueryCriterions.geq(UserExchangeLogField.EXCHANGE_TIME, from));
        }
        if (to != null) {
            queryExpress.add(QueryCriterions.leq(UserExchangeLogField.EXCHANGE_TIME, to));
        }
        queryExpress.add(QuerySort.add(UserExchangeLogField.EXCHANGE_TIME, QuerySortOrder.DESC));
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public List<UserExchangeLog> queryByUserGoodsIdConsumeTime(String uno, long goodsId, Date consumeTime, Connection conn) throws DbException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(UserExchangeLogField.USER_NO, uno));
        queryExpress.add(QueryCriterions.eq(UserExchangeLogField.EXCHANGE_GOODS_ID, goodsId));
        queryExpress.add(QueryCriterions.eq(UserExchangeLogField.EXCHANGE_TIME, consumeTime));
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    //为手机客户端提供接口
    @Override
    public List<UserExchangeLog> queryByUno(String uno, Pagination pagination, Connection conn) throws DbException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(UserExchangeLogField.USER_NO, uno));
        if (pagination == null) {
            return query(KEY_TABLE_NAME, queryExpress, conn);
        }
        List<UserExchangeLog> returnValue = new ArrayList<UserExchangeLog>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT DISTINCT(exchange_goods_id),exchange_goods_id,exchange_goods_name,exchange_goods_desc,exchange_goods_pic FROM "
                + KEY_TABLE_NAME + "  " + ObjectFieldUtil.generateQueryClause(queryExpress, true) + " ORDER BY user_exchange_log_id DESC LIMIT ?, ?";
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            //
            pagination.setTotalRows(queryDistinctRowSize(uno, conn));

            //
            pstmt = conn.prepareStatement(sql);

            int index = 1;
            index = ObjectFieldUtil.setStmtParams(pstmt, 1, queryExpress);
            pstmt.setInt(index++, pagination.getStartRowIdx());
            pstmt.setInt(index++, pagination.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                UserExchangeLog userExchangeLog = new UserExchangeLog();
                userExchangeLog.setGoodsId(rs.getLong("exchange_goods_id"));
                userExchangeLog.setGoodsName(rs.getString("exchange_goods_name"));
                userExchangeLog.setGoodsDesc(rs.getString("exchange_goods_desc"));
                userExchangeLog.setGoodsPic(rs.getString("exchange_goods_pic"));
                returnValue.add(userExchangeLog);
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

    private int queryDistinctRowSize(String uno, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(DISTINCT(exchange_goods_id)) FROM " + KEY_TABLE_NAME + " WHERE user_no=?";
        if (logger.isDebugEnabled()) {
            logger.debug("The queryRowSize sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, uno);

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


    @Override
    public List<UserExchangeLog> queryByUserGoodsId(String uno, long goodsId, Connection conn) throws DbException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(UserExchangeLogField.USER_NO, uno));
        queryExpress.add(QueryCriterions.eq(UserExchangeLogField.EXCHANGE_GOODS_ID, goodsId));
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<UserExchangeLog> queryByQueryExpress(QueryExpress queryExprss, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExprss, conn);
    }

    @Override
    public List<UserExchangeLog> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int queryByDate(QueryExpress queryExpress, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT count(*) FROM "
                + KEY_TABLE_NAME + "  " + ObjectFieldUtil.generateQueryClause(queryExpress, true);
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {

            pstmt = conn.prepareStatement(sql);
            ObjectFieldUtil.setStmtParams(pstmt, 1, queryExpress);
            rs = pstmt.executeQuery();
            int size = 0;
            while (rs.next()) {
                size = rs.getInt(1);
            }
            return size;
        } catch (SQLException e) {
            GAlerter.lab("On query, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public UserExchangeLog get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME +
                "(user_no,exchange_goods_id, exchange_goods_name,exchange_goods_desc, exchange_goods_pic,exchange_goods_type, exchange_goods_item_id, sn_name1, sn_value1,sn_name2,sn_value2,exchange_type,exchange_date, exchange_time, exchange_ip,exchange_domain,profileid,exchange_point,appkey) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

        if (logger.isDebugEnabled()) {
            logger.debug("UserExchangeLog insert sql:" + sql);
        }

        return sql;
    }

    @Override
    protected UserExchangeLog rsToObject(ResultSet rs) throws SQLException {
        UserExchangeLog userExchangeLog = new UserExchangeLog();

        //user_no,exchange_goods_id, exchange_goods_name,exchange_goods_desc, exchange_goods_pic,exchange_goods_type,
        // exchange_goods_item_id, sn_name1, sn_value1,sn_name2,sn_value2,exchange_type,exchange_date, exchange_time, exchange_ip


        userExchangeLog.setUserExchangeLogId(rs.getLong("user_exchange_log_id"));
        userExchangeLog.setUserNo(rs.getString("user_no"));

        userExchangeLog.setGoodsId(rs.getLong("exchange_goods_id"));
        userExchangeLog.setGoodsName(rs.getString("exchange_goods_name"));
        userExchangeLog.setGoodsDesc(rs.getString("exchange_goods_desc"));
        userExchangeLog.setGoodsPic(rs.getString("exchange_goods_pic"));
        userExchangeLog.setGoodsType(GoodsType.getByCode(rs.getInt("exchange_goods_type")));

        userExchangeLog.setGoodsItemId(rs.getLong("exchange_goods_item_id"));

        userExchangeLog.setSnName1(rs.getString("sn_name1"));
        userExchangeLog.setSnValue1(rs.getString("sn_value1"));
        userExchangeLog.setSnName2(rs.getString("sn_name2"));
        userExchangeLog.setSnValue2(rs.getString("sn_value2"));

        userExchangeLog.setExchangeType(UserExchangeType.getByCode(rs.getString("exchange_type")));
        userExchangeLog.setExchangeDate(rs.getDate("exchange_date"));
        userExchangeLog.setExhangeTime(rs.getTimestamp("exchange_time"));
        userExchangeLog.setExchangeIp(rs.getString("exchange_ip"));
        userExchangeLog.setExchangeDomain(UserExchangeDomain.getByCode(rs.getString("exchange_domain")));
        userExchangeLog.setSmsCount(rs.getInt("sms_count"));
        userExchangeLog.setProfileId(rs.getString("profileid"));
        userExchangeLog.setExchangePoint(rs.getInt("exchange_point"));
        userExchangeLog.setAppkey(StringUtil.isEmpty(rs.getString("appkey")) ? "" : rs.getString("appkey"));
        return userExchangeLog;
    }

}
