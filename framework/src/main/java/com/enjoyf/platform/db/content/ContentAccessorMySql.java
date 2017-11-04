package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentPublishType;
import com.enjoyf.platform.service.content.ContentQueryParam;
import com.enjoyf.platform.service.content.ContentType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a> ,zx
 */
class ContentAccessorMySql extends AbstractContentAccessor {

    private static final Logger logger = LoggerFactory.getLogger(ContentAccessorMySql.class);

    @Override
    public List<Content> queryContents(String uno, Pagination page, Connection conn) throws DbException {
        List<Content> returnValue = new ArrayList<Content>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            page.setTotalRows(queryUnoRowSize(uno, conn));

            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName() + " USE KEY(IDX_CONTENT_UNO) WHERE UNO = ? AND REMOVESTATUS = ? ORDER BY PUBLISHDATE DESC LIMIT ?, ?");

            pstmt.setString(1, uno);
            pstmt.setString(2, ActStatus.UNACT.getCode());

            pstmt.setInt(3, page.getStartRowIdx());
            pstmt.setInt(4, page.getPageSize());

            rs = pstmt.executeQuery();
            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On query Contents, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
            DataBaseUtil.closeResultSet(rs);
        }

        return returnValue;
    }

    private int queryUnoRowSize(String uno, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT COUNT(1) FROM " + getTableName() + " WHERE UNO = ? AND REMOVESTATUS = ?");

            pstmt.setString(1, uno);
            pstmt.setString(2, ActStatus.UNACT.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On content queryUnoRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

    @Override
    public List<Content> queryLastestContents(ContentPublishType publishType, ContentType contentType, Integer size, Connection conn) throws DbException {
        List<Content> returnValue = new ArrayList<Content>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName() + " WHERE PUBLISHTYPE = ? AND CONTENTTYPE&? > 0 AND  REMOVESTATUS = ? ORDER BY PUBLISHDATE DESC LIMIT ?");

            pstmt.setString(1, publishType.getCode());
            pstmt.setInt(2, contentType.getValue());
            pstmt.setString(3, ActStatus.UNACT.getCode());
            pstmt.setInt(4, size);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On query Contents, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public List<Content> queryLastestContents(String uno, Integer size, Connection conn) throws DbException {
        List<Content> returnValue = new ArrayList<Content>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName() + " USE KEY(IDX_CONTENT_UNO) WHERE UNO = ? AND  REMOVESTATUS = ? ORDER BY PUBLISHDATE DESC LIMIT ?");

            pstmt.setString(1, uno);
            pstmt.setString(2, ActStatus.UNACT.getCode());
            pstmt.setInt(3, size);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryLastestContents, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public List<Content> queryContentsByTimeStep(java.util.Date startDate, java.util.Date endDate, Pagination page, Connection conn) throws DbException {
        //
        List<Content> returnValue = new ArrayList<Content>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            page.setTotalRows(queryRowSizeByDateStep(startDate, endDate, conn));
            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName()
                    + " WHERE UPDATEDATE BETWEEN ? AND ? AND REMOVESTATUS = ?  LIMIT ?, ?");
            int param = 1;
            pstmt.setTimestamp(param++, new Timestamp(startDate.getTime()));
            pstmt.setTimestamp(param++, new Timestamp(endDate.getTime()));
            pstmt.setString(param++, ActStatus.UNACT.getCode());
            pstmt.setInt(param++, page.getStartRowIdx());
            pstmt.setInt(param++, page.getPageSize());

            rs = pstmt.executeQuery();
            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryContentsByTimeStep, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public List<Content> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return query(getTableName(), queryExpress, page, conn);
    }

    private int viewQueryRowSize(ContentQueryParam param, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(1) FROM " + getTableNameByViewContent() + " WHERE 1=1");

        if (param != null && !Strings.isNullOrEmpty(param.getUno())) {
            sql.append(" AND UNO =?");
        }
        if (param != null && param.getStartDate() != null && param.getEndDate() != null) {
            sql.append(" AND publishDate BETWEEN ? AND ?");
        }
        if (param != null && param.getAuditStatus() != null && param.getAuditStatus().getValue() > 0 && param.getAuditStatus().getResult() > 0) {
            sql.append(" AND AUDITSTATUS & ? = ?");
        }
        if (param != null && param.getContentType() != null && param.getContentType().getValue() > 0) {
            sql.append(" AND CONTENTTYPE & ? >0");
        }
        if (param != null && !Strings.isNullOrEmpty(param.getKey())) {
            sql.append(" AND CONTENTBODY LIKE ?");
        }
        if (param != null && param.getRemoveStatus() != null) {
            sql.append(" AND REMOVESTATUS = ?");
        }

        try {
            int i = 1;
            pstmt = conn.prepareStatement(sql.toString());

            if (param != null && !Strings.isNullOrEmpty(param.getUno())) {
                pstmt.setString(i++, param.getUno());
            }
            if (param != null && param.getStartDate() != null && param.getEndDate() != null) {
                pstmt.setTimestamp(i++, new Timestamp(param.getStartDate().getTime()));
                pstmt.setTimestamp(i++, new Timestamp(param.getEndDate().getTime()));
            }
            if (param != null && param.getAuditStatus() != null && param.getAuditStatus().getValue() > 0 && param.getAuditStatus().getResult() > 0) {
                pstmt.setInt(i++, param.getAuditStatus().getValue());
                pstmt.setInt(i++, param.getAuditStatus().getResult());
            }
            if (param != null && param.getContentType() != null && param.getContentType().getValue() > 0) {
                pstmt.setInt(i++, param.getContentType().getValue());
            }
            if (param != null && !Strings.isNullOrEmpty(param.getKey())) {
                pstmt.setString(i++, "%" + param.getKey() + "%");
            }
            if (param != null && param.getRemoveStatus() != null) {
                pstmt.setString(i++, param.getRemoveStatus().getCode());
            }

            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On content queryRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

    private int queryRowSizeByDateStep(java.util.Date startDate, java.util.Date endDate, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT COUNT(1) FROM " + getTableName()
                    + " WHERE UPDATEDATE BETWEEN ? AND ? AND REMOVESTATUS = ? ");

            int i = 1;
            pstmt.setTimestamp(i++, new Timestamp(startDate.getTime()));
            pstmt.setTimestamp(i++, new Timestamp(endDate.getTime()));
            pstmt.setString(i++, ActStatus.UNACT.getCode());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On content queryRowSizeByDateStep, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

    private int querySearchTextRowSize(Set<String> keySet, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT COUNT(1) FROM " + getTableName()
                    + " WHERE " + getSearchTextWhereCause(keySet) + " AND REMOVESTATUS = ?");
            int param = setSearchTextSqlParam(keySet, pstmt, 1);
            pstmt.setString(param, ActStatus.UNACT.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On content querySearchTextRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

    private int querySearchTextRowSizeByType(Set<String> keySet, ContentType contentType, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT COUNT(1) FROM " + getTableName()
                    + " WHERE " + getSearchTextWhereCause(keySet) + " AND REMOVESTATUS = ? AND CONTENTTYPE & ?");

            int param = setSearchTextSqlParam(keySet, pstmt, 1);
            pstmt.setString(param, ActStatus.UNACT.getCode());
            param++;
            pstmt.setInt(param, contentType.getValue());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On content querySearchTextRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }


}
