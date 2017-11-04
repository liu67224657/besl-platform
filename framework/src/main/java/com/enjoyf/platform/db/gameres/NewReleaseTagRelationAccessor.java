package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.NewReleaseTagRelation;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-21
 * Time: 下午12:39
 * To change this template use File | Settings | File Templates.
 */
public interface NewReleaseTagRelationAccessor {

    public NewReleaseTagRelation insert(NewReleaseTagRelation newTagRelation, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public NewReleaseTagRelation get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<NewReleaseTagRelation> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<NewReleaseTagRelation> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;
}
