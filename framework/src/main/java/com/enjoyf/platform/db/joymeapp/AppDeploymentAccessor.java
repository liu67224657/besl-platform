package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.AppDeployment;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-7-29
 * Time: 下午6:43
 * To change this template use File | Settings | File Templates.
 */
public interface AppDeploymentAccessor {

    public AppDeployment insert(AppDeployment appDeployment, Connection conn) throws DbException;

    public AppDeployment get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<AppDeployment> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;
}
