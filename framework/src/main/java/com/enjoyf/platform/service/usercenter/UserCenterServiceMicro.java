package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.serv.usercenter.UserCenterConfig;
import com.enjoyf.platform.serv.usercenter.UsercenterRedis;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityProfile;
import com.enjoyf.platform.userservice.client.ApiException;
import com.enjoyf.platform.userservice.client.ApiResponse;
import com.enjoyf.platform.userservice.client.Configuration;
import com.enjoyf.platform.userservice.client.api.*;
import com.enjoyf.platform.userservice.client.model.*;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.redis.ScoreRange;
import com.enjoyf.platform.util.redis.ScoreRangeRows;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.util.*;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by shuguangcao on 2017/3/28.
 */
public class UserCenterServiceMicro implements UserCenterService {

    private static final Logger logger = LoggerFactory.getLogger(UserCenterServiceMicro.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String PAGE_TOTAL = "X-Total-Count";

    private final ThreadLocal<String> oauthToken = new ThreadLocal<String>();

    private final Gson gson;

    private UsercenterRedis userCenterRedis;

    public UserCenterServiceMicro() {
        String userServiceUrl = WebappConfig.get().getUserServiceUrl();
        if (StringUtil.isEmpty(userServiceUrl))
            throw new RuntimeException("UserService url is null");
        Configuration.getDefaultApiClient().setBasePath(userServiceUrl);
        gson = new Gson();
        FiveProps servProps = WebappConfig.get().getProps();
        UserCenterConfig config = new UserCenterConfig(servProps);
        logger.info("\n-------------usercenter redis:---------------\n");
        logger.info("*** reids host:{} **********", servProps.get("redis.host"));
        userCenterRedis = new UsercenterRedis(config.getProps());
    }

    @Override
    @Deprecated
    public boolean receiveEvent(Event event) throws ServiceException {
        return false;
    }

    /**
     * 同getUserLoginByLoginId
     *
     * @param loginKey
     * @param loginDomain
     * @return
     * @throws ServiceException
     */
    @Override
    public UserLogin getUserLoginByLoginKey(String loginKey, LoginDomain loginDomain) throws ServiceException {
        UserLoginResourceApi userLoginResourceApi = new UserLoginResourceApi();
        UserLogin localUserLogin = null;
        try {
            com.enjoyf.platform.userservice.client.model.UserLogin userLogin = userLoginResourceApi.getUserLoginByLoginAndLoginDomainUsingGET(loginKey, loginDomain.getCode());
            if (userLogin != null) {
                localUserLogin = toLogin(userLogin);

            }
        } catch (ApiException e) {
            logger.error("获取登陆信息失败：{}", e.getMessage());
            throw new ServiceException(e.getCode(), "获取登陆信息失败" + e.getMessage());
        }
        return localUserLogin;
    }

    private UserLogin toLogin(com.enjoyf.platform.userservice.client.model.UserLogin userLogin) {
        UserLogin localUserLogin = new UserLogin();
        if (userLogin != null) {
            localUserLogin.setLoginId(userLogin.getId() + "");
            localUserLogin.setLoginKey(userLogin.getLogin());
            localUserLogin.setLoginDomain(LoginDomain.getByCode(userLogin.getLoginDomain()));
            localUserLogin.setLoginPassword(userLogin.getPassword());
            localUserLogin.setPasswdTime(userLogin.getPasswordTime());
            localUserLogin.setUno(userLogin.getAccountNo());
            localUserLogin.setLoginName(userLogin.getLoginName());
        }
        return localUserLogin;
    }

    /**
     * 根据loginid获取用户登录信息
     *
     * @param loginid LoginUtil.getLoingId(loginkey, logindomain)
     * @return
     * @throws ServiceException
     */
    @Override
    @Deprecated
    public UserLogin getUserLoginByLoginId(String loginid) throws ServiceException {
        UserLoginResourceApi userLoginResourceApi = new UserLoginResourceApi();
        UserLogin localUserLogin = null;
        try {
            com.enjoyf.platform.userservice.client.model.UserLogin userLogin = userLoginResourceApi.getUserLoginUsingGET(Long.parseLong(loginid));
            if (userLogin != null) {
                localUserLogin = toLogin(userLogin);

            }
        } catch (ApiException e) {
            logger.error("获取登陆信息失败：{}", e.getMessage());
            throw new ServiceException(e.getCode(), "获取登陆信息失败" + e.getMessage());
        } catch (Exception ex) {
            logger.error("获取登陆信息失败:{}", ex.getMessage());
        }
        return localUserLogin;
    }

    /**
     * uno和多种登录方式
     *
     * @param uno
     * @param loginDomains
     * @return
     * @throws ServiceException
     */
    @Override
    public List<UserLogin> queryUserLoginUno(String uno, Set<LoginDomain> loginDomains) throws ServiceException {
        List<UserLogin> localLogins = null;
        UserAccountResourceApi accountResourceApi = new UserAccountResourceApi();
        try {
            if (StringUtil.isEmpty(uno) || CollectionUtil.isEmpty(loginDomains))
                return null;
            List<String> domains = new ArrayList<String>();
            for (LoginDomain domain : loginDomains) {
                domains.add(domain.getCode());
            }
            List<com.enjoyf.platform.userservice.client.model.UserLogin> userLogins =
                    accountResourceApi.getUserLoginsByAccountNoAndLogindomainsUsingGET(uno, domains);
            if (!CollectionUtil.isEmpty(userLogins)) {
                localLogins = new ArrayList<UserLogin>();
                for (com.enjoyf.platform.userservice.client.model.UserLogin login : userLogins) {
                    localLogins.add(toLogin(login));
                }
            }
        } catch (ApiException e) {
            logger.error("获取登陆信息失败：{}", e.getMessage());
            throw new ServiceException(e.getCode(), "获取登陆信息失败" + e.getMessage());
        } catch (Exception ex) {
            logger.error("获取登陆信息失败：{}", ex.getMessage());
        }
        return localLogins;
    }

    /**
     * 修改login的方法
     *
     * @param updateExpress
     * @param loginId
     * @return
     * @throws ServiceException
     */
    @Override
    @Deprecated
    public boolean modifyUserLogin(UpdateExpress updateExpress, String loginId) throws ServiceException {
        UserLoginResourceApi loginResourceApi = new UserLoginResourceApi();
        try {
            if (StringUtil.isEmpty(loginId)) return false;
            com.enjoyf.platform.userservice.client.model.UserLogin userLogin = new com.enjoyf.platform.userservice.client.model.UserLogin();
            userLogin.setId(Long.parseLong(loginId));
            if (updateExpress.containField(UserLoginField.LOGIN_PASSWORD))
                userLogin.setPassword(updateExpress.getUpdateValueByField(UserLoginField.LOGIN_PASSWORD).toString());
            if (updateExpress.containField(UserLoginField.LOGIN_KEY))
                userLogin.setLogin(updateExpress.getUpdateValueByField(UserLoginField.LOGIN_KEY).toString());
            loginResourceApi.updateUserLoginUsingPUT(userLogin);
        } catch (ApiException e) {
            logger.error("修改登陆信息失败：{},响应码:{}", e.getMessage(), e.getCode());
            throw new ServiceException(e.getCode(), "获取登陆信息失败" + e.getMessage());
        } catch (Exception ex) {
            logger.error("修改登陆信息失败：{}", ex.getMessage());
        }
        return true;
    }


    private AuthProfile toAuthProfile(AccountDTO accountDTO) {
        AuthProfile authProfile = new AuthProfile();
        authProfile.setUserLogin(toLogin(accountDTO.getUserLogin()));
        authProfile.setUserAccount(toAccount(accountDTO.getUserAccount()));
        authProfile.setProfile(toProfile(accountDTO.getUserProfile()));
        return authProfile;
    }

    private Profile toProfile(UserProfile userProfile) {
        Profile profile = new Profile();
        profile.setUno(userProfile.getAccountNo());
        profile.setUid(userProfile.getId());
        profile.setProfileId(userProfile.getProfileNo());
        profile.setProfileKey(userProfile.getProfileKey());
        profile.setCreateTime(userProfile.getCreatedTime().toDate());
        profile.setCreateIp(userProfile.getCreatedIp());
        profile.setAppkey(userProfile.getAppKey());
        profile.setNick(userProfile.getNick());
        profile.setIcon(userProfile.getIcon());
        //todo profile.setIcon(userProfile.getIcons());
        profile.setCheckNick(userProfile.getLowercaseNick());
        profile.setSignature(userProfile.getSignature());
        profile.setCityId(userProfile.getCityId() == null ? 0 : userProfile.getCityId());
        profile.setProvinceId(userProfile.getProvinceId() == null ? 0 : userProfile.getProvinceId());
        profile.setSex(userProfile.getSex() == null ? "" : userProfile.getSex() + "");
        profile.setBackPic(userProfile.getBackPic());
        profile.setDescription(userProfile.getDiscription());
        profile.setHobby(userProfile.getHobby());
        profile.setMobile(userProfile.getMobileNo());
        profile.setRealName(userProfile.getRealName());
        profile.setBirthday(userProfile.getBirthDay());
        profile.setLevel(userProfile.getLevel() == null ? 0 : userProfile.getLevel());
        profile.setDomain(userProfile.getDomain());
        profile.setFlag(new ProfileFlag(userProfile.getFlag()));
        profile.setFreshUser(false);
        return profile;
    }

    private UserAccount toAccount(com.enjoyf.platform.userservice.client.model.UserAccount userAccount) {
        UserAccount account = new UserAccount();
        account.setUno(userAccount.getAccountNo());
        account.setAccountFlag(new AccountFlag(userAccount.getFlag()));
        if (!StringUtil.isEmpty(userAccount.getAddress()))
            account.setAddress(gson.fromJson(userAccount.getAddress(), Address.class));
        return account;
    }

    /**
     * 通过uid获取 profile token useraccount
     *
     * @param profileId
     * @param paramMap
     * @return
     * @throws ServiceException
     */
    @Override
    @Deprecated
    public AuthProfile getAuthProfileByUid(long profileId, HashMap<String, String> paramMap) throws ServiceException {


        UserAccountResourceApi accountResourceApi = new UserAccountResourceApi();
        AuthProfile authProfile = null;
        try {
            ApiResponse<AccountDTO> response = accountResourceApi.getAccountByProfileIdUsingGETWithHttpInfo(profileId);
            if (response != null && response.getStatusCode() == 200) {
                authProfile = toAuthProfile(response.getData());
                authProfile.setToken(this.storeToken(response, resolveToken(response)));
                return authProfile;
            }
        } catch (ApiException e) {
            logger.error("getAuthProfileByUid：{}", e.getMessage());
            throw new ServiceException(e.getCode(), "getAuthProfileByUid" + e.getMessage());
        }
        return null;
    }

    /**
     * 同上个方法是一样的
     *
     * @param accountNo
     * @param profileKey
     * @param map
     * @return
     * @throws ServiceException
     */
    @Override
    public AuthProfile getAuthProfileByUno(String accountNo, String profileKey, HashMap<String, String> map) throws ServiceException {
        UserAccountResourceApi accountResourceApi = new UserAccountResourceApi();
        try {
            AccountDTO accountDTO = accountResourceApi.getAccountByAccountNoKeyUsingGET(accountNo, profileKey);
            if (accountDTO != null)
                return this.toAuthProfile(accountDTO);
        } catch (ApiException e) {
            logger.error("getAuthProfileByUid：{}", e.getMessage());
            throw new ServiceException(e.getCode(), "getAuthProfileByUid" + e.getMessage());
        }
        return null;
    }

    /**
     * 账号绑定。用于用户设置里面
     *
     * @param loginKey
     * @param password
     * @param loginDomain
     * @param profileKey
     * @param uno
     * @param createIp
     * @param createDate
     * @param icon
     * @param nick
     * @param paramMap
     * @return
     * @throws ServiceException
     */
    @Override
    public AuthProfile bind(String loginKey, String password, LoginDomain loginDomain, String profileKey, String uno, String createIp, Date createDate, String icon, String nick, HashMap<String, String> paramMap) throws ServiceException {
        UserAccountResourceApi userAccountResourceApi = new UserAccountResourceApi();
        //todo add token
        userAccountResourceApi.getApiClient().setAccessToken(oauthToken.get());
        BindDTO bindDTO = new BindDTO();
        bindDTO.login(loginKey).password(password).loginDomain(loginDomain.getCode()).profileKey(profileKey)
                .accountNo(uno).createdIp(createIp).icon(icon).nick(nick).extraParams(paramMap);
        AuthProfile authProfile = null;
        try {
            ApiResponse<AccountDTO> response = userAccountResourceApi.bindUsingPUTWithHttpInfo(bindDTO);
            if (response != null && response.getStatusCode() == 200) {
                authProfile = toAuthProfile(response.getData());
                authProfile.setToken(this.storeToken(response, this.resolveToken(response)));
            }
        } catch (ApiException e) {
            logger.error("绑定失败：" + e.getMessage());
            if (e.getMessageFromBody().equals("third.uid.has.binded.error"))
                throw UserCenterServiceException.PROFILE_HAS_EXISTS;
            else if (e.getMessageFromBody().equals("mobile.has.binded.error"))
                throw UserCenterServiceException.BIND_PHONE_ERROR_PHONE_HAS_BINDED;
            else
                throw new ServiceException(e.getCode(), "bind:" + e.getMessageFromBody());
        }
        return authProfile;
    }

    /**
     * 解绑
     *
     * @param loginDomain
     * @param profileKey
     * @param accountNo
     * @param createIp
     * @param createDate
     * @return
     * @throws ServiceException
     */
    @Override
    public boolean unbind(LoginDomain loginDomain, String profileKey, String accountNo, String createIp, Date createDate) throws ServiceException {
        UserAccountResourceApi userAccountResourceApi = new UserAccountResourceApi();
        //todo add token
        userAccountResourceApi.getApiClient().setAccessToken(oauthToken.get());
        boolean result;
        try {
            result = userAccountResourceApi.unBindUsingPUT(accountNo, loginDomain.getCode(), profileKey);
        } catch (ApiException e) {
            logger.error("解绑失败：" + e.getMessage());
            throw new ServiceException(e.getCode(), "unbind:" + e.getMessage());
        }
        return result;
    }

    /**
     * flag为logindomain和完善过昵称
     *
     * @param loginKey
     * @param password
     * @param loginDomain
     * @param profileKey
     * @param nick
     * @param createIp
     * @param createDate
     * @param paramMap
     * @return
     * @throws ServiceException
     */
    @Override
    public AuthProfile register(String loginKey, String password, LoginDomain loginDomain, String profileKey, String nick, String createIp, Date createDate, HashMap<String, String> paramMap) throws ServiceException {
        UserAccountResourceApi userAccountResourceApi = new UserAccountResourceApi();
        RegisterReqDTO registerReqDTO = new RegisterReqDTO();
        registerReqDTO.login(loginKey).password(password).loginDomain(loginDomain.getCode()).appKey("default")
                .profileKey(StringUtil.isEmpty(profileKey) ? "www" : profileKey)
                .createdIp(createIp).nick(nick).extraParams(paramMap);
        AuthProfile authProfile = null;
        try {
            ApiResponse<AccountDTO> response = userAccountResourceApi.registerUsingPOSTWithHttpInfo(registerReqDTO);
            if (response != null && response.getStatusCode() == 200) {
                authProfile = toAuthProfile(response.getData());
                authProfile.setToken(this.storeToken(response, this.resolveToken(response)));
            }
        } catch (ApiException e) {
            logger.error("注册失败：" + e.getMessage());
            if (e.getMessageFromBody().equals("login.error.exist"))
                throw UserCenterServiceException.USERLOGIN_HAS_EXISTS;
            else if (e.getMessageFromBody().equals("nick.error.exist"))
                throw UserCenterServiceException.NICK_HAS_EXIST;
            else if (e.getMessageFromBody().equals("mobileNo.error.valid.code"))
                throw UserCenterServiceException.PHONE_VERIFY_CODE_ERROR;
            else if (e.getMessageFromBody().equals("mobileNo.error.binded"))
                throw UserCenterServiceException.BIND_PHONE_ERROR_PHONE_HAS_BINDED;
            else
                throw new UserCenterServiceException(e.getCode(), e.getMessage());
        }
        return authProfile;
    }

    /**
     * 登录
     *
     * @param loginKey
     * @param password
     * @param loginDomain
     * @param profileKey
     * @param createIp
     * @param createDate
     * @param paramMap
     * @return
     * @throws ServiceException
     */
    @Override
    public AuthProfile login(String loginKey, String password, LoginDomain loginDomain, String profileKey, String createIp, Date createDate, HashMap<String, String> paramMap) throws ServiceException {
        UserAccountResourceApi userAccountResourceApi = new UserAccountResourceApi();
        boolean remember = false;
        if (!StringUtil.isEmpty(paramMap.get("remember"))) {
            remember = paramMap.get("remember").equals("true") ? true : false;
        }
        LoginVM loginVM = new LoginVM().username(loginKey).password(password).profileKey(profileKey).rememberMe(remember).loginDomain(loginDomain.getCode());
        AuthProfile authProfile = null;
        try {
            ApiResponse<AccountDTO> response = userAccountResourceApi.loginUsingPOSTWithHttpInfo(loginVM);
            if (response != null && response.getStatusCode() == 200) {
                authProfile = toAuthProfile(response.getData());
                authProfile.setToken(this.storeToken(response, this.resolveToken(response)));
            }
        } catch (ApiException e) {
            logger.info("登陆失败：" + e.getMessageFromBody());
            //throw new UserCenterServiceException(e.getCode(), e.getMessage());
            throw UserCenterServiceException.USERLOGIN_NOT_EXISTS;
        }
        return authProfile;
    }

    /**
     * 获得当前登陆者的账户信息
     *
     * @return
     */
    @Override
    public AuthProfile getCurrentAccount() throws ServiceException {
        logger.info("****** token is : {} ********", oauthToken.get());
        if (StringUtil.isEmpty(oauthToken.get())) {
            logger.info("******** token is null ***************");
            return null;
        }
        UserAccountResourceApi accountResourceApi = new UserAccountResourceApi();
        accountResourceApi.getApiClient().setAccessToken(oauthToken.get());
        AuthProfile authProfile = null;
        try {
            ApiResponse<AccountDTO> response = accountResourceApi.getCurrentAccountUsingGETWithHttpInfo();
            if (response != null && response.getStatusCode() == 200) {
                authProfile = toAuthProfile(response.getData());
                authProfile.setToken(this.storeToken(response, oauthToken.get()));
                return authProfile;
            }
        } catch (ApiException e) {
            logger.error("getCurrentAccount:{}", e.getMessage());
            // throw new ServiceException(e.getCode(), "getCurrentAccount" + e.getMessage());
        }
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
        if (StringUtil.isEmpty(oauthToken.get()))
            return;
        UserAccountResourceApi accountResourceApi = new UserAccountResourceApi();
        accountResourceApi.getApiClient().setAccessToken(oauthToken.get());
        try {
            accountResourceApi.changePasswordUsingPUT(newPassword);
        } catch (ApiException e) {
            logger.error("changePassword：{} code:{}", e.getMessage(), e.getCode());
            throw new ServiceException(e.getCode(), "changePassword:" + e.getMessage());
        }
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
        UserAccountResourceApi accountResourceApi = new UserAccountResourceApi();
        try {
            accountResourceApi.forgetPasswordUsingPUT(mobileNo, validCode, newPassword);
        } catch (ApiException e) {
            logger.error("changePassword：{} code:{}", e.getMessage(), e.getCode());
            if (e.getMessageFromBody().equals("mobile.valid.code.error"))
                throw UserCenterServiceException.PHONE_VERIFY_CODE_ERROR;
        }
    }

    /**
     * 修改手机号
     *
     * @param newMobileNo
     * @throws ServiceException
     */
    @Override
    public com.enjoyf.platform.userservice.client.model.UserLogin changeMobileNo(String newMobileNo) throws ServiceException {
        if (StringUtil.isEmpty(oauthToken.get()))
            return null;
        UserAccountResourceApi accountResourceApi = new UserAccountResourceApi();
        accountResourceApi.getApiClient().setAccessToken(oauthToken.get());
        com.enjoyf.platform.userservice.client.model.UserLogin login;
        try {
            login = accountResourceApi.changeMobileNoUsingPUT(newMobileNo);
        } catch (ApiException e) {
            logger.error("changeMobileNo：{} code:{}", e.getMessage(), e.getCode());
            throw UserCenterServiceException.BIND_PHONE_ERROR_PHONE_HAS_BINDED;
        }
        return login;
    }

    /**
     * 本地存储token
     *
     * @param response
     */
    private Token storeToken(ApiResponse<AccountDTO> response, String token) {

        Token localToken = new Token();
        localToken.setProfileId(response.getData().getUserProfile().getProfileNo());
        localToken.setProfileKey(response.getData().getUserProfile().getProfileKey());
        localToken.setToken(token);
        localToken.setTokenExpires(response.getData().getTokenValiditySeconds().intValue());
        localToken.setUid(response.getData().getUserProfile().getId());
        localToken.setUno(response.getData().getUserAccount().getAccountNo());
        oauthToken.set(token);
        return localToken;
    }


    private int getPageTotal(ApiResponse<?> response) {
        String total = this.getResponseHeader(response, PAGE_TOTAL);
        if (StringUtil.isEmpty(total))
            return 0;
        return Integer.parseInt(total);
    }

    /**
     * 抽取jwt token
     *
     * @param response
     * @return
     */
    private String resolveToken(ApiResponse<AccountDTO> response) {
        String bearerToken = this.getResponseHeader(response, AUTHORIZATION_HEADER);
        if (bearerToken == null) return "";
        if (!StringUtil.isEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return "";
    }

    /**
     * 根据header 获得响应值
     *
     * @param response
     * @param headerName
     * @return
     */
    private String getResponseHeader(ApiResponse<?> response, String headerName) {
        List<String> headerValues = response.getHeaders().get(headerName);
        if (headerValues == null || headerValues.isEmpty()) return null;
        return headerValues.get(0);
    }

    /**
     * 第三方登录，自注册登录等需要用到。
     *
     * @param loginKey
     * @param loginDomain
     * @param tokenInfo
     * @param icon
     * @param nick
     * @param profileKey
     * @param createIp
     * @param createDate
     * @param paramMap
     * @return
     * @throws ServiceException
     */
    @Override
    public AuthProfile auth(String loginKey, LoginDomain loginDomain, TokenInfo tokenInfo, String icon, String nick, String profileKey, String createIp, Date createDate, HashMap<String, String> paramMap) throws ServiceException {
        AuthProfile authProfile = null;
        UserAccountResourceApi accountResourceApi = new UserAccountResourceApi();
        SocialAuthDTO socialAuthDTO = new SocialAuthDTO();
        socialAuthDTO.login(loginKey).loginDomain(loginDomain.getCode()).icon(icon).nick(nick).profileKey(profileKey).createdIp(createIp).extraParams(paramMap);
        try {
            ApiResponse<AccountDTO> response = accountResourceApi.socialLoginUsingPOSTWithHttpInfo(socialAuthDTO);
            if (response != null && response.getStatusCode() == 200) {
                authProfile = this.toAuthProfile(response.getData());
                authProfile.setToken(this.storeToken(response, this.resolveToken(response)));

            }
        } catch (ApiException e) {
            logger.error("第三方登陆失败：{}", e.getMessage());
            throw new ServiceException(e.getCode(), "第三方登陆失败" + e.getMessage());
        }
        return authProfile;
    }

    /**
     * 删除token
     *
     * @param token
     * @return
     * @throws ServiceException
     */
    @Override
    public boolean deleteToken(String token) throws ServiceException {
        oauthToken.remove();
        return true;
    }

    /**
     * 获取token
     *
     * @param token
     * @return
     * @throws ServiceException
     */
    @Override
    @Deprecated
    public Token getToken(String token) throws ServiceException {
        return null;
    }

    /**
     * 根据pid获取用户
     *
     * @param profileNo
     * @return
     * @throws ServiceException
     */
    @Override
    public Profile getProfileByProfileId(String profileNo) throws ServiceException {
        if (StringUtil.isEmpty(profileNo))
            return null;
        UserProfileResourceApi profileResourceApi = new UserProfileResourceApi();
        Profile profile = null;
        try {
            UserProfile userProfile = profileResourceApi.getUserProfileByProfileNoUsingGET(profileNo);
            if (userProfile != null)
                profile = toProfile(userProfile);
        } catch (ApiException e) {
            logger.info("获取用户信息失败：code={},mesage={}", e.getCode(), e.getMessage());
            //throw new ServiceException(e.getCode(), "profile:" + e.getMessage());
            return null;
        }
        return profile;
    }

    /**
     * 同上
     *
     * @param uno
     * @param profileKey
     * @return
     * @throws ServiceException
     */
    @Override
    public Profile getProfileByUno(String uno, String profileKey) throws ServiceException {
        String profileNo = UserCenterUtil.getProfileId(uno, profileKey);
        return this.getProfileByProfileId(profileNo);
    }

    /**
     * 获取uid
     *
     * @param uid
     * @return
     * @throws ServiceException
     */
    @Override
    public Profile getProfileByUid(long uid) throws ServiceException {
        UserProfileResourceApi profileResourceApi = new UserProfileResourceApi();
        Profile profile = null;
        try {
            UserProfile userProfile = profileResourceApi.getUserProfileUsingGET(uid);
            if (userProfile != null)
                profile = toProfile(userProfile);
        } catch (ApiException e) {
            logger.info("获取用户信息：" + e.getMessage());
            throw new ServiceException(e.getCode(), "profile:" + e.getMessage());
        }
        return profile;
    }

    /**
     * 批量查询  by uids
     *
     * @param uids
     * @return
     * @throws ServiceException
     */
    @Override
    public Map<Long, Profile> queryProfilesByUids(Set<Long> uids) throws ServiceException {
        UserProfileResourceApi profileResourceApi = new UserProfileResourceApi();
        Map<Long, Profile> profiles = null;
        try {
            List<Long> ids = new ArrayList<Long>();
            for (Long id : uids) {
                ids.add(id);
            }
            List<UserProfile> userProfiles = profileResourceApi.getProfilesByIdsUsingGET(ids);
            if (!CollectionUtil.isEmpty(userProfiles)) {
                profiles = new HashMap<Long, Profile>();
                for (UserProfile userProfile : userProfiles) {
                    Profile profile = toProfile(userProfile);
                    profiles.put(userProfile.getId(), profile);
                }

            }
        } catch (ApiException e) {
            logger.info("批量获取用户信息失败：" + e.getMessage());
            throw new ServiceException(e.getCode(), "batch profile:" + e.getMessage());
        }
        return profiles;
    }

    @Override
    public List<Profile> listProfilesByIds(Set<Long> uids) throws ServiceException {
        UserProfileResourceApi profileResourceApi = new UserProfileResourceApi();
        List<Profile> profiles = null;
        try {
            List<Long> ids = new ArrayList<Long>();
            for (Long id : uids) {
                ids.add(id);
            }
            List<UserProfile> userProfiles = profileResourceApi.getProfilesByIdsUsingGET(ids);
            if (!CollectionUtil.isEmpty(userProfiles)) {
                profiles = new ArrayList<Profile>();
                for (UserProfile userProfile : userProfiles) {
                    Profile profile = toProfile(userProfile);
                    profiles.add(profile);
                }

            }
        } catch (ApiException e) {
            logger.info("批量获取用户信息失败：" + e.getMessage());
            throw new ServiceException(e.getCode(), "batch profile:" + e.getMessage());
        }
        return profiles;
    }

    /**
     * 批量查询 by profielIds
     *
     * @param profielIds
     * @return
     * @throws ServiceException
     */
    @Override
    public Map<String, Profile> queryProfiles(Set<String> profielIds) throws ServiceException {
        if (CollectionUtil.isEmpty(profielIds)) return null;
        UserProfileResourceApi profileResourceApi = new UserProfileResourceApi();
        Map<String, Profile> profiles = new HashMap<String, Profile>();
        try {
            List<String> ids = new ArrayList<String>();
            for (String profileNo : profielIds) {
                ids.add(profileNo);
            }
            List<UserProfile> userProfiles = profileResourceApi.getProfilesByProfileNosUsingGET(ids);
            if (!CollectionUtil.isEmpty(userProfiles)) {

                for (UserProfile userProfile : userProfiles) {
                    Profile profile = toProfile(userProfile);
                    profiles.put(userProfile.getProfileNo(), profile);
                }

            }
        } catch (ApiException e) {
            logger.info("批量获取用户信息失败：" + e.getMessage());
            throw new ServiceException(e.getCode(), "batch profile:" + e.getMessage());
        }
        return profiles;
    }

    /**
     * 通过昵称获取
     *
     * @param nick
     * @return
     * @throws ServiceException
     */
    @Override
    public Profile getProfileByNick(String nick) throws ServiceException {
        UserProfileResourceApi profileResourceApi = new UserProfileResourceApi();
        Profile profile = null;
        try {
            ApiResponse<UserProfile> userProfileResponse = profileResourceApi.getProfileByNickUsingGETWithHttpInfo(nick);
            if (userProfileResponse.getStatusCode() == 200)
                profile = toProfile(userProfileResponse.getData());
        } catch (ApiException e) {
            logger.info("获取用户信息：" + e.getMessage());
            if ( e.getCode()!=404) {
                throw new ServiceException(e.getCode(), "profile:" + e.getMessage());
            }
        }

        return profile;
    }

    /**
     * 修改用户信息
     *
     * @param updateExpress
     * @param profileNo
     * @return
     * @throws ServiceException
     */
    @Override
    public boolean modifyProfile(UpdateExpress updateExpress, String profileNo) throws ServiceException {
        UserProfileResourceApi profileResourceApi = new UserProfileResourceApi();
        try {
            UserProfile userProfile = profileResourceApi.getUserProfileByProfileNoUsingGET(profileNo);
            if (userProfile == null)
                return false;
            this.updateUserProfile(userProfile, updateExpress);
            profileResourceApi.updateUserProfileUsingPUT(userProfile);
        } catch (ApiException e) {
            logger.info("修改用户信息：" + e.getMessage());
            throw new ServiceException(e.getCode(), "update profile:" + e.getMessage());
        }
        return true;
    }

    @Override
    public Profile updateNick(String nick) throws ServiceException {
        if (StringUtil.isEmpty(oauthToken.get()))
            throw UserCenterServiceException.USERLOGIN_NOT_EXISTS;
        UserProfileResourceApi profileResourceApi = new UserProfileResourceApi();
        Profile profile = null;
        try {
            profileResourceApi.getApiClient().setAccessToken(oauthToken.get());
            profile = toProfile(profileResourceApi.updateNickUsingPUT(nick));
        } catch (ApiException e) {
            logger.info("修改用户昵称：" + e.getMessage());
            if (e.getMessageFromBody().equals("nick.error.exist"))
                throw UserCenterServiceException.NICK_HAS_EXIST;
            else
                throw new UserCenterServiceException(e.getCode(), e.getMessage());
        }
        return profile;
    }

    private void updateUserProfile(UserProfile userProfile, UpdateExpress updateExpress) {
        if (updateExpress.containField(ProfileField.NICK)) {
            userProfile.setNick(updateExpress.getUpdateValueByField(ProfileField.NICK).toString());
            userProfile.setLowercaseNick(updateExpress.getUpdateValueByField(ProfileField.NICK).toString().toLowerCase());
            if (updateExpress.containField(ProfileField.FLAG))
                userProfile.setFlag(new ProfileFlag(userProfile.getFlag()).has(ProfileFlag.FLAG_NICK_HASCOMPLETE).getValue());
        }
        if (updateExpress.containField(ProfileField.ICON))
            userProfile.setIcon(updateExpress.getUpdateValueByField(ProfileField.ICON).toString());
        if (updateExpress.containField(ProfileField.PROVINCEID))
            userProfile.setProvinceId(Integer.parseInt(updateExpress.getUpdateValueByField(ProfileField.PROVINCEID).toString()));
        if (updateExpress.containField(ProfileField.CITYID))
            userProfile.setCityId(Integer.parseInt(updateExpress.getUpdateValueByField(ProfileField.CITYID).toString()));
        if (updateExpress.containField(ProfileField.MOBILE))
            userProfile.setMobileNo(updateExpress.getUpdateValueByField(ProfileField.MOBILE).toString());
        if (updateExpress.containField(ProfileField.BIRTHDAY))
            userProfile.setBirthDay(updateExpress.getUpdateValueByField(ProfileField.BIRTHDAY).toString());
        if (updateExpress.containField(ProfileField.SEX))
            userProfile.setSex(Integer.parseInt(updateExpress.getUpdateValueByField(ProfileField.SEX).toString()));
        if (updateExpress.containField(ProfileField.SIGNATURE))
            userProfile.setSignature(updateExpress.getUpdateValueByField(ProfileField.SIGNATURE).toString());
        if (updateExpress.containField(ProfileField.DESCRIPTION))
            userProfile.setDiscription(updateExpress.getUpdateValueByField(ProfileField.DESCRIPTION).toString());
        if (updateExpress.containField(ProfileField.DOMAIN))
            userProfile.setDomain(updateExpress.getUpdateValueByField(ProfileField.DOMAIN).toString());
        if (updateExpress.containField(ProfileField.REALNAME))
            userProfile.setRealName(updateExpress.getUpdateValueByField(ProfileField.REALNAME).toString());
        if (updateExpress.containField(ProfileField.HOBBY))
            userProfile.setHobby(updateExpress.getUpdateValueByField(ProfileField.HOBBY).toString());


    }

    /**
     * 用于虚拟用户的创建不能对外使用
     *
     * @param profile
     * @return
     * @throws ServiceException
     */
    @Override
    public Profile createProfile(Profile profile) throws ServiceException {
        if (profile == null) return null;
        UserProfileResourceApi profileResourceApi = new UserProfileResourceApi();
        try {
            UserProfile userProfile = createUserProfile(profile);
            userProfile = profileResourceApi.createUserProfileUsingPOST(userProfile);
            if (userProfile != null)
                profile.setUid(userProfile.getId());
        } catch (ApiException e) {
            logger.info("获取用户信息：" + e.getMessage());
            throw new ServiceException(e.getCode(), "profile:" + e.getMessage());
        }
        return profile;
    }

    private UserProfile createUserProfile(Profile profile) {
        UserProfile userProfile = new UserProfile();
        userProfile.setAccountNo(profile.getUno());
        userProfile.setProfileKey(profile.getProfileKey());
        userProfile.setProfileNo(profile.getProfileId());
        userProfile.setAppKey(profile.getAppkey());
        userProfile.setNick(profile.getNick());
        userProfile.setLowercaseNick(profile.getCheckNick());
        userProfile.setIcon(profile.getIcon());
        userProfile.setDiscription(profile.getDescription());
        userProfile.setSex(Integer.parseInt(profile.getSex()));
        userProfile.setCreatedIp(profile.getCreateIp());
        userProfile.setBirthDay(profile.getBirthday());
        userProfile.setBackPic(profile.getBackPic());
        return userProfile;
    }

    /**
     * 通过域名获取profile 暂时不用
     *
     * @param domain
     * @return
     * @throws ServiceException
     */
    @Override
    @Deprecated
    public Profile getProfileByDomain(String domain) throws ServiceException {
        return null;
    }

    /**
     * 后台工具通过DB查询获取用户信息
     * 用
     *
     * @param queryExpress
     * @return
     * @throws ServiceException
     */
    @Override
    public List<Profile> queryProfile(QueryExpress queryExpress) throws ServiceException {
        //todo getProfileByNick 和 listProfilesByIds 代替
        return null;
    }

    @Override
    public List<Profile> queryProfilesByNickLike(String nick) throws ServiceException {
        UserProfileResourceApi profileResourceApi = new UserProfileResourceApi();
        List<Profile> profiles = null;
        try {
            List<UserProfile> userProfiles = profileResourceApi.getProfileByLikeNickUsingGET(nick);
            if (!CollectionUtil.isEmpty(userProfiles)) {
                profiles = new ArrayList<Profile>();
                profiles.add(toProfile(userProfiles.get(0)));
            }
        } catch (ApiException e) {
            logger.info("获取用户信息：" + e.getMessage());
            throw new ServiceException(e.getCode(), "profile:" + e.getMessage());
        }
        return profiles;
    }

    /**
     * 分页查询
     *
     * @param queryExpress
     * @param pagination
     * @return
     * @throws ServiceException
     */
    @Override
    public PageRows<Profile> queryProfileByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        UserProfileResourceApi profileResourceApi = new UserProfileResourceApi();
        PageRows<Profile> pageRows = null;
        try {
            ApiResponse<List<UserProfile>> response = profileResourceApi.getAllUserProfilesUsingGETWithHttpInfo(pagination.getCurPage() - 1, pagination.getPageSize(), null);
            if (response != null && response.getStatusCode() == 200) {
                pageRows = new PageRows<Profile>();
                List<Profile> list = new ArrayList<Profile>();
                for (UserProfile userProfile : response.getData()) {
                    Profile profile = toProfile(userProfile);
                    list.add(profile);
                }
                pageRows.setRows(list);
                pagination.setTotalRows(this.getPageTotal(response));
                pageRows.setPage(pagination);
            }
        } catch (ApiException e) {
            logger.info("获取所有用户信息：" + e.getMessage());
            throw new ServiceException(e.getCode(), "getAllprofile:" + e.getMessage());
        }
        return pageRows;
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
        UserProfileResourceApi profileResourceApi = new UserProfileResourceApi();
        PageRows<Profile> pageRows = new PageRows<Profile>();
        try {
            List<String> sorts = new ArrayList<String>();
            sorts.add("createdTime,desc");
            ApiResponse<List<UserProfile>> response = profileResourceApi.searchAllUserProfilesUsingGETWithHttpInfo(pagination.getCurPage() - 1, pagination.getPageSize(), nick, inOrNot, profileNos, startTime, endTime, sorts);
            if (response != null && response.getStatusCode() == 200) {
                List<Profile> list = new ArrayList<Profile>();
                for (UserProfile userProfile : response.getData()) {
                    Profile profile = toProfile(userProfile);
                    list.add(profile);
                }
                pageRows.setRows(list);
                pagination.setTotalRows(this.getPageTotal(response));
                pageRows.setPage(pagination);
            }
        } catch (ApiException e) {
            logger.info("获取所有用户信息：" + e.getMessage());
            //throw new ServiceException(e.getCode(), "getAllprofile:" + e.getMessage());
        }
        return pageRows;
    }

    /**
     * 手机验证码
     *
     * @param uno
     * @param code
     * @return
     * @throws ServiceException
     */
    @Override
    @Deprecated
    public String saveMobileCode(String uno, String code) throws ServiceException {


        return null;
    }

    /**
     * 获取手机验证码
     *
     * @param uno
     * @return
     * @throws ServiceException
     */
    @Override
    @Deprecated
    public String getMobileCode(String uno) throws ServiceException {
        UserMobileResourceApi mobileResourceApi = new UserMobileResourceApi();

        return null;
    }

    /**
     * 删除手机验证码
     *
     * @param uno
     * @return
     * @throws ServiceException
     */
    @Override
    @Deprecated
    public boolean removeMobileCode(String uno) throws ServiceException {
        return false;
    }

    /**
     * 修改密码等操作发送的手机码
     *
     * @param loginId
     * @param time
     * @return
     * @throws ServiceException
     */
    @Override
    @Deprecated
    public boolean savePasswordCode(String loginId, Long time) throws ServiceException {
        return false;
    }

    @Override
    @Deprecated
    public Long getPassordCode(String loginId) throws ServiceException {
        return null;
    }

    @Override
    @Deprecated
    public boolean removePasswordCode(String loginId) throws ServiceException {
        return false;
    }

    /**
     * uno get useraccount
     *
     * @param uno
     * @return
     * @throws ServiceException
     */
    @Override
    public UserAccount getUserAccount(String uno) throws ServiceException {
        UserAccountResourceApi accountResourceApi = new UserAccountResourceApi();
        UserAccount account = null;
        try {
            //todo 添加token
            accountResourceApi.getApiClient().setAccessToken(oauthToken.get());
            AccountDTO accountDTO = accountResourceApi.getCurrentAccountUsingGET();
            if (accountDTO != null)
                account = toAccount(accountDTO.getUserAccount());
        } catch (ApiException e) {
            logger.info("获取当前登陆用户信息：" + e.getMessage());
            throw new ServiceException(e.getCode(), "getUserAccount:" + e.getMessage());
        }
        return account;
    }

    /**
     * 修改Account 收货地址
     *
     * @param upateExpress
     * @param uno
     * @return
     * @throws ServiceException
     */
    @Override
    public boolean modifyUserAccount(UpdateExpress upateExpress, String uno) throws ServiceException {
        UserAccountResourceApi accountResourceApi = new UserAccountResourceApi();
        //todo 添加token
        accountResourceApi.getApiClient().setAccessToken(oauthToken.get());
        try {
            AccountDTO accountDTO = accountResourceApi.getCurrentAccountUsingGET();
            if (accountDTO == null) return false;
            accountDTO.getUserAccount().setAddress(upateExpress.getUpdateValueByField(UserAccountField.ADDRESS).toString());
            accountResourceApi.updateUserAccountUsingPUT(accountDTO.getUserAccount());
        } catch (ApiException e) {
            logger.info("修改当前登陆用户地址：" + e.getMessage());
            throw new ServiceException(e.getCode(), "getUserAccount:" + e.getMessage());
        }
        return true;
    }

    @Override
    @Deprecated
    public boolean initUidPool(long startNum, long endNum) throws ServiceException {
        return false;
    }

    @Override
    @Deprecated
    public long getUidPoolLength() throws ServiceException {
        return 0;
    }

//    /**
//     * 查询用户计数
//     *
//     * @param profileIds
//     * @return
//     * @throws ServiceException
//     */
//    @Override
//    public Map<String, ProfileSum> queryProfileSumByProfileids(Set<String> profileIds) throws ServiceException {
//        Map<String,ProfileSum> profileSumMap = new HashMap<String,ProfileSum>();
//        if(CollectionUtil.isEmpty(profileIds)) return profileSumMap;
//        ProfileCountResourceApi profileCountResourceApi = new ProfileCountResourceApi();
//        try {
//            List<String> profileNos = new ArrayList<String>();
//            for(String profileId : profileIds){
//                profileNos.add(profileId);
//            }
//            List<ProfileCount> summaries = profileCountResourceApi.getProfileCountsUsingGET(profileNos);
//            if(!CollectionUtil.isEmpty(summaries)){
//                for(ProfileCount profileSummary : summaries) {
//                    profileSumMap.put(profileSummary.getProfileNo(),toProfileSum(profileSummary));
//                }
//            }
//        } catch (ApiException e) {
//            logger.info("getProfileSum errorcode:{},mesage:{}",e.getCode(),e.getMessage());
//        }
//        return profileSumMap;
//    }
//    private ProfileSum toProfileSum(ProfileCount profileSummary){
//        ProfileSum profileSum = new ProfileSum();
//        profileSum.setProfileId(profileSummary.getProfileNo());
//        profileSum.setFansSum(profileSummary.getFans());
//        profileSum.setFollowSum(profileSummary.getFollows());
//        return profileSum;
//    }
//
//    @Override
//    public boolean increaseProfileSum(String profileId, ProfileSumField sumFiled, int value) throws ServiceException {
//        if(sumFiled==null||StringUtil.isEmpty(sumFiled.getColumn()))
//            return false;
//        ProfileCountResourceApi profileCountResourceApi = new ProfileCountResourceApi();
//        String fieldType = sumFiled.getColumn().equals("followsum")?"follows":"fans";
//        try {
//            profileCountResourceApi.incrementUsingPUT(profileId,value,fieldType);
//        } catch (ApiException e) {
//            logger.error("increaseProfileSum errorcode:{},message:{}",e.getCode(),e.getMessage());
//            throw   new ServiceException(e.getCode(),e.getMessage());
//        }
//        return true;
//    }
//
//    @Override
//    public ProfileSum getProfileSum(String profileId) throws ServiceException {
//        ProfileSum profileSum = null ;
//        ProfileCountResourceApi profileCountResourceApi = new ProfileCountResourceApi();
//        try {
//           ProfileCount summary = profileCountResourceApi.getProfileCountUsingGET(profileId);
//           if(summary!=null){
//               profileSum = new ProfileSum();
//               profileSum.setProfileId(profileId);
//               profileSum.setFansSum(summary.getFans());
//               profileSum.setFollowSum(summary.getFans());
//           }
//        } catch (ApiException e) {
//            logger.info("getProfileSum errorcode:{},mesage:{}",e.getCode(),e.getMessage());
//            return null;
//        }
//        return profileSum;
//    }

    ////////////
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
        return returnMap;
    }

