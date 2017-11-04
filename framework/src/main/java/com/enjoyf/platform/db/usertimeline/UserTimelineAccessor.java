package com.enjoyf.platform.db.usertimeline;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.timeline.UserTimeline;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

public interface UserTimelineAccessor {

    public UserTimeline insert(UserTimeline userTimeline, Connection conn) throws DbException;

    public UserTimeline get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<UserTimeline> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<UserTimeline> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;

}
