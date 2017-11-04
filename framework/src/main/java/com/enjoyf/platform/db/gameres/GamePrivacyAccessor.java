package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.GamePrivacy;
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
public interface GamePrivacyAccessor {
    //insert
    GamePrivacy insert(GamePrivacy gamePrivcacy, Connection conn) throws DbException;

    //get
    GamePrivacy get(QueryExpress getExpress, Connection conn) throws DbException;

    //query
    List<GamePrivacy> query(QueryExpress queryExpress, Connection conn) throws DbException;

    //update
    int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    //delete
    int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
