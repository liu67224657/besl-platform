package com.enjoyf.platform.db.oauth;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.oauth.GameChannelInfo;
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
public abstract class AbstractGameChannelInfoAccessor extends AbstractBaseTableAccessor<GameChannelInfo> implements GameChannelInfoAccessor {

    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractGameChannelInfoAccessor.class);

    //
    protected static final String KEY_TABLE_NAME = "oauth_auth_channelinfo";

    @Override
    public GameChannelInfo insert(GameChannelInfo channelInfo, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());

            channelInfo.calInfoId();

            //info_id,appkey,gamekey,platform,channel,channelkey,channelsecr,publickey,privatekey,
            //lastmodify_time,lastmodify_ip,lastmodify_userid
            pstmt.setString(1, channelInfo.getInfoId());
            pstmt.setString(2, channelInfo.getAppkey());
            pstmt.setString(3, channelInfo.getGamekey());
            pstmt.setInt(4, channelInfo.getAppPlatform().getCode());
            pstmt.setString(5, channelInfo.getChannel());
            pstmt.setString(6, channelInfo.getChannelKey());
            pstmt.setString(7, channelInfo.getChannelSecr());
            pstmt.setString(8, channelInfo.getPublickey());
            pstmt.setString(9, channelInfo.getPrivatekey());
            pstmt.setTimestamp(10, new Timestamp(channelInfo.getLastModifyTime().getTime()));
            pstmt.setString(11, channelInfo.getLastModifyIp());
            pstmt.setString(12, channelInfo.getLastModifyUserId());
            pstmt.setString(13,channelInfo.getChannelAppId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return channelInfo;
    }


    @Override
    public GameChannelInfo get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<GameChannelInfo> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
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
                " (info_id,appkey,gamekey,platform,channel,channelkey,channelsecr,publickey,privatekey,lastmodify_time,lastmodify_ip,lastmodify_userid,channelappid) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("AuthAppChannelInfo insert sql:" + sql);
        }

        return sql;
    }

    @Override
    protected GameChannelInfo rsToObject(ResultSet rs) throws SQLException {
        GameChannelInfo info = new GameChannelInfo();
        info.setInfoId(rs.getString("info_id"));
        info.setGamekey(rs.getString("gamekey"));
        info.setAppPlatform(AppPlatform.getByCode(rs.getInt("platform")));
        info.setAppkey(rs.getString("appkey"));
        info.setChannel(rs.getString("channel"));
        info.setChannelKey(rs.getString("channelkey"));
        info.setChannelSecr(rs.getString("channelsecr"));
        info.setPublickey(rs.getString("publickey"));
        info.setPrivatekey(rs.getString("privatekey"));
        info.setLastModifyTime(rs.getTimestamp("lastmodify_time"));
        info.setLastModifyIp(rs.getString("lastmodify_ip"));
        info.setLastModifyUserId(rs.getString("lastmodify_userid"));
        info.setChannelAppId(rs.getString("channelappid"));
        return info;
    }
}
