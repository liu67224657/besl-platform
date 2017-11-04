package com.enjoyf.platform.db.point.pointwall;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.pointwall.PointwallApp;
import com.enjoyf.platform.service.point.pointwall.PointwallWallApp;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by tonydiao on 2014/11/28.
 */
public interface PointwallWallAppAccessor {


    //查询分页显示
    public List<PointwallWallApp> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;
    public int queryTotalOfAppsOfOneWall(QueryExpress queryExpress, Connection conn) throws DbException;

    //查询一个积分墙的所有app
    public List<PointwallWallApp> queryAll(QueryExpress queryExpress, Connection conn) throws DbException;

    //查询所有不属于某个积分墙的app的数目
    public int   queryTotalOfApps(QueryExpress queryExpress, Connection conn)  throws DbException;

    //更新某个积分墙的某个app的配置信息
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;


    //在某个积分墙中添加一个app
    public PointwallWallApp insert(PointwallWallApp pointwallWallApp, Connection conn) throws DbException;


    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;

    public PointwallWallApp get(QueryExpress queryExpress, Connection conn) throws DbException;
}
