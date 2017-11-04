package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.event.task.*;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-12-16
 * Time: 下午4:04
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractTaskLogAccessor extends AbstractBaseTableAccessor<TaskLog> implements TaskLogAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTaskLogAccessor.class);
    private static final String KEY_TABLE_NAME = "task_log";

    @Override
    public TaskLog insert(TaskLog taskLog, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            //log_id,profileid,task_id,over_time,over_ip,over_status,appkey,platform,task_type
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, taskLog.getLogId());
            pstmt.setString(2, taskLog.getProfileId());
            pstmt.setString(3, taskLog.getTaskId());
            pstmt.setTimestamp(4, new Timestamp(taskLog.getOverTime() == null ? System.currentTimeMillis() : taskLog.getOverTime().getTime()));
            pstmt.setString(5, taskLog.getOverIp());
            pstmt.setString(6, taskLog.getOverStatus().getCode());
            pstmt.setString(7, taskLog.getAppKey());
            pstmt.setInt(8, taskLog.getPlatform() == null ? -1 : taskLog.getPlatform().getCode());
            pstmt.setInt(9, taskLog.getTaskType().getCode());


            pstmt.executeUpdate();

        } catch (SQLException e) {
            //GAlerter.lab("AbstractTaskLogAccessor insert task occur Exception.e:", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return taskLog;
    }

    @Override
    public TaskLog get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<TaskLog> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<TaskLog> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected TaskLog rsToObject(ResultSet rs) throws SQLException {
        TaskLog taskLog = new TaskLog();
        taskLog.setLogId(rs.getString("log_id"));
        taskLog.setProfileId(rs.getString("profileid"));
        taskLog.setTaskId(rs.getString("task_id"));
        taskLog.setAppKey(rs.getString("appkey"));
        taskLog.setPlatform(AppPlatform.getByCode(rs.getInt("platform")));
        taskLog.setOverTime(rs.getTimestamp("over_time"));
        taskLog.setOverIp(rs.getString("over_ip"));
        taskLog.setOverStatus(ActStatus.getByCode(rs.getString("over_status")));
        taskLog.setTaskType(TaskType.getByCode(rs.getType()));
        taskLog.setOverTimes(rs.getInt("over_times"));
        return taskLog;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(log_id,profileid,task_id,over_time,over_ip,over_status,appkey,platform,task_type) VALUES(?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AbstractTaskLogAccessor insert sql:" + sql);
        }
        return sql;
    }
}
