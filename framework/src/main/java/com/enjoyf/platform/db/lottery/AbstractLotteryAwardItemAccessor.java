package com.enjoyf.platform.db.lottery;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.lottery.LotteryAwardItem;
import com.enjoyf.platform.service.lottery.LotteryAwardItemField;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-17
 * Time: 下午5:14
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractLotteryAwardItemAccessor extends AbstractSequenceBaseTableAccessor<LotteryAwardItem> implements LotteryAwardItemAccessor {
    private Logger logger = LoggerFactory.getLogger(AbstractLotteryAwardItemAccessor.class);

    private static final String KEY_SEQUENCE_NAME = "SEQ_LOTTERY_AWARD_ITEM_ID";
    private static final String KEY_TABLE_NAME = "lottery_award_item";

    @Override
    public int batchInsert(List<LotteryAwardItem> lotteryAwardItemList, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {

            //lottery_award_item_id,lottery_award_id,sn_name1,sn_value1,sn_name2,sn_value2,sn_name3,sn_value3,exchange_status,own_uno,exchange_date,createdate
            pstmt = conn.prepareStatement(getInsertSql());
            for (LotteryAwardItem lotteryAwardItem : lotteryAwardItemList) {

                pstmt.setLong(1, lotteryAwardItem.getLotteryAwardId());
                pstmt.setString(2, lotteryAwardItem.getName1());
                pstmt.setString(3, lotteryAwardItem.getValue1());
                pstmt.setString(4, lotteryAwardItem.getName2());
                pstmt.setString(5, lotteryAwardItem.getValue2());
                pstmt.setString(6, lotteryAwardItem.getName3());
                pstmt.setString(7, lotteryAwardItem.getValue3());
                pstmt.setString(8, lotteryAwardItem.getLotteryStatus().getCode());

                pstmt.addBatch();
            }
            return pstmt.executeBatch().length;
        } catch (SQLException e) {
            GAlerter.lab("On insert batchInsert, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }


    @Override
    public LotteryAwardItem insert(LotteryAwardItem lotteryAwardItem, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setLong(1, lotteryAwardItem.getLotteryAwardId());
            pstmt.setString(2, lotteryAwardItem.getName1());
            pstmt.setString(3, lotteryAwardItem.getValue1());
            pstmt.setString(4, lotteryAwardItem.getName2());
            pstmt.setString(5, lotteryAwardItem.getValue2());
            pstmt.setString(6, lotteryAwardItem.getName3());
            pstmt.setString(7, lotteryAwardItem.getValue3());
            pstmt.setString(8, lotteryAwardItem.getLotteryStatus().getCode());
            pstmt.setLong(9, lotteryAwardItem.getLotteryId());

            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                lotteryAwardItem.setLotteryAwardItemId(rs.getLong(1));
            }

            return lotteryAwardItem;
        } catch (SQLException e) {
            GAlerter.lab("On insert batchInsert, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
    }


    @Override
    public int update(UpdateExpress updateExpress, long lotteryAwardItemId, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, new QueryExpress().add(QueryCriterions.eq(LotteryAwardItemField.LOTTERY_AWARD_ITEM_ID, lotteryAwardItemId)), conn);
    }

    @Override
    public List<LotteryAwardItem> query(long lotteryAwardId, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, new QueryExpress().add(QueryCriterions.eq(LotteryAwardItemField.LOTTERY_AWARD_ID, lotteryAwardId)), pagination, conn);
    }

    @Override
    public List<LotteryAwardItem> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public LotteryAwardItem get(long lotteryAwardId, ValidStatus lotteryStatus, Connection conn) throws DbException {
        LotteryAwardItem returnObj = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + KEY_TABLE_NAME + " WHERE lottery_award_id=? AND exchange_status=? limit 0,1  FOR UPDATE";
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            //
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, lotteryAwardId);
            pstmt.setString(2, lotteryStatus.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnObj = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On query, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return returnObj;
    }

    @Override
    public LotteryAwardItem get(long lotteryAwardItemId, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, new QueryExpress().add(QueryCriterions.eq(LotteryAwardItemField.LOTTERY_AWARD_ITEM_ID, lotteryAwardItemId)), conn);
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(lottery_award_id,sn_name1,sn_value1,sn_name2,sn_value2,sn_name3,sn_value3,exchange_status,lottery_id) VALUES(?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("award item insert sql:" + sql);
        }

        return sql;
    }

    @Override
    protected LotteryAwardItem rsToObject(ResultSet rs) throws SQLException {
        LotteryAwardItem lotteryAwardItem = new LotteryAwardItem();

        lotteryAwardItem.setLotteryAwardItemId(rs.getLong("lottery_award_item_id"));
        lotteryAwardItem.setLotteryAwardId(rs.getLong("lottery_award_id"));
        lotteryAwardItem.setName1(rs.getString("sn_name1"));
        lotteryAwardItem.setValue1(rs.getString("sn_value1"));
        lotteryAwardItem.setName2(rs.getString("sn_name2"));
        lotteryAwardItem.setValue2(rs.getString("sn_value2"));
        lotteryAwardItem.setName3(rs.getString("sn_name3"));
        lotteryAwardItem.setValue3(rs.getString("sn_value3"));
        lotteryAwardItem.setLotteryStatus(ValidStatus.getByCode(rs.getString("exchange_status")));
        lotteryAwardItem.setOwnUserNo(rs.getString("own_uno"));
        lotteryAwardItem.setLotteryDate(rs.getTimestamp("exchange_date"));
        lotteryAwardItem.setCreateDate(rs.getTimestamp("createdate"));
        lotteryAwardItem.setLotteryId(rs.getLong("lottery_id"));
        return lotteryAwardItem;
    }


    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }
}
