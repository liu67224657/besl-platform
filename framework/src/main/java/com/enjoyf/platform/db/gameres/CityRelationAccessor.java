package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.CityRelation;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-21
 * Time: 下午2:19
 * To change this template use File | Settings | File Templates.
 */
public interface CityRelationAccessor {

    public CityRelation insert(CityRelation cityRelation, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public CityRelation get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<CityRelation> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<CityRelation> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;
}
