package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.AppTips;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-19
 * Time: 上午11:48
 * To change this template use File | Settings | File Templates.
 */
public interface AppTipsAccessor {

    public AppTips insert(AppTips appTips, Connection conn) throws DbException;

    public AppTips get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<AppTips> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<AppTips> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;

}
