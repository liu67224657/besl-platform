package com.enjoyf.platform.serv.message;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.db.joymeapp.JoymeAppHandler;
import com.enjoyf.platform.db.message.MessageHandler;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebApiHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.Question;
import com.enjoyf.platform.service.ask.WanbaJt;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.*;
import com.enjoyf.platform.service.event.system.wanba.WanbaQuestionNoticeEvent;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.PushMessageOption;
import com.enjoyf.platform.service.joymeapp.PushMessageOptions;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.AppConfigInfo;
import com.enjoyf.platform.service.joymeapp.config.AppConfigUtil;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.message.*;
import com.enjoyf.platform.service.message.ClientDevice;
import com.enjoyf.platform.service.message.ClientDeviceField;
import com.enjoyf.platform.service.message.PushMessage;
import com.enjoyf.platform.service.message.PushMessageField;
import com.enjoyf.platform.service.notice.wanba.WanbaNoticeBodyType;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.point.GiftReserve;
import com.enjoyf.platform.service.point.GiftReserveField;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.profile.*;
import com.enjoyf.platform.service.profile.ProfileSumField;
import com.enjoyf.platform.service.profile.socialclient.SocialProfile;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.SocialConstants;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.sql.*;
import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */

/**
 * The UserPropsLogic class holds the core logic for the server.
 * This class is expected to change almost completely from
 * server to server. <p>
 * <p/>
 * UserPropsLogic is called by UserPropsPacketDecoder.
 */
class MessageLogic implements MessageService {
    //
    private static final Logger logger = LoggerFactory.getLogger(MessageLogic.class);

    private static final int SOCIAL_MESSAGE_PAGE_SIZE = 10;

    //
    private MessageConfig config;

    //the handler's
    private MessageHandler writeAbleMessageHandler;
    private HandlerPool<MessageHandler> readonlyMessageHandlersPool;

    //
    private QueueThreadN eventProcessQueueThreadN = null;

    // push msg
    private QueueThreadN pushMsgProcessQueueThreadN = null;

    private NoticeCache noticeCache;
    private SocialMessageCache socialMessageCache;

    //the handler's
    private JoymeAppHandler writeAbleJoymeAppHandler;
    private HandlerPool<JoymeAppHandler> readonlyJoymeAppHandlersPool;

    private RedisManager messageRedis;

    MessageLogic(MessageConfig cfg) {
        config = cfg;
        messageRedis = config.getRedisManager();
        //initialize the handler.
        try {
            //
            writeAbleMessageHandler = new MessageHandler(config.getWriteableDataSourceName(), config.getProps(), messageRedis);
            writeAbleJoymeAppHandler = new JoymeAppHandler(config.getJoymeAppWriteableDataSourceName(), config.getProps());
            //
            readonlyMessageHandlersPool = new HandlerPool<MessageHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyMessageHandlersPool.add(new MessageHandler(dsn, config.getProps(), messageRedis));
            }

            readonlyJoymeAppHandlersPool = new HandlerPool<JoymeAppHandler>();
            for (String dsn : config.getJoymeAppReadonlyDataSourceNames()) {
                readonlyJoymeAppHandlersPool.add(new JoymeAppHandler(dsn, config.getProps()));
            }
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());

            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        //
        eventProcessQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                if (obj instanceof Event) {
                    processQueuedEvent((Event) obj);
                } else if (obj instanceof Notice) {
                    processQueuedPostNotice((Notice) obj);
                } else if (obj instanceof ClientDevice) {
                    processClientDevice((ClientDevice) obj);
                } else {
                    GAlerter.lab("In eventProcessQueueThreadN, there is a unknown obj.");
                }
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessorQueue"));

        //
        pushMsgProcessQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                if (obj instanceof PushMsg) {
                    processQueuedPushMessage((PushMsg) obj);
                } else if (obj instanceof Message) {
                    processPostMessage((Message) obj);
                } else if (obj instanceof com.enjoyf.platform.service.joymeapp.PushMessage) {
                    processSocialPostMessage((com.enjoyf.platform.service.joymeapp.PushMessage) obj);
                } else {
                    GAlerter.lab("In pushMsgProcessQueueThreadN, there is a unknown obj.");
                }
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "pushMsgProcessorQueue"));

        noticeCache = new NoticeCache(config.getMemCachedConfig());

        socialMessageCache = new SocialMessageCache(config.getMemCachedConfig());

