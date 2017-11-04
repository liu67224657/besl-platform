package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.point.ConsumeTimesType;
import com.enjoyf.platform.service.point.Goods;
import com.enjoyf.platform.service.point.GoodsField;
import com.enjoyf.platform.service.point.GoodsType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午4:38
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGoodsAccessor extends AbstractSequenceBaseTableAccessor<Goods> implements GoodsAccessor{

    private Logger logger = LoggerFactory.getLogger(AbstractGoodsAccessor.class);

    private static final String KEY_SEQUENCE_NAME = "GOODS_ID";
    protected static final String KEY_TABLE_NAME = "goods";


    @Override
    public Goods insert(Goods goods, Connection conn) throws DbException {

        PreparedStatement pstmt = null;
        try {
            goods.setGoodsId(getSeqNo(KEY_SEQUENCE_NAME, conn));

            pstmt = conn.prepareStatement(getInsertSql());
            //goods_id,goods_name,goods_desc,goods_pic,goods_type,goods_expire_date,goods_amount,goods_reset_amount
            //goods_consume_point,consume_times_type,is_new,is_host,valid_status,createdate,createip,createuserid,share_id,notice_body,detail_url

            pstmt.setLong(1, goods.getGoodsId());
            pstmt.setString(2, goods.getGoodsName());
            pstmt.setString(3, goods.getGoodsDesc());
            pstmt.setString(4, goods.getGoodsPic());
            pstmt.setInt(5, goods.getGoodsType().getCode());
            pstmt.setTimestamp(6, goods.getGoodsExpireDate()==null?null:new Timestamp(goods.getGoodsExpireDate().getTime()));
            pstmt.setInt(7, goods.getGoodsAmount());
            pstmt.setInt(8, goods.getGoodsResetAmount());
            pstmt.setInt(9, goods.getGoodsConsumePoint());
            pstmt.setInt(10, goods.getConsumeTimesType().getCode());
            pstmt.setInt(11, goods.getDisplayOrder());
            pstmt.setBoolean(12, goods.getIsNew());
            pstmt.setBoolean(13, goods.getIsHot());
            pstmt.setString(14, goods.getValidStatus().getCode());
            pstmt.setTimestamp(15, new Timestamp(goods.getCreateDate()==null?System.currentTimeMillis():goods.getCreateDate().getTime()));
            pstmt.setString(16, goods.getCreateIp());
            pstmt.setString(17, goods.getCreateUserId());
            pstmt.setLong(18, goods.getShareId());
            pstmt.setString(19, goods.getNoticeBody());
            pstmt.setString(20, goods.getDetailUrl());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert AppErrorInfo, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return goods;
    }

    @Override
    public int update(UpdateExpress updateExpress, long goodsId, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, new QueryExpress().add(QueryCriterions.eq(GoodsField.GOODSID, goodsId)), conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
         return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    //select * from goods order by displayourder asc
    public List<Goods> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    //select * from goods where valid_status=?
    public List<Goods> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    //select * from goods where goods_id = ?
    public Goods get(long goodsId, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, new QueryExpress().add(QueryCriterions.eq(GoodsField.GOODSID, goodsId)), conn);
    }

    @Override
    protected Goods rsToObject(ResultSet rs) throws SQLException {
        Goods goods = new Goods();
        goods.setGoodsId(rs.getLong("goods_id"));
        goods.setGoodsName(rs.getString("goods_name"));
        goods.setGoodsDesc(rs.getString("goods_desc"));
        goods.setGoodsPic(rs.getString("goods_pic"));
        goods.setGoodsType(GoodsType.getByCode(rs.getInt("goods_type")));
        goods.setGoodsExpireDate(rs.getTimestamp("goods_expire_date"));
        goods.setGoodsAmount(rs.getInt("goods_amount"));
        goods.setGoodsResetAmount(rs.getInt("goods_reset_amount"));
        goods.setGoodsConsumePoint(rs.getInt("goods_consume_point"));
        goods.setConsumeTimesType(ConsumeTimesType.getByCode(rs.getInt("consume_times_type")));
        goods.setDisplayOrder(rs.getInt("display_order"));
        goods.setIsNew(rs.getBoolean("is_new"));
        goods.setIsHot(rs.getBoolean("is_hot"));
        goods.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        goods.setCreateDate(rs.getTimestamp("createdate"));
        goods.setCreateIp(rs.getString("createip"));
        goods.setCreateUserId(rs.getString("createuserid"));
        goods.setShareId(rs.getLong("share_id"));
        goods.setNoticeBody(rs.getString("notice_body"));
        goods.setDetailUrl(rs.getString("detail_url"));
        return goods;
    }

    public String getInsertSql(){
        String sql = "INSERT INTO "+ KEY_TABLE_NAME +"(goods_id,goods_name,goods_desc,goods_pic,goods_type,goods_expire_date,goods_amount,goods_reset_amount," +
                "goods_consume_point,consume_times_type,display_order,is_new,is_hot,valid_status,createdate,createip,createuserid,share_id,notice_body,detail_url) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        if(logger.isDebugEnabled()){
            logger.debug("goods insert sql:"+sql);
        }
        return sql;
    }

}
