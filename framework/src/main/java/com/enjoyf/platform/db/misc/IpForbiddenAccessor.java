package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.misc.IpForbidden;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-4-6
 * Time: 上午11:42
 * To change this template use File | Settings | File Templates.
 */
public interface IpForbiddenAccessor {
    //insert
    public IpForbidden insert(IpForbidden entry, Connection conn) throws DbException;

    //get by properties
    public IpForbidden get(QueryExpress queryExpress, Connection conn) throws DbException;

    //query
    public List<IpForbidden> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

    public List<IpForbidden> query(QueryExpress queryExpress, Connection conn) throws DbException;

    //update
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;

}
