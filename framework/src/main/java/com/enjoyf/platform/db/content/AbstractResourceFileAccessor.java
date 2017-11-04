/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.content;


import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.ResourceFile;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.ResourceFilePathUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

class AbstractResourceFileAccessor implements ResourceFileAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractResourceFileAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "RESOURCE_FILE_";
    private static final String TABLE_SUFFIX_FORMAT = "yyyyMM";

    @Override
    public ResourceFile insert(ResourceFile entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql(entry.getFileId()));

            //FILEID, OWNUNO, RESOURCEFILETYPE, FILEBYTES, PRIVACY, USEDTIMES, CREATEDATE, CREATEIP,
            // AUDITSTATUS, AUDITDATE, TABLEREMOVESTATUS, TABLEREMOVEDATE, FILEREMOVESTATUS, FILEREMOVEDATE

            pstmt.setString(1, entry.getFileId());
            pstmt.setString(2, entry.getOwnUno());

            pstmt.setString(3, entry.getResourceFileType().getCode());
            pstmt.setLong(4, entry.getFileBytes());

            pstmt.setString(5, entry.getPrivacy().getCode());
            pstmt.setInt(6, entry.getUsedTimes());

            pstmt.setTimestamp(7, entry.getCreateDate() != null ? new Timestamp(entry.getCreateDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(8, entry.getCreateIp());

            pstmt.setString(9, entry.getAuditStatus() == null ? null : entry.getAuditStatus().getCode());
            pstmt.setTimestamp(10, entry.getAuditDate() != null ? new Timestamp(entry.getAuditDate().getTime()) : null);

            pstmt.setString(11, entry.getTableRemoveStatus().getCode());
            pstmt.setTimestamp(12, entry.getTableRemoveDate() != null ? new Timestamp(entry.getTableRemoveDate().getTime()) : null);

            pstmt.setString(13, entry.getFileRemoveStatus().getCode());
            pstmt.setTimestamp(14, entry.getFileRemoveDate() != null ? new Timestamp(entry.getFileRemoveDate().getTime()) : null);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert ResourceFile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    @Override
    public boolean increaseUsedTimes(String fileId, int delta, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = "UPDATE " + getTableName(fileId) + " SET USEDTIMES = USEDTIMES + ? WHERE FILEID = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("The increaseUsedTimes sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, delta);
            pstmt.setString(2, fileId);

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On increaseUsedTimes, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public boolean update(String fileId, Map<ObjectField, Object> map, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = "UPDATE " + getTableName(fileId) + " SET " + ObjectFieldUtil.generateMapSetClause(map) + " WHERE FILEID = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("ResourceFile update script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            int index = ObjectFieldUtil.setStmtParams(pstmt, 1, map);
            pstmt.setString(index, fileId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            GAlerter.lab("On update ResourceFile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    ////////////////////////////////////////////////////////
    private String getInsertSql(String fileId) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(fileId) +
                " (FILEID, OWNUNO, RESOURCEFILETYPE, FILEBYTES, PRIVACY, USEDTIMES, CREATEDATE, CREATEIP, AUDITSTATUS, AUDITDATE, TABLEREMOVESTATUS, TABLEREMOVEDATE, FILEREMOVESTATUS, FILEREMOVEDATE)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ResourceFile INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    protected String getTableName(String fileId) throws DbException {
        return KEY_TABLE_NAME_PREFIX + DateUtil.formatDateToString(ResourceFilePathUtil.getDateByFilePath(fileId), TABLE_SUFFIX_FORMAT);
    }
}
