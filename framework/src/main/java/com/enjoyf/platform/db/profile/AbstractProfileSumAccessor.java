package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.profile.ProfileSum;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class AbstractProfileSumAccessor implements ProfileSumAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractProfileSumAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "PROFILE_SUM_";
    private static final int TABLE_NUM = 100;

    @Override
    public ProfileSum insert(ProfileSum sum, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql(sum.getUno()));

            //
            pstmt.setString(1, sum.getUno());
            pstmt.setInt(2, sum.getBlogSum());
            pstmt.setInt(3, sum.getFansSum());
            pstmt.setInt(4, sum.getFavorSum());
            pstmt.setInt(5, sum.getFocusSum());
            pstmt.setInt(6, sum.getForwardSum());
            pstmt.setString(7, sum.getLastContentId());
            pstmt.setString(8, sum.getLastReplyId());
            pstmt.setInt(9, sum.getSocialBlogSum());
            pstmt.setInt(10, sum.getSocialPlaySum());
            pstmt.setInt(11, sum.getSocialFocusSum());
            pstmt.setInt(12, sum.getSocialFansSum());
            pstmt.setInt(13, sum.getSocialAgreeMsgSum());
            pstmt.setInt(14, sum.getSocialReplyMsgSum());
            pstmt.setInt(15, sum.getSocialNoticeMsgSum());
            //
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert ProfileSum, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return sum;
    }

    @Override
    public boolean increase(String uno, Map<ObjectField, Object> keyDeltas, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getIncreaseScript(uno, keyDeltas));

            int index = ObjectFieldUtil.setStmtParams(pstmt, 1, keyDeltas);
            pstmt.setString(index, uno);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            GAlerter.lab("On  profileNun increase a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public boolean increase(String uno, ObjectField field, Integer value, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = "UPDATE " + getTableName(uno) +
                " SET " + field.getColumn() + " = " + field.getColumn() + " + ? WHERE UNO = ? AND " + field.getColumn() + " >= 0";
        if (logger.isDebugEnabled()) {
            logger.debug("ProfileSum increase sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, value != null ? value : 1);
            pstmt.setString(2, uno);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            GAlerter.lab("On  profileNun increase a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public boolean update(String uno, Map<ObjectField, Object> keyValues, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getUpdateScript(uno, keyValues));

            int index = ObjectFieldUtil.setStmtParams(pstmt, 1, keyValues);
            pstmt.setString(index, uno);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            GAlerter.lab("On update, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public ProfileSum get(String uno, Connection conn) throws DbException {
        ProfileSum returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName(uno) + " WHERE UNO = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("ProfileSum get sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, uno);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On select a relation, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    ///private methods.
    private String getInsertSql(String uno) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(uno)
                + " (UNO, BLOGSUM, FANSSUM, FAVORSUM, FOCUSSUM, FORWARDSUM, EXTSTR01, EXTSTR02,SOCIALBLOGSUM," +
                "SOCIALPLAYSUM,SOCIALFOCUSSUM,SOCIALFANSSUM,EXTNUM01,EXTNUM02,EXTNUM03) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ProfileSum INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    protected String getTableName(String uno) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(uno.hashCode(), TABLE_NUM);
    }

    protected ProfileSum rsToObject(ResultSet rs) throws SQLException {
        ProfileSum entry = new ProfileSum();

        entry.setUno(rs.getString("UNO"));
        entry.setBlogSum(rs.getInt("BLOGSUM"));
        entry.setFansSum(rs.getInt("FANSSUM"));
        entry.setFavorSum(rs.getInt("FAVORSUM"));
        entry.setFocusSum(rs.getInt("FOCUSSUM"));
        entry.setForwardSum(rs.getInt("FORWARDSUM"));
        entry.setLastContentId(rs.getString("EXTSTR01"));
        entry.setLastReplyId(rs.getString("EXTSTR02"));
        entry.setSocialBlogSum(rs.getInt("SOCIALBLOGSUM"));
        entry.setSocialPlaySum(rs.getInt("SOCIALPLAYSUM"));
        entry.setSocialFocusSum(rs.getInt("SOCIALFOCUSSUM"));
        entry.setSocialFansSum(rs.getInt("SOCIALFANSSUM"));
        entry.setSocialAgreeMsgSum(rs.getInt("EXTNUM01"));
        entry.setSocialReplyMsgSum(rs.getInt("EXTNUM02"));
        entry.setSocialNoticeMsgSum(rs.getInt("EXTNUM03"));
        return entry;
    }

    protected String getUpdateScript(String uno, Map<ObjectField, Object> keyValues) throws DbException {
        StringBuffer returnValue = new StringBuffer();

        returnValue.append("UPDATE ").append(getTableName(uno));

        if (keyValues != null && keyValues.size() > 0) {
            returnValue.append(" SET ").append(ObjectFieldUtil.generateMapSetClause(keyValues));
        }

        returnValue.append(" WHERE UNO = ?");

        if (logger.isDebugEnabled()) {
            logger.debug("ProfileSum update sql:" + returnValue.toString());
        }

        return returnValue.toString();
    }

    protected String getIncreaseScript(String uno, Map<ObjectField, Object> keyValues) throws DbException {
        StringBuffer returnValue = new StringBuffer();

        returnValue.append("UPDATE ").append(getTableName(uno));

        if (keyValues != null && keyValues.size() > 0) {
            returnValue.append(" SET ").append(ObjectFieldUtil.generateMapIncreaseClause(keyValues));
        }

        returnValue.append(" WHERE UNO = ?");

        return returnValue.toString();
    }
}
