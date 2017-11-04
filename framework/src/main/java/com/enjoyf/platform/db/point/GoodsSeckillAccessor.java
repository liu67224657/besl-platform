package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.GoodsSeckill;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by zhitaoshi on 2015/7/23.
 */
public interface GoodsSeckillAccessor {
    public GoodsSeckill insert(GoodsSeckill goodsSeckill, Connection conn) throws DbException;

    public GoodsSeckill get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<GoodsSeckill> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<GoodsSeckill> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;
}
