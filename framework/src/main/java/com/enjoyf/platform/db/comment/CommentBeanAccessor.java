package com.enjoyf.platform.db.comment;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-11
 * Time: 上午11:42
 * To change this template use File | Settings | File Templates.
 */
public interface CommentBeanAccessor {

    public CommentBean insert(CommentBean commentBean, Connection conn) throws DbException;

    public CommentBean get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<CommentBean> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<CommentBean> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public List<CommentBean> queryByAvgScore(String keyWords, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
