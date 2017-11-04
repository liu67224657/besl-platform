package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.serv.usercenter.UsercenterRedis;
import com.enjoyf.platform.service.usercenter.UserPrivacy;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityUser;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityUserLog;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.profile.ProfileDomainGenerator;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by ericliu on 14/10/22.
 */
public class UserCenterHandler {

    private static Logger logger = LoggerFactory.getLogger(UserCenterHandler.class);

    private DataBaseType dataBaseType;
    private String dataSourceName;


    private UserLoginAccessor userLoginAccessor;
    private ProfileAccessor profileAccessor;
    private TokenAccessor tokenAccessor;
    private UserAccountAccessor userAccountAccessor;
    private UidProfileIdxAccessor uidProfileIdxAccessor;
    private ProfileSumAccessor profileSumAccessor;
    private ProfileMobileAccessor profileMobileAccessor;


    private AppSumAccessor appSumAccessor;
    private ActivityUserAccessor activityUserAccessor;
    private ActivityUserLogAccessor activityUserLogAccessor;

    private VerifyAccessor verifyAccessor;
    private VerifyProfileAccessor verifyProfileAccessor;
    private UserPrivacyAccessor userPrivacyAccessor;

//    private RedisManager redisManager;

    public UserCenterHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the data source
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        userLoginAccessor = TableAccessorFactory.get().factoryAccessor(UserLoginAccessor.class, dataBaseType);
        profileAccessor = TableAccessorFactory.get().factoryAccessor(ProfileAccessor.class, dataBaseType);
        tokenAccessor = TableAccessorFactory.get().factoryAccessor(TokenAccessor.class, dataBaseType);
        userAccountAccessor = TableAccessorFactory.get().factoryAccessor(UserAccountAccessor.class, dataBaseType);
        uidProfileIdxAccessor = TableAccessorFactory.get().factoryAccessor(UidProfileIdxAccessor.class, dataBaseType);
        profileSumAccessor = TableAccessorFactory.get().factoryAccessor(ProfileSumAccessor.class, dataBaseType);
        profileMobileAccessor = TableAccessorFactory.get().factoryAccessor(ProfileMobileAccessor.class, dataBaseType);


        appSumAccessor = TableAccessorFactory.get().factoryAccessor(AppSumAccessor.class, dataBaseType);
        activityUserAccessor = TableAccessorFactory.get().factoryAccessor(ActivityUserAccessor.class, dataBaseType);
        activityUserLogAccessor = TableAccessorFactory.get().factoryAccessor(ActivityUserLogAccessor.class, dataBaseType);
        verifyAccessor = TableAccessorFactory.get().factoryAccessor(VerifyAccessor.class, dataBaseType);
        verifyProfileAccessor = TableAccessorFactory.get().factoryAccessor(VerifyProfileAccessor.class, dataBaseType);

