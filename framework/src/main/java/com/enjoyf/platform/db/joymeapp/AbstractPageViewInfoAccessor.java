package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.*;
import com.enjoyf.platform.service.joymeapp.AppPageViewEntry;
import com.enjoyf.platform.service.joymeapp.AppPageViewInfo;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-22
 * Time: 下午5:26
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractPageViewInfoAccessor extends AbstractBaseTableAccessor<AppPageViewInfo> implements PageViewInfoAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPageViewInfoAccessor.class);

    protected static final String KEY_TABLE_NAME = "app_pageview_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";

    @Override
    public void batchInsert(AppPageViewInfo info, Map<String, List<AppPageViewEntry>> entryMap, Connection conn) throws DbException {

        PreparedStatement pstmt = null;

        try {

            for (Map.Entry<String, List<AppPageViewEntry>> entry : entryMap.entrySet()) {
                pstmt = conn.prepareStatement(getInsertSql(entry.getKey()));

                for (AppPageViewEntry pageViewEntry : entry.getValue()) {

                    //clientid, appkey, platform, channelid, location, locationtype, refer, referype, createdate,rtime,device,
                    // screen,osversion,ip,access_token,token_secr,reportdate
                    pstmt.setString(1, info.getClientId());
                    pstmt.setString(2, info.getAppKey());
                    pstmt.setInt(3, info.getPlatform());
                    pstmt.setString(4, info.getChannelid());

                    pstmt.setString(5, pageViewEntry.getLocation());
                    pstmt.setInt(6, pageViewEntry.getLocationtype());
                    pstmt.setString(7, pageViewEntry.getRefer());
                    pstmt.setInt(8, pageViewEntry.getRefertype());
                    pstmt.setTimestamp(9, new Timestamp(pageViewEntry.getCreateTime().getTime()));
                    pstmt.setInt(10, pageViewEntry.getRtime());
                    pstmt.setString(11, info.getDevice());
                    pstmt.setString(12, info.getScreen());
                    pstmt.setString(13, info.getOsVersion());
                    pstmt.setString(14, info.getIp());
                    pstmt.setString(15, info.getAccess_token());
                    pstmt.setString(16, info.getToken_secr());
                    pstmt.setTimestamp(17, new Timestamp(info.getReportDate() == null ? System.currentTimeMillis() : info.getReportDate().getTime()));
                    pstmt.addBatch();
                }
                pstmt.executeBatch();

            }


        } catch (SQLException e) {
            GAlerter.lab("On insert AppInstallInfo, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

    }

    @Override
    public AppPageViewInfo insert(AppPageViewInfo appPageViewInfo, Connection conn) throws DbException {
        return null;
    }

    private String getInsertSql(String tableName) {
        String insertSql = "INSERT INTO " + tableName + " (clientid, appkey, platform, channelid, location, locationtype, refer, referype, createdate,rtime,device,screen,osversion,ip,access_token,token_secr,reportdate) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("AppInstallInfo INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    private String getTableName(Date d) {
        return KEY_TABLE_NAME + TableUtil.getTableDateSuffix(d, KEY_TABLE_SUFFIX_FMT);
    }

    @Override
    protected AppPageViewInfo rsToObject(ResultSet rs) throws SQLException {
        return null;
    }
}
