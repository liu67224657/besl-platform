package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.event.task.TaskGroup;
import com.enjoyf.platform.service.event.task.TaskLog;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-12-16
 * Time: 下午7:01
 * To change this template use File | Settings | File Templates.
 */
public interface TaskGroupAccessor {
    public TaskGroup insert(TaskGroup taskGroup, Connection conn) throws DbException;

    public TaskGroup get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<TaskGroup> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<TaskGroup> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
