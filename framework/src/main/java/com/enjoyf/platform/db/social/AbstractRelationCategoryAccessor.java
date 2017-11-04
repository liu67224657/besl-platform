/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.social;


import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.social.RelationType;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
abstract class AbstractRelationCategoryAccessor implements RelationCategoryAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractRelationCategoryAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "RELATION_CATEGORY_";
    private static final int TABLE_NUM = 100;

    ///private methods.
    private String getInsertSql(RelationType type, String srcUno) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(type, srcUno)
                + " (SRCUNO, RELATIONTYPE, DESTUNO, CATEGORYID) VALUES (?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("SocialRelation INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    protected String getTableName(RelationType type, String uno) throws DbException {
        return KEY_TABLE_NAME_PREFIX + type.getCode().toUpperCase() + "_" + TableUtil.getTableNumSuffix(uno.hashCode(), TABLE_NUM);
    }


    @Override
    public boolean insert(String srcUno, String destUno, RelationType type, Set<Long> categoryIds, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql(type, srcUno));

            //
            for (Long cateId : categoryIds) {
                pstmt.setString(1, srcUno);
                pstmt.setString(2, type.getCode());
                pstmt.setString(3, destUno);
                pstmt.setLong(4, cateId);

                pstmt.addBatch();
            }

            //
            pstmt.executeBatch();
        } catch (SQLException e) {
            GAlerter.lab("On insert, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return true;
    }

    @Override
    public Set<Long> queryRelationCategories(String srcUno, String destUno, RelationType type, Connection conn) throws DbException {
        Set<Long> returnValue = new HashSet<Long>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT CATEGORYID FROM " + getTableName(type, srcUno) + " WHERE SRCUNO = ? AND DESTUNO = ? AND RELATIONTYPE = ?");

            pstmt.setString(1, srcUno);
            pstmt.setString(2, destUno);
            pstmt.setString(3, type.getCode());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryRelationCategories, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public boolean remove(String srcUno, String destUno, RelationType type, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("DELETE FROM " + getTableName(type, srcUno) + " WHERE SRCUNO = ? AND DESTUNO = ? AND RELATIONTYPE = ?");

            pstmt.setString(1, srcUno);
            pstmt.setString(2, destUno);
            pstmt.setString(3, type.getCode());

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On remove, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public boolean remove(String srcUno, String destUno, RelationType type, Long cateId, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("DELETE FROM " + getTableName(type, srcUno) + " WHERE SRCUNO = ? AND DESTUNO = ? AND CATEGORYID = ? AND RELATIONTYPE = ?");

            pstmt.setString(1, srcUno);
            pstmt.setString(2, destUno);
            pstmt.setLong(3, cateId);
            pstmt.setString(4, type.getCode());

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On remove, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public boolean remove(String srcUno, RelationType type, Long cateId, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("DELETE FROM " + getTableName(type, srcUno) + " WHERE SRCUNO = ? AND CATEGORYID = ? AND RELATIONTYPE = ?");

            pstmt.setString(1, srcUno);
            pstmt.setLong(2, cateId);
            pstmt.setString(3, type.getCode());

            pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            GAlerter.lab("On remove, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }
}
