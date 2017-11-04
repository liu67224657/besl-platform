package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityProfile;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.redis.ScoreRange;
import com.enjoyf.platform.util.redis.ScoreRangeRows;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.io.Serializable;
import java.util.*;

/**
 * Created by ericliu on 14/10/22.
 */
public class UserCenterServiceImpl implements UserCenterService {

    private ReqProcessor reqProcessor = null;

    public UserCenterServiceImpl(ServiceConfig scfg) {
        if (scfg == null) {
            throw new RuntimeException("UserCenterServiceImpl.ctor: ServiceConfig is null!");
        }

        //
        reqProcessor = scfg.getReqProcessor();

    }

    @Override
    public UserLogin getUserLoginByLoginKey(String loginKey, LoginDomain loginDomain) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(loginKey);
        wp.writeSerializable(loginDomain);

        Request req = new Request(UserCenterConstants.GET_USERLOGIN, wp);

        RPacket rp = reqProcessor.process(req);

        return (UserLogin) rp.readSerializable();
    }

    @Override
    public UserLogin getUserLoginByLoginId(String loginid) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(loginid);

        Request req = new Request(UserCenterConstants.GET_USERLOGIN_BY_LOGINID, wp);

        RPacket rp = reqProcessor.process(req);

        return (UserLogin) rp.readSerializable();
    }

    @Override
    public List<UserLogin> queryUserLoginUno(String uno, Set<LoginDomain> loginDomainSet) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        wp.writeSerializable((Serializable) loginDomainSet);

        Request req = new Request(UserCenterConstants.QUERY_USERLOGIN_BY_UNO_LOGINDOMAINS, wp);

        RPacket rp = reqProcessor.process(req);

        return (List<UserLogin>) rp.readSerializable();
    }

    @Override
    public boolean modifyUserLogin(UpdateExpress updateExpress, String loginId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeStringUTF(loginId);

        Request req = new Request(UserCenterConstants.MODIFY_USERLOGIN, wp);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public AuthProfile auth(String clientid, LoginDomain loginDomain, TokenInfo tokenInfo, String icon, String nick, String appKey, String createIp, Date createDate, HashMap<String, String> paramMap) throws ServiceException {

        WPacket wp = new WPacket();
        wp.writeStringUTF(clientid);
        wp.writeSerializable(loginDomain);
        wp.writeSerializable(tokenInfo);
        wp.writeStringUTF(icon);
        wp.writeStringUTF(nick);
        wp.writeStringUTF(appKey);
        wp.writeStringUTF(createIp);
        wp.writeSerializable(createDate);
        wp.writeSerializable(paramMap);

        Request req = new Request(UserCenterConstants.AUTH, wp);

        RPacket rp = reqProcessor.process(req);

        return (AuthProfile) rp.readSerializable();
    }

    @Override
    public AuthProfile getAuthProfileByUid(long uid, HashMap<String, String> paramMap) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(uid);
        wp.writeSerializable(paramMap);

        Request req = new Request(UserCenterConstants.GET_AUTHPROFILE_BY_UID, wp);

        RPacket rp = reqProcessor.process(req);

        return (AuthProfile) rp.readSerializable();
    }

    @Override
    public AuthProfile getAuthProfileByUno(String uno, String profileKey, HashMap<String, String> map) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        wp.writeStringUTF(profileKey);
        wp.writeSerializable(map);
        Request req = new Request(UserCenterConstants.GET_AUTHPROFILE_BY_UNO, wp);
        RPacket rp = reqProcessor.process(req);
        return (AuthProfile) rp.readSerializable();
    }

    @Override
    public AuthProfile bind(String loginKey, String password, LoginDomain loginDomain, String appKey, String uno, String createIp, Date createDate, String icon, String nick, HashMap<String, String> paramMap) throws ServiceException {

        WPacket wp = new WPacket();
        wp.writeStringUTF(loginKey);
        wp.writeStringUTF(password);
        wp.writeSerializable(loginDomain);
        wp.writeStringUTF(appKey);
        wp.writeStringUTF(uno);
        wp.writeStringUTF(createIp);
        wp.writeSerializable(createDate);
        wp.writeStringUTF(icon);
        wp.writeStringUTF(nick);
        wp.writeSerializable(paramMap);

        Request req = new Request(UserCenterConstants.BIND, wp);

        RPacket rp = reqProcessor.process(req);

        return (AuthProfile) rp.readSerializable();
    }

    @Override
    public boolean unbind(LoginDomain loginDomain, String appKey, String uno, String createIp, Date createDate) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(loginDomain);
        wp.writeStringUTF(appKey);
        wp.writeStringUTF(uno);
        wp.writeStringUTF(createIp);
        wp.writeSerializable(createDate);

        Request req = new Request(UserCenterConstants.UNBIND, wp);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public AuthProfile register(String loginKey, String password, LoginDomain loginDomain, String appKey, String nick, String createIp, Date createDate, HashMap<String, String> paramMap) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(loginKey);
        wp.writeStringUTF(password);
        wp.writeSerializable(loginDomain);
        wp.writeStringUTF(appKey);
        wp.writeStringUTF(nick);
        wp.writeStringUTF(createIp);
        wp.writeSerializable(createDate);
        wp.writeSerializable(paramMap);

        Request req = new Request(UserCenterConstants.REGISTER, wp);

        RPacket rp = reqProcessor.process(req);

        return (AuthProfile) rp.readSerializable();
    }


    @Override
    public AuthProfile login(String loginKey, String password, LoginDomain loginDomain, String appKey, String createIp, Date createDate, HashMap<String, String> paramMap) throws ServiceException {

        WPacket wp = new WPacket();
        wp.writeStringUTF(loginKey);
        wp.writeStringUTF(password);
        wp.writeSerializable(loginDomain);
        wp.writeStringUTF(appKey);
        wp.writeStringUTF(createIp);
        wp.writeSerializable(createDate);
        wp.writeSerializable(paramMap);

        Request req = new Request(UserCenterConstants.LOGIN, wp);

        RPacket rp = reqProcessor.process(req);

        return (AuthProfile) rp.readSerializable();
    }

    @Override
    public boolean deleteToken(String token) throws ServiceException {

        WPacket wp = new WPacket();
        wp.writeStringUTF(token);

        Request req = new Request(UserCenterConstants.DELETE_TOKEN, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public Token getToken(String token) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(token);

        Request req = new Request(UserCenterConstants.GET_TOKEN, wp);

        RPacket rp = reqProcessor.process(req);

        return (Token) rp.readSerializable();
    }


    @Override
    public Profile getProfileByUno(String uno, String appkey) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        wp.writeStringUTF(appkey);

        Request req = new Request(UserCenterConstants.GET_PROFILE_BYUNO, wp);

        RPacket rp = reqProcessor.process(req);

        return (Profile) rp.readSerializable();
    }

    @Override
    public Profile getProfileByProfileId(String profileId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(profileId);

        Request req = new Request(UserCenterConstants.GET_PROFILE_BYPROFILEID, wp);

        RPacket rp = reqProcessor.process(req);

        return (Profile) rp.readSerializable();
    }

    @Override
    public Profile getProfileByUid(long uid) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(uid);

        Request req = new Request(UserCenterConstants.GET_PROFILE_BYUID, wp);

        RPacket rp = reqProcessor.process(req);

        return (Profile) rp.readSerializable();
    }

    @Override
    public Map<String, Profile> queryProfiles(Set<String> unoSet) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) unoSet);

        Request req = new Request(UserCenterConstants.QUERY_PROFILE_PROFILES, wp);

        return (Map<String, Profile>) reqProcessor.process(req).readSerializable();
    }

    @Override
    public Profile getProfileByNick(String blogName) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(blogName);

        Request req = new Request(UserCenterConstants.GET_PROFILE_BYNICK, wp);

        RPacket rp = reqProcessor.process(req);

        return (Profile) rp.readSerializable();
    }

    @Override
    public boolean modifyProfile(UpdateExpress updateExpress, String profileId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeStringUTF(profileId);

        Request req = new Request(UserCenterConstants.MODIFY_PROFILE, wp);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public Profile updateNick(String nick) throws ServiceException {
        return null;
    }

    @Override
    public Profile createProfile(Profile Profile) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(Profile);

        Request req = new Request(UserCenterConstants.CREATE_PROFILE, wp);

        RPacket rp = reqProcessor.process(req);

        return (Profile) rp.readSerializable();
    }

    @Override
    public Profile getProfileByDomain(String domain) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(domain);

        Request req = new Request(UserCenterConstants.GET_PROFILE_BYDOMAIN, wp);

        RPacket rp = reqProcessor.process(req);

        return (Profile) rp.readSerializable();
    }

    /**
     * 后台工具通过DB查询获取用户信息
     *
     * @param nick
     * @return
     * @throws ServiceException
     */
    @Override
    public List<Profile> queryProfilesByNickLike(String nick) throws ServiceException {
        return null;
    }

    @Override
    public List<Profile> queryProfile(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request req = new Request(UserCenterConstants.QUERY_PROFILE_BY_QUERYEXPRESS, wp);

        RPacket rp = reqProcessor.process(req);

        return (List<Profile>) rp.readSerializable();
    }

    @Override
    public PageRows<Profile> queryProfileByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);

        Request req = new Request(UserCenterConstants.QUERY_PROFILE_BY_PAGEROWS, wp);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<Profile>) rp.readSerializable();
    }

    /**
     * 微服务改写
     *
     * @param nick
     * @param inOrNot
     * @param profileNos
     * @param startTime
     * @param endTime
     * @param pagination
     * @return
     * @throws ServiceException
     */
    @Override
    public PageRows<Profile> searchProfiles(String nick, String inOrNot, String profileNos, String startTime, String endTime, Pagination pagination) throws ServiceException {
        return null;
    }

    @Override
    public String saveMobileCode(String uno, String code) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        wp.writeStringUTF(code);
        Request req = new Request(UserCenterConstants.SAVE_CODE_FOR_MEMCACHED, wp);

        RPacket rp = reqProcessor.process(req);
        return rp.readStringUTF();
    }

    @Override
    public String getMobileCode(String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        Request req = new Request(UserCenterConstants.GET_CODE_FOR_MEMCACHED, wp);

        RPacket rp = reqProcessor.process(req);
        return (String) rp.readSerializable();
    }

    @Override
    public boolean removeMobileCode(String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        Request req = new Request(UserCenterConstants.DELETE_CODE_FOR_MEMCACHED, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public UserAccount getUserAccount(String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        Request req = new Request(UserCenterConstants.GET_USERACCOUNT, wp);
        RPacket rp = reqProcessor.process(req);
        return (UserAccount) rp.readSerializable();
    }

    @Override
    public boolean modifyUserAccount(UpdateExpress upateExpress, String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(upateExpress);
        wp.writeStringUTF(uno);
        Request req = new Request(UserCenterConstants.MODIFY_USERACCOUNT, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public boolean initUidPool(long startNum, long endNum) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(startNum);
        wp.writeLongNx(endNum);
        Request req = new Request(UserCenterConstants.INIT_UID, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public long getUidPoolLength() throws ServiceException {
        WPacket wp = new WPacket();
        Request req = new Request(UserCenterConstants.GET_UID_POOL_LENGTH, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readLongNx();
    }

    @Override
    public boolean savePasswordCode(String uno, Long time) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        wp.writeSerializable(time);
        Request req = new Request(UserCenterConstants.SAVE_PASSWORD_TIME, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public boolean removePasswordCode(String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        Request req = new Request(UserCenterConstants.REMOVE_PASSWORD_TIME, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public Long getPassordCode(String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        Request req = new Request(UserCenterConstants.GET_PASSWORD_TIME, wp);
        RPacket rp = reqProcessor.process(req);
        return (Long) rp.readSerializable();
    }


    @Override
    public Map<String, ProfileSum> queryProfileSumByProfileids(Set<String> profileIds) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable((java.io.Serializable) profileIds);

        Request req = new Request(UserCenterConstants.QUERY_PROFILE_SUM_BY_PROFILEID, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (Map<String, ProfileSum>) rPacket.readSerializable();
    }

    @Override
    public boolean increaseProfileSum(String profileId, ProfileSumField sumFiled, int value) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeStringUTF(profileId);
        wPacket.writeSerializable(sumFiled);
        wPacket.writeIntNx(value);
        Request req = new Request(UserCenterConstants.INCREASE_PROFILE_SUM, wPacket);
        RPacket rPacket = reqProcessor.process(req);
        return rPacket.readBooleanNx();
    }

    @Override
    public ProfileSum getProfileSum(String profileId) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeStringUTF(profileId);

        Request req = new Request(UserCenterConstants.GET_PROFILE_SUM_BY_PROFILEID, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (ProfileSum) rPacket.readSerializable();
    }


    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(event);
        Request req = new Request(UserCenterConstants.RECIEVE_EVENT, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public boolean checkMobileIsBinded(String mobile, String profileKey) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(mobile);
        wp.writeStringUTF(profileKey);
        Request req = new Request(UserCenterConstants.CHECK_MOBILE_BINDED, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public boolean bindMobile(String mobile, String profileId, String ip) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(mobile);
        wp.writeStringUTF(profileId);
        wp.writeStringUTF(ip);
        Request req = new Request(UserCenterConstants.MOBILE_BIND, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public boolean unbindMobile(String profileId, String ip) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(profileId);
        wp.writeStringUTF(ip);
        Request req = new Request(UserCenterConstants.MOBILE_UNBIND, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public boolean unbindMobile(String profileMobileId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(profileMobileId);
        Request req = new Request(UserCenterConstants.UNBIND_MOBILE, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }


    @Override
    public PageRows<ActivityProfile> queryActivityProfile(String appkey, String subkey, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(appkey);
        wp.writeStringUTF(subkey);
        wp.writeSerializable(pagination);
        Request req = new Request(UserCenterConstants.QUERY_ACTIVITY_USER, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<ActivityProfile>) rp.readSerializable();
    }

    @Override
    public int getActvitiyUserSum(String appkey, String subkey) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(appkey);
        wp.writeStringUTF(subkey);
        Request req = new Request(UserCenterConstants.GET_ACTIVITY_SUM, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readIntNx();
    }

    @Override
    public Map<Long, Profile> queryProfilesByUids(Set<Long> uids) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) uids);

        Request req = new Request(UserCenterConstants.QUERY_PROFILE_PROFILES_BY_UIDS, wp);

        return (Map<Long, Profile>) reqProcessor.process(req).readSerializable();
    }

    @Override
    public List<Profile> listProfilesByIds(Set<Long> ids) throws ServiceException {
        return null;
    }

    @Override
    public Map<String, VerifyProfile> queryProfileByIds(Set<String> profileIds) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) profileIds);
        Request req = new Request(UserCenterConstants.QUERY_VERIFY_PROFILE_BY_IDS, wp);
        return (Map<String, VerifyProfile>) reqProcessor.process(req).readSerializable();
    }

    @Override
    public VerifyProfile getVerifyProfileById(String profileId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(profileId);
        Request req = new Request(UserCenterConstants.GET_VERIFY_PROFILE_BY_ID, wp);
        RPacket rp = reqProcessor.process(req);
        return (VerifyProfile) rp.readSerializable();
    }

    @Override
    public boolean modifyVerifyProfile(String profileId, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(profileId);
        wp.writeSerializable(updateExpress);
        Request req = new Request(UserCenterConstants.MODIFY_VERIFY_PROFILE, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public PageRows<VerifyProfile> queryVerifyProfile(QueryExpress queryExpress, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(page);
        Request req = new Request(UserCenterConstants.QUERY_VERIFY_PROFILE_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<VerifyProfile>) rp.readSerializable();
    }

    @Override
    public PageRows<VerifyProfile> queryPlayers(String nick, String appKey, Long levelId, Pagination page) throws ServiceException {
        return null;
    }

    @Override
    public PageRows<VerifyProfile> queryVerifyProfileByTag(long tagId, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(tagId);
        wp.writeSerializable(pagination);
        Request req = new Request(UserCenterConstants.QUERY_VERIFY_PROFILE_BY_TAGID, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<VerifyProfile>) rp.readSerializable();
    }

    @Override
    public boolean verifyProfile(VerifyProfile profile, long tagId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(profile);
        wp.writeLongNx(tagId);
        Request req = new Request(UserCenterConstants.VERIFY_PROFILE, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public boolean deleteVerifyProfile(long tagId, String profileId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(tagId);
        wp.writeStringUTF(profileId);
        Request req = new Request(UserCenterConstants.DELETE_VERIFY_PROFILE, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public ScoreRangeRows<VerifyProfile> queryFollowProfile(Set<String> idSet, ScoreRange range) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) idSet);
        wp.writeSerializable(range);
        Request req = new Request(UserCenterConstants.QUERY_FOLLOW_VERIFY_PROFILE, wp);
        RPacket rp = reqProcessor.process(req);
        return (ScoreRangeRows<VerifyProfile>) rp.readSerializable();
    }

    @Override
    public Verify addVerify(Verify wanbaVerify) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(wanbaVerify);
        Request req = new Request(UserCenterConstants.ADD_VERIFY, wp);
        RPacket rp = reqProcessor.process(req);
        return (Verify) rp.readSerializable();
    }

    @Override
    public List<Verify> queryVerify(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(UserCenterConstants.QUERY_VERIFY, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<Verify>) rp.readSerializable();
    }

    @Override
    public PageRows<Verify> queryVerifyByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(UserCenterConstants.QUERY_VERIFY_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<Verify>) rp.readSerializable();
    }

    @Override
    public Verify getVerify(Long verifyId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(verifyId);
        Request req = new Request(UserCenterConstants.GET_VERIFY, wp);
        RPacket rp = reqProcessor.process(req);
        return (Verify) rp.readSerializable();
    }

    @Override
    public boolean modifyVerify(Long verifyId, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(verifyId);
        wp.writeSerializable(updateExpress);
        Request req = new Request(UserCenterConstants.MODIFY_VERIFY, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public UserPrivacy addUserPrivacy(UserPrivacy userPrivacy) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(userPrivacy);
        Request req = new Request(UserCenterConstants.ADD_USER_PRIVACY, wp);
        RPacket rp = reqProcessor.process(req);
        return (UserPrivacy) rp.readSerializable();
    }

    @Override
    public UserPrivacy getUserPrivacy(String profileId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(profileId);
        Request req = new Request(UserCenterConstants.GET_USER_PRIVACY, wp);
        RPacket rp = reqProcessor.process(req);
        return (UserPrivacy) rp.readSerializable();
    }

    @Override
    public boolean modifyUserPrivacy(String profileId, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(profileId);
        wp.writeSerializable(updateExpress);
        Request req = new Request(UserCenterConstants.MODIFY_USER_PRIVACY, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public void setOauthToken(String token) {

    }

    @Override
    public String getOauthToken(String token) {
        return null;
    }

    /**
     * 获得当前登陆者的账户信息
     *
     * @return
     */
    @Override
    public AuthProfile getCurrentAccount() throws ServiceException {
        return null;
    }

    /**
     * 修改密码，登陆操作
     *
     * @param newPassword
     * @throws ServiceException
     */
    @Override
    public void changePassword(String newPassword) throws ServiceException {

    }

    /**
     * 忘记密码时修改密码
     *
     * @param mobileNo
     * @param validCode
     * @param newPassword
     * @throws ServiceException
     */
    @Override
    public void forgetPassword(String mobileNo, String validCode, String newPassword) throws ServiceException {

    }

    /**
     * 修改手机号
     *
     * @param newMobileNo
     * @throws ServiceException
     */
    @Override
    public com.enjoyf.platform.userservice.client.model.UserLogin changeMobileNo(String newMobileNo) throws ServiceException {
        return null;
    }

    /**
     * 登陆后发送手机验证码
     *
     * @param mobileNo
     * @return
     */
    @Override
    public boolean sendMobileNo(String mobileNo) throws ServiceException {
        return false;
    }

    /**
     * 登陆后发送手机验证码
     *
     * @param mobileNo
     * @return
     * @throws ServiceException
     */
    @Override
    public boolean sendMobileCodeLogin(String mobileNo) throws ServiceException {
        return false;
    }

    /**
     * 登陆后验证手机验证码
     *
     * @param mobileNo
     * @param code
     * @return
     * @throws ServiceException
     */
    @Override
    public boolean verifyCodeLogin(String mobileNo, String code) throws ServiceException {
        return false;
    }

    /**
     * 注册时检查手机验证码是否正确
     *
     * @param mobileNo
     * @param code
     * @return
     * @throws ServiceException
     */
    @Override
    public boolean checkMobileVerifyCode(String mobileNo, String code) throws ServiceException {
        return false;
    }

    @Override
    public boolean sortVerifyProfileByTagId(Long tagId, int sort, String profileId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(tagId);
        wp.writeIntNx(sort);
        wp.writeStringUTF(profileId);
        Request req = new Request(UserCenterConstants.SORT_VERIFY_PROFILE_BY_TAGID, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public Set<String> getVerifyProfileTagsByProfileId(String profileId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(profileId);
        Request req = new Request(UserCenterConstants.GET_VERIFYPROFILE_TAGS_BYPROFILEID, wp);
        RPacket rp = reqProcessor.process(req);
        return (Set<String>) rp.readSerializable();
    }

    @Override
    public boolean addVerifyProfileTagsByProfileId(Long tagId, String profileId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(tagId);
        wp.writeStringUTF(profileId);
        Request req = new Request(UserCenterConstants.ADD_VERIFYPROFILE_TAGS_BYPROFILEID, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public boolean removeVerifyProfileTagsByProfileId(Long tagId, String profileId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(tagId);
        wp.writeStringUTF(profileId);
        Request req = new Request(UserCenterConstants.REMOVE_VERIFYPROFILE_TAGS_BYPROFILEID, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }
}
