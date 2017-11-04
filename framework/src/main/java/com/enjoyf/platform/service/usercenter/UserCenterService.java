package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityProfile;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.redis.ScoreRange;
import com.enjoyf.platform.util.redis.ScoreRangeRows;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.*;

/**
 * Created by ericliu on 14/10/22.
 */
public interface UserCenterService extends EventReceiver {

    /**
     * 同getUserLoginByLoginId
     * @param loginKey
     * @param loginDomain
     * @return
     * @throws ServiceException
     */
    public UserLogin getUserLoginByLoginKey(String loginKey, LoginDomain loginDomain) throws ServiceException;

    /**
     * 根据loginid获取用户登录信息
     * @param loginid  LoginUtil.getLoingId(loginkey, logindomain)
     * @return
     * @throws ServiceException
     */
    public UserLogin getUserLoginByLoginId(String loginid) throws ServiceException;

    /**
     * uno和多种登录方式
     * @param uno
     * @param loginDomain
     * @return
     * @throws ServiceException
     */
    public List<UserLogin> queryUserLoginUno(String uno, Set<LoginDomain> loginDomain) throws ServiceException;

    /**
     * 修改login的方法
     * @param updateExpress
     * @param loginId
     * @return
     * @throws ServiceException
     */
    public boolean modifyUserLogin(UpdateExpress updateExpress, String loginId) throws ServiceException;

    /**
     * 第三方登录，自注册登录等需要用到。
     * @param loginKey
     * @param loginDomain
     * @param tokenInfo
     * @param icon
     * @param nick
     * @param appKey
     * @param createIp
     * @param createDate
     * @param paramMap
     * @return
     * @throws ServiceException
     */
    public AuthProfile auth(String loginKey, LoginDomain loginDomain, TokenInfo tokenInfo, String icon, String nick, String appKey, String createIp, Date createDate, HashMap<String, String> paramMap) throws ServiceException;

    /**
     * 通过uid获取 profile token useraccount
     * @param uid
     * @param paramMap
     * @return
     * @throws ServiceException
     */
    public AuthProfile getAuthProfileByUid(long uid, HashMap<String, String> paramMap) throws ServiceException;

    /**
     * 同上个方法是一样的
     * @param uno
     * @param profileKey
     * @param map
     * @return
     * @throws ServiceException
     */
    @Deprecated
    public AuthProfile getAuthProfileByUno(String uno, String profileKey, HashMap<String, String> map) throws ServiceException;

    /**
     * 账号绑定。用于用户设置里面
     * @param loginKey
     * @param passowrd
     * @param loginDomain
     * @param appKey
     * @param uno
     * @param createIp
     * @param createDate
     * @param icon
     * @param nick
     * @param paramMap
     * @return
     * @throws ServiceException
     */
    public AuthProfile bind(String loginKey, String passowrd, LoginDomain loginDomain, String appKey, String uno, String createIp, Date createDate, String icon, String nick, HashMap<String, String> paramMap) throws ServiceException;

    /**
     * 解绑
     * @param loginDomain
     * @param appKey
     * @param uno
     * @param createIp
     * @param createDate
     * @return
     * @throws ServiceException
     */
    public boolean unbind(LoginDomain loginDomain, String appKey, String uno, String createIp, Date createDate) throws ServiceException;

    /**
     * flag为logindomain和完善过昵称
     *
     * @param loginKey
     * @param password
     * @param loginDomain
     * @param appKey
     * @param nick
     * @param createIp
     * @param createDate
     * @param paramMap
     * @return
     * @throws ServiceException
     */
    public AuthProfile register(String loginKey, String password, LoginDomain loginDomain, String appKey, String nick, String createIp, Date createDate, HashMap<String, String> paramMap) throws ServiceException;

    /**
     * 登录
     * @param loginKey
     * @param password
     * @param loginDomain
     * @param appKey
     * @param createIp
     * @param createDate
     * @param paramMap
     * @return
     * @throws ServiceException
     */
    public AuthProfile login(String loginKey, String password, LoginDomain loginDomain, String appKey, String createIp, Date createDate, HashMap<String, String> paramMap) throws ServiceException;

    /**
     * 删除token
     * @param token
     * @return
     * @throws ServiceException
     */
    public boolean deleteToken(String token) throws ServiceException;

    /**
     * 获取token
     * @param token
     * @return
     * @throws ServiceException
     */
    public Token getToken(String token) throws ServiceException;

    /**
     * 根据pid获取用户
     * @param profileId
     * @return
     * @throws ServiceException
     */
    public Profile getProfileByProfileId(String profileId) throws ServiceException;

