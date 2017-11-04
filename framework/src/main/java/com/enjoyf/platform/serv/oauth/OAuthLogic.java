/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.oauth;


import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.db.oauth.OAuthHandler;
import com.enjoyf.platform.service.oauth.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>,zx
 */

/**
 * The OAuthLogic class holds the core logic for the server.
 * This class is expected to change almost completely from
 * server to server. <p>
 * <p/>
 */
class OAuthLogic implements OAuthService {
    //
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    private OAuthConfig config;

    //the handler's
    private OAuthHandler writeAbleHandler;
    private HandlerPool<OAuthHandler> readonlyHandlersPool;

    //the auth gameres map cache.
    private AuthAppCache authAppCache;

    private TicketCache ticketCache;

    private UnoTimestampCache timestampCache;

    private static final Long EXPIRE_LONGTIME = 1000L * 60L * 60L * 24L * 7L;

    //
    OAuthLogic(OAuthConfig cfg) {
        config = cfg;

        //initialize the handler.
        try {
            //
            writeAbleHandler = new OAuthHandler(config.getWriteableDataSourceName(), config.getProps());

            //
            readonlyHandlersPool = new HandlerPool<OAuthHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyHandlersPool.add(new OAuthHandler(dsn, config.getProps()));
            }
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());

            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        //clear exipred tokens.
//        Timer tokenExpireClearTimer = new Timer();
//        tokenExpireClearTimer.scheduleAtFixedRate(new TokenClearExpireTask(), config.getExpireTokenClearIntervalMsecs(), config.getExpireTokenClearIntervalMsecs());

        try {
            ticketCache = new TicketCache(config.getMemCachedConfig());
        } catch (Exception e) {
            GAlerter.lab("There isn't ticketCache in the configure." + this.getClass(), e);
        }

