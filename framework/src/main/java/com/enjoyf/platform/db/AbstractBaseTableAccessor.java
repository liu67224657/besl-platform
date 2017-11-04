/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db;

import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectFieldUtil;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-1-30 下午12:04
 * Description:
 */
public abstract class AbstractBaseTableAccessor<T> {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractBaseTableAccessor.class);

    //insert object
    public abstract T insert(T t, Connection conn) throws DbException;

    //fullfill the object use the result set.
    protected abstract T rsToObject(ResultSet rs) throws SQLException;

    protected List<T> query(String tableName, QueryExpress queryExpress, Rangination range, Connection conn) throws DbException {
        //
        if (range == null) {
            return query(tableName, queryExpress, conn);
        }

        //
        List<T> returnValue = new ArrayList<T>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + tableName + " " + ObjectFieldUtil.generateQueryClause(queryExpress, true) + " LIMIT ?, ?";
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            //
            range.setTotal(queryRowSize(tableName, queryExpress, conn));

            //
            pstmt = conn.prepareStatement(sql);

            int index = 1;
            index = ObjectFieldUtil.setStmtParams(pstmt, 1, queryExpress);
            pstmt.setInt(index++, range.getStart());
            pstmt.setInt(index++, range.getSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
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

    //the query by queryexpress and page, the default implements is for mysql.
    protected List<T> query(String tableName, QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        //
        if (page == null) {
            return query(tableName, queryExpress, conn);
        }

        //
        List<T> returnValue = new ArrayList<T>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + tableName + " " + ObjectFieldUtil.generateQueryClause(queryExpress, true) + " LIMIT ?, ?";
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            //
            page.setTotalRows(queryRowSize(tableName, queryExpress, conn));

            //
            pstmt = conn.prepareStatement(sql);

            int index = 1;
            index = ObjectFieldUtil.setStmtParams(pstmt, 1, queryExpress);
            pstmt.setInt(index++, page.getStartRowIdx());
            pstmt.setInt(index++, page.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
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

    //query by queryexpress
    protected final List<T> query(String tableName, QueryExpress queryExpress, Connection conn) throws DbException {
        List<T> returnValue = new ArrayList<T>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + tableName + " " + ObjectFieldUtil.generateQueryClause(queryExpress, true);
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            //
            pstmt = conn.prepareStatement(sql);

            //
            ObjectFieldUtil.setStmtParams(pstmt, 1, queryExpress);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
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

    //query by queryexpress
    protected final T get(String tableName, QueryExpress queryExpress, Connection conn) throws DbException {
        T returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + tableName + " " + ObjectFieldUtil.generateQueryClause(queryExpress, true) + " LIMIT 0,1";
        if (logger.isDebugEnabled()) {
            logger.debug("The get sql:" + sql);
        }

        try {
            //
            pstmt = conn.prepareStatement(sql);

            //
            ObjectFieldUtil.setStmtParams(pstmt, 1, queryExpress);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
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

    //fetch the page size
    protected int queryRowSize(String tableName, QueryExpress queryExpress, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(1) FROM " + tableName + " " + ObjectFieldUtil.generateQueryClause(queryExpress, false);
        if (logger.isDebugEnabled()) {
            logger.debug("The queryRowSize sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            ObjectFieldUtil.setStmtParams(pstmt, 1, queryExpress);

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

    //the query by queryexpress
    protected final int update(String tableName, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        //
        String sql = "UPDATE " + tableName + " " + ObjectFieldUtil.generateUpdateClause(updateExpress) + " " + ObjectFieldUtil.generateQueryClause(queryExpress, false);
        if (logger.isDebugEnabled()) {
            logger.debug("The update sql:" + sql);
        }

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);

            int index = 1;
            index = ObjectFieldUtil.setStmtParams(pstmt, index, updateExpress);
            index = ObjectFieldUtil.setStmtParams(pstmt, index, queryExpress);

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On update, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    //the remove by queryexpress
    protected final int delete(String tableName, QueryExpress queryExpress, Connection conn) throws DbException {
        //
        String sql = "DELETE FROM " + tableName + " " + ObjectFieldUtil.generateQueryClause(queryExpress, false);
        if (logger.isDebugEnabled()) {
            logger.debug("The delete sql:" + sql);
        }

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);

            int index = 1;
            index = ObjectFieldUtil.setStmtParams(pstmt, index, queryExpress);

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On delete, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }
}
