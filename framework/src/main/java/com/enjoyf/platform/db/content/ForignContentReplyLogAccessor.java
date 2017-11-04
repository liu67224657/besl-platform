package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.ForignContentReplyLog;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-2-12
 * Time: 下午2:37
 * To change this template use File | Settings | File Templates.
 */
public interface ForignContentReplyLogAccessor {

    public ForignContentReplyLog insert(ForignContentReplyLog log, Connection conn) throws DbException;

    public ForignContentReplyLog get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<ForignContentReplyLog> query(QueryExpress queryExpress, Connection conn) throws DbException;

}
