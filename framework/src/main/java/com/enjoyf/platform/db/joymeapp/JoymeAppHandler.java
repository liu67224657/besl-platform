package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTV;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchiveCheat;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.joymeappconfig.AppChannel;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public class JoymeAppHandler {
    private DataBaseType dataBaseType;
    private String dataSourceName;

    private ContentVersionAccessor contentVersionAccessor;
    private PushMessageAccessor pushMessageAccessor;

    private AppMenuAccessor joymeAppMenuAccessor;
    private AppTopMenuAccessor joymeAppTopMenuAccessor;
    private IngoreAppAccessor ingoreAppAccessor;

    private InstalledAppInfoAccessor installedAppInfoAccessor;

    private ActivityTopMenuAccessor activityTopMenuAccessor;

    private AppChannelAccessor appChannelAccessor;

    private ClientLineAccessor clientLineAccessor;

    private ClientLineItemAccessor clientLineItemAccessor;

    private ClientLineFlagAccessor clientLineFlagAccessor;

    private AppDeploymentAccessor appDeploymentAccessor;

    private JoymeAppMenuTagAccessor tagAccessor;

    private AppTopNewsAccessor joymeAppTopNewsAccessor;

    private AppTipsAccessor appTipsAccessor;

    private SocialShareAccessor socialShareAccessor;

    private MobileGameArticleAccessor mobileGameArticleAccessor;

    private AppInfoAccessor appInfoAccessor;

    private AnimeSpecialAccessor animeSpecialAccessor;

    private AnimeTagAccessor animeTagAccessor;

    private AnimeSpecialItemAccessor animeSpecialItemAccessor;

    private AnimeTVAccessor animeTVAccessor;

    private TagDedearchivesAccessor tagDedearchivesAccessor;

    private TagDedearchiveCheatAccessor tagDedearchiveCheatAccessor;

    private AppConfigAccessor appConfigAccessor;

    public JoymeAppHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);

        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        contentVersionAccessor = TableAccessorFactory.get().factoryAccessor(ContentVersionAccessor.class, dataBaseType);

        pushMessageAccessor = TableAccessorFactory.get().factoryAccessor(PushMessageAccessor.class, dataBaseType);


        joymeAppMenuAccessor = TableAccessorFactory.get().factoryAccessor(AppMenuAccessor.class, dataBaseType);

        joymeAppTopMenuAccessor = TableAccessorFactory.get().factoryAccessor(AppTopMenuAccessor.class, dataBaseType);


        ingoreAppAccessor = TableAccessorFactory.get().factoryAccessor(IngoreAppAccessor.class, dataBaseType);

        installedAppInfoAccessor = TableAccessorFactory.get().factoryAccessor(InstalledAppInfoAccessor.class, dataBaseType);

        activityTopMenuAccessor = TableAccessorFactory.get().factoryAccessor(ActivityTopMenuAccessor.class, dataBaseType);

        appChannelAccessor = TableAccessorFactory.get().factoryAccessor(AppChannelAccessor.class, dataBaseType);


        clientLineAccessor = TableAccessorFactory.get().factoryAccessor(ClientLineAccessor.class, dataBaseType);
        clientLineItemAccessor = TableAccessorFactory.get().factoryAccessor(ClientLineItemAccessor.class, dataBaseType);
        clientLineFlagAccessor = TableAccessorFactory.get().factoryAccessor(ClientLineFlagAccessor.class, dataBaseType);


        appDeploymentAccessor = TableAccessorFactory.get().factoryAccessor(AppDeploymentAccessor.class, dataBaseType);

        tagAccessor = TableAccessorFactory.get().factoryAccessor(JoymeAppMenuTagAccessor.class, dataBaseType);

        joymeAppTopNewsAccessor = TableAccessorFactory.get().factoryAccessor(AppTopNewsAccessor.class, dataBaseType);

        appTipsAccessor = TableAccessorFactory.get().factoryAccessor(AppTipsAccessor.class, dataBaseType);

        socialShareAccessor = TableAccessorFactory.get().factoryAccessor(SocialShareAccessor.class, dataBaseType);

        mobileGameArticleAccessor = TableAccessorFactory.get().factoryAccessor(MobileGameArticleAccessor.class, dataBaseType);

        appInfoAccessor = TableAccessorFactory.get().factoryAccessor(AppInfoAccessor.class, dataBaseType);

        animeSpecialAccessor = TableAccessorFactory.get().factoryAccessor(AnimeSpecialAccessor.class, dataBaseType);

        animeTagAccessor = TableAccessorFactory.get().factoryAccessor(AnimeTagAccessor.class, dataBaseType);

        animeSpecialItemAccessor = TableAccessorFactory.get().factoryAccessor(AnimeSpecialItemAccessor.class, dataBaseType);

        animeTVAccessor = TableAccessorFactory.get().factoryAccessor(AnimeTVAccessor.class, dataBaseType);

        tagDedearchivesAccessor = TableAccessorFactory.get().factoryAccessor(TagDedearchivesAccessor.class, dataBaseType);

        tagDedearchiveCheatAccessor = TableAccessorFactory.get().factoryAccessor(TagDedearchiveCheatAccessor.class, dataBaseType);

        appConfigAccessor = TableAccessorFactory.get().factoryAccessor(AppConfigAccessor.class, dataBaseType);
    }

    /////////////////////////////////////content update info
    public AppContentVersionInfo insertContentVersionInfo(AppContentVersionInfo versionInfo) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return contentVersionAccessor.insert(versionInfo, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateContentVersionInfo(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return contentVersionAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<AppContentVersionInfo> queryContentVersionInfo(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return contentVersionAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AppContentVersionInfo> queryContentVersionInfo(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<AppContentVersionInfo> pageRows = null;

        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<AppContentVersionInfo> list = contentVersionAccessor.query(queryExpress, pagination, conn);

            pageRows = new PageRows<AppContentVersionInfo>();

            pageRows.setPage(pagination);
            pageRows.setRows(list);

            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public PushMessage insertPushMessage(PushMessage message) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return pushMessageAccessor.insert(message, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updatePushMessage(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        PageRows<PushMessageDevice> returnObj = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return pushMessageAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public PageRows<PushMessage> queryPushMessageByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;

        PageRows<PushMessage> returnObj = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnObj = new PageRows<PushMessage>();

            returnObj.setRows(pushMessageAccessor.query(queryExpress, pagination, conn));
            returnObj.setPage(pagination);

            return returnObj;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PushMessage getPushMessage(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return pushMessageAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public long getLastAppContentVersionByAppKey(String appKey) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentVersionAccessor.getLastestVersionByAppKey(appKey, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AppContentVersionInfo getAppContentVersion(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentVersionAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public JoymeAppMenu insertJoymeAppMenu(JoymeAppMenu joymeAppMenu) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return joymeAppMenuAccessor.insert(joymeAppMenu, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateJoymeAppMenu(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {

            conn = DbConnFactory.factory(dataSourceName);

            return joymeAppMenuAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<JoymeAppMenu> queryJoymeAppMenu(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {

            conn = DbConnFactory.factory(dataSourceName);

            return joymeAppMenuAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<JoymeAppMenu> queryJoymeAppMenu(QueryExpress queryExpress, Pagination pagination) throws DbException {

        PageRows<JoymeAppMenu> returnObj = new PageRows<JoymeAppMenu>();
        Connection conn = null;

        try {

            conn = DbConnFactory.factory(dataSourceName);

            List<JoymeAppMenu> list = joymeAppMenuAccessor.query(queryExpress, pagination, conn);

            returnObj.setPage(pagination);
            returnObj.setRows(list);

            return returnObj;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public JoymeAppMenu getJoymeAppMenu(QueryExpress queryExpress) throws DbException {
        JoymeAppMenu joymeAppMenu = null;
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            joymeAppMenu = joymeAppMenuAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return joymeAppMenu;

    }

    /////////////////////////////////////
    public JoymeAppTopMenu insertJoymeAppTopMenu(JoymeAppTopMenu joymeAppTopMenu) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return joymeAppTopMenuAccessor.insert(joymeAppTopMenu, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateJoymeAppTopMenu(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return joymeAppTopMenuAccessor.update(queryExpress, updateExpress, conn) > 0;

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<JoymeAppTopMenu> queryJoymeAppTopMenu(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return joymeAppTopMenuAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public PageRows<JoymeAppTopMenu> queryJoymeAppTopMenu(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<JoymeAppTopMenu> returnObj = new PageRows<JoymeAppTopMenu>();
        Connection conn = null;

        try {

            conn = DbConnFactory.factory(dataSourceName);
            List<JoymeAppTopMenu> list = joymeAppTopMenuAccessor.query(queryExpress, pagination, conn);

            returnObj.setPage(pagination);
            returnObj.setRows(list);

            return returnObj;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public JoymeAppTopMenu getJoymeAppTopMenu(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return joymeAppTopMenuAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    //////////////////////////////////////
    public List<IngoreApp> queryIngoreApp(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return ingoreAppAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public InstalledAppInfo insertInstalledApp(InstalledAppInfo installedAppInfo) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return installedAppInfoAccessor.insert(installedAppInfo, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateInstalledApp(String installAppInfo, String clientId, String clientToken, String appkey, int platform, Date createTime) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(InstalledAppInfoField.INSTALLED_INFO, installAppInfo);
            updateExpress.set(InstalledAppInfoField.PLATFORM, platform);
            updateExpress.set(InstalledAppInfoField.CREATETIME, createTime == null ? new Date() : createTime);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(InstalledAppInfoField.CLIENTID, clientId));
            queryExpress.add(QueryCriterions.eq(InstalledAppInfoField.CLIENTTOKEN, clientToken));
            queryExpress.add(QueryCriterions.eq(InstalledAppInfoField.APPKEY, appkey));

            return installedAppInfoAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ActivityTopMenu insertActivityTopMenu(ActivityTopMenu activityTopMenu) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityTopMenuAccessor.insert(activityTopMenu, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public List<ActivityTopMenu> queryActivityTopMenu(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityTopMenuAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ActivityTopMenu> queryActivityTopMenuPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<ActivityTopMenu> pageRows = new PageRows<ActivityTopMenu>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<ActivityTopMenu> list = activityTopMenuAccessor.query(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ActivityTopMenu getActivityTopMenuById(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityTopMenuAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateActivityTopMenu(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityTopMenuAccessor.update(queryExpress, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public AppChannel insertAppChannel(AppChannel appChannel) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appChannelAccessor.insert(appChannel, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AppChannel getAppChannel(long channelId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appChannelAccessor.get(channelId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateAppChannel(long channelId, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appChannelAccessor.update(channelId, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<AppChannel> queryAppChannel() throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appChannelAccessor.query(conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AppChannel> queryAppChannelByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<AppChannel> pageRows = new PageRows<AppChannel>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<AppChannel> channelList = appChannelAccessor.queryByPage(queryExpress, pagination, conn);
            pageRows.setRows(channelList);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ClientLine> queryClientLineByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<ClientLine> pageRows = new PageRows<ClientLine>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<ClientLine> list = clientLineAccessor.query(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ClientLine> queryClientLine(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return clientLineAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyClientLine(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return clientLineAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ClientLine createClientLine(ClientLine clientLine) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return clientLineAccessor.insert(clientLine, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ClientLine getClientLine(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return clientLineAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    ///client Line Item;

    public ClientLineItem getClientLineItem(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return clientLineItemAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ClientLineItem> queryClientLineItem(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return clientLineItemAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ClientLineItem> queryClientLineItemByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<ClientLineItem> pageRows = new PageRows<ClientLineItem>();
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<ClientLineItem> list = clientLineItemAccessor.query(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return pageRows;
    }

    public boolean modifyClientLineItem(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return clientLineItemAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ClientLineItem createClientLineItem(ClientLineItem clientLineItem) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return clientLineItemAccessor.insert(clientLineItem, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ClientLineFlag createClientLineFlag(ClientLineFlag flag) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return clientLineFlagAccessor.insert(flag, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public ClientLineFlag getClientLineFlag(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return clientLineFlagAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public List<ClientLineFlag> queryClientLineFlag(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return clientLineFlagAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public PageRows<ClientLineFlag> queryClientLineFlagByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<ClientLineFlag> pageRows = new PageRows<ClientLineFlag>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<ClientLineFlag> list = clientLineFlagAccessor.query(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public boolean modifyClientLineFlag(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return clientLineFlagAccessor.update(queryExpress, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }


    public AppDeployment createAppDeployment(AppDeployment appDeployment) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appDeploymentAccessor.insert(appDeployment, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AppDeployment getAppDeployment(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appDeploymentAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AppDeployment> queryAppDeploymentByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<AppDeployment> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            pageRows = new PageRows<AppDeployment>();
            List<AppDeployment> list = appDeploymentAccessor.query(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyAppDeployment(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appDeploymentAccessor.update(queryExpress, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public JoymeAppMenuTag insertJoymeAppTopMenuTag(JoymeAppMenuTag tag) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return tagAccessor.insert(tag, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateJoymeAppTopMenuTag(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return tagAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public JoymeAppMenuTag getJoymeAppTopMenuTag(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return tagAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<JoymeAppMenuTag> queryJoymeAppTopMenuTag(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return tagAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////JoymeAppTopNews start///////////
    public JoymeAppTopNews insertJoymeAppTopNews(JoymeAppTopNews joymeAppTopNews) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return joymeAppTopNewsAccessor.insert(joymeAppTopNews, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateJoymeAppTopNews(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return joymeAppTopNewsAccessor.update(queryExpress, updateExpress, conn) > 0;

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<JoymeAppTopNews> queryJoymeAppTopNews(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return joymeAppTopNewsAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public PageRows<JoymeAppTopNews> queryJoymeAppTopNews(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<JoymeAppTopNews> returnObj = new PageRows<JoymeAppTopNews>();
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<JoymeAppTopNews> list = joymeAppTopNewsAccessor.query(queryExpress, pagination, conn);
            returnObj.setPage(pagination);
            returnObj.setRows(list);
            return returnObj;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public JoymeAppTopNews getJoymeAppTopNews(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return joymeAppTopNewsAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AppTips createAppTips(AppTips appTips) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appTipsAccessor.insert(appTips, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AppTips getAppTips(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appTipsAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<AppTips> queryAppTips(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appTipsAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AppTips> queryAppTipsByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<AppTips> pageRows = new PageRows<AppTips>();
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<AppTips> list = appTipsAccessor.query(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyAppTips(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appTipsAccessor.update(queryExpress, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    ///////////SocialShareAccessor start///////////////
    public SocialShare insertSocialShare(SocialShare socialShare) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialShareAccessor.insert(socialShare, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateSocialShare(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialShareAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<SocialShare> querySocialShare(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialShareAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public PageRows<SocialShare> querySocialShare(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<SocialShare> returnObj = new PageRows<SocialShare>();
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<SocialShare> list = socialShareAccessor.query(queryExpress, pagination, conn);
            returnObj.setPage(pagination);
            returnObj.setRows(list);
            return returnObj;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialShare getSocialShare(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialShareAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
    ///////////SocialShareAccessor end///////////////

    /////////MobileGameArticle start/////////

    public MobileGameArticle insertMobileGameArticle(MobileGameArticle mobileGameArticle) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return mobileGameArticleAccessor.insert(mobileGameArticle, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public MobileGameArticle getMobileGameArticle(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return mobileGameArticleAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<MobileGameArticle> queryMobileGameArticles(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return mobileGameArticleAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<MobileGameArticle> queryMobileGameArticleByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<MobileGameArticle> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<MobileGameArticle> list = mobileGameArticleAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<MobileGameArticle>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);

            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateMobileGameArticle(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return mobileGameArticleAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AppInfo getAppInfo(QueryExpress add) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appInfoAccessor.get(add, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AppInfo> queryAppInfoByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<AppInfo> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<AppInfo> list = appInfoAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<AppInfo>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);

            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AppInfo createAppInfo(AppInfo appInfo) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appInfoAccessor.insert(appInfo, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyAppInfo(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appInfoAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AnimeSpecial getAnimeSpecial(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return animeSpecialAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AnimeSpecial createAnimeSpecial(AnimeSpecial animeSpecial) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return animeSpecialAccessor.insert(animeSpecial, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<AnimeSpecial> queryAnimeSpecial(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return animeSpecialAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AnimeSpecial> queryAnimeSpecial(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<AnimeSpecial> list = animeSpecialAccessor.query(queryExpress, pagination, conn);
            PageRows<AnimeSpecial> pageRows = new PageRows<AnimeSpecial>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyAnimeSpecial(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return animeSpecialAccessor.update(queryExpress, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////anime tag start ////////
    public AnimeTag createAnimeTag(AnimeTag animeTag) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return animeTagAccessor.insert(animeTag, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AnimeTag getanimeTag(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return animeTagAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<AnimeTag> queryAnimeTag(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return animeTagAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AnimeTag> queryAnimeTagByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<AnimeTag> pageRows = new PageRows<AnimeTag>();
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<AnimeTag> list = animeTagAccessor.query(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean modifyAnimeTag(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return animeTagAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    //////

    public AnimeSpecialItem getAnimeSpecialitem(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return animeSpecialItemAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AnimeSpecialItem createAnimeSpecialItem(AnimeSpecialItem animeSpecialItem) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return animeSpecialItemAccessor.insert(animeSpecialItem, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<AnimeSpecialItem> queryAnimeSpecialItem(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return animeSpecialItemAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AnimeSpecialItem> queryAnimeSpecialItem(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<AnimeSpecialItem> list = animeSpecialItemAccessor.query(queryExpress, pagination, conn);
            PageRows<AnimeSpecialItem> pageRows = new PageRows<AnimeSpecialItem>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyAnimeSpecialItem(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return animeSpecialItemAccessor.update(queryExpress, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////anime tv start ////////
    public AnimeTV createAnimeTV(AnimeTV animeTV) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return animeTVAccessor.insert(animeTV, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AnimeTV getAnimeTV(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return animeTVAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<AnimeTV> queryAnimeTV(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return animeTVAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AnimeTV> queryAnimeTVByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<AnimeTV> pageRows = new PageRows<AnimeTV>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<AnimeTV> list = animeTVAccessor.query(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyAnimeTV(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return animeTVAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
    //////////////anime tv end/////////////////


    //////////////TagDedearchives  start ////////
    public TagDedearchives createTagDedearchives(TagDedearchives tagDedearchives) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return tagDedearchivesAccessor.insert(tagDedearchives, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public TagDedearchives getTagDedearchives(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return tagDedearchivesAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<TagDedearchives> queryTagDedearchives(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return tagDedearchivesAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<TagDedearchives> queryTagDedearchivesByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<TagDedearchives> pageRows = new PageRows<TagDedearchives>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<TagDedearchives> list = tagDedearchivesAccessor.query(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyTagDedearchives(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return tagDedearchivesAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
    //////////////TagDedearchives  end/////////////////


    //////////////TagDedearchiveCheat start ////////
    public TagDedearchiveCheat createTagDedearchiveCheat(TagDedearchiveCheat cheat) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return tagDedearchiveCheatAccessor.insert(cheat, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public TagDedearchiveCheat getTagDedearchiveCheat(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return tagDedearchiveCheatAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<TagDedearchiveCheat> queryTagDedearchiveCheat(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return tagDedearchiveCheatAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<TagDedearchiveCheat> queryTagDedearchiveCheatByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<TagDedearchiveCheat> pageRows = new PageRows<TagDedearchiveCheat>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<TagDedearchiveCheat> list = tagDedearchiveCheatAccessor.query(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyTagDedearchiveCheat(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return tagDedearchiveCheatAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean removePushMessage(Long msgId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return pushMessageAccessor.remove(msgId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AppConfig createAppConfig(AppConfig appConfig) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appConfigAccessor.insert(appConfig, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AppConfig getAppConfig(QueryExpress queryExpress) throws DbException {

        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appConfigAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<AppConfig> queryAppConfig(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appConfigAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AppConfig> queryAppConfigByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<AppConfig> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<AppConfig> list = appConfigAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<AppConfig>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyAppConfig(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appConfigAccessor.update(queryExpress, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean deleteAppConfig(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appConfigAccessor.delete(queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<Integer> queryPostNum(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return tagDedearchivesAccessor.queryPostNum(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<Long> queryTagDedeArchivesByDistinct(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return tagDedearchivesAccessor.queryByDistinct(queryExpress, page, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
}
