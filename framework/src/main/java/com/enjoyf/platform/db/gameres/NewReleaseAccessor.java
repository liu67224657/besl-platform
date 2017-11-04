package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.NewRelease;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-21
 * Time: 上午10:07
 * To change this template use File | Settings | File Templates.
 */
public interface NewReleaseAccessor {

    public NewRelease insert(NewRelease newRelease, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;

    public NewRelease get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<NewRelease> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<NewRelease> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

}
