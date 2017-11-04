package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.social.SocialWatermark;
import com.enjoyf.platform.util.NextPagination;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-27
 * Time: 上午11:09
 * To change this template use File | Settings | File Templates.
 */
public interface SocialWatermarkAccessor {

    public SocialWatermark insert(SocialWatermark socialWaterMark, Connection conn) throws DbException;

    public SocialWatermark get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<SocialWatermark> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<SocialWatermark> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;

    public int getMaxDisplayOrder(Connection conn) throws DbException;

    public List<SocialWatermark> query(NextPagination nextPagination, Connection conn) throws DbException;
}
