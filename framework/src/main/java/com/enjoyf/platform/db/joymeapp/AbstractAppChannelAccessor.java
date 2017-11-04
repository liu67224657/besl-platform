package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeappconfig.AppChannel;
import com.enjoyf.platform.service.joymeappconfig.AppChannelField;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-7-24
 * Time: 下午6:16
 * To change this template use File | Settings | File Templates.
 */
 public abstract class AbstractAppChannelAccessor extends AbstractSequenceBaseTableAccessor<AppChannel> implements AppChannelAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAppChannelAccessor.class);
    private static final String KEY_TABLE_NAME = "app_channel";

    @Override
    public AppChannel insert(AppChannel appChannel, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, appChannel.getChannelCode());
            pstmt.setString(2, appChannel.getChannelName());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                long pkId = rs.getLong(1);
                appChannel.setChannelId(pkId);
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert AppChannel, a SQLException occured:", e);
            throw new DbException(e);
        }finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return appChannel;
    }

    @Override
    public AppChannel get(long channelId, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM "+KEY_TABLE_NAME+" WHERE app_channel_id=?";
        if(logger.isDebugEnabled()){
            logger.debug("the get AppChannel sql:"+sql);
        }
        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, channelId);

            rs = pstmt.executeQuery();
            if(rs.next()){
                return rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On get AppChannel, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        }
        return null;
    }

    @Override
    public int update(long channelId, UpdateExpress updateExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, new QueryExpress().add(QueryCriterions.eq(AppChannelField.APP_CHANNEL_ID, channelId)), conn);
    }

    @Override
    public List<AppChannel> query(Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, new QueryExpress(), conn);
    }

    @Override
    public List<AppChannel> queryByPage(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    protected AppChannel rsToObject(ResultSet rs) throws SQLException {
        AppChannel appChannel = new AppChannel();
        appChannel.setChannelId(rs.getLong("app_channel_id"));
        appChannel.setChannelCode(rs.getString("app_channel_code"));
        appChannel.setChannelName(rs.getString("app_channel_name"));
        return appChannel;
    }

    private String getInsertSql(){
        String sql = "INSERT INTO "+KEY_TABLE_NAME+"(app_channel_code,app_channel_name) VALUES(?,?)";
        if(logger.isDebugEnabled()){
            logger.debug("AppChannel insert sql:"+sql);
        }
        return sql;
    }
}
