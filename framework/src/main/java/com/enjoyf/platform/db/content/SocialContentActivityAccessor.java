package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.social.SocialContentActivity;
import com.enjoyf.platform.util.NextPagination;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-12
 * Time: 下午2:19
 * To change this template use File | Settings | File Templates.
 */
public interface SocialContentActivityAccessor {

    public SocialContentActivity insert(SocialContentActivity socialContentActivity, Connection conn) throws DbException;

    public SocialContentActivity get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<SocialContentActivity> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<SocialContentActivity> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;

    public long getMinDisplayOrder(Connection conn) throws DbException;

    public List<SocialContentActivity> query(long activityId, NextPagination nextPagination, Connection conn) throws DbException;

}
