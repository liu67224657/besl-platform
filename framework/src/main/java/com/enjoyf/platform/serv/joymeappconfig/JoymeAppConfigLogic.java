/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.joymeappconfig;


import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.db.joymeapp.JoymeAppHandler;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.system.AppPushMessageDeviceRegisterEvent;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.config.*;
import com.enjoyf.platform.service.joymeappconfig.AppChannel;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigService;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author <a href=mailto:ericliu@staff.joyme.com>Eirc Liu</a>
 */

/**
 * The JoymeAppLogic class holds the core logic for the server.
 * This class is expected to change almost completely from
 * server to server. <p>
 * <p/>
 * JoymeAppLogic is called by JoymeAppPacketDecoder.
 */
public class JoymeAppConfigLogic implements JoymeAppConfigService {
    //
    private static final Logger logger = LoggerFactory.getLogger(JoymeAppConfigLogic.class);

    private static final long DEFAULT_NECESSARY_COMPLETE_ID = -1l;

    //
    private JoymeAppConfigConfig config;

    //////////////
    private QueueThreadN eventQueueThreadN = null;

    private JoymeAppHandler writeAbleHandler;
    private HandlerPool<JoymeAppHandler> readonlyHandlersPool;

    private JoymeAppCache joymeAppCache;

    private AppInfoCache appInfoCache;
    private ShakeRedis shakeRedis;

    JoymeAppConfigLogic(JoymeAppConfigConfig cfg) {
        this.config = cfg;

        eventQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                if (obj instanceof AppPushMessageDeviceRegisterEvent) {

                }
                {
                    GAlerter.lab("In eventQueueThreadN, there is a unknown obj.");
                }
            }


        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessQueue"));

