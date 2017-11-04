/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.profile;


import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.db.profile.ProfileHandler;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.*;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.profile.*;
import com.enjoyf.platform.service.profile.socialclient.SocialProfile;
import com.enjoyf.platform.service.profile.socialclient.SocialProfileBlog;
import com.enjoyf.platform.service.profile.socialclient.SocialProfileBlogField;
import com.enjoyf.platform.service.search.SearchServiceSngl;
import com.enjoyf.platform.service.search.solr.SolrCore;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.*; /**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */

/**
 * The UserPropsLogic class holds the core logic for the server.
 * This class is expected to change almost completely from
 * server to server. <p>
 * <p/>
 * UserPropsLogic is called by UserPropsPacketDecoder.
 */
class ProfileLogic implements ProfileService {
    //
    private static final Logger logger = LoggerFactory.getLogger(ProfileLogic.class);

    //
    private ProfileConfig config;

    //the handler's
    private ProfileHandler writeAbleProfileHandler;
    private HandlerPool<ProfileHandler> readonlyProfileHandlersPool;

    //the event queue thread n.
    private QueueThreadN eventProcessQueueThreadN = null;
    private QueueThreadN pushMessageQueueThreadN = null;
    //the event queue thread n.
    private QueueThreadN modiyBlogProcessQueueThreadN = null;

    //the cache setting.
    private ProfileCache profileCache;

    private ProfileMemcachedCache profileMemcachedCache;

    private SocialProfileCache socialProfileCache;

    ProfileLogic(ProfileConfig cfg) {
        config = cfg;

        //initialize the handler.
        try {
            //
            writeAbleProfileHandler = new ProfileHandler(config.getWriteableDataSourceName(), config.getProps());

            //
            readonlyProfileHandlersPool = new HandlerPool<ProfileHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyProfileHandlersPool.add(new ProfileHandler(dsn, config.getProps()));
            }
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());

            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        //the queue thread setting.
        eventProcessQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                if (obj instanceof Event) {
                    processQueuedEvent((Event) obj);
                } else {
                    GAlerter.lab("In eventProcessQueueThreadN, there is a unknown obj.");
                }
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessQueue"));

        modiyBlogProcessQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                if (obj instanceof Event) {
                    processModifyQueuedEvent((UpdateExpressWarp) obj);
                } else {
                    GAlerter.lab("In modiyBlogProcessQueueThreadN, there is a unknown obj.");
                }
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "modiyBlogProcessQueue"));

        pushMessageQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                if (obj instanceof PushMessage) {
                    PushMessage pushMessage = (PushMessage) obj;
                    if (pushMessage.getAppPlatform().equals(AppPlatform.IOS)) {
                        processQueuedIOSPushMessage(pushMessage);
                    } else if (pushMessage.getAppPlatform().equals(AppPlatform.ANDROID)) {
//                        processQueuedAndroidPushMessage(pushMessage);
                    } else {
                        GAlerter.lab("pushMessage not support platform.pushMessage: " + pushMessage);
                    }
                } else {
                    GAlerter.lab("In pushMessageQueueThreadN, there is a unknown obj.");
                }
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "pushMsgProcessorQueue"));

        //the cache setting.
        profileCache = new ProfileCache(config.getMemDiskCacheConfig());

        profileMemcachedCache = new ProfileMemcachedCache(config.getMemCachedConfig());

        socialProfileCache = new SocialProfileCache(config.getMemCachedConfig());

        //initialize the wall.
