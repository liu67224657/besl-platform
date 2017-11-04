package com.enjoyf.platform.db.notice;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.notice.UserNotice;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

public interface UserNoticeAccessor {

    public UserNotice insert(UserNotice userNotice, Connection conn) throws DbException;

    public UserNotice get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<UserNotice> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<UserNotice> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
