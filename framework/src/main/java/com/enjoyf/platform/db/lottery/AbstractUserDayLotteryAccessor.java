package com.enjoyf.platform.db.lottery;

import com.enjoyf.platform.db.*;
import com.enjoyf.platform.service.lottery.UserDayLottery;
import com.enjoyf.platform.service.lottery.UserDayLotteryField;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-25
 * Time: 上午12:21
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractUserDayLotteryAccessor extends AbstractBaseTableAccessor<UserDayLottery> implements UserDayLotteryAccessor {

    private Logger logger = LoggerFactory.getLogger(AbstractUserDayLotteryAccessor.class);

    private static final String KEY_TABLE_NAME = "user_day_lottery_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";

    @Override
    public UserDayLottery insert(UserDayLottery userDayLottery, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(userDayLottery.getLotteryDate()));

            pstmt.setString(1, userDayLottery.getUserNo());
            pstmt.setLong(2, userDayLottery.getLotteryId());
            pstmt.setInt(3, userDayLottery.getLotteryTimes());
            pstmt.setTimestamp(4, new Timestamp(userDayLottery.getLotteryDate() == null ? System.currentTimeMillis() : userDayLottery.getLotteryDate().getTime()));


            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + "On insert , a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return userDayLottery;
    }

    @Override
    public int update(UpdateExpress updateExpress, long userDayLotteryId, Date date, Connection conn) throws DbException {
        return super.update(getTableName(date), updateExpress, new QueryExpress().add(QueryCriterions.eq(UserDayLotteryField.USER_DAY_LOTTERY_ID, userDayLotteryId)), conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, long lotteryId, String uno, Date date, Connection conn) throws DbException {
        return super.update(getTableName(date), updateExpress, new QueryExpress().add(QueryCriterions.eq(UserDayLotteryField.USER_NO, uno)).add(QueryCriterions.eq(UserDayLotteryField.LOTTERY_ID, lotteryId)), conn);
    }

    @Override
    public List<UserDayLottery> query(long lotteryId, Date date, Pagination pagination, Connection conn) throws DbException {
        return super.query(getTableName(date), new QueryExpress().add(QueryCriterions.eq(UserDayLotteryField.LOTTERY_ID, lotteryId)), pagination, conn);
    }

    @Override
    public List<UserDayLottery> query(long lotteryId, Date date, Connection conn) throws DbException {
        return super.query(getTableName(date), new QueryExpress().add(QueryCriterions.eq(UserDayLotteryField.LOTTERY_ID, lotteryId)), conn);
    }

    @Override
    public UserDayLottery get(long userDayLotteryId, Date date, Connection conn) throws DbException {
        return super.get(getTableName(date), new QueryExpress().add(QueryCriterions.eq(UserDayLotteryField.USER_DAY_LOTTERY_ID, userDayLotteryId)), conn);
    }

    @Override
    public UserDayLottery get(long lotteryId, String uno, Date date, Connection conn) throws DbException {
        return super.get(getTableName(date), new QueryExpress().add(QueryCriterions.eq(UserDayLotteryField.USER_NO, uno)).add(QueryCriterions.eq(UserDayLotteryField.LOTTERY_DATE, date)).add(QueryCriterions.eq(UserDayLotteryField.LOTTERY_ID, lotteryId)), conn);
    }

    @Override
    protected UserDayLottery rsToObject(ResultSet rs) throws SQLException {
        UserDayLottery userDayLottery = new UserDayLottery();
        userDayLottery.setUserDayLotteryId(rs.getLong("user_day_lottery_id"));
        userDayLottery.setUserNo(rs.getString("user_no"));
        userDayLottery.setLotteryId(rs.getLong("lottery_id"));
        userDayLottery.setLotteryTimes(rs.getInt("lottery_times"));
        userDayLottery.setLotteryDate(rs.getDate("lottery_date"));
        return userDayLottery;
    }

    private String getTableName(Date d) {
        return KEY_TABLE_NAME + TableUtil.getTableDateSuffix(d, KEY_TABLE_SUFFIX_FMT);
    }

    private String getInsertSql(Date date) {
        String sql = "INSERT INTO " + getTableName(date) + "(user_no,lottery_id,lottery_times,lottery_date) VALUES(?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("award item insert sql:" + sql);
        }

        return sql;
    }
}
