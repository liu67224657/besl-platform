package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.misc.RefreshCMSTiming;
import com.enjoyf.platform.service.misc.RefreshReleaseType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by zhimingli on 2015/7/28.
 */
public class AbstractRefreshCMSTimingReleaseAccessor extends AbstractBaseTableAccessor<RefreshCMSTiming> implements RefreshCMSTimingReleaseAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(RefreshCMSTimingReleaseAccessor.class);

    //
    private static final String KEY_TABLE_NAME = "refresh_cms_timing_release";

    @Override
    public RefreshCMSTiming get(QueryExpress getExpress, Connection conn) throws DbException {
        return get(KEY_TABLE_NAME, getExpress, conn);
    }

    @Override
    public RefreshCMSTiming insert(RefreshCMSTiming entity, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = getInsertSql();

        try {
            pstmt = conn.prepareStatement(sql);

            //cms_id, cms_name, release_type, release_time, remove_status,modify_user,modify_time
            pstmt.setString(1, entity.getCms_id());
            pstmt.setString(2, entity.getCms_name());
            pstmt.setInt(3, entity.getRefreshReleaseType().getCode());

            pstmt.setLong(4, entity.getRelease_time());
            pstmt.setString(5, entity.getRemove_status().getCode());
            pstmt.setString(6, entity.getModify_user());

            pstmt.setTimestamp(7, new Timestamp(entity.getModify_time() != null ? entity.getModify_time().getTime() : System.currentTimeMillis()));

            pstmt.execute();
        } catch (SQLException e) {
            GAlerter.lab("On insert, a SQLException occured.", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return entity;
    }


    @Override
    public List<RefreshCMSTiming> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<RefreshCMSTiming> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }


    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (cms_id, cms_name, release_type, release_time, remove_status,modify_user,modify_time) " +
                "VALUES (?, ?, ?, ?, ?, ?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    //time_id, cms_id, cms_name, release_type, release_time, remove_status,modify_user,modify_time
    protected RefreshCMSTiming rsToObject(ResultSet rs) throws SQLException {
        RefreshCMSTiming entity = new RefreshCMSTiming();

        //
        entity.setTime_id(rs.getLong("time_id"));
        entity.setCms_id(rs.getString("cms_id"));
        entity.setCms_name(rs.getString("cms_name"));
        entity.setRefreshReleaseType(RefreshReleaseType.getByCode(rs.getInt("release_type")));
        entity.setRelease_time(rs.getLong("release_time"));
        entity.setRemove_status(ActStatus.getByCode(rs.getString("remove_status")));
        entity.setModify_user(rs.getString("modify_user"));
        entity.setModify_time(rs.getTimestamp("modify_time"));

        //
        return entity;
    }
}