        timestampCache = new UnoTimestampCache(config.getMemCachedConfig());
        authAppCache = new AuthAppCache(config.getMemCachedConfig());
    }


    @Override
    public AuthApp getApp(String appId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to getApp, appId:" + appId);
        }
        appId = AppUtil.getAppKey(appId);

        AuthApp returnValue = authAppCache.get(appId);
        if (returnValue == null) {
            returnValue = readonlyHandlersPool.getHandler().getAuthApp(appId);

            if (returnValue != null) {
                authAppCache.put(appId, returnValue);
            }
        }

        return returnValue;
    }

    @Override
    public AuthApp getAppByGameKey(String gamekey) throws ServiceException {
        AuthApp returnValue = authAppCache.getByGameKey(gamekey);
        if (returnValue == null) {
            returnValue = readonlyHandlersPool.getHandler().getAuthAppByGameKey(gamekey);

            if (returnValue != null) {
                authAppCache.put(returnValue.getAppId(), returnValue);
            }
        }
        return returnValue;
    }

    @Override
    public AuthApp appplyAuthApp(AuthApp app) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to appplyAuthApp, app:" + app);
        }

        return writeAbleHandler.createAuthApp(app);
    }

    @Override
    public boolean modifyAuthApp(String appId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to modifyAuthApp, appId:" + appId + ", updateExpress:" + updateExpress);
        }

        boolean returnValue = writeAbleHandler.updateAuthApp(updateExpress, new QueryExpress().add(QueryCriterions.eq(AuthAppField.APPID, appId)));

        if (returnValue) {
            authAppCache.remove(appId);
        }

        return returnValue;
    }

    @Override
    public List<AuthApp> queryAuthApp(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to QueryExpress, queryExpress:" + queryExpress);
        }

        return readonlyHandlersPool.getHandler().queryAuthApp(queryExpress);
    }

    @Override
    public PageRows<AuthApp> queryAuthApp(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to QueryExpress, queryExpress:" + queryExpress);
        }

        return readonlyHandlersPool.getHandler().queryAuthApp(queryExpress, pagination);
    }

    @Override
    public AuthToken applyToken(AuthToken token) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to applyToken, token:" + token);
        }

        return writeAbleHandler.createAuthToken(token);
    }

    @Override
    public AuthToken getToken(String token, AuthTokenType type) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to getToken, token:" + token);
        }

        return readonlyHandlersPool.getHandler().getAuthToken(token);
    }

    @Override
    public boolean removeToken(String token, AuthTokenType type) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to removeToken, token:" + token);
        }

        return writeAbleHandler.removeAuthToken(token);
    }

    @Override
    public Sticket generatorSTicket(String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call cache put sticket:" + uno);
        }

        Sticket sTicket = new Sticket();
        sTicket.setUno(uno);
        sTicket.setSecr(UUID.randomUUID().toString());
        sTicket.setrCode(UUID.randomUUID().toString());

        ticketCache.putSTicket(sTicket);

        return sTicket;
    }

    @Override
    public Sticket getSTicket(String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call cache get and remove sticket:" + uno);
        }

        Sticket sTicket = ticketCache.getSTicket(uno);
        if (sTicket != null) {

            ticketCache.deleteSTicket(uno);

            return sTicket;
        }

        return null;
    }

    //验证用户接口登录
    @Override
    public TimestampVerification getTimestampByUno(String uno, String timeStamp) {
        if (logger.isDebugEnabled()) {
            logger.debug("call unocache get timestamp by uno : " + uno + " timeStamp: " + timeStamp);
        }

        return timestampCache.getTimestamp(uno, timeStamp);
    }

    @Override
    public TimestampVerification saveTimestamp(TimestampVerification verification) {
        if (logger.isDebugEnabled()) {
            logger.debug("call unocache verification:" + verification);
        }

        timestampCache.putTimestamp(verification);
        return verification;
    }

    class TokenClearExpireTask extends TimerTask {
        //
        public void run() {
            logger.info("TokenClearExpireTask start to clear expired token.");

            //clear the expired tokens
            try {
                writeAbleHandler.clearExpireTokens();
            } catch (Exception e) {
                //
                GAlerter.lab("TokenClearExpireTask clearExpireTokens error.", e);
            }

            logger.info("TokenClearExpireTask finish to clear expired token.");
        }
    }

    @Override
    public OAuthInfo getOAuthInfoByAccessToken(String accessToken, String appKey, String isInterceptor) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call cache get and remove OAuthInfo:" + accessToken);
        }

        OAuthInfo returnValue = ticketCache.getOauthInfo(accessToken, appKey);
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(OAuthInfoField.ACCESS_TOKEN, accessToken));
        queryExpress.add(QueryCriterions.eq(OAuthInfoField.APPKEY, appKey));
        if (returnValue == null) {
            returnValue = readonlyHandlersPool.getHandler().getAccess(queryExpress);
        }

        if (returnValue != null) {
            //如果时间超过2分钟，更新 EXPIRE_LONGTIME
            if (returnValue.getExpire_longtime() + 1000L * 60L * 15L < System.currentTimeMillis() && isInterceptor.equals("0")) {
                UpdateExpress updateExpress = new UpdateExpress();
                Long longtime = System.currentTimeMillis();
                updateExpress.set(OAuthInfoField.EXPIRE_LONGTIME, longtime);
                returnValue.setExpire_longtime(longtime);
                writeAbleHandler.update(updateExpress, queryExpress);
            }
            ticketCache.putOAuthInfo(returnValue);
        }

        return returnValue;
    }

    @Override
    public OAuthInfo getOAuthInfoByRereshToken(String refreshToken, String appKey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call cache get and remove OAuthInfo:" + refreshToken);
        }

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(OAuthInfoField.REFRESH_TOKEN, refreshToken));
        queryExpress.add(QueryCriterions.eq(OAuthInfoField.APPKEY, appKey));
        String accessToken = UUID.randomUUID().toString().replaceAll("-", "0").replaceAll("_", "0");

        OAuthInfo returnValue = readonlyHandlersPool.getHandler().getRefresh(queryExpress);
        if (returnValue != null) {
            String newrefreshToken = UUID.randomUUID().toString().replaceAll("-", "0").replaceAll("_", "0");
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(OAuthInfoField.ACCESS_TOKEN, accessToken);
            Long longtime = System.currentTimeMillis();
            updateExpress.set(OAuthInfoField.EXPIRE_LONGTIME, longtime);
            updateExpress.set(OAuthInfoField.REFRESH_TOKEN, newrefreshToken);
            writeAbleHandler.update(updateExpress, queryExpress);

            returnValue.setRefresh_token(newrefreshToken);
            returnValue.setExpire_longtime(longtime);
            //删除之前的缓
            ticketCache.deleteOAuthInfo(returnValue.getAccess_token(), returnValue.getApp_key());
            returnValue.setAccess_token(accessToken);
            //放入新的缓存
            ticketCache.putOAuthInfo(returnValue);
        }

        return returnValue;
    }

    @Override
    public OAuthInfo create(OAuthInfo oAuthInfo) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call cache get and remove OAuthInfo:" + oAuthInfo);
        }
        OAuthInfo returnValue = writeAbleHandler.insert(oAuthInfo);
        if (returnValue != null) {
            ticketCache.putOAuthInfo(returnValue);
        }
        return returnValue;
    }

    public OAuthInfo generaterOauthInfo(String uno, String appKey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call cache get generaterOauthInfo remove uno:" + uno + ":appKey--" + appKey);
        }
        OAuthInfo oAuthInfo = new OAuthInfo();
        oAuthInfo.setUno(uno);
        oAuthInfo.setApp_key(appKey);


        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(OAuthInfoField.UNO, uno));
        queryExpress.add(QueryCriterions.eq(OAuthInfoField.APPKEY, appKey));
        List<OAuthInfo> list = writeAbleHandler.query(queryExpress);
        if (list.size() > 0) {
            UpdateExpress updateExpress = new UpdateExpress();
            Long longtime = System.currentTimeMillis();
            updateExpress.set(OAuthInfoField.EXPIRE_LONGTIME, longtime);
            writeAbleHandler.update(updateExpress, queryExpress);
            ticketCache.putOAuthInfo(list.get(0));
            return list.get(0);
        }
        OAuthInfo returnValue = writeAbleHandler.insert(oAuthInfo);
        if (returnValue != null) {
            ticketCache.putOAuthInfo(returnValue);
        }
        return returnValue;
    }


    @Override
    public String getSocialAPI(String uno, String time) throws ServiceException {
        String socialAPI = ticketCache.getSocialAPI(uno, time);
        if (StringUtil.isEmpty(socialAPI)) {
            ticketCache.putSocialAPI(uno, time);
        }
        return socialAPI;
    }

    @Override
    public boolean removeSocialAPI(String uno, String time) throws ServiceException {
        return ticketCache.removeSocialAPI(uno, time);
    }


    /////////////////////////////////////////////////////

    @Override
    public GameChannelInfo createGameChannelInfo(GameChannelInfo info) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("createGameChannelInfo info:" + info);
        }

        info = writeAbleHandler.insertAuthAppChannelInfo(info);

        authAppCache.putChannelInfo(info);

        return info;
    }

    @Override
    public List<GameChannelInfo> queryGameChannelInfo(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryGameChannelInfo queryExpress:" + queryExpress);
        }

        return readonlyHandlersPool.getHandler().queryAuthAppChannelInfo(queryExpress);
    }

    @Override
    public GameChannelInfo getGameChannelInfo(String infoId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryGameChannelInfo getGameChannelInfo:" + infoId);
        }

        GameChannelInfo info = authAppCache.getChannelInfo(infoId);
        if (info == null) {
            info = readonlyHandlersPool.getHandler().getAuthAppChannelInfo(infoId);

            if (info != null) {
                authAppCache.putChannelInfo(info);
            }
        }

        return info;
    }

    @Override
    public boolean modifyGameChannelInfo(UpdateExpress updateExpress, String infoId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("modifyGameChannelInfo updateExpress:" + updateExpress + " infoId:" + infoId);
        }

        boolean bval = writeAbleHandler.updateAuthAppChannelInfo(updateExpress, infoId);
        if (bval) {
            authAppCache.removeChannelInfo(infoId);
        }

        return bval;
    }

    @Override
    public boolean deleteGameChannelInfo(String infoId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("deleteGameChannelInfo updateExpress:" + infoId);
        }

        boolean bval = writeAbleHandler.deleteAuthAppChannelInfo(infoId);
        if (bval) {
            authAppCache.removeChannelInfo(infoId);
        }

        return bval;
    }


    @Override
    public GameChannelConfig createGameChannelConfig(GameChannelConfig config) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("createGameChannelConfig config:" + config);
        }

        config = writeAbleHandler.insertGameChannelConfig(config);

        authAppCache.putChannelConfig(config);

        return config;
    }

    @Override
    public PageRows<GameChannelConfig> queryGameChannelConfig(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryGameChannelConfig queryExpress:" + queryExpress + " pagination:" + pagination);
        }

        return readonlyHandlersPool.getHandler().queryGameChannelConfig(queryExpress, pagination);
    }

    @Override
    public GameChannelConfig getGameChannelConfig(String configId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getGameChannelConfig configId:" + configId);
        }

        GameChannelConfig config = authAppCache.getChannelConfig(configId);
        if (config == null) {
            config = readonlyHandlersPool.getHandler().getGameChannelConfig(configId);

            if (config != null) {
                authAppCache.putChannelConfig(config);
            }
        }

        return config;
    }

    @Override
    public boolean modifyGameChannelConfig(UpdateExpress updateExpress, String configId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("modifyGameChannelConfig updateExpress:" + updateExpress + " configId:" + configId);
        }

        boolean bval = writeAbleHandler.updateGameChannelConfig(updateExpress, configId);
        if (bval) {
            authAppCache.removeChannelConfig(configId);
        }

        return bval;
    }

    @Override
    public boolean deleteGameChannelConfig(String configId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("deleteGameChannelConfig configId:" + configId);
        }

        boolean bval = writeAbleHandler.deleteGameChannelConfig(configId);
        if (bval) {
            authAppCache.removeChannelConfig(configId);
        }

        return bval;
    }
}
