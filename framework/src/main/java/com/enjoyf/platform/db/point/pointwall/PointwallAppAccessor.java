package com.enjoyf.platform.db.point.pointwall;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.pointwall.PointwallApp;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by tonydiao on 2014/11/28.
 */
public interface PointwallAppAccessor {

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public List<PointwallApp> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public PointwallApp insert(PointwallApp pointwallApp, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;

    public PointwallApp get(QueryExpress queryExpress, Connection conn) throws DbException;
}
