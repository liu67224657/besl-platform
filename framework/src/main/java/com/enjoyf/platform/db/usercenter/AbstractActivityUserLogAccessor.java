package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityActionType;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityObjectType;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityUserLog;
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
public abstract class AbstractActivityUserLogAccessor extends AbstractBaseTableAccessor<ActivityUserLog> implements ActivityUserLogAccessor {

    private static final Logger logger = LoggerFactory.getLogger(ActivityUserLogAccessor.class);

    private static final String KEY_TABLE_NAME = "activity_user_log";

    @Override
    public ActivityUserLog insert(ActivityUserLog log, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            //activity_userid,appkey,subkey,profileid,uno,action_type,object_type,object_id,action_time
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, log.getAppkey());
            pstmt.setString(2, log.getSubkey());
            pstmt.setString(3, log.getProfileId());
            pstmt.setString(4, log.getUno());
            pstmt.setInt(5, log.getActionType().getCode());
            pstmt.setInt(6, log.getObjectType().getCode());
            pstmt.setString(7, log.getObjectId());
            pstmt.setTimestamp(8, new Timestamp(log.getActionTime() == null ? System.currentTimeMillis() : log.getActionTime().getTime()));

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                log.setActivityUserLogId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return log;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(appkey,subkey,profileid,uno,action_type,object_type,object_id,action_time) VALUES(?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("ActivityUserLog insert sql" + sql);
        }
        return sql;
    }

    @Override
    public ActivityUserLog get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<ActivityUserLog> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ActivityUserLog> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }


    @Override
    protected ActivityUserLog rsToObject(ResultSet rs) throws SQLException {

        //activity_userid,appkey,subkey,profileid,uno,action_type,object_type,object_id,action_time
        ActivityUserLog log = new ActivityUserLog();

        log.setActivityUserLogId(rs.getLong("activity_user_log_id"));
        log.setActionTime(new Date(rs.getTimestamp("action_time").getTime()));
        log.setActionType(ActivityActionType.getByCode(rs.getInt("action_type")));
        log.setAppkey(rs.getString("appkey"));
        log.setSubkey(rs.getString("subkey"));
        log.setObjectId(rs.getString("object_id"));
        log.setObjectType(ActivityObjectType.getByCode(rs.getInt("object_type")));
        log.setProfileId(rs.getString("profileid"));
        log.setUno(rs.getString("uno"));

        return log;
    }
}
