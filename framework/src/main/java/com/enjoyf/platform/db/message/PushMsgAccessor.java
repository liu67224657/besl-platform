package com.enjoyf.platform.db.message;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.message.PushMessage;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-1
 * Time: 下午3:09
 * To change this template use File | Settings | File Templates.
 */
public interface PushMsgAccessor {
    //insert
    public PushMessage insert(PushMessage pushMessage, Connection conn) throws DbException;

    //get by id
    public PushMessage get(QueryExpress queryExpress, Connection conn) throws DbException;

    //query
    public List<PushMessage> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

    public List<PushMessage> query(QueryExpress queryExpress, Rangination range, Connection conn) throws DbException;

    //update
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
