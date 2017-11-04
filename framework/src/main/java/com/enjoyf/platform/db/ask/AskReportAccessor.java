package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ask.AskReport;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

public interface AskReportAccessor {

    public AskReport insert(AskReport askReport, Connection conn) throws DbException;

    public AskReport get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<AskReport> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<AskReport> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
