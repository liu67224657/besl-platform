/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.oauth;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-10-18 上午11:29
 * Description:
 */
public interface OAuthService {
    ///////////////////////////////////////
    //the auth app apis
    ///////////////////////////////////////
    //get the app by the appid
    public AuthApp getApp(String appId) throws ServiceException;

    //get the app by the appid
    public AuthApp getAppByGameKey(String gamekey) throws ServiceException;

    //apply a new auth app
    public AuthApp appplyAuthApp(AuthApp app) throws ServiceException;

    //modify the auth app.
    public boolean modifyAuthApp(String appId, UpdateExpress updateExpress) throws ServiceException;


    public PageRows<AuthApp> queryAuthApp(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public List<AuthApp> queryAuthApp(QueryExpress queryExpress) throws ServiceException;


    ///////////////////////////////////////
    //the auth token apis
    ///////////////////////////////////////
    //apply a token which is created.
    public AuthToken applyToken(AuthToken token) throws ServiceException;

    //get the token by the token key.
    public AuthToken getToken(String token, AuthTokenType type) throws ServiceException;

    //remove the token.
    public boolean removeToken(String token, AuthTokenType type) throws ServiceException;


    ///////////////////////////////////////
    //the ticket apis
    ///////////////////////////////////////

    /**
     * 生成ticket放入memcached
     *
     * @param uno
     * @return
     * @throws ServiceException
     */
    public Sticket generatorSTicket(String uno) throws ServiceException;

    /**
     * 一次性使用get后删除
     *
     * @param uno
     * @return
     * @throws ServiceException
     */
    public Sticket getSTicket(String uno) throws ServiceException;


    public TimestampVerification getTimestampByUno(String uno, String timestamp) throws ServiceException;

    public TimestampVerification saveTimestamp(TimestampVerification timestampVerification) throws ServiceException;

    /**
     * 根据accessToken和appenKey 获取UNO
     *
     * @param accessToken
     * @param appKey
     * @return
     * @throws ServiceException
     */
    public OAuthInfo getOAuthInfoByAccessToken(String accessToken, String appKey, String isInterceptor) throws ServiceException;

    /**
     * 根据refreshToken和appenKey 获取UNO
     *
     * @param refreshToken
     * @param appKey
     * @return
     * @throws ServiceException
     */
    public OAuthInfo getOAuthInfoByRereshToken(String refreshToken, String appKey) throws ServiceException;

    /**
     * 创建 OAuthInfo
     *
     * @param oAuthInfo
     * @return
     * @throws ServiceException
     */
    public OAuthInfo create(OAuthInfo oAuthInfo) throws ServiceException;


    /**
     *
     */
    public OAuthInfo generaterOauthInfo(String uno, String appKey) throws ServiceException;


    //////////////SocialAPIInterceptor//////////////////////
    public String getSocialAPI(String uno, String time) throws ServiceException;

    public boolean removeSocialAPI(String uno, String time) throws ServiceException;


    /////////////////////////////////////////////////////
    public GameChannelInfo createGameChannelInfo(GameChannelInfo info) throws ServiceException;

    public List<GameChannelInfo> queryGameChannelInfo(QueryExpress queryExpress) throws ServiceException;

    public GameChannelInfo getGameChannelInfo(String infoId) throws ServiceException;

    public boolean modifyGameChannelInfo(UpdateExpress updateExpress, String infoId) throws ServiceException;

    public boolean deleteGameChannelInfo(String infoId) throws ServiceException;


    public GameChannelConfig createGameChannelConfig(GameChannelConfig config) throws ServiceException;

    public PageRows<GameChannelConfig> queryGameChannelConfig(QueryExpress queryExpress,Pagination pagination) throws ServiceException;

    public GameChannelConfig getGameChannelConfig(String configId) throws ServiceException;

    public boolean modifyGameChannelConfig(UpdateExpress updateExpress, String configId) throws ServiceException;

    public boolean deleteGameChannelConfig(String configId) throws ServiceException;
}
