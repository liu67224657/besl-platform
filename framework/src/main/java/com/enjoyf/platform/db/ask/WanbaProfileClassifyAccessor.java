package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ask.WanbaProfileClassify;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

public interface WanbaProfileClassifyAccessor {

    public WanbaProfileClassify insert(WanbaProfileClassify answer, Connection conn) throws DbException;

    public WanbaProfileClassify get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<WanbaProfileClassify> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<WanbaProfileClassify> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

}
