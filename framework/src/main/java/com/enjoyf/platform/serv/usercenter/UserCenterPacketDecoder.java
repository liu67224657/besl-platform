/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.usercenter;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.usercenter.UserPrivacy;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.redis.ScoreRange;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
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
class UserCenterPacketDecoder extends PacketDecoder {
    private UserCenterLogic processLogic;

    /**
     * Constructor takes the UserPropsLogic object that we're going to use
     * to logicProcess the packets.
     *
     * @param logic our logical friend
     */
    UserCenterPacketDecoder(UserCenterLogic logic) {
        processLogic = logic;

        setTransContainer(UserCenterConstants.getTransContainer());
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
            case UserCenterConstants.GET_USERLOGIN:
                wp.writeSerializable(processLogic.getUserLoginByLoginKey(rPacket.readStringUTF(), (LoginDomain) rPacket.readSerializable()));
                break;
            case UserCenterConstants.GET_USERLOGIN_BY_LOGINID:
                wp.writeSerializable(processLogic.getUserLoginByLoginId(rPacket.readStringUTF()));
                break;
            case UserCenterConstants.QUERY_USERLOGIN_BY_UNO_LOGINDOMAINS:
                wp.writeSerializable((Serializable) processLogic.queryUserLoginUno(rPacket.readStringUTF(), (java.util.Set<LoginDomain>) rPacket.readSerializable()));
                break;
            case UserCenterConstants.AUTH:
                wp.writeSerializable(processLogic.auth(rPacket.readStringUTF(), (LoginDomain) rPacket.readSerializable(), (TokenInfo) rPacket.readSerializable(), rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF(), (Date) rPacket.readSerializable(), (HashMap<String, String>) rPacket.readSerializable()));
                break;
            case UserCenterConstants.GET_AUTHPROFILE_BY_UID:
                wp.writeSerializable(processLogic.getAuthProfileByUid(rPacket.readLongNx(), (HashMap<String, String>) rPacket.readSerializable()));
                break;
            case UserCenterConstants.BIND:
                wp.writeSerializable(processLogic.bind(rPacket.readStringUTF(), rPacket.readStringUTF(), (LoginDomain) rPacket.readSerializable(), rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF(), (Date) rPacket.readSerializable(), rPacket.readStringUTF(), rPacket.readStringUTF(), (HashMap<String, String>) rPacket.readSerializable()));
                break;
            case UserCenterConstants.UNBIND:
                wp.writeSerializable(processLogic.unbind((LoginDomain) rPacket.readSerializable(), rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF(), (Date) rPacket.readSerializable()));
                break;
            case UserCenterConstants.REGISTER:
                wp.writeSerializable(processLogic.register(rPacket.readStringUTF(), rPacket.readStringUTF(), (LoginDomain) rPacket.readSerializable(), rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF(), (Date) rPacket.readSerializable(), (HashMap<String, String>) rPacket.readSerializable()));
                break;
            case UserCenterConstants.LOGIN:
                wp.writeSerializable(processLogic.login(rPacket.readStringUTF(), rPacket.readStringUTF(), (LoginDomain) rPacket.readSerializable(), rPacket.readStringUTF(), rPacket.readStringUTF(), (Date) rPacket.readSerializable(), (HashMap<String, String>) rPacket.readSerializable()));
                break;
            case UserCenterConstants.DELETE_TOKEN:
                wp.writeSerializable(processLogic.deleteToken(rPacket.readStringUTF()));
                break;
            case UserCenterConstants.GET_TOKEN:
                wp.writeSerializable(processLogic.getToken(rPacket.readStringUTF()));
                break;
            case UserCenterConstants.GET_PROFILE_BYUID:
                wp.writeSerializable(processLogic.getProfileByUid(rPacket.readLongNx()));
                break;
            case UserCenterConstants.GET_PROFILE_BYUNO:
                wp.writeSerializable(processLogic.getProfileByUno(rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case UserCenterConstants.GET_PROFILE_BYPROFILEID:
                wp.writeSerializable(processLogic.getProfileByProfileId(rPacket.readStringUTF()));
                break;
            case UserCenterConstants.QUERY_PROFILE_PROFILES:
                wp.writeSerializable((java.io.Serializable) processLogic.queryProfiles((java.util.Set<String>) rPacket.readSerializable()));
                break;
            case UserCenterConstants.MODIFY_USERLOGIN:
                wp.writeBooleanNx(processLogic.modifyUserLogin((UpdateExpress) rPacket.readSerializable(), rPacket.readStringUTF()));
                break;
            case UserCenterConstants.MODIFY_PROFILE:
                wp.writeBooleanNx(processLogic.modifyProfile((UpdateExpress) rPacket.readSerializable(), rPacket.readStringUTF()));
                break;
            case UserCenterConstants.GET_PROFILE_BYNICK:
                wp.writeSerializable(processLogic.getProfileByNick(rPacket.readStringUTF()));
                break;
            case UserCenterConstants.GET_PROFILE_BYDOMAIN:
                wp.writeSerializable(processLogic.getProfileByDomain(rPacket.readStringUTF()));
                break;

            case UserCenterConstants.SAVE_CODE_FOR_MEMCACHED:
                wp.writeStringUTF(processLogic.saveMobileCode(rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case UserCenterConstants.GET_CODE_FOR_MEMCACHED:
                wp.writeSerializable(processLogic.getMobileCode(rPacket.readStringUTF()));
                break;
            case UserCenterConstants.DELETE_CODE_FOR_MEMCACHED:
                wp.writeBooleanNx(processLogic.removeMobileCode(rPacket.readStringUTF()));
                break;

            case UserCenterConstants.SAVE_PASSWORD_TIME:
                wp.writeBooleanNx(processLogic.savePasswordCode(rPacket.readStringUTF(), (Long) rPacket.readSerializable()));
                break;
            case UserCenterConstants.GET_PASSWORD_TIME:
                wp.writeSerializable(processLogic.getPassordCode(rPacket.readStringUTF()));
                break;
            case UserCenterConstants.REMOVE_PASSWORD_TIME:
                wp.writeBooleanNx(processLogic.removePasswordCode(rPacket.readStringUTF()));
                break;
            case UserCenterConstants.GET_USERACCOUNT:
                wp.writeSerializable(processLogic.getUserAccount(rPacket.readStringUTF()));
                break;
            case UserCenterConstants.MODIFY_USERACCOUNT:
                wp.writeBooleanNx(processLogic.modifyUserAccount((UpdateExpress) rPacket.readSerializable(), rPacket.readStringUTF()));
                break;
            case UserCenterConstants.INIT_UID:
                wp.writeBooleanNx(processLogic.initUidPool(rPacket.readLongNx(), rPacket.readLongNx()));
                break;
            case UserCenterConstants.GET_UID_POOL_LENGTH:
                wp.writeLongNx(processLogic.getUidPoolLength());
                break;

            case UserCenterConstants.QUERY_PROFILE_SUM_BY_PROFILEID:
                wp.writeSerializable((java.io.Serializable) processLogic.queryProfileSumByProfileids((java.util.Set<String>) rPacket.readSerializable()));
                break;
            case UserCenterConstants.GET_PROFILE_SUM_BY_PROFILEID:
                wp.writeSerializable(processLogic.getProfileSum(rPacket.readStringUTF()));
                break;
            case UserCenterConstants.RECIEVE_EVENT:
                wp.writeSerializable(processLogic.receiveEvent((com.enjoyf.platform.service.event.Event) rPacket.readSerializable()));
                break;

            case UserCenterConstants.CHECK_MOBILE_BINDED:
                wp.writeBooleanNx(processLogic.checkMobileIsBinded(rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case UserCenterConstants.MOBILE_BIND:
                wp.writeSerializable(processLogic.bindMobile(rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case UserCenterConstants.MOBILE_UNBIND:
                wp.writeSerializable(processLogic.unbindMobile(rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case UserCenterConstants.UNBIND_MOBILE:
                wp.writeSerializable(processLogic.unbindMobile(rPacket.readStringUTF()));
                break;
            case UserCenterConstants.INCREASE_PROFILE_SUM:
                wp.writeBooleanNx(processLogic.increaseProfileSum(rPacket.readStringUTF(), (ProfileSumField) rPacket.readSerializable(), rPacket.readIntNx()));
                break;
            case UserCenterConstants.QUERY_PROFILE_BY_QUERYEXPRESS:
                wp.writeSerializable((java.io.Serializable) processLogic.queryProfile((QueryExpress) rPacket.readSerializable()));
                break;
            case UserCenterConstants.QUERY_ACTIVITY_USER:
                wp.writeSerializable(processLogic.queryActivityProfile(rPacket.readStringUTF(), rPacket.readStringUTF(), (com.enjoyf.platform.util.Pagination) rPacket.readSerializable()));
                break;
            case UserCenterConstants.GET_ACTIVITY_SUM:
                wp.writeIntNx(processLogic.getActvitiyUserSum(rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case UserCenterConstants.QUERY_PROFILE_BY_PAGEROWS:
                wp.writeSerializable(processLogic.queryProfileByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case UserCenterConstants.QUERY_PROFILE_PROFILES_BY_UIDS:
                wp.writeSerializable((java.io.Serializable) processLogic.queryProfilesByUids((java.util.Set<Long>) rPacket.readSerializable()));
                break;

            case UserCenterConstants.GET_AUTHPROFILE_BY_UNO:
                wp.writeSerializable(processLogic.getAuthProfileByUno(rPacket.readStringUTF(), rPacket.readStringUTF(), (HashMap<String, String>) rPacket.readSerializable()));
                break;
            case UserCenterConstants.QUERY_VERIFY_PROFILE_BY_IDS:
                wp.writeSerializable((Serializable) processLogic.queryProfileByIds((java.util.Set<String>) rPacket.readSerializable()));
                break;
            case UserCenterConstants.GET_VERIFY_PROFILE_BY_ID:
                wp.writeSerializable(processLogic.getVerifyProfileById(rPacket.readStringUTF()));
                break;
            case UserCenterConstants.MODIFY_VERIFY_PROFILE:
                wp.writeSerializable(processLogic.modifyVerifyProfile(rPacket.readStringUTF(), (UpdateExpress) rPacket.readSerializable()));
                break;
            case UserCenterConstants.QUERY_VERIFY_PROFILE_BY_PAGE:
                wp.writeSerializable(processLogic.queryVerifyProfile((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case UserCenterConstants.QUERY_VERIFY_PROFILE_BY_TAGID:
                wp.writeSerializable(processLogic.queryVerifyProfileByTag(rPacket.readLongNx(), (Pagination) rPacket.readSerializable()));
                break;
            case UserCenterConstants.VERIFY_PROFILE:
                wp.writeSerializable(processLogic.verifyProfile((VerifyProfile) rPacket.readSerializable(), rPacket.readLongNx()));
                break;
            case UserCenterConstants.DELETE_VERIFY_PROFILE:
                wp.writeSerializable(processLogic.deleteVerifyProfile(rPacket.readLongNx(), rPacket.readStringUTF()));
                break;
            case UserCenterConstants.QUERY_FOLLOW_VERIFY_PROFILE:
                wp.writeSerializable(processLogic.queryFollowProfile((Set<String>) rPacket.readSerializable(), (ScoreRange) rPacket.readSerializable()));
                break;
            case UserCenterConstants.ADD_VERIFY:
                wp.writeSerializable(processLogic.addVerify((Verify) rPacket.readSerializable()));
                break;
            case UserCenterConstants.QUERY_VERIFY:
                wp.writeSerializable((Serializable) processLogic.queryVerify((QueryExpress) rPacket.readSerializable()));
                break;
            case UserCenterConstants.QUERY_VERIFY_BY_PAGE:
                wp.writeSerializable(processLogic.queryVerifyByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case UserCenterConstants.GET_VERIFY:
                wp.writeSerializable(processLogic.getVerify(rPacket.readLongNx()));
                break;
            case UserCenterConstants.MODIFY_VERIFY:
                wp.writeSerializable(processLogic.modifyVerify(rPacket.readLongNx(), (UpdateExpress) rPacket.readSerializable()));
                break;
            case UserCenterConstants.SORT_VERIFY_PROFILE_BY_TAGID:
                wp.writeSerializable(processLogic.sortVerifyProfileByTagId(rPacket.readLongNx(), rPacket.readIntNx(), rPacket.readStringUTF()));
                break;
            case UserCenterConstants.CREATE_PROFILE:
                wp.writeSerializable(processLogic.createProfile((Profile) rPacket.readSerializable()));
                break;
            case UserCenterConstants.GET_VERIFYPROFILE_TAGS_BYPROFILEID:
                wp.writeSerializable((java.io.Serializable) processLogic.getVerifyProfileTagsByProfileId(rPacket.readStringUTF()));
                break;
            case UserCenterConstants.GET_USER_PRIVACY:
                wp.writeSerializable((java.io.Serializable) processLogic.getUserPrivacy(rPacket.readStringUTF()));
                break;
            case UserCenterConstants.ADD_USER_PRIVACY:
                wp.writeSerializable(processLogic.addUserPrivacy((UserPrivacy) rPacket.readSerializable()));
                break;
            case UserCenterConstants.MODIFY_USER_PRIVACY:
                wp.writeSerializable(processLogic.modifyUserPrivacy(rPacket.readString(), (UpdateExpress) rPacket.readSerializable()));
                break;
            case UserCenterConstants.ADD_VERIFYPROFILE_TAGS_BYPROFILEID:
                wp.writeBooleanNx(processLogic.addVerifyProfileTagsByProfileId(rPacket.readLongNx(), rPacket.readString()));
                break;
            case UserCenterConstants.REMOVE_VERIFYPROFILE_TAGS_BYPROFILEID:
                wp.writeBooleanNx(processLogic.removeVerifyProfileTagsByProfileId(rPacket.readLongNx(), rPacket.readString()));
                break;

            default:
                GAlerter.lab("UserPropsPacketDecoder.logicProcess: Unrecognized type: " + type);
                throw new ServiceException(ServiceException.BAD_PACKET);
        }

        return wp;
    }
}
