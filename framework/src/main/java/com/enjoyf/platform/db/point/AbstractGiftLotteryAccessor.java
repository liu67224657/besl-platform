package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.GiftLottery;
import com.enjoyf.platform.service.point.LotteryType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public abstract class AbstractGiftLotteryAccessor extends AbstractBaseTableAccessor<GiftLottery> implements GiftLotteryAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractGiftLotteryAccessor.class);

    private static final String KEY_TABLE_NAME = "gift_lottery";

    @Override
    public GiftLottery insert(GiftLottery giftLottery, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, giftLottery.getGiftLotteryName());
            pstmt.setDouble(2, giftLottery.getProbability());
            pstmt.setInt(3, giftLottery.getReturnPoint());
            pstmt.setString(4, giftLottery.getPicName());
            pstmt.setString(5, giftLottery.getDisPicName());
            pstmt.setString(6, giftLottery.getPicKey());
            pstmt.setInt(7, giftLottery.getLotteryType().getCode());
            pstmt.setInt(8, giftLottery.getStarRating());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                giftLottery.setGiftLotteryId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return giftLottery;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(gift_lottery_name,probability,return_point,pic_name,dis_pic_name,pic_key,lottery_type,star_rating) VALUES (?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("GiftLottery insert sql" + sql);
        }
        return sql;
    }

    @Override
    public GiftLottery get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<GiftLottery> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<GiftLottery> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected GiftLottery rsToObject(ResultSet rs) throws SQLException {

        GiftLottery returnObject = new GiftLottery();

        returnObject.setGiftLotteryId(rs.getLong("gift_lottery_id"));
        returnObject.setGiftLotteryName(rs.getString("gift_lottery_name"));
        returnObject.setProbability(rs.getDouble("probability"));
        returnObject.setReturnPoint(rs.getInt("return_point"));
        returnObject.setPicKey(rs.getString("pic_key"));
        returnObject.setPicName(rs.getString("pic_name"));
        returnObject.setDisPicName(rs.getString("dis_pic_name"));
        returnObject.setLotteryType(LotteryType.getByCode(rs.getInt("lottery_type")));
        returnObject.setStarRating(rs.getInt("star_rating"));
        return returnObject;
    }
}