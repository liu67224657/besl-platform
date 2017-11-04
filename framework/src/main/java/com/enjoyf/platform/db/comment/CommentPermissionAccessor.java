package com.enjoyf.platform.db.comment;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.comment.CommentPermission;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * 
 * @author huazhang
 *
 */
public interface CommentPermissionAccessor {

    public CommentPermission insert(CommentPermission permission, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public CommentPermission get(QueryExpress queryExpess, Connection conn) throws DbException;

    public List<CommentPermission> query(QueryExpress add, Connection conn)throws DbException;

    public List<CommentPermission> query(QueryExpress queryExpress, Pagination pagination, Connection conn)throws DbException;
}
