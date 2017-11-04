package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.GiftReserve;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-6-7
 * Time: 下午2:05
 * To change this template use File | Settings | File Templates.
 */
public interface GiftReserveAccessor {

    public GiftReserve insert(GiftReserve giftReserve, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public List<GiftReserve> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public List<GiftReserve> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public GiftReserve get(QueryExpress queryExpress, Connection conn) throws DbException;

}
