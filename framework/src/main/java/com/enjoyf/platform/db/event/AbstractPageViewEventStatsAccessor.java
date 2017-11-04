package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectFieldUtil;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class AbstractPageViewEventStatsAccessor implements PageViewEventStatsAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractPageViewEventStatsAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "PV_EVENT_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";

    @Override
    public long statsPageView(Date month, QueryExpress queryExpress, Connection conn) throws DbException {
        long returnValue = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(1) AS STATSVAL FROM " + getTableName(month) + " " + ObjectFieldUtil.generateQueryClause(queryExpress, false);
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            //
            pstmt = conn.prepareStatement(sql);

            ObjectFieldUtil.setStmtParams(pstmt, 1, queryExpress);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rs.getLong("STATSVAL");
            }
        } catch (SQLException e) {
            GAlerter.lab("On query, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public long statsUniqueUser(Date month, QueryExpress queryExpress, Connection conn) throws DbException {
        long returnValue = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(DISTINCT(GLOBALID)) AS STATSVAL FROM " + getTableName(month) + " " + ObjectFieldUtil.generateQueryClause(queryExpress, false);
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            //
            pstmt = conn.prepareStatement(sql);

            ObjectFieldUtil.setStmtParams(pstmt, 1, queryExpress);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rs.getLong("STATSVAL");
            }
        } catch (SQLException e) {
            GAlerter.lab("On query, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    private String getTableName(Date d) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableDateSuffix(d, KEY_TABLE_SUFFIX_FMT);
    }
}