        //initialize the handler.
        try {
            writeAbleHandler = new JoymeAppHandler(config.getWriteableDataSourceName(), config.getProps());

            readonlyHandlersPool = new HandlerPool<JoymeAppHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyHandlersPool.add(new JoymeAppHandler(dsn, config.getProps()));
            }
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());
            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        joymeAppCache = new JoymeAppCache(config.getMemCachedConfig());
        appInfoCache = new AppInfoCache(config.getMemCachedConfig());
        shakeRedis = new ShakeRedis(config.getProps());
    }


    public List<AppContentVersionInfo> queryContentVersionInfoByAppKey(String appKey, long version) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandlersPool queryContentInfo appKey:" + appKey + " version:" + version);
        }

        List<AppContentVersionInfo> returnObj = new ArrayList<AppContentVersionInfo>();

        List<AppContentVersionInfo> appContentVersionInfoList = joymeAppCache.getAppContentVersionList(appKey);

        //put cache
        if (appContentVersionInfoList == null) {
            appContentVersionInfoList = readonlyHandlersPool.getHandler().queryContentVersionInfo(new QueryExpress()
                    .add(QueryCriterions.eq(AppContentVersionField.APPKEY, appKey))
                    .add(QueryCriterions.eq(AppContentVersionField.REMOVESTTAUS, ActStatus.UNACT.getCode())));
            Collections.sort(appContentVersionInfoList);

            joymeAppCache.putAppContentVersionList(appKey, appContentVersionInfoList);
        }

        long lastNecessaryCompleteVersion = DEFAULT_NECESSARY_COMPLETE_ID;
        for (AppContentVersionInfo versionInfo : appContentVersionInfoList) {
            if (versionInfo.getCurrent_version() > version) {
                returnObj.add(versionInfo);
            }

            //如果是完整包，并且是必须要下的，break并且返回
            if (versionInfo.getPackageType() == ContentPackageType.COMPLETE.getCode() && versionInfo.isNecessaryUpdate()) {
                lastNecessaryCompleteVersion = versionInfo.getCurrent_version();
                break;
            }
        }

        if (CollectionUtil.isEmpty(returnObj)) {
            return returnObj;
        }

        if (lastNecessaryCompleteVersion != DEFAULT_NECESSARY_COMPLETE_ID && version >= lastNecessaryCompleteVersion) {
            //计算用户现在的版本号，如果versionNum<最近的必下完整包，如果遇到不是必下完整包删掉，只下载补丁
            for (AppContentVersionInfo versionInfo : returnObj) {
                if (versionInfo.getPackageType() == ContentPackageType.COMPLETE.getCode() && !versionInfo.isNecessaryUpdate()) {
                    returnObj.remove(versionInfo);
                }
            }
        } else {
            //versionNum>最后的值，返回patch+最近的一个完整包
            List<AppContentVersionInfo> tempList = new ArrayList<AppContentVersionInfo>();
            for (AppContentVersionInfo versionInfo : returnObj) {
                tempList.add(versionInfo);
                if (versionInfo.getPackageType() == ContentPackageType.COMPLETE.getCode()) {
                    break;
                }
            }
            returnObj = tempList;
        }

        return returnObj;
    }

    @Override
    public long getLastAppContentVersionByAppKey(String appKey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call queryMaxAppContentVersionBy appKey:" + appKey);
        }

        return readonlyHandlersPool.getHandler().getLastAppContentVersionByAppKey(appKey);
    }

    @Override
    public boolean modifyAppContentVersion(UpdateExpress updateExpress, QueryExpress queryExpress, String appKey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call queryMaxAppContentVersionBy updateExpress:" + updateExpress + " queryExpress:" + queryExpress + " appKey:" + appKey);
        }

        boolean returnBoolen = writeAbleHandler.updateContentVersionInfo(updateExpress, queryExpress);
        if (returnBoolen) {
            joymeAppCache.deleteAppContentVersion(appKey);
        }

        return returnBoolen;
    }

    @Override
    public AppContentVersionInfo createAppContentVersion(AppContentVersionInfo info) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call queryMaxAppContentVersionBy info:" + info);
        }

        return writeAbleHandler.insertContentVersionInfo(info);
    }

    @Override
    public PageRows<AppContentVersionInfo> queryContentVersionByAppKeyPage(String appKey, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " call handler queryContentVersionInfoByAppKey.appKey:" + appKey + " pagination:" + pagination);
        }

        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(AppContentVersionField.APPKEY, appKey))
                .add(QueryCriterions.eq(AppContentVersionField.REMOVESTTAUS, ActStatus.UNACT.getCode()))
                .add(QuerySort.add(AppContentVersionField.CURRENT_VERSION, QuerySortOrder.DESC));

        return readonlyHandlersPool.getHandler().queryContentVersionInfo(queryExpress, pagination);
    }

    @Override
    public AppContentVersionInfo getAppContnetVersion(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " call handler queryContentVersionInfoByAppKey.queryExpress:" + queryExpress);
        }

        return readonlyHandlersPool.getHandler().getAppContentVersion(queryExpress);
    }


    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" receiveEvent. event:" + event);
        }

        eventQueueThreadN.add(event);
        return true;
    }

    @Override
    public JoymeAppMenu createJoymeAppMenu(JoymeAppMenu joymeAppMenu) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" createJoymeAppMenu. joymeAppMenu:" + joymeAppMenu);
        }

        JoymeAppMenu returnObj = writeAbleHandler.insertJoymeAppMenu(joymeAppMenu);

        if (logger.isDebugEnabled()) {
            logger.debug(" delete menu cache. joymeAppMenu:" + joymeAppMenu);
        }
        joymeAppCache.deleteJoymeAppMenu(joymeAppMenu.getAppkey(), joymeAppMenu.getParentId());
        joymeAppCache.deleteJoymeAppMenuListByCahe(joymeAppMenu.getAppkey(), joymeAppMenu.getParentId(), joymeAppMenu.getModuleType().getCode());
        return returnObj;
    }

    /*
   * @修改方法
   * */
    @Override
    public boolean modifyJoymeAppMenu(Long menuId, String appKey, Long pid, JoymeAppMenuModuleType module, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic modifyJoymeAppMenu,menuId:" + menuId + ",appKey:" + appKey + ",pid:" + pid + ",module:" + (module == null ? null : module.getCode()));
        }
        boolean bVal = writeAbleHandler.updateJoymeAppMenu(updateExpress, new QueryExpress().add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, menuId)));
        if (bVal) {
            joymeAppCache.deleteJoymeAppMenu(appKey, pid);
//            joymeAppCache.removeMenusByTagId(menu.getTagId());
//            joymeAppCache.deleteJoymeAppMenuByCategory(appKey, module.getCode());
            joymeAppCache.deleteJoymeAppMenuListByCahe(appKey, pid, (module == null ? null : module.getCode()));
        }

        return bVal;
    }

    /*
   * @查询
   * */
    @Override
    public List<JoymeAppMenu> queryJoymeAppMenu(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" queryJoymeAppMenu. joymeAppMenu:QueryExpress=" + queryExpress);
        }

        return readonlyHandlersPool.getHandler().queryJoymeAppMenu(queryExpress);
    }

    @Override
    public JoymeAppMenu getJoymeAppMenu(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" getJoymeAppMenu. joymeAppMenu:QueryExpress=" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getJoymeAppMenu(queryExpress);
    }

    @Override
    public PageRows<JoymeAppMenu> queryJoymeAppMenuByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" queryJoymeAppMenuByPage. joymeAppMenu:queryExpress=" + queryExpress + "Pagination=" + pagination);
        }

        return readonlyHandlersPool.getHandler().queryJoymeAppMenu(queryExpress, pagination);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public List<JoymeAppTopMenu> queryJoymeAppTopMenuByAppKey(String appKey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryJoymeAppTopMenuByAppKey. appkey:" + appKey);
        }

        List<JoymeAppTopMenu> returnObj = joymeAppCache.getJoymeAppTopMenu(appKey);
        if (returnObj == null) {
            returnObj = readonlyHandlersPool.getHandler().queryJoymeAppTopMenu(new QueryExpress()
                    .add(QueryCriterions.eq(JoymeAppTopMenuField.APPKEY, appKey))
                    .add(QueryCriterions.eq(JoymeAppTopMenuField.REMOVESTATUS, ActStatus.UNACT.getCode()))
                    .add(QuerySort.add(JoymeAppTopMenuField.DISPLAYORDER, QuerySortOrder.ASC)));
            joymeAppCache.putJoymeAppTopMenu(appKey, returnObj);
        }

        return returnObj;
    }

    @Override
    public JoymeAppTopMenu createJoymeAppTopMenu(JoymeAppTopMenu joymeAppTopMenu) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" queryjoymeAppTopMenu. joymeAppTopMenu:joymeAppTopMenu=" + joymeAppTopMenu);
        }

        JoymeAppTopMenu returnObj = writeAbleHandler.insertJoymeAppTopMenu(joymeAppTopMenu);

        joymeAppCache.deleteJoymeAppTopMenu(joymeAppTopMenu.getAppkey());

        return returnObj;
    }

    @Override
    public boolean modifyJoymeAppTopMenu(QueryExpress queryExpress, UpdateExpress updateExpresse, String appkey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" queryjoymeAppTopMenu. joymeAppTopMenu:queryExpress=" + queryExpress + " UpdateExpress=" + updateExpresse);
        }


        boolean bVal = writeAbleHandler.updateJoymeAppTopMenu(queryExpress, updateExpresse);

        if (bVal) {
            joymeAppCache.deleteJoymeAppTopMenu(appkey);
        }

        return bVal;
    }

    @Override
    public PageRows<JoymeAppTopMenu> queryJoymeAppTopMenuByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" queryjoymeAppTopMenu. joymeAppTopMenu:QueryExpress=" + queryExpress + " Pagination=" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryJoymeAppTopMenu(queryExpress, pagination);
    }


    @Override
    public JoymeAppTopMenu getJoymeAppTopMenu(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" queryjoymeAppTopMenu. joymeAppTopMenu:QueryExpress=" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getJoymeAppTopMenu(queryExpress);
    }

    @Override
    public JoymeAppMenuTag createJoymeAppTopMenuTag(JoymeAppMenuTag tag) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" createJoymeAppTopMenuTag. tag=" + tag);
        }

        //请掉缓存
        joymeAppCache.removeMenuTags(tag.getTopMenuId());

        return writeAbleHandler.insertJoymeAppTopMenuTag(tag);
    }

    @Override
    public boolean modifyJoymeAppTopMenuTag(UpdateExpress express, Long tagId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" modifyJoymeAppTopMenuTag. Long=" + tagId);
        }

        JoymeAppMenuTag tag = writeAbleHandler.getJoymeAppTopMenuTag(new QueryExpress().add(QueryCriterions.eq(JoymeAppTopMenuTagField.TAGID, tagId)));
        if (tag == null) {
            return false;
        }

        //修改成功清掉缓存
        boolean returnBool = writeAbleHandler.updateJoymeAppTopMenuTag(express, new QueryExpress().add(QueryCriterions.eq(JoymeAppTopMenuTagField.TAGID, tagId)));
        if (returnBool) {
            joymeAppCache.removeMenuTags(tag.getTopMenuId());
        }
        return returnBool;
    }

    @Override
    public List<JoymeAppMenuTag> queryJoymeAppTopMenuTagByMenuId(long menuId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" queryJoymeAppTopMenuTag. menuId=" + menuId);
        }

        List<JoymeAppMenuTag> returnObj = joymeAppCache.getMenuTags(menuId);
        if (returnObj == null) {
            returnObj = readonlyHandlersPool.getHandler().queryJoymeAppTopMenuTag(new QueryExpress()
                            .add(QueryCriterions.eq(JoymeAppTopMenuTagField.TOPMEUNID, menuId))
                            .add(QueryCriterions.eq(JoymeAppTopMenuTagField.REMOVESTATUS, ActStatus.UNACT.getCode()))
                            .add(QuerySort.add(JoymeAppTopMenuTagField.DISPLAYORDER, QuerySortOrder.ASC))
            );
            joymeAppCache.putMenuTags(menuId, returnObj);
        }
        return returnObj;
    }

    @Override
    public List<JoymeAppMenu> queryJoymeAppMenuByTagId(long tagId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" queryJoymeAppMenuByTagId. tagId=" + tagId);
        }

        List<JoymeAppMenu> returnObj = joymeAppCache.getMenusByTagId(tagId);
        if (returnObj == null) {
            returnObj = readonlyHandlersPool.getHandler().queryJoymeAppMenu(new QueryExpress()
                            .add(QueryCriterions.eq(JoymeAppMenuField.TAG_ID, tagId))
                            .add(QueryCriterions.eq(JoymeAppMenuField.REMOVESTATUS, ActStatus.UNACT.getCode()))
                            .add(QuerySort.add(JoymeAppMenuField.DISPLAY_ORDER, QuerySortOrder.ASC))
                            .add(QueryCriterions.eq(JoymeAppMenuField.MODULE_TYPE, JoymeAppMenuModuleType.DEFAULT.getCode()))
                            .add(QuerySort.add(JoymeAppMenuField.DISPLAY_ORDER, QuerySortOrder.ASC))
            );
            joymeAppCache.putMenusByTagId(tagId, returnObj);
        }
        return returnObj;
    }

    @Override
    public JoymeAppMenuTag getJoymeAppTopMenuTagBytagId(long tagId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" getJoymeAppTopMenuTagBytagId. tagId=" + tagId);
        }

        return readonlyHandlersPool.getHandler().getJoymeAppTopMenuTag(new QueryExpress().add(QueryCriterions.eq(JoymeAppTopMenuTagField.TAGID, tagId)));
    }

    /////////////////////////////////////////////////////////////////////////
