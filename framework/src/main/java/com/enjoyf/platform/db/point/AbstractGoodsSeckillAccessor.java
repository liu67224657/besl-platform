package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.IntRemoveStatus;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.point.GoodsSeckill;
import com.enjoyf.platform.service.point.PointKeyType;
import com.enjoyf.platform.service.point.SeckillTips;
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
 * Created by zhitaoshi on 2015/7/23.
 */
public abstract class AbstractGoodsSeckillAccessor extends AbstractBaseTableAccessor<GoodsSeckill> implements GoodsSeckillAccessor {
    private static final Logger logger = LoggerFactory.getLogger(AbstractGoodsSeckillAccessor.class);

    protected static final String KEY_TABLE_NAME = "goods_seckill";

    @Override
    public GoodsSeckill insert(GoodsSeckill goodsSeckill, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, goodsSeckill.getGoodsId());
            pstmt.setString(2, goodsSeckill.getSeckillTitle());
            pstmt.setString(3, goodsSeckill.getSeckillDesc());
            pstmt.setString(4, goodsSeckill.getSeckillPic());
            pstmt.setTimestamp(5, new Timestamp(goodsSeckill.getStartTime().getTime()));
            pstmt.setTimestamp(6, new Timestamp(goodsSeckill.getEndTime().getTime()));
            pstmt.setInt(7, goodsSeckill.getSeckillTotal());
            pstmt.setString(8, goodsSeckill.getSeckillTips().toJson());
            pstmt.setInt(9, goodsSeckill.getGoodsActionType().getCode());

            pstmt.setTimestamp(10, new Timestamp(goodsSeckill.getCreateDate() == null ? System.currentTimeMillis() : goodsSeckill.getCreateDate().getTime()));
            pstmt.setString(11, goodsSeckill.getCreateUser() == null ? "" : goodsSeckill.getCreateUser());
            pstmt.setInt(12, goodsSeckill.getRemoveStatus().getCode());
            pstmt.setInt(13, goodsSeckill.getSeckillSum());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                goodsSeckill.setSeckillId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("AbstractGoodsSeckillAccessor insert occur SQLException.e:", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return goodsSeckill;
    }

    @Override
    public GoodsSeckill get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<GoodsSeckill> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<GoodsSeckill> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        if (pagination == null) {
            return super.query(KEY_TABLE_NAME, queryExpress, conn);
        } else {
            return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
        }
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected GoodsSeckill rsToObject(ResultSet rs) throws SQLException {
        GoodsSeckill goodsSeckill = new GoodsSeckill();
        goodsSeckill.setSeckillId(rs.getLong("seckillid"));
        goodsSeckill.setGoodsId(rs.getString("goodsid"));
        goodsSeckill.setSeckillTitle(rs.getString("seckilltitle"));
        goodsSeckill.setSeckillDesc(rs.getString("seckilldesc"));
        goodsSeckill.setSeckillPic(URLUtils.getJoymeDnUrl(rs.getString("seckillpic")));
        goodsSeckill.setStartTime(rs.getTimestamp("starttime"));
        goodsSeckill.setEndTime(rs.getTimestamp("endtime"));
        goodsSeckill.setSeckillTotal(rs.getInt("seckilltotal"));
        goodsSeckill.setSeckillTips(SeckillTips.parse(rs.getString("seckilltips")));
        goodsSeckill.setGoodsActionType(GoodsActionType.getByCode(rs.getInt("goodsactiontype")));
        goodsSeckill.setCreateUser(rs.getString("createuser"));
        goodsSeckill.setCreateDate(rs.getTimestamp("createdate"));
        goodsSeckill.setModifyUser(rs.getString("modifyuser"));
        goodsSeckill.setModifyDate(rs.getTimestamp("modifydate"));
        goodsSeckill.setRemoveStatus(IntRemoveStatus.getByCode(rs.getInt("removestatus")));
        goodsSeckill.setSeckillSum(rs.getInt("seckillsum"));
        return goodsSeckill;
    }

    public String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + "(goodsid,seckilltitle,seckilldesc,seckillpic,starttime,endtime," +
                "seckilltotal,seckilltips,goodsactiontype,createdate,createuser,removestatus,seckillsum) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("insert sql:" + insertSql);
        }
        return insertSql;
    }
}
