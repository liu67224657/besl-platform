package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.point.GoodsItem;
import com.enjoyf.platform.service.point.GoodsItemField;
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
public abstract class AbstractGoodsItemAccessor extends AbstractSequenceBaseTableAccessor<GoodsItem> implements GoodsItemAccessor {
    private Logger logger = LoggerFactory.getLogger(AbstractGoodsItemAccessor.class);

    private static final String KEY_SEQUENCE_NAME = "GOODS_ITEM_ID";
    private static final String KEY_TABLE_NAME = "goods_item";

    @Override
    public int batchInsert(List<GoodsItem> goodsItemList, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            //goods_item_id,goods_id,sn_name1,sn_value1,sn_name2,sn_value2,sn_name3,sn_value3,exchange_status
            pstmt = conn.prepareStatement(getInsertSql());
            for (GoodsItem goodsItem : goodsItemList) {
                goodsItem.setGoodsItemId(getSeqNo(KEY_SEQUENCE_NAME, conn));
                pstmt.setLong(1, goodsItem.getGoodsItemId());
                pstmt.setLong(2, goodsItem.getGoodsId());
                pstmt.setString(3, goodsItem.getSnName1());
                pstmt.setString(4, goodsItem.getSnValue1());
                pstmt.setString(5, goodsItem.getSnName2());
                pstmt.setString(6, goodsItem.getSnValue2());
                pstmt.setString(7, goodsItem.getSnName3());
                pstmt.setString(8, goodsItem.getSnValue3());
                pstmt.setString(9, goodsItem.getExchangeStatus().getCode());
                pstmt.setString(10, goodsItem.getProfileId());

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
    public int update(UpdateExpress updateExpress, long goodsItemId, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, new QueryExpress().add(QueryCriterions.eq(GoodsItemField.GOODSITEM_ID, goodsItemId)), conn);
    }

    //select * from  goods_item where goodsId=? and exchangeStatus=?;
    @Override
    public List<GoodsItem> queryByGoodsIdExchangeStatus(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        if (pagination == null) {
            return super.query(KEY_TABLE_NAME, queryExpress, conn);
        } else {
            return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
        }
    }

    @Override
    public GoodsItem getExchangeByStatus(long goodsId, ActStatus exchangeStatus, Connection conn) throws DbException {
        GoodsItem returnObj = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + KEY_TABLE_NAME + " WHERE goods_id=? AND exchange_status=? limit 0,1 FOR UPDATE";
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

    //select * from goods_item where goods_item_id=?
    @Override
    public GoodsItem get(long goodsItemId, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, new QueryExpress().add(QueryCriterions.eq(GoodsItemField.GOODSITEM_ID, goodsItemId)), conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<GoodsItem> queryGoodsItem(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public GoodsItem insert(GoodsItem goodsItem, Connection conn) throws DbException {
        return null;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(goods_item_id,goods_id,sn_name1,sn_value1,sn_name2,sn_value2,sn_name3,sn_value3,exchange_status,profileid) VALUES(?,?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("UserConsumeLog insert sql:" + sql);
        }

        return sql;
    }

    @Override
    protected GoodsItem rsToObject(ResultSet rs) throws SQLException {
        GoodsItem goodsItem = new GoodsItem();

        goodsItem.setGoodsItemId(rs.getLong("goods_item_id"));
        goodsItem.setGoodsId(rs.getLong("goods_id"));
        goodsItem.setSnName1(rs.getString("sn_name1"));
        goodsItem.setSnValue1(rs.getString("sn_value1"));
        goodsItem.setSnName2(rs.getString("sn_name2"));
        goodsItem.setSnValue2(rs.getString("sn_value2"));
        goodsItem.setSnName3(rs.getString("sn_name3"));
        goodsItem.setSnValue3(rs.getString("sn_value3"));
        goodsItem.setExchangeStatus(ActStatus.getByCode(rs.getString("exchange_status")));
        goodsItem.setOwnUserNo(rs.getString("own_uno"));
        goodsItem.setConsumeDate(rs.getTimestamp("exchange_date"));
        goodsItem.setCreateDate(rs.getTime("createdate"));
        goodsItem.setProfileId(rs.getString("profileid"));
        return goodsItem;
    }
}
