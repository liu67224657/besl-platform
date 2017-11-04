package com.enjoyf.platform.db.oauth;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.oauth.GameChannelConfig;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by ericliu on 15/12/29.
 */
public abstract class AbstractGameChannelConfigAccessor extends AbstractBaseTableAccessor<GameChannelConfig> implements GameChannelConfigAccessor {

    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractGameChannelConfigAccessor.class);

    //
    protected static final String KEY_TABLE_NAME = "oauth_auth_channelconfig";

    @Override
    public GameChannelConfig insert(GameChannelConfig config, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());

            config.calConfigId();

            //config_id,appkey,gamekey,platform,channel,version,
            // isdebug,lastmodify_time,lastmodify_ip,lastmodify_userid
            pstmt.setString(1, config.getConfigId());
            pstmt.setString(2, config.getAppkey());
            pstmt.setString(3, config.getGamekey());
            pstmt.setInt(4, config.getAppPlatform().getCode());
            pstmt.setString(5, config.getChannel());
            pstmt.setString(6, config.getVersion());
            pstmt.setBoolean(7, config.isDebug());
            pstmt.setTimestamp(8, new Timestamp(config.getLastModifyTime().getTime()));
            pstmt.setString(9, config.getLastModifyIp());
            pstmt.setString(10, config.getLastModifyUserId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return config;
    }


    @Override
    public GameChannelConfig get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<GameChannelConfig> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, page, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME +
                " (config_id,appkey,gamekey,platform,channel,version,isdebug,lastmodify_time,lastmodify_ip,lastmodify_userid) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("GameChannelConfig insert sql:" + sql);
        }

        return sql;
    }

    @Override
    protected GameChannelConfig rsToObject(ResultSet rs) throws SQLException {
        GameChannelConfig info = new GameChannelConfig();
        info.setConfigId(rs.getString("config_id"));
        info.setAppkey(rs.getString("appkey"));
        info.setGamekey(rs.getString("gamekey"));
        info.setAppPlatform(AppPlatform.getByCode(rs.getInt("platform")));
        info.setChannel(rs.getString("channel"));
        info.setVersion(rs.getString("version"));
        info.setDebug(rs.getBoolean("isdebug"));
        info.setLastModifyTime(rs.getTimestamp("lastmodify_time"));
        info.setLastModifyIp(rs.getString("lastmodify_ip"));
        info.setLastModifyUserId(rs.getString("lastmodify_userid"));
        return info;
    }
}
