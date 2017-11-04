package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;


/**
 * Author: taijunli
 * Date: 12-1-11
 * Time: 下午4:51
 * Desc:
 */
public interface GameResourceAccessor {
    //insert
    GameResource insert(GameResource gameResource, Connection conn) throws DbException;

    //get
    GameResource get(QueryExpress getExpress, Connection conn) throws DbException;

    //query
    List<GameResource> query(QueryExpress queryExpress, Connection conn) throws DbException;

    List<GameResource> queryByPage(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

    List<GameResource> queryBySynonyms(String synonyms, Connection conn) throws DbException;

    //update
    int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    //delete
    int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
