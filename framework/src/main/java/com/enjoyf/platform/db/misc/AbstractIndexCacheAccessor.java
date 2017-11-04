/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.misc;


import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.JoymeAppMenu;
import com.enjoyf.platform.service.misc.Feedback;
import com.enjoyf.platform.service.misc.IndexCache;
import com.enjoyf.platform.service.misc.IndexCacheType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectFieldUtil;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class AbstractIndexCacheAccessor extends AbstractSequenceBaseTableAccessor<IndexCache> implements IndexCacheAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractIndexCacheAccessor.class);

    //
    private static final String KEY_TABLE_NAME = "index_cache";

    @Override
    public IndexCache insert(IndexCache indexCache, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, indexCache.getServerId());
            pstmt.setString(2, indexCache.getHtml());
            pstmt.setString(3, indexCache.getVersion());
            pstmt.setString(4, indexCache.getRemoveStatus().getCode());
            pstmt.setInt(5,indexCache.getIndexCacheType().getCode());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                indexCache.setIndexCacheId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert IndexCache, a SQLException occured:", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return indexCache;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<IndexCache> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<IndexCache> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public IndexCache get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public long getMaxId(Connection conn,IndexCacheType type) throws DbException {
        long returnValue = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT MAX(index_cache_id) FROM " + KEY_TABLE_NAME+" WHERE type= ?" ;
        if (logger.isDebugEnabled()) {
            logger.debug("The getMaxId sql:" + sql);
        }

        try {
            //
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1,type.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rs.getLong(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On get, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    protected IndexCache rsToObject(ResultSet rs) throws SQLException {
        IndexCache indexCache = new IndexCache();
        indexCache.setIndexCacheId(rs.getLong("index_cache_id"));
        indexCache.setServerId(rs.getString("server_id"));
        indexCache.setHtml(rs.getString("html"));
        indexCache.setVersion(rs.getString("version"));
        indexCache.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));
        indexCache.setIndexCacheType(IndexCacheType.getByCode(rs.getInt("type")));
        return indexCache;
    }

    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (server_id,html,version,remove_status,type) VALUES (?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("IndexCache INSERT Script:" + insertSql);
        }


        return insertSql;
    }
}
