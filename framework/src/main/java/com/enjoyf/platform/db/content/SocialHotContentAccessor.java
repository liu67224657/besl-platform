package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.social.SocialHotContent;
import com.enjoyf.platform.util.NextPagination;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-4-16
 * Time: 下午8:38
 * To change this template use File | Settings | File Templates.
 */
public interface SocialHotContentAccessor {

    public SocialHotContent insert(SocialHotContent socialHotContent, Connection conn) throws DbException;

    public SocialHotContent get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<SocialHotContent> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<SocialHotContent> query(NextPagination pagination, Connection conn) throws DbException;

    public List<SocialHotContent> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int getMaxValue(Connection conn) throws DbException;

}
