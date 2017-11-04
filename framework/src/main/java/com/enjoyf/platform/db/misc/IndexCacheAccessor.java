/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.misc.IndexCache;
import com.enjoyf.platform.service.misc.IndexCacheType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public interface IndexCacheAccessor {
    public IndexCache insert(IndexCache indexCache, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public List<IndexCache> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<IndexCache> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public IndexCache get(QueryExpress queryExpress, Connection conn) throws DbException;

    public long getMaxId(Connection conn,IndexCacheType cacheType)throws DbException;
}