//    @Override
//    public boolean reportAppInstalledApp(InstalledAppInfo installedApp) throws ServiceException {
//        if (logger.isDebugEnabled()) {
//            logger.debug(" Call reportQueueThreadN to reportAppErrorInfo:" + installedApp);
//        }
//
//        reportQueueThreadN.add(installedApp);
//        return true;
//    }

//    private void processQueuedAndroidPushMessage(PushMessage msg) {
//        joymeAppCache.putLastPushMessage(msg.getAppKey(), msg.getAppVersion(), msg);
//    }
//
//
//    private void processQueuedIOSPushMessage(PushMessage msg) {
//        if (logger.isDebugEnabled()) {
//            logger.debug(" Call handler to processQueuedPushMessage:" + msg);
//        }
//
//        try {
//            AppDeployment deployment = getAppDeploymentByCache(msg.getAppKey(), msg.getAppPlatform().getCode(), AppDeploymentType.CERTIFICATE.getCode(), null, null);
//            if (deployment == null) {
//                GAlerter.lab(this.getClass().getName() + " message not support ios.certinfo empty:" + msg);
//                return;
//            }
//            GAlerter.lab("------------------------------------------------" + deployment.getPath() + "--------------------------------------------------");
//            GAlerter.lab("------------------------------------------------" + deployment.getPassword() + "--------------------------------------------------");
//            GAlerter.lab("------------------------------------------------" + deployment.getIsProduct() + "--------------------------------------------------");
//
//        } catch (Exception e) {
//            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
//        }
//
//        QueryExpress queryExpress = new QueryExpress()
//                .add(QueryCriterions.ne(PushMessageDeviceField.LASTMSGID, msg.getPushMsgId()))
//                .add(QueryCriterions.eq(PushMessageDeviceField.PLATFORM, AppPlatform.IOS.getCode()))
//                .add(QueryCriterions.eq(PushMessageDeviceField.APPKEY, msg.getAppKey()));
//
//        Pagination pagination = new Pagination(100, 1, 100);
//
//        UpdateExpress updateExpress = new UpdateExpress()
//                .set(PushMessageDeviceField.LASTMSGID, msg.getPushMsgId());
//        do {
//            try {
//                PageRows<PushMessageDevice> pushMessageDevicePageRows = writeAbleHandler.queryPushMessageDevice(queryExpress, pagination);
//
//                if (pushMessageDevicePageRows.getRows() == null) {
//                    continue;
//                }
//
//                List<Long> successList = new ArrayList<Long>();
//                for (PushMessageDevice device : pushMessageDevicePageRows.getRows()) {
//                    boolean isSendMsg = sendPushMsgToIOS(msg, device);
//
//                    if (isSendMsg) {
//                        successList.add(device.getDeviceId());
//                    }
//                }
//
//                if (!CollectionUtil.isEmpty(successList)) {
//                    QueryExpress updateQueryExpress = new QueryExpress()
//                            .add(QueryCriterions.in(PushMessageDeviceField.DEVICEID, successList.toArray()));
//                    writeAbleHandler.updatePushMessageDevice(updateExpress, updateQueryExpress);
//                }
//            } catch (DbException e) {
//                GAlerter.lab(this.getClass().getName() + " occured DbException.e:", e);
//            }
//
//        } while (pagination.hasNextPage());
//    }
//
//    private boolean sendPushMsgToIOS(PushMessage pushMsg, PushMessageDevice device) {
//
//        String deviceToken = device.getClientToken();//iphone手机获取的token
//
//        // default ..if other modify   (message.pushMsgCode)
////        String sound = "default";//铃音
//        String sound = "";
//        int badge = 0;
//
//        List<String> tokens = new ArrayList<String>();
//        tokens.add(deviceToken);
//
//        PushNotificationPayload payLoad = new PushNotificationPayload();
//        try {
//            payLoad.addAlert(pushMsg.getShortMessage()); // 消息内容
//            payLoad.addBadge(badge); // iphone应用图标上小红圈上的数值
//            if (pushMsg.getOptions() != null) {
//                payLoad.addCustomDictionary("option", JsonBinder.buildNormalBinder().toJson(pushMsg.getOptions()));
//            }
//            if (!StringUtil.isEmpty(sound)) {
//                payLoad.addSound(sound);//铃音
//            }
//        } catch (JSONException e) {
//            logger.debug("payLoad is error " + e);
//        }
//
//        AppCertificateInfo info = null;
//        try {
//            AppDeployment deployment = getAppDeploymentByCache(pushMsg.getAppKey(), pushMsg.getAppPlatform().getCode(), AppDeploymentType.CERTIFICATE.getCode(), null, null);
//            if (deployment == null) {
//                GAlerter.lab(this.getClass().getName() + " message not support ios.certinfo empty:" + pushMsg);
//                return false;
//            }
//            info = new AppCertificateInfo(deployment.getAppkey(), deployment.getPath(), deployment.getPassword(), deployment.getIsProduct());
//        } catch (Exception e) {
//            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
//        }
//        if (logger.isDebugEnabled()) {
//            logger.debug("push msg to APNS : AppCertificateInfo is " + info);
//        }
//        GAlerter.lab("------------------------------------------------" + info.getPath() + "--------------------------------------------------");
//        GAlerter.lab("------------------------------------------------" + info.getPassword() + "--------------------------------------------------");
//        GAlerter.lab("------------------------------------------------" + info.isProduction() + "--------------------------------------------------");
//        GAlerter.lab("------------------------------------------------" + tokens + "--------------------------------------------------");
//        GAlerter.lab("------------------------------------------------" + payLoad + "--------------------------------------------------");
//        try {
//            GAlerter.lab("------------------------------------------------" + payLoad.getPayloadAsBytes().length + "--------------------------------------------------");
//        } catch (Exception e) {
//        }
//        boolean b = sendToAPNS(tokens, payLoad, info.getPath(), info.getPassword(), info.isProduction());
//        if (!b) {
//            b = sendToAPNS(tokens, payLoad, info.getPath(), info.getPassword(), info.isProduction());
//        }
//        return b;
//    }
//
//    private boolean sendToAPNS(List<String> tokens, PushNotificationPayload payLoad, String certificatePath, String certificatePassword, boolean isProduction) {
//        int successful = 0;
//        PushNotificationManager pushManager = null;
//        try {
//            pushManager = new PushNotificationManager();
//            //true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
//            AppleNotificationServerBasicImpl server = new AppleNotificationServerBasicImpl(certificatePath, certificatePassword, isProduction);
//            pushManager.initializeConnection(new AppleNotificationServerBasicImpl(certificatePath, certificatePassword, isProduction));
//            GAlerter.lab("------------------------------------------------host:" + server.getNotificationServerHost() + "--------------------------------------------------");
//            GAlerter.lab("------------------------------------------------port:" + server.getNotificationServerPort() + "--------------------------------------------------");
//            List<PushedNotification> notifications = new ArrayList<PushedNotification>();
//
//            // 发送push消息
//            if (tokens.size() == 1) {
//
//                Device device = new BasicDevice();
//                device.setToken(tokens.get(0));
//
//                if (logger.isDebugEnabled()) {
//                    logger.debug("deviceToken is " + device.getToken());
//                }
//
//                PushedNotification notification = pushManager.sendNotification(device, payLoad, true);
//                notifications.add(notification);
//
//            } else {
//                List<Device> device = new ArrayList<Device>();
//                for (String token : tokens) {
//                    if (logger.isDebugEnabled()) {
//                        logger.debug("deviceToken is " + token);
//                    }
//
//                    device.add(new BasicDevice(token));
//                }
//
//                notifications = pushManager.sendNotifications(payLoad, device);
//            }
//
//            List<PushedNotification> failedNotifications = PushedNotification.findFailedNotifications(notifications);
//            List<PushedNotification> successfulNotifications = PushedNotification.findSuccessfulNotifications(notifications);
//
//            int failed = failedNotifications.size();
//            successful = successfulNotifications.size();
//            if (logger.isDebugEnabled()) {
//                logger.error("failedNotifications size: " + failed);
//                logger.debug("successfulNotifications size: " + successful);
//            }
//            GAlerter.lab("------------------------------------------------failed:" + failed + "--------------------------------------------------");
//            GAlerter.lab("------------------------------------------------successful:" + successful + "--------------------------------------------------");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (pushManager != null) {
//                try {
//                    pushManager.stopConnection();
//                } catch (CommunicationException e) {
//                    GAlerter.lab(this.getClass().getName() + " sendToAPNS occured CommunicationException.e: ", e);
//                } catch (KeystoreException e) {
//                    GAlerter.lab(this.getClass().getName() + " sendToAPNS occured KeystoreException.e: ", e);
//                }
//            }
//        }
//        return tokens.size() == successful;
//    }

    ////////////////////////////////////////////////////////
    @Override
    public ActivityTopMenu createActivityTopMenu(ActivityTopMenu activityTopMenu) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler createActivityTopMenu:activityTopMenu" + activityTopMenu);
        }
        ActivityTopMenu menu = writeAbleHandler.insertActivityTopMenu(activityTopMenu);
        if (menu != null) {
            joymeAppCache.deleteActivityTopMenu(menu.getPlatform());
            joymeAppCache.deleteClientTopMenu(menu.getPlatform());

            String activitytopmenulinekey = ActivityTopMenuUtil.getActivityTopMenuLineKey(menu.getAppKey(), menu.getChannelId(), menu.getPlatform());

            joymeAppCache.deleteActivityTopMenuList(activitytopmenulinekey);
        }
        return menu;
    }

    @Override
    public List<ActivityTopMenu> queryActivityTopMenu(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryActivityTopMenu:queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryActivityTopMenu(queryExpress);
    }

    @Override
    public List<ActivityTopMenu> queryActivityTopMenuList(String appkey, Long channelId, Integer platform) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryActivityTopMenu:appkey:" + appkey + ",channelId=" + channelId + ",platform=" + platform);
        }
        String activitytopmenulinekey = ActivityTopMenuUtil.getActivityTopMenuLineKey(appkey, channelId, platform);

        List<ActivityTopMenu> activityTopMenuList = joymeAppCache.getActivityTopMenuList(activitytopmenulinekey);
        if (CollectionUtil.isEmpty(activityTopMenuList)) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.VALID_STATUS, ValidStatus.VALID.getCode()));
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.APP_KEY, appkey));
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.CHANNEL_ID, channelId));
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.PLATFORM, platform));
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.CATEGORY, AppTopMenuCategory.WANBA_ASK.getCode()));
            queryExpress.add(QuerySort.add(ActivityTopMenuField.DISPLAY_ORDER, QuerySortOrder.ASC));

            activityTopMenuList = readonlyHandlersPool.getHandler().queryActivityTopMenu(queryExpress);
            if (!CollectionUtil.isEmpty(activityTopMenuList)) {
                joymeAppCache.putActivityTopMenuList(activitytopmenulinekey, activityTopMenuList);
            }
        }
        return activityTopMenuList;
    }

    @Override
    public PageRows<ActivityTopMenu> queryActivityTopMenuPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryActivityTopMenuPage:queryExpress" + queryExpress + ",pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryActivityTopMenuPage(queryExpress, pagination);
    }

    @Override
    public ActivityTopMenu getActivityTopMenuById(long activityTopMenuId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler getActivityTopMenuById:activityTopMenuId" + activityTopMenuId);
        }
        QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(ActivityTopMenuField.ACTIVITY_MENU_ID, activityTopMenuId));
        return readonlyHandlersPool.getHandler().getActivityTopMenuById(queryExpress);
    }

    @Override
    public boolean modifyActivityTopMenu(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler modifyActivityTopMenu:queryExpress" + queryExpress + ",updateExpress:" + updateExpress);
        }
        Boolean bool = writeAbleHandler.updateActivityTopMenu(queryExpress, updateExpress);
        if (bool) {
            joymeAppCache.deleteActivityTopMenu(AppPlatform.ANDROID.getCode());
            joymeAppCache.deleteActivityTopMenu(AppPlatform.IOS.getCode());
            joymeAppCache.deleteClientTopMenu(AppPlatform.ANDROID.getCode());
            joymeAppCache.deleteClientTopMenu(AppPlatform.IOS.getCode());

            ActivityTopMenu menu = readonlyHandlersPool.getHandler().getActivityTopMenuById(queryExpress);
            if (menu != null) {
                String activitytopmenulinekey = ActivityTopMenuUtil.getActivityTopMenuLineKey(menu.getAppKey(), menu.getChannelId(), menu.getPlatform());
                joymeAppCache.deleteActivityTopMenuList(activitytopmenulinekey);
            }

        }
        return bool;
    }

    @Override
    public List<ActivityTopMenu> queryActivityTopMenuCache(int platform, String appKey, String flag) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryActivityTopMenuCache:platform:" + platform);
        }
        if (StringUtil.isEmpty(flag)) {
            List<ActivityTopMenu> activityTopMenuList = joymeAppCache.getActivityTopMenu(platform);
            if (CollectionUtil.isEmpty(activityTopMenuList)) {
                ClientLineFlag clientLineFlag = readonlyHandlersPool.getHandler().getClientLineFlag(new QueryExpress()
                        .add(QueryCriterions.eq(ClientLineFlagField.LINE_CODE, appKey))
                        .add(QueryCriterions.eq(ClientLineFlagField.LINE_ID, (long) platform))
                        .add(QueryCriterions.eq(ClientLineFlagField.FLAG_TYPE, ClientLineFlagType.TOP_MENU.getCode()))
                        .add(QueryCriterions.eq(ClientLineFlagField.VALID_STATUS, ValidStatus.VALID.getCode())));

                QueryExpress queryExpress = new QueryExpress();
                if (clientLineFlag != null) {
                    queryExpress.add(QueryCriterions.leq(ActivityTopMenuField.ACTIVITY_MENU_ID, clientLineFlag.getMaxItemId()));
                }
                queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.PLATFORM, platform));
                queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.CATEGORY, AppTopMenuCategory.DEFAULT.getCode()));
                queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.VALID_STATUS, ValidStatus.VALID.getCode()));
                queryExpress.add(QuerySort.add(ActivityTopMenuField.DISPLAY_ORDER, QuerySortOrder.ASC));

                activityTopMenuList = readonlyHandlersPool.getHandler().queryActivityTopMenu(queryExpress);
                joymeAppCache.putActivityTopMenu(platform, activityTopMenuList);
            }
            return activityTopMenuList;
        } else {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.PLATFORM, platform));
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.CATEGORY, AppTopMenuCategory.DEFAULT.getCode()));
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.VALID_STATUS, ValidStatus.VALID.getCode()));
            queryExpress.add(QuerySort.add(ActivityTopMenuField.DISPLAY_ORDER, QuerySortOrder.ASC));
            List<ActivityTopMenu> activityTopMenuList = readonlyHandlersPool.getHandler().queryActivityTopMenu(queryExpress);
            return activityTopMenuList;
        }

    }


    /////////////////////////////////////////////////////////////////////////
