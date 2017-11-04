package com.enjoyf.platform.db.viewline;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.viewline.ViewCategoryPrivacy;
import com.enjoyf.platform.service.viewline.ViewCategoryPrivacyLevel;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public abstract class AbstractViewCategoryPrivacyAccessor extends AbstractSequenceBaseTableAccessor<ViewCategoryPrivacy> implements ViewCategoryPrivacyAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractViewCategoryPrivacyAccessor.class);

    //
    protected static final String KEY_TABLE_NAME = "VIEWLINE_CATEGORY_PRIVACY";

    @Override
    public ViewCategoryPrivacy insert(ViewCategoryPrivacy entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql());

            //CATEGORYID, PRIVACYLEVEL, UNO, CREATEDATE, CREATEUSERID, UPDATEDATE, UPDATEUSERID
            pstmt.setInt(1, entry.getCategoryId());

            pstmt.setString(2, entry.getPrivacyLevel().getCode());
            pstmt.setString(3, entry.getUno());

            pstmt.setTimestamp(4, entry.getCreateDate() != null ? new Timestamp(entry.getCreateDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(5, entry.getCreateUserid());

            pstmt.setTimestamp(6, entry.getUpdateDate() != null ? new Timestamp(entry.getUpdateDate().getTime()) : null);
            pstmt.setString(7, entry.getUpdateUserid());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert ViewCategoryPrivacy, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    @Override
    public ViewCategoryPrivacy get(QueryExpress getExpress, Connection conn) throws DbException {
        //call the parent get method to get example entry.
        return get(KEY_TABLE_NAME, getExpress, conn);
    }

    @Override
    protected ViewCategoryPrivacy rsToObject(ResultSet rs) throws SQLException {
        ViewCategoryPrivacy entry = new ViewCategoryPrivacy();

        //CATEGORYID, PRIVACYLEVEL, UNO, CREATEDATE, CREATEUSERID, UPDATEDATE, UPDATEUSERID

        //
        entry.setCategoryId(rs.getInt("CATEGORYID"));

        //
        entry.setPrivacyLevel(ViewCategoryPrivacyLevel.getByCode(rs.getString("PRIVACYLEVEL")));
        entry.setUno(rs.getString("UNO"));

        //
        entry.setCreateDate(rs.getTimestamp("CREATEDATE") != null ? new Date(rs.getTimestamp("CREATEDATE").getTime()) : null);
        entry.setCreateUserid(rs.getString("CREATEUSERID"));

        entry.setUpdateDate(rs.getTimestamp("UPDATEDATE") != null ? new Date(rs.getTimestamp("UPDATEDATE").getTime()) : null);
        entry.setUpdateUserid(rs.getString("UPDATEUSERID"));

        return entry;
    }

    @Override
    public List<ViewCategoryPrivacy> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<Integer> queryCategoryIds(QueryExpress queryExpress, Connection conn) throws DbException {
        List<Integer> returnValue = new ArrayList<Integer>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT CATEGORYID FROM " + KEY_TABLE_NAME + " " + ObjectFieldUtil.generateQueryClause(queryExpress, true);
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
                returnValue.add(rs.getInt("CATEGORYID"));
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
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (CATEGORYID, PRIVACYLEVEL, UNO, CREATEDATE, CREATEUSERID, UPDATEDATE, UPDATEUSERID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ViewCategoryPrivacy INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException{
        return delete(KEY_TABLE_NAME, queryExpress, conn);
    }
}
