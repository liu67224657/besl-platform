package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.event.task.TaskAward;
import com.enjoyf.platform.service.event.task.TaskGroup;
import com.enjoyf.platform.service.event.task.TaskGroupShowType;
import com.enjoyf.platform.service.event.task.TaskGroupType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
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
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/7
 * Description:
 */
public class AbstractTaskGroupAccessor extends AbstractBaseTableAccessor<TaskGroup> implements TaskGroupAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTaskGroupAccessor.class);
    private static final String KEY_TABLE_NAME = "task_group";

    @Override
    public TaskGroup insert(TaskGroup taskGroup, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());

            pstmt.setString(1, taskGroup.getTaskGroupId());
            pstmt.setString(2, taskGroup.getTaskGroupName());
            pstmt.setString(3, taskGroup.getAppKey());
            pstmt.setInt(4, taskGroup.getTaskCount());
            pstmt.setString(5, taskGroup.getTaskAward() == null ? "" : JsonUtil.beanToJson(taskGroup.getTaskAward()));
            pstmt.setInt(6, taskGroup.getAppPlatform().getCode());
            pstmt.setTimestamp(7, new Timestamp(taskGroup.getCreateTime() == null ? System.currentTimeMillis() : taskGroup.getCreateTime().getTime()));
            pstmt.setString(8, taskGroup.getCreateUserId());
            pstmt.setString(9, taskGroup.getRemoveStatus().getCode());
            pstmt.setInt(10, taskGroup.getDisplayOrder());
            pstmt.setString(11, taskGroup.getType().getCode());
            pstmt.setInt(12, taskGroup.getShowtype().getCode());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("AbstractTaskAccessor insert task occur Exception.e:", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return taskGroup;
    }

    @Override
    public TaskGroup get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<TaskGroup> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<TaskGroup> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected TaskGroup rsToObject(ResultSet rs) throws SQLException {
        TaskGroup taskGroup = new TaskGroup();

        taskGroup.setTaskGroupId(rs.getString("task_group_id"));
        taskGroup.setTaskGroupName(rs.getString("task_group_name"));
        taskGroup.setAppKey(rs.getString("appkey"));
        taskGroup.setTaskCount(rs.getInt("task_count"));
        try {
            taskGroup.setTaskAward(JsonUtil.jsonToBean(rs.getString("task_award"), TaskAward.class));
        } catch (Exception e) {

        }
        taskGroup.setAppPlatform(AppPlatform.getByCode(rs.getInt("platform")));
        taskGroup.setCreateTime(rs.getTimestamp("create_time"));
        taskGroup.setCreateUserId(rs.getString("create_userid"));
        taskGroup.setModifyTime(rs.getTimestamp("modify_time"));
        taskGroup.setModifyUserId(rs.getString("modify_userid"));
        taskGroup.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));
        taskGroup.setType(TaskGroupType.getByCode(rs.getString("type")));
        if (rs.getObject("showtype") != null) {
            taskGroup.setShowtype(TaskGroupShowType.getByCode(rs.getInt("showtype")));
        }
        if (rs.getObject("display_order") != null) {
            taskGroup.setDisplayOrder(rs.getInt("display_order"));
        }
        return taskGroup;
    }

    protected String getInsertSql() throws SQLException {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(task_group_id,task_group_name,appkey,task_count,task_award, platform,create_time,create_userid,remove_status,display_order,type,showtype) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AbstractTaskAccessor insert sql:" + sql);
        }
        return sql;
    }
}
