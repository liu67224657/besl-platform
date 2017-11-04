package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.NewReleaseTag;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-21
 * Time: 下午12:06
 * To change this template use File | Settings | File Templates.
 */
public interface NewReleaseTagAccessor {
    public NewReleaseTag insert(NewReleaseTag newGameTag, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;

    public NewReleaseTag get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<NewReleaseTag> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<NewReleaseTag> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;
}
