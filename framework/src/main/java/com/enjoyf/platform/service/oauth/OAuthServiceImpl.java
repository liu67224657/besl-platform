package com.enjoyf.platform.service.oauth;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.List;

class OAuthServiceImpl implements OAuthService {
    //
    private ReqProcessor reqProcessor = null;
    private int numOfPartitions;

    //
    public OAuthServiceImpl(ServiceConfig scfg) {
        if (scfg == null) {
            throw new RuntimeException("OAuthServiceImpl.ctor: ServiceConfig is null!");
        }

        //
        reqProcessor = scfg.getReqProcessor();
        numOfPartitions = EnvConfig.get().getServicePartitionNum(OAuthConstants.SERVICE_SECTION);
    }


    @Override
    public AuthApp getApp(String appId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(appId);

        Request req = new Request(OAuthConstants.APP_GET, wp);
        req.setPartition(Math.abs(appId.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (AuthApp) rp.readSerializable();
    }

    @Override
    public AuthApp getAppByGameKey(String gamekey) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(gamekey);

        Request req = new Request(OAuthConstants.APP_GET_GAMEKEY, wp);
        req.setPartition(Math.abs(gamekey.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (AuthApp) rp.readSerializable();
    }

    @Override
    public AuthApp appplyAuthApp(AuthApp app) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(app);

        Request req = new Request(OAuthConstants.APP_APPLY, wp);
        req.setPartition(Math.abs(app.getAppId().hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (AuthApp) rp.readSerializable();
    }

    @Override
    public boolean modifyAuthApp(String appId, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(appId);
        wp.writeSerializable(updateExpress);

        Request req = new Request(OAuthConstants.APP_MODIFY, wp);
        req.setPartition(Math.abs(appId.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public AuthToken applyToken(AuthToken token) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(token);

        Request req = new Request(OAuthConstants.TOKEN_APPLY, wp);
        req.setPartition(Math.abs(token.getToken().hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (AuthToken) rp.readSerializable();
    }

    @Override
    public AuthToken getToken(String token, AuthTokenType type) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(token);
        wp.writeSerializable(type);

        Request req = new Request(OAuthConstants.TOKEN_GET, wp);
        req.setPartition(Math.abs(token.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (AuthToken) rp.readSerializable();
    }

    @Override
    public boolean removeToken(String token, AuthTokenType type) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(token);
        wp.writeSerializable(type);

        Request req = new Request(OAuthConstants.TOKEN_REMOVE, wp);
        req.setPartition(Math.abs(token.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public List<AuthApp> queryAuthApp(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request req = new Request(OAuthConstants.APP_QUERY, wp);
        req.setPartition(Math.abs(queryExpress.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (List<AuthApp>) rp.readSerializable();
    }

    @Override
    public PageRows<AuthApp> queryAuthApp(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);

        Request req = new Request(OAuthConstants.APP_QUERY_PAGE, wp);
        req.setPartition(Math.abs(queryExpress.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<AuthApp>) rp.readSerializable();
    }

    @Override
    public Sticket generatorSTicket(String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);

        Request req = new Request(OAuthConstants.GENERATOR_STICKET, wp);
        req.setPartition(Math.abs(uno.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (Sticket) rp.readSerializable();
    }

    @Override
    public Sticket getSTicket(String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);

        Request req = new Request(OAuthConstants.GET_STICKET, wp);
        req.setPartition(Math.abs(uno.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (Sticket) rp.readSerializable();
    }

    @Override
    public TimestampVerification getTimestampByUno(String uno, String timestamp) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        wp.writeStringUTF(timestamp);

        Request req = new Request(OAuthConstants.GET_UNO_TIMESTAMP, wp);
        req.setPartition(Math.abs(uno.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (TimestampVerification) rp.readSerializable();
    }

    @Override
    public TimestampVerification saveTimestamp(TimestampVerification timestampVerification) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(timestampVerification);

        Request req = new Request(OAuthConstants.SAVE_UNO_TIMESTAMP, wp);
        req.setPartition(Math.abs(timestampVerification.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (TimestampVerification) rp.readSerializable();
    }

    public OAuthInfo getOAuthInfoByAccessToken(String accessToken, String appKey, String isInterceptor) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(accessToken);
        wp.writeStringUTF(appKey);
        wp.writeStringUTF(isInterceptor);
        Request req = new Request(OAuthConstants.TOKEN_GET_ACCESSTOKEN, wp);

        RPacket rp = reqProcessor.process(req);
        return (OAuthInfo) rp.readSerializable();
    }

    @Override
    public OAuthInfo getOAuthInfoByRereshToken(String refreshToken, String appKey) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(refreshToken);
        wp.writeStringUTF(appKey);

        Request req = new Request(OAuthConstants.TOKEN_GET_REFESHTOKEN, wp);
        RPacket rp = reqProcessor.process(req);
        return (OAuthInfo) rp.readSerializable();
    }

    @Override
    public OAuthInfo create(OAuthInfo oAuthInfo) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(oAuthInfo);

        Request req = new Request(OAuthConstants.TOKEN_CREATE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (OAuthInfo) rPacket.readSerializable();
    }

    @Override
    public OAuthInfo generaterOauthInfo(String uno, String appKey) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        wp.writeStringUTF(appKey);
        Request req = new Request(OAuthConstants.GENERATER_OAUTHINFO, wp);
        RPacket rp = reqProcessor.process(req);
        return (OAuthInfo) rp.readSerializable();
    }


    @Override
    public String getSocialAPI(String uno, String time) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        wp.writeStringUTF(time);
        Request req = new Request(OAuthConstants.GET_SOCIALAPI, wp);
        RPacket rp = reqProcessor.process(req);
        return (String) rp.readSerializable();
    }

    @Override
    public boolean removeSocialAPI(String uno, String time) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        wp.writeStringUTF(time);
        Request req = new Request(OAuthConstants.REMOVE_SOCIALAPI, wp);
        RPacket rp = reqProcessor.process(req);
        return (Boolean) rp.readSerializable();
    }

    @Override
    public GameChannelInfo createGameChannelInfo(GameChannelInfo info) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(info);
        Request req = new Request(OAuthConstants.CHANNELINFO_CREATE, wp);
        RPacket rp = reqProcessor.process(req);
        return (GameChannelInfo) rp.readSerializable();
    }

    @Override
    public List<GameChannelInfo> queryGameChannelInfo(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(OAuthConstants.CHANNELINFO_QUERY, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<GameChannelInfo>) rp.readSerializable();
    }

    @Override
    public GameChannelInfo getGameChannelInfo(String infoId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(infoId);
        Request req = new Request(OAuthConstants.CHANNELINFO_GET, wp);
        RPacket rp = reqProcessor.process(req);
        return (GameChannelInfo) rp.readSerializable();
    }

    @Override
    public boolean modifyGameChannelInfo(UpdateExpress updateExpress, String infoId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeStringUTF(infoId);
        Request req = new Request(OAuthConstants.CHANNELINFO_UPDATE, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public boolean deleteGameChannelInfo(String infoId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(infoId);
        Request req = new Request(OAuthConstants.CHANNELINFO_DELETE, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }


    @Override
    public GameChannelConfig createGameChannelConfig(GameChannelConfig config) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(config);
        Request req = new Request(OAuthConstants.CHANNELCONFIG_CREATE, wp);
        RPacket rp = reqProcessor.process(req);
        return (GameChannelConfig) rp.readSerializable();
    }

    @Override
    public PageRows<GameChannelConfig> queryGameChannelConfig(QueryExpress queryExpress,Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(OAuthConstants.CHANNELCONFIG_QUERY, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<GameChannelConfig>) rp.readSerializable();
    }

    @Override
    public GameChannelConfig getGameChannelConfig(String configId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(configId);
        Request req = new Request(OAuthConstants.CHANNELCONFIG_GET, wp);
        RPacket rp = reqProcessor.process(req);
        return (GameChannelConfig) rp.readSerializable();
    }

    @Override
    public boolean modifyGameChannelConfig(UpdateExpress updateExpress, String configId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeStringUTF(configId);
        Request req = new Request(OAuthConstants.CHANNELCONFIG_UPDATE, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public boolean deleteGameChannelConfig(String configId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(configId);
        Request req = new Request(OAuthConstants.CHANNELCONFIG_DELETE, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }
}
