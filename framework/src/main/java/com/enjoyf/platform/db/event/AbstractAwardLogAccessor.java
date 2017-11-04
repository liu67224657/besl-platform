package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.event.ActivityAwardLog;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-6
 * Time: 上午9:15
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAwardLogAccessor extends AbstractBaseTableAccessor<ActivityAwardLog> implements ActivityAwardLogAccessor{

    private Logger logger= LoggerFactory.getLogger(AbstractAwardLogAccessor.class);

    private static final String KEY_TABLE_NAME="activity_award_log";

    @Override
    public ActivityAwardLog insert(ActivityAwardLog log, Connection conn) throws DbException {
        //uno, activityid, createtime, award_date
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, log.getUno());
            pstmt.setLong(2, log.getActivityId());

            pstmt.setTimestamp(3, new Timestamp(log.getCreateTime()!=null?log.getCreateTime().getTime():System.currentTimeMillis()));
            pstmt.setDate(4, new Date(log.getAwardDate().getTime()));

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                log.setLogId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert activity,SQLException:" + e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return log;
    }

    @Override
    protected ActivityAwardLog rsToObject(ResultSet rs) throws SQLException {
        ActivityAwardLog log=new ActivityAwardLog();

        log.setUno(rs.getString("uno"));
        log.setActivityId(rs.getLong("activityid"));
        log.setCreateTime(rs.getTimestamp("createtime"));
        log.setAwardDate(rs.getDate("award_date"));
        log.setLogId(rs.getLong("logid"));

        return log;
    }

    @Override
    public List<ActivityAwardLog> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME,queryExpress,conn);
    }

    private String getInsertSql(){
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (uno, activityid, createtime, award_date) VALUES (?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ActivityAwardLog INSERT Script:" + insertSql);
        }

        return insertSql;

    }

}
