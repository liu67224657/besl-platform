package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.ClientLineFlag;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-1-13
 * Time: 上午11:51
 * To change this template use File | Settings | File Templates.
 */
public interface ClientLineFlagAccessor {

    public ClientLineFlag insert(ClientLineFlag flag, Connection conn) throws DbException;

    public ClientLineFlag get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<ClientLineFlag> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<ClientLineFlag> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;

}