//        joymeappPushMessageQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
//            public void process(Object obj) {
//                if (obj instanceof com.enjoyf.platform.service.joymeapp.PushMessage) {
//                    com.enjoyf.platform.service.joymeapp.PushMessage pushMessage = (com.enjoyf.platform.service.joymeapp.PushMessage) obj;
//                    if (pushMessage.getSendStatus().equals(ActStatus.REJECTED)) {
//                        //发送中
//                        UpdateExpress updateExpress = new UpdateExpress();
//                        updateExpress.set(com.enjoyf.platform.service.joymeapp.PushMessageField.SEND_STATUS, ActStatus.ACTING.getCode());
//                        try {
//                            modifyPushMessage(updateExpress, new QueryExpress()
//                                    .add(QueryCriterions.eq(com.enjoyf.platform.service.joymeapp.PushMessageField.PUSHMSGID, pushMessage.getPushMsgId())), pushMessage.getAppKey());
//                        } catch (Exception e) {
//                            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
//                        }
//                        logger.info("-----------------pushMessageQueueThreadN acting msgid:" + pushMessage.getPushMsgId() + "---------------------");
//                        //发送
//                        int umengPushChannel = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getUmengProductionMode();
//                        String appKeyPlatform = pushMessage.getAppKey();
//                        int platform = 0;
//                        if (pushMessage.getAppPlatform().getCode() == 0) {
//                            appKeyPlatform += "I";
//                            platform = 0;
//                        } else if (pushMessage.getAppPlatform().getCode() == 1) {
//                            appKeyPlatform += "A";
//                            platform = 1;
//                        } else if (pushMessage.getAppPlatform().getCode() == 2) {
//                            appKeyPlatform += "E";
//                            platform = 0;
//                        }
//                        List<String> umengVersionList = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getPushVersionList(appKeyPlatform, umengPushChannel);
//                        //全版本推
//                        if (StringUtil.isEmpty(pushMessage.getAppVersion())) {
//                            AppPushFactory.get().factory(AppPushChannel.getByCode(umengPushChannel), AppPlatform.getByCode(platform)).sendPushMessage(pushMessage);
//                        } else {
//                            //指定版本推
//                            String[] versionList = pushMessage.getAppVersion().split("\\|");
//                            String umengVersion = "";
//                            for (String version : versionList) {
//                                if (umengVersionList.contains(version)) {
//                                    umengVersion += (version + "|");
//                                }
//                            }
//                            logger.info("-----------------pushMessageQueueThreadN umeng push msgid:" + pushMessage.getPushMsgId() + " " + umengVersion + "---------------------");
//                            pushMessage.setAppVersion(umengVersion);
//                            AppPushFactory.get().factory(AppPushChannel.getByCode(umengPushChannel), AppPlatform.getByCode(platform)).sendPushMessage(pushMessage);
//                        }
//                    }
//                } else {
//                    GAlerter.lab("In pushMessageQueueThreadN, there is a unknown obj.");
//                }
//            }
//        }, new FQueueQueue(config.getQueueDiskStorePath(), "pushMsgProcessorQueue"));

    }


    //玩霸问答自动推送
    private void processWanbaQuestionNoticeEvent(WanbaQuestionNoticeEvent obj) {
        ClientDevice device = null;
        com.enjoyf.platform.service.usercenter.Profile profileSend = null;
        try {

            profileSend = UserCenterServiceSngl.get().getProfileByProfileId(obj.getProfileId());
            GAlerter.lab("====devicedevicedevice is profileSend====>" + profileSend.toString());
            if (profileSend == null) {
                return;
            }
            //过滤玩霸3.0 appkey
            QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(ClientDeviceField.UNO, profileSend.getUno()));
            queryExpress.add(QueryCriterions.eq(ClientDeviceField.APP_ID, "3iiv7VWfx84pmHgCUqRwun"));
            device = MessageServiceSngl.get().getClientDevice(queryExpress);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        if (device != null) {


            Question question = null;
            try {
                question = AskServiceSngl.get().getQuestion(obj.getQuestionId());
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            if (question == null) {
                return;
            }

            String text = "";
            try {
                if (WanbaNoticeBodyType.QUESTION_ACCEPTANSWER.equals(obj.getBodyType())) {
                    text = "恭喜你在“" + question.getTitle() + "”问题中的答案被采纳为最佳，快去看看吧";
                } else if (WanbaNoticeBodyType.QUESTION_NEWANSWER.equals(obj.getBodyType())) {
                    Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(obj.getDestProfileId());
                    String nick = profile == null ? "" : profile.getNick();
                    text = nick + "已经回答了你的问题“" + question.getTitle() + "”，快去看看吧";
                } else if (WanbaNoticeBodyType.QUESTION_EXPIRE.equals(obj.getBodyType())) {
                    text = "你的问题“" + question.getTitle() + "”已过期，快去看看有没有满意的答案";
                } else if (WanbaNoticeBodyType.QUESTION_FOLLOWQUESIONACCEPT.equals(obj.getBodyType())) {
                    text = "你关注的问题“" + question.getTitle() + "”已经被解决了，快去看看吧";
                } else if (WanbaNoticeBodyType.QUESTION_INVITE_ANSWERQUESTION.equals(obj.getBodyType())) {
                    Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(obj.getDestProfileId());
                    String nick = profile == null ? "" : profile.getNick();
                    text = nick + "邀请你回答问题“" + question.getTitle() + "”，快去看看吧";
                } else if (WanbaNoticeBodyType.QUESTION_ONEONONE_ASK.equals(obj.getBodyType())) {
                    Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(obj.getDestProfileId());
                    String nick = profile == null ? "" : profile.getNick();
                    text = nick + "向你提问“" + question.getTitle() + "”，快去看看吧";
                } else if (WanbaNoticeBodyType.QUESTION_VERIFY.equals(obj.getBodyType())) {
                    text = "大神，《" + obj.getExtStr() + "》里有问题“" + question.getTitle() + "”尚未被解决，等待您去抢答。";
                }
            } catch (ServiceException e) {
                e.printStackTrace();
                GAlerter.lab("====devicedevicedevice is null====>");
            }


            String sound = "nosound.caf";
            AppPushEvent appPushEvent = new AppPushEvent();
            appPushEvent.setPushMsgId(obj.getQuestionId());
            appPushEvent.setDevices(device.getClientToken());
            appPushEvent.setAppKey(device.getAppId());
            appPushEvent.setPlatform(device.getPlatform());
            appPushEvent.setEnterpriseType(device.getEnterpriserType());
            appPushEvent.setIcon("");
            appPushEvent.setSubject("着迷玩霸");
            appPushEvent.setContext(text);
            appPushEvent.setChannel(device.getAppChannel());
            appPushEvent.setVersion(device.getAppVersion());
            appPushEvent.setTags("");
            appPushEvent.setSound(sound);
            appPushEvent.setBadge(0);
            appPushEvent.setProfileIds(device.getProfileId());
            appPushEvent.setJt(WanbaJt.ASK_QUESTION_PAGE.getCode());
            appPushEvent.setJi(String.valueOf(obj.getQuestionId()));
            appPushEvent.setPushListType(PushListType.PUSH_MESSAGE);

            GAlerter.lab(text + "========>" + appPushEvent.toString());
            appPushFactorySendPushMessage(appPushEvent);
        } else {
            GAlerter.lab("====devicedevicedevice is null====>");
        }

    }

    private void processClientDevice(ClientDevice clientDevice) {
        try {
            UpdateExpress tokenUpdate = new UpdateExpress();
            tokenUpdate.set(ClientDeviceField.UNO, clientDevice.getUno());
            tokenUpdate.set(ClientDeviceField.PROFILEID, clientDevice.getProfileId());
            tokenUpdate.set(ClientDeviceField.CLIENT_TOKEN, clientDevice.getClientToken());
            if (clientDevice.getPushChannel() != null) {
                tokenUpdate.set(ClientDeviceField.PUSH_CHANNEL, clientDevice.getPushChannel().getCode());
            }
            if (clientDevice.getPlatform() != null) {
                tokenUpdate.set(ClientDeviceField.PLATFORM, clientDevice.getPlatform().getCode());
            }
            if (!StringUtil.isEmpty(clientDevice.getAppChannel())) {
                tokenUpdate.set(ClientDeviceField.APP_CHANNEL, clientDevice.getAppChannel());
            }
            if (!StringUtil.isEmpty(clientDevice.getAppVersion())) {
                tokenUpdate.set(ClientDeviceField.APP_VERSION, clientDevice.getAppVersion());
            }
            if (!StringUtil.isEmpty(clientDevice.getTags())) {
                tokenUpdate.set(ClientDeviceField.TAGS, clientDevice.getTags());
            }
            if (!StringUtil.isEmpty(clientDevice.getAdvId())) {
                tokenUpdate.set(ClientDeviceField.ADVID, clientDevice.getAdvId());
            }
            if (!StringUtil.isEmpty(clientDevice.getIp())) {
                tokenUpdate.set(ClientDeviceField.IP, clientDevice.getIp());
            }
            if (clientDevice.getEnterpriserType() != null) {
                tokenUpdate.set(ClientDeviceField.ENTERPRISER_TYPE, clientDevice.getEnterpriserType().getCode());
            }

            //保证一个设备对应一个人
            boolean tokenBool = writeAbleMessageHandler.modifyClientDevice(new QueryExpress()
                    .add(QueryCriterions.eq(ClientDeviceField.CLIENT_ID, clientDevice.getClientId()))
                    .add(QueryCriterions.eq(ClientDeviceField.APP_ID, clientDevice.getAppId())), tokenUpdate);

            boolean unoBool = false;
            if (!StringUtil.isEmpty(clientDevice.getProfileId())) {
                UpdateExpress profileUpdate = new UpdateExpress();
                profileUpdate.set(ClientDeviceField.UNO, clientDevice.getUno());
                profileUpdate.set(ClientDeviceField.CLIENT_ID, clientDevice.getClientId());
                profileUpdate.set(ClientDeviceField.CLIENT_TOKEN, clientDevice.getClientToken());
                if (clientDevice.getPushChannel() != null) {
                    profileUpdate.set(ClientDeviceField.PUSH_CHANNEL, clientDevice.getPushChannel().getCode());
                }
                if (clientDevice.getPlatform() != null) {
                    profileUpdate.set(ClientDeviceField.PLATFORM, clientDevice.getPlatform().getCode());
                }
                if (!StringUtil.isEmpty(clientDevice.getAppChannel())) {
                    profileUpdate.set(ClientDeviceField.APP_CHANNEL, clientDevice.getAppChannel());
                }
                if (!StringUtil.isEmpty(clientDevice.getAppVersion())) {
                    profileUpdate.set(ClientDeviceField.APP_VERSION, clientDevice.getAppVersion());
                }
                if (!StringUtil.isEmpty(clientDevice.getTags())) {
                    profileUpdate.set(ClientDeviceField.TAGS, clientDevice.getTags());
                }
                if (!StringUtil.isEmpty(clientDevice.getAdvId())) {
                    profileUpdate.set(ClientDeviceField.ADVID, clientDevice.getAdvId());
                }
                if (!StringUtil.isEmpty(clientDevice.getIp())) {
                    profileUpdate.set(ClientDeviceField.IP, clientDevice.getIp());
                }
                if (clientDevice.getEnterpriserType() != null) {
                    profileUpdate.set(ClientDeviceField.ENTERPRISER_TYPE, clientDevice.getEnterpriserType().getCode());
                }

                //保证一个人对应一个设备
                unoBool = writeAbleMessageHandler.modifyClientDevice(new QueryExpress()
                        .add(QueryCriterions.eq(ClientDeviceField.PROFILEID, clientDevice.getProfileId()))
                                // .add(QueryCriterions.ne(ClientDeviceField.CLIENT_ID, clientDevice.getClientId()))
                        .add(QueryCriterions.eq(ClientDeviceField.APP_ID, clientDevice.getAppId())), profileUpdate);
            }

            if (!unoBool && !tokenBool) {
                writeAbleMessageHandler.createClientDevice(clientDevice);
            }

            AppConfig appConfig = JoymeAppConfigServiceSngl.get().getAppConfig(AppConfigUtil.getAppConfigId(AppUtil.getAppKey(clientDevice.getAppId()), String.valueOf(clientDevice.getPlatform().getCode()), clientDevice.getAppVersion(), clientDevice.getAppChannel(), String.valueOf(clientDevice.getEnterpriserType().getCode())));
            if (appConfig == null) {
                AuthApp authApp = OAuthServiceSngl.get().getApp(AppUtil.getAppKey(clientDevice.getAppId()));
                appConfig = new AppConfig();
                appConfig.setConfigId(AppConfigUtil.getAppConfigId(AppUtil.getAppKey(clientDevice.getAppId()), String.valueOf(clientDevice.getPlatform().getCode()), clientDevice.getAppVersion(), clientDevice.getAppChannel(), String.valueOf(clientDevice.getEnterpriserType().getCode())));
                appConfig.setAppKey(AppUtil.getAppKey(clientDevice.getAppId()));
                appConfig.setPlatform(clientDevice.getPlatform());
                appConfig.setVersion(clientDevice.getAppVersion());
                appConfig.setChannel(clientDevice.getAppChannel());
                appConfig.setEnterpriseType(clientDevice.getEnterpriserType());
                if (authApp != null) {
                    if (AppPlatform.IOS.equals(clientDevice.getPlatform())) {
                        appConfig.setAppSecret(authApp.getAppSecret().getIos());
                    } else if (AppPlatform.ANDROID.equals(clientDevice.getPlatform())) {
                        appConfig.setAppSecret(authApp.getAppSecret().getAndroid());
                    }
                }

                AppConfigInfo info = new AppConfigInfo();
                info.setShake_open("false");
                info.setDefad_url("");
                info.setGift_open("false");
                appConfig.setInfo(info);
                JoymeAppConfigServiceSngl.get().createAppConfig(appConfig);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
        }
    }

    private void processQueuedPostNotice(Notice notice) {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic Call handler to processQueuedPostNotice.notice:" + notice);
        }
        try {
            postNotice(notice.getOwnUno(), notice);
        } catch (ServiceException e) {
            GAlerter.lab("MessageLogic processQueuedPostNotice occurred Exception:", e);
        }
    }

    private void processQueuedEvent(Event event) {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic Call handler to processQueuedEvent:" + event);
        }

        //check the event type.
        if (event instanceof NoticeInsertEvent) {
            NoticeInsertEvent castEvent = (NoticeInsertEvent) event;
            Notice notice = new Notice();

            notice.setOwnUno(castEvent.getOwnUno());
            notice.setNoticeType(castEvent.getNoticeType());
            notice.setCount(castEvent.getCount());

            if (logger.isDebugEnabled() && castEvent.getNoticeType().equals(NoticeType.BILLING_COIN)) {
                logger.debug("MessageLogic Call handler to process NoticeInsertEvent eventType:" + NoticeType.BILLING_COIN.getCode());
            }

            try {
                writeAbleMessageHandler.postNotice(notice);
            } catch (Exception e) {
                //
                GAlerter.lan("MessageLogic processQueuedEvent error:", e);
            }
        } else if (event instanceof NoticeResetEvent) {
            NoticeResetEvent castEvent = (NoticeResetEvent) event;

            try {
                writeAbleMessageHandler.readNotice(castEvent.getOwnUno(), castEvent.getNoticeType());
            } catch (Exception e) {
                //
                GAlerter.lan("MessageLogic processQueuedEvent error:", e);
            }
        } else if (event instanceof ProfileMobileDeviceEvent) {
//            ProfileMobileDeviceEvent catEvent = (ProfileMobileDeviceEvent) event;
//            QueryExpress queryExpress = new QueryExpress();
//            queryExpress.add(QueryCriterions.eq(ClientDeviceField.UNO, catEvent.getUno()));
//            queryExpress.add(QueryCriterions.eq(ClientDeviceField.PLATFORM, catEvent.getPlatform()));
//            queryExpress.add(QueryCriterions.eq(ClientDeviceField.CLIENT_ID, catEvent.getClientId()));
//            queryExpress.add(QueryCriterions.eq(ClientDeviceField.CLIENT_TOKEN, catEvent.getClientToken()));
//            queryExpress.add(QueryCriterions.eq(ClientDeviceField.INFO_ID, catEvent.getAppId()));
//            ClientDevice mobileDevice = new ClientDevice();
//            mobileDevice.setAppId(catEvent.getAppId());
//            mobileDevice.setUno(catEvent.getUno());
//            mobileDevice.setPlatform(AppPlatform.getByCode(catEvent.getPlatform()));
//            mobileDevice.setClientId(catEvent.getClientId());
//            mobileDevice.setClientToken(catEvent.getClientToken());
//            try {
//                createClientDevice(mobileDevice);
//            } catch (ServiceException e) {
//                logger.info("MessageLogic insertProfileClientMobileDevice error.", e);
//
//                QueryExpress queryExpress = new QueryExpress();
//                queryExpress.add(QueryCriterions.eq(ClientDeviceField.CLIENT_ID, catEvent.getClientId()));
//                queryExpress.add(QueryCriterions.eq(ClientDeviceField.CLIENT_TOKEN, catEvent.getClientToken()));
////                queryExpress.add(QueryCriterions.eq(ClientDeviceField.INFO_ID, catEvent.getAppId()));
//
//                UpdateExpress updateExpress = new UpdateExpress();
//                updateExpress.set(ClientDeviceField.UNO, catEvent.getUno());
//                try {
//                    writeAbleMessageHandler.modifyClientDevice(queryExpress, updateExpress);
//                } catch (DbException e1) {
//                    GAlerter.lab("MessageLogic modifyClientDevice occur Exception.e", e);
//                }
//            }

        } else if (event instanceof SocialMessageEvent) {
            SocialMessageEvent catEvent = (SocialMessageEvent) event;
//            try {
//                SocialMsgCatchEventFactory.get().factory(SocialMessageType.getByCode(catEvent.getType())).catchEvent(catEvent, writeAbleMessageHandler, readonlyMessageHandlersPool.getHandler(), socialMessageCache);
//            } catch (DbConnException e) {
//                GAlerter.lab(this.getClass().getName() + " getNotice occur exception.e", e);
//            }
            try {
                if (catEvent.getType() == SocialMessageType.HOT.getCode()) {
                    SocialNotice receiveNotice = getSocialNotice(catEvent.getOwnUno());
                    int sum = 0;
                    if (receiveNotice == null) {
                        sum = 1;
                        receiveNotice = new SocialNotice();
                        receiveNotice.setOwnUno(catEvent.getOwnUno());
                        receiveNotice.setDescription(catEvent.getMsgBody());
                        receiveNotice.setHotCount(1);
                        receiveNotice.setAgreeCount(0);
                        receiveNotice.setNoticeCount(0);
                        receiveNotice.setFocusCount(0);
                        receiveNotice.setReplyCount(0);
                        receiveNotice.setRemoveStatus(ActStatus.UNACT);
                        receiveNotice.setCreateDate(new Date());
                        receiveNotice.setReadHotDate(new Date());
                        writeAbleMessageHandler.insertSocialNotice(receiveNotice);
                    } else {
                        sum = receiveNotice.getHotCount() + 1;
                        UpdateExpress updateExpress = new UpdateExpress();
                        updateExpress.increase(SocialNoticeField.HOT_COUNT, 1);
                        if (receiveNotice.getReadHotDate() == null) {
                            updateExpress.set(SocialNoticeField.READ_HOT_DATE, new Date());
                        }
                        writeAbleMessageHandler.updateSocialNotice(catEvent.getOwnUno(), updateExpress);
                        socialMessageCache.removeSocialNotice(catEvent.getOwnUno());
                    }
                    SocialProfile profile = ProfileServiceSngl.get().getSocialProfileByUno(catEvent.getOwnUno());
                    String sound = "nosound.caf";
                    if (profile != null && profile.getSetting() != null && AllowType.A_ALLOW.equals(profile.getSetting().getAllowSoundSocial())) {
                        sound = "default";
                    }

                    String body = "您有1篇文章被推荐到了咔哒热门频道，快来看看吧";
                    if (sum > 0) {
                        body = "您有" + sum + "篇文章被推荐到了咔哒热门频道，快来看看吧";
                    }

                    long lastTime = receiveNotice.getReadHotDate() == null ? 0l : receiveNotice.getReadHotDate().getTime();
                    long nowTime = System.currentTimeMillis();
                    //todo
                    if (getIntervalHour(lastTime, nowTime) >= 10) {
                        sendSocialMessage(catEvent.getOwnUno(), body, catEvent.getType(), sound);
                        UpdateExpress updateExpress = new UpdateExpress();
                        updateExpress.set(SocialNoticeField.READ_HOT_DATE, new Date());
                        writeAbleMessageHandler.updateSocialNotice(catEvent.getOwnUno(), updateExpress);
                    }
                } else if (catEvent.getType() == SocialMessageType.FOCUS.getCode()) {
                    SocialNotice receiveNotice = getSocialNotice(catEvent.getOwnUno());
                    if (receiveNotice == null) {
                        receiveNotice = new SocialNotice();
                        receiveNotice.setOwnUno(catEvent.getOwnUno());
                        receiveNotice.setDescription(catEvent.getMsgBody());
                        receiveNotice.setFocusCount(1);
                        receiveNotice.setHotCount(0);
                        receiveNotice.setAgreeCount(0);
                        receiveNotice.setNoticeCount(0);
                        receiveNotice.setReplyCount(0);
                        receiveNotice.setRemoveStatus(ActStatus.UNACT);
                        receiveNotice.setCreateDate(new Date());
                        receiveNotice.setReadFocusDate(new Date());
                        writeAbleMessageHandler.insertSocialNotice(receiveNotice);
                    } else {
                        UpdateExpress updateExpress = new UpdateExpress();
                        updateExpress.increase(SocialNoticeField.FOCUS_COUNT, 1);
                        if (receiveNotice.getReadFocusDate() == null) {
                            updateExpress.set(SocialNoticeField.READ_FOCUS_DATE, new Date());
                        }
                        writeAbleMessageHandler.updateSocialNotice(catEvent.getOwnUno(), updateExpress);
                        socialMessageCache.removeSocialNotice(catEvent.getOwnUno());
                    }

                    SocialProfile profile = ProfileServiceSngl.get().getSocialProfileByUno(catEvent.getOwnUno());

                    SocialProfile sendProfile = ProfileServiceSngl.get().getSocialProfileByUno(catEvent.getSendUno());
                    if (profile != null && profile.getSetting() != null && AllowType.A_ALLOW.equals(profile.getSetting().getAllowFocusSocial())) {
                        int sum = receiveNotice.getFocusCount() + 1;
                        String body = "您有" + sum + "个新粉丝~";
                        if (sendProfile != null && sendProfile.getBlog() != null) {
                            if (sum == 1) {
                                body = sendProfile.getBlog().getScreenName() + "成为了您的粉丝";
                            } else {
                                body = sendProfile.getBlog().getScreenName() + "等" + sum + "人成为了您的粉丝";
                            }
                        }
                        String sound = "nosound.caf";
                        if (AllowType.A_ALLOW.equals(profile.getSetting().getAllowSoundSocial())) {
                            sound = "default";
                        }
                        long lastTime = receiveNotice.getReadFocusDate() == null ? 0l : receiveNotice.getReadFocusDate().getTime();
                        long nowTime = System.currentTimeMillis();
                        //todo
                        if (getIntervalHour(lastTime, nowTime) >= 10) {
                            sendSocialMessage(catEvent.getOwnUno(), body, catEvent.getType(), sound);
                            UpdateExpress updateExpress = new UpdateExpress();
                            updateExpress.set(SocialNoticeField.READ_FOCUS_DATE, new Date());
                            writeAbleMessageHandler.updateSocialNotice(catEvent.getOwnUno(), updateExpress);
                        }
                    }

                    //TODO
                } else if (catEvent.getType() == SocialMessageType.WANBA_MESSAGE_LIST.getCode()) {//着迷玩霸2.2.0消息中心列表
                    String body = catEvent.getMsgBody();
                    SocialMessage socialMessage = new SocialMessage();
                    socialMessage.setMsgBody(catEvent.getRootUno());
                    socialMessage.setMsgType(SocialMessageType.getByCode(catEvent.getType()));
                    socialMessage.setMsgCategory(SocialMessageCategory.DEFAULT_MSG);
                    socialMessage.setOwnUno(catEvent.getOwnUno());
                    socialMessage.setSendUno(catEvent.getSendUno());
                    socialMessage.setReceiveUno(catEvent.getReceiveUno());

                    socialMessage.setReplyId(catEvent.getReplyId());
                    socialMessage.setReplyUno(catEvent.getReplyUno());
                    socialMessage.setContentId(catEvent.getContentId());
                    socialMessage.setContentUno(catEvent.getContentUno());
                    socialMessage.setParentId(catEvent.getParentId());
                    socialMessage.setParentUno(catEvent.getParentUno());
                    socialMessage.setRootId(catEvent.getRootId());
                    //socialMessage.setRootUno(catEvent.getRootUno());

                    socialMessage.setCreateDate(catEvent.getCreateDate());
                    socialMessage.setRemoveStatus(ActStatus.UNACT);


                    socialMessage = writeAbleMessageHandler.insertSocialMessage(socialMessage);
                    //GAlerter.lab("test messagetest message,SocialMessage:" + socialMessage.toString());
                    if (socialMessage.getMsgId() <= 0) {
                        return;
                    }
                    messageRedis.lpush(SocialConstants.SERVICE_PREFIX + catEvent.getOwnUno(), String.valueOf(socialMessage.getMsgId()));
                    socialMessageCache.putSocialMessage(catEvent.getOwnUno(), socialMessage.getMsgId(), socialMessage);
                    // String lizmuno = "4ab52664-2968-4e67-bc37-0187490fe763";
                    //推送
                    com.enjoyf.platform.service.usercenter.Profile profileSend = UserCenterServiceSngl.get().getProfileByProfileId(catEvent.getOwnUno());
                    if (profileSend == null) {
                        return;
                    }
                    QueryExpress qu = new QueryExpress();
                    qu.add(QueryCriterions.eq(ClientDeviceField.UNO, profileSend.getUno()));
                    //qu.add(QueryCriterions.eq(ClientDeviceField.CLIENT_TOKEN, catEvent.getParentUno()));
                    ClientDevice device = MessageServiceSngl.get().getClientDevice(qu);
                    // GAlerter.lab("test message,SocialMessage:" + socialMessage.toString());


                    if (device != null) {
                        GAlerter.lan("test message,ClientDevice:" + device.toString());
                        com.enjoyf.platform.service.usercenter.Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(socialMessage.getSendUno());
                        if (profile == null) {
                            return;
                        }

                        //  Set<String> deviceSet = new HashSet<String>();
                        // deviceSet.add(device.getClientToken());
                        String sound = "nosound.caf";
                        // sendSocialMessage(catEvent.getOwnUno(), body, catEvent.getType(), sound);

                        AppPushEvent appPushEvent = new AppPushEvent();
                        appPushEvent.setPushMsgId(socialMessage.getMsgId());
                        appPushEvent.setDevices(device.getClientToken());
                        appPushEvent.setAppKey(device.getAppId());     //25AQWaK997Po2x300CQeP0   17yfn24TFexGybOF0PqjdY
                        appPushEvent.setPlatform(device.getPlatform());
                        appPushEvent.setEnterpriseType(device.getEnterpriserType());
                        appPushEvent.setIcon("");
                        appPushEvent.setSubject("您收到一条新的评论");
                        if (body.equals("我发布的图片主题")) {
                            appPushEvent.setContext(profile.getNick() + "评论了您的图片主题");
                        } else {
                            appPushEvent.setContext(profile.getNick() + "评论了您的主题《" + body + "》");
                        }

                        appPushEvent.setChannel(device.getAppChannel());
                        appPushEvent.setVersion(device.getAppVersion());
                        appPushEvent.setTags("");
                        appPushEvent.setSound(sound);
                        appPushEvent.setBadge(0);
                        appPushEvent.setProfileIds(device.getProfileId());
                        //appPushEvent.setSendDate(new Date());
                        appPushEvent.setJt(AppRedirectType.REDIRECT_WANBA_MESSAGELIST.getCode());
                        appPushEvent.setJi("");
                        appPushEvent.setPushListType(PushListType.PUSH_MESSAGE);

                        GAlerter.lab("send wanba message" + appPushEvent.toString());
                        appPushFactorySendPushMessage(appPushEvent);

                    }

                } else {
                    SocialMessage socialMessage = new SocialMessage();
                    socialMessage.setMsgBody(catEvent.getMsgBody());
                    socialMessage.setMsgType(SocialMessageType.getByCode(catEvent.getType()));
                    socialMessage.setMsgCategory(SocialMessageCategory.DEFAULT_MSG);

                    socialMessage.setOwnUno(catEvent.getOwnUno());
                    socialMessage.setSendUno(catEvent.getSendUno());
                    socialMessage.setReceiveUno(catEvent.getReceiveUno());

                    socialMessage.setReplyId(catEvent.getReplyId());
                    socialMessage.setReplyUno(catEvent.getReplyUno());
                    socialMessage.setContentId(catEvent.getContentId());
                    socialMessage.setContentUno(catEvent.getContentUno());
                    socialMessage.setParentId(catEvent.getParentId());
                    socialMessage.setParentUno(catEvent.getParentUno());
                    socialMessage.setRootId(catEvent.getRootId());
                    socialMessage.setRootUno(catEvent.getRootUno());

                    socialMessage.setCreateDate(catEvent.getCreateDate());
                    socialMessage.setRemoveStatus(ActStatus.UNACT);
                    writeAbleMessageHandler.insertSocialMessage(socialMessage);

                    Map<ObjectField, Object> paramMap = new HashMap<ObjectField, Object>();
                    if (catEvent.getType() == SocialMessageType.AGREE.getCode()) {
                        paramMap.put(ProfileSumField.SOCIALAGREEMSGSUM, 1);
                    } else if (catEvent.getType() == SocialMessageType.REPLY.getCode()) {
                        paramMap.put(ProfileSumField.SOCIALREPLYMSGSUM, 1);
                    }
                    ProfileServiceSngl.get().increaseProfileSum(catEvent.getOwnUno(), paramMap);

                    SocialProfile profile = ProfileServiceSngl.get().getSocialProfileByUno(catEvent.getOwnUno());

                    SocialProfile sendProfile = ProfileServiceSngl.get().getSocialProfileByUno(catEvent.getSendUno());

                    int totals = 0;
                    int agreeCount = 0;
                    int replyCount = 0;
                    if (catEvent.getType() == SocialMessageType.AGREE.getCode()) {
                        totals = profile.getSum().getSocialAgreeMsgSum();
                        agreeCount = 1;
                    } else if (catEvent.getType() == SocialMessageType.REPLY.getCode()) {
                        totals = profile.getSum().getSocialReplyMsgSum();
                        replyCount = 1;
                    }
                    socialMessageCache.removeSocialMessageIdList(catEvent.getOwnUno(), catEvent.getType(), totals);

                    SocialNotice receiveNotice = getSocialNotice(catEvent.getOwnUno());
                    if (receiveNotice == null) {
                        receiveNotice = new SocialNotice();
                        receiveNotice.setOwnUno(catEvent.getOwnUno());
                        receiveNotice.setDescription(catEvent.getMsgBody());
                        receiveNotice.setAgreeCount(agreeCount);
                        receiveNotice.setReplyCount(replyCount);
                        receiveNotice.setHotCount(0);
                        receiveNotice.setFocusCount(0);
                        receiveNotice.setNoticeCount(0);
                        receiveNotice.setRemoveStatus(ActStatus.UNACT);
                        receiveNotice.setCreateDate(new Date());
                        if (catEvent.getType() == SocialMessageType.AGREE.getCode()) {
                            receiveNotice.setReadAgreeDate(new Date());
                        }
                        if (catEvent.getType() == SocialMessageType.REPLY.getCode()) {
                            receiveNotice.setReadReplyDate(new Date());
                        }
                        writeAbleMessageHandler.insertSocialNotice(receiveNotice);
                    } else {
                        UpdateExpress updateExpress = new UpdateExpress();
                        updateExpress.increase(SocialNoticeField.AGREE_COUNT, agreeCount);
                        updateExpress.increase(SocialNoticeField.REPLY_COUNT, replyCount);
                        if (catEvent.getType() == SocialMessageType.AGREE.getCode() && receiveNotice.getReadAgreeDate() == null) {
                            updateExpress.set(SocialNoticeField.READ_AGREE_DATE, new Date());
                        }
                        if (catEvent.getType() == SocialMessageType.REPLY.getCode() && receiveNotice.getReadReplyDate() == null) {
                            updateExpress.set(SocialNoticeField.READ_REPLY_DATE, new Date());
                        }
                        writeAbleMessageHandler.updateSocialNotice(catEvent.getOwnUno(), updateExpress);
                        socialMessageCache.removeSocialNotice(catEvent.getOwnUno());
                    }

                    if (catEvent.getType() == SocialMessageType.AGREE.getCode() && profile != null && profile.getSetting() != null && AllowType.A_ALLOW.equals(profile.getSetting().getAllowAgreeSocial())) {
                        long lastTime = 0l;
                        int sum = receiveNotice.getAgreeCount() + agreeCount;
                        String body = "您有" + sum + "条新赞~";
                        if (sendProfile != null && sendProfile.getBlog() != null) {
                            if (sum == 1) {
                                body = sendProfile.getBlog().getScreenName() + "赞了您的文章";
                            } else {
                                body = sendProfile.getBlog().getScreenName() + "等" + sum + "人赞了您的文章";
                            }

                        }

                        String sound = "nosound.caf";
                        if (AllowType.A_ALLOW.equals(profile.getSetting().getAllowSoundSocial())) {
                            sound = "default";
                        }
                        lastTime = receiveNotice.getReadAgreeDate() == null ? 0l : receiveNotice.getReadAgreeDate().getTime();
                        long nowTime = System.currentTimeMillis();
                        //todo
                        if (getIntervalHour(lastTime, nowTime) >= 10) {
                            sendSocialMessage(catEvent.getOwnUno(), body, catEvent.getType(), sound);
                            UpdateExpress updateExpress = new UpdateExpress();
                            updateExpress.set(SocialNoticeField.READ_AGREE_DATE, new Date());
                            writeAbleMessageHandler.updateSocialNotice(catEvent.getOwnUno(), updateExpress);
                        }
                    } else if (catEvent.getType() == SocialMessageType.REPLY.getCode() && profile != null && profile.getSetting() != null && AllowType.A_ALLOW.equals(profile.getSetting().getAllowReplySocial())) {
                        long lastTime = 0l;
                        int sum = receiveNotice.getReplyCount() + replyCount;
                        String body = "您有" + sum + "条新评论~";
                        if (sendProfile != null && sendProfile.getBlog() != null) {
                            if (sum == 1) {
                                body = sendProfile.getBlog().getScreenName() + "评论了您的文章";
                            } else {
                                body = sendProfile.getBlog().getScreenName() + "等" + sum + "人评论了您的文章";
                            }
                        }
                        String sound = "nosound.caf";
                        if (AllowType.A_ALLOW.equals(profile.getSetting().getAllowSoundSocial())) {
                            sound = "default";
                        }
                        lastTime = receiveNotice.getReadReplyDate() == null ? 0l : receiveNotice.getReadReplyDate().getTime();
                        long nowTime = System.currentTimeMillis();
                        //todo
                        if (getIntervalHour(lastTime, nowTime) >= 10) {
                            sendSocialMessage(catEvent.getOwnUno(), body, catEvent.getType(), sound);
                            UpdateExpress updateExpress = new UpdateExpress();
                            updateExpress.set(SocialNoticeField.READ_REPLY_DATE, new Date());
                            writeAbleMessageHandler.updateSocialNotice(catEvent.getOwnUno(), updateExpress);
                        }
                    }
                }
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " getNotice occur exception.e", e);
            }
        } else if (event instanceof AppPushEvent) {
            AppPushEvent appPushEvent = (AppPushEvent) event;
            try {
                String devicesToken = "";
                //普通 推送
                if (PushListType.PUSH_MESSAGE.equals(appPushEvent.getPushListType())) {
                    Thread sendByProfileThread = new AppPushByProfileThread(appPushEvent);
                    sendByProfileThread.start();
                    //关注游戏 推送
                } else if (PushListType.PUSH_GAME_MESSAGE.equals(appPushEvent.getPushListType())) {
                    com.enjoyf.platform.service.joymeapp.PushMessage pushMessage = modifyOrCreatePushMessage(appPushEvent);
                    logger.info("-----------------pushMessageQueueThreadN acting msgid:" + pushMessage.getPushMsgId() + "---------------------");
                    appPushFactorySendPushMessage(appPushEvent);
                    //礼包预定 推送
                } else if (PushListType.PUSH_GIFT_RESERVE_MESSAGE.equals(appPushEvent.getPushListType())) {
                    Thread sendByGiftThread = new AppPushByGiftThread(appPushEvent);
                    sendByGiftThread.start();
                }
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            }
        } else if (event instanceof WanbaQuestionNoticeEvent) {
            processWanbaQuestionNoticeEvent((WanbaQuestionNoticeEvent) event);
        } else {
            logger.info("MessageLogic discard the unknown event:" + event);
        }
    }

    private void appPushFactorySendPushMessage(AppPushEvent appPushEvent) {
        try {
            //发送
            int umengPushChannel = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getUmengProductionMode();
            String appKeyPlatform = appPushEvent.getAppKey();
            int platform = 0;
            if (appPushEvent.getPlatform().getCode() == 0 && appPushEvent.getEnterpriseType().equals(AppEnterpriserType.DEFAULT)) {
                appKeyPlatform += "I";
                platform = 0;
            } else if (appPushEvent.getPlatform().getCode() == 1 && appPushEvent.getEnterpriseType().equals(AppEnterpriserType.DEFAULT)) {
                appKeyPlatform += "A";
                platform = 1;
            } else if (appPushEvent.getPlatform().getCode() == 0 && appPushEvent.getEnterpriseType().equals(AppEnterpriserType.ENTERPRISE)) {
                appKeyPlatform += "E";
                platform = 0;
            }

            GAlerter.lab("appPushFactorySendPushMessage-->" + umengPushChannel + ",platform-->" + platform + ",appPushEvent-->" + appPushEvent.toString());
            AppPushFactory.get().factory(AppPushChannel.getByCode(umengPushChannel), AppPlatform.getByCode(platform)).sendPushMessage(appPushEvent);

//            List<String> umengVersionList = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getPushVersionList(appKeyPlatform, umengPushChannel);
//            //全版本推
//            if (StringUtil.isEmpty(appPushEvent.getVersion())) {
//                AppPushFactory.get().factory(AppPushChannel.getByCode(umengPushChannel), AppPlatform.getByCode(platform)).sendPushMessage(appPushEvent);
//            } else {
//                //指定版本推
//                String[] versionList = appPushEvent.getVersion().split("\\|");
//                String umengVersion = "";
//                for (String version : versionList) {
//                    if (umengVersionList.contains(version)) {
//                        umengVersion += (version + "|");
//                    }
//                }
//                logger.info("-----------------pushMessageQueueThreadN umeng push msgid:" + appPushEvent.getPushMsgId() + " " + umengVersion + "---------------------");
//                appPushEvent.setVersion(umengVersion);
//                AppPushFactory.get().factory(AppPushChannel.getByCode(umengPushChannel), AppPlatform.getByCode(platform)).sendPushMessage(appPushEvent);
//            }
        } catch (Exception e) {
        }
    }

    private com.enjoyf.platform.service.joymeapp.PushMessage modifyOrCreatePushMessage(AppPushEvent appPushEvent) {
        try {
            com.enjoyf.platform.service.joymeapp.PushMessage pushMessage = getPushMessage(new QueryExpress().add(QueryCriterions.eq(com.enjoyf.platform.service.joymeapp.PushMessageField.PUSHMSGID, appPushEvent.getPushMsgId())));
            if (pushMessage == null) {
                pushMessage = new com.enjoyf.platform.service.joymeapp.PushMessage();
                pushMessage.setAppKey(appPushEvent.getAppKey());
                pushMessage.setAppPlatform(appPushEvent.getPlatform());
                pushMessage.setEnterpriseType(appPushEvent.getEnterpriseType());
                pushMessage.setCreateDate(new Date());
                pushMessage.setCreateUserid("");
                pushMessage.setMsgIcon(appPushEvent.getIcon());
                pushMessage.setMsgSubject(appPushEvent.getSubject());
                pushMessage.setPushStatus(ActStatus.UNACT);
                pushMessage.setShortMessage(appPushEvent.getContext());
                pushMessage.setPushListType(appPushEvent.getPushListType());
                pushMessage.setAppChannel(appPushEvent.getChannel());
                pushMessage.setAppVersion(appPushEvent.getVersion());
                pushMessage.setTags(appPushEvent.getTags());
                pushMessage.setSound(appPushEvent.getSound());
                pushMessage.setBadge(appPushEvent.getBadge());
                pushMessage.setSendDate(appPushEvent.getSendDate());
                pushMessage.setSendStatus(ActStatus.ACTING);
                PushMessageOptions options2 = new PushMessageOptions();
                PushMessageOption pushMessageOption2 = new PushMessageOption();
                pushMessageOption2.setInfo(appPushEvent.getJi());
                pushMessageOption2.setType(appPushEvent.getJt());
                options2.getList().add(pushMessageOption2);
                options2.setTemplate(0);
                pushMessage.setOptions(options2);
                pushMessage = ceatePushMessage(pushMessage);
            } else if (pushMessage.getSendStatus().equals(ActStatus.REJECTED)) {
                //发送中状态z
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(com.enjoyf.platform.service.joymeapp.PushMessageField.SEND_STATUS, ActStatus.ACTING.getCode());
                modifyPushMessage(updateExpress, new QueryExpress().add(QueryCriterions.eq(com.enjoyf.platform.service.joymeapp.PushMessageField.PUSHMSGID, pushMessage.getPushMsgId())), pushMessage.getAppKey());
            }
            return pushMessage;
        } catch (Exception e) {
            GAlerter.lab("MessageLogic process AppPushEvent modifyOrCreatePushMessage occur Exception.e:", e);
        }
        return null;
    }

    private void sendSocialMessage(String ownUno, String body, int type, String sound) {
        GAlerter.lab("-----------------------------------------START SEND MESSAGE FACTORY------------------------------------------------");
        List<ClientDevice> deviceList = null;
        try {
            deviceList = readonlyMessageHandlersPool.getHandler().queryClientDevice(new QueryExpress().add(QueryCriterions.eq(ClientDeviceField.UNO, ownUno)).add(QueryCriterions.eq(ClientDeviceField.PLATFORM, 0)));
            if (CollectionUtil.isEmpty(deviceList)) {
                GAlerter.lab("-----------------------------------------DEVICE EMPTY " + ownUno + "------------------------------------------------");
                return;
            }
        } catch (DbException e) {
            GAlerter.lab(this.getClass().getName() + " queryClientDevice occur exception.e", e);
        }
        Set<String> deviceSet = new HashSet<String>();
        for (ClientDevice device : deviceList) {
            deviceSet.add(device.getClientToken());
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("deviceset", deviceSet);
        paramMap.put("body", body);
        paramMap.put("msgtype", type);
        paramMap.put("sound", sound);
        paramMap.put("info", ownUno);
        GAlerter.lab("-----------------------------------------START PROCESS MESSAGE FACTORY------------------------------------------------");
        MessageProcessFactory.get().factory(new SocialMessage()).processSendMessage(paramMap);
    }

    private void processQueuedPushMessage(PushMsg msg) {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic Call handler to processQueuedPushMessage:" + msg);
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pushmsg", msg);
        boolean isSendMsg = MessageProcessFactory.get().factory(new PushMsg()).processSendMessage(paramMap);

        UpdateExpress updateExpress = new UpdateExpress();
        if (isSendMsg) {
            updateExpress.set(MessageField.READSTATUS, ActStatus.ACTED.getCode());
        } else {
            updateExpress.set(MessageField.READSTATUS, ActStatus.UNACT.getCode());
        }

        try {

            // update message
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(MessageField.OWNUNO, msg.getMessage().getOwnUno()));
            queryExpress.add(QueryCriterions.eq(MessageField.READSTATUS, msg.getMessage().getReadStatus().getCode()));
            int i = writeAbleMessageHandler.updateMessage(msg.getMessage().getOwnUno(), updateExpress, queryExpress);
            logger.debug("update message num: " + i);

        } catch (ServiceException e) {
            GAlerter.lab("MessageLogic Call handler to processQueuedPushMessage error.", e);
        }


    }

    @Override
    public void postMessage(String ownUno, Message message) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call pushMsgProcessQueueThreadN to postMessage, message is " + message);
        }

        pushMsgProcessQueueThreadN.add(message);
    }

    private Message processPostMessage(Message message) {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call writeAbleMessageHandler to postMessage, message is " + message);
        }

        //
        try {
            message = writeAbleMessageHandler.postMessage(message);

            /////////////////////////////////////////////
            //send out event.
            if (message != null) {
                sendOutPostMessageEvent(message);
            }

        } catch (Exception e) {
            GAlerter.lab("insert message occured ServiceException.e: ", e);
        }

        return message;
    }

    @Override
    public Message getMessage(String ownUno, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call readonlyMessageHandlersPool to getMessage, ownUno is " + ownUno);
        }

        return readonlyMessageHandlersPool.getHandler().getMessage(ownUno, queryExpress);
    }

    @Override
    public PageRows<Message> queryMessage(String ownUno, QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call readonlyMessageHandlersPool to queryMessage, ownUno is " + ownUno);
        }

        return readonlyMessageHandlersPool.getHandler().queryMessage(ownUno, queryExpress, page);
    }

    @Override
    public RangeRows<Message> queryMessage(String ownUno, QueryExpress queryExpress, Rangination rangination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call readonlyMessageHandlersPool to queryMessage, ownUno is " + ownUno);
        }

        return readonlyMessageHandlersPool.getHandler().queryMessage(ownUno, queryExpress, rangination);
    }

    @Override
    public boolean updateMessage(String ownUno, UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call readonlyMessageHandlersPool to updateMessage, ownUno is " + ownUno);
        }

        return writeAbleMessageHandler.updateMessage(ownUno, updateExpress, queryExpress) > 0;
    }

    private void sendOutPostMessageEvent(Message message) {
        //send out the notice
        NoticeInsertEvent notice = new NoticeInsertEvent();

        //
        notice.setCount(1);
        notice.setOwnUno(message.getRecieverUno());

        if (message.getMsgType().equals(MessageType.PRIVATE)) {
            notice.setNoticeType(NoticeType.NEW_MESSAGE);
        } else {
            notice.setNoticeType(NoticeType.NEW_BULLETIN);
        }

        try {
            EventDispatchServiceSngl.get().dispatch(notice);
        } catch (Exception e) {
            //
            GAlerter.lab("MessageLogic postMessage dispatch event error.", e);
        }
    }

    @Override
    public PageRows<MessageTopic> queryMessageTopics(String ownUno, MessageType type, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call readonlyMessageHandlersPool to queryMessageTopics, ownUno is " + ownUno);
        }

        return readonlyMessageHandlersPool.getHandler().queryMessageTopics(ownUno, type, page);
    }

    @Override
    public PageRows<MessageTopic> querySenderTopics(String ownUno, MessageType type, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call readonlyMessageHandlersPool to queryMessageTopics, ownUno is " + ownUno);
        }

        return readonlyMessageHandlersPool.getHandler().querySenderTopics(ownUno, type, page);
    }

    @Override
    public PageRows<Message> queryMessagesBySender(String ownUno, String senderUno, MessageType type, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call readonlyMessageHandlersPool to queryMessagesByTopic, ownUno is " + ownUno + ", senderUno is " + senderUno);
        }

        return readonlyMessageHandlersPool.getHandler().queryMessages(ownUno, senderUno, type, page);
    }

    @Override
    public PageRows<Message> queryMessagesByPmd(String ownUno, String senderUno, MessageType type, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call readonlyMessageHandlersPool to queryMessagesByPmd, ownUno is " + ownUno + ", senderUno is " + senderUno);
        }

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ProfileMobileDeviceField.MDSERIAL, ownUno));
        ProfileMobileDevice pmd = ProfileServiceSngl.get().getProfileMobileDevice(queryExpress);

        if (pmd == null) {
            GAlerter.lab("queryMessagesBySender error by ProfileMobileDevice is null .");
            return new PageRows<Message>();
        }


        QueryExpress mgsQueryExpress = new QueryExpress();
        mgsQueryExpress.add(QueryCriterions.eq(MessageField.OWNUNO, pmd.getPmdId()));
        mgsQueryExpress.add(QueryCriterions.eq(MessageField.SENDERUNO, senderUno));
        mgsQueryExpress.add(QueryCriterions.eq(MessageField.MSGTYPE, type.getCode()));
        mgsQueryExpress.add(QueryCriterions.eq(MessageField.READSTATUS, ActStatus.ACTING));

        PageRows<Message> p = readonlyMessageHandlersPool.getHandler().queryMessage(pmd.getPmdId() + "", mgsQueryExpress, page);


        // update  一次查询量不会太多。查完之后，全部更新。
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(MessageField.READSTATUS, ActStatus.ACTED);
        writeAbleMessageHandler.updateMessage(pmd.getPmdId() + "", updateExpress, mgsQueryExpress);

        return p;
    }

    @Override
    public PageRows<Message> queryMessagesByTopic(String ownUno, Long topicId, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call readonlyMessageHandlersPool to queryMessageTopics, ownUno is " + ownUno + ", topicId is " + topicId);
        }

        return readonlyMessageHandlersPool.getHandler().queryMessages(ownUno, topicId, page);
    }

    @Override
    public boolean removeMessage(String ownUno, Long msgId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call writeAbleMessageHandler to removeMessage, ownUno is " + ownUno + ", msgId is " + msgId);
        }

        return writeAbleMessageHandler.removeMessage(ownUno, msgId);
    }

    @Override
    public boolean removeTopicMessages(String ownUno, Long topicId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call writeAbleMessageHandler to removeTopicMessages, ownUno is " + ownUno + ", topicId is " + topicId);
        }

        return writeAbleMessageHandler.removeTopicMessages(ownUno, topicId);
    }

    @Override
    public boolean removeSenderMessages(String ownUno, String senderUno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call writeAbleMessageHandler to removeSenderMessages, ownUno is " + ownUno + ", senderUno is " + senderUno);
        }

        return writeAbleMessageHandler.removeSenderMessages(ownUno, senderUno);
    }

    @Override
    public Notice postNotice(String ownUno, Notice notice) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call writeAbleMessageHandler to postNotice, ownUno is " + ownUno + ", notice is " + notice);
        }

        return writeAbleMessageHandler.postNotice(notice);
    }

    @Override
    public List<Notice> queryNotices(String ownUno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call readonlyMessageHandlersPool to queryNotices, ownUno is " + ownUno);
        }

        return readonlyMessageHandlersPool.getHandler().queryNotices(ownUno);
    }

    @Override
    public Notice getNotice(String ownUno, NoticeType type) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call readonlyMessageHandlersPool to getNotice, ownUno is " + ownUno);
        }

        return readonlyMessageHandlersPool.getHandler().getNotice(ownUno, type);
    }

    @Override
    public boolean readNoticeAll(String ownUno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call writeAbleMessageHandler to readNoticeByType, ownUno is " + ownUno);
        }
        writeAbleMessageHandler.readNotice(ownUno, NoticeType.BILLING_COIN);
        return writeAbleMessageHandler.readNotice(ownUno);
    }

    @Override
    public boolean readNoticeAllExcludeNC(String ownUno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call writeAbleMessageHandler to readNoticeAllExcludeNC, ownUno is " + ownUno);
        }
        writeAbleMessageHandler.readNotice(ownUno, NoticeType.BILLING_COIN);
        return writeAbleMessageHandler.readNoticeAllExcludeNC(ownUno);
    }

    @Override
    public boolean readNoticeByType(String ownUno, NoticeType type) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call writeAbleMessageHandler to readNoticeByType, ownUno is " + ownUno + ", type is " + type);
        }
        boolean bool = writeAbleMessageHandler.readNotice(ownUno, type);
        if (bool) {
            noticeCache.deleteNoticeCache(ownUno, type.getCode());
        }
        return bool;
    }

    @Override
    public boolean updateNoticeKeyValues(String ownUno, NoticeType type, Map<ObjectField, Object> keyValues) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call updateNotice, ownUno is " + ownUno + ", type is " + type.getCode());
        }

        return writeAbleMessageHandler.updateNoticeKeyValues(ownUno, type, keyValues);
    }

    @Override
    public PushMessage create(PushMessage entry) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic create PushMessage .");
        }

        return writeAbleMessageHandler.insert(entry);
    }

    @Override
    public PushMessage get(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic get PushMessage . queryExpress is " + queryExpress);
        }

        return readonlyMessageHandlersPool.getHandler().get(queryExpress);
    }

    @Override
    public PageRows<PushMessage> query(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic query by page. queryExpress is " + queryExpress);
        }

        return readonlyMessageHandlersPool.getHandler().query(queryExpress, page);
    }

    @Override
    public RangeRows<PushMessage> query(QueryExpress queryExpress, Rangination range) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic query by range");
        }

        return readonlyMessageHandlersPool.getHandler().query(queryExpress, range);
    }