//		Timer onlineUserReporter = new Timer();
//		onlineUserReporter.scheduleAtFixedRate(new OnlineStatusReportTimer(), 1000 * 15, 1000 * 60);
    }


    private void processQueuedEvent(Event event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to processQueuedEvent:" + event);
        }

        //check the event type.
        if (event instanceof ProfileSumIncreaseEvent) {
            ProfileSumIncreaseEvent castEvent = (ProfileSumIncreaseEvent) event;

            boolean increaseFlag = false;
            try {
                increaseFlag = writeAbleProfileHandler.increaseProfileSum(castEvent.getProfileId(), castEvent.getField(), castEvent.getCount());
            } catch (Exception e) {
                //
                GAlerter.lab("ProfileLogic processQueuedEvent error.", e);
            }

            //
            if (increaseFlag) {
                profileCache.removeProfile(castEvent.getProfileId());
            }
        } else if (event instanceof ProfileOnlineOnEvent) {
            ProfileOnlineOnEvent castEvent = (ProfileOnlineOnEvent) event;

            profileCache.putProfileOnlineStatus(castEvent.getUno());
        } else if (event instanceof ProfileOnlineOffEvent) {
            ProfileOnlineOffEvent castEvent = (ProfileOnlineOffEvent) event;

            profileCache.removeProfileOnlineStatus(castEvent.getUno());
        } else if (event instanceof ProfileLastInteractionIncreaseEvent) {
            ProfileLastInteractionIncreaseEvent lastInteractionIncreaseEvent = (ProfileLastInteractionIncreaseEvent) event;
            if (!StringUtil.isEmpty(lastInteractionIncreaseEvent.getValue())) {
                boolean increaseFlag = false;
                try {
                    Map<ObjectField, Object> keyValues = new HashMap<ObjectField, Object>();
                    keyValues.put(lastInteractionIncreaseEvent.getField(), lastInteractionIncreaseEvent.getValue());
                    increaseFlag = writeAbleProfileHandler.updateProfileSum(lastInteractionIncreaseEvent.getOwnUno(), keyValues);
                } catch (Exception e) {
                    //
                    GAlerter.lab("ProfileLogic processQueuedEvent error.", e);
                }

                //
                if (increaseFlag) {
                    profileCache.removeProfile(lastInteractionIncreaseEvent.getOwnUno());
                }

            } else {
                boolean increaseFlag = false;

                try {
                    if (lastInteractionIncreaseEvent.getField().equals(ProfileSumField.LASTCONTENTID)) {
                        // last content id
                        Set<String> unos = new HashSet<String>();
                        unos.add(lastInteractionIncreaseEvent.getOwnUno());
                        Map<String, List<Content>> map = ContentServiceSngl.get().queryLastestContentsByUno(unos, 1);

                        if (map.get(lastInteractionIncreaseEvent.getOwnUno()) != null) {
                            if (!CollectionUtil.isEmpty(map.get(lastInteractionIncreaseEvent.getOwnUno()))) {
                                Map<ObjectField, Object> keyValues = new HashMap<ObjectField, Object>();
                                keyValues.put(ProfileSumField.LASTCONTENTID, map.get(lastInteractionIncreaseEvent.getOwnUno()).get(0).getContentId());

                                increaseFlag = writeAbleProfileHandler.updateProfileSum(lastInteractionIncreaseEvent.getOwnUno(), keyValues);
                            }
                        }
                    } else if (lastInteractionIncreaseEvent.getField().equals(ProfileSumField.LASTREPLYID)) {
                        // last reply id  todo
                    }

                } catch (Exception e) {
                    //
                    GAlerter.lab("ProfileLogic processQueuedEvent error.", e);
                }

                //
                if (increaseFlag) {
                    profileCache.removeProfile(lastInteractionIncreaseEvent.getOwnUno());
                }
            }
        } else if (event instanceof ProfileMobileDeviceEvent) {
            ProfileMobileDeviceEvent catEvent = (ProfileMobileDeviceEvent) event;
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ProfileClientMobileDeviceField.UNO, catEvent.getUno()));
            queryExpress.add(QueryCriterions.eq(ProfileClientMobileDeviceField.PLATFORM, catEvent.getPlatform()));
            queryExpress.add(QueryCriterions.eq(ProfileClientMobileDeviceField.CLIENT_ID, catEvent.getClientId()));
            queryExpress.add(QueryCriterions.eq(ProfileClientMobileDeviceField.CLIENT_TOKEN, catEvent.getClientToken()));
            queryExpress.add(QueryCriterions.eq(ProfileClientMobileDeviceField.APP_ID, catEvent.getAppId()));
            ProfileClientMobileDevice mobileDevice = null;
            try {
                mobileDevice = getProfileClientMobileDevice(queryExpress);
            } catch (ServiceException e) {
                GAlerter.lab("ProfileLogic getClientDevice error.", e);
            }
            if (mobileDevice == null) {
                mobileDevice = new ProfileClientMobileDevice();
                mobileDevice.setAppId(catEvent.getAppId());
                mobileDevice.setUno(catEvent.getUno());
                mobileDevice.setPlatform(AppPlatform.getByCode(catEvent.getPlatform()));
                mobileDevice.setClientId(catEvent.getClientId());
                mobileDevice.setClientToken(catEvent.getClientToken());
                try {
                    createProfileClientMobileDevice(mobileDevice);
                } catch (ServiceException e) {
                    GAlerter.lab("ProfileLogic insertProfileClientMobileDevice error.", e);
                }
            }

        } else {
            logger.info("ProfileLogic discard the unknown event:" + event);
        }
    }

    private void processModifyQueuedEvent(UpdateExpressWarp updateExpressWarp) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to processModifyQueuedEvent:" + updateExpressWarp);
        }

        try {
            writeAbleProfileHandler.updateBlog(updateExpressWarp.getUpdateExpress(), updateExpressWarp.getQueryExpress());
        } catch (Exception e) {
            //
            GAlerter.lab("ProfileLogic processQueuedEvent error.", e);
        }

    }


    @Override
    public Profile getProfileByUno(String uno) throws ServiceException {
        Profile returnValue = null;

        //get the profile from cache or from db.
        returnValue = profileCache.getProfile(uno);
        if (returnValue == null) {
            ProfileBlog blog = getProfileBlogByUno(uno);
            if (blog != null) {
                returnValue = new Profile(uno);

                returnValue.setBlog(blog);
                returnValue.setDetail(this.getProfileDetail(uno));
                returnValue.setSum(this.getProfileSum(uno));
                returnValue.setSetting(this.getProfileSetting(uno));

                //put the profile back to cache
                profileCache.putProfile(uno, returnValue);
            }
        }

        //get the online status.
        if (returnValue != null) {
            returnValue.setOnlineStatus(profileCache.getProfileOnlineStatus(uno));
        }

        return returnValue;
    }

    @Override
    public List<Profile> queryProfilesByUnos(Set<String> unos) throws ServiceException {
        List<Profile> returnValue = new ArrayList<Profile>();

        for (String uno : unos) {
            Profile profile = getProfileByUno(uno);
            if (profile != null) {
                returnValue.add(profile);
            }
        }

        return returnValue;
    }

    @Override
    public Map<String, Profile> queryProfilesByUnosMap(Set<String> unos) throws ServiceException {
        Map<String, Profile> returnValue = new HashMap<String, Profile>();
        if (!CollectionUtil.isEmpty(unos)) {
            for (String uno : unos) {
                if (StringUtil.isEmpty(uno)) {
                    Profile profile = getProfileByUno(uno);
                    if (profile != null) {
                        returnValue.put(uno, profile);
                    }
                }
            }
        }

        return returnValue;
    }

    @Override
    public ProfileBlog getProfileBlogByUno(String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyProfileHandlersPool to getProfileBlogByUno, uno is " + uno);
        }

        return readonlyProfileHandlersPool.getHandler().getProfileBlogByUno(uno);
    }

    @Override
    public ProfileBlog getProfileBlogByDomain(String domain) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyProfileHandlersPool to getProfileBlogByUno, domain is " + domain);
        }

        return readonlyProfileHandlersPool.getHandler().getProfileBlogByDomain(domain);
    }

    @Override
    public ProfileBlog getProfileBlogByScreenName(String screenName) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyProfileHandlersPool to getProfileBlogByUno, screenName " + screenName);
        }

        return readonlyProfileHandlersPool.getHandler().getProfileBlogByScreenName(screenName);
    }

    public Map<String, ProfileBlog> queryProfileBlogsByLikeScreenName(String screenName) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyProfileHandlersPool to getProfileBlogMapByScreenName, screenName " + screenName);
        }

        return readonlyProfileHandlersPool.getHandler().queryProfileBlogsByLikeScreenName(screenName);
    }

    @Override
    public Map<String, ProfileBlog> queryProfileBlogByScreenNamesMap(Set<String> screenNames) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyProfileHandlersPool to getProfileBlogByUno, screenNames ");
        }

        return readonlyProfileHandlersPool.getHandler().queryProfileBlogByScreenNamesMap(screenNames);
    }

    @Override
    public PageRows<Profile> searchProfileBlogs(String keyWord, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyProfileHandlersPool to searchProfileBlogs, keyWord " + keyWord);
        }

        PageRows<Profile> returnValue = new PageRows<Profile>();

        PageRows<ProfileBlog> list = readonlyProfileHandlersPool.getHandler().searchProfileBlog(keyWord, page);

        returnValue.setRows(new ArrayList<Profile>());
        returnValue.setPage(list.getPage());

        for (ProfileBlog blog : list.getRows()) {
            Profile profile = new Profile(blog.getUno());

            profile.setBlog(blog);
            profile.setDetail(this.getProfileDetail(blog.getUno()));
            profile.setSum(this.getProfileSum(blog.getUno()));
            profile.setSetting(this.getProfileSetting(blog.getUno()));

            //
            returnValue.getRows().add(profile);
        }


        return returnValue;
    }

    @Override
    public PageRows<Profile> queryProfileBlogsByDateStep(Date startDate, Date endDate, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleProfileHandler to queryProfileBlogsByDateStep, startDate is " + new Timestamp(startDate.getTime()) + "endDate is " + new Timestamp(endDate.getTime()));
        }
        PageRows<Profile> returnValue = new PageRows<Profile>();

        PageRows<ProfileBlog> list = readonlyProfileHandlersPool.getHandler().queryProfileBlogsByDateStep(startDate, endDate, page);

        returnValue.setRows(new ArrayList<Profile>());
        returnValue.setPage(list.getPage());

        for (ProfileBlog blog : list.getRows()) {
            Profile profile = new Profile(blog.getUno());

            profile.setBlog(blog);
            profile.setDetail(this.getProfileDetail(blog.getUno()));
            profile.setSum(this.getProfileSum(blog.getUno()));
            profile.setSetting(this.getProfileSetting(blog.getUno()));
            //
            returnValue.getRows().add(profile);
        }
        return returnValue;
    }

    @Override
    public ProfileBlog createProfileBlog(ProfileBlog profileBlog) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleProfileHandler to createProfileBlog, profileBlog is " + profileBlog);
        }


        return writeAbleProfileHandler.createProfileBlog(profileBlog);
    }

