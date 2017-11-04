package com.enjoyf.platform.db.notice;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.notice.wiki.WikiNotice;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by ericliu on 16/3/7.
 */
public interface WikiNoticeAccessor {

    public WikiNotice insert(WikiNotice wikiNotice, Connection conn) throws DbException;

    public WikiNotice get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<WikiNotice> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<WikiNotice> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
