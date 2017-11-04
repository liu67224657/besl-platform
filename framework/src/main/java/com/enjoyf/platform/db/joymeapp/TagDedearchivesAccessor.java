package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by zhimingli
 * Date: 2014/12/18
 * Time: 16:15
 */
public interface TagDedearchivesAccessor {
    public TagDedearchives insert(TagDedearchives commentHistory, Connection conn) throws DbException;

    public TagDedearchives get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<TagDedearchives> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<TagDedearchives> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;
     //查询发帖人和发帖数
    public List<Integer> queryPostNum(QueryExpress queryExpress,Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public PageRows<Long> queryByDistinct(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;
}