//    @Override
//    public ProfileBlog createProfileBlogGenDomain(ProfileBlog profileBlog) throws ServiceException {
//        if (logger.isDebugEnabled()) {
//            logger.debug("Call the writeAbleProfileHandler to createProfileBlogGenDomain, profileBlog is " + profileBlog);
//        }
//
//        ProfileBlog returnObj = null;
//        returnObj = writeAbleProfileHandler.createProfileBlogGenDomain(profileBlog);
//        if (returnObj == null) {
//            throw new ServiceException(ProfileServiceException.CREATE_BY_GENDOMAIN_REPEAT, "create profile gen domain error.");
//        }
//
//        return returnObj;
//    }

    @Override
    public boolean updateProfileBlog(ProfileBlog profileBlog) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleProfileHandler to updateProfileBlog, profileBlog is " + profileBlog);
        }

        //remove the cache
        profileCache.removeProfile(profileBlog.getUno());

        return writeAbleProfileHandler.updateProfileBlog(profileBlog);
    }

    @Override
    public boolean updateProfileBlog(String uno, Map<ObjectField, Object> keyValues) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleProfileHandler to updateProfileBlog, keyValues is " + keyValues);
        }

        //remove the cache
        profileCache.removeProfile(uno);

        return writeAbleProfileHandler.updateProfileBlog(uno, keyValues);
    }

    @Override
    public ProfileDetail getProfileDetail(String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyProfileHandlersPool to getProfileDetail, uno is " + uno);
        }

        ProfileDetail returnValue = readonlyProfileHandlersPool.getHandler().getProfileDetail(uno);
        if (returnValue == null) {
            returnValue = new ProfileDetail(uno);
            returnValue.setCreateDate(new Date());
            returnValue.setCompleteStatus(ActStatus.UNACT);

            returnValue = writeAbleProfileHandler.createProfileDetail(returnValue);
        } else {
            logger.error("Profile Logic getProfileDetail error, the detail is null.");
        }

        return returnValue;
    }

    @Override
    public ProfileDetail createProfileDetail(ProfileDetail profileDetail) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleProfileHandler to createProfileDetail, profileDetail " + profileDetail);
        }

        return writeAbleProfileHandler.createProfileDetail(profileDetail);
    }

    @Override
    public boolean updateProfileDetail(ProfileDetail profileDetail) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleProfileHandler to updateProfileDetail, profileDetail " + profileDetail);
        }

        //remove the cache
        profileCache.removeProfile(profileDetail.getUno());


        socialProfileCache.removeSocialProfile(profileDetail.getUno());

        return writeAbleProfileHandler.updateProfileDetail(profileDetail);
    }

    @Override
    public boolean updateProfileDetail(String uno, Map<ObjectField, Object> keyValues) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleProfileHandler to updateProfileDetail, keyValues " + keyValues);
        }

        //remove the cache
        profileCache.removeProfile(uno);

        socialProfileCache.removeSocialProfile(uno);

        return writeAbleProfileHandler.updateProfileDetail(uno, keyValues);
    }

    @Override
    public boolean updateProfileBlog(String uno, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleProfileHandler to updateProfileDetail, updateExpress " + updateExpress);
        }

        //remove the cache
        profileCache.removeProfile(uno);

        return writeAbleProfileHandler.updateProfileBlog(uno, updateExpress);
    }

    @Override
    public ProfileBlog queryProfileblogByPhoneNum(String phone, ActStatus actStatus) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleProfileHandler to queryProfileByPhoneNum, phone " + phone + " actStatus:" + actStatus);
        }


        return readonlyProfileHandlersPool.getHandler().getProfileBlogByPhone(phone, actStatus);
    }

    @Override
    public ProfileSetting getProfileSetting(String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyProfileHandlersPool to getProfileSetting, uno " + uno);
        }

        ProfileSetting returnValue = null;

        returnValue = readonlyProfileHandlersPool.getHandler().getProfileSettingByUno(uno);
        if (returnValue == null) {
            returnValue = new ProfileSetting(uno);

            writeAbleProfileHandler.createProfileSetting(returnValue);
        } else {
            logger.error("Profile Logic getProfileSetting error, the setting is null.");
        }

        return returnValue;
    }

    @Override
    public ProfileSetting saveProfileSetting(ProfileSetting setting) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleProfileHandler to saveProfileSetting, setting " + setting);
        }

        //remove the cache
        profileCache.removeProfile(setting.getUno());

        return writeAbleProfileHandler.createProfileSetting(setting);
    }

    @Override
    public boolean updateProfileSetting(ProfileSetting setting) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleProfileHandler to saveProfileSetting, setting " + setting);
        }

        //remove the cache
        profileCache.removeProfile(setting.getUno());

        removeCache(setting.getUno());

        return writeAbleProfileHandler.updateProfileSetting(setting);
    }

    @Override
    public boolean updateProfileSetting(String uno, Map<ObjectField, Object> keyValues) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleProfileHandler to saveProfileSetting, keyValues " + keyValues);
        }

        //remove the cache
        profileCache.removeProfile(uno);

        removeCache(uno);

        return writeAbleProfileHandler.updateProfileSetting(uno, keyValues);
    }


    @Override
    public ProfileSum getProfileSum(String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyProfileHandlersPool to getProfileSum, uno " + uno);
        }

        ProfileSum returnValue = readonlyProfileHandlersPool.getHandler().getProfileSum(uno);
        if (returnValue == null) {
            returnValue = new ProfileSum(uno);

            writeAbleProfileHandler.createProfileSum(returnValue);
        } else {
            logger.error("Profile Logic getProfileSum error, the sum is null.");
        }

        return returnValue;
    }

    @Override
    public boolean updateProfileSum(String uno, Map<ObjectField, Object> keyValues) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the updateProfileSum to updateProfileSum, keyValues " + keyValues);
        }

        //remove the cache
        profileCache.removeProfile(uno);

        removeCache(uno);

        return writeAbleProfileHandler.updateProfileSum(uno, keyValues);
    }

    @Override
    public boolean increaseProfileSum(String uno, Map<ObjectField, Object> keyValues) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the updateProfileSum to increaseProfileSum, keyValues " + keyValues);
        }

        //remove the cache
        profileCache.removeProfile(uno);

        socialProfileCache.removeSocialProfile(uno);

        return writeAbleProfileHandler.increaseProfileSum(uno, keyValues);
    }

    //  -------- profile  experience begin------
    @Override
    public List<ProfileExperience> createProfileExperience(String uno, List<ProfileExperience> list) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call the createProfileExperience experience :" + uno);
        }
        return writeAbleProfileHandler.createProfileExperience(list);
    }

    @Override
    public boolean removeProfileExperience(String uno, ProfileExperienceType experienceType) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call the removeProfileExperience uno = " + uno);
        }
        profileCache.removeProfile(uno);
        return writeAbleProfileHandler.removeProfileExperience(uno, experienceType);
    }

    @Override
    public List<ProfileExperience> queryProfileExperienceByUno(String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call the queryProfileExperienceByUno uno = " + uno);
        }
        return writeAbleProfileHandler.queryProfileExperienceByUno(uno);
    }
    //  -------- profile  experience end ------

