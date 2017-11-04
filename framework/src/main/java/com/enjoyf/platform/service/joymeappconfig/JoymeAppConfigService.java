package com.enjoyf.platform.service.joymeappconfig;

import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.ShakeItem;
import com.enjoyf.platform.service.joymeapp.config.ShakeType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.rpc.RPC;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-5 下午5:06
 * Description:
 */
public interface JoymeAppConfigService extends EventReceiver {

    /**
     * 通过appkey得到版本号大于version的增量版本信息，如果version<=0，返回该APP所有的增量版本信息
     *
     * @param appKey
     * @param version
     * @return
     * @throws ServiceException
     */
    @RPC
    public List<AppContentVersionInfo> queryContentVersionInfoByAppKey(String appKey, long version) throws ServiceException;

    @RPC
    public long getLastAppContentVersionByAppKey(String appKey) throws ServiceException;

    @RPC
    public AppContentVersionInfo createAppContentVersion(AppContentVersionInfo info) throws ServiceException;

    @RPC
    public boolean modifyAppContentVersion(UpdateExpress updateExpress, QueryExpress queryExpress, String appKey) throws ServiceException;

    @RPC
    public PageRows<AppContentVersionInfo> queryContentVersionByAppKeyPage(String appKey, Pagination pagination) throws ServiceException;

    @RPC
    public AppContentVersionInfo getAppContnetVersion(QueryExpress queryExpress) throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////////
    @RPC
    public JoymeAppMenu getJoymeAppMenu(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public JoymeAppMenu createJoymeAppMenu(JoymeAppMenu joymeAppMenu) throws ServiceException;

    @RPC
    public boolean modifyJoymeAppMenu(Long menuId, String appKey, Long pid, JoymeAppMenuModuleType module, UpdateExpress updateExpress) throws ServiceException;

    @RPC
    public List<JoymeAppMenu> queryJoymeAppMenu(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public PageRows<JoymeAppMenu> queryJoymeAppMenuByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public List<JoymeAppMenu> queryJoymeAppMenuByCache(String appKey, Long pid, JoymeAppMenuModuleType moduleType) throws ServiceException;

    @RPC
    public List<JoymeAppTopMenu> queryJoymeAppTopMenuByAppKey(String appKey) throws ServiceException;

    @RPC
    public JoymeAppTopMenu createJoymeAppTopMenu(JoymeAppTopMenu joymeAppTopMenu) throws ServiceException;

    @RPC
    public boolean modifyJoymeAppTopMenu(QueryExpress queryExpress, UpdateExpress updateExpresse, String appKey) throws ServiceException;

    @RPC
    public PageRows<JoymeAppTopMenu> queryJoymeAppTopMenuByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public JoymeAppTopMenu getJoymeAppTopMenu(QueryExpress queryExpress) throws ServiceException;

    //-------------------
    @RPC
    public JoymeAppMenuTag createJoymeAppTopMenuTag(JoymeAppMenuTag tag) throws ServiceException;

    @RPC
    public boolean modifyJoymeAppTopMenuTag(UpdateExpress express, Long tagId) throws ServiceException;

    @RPC
    public List<JoymeAppMenuTag> queryJoymeAppTopMenuTagByMenuId(long menuId) throws ServiceException;

    @RPC
    public JoymeAppMenuTag getJoymeAppTopMenuTagBytagId(long tagId) throws ServiceException;


    /**
     * 查询tag下的文章
     *
     * @param tagId
     * @return
     * @throws ServiceException
     */
    @RPC
    public List<JoymeAppMenu> queryJoymeAppMenuByTagId(long tagId) throws ServiceException;

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //activity_top_menu   活动轮播图
    @RPC
    public ActivityTopMenu createActivityTopMenu(ActivityTopMenu activityTopMenu) throws ServiceException;

    @RPC
    public List<ActivityTopMenu> queryActivityTopMenu(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public List<ActivityTopMenu> queryActivityTopMenuList(String appkey, Long channelId, Integer platform) throws ServiceException;

    @RPC
    public PageRows<ActivityTopMenu> queryActivityTopMenuPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public ActivityTopMenu getActivityTopMenuById(long activityTopMenuId) throws ServiceException;

    @RPC
    public boolean modifyActivityTopMenu(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException;

    @RPC
    public List<ActivityTopMenu> queryActivityTopMenuCache(int platform, String appKey, String flag) throws ServiceException;

    //app_channel
    @RPC
    public AppChannel getAppChannel(long channelId) throws ServiceException;

    @RPC
    public List<AppChannel> queryAppChannel() throws ServiceException;


    @RPC
    public AppDeployment createAppDeployment(AppDeployment appDeployment) throws ServiceException;

    @RPC
    public AppDeployment getAppDeployment(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public PageRows<AppDeployment> queryAppDeploymentByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public boolean modifyAppDeployment(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException;

    @RPC
    public AppDeployment getAppDeploymentByCache(String appKey, int platform, int appDeploymentType, String channel, Integer enterpriser) throws ServiceException;

    @RPC
    public List<JoymeAppTopNews> queryJoymeAppTopNewsByAppKey(String appKey) throws ServiceException;

    @RPC
    public JoymeAppTopNews createJoymeAppTopNews(JoymeAppTopNews joymeAppTopNews) throws ServiceException;

    @RPC
    public boolean modifyJoymeAppTopNews(QueryExpress queryExpress, UpdateExpress updateExpresse, String appKey) throws ServiceException;

    @RPC
    public PageRows<JoymeAppTopNews> queryJoymeAppTopNewsByPage(QueryExpress queryExpress, Pagination pagination, String appkey) throws ServiceException;

    @RPC
    public JoymeAppTopNews getJoymeAppTopNews(QueryExpress queryExpress) throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RPC
    public AppTips createAppTips(AppTips appTips) throws ServiceException;

    @RPC
    public AppTips getAppTips(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public PageRows<AppTips> queryAppTipsByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public boolean modifyAppTips(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RPC
    public AppTips getAppTipsByCache(String appKey, int platform) throws ServiceException;


    @RPC
    public AppInfo getAppInfoByCache(String appKey, String version, int platform, String channelCode) throws ServiceException;

    @RPC
    public PageRows<AppInfo> queryAppInfoByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public AppInfo createAppInfo(AppInfo appInfo) throws ServiceException;

    @RPC
    public boolean modifyAppInfo(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException;

    @RPC
    public AppInfo getAppInfo(QueryExpress queryExpress) throws ServiceException;

    /////

    @RPC
    public AppConfig createAppConfig(AppConfig appConfig) throws ServiceException;

    @RPC
    public AppConfig getAppConfig(String configId) throws ServiceException;

    @RPC
    public PageRows<AppConfig> queryAppConfigByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public boolean modifyAppConfig(String configId, UpdateExpress updateExpress) throws ServiceException;

    @RPC
    public List<AppConfig> queryAppConfig(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public boolean deleteAppConfig(String configId) throws ServiceException;

    @RPC
    public PageRows<ShakeItem> queryShakeItems(String appConfigId, ShakeType shakeType, Pagination pagination) throws ServiceException;

    @RPC
    public ShakeItem shake(String configid, ShakeType shakeType, String profileId, Date shakeDate) throws ServiceException;

    @RPC
    public boolean initShakeItems(String appConfigId, ShakeType shakeType, List<ShakeItem> shakeItems) throws ServiceException;

    @RPC
    public boolean addShakeItem(AppConfig appConfig, ShakeType shakeType, ShakeItem shakeItems) throws ServiceException;

    @RPC
    public boolean removeShakeItem(String appConfigId, ShakeItem shakeItem) throws ServiceException;


}
