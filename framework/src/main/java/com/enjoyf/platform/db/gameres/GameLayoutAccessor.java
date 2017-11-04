package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.GameLayout;
import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;


/**
 * Author: 刘浩
 * Date: 12-1-11
 * Time: 下午4:51
 * Desc: 游戏视图模板的Accessor
 */
public interface GameLayoutAccessor {
    //insert
    GameLayout insert(GameLayout gameLayout, Connection conn) throws DbException;

    //get
    GameLayout get(QueryExpress getExpress, Connection conn) throws DbException;

    //query
    List<GameLayout> query(QueryExpress queryExpress, Connection conn) throws DbException;

    //update
    int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    //delete
    int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
