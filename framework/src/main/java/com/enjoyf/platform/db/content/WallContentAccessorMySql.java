package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.DiscoveryWallContent;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class WallContentAccessorMySql extends AbstractWallContentAccessor {
    @Override
    public List<DiscoveryWallContent> query(Pagination page, Connection conn) throws DbException {
        List<DiscoveryWallContent> returnValue = new ArrayList<DiscoveryWallContent>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            page.setTotalRows(queryRowSize(conn));

            pstmt = conn.prepareStatement("SELECT * FROM " + KEY_TABLE_NAME + " WHERE REMOVESTATUS = ? ORDER BY PUBLISHDATE DESC LIMIT ?, ?");

            pstmt.setString(1, ActStatus.UNACT.getCode());

            pstmt.setInt(2, page.getStartRowIdx());
            pstmt.setInt(3, page.getPageSize());

            rs = pstmt.executeQuery();
            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On WallContentAccessorMySql query, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    private int queryRowSize(Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT COUNT(1) FROM " + KEY_TABLE_NAME + " WHERE REMOVESTATUS = ?");

            pstmt.setString(1, ActStatus.UNACT.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On WallContentAccessorMySql queryRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }
}