//    public PageRows<ViewAccountProfile> queryViewAccountProfileBlogListByAccount(ProfileQueryParam param, Pagination page) throws ServiceException {
//        if (logger.isDebugEnabled()) {
//            logger.debug("Call the readonlyProfileHandlersPool to queryViewAccountProfileBlogListByAccount, ProfileQueryParam is " + param);
//        }
//
//        return readonlyProfileHandlersPool.getHandler().queryViewAccountProfileBlogListByAccount(param, page);
//    }
//
//    public PageRows<ViewAccountProfile> queryViewAccountProfileBlogListByBlog(ProfileQueryParam param, Pagination page) throws ServiceException {
//        if (logger.isDebugEnabled()) {
//            logger.debug("Call the readonlyProfileHandlersPool to queryViewAccountProfileBlogListByBlog, ProfileQueryParam is " + param);
//        }
//
//        return readonlyProfileHandlersPool.getHandler().queryViewAccountProfileBlogListByBlog(param, page);
//    }

    @Override
    public boolean modifyBlog(String profileUno, UpdateExpress updateExpress, QueryExpress queryExpress, boolean isQueued) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to update, updateExpress:" + updateExpress + ", queryExpress:" + queryExpress + " isQueued:" + isQueued);
        }

        boolean returnBoolean = true;
        if (!isQueued) {
            returnBoolean = writeAbleProfileHandler.updateBlog(updateExpress, queryExpress) > 0;
        } else {
            modiyBlogProcessQueueThreadN.add(new UpdateExpressWarp(updateExpress, queryExpress));
        }
        //remove the cache
        profileCache.removeProfile(profileUno);
        return returnBoolean;
    }

    @Override
    public PageRows<ProfileBlog> query(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to query, QueryExpress:" + queryExpress + ", Pagination:" + page);
        }
        return readonlyProfileHandlersPool.getHandler().query(queryExpress, page);
    }

    @Override
    public ProfileMobileDevice increaseProfileMobileDevice(ProfileMobileDevice entry) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to create profileMobileDevice");
        }

        return writeAbleProfileHandler.increaseProfileMobileDevice(entry);
    }

    @Override
    public ProfileMobileDevice getProfileMobileDevice(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to get ProfileMobileDevice");
        }

        return readonlyProfileHandlersPool.getHandler().getProfileMobileDevice(queryExpress);
    }

    @Override
    public PageRows<ProfileMobileDevice> queryProfileMobileDevice(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to query ProfileMobileDevice");
        }

        return readonlyProfileHandlersPool.getHandler().queryProfileMobileDevice(queryExpress, page);
    }

    @Override
    public RangeRows<ProfileMobileDevice> queryProfileMobileDevice(QueryExpress queryExpress, Rangination range) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to query ProfileMobileDevice");
        }

        return readonlyProfileHandlersPool.getHandler().queryProfileMobileDevice(queryExpress, range);
    }

    @Override
    public boolean modifyProfileMobileDevice(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to modifyProfileMobileDevice");
        }

        return writeAbleProfileHandler.updateProfileMobileDevice(updateExpress, queryExpress) > 0;
    }

    @Override
    public ProfileDeveloper createProfileDeveloper(ProfileDeveloper profileDeveloper) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to createProfileDeveloper");
        }
        return writeAbleProfileHandler.createProfileDeveloper(profileDeveloper);
    }

    @Override
    public ProfileDeveloper getProfileDeveloper(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to createProfileDeveloper");
        }
        return readonlyProfileHandlersPool.getHandler().getProfileDeveloper(queryExpress);
    }

    @Override
    public List<ProfileDeveloper> queryProfileDeveloper(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to createProfileDeveloper");
        }
        return readonlyProfileHandlersPool.getHandler().queryProfileDeveloper(queryExpress);
    }

    @Override
    public PageRows<ProfileDeveloper> queryProfileDeveloperByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to createProfileDeveloper");
        }
        return readonlyProfileHandlersPool.getHandler().queryProfileDeveloperByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyProfileDeveloper(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to createProfileDeveloper");
        }
        return writeAbleProfileHandler.modifyProfileDeveloper(updateExpress, queryExpress);
    }

    @Override
    public String saveMobileCode(String uno, String code) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to saveMobileCode");
        }
        logger.info("=====================saveMobileCode===================== uno===" + uno + "============code=" + code);
        profileMemcachedCache.putMobileCode(uno, code);
        return uno;
    }

    @Override
    public String getMobileCode(String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to getMobileCode");
        }
        logger.info("=====================getMobileCode===================== uno===" + uno);
        return profileMemcachedCache.getMobileCode(uno);
    }

    @Override
    public boolean removeMobileCode(String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to getMobileCode");
        }

        return profileMemcachedCache.removeMobileCode(uno);
    }

    @Override
    public ProfilePlayingGames createProfilePlayingGames(ProfilePlayingGames profilePlayingGames) throws ServiceException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ProfilePlayingGames getProfilePlayingGames(String uno, long gameId) throws ServiceException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean modifyProfilePlayingGames(String uno, long gameId, UpdateExpress updateExpress) throws ServiceException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ProfilePlayingGames> queryProfilePlayingGames(String uno) throws ServiceException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ProfileClientMobileDevice createProfileClientMobileDevice(ProfileClientMobileDevice mobileDevice) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to createClientDevice:" + mobileDevice);
        }
        return writeAbleProfileHandler.createProfileClientMobileDevice(mobileDevice);
    }

    @Override
    public ProfileClientMobileDevice getProfileClientMobileDevice(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to getClientDevice:" + queryExpress);
        }
        return writeAbleProfileHandler.getProfileClientMobileDevice(queryExpress);
    }


    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("The event is recieved, event:" + event);
        }

        eventProcessQueueThreadN.add(event);

        return true;
    }

    @Override
    public boolean sendPushMessage(PushMessage pushMessage) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic sendPushMessage . pushMessage = " + pushMessage);
        }
        pushMessageQueueThreadN.add(pushMessage);
        return true;
    }

    ///////////////////////////
    @Override
    public SocialProfile getSocialProfileByScreenName(String screenName) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("ProfileLogic getSocialProfileByScreenName .screenName= " + screenName);
        }

        SocialProfile socialProfile = null;

        String uno = socialProfileCache.getUnoByScreenName(screenName);
        if (!StringUtil.isEmpty(uno)) {
            socialProfile = getSocialProfileByUno(uno);
        } else {
            socialProfile = loadProfileByScreenName(screenName);
        }

        return socialProfile;
    }

    @Override
    public SocialProfile createSocialProfile(SocialProfileBlog profileBlog) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("ProfileLogic createSocialProfile . profileBlog = " + profileBlog);
        }

        profileBlog = writeAbleProfileHandler.createSocialProfileBlog(profileBlog);

        ProfileDetail profileDetail = getProfileDetail(profileBlog.getUno());
        ProfileSetting profileSetting = getProfileSetting(profileBlog.getUno());
        ProfileSum profileSum = getProfileSum(profileBlog.getUno());

        SocialProfile returnObj = new SocialProfile(profileBlog.getUno());
        returnObj.setBlog(profileBlog);
        returnObj.setDetail(profileDetail);
        returnObj.setSetting(profileSetting);
        returnObj.setSum(profileSum);

        putCache(returnObj);

        SocialDefaultFocusEvent event = new SocialDefaultFocusEvent();
        event.setUno(profileBlog.getUno());
        EventDispatchServiceSngl.get().dispatch(event);

        return returnObj;
    }

    @Override
    public SocialProfile getSocialProfileByUno(String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("ProfileLogic getSocialProfileByUno.uno = " + uno);
        }

        SocialProfile profile = socialProfileCache.getSocialProfileByUno(uno);
        if (profile == null) {
            profile = loadProfileByUno(uno);
        }

        return profile;
    }

    @Override
    public Map<String, SocialProfile> querySocialProfilesByUnosMap(Set<String> unoSet) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("ProfileLogic querySocialProfilesByUnosMap.unoSet = " + unoSet);
        }

        //get by cache
        Map<String, SocialProfile> cacheMap = new HashMap<String, SocialProfile>();
        Set<String> dbUnoSet = new HashSet<String>();
        for (String uno : unoSet) {
            SocialProfile socialProfile = socialProfileCache.getSocialProfileByUno(uno);
            if (socialProfile != null) {
                cacheMap.put(socialProfile.getBlog().getUno(), socialProfile);
            } else {
                dbUnoSet.add(uno);
            }
        }

        //get by db and add cache
        Map<String, SocialProfile> dbMap = new HashMap<String, SocialProfile>();
        if (!CollectionUtil.isEmpty(dbUnoSet)) {
            for (String uno : unoSet) {
                SocialProfile socialProfile = loadProfileByUno(uno);
                if (socialProfile != null) {
                    dbMap.put(socialProfile.getBlog().getUno(), socialProfile);
                }
            }
        }

        //return
        Map<String, SocialProfile> returnMap = new HashMap<String, SocialProfile>();
        for (String uno : unoSet) {
            SocialProfile socialProfile = cacheMap.get(uno);

            if (socialProfile == null) {
                socialProfile = dbMap.get(uno);
            }

            if (socialProfile != null) {
                returnMap.put(uno, socialProfile);
            }
        }

        return returnMap;
    }

    @Override
    public boolean modifySocialProfileBlogByUno(UpdateExpress updateExpress, String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("ProfileLogic modifySocialProfileBlogByUno.updateExpress = " + updateExpress + " uno=" + uno);
        }

        boolean result = writeAbleProfileHandler.updateSocialBlog(updateExpress, new QueryExpress().add(QueryCriterions.eq(SocialProfileBlogField.UNO, uno)));

        if (result) {
            removeCache(uno);
        }

        return result;
    }

    @Override
    public boolean modifySocialProfileDetailByUno(UpdateExpress updateExpress, String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("ProfileLogic modifySocialProfileDetailByUno.updateExpress = " + updateExpress + " uno=" + uno);
        }

        boolean result = writeAbleProfileHandler.updateProfileDetail(uno, updateExpress);

        if (result) {
            removeCache(uno);
        }

        return result;
    }

    @Override
    public List<SocialProfile> querySocialProfilesByUnos(Set<String> unoSet) throws ServiceException {
        List<SocialProfile> returnValue = new ArrayList<SocialProfile>();

        for (String uno : unoSet) {
            SocialProfile profile = getSocialProfileByUno(uno);
            if (profile != null) {
                returnValue.add(profile);
            }
        }

        return returnValue;
    }

    @Override
    public PageRows<SocialProfile> queryNewestSocialProfile(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("ProfileLogic queryNewestSocialProfile.queryExpress:" + queryExpress + ",pagination:" + pagination);
        }
        PageRows<SocialProfile> returnRows = new PageRows<SocialProfile>();

        List<String> unoList = socialProfileCache.getNewestSocialProfileUnoList(pagination.getCurPage());
        if (unoList == null || CollectionUtil.isEmpty(unoList)) {
            PageRows<SocialProfileBlog> blogPageRows = readonlyProfileHandlersPool.getHandler().querySocialProfileBlogByPage(queryExpress, pagination);
            returnRows.setPage(blogPageRows == null ? pagination : blogPageRows.getPage());
            if (blogPageRows == null || CollectionUtil.isEmpty(blogPageRows.getRows())) {
                return returnRows;
            }

            unoList = new ArrayList<String>();
            for (SocialProfileBlog blog : blogPageRows.getRows()) {
                unoList.add(blog.getUno());
            }
        }
        if (!CollectionUtil.isEmpty(unoList)) {
            socialProfileCache.putNewestSocialProfileUnoList(pagination.getCurPage(), unoList);

            List<SocialProfile> list = new ArrayList<SocialProfile>();
            for (String uno : unoList) {
                SocialProfile socialProfile = getSocialProfileByUno(uno);
                if (socialProfile != null) {
                    list.add(socialProfile);
                }
            }
            returnRows.setRows(list);
        }
        return returnRows;
    }

    private SocialProfile loadProfileByUno(String uno) throws ServiceException {
        SocialProfileBlog blog = readonlyProfileHandlersPool.getHandler().getSocialProfileBlogByUno(uno);

        if (blog != null) {
            SocialProfile returnValue = new SocialProfile(uno);

            returnValue.setBlog(blog);

            ProfileDetail detail = readonlyProfileHandlersPool.getHandler().getProfileDetail(uno);
            returnValue.setDetail(detail);

            ProfileSum sum = readonlyProfileHandlersPool.getHandler().getProfileSum(uno);
            returnValue.setSum(sum);

            ProfileSetting setting = readonlyProfileHandlersPool.getHandler().getProfileSettingByUno(uno);
            returnValue.setSetting(setting);

            //put the profile back to cache
            putCache(returnValue);

            return returnValue;
        }
        return null;
    }

    private SocialProfile loadProfileByScreenName(String screenName) throws ServiceException {
        SocialProfileBlog blog = readonlyProfileHandlersPool.getHandler().getSocialProfileBlogByScreenName(screenName);

        if (blog != null) {
            SocialProfile returnValue = new SocialProfile(blog.getUno());

            returnValue.setBlog(blog);
            returnValue.setDetail(this.getProfileDetail(blog.getUno()));
            returnValue.setSum(this.getProfileSum(blog.getUno()));
            returnValue.setSetting(this.getProfileSetting(blog.getUno()));

            //put the profile back to cache
            putCache(returnValue);

            return returnValue;
        }
        return null;
    }


    private void putCache(SocialProfile profile) {
        socialProfileCache.putSocialProfile(profile);
        socialProfileCache.putUnoByScreenName(profile.getBlog().getScreenName(), profile.getBlog().getUno());
    }

    private void removeCache(String uno) {
        socialProfileCache.removeSocialProfile(uno);
    }

    ///////////////////////////