    public boolean increaseProfileSum(String profileId, ProfileSumField sumFiled, int value) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " profileId :" + profileId);
        }
        GAlerter.lab(this.getClass().getName() + " profileId :" + profileId + " sumfield:" + sumFiled.getColumn());
        userCenterRedis.increaseProfileSum(profileId, sumFiled, value);
        return true;
    }


    public ProfileSum getProfileSum(String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getProfileSum profileId: " + profileId);
        }

        return userCenterRedis.getProfileSum(profileId);
    }

    /**
     * 手机绑定接口
     *
     * @param mobile
     * @param profileKey
     * @return
     * @throws ServiceException
     */
    @Override
    @Deprecated
    public boolean checkMobileIsBinded(String mobile, String profileKey) throws ServiceException {
        return false;
    }

    @Override
    public boolean bindMobile(String mobile, String profileId, String ip) throws ServiceException {
        Profile profile = this.getProfileByProfileId(profileId);
        if (profile == null) {
            throw UserCenterServiceException.PROFILE_NOT_EXISTS;
        }

        if (!com.enjoyf.util.StringUtil.isEmpty(profile.getMobile())) {
            GAlerter.lan(this.getClass().getName() + " bindMobile error.profile mobile is not null" + profile.getProfileId() + " " + profile.getMobile());
            throw UserCenterServiceException.BIND_PHONE_ERROR_PRFOILE_HAS_BINDED;
        }
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ProfileField.MOBILE, mobile);
        this.modifyProfile(updateExpress, profileId);
        return true;
    }

    @Override
    @Deprecated
    public boolean unbindMobile(String profileMobileId) throws ServiceException {

        return false;
    }

    @Override
    @Deprecated
    public boolean unbindMobile(String profileId, String ip) throws ServiceException {
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ProfileField.MOBILE, "");
        return this.modifyProfile(updateExpress, profileId);
    }

    @Override
    public PageRows<ActivityProfile> queryActivityProfile(String appkey, String subkey, Pagination pagination) throws ServiceException {
        return null;
    }

    @Override
    public int getActvitiyUserSum(String appkey, String subkey) throws ServiceException {
        return 0;
    }

    @Override
    public Map<String, VerifyProfile> queryProfileByIds(Set<String> profileIds) throws ServiceException {
        if (CollectionUtil.isEmpty(profileIds)) return null;
        PlayerResourceApi playerResourceApi = new PlayerResourceApi();
        List<String> profileNos = new ArrayList<String>();
        for (String profileNo : profileIds) {
            profileNos.add(profileNo);
        }
        Map<String, VerifyProfile> result = new HashMap<String, VerifyProfile>();
        try {
            List<Player> players = playerResourceApi.getAllPlayersByProfileNosUsingGET(profileNos);
            for (Player player : players) {
                result.put(player.getProfileNo(), toVerifyProfile(player));
            }
        } catch (ApiException e) {
            logger.info("queryProfileByIds : code={},message={}", e.getCode(), e.getMessage());
        }
        return result;
    }

    @Override
    public VerifyProfile getVerifyProfileById(String profileId) throws ServiceException {
        PlayerResourceApi playerResourceApi = new PlayerResourceApi();
        try {
            Player player = playerResourceApi.getPlayerByProfileNoUsingGET(profileId);
            if (player != null)
                return toVerifyProfile(player);
        } catch (ApiException e) {
            logger.info("getVerifyProfileById by profileId: {} is not found . responseCode: {} ",
                    profileId, e.getCode());
            return null;
        }
        return null;
    }

    private VerifyProfile toVerifyProfile(Player player) {
        VerifyProfile verifyProfile = new VerifyProfile();
        verifyProfile.setAppkey(player.getAppKey());
        verifyProfile.setAskPoint(player.getPoint() == null ? 0 : player.getPoint());
        verifyProfile.setNick(player.getNick());
        verifyProfile.setProfileId(player.getProfileNo());
        //todo
        verifyProfile.setVerifyType(player.getLevelId());
        verifyProfile.setVerifyTitle(player.getLevelName());
        verifyProfile.setDescription(player.getDescription());
        return verifyProfile;
    }

    @Override
    public boolean modifyVerifyProfile(String profileId, UpdateExpress updateExpress) throws ServiceException {

        PlayerResourceApi playerResourceApi = new PlayerResourceApi();
        try {
            Player player = playerResourceApi.getPlayerByProfileNoUsingGET(profileId);
            if (player == null)
                return false;
            if (updateExpress.containField(VerifyProfileField.APPKEY))
                player.setAppKey(updateExpress.getUpdateValueByField(VerifyProfileField.APPKEY).toString());
            if (updateExpress.containField(VerifyProfileField.DESCRIPTION))
                player.setDescription(updateExpress.getUpdateValueByField(VerifyProfileField.DESCRIPTION).toString());
            if (updateExpress.containField(VerifyProfileField.POINT))
                player.setPoint((Integer) updateExpress.getUpdateValueByField(VerifyProfileField.POINT));
            if (updateExpress.containField(VerifyProfileField.VERIFYTYPE))
                player.setLevelId((Long) updateExpress.getUpdateValueByField(VerifyProfileField.VERIFYTYPE));
            playerResourceApi.updatePlayerUsingPUT(player);
        } catch (ApiException e) {
            logger.info(" verifyProfile is error. code:{},message:{}", e.getCode(), e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    @Deprecated
    public PageRows<VerifyProfile> queryVerifyProfile(QueryExpress queryExpress, Pagination page) throws ServiceException {

        return null;
    }

    @Override
    public PageRows<VerifyProfile> queryPlayers(String nick, String appKey, Long levelId, Pagination page) throws ServiceException {
        PlayerResourceApi playerResourceApi = new PlayerResourceApi();
        PageRows<VerifyProfile> result = new PageRows<VerifyProfile>();
        try {
            ApiResponse<List<Player>> response = playerResourceApi.searchAllPlayersUsingGETWithHttpInfo(page.getCurPage() - 1, page.getPageSize(), null, null, levelId, null, null, nick, appKey, null, null);
            if (response != null && response.getStatusCode() == 200) {
                List<VerifyProfile> list = new ArrayList<VerifyProfile>();
                for (Player player : response.getData()) {
                    list.add(toVerifyProfile(player));
                }
                result.setRows(list);
                page.setTotalRows(this.getPageTotal(response));
                result.setPage(page);

            }
        } catch (ApiException e) {
            logger.info("queryPlayers : message={} ,responseCode={} ",
                    e.getMessage(), e.getCode());
            //return null;
        }
        return result;
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
            PlayerResourceApi playerResourceApi = new PlayerResourceApi();
            try {
                playerResourceApi.createPlayerUsingPOST(toPlayer(profile));
            } catch (ApiException e) {
                logger.info(" verifyProfile is error. code:{},message:{}", e.getCode(), e.getMessage());
                return false;
            }
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

    private Player toPlayer(VerifyProfile profile) {
        Player player = new Player();
        player.appKey(profile.getAppkey()).profileNo(profile.getProfileId()).point(profile.getAskPoint())
                .description(profile.getDescription()).nick(profile.getNick()).levelId(profile.getVerifyType());
        return player;
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
    public Verify addVerify(Verify verify) throws ServiceException {
        return null;
    }

    @Override
    public List<Verify> queryVerify(QueryExpress queryExpress) throws ServiceException {
        PlayerLevelResourceApi levelResourceApi = new PlayerLevelResourceApi();
        List<Verify> result = new ArrayList<Verify>();
        try {

            List<PlayerLevel> levels = levelResourceApi.getAllPlayerLevelsUsingGET();
            for (PlayerLevel level : levels) {
                if (level.getStatus() == PlayerLevel.StatusEnum.VALID) {
                    result.add(toVerify(level));
                }
            }
        } catch (ApiException e) {
            logger.error("queryVerify is not found. code:{},message:{} ", e.getCode(), e.getMessage());
            return null;
        }
        return result;
    }

    private Verify toVerify(PlayerLevel level) {
        Verify verify = new Verify();
        verify.setVerifyId(level.getId());
        verify.setVerifyName(level.getName());
        verify.setValidStatus(ValidStatus.VALID);
        return verify;
    }

    @Override
    public PageRows<Verify> queryVerifyByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        PlayerLevelResourceApi levelResourceApi = new PlayerLevelResourceApi();
        PageRows<Verify> pageRows = null;
        try {
            List<PlayerLevel> levels = levelResourceApi.getAllPlayerLevelsUsingGET();
            pageRows = new PageRows<Verify>();
            List<Verify> list = new ArrayList<Verify>();
            for (PlayerLevel level : levels) {
                list.add(toVerify(level));
            }
            pageRows.setPage(pagination);
            pageRows.setRows(list);
        } catch (ApiException e) {
            logger.error("queryVerifyByPage error, code:{},message:{}", e.getCode(), e.getMessage());
            return null;
        }
        return pageRows;
    }

    @Override
    public Verify getVerify(Long verifyId) throws ServiceException {
        PlayerLevelResourceApi levelResourceApi = new PlayerLevelResourceApi();
        try {
            PlayerLevel level = levelResourceApi.getPlayerLevelUsingGET(verifyId);
            return toVerify(level);
        } catch (ApiException e) {
            logger.error("getVerify error,code:{},message:{}", e.getCode(), e.getMessage());
            return null;
        }
    }

    @Override
    public boolean modifyVerify(Long verifyId, UpdateExpress updateExpress) throws ServiceException {
        PlayerLevelResourceApi levelResourceApi = new PlayerLevelResourceApi();
        try {
            PlayerLevel level = levelResourceApi.getPlayerLevelUsingGET(verifyId);
            if (updateExpress.containField(VerifyField.VERIFY_NAME)) {
                level.setName(updateExpress.getUpdateValueByField(VerifyField.VERIFY_NAME).toString());
                levelResourceApi.updatePlayerLevelUsingPUT(level);
            }
        } catch (ApiException e) {
            logger.error("getVerify error,code:{},message:{}", e.getCode(), e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public UserPrivacy addUserPrivacy(UserPrivacy userPrivacy) throws ServiceException {
        UserSettingsResourceApi settingsResourceApi = new UserSettingsResourceApi();
        try {
            //todo 认证操作需加入toekn
            settingsResourceApi.getApiClient().setAccessToken(oauthToken.get());
            UserSettings settings = settingsResourceApi.createUserSettingsUsingPOST(toSettings(userPrivacy));
        } catch (ApiException e) {
            logger.info("修改用户设置异常：" + e.getMessage());
            throw new ServiceException(e.getCode(), "getUserAccount:" + e.getMessage());
        }
        return userPrivacy;
    }

    UserSettings toSettings(UserPrivacy userPrivacy) {
        UserSettings settings = new UserSettings();
        settings.profileNo(userPrivacy.getProfileId())
                .alarmSettings(gson.toJson(userPrivacy.getAlarmSetting()))
                .funcSettings(gson.toJson(userPrivacy.getFunctionSetting()))
                .createdIp(userPrivacy.getCreateip());
        return settings;
    }

    @Override
    public UserPrivacy getUserPrivacy(String profileid) throws ServiceException {
        UserPrivacy privacy = null;
        UserSettingsResourceApi settingsResourceApi = new UserSettingsResourceApi();
        try {

            UserSettings settings = settingsResourceApi.getUserSettingsByProfileNoUsingGET(profileid);
            privacy = toPrivacy(settings);
        } catch (ApiException e) {
            logger.info("查询用户设置异常：" + e.getMessage());
            //throw new ServiceException(e.getCode(), "getUserPrivacy:" + e.getMessage());
            privacy = this.initPrivacy(profileid);
        }
        return privacy;
    }

    UserPrivacy initPrivacy(String profileId) {
        UserPrivacy userPrivacy = new UserPrivacy();
        UserPrivacyPrivacyAlarm privacyAlarm = new UserPrivacyPrivacyAlarm();
        privacyAlarm.setUserat("1");
        privacyAlarm.setSysteminfo("1");
        privacyAlarm.setFollow("1");
        privacyAlarm.setComment("1");
        privacyAlarm.setAgreement("1");

        UserPrivacyFunction privacyFunction = new UserPrivacyFunction();
        privacyFunction.setAcceptFollow("1");
        privacyFunction.setChat("1");

        userPrivacy.setFunctionSetting(privacyFunction);
        userPrivacy.setAlarmSetting(privacyAlarm);
        userPrivacy.setCreateip("127.0.0.1");
        userPrivacy.setCreatetime(new Date());
        userPrivacy.setProfileId(profileId);
        userPrivacy.setUpdateip("127.0.0.1");
        userPrivacy.setUpdatetime(new Date());
        return userPrivacy;
    }

    UserPrivacy toPrivacy(UserSettings settings) {
        UserPrivacy privacy = new UserPrivacy();
        privacy.setProfileId(settings.getProfileNo());
        privacy.setAlarmSetting(gson.fromJson(settings.getAlarmSettings(), UserPrivacyPrivacyAlarm.class));
        privacy.setFunctionSetting(gson.fromJson(settings.getFuncSettings(), UserPrivacyFunction.class));
        privacy.setCreateip(settings.getCreatedIp());
        return privacy;
    }

    @Override
    public boolean modifyUserPrivacy(String profileid, UpdateExpress updateExpress) throws ServiceException {
        boolean result = false;
        UserSettingsResourceApi settingsResourceApi = new UserSettingsResourceApi();
        try {
            //todo 认证操作需加入toekn
            settingsResourceApi.getApiClient().setAccessToken(oauthToken.get());
            UserSettings settings = new UserSettings();
            settings.profileNo(profileid)
                    .alarmSettings(updateExpress.getUpdateValueByField(UserPrivacyField.ALARMSETTING).toString())
                    .funcSettings(updateExpress.getUpdateValueByField(UserPrivacyField.FUNCTIONSETTING).toString())
                    .updatedIp(updateExpress.getUpdateValueByField(UserPrivacyField.UPDATEIP).toString());
            settings = settingsResourceApi.updateUserSettingsByProfileNoUsingPUT(settings);
            if (settings != null)
                result = true;
        } catch (ApiException e) {
            logger.info("修改用户设置异常：" + e.getMessage());
            throw new ServiceException(e.getCode(), "modifyUserPrivacy:" + e.getMessage());
        }
        return result;
    }

    @Override
    public void setOauthToken(String token) {
        oauthToken.set(token);
    }

    @Override
    public String getOauthToken(String token) {
        return oauthToken.get();
    }


    /**
     * 注册时发送手机验证码
     *
     * @param mobileNo
     * @return
     */
    @Override
    public boolean sendMobileNo(String mobileNo) throws ServiceException {
        Boolean result = false;
        UserMobileResourceApi mobileResourceApi = new UserMobileResourceApi();
        try {
            ApiResponse<Boolean> response = mobileResourceApi.sendRegisterCodeUsingPUTWithHttpInfo(mobileNo, "www");
            result = response.getData();
        } catch (ApiException e) {
            logger.info("发送手机验证码失败：" + e.getResponseBody());
            if (e.getMessageFromBody().equals("sendsms.times.outlimit"))
                throw UserCenterServiceException.PHONE_CODE_LIMIT;
            if (e.getMessageFromBody().equals("mobile.has.exists"))
                throw UserCenterServiceException.BIND_PHONE_ERROR_PHONE_HAS_BINDED;
        }
        return result;
    }

    /**
     * 登陆后发送手机验证码
     *
     * @param mobileNo
     * @return
     * @throws ServiceException
     */
    public boolean sendMobileCodeLogin(String mobileNo) throws ServiceException {
        Boolean result = false;
        UserMobileResourceApi mobileResourceApi = new UserMobileResourceApi();
        mobileResourceApi.getApiClient().setAccessToken(oauthToken.get());
        try {
            ApiResponse<Boolean> response = mobileResourceApi.sendBindCodeUsingPUTWithHttpInfo(mobileNo);
            result = response.getData();
        } catch (ApiException e) {
            logger.info("发送手机验证码失败：{},message:{}", mobileNo, e.getResponseBody());

            if (e.getMessageFromBody().equals("sendsms.times.outlimit"))
                throw UserCenterServiceException.PHONE_CODE_LIMIT;
        }
        return result;
    }

    /**
     * 登陆后验证手机验证码
     *
     * @param mobileNo
     * @param code
     * @return
     * @throws ServiceException
     */
    public boolean verifyCodeLogin(String mobileNo, String code) throws ServiceException {
        boolean result = false;
        UserMobileResourceApi mobileResourceApi = new UserMobileResourceApi();
        mobileResourceApi.getApiClient().setAccessToken(oauthToken.get());
        try {
            result = mobileResourceApi.verifyBindCodeUsingPOST(mobileNo, code);
        } catch (ApiException e) {
            logger.info("检查手机验证码失败：" + mobileNo + "：" + code);
        }
        return result;
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
        boolean result = false;
        UserMobileResourceApi mobileResourceApi = new UserMobileResourceApi();
        try {
            result = mobileResourceApi.verifyRegisterBindCodeUsingGET(mobileNo, code);
        } catch (ApiException e) {
            logger.info("检查手机验证码失败：" + mobileNo + "：" + code);
            // throw new ServiceException(e.getCode(), e.getMessage());
        }
        return result;
    }
}
