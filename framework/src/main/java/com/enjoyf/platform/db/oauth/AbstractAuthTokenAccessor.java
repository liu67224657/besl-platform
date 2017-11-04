/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.oauth;


import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.oauth.AuthToken;
import com.enjoyf.platform.service.oauth.AuthTokenType;
import com.enjoyf.platform.util.log.GAlerter;
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
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class AbstractAuthTokenAccessor implements AuthTokenAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractAuthTokenAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "OAUTH_AUTH_TOKEN_";
    private static final int TABLE_NUM = 100;


    protected AuthToken rsToObject(ResultSet rs) throws SQLException {
        AuthToken entry = new AuthToken();

        //TOKEN, TOKENTYPE, CREDENTIALID, APPID,
        //REFRESHTOKEN, EXPIREDATE, CREATEDATE, ACCESSTIMES
        entry.setToken(rs.getString("TOKEN"));
        entry.setTokenType(AuthTokenType.getByCode(rs.getString("TOKENTYPE")));
        entry.setCredentialId(rs.getString("CREDENTIALID"));
        entry.setAppId(rs.getString("APPID"));

        entry.setRefreshToken(rs.getString("REFRESHTOKEN"));
        entry.setExpireDate(rs.getTimestamp("EXPIREDATE"));
        entry.setCreateDate(rs.getTimestamp("CREATEDATE"));
        entry.setAccessTimes(rs.getInt("ACCESSTIMES"));
        //

        return entry;
    }

    @Override
    public AuthToken insert(AuthToken entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO " + getTableName(entry.getToken()) +
                " (TOKEN, TOKENTYPE, CREDENTIALID, APPID, REFRESHTOKEN, EXPIREDATE, CREATEDATE, ACCESSTIMES) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("AuthToken insert sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            //TOKEN, TOKENTYPE, CREDENTIALID, APPID,
            //REFRESHTOKEN, EXPIREDATE, CREATEDATE, ACCESSTIMES
            pstmt.setString(1, entry.getToken());

            pstmt.setString(2, entry.getTokenType().getCode());
            pstmt.setString(3, entry.getCredentialId());
            pstmt.setString(4, entry.getAppId());
            pstmt.setString(5, entry.getRefreshToken());
            pstmt.setTimestamp(6, entry.getExpireDate() != null ? new Timestamp(entry.getExpireDate().getTime()) : null);
            pstmt.setTimestamp(7, entry.getCreateDate() != null ? new Timestamp(entry.getCreateDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(8, entry.getAccessTimes());

            //
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    @Override
    public boolean remove(String token, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = "DELETE FROM " + getTableName(token) + " WHERE TOKEN = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("The remove sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, token);

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On remove, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public int clearExpired(Connection conn) throws DbException {
        int returnValue = 0;

        Date now = new Date();
        for (String tableName : getAllTableNames()) {
            returnValue += clear(tableName, now, conn);
        }

        return returnValue;
    }

    private int clear(String tableName, Date now, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = "DELETE FROM " + tableName + " WHERE EXPIREDATE < ?";
        if (logger.isDebugEnabled()) {
            logger.debug("The clear sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setTimestamp(1, new Timestamp(now.getTime()));

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On clear, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public AuthToken get(String token, Connection conn) throws DbException {
        AuthToken returnValue = null;

        String sql = "SELECT * FROM " + getTableName(token) + " WHERE TOKEN = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("The get sql:" + sql);
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, token);

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

    //////////////////////////////////////////////
    protected String getTableName(String token) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(token.hashCode(), TABLE_NUM);
    }

    protected List<String> getAllTableNames() {
        List<String> returnValue = new ArrayList<String>();

        for (int i = 0; i < TABLE_NUM; i++) {
            returnValue.add(KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(i, TABLE_NUM));
        }

        return returnValue;
    }
}
