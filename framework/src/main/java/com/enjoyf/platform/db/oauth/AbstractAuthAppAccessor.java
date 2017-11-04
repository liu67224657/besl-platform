/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.oauth;


import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.AppContentVersionInfo;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.AppSecret;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppDetail;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldUtil;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.spi.CurrencyNameProvider;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class AbstractAuthAppAccessor extends AbstractBaseTableAccessor<AuthApp> implements AuthAppAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractAuthAppAccessor.class);

    //
    protected static final String KEY_TABLE_NAME = "oauth_auth_app";


    protected AuthApp rsToObject(ResultSet rs) throws SQLException {
        AuthApp entry = new AuthApp();

        //APPID, APPKEY, APPTYPE, APPDETAIL,VALIDSTATUS,
        //CREATEDATE,CREATEIP,AUDITSTATUS,AUDITDATE,AUDITIP
        entry.setAppId(rs.getString("appid"));
        entry.setAppKey(rs.getString("appkey"));

        entry.setAppType(AuthAppType.getByCode(rs.getString("apptype")));

        entry.setPlatform(AppPlatform.getByCode(rs.getInt("platform")));

        entry.setAppDetail(AuthAppDetail.parse(rs.getString("appdetail")));
        entry.setValidStatus(ValidStatus.getByCode(rs.getString("validstatus")));

        entry.setCreateDate(rs.getTimestamp("createdate"));
        entry.setCreateIp(rs.getString("createip"));

        entry.setAuditStatus(ActStatus.getByCode(rs.getString("auditstatus")));
        entry.setAuditDate(rs.getTimestamp("auditdate"));
        entry.setAuditIp(rs.getString("auditip"));
        entry.setAppName(rs.getString("appname"));
        entry.setProfileKey(rs.getString("profilekey"));
        entry.setDisplayMy(rs.getInt("displaymy"));
        entry.setModifyDate(rs.getTimestamp("modifydate"));
        entry.setAppSecret(AppSecret.fromJson(rs.getString("appsecret")));
        entry.setDepositCallback(rs.getString("depositcallback"));
        entry.setGameKey(rs.getString("gamekey"));
        //

        return entry;
    }


    @Override
    public AuthApp insert(AuthApp entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO " + KEY_TABLE_NAME +
                " (appid, appkey, apptype, appdetail, validstatus, createdate, createip, auditstatus, auditdate, auditip, appname,platform,profilekey,displaymy,modifydate,appsecret,depositcallback,gamekey) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("AuthApp insert sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            //APPID,APPKEY,APPTYPE,APPDETAIL,VALIDSTATUS,
            //CREATEDATE,CREATEIP,AUDITSTATUS,AUDITDATE,AUDITIP
            pstmt.setString(1, entry.getAppId());
            pstmt.setString(2, entry.getAppKey());
            pstmt.setString(3, entry.getAppType().getCode());
            pstmt.setString(4, entry.getAppDetail() != null ? entry.getAppDetail().toJsonStr() : null);
            pstmt.setString(5, entry.getValidStatus().getCode());

            pstmt.setTimestamp(6, entry.getCreateDate() != null ? new Timestamp(entry.getCreateDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(7, entry.getCreateIp());

            pstmt.setString(8, entry.getAuditStatus().getCode());
            pstmt.setTimestamp(9, entry.getAuditDate() != null ? new Timestamp(entry.getAuditDate().getTime()) : null);
            pstmt.setString(10, entry.getAuditIp());
            pstmt.setString(11, entry.getAppName());
            if (entry.getPlatform() == null) {
                pstmt.setNull(12, Types.INTEGER);
            } else {
                pstmt.setInt(12, entry.getPlatform().getCode());
            }
            pstmt.setString(13, entry.getProfileKey());
            pstmt.setInt(14, entry.getDisplayMy());
            pstmt.setTimestamp(15, entry.getModifyDate() != null ? new Timestamp(entry.getModifyDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(16, entry.getAppSecret().toJson());
            pstmt.setString(17, entry.getDepositCallback());
            pstmt.setString(18, entry.getGameKey());
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
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public AuthApp get(String appId, Connection conn) throws DbException {
        AuthApp returnValue = null;

        String sql = "SELECT * FROM " + KEY_TABLE_NAME + " WHERE appid = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("The get sql:" + sql);
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, appId);

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


    @Override
    public List<AuthApp> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public List<AuthApp> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public AuthApp get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    //////////////////////////////////////////////
    protected String getUpdateSql(Map<ObjectField, Object> keyValues) throws DbException {
        StringBuffer returnValue = new StringBuffer();

        returnValue.append("UPDATE ").append(KEY_TABLE_NAME).append(" SET ");
        returnValue.append(ObjectFieldUtil.generateMapSetClause(keyValues));
        returnValue.append(" WHERE APPID = ?");

        return returnValue.toString();
    }
}
