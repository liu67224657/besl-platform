package com.enjoyf.platform.db.message;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.message.ClientDevice;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-14
 * Time: 下午12:50
 * To change this template use File | Settings | File Templates.
 */
public interface ClientDeviceAccessor {

    public ClientDevice insert(ClientDevice mobileDevice, Connection conn) throws DbException;

    public ClientDevice get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<ClientDevice> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<ClientDevice> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;

}