//    @Override
//    public List<IngoreApp> queryIngoreApp(Integer platform) throws ServiceException {
//        if (logger.isDebugEnabled()) {
//            logger.debug(" call memcached joymeAppCached to queryIngoreApp.");
//        }
//        List<IngoreApp> returnObj = new ArrayList<IngoreApp>();
//
//        List<IngoreApp> ingoreAppList = joymeAppCache.getIngoreApp();
//        if (CollectionUtil.isEmpty(ingoreAppList)) {
//            if (logger.isDebugEnabled()) {
//                logger.debug(" call readHandler to queryIngoreApp.");
//            }
//
//            ingoreAppList = readonlyHandlersPool.getHandler().queryIngoreApp(new QueryExpress());
//        }
//
//        if (platform != null) {
//            int platformValue = platform.intValue();
//            for (IngoreApp ingoreApp : ingoreAppList) {
//                if (ingoreApp.getPlatform() == platformValue) {
//                    returnObj.add(ingoreApp);
//                }
//            }
//        } else {
//            returnObj = ingoreAppList;
//        }
//
//        return returnObj;
//    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public AppChannel getAppChannel(long channelId) throws ServiceException {
        AppChannel appChannel = joymeAppCache.getAppChannel(channelId);

        if (appChannel == null) {
            appChannel = readonlyHandlersPool.getHandler().getAppChannel(channelId);
            if (appChannel != null) {
                joymeAppCache.putAppChannel(channelId, appChannel);
            }
        }

        return appChannel;
    }

    @Override
    public List<AppChannel> queryAppChannel() throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryAppChannel ");
        }
        List<AppChannel> list = appInfoCache.getAppChannelList();
        if (list == null) {
            list = readonlyHandlersPool.getHandler().queryAppChannel();
            if (list != null) {
                appInfoCache.putAppChannelList(list);
            }
        }
        return list;
    }

    ///////////////////////////////////////////////////////////////


    @Override
    public AppDeployment createAppDeployment(AppDeployment appDeployment) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " createAppDeployment.appDeployment:" + appDeployment);
        }
        return writeAbleHandler.createAppDeployment(appDeployment);
    }

    @Override
    public AppDeployment getAppDeployment(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAppDeployment.queryExpress:" + queryExpress);
        }
        //todo cache
        return readonlyHandlersPool.getHandler().getAppDeployment(queryExpress);
    }

    @Override
    public PageRows<AppDeployment> queryAppDeploymentByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAppDeployment.queryExpress:" + queryExpress);
        }
        //todo cache
        return readonlyHandlersPool.getHandler().queryAppDeploymentByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyAppDeployment(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAppDeployment.queryExpress:" + queryExpress + ",updateExpress:" + updateExpress);
        }
        AppDeployment appDeployment = readonlyHandlersPool.getHandler().getAppDeployment(queryExpress);
        if (appDeployment == null) {
            return false;
        }
        boolean bool = writeAbleHandler.modifyAppDeployment(queryExpress, updateExpress);
        if (bool) {
            appInfoCache.deleteAppDeployment(appDeployment.getAppkey(), appDeployment.getAppPlatform().getCode(), appDeployment.getAppDeploymentType().getCode(), (appDeployment.getChannel() == null ? "" : appDeployment.getChannel().getCode()), appDeployment.getAppEnterpriserType().getCode());
        }
        return bool;
    }

    @Override
    public AppDeployment getAppDeploymentByCache(String appKey, int platform, int appDeploymentType, String channel, Integer enterpriser) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAppDeploymentByCache.appKey:" + appKey + ",platform:" + platform + ",appDeploymentType:" + appDeploymentType + ",enterpriser:" + enterpriser);
        }
        AppDeployment returnValue = appInfoCache.getAppDeployment(appKey, platform, appDeploymentType, channel, enterpriser);
        if (returnValue == null) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AppDeploymentField.APP_KEY, appKey));
            queryExpress.add(QueryCriterions.eq(AppDeploymentField.PLATFORM, platform));
            queryExpress.add(QueryCriterions.eq(AppDeploymentField.DEPLOYMENT_TYPE, appDeploymentType));
            if (channel != null) { //default为"",不要写StringUtil.isEmpty().
                queryExpress.add(QueryCriterions.eq(AppDeploymentField.CHANNEL, channel));
            }
            if (enterpriser != null) {
                queryExpress.add(QueryCriterions.eq(AppDeploymentField.ENTERPRISER, enterpriser));
            }
            queryExpress.add(QueryCriterions.eq(AppDeploymentField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            queryExpress.add(QuerySort.add(AppDeploymentField.DEPLOYMENT_ID, QuerySortOrder.DESC));
            returnValue = readonlyHandlersPool.getHandler().getAppDeployment(queryExpress);

            if (returnValue != null) {
                appInfoCache.putAppDeployment(appKey, platform, appDeploymentType, channel, returnValue, enterpriser);
            }
        }
        return returnValue;
    }

    //////////////////JoymeAppTopNews start//////////////////
    @Override
    public List<JoymeAppTopNews> queryJoymeAppTopNewsByAppKey(String appKey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryJoymeAppTopNewsByAppKey.appKey:" + appKey);
        }

        List<JoymeAppTopNews> returnObj = joymeAppCache.getJoymeAppTopNews(appKey);

        if (returnObj == null) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(JoymeAppTopNewsField.APPKEY, appKey));
            queryExpress.add(QueryCriterions.eq(JoymeAppTopNewsField.REMOVESTATUS, ActStatus.UNACT.getCode()));
            returnObj = readonlyHandlersPool.getHandler().queryJoymeAppTopNews(queryExpress);
            if (returnObj != null) {
                joymeAppCache.putJoymeAppTopNews(appKey, returnObj);
            }
        }
        return returnObj;
    }

    @Override
    public JoymeAppTopNews createJoymeAppTopNews(JoymeAppTopNews joymeAppTopNews) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " createJoymeAppTopNews.JoymeAppTopNews:" + joymeAppTopNews);
        }
        joymeAppCache.deleteJoymeAppTopNews(joymeAppTopNews.getAppkey());

        return writeAbleHandler.insertJoymeAppTopNews(joymeAppTopNews);
    }

    @Override
    public boolean modifyJoymeAppTopNews(QueryExpress queryExpress, UpdateExpress updateExpresse, String appKey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyJoymeAppTopNews.queryExpress:" + queryExpress + ",updateExpresse:" + updateExpresse + ",appkey:" + appKey);
        }
        joymeAppCache.deleteJoymeAppTopNews(appKey);

        return writeAbleHandler.updateJoymeAppTopNews(queryExpress, updateExpresse);
    }

    @Override
    public PageRows<JoymeAppTopNews> queryJoymeAppTopNewsByPage(QueryExpress queryExpress, Pagination pagination, String appkey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryJoymeAppTopNewsByPage.queryExpress:" + queryExpress + ",pagination:" + pagination);
        }
        PageRows<JoymeAppTopNews> pageRows = readonlyHandlersPool.getHandler().queryJoymeAppTopNews(queryExpress, pagination);
        return pageRows;
    }

    @Override
    public JoymeAppTopNews getJoymeAppTopNews(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getJoymeAppTopNews.queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getJoymeAppTopNews(queryExpress);
    }

    @Override
    public AppTips createAppTips(AppTips appTips) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " createAppTips.appTips:" + appTips);
        }
        joymeAppCache.removeAppTips(appTips.getAppId(), appTips.getPlatform());
        return writeAbleHandler.createAppTips(appTips);
    }

    @Override
    public AppTips getAppTips(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAppTips.queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getAppTips(queryExpress);
    }

    @Override
    public PageRows<AppTips> queryAppTipsByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAppTips.queryAppTipsByPage:" + queryExpress + ",pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryAppTipsByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyAppTips(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyAppTips.queryExpress:" + queryExpress + ",updateExpress:" + updateExpress);
        }
        AppTips appTips = readonlyHandlersPool.getHandler().getAppTips(queryExpress);
        if (appTips != null) {
            joymeAppCache.removeAppTips(appTips.getAppId(), appTips.getPlatform());
        }
        boolean bool = writeAbleHandler.modifyAppTips(queryExpress, updateExpress);
        if (bool) {
            appTips = writeAbleHandler.getAppTips(queryExpress);
            joymeAppCache.removeAppTips(appTips.getAppId(), appTips.getPlatform());
        }
        return bool;
    }

    @Override
    public AppTips getAppTipsByCache(String appKey, int platform) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAppTipsByCache.appKey:" + appKey + ",platform:" + platform);
        }
        AppTips lastAppTips = joymeAppCache.getAppTips(appKey, platform);
        if (lastAppTips == null) {
            lastAppTips = readonlyHandlersPool.getHandler().getAppTips(new QueryExpress()
                    .add(QueryCriterions.eq(AppTipsField.APP_KEY, appKey))
                    .add(QueryCriterions.eq(AppTipsField.PLATFORM, platform))
                    .add(QueryCriterions.eq(AppTipsField.REMOVE_STATUS, ActStatus.UNACT.getCode()))
                    .add(QuerySort.add(AppTipsField.TIPS_ID, QuerySortOrder.DESC)));
            if (lastAppTips != null) {
                joymeAppCache.putAppTips(appKey, platform, lastAppTips);
            }
        }
        return lastAppTips;
    }


    @Override
    public AppInfo getAppInfoByCache(String appKey, String version, int platform, String channelType) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAppInfo.appKey:" + appKey + ",version:" + version + ",platform:" + platform + ",channelType:" + channelType);
        }
        AppInfo appInfo = appInfoCache.getAppInfo(appKey, version, platform, channelType);
        if (appInfo == null) {
            appInfo = writeAbleHandler.getAppInfo(new QueryExpress()
                    //query 条件的顺序 按照 DB的 组合索引 顺序
                    .add(QueryCriterions.eq(AppInfoField.APP_KEY, appKey))
                    .add(QueryCriterions.eq(AppInfoField.VERSION, version))
                    .add(QueryCriterions.eq(AppInfoField.PLATFORM, platform))
                    .add(QueryCriterions.eq(AppInfoField.CHANNEL_TYPE, channelType))
                    .add(QueryCriterions.eq(AppInfoField.REMOVE_STATUS, ActStatus.UNACT.getCode())));

            if (appInfo != null) {
                appInfoCache.putAppInfo(appKey, version, platform, channelType, appInfo);
            }
        }
        return appInfo;
    }

    @Override
    public PageRows<AppInfo> queryAppInfoByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAppInfoByPage.queryExpress:" + queryExpress + ",pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryAppInfoByPage(queryExpress, pagination);
    }

    @Override
    public AppInfo createAppInfo(AppInfo appInfo) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " createAppInfo.appInfo:" + appInfo);
        }
        return writeAbleHandler.createAppInfo(appInfo);
    }

    @Override
    public boolean modifyAppInfo(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyAppInfo.queryExpress:" + queryExpress + ",updateExpress:" + updateExpress);
        }
        AppInfo appInfo = readonlyHandlersPool.getHandler().getAppInfo(queryExpress);
        if (appInfo != null) {
            appInfoCache.deleteAppInfo(appInfo.getAppKey(), appInfo.getVersion(), appInfo.getAppPlatform().getCode(), appInfo.getChannelType().getCode());
        }
        return writeAbleHandler.modifyAppInfo(queryExpress, updateExpress);
    }

    @Override
    public AppInfo getAppInfo(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyAppInfo.queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getAppInfo(queryExpress);
    }

    @Override
    public List<JoymeAppMenu> queryJoymeAppMenuByCache(String appKey, Long pid, JoymeAppMenuModuleType moduleType) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" queryJoymeAppMenuByCache.appkey:" + appKey + ",pid:" + pid + ",moduleType:" + (moduleType == null ? null : moduleType.getCode()));
        }
        appKey = AppUtil.getAppKey(appKey);
        List<JoymeAppMenu> menuList = joymeAppCache.getJoymeAppMenuListByCache(appKey, pid, (moduleType == null ? null : moduleType.getCode()));
        if (CollectionUtil.isEmpty(menuList)) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.APPKEY, appKey));
            if (pid != null) {
                queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.PID, pid));
            }
            if (moduleType != null) {
                queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.MODULE_TYPE, moduleType.getCode()));
            }
            queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.REMOVESTATUS, ActStatus.UNACT.getCode()));

            menuList = readonlyHandlersPool.getHandler().queryJoymeAppMenu(queryExpress);
            if (!CollectionUtil.isEmpty(menuList)) {
                joymeAppCache.putJoymeAppMenuListByCache(appKey, pid, (moduleType == null ? null : moduleType.getCode()), menuList);
            }
        }
        return menuList;
    }


    @Override
    public AppConfig createAppConfig(AppConfig appConfig) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic createAppConfig:" + appConfig);
        }
        appConfig = writeAbleHandler.createAppConfig(appConfig);
        if (appConfig != null) {
            joymeAppCache.putAppConfig(appConfig.getConfigId(), appConfig);
        }
        return appConfig;
    }

    @Override
    public AppConfig getAppConfig(String configId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic getAppConfig:" + configId);
        }

        System.out.println("aaaaaaaaaaaaaaaaa");
        AppConfig appConfig = joymeAppCache.getAppConfig(configId);
        if (appConfig == null) {
            appConfig = readonlyHandlersPool.getHandler().getAppConfig(new QueryExpress().add(QueryCriterions.eq(AppConfigField.CONFIGID, configId)));
            if (appConfig != null) {
                joymeAppCache.putAppConfig(configId, appConfig);
            }
        }
        return appConfig;
    }

    @Override
    public PageRows<AppConfig> queryAppConfigByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic queryAppConfigByPage.queryExpress:" + queryExpress + ",pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryAppConfigByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyAppConfig(String configId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic modifyAppConfig.configId:" + configId + ",updateExpress:" + updateExpress);
        }
        boolean bool = writeAbleHandler.modifyAppConfig(new QueryExpress().add(QueryCriterions.eq(AppConfigField.CONFIGID, configId)), updateExpress);
        if (bool) {
            joymeAppCache.removeAppConfig(configId);
            AppConfig appConfig = writeAbleHandler.getAppConfig(new QueryExpress().add(QueryCriterions.eq(AppConfigField.CONFIGID, configId)));
            if (appConfig != null) {
                joymeAppCache.putAppConfig(configId, appConfig);
            }
        }
        return bool;
    }

    @Override
    public List<AppConfig> queryAppConfig(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic queryAppConfig.queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryAppConfig(queryExpress);
    }

    @Override
    public boolean deleteAppConfig(String configId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic deleteAppConfig.configId:" + configId);
        }
        boolean bool = writeAbleHandler.deleteAppConfig(new QueryExpress().add(QueryCriterions.eq(AppConfigField.CONFIGID, configId)));
        if (bool) {
            joymeAppCache.removeAppConfig(configId);
        }
        return bool;
    }


    @Override
    public ShakeItem shake(String configid, ShakeType shakeType, String profileId, Date shakeDate) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("shake configid: " + configid + "shakeType:" + shakeType);
        }

        AppConfig appConfig = getAppConfig(configid);

        //随机取shakeType
        ShakeType type = null;
        if (shakeType == null) {
            Map<Integer, ShakeConfig> configMap = appConfig.getInfo().getShakeconfigs();
            int i = RandomUtil.getRandomInt(100);
            for (Map.Entry<Integer, ShakeConfig> config : configMap.entrySet()) {
                if (i <= config.getValue().getShakeRange().getMax() && i >= config.getValue().getShakeRange().getMin()) {
                    type = ShakeType.getByCode(config.getKey());
                    break;
                }
            }
        } else {
            type = shakeType;
        }

        return shakeItem(appConfig.getAppKey(), appConfig.getPlatform().getCode(), appConfig.getChannel(), appConfig.getInfo().getShakeconfigs().get(type.getCode()), type, profileId, shakeDate);
    }

    //摇一摇逻辑
    private ShakeItem shakeItem(String appkey, int platform, String channel, ShakeConfig shakeConfig, ShakeType shakeType, String profileId, Date shakeDate) throws ServiceException {
        //有时间限制，并且超时
        if (shakeConfig.isTimelimit() && shakeConfig.getBegintime() <= shakeDate.getTime() && shakeConfig.getEndtime() >= shakeDate.getTime()) {
            //todo
            return null;
        }

        if (shakeConfig.getShaketimes() > 0) {
            String times = shakeRedis.getProfile(appkey, platform, channel, shakeType, shakeConfig, profileId);

            if (!StringUtil.isEmpty(times)) {
                int t = Integer.parseInt(times);
                if (t >= shakeConfig.getShaketimes()) {
                    //todo 超过限制
                    return null;
                }
            }
        }

        String result = "";
        boolean exists = shakeRedis.keyExists(appkey, platform, channel, shakeType, shakeConfig);
        if (!exists) {
            if (shakeType.equals(ShakeType.GAME)) {
                result = shakeRedis.randomGetItems(appkey, platform, AppChannelType.DEF.getCode(), shakeType, shakeConfig);
            } else if (shakeType.equals(ShakeType.POINT)) {
                result = shakeRedis.randomPop(appkey, platform, AppChannelType.DEF.getCode(), shakeType, shakeConfig);
            }
        } else {
            if (shakeType.equals(ShakeType.GAME)) {
                result = shakeRedis.randomGetItems(appkey, platform, channel, shakeType, shakeConfig);
            } else if (shakeType.equals(ShakeType.POINT)) {
                result = shakeRedis.randomPop(appkey, platform, channel, shakeType, shakeConfig);
            }
        }

        if (shakeConfig.getShaketimes() > 0) {
            int timeout = -1;
            if (shakeConfig.getEndtime() > 0) {
                timeout = (int) ((shakeConfig.getEndtime() - shakeDate.getTime()) / 1000);
            }
            shakeRedis.incrProfile(appkey, platform, channel, shakeType, shakeConfig, profileId, timeout);
        }

        return ShakeItem.parse(result);
    }

    @Override
    public boolean initShakeItems(String appConfigId, ShakeType shakeType, List<ShakeItem> shakeItems) throws ServiceException {

        AppConfig appConfig = getAppConfig(appConfigId);

        ShakeConfig shakeConfig = appConfig.getInfo().getShakeconfigs().get(shakeType.getCode());
        //判断是否支持point
        int start = 0;
        for (ShakeItem shakeItem : shakeItems) {
            addShakeItem(appConfig, shakeType, shakeItem);
        }
        return true;
    }

    @Override
    public boolean addShakeItem(AppConfig appConfig, ShakeType shakeType, ShakeItem shakeItem) throws ServiceException {
        ShakeConfig shakeConfig = appConfig.getInfo().getShakeconfigs().get(shakeType.getCode());
        int start = 0;
        String[] list = new String[200];
        for (int i = 0; i < shakeItem.getWeight(); i++) {
            ShakeItem tenmpObj = new ShakeItem();
            tenmpObj.setShakeType(shakeType.getCode());
            tenmpObj.setDirectId(shakeItem.getDirectId());
            list[start] = tenmpObj.toJsonStr();
            start++;
            if (start == 200) {
                shakeRedis.pushItems(appConfig.getAppKey(), appConfig.getPlatform().getCode(), appConfig.getChannel(), shakeType, shakeConfig, list);
                list = new String[200];
                start = 0;
            } else if (start < 200 && i == shakeItem.getWeight() - 1) {
                list = Arrays.copyOf(list, start);
                shakeRedis.pushItems(appConfig.getAppKey(), appConfig.getPlatform().getCode(), appConfig.getChannel(), shakeType, shakeConfig, list);
            }

        }
        return true;
    }

    @Override
    public boolean removeShakeItem(String appConfigId, ShakeItem shakeItem) throws ServiceException {
        AppConfig appConfig = getAppConfig(appConfigId);

        ShakeConfig shakeConfig = appConfig.getInfo().getShakeconfigs().get(shakeItem.getShakeType());

        ShakeItem tenmpObj = new ShakeItem();
        tenmpObj.setShakeType(shakeItem.getShakeType());
        tenmpObj.setDirectId(shakeItem.getDirectId());

        shakeRedis.removeItems(appConfig.getAppKey(), appConfig.getPlatform().getCode(), appConfig.getChannel(), ShakeType.getByCode(shakeItem.getShakeType()), shakeConfig, shakeItem.getWeight(), tenmpObj.toJsonStr());
        return true;
    }

    @Override
    public PageRows<ShakeItem> queryShakeItems(String appConfigId, ShakeType shakeType, Pagination pagination) throws ServiceException {

        AppConfig appConfig = getAppConfig(appConfigId);
        ShakeConfig shakeConfig = appConfig.getInfo().getShakeconfigs().get(shakeType.getCode());

        List<String> shakeItems = shakeRedis.queryShakeItems(appConfig.getAppKey(), appConfig.getPlatform().getCode(), appConfig.getChannel(), shakeType, shakeConfig, pagination);

        List<ShakeItem> itemList = new ArrayList<ShakeItem>();
        for (String result : shakeItems) {
            itemList.add(ShakeItem.parse(result));
        }

        PageRows<ShakeItem> shakeItemPageRows = new PageRows<ShakeItem>();
        shakeItemPageRows.setRows(itemList);
        shakeItemPageRows.setPage(pagination);

        return shakeItemPageRows;
    }

}

