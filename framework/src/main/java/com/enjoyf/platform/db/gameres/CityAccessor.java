package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.City;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-21
 * Time: 下午1:51
 * To change this template use File | Settings | File Templates.
 */
public interface CityAccessor {

    public City insert(City city, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public City get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<City> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<City> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;
}
