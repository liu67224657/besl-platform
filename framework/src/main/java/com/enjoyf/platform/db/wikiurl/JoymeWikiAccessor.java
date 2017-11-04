package com.enjoyf.platform.db.wikiurl;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.JoymeWiki;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by zhitaoshi on 2015/4/14.
 */
public interface JoymeWikiAccessor {

    public JoymeWiki get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<JoymeWiki> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<JoymeWiki> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

}
