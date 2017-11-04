package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.event.task.Task;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-12-16
 * Time: 下午3:50
 * To change this template use File | Settings | File Templates.
 */
public interface TaskAccessor {

    public Task insert(Task task, Connection conn) throws DbException;

    public Task get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<Task> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<Task> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
