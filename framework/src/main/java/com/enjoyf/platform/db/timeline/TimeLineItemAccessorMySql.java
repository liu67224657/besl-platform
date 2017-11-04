package com.enjoyf.platform.db.timeline;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.timeline.TimeLineDomain;
import com.enjoyf.platform.service.timeline.TimeLineFilterType;
import com.enjoyf.platform.service.timeline.TimeLineItem;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class TimeLineItemAccessorMySql extends AbstractTimeLineItemAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(TimeLineItemAccessorMySql.class);

    @Override
    public List<TimeLineItem> query(String ownUno, TimeLineDomain domain, Pagination page, Connection conn) throws DbException {
        List<TimeLineItem> returnValue = new ArrayList<TimeLineItem>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName(domain, ownUno) + " WHERE OWNUNO = ? AND REMOVESTATUS = ? ORDER BY CREATEDATE DESC LIMIT ?, ?";
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineItemAccessorMySql query sql:" + sql);
        }

        try {
            page.setTotalRows(queryRowSize(ownUno, domain, conn));

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ownUno);
            pstmt.setString(2, ActStatus.UNACT.getCode());
            pstmt.setInt(3, page.getStartRowIdx());
            pstmt.setInt(4, page.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On select TimeLineItem, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public List<TimeLineItem> queryOnlyFocus(String ownUno, TimeLineDomain domain, Pagination page, Connection conn) throws DbException {
        List<TimeLineItem> returnValue = new ArrayList<TimeLineItem>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName(domain, ownUno) + " WHERE OWNUNO = ? AND REMOVESTATUS = ? AND DIRECTUNO != ? ORDER BY CREATEDATE DESC LIMIT ?, ?";
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineItemAccessorMySql query sql:" + sql);
        }

        try {
            page.setTotalRows(queryRowSize(ownUno, domain, conn));

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ownUno);
            pstmt.setString(2, ActStatus.UNACT.getCode());
            pstmt.setString(3,ownUno);
            pstmt.setInt(4, page.getStartRowIdx());
            pstmt.setInt(5, page.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On select TimeLineItem, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    public List<TimeLineItem> queryBefore(String ownUno, TimeLineDomain domain, Long before, Integer size, Connection conn) throws DbException {
        List<TimeLineItem> returnValue = new ArrayList<TimeLineItem>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName(domain, ownUno) + " WHERE OWNUNO = ? AND CREATEDATE < ? AND REMOVESTATUS = ? ORDER BY CREATEDATE DESC LIMIT ?";
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineItemAccessorMySql queryTimelinesBefore sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ownUno);
            pstmt.setTimestamp(2, new Timestamp(before));
            pstmt.setString(3, ActStatus.UNACT.getCode());
            pstmt.setInt(4, size);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On select TimeLineItem, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    public List<TimeLineItem> queryAfter(String ownUno, TimeLineDomain domain, Long after, Integer size, Connection conn) throws DbException {
        List<TimeLineItem> returnValue = new ArrayList<TimeLineItem>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName(domain, ownUno) + " WHERE OWNUNO = ? AND CREATEDATE > ? AND REMOVESTATUS = ? ORDER BY CREATEDATE DESC LIMIT ?";
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineItemAccessorMySql queryTimelinesAfter sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ownUno);
            pstmt.setTimestamp(2, new Timestamp(after));
            pstmt.setString(3, ActStatus.UNACT.getCode());
            pstmt.setInt(4, size);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On select TimeLineItem, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public List<TimeLineItem> queryOrg(String ownUno, TimeLineDomain domain, Pagination page, Connection conn) throws DbException {
        List<TimeLineItem> returnValue = new ArrayList<TimeLineItem>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName(domain, ownUno) + " WHERE OWNUNO = ? AND REMOVESTATUS = ? AND RELATIONID IS NULL ORDER BY CREATEDATE DESC LIMIT ?, ?";
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineItemAccessorMySql queryOrg:" + sql);
        }

        try {
            page.setTotalRows(queryRowSizeOrg(ownUno, domain, conn));

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ownUno);
            pstmt.setString(2, ActStatus.UNACT.getCode());
            pstmt.setInt(3, page.getStartRowIdx());
            pstmt.setInt(4, page.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On select TimeLineItem, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    private int queryRowSize(String ownUno, TimeLineDomain domain, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(1) FROM " + getTableName(domain, ownUno) + " FORCE INDEX (" + getUnoRemoveCreateDateIndexName(domain, ownUno) + ") WHERE OWNUNO = ? AND REMOVESTATUS = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineItemAccessorMySql queryRowSize:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ownUno);
            pstmt.setString(2, ActStatus.UNACT.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On TimeLineItemAccessorMySql queryRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

    private int queryRowSizeOrg(String ownUno, TimeLineDomain domain, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(1) FROM " + getTableName(domain, ownUno) + " WHERE OWNUNO = ? AND REMOVESTATUS = ? AND RELATIONID IS NULL";
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineItemAccessorMySql queryRowSizeOrg:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ownUno);
            pstmt.setString(2, ActStatus.UNACT.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On TimeLineItemAccessorMySql queryRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

    @Override
    public List<TimeLineItem> query(String ownUno, TimeLineDomain domain, TimeLineFilterType timeLineFilterType, Pagination page, Connection conn) throws DbException {
        List<TimeLineItem> returnValue = new ArrayList<TimeLineItem>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName(domain, ownUno) + " WHERE OWNUNO = ? AND REMOVESTATUS = ? AND FILTERTYPE & ?  ORDER BY CREATEDATE DESC LIMIT ?, ?";
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineItemAccessorMySql query:" + sql);
        }

        try {
            page.setTotalRows(queryRowSize(ownUno, domain, timeLineFilterType, conn));
            pstmt = conn.prepareStatement(sql);


            pstmt.setString(1, ownUno);
            pstmt.setString(2, ActStatus.UNACT.getCode());
            pstmt.setInt(3, timeLineFilterType == null ? 0 : timeLineFilterType.getValue());
            pstmt.setInt(4, page.getStartRowIdx());
            pstmt.setInt(5, page.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On select TimeLineItem, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public List<TimeLineItem> queryRelationID(String ownUno, String relationId, TimeLineDomain domain, TimeLineFilterType timeLineFilterType, Pagination page, Connection conn) throws DbException {
        List<TimeLineItem> returnValue = new ArrayList<TimeLineItem>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName(domain, ownUno) + " WHERE OWNUNO = ? AND RELATIONID = ? AND FILTERTYPE & ?  ORDER BY CREATEDATE DESC LIMIT ?, ?";
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineItemAccessorMySql queryRelationID:" + sql);
        }

        try {
            page.setTotalRows(queryRowSize(ownUno, domain, timeLineFilterType, conn));
            pstmt = conn.prepareStatement(sql);


            pstmt.setString(1, ownUno);
            pstmt.setString(2, relationId);
            pstmt.setInt(3, timeLineFilterType == null ? 0 : timeLineFilterType.getValue());
            pstmt.setInt(4, page.getStartRowIdx());
            pstmt.setInt(5, page.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On select TimeLineItem, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    private int queryRowSize(String ownUno, TimeLineDomain domain, TimeLineFilterType timeLineFilterType, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(1) FROM " + getTableName(domain, ownUno) + " WHERE OWNUNO = ? AND REMOVESTATUS = ? AND FILTERTYPE & ?";
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineItemAccessorMySql queryRowSize:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ownUno);
            pstmt.setString(2, ActStatus.UNACT.getCode());
            pstmt.setInt(3, timeLineFilterType == null ? 0 : timeLineFilterType.getValue());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On TimeLineItemAccessorMySql queryRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

}
