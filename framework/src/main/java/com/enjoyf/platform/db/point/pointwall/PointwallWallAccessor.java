package com.enjoyf.platform.db.point.pointwall;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.pointwall.PointwallApp;
import com.enjoyf.platform.service.point.pointwall.PointwallWall;
import com.enjoyf.platform.service.point.pointwall.PointwallWallApp;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by tonydiao on 2014/11/28.
 */
public interface PointwallWallAccessor {

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public List<PointwallWall> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public List<PointwallWall> queryAll(QueryExpress queryExpress, Connection conn) throws DbException;


    public int queryTotalWalls(QueryExpress queryExpress, Connection conn) throws DbException;


    public PointwallWall insert(PointwallWall pointwallWall, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;

    public PointwallWall get(QueryExpress queryExpress, Connection conn) throws DbException;




}