//	class OnlineStatusReportTimer extends TimerTask {
//		//
//		public void run() {
//			logger.info("OnlineStatusReportTimer start to reload.");
//
//			Date now = new Date();
//
//			long onlineNum = 0;
//			synchronized (profileCache) {
//				onlineNum = profileCache.getProfileOnlineStatusSize();
//			}
//
//			StatItemIncrease item = new StatItemIncrease(onlineNum);
//
//			item.setStatDomain(UserOnlineStatDomain.ONLINE_USER);
//			item.setSectionName("Online User");
//			item.setStatSection(new StatSectionDefault(StatSectionDefault.DEFAULT_SECTION_KEY));
//			item.setSectionName("total");
//
//			item.setDateType(StatDateType.MINUTE);
//			item.setStatDate(now);
//
//			item.setReportDate(now);
//			item.setStatValue(onlineNum);
//
//			try {
//				StatServiceSngl.get().increaseStat(item);
//			} catch (Exception e) {
//				//
//				GAlerter.lab("OnlineStatusReportTimer reportStat error.", e);
//			}
//
//			//stat the pool.
//			logger.info(">>>>>>>>>>>>>>>>>>>>>" + onlineNum);
//
//			logger.info("OnlineStatusReportTimer finish to reload.");
//		}
//	}

    private void processQueuedIOSPushMessage(PushMessage msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(" Call handler to processQueuedPushMessage:" + msg);
        }

        try {
            AppDeployment deployment = JoymeAppConfigServiceSngl.get().getAppDeploymentByCache(msg.getAppKey(), msg.getAppPlatform().getCode(), AppDeploymentType.CERTIFICATE.getCode(), null, null);
            if (deployment == null) {
                GAlerter.lab(this.getClass().getName() + " message not support ios.certinfo empty:" + msg);
                return;
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
//        if (!HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getCertificatieInfoMap().containsKey(msg.getAppKey())) {
//            GAlerter.lab(this.getClass().getName() + " message not support ios.certinfo empty:" + msg);
//            return;
//        }

        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.ne(ProfileClientMobileDeviceField.LAST_MSG_ID, msg.getPushMsgId()))
                .add(QueryCriterions.eq(ProfileClientMobileDeviceField.PLATFORM, AppPlatform.IOS.getCode()))
                .add(QueryCriterions.eq(ProfileClientMobileDeviceField.APP_ID, msg.getAppKey()));

        Pagination pagination = new Pagination(200, 1, 200);
        do {
            try {
                PageRows<ProfileClientMobileDevice> pushMessageDevicePageRows = writeAbleProfileHandler.queryProfileClientMobileByPage(queryExpress, pagination);

                if (pushMessageDevicePageRows.getRows() == null) {
                    continue;
                }

                List<Long> successList = new ArrayList<Long>();
                for (ProfileClientMobileDevice device : pushMessageDevicePageRows.getRows()) {
                    boolean isSendMsg = sendPushMsgToIOS(msg, device);

                    if (isSendMsg) {
                        successList.add(device.getDeviceId());
                    }
                }

                if (!CollectionUtil.isEmpty(successList)) {
                    UpdateExpress updateExpress = new UpdateExpress()
                            .set(ProfileClientMobileDeviceField.LAST_MSG_ID, msg.getPushMsgId());
                    QueryExpress updateQueryExpress = new QueryExpress()
                            .add(QueryCriterions.in(ProfileClientMobileDeviceField.DEVICE_ID, successList.toArray()));
                    writeAbleProfileHandler.modifyProfileClientMobile(updateExpress, updateQueryExpress);
                }
            } catch (DbException e) {
                GAlerter.lab(this.getClass().getName() + " occured DbException.e:", e);
            }

        } while (pagination.hasNextPage());
    }

    private boolean sendPushMsgToIOS(PushMessage pushMsg, ProfileClientMobileDevice device) {

        String deviceToken = device.getClientToken();//iphone手机获取的token

        // default ..if other modify   (message.pushMsgCode)
//        String sound = "default";//铃音
        String sound = "";
        int badge = 0;

        List<String> tokens = new ArrayList<String>();
        tokens.add(deviceToken);

        PushNotificationPayload payLoad = new PushNotificationPayload();
        try {
            payLoad.addAlert(pushMsg.getShortMessage()); // 消息内容
            payLoad.addBadge(badge); // iphone应用图标上小红圈上的数值
            if (pushMsg.getOptions() != null) {
                payLoad.addCustomDictionary("option", JsonBinder.buildNormalBinder().toJson(pushMsg.getOptions()));
            }
            if (!StringUtil.isEmpty(sound)) {
                payLoad.addSound(sound);//铃音
            }
        } catch (JSONException e) {
            logger.debug("payLoad is error " + e);
        }

        AppCertificateInfo info = null;
        try {
            AppDeployment deployment = JoymeAppConfigServiceSngl.get().getAppDeploymentByCache(pushMsg.getAppKey(), pushMsg.getAppPlatform().getCode(), AppDeploymentType.CERTIFICATE.getCode(), null, null);
            if (deployment == null) {
                GAlerter.lab(this.getClass().getName() + " message not support ios.certinfo empty:" + pushMsg);
                return false;
            }
            info = new AppCertificateInfo(deployment.getAppkey(), deployment.getPath(), deployment.getPassword(), deployment.getIsProduct());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("push msg to APNS : AppCertificateInfo is " + info);
        }
        boolean b = sendToAPNS(tokens, payLoad, info.getPath(), info.getPassword(), info.isProduction());
        if (!b) {
            b = sendToAPNS(tokens, payLoad, info.getPath(), info.getPassword(), info.isProduction());
        }
        return b;
    }

    private boolean sendToAPNS(List<String> tokens, PushNotificationPayload payLoad, String certificatePath, String certificatePassword, boolean isProduction) {
        int successful = 0;
        PushNotificationManager pushManager = null;
        try {
            pushManager = new PushNotificationManager();
            //true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
            pushManager.initializeConnection(new AppleNotificationServerBasicImpl(certificatePath, certificatePassword, isProduction));
            List<PushedNotification> notifications = new ArrayList<PushedNotification>();

            // 发送push消息
            if (tokens.size() == 1) {

                Device device = new BasicDevice();
                device.setToken(tokens.get(0));

                if (logger.isDebugEnabled()) {
                    logger.debug("deviceToken is " + device.getToken());
                }

                PushedNotification notification = pushManager.sendNotification(device, payLoad, true);
                notifications.add(notification);

            } else {
                List<Device> device = new ArrayList<Device>();
                for (String token : tokens) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("deviceToken is " + token);
                    }

                    device.add(new BasicDevice(token));
                }

                notifications = pushManager.sendNotifications(payLoad, device);
            }

            List<PushedNotification> failedNotifications = PushedNotification.findFailedNotifications(notifications);
            List<PushedNotification> successfulNotifications = PushedNotification.findSuccessfulNotifications(notifications);

            int failed = failedNotifications.size();
            successful = successfulNotifications.size();
            if (logger.isDebugEnabled()) {
                logger.error("failedNotifications size: " + failed);
                logger.debug("successfulNotifications size: " + successful);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pushManager != null) {
                try {
                    pushManager.stopConnection();
                } catch (CommunicationException e) {
                    GAlerter.lab(this.getClass().getName() + " sendToAPNS occured CommunicationException.e: ", e);
                } catch (KeystoreException e) {
                    GAlerter.lab(this.getClass().getName() + " sendToAPNS occured KeystoreException.e: ", e);
                }
            }
        }
        return tokens.size() == successful;
    }

    private void sendOutSolrjProfileEvent(String uno, String appKey) {
        try {
            SocialProfile socialProfile = getSocialProfileByUno(uno);
            ProfileSolrjEvent event = new ProfileSolrjEvent();
            event.setUno(socialProfile.getUno());
            event.setAppkey(appKey);
            event.setSearchtext(socialProfile.getBlog().getScreenName() + socialProfile.getBlog().getDescription());
            event.setBirthday(socialProfile.getDetail().getBirthday());
            event.setSex(socialProfile.getDetail().getSex());
            event.set_version_(System.currentTimeMillis());
            EventDispatchServiceSngl.get().dispatch(event);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
        }
    }

}