//    @Override
//    public int modifyPushMessage(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
//        if (logger.isDebugEnabled()) {
//            logger.debug("MessageLogic modify PushMessage queryExpress is " + queryExpress);
//        }
//
//        return writeAbleMessageHandler.updatePushMessage(updateExpress, queryExpress);
//    }
//
//    @Override
//    public boolean sendPushMessage(PushMessage pushMessage, boolean isTest) throws ServiceException {
//        if (logger.isDebugEnabled()) {
//            logger.debug("MessageLogic sendPushMessage . isTest = " + isTest);
//        }
//
//        return parsePushMessage(pushMessage, isTest);
//    }

    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("The event is recieved, event:" + event);
        }

        eventProcessQueueThreadN.add(event);

        return true;
    }

    @Override
    public void postNoticeEvent(Notice notice) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call postNoticeProcessQueueThreadN to postNoticeEvent, notice is " + notice);
        }
        eventProcessQueueThreadN.add(notice);
        Notice returnObj = noticeCache.getNoticeCache(notice.getOwnUno(), notice.getNoticeType().getCode());
        if (returnObj != null) {
            returnObj.setCount(returnObj.getCount() + 1);
            noticeCache.putNoticeCache(returnObj.getOwnUno(), returnObj.getNoticeType().getCode(), returnObj);
        }

    }

    @Override
    public Notice getNoticeByCache(String uno, NoticeType noticeType) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic call handler to getNoticeByCache, uno is " + uno + ",type is " + noticeType);
        }
        Notice returnObj = noticeCache.getNoticeCache(uno, noticeType.getCode());
        if (returnObj == null) {
            Notice notice = readonlyMessageHandlersPool.getHandler().getNotice(uno, noticeType);
            if (notice != null) {
                noticeCache.putNoticeCache(uno, noticeType.getCode(), notice);
                returnObj = notice;
            }
        }
        return returnObj;
    }

    @Override
    public ClientDevice createClientDevice(ClientDevice mobileDevice) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to createClientDevice:" + mobileDevice);
        }
        eventProcessQueueThreadN.add(mobileDevice);
        return null;
    }

    @Override
    public ClientDevice getClientDevice(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to getClientDevice:" + queryExpress);
        }
        return readonlyMessageHandlersPool.getHandler().getClientDevice(queryExpress);
    }

    @Override
    public PageRows<SocialMessage> querySocialMessageList(String ownUno, SocialMessageType socialMessageType, Pagination page, boolean desc) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to querySocialMessageList SocialMessageType:" + socialMessageType + ",ownUno:" + ownUno);
        }
        PageRows<SocialMessage> returnRows = new PageRows<SocialMessage>();
        List<SocialMessage> msgList = new ArrayList<SocialMessage>();

        PageRows<Long> msgIdRows = querySocialMessageIdRows(ownUno, socialMessageType, page, desc);
        if (msgIdRows == null || CollectionUtil.isEmpty(msgIdRows.getRows())) {
            returnRows.setRows(new ArrayList<SocialMessage>());
            returnRows.setPage(page);
            return returnRows;
        }

        for (Long msgId : msgIdRows.getRows()) {
            SocialMessage socialMessage = socialMessageCache.getSocialMessage(ownUno, msgId);
            if (socialMessage == null) {
                socialMessage = readonlyMessageHandlersPool.getHandler().getSocialMessage(ownUno, new QueryExpress().add(QueryCriterions.eq(SocialMessageField.MSG_ID, msgId)).add(QueryCriterions.eq(SocialMessageField.REMOVE_STATUS, ActStatus.UNACT.getCode())));
                if (socialMessage != null) {
                    socialMessageCache.putSocialMessage(socialMessage.getOwnUno(), msgId, socialMessage);
                    msgList.add(socialMessage);
                }
            } else {
                msgList.add(socialMessage);
            }
        }


        UpdateExpress updateExpress = new UpdateExpress();
        if (SocialMessageType.AGREE.equals(socialMessageType)) {
            updateExpress.set(SocialNoticeField.AGREE_COUNT, 0);
            updateExpress.set(SocialNoticeField.READ_AGREE_DATE, new Date());
        } else if (SocialMessageType.REPLY.equals(socialMessageType)) {
            updateExpress.set(SocialNoticeField.REPLY_COUNT, 0);
            updateExpress.set(SocialNoticeField.READ_REPLY_DATE, new Date());
        }
        writeAbleMessageHandler.updateSocialNotice(ownUno, updateExpress);


        returnRows.setPage(page);
        returnRows.setRows(msgList);
        return returnRows;
    }

    @Override
    public PageRows<SocialMessage> queryWanbaSocialMessageList(String ownUno, SocialMessageType socialMessageType, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to querySocialMessageList ownUno:" + ownUno + ",page:" + page);
        }
        PageRows<SocialMessage> returnValue = new PageRows<SocialMessage>();
        List<SocialMessage> rowLists = new ArrayList<SocialMessage>();
        List<String> list = messageRedis.lrange(SocialConstants.SERVICE_PREFIX + ownUno, page.getStartRowIdx(), page.getEndRowIdx());
        if (!CollectionUtil.isEmpty(list)) {
            page.setTotalRows(Long.valueOf(messageRedis.length(SocialConstants.SERVICE_PREFIX + ownUno)).intValue());
            for (String str : list) {
                SocialMessage socialMessage = socialMessageCache.getSocialMessage(ownUno, Long.valueOf(str));
                if (socialMessage == null) {
                    QueryExpress queryExpress = new QueryExpress();
                    queryExpress.add(QueryCriterions.eq(SocialMessageField.MSG_ID, Long.valueOf(str)));
                    socialMessage = readonlyMessageHandlersPool.getHandler().getSocialMessage(ownUno, queryExpress);
                }
                rowLists.add(socialMessage);
            }
        } else {
            PageRows<SocialMessage> dbRows = readonlyMessageHandlersPool.getHandler().querySocialMessage(ownUno, new QueryExpress()
                    .add(QueryCriterions.eq(SocialMessageField.OWN_UNO, ownUno))
                    .add(QueryCriterions.eq(SocialMessageField.MSG_TYPE, socialMessageType.getCode()))
                    .add(QueryCriterions.eq(SocialMessageField.REMOVE_STATUS, ActStatus.UNACT.getCode()))
                    .add(QuerySort.add(SocialMessageField.MSG_ID, QuerySortOrder.DESC)), page);
            return dbRows;
        }

        returnValue.setRows(rowLists);
        returnValue.setPage(page);
        return returnValue;
    }

    private PageRows<Long> querySocialMessageIdRows(String ownUno, SocialMessageType socialMessageType, Pagination page, boolean desc) throws ServiceException {
        PageRows<Long> pageRows = new PageRows<Long>();
        List<Long> msgList = new ArrayList<Long>();

        //get content by contentid get totalNums
        int fetchCacheTimes = page.getPageSize() / SOCIAL_MESSAGE_PAGE_SIZE;

        for (int times = 0; times < fetchCacheTimes; times++) {
            //得到分页的参数 第N页就从缓存取 总页数-N页*times+1,根据外面传入的pageSize可以得到需要从缓存取多少次
            Pagination pageByCache = new Pagination(page.getTotalRows(), 1, SOCIAL_MESSAGE_PAGE_SIZE);
            if (desc) {
                pageByCache.setCurPage(pageByCache.getMaxPage() - (page.getCurPage() - 1) * fetchCacheTimes - times);
            } else {
                pageByCache.setCurPage((page.getCurPage() - 1) * fetchCacheTimes + 1 + times);
            }

            //get by cache
            List<Long> msgIdList = socialMessageCache.getSocialMessageIdList(ownUno, socialMessageType.getCode(), pageByCache.getCurPage());

            //get by db
            if (CollectionUtil.isEmpty(msgIdList)) {
                PageRows<SocialMessage> dbRows = readonlyMessageHandlersPool.getHandler().querySocialMessage(ownUno, new QueryExpress()
                        .add(QueryCriterions.eq(SocialMessageField.OWN_UNO, ownUno))
                        .add(QueryCriterions.eq(SocialMessageField.MSG_TYPE, socialMessageType.getCode()))
                        .add(QueryCriterions.eq(SocialMessageField.REMOVE_STATUS, ActStatus.UNACT.getCode()))
                        .add(QuerySort.add(SocialMessageField.MSG_ID, QuerySortOrder.DESC)), pageByCache);
                List<SocialMessage> dbList = dbRows.getRows();
                msgIdList = new ArrayList<Long>();
                for (int i = dbList.size() - 1; i >= 0; i--) {
                    msgIdList.add(dbList.get(i).getMsgId());
                }
                socialMessageCache.putSocialMessageIdList(ownUno, socialMessageType.getCode(), pageByCache.getCurPage(), msgIdList);
            }
            msgList.addAll(msgIdList);

            if ((desc && pageByCache.getCurPage() <= 1) || (!desc && pageByCache.getCurPage() >= pageByCache.getMaxPage())) {
                break;
            }
        }

        Collections.sort(msgList, new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o1 < o2 ? 1 : (o1 == o2 ? 0 : -1);
            }
        });
        pageRows.setPage(page);
        pageRows.setRows(msgList);
        return pageRows;
    }

    @Override
    public SocialMessage getSocialMessage(String ownUno, SocialMessageType socialMessageType, SocialMessageCategory socialMessageCategory) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to getSocialMessage.ownUno:" + ownUno + ",socialMessageType:" + socialMessageType + ",socialMessageCategory:" + socialMessageCategory);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(SocialMessageField.OWN_UNO, ownUno));
        queryExpress.add(QueryCriterions.eq(SocialMessageField.MSG_TYPE, socialMessageType.getCode()));
        queryExpress.add(QueryCriterions.eq(SocialMessageField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
        queryExpress.add(QueryCriterions.eq(SocialMessageField.READ_STATUS, ActStatus.UNACT.getCode()));
        return readonlyMessageHandlersPool.getHandler().getSocialMessage(ownUno, queryExpress);
    }

    @Override
    public SocialMessage createSocialMessage(SocialMessage socialMessage) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to createSocialMessage.socialMessage:" + socialMessage);
        }
        SocialMessage message = writeAbleMessageHandler.insertSocialMessage(socialMessage);
        if (message != null) {
            socialMessageCache.putSocialMessage(message.getOwnUno(), message.getMsgId(), message);
        }
        return message;
    }

    @Override
    public boolean removeSocialMessage(long msgId, String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to removeSocialMessage.msgId:" + msgId + ",uno:" + uno);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(SocialMessageField.MSG_ID, msgId));
        queryExpress.add(QueryCriterions.eq(SocialMessageField.OWN_UNO, uno));
        SocialMessage message = readonlyMessageHandlersPool.getHandler().getSocialMessage(uno, queryExpress);
        if (message == null) {
            return false;
        }
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(SocialMessageField.REMOVE_STATUS, ActStatus.ACTED.getCode());
        updateExpress.set(SocialMessageField.MODIFY_DATE, new Date());

        boolean bool = writeAbleMessageHandler.updateSocialMessage(uno, updateExpress, queryExpress);
        if (bool) {
            socialMessageCache.removeSocialMessage(uno, msgId);

            if (message.getMsgType().equals(SocialMessageType.AGREE)) {
                Map<ObjectField, Object> paramMap = new HashMap<ObjectField, Object>();
                paramMap.put(ProfileSumField.SOCIALAGREEMSGSUM, -1);
                ProfileServiceSngl.get().increaseProfileSum(uno, paramMap);
            } else if (message.getMsgType().equals(SocialMessageType.REPLY)) {
                Map<ObjectField, Object> paramMap = new HashMap<ObjectField, Object>();
                paramMap.put(ProfileSumField.SOCIALREPLYMSGSUM, -1);
                ProfileServiceSngl.get().increaseProfileSum(uno, paramMap);
            }
        }
        return bool;
    }

    @Override
    public SocialNotice getSocialNotice(String ownUno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getSocialNotice.ownUno:" + ownUno);
        }
        SocialNotice socialNotice = socialMessageCache.getSocialNotice(ownUno);
        if (socialNotice == null) {
            socialNotice = writeAbleMessageHandler.getSocialNotice(ownUno);
            if (socialNotice != null) {
                socialMessageCache.putSocialNotice(ownUno, socialNotice);
            }
        }
        return socialNotice;
    }

    @Override
    public boolean modifySocialNotice(String uno, int messageType) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifySocialNotice.ownUno:" + uno);
        }
        UpdateExpress updateExpress = new UpdateExpress();
        if (SocialMessageType.AGREE.getCode() == messageType) {
            updateExpress.set(SocialNoticeField.AGREE_COUNT, 0);
            updateExpress.set(SocialNoticeField.READ_AGREE_DATE, new Date());
        } else if (SocialMessageType.REPLY.getCode() == messageType) {
            updateExpress.set(SocialNoticeField.REPLY_COUNT, 0);
            updateExpress.set(SocialNoticeField.READ_REPLY_DATE, new Date());
        } else if (SocialMessageType.HOT.getCode() == messageType) {
            updateExpress.set(SocialNoticeField.HOT_COUNT, 0);
            updateExpress.set(SocialNoticeField.READ_HOT_DATE, new Date());
        } else if (SocialMessageType.FOCUS.getCode() == messageType) {
            updateExpress.set(SocialNoticeField.FOCUS_COUNT, 0);
            updateExpress.set(SocialNoticeField.READ_FOCUS_DATE, new Date());
        } else if (SocialMessageType.NOTICE.getCode() == messageType) {
            updateExpress.set(SocialNoticeField.NOTICE_COUNT, 0);
            updateExpress.set(SocialNoticeField.READ_NOTICE_DATE, new Date());
        }
        boolean bool = writeAbleMessageHandler.updateSocialNotice(uno, updateExpress);
        if (bool) {
            socialMessageCache.removeSocialNotice(uno);
        }
        return bool;
    }


    @Override
    public void sendSociailPushMessage(com.enjoyf.platform.service.joymeapp.PushMessage pushMessage) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic sendSociailPushMessage . pushMessage = " + pushMessage);
        }
        pushMsgProcessQueueThreadN.add(pushMessage);
    }

    @Override
    public List<ClientDevice> queryClientDevice(QueryExpress add) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic queryClientDevice . add = " + add);
        }
        return readonlyMessageHandlersPool.getHandler().queryClientDevice(add);
    }

    @Override
    public PageRows<ClientDevice> queryClientDeviceByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic queryClientDeviceByPage.queryExpress = " + queryExpress + ",pagination = " + pagination);
        }
        return readonlyMessageHandlersPool.getHandler().queryClientDeviceByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifySocialMessage(String ownUno, QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic removeSocialMessageByReply . ownUno = " + ownUno);
        }

        SocialMessage message = readonlyMessageHandlersPool.getHandler().getSocialMessage(ownUno, queryExpress);
        if (message == null) {
            return false;
        }

        boolean bool = writeAbleMessageHandler.updateSocialMessage(ownUno, updateExpress, queryExpress);
        if (bool) {
            socialMessageCache.removeSocialMessage(ownUno, message.getMsgId());
        }
        return bool;
    }

    @Override
    public List<com.enjoyf.platform.service.joymeapp.PushMessage> queryPushMessageByCache(String appKey, int platform, String uno, Long timestamp, PushListType pushListType) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryPushMessageByCache.appKey:" + appKey + ",platform:" + platform);
        }
        //	joymeAppCache.removePushMessageList(appKey,platform);
        List<com.enjoyf.platform.service.joymeapp.PushMessage> pushMessageList = noticeCache.getPushMessageList(appKey, platform, uno, timestamp.longValue(), pushListType);
        if (CollectionUtil.isEmpty(pushMessageList)) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(com.enjoyf.platform.service.joymeapp.PushMessageField.APPKEY, appKey));
            queryExpress.add(QueryCriterions.eq(com.enjoyf.platform.service.joymeapp.PushMessageField.PLATFORM, platform));
            //系统通知
            queryExpress.add(QueryCriterions.eq(com.enjoyf.platform.service.joymeapp.PushMessageField.PUSHLISTTYPE, pushListType.getCode()));
            //使用中
            queryExpress.add(QueryCriterions.eq(com.enjoyf.platform.service.joymeapp.PushMessageField.SEND_STATUS, ActStatus.ACTING.getCode()));
            //有时间
            if (timestamp > 0) {
                queryExpress.add(QueryCriterions.gt(com.enjoyf.platform.service.joymeapp.PushMessageField.SENDDATE, new Date(timestamp)));
                //没时间 最多取 7 天
            } else if (appKey.equals("0G30ZtEkZ4vFBhAfN7Bx4v")) {
                queryExpress.add(QueryCriterions.gt(com.enjoyf.platform.service.joymeapp.PushMessageField.SENDDATE, new Date(System.currentTimeMillis() - 1000l * 60l * 60l * 24l * 7l)));
            }
            queryExpress.add(QuerySort.add(com.enjoyf.platform.service.joymeapp.PushMessageField.SENDDATE, QuerySortOrder.DESC));

            PageRows rows = readonlyJoymeAppHandlersPool.getHandler().queryPushMessageByPage(queryExpress, new Pagination(20 * 1, 1, 20));
            if (rows != null && !CollectionUtil.isEmpty(rows.getRows())) {
                pushMessageList = rows.getRows();
                noticeCache.putPushMessageList(appKey, platform, uno, timestamp, pushMessageList, pushListType);
            }
        }
        return pushMessageList;
    }

    @Override
    public PageRows<com.enjoyf.platform.service.joymeapp.PushMessage> queryPushMessage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " call handler queryPushMessage.queryExpress" + queryExpress);
        }

        return readonlyJoymeAppHandlersPool.getHandler().queryPushMessageByPage(queryExpress, pagination);
    }

    @Override
    public com.enjoyf.platform.service.joymeapp.PushMessage ceatePushMessage(com.enjoyf.platform.service.joymeapp.PushMessage pushMessage) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic sendPushMessage . pushMessage: " + pushMessage);
        }

        return writeAbleJoymeAppHandler.insertPushMessage(pushMessage);
    }

    @Override
    public boolean sendPushMessage(com.enjoyf.platform.service.joymeapp.PushMessage pushMessage) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic sendPushMessage . pushMessage = " + pushMessage);
        }
        return true;
    }

    @Override
    public boolean modifyPushMessage(UpdateExpress updateExpress, QueryExpress queryExpress, String appKey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageLogic modifyPushMessage . UpdateExpress = " + updateExpress + " queryExpress:" + queryExpress);
        }
        com.enjoyf.platform.service.joymeapp.PushMessage pushMessage = readonlyJoymeAppHandlersPool.getHandler().getPushMessage(queryExpress);
        if (pushMessage == null) {
            return false;
        }
        boolean returnBval = writeAbleJoymeAppHandler.updatePushMessage(updateExpress, queryExpress);
        if (returnBval) {
            noticeCache.deleteLastPushMessage(appKey, pushMessage.getAppVersion());
        }
        return returnBval;
    }

    @Override
    public void pushMessageNoticeTime(String key, String value) throws ServiceException {
        writeAbleMessageHandler.pushMessageNoticeTime(key, value);
        socialMessageCache.putMessageNoticeTime(key, value);
    }

    @Override
    public Long getMessageNoticeTime(String key) throws ServiceException {
        String time = socialMessageCache.getMessageNoticeTime(key);
        if (StringUtil.isEmpty(time)) {
            time = readonlyMessageHandlersPool.getHandler().getMessageNoticeTime(key);
        }
        if (StringUtil.isEmpty(time)) {
            return 0L;
        }
        return Long.valueOf(time);
    }

    @Override
    public com.enjoyf.platform.service.joymeapp.PushMessage getPushMessage(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getPushMessageForAndroid.getPushMessage.queryExpress:  " + queryExpress);
        }

        return readonlyJoymeAppHandlersPool.getHandler().getPushMessage(queryExpress);
    }

    @Override
    public boolean removePushMessage(Long msgId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic removePushMessage.msgId:" + msgId);
        }
        return writeAbleJoymeAppHandler.removePushMessage(msgId);
    }

    @Override
    public void setTORedis(String key, String value) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("setTORedis.key:  " + key + ",value:" + value);
        }
        messageRedis.set(key, value);
    }

    @Override
    public String getRedis(String key) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getRedis.key:  " + key);
        }
        return messageRedis.get(key);
    }

    @Override
    public void setTORedisIncr(String key, long value, int timeOutSec) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getRedis.key:  " + key);
        }
        messageRedis.incr(key, value, timeOutSec);
    }