    /**
     * 同上
     * @param uno
     * @param profileKey
     * @return
     * @throws ServiceException
     */
    @Deprecated
    public Profile getProfileByUno(String uno, String profileKey) throws ServiceException;

    /**
     * 获取uid
     * @param uid
     * @return
     * @throws ServiceException
     */
    public Profile getProfileByUid(long uid) throws ServiceException;

    /**
     * 批量查询  by uids
     * @param uids
     * @return
     * @throws ServiceException
     */
    public Map<Long, Profile> queryProfilesByUids(Set<Long> uids) throws ServiceException;

    public List<Profile>     listProfilesByIds(Set<Long> ids)  throws ServiceException;

    /**
     * 批量查询 by profielIds
     * @param profielIds
     * @return
     * @throws ServiceException
     */
    public Map<String, Profile> queryProfiles(Set<String> profielIds) throws ServiceException;

    /**
     * 通过昵称获取
     * @param nick
     * @return
     * @throws ServiceException
     */
    public Profile getProfileByNick(String nick) throws ServiceException;

    /**
     * 修改用户信息
     * @param updateExpress
     * @param profileId
     * @return
     * @throws ServiceException
     */
    public boolean modifyProfile(UpdateExpress updateExpress, String profileId) throws ServiceException;

    /**
     * 只修改昵称，todo 微服务改造新增
     * @param nick
     * @return
     * @throws ServiceException
     */
    public Profile updateNick(String nick) throws ServiceException;

    /**
     * 用于虚拟用户的创建不能对外使用
     * @param Profile
     * @return
     * @throws ServiceException
     */
    public Profile createProfile(Profile Profile) throws ServiceException;

    /**
     * 通过域名获取profile 暂时不用
     * @param domain
     * @return
     * @throws ServiceException
     */
    public Profile getProfileByDomain(String domain) throws ServiceException;

    /**
     * 后台工具通过DB查询获取用户信息
     * @param queryExpress
     * @return
     * @throws ServiceException
     */
    public List<Profile> queryProfile(QueryExpress queryExpress) throws ServiceException;

    public List<Profile> queryProfilesByNickLike(String nick) throws ServiceException;

    /**
     * 分页查询
     * @param queryExpress
     * @param pagination
     * @return
     * @throws ServiceException
     */
    @Deprecated
    public PageRows<Profile> queryProfileByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    /**
     * 微服务改写
     * @param nick
     * @param inOrNot
     * @param profileNos
     * @param startTime
     * @param endTime
     * @param pagination
     * @return
     * @throws ServiceException
     */
    public PageRows<Profile> searchProfiles(String nick, String inOrNot, String profileNos, String startTime, String endTime,Pagination pagination) throws ServiceException;

    /**
     * 手机验证码
     * @param uno
     * @param code
     * @return
     * @throws ServiceException
     */
    public String saveMobileCode(String uno, String code) throws ServiceException;

    /**
     * 获取手机验证码
     * @param uno
     * @return
     * @throws ServiceException
     */
    public String getMobileCode(String uno) throws ServiceException;

    /**
     * 删除手机验证码
     * @param uno
     * @return
     * @throws ServiceException
     */
    public boolean removeMobileCode(String uno) throws ServiceException;

    /**
     * 修改密码等操作发送的手机码
     * @param loginId
     * @param time
     * @return
     * @throws ServiceException
     */
    public boolean savePasswordCode(String loginId, Long time) throws ServiceException;

    public Long getPassordCode(String loginId) throws ServiceException;

    public boolean removePasswordCode(String loginId) throws ServiceException;

    /**
     * uno get useraccount
     * @param uno
     * @return
     * @throws ServiceException
     */
    public UserAccount getUserAccount(String uno) throws ServiceException;

    /**
     *
     * @param upateExpress
     * @param uno
     * @return
     * @throws ServiceException
     */
    public boolean modifyUserAccount(UpdateExpress upateExpress, String uno) throws ServiceException;

    @Deprecated
    public boolean initUidPool(long startNum, long endNum) throws ServiceException;

    @Deprecated
    public long getUidPoolLength() throws ServiceException;


    /**
     * 查询用户计数
     *
     * @param profileIds
     * @return
     * @throws ServiceException
     */
    public Map<String, ProfileSum> queryProfileSumByProfileids(Set<String> profileIds) throws ServiceException;

    public boolean increaseProfileSum(String profileId, ProfileSumField sumFiled, int value) throws ServiceException;

    public ProfileSum getProfileSum(String profileId) throws ServiceException;

    /**
     * 手机绑定接口
     * @param mobile
     * @param profileKey
     * @return
     * @throws ServiceException
     */
    public boolean checkMobileIsBinded(String mobile, String profileKey) throws ServiceException;

