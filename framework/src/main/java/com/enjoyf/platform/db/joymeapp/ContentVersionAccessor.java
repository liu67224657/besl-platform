package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.AppContentVersionInfo;
import com.enjoyf.platform.service.joymeapp.AppPageViewInfo;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-22
 * Time: 下午5:21
 * To change this template use File | Settings | File Templates.
 */
public interface ContentVersionAccessor {
    public AppContentVersionInfo insert(AppContentVersionInfo contentVersionInfo, Connection conn) throws DbException;

    public List<AppContentVersionInfo> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<AppContentVersionInfo> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public AppContentVersionInfo get(QueryExpress queryExpress, Connection conn) throws DbException;

    long getLastestVersionByAppKey(String appKey, Connection conn) throws DbException;
}
