package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-17
 * Time: 下午5:14
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractExchangeGoodsItemAccessor extends AbstractBaseTableAccessor<ExchangeGoodsItem> implements ExchangeGoodsItemAccessor {

    //todo
    private Logger logger = LoggerFactory.getLogger(AbstractExchangeGoodsItemAccessor.class);

    private static final String KEY_TABLE_NAME = "exchange_goods_item";

    @Override
    public int batchInsert(List<ExchangeGoodsItem> goodsItemList, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            for (ExchangeGoodsItem item : goodsItemList) {
                pstmt.setLong(1, item.getGoodsId());
                pstmt.setString(2, item.getSnName1());
                pstmt.setString(3, item.getSnValue1());
                pstmt.setString(4, item.getSnName2());
                pstmt.setString(5, item.getSnValue2());
                pstmt.setString(6, item.getExchangeStatus().getCode());
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
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress,queryExpress, conn);
    }

    //select * from  goods_item where goodsId=? and exchangeStatus=?;
    @Override
    public List<ExchangeGoodsItem> queryByGoodsIdExchangeStatus(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {

        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public ExchangeGoodsItem getExchangeByStatus(long goodsId, ActStatus exchangeStatus, Connection conn) throws DbException {
        ExchangeGoodsItem returnObj = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + KEY_TABLE_NAME + " WHERE exchange_goods_id=? AND exchange_status=? limit 0,1 FOR UPDATE";
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            //
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, goodsId);
            pstmt.setString(2, exchangeStatus.getCode());

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
    public List<ExchangeGoodsItem> queryExchangeGoodsItem(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    //select * from goods_item where goods_item_id=?
    @Override
    public ExchangeGoodsItem get(long goodsItemId, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, new QueryExpress().add(QueryCriterions.eq(ExchangeGoodsItemField.EXCHANGE_GOODS_ID, goodsItemId)), conn);
    }

    @Override
    public ExchangeGoodsItem insert(ExchangeGoodsItem goodsItem, Connection conn) throws DbException {
        return null;
    }

    private String getInsertSql() {
        String sql = "insert into " + KEY_TABLE_NAME + "(exchange_goods_id,sn_name1,sn_value1,sn_name2,sn_value2,exchange_status)values(?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("UserConsumeLog insert sql:" + sql);
        }

        return sql;
    }

    @Override
    protected ExchangeGoodsItem rsToObject(ResultSet rs) throws SQLException {
        ExchangeGoodsItem goodsItem = new ExchangeGoodsItem();
        goodsItem.setGoodsItemId(rs.getLong("exchange_goods_item_id"));
        goodsItem.setGoodsId(rs.getLong("exchange_goods_id"));
        goodsItem.setSnName1(rs.getString("sn_name1"));
        goodsItem.setSnValue1(rs.getString("sn_value1"));
        goodsItem.setSnName2(rs.getString("sn_name2"));
        goodsItem.setSnValue2(rs.getString("sn_value2"));
        goodsItem.setExchangeStatus(ActStatus.getByCode(rs.getString("exchange_status")));
        goodsItem.setOwnUserNo(rs.getString("own_user_no"));
        goodsItem.setExchangeTime(rs.getTimestamp("exchange_time"));
        goodsItem.setCreateTime(rs.getTimestamp("create_time"));
        goodsItem.setProfileId(rs.getString("profileid"));
        return goodsItem;
    }

}
