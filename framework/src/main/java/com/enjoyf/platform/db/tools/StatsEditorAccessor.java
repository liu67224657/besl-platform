package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.tools.StatsEditor;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * the accessor of stats editor.
 */
interface StatsEditorAccessor {
    //
    public StatsEditor insert(StatsEditor entity, Connection conn) throws DbException;

    //
    public StatsEditor get(QueryExpress getExpress, Connection conn) throws DbException;

    //
    public List<StatsEditor> query(QueryExpress queryExpress, Connection conn) throws DbException;

      public List<StatsEditor> query(QueryExpress queryExpress,Pagination pagination, Connection conn) throws DbException;

    //
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    //
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
