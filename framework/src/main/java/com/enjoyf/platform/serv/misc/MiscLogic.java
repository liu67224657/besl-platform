/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.misc;


import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.db.misc.MiscHandler;
import com.enjoyf.platform.db.misc.MiscMogoHandler;
import com.enjoyf.platform.serv.ask.AskRedis;
import com.enjoyf.platform.service.misc.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.UserLogin;
import com.enjoyf.platform.service.weixin.AccessToken;
import com.enjoyf.platform.service.weixin.Ticket;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sms.SMSSenderSngl;
import com.enjoyf.platform.util.sms.SendResult;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.util.weixin.WeixinUtil;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
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
class MiscLogic implements MiscService {
    //
    private static final Logger logger = LoggerFactory.getLogger(MiscLogic.class);

    private MiscConfig config;

    //the handler's
    private MiscHandler writeAbleMiscHandler;
    private HandlerPool<MiscHandler> readonlyMiscHandlersPool;

    private MiscMogoHandler writeMiscMongoHandler;
    private HandlerPool<MiscMogoHandler> readonlyMiscMongoHandlersPool;

    private QueueThreadN sendSMSQueue;
    private WeixinCache weixinCache;
    private MiscCache miscCache;


    private MiscRedis miscRedis;

    MiscLogic(MiscConfig cfg) {

        miscRedis = new MiscRedis(cfg.getProps());

        config = cfg;

        //initialize the handler.
        try {
            //
            writeAbleMiscHandler = new MiscHandler(config.getWriteableDataSourceName(), config.getProps());

            //
            readonlyMiscHandlersPool = new HandlerPool<MiscHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyMiscHandlersPool.add(new MiscHandler(dsn, config.getProps()));
            }
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass(), e);

            // sleep 5 seconds for the system to send out the alert.
//            Utility.sleep(5000);
//            System.exit(0);
        }
