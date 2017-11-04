package com.enjoyf.platform.service.oauth;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.List;

class OAuthServiceCache implements OAuthService {
    //
    private OAuthService service = null;

    //
    public OAuthServiceCache(OAuthService service) {
        this.service = service;
    }

    @Override
    public AuthApp getApp(String appId) throws ServiceException {
        return service.getApp(appId);
    }

    @Override
    public AuthApp getAppByGameKey(String gamekey) throws ServiceException {
        return service.getAppByGameKey(gamekey);
    }

    @Override
    public AuthApp appplyAuthApp(AuthApp app) throws ServiceException {
        return service.appplyAuthApp(app);
    }

    @Override
    public boolean modifyAuthApp(String appId, UpdateExpress updateExpress) throws ServiceException {
        return service.modifyAuthApp(appId, updateExpress);
    }

    @Override
    public AuthToken applyToken(AuthToken token) throws ServiceException {
        return service.applyToken(token);
    }

    @Override
    public AuthToken getToken(String token, AuthTokenType type) throws ServiceException {
        return service.getToken(token, type);
    }

    @Override
    public boolean removeToken(String token, AuthTokenType type) throws ServiceException {
        return service.removeToken(token, type);
    }

    @Override
    public List<AuthApp> queryAuthApp(QueryExpress queryExpress) throws ServiceException {
        //todo
        return service.queryAuthApp(queryExpress);
    }

    @Override
    public PageRows<AuthApp> queryAuthApp(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        return service.queryAuthApp(queryExpress, pagination);
    }

    @Override
    public Sticket generatorSTicket(String uno) throws ServiceException {
        return service.generatorSTicket(uno);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Sticket getSTicket(String uno) throws ServiceException {
        return service.getSTicket(uno);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TimestampVerification getTimestampByUno(String uno, String timestamp) throws ServiceException {
        return service.getTimestampByUno(uno, timestamp);
    }

    @Override
    public TimestampVerification saveTimestamp(TimestampVerification timestampVerification) throws ServiceException {
        return service.saveTimestamp(timestampVerification);
    }

    public OAuthInfo getOAuthInfoByAccessToken(String accessToken, String appKey, String isInterceptor) throws ServiceException {
        return service.getOAuthInfoByAccessToken(accessToken, appKey, isInterceptor);
    }

    @Override
    public OAuthInfo getOAuthInfoByRereshToken(String refreshToken, String appKey) throws ServiceException {
        return service.getOAuthInfoByRereshToken(refreshToken, appKey);
    }

    @Override
    public OAuthInfo create(OAuthInfo oAuthInfo) throws ServiceException {
        return service.create(oAuthInfo);
    }

    @Override
    public OAuthInfo generaterOauthInfo(String uno, String appKey) throws ServiceException {
        return service.generaterOauthInfo(uno, appKey);
    }


    @Override
    public String getSocialAPI(String uno, String time) throws ServiceException {
        return service.getSocialAPI(uno, time);
    }

    @Override
    public boolean removeSocialAPI(String uno, String time) throws ServiceException {
        return service.removeSocialAPI(uno, time);
    }

    @Override
    public GameChannelInfo createGameChannelInfo(GameChannelInfo info) throws ServiceException {
        return service.createGameChannelInfo(info);
    }

    @Override
    public List<GameChannelInfo> queryGameChannelInfo(QueryExpress queryExpress) throws ServiceException {
        return service.queryGameChannelInfo(queryExpress);
    }

    @Override
    public GameChannelInfo getGameChannelInfo(String infoId) throws ServiceException {
        return service.getGameChannelInfo(infoId);
    }

    @Override
    public boolean modifyGameChannelInfo(UpdateExpress updateExpress, String infoId) throws ServiceException {
        return service.modifyGameChannelInfo(updateExpress, infoId);
    }

    @Override
    public boolean deleteGameChannelInfo(String infoId) throws ServiceException {
        return service.deleteGameChannelInfo(infoId);
    }

    @Override
    public GameChannelConfig createGameChannelConfig(GameChannelConfig config) throws ServiceException {
        return service.createGameChannelConfig(config);
    }

    @Override
    public PageRows<GameChannelConfig> queryGameChannelConfig(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        return service.queryGameChannelConfig(queryExpress,pagination);
    }

    @Override
    public GameChannelConfig getGameChannelConfig(String configId) throws ServiceException {
        return service.getGameChannelConfig(configId);
    }

    @Override
    public boolean modifyGameChannelConfig(UpdateExpress updateExpress, String configId) throws ServiceException {
        return service.modifyGameChannelConfig(updateExpress,configId);
    }

    @Override
    public boolean deleteGameChannelConfig(String configId) throws ServiceException {
        return service.deleteGameChannelConfig(configId);
    }
}