        userPrivacyAccessor = TableAccessorFactory.get().factoryAccessor(UserPrivacyAccessor.class, dataBaseType);
//        this.redisManager = redisManager;
    }

    public UserLogin getUserLogin(String userLoginId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userLoginAccessor.get(new QueryExpress().add(QueryCriterions.eq(UserLoginField.LOGIN_ID, userLoginId)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserLogin insertUserLogin(UserLogin userLogin) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            userLogin = userLoginAccessor.insert(userLogin, conn);
            return userLogin;

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserAccount insertUserAccount(UserAccount userAccount) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            userAccount.setUno(userAccount.getUno());
            userAccount.setCreateTime(userAccount.getCreateTime());
            userAccount.setCreateIp(userAccount.getCreateIp());
            userAccount.setAccountFlag(new AccountFlag().has(0));

            return userAccountAccessor.insert(userAccount, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<Profile> queryProfiles(Set<String> profileIds) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return profileAccessor.query(new QueryExpress().add(QueryCriterions.in(ProfileField.PROFILEID, profileIds.toArray())), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<Profile> queryProfileByQueryExpress(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return profileAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public Profile getProfile(String profileId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return profileAccessor.get(new QueryExpress().add(QueryCriterions.eq(ProfileField.PROFILEID, profileId)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Profile getProfile(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return profileAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Profile getProfile(String uno, String appKey) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return profileAccessor.get(new QueryExpress().add(QueryCriterions.eq(ProfileField.UNO, uno)).add(QueryCriterions.eq(ProfileField.PROFILEKEY, appKey)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Profile getProfileByUid(long uid) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return profileAccessor.get(new QueryExpress().add(QueryCriterions.eq(ProfileField.UID, uid)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyProfile(UpdateExpress updateExpress, String profileId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return profileAccessor.update(updateExpress, new QueryExpress().add(QueryCriterions.eq(ProfileField.PROFILEID, profileId)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean modifyProfileByUno(UpdateExpress updateExpress, String uno) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return profileAccessor.update(updateExpress, new QueryExpress().add(QueryCriterions.eq(ProfileField.UNO, uno)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public Profile createProfile(Profile profile) throws DbException, UserCenterServiceException {
        Connection conn = null;
        long uid = -1l;
        try {
            conn = DbConnFactory.factory(dataSourceName);
//            if (profile.getUid() <= 0l) {
//                uid = getUid(redisManager);
//                profile.setUid(uid);
//            }
//            uidProfileIdxAccessor.update(new UpdateExpress()
//                            .set(UserProfileIdxField.UNO, profile.getUno())
//                            .set(UserProfileIdxField.PROFILEID, profile.getProfileId())
//                            .set(UserProfileIdxField.PROFILEKEY, profile.getProfileKey()),
//                    new QueryExpress().add(QueryCriterions.eq(UserProfileIdxField.UID, profile.getUid()))
//                    , conn);

            try {
                profile = profileAccessor.insert(profile, conn);
            } catch (DbException e) {
                logger.info("e: " + e + " nick: " + profile.getNick());
                String nick = StringUtil.subString(profile.getNick(), 5) + ProfileDomainGenerator.generateProfileDomain(new Random().nextInt(10000), 7);
                profile.setNick(nick);
                profile.setCheckNick(nick.toLowerCase());
                profile = profileAccessor.insert(profile, conn);
            }
            return profile;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public Token createToken(Token token) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
//            tokenAccessor.delete(new QueryExpress()
//                    .add(QueryCriterions.eq(TokenField.TOKEN_UNO, token.getUno())).add(QueryCriterions.eq(TokenField.PROFILEAPPKEY, token.getProfileKey())), conn);
            return tokenAccessor.insert(token, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyToken(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return tokenAccessor.update(updateExpress, queryExpress, conn) > 0;

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public Token getTokenByUnoAppKey(String uno, String appkey) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return tokenAccessor.get(new QueryExpress()
                    .add(QueryCriterions.eq(TokenField.TOKEN_UNO, uno))
                    .add(QueryCriterions.eq(TokenField.PROFILEAPPKEY, appkey)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Token getToken(String token) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return tokenAccessor.get(new QueryExpress()
                    .add(QueryCriterions.eq(TokenField.TOKEN, token)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserAccount getAccount(String uno) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userAccountAccessor.get(new QueryExpress()
                    .add(QueryCriterions.eq(UserAccountField.UNO, uno)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyUserLogin(UpdateExpress updateExpress, String loginId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userLoginAccessor.update(updateExpress, new QueryExpress()
                    .add(QueryCriterions.eq(UserLoginField.LOGIN_ID, loginId)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<UserLogin> queryUserLogin(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userLoginAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserLogin getUserLogin(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userLoginAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyUserAccount(UpdateExpress upateExpress, String uno) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userAccountAccessor.update(upateExpress, new QueryExpress().add(QueryCriterions.eq(UserAccountField.UNO, uno)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserAccount getAccount(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userAccountAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<Profile> queryProfile(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<Profile> profileList = profileAccessor.query(queryExpress, pagination, conn);

            PageRows<Profile> rows = new PageRows<Profile>();
            rows.setRows(profileList);
            rows.setPage(pagination);

            return rows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UidProfileIdx insertUidProfileIdx(UidProfileIdx uidProfileIdx) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return uidProfileIdxAccessor.insert(uidProfileIdx, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UidProfileIdx getUidProfileIdx(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return uidProfileIdxAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int bachInsertUidProfileIdx(List<UidProfileIdx> idxList) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            conn.setAutoCommit(false);

            int result = uidProfileIdxAccessor.bachInsert(idxList, conn);
            conn.commit();
            return result;
        } catch (SQLException e) {
            DataBaseUtil.rollbackConnection(conn);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public long getUid(RedisManager manager) throws UserCenterServiceException {
        String uidString = manager.rpop(UserCenterConstants.KEY_USERCENTER_UID);
        if (uidString == null) {
            throw UserCenterServiceException.UID_GET_NULL;
        }

        return Long.parseLong(uidString);
    }

    public void returnUid(RedisManager manager, long uid) {
        if (uid > 0) {
            manager.rpush(UserCenterConstants.KEY_USERCENTER_UID, String.valueOf(uid));
        }
    }


    public void initUidPool(long start, long end, UsercenterRedis usercenterRedis) throws DbException {
        usercenterRedis.remove(UserCenterConstants.KEY_USERCENTER_UID);

        List<String> list = new ArrayList<String>();
        List<UidProfileIdx> batchList = new ArrayList<UidProfileIdx>();
        for (long i = start; i <= end; i++) {

            UidProfileIdx uidProfileIdx = new UidProfileIdx();
            uidProfileIdx.setUid(i);
            uidProfileIdx.setCreateTime(new Date());

            //批量提交200次插入一次
            batchList.add(uidProfileIdx);
            list.add(String.valueOf(i));
            if (batchList.size() == 200) {

                bachInsertUidProfileIdx(batchList);
                usercenterRedis.lpush(UserCenterConstants.KEY_USERCENTER_UID, list);

                batchList.clear();
                list.clear();
            }
        }

    }


    public boolean updateProfileSum(UpdateExpress updateExpress, String profileId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(ProfileSumField.PROFILEID, profileId));
            if (profileSumAccessor.update(updateExpress, queryExpress, conn) <= 0) {
                ProfileSum profileSum = new ProfileSum();
                profileSum.setProfileId(profileId);
//                for (UpdateAttribute ua : updateExpress.getUpdateAttributes()) {
//                    if (ua.getField().equals(ProfileSumField.LIKEDSUM)) {
//                        profileSum.setLikedSum((Integer) ua.getValue());
//                    } else if (ua.getField().equals(ProfileSumField.LIKESUM)) {
//                        profileSum.setLikeSum((Integer) ua.getValue());
//                    } else if (ua.getField().equals(ProfileSumField.LIKEPICSUM)) {
//                        profileSum.setLikePicSum((Integer) ua.getValue());
//                    }
//                }

                profileSumAccessor.insert(profileSum, conn);
                return profileSumAccessor.update(updateExpress, queryExpress, conn) > 0;
            }
            return true;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ProfileSum createProfilieSum(ProfileSum profileSum) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileSumAccessor.insert(profileSum, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ProfileSum getProfileSum(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileSumAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ProfileSum> queryProfileSum(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileSumAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean deleteToken(String token) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return tokenAccessor.delete(new QueryExpress().add(QueryCriterions.eq(TokenField.TOKEN, token)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ProfileMobile insertProfileMobile(ProfileMobile profileMobile) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileMobileAccessor.insert(profileMobile, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ProfileMobile getProfileMobile(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileMobileAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean deleteProfileMobile(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileMobileAccessor.delete(queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyProfileSum(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileSumAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////////////////////
    public boolean increaseAppSum(UpdateExpress updateExpress, String appkey, String subKey) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            String id = UserCenterUtil.getAppSumId(appkey, subKey);
            //update or insert
            int i = appSumAccessor.update(updateExpress, new QueryExpress().add(QueryCriterions.eq(AppSumField.APP_SUM_ID, id)), conn);
            if (i <= 0) {
                AppSum appSum = new AppSum();
                appSum.setAppKey(appkey);
                appSum.setSubKey(subKey);
                appSum.setAppSumId(id);
                if (updateExpress.containField(AppSumField.ACTIVITY_USERSUM)) {
                    appSum.setActivitySum((Integer) updateExpress.getUpdateValueByField(AppSumField.ACTIVITY_USERSUM));
                }
                if (updateExpress.containField(AppSumField.ACTIVITY_USERSUM)) {
                    appSum.setActivityLogSum((Integer) updateExpress.getUpdateValueByField(AppSumField.ACTIVITY_LOGSUM));
                }

                appSumAccessor.insert(appSum, conn);
            }

            return true;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public ActivityUserLog insertActiviyUserLog(ActivityUserLog userLog) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return activityUserLogAccessor.insert(userLog, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public ActivityUser insertActiviyUser(ActivityUser user) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            activityUserAccessor.insert(user, conn);
            return user;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateActivityUser(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            int i = activityUserAccessor.update(updateExpress, queryExpress, conn);

            return i > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public PageRows<ActivityUser> queryActivityUser(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            PageRows<ActivityUser> rows = new PageRows<ActivityUser>();
            rows.setRows(activityUserAccessor.query(queryExpress, pagination, conn));
            rows.setPage(pagination);
            return rows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ActivityUser> queryActivityUser(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityUserAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public AppSum getAppSum(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appSumAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    //////////

    /////////////
    public VerifyProfile getVerifyProfileByProfileId(String profileId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(VerifyProfileField.PROFILEID, profileId));
            return verifyProfileAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<VerifyProfile> queryVerifyProfile(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return verifyProfileAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<VerifyProfile> queryVerifyProfileByPage(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            PageRows<VerifyProfile> pageRows = new PageRows<VerifyProfile>();
            pageRows.setPage(page);
            pageRows.setRows(verifyProfileAccessor.query(queryExpress, page, conn));
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public VerifyProfile insertVerifyProfile(VerifyProfile verifyProfile) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return verifyProfileAccessor.insert(verifyProfile, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyVerifyProfile(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return verifyProfileAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public Verify addVerify(Verify Verify) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return verifyAccessor.insert(Verify, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Verify getVerify(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return verifyAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyVerify(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return verifyAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean deleteVerify(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return verifyAccessor.delete(queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<Verify> queryVerify(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return verifyAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<Verify> queryVerifyByPage(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<Verify> verifyList = verifyAccessor.query(queryExpress, page, conn);

            PageRows<Verify> pageRows = new PageRows<Verify>();
            pageRows.setPage(page);
            pageRows.setRows(verifyList);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public PageRows<UserLogin> queryUserLoginPage(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            PageRows<UserLogin> pageRows = new PageRows<UserLogin>();
            pageRows.setPage(page);
            pageRows.setRows(userLoginAccessor.query(queryExpress, page, conn));
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserPrivacy addUserPrivacy(UserPrivacy userPrivacy) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userPrivacyAccessor.insert(userPrivacy, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserPrivacy getUserPrivacy(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userPrivacyAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyUserPrivacy(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userPrivacyAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean deleteUserPrivacy(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userPrivacyAccessor.delete(queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
}
