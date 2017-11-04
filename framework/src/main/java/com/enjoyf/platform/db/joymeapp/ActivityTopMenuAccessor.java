package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.ActivityTopMenu;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-9-17
 * Time: 下午12:29
 * To change this template use File | Settings | File Templates.
 */
public interface ActivityTopMenuAccessor {

    public ActivityTopMenu insert(ActivityTopMenu activityTopMenu, Connection conn) throws DbException;

    public List<ActivityTopMenu> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<ActivityTopMenu> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public ActivityTopMenu get(QueryExpress queryExpress, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;
}
