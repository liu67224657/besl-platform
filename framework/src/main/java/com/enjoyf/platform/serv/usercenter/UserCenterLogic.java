/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.usercenter;


import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.db.usercenter.UserCenterHandler;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.system.ProfileSumIncreaseEvent;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.service.usercenter.UserAccount;
import com.enjoyf.platform.service.usercenter.UserLogin;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityProfile;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityUser;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityUserField;
import com.enjoyf.platform.userservice.client.model.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.redis.ScoreRange;
import com.enjoyf.platform.util.redis.ScoreRangeRows;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.util.StringUtil;
import net.sf.json.JSONArray;
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
class UserCenterLogic implements UserCenterService {
    //
    private static final Logger logger = LoggerFactory.getLogger(UserCenterLogic.class);

    //

    private UserCenterConfig config;
    private UserCenterHandler writeAbleHandler;
    private HandlerPool<UserCenterHandler> readOnyleHandlerPool;

    private UserCenterCache userCenterCache;
    private ActivityUserRedis activityUserRedis;
    private ActivityUserCache activityUserCache;

    private UsercenterRedis userCenterRedis;

    //queue thread pool
    private QueueThreadN eventProcessQueueThreadN = null;

    UserCenterLogic(UserCenterConfig cfg) {
        config = cfg;

        userCenterRedis = new UsercenterRedis(cfg.getProps());

        activityUserRedis = new ActivityUserRedis(config.getProps());
        activityUserCache = new ActivityUserCache(config.getMemCachedConfig());

        try {
            writeAbleHandler = new UserCenterHandler(config.getWriteableDataSourceName(), config.getProps());

            //initialize the db handler
            readOnyleHandlerPool = new HandlerPool<UserCenterHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                //create the handler and put it into the map.
                readOnyleHandlerPool.add(new UserCenterHandler(dsn, config.getProps()));
            }

            userCenterCache = new UserCenterCache(config.getMemCachedConfig());

            eventProcessQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
                public void process(Object obj) {
                    if (obj instanceof Event) {
                        processEvent(obj);
                    } else {
                        GAlerter.lab("In timeLineProcessQueueThreadN, there is a unknown obj.");
                    }
                }
            }, new FQueueQueue(config.getQueueDiskStorePath(), "userCenterProcessQueue"));

        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());
            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }
    }

    private void processEvent(Object obj) {
        if (obj instanceof ProfileSumIncreaseEvent) {
            ProfileSumIncreaseEvent event = (ProfileSumIncreaseEvent) obj;
            try {
                increaseProfileSum(event.getProfileId(), event.getField(), event.getCount());
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " occured error.e: ", e);
            }
        } else {
            logger.error("not support event." + obj);
        }
    }

    @Override
    public UserLogin getUserLoginByLoginKey(String loginKey, LoginDomain loginDomain) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call logic getUserLoginByLoginKey loginKey:" + loginKey + " loginDomain:" + loginDomain);
        }

        return getUserLoginByLoginId(UserCenterUtil.getUserLoginId(loginKey, loginDomain), readOnyleHandlerPool.getHandler());
    }

    @Override
    public UserLogin getUserLoginByLoginId(String loginid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call logic getUserLoginByLoginKey loginid:" + loginid);
        }

        return getUserLoginByLoginId(loginid, readOnyleHandlerPool.getHandler());
    }

    @Override
    public List<UserLogin> queryUserLoginUno(String uno, Set<LoginDomain> loginDomainSet) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call logic getUserLoginByLoginKey uno:" + uno + " loginDomainSet:" + loginDomainSet);
        }

        String[] loginDomainCode = new String[loginDomainSet.size()];
        int idx = 0;
        for (LoginDomain loginDomain : loginDomainSet) {
            loginDomainCode[idx] = loginDomain.getCode();
            idx++;
        }

        QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(UserLoginField.UNO, uno))
                .add(QueryCriterions.in(UserLoginField.LOGIN_DOMAIN, loginDomainCode));

        return readOnyleHandlerPool.getHandler().queryUserLogin(queryExpress);
    }

    @Override
    public boolean modifyUserLogin(UpdateExpress updateExpress, String loginId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call logic modifyUserLogin updateExpress:" + updateExpress + " loginKey:" + loginId);
        }

        boolean result = writeAbleHandler.modifyUserLogin(updateExpress, loginId);
        if (result) {
            userCenterCache.deleteUserLogin(loginId);
        }
        return result;
    }

    @Override
    public AuthProfile auth(String loginKey, LoginDomain loginDomain, TokenInfo tokenInfo, String icon, String nick, String appKey, String createIp, Date createDate, HashMap<String, String> paramMap) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call logic auth loginKey:" + loginKey + " appkey:" + appKey);
        }
