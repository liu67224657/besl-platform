package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeappconfig.AppChannel;
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
public interface AppChannelAccessor {

    public AppChannel insert(AppChannel appChannel, Connection conn) throws DbException;

    public AppChannel get(long channelId, Connection conn) throws DbException;

    public int update(long channelId, UpdateExpress updateExpress, Connection conn) throws DbException;

    public List<AppChannel> query(Connection conn) throws DbException;

    public List<AppChannel> queryByPage(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;
}
