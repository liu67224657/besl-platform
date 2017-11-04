package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 15-3-3
 * Time: 下午7:26
 * To change this template use File | Settings | File Templates.
 */
public interface AppConfigAccessor {
    public AppConfig insert(AppConfig appConfig, Connection conn) throws DbException;

    public AppConfig get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<AppConfig> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<AppConfig> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
