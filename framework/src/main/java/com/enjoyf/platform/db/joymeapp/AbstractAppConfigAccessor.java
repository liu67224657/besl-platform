package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.AppEnterpriserType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.AppConfigInfo;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 15-3-3
 * Time: 下午7:39
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAppConfigAccessor extends AbstractBaseTableAccessor<AppConfig> implements AppConfigAccessor {

    private static final String TABLE_NAME = "app_config";
    private static final Logger logger = LoggerFactory.getLogger(AbstractAppConfigAccessor.class);

    @Override
    protected AppConfig rsToObject(ResultSet rs) throws SQLException {
        AppConfig appConfig = new AppConfig();
        appConfig.setConfigId(rs.getString("configid"));
        appConfig.setAppKey(rs.getString("appkey"));
        appConfig.setPlatform(AppPlatform.getByCode(rs.getInt("platform")));
        appConfig.setVersion(rs.getString("version"));
        appConfig.setChannel(rs.getString("channel"));
        appConfig.setEnterpriseType(AppEnterpriserType.getByCode(rs.getInt("enterprise")));
        appConfig.setInfo(AppConfigInfo.parse(rs.getString("appinfo")));
        appConfig.setCreateDate(rs.getTimestamp("createdate"));
        appConfig.setCreateUserId(rs.getString("createuserid"));
        appConfig.setModifyDate(rs.getTimestamp("modifydate"));
        appConfig.setModifyUserId(rs.getString("modifyuserid"));
        appConfig.setAppSecret(rs.getString("appsecret"));
        appConfig.setBucket(StringUtil.isEmpty(rs.getString("bucket")) ? AppConfig.DEFAULT_BUCKET : rs.getString("bucket"));

        return appConfig;
    }

    @Override
    public AppConfig insert(AppConfig appConfig, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, appConfig.getConfigId());
            pstmt.setString(2, appConfig.getAppKey());
            pstmt.setInt(3, appConfig.getPlatform().getCode());
            pstmt.setString(4, appConfig.getVersion());
            pstmt.setString(5, appConfig.getChannel());
            pstmt.setString(6, appConfig.getInfo().toJsonStr());
            pstmt.setTimestamp(7, new Timestamp(appConfig.getCreateDate() == null ? System.currentTimeMillis() : appConfig.getCreateDate().getTime()));
            pstmt.setString(8, appConfig.getCreateUserId());
            pstmt.setInt(9, appConfig.getEnterpriseType().getCode());
            pstmt.setString(10, appConfig.getAppSecret());
            pstmt.setString(11, StringUtil.isEmpty(appConfig.getBucket()) ? AppConfig.DEFAULT_BUCKET : appConfig.getBucket());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert joymeAppTopMenu, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return appConfig;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + TABLE_NAME + " (configid,appkey,platform,version,channel,appinfo,createdate,createuserid,enterprise,appsecret,bucket) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AbstractAppConfigAccessor insert sql:" + sql);
        }
        return sql;
    }

    @Override
    public AppConfig get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<AppConfig> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<AppConfig> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
        return super.update(TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(TABLE_NAME, queryExpress, conn);
    }


}
