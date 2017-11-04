package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.event.task.*;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.AppRedirectType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.json.JsonUtil;
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
public abstract class AbstractTaskAccessor extends AbstractBaseTableAccessor<Task> implements TaskAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTaskAccessor.class);
    private static final String KEY_TABLE_NAME = "task";

    @Override
    public Task insert(Task task, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
//task_id,appkey,platform,task_domain,direct_id, task_type,task_name,task_desc,task_pic,
// task_award,over_times,display_order,remove_status,create_time,create_userid
            pstmt.setString(1, task.getTaskId());
            pstmt.setString(2, task.getAppKey());
            pstmt.setInt(3, task.getPlatform() == null ? -1 : task.getPlatform().getCode());
            pstmt.setString(4, task.getTaskGroupId());
            pstmt.setString(5, task.getDirectId());
            pstmt.setInt(6, task.getTaskType().getCode());
            pstmt.setString(7, task.getTaskName());
            pstmt.setString(8, task.getTaskDesc());
            pstmt.setString(9, task.getTaskPic());
            pstmt.setString(10, JsonUtil.beanToJson(task.getTaskAward()));
            pstmt.setInt(11, task.getOverTimes());
            pstmt.setInt(12, task.getDisplayOrder());
            pstmt.setString(13, task.getRemoveStatus().getCode());
            pstmt.setTimestamp(14, new Timestamp(task.getCreateTime() == null ? System.currentTimeMillis() : task.getCreateTime().getTime()));
            pstmt.setString(15, task.getCreateUserId() == null ? "" : task.getCreateUserId());
            pstmt.setInt(16, task.getRedirectType().getCode());
            pstmt.setString(17, task.getRedirectUri());
            pstmt.setBoolean(18, task.isAutoSendAward());
            pstmt.setInt(19, task.getTaskAction().getCode());
            pstmt.setInt(20, task.getBeginHour());
            pstmt.setInt(21, task.getTaskVerifyId());
            pstmt.setString(22, task.getTaskJsonInfo().toJsonStr());
            if (task.getBeginTime() == null) {
                pstmt.setTimestamp(23, null);
            } else {
                pstmt.setTimestamp(23, new Timestamp(task.getBeginTime().getTime()));
            }
            if (task.getEndTime() == null) {
                pstmt.setTimestamp(24, null);
            } else {
                pstmt.setTimestamp(24, new Timestamp(task.getEndTime().getTime()));
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("AbstractTaskAccessor insert task occur Exception.e:", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return task;
    }

    @Override
    public Task get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<Task> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<Task> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected Task rsToObject(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setTaskId(rs.getString("task_id"));
        task.setAppKey(rs.getString("appkey"));
        task.setPlatform(AppPlatform.getByCode(rs.getInt("platform")));
        task.setTaskGroupId(rs.getString("task_group_id"));
        task.setDirectId(rs.getString("direct_id"));
        task.setTaskType(TaskType.getByCode(rs.getInt("task_type")));
        task.setTaskName(rs.getString("task_name"));
        task.setTaskDesc(rs.getString("task_desc"));
        task.setTaskPic(rs.getString("task_pic"));
        try {
            task.setTaskAward(JsonUtil.jsonToBean(rs.getString("task_award"), TaskAward.class));
        } catch (Exception e) {

        }
        task.setOverTimes(rs.getInt("over_times"));
        task.setDisplayOrder(rs.getInt("display_order"));
        task.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));
        task.setCreateTime(rs.getTimestamp("create_time"));
        task.setCreateUserId(rs.getString("create_userid"));
        task.setModifyTime(rs.getTimestamp("modify_time"));
        task.setModifyUserId(rs.getString("modify_userid"));

        task.setRedirectType(AppRedirectType.getByCode(rs.getInt("redirect_type")));
        task.setRedirectUri(rs.getString("redirect_uri"));
        task.setAutoSendAward(rs.getBoolean("auto_sendaward"));
        task.setTaskAction(TaskAction.getByCode(rs.getInt("task_action")));
        task.setBeginHour(rs.getInt("task_hour"));
        task.setTaskVerifyId(rs.getInt("task_verify_id"));
        task.setTaskJsonInfo(TaskJsonInfo.parse(rs.getString("task_jsoninfo")));

        if (rs.getObject("begin_time") != null) {
            task.setBeginTime(rs.getTimestamp("begin_time"));
        }
        if (rs.getObject("end_time") != null) {
            task.setEndTime(rs.getTimestamp("end_time"));
        }
        return task;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(task_id,appkey,platform,task_group_id,direct_id, task_type,task_name,task_desc,task_pic,task_award,over_times,display_order,remove_status,create_time,create_userid,redirect_type,redirect_uri,auto_sendaward,task_action,task_hour,task_verify_id,task_jsoninfo,begin_time,end_time) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AbstractTaskAccessor insert sql:" + sql);
        }
        return sql;
    }
}
