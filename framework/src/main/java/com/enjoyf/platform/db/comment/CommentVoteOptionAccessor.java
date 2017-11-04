package com.enjoyf.platform.db.comment;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.comment.CommentForbid;
import com.enjoyf.platform.service.comment.CommentVoteOption;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Diao
 * Date: 14-12-31
 * Time: 下午5:24
 * To change this template use File | Settings | File Templates.
 */
public interface CommentVoteOptionAccessor {

    public CommentVoteOption insert(CommentVoteOption commentVoteOption, Connection conn) throws DbException;

    public CommentVoteOption get(QueryExpress queryExpress, Connection conn) throws DbException;

    public int countCommentVoteOption(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<CommentVoteOption> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<CommentVoteOption> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
