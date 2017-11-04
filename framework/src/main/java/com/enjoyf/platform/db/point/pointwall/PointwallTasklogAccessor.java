package com.enjoyf.platform.db.point.pointwall;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.pointwall.PointwallTasklog;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by tonydiao on 2014/11/28.
 */
public interface PointwallTasklogAccessor {



    public List<PointwallTasklog> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public PointwallTasklog get(QueryExpress queryExpress, Connection conn) throws DbException;
    public PointwallTasklog insert(PointwallTasklog pointwallTasklog, Connection conn) throws DbException;

    //查询一个符合一定条件的所有记录,非分页,用于数据导出
    public List<PointwallTasklog> queryAll(QueryExpress queryExpress, int startIndex,int size,Connection conn) throws DbException;


    public int countTotal(QueryExpress queryExpress, Connection conn) throws DbException;


}
