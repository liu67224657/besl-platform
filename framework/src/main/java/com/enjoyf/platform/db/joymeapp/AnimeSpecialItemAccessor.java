package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.AnimeSpecialItem;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-7-24
 * Time: 下午6:09
 * To change this template use File | Settings | File Templates.
 */
public interface AnimeSpecialItemAccessor {

    public AnimeSpecialItem insert(AnimeSpecialItem animeSpecialItem, Connection conn) throws DbException;

    public AnimeSpecialItem get(QueryExpress queryExpress, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;

    public List<AnimeSpecialItem> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<AnimeSpecialItem> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;
}
