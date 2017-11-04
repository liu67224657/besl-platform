package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.UserLotteryLog;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractUserLotteryLogAccessor extends AbstractBaseTableAccessor<UserLotteryLog> implements UserLotteryLogAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractUserLotteryLogAccessor.class);

    private static final String KEY_TABLE_NAME = "user_lottery_log";

    @Override
    public UserLotteryLog insert(UserLotteryLog userLotteryLog, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, userLotteryLog.getUserLotteryLogId());
            pstmt.setLong(2, userLotteryLog.getGiftLotteryId());
            pstmt.setString(3, userLotteryLog.getProfileId());
            pstmt.setString(4, userLotteryLog.getGiftLotteryName());
            pstmt.setTimestamp(5, new Timestamp(userLotteryLog.getLotteryDate().getTime()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return userLotteryLog;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(user_lottery_log_id,gift_lottery_id,profile_id,gift_lottery_name,lottery_date) VALUES (?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("UserLotteryLog insert sql" + sql);
        }
        return sql;
    }

    @Override
    public UserLotteryLog get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<UserLotteryLog> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<UserLotteryLog> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected UserLotteryLog rsToObject(ResultSet rs) throws SQLException {

        UserLotteryLog returnObject = new UserLotteryLog();

        returnObject.setUserLotteryLogId(rs.getString("user_lottery_log_id"));
        returnObject.setGiftLotteryId(rs.getLong("gift_lottery_id"));
        returnObject.setProfileId(rs.getString("profile_id"));
        returnObject.setGiftLotteryName(rs.getString("gift_lottery_name"));
        returnObject.setLotteryDate(new Date(rs.getTimestamp("lottery_date").getTime()));


        return returnObject;
    }
}