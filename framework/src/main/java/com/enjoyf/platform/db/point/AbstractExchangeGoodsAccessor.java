package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.point.*;
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
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午4:38
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractExchangeGoodsAccessor extends AbstractBaseTableAccessor<ExchangeGoods> implements ExchangeGoodsAccessor {

    private Logger logger = LoggerFactory.getLogger(AbstractExchangeGoodsAccessor.class);

    protected static final String KEY_TABLE_NAME = "exchange_goods";


    @Override
    public ExchangeGoods insert(ExchangeGoods goods, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, goods.getGoodsName());
            pstmt.setString(2, goods.getGoodsDesc());
            pstmt.setString(3, goods.getGoodsPic());
            pstmt.setInt(4, goods.getGoodsType().getCode());
//            pstmt.setTimestamp(5, new Timestamp(goods.getStartTime() == null ? System.currentTimeMillis() : goods.getStartTime().getTime()));
//            pstmt.setTimestamp(6, new Timestamp(goods.getEndTime() == null ? System.currentTimeMillis() : goods.getEndTime().getTime()));
            pstmt.setInt(5, goods.getGoodsAmount());
            pstmt.setInt(6, goods.getGoodsResetAmount());
            pstmt.setInt(7, goods.getExchangeTimeType().getCode());
            pstmt.setLong(8, goods.getExchangeIntrval());
            pstmt.setInt(9, goods.getTaoTimesType().getCode());
            pstmt.setLong(10, goods.getTaoIntrval());
            pstmt.setInt(11, goods.getDisplayOrder());
            pstmt.setBoolean(12, goods.getIsNew());
            pstmt.setBoolean(13, goods.getIsHot());
            pstmt.setString(14, goods.getValidStatus().getCode());
            pstmt.setString(15, goods.getCreateUserId());
            pstmt.setString(16, goods.getCreateIp());
            pstmt.setTimestamp(17, new Timestamp(goods.getCreateDate() == null ? System.currentTimeMillis() : goods.getCreateDate().getTime()));
            pstmt.setLong(18, goods.getShareId());
            pstmt.setString(19, goods.getNoticeBody());
            pstmt.setString(20, goods.getDetailUrl());
            pstmt.setInt(21, goods.getGoodsConsumePoint());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                goods.setGoodsId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert exchangeGoods, a SQLException occured.", e);
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
    //select * from goods order by displayourder asc
    public List<ExchangeGoods> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    //select * from goods where valid_status=?
    public List<ExchangeGoods> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    //select * from goods where goods_id = ? and valid_status='valid'
    public ExchangeGoods get(long goodsId, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, new QueryExpress().add(QueryCriterions.eq(ExchangeGoodsField.GOODSID, goodsId)), conn);
    }

    @Override
    protected ExchangeGoods rsToObject(ResultSet rs) throws SQLException {
        //tao_time_type,tao_intrval,display_order,is_new,is_hot,valid_status,createuserid,createip,createdate,share_id,notice_body,detail_url
        ExchangeGoods exchangeGoods = new ExchangeGoods();
        exchangeGoods.setGoodsId(rs.getInt("exchange_goods_id"));
        exchangeGoods.setGoodsName(rs.getString("exchange_goods_name"));
        exchangeGoods.setGoodsDesc(rs.getString("exchange_goods_desc"));
        exchangeGoods.setGoodsPic(rs.getString("exchange_goods_pic"));
        exchangeGoods.setGoodsType(GoodsType.getByCode(rs.getInt("exchange_goods_type")));
        exchangeGoods.setStartTime(rs.getTimestamp("exchange_goods_start_time"));
        exchangeGoods.setEndTime(rs.getTimestamp("exchange_goods_end_endtime"));
        exchangeGoods.setGoodsAmount(rs.getInt("exchange_goods_amount"));
        exchangeGoods.setGoodsResetAmount(rs.getInt("exchange_goods_resetamount"));
        exchangeGoods.setExchangeTimeType(ConsumeTimesType.getByCode(rs.getInt("exchange_time_type")));
        exchangeGoods.setExchangeIntrval(rs.getInt("exchange_intrval"));
        exchangeGoods.setTaoTimesType(ConsumeTimesType.getByCode(rs.getInt("tao_time_type")));
        exchangeGoods.setTaoIntrval(rs.getInt("tao_intrval"));
        exchangeGoods.setDisplayOrder(rs.getInt("display_order"));
        exchangeGoods.setIsNew(rs.getBoolean("is_new"));
        exchangeGoods.setIsHot(rs.getBoolean("is_hot"));
        exchangeGoods.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        exchangeGoods.setCreateUserId(rs.getString("createuserid"));
        exchangeGoods.setCreateIp(rs.getString("createip"));
        exchangeGoods.setCreateDate(rs.getTimestamp("createdate"));
        exchangeGoods.setShareId(rs.getLong("share_id"));
        exchangeGoods.setNoticeBody(rs.getString("notice_body"));
        exchangeGoods.setDetailUrl(rs.getString("detail_url"));
        exchangeGoods.setGoodsConsumePoint(rs.getInt("exchange_goods_point"));
        return exchangeGoods;
    }

    public String getInsertSql() {
        String sql = "insert into " + KEY_TABLE_NAME + "(exchange_goods_name,exchange_goods_desc,exchange_goods_pic,exchange_goods_type,exchange_goods_amount,exchange_goods_resetamount,exchange_time_type,exchange_intrval,tao_time_type,tao_intrval,display_order,is_new,is_hot,valid_status,createuserid,createip,createdate,share_id,notice_body,detail_url,exchange_goods_point) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ExchangeGoods insert sql:" + sql);
        }
        return sql;
    }

}
