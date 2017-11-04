package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.usercenter.Verify;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

public interface VerifyAccessor {

    public Verify insert(Verify Verify, Connection conn) throws DbException;

    public Verify get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<Verify> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<Verify> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
