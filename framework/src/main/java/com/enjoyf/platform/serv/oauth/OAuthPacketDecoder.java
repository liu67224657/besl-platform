/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.oauth;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.oauth.*;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.io.Serializable;
/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */

/**
 * This interface receives packets from the remote clients. It
 * translates them into method calls into the business logic.
 * <p/>
 * The logicProcess() method may be called reentrantly, so it and any
 * handlers must be thread-safe.
 */
class OAuthPacketDecoder extends PacketDecoder {
    private OAuthLogic processLogic;

    /**
     * Constructor takes the UserPropsLogic object that we're going to use
     * to logicProcess the packets.
     *
     * @param logic our logical friend
     */
    OAuthPacketDecoder(OAuthLogic logic) {
        processLogic = logic;

        setTransContainer(OAuthConstants.getTransContainer());
    }

    /**
     * Called when ThreadSampleInfo packet arrives. This routine will
     * just forward the call to the logic object which
     * will take care of actually decoding the packet.
     */
    protected WPacket logicProcess(ConnThreadBase conn, RPacket rPacket) throws ServiceException {
        byte type = rPacket.getType();

        WPacket wp = new WPacket();
        wp.writeByteNx(ServiceConstants.OK);

        switch (type) {
            //
            case OAuthConstants.APP_APPLY:
                wp.writeSerializable(processLogic.appplyAuthApp((AuthApp) rPacket.readSerializable()));

                break;
            case OAuthConstants.APP_GET:
                wp.writeSerializable(processLogic.getApp(rPacket.readString()));

                break;
            case OAuthConstants.APP_MODIFY:
                wp.writeBooleanNx(processLogic.modifyAuthApp(rPacket.readString(), (UpdateExpress) rPacket.readSerializable()));

                break;
            case OAuthConstants.APP_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryAuthApp((QueryExpress) rPacket.readSerializable()));
                break;
            case OAuthConstants.APP_QUERY_PAGE:
                wp.writeSerializable(processLogic.queryAuthApp((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;

            //
            case OAuthConstants.TOKEN_APPLY:
                wp.writeSerializable(processLogic.applyToken((AuthToken) rPacket.readSerializable()));

                break;
            case OAuthConstants.TOKEN_GET:
                wp.writeSerializable(processLogic.getToken(rPacket.readString(), (AuthTokenType) rPacket.readSerializable()));

                break;
            case OAuthConstants.TOKEN_REMOVE:
                wp.writeBooleanNx(processLogic.removeToken(rPacket.readString(), (AuthTokenType) rPacket.readSerializable()));

                break;
            case OAuthConstants.GENERATOR_STICKET:
                wp.writeSerializable(processLogic.generatorSTicket(rPacket.readStringUTF()));

                break;
            case OAuthConstants.GET_STICKET:
                wp.writeSerializable(processLogic.getSTicket(rPacket.readStringUTF()));
                break;

            case OAuthConstants.GET_UNO_TIMESTAMP:
                wp.writeSerializable(processLogic.getTimestampByUno(rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case OAuthConstants.TOKEN_CREATE:
                wp.writeSerializable(processLogic.create((OAuthInfo) rPacket.readSerializable()));

                break;
            case OAuthConstants.TOKEN_GET_ACCESSTOKEN:
                wp.writeSerializable(processLogic.getOAuthInfoByAccessToken(rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case OAuthConstants.SAVE_UNO_TIMESTAMP:
                wp.writeSerializable(processLogic.saveTimestamp((TimestampVerification) rPacket.readSerializable()));
                break;
            case OAuthConstants.TOKEN_GET_REFESHTOKEN:
                wp.writeSerializable(processLogic.getOAuthInfoByRereshToken(rPacket.readStringUTF(), rPacket.readStringUTF()));

                break;
            case OAuthConstants.GENERATER_OAUTHINFO:
                wp.writeSerializable(processLogic.generaterOauthInfo(rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case OAuthConstants.GET_SOCIALAPI:
                wp.writeSerializable(processLogic.getSocialAPI(rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case OAuthConstants.REMOVE_SOCIALAPI:
                wp.writeSerializable(processLogic.removeSocialAPI(rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case OAuthConstants.APP_GET_GAMEKEY:
                wp.writeSerializable(processLogic.getAppByGameKey(rPacket.readStringUTF()));
                break;


            case OAuthConstants.CHANNELINFO_CREATE:
                wp.writeSerializable(processLogic.createGameChannelInfo((GameChannelInfo) rPacket.readSerializable()));
                break;
            case OAuthConstants.CHANNELINFO_DELETE:
                wp.writeBooleanNx(processLogic.deleteGameChannelInfo(rPacket.readStringUTF()));
                break;
            case OAuthConstants.CHANNELINFO_UPDATE:
                wp.writeBooleanNx(processLogic.modifyGameChannelInfo((UpdateExpress) rPacket.readSerializable(), rPacket.readStringUTF()));
                break;
            case OAuthConstants.CHANNELINFO_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryGameChannelInfo((QueryExpress) rPacket.readSerializable()));
                break;
            case OAuthConstants.CHANNELINFO_GET:
                wp.writeSerializable(processLogic.getGameChannelInfo(rPacket.readStringUTF()));
                break;


            case OAuthConstants.CHANNELCONFIG_CREATE:
                wp.writeSerializable(processLogic.createGameChannelConfig((GameChannelConfig) rPacket.readSerializable()));
                break;
            case OAuthConstants.CHANNELCONFIG_DELETE:
                wp.writeBooleanNx(processLogic.deleteGameChannelConfig(rPacket.readStringUTF()));
                break;
            case OAuthConstants.CHANNELCONFIG_UPDATE:
                wp.writeBooleanNx(processLogic.modifyGameChannelConfig((UpdateExpress) rPacket.readSerializable(), rPacket.readStringUTF()));
                break;
            case OAuthConstants.CHANNELCONFIG_QUERY:
                wp.writeSerializable(processLogic.queryGameChannelConfig((QueryExpress) rPacket.readSerializable(),(Pagination)rPacket.readSerializable()));
                break;
            case OAuthConstants.CHANNELCONFIG_GET:
                wp.writeSerializable(processLogic.getGameChannelConfig(rPacket.readStringUTF()));
                break;
            //
            default:
                GAlerter.lab("OAuthPacketDecoder.logicProcess: Unrecognized type: " + type);
                throw new ServiceException(ServiceException.BAD_PACKET);
        }

        return wp;
    }
}
