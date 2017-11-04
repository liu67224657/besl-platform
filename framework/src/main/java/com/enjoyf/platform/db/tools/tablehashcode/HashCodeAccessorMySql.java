package com.enjoyf.platform.db.tools.tablehashcode;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
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
class HashCodeAccessorMySql implements HashCodeAccessor {

    @Override
    public boolean updateHashCode(String tableName, String srcColumnName, String srcColumnValue, String destColumnName, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("UPDATE " + tableName + " SET " + destColumnName + " = ? WHERE " + srcColumnName + " = ?");

            pstmt.setInt(1, srcColumnValue.hashCode());
            pstmt.setString(2, srcColumnValue);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            //GAlerter.lab("On updateHashCode, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public List<String> queryUniqueKeys(String tableName, String srcColumnName, Pagination page, Connection conn) throws DbException {
        List<String> returnValue = new ArrayList<String>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            page.setTotalRows(queryRowSize(tableName, srcColumnName, conn));

            pstmt = conn.prepareStatement("SELECT DISTINCT(" + srcColumnName + ") FROM " + tableName + " ORDER BY " + srcColumnName + " LIMIT ?, ?");

            pstmt.setInt(1, page.getStartRowIdx());
            pstmt.setInt(2, page.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rs.getString(srcColumnName));
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryUniqueKeys, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    private int queryRowSize(String tableName, String srcColumnName, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(DISTINCT(" + srcColumnName + ")) FROM " + tableName;

        try {
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }
}
