package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.AppInfo;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-7-24
 * Time: 下午3:23
 * To change this template use File | Settings | File Templates.
 */
public interface AppInfoAccessor {

    public AppInfo insert(AppInfo appInfo, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public List<AppInfo> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<AppInfo> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public AppInfo get(QueryExpress queryExpress, Connection conn) throws DbException;
}