//        userCenterCache.addAuthCallsCounter(createDate);
        AuthProfile authProfile;

        String uno = paramMap == null ? UUID.randomUUID().toString() : (paramMap.containsKey(UserCenterUtil.UNO_STRING) ? paramMap.get(UserCenterUtil.UNO_STRING) : UUID.randomUUID().toString());
        if (StringUtil.isEmpty(uno)) {
            uno = UUID.randomUUID().toString();
        }

        String userLoginId = UserCenterUtil.getUserLoginId(loginKey, loginDomain);
        UserLogin userLogin = getUserLoginByLoginId(userLoginId, writeAbleHandler);
        UserAccount userAccount = getUserAccount(uno, writeAbleHandler);
        if (userLogin == null) {
            userLogin = new UserLogin();
            userLogin.setUno(uno);
            userLogin.setLoginId(userLoginId);
            userLogin.setCreateTime(createDate);
            userLogin.setCreateIp(createIp);
            userLogin.setLoginDomain(loginDomain);
            userLogin.setLoginKey(loginKey);
            userLogin.setTokenInfo(tokenInfo);
            userLogin.setPasswdTime(String.valueOf(createDate.getTime()));
            userLogin.setLoginName(nick);

            if (userAccount == null) {
                userAccount = new UserAccount();
                userAccount.setUno(userLogin.getUno());
                userAccount.setCreateTime(userLogin.getCreateTime());
                userAccount.setCreateIp(userLogin.getCreateIp());
                userAccount.setAccountFlag(new AccountFlag().has(0));
                writeAbleHandler.insertUserAccount(userAccount);
            }

            userLogin = writeAbleHandler.insertUserLogin(userLogin);
        } else if (StringUtil.isEmpty(userLogin.getUno())) {
            if (userAccount == null) {
                userAccount = new UserAccount();
                userAccount.setUno(uno);
                userAccount.setCreateTime(userLogin.getCreateTime());
                userAccount.setCreateIp(userLogin.getCreateIp());
                userAccount.setAccountFlag(new AccountFlag().has(0));
                writeAbleHandler.insertUserAccount(userAccount);
            }

            if (modifyUserLogin(new UpdateExpress().set(UserLoginField.UNO, userAccount.getUno()), userLoginId)) {
                userLogin.setUno(uno);
            }
        }

        userCenterCache.putUserLogin(userLogin);
        if (userAccount == null) {
            userAccount = getUserAccount(userLogin.getUno(), writeAbleHandler);
        }


        String profieId = UserCenterUtil.getProfileId(userLogin.getUno(), appKey);
        Profile profile = getProfileByProfileId(profieId, writeAbleHandler);

        int flag = ProfileFlag.getFlagByLoginDomain(loginDomain);
        Token token;
        if (profile == null) {
            profile = new Profile();
            profile.setProfileId(profieId);
            profile.setUno(userLogin.getUno());
            profile.setProfileKey(appKey);
            profile.setCreateIp(createIp);
            profile.setCreateTime(createDate);
            profile.setFlag(new ProfileFlag().has(flag));
            profile.setIcon(icon);
            profile.setNick(nick);


            if (!CollectionUtil.isEmpty(paramMap)) {
                String profileappkey = paramMap.get(UserCenterUtil.PROFILE_TABLE_APPKEY);
                if (!StringUtil.isEmpty(profileappkey)) {
                    profile.setAppkey(profileappkey);
                }
            }


            if (!StringUtil.isEmpty(icon)) {
                Icons icons = new Icons();
                icons.getIconList().add(new Icon(0, icon));
                profile.setIcons(icons);
            }
            profile = writeAbleHandler.createProfile(profile);
            userCenterCache.addProfileCounter(createDate);
            userCenterCache.putProfile(profile);
            //新用户
            if (profile != null) {
                profile.setFreshUser(true);
            }
            token = generateToken(profile, paramMap);
        } else {
            profile.setFreshUser(false);
            token = generateToken(profile, paramMap);

            if (!LoginDomain.CLIENT.equals(loginDomain) && !profile.getFlag().hasFlag(flag)) {
                writeAbleHandler.modifyProfile(new UpdateExpress().set(ProfileField.FLAG, profile.getFlag().has(flag).getValue()), profieId);
                userCenterCache.deleteProfile(profile);
            }
        }


        authProfile = new AuthProfile();
        authProfile.setToken(token);
        authProfile.setProfile(profile);
        authProfile.setUserAccount(userAccount);
        authProfile.setUserLogin(userLogin);

        return authProfile;
    }

    @Override
    public AuthProfile getAuthProfileByUid(long uid, HashMap<String, String> paramMap) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call logic auth getAuthProfileByUid:" + uid);
        }

        Profile profile = getProfileByUid(uid);
        if (profile == null) {
            return null;
        }
        Token token = null;
        String tokenString = paramMap.get(UserCenterUtil.TOKEN_STRING);
        if (!StringUtil.isEmpty(tokenString)) {
            token = getToken(tokenString);
        }


        if (token == null) {
            token = generateToken(profile, paramMap);
        }
        UserAccount userAccount = this.getUserAccount(profile.getUno());
        if (userAccount == null) {
            return null;
        }

        AuthProfile authProfile = new AuthProfile();
        authProfile.setToken(token);
        authProfile.setProfile(profile);
        authProfile.setUserAccount(userAccount);
        return authProfile;
    }

    @Override
    public AuthProfile getAuthProfileByUno(String uno, String profileKey, HashMap<String, String> map) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call logic auth getAuthProfileByUno:" + uno + "," + profileKey);
        }

        Profile profile = getProfileByUno(uno, profileKey);
        if (profile == null) {
            return null;
        }
        Token token = null;
        if (map != null && map.containsKey(UserCenterUtil.TOKEN_STRING)) {
            String tokenString = map.get(UserCenterUtil.TOKEN_STRING);
            if (!StringUtil.isEmpty(tokenString)) {
                token = getToken(tokenString);
            }
        }

        if (token == null) {
            token = generateToken(profile, map);
        }
        UserAccount userAccount = this.getUserAccount(profile.getUno());
        if (userAccount == null) {
            return null;
        }

        AuthProfile authProfile = new AuthProfile();
        authProfile.setToken(token);
        authProfile.setProfile(profile);
        authProfile.setUserAccount(userAccount);
        return authProfile;
    }

    @Override
    public AuthProfile bind(String loginKey, String password, LoginDomain loginDomain, String appKey, String uno, String createIp, Date createDate, String icon, String nick, HashMap<String, String> paramMap) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call logic bind clientid:" + loginKey + " appkey:" + appKey);
        }

        int flag = ProfileFlag.getFlagByLoginDomain(loginDomain);
        String profieId = UserCenterUtil.getProfileId(uno, appKey);
        Profile profile = getProfileByProfileId(profieId, writeAbleHandler);
        if (profile == null) {
            throw UserCenterServiceException.PROFILE_NOT_EXISTS;
        }

        if (profile.getFlag().hasFlag(flag)) {
            throw UserCenterServiceException.PROFILE_HAS_EXISTS;
        }

        String userLoginId = UserCenterUtil.getUserLoginId(loginKey, loginDomain);
        UserLogin userLogin = getUserLoginByLoginId(userLoginId, writeAbleHandler);

        /**
         * userlogin为空第一次用该账号登陆
         *
         * userlogin不为空但是uno为空，改账号以前被绑定过，但是现在是解绑状态
         *
         * userlogin不为空uno不为空，抛出异常，说明改账号已经被别人绑定了
         */
        if (userLogin == null) {
            String passwordTime = String.valueOf(createDate.getTime());
            userLogin = new UserLogin();
            userLogin.setLoginId(userLoginId);
            userLogin.setCreateTime(createDate);
            userLogin.setCreateIp(createIp);
            userLogin.setLoginDomain(loginDomain);
            userLogin.setLoginKey(loginKey);
            if (!com.enjoyf.platform.util.StringUtil.isEmpty(password)) {
                userLogin.setLoginPassword(UserCenterUtil.getPassowrd(password, passwordTime));
            }
            userLogin.setPasswdTime(passwordTime);
            userLogin.setUno(uno);

            userLogin = writeAbleHandler.insertUserLogin(userLogin);
            userCenterCache.putUserLogin(userLogin);
            //新用户
            profile.setFreshUser(true);
        } else if (StringUtil.isEmpty(userLogin.getUno())) {
            if (modifyUserLogin(new UpdateExpress().set(UserLoginField.UNO, uno), userLoginId)) {
                userLogin.setUno(uno);
            }
            userCenterCache.putUserLogin(userLogin);
        } else {
            throw UserCenterServiceException.PROFILE_HAS_EXISTS;
        }

        //只有初次绑定时候才会修改昵称
        boolean needModfiyNick = profile.getFlag().equalFlag(ProfileFlag.FLAG_CLIENTID);
        UpdateExpress updateExpress = new UpdateExpress().set(ProfileField.FLAG, profile.getFlag().has(ProfileFlag.getFlagByLoginDomain(loginDomain)).getValue());
        //处理头像
        if (!StringUtil.isEmpty(icon)) {
            if (profile.getIcons() != null && profile.getIcons().getIconList().size() > 0) {
                profile.getIcons().add(new Icon(profile.getIcons().getIconList().size(), icon));
                updateExpress.set(ProfileField.ICONS, profile.getIcons().toJsonStr());
            } else {
                updateExpress.set(ProfileField.ICON, icon);
                Icons icons = new Icons();
                icons.add(new Icon(0, icon));
                updateExpress.set(ProfileField.ICONS, icons.toJsonStr());
            }
        }

        //处理昵称
        if (needModfiyNick && !StringUtil.isEmpty(nick)) {
            //check
            Profile nickProfile = writeAbleHandler.getProfile(new QueryExpress().add(QueryCriterions.eq(ProfileField.CHECKNICK, nick.toLowerCase())));
            if (nickProfile == null) {
                updateExpress.set(ProfileField.NICK, nick);
                updateExpress.set(ProfileField.CHECKNICK, nick.toLowerCase());
            } else if (nickProfile != null) {
                if (nickProfile.getUid() != profile.getUid()) {
                    nick = com.enjoyf.platform.util.StringUtil.subString(profile.getNick(), 5) + "_" + profile.getUid();
                    updateExpress.set(ProfileField.NICK, nick + "_" + profile.getUid());
                    updateExpress.set(ProfileField.CHECKNICK, nick.toLowerCase() + "_" + profile.getUid());
                }
            }
        }

        boolean result = writeAbleHandler.modifyProfile(updateExpress, profieId);
        if (result) {
            userCenterCache.deleteProfile(profile);
//            userCenterCache.deletePidByUID(profile.getUid());
        }

        Token token = generateToken(profile, paramMap);
        AuthProfile authProfile = new AuthProfile();
        authProfile.setToken(token);
        authProfile.setProfile(profile);
        authProfile.setUserAccount(getUserAccount(profile.getUno(), writeAbleHandler));
        authProfile.setUserLogin(userLogin);

        return authProfile;
    }

    @Override
    public boolean unbind(LoginDomain loginDomain, String appKey, String uno, String createIp, Date createDate) throws ServiceException {

        int flag = ProfileFlag.getFlagByLoginDomain(loginDomain);
        String profieId = UserCenterUtil.getProfileId(uno, appKey);
        Profile profile = getProfileByProfileId(profieId, writeAbleHandler);
        if (profile == null) {
            throw UserCenterServiceException.PROFILE_NOT_EXISTS;
        }

        UserLogin userLogin = writeAbleHandler.getUserLogin(new QueryExpress()
                .add(QueryCriterions.eq(UserLoginField.UNO, uno))
                .add(QueryCriterions.eq(UserLoginField.LOGIN_DOMAIN, loginDomain.getCode())));
        if (userLogin == null) {
            return false;
        }


        if (!profile.getFlag().hasFlag(flag)) {
            return false;
        }

        boolean result = writeAbleHandler.modifyProfile(new UpdateExpress().set(ProfileField.FLAG, profile.getFlag().reduce(ProfileFlag.getFlagByLoginDomain(loginDomain)).getValue()), profieId);
        if (result) {
            userCenterCache.deleteProfile(profile);
//            userCenterCache.deletePidByUID(profile.getUid());
        }

        result = writeAbleHandler.modifyUserLogin(new UpdateExpress().set(UserLoginField.UNO, ""), userLogin.getLoginId());
        if (result) {
            userCenterCache.deleteUserLogin(userLogin.getLoginId());
        }

        return result;
    }

    @Override
    public AuthProfile register(String loginKey, String password, LoginDomain loginDomain, String profilekey, String nick, String createIp, Date createDate, HashMap<String, String> paramMap) throws ServiceException {
        String userLoginId = UserCenterUtil.getUserLoginId(loginKey, loginDomain);
        UserLogin userLogin = getUserLoginByLoginId(userLoginId, writeAbleHandler);
        if (userLogin != null) {
            throw UserCenterServiceException.USERLOGIN_HAS_EXISTS;
        }

        UserAccount userAccount = null;
        if (userLogin == null) {
            String uno = UUID.randomUUID().toString();
            userLogin = new UserLogin();
            userLogin.setUno(uno);
            userLogin.setLoginId(userLoginId);

            String passwordTime = String.valueOf(createDate.getTime());
            userLogin.setCreateTime(createDate);
            userLogin.setCreateIp(createIp);
            userLogin.setLoginDomain(loginDomain);
            userLogin.setLoginKey(loginKey);
            userLogin.setPasswdTime(passwordTime);
            userLogin.setLoginName(nick);
            userLogin.setLoginPassword(UserCenterUtil.getPassowrd(password, passwordTime));

            userAccount = new UserAccount();
            userAccount.setUno(userLogin.getUno());
            userAccount.setCreateTime(userLogin.getCreateTime());
            userAccount.setCreateIp(userLogin.getCreateIp());
            userAccount.setAccountFlag(new AccountFlag().has(0));
            writeAbleHandler.insertUserAccount(userAccount);

            userLogin = writeAbleHandler.insertUserLogin(userLogin);
        }

//        else if (StringUtil.isEmpty(userLogin.getUno())) {
//            String uno = UUID.randomUUID().toString();
//            userAccount = new UserAccount();
//            userAccount.setUno(uno);
//            userAccount.setCreateTime(userLogin.getCreateTime());
//            userAccount.setCreateIp(userLogin.getCreateIp());
//            userAccount.setAccountFlag(new AccountFlag().has(0));
//            writeAbleHandler.insertUserAccount(userAccount);
//
//            if (modifyUserLogin(new UpdateExpress().set(UserLoginField.UNO, userAccount.getUno()), userLoginId)) {
//                userLogin.setUno(uno);
//            }
//        }

        userCenterCache.putUserLogin(userLogin);
//        if (userAccount == null) {
//            userAccount = getUserAccount(userLogin.getUno(), writeAbleHandler);
//        }

//        String profieId = UserCenterUtil.getProfileId(userLogin.getUno(), profilekey);
//        Profile profile = getProfileByProfileId(profieId, writeAbleHandler);
        int flag = ProfileFlag.getFlagByLoginDomain(loginDomain);

        //昵称为已经修改过
        ProfileFlag profileFlag = new ProfileFlag().has(flag);
        if (!StringUtil.isEmpty(nick)) {
            profileFlag.has(ProfileFlag.FLAG_NICK_HASCOMPLETE);
        }

        Token token;
//        if (profile == null) {
        Profile profile = new Profile();
        String profieId = UserCenterUtil.getProfileId(userLogin.getUno(), profilekey);
        profile.setProfileId(profieId);
        profile.setUno(userLogin.getUno());
        profile.setProfileKey(profilekey);
        profile.setCreateIp(createIp);
        profile.setCreateTime(createDate);
        profile.setFlag(profileFlag);
        profile.setNick(nick);

        if (!CollectionUtil.isEmpty(paramMap)) {
            String profileappkey = paramMap.get(UserCenterUtil.PROFILE_TABLE_APPKEY);
            if (!StringUtil.isEmpty(profileappkey)) {
                profile.setAppkey(profileappkey);
            }
        }
        profile = writeAbleHandler.createProfile(profile);
        userCenterCache.addProfileCounter(createDate);
        userCenterCache.putProfile(profile);
        //新用户
        if (profile != null) {
            profile.setFreshUser(true);
        }
        token = generateToken(profile, paramMap);
//        } else {
//            profile.setFreshUser(false);
//            token = generateToken(profile, paramMap);
//        }

        AuthProfile authProfile = new AuthProfile();
        authProfile.setToken(token);
        authProfile.setProfile(profile);
        authProfile.setUserAccount(userAccount);
        authProfile.setUserLogin(userLogin);
        return authProfile;
    }


    @Override
    public AuthProfile login(String loginKey, String password, LoginDomain loginDomain, String appKey, String createIp, Date createDate, HashMap<String, String> paramMap) throws ServiceException {
        String userLoginId = UserCenterUtil.getUserLoginId(loginKey, loginDomain);
        UserLogin userLogin = getUserLoginByLoginId(userLoginId, writeAbleHandler);
        if (userLogin == null) {
            throw UserCenterServiceException.USERLOGIN_NOT_EXISTS;
        }

        if (!userLogin.getLoginPassword().equals(UserCenterUtil.getPassowrd(password, userLogin.getPasswdTime()))) {
            throw UserCenterServiceException.USERLOGIN_NOT_EXISTS;
        }

        String profieId = UserCenterUtil.getProfileId(userLogin.getUno(), appKey);
        Profile profile = getProfileByProfileId(profieId, writeAbleHandler);
        if (profile == null) {
            throw UserCenterServiceException.PROFILE_NOT_EXISTS;
        }

        //get token if token null regentoken
        Token token = generateToken(profile, paramMap);

        AuthProfile authProfile = new AuthProfile();
        authProfile.setToken(token);
        authProfile.setProfile(profile);
        authProfile.setUserAccount(getUserAccount(profile.getUno(), writeAbleHandler));
        authProfile.setUserLogin(userLogin);

        return authProfile;
    }

    @Override
    public boolean deleteToken(String token) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call deleteToken token:" + token);
        }
        boolean result = writeAbleHandler.deleteToken(token);
        if (result) {
            userCenterCache.deleteTokenKey(token);
        }

        return result;
    }

    @Override
    public Token getToken(String tokenKey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call getToken token:" + tokenKey);
        }

        Token token = userCenterCache.getToken(tokenKey);
        if (token == null) {
            token = readOnyleHandlerPool.getHandler().getToken(tokenKey);

            if (token != null) {
                userCenterCache.putTokenByProfile(tokenKey, token);
            }
        }

        return token;
    }


    @Override
    public Profile getProfileByUno(String uno, String profileKey) throws ServiceException {
        String profileId = UserCenterUtil.getProfileId(uno, profileKey);

        return getProfileByProfileId(profileId, readOnyleHandlerPool.getHandler());
    }

    @Override
    public Profile getProfileByProfileId(String profileId) throws ServiceException {
        return getProfileByProfileId(profileId, readOnyleHandlerPool.getHandler());
    }

    @Override
    public Profile getProfileByUid(long uid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call getProfileByUid uid:" + uid);
        }

        Profile profile = userCenterCache.getProfileByUid(uid);

        if (profile == null) {
            profile = readOnyleHandlerPool.getHandler().getProfileByUid(uid);
            if (profile != null) {
                userCenterCache.putProfile(profile);
            }
        }

        return profile;
    }


    @Override
    public Profile getProfileByNick(String nick) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call getProfileByNick nick:" + nick);
        }
        return readOnyleHandlerPool.getHandler().getProfile(new QueryExpress().add(QueryCriterions.eq(ProfileField.CHECKNICK, nick.toLowerCase())));
    }


    @Override
    public Map<String, Profile> queryProfiles(Set<String> profileIds) throws ServiceException {
        Map<String, Profile> result = new HashMap<String, Profile>();
        if (CollectionUtil.isEmpty(profileIds)) {
            return result;
        }

        Set<String> querySetIds = new HashSet<String>();
        for (String pid : profileIds) {
            Profile profile = userCenterCache.getProfile(pid);
            if (profile == null) {
                querySetIds.add(pid);
            } else {
                result.put(pid, profile);
            }
        }

        if (!CollectionUtil.isEmpty(querySetIds)) {
            for (Profile profile : readOnyleHandlerPool.getHandler().queryProfiles(profileIds)) {
                result.put(profile.getProfileId(), profile);
                userCenterCache.putProfile(profile);
//            userCenterCache.putPidByUid(profile.getUid(), profile.getProfileId());
            }
        }

        return result;
    }

    @Override
    public boolean modifyProfile(UpdateExpress updateExpress, String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call modifyProfile updateExpress:" + updateExpress + " profileId:" + profileId);
        }

        boolean bVal = false;
        Profile profile = getProfileByProfileId(profileId);
        if (profile == null) {
            return bVal;
        }

        bVal = writeAbleHandler.modifyProfile(updateExpress, profileId);
        if (bVal) {
            userCenterCache.deleteProfile(profile);
        }

        return bVal;
    }

    @Override
    public Profile updateNick(String nick) throws ServiceException {
        return null;
    }


    @Override
    public Profile createProfile(Profile profile) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call createProfile Profile:" + profile);
        }
        profile = writeAbleHandler.createProfile(profile);
        if (profile.getUid() > 0) {
            userCenterCache.putProfile(profile);
        }
        return profile;
    }

    @Override
    public String saveMobileCode(String uno, String code) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to saveMobileCode");
        }
        userCenterCache.putMobileCode(uno, code);
        return uno;
    }

    @Override
    public String getMobileCode(String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to getMobileCode");
        }
        if (StringUtil.isEmpty(uno)) {
            return null;
        }

        return userCenterCache.getMobileCode(uno);
    }

    @Override
    public boolean removeMobileCode(String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to getMobileCode");
        }

        return userCenterCache.removeMobileCode(uno);
    }

    @Override
    public boolean savePasswordCode(String uno, Long time) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to savePasswordCode");
        }

        userCenterCache.putPwdTime(uno, time);
        return true;
    }

    @Override
    public Long getPassordCode(String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to getPassordCode");
        }

        return userCenterCache.getPwdTime(uno);
    }

    @Override
    public boolean removePasswordCode(String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to getPassordCode");
        }

        return userCenterCache.removeMobileCode(uno);
    }

    @Override
    public UserAccount getUserAccount(String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to getUserAccount，uno:" + uno);
        }

        return getUserAccount(uno, readOnyleHandlerPool.getHandler());
    }

    @Override
    public boolean modifyUserAccount(UpdateExpress upateExpress, String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to upateExpress: " + upateExpress + " , uno:" + uno);
        }

        boolean result = writeAbleHandler.modifyUserAccount(upateExpress, uno);


        userCenterCache.deleteUserAccount(uno);
        return result;
    }

    private UserLogin getUserLoginByLoginId(String loginId, UserCenterHandler handler) throws ServiceException {
        UserLogin userLogin = userCenterCache.getUserLogin(loginId);
        if (userLogin == null) {
            userLogin = handler.getUserLogin(loginId);

            if (userLogin != null) {
                userCenterCache.putUserLogin(userLogin);
            }
        }

        return userLogin;
    }

    @Override
    public Profile getProfileByDomain(String domain) throws ServiceException {
        return readOnyleHandlerPool.getHandler().getProfile(new QueryExpress().add(QueryCriterions.eq(ProfileField.DOMAIN, domain)));
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
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to QueryExpress: " + queryExpress);
        }

        return readOnyleHandlerPool.getHandler().queryProfileByQueryExpress(queryExpress);
    }

    @Override
    public PageRows<Profile> queryProfileByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to queryProfileByPage QueryExpress: " + queryExpress + " pagination" + pagination);
        }
        return readOnyleHandlerPool.getHandler().queryProfile(queryExpress, pagination);  //To change body of implemented methods use File | Settings | File Templates.
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
    public boolean initUidPool(long startNum, long endNum) throws ServiceException {
        eventProcessQueueThreadN.add(new UidPoolWrap(startNum, endNum));
        return true;
    }

    @Override
    public long getUidPoolLength() throws ServiceException {
        return userCenterRedis.length(UserCenterConstants.KEY_USERCENTER_UID);
    }


    //////////
    @Override
    public Map<String, ProfileSum> queryProfileSumByProfileids(Set<String> profileIds) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler queryProfileSumByProfileids:,destProfileId " + profileIds);
        }

        Map<String, ProfileSum> returnMap = new LinkedHashMap<String, ProfileSum>();

        for (String profileId : profileIds) {
            ProfileSum sum = userCenterRedis.getProfileSum(profileId);
            if (sum != null) {
                returnMap.put(profileId, sum);
            }
        }

