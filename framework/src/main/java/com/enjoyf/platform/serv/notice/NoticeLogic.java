package com.enjoyf.platform.serv.notice;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.notice.NoticeHandler;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.system.wanba.WanbaQuestionNoticeEvent;
import com.enjoyf.platform.service.event.system.wanba.WanbaReplyEvent;
import com.enjoyf.platform.service.event.system.wiki.WikiNoticeEvent;
import com.enjoyf.platform.service.notice.*;
import com.enjoyf.platform.service.notice.wanba.WanbaQuestionNoticeBody;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
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
 * NoticeLogic is called by NoticePacketDecoder.
 */
class NoticeLogic implements NoticeService {
    //
    private static final Logger logger = LoggerFactory.getLogger(NoticeLogic.class);

    private NoticeConfig config;

    private NoticeRedis noticeRedis;
    //queue thread pool
    private QueueThreadN eventProcessQueueThreadN = null;

    private NoticeHandler noticeHandler = null;


    NoticeLogic(NoticeConfig cfg) {
        config = cfg;

        eventProcessQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                if (obj instanceof WanbaQuestionNoticeEvent) {
                    processQuestionNoticeEvent((WanbaQuestionNoticeEvent) obj);
                } else if (obj instanceof WanbaReplyEvent) {
                    processWanbaReplyEvent((WanbaReplyEvent) obj);
                } else if (obj instanceof WikiNoticeEvent) {
                    processWikiNoticeEvent((WikiNoticeEvent) obj);
                } else {
                    GAlerter.lab("In timeLineProcessQueueThreadN, there is a unknown obj.");
                }
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "noticeProcessQueue"));

        noticeRedis = new NoticeRedis(cfg.getProps());

        try {
            //
            noticeHandler = new NoticeHandler(config.getWriteableDataSourceName(), config.getProps());
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());

            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

    }

    private void processWikiNoticeEvent(WikiNoticeEvent wikiNoticeEvent) {
        UserNotice userNotice = new UserNotice();
        userNotice.setAppkey(wikiNoticeEvent.getAppkey());
        userNotice.setCreateTime(wikiNoticeEvent.getCreateTime());
        userNotice.setNoticeType(wikiNoticeEvent.getType().getCode());
        userNotice.setProfileId(wikiNoticeEvent.getProfileId());
        userNotice.setBody(wikiNoticeEvent.getBody().toJson());
        try {
            userNotice = createUserNotice(userNotice);
            noticeRedis.increaseNoticeSum(wikiNoticeEvent.getProfileId(), wikiNoticeEvent.getAppkey(), wikiNoticeEvent.getType(), 1);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " processWikiNoticeEvent e", e);
        }
    }

    private void processWanbaReplyEvent(WanbaReplyEvent obj) {
        //todo
        UserNotice userNotice = new UserNotice();
        userNotice.setAppkey(obj.getAppkey());


        userNotice.setBody(obj.getWanbaReplyBody().toJson());
        userNotice.setCreateTime(obj.getCreateTime());
        userNotice.setNoticeType(obj.getType().getCode());
        userNotice.setProfileId(obj.getProfileId());
        userNotice.setDestId(obj.getDestId());


        try {
            userNotice = createUserNotice(userNotice);

            //increase sm
            noticeRedis.increaseNoticeSum(obj.getProfileId(), obj.getAppkey(), obj.getType(), 1);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private void processQuestionNoticeEvent(WanbaQuestionNoticeEvent event) {
        //todo
        UserNotice userNotice = new UserNotice();
        userNotice.setAppkey(event.getAppkey());

        WanbaQuestionNoticeBody noticeBody = new WanbaQuestionNoticeBody();
        noticeBody.setAnswerId(event.getAnswerId());
        noticeBody.setQuertionId(event.getQuestionId());
        noticeBody.setDestProfileId(event.getDestProfileId());
        noticeBody.setExtStr(StringUtil.isEmpty(event.getExtStr()) ? "" : event.getExtStr());
        noticeBody.setBodyType(event.getBodyType().getCode());

        userNotice.setBody(noticeBody.toJson());
        userNotice.setCreateTime(event.getCreateTime());
        userNotice.setNoticeType(event.getType().getCode());
        userNotice.setProfileId(event.getProfileId());

        try {
            userNotice = createUserNotice(userNotice);

            //increase sm
            noticeRedis.increaseNoticeSum(event.getProfileId(), event.getAppkey(), event.getType(), 1);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    //insert db  and cache
    private UserNotice createUserNotice(UserNotice userNotice) throws ServiceException {
        userNotice = noticeHandler.insertUserNotice(userNotice);

        noticeRedis.setUserNotice(userNotice);
        noticeRedis.addUserNoticeList(userNotice.getProfileId(), userNotice.getAppkey(), userNotice.getNoticeType(), userNotice.getUserNoticeId());
        return userNotice;
    }

    @Override
    public Map<String, AppNoticeSum> queryAppNoticeSum(String profileId, String appKey, Collection<NoticeType> types) throws ServiceException {
        Map<String, AppNoticeSum> returnMap = noticeRedis.getNoticeSums(profileId, appKey, types);
        return returnMap;
    }

    //读取系统消息的接口另外调用
    @Override
    public boolean readNotice(String profileId, String appkey, NoticeType type) throws ServiceException {
        return noticeRedis.removeNoticeSum(profileId, appkey, type);
    }

    @Override
    public AppNoticeSum querySystemNoticeSum(String profileId, String version, String platfrom, String appKey) {
        String systemFlag = noticeRedis.getSysNoticeSum(profileId, appKey);

        int sum = noticeRedis.systemNoticeSize(appKey, version, platfrom, systemFlag);

        AppNoticeSum noticeSum = null;
        if (sum > 0) {
            noticeSum = new AppNoticeSum();
            noticeSum.setAppkey(appKey);
            noticeSum.setProfileId(profileId);
            noticeSum.setValue(String.valueOf(sum));
            noticeSum.setType(NoticeType.SYSTEM.getCode());
            noticeSum.setDtype(NoticeType.SYSTEM.getDtype());
        }
        return noticeSum;
    }

    @Override
    public PageRows<SystemNotice> querySystemNoitce(String appKey, String version, String platform, Pagination page) throws ServiceException {

        Set<String> noticeIdList = noticeRedis.querySystemNotice(appKey, version, platform, page);

        List<SystemNotice> systemNoticeList = new ArrayList<SystemNotice>();
        for (String noticeId : noticeIdList) {
            SystemNotice systemNotice = noticeRedis.getSystemNotice(noticeId);
            if (systemNotice == null) {
                continue;
            }
            systemNoticeList.add(systemNotice);
        }

        PageRows<SystemNotice> pageRows = new PageRows<SystemNotice>();
        pageRows.setPage(page);
        pageRows.setRows(systemNoticeList);

        return pageRows;
    }

    @Override
    public PageRows<SystemNotice> querySystemNoticeByProfile(String profileId, String appkey, String version, String platform, Pagination pagination) throws ServiceException {
        return null;
    }

    @Override
    public PageRows<SystemNotice> querySystemNoitceByPage(QueryExpress queryExpress, Pagination page) throws ServiceException {
        return noticeHandler.querySystemNoitcePage(queryExpress, page);
    }

    @Override
    public boolean readSystemNotice(String profileId, String appKey, String version, String platform) throws ServiceException {
        String maxFlag = noticeRedis.maxFlag(appKey, version, platform);
        if (StringUtil.isEmpty(maxFlag)) {
            return false;
        }
        long flag = Long.parseLong(maxFlag);
        noticeRedis.setSysNoticeSum(profileId, appKey, String.valueOf(flag));
        return true;
    }

    @Override
    public SystemNotice getSystemNoitce(Long systemNoticeId) throws ServiceException {
        return noticeHandler.getSystemNotice(systemNoticeId);
    }

    @Override
    public SystemNotice createSystemNotice(SystemNotice systemNotice) throws ServiceException {

        //todo insert db or redis
        systemNotice = noticeHandler.insertSystemNotice(systemNotice);

        //todo zaddList
        noticeRedis.setSystemNotice(systemNotice);
        noticeRedis.addNoticeSystem(systemNotice);

        return systemNotice;
    }

    @Override
    public boolean modifySystemNotice(Long systemNoticeId, UpdateExpress updateExpress) throws ServiceException {
        SystemNotice systemNotice = noticeRedis.getSystemNotice(String.valueOf(systemNoticeId));
        boolean bval = noticeHandler.modifySystemNotice(new QueryExpress().add(QueryCriterions.eq(SystemNoticeField.SYSTEMNOTICEID, systemNoticeId)), updateExpress);
        if (bval && systemNotice != null) {
            systemNotice = getSystemNoitce(systemNoticeId);
            if (systemNotice != null) {
                noticeRedis.setSystemNotice(systemNotice);
            }
        }
        return bval;
    }

    @Override
    public boolean deleteSystemNotice(String appkey, String version, String platform, Long systemNoticeId) throws ServiceException {

        boolean bval = noticeHandler.deleteSystemNotice(systemNoticeId);

        if (bval) {
            SystemNotice systemNotice = noticeRedis.getSystemNotice(String.valueOf(systemNoticeId));
            if (systemNotice != null) {
                noticeRedis.delSystemNotice(String.valueOf(systemNoticeId));
                noticeRedis.deleteNoticeSystem(systemNotice.getAppkey(), systemNotice.getVersion(),
                        String.valueOf(systemNotice.getPlatform()), systemNotice.getSystemNoticeId());
            }
        }

        return false;
    }

    ///////////////
    @Override
    public PageRows<UserNotice> queryUserNotice(String profileId, String appKey, NoticeType type, Pagination page) throws ServiceException {

        Set<String> noticeIdList = noticeRedis.queryUserNoticeList(profileId, appKey, type.getCode(), page);

        List<UserNotice> noticeList = new ArrayList<UserNotice>();
        for (String noticeid : noticeIdList) {
            UserNotice notice = noticeRedis.getUserNotice(noticeid);
            noticeList.add(notice);
        }

        PageRows<UserNotice> userNoticePageRows = new PageRows<UserNotice>();
        userNoticePageRows.setPage(page);
        userNoticePageRows.setRows(noticeList);

        return userNoticePageRows;
    }

    @Override
    public boolean deleteUserNotice(String profileId, String appKey, String type, long noticeId) throws ServiceException {
        //first del by list and db and cache
        noticeRedis.removeUserNoticeList(profileId, appKey, type, noticeId);
        noticeHandler.deleteUserNotice(noticeId);
        boolean bval = noticeRedis.delUserNotice(noticeId);
        return bval;
    }

    @Override
    public boolean deleteUserNoticeAllByType(String profileId, String appkey, NoticeType noticeType) throws ServiceException {
        return noticeRedis.removeUserNoticeAll(profileId, appkey, noticeType.getCode());
    }

    @Override
    public UserNotice getUserNotice(Long noticeId) throws ServiceException {
        UserNotice userNotice = noticeRedis.getUserNotice(String.valueOf(noticeId));
        if (userNotice == null) {
            userNotice = noticeHandler.getUserNotice(noticeId);
            if (userNotice != null) {
                noticeRedis.setUserNotice(userNotice);
            }
        }

        return userNotice;
    }

    @Override
    public boolean deleteUserNoticeByDestId(String profileId, String destId) throws ServiceException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(UserNoticeField.PROFILEID, profileId));
        queryExpress.add(QueryCriterions.eq(UserNoticeField.DESTID, destId));
        UserNotice userNotice = noticeHandler.getUserNotice(queryExpress);
        if (userNotice != null) {
            deleteUserNotice(userNotice.getProfileId(), userNotice.getAppkey(), userNotice.getNoticeType(), userNotice.getUserNoticeId());
        }
        return true;
    }

    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" receiveEvent event:" + event);
        }

        eventProcessQueueThreadN.add(event);
        return true;
    }
}