    public boolean bindMobile(String mobile, String profileId, String ip) throws ServiceException;

    public boolean unbindMobile(String profileMobileId) throws ServiceException;

    public boolean unbindMobile(String profileId, String ip) throws ServiceException;

    //////////////////////////////////////////
    @Deprecated
    public PageRows<ActivityProfile> queryActivityProfile(String appkey, String subkey, Pagination pagination) throws ServiceException;
    @Deprecated
    public int getActvitiyUserSum(String appkey, String subkey) throws ServiceException;

    ////////////////////认证达人//////////////////////
    public Map<String, VerifyProfile> queryProfileByIds(Set<String> profileIds) throws ServiceException;

    public VerifyProfile getVerifyProfileById(String profileId) throws ServiceException;

    public boolean modifyVerifyProfile(String profileId, UpdateExpress updateExpress) throws ServiceException;

    public PageRows<VerifyProfile> queryVerifyProfile(QueryExpress queryExpress, Pagination page) throws ServiceException;

    PageRows<VerifyProfile> queryPlayers(String nick,String appKey,Long levelId, Pagination page) throws ServiceException;

    public PageRows<VerifyProfile> queryVerifyProfileByTag(long tagId, Pagination pagination) throws ServiceException;

    public boolean verifyProfile(VerifyProfile profile, long tagId) throws ServiceException;

    public boolean deleteVerifyProfile(long tagId, String profileId) throws ServiceException;

    public ScoreRangeRows<VerifyProfile> queryFollowProfile(Set<String> idSet, ScoreRange range) throws ServiceException;

    public boolean sortVerifyProfileByTagId(Long tagId, int sort, String profileId) throws ServiceException;

    public Set<String> getVerifyProfileTagsByProfileId(String profileId) throws ServiceException;

    public boolean addVerifyProfileTagsByProfileId(Long tagId, String profileId) throws ServiceException;

    public boolean removeVerifyProfileTagsByProfileId(Long tagId, String profileId) throws ServiceException;

    ////////////////////达人类型//////////////////////
    public Verify addVerify(Verify verify) throws ServiceException;

    public List<Verify> queryVerify(QueryExpress queryExpress) throws ServiceException;

    public PageRows<Verify> queryVerifyByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public Verify getVerify(Long verifyId) throws ServiceException;

    public boolean modifyVerify(Long verifyId, UpdateExpress updateExpress) throws ServiceException;

    //////////////////////隐私设置/////////////////////////
    public UserPrivacy addUserPrivacy(UserPrivacy userPrivacy) throws ServiceException;

    public UserPrivacy getUserPrivacy(String profileid) throws ServiceException;

    public boolean modifyUserPrivacy(String profileid, UpdateExpress updateExpress) throws ServiceException;



    //todo ****  微服务改造新增 *******／

    /**
     * jwt 设置 token，注意线程安全
     * @param token
     */
    void setOauthToken(String token);

    /**
     * get token
     * @param token
     * @return
     */
    String getOauthToken(String token);

    /**
     * 获得当前登陆者的账户信息
     * @return
     */
    AuthProfile getCurrentAccount() throws ServiceException;

    /**
     * 修改密码，登陆操作
     * @param newPassword
     * @throws ServiceException
     */
    void changePassword(String newPassword) throws ServiceException;

    /**
     * 忘记密码时修改密码
     * @param mobileNo
     * @param validCode
     * @param newPassword
     * @throws ServiceException
     */
    void forgetPassword(String mobileNo,String validCode,String newPassword) throws ServiceException;


    /**
     * 修改手机号
     * @param newMobileNo
     * @throws ServiceException
     */
    com.enjoyf.platform.userservice.client.model.UserLogin changeMobileNo(String newMobileNo) throws ServiceException;

    /**
     * 注册时发送手机验证码
     * @param mobileNo
     * @return
     */
    boolean sendMobileNo(String mobileNo) throws ServiceException;

    /**
     * 登陆后发送手机验证码
     * @param mobileNo
     * @return
     * @throws ServiceException
     */
     boolean sendMobileCodeLogin(String mobileNo) throws ServiceException;

    /**
     * 登陆后验证手机验证码
     * @param mobileNo
     * @param code
     * @return
     * @throws ServiceException
     */
     boolean verifyCodeLogin(String mobileNo,String code) throws ServiceException;


    /**
     * 注册时检查手机验证码是否正确
     * @param mobileNo
     * @param code
     * @return
     * @throws ServiceException
     */
    boolean checkMobileVerifyCode(String mobileNo, String code) throws ServiceException;

}
