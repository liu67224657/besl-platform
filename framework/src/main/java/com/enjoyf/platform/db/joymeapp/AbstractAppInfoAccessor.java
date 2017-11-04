package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.AppChannelType;
import com.enjoyf.platform.service.joymeapp.AppInfo;
import com.enjoyf.platform.service.joymeapp.AppInfoType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-7-2
 * Time: 下午6:20
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAppInfoAccessor extends AbstractBaseTableAccessor<AppInfo> implements AppInfoAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAppInfoAccessor.class);


    protected static final String KEY_TABLE_NAME = "app_info";

    @Override
    public AppInfo insert(AppInfo appInfo, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, appInfo.getAppName());
            pstmt.setString(2, appInfo.getAppKey());
            pstmt.setInt(3, appInfo.getAppPlatform().getCode());
            pstmt.setInt(4, appInfo.getAppInfoType().getCode());
            pstmt.setString(5, appInfo.getPackageName());
            pstmt.setInt(6, appInfo.getRecommend());
            pstmt.setTimestamp(7, new Timestamp(appInfo.getCreateDate() == null ? System.currentTimeMillis() : appInfo.getCreateDate().getTime()));
            pstmt.setString(8, appInfo.getCreateUserId());
            pstmt.setString(9, appInfo.getCreateIp());
            pstmt.setBoolean(10, appInfo.getIsSearch());
            pstmt.setBoolean(11, appInfo.getIsCommplete());

            pstmt.setString(12, appInfo.getVersion() == null ? "" : appInfo.getVersion());
            pstmt.setString(13, appInfo.getChannelType() == null ? "" : appInfo.getChannelType().getCode());
            pstmt.setBoolean(14, appInfo.getHasGift());

            pstmt.setString(15, appInfo.getRemoveStatus() == null ? ActStatus.UNACT.getCode() : appInfo.getRemoveStatus().getCode());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                appInfo.setAppInfoId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert appinfo, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeConnection(conn);
        }
        return appInfo;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<AppInfo> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<AppInfo> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }


    @Override
    public AppInfo get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected AppInfo rsToObject(ResultSet rs) throws SQLException {
        AppInfo appInfo = new AppInfo();
        appInfo.setAppInfoId(rs.getLong("info_id"));
        appInfo.setAppName(rs.getString("app_name"));
        appInfo.setAppKey(rs.getString("app_key"));
        appInfo.setAppPlatform(AppPlatform.getByCode(rs.getInt("platform")));
        appInfo.setAppInfoType(AppInfoType.getByCode(rs.getInt("app_type")));
        appInfo.setPackageName(rs.getString("app_packagename"));
        appInfo.setRecommend(rs.getInt("recommend_num"));
        appInfo.setCreateDate(rs.getTimestamp("create_time"));
        appInfo.setCreateUserId(rs.getString("create_userid"));
        appInfo.setCreateIp(rs.getString("create_ip"));
        appInfo.setIsSearch(rs.getBoolean("app_issearch"));
        appInfo.setIsCommplete(rs.getBoolean("app_iscomplete"));

        appInfo.setModifyDate(rs.getTimestamp("modifydate"));
        appInfo.setModifyIp(rs.getString("modifyip"));
        appInfo.setModifyUserId(rs.getString("modifyuserid"));
        appInfo.setRemoveStatus(ActStatus.getByCode(rs.getString("removestatus")));

        appInfo.setVersion(rs.getString("version"));
        appInfo.setChannelType(AppChannelType.getByCode(rs.getString("channel")));
        appInfo.setHasGift(rs.getBoolean("hasgift"));

        return appInfo;
    }

    private String getInsertSql() {
        String sql = "insert into " + KEY_TABLE_NAME + "(app_name,app_key,platform,app_type,app_packagename,recommend_num,create_time,create_userid,create_ip,app_issearch,app_iscomplete,version,channel,hasgift,removestatus) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AppErrorInfo INSERT Script:" + sql);
        }

        return sql;
    }


}
