package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.GamePrivacy;
import com.enjoyf.platform.service.gameres.WikiResource;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-2
 * Time: 下午5:56
 * To change this template use File | Settings | File Templates.
 */
public interface WikiResourceAccessor {

    //insert
    WikiResource insert(WikiResource wikiResource, Connection conn) throws DbException;

    //get
    WikiResource get(QueryExpress getExpress, Connection conn) throws DbException;

    //query
    List<WikiResource> query(QueryExpress queryExpress, Connection conn) throws DbException;

    List<WikiResource> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    //update
    int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    //delete
    int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