//    @Override
//    public com.enjoyf.platform.service.joymeapp.PushMessage getLastPushMessage(String appKey, String version, String imei, String ismi, long currentMaxMesgId) throws ServiceException {
//        if (logger.isDebugEnabled()) {
//            logger.debug("getPushMessageForAndroid.appKey: " + appKey + " currentMaxMesgId: " + currentMaxMesgId);
//        }
//
//        com.enjoyf.platform.service.joymeapp.PushMessage lastPushMesg = noticeCache.getLastPushMessage(appKey, version);
//        if (lastPushMesg == null) {
//            com.enjoyf.platform.service.joymeapp.PushMessage pushMessage = getAndroidLastestPushMessage(appKey, version);
//
//            //从db查询如果为空，说明该APP从来没发送过消息，new一个pushmessage ID设置为-1，放在缓存中减少DB压力
//            if (pushMessage == null) {
//                pushMessage = new com.enjoyf.platform.service.joymeapp.PushMessage();
//                pushMessage.setPushMsgId(-1l);
//            }
//            noticeCache.putLastPushMessage(appKey, version, pushMessage);
//            lastPushMesg = pushMessage;
//        }
//
//        if (lastPushMesg.getPushMsgId() <= 0 || lastPushMesg.getPushMsgId() <= currentMaxMesgId) {
//            return null;
//        }
//
//        UpdateExpress updateExpress = new UpdateExpress()
//                .set(PushMessageDeviceField.LASTMSGID, lastPushMesg.getPushMsgId());
//
//        QueryExpress queryExpress = new QueryExpress()
//                .add(QueryCriterions.eq(PushMessageDeviceField.CLIENTID, imei))
//                .add(QueryCriterions.eq(PushMessageDeviceField.CLIENTTOKEN, ismi));
//
//        writeAbleHandler.updatePushMessageDevice(updateExpress, queryExpress);
//
//        return lastPushMesg;
//    }
//
//    @Override
//    public void putLastPushMessage(com.enjoyf.platform.service.joymeapp.PushMessage pushMessage) throws ServiceException {
//        noticeCache.putLastPushMessage(pushMessage.getAppKey(), pushMessage.getAppVersion(), pushMessage);
//    }

    private void processSocialPostMessage(com.enjoyf.platform.service.joymeapp.PushMessage pushMessage) {
        SocialMessage socialMessage = new SocialMessage();
        try {
            Pagination page = new Pagination(100, 1, 100);
            Set<String> exitTokenSet = new HashSet<String>();
            do {
                PageRows<ClientDevice> pageRows = writeAbleMessageHandler.queryClientDeviceByPage(new QueryExpress().add(QueryCriterions.eq(ClientDeviceField.PLATFORM, 0)), page);
                if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                    for (ClientDevice device : pageRows.getRows()) {
                        if (exitTokenSet.contains(device.getClientToken())) {
                            continue;
                        } else {
                            exitTokenSet.add(device.getClientToken());
                        }
                        Set<String> deviceSet = new HashSet<String>();
                        deviceSet.add(device.getClientToken());
                        String sound = "nosound.caf";
                        try {
                            SocialProfile socialProfile = ProfileServiceSngl.get().getSocialProfileByUno(device.getUno());
                            if (socialProfile != null && socialProfile.getSetting() != null && AllowType.A_ALLOW.equals(socialProfile.getSetting().getAllowSoundSocial())) {
                                sound = "default";
                            }
                        } catch (ServiceException e) {
                            e.printStackTrace();
                        }

                        int type = 0;
                        String info = "";
                        if (pushMessage.getOptions() != null && !CollectionUtil.isEmpty(pushMessage.getOptions().getList())) {
                            type = pushMessage.getOptions().getList().get(0).getType();
                            info = pushMessage.getOptions().getList().get(0).getInfo();
                        }

//                        Set<String> test = new HashSet<String>();
//                        test.add("5f82e3717e09d382c2cf899878892cd5c643f7b7a42214b9e7e9cfc959940ff2");

                        Map<String, Object> paramMap = new HashMap<String, Object>();
                        paramMap.put("deviceset", deviceSet);
                        paramMap.put("body", pushMessage.getShortMessage());
                        paramMap.put("msgtype", type);
                        paramMap.put("sound", sound);
                        paramMap.put("info", info);
                        MessageProcessFactory.get().factory(socialMessage).processSendMessage(paramMap);
                    }
                }
            } while (!page.isLastPage());
        } catch (DbException e) {
            GAlerter.lab(this.getClass().getName() + " sendSociailPushMessage occur exception.e", e);
        }
    }


    private boolean parsePushMessage(PushMessage pushMessage, boolean isTest) {

        Message message = new Message();

        message.setTopicId(pushMessage.getPushMsgId());
        message.setMsgType(MessageType.CLIENTPUSH);
        message.setBody(pushMessage.getPushMsgId() + "");

        message.setSendDate(new Date());
        message.setSenderUno(WebappConfig.get().getSystemUno());
        message.setSendIp("127.0.0.1");

        //
        return pushMsgInsertDB(message, isTest);

    }

    private boolean pushMsgInsertDB(Message message, boolean isTest) {

        // if test
        if (isTest) {
            List<String> iosTestClient = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getIosTestClient();
            for (String uuid : iosTestClient) {
                ProfileMobileDevice profileMobileDevice = new ProfileMobileDevice();
                profileMobileDevice.setMdSerial(uuid);
                profileMobileDevice.setMdClientType(ProfileMobileDeviceClientType.IOS);
                profileMobileDevice.setMdHdType("ip");
                profileMobileDevice.setMdOsVersion("5.1.1");
                profileMobileDevice.setPushStatus(ActStatus.ACTED);
                profileMobileDevice.setPmdId(1);

                message.setReadStatus(ActStatus.ACTING);
                message.setRecieverUno(profileMobileDevice.getPmdId() + "");
                message.setOwnUno(profileMobileDevice.getPmdId() + "");

                PushMessage pm = null;
                try {
                    writeAbleMessageHandler.postMessage(message);

                    // query pushMessage
                    QueryExpress pMsgQxp = new QueryExpress();
                    pMsgQxp.add(QueryCriterions.eq(PushMessageField.PUSHMSGID, new Long(message.getBody())));
                    pm = readonlyMessageHandlersPool.getHandler().get(pMsgQxp);

                } catch (DbException e) {
                    e.printStackTrace();
                }

                // is ios
                if (profileMobileDevice.getMdClientType().equals(ProfileMobileDeviceClientType.IOS)) {
                    logger.debug("getMdClientType is ios at pushMsgProcessQueueThreadN.....");
                    //
                    ProfileMobileDevice pmd = new ProfileMobileDevice();
                    pmd.setMdSerial(profileMobileDevice.getMdSerial());

                    PushMsg pushMsg = new PushMsg();
                    pushMsg.setMessage(message);
                    pushMsg.setPushMessage(pm);
                    pushMsg.setProfileMobileDevice(pmd);
                    pushMsg.setTest(isTest);

                    // queue
                    pushMsgProcessQueueThreadN.add(pushMsg);
                }
            }
            //
            logger.debug(" test send mess --!!!!");

        } else {
            //
            logger.debug(" send mess ---START----");

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ProfileMobileDeviceField.PUSHSTATUS, ActStatus.ACTED.getCode()));
            queryExpress.add(QueryCriterions.ne(ProfileMobileDeviceField.PUSHMSGID, message.getTopicId()));

            Pagination page = new Pagination(0, 1, 1000);
            try {
                PageRows<ProfileMobileDevice> pageRows = ProfileServiceSngl.get().queryProfileMobileDevice(queryExpress, page);

                // query pushMessage
                QueryExpress pMsgQxp = new QueryExpress();
                pMsgQxp.add(QueryCriterions.eq(PushMessageField.PUSHMSGID, new Long(message.getBody())));
                PushMessage pm = readonlyMessageHandlersPool.getHandler().get(pMsgQxp);


                for (ProfileMobileDevice profileMobileDevice : pageRows.getRows()) {
                    message.setReadStatus(ActStatus.ACTING);
                    message.setRecieverUno(profileMobileDevice.getPmdId() + "");
                    message.setOwnUno(profileMobileDevice.getPmdId() + "");

                    writeAbleMessageHandler.postMessage(message);

                    // update ProfileMobileDevice
                    QueryExpress pmdQueryExpress = new QueryExpress();
                    pmdQueryExpress.add(QueryCriterions.eq(ProfileMobileDeviceField.PUSHMSGID, profileMobileDevice.getPushMsgId()));
                    pmdQueryExpress.add(QueryCriterions.eq(ProfileMobileDeviceField.PUSHSTATUS, ActStatus.ACTED.getCode()));

                    UpdateExpress pmdUpdateExpress = new UpdateExpress();
                    pmdUpdateExpress.set(ProfileMobileDeviceField.PUSHMSGID, message.getTopicId());

                    ProfileServiceSngl.get().modifyProfileMobileDevice(pmdUpdateExpress, pmdQueryExpress);

                    // is ios
                    if (profileMobileDevice.getMdClientType().equals(ProfileMobileDeviceClientType.IOS)) {
                        logger.debug("getMdClientType is ios at pushMsgProcessQueueThreadN.....");
                        //
                        ProfileMobileDevice pmd = new ProfileMobileDevice();
                        pmd.setMdSerial(profileMobileDevice.getMdSerial());

                        PushMsg pushMsg = new PushMsg();
                        pushMsg.setMessage(message);
                        pushMsg.setPushMessage(pm);
                        pushMsg.setProfileMobileDevice(pmd);
                        pushMsg.setTest(isTest);

                        // queue
                        pushMsgProcessQueueThreadN.add(pushMsg);
                    }
                }

                if (pageRows.getPage().getMaxPage() > 1) {
                    pushMsgInsertDB(message, isTest);
                }

            } catch (ServiceException e) {
                GAlerter.lab("MessageLogic parsePushMessage push message into db  error. queryExpress is " + queryExpress, e);
                return false;
            }
        }
        return true;
    }

    private boolean sendToAPNS(List<String> tokens, PushNotificationPayload payLoad, String certificatePath, String certificatePassword, boolean isProduction) {
        int successful = 0;
        try {

            PushNotificationManager pushManager = new PushNotificationManager();

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

//            pushManager.stopConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tokens.size() == successful;
    }

    public static int getIntervalHour(long begin, long end) {
        long ms = (end - begin) / 1000 / 60;
        return (int) ms;
    }

    private class AppPushByProfileThread extends Thread {
        private AppPushEvent appPushEvent;

        public AppPushByProfileThread(AppPushEvent appPushEvent) {
            this.appPushEvent = appPushEvent;
        }

        @Override
        public void run() {
            try {
                appPushByProfile(this.appPushEvent);
            } catch (ServiceException e) {
            } finally {
                this.interrupt();
            }
        }
    }

    private void appPushByProfile(AppPushEvent appPushEvent) throws ServiceException {
        com.enjoyf.platform.service.joymeapp.PushMessage pushMessage = modifyOrCreatePushMessage(appPushEvent);
        String deviceAppKey = appPushEvent.getAppKey();
        if (appPushEvent.getAppKey().equals("25AQWaK997Po2x300CQeP0")) {
            deviceAppKey = "17yfn24TFexGybOF0PqjdY";
        }
        String devicesToken = "";
        if (!StringUtil.isEmpty(appPushEvent.getProfileIds())) {
            String[] profileIds = appPushEvent.getProfileIds().split(" ");
            if (!CollectionUtil.isEmpty(profileIds)) {
                List<ClientDevice> deviceList = queryClientDevice(new QueryExpress()
                        .add(QueryCriterions.eq(ClientDeviceField.APP_ID, deviceAppKey))
                        .add(QueryCriterions.eq(ClientDeviceField.PLATFORM, appPushEvent.getPlatform().getCode()))
                        .add(QueryCriterions.in(ClientDeviceField.PROFILEID, profileIds)));
                for (ClientDevice device : deviceList) {
                    if (device != null) {
                        devicesToken = devicesToken + device.getClientToken() + " ";
                    }
                }
            }
            appPushEvent.setDevices(devicesToken);
        }
        logger.info("-----------------pushMessageQueueThreadN acting msgid:" + pushMessage.getPushMsgId() + "---------------------");
        appPushFactorySendPushMessage(appPushEvent);
    }

    private class AppPushByGiftThread extends Thread {
        private AppPushEvent appPushEvent;

        public AppPushByGiftThread(AppPushEvent appPushEvent) {
            this.appPushEvent = appPushEvent;
        }

        @Override
        public void run() {
            try {
                appPushByGift(this.appPushEvent);
            } catch (ServiceException e) {
            } finally {
                this.interrupt();
            }
        }

    }

    private void appPushByGift(AppPushEvent appPushEvent) throws ServiceException {
        if (!StringUtil.isEmpty(appPushEvent.getTags())) {
            com.enjoyf.platform.service.joymeapp.PushMessage pushMessage = modifyOrCreatePushMessage(appPushEvent);

            Long giftId = Long.valueOf(appPushEvent.getTags().split(" ")[0]);
            String deviceAppKey = appPushEvent.getAppKey();
            if (appPushEvent.getAppKey().equals("25AQWaK997Po2x300CQeP0")) {
                deviceAppKey = "17yfn24TFexGybOF0PqjdY";
            }
            if (giftId != null && giftId > 0) {
                int cp = 0;
                Pagination pagination = null;
                do {
                    cp += 1;
                    pagination = new Pagination(200 * cp, cp, 200);
                    PageRows<GiftReserve> giftReserveList = PointServiceSngl.get().queryGiftReserveByPage(new QueryExpress()
                            .add(QueryCriterions.eq(GiftReserveField.AID, giftId))
                            .add(QueryCriterions.eq(GiftReserveField.APPKEY, deviceAppKey))
                            .add(QueryCriterions.eq(GiftReserveField.VALID_STATUS, ValidStatus.INVALID.getCode())), pagination);
                    if (giftReserveList != null && !CollectionUtil.isEmpty(giftReserveList.getRows())) {
                        pagination = giftReserveList.getPage();

                        Set<String> profileIds = new HashSet<String>();
                        for (GiftReserve giftReserve : giftReserveList.getRows()) {
                            if (giftReserve != null) {
                                profileIds.add(giftReserve.getProfileId());
                            }
                        }
                        if (!CollectionUtil.isEmpty(profileIds)) {
                            String devicesToken = "";
                            List<ClientDevice> deviceList = queryClientDevice(new QueryExpress()
                                    .add(QueryCriterions.eq(ClientDeviceField.APP_ID, deviceAppKey))
                                    .add(QueryCriterions.eq(ClientDeviceField.PLATFORM, appPushEvent.getPlatform().getCode()))
                                    .add(QueryCriterions.in(ClientDeviceField.PROFILEID, profileIds.toArray())));
                            if (!CollectionUtil.isEmpty(deviceList)) {
                                for (ClientDevice device : deviceList) {
                                    if (device != null) {
                                        devicesToken = devicesToken + device.getClientToken() + " ";
                                    }
                                }
                                appPushEvent.setDevices(devicesToken);
                                logger.info("-----------------pushMessageQueueThreadN acting msgid:" + pushMessage.getPushMsgId() + "---------------------");
                                appPushFactorySendPushMessage(appPushEvent);
                            }
                        }
                    }
                } while (!pagination.isLastPage());
            }
        }
    }
}