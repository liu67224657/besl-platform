package com.enjoyf.platform.db.lottery;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.lottery.UserDayLottery;
import com.enjoyf.platform.service.lottery.UserLotteryLog;
import com.enjoyf.platform.service.lottery.UserLotteryLogField;
import com.enjoyf.platform.service.point.UserExchangeLogField;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-17
 * Time: 下午5:14
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractUserLotteryLogAccessor extends AbstractBaseTableAccessor<UserLotteryLog> implements UserLotteryLogAccessor {
    private Logger logger = LoggerFactory.getLogger(AbstractUserLotteryLogAccessor.class);

    //    private static final String KEY_SEQUENCE_NAME = "SEQ_USER_LOTTERY_ID";
    private static final String KEY_TABLE_NAME = "user_lottery_log";

    @Override
    public UserLotteryLog insert(UserLotteryLog lotteryLog, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {

            long lotteryTimestamp = lotteryLog.getLotteryDate() == null ? System.currentTimeMillis() : lotteryLog.getLotteryDate().getTime();
            //user_lottery_id,user_no,lottery_id,lottery_award_id,lottery_award_name,lottery_award_desc,lottery_award_pic,lottery_award_level,lottery_date,lottery_ip,lottery_award_item_id,lottery_award_item_name1,lottery_award_item_value1,lottery_award_item_name2,lottery_award_item_value2 , screen_name
            pstmt = conn.prepareStatement(getInsertSql());

            pstmt.setString(1, lotteryLog.getUno());
            pstmt.setLong(2, lotteryLog.getLotteryId());
            pstmt.setLong(3, lotteryLog.getLotteryAwardId());
            pstmt.setString(4, lotteryLog.getLotteryAwardName());
            pstmt.setString(5, lotteryLog.getLotteryAwardDesc());
            pstmt.setString(6, lotteryLog.getLotteryAwardPic());
            pstmt.setInt(7, lotteryLog.getLotteryAwardLevel());
            pstmt.setTimestamp(8, new Timestamp(lotteryTimestamp));
            pstmt.setString(9, lotteryLog.getLotteryIp());
            pstmt.setLong(10, lotteryLog.getLotteryAwardItemId());
            pstmt.setString(11, lotteryLog.getName1());
            pstmt.setString(12, lotteryLog.getValue1());
            pstmt.setString(13, lotteryLog.getName2());
            pstmt.setString(14, lotteryLog.getValue2());
            pstmt.setString(15, lotteryLog.getScreenName());
            pstmt.setString(16, lotteryLog.getLottery_code());
            pstmt.setString(17, lotteryLog.getExtension());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + "On insert , a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return lotteryLog;
    }

    @Override
    public List<UserLotteryLog> queryByUser(long lotteryId, String uno, Date from, Date to, Pagination pagination, Connection conn) throws DbException {
        QueryExpress queryExpress = new QueryExpress();
        if(lotteryId > 0l){
            queryExpress.add(QueryCriterions.eq(UserLotteryLogField.LOTTERY_ID, lotteryId));
        }
        if(!StringUtil.isEmpty(uno)){
            queryExpress.add(QueryCriterions.eq(UserLotteryLogField.USER_NO, uno));
        }
        if (from != null) {
            queryExpress.add(QueryCriterions.geq(UserLotteryLogField.LOTTERY_DATE, from));
        }
        if (to != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(to);
            calendar.add(Calendar.DATE, 1);
            to = calendar.getTime();
            queryExpress.add(QueryCriterions.leq(UserLotteryLogField.LOTTERY_DATE, to));
        }
        queryExpress.add(QuerySort.add(UserLotteryLogField.USER_LOTTERY_ID, QuerySortOrder.DESC));
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public List<UserLotteryLog> queryByUser(long lotteryId, String uno, Connection conn) throws DbException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(UserLotteryLogField.LOTTERY_ID, lotteryId));
        queryExpress.add(QueryCriterions.eq(UserLotteryLogField.USER_NO, uno));
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public UserLotteryLog getByUser(long lotteryId, String uno, Connection conn) throws DbException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(UserLotteryLogField.LOTTERY_ID, lotteryId));
        queryExpress.add(QueryCriterions.eq(UserLotteryLogField.USER_NO, uno));
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    //lotteryid=? and lottery_award_id>0 order by lottery_date desc
    @Override
    public List<UserLotteryLog> queryByLastestByLotteryId(long lotteryId, int size, Connection conn) throws DbException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(UserLotteryLogField.LOTTERY_ID, lotteryId));
        queryExpress.add(QueryCriterions.gt(UserLotteryLogField.LOTTERY_AWARD_ID, 0l));
        queryExpress.add(QuerySort.add(UserLotteryLogField.LOTTERY_DATE, QuerySortOrder.DESC));
        return super.query(KEY_TABLE_NAME, queryExpress, new Pagination(size, 1, size), conn);
    }

    @Override
    public List<UserLotteryLog> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int queryRandomNum(long lotteryId, int maxNum, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList<Integer> list = new LinkedList<Integer>();
        //SELECT * FROM seq b WHERE id NOT IN (SELECT r_num FROM r_num) AND id < 10 ORDER BY RAND() LIMIT 1;
        //SELECT * FROM seq WHERE (SELECT COUNT(1) AS num FROM r_num WHERE seq.id = r_num.r_num) = 0 AND seq.id < 10 ORDER BY RAND() LIMIT 1;
        String sql = "SELECT * FROM seq WHERE (SELECT COUNT(1) AS num FROM (SELECT random_num AS r_num FROM " + KEY_TABLE_NAME + " WHERE lottery_id=?) AS r_num WHERE seq.seq_id = r_num.r_num) = 0 AND seq.seq_id<? ORDER BY RAND() LIMIT 1";
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }
        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, lotteryId);
            pstmt.setInt(2, maxNum);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("seq_id");
            }
            return 0;
        } catch (SQLException e) {
            GAlerter.lab("On query, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
    }


    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(user_no,lottery_id,lottery_award_id,lottery_award_name,lottery_award_desc,lottery_award_pic,lottery_award_level,lottery_date,lottery_ip,lottery_award_item_id,lottery_award_item_name1,lottery_award_item_value1,lottery_award_item_name2,lottery_award_item_value2,screen_name,lottery_code,extension) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("UserLotteryLog insert sql:" + sql);
        }

        return sql;
    }

    @Override
    protected UserLotteryLog rsToObject(ResultSet rs) throws SQLException {
        UserLotteryLog lotteryLog = new UserLotteryLog();

        lotteryLog.setUserLotteryId(rs.getLong("user_lottery_id"));
        lotteryLog.setUno(rs.getString("user_no"));

        lotteryLog.setLotteryId(rs.getLong("lottery_id"));
        lotteryLog.setLotteryAwardId(rs.getLong("lottery_award_id"));
        lotteryLog.setLotteryAwardName(rs.getString("lottery_award_name"));
        lotteryLog.setLotteryAwardDesc(rs.getString("lottery_award_desc"));
        lotteryLog.setLotteryAwardPic(rs.getString("lottery_award_pic"));

        lotteryLog.setLotteryAwardLevel(rs.getInt("lottery_award_level"));
        lotteryLog.setLotteryDate(rs.getTimestamp("lottery_date"));
        lotteryLog.setLotteryIp(rs.getString("lottery_ip"));

        lotteryLog.setLotteryAwardItemId(rs.getLong("lottery_award_item_id"));
        lotteryLog.setName1(rs.getString("lottery_award_item_name1"));
        lotteryLog.setValue1(rs.getString("lottery_award_item_value1"));
        lotteryLog.setName2(rs.getString("lottery_award_item_name2"));
        lotteryLog.setValue2(rs.getString("lottery_award_item_value2"));

        lotteryLog.setScreenName(rs.getString("screen_name"));
        lotteryLog.setLottery_code(rs.getString("lottery_code"));
        lotteryLog.setExtension(rs.getString("extension"));
        return lotteryLog;
    }
}
