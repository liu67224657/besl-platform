package com.enjoyf.platform.db.billing;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.billing.UserBalance;
import com.enjoyf.platform.service.billing.BillingUtil;
import com.enjoyf.platform.service.billing.DepositChannel;
import com.enjoyf.platform.service.billing.UserBalance;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-1-6
 * Time: 上午10:02
 * To change this template use File | Settings | File Templates.
 */
abstract class AbstractUserBalanceAccessor extends AbstractBaseTableAccessor<UserBalance> implements UserBalanceAccessor {
    private static final Logger logger = LoggerFactory.getLogger(AbstractUserBalanceAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "billing_user_balance";


    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME_PREFIX + " (balance_id, profileid, appkey, zonekey, amount) " +
                "VALUES (?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("UserBalance INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    @Override
    public UserBalance insert(UserBalance balance, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());

            balance.setBalanceId(BillingUtil.getBalanceId(balance.getProfileId(), balance.getAppKey(), balance.getZoneKey()));

            //balance_id, profileid, appkey, zonekey, amount
            pstmt.setString(1, balance.getBalanceId());
            pstmt.setString(2, balance.getProfileId());
            pstmt.setString(3, balance.getAppKey());
            pstmt.setString(4, balance.getZoneKey());
            pstmt.setInt(5, balance.getAmount());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert UserBalance, occur SQLException.e:", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return balance;
    }

    @Override
    public UserBalance get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME_PREFIX, queryExpress, conn);
    }

    @Override
    public UserBalance getForUpdate(String balanceId, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        UserBalance userBalance = null;
        try {
            String sql = "SELECT * FROM " + KEY_TABLE_NAME_PREFIX + " WHERE balance_id=? FOR UPDATE";
            pstmt = conn.prepareStatement(sql);
            if (logger.isDebugEnabled()) {
                logger.debug("get for update sql:" + sql);
            }

            pstmt.setString(1, balanceId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                userBalance = rsToObject(rs);
            }

        } catch (SQLException e) {
            GAlerter.lab("On insert UserBalance, occur SQLException.e:", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return userBalance;
    }

    @Override
    public List<UserBalance> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME_PREFIX, queryExpress, conn);
    }

    @Override
    public List<UserBalance> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME_PREFIX, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME_PREFIX, updateExpress, queryExpress, conn);
    }

    @Override
    protected UserBalance rsToObject(ResultSet rs) throws SQLException {

        UserBalance balance = new UserBalance();
        balance.setBalanceId(rs.getString("balance_id"));
        balance.setProfileId(rs.getString("profileid"));
        balance.setAppKey(rs.getString("appkey"));
        balance.setZoneKey(rs.getString("zonekey"));
        balance.setAmount(rs.getInt("amount"));
        return balance;
    }

}
