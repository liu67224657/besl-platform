package com.enjoyf.platform.db.comment;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.comment.CommentReply;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-13
 * Time: 下午1:48
 * To change this template use File | Settings | File Templates.
 */
public interface CommentReplyAccessor {

    public CommentReply insert(CommentReply commentReply, Connection conn) throws DbException;

    public List<CommentReply> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public List<CommentReply> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public long getMinDisplayOrder(Connection conn) throws DbException;

    public CommentReply get(QueryExpress queryExpress, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;

    public int remove(String commentId, Connection conn) throws DbException;

    public int count(QueryExpress queryExpress, Connection conn) throws DbException;
}
