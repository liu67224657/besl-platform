/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.userprops;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.userprops.UserPropKey;
import com.enjoyf.platform.service.userprops.UserProperty;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class AbstractUserPropsAccessor implements UserPropsAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractUserPropsAccessor.class);

    private static final String PREFIX_TABLE_NAME = "USER_PROPS_";
    private static final String KEY_UNDERLINE = "_";
    private static final int TABLE_NUM = 100;

    public UserProperty select(UserPropKey key, Connection conn) throws DbException {
        UserProperty userProp = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(getSelectSql(key));
            //GAlerterLogger.lm("Query Script:" + getSelectSql(key));

            pstmt.setString(1, key.getUno());
            pstmt.setString(2, key.getKey());
            pstmt.setInt(3, key.getIdx());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                userProp = new UserProperty();

                userProp.setUserPropKey(key);
                userProp.setValue(rs.getString("UPVALUE"));

                if (rs.getTimestamp("INITIALDATE") != null) {
                    userProp.setInitialDate(new Date(rs.getTimestamp("INITIALDATE").getTime()));
                }

                if (rs.getTimestamp("MODIFYDATE") != null) {
                    userProp.setModifyDate(new Date(rs.getTimestamp("MODIFYDATE").getTime()));
                }
            }
        } catch (SQLException e) {
            GAlerter.lab("On select user props, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return userProp;
    }

    public List<UserProperty> query(UserPropKey key, Connection conn) throws DbException {
        List<UserProperty> userProps = new ArrayList<UserProperty>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(getQuerySql(key));

            pstmt.setString(1, key.getUno());
            pstmt.setString(2, key.getKey());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                UserProperty userProp = new UserProperty();

                UserPropKey propKey = new UserPropKey(key.getDomain(), key.getUno(), key.getKey(), rs.getInt("IDX"));

                userProp.setUserPropKey(propKey);
                userProp.setValue(rs.getString("UPVALUE"));

                if (rs.getTimestamp("INITIALDATE") != null) {
                    userProp.setInitialDate(new Date(rs.getTimestamp("INITIALDATE").getTime()));
                }

                if (rs.getTimestamp("MODIFYDATE") != null) {
                    userProp.setModifyDate(new Date(rs.getTimestamp("MODIFYDATE").getTime()));
                }

                //add the prop to list.
                userProps.add(userProp);
            }
        } catch (SQLException e) {
            GAlerter.lab("On query user props, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return userProps;
    }

    public UserProperty insert(UserProperty userProp, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql(userProp.getKey()));

            long curTime = System.currentTimeMillis();

            pstmt.setString(1, userProp.getKey().getUno());
            pstmt.setString(2, userProp.getKey().getKey());
            pstmt.setInt(3, userProp.getKey().getIdx());
            pstmt.setString(4, userProp.getValue());
            pstmt.setTimestamp(5, new Timestamp(curTime));
            pstmt.setTimestamp(6, new Timestamp(curTime));

            if (pstmt.executeUpdate() == 1) {
                userProp.setInitialDate(new Date(curTime));
                userProp.setModifyDate(new Date(curTime));
            } else {
                throw new DbException(DbException.SQL_GENERIC, "Insert user prop failed: " + userProp.toString());
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert user props, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return userProp;
    }

    public boolean delete(UserPropKey key, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getDeleteSql(key));

            pstmt.setString(1, key.getUno());
            pstmt.setString(2, key.getKey());
            pstmt.setInt(3, key.getIdx());

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On remove user props, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    public boolean update(UserProperty userProp, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getUpdateSql(userProp.getKey()));

            long curTime = System.currentTimeMillis();

            pstmt.setString(1, userProp.getValue());
            pstmt.setTimestamp(2, new Timestamp(userProp.getModifyDate() == null ? curTime : userProp.getModifyDate().getTime()));
            pstmt.setTimestamp(3, new Timestamp(userProp.getInitialDate() == null ? curTime : userProp.getInitialDate().getTime()));
            pstmt.setString(4, userProp.getKey().getUno());
            pstmt.setString(5, userProp.getKey().getKey());
            pstmt.setInt(6, userProp.getKey().getIdx());

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On update user props, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    public boolean increase(UserPropKey key, long newValue, long orgValue, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getIncreaseSql(key));

            long curTime = System.currentTimeMillis();

            pstmt.setString(1, Long.toString(newValue));
            pstmt.setTimestamp(2, new Timestamp(curTime));
            pstmt.setString(3, key.getUno());
            pstmt.setString(4, key.getKey());
            pstmt.setInt(5, key.getIdx());
            pstmt.setString(6, Long.toString(orgValue));

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On increase the user props, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    /////////////////////////////////////////////////////
    private String getInsertSql(UserPropKey key) {
        return "INSERT INTO " + getTableName(key) + " (UNO, PROPKEY, IDX, UPVALUE, INITIALDATE, MODIFYDATE) VALUES (?, ?, ?, ?, ?, ?)";
    }

    private String getUpdateSql(UserPropKey key) {
        return "UPDATE " + getTableName(key) + " SET UPVALUE = ?, MODIFYDATE = ?,INITIALDATE=? WHERE UNO = ? AND PROPKEY = ? AND IDX = ?";
    }

    private String getIncreaseSql(UserPropKey key) {
        return "UPDATE " + getTableName(key) + " SET UPVALUE = ?, MODIFYDATE = ? WHERE UNO = ? AND PROPKEY = ? AND IDX = ? AND UPVALUE = ?";
    }

    private String getSelectSql(UserPropKey key) {
        return "SELECT * FROM " + getTableName(key) + " WHERE UNO = ? AND PROPKEY = ? AND IDX = ?";
    }

    private String getQuerySql(UserPropKey key) {
        return "SELECT * FROM " + getTableName(key) + " WHERE UNO = ? AND PROPKEY = ? ORDER BY IDX";
    }

    private String getDeleteSql(UserPropKey key) {
        return "DELETE " + getTableName(key) + " WHERE UNO = ? AND PROPKEY = ? AND IDX = ?";
    }

    private String getTableName(UserPropKey key) {
        return PREFIX_TABLE_NAME + key.getDomain().getCode().toUpperCase() + KEY_UNDERLINE + TableUtil.getTableNumSuffix(key.getUno().hashCode(), TABLE_NUM);
    }
}