//        //先从缓存取
//        Set<String> queryDbSet = new HashSet<String>();
//        Map<String, ProfileSum> cacheMap = new HashMap<String, ProfileSum>();
//        for (String profileId : profileIds) {
//            ProfileSum sum = userCenterCache.getProfileSum(profileId);
//            if (sum == null) {
//                queryDbSet.add(profileId);
//            } else {
//                cacheMap.put(profileId, sum);
//            }
//        }
//
//        //查询db 放缓存
//        Map<String, ProfileSum> dbMap = new HashMap<String, ProfileSum>();
//        if (!CollectionUtil.isEmpty(queryDbSet)) {
//            List<ProfileSum> profileSumList = readOnyleHandlerPool.getHandler().queryProfileSum(new QueryExpress()
//                    .add(QueryCriterions.in(ProfileSumField.PROFILEID, queryDbSet.toArray())));
//
//            for (ProfileSum profileSum : profileSumList) {
//                dbMap.put(profileSum.getProfileId(), profileSum);
//                userCenterCache.putProfileSum(profileSum);
//            }
//        }
//
//        //merage
//        for (String profileId : profileIds) {
//            ProfileSum sum = cacheMap.get(profileId);
//            if (sum == null) {
//                sum = dbMap.get(profileId);
//            }
//
//            if (sum == null) {
//                continue;
//            }
//            returnMap.put(sum.getProfileId(), sum);
//        }

        return returnMap;
    }

    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler getProfileSum:,event " + event);
        }

        eventProcessQueueThreadN.add(event);
        return true;
    }

    @Override
    public boolean checkMobileIsBinded(String mobile, String profileKey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler mobile:,mobile " + mobile + " profileKey" + profileKey);
        }

        String id = UserCenterUtil.getProfileMobileId(mobile, profileKey);
        ProfileMobile profileMobile = userCenterCache.getProfileMobile(id);
        if (profileMobile == null) {
            profileMobile = writeAbleHandler.getProfileMobile(new QueryExpress().add(QueryCriterions.eq(ProfileMobileField.PROFILE_MOBILE_ID, id)));

            if (profileMobile != null) {
                userCenterCache.putProfileMobile(profileMobile);
            }
        }

        return profileMobile != null;
    }

    @Override
    public boolean bindMobile(String mobile, String profileId, String ip) throws ServiceException {
        Profile profile = getProfileByProfileId(profileId);

        if (profile == null) {
            throw UserCenterServiceException.PROFILE_NOT_EXISTS;
        }

        if (!StringUtil.isEmpty(profile.getMobile())) {
            GAlerter.lan(this.getClass().getName() + " bindMobile error.profile mobile is not null" + profile.getProfileId() + " " + profile.getMobile());
            throw UserCenterServiceException.BIND_PHONE_ERROR_PRFOILE_HAS_BINDED;
        }

        boolean checkPhoneHasBinded = this.checkMobileIsBinded(mobile, profile.getProfileKey());
        if (checkPhoneHasBinded) {
            throw UserCenterServiceException.BIND_PHONE_ERROR_PHONE_HAS_BINDED;
        }

        ProfileMobile profileMobile = new ProfileMobile();
        profileMobile.setProfileKey(profile.getProfileKey());
        profileMobile.setMobile(mobile);
        profileMobile.setUno(profile.getUno());
        profileMobile.setUid(profile.getUid());
        profileMobile.setProfileId(profile.getProfileId());
        profileMobile.setCreateTime(new Date());
        writeAbleHandler.insertProfileMobile(profileMobile);

        boolean bval = modifyProfile(new UpdateExpress().set(ProfileField.MOBILE, mobile), profileId);

        return bval;
    }

    @Override
    public boolean unbindMobile(String profileId, String ip) throws ServiceException {

        Profile profile = getProfileByProfileId(profileId);
        if (StringUtil.isEmpty(profile.getMobile())) {
            return false;
        }

        //delete unbind mobile
        String id = UserCenterUtil.getProfileMobileId(profile.getMobile(), profile.getProfileKey());
        boolean bval = writeAbleHandler.deleteProfileMobile(new QueryExpress().add(QueryCriterions.eq(ProfileMobileField.PROFILE_MOBILE_ID, id)));
        if (bval) {
            userCenterCache.deleteProfileMobile(id);
        }

        if (bval) {
            bval = modifyProfile(new UpdateExpress().set(ProfileField.MOBILE, ""), profileId);
        }

        return bval;
    }

    @Override
    public boolean unbindMobile(String profileMobileId) throws ServiceException {
        ProfileMobile profileMobile = writeAbleHandler.getProfileMobile(new QueryExpress().add(QueryCriterions.eq(ProfileMobileField.PROFILE_MOBILE_ID, profileMobileId)));
        if (profileMobile == null) {
            return false;
        }
        boolean bval = writeAbleHandler.deleteProfileMobile(new QueryExpress().add(QueryCriterions.eq(ProfileMobileField.PROFILE_MOBILE_ID, profileMobileId)));
        if (bval) {
            userCenterCache.deleteProfileMobile(profileMobileId);
        }

        if (bval) {
            bval = modifyProfile(new UpdateExpress().set(ProfileField.MOBILE, ""), profileMobile.getProfileId());
        }

        return bval;
    }

    private Profile getProfileByProfileId(String profileId, UserCenterHandler handler) throws ServiceException {
        Profile profile = userCenterCache.getProfile(profileId);
        if (profile == null) {
            profile = handler.getProfile(profileId);

            if (profile != null) {
                userCenterCache.putProfile(profile);
            }
        }

        return profile;
    }


    private Token generateToken(Profile profile, HashMap<String, String> paramMap) throws ServiceException {
        //get by cached
        String tokenKey = UserCenterUtil.getTokenCacheKey(profile.getUno(), profile.getProfileKey());
        Token token = userCenterCache.getTokenByProfileKey(tokenKey);
        //todo 逻辑还是有问题
        if (token != null) {
            return token;
        }

        token = readOnyleHandlerPool.getHandler().getTokenByUnoAppKey(profile.getUno(), profile.getProfileKey());
        if (token == null) {
            token = new Token();
            token.setTokenType(TokenType.DEFAULT);
            token.setUno(profile.getUno());
            token.setProfileKey(profile.getProfileKey());
            token.setUid(profile.getUid());
            token.setProfileId(profile.getProfileId());
            if (!CollectionUtil.isEmpty(paramMap)) {
                JSONObject json = JSONObject.fromObject(paramMap);
                token.setRequest_parameter(json.toString());
            }
            token = writeAbleHandler.createToken(token);
            userCenterCache.putTokenByProfile(tokenKey, token);
        } else {
            //2015-10-10 //  因为客户端老版本没有调用logout接口 导致用户使用的token没有过期机制，从而导致没有mock等参数 无法领取礼包等

            String requestParam = token.getRequest_parameter();
            //获得原有token里的参数
            if (!StringUtil.isEmpty(requestParam)) {
                JSONObject jsonObject = JSONObject.fromObject(requestParam);
                //如果有mock和source 直接return
                if (jsonObject.has("mock") && jsonObject.has("source")) {
                    userCenterCache.putTokenByProfile(tokenKey, token);
                    return token;
                }
            }

            String mock = paramMap.get("mock");
            String source = paramMap.get("source");
            //获得客户端传过来的参数 如果mock和source不为null 则修改一次
            if (!StringUtil.isEmpty(mock) && !StringUtil.isEmpty(source)) {
                JSONArray json = JSONArray.fromObject(paramMap);
                if (json != null) {
                    requestParam = json.toString().substring(1, json.toString().length() - 1);
                    boolean bool = writeAbleHandler.modifyToken(
                            new UpdateExpress().set(TokenField.REQUEST_PARAMETER, requestParam),
                            new QueryExpress().add(QueryCriterions.eq(TokenField.TOKEN, token.getToken())));
                    if (bool) {
                        token.setRequest_parameter(requestParam);
                        userCenterCache.putTokenByProfile(tokenKey, token);
                    }
                }
            }
        }
        return token;
    }


    private UserAccount getUserAccount(String uno, UserCenterHandler handler) throws ServiceException {
        //
        UserAccount userAccount = userCenterCache.getUserAccount(uno);
        if (userAccount == null) {
            userAccount = handler.getAccount(uno);
            if (userAccount != null) {
                userCenterCache.putUserAccount(userAccount);
            }
        }

        return userAccount;
    }

    @Override
    public PageRows<ActivityProfile> queryActivityProfile(String appkey, String subkey, Pagination pagination) throws ServiceException {

        int total = Integer.parseInt(activityUserRedis.queryActivityUserSize(appkey, subkey));
        pagination.setTotalRows(total);

        Set<String> activityIds = activityUserRedis.queryActivityuser(appkey, subkey, pagination);

        PageRows<ActivityProfile> rows = new PageRows<ActivityProfile>();
        rows.setPage(pagination);
        rows.setRows(queryActivityUser(activityIds));
        return rows;
    }


    @Override
    public int getActvitiyUserSum(String appkey, String subkey) throws ServiceException {
        return Integer.parseInt(activityUserRedis.queryActivityUserSize(appkey, subkey));
    }

    private List<ActivityProfile> queryActivityUser(Set<String> queryIdSet) throws ServiceException {

        List<ActivityProfile> returnList = new ArrayList<ActivityProfile>();

        //query activityuser
        Map<String, ActivityUser> cachedMap = new HashMap<String, ActivityUser>();
        Set<String> dbIdset = new HashSet<String>();
        for (String aid : queryIdSet) {
            ActivityUser user = activityUserCache.getActivityUser(aid);
            if (user == null) {
                dbIdset.add(aid);
            } else {
                cachedMap.put(aid, user);
            }
        }

        if (!CollectionUtil.isEmpty(dbIdset)) {
            List<ActivityUser> userDbList = readOnyleHandlerPool.getHandler().queryActivityUser(new QueryExpress().add(
                    QueryCriterions.in(ActivityUserField.ACTIVITY_USER_ID, dbIdset.toArray())));
            for (ActivityUser auser : userDbList) {
                cachedMap.put(auser.getActivityUserId(), auser);
            }
        }

        List<ActivityUser> activityUserList = new ArrayList<ActivityUser>();
        for (String aid : queryIdSet) {
            if (cachedMap.containsKey(aid)) {
                activityUserList.add(cachedMap.get(aid));
            }
        }

        //query profile
        Set<String> profileIds = new HashSet<String>();
        for (ActivityUser user : activityUserList) {
            profileIds.add(user.getProfileId());
        }
        Map<String, Profile> profileMap = queryProfiles(profileIds);

        //build returnlist
        for (ActivityUser user : activityUserList) {
            if (profileMap.containsKey(user.getProfileId())) {
                Profile profile = profileMap.get(user.getProfileId());
                ActivityProfile aProfile = new ActivityProfile();
                aProfile.setDesc(profile.getDescription());
                aProfile.setNick(profile.getNick());
                aProfile.setIconurl(profile.getIcon());
                aProfile.setActivityUser(user);
                returnList.add(aProfile);
            }
        }

        return returnList;
    }


    @Override
    public Map<Long, Profile> queryProfilesByUids(Set<Long> uids) throws ServiceException {
        Map<Long, Profile> result = new HashMap<Long, Profile>();
        if (CollectionUtil.isEmpty(uids)) {
            return result;
        }
        for (Long uid : uids) {
            Profile profile = getProfileByUid(uid);
            if (profile != null) {
                result.put(uid, profile);
            }
        }

        return result;
    }

    @Override
    public List<Profile> listProfilesByIds(Set<Long> ids) throws ServiceException {
        return null;
    }

    /////////////////////////////////Verify///////////////////////////////
    @Override
    public Map<String, VerifyProfile> queryProfileByIds(Set<String> profileIds) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryProfileByIds profileIds:" + profileIds);
        }

        Map<String, VerifyProfile> returnMap = new HashMap<String, VerifyProfile>();
        Set<String> queryDbId = new HashSet<String>();
        //query by cache
        for (String id : profileIds) {
            VerifyProfile profile = userCenterRedis.getProfileById(id);
            if (profile != null) {
                Verify wanbaVerify = getVerify(profile.getVerifyType());
                if (wanbaVerify != null) {
                    profile.setVerifyTitle(wanbaVerify.getVerifyName());
                }
                returnMap.put(id, profile);
            } else {
                queryDbId.add(id);
            }
        }

        //query by db
        if (!CollectionUtil.isEmpty(queryDbId)) {
            List<VerifyProfile> profileList = writeAbleHandler.queryVerifyProfile(new QueryExpress().add(QueryCriterions.in(VerifyProfileSumField.PROFILEID, queryDbId.toArray())));
            for (VerifyProfile wanbaProfile : profileList) {
                Verify wanbaVerify = getVerify(wanbaProfile.getVerifyType());
                if (wanbaVerify != null) {
                    wanbaProfile.setVerifyTitle(wanbaVerify.getVerifyName());
                }
                returnMap.put(wanbaProfile.getProfileId(), wanbaProfile);
            }
        }


        return returnMap;
    }

    @Override
    public VerifyProfile getVerifyProfileById(String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getProfileById profileId:" + profileId);
        }

        VerifyProfile profile = userCenterRedis.getProfileById(profileId);
        if (profile == null) {
            profile = writeAbleHandler.getVerifyProfileByProfileId(profileId);
            if (profile != null) {
                userCenterRedis.setProfile(profile);
            }
        }
        if (profile != null) {
            Verify wanbaVerify = userCenterRedis.getWanbaVerify(profile.getVerifyType());
            if (wanbaVerify == null) {
                wanbaVerify = getVerify(profile.getVerifyType());
            }
            if (wanbaVerify != null) {
                profile.setVerifyTitle(wanbaVerify.getVerifyName());
            }
        }
        return profile;
    }

    @Override
    public boolean modifyVerifyProfile(String profileId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyWanbaProfile profileId:" + profileId + " updateExpress:" + updateExpress);
        }

        boolean bool = writeAbleHandler.modifyVerifyProfile(new QueryExpress().add(QueryCriterions.eq(VerifyProfileField.PROFILEID, profileId)), updateExpress);
        if (bool) {
            userCenterRedis.delProfile(profileId);
        }

        return false;
    }


    @Override
    public PageRows<VerifyProfile> queryVerifyProfile(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryWanbaProfile queryExpress:" + queryExpress + " page:" + page);
        }


        return writeAbleHandler.queryVerifyProfileByPage(queryExpress, page);
    }

    @Override
    public PageRows<VerifyProfile> queryPlayers(String nick, String appKey, Long levelId, Pagination page) throws ServiceException {
        return null;
    }

    @Override
    public PageRows<VerifyProfile> queryVerifyProfileByTag(long tagId, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryVerifyProfileByTag tagId:" + tagId + " page:" + pagination);
        }

        Set<String> profileIds = userCenterRedis.getProfileIdByTagId(tagId, pagination);

        Map<String, VerifyProfile> profileMap = queryProfileByIds(profileIds);
        pagination.setTotalRows((int) userCenterRedis.getProfileIdByTagIdSize(tagId));

        PageRows<VerifyProfile> returnObj = new PageRows<VerifyProfile>();
        List<VerifyProfile> returnList = new ArrayList<VerifyProfile>();
        for (String id : profileIds) {
            returnList.add(profileMap.get(id));
        }
        returnObj.getRows().addAll(returnList);
        returnObj.setPage(pagination);

        return returnObj;
    }

    @Override
    public boolean verifyProfile(VerifyProfile profile, long tagId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " verifyWanbaProfile profile:" + profile + " tagId:" + tagId);
        }

        VerifyProfile profileByQuery = getVerifyProfileById(profile.getProfileId());
        if (profileByQuery == null) {
            writeAbleHandler.insertVerifyProfile(profile);
        }


        boolean success = true;
        if (tagId > 0) {
            if (!userCenterRedis.existsPrifileIdByTagId(tagId, profile.getProfileId())) {
                userCenterRedis.addProfileIdByTagId(tagId, profile.getProfileId(), System.currentTimeMillis());
                success = false;
            }

            if (!userCenterRedis.existsPrifileIdByTagId(-1l, profile.getProfileId())) {
                userCenterRedis.addProfileIdByTagId(-1l, profile.getProfileId(), System.currentTimeMillis());
            }
        }
        return success;
    }

    @Override
    public boolean deleteVerifyProfile(long tagId, String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " deleteWanbaProfile profile:" + profileId + " tagId:" + tagId);
        }
        return userCenterRedis.removePrifileIdByTagId(tagId, profileId) > 0;
    }


    @Override
    public ScoreRangeRows<VerifyProfile> queryFollowProfile(Set<String> idSet, ScoreRange range) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryFollowProfile questionId:" + idSet + " range:" + range);
        }
        ScoreRangeRows<VerifyProfile> returnPageRows = new ScoreRangeRows<VerifyProfile>();

        returnPageRows.setRange(range);

        //
        if (CollectionUtil.isEmpty(idSet)) {
            return returnPageRows;
        }

        Map<String, VerifyProfile> queryMap = queryProfileByIds(idSet);
        List<VerifyProfile> returnList = new ArrayList<VerifyProfile>();
        for (String id : idSet) {
            if (queryMap.containsKey(id)) {
                returnList.add(queryMap.get(id));
            }
        }
        returnPageRows.setRows(returnList);
        return returnPageRows;
    }


    @Override
    public Verify addVerify(Verify verify) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " addWanbaVerify :" + verify.toString());
        }
        return writeAbleHandler.addVerify(verify);
    }

    @Override
    public List<Verify> queryVerify(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryWanbaVerify :" + queryExpress.toString());
        }
        return writeAbleHandler.queryVerify(queryExpress);
    }


    @Override
    public PageRows<Verify> queryVerifyByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryWanbaVerifyByPage :" + queryExpress.toString());
        }
        return writeAbleHandler.queryVerifyByPage(queryExpress, pagination);
    }

    @Override
    public Verify getVerify(Long verifyId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " verifyId :" + verifyId);
        }
        Verify wanbaVerify = userCenterRedis.getWanbaVerify(verifyId);
        if (userCenterRedis.getWanbaVerify(verifyId) == null) {
            wanbaVerify = writeAbleHandler.getVerify(new QueryExpress().add(QueryCriterions.eq(VerifyField.VERIFY_ID, verifyId)));
            if (wanbaVerify != null) {
                userCenterRedis.setVerify(wanbaVerify);
            }
        }
        return wanbaVerify;
    }

    @Override
    public boolean modifyVerify(Long verifyId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " verifyId :" + verifyId + " updateExpress=" + updateExpress);
        }
        boolean bool = writeAbleHandler.modifyVerify(new QueryExpress().add(QueryCriterions.eq(VerifyField.VERIFY_ID, verifyId)), updateExpress);

        if (bool) {
            userCenterRedis.delVerify(verifyId);
        }
        return bool;
    }

    @Override
    public boolean sortVerifyProfileByTagId(Long tagId, int sort, String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " sortWanbaProfileByTagId  tagId:" + tagId + " sort=" + sort);
        }

        Set<String> profileSet = userCenterRedis.queryAllProfileIdByTagId(tagId);
        String id = "";
        if (sort > profileSet.size()) {
            Object[] profileArray = profileSet.toArray();
            id = String.valueOf(profileArray[profileArray.length - 1]);//获得最后一个Id
        } else {
            int i = 1;
            for (String setId : profileSet) {
                if (i == sort) {
                    id = setId;
                    break;
                }
                i++;
            }
        }
        if (profileId.equals(id)) {
            return true;
        }
        Double scoreDouble = userCenterRedis.profileidByTagId(tagId, id);
        long score = Math.round(scoreDouble);

        long rank = userCenterRedis.profileZrank(tagId, profileId);
        if (sort > (rank + 1l)) {   //判断向上还是向下移动
            score = score - 1l;
        } else {
            score = score + 1l;
        }

        userCenterRedis.addProfileIdByTagId(tagId, profileId, score);
        return true;
    }

    @Override
    public Set<String> getVerifyProfileTagsByProfileId(String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getVerifyProfileTagsByProfileId  profileId:" + profileId);
        }
        return userCenterRedis.getVerifyProfileTagsByProfileId(profileId);
    }

    @Override
    public boolean addVerifyProfileTagsByProfileId(Long tagId, String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " addVerifyProfileTagsByProfileId  tagId:" + tagId + ",profileId:" + profileId);
        }
        userCenterRedis.addVerifyprofiletags(tagId, profileId, System.currentTimeMillis());
        return true;
    }

    @Override
    public boolean removeVerifyProfileTagsByProfileId(Long tagId, String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " removeVerifyProfileTagsByProfileId  tagId:" + tagId + ",profileId:" + profileId);
        }
        return userCenterRedis.removePrifileIdByTagId(tagId, profileId) > 0;
    }

    @Override
    public UserPrivacy addUserPrivacy(UserPrivacy userPrivacy) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " addUserPrivacy :" + userPrivacy.toString());
        }
        return writeAbleHandler.addUserPrivacy(userPrivacy);
    }

    @Override
    public UserPrivacy getUserPrivacy(String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " profileId :" + profileId);
        }

        UserPrivacy userPrivacy = userCenterRedis.getWikiUserPrivacy(profileId);
        if (userPrivacy == null) {
            userPrivacy = writeAbleHandler.getUserPrivacy(new QueryExpress().add(QueryCriterions.eq(UserPrivacyField.PROFILEID, profileId)));

            if (userPrivacy == null) {
                UserPrivacyPrivacyAlarm privacyAlarm = new UserPrivacyPrivacyAlarm();
                privacyAlarm.setUserat("1");
                privacyAlarm.setSysteminfo("1");
                privacyAlarm.setFollow("1");
                privacyAlarm.setComment("1");
                privacyAlarm.setAgreement("1");

                UserPrivacyFunction privacyFunction = new UserPrivacyFunction();
                privacyFunction.setAcceptFollow("1");
                privacyFunction.setChat("1");

                userPrivacy = new UserPrivacy();
                userPrivacy.setFunctionSetting(privacyFunction);
                userPrivacy.setAlarmSetting(privacyAlarm);
                userPrivacy.setCreateip("127.0.0.1");
                userPrivacy.setCreatetime(new Date());
                userPrivacy.setProfileId(profileId);
                userPrivacy.setUpdateip("127.0.0.1");
                userPrivacy.setUpdatetime(new Date());
                addUserPrivacy(userPrivacy);

                userCenterRedis.setWikiUserPrivacy(userPrivacy);
            } else {
                userCenterRedis.setWikiUserPrivacy(userPrivacy);
            }

        }
        return userPrivacy;
    }

    @Override
    public boolean modifyUserPrivacy(String profileId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " profileId :" + profileId + " updateExpress=" + updateExpress);
        }
        boolean bool = writeAbleHandler.modifyUserPrivacy(new QueryExpress().add(QueryCriterions.eq(UserPrivacyField.PROFILEID, profileId)), updateExpress);

        if (bool) {
            userCenterRedis.delWikiUserPrivacy(profileId);
        }
        return bool;
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

    ////////////
    public boolean increaseProfileSum(String profileId, ProfileSumField sumFiled, int value) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " profileId :" + profileId);
        }

        userCenterRedis.increaseProfileSum(profileId, sumFiled, value);
        return true;
    }


    public ProfileSum getProfileSum(String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getProfileSum profileId: " + profileId);
        }

        return userCenterRedis.getProfileSum(profileId);
    }
}
