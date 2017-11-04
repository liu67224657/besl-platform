package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ask.wiki.ContentSum;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

public interface ContentSumAccessor {

    public ContentSum insert(ContentSum contentSum, Connection conn) throws DbException;

    public ContentSum get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<ContentSum> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<ContentSum> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
