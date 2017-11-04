package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.social.SocialActivity;
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
 * Time: 上午9:48
 * To change this template use File | Settings | File Templates.
 */
public interface SocialActivityAccessor {

    public SocialActivity insert(SocialActivity socialActivity, Connection conn) throws DbException;

    public SocialActivity get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<SocialActivity> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<SocialActivity> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;

    public int getMaxDisplayOrder(Connection conn) throws DbException;

    public List<SocialActivity> query(NextPagination nextPagination, Connection conn) throws DbException;

}
