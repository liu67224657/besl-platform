package com.enjoyf.platform.db.comment;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.comment.CommentHistory;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-19
 * Time: 下午5:18
 * To change this template use File | Settings | File Templates.
 */
public interface CommentHistoryAccessor {

    public CommentHistory insert(CommentHistory commentHistory, Connection conn) throws DbException;

    public CommentHistory get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<CommentHistory> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
