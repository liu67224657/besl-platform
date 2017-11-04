/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.oauth;


import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.oauth.OAuthInfo;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.UUID;

/**
 * 
 */
abstract class AbstractOAuthInfoAccessor extends AbstractBaseTableAccessor<OAuthInfo> implements OAuthInfoAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractOAuthInfoAccessor.class);

    private static final String KEY_TABLE_NAME = "oauth_oauth_info";

    private static final Long EXPIR_TIME = 1000L * 60L * 60L * 24L * 7L;

    private static final Long EXPIRE_LONGTIME = 1000L * 60L * 60L * 24L * 7L;

    @Override
    public OAuthInfo insert(OAuthInfo oAuthInfo, Connection conn) throws DbException {
        PreparedStatement pstm = null;
        String sql = "INSERT INTO " + KEY_TABLE_NAME + " (UNO, ACCESS_TOKEN, REFRESH_TOKEN, APP_KEY, EXPIRE_DATE,EXPIRE_LONGTIME, CREATE_DATE) " +
                " VALUES( ?, ?, ?, ?, ?,?, ?)";
        if (logger.isDebugEnabled()) {
            logger.debug("OAuthInfo  insert sql:" + sql);
        }
        String accessToken = UUID.randomUUID().toString().replaceAll("-", "0").replaceAll("_", "0");
        String refreshToken = UUID.randomUUID().toString().replaceAll("-", "0").replaceAll("_", "0");
        try {

            pstm = conn.prepareStatement(sql);
            pstm.setString(1, oAuthInfo.getUno());
            pstm.setString(2, accessToken);
            pstm.setString(3, refreshToken);
            pstm.setString(4, oAuthInfo.getApp_key());
            pstm.setLong(5, EXPIR_TIME );
            pstm.setLong(6,EXPIRE_LONGTIME + System.currentTimeMillis());
            pstm.setTimestamp(7, new Timestamp(System.currentTimeMillis()));

            pstm.executeUpdate();
            oAuthInfo.setUno( oAuthInfo.getUno());
            oAuthInfo.setAccess_token(accessToken);
            oAuthInfo.setRefresh_token(refreshToken);
            oAuthInfo.setApp_key(oAuthInfo.getApp_key());
            oAuthInfo.setExpire_date(EXPIR_TIME);

        } catch (SQLException e) {
            GAlerter.lab("on insert AbstractOAuthInfoAccessor ,a sqlexception." + e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstm);
        }
        return oAuthInfo;
    }

    @Override
    protected OAuthInfo rsToObject(ResultSet rs) throws SQLException {
        OAuthInfo entry = new OAuthInfo();
        entry.setId(rs.getString("ID"));
        entry.setUno(rs.getString("UNO"));
        entry.setAccess_token(rs.getString("ACCESS_TOKEN"));
        entry.setRefresh_token(rs.getString("REFRESH_TOKEN"));
        entry.setApp_key(rs.getString("APP_KEY"));
        entry.setExpire_date(rs.getLong("EXPIRE_DATE"));
        entry.setExpire_longtime(rs.getLong("EXPIRE_LONGTIME"));
        entry.setCreate_date(rs.getTimestamp("CREATE_DATE"));
        return entry;
    }

    @Override
    public OAuthInfo getAccess(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public OAuthInfo getRefresh(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<OAuthInfo> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }
}
