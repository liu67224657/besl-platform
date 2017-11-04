package com.enjoyf.platform.db.lottery;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.lottery.Address;
import com.enjoyf.platform.service.lottery.LotteryAddress;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/4/10
 * Description:
 */
public abstract class AbstractLotteryAddressAccessor extends AbstractBaseTableAccessor<LotteryAddress> implements LotteryAddressAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractLotteryAddressAccessor.class);

    private static final String KEY_TABLE_NAME = "lottery_address";

    @Override
    public LotteryAddress insert(LotteryAddress lotteryAddress, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, lotteryAddress.getProfileid());
            pstmt.setString(2, lotteryAddress.getAddress() == null ? "" : lotteryAddress.getAddress().toJsonStr());
            pstmt.setTimestamp(3, new Timestamp(lotteryAddress.getCreateTime() == null ? System.currentTimeMillis() : lotteryAddress.getCreateTime().getTime()));
            pstmt.setString(4, lotteryAddress.getCreateIp());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return lotteryAddress;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(profileid,address,createtime,createip) VALUES (?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("lotteryAddress insert sql" + sql);
        }
        return sql;
    }

    @Override
    public LotteryAddress get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<LotteryAddress> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<LotteryAddress> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected LotteryAddress rsToObject(ResultSet rs) throws SQLException {
        LotteryAddress returnObject = new LotteryAddress();
        returnObject.setProfileid(rs.getString("profileid"));
        returnObject.setAddress(Address.parse(rs.getString("address")));
        returnObject.setCreateTime(new Date(rs.getTimestamp("createtime").getTime()));
        returnObject.setCreateIp(rs.getString("createip"));
        return returnObject;
    }
}