package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.event.task.Task;
import com.enjoyf.platform.service.event.task.TaskGroup;
import com.enjoyf.platform.service.event.task.TaskLog;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-5
 * Time: 下午6:57
 * To change this template use File | Settings | File Templates.
 */
public class TaskHandler {

    private DataBaseType dataBaseType;
    private String dataSourceName;

    private TaskAccessor taskAccessor;
    private TaskLogAccessor taskLogAccessor;
    private TaskGroupAccessor taskGroupAccessor;

    public TaskHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        taskAccessor = TableAccessorFactory.get().factoryAccessor(TaskAccessor.class, dataBaseType);
        taskLogAccessor = TableAccessorFactory.get().factoryAccessor(TaskLogAccessor.class, dataBaseType);
        taskGroupAccessor = TableAccessorFactory.get().factoryAccessor(TaskGroupAccessor.class, dataBaseType);
    }


    public Task createTask(Task task) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return taskAccessor.insert(task, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Task getTask(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return taskAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<Task> queryTask(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return taskAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<Task> queryTaskByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<Task> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<Task> list = taskAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<Task>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyTask(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return taskAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public TaskLog createTaskLog(TaskLog taskLog) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return taskLogAccessor.insert(taskLog, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public TaskLog getTaskLog(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return taskLogAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<TaskLog> queryTaskLog(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return taskLogAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<TaskLog> queryTaskLogByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<TaskLog> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<TaskLog> list = taskLogAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<TaskLog>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyTaskLog(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return taskLogAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public TaskGroup insertTaskGroup(TaskGroup taskGroup) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return taskGroupAccessor.insert(taskGroup, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public TaskGroup getTaskGroup(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return taskGroupAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateTaskGroup(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return taskGroupAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<TaskGroup> queryTaskGroup(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return taskGroupAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
}
