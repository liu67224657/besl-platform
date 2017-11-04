package com.enjoyf.platform.db.advertise;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.advertise.AdvertiseAppUrl;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-8-13
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
public interface AdvertiseAppUrlAccessor {
    public AdvertiseAppUrl insert(AdvertiseAppUrl advertiseAppUrl, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public AdvertiseAppUrl get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<AdvertiseAppUrl> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<AdvertiseAppUrl> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

}
