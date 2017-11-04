package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.gamedb.GameDBRelation;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/26
 * Description:
 */
public interface GameDBRelationAccessor {
    //insert
    GameDBRelation insert(GameDBRelation relation, Connection conn) throws DbException;

    //get
    GameDBRelation get(QueryExpress getExpress, Connection conn) throws DbException;

    //query
    List<GameDBRelation> query(QueryExpress queryExpress, Connection conn) throws DbException;

    //update
    int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    //delete
    int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
