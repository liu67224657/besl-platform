package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.social.SocialTag;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-9
 * Time: 下午3:27
 * To change this template use File | Settings | File Templates.
 */
public interface SocialTagAccessor {

    public SocialTag insert(SocialTag socialTag, Connection conn) throws DbException;

    public SocialTag get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<SocialTag> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<SocialTag> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;

}