//
//        try {
//            writeMiscMongoHandler = new MiscMogoHandler(config.getMongoDbWriteAbleDateSourceName(), config.getProps());
//
//            readonlyMiscMongoHandlersPool = new HandlerPool<MiscMogoHandler>();
//            for (String dsn : config.getMongoDbReadonlyDataSourceNames()) {
//                readonlyMiscMongoHandlersPool.add(new MiscMogoHandler(dsn, config.getProps()));
//            }
//        } catch (DbException e) {
//            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());
//            Utility.sleep(5000);
//            System.exit(0);
//        }

        sendSMSQueue = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                if (obj instanceof SMSWarp) {
                    processQueuedEvent((SMSWarp) obj);
                } else {
                    GAlerter.lab("In eventProcessQueueThreadN, there is a unknown obj.");
                }
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessQueue"));
        weixinCache = new WeixinCache(config.getMemCachedConfig());

        miscCache = new MiscCache(config.getMemCachedConfig());

        //cms定时刷新任务
        if (cfg.isOpenStatTrigger()) {
            try {
                RefreshCmsTimingReleaseTrigger releaseTrigger = new RefreshCmsTimingReleaseTrigger(cfg, writeAbleMiscHandler);
                releaseTrigger.init();
                releaseTrigger.start();
            } catch (Exception e) {
                GAlerter.lab("EditorQuartzCronTrigger start error.", e);
            }
        }
    }

    private void processQueuedEvent(SMSWarp obj) {

        int times = (int) miscCache.incrSMSLimit(obj.getPhone(), 1);
        if (times > 5) {
            GAlerter.lan(this.getClass().getName() + " phone send sms out limit >=5 phone: " + obj.getPhone());
            return;
        }

        SendResult result = SMSSenderSngl.getByCode(obj.getCode()).sendMessage(obj.getPhone(), obj.getContent(), obj.getType());

        miscCache.addSMSSender(new Date());
        if (result.getCode() == SendResult.SEND_SUCESS) {
            miscCache.addSMSSenderSuccess(new Date());
        }
        //write stats
        SMSLog smsLog = new SMSLog();
        smsLog.setPhone(obj.getPhone());
        smsLog.setCreateTime(new Date());
        smsLog.setResult(result.getCode());
        smsLog.setThirdCode(result.getThirdCode());
        smsLog.setThirdMessage(result.getMsg());
        smsLog.setSmsType(obj.getType());
//        try {
//            writeMiscMongoHandler.insertSMSLog(smsLog);
//        } catch (DbException e) {
//            GAlerter.lab("insert sms log error.e:", e);
//        }

    }

    @Override
    public List<Region> getAllRegions() throws ServiceException {
        //
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call readonlyMiscHandlersPool to getAllRegions.");
        }

        //
        return readonlyMiscHandlersPool.getHandler().getAllRegion();
    }

    @Override
    public RegCode getRegCode(String regCode) throws ServiceException {
        //
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call readonlyMiscHandlersPool to getRegCode.");
        }

        //
        return readonlyMiscHandlersPool.getHandler().getRegCode(regCode);
    }

    @Override
    public boolean updateApplyInfo(String regCode, String applyEmail, String applyReason) throws ServiceException {
        //
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call writeAbleMiscHandler to updateApplyInfo.");
        }

        //
        return writeAbleMiscHandler.updateApplyInfo(regCode, applyEmail, applyReason);
    }

    @Override
    public boolean useRegCode(String regCode, String useUno, String useUserid) throws ServiceException {
        //
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call writeAbleMiscHandler to useRegCode.");
        }

        //
        return writeAbleMiscHandler.useRegCode(regCode, useUno, useUserid);
    }

    @Override
    public RegCodeApply applyRegCode(RegCodeApply apply) throws ServiceException {
        //
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call writeAbleMiscHandler to applyRegCode.");
        }

        //
        return writeAbleMiscHandler.applyRegCode(apply);
    }

    @Override
    public RegCodeApply getRegCodeApply(String email) throws ServiceException {
        //
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call readonlyMiscHandlersPool to getRegCodeApply.");
        }

        //
        return readonlyMiscHandlersPool.getHandler().getRegCodeApply(email);
    }

    @Override
    public boolean appendApplyRegCode(String email, String regCode) throws ServiceException {
        //
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call writeAbleMiscHandler to appendApplyRegCode.");
        }

        //
        return writeAbleMiscHandler.appendApplyRegCode(email, regCode);
    }

    @Override
    public Feedback postFeedback(Feedback feedback) throws ServiceException {
        //
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call writeAbleMiscHandler to postFeedback, feedback:" + feedback);
        }

        //
        return writeAbleMiscHandler.postFeedback(feedback);
    }

    @Override
    public IpForbidden createIpForbidden(IpForbidden ipForbidden) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call writeAbleMiscHandler to createIpForbidden, ipForbidden ==> " + ipForbidden);
        }
        return writeAbleMiscHandler.createIpForbidden(ipForbidden);
    }

    @Override
    public IpForbidden getIpForbidden(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call readonlyMiscHandlersPool to getIpForbidden, queryExpress, " + queryExpress);
        }

        return readonlyMiscHandlersPool.getHandler().getIpForbidden(queryExpress);
    }

    @Override
    public PageRows<IpForbidden> queryIpForbiddens(QueryExpress queryExpress, Pagination paginatgion) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call readonlyMiscHandlersPool to queryIpForbiddens, queryExpress ==> "
                    + queryExpress + "; pagination ==>" + paginatgion);
        }
        return readonlyMiscHandlersPool.getHandler().queryIpForbiddens(queryExpress, paginatgion);
    }

    @Override
    public List<IpForbidden> queryIpForbiddensNoPagination(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call readonlyMiscHandlersPool to queryIpForbiddens, queryExpress ==> " + queryExpress);
        }
        return readonlyMiscHandlersPool.getHandler().queryIpForbiddens(queryExpress);
    }

    @Override
    public boolean modifyIpForbidden(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call the handler to update, queryExpress:" + queryExpress + ", updateExpress:" + updateExpress);
        }

        return writeAbleMiscHandler.updateIpForbidden(queryExpress, updateExpress) > 0;
    }

    //
    @Override
    public PageRows<GamePropDb> queryGamePropDb(QueryExpress queryExpress, String gamePropCode, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call the queryGamePropDb:queryExpress " + queryExpress);
        }

        return readonlyMiscHandlersPool.getHandler().queryGamePropDb(queryExpress, gamePropCode, pagination);
    }

    @Override
    public GamePropDb getGamePropDb(QueryExpress getExpress, String gamePropCode) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call the handler to update, getExpress:" + getExpress);
        }

        return readonlyMiscHandlersPool.getHandler().getGamePropDb(getExpress, gamePropCode);
    }

    @Override
    public Map<Long, Map<String, Object>> queryGamePropDbByParam(List<List<GamePropDbQueryParam>> paramLists, String gamePropCode) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call the handler to queryGamePropMapsDb, paramList:" + paramLists);
        }

        Map<Long, Map<String, Object>> returnObj = new HashMap<Long, Map<String, Object>>();

        for (List<GamePropDbQueryParam> paramList : paramLists) {
            List<GamePropDb> idList = readonlyMiscHandlersPool.getHandler().queryGamePropKeyId(paramList, gamePropCode);

            if (CollectionUtil.isEmpty(idList)) {
                continue;
            }

            Set<Long> keyIdSet = new HashSet<Long>();
            for (GamePropDb db : idList) {
                keyIdSet.add(db.getKey_id());
            }

            QueryExpress queryDbItemExpress = null;
            if (keyIdSet.size() > 1) {
                queryDbItemExpress = new QueryExpress().add(QueryCriterions.in(GamePropDbField.KEY_ID, keyIdSet.toArray()));
            } else {
                queryDbItemExpress = new QueryExpress().add(QueryCriterions.eq(GamePropDbField.KEY_ID, keyIdSet.iterator().next()));
            }

            List<GamePropDb> gameDbPropList = readonlyMiscHandlersPool.getHandler().queryGamePropDb(queryDbItemExpress, gamePropCode);

            for (GamePropDb db : gameDbPropList) {
                if (!returnObj.containsKey(db.getKey_id())) {
                    returnObj.put(db.getKey_id(), new HashMap<String, Object>());
                }
                returnObj.get(db.getKey_id()).put(db.getKey_name(), getValueByValueType(db));
            }
        }
        return returnObj;
    }

    private Object getValueByValueType(GamePropDb gamePropDb) {
        if (gamePropDb.getValueType().equals(GamePropValueType.NUM_VALUE)) {
            return gamePropDb.getNum_value();
        } else if (gamePropDb.getValueType().equals(GamePropValueType.DATE_VALUE)) {
            return gamePropDb.getDate_value();
        } else {
            return gamePropDb.getString_value();
        }
    }

    /////////////////////////////////


    @Override
    public JoymeOperate createOperate(JoymeOperate joymeOperate) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call the handler to createOperate, joymeOperate:" + joymeOperate);
        }

        return writeAbleMiscHandler.insertJoymeOperate(joymeOperate);
    }

    @Override
    public List<JoymeOperate> doOperate(String serviceId, JoymeOperateType operateType) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call the handler to doOperate, serviceId:" + serviceId + " operateType:" + operateType);
        }

        return readonlyMiscHandlersPool.getHandler().getUndoOperate(serviceId, operateType);
    }

    @Override
    public JoymeOperateLog createOperateLog(JoymeOperateLog operateLog) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call the handler to createOperateLog, operateLog" + operateLog);
        }

        return writeAbleMiscHandler.insertJoymeOperateLog(operateLog);
    }

    @Override
    public IndexCache createIndexCache(IndexCache indexCache) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call the handler to createIndexCache, IndexCache" + indexCache);
        }
        return writeAbleMiscHandler.insertIndexCache(indexCache);
    }

    @Override
    public boolean modifyIndexCache(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call the handler to modifyIndexCache, UpdateExpress=" + updateExpress + " QueryExpress=" + queryExpress);
        }
        return writeAbleMiscHandler.updateIndexCache(updateExpress, queryExpress);
    }

    @Override
    public List<IndexCache> queryIndexCacheList(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call the handler to queryIndexCacheList, QueryExpress" + queryExpress);
        }
        return readonlyMiscHandlersPool.getHandler().queryIndexCache(queryExpress);
    }

    @Override
    public IndexCache getIndexCache(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call the handler to getIndexCache, QueryExpress" + queryExpress);
        }
        return readonlyMiscHandlersPool.getHandler().getIndexCache(queryExpress);
    }

    @Override
    public IndexCache getRecentIndexCacheById(Long indexCacheId, IndexCacheType type) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call the handler to getIndexCache, indexCacheId:" + indexCacheId + " IndexCacheType:" + type);
        }

        long maxIndexCacheId = readonlyMiscHandlersPool.getHandler().getMaxIndexCacheId(type);


        if (indexCacheId == null || indexCacheId < 0) {
            return readonlyMiscHandlersPool.getHandler().getIndexCache(new QueryExpress().add(QueryCriterions.eq(IndexCacheField.INDEX_CACHE_ID, maxIndexCacheId)));
        }

        if (maxIndexCacheId > indexCacheId) {
            return readonlyMiscHandlersPool.getHandler().getIndexCache(new QueryExpress().add(QueryCriterions.eq(IndexCacheField.INDEX_CACHE_ID, maxIndexCacheId)));
        }

        IndexCache indexCache = new IndexCache();
        indexCache.setIndexCacheId(indexCacheId);

        return indexCache;
    }

    @Override
    public SMSLog createSMS(SMSLog smsLog) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic call the handler to write smslog:" + smsLog);
        }

        return writeMiscMongoHandler.insertSMSLog(smsLog);
    }

    @Override
    public boolean sendSMS(String phone, String content, String type, String code) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic sendSMS:" + phone + " " + content + " " + type + " " + code
            );
        }

        sendSMSQueue.add(new SMSWarp(phone, content, type, code));

        return true;
    }

    @Override
    public boolean checkPhoneLimit(String phone) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic checkPhoneLimit:" + phone);
        }


        return miscCache.getSMSLimit(phone) <= 5;
    }

    public String getAccessTokenCache(String appId, String appSecret) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getAccessToken appId:" + appId + " appSecret:" + appSecret);
        }

        String accessTokenString = weixinCache.getAccessToken(appId);
        if (StringUtil.isEmpty(accessTokenString)) {
            AccessToken accessToken = getAccessToken(appId, appSecret);
            if (accessToken == null) {
                return null;
            }
            accessTokenString = accessToken.getToken();
            weixinCache.putAccessToken(appId, accessToken.getToken());
        }

        return accessTokenString;
    }


    @Override
    public InterFlowAccount createInterFlowAccount(InterFlowAccount interflowAccount) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic createInterFlowAccount.interflowAccount:" + interflowAccount);
        }
        return writeAbleMiscHandler.createInterFlowAccount(interflowAccount);
    }

    @Override
    public List<InterFlowAccount> queryInterFlowAccount(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic queryInterFlowAccount.queryExpress:" + queryExpress);
        }
        return readonlyMiscHandlersPool.getHandler().queryInterFlowAccount(queryExpress);
    }

    @Override
    public PageRows<InterFlowAccount> queryInterFlowAccountByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic queryInterFlowAccountByPage.queryExpress:" + queryExpress + ",pagination:" + pagination);
        }
        return readonlyMiscHandlersPool.getHandler().queryInterFlowAccountByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyInterFlowAccount(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic modifyInterFlowAccount.updateExpress:" + updateExpress + ",queryExpress:" + queryExpress);
        }
        return writeAbleMiscHandler.modifyInterFlowAccount(updateExpress, queryExpress);
    }

    @Override
    public InterFlowAccount getInterFlowAccount(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("MiscLogic getInterFlowAccount.queryExpress:" + queryExpress);
        }
        return readonlyMiscHandlersPool.getHandler().getInterFlowAccount(queryExpress);
    }

    @Override
    public String saveAdvertiseInfo(String key, String value) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to saveAdvertiseInfo");
        }
        weixinCache.putAdvertiseInfo(key, value);
        return key;
    }

    @Override
    public String getAdvertiseInfo(String key) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to getAdvertiseInfo");
        }
        String value = weixinCache.getAdvertiseInfo(key);
        if (value == null) {
            value = "";
        }
        return value;
    }


    @Override
    public boolean saveRedisMiscValue(String key, String value, int timoutSec) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to saveMiscCode");
        }

        miscRedis.saveRedisMiscValue(key, value, timoutSec);
        return true;
    }

    @Override
    public String getRedisMiscValue(String key) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to saveMiscCode");
        }

        return miscRedis.getRedisMiscValue(key);
    }

    @Override
    public boolean removeRedisMiscValue(String key) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to saveMiscCode");
        }


        return miscRedis.removeRedisMiscValue(key);
    }

    @Override
    public RefreshCMSTiming createRefreshCMSTiming(RefreshCMSTiming refreshCMSTiming) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to createRefreshCMSTiming RefreshCMSTiming=" + refreshCMSTiming);
        }
        return writeAbleMiscHandler.createRefreshCMSTiming(refreshCMSTiming);
    }

    @Override
    public List<RefreshCMSTiming> queryRefreshCMSTiming(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to queryRefreshCMSTiming QueryExpress=" + queryExpress);
        }
        return readonlyMiscHandlersPool.getHandler().queryRefreshCMSTiming(queryExpress);
    }

    @Override
    public PageRows<RefreshCMSTiming> queryRefreshCMSTimingByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to queryRefreshCMSTimingByPage QueryExpress=" + queryExpress);
        }
        return readonlyMiscHandlersPool.getHandler().queryRefreshCMSTimingByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyRefreshCMSTiming(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to modifyRefreshCMSTiming QueryExpress=" + queryExpress);
        }
        return writeAbleMiscHandler.modifyRefreshCMSTiming(updateExpress, queryExpress);
    }

    @Override
    public RefreshCMSTiming getRefreshCMSTiming(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to getRefreshCMSTiming QueryExpress=" + queryExpress);
        }
        return readonlyMiscHandlersPool.getHandler().getRefreshCMSTiming(queryExpress);
    }

    @Override
    public String getTicketCache(String appid, String secret) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getTicketCache appid:" + appid + ",secret:" + secret);
        }
        String ticket = "";
        String access_token = getAccessTokenCache(appid, secret);
        if (!StringUtil.isEmpty(access_token)) {
            ticket = weixinCache.getTicket(access_token);
            if (StringUtil.isEmpty(ticket)) {
                Ticket weixin_ticket = getTicket(access_token);
                if (weixin_ticket != null) {
                    ticket = weixin_ticket.getTicket();
                    weixinCache.putTicket(access_token, weixin_ticket.getTicket());
                }
            }
        }
        return ticket;
    }

    @Override
    public UserLogin getUserLogin(String openid) throws ServiceException {
        return miscRedis.getUserLogin(openid);
    }

    @Override
    public boolean deleteUserLogin(String openid) throws ServiceException {
        return miscRedis.deleteUserLogin(openid);
    }

    /**
     * 获取access_token
     *
     * @param appId     凭证
     * @param appSecret 密匙
     * @return
     */
    private static AccessToken getAccessToken(String appId, String appSecret) {
        AccessToken accessToken = null;
        // 获取access_token的接口地址（GET） 限200（次/天）
        String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        String requestUrl = ACCESS_TOKEN_URL.replace("APPID", appId).replace("APPSECRET", appSecret);
        JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "GET", null);

        try {
            if (jsonObject != null) {
                accessToken = new AccessToken();
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
            }
        } catch (JSONException e) {
            GAlerter.lab("获取token失败 ", e);
        }
        return accessToken;
    }


    /**
     * 获取Ticket
     *
     * @return
     */
    private static Ticket getTicket(String access_token) {
        Ticket ticket = null;
        // 获取access_token的接口地址（GET） 限200（次/天）
        String JS_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";
        JSONObject object = WeixinUtil.httpRequest(JS_TICKET_URL, "GET", null);
        try {
            if (object != null) {
                ticket = new Ticket();
                ticket.setErrcode(object.getString("errcode"));
                ticket.setErrmsg(object.getString("errmsg"));
                ticket.setTicket(object.getString("ticket"));
                ticket.setExpires_in(object.getInt("expires_in"));
            }
        } catch (JSONException e) {
            GAlerter.lab("获取Ticket失败 ", e);
        }
        return ticket;
    }

    @Override
    public List<String> getRedisListKey(String key) throws ServiceException {
        return miscRedis.getRedisListKey(key);
    }

    @Override
    public boolean updateRedisListKey(String key, Collection<String> keys) throws ServiceException {
        return miscRedis.updateRedisListKey(key, keys);
    }

    @Override
    public Map<String, String> hgetAll(String key) throws ServiceException {
        return miscRedis.hgetAll(key);
    }

    @Override
    public Long hset(String key, String field, String value) throws ServiceException {
        return miscRedis.hset(key, field, value);
    }

    @Override
    public String hget(String key, String field) throws ServiceException {
        return miscRedis.hget(key, field);
    }

    @Override
    public Long hdel(String key, String field) throws ServiceException {
        return miscRedis.hdel(key, field);
    }
}
