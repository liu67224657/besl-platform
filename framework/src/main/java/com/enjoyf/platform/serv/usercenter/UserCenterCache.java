package com.enjoyf.platform.serv.usercenter;

import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户中心缓存类
 * <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/10/23
 * Description:
 */
public class UserCenterCache {

    private static final long TIME_OUT_SEC_ONE_HOUR = 60l * 60l * 6l;
    private static final long PASSORDTIME_TIME_OUT_SEC = 60l * 60l * 2l;
    private static final String KEY_USERLOGIN = "_userlogin";
    private static final String KEY_PROFILE = "_profile";
    private static final String KEY_PROFILE_BY_UID = "_profileuid";
    private static final String KEY_TOKEN_BYKEY = "_tokenkey";
    private static final String KEY_TOKEN = "_token";
    private static final String KEY_ACCOUNT = "_useraccount";
    private static final String PREFIX_MOBILE_CODE = "_mc_";
    private static final String PREFIX_MOBILE_CODE_TIMES = "_mc_times_";
    private static final String PREFIX_PWDTIME = "_pwdtime_";
    private static final String KEY_PROFILESUM = "_ps_";
    private static final String KEY_PROFILEMOBILE = "_pm_";
    private static final String KEY_GENERATOR_PROFILE_COUNTER = "_gpcounter_";
    private static final String KEY_AUTH_CALLS_COUNTER = "_authcallscounter_";

    private MemCachedManager manager;

    private String serverPrefix = "uc";

    public UserCenterCache(MemCachedConfig config) {
        manager = new MemCachedManager(config);
    }

    public MemCachedManager getManager() {
        return manager;
    }

    //////////////////////////////////////////////////////////
    public void putUserLogin(UserLogin userLogin) {
        manager.put(serverPrefix + KEY_USERLOGIN + userLogin.getLoginId(), userLogin, TIME_OUT_SEC_ONE_HOUR);
    }


    public UserLogin getUserLogin(String key) {
        Object obj = manager.get(serverPrefix + KEY_USERLOGIN + key);

        if (obj != null) {
            return (UserLogin) obj;
        }

        return null;
    }

    public boolean deleteUserLogin(String key) {
        return manager.remove(serverPrefix + KEY_USERLOGIN + key);
    }


    //////////////////////////////////////////////////////////
    public void putProfile(Profile profile) {
        manager.put(serverPrefix + KEY_PROFILE + profile.getProfileId(), profile, TIME_OUT_SEC_ONE_HOUR);
        manager.put(serverPrefix + KEY_PROFILE_BY_UID + profile.getUid(), profile, TIME_OUT_SEC_ONE_HOUR);

    }


    public Profile getProfile(String profileId) {
        Object obj = manager.get(serverPrefix + KEY_PROFILE + profileId);

        if (obj != null) {
            return (Profile) obj;
        }

        return null;
    }

    public Profile getProfileByUid(long uid) {
        Object obj = manager.get(serverPrefix + KEY_PROFILE_BY_UID + uid);

        if (obj != null) {
            return (Profile) obj;
        }

        return null;
    }

    public boolean deleteProfile(Profile profile) {
        manager.remove(serverPrefix + KEY_PROFILE + profile.getProfileId());
        manager.remove(serverPrefix + KEY_PROFILE_BY_UID + profile.getUid());
        return true;
    }

//    public void putPidByUid(long uid, String pid) {
//        manager.put(serverPrefix + KEY_UID2PID + uid, pid, TIME_OUT_SEC_ONE_HOUR);
//    }
//
//
//    public String getPidByUid(long uid) {
//        Object obj = manager.get(serverPrefix + KEY_UID2PID + uid);
//
//        if (obj != null) {
//            return (String) obj;
//        }
//
//        return null;
//    }
//
//    public boolean deletePidByUID(long uid) {
//        return manager.remove(serverPrefix + KEY_UID2PID + uid);
//    }

    //////////////////////////////////////////////////////////
    public void putTokenByProfile(String key, Token token) {
        manager.put(serverPrefix + KEY_TOKEN_BYKEY + key, token, TIME_OUT_SEC_ONE_HOUR);
        manager.put(serverPrefix + KEY_TOKEN + token.getToken(), token, TIME_OUT_SEC_ONE_HOUR);
    }


    public Token getTokenByProfileKey(String tokenString) {
        Object obj = manager.get(serverPrefix + KEY_TOKEN_BYKEY + tokenString);

        if (obj != null) {
            return (Token) obj;
        }

        return null;
    }

    public Token getToken(String token) {
        Object obj = manager.get(serverPrefix + KEY_TOKEN + token);

        if (obj != null) {
            return (Token) obj;
        }

        return null;
    }

    public boolean deleteTokenKey(String tokenString) {
        Token token = getToken(tokenString);
        if (token != null) {
            manager.remove(serverPrefix + KEY_TOKEN_BYKEY + UserCenterUtil.getTokenCacheKey(token.getUno(), token.getProfileKey()));
        }

        return manager.remove(serverPrefix + KEY_TOKEN + tokenString);
    }


    //////////////////////////////////////////////////////////
    public void putUserAccount(UserAccount userAccount) {
        manager.put(serverPrefix + KEY_ACCOUNT + userAccount.getUno(), userAccount, TIME_OUT_SEC_ONE_HOUR);
    }


    public UserAccount getUserAccount(String uno) {
        Object obj = manager.get(serverPrefix + KEY_ACCOUNT + uno);

        if (obj != null) {
            return (UserAccount) obj;
        }

        return null;
    }

    public boolean deleteUserAccount(String uno) {
        return manager.remove(serverPrefix + KEY_ACCOUNT + uno);
    }

    //////////////////////////////////////////////////////////
    public String getMobileCode(String uno) {
        Object code = manager.get(serverPrefix + PREFIX_MOBILE_CODE + uno);

        if (code == null) {
            return null;
        }
        return (String) code;
    }

    public void putMobileCode(String uno, String code) {
        manager.put(serverPrefix + PREFIX_MOBILE_CODE + uno, code, TIME_OUT_SEC_ONE_HOUR);

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        manager.addOrIncr(serverPrefix + PREFIX_MOBILE_CODE_TIMES + uno + "_" + df.format(date), 1l, (int) TIME_OUT_SEC_ONE_HOUR);
    }

    public int getMobileCodeTimes(String uno) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Object obj = manager.get(serverPrefix + PREFIX_MOBILE_CODE_TIMES + uno + "_" + df.format(date));
        if (obj == null) {
            return 0;
        }
        return (Integer) obj;
    }

    public boolean removeMobileCode(String uno) {
        return manager.remove(serverPrefix + PREFIX_MOBILE_CODE + uno);
    }

    //////////////////////////////////////////////////////////
    public Long getPwdTime(String uno) {
        Object code = manager.get(serverPrefix + PREFIX_PWDTIME + uno);

        if (code == null) {
            return -1l;
        }
        return (Long) code;
    }

    public void putPwdTime(String uno, Long time) {
        manager.put(serverPrefix + PREFIX_PWDTIME + uno, time, PASSORDTIME_TIME_OUT_SEC);
    }

    public boolean removePwdTime(String uno) {
        return manager.remove(serverPrefix + PREFIX_PWDTIME + uno);
    }


    /////////////////////////////////////////////////////////////
    public void putProfileSum(ProfileSum sum) {
        manager.put(serverPrefix + KEY_PROFILESUM + sum.getProfileId(), sum, TIME_OUT_SEC_ONE_HOUR);
    }


    public ProfileSum getProfileSum(String key) {
        Object obj = manager.get(serverPrefix + KEY_PROFILESUM + key);

        if (obj != null) {
            return (ProfileSum) obj;
        }

        return null;
    }

    public boolean deleteProfileSum(String key) {
        return manager.remove(serverPrefix + KEY_PROFILESUM + key);
    }


    //////////////////////////////////////////////////////////
    public void putProfileMobile(ProfileMobile mobile) {
        manager.put(serverPrefix + KEY_PROFILEMOBILE + mobile.getProfileMobileId(), mobile, TIME_OUT_SEC_ONE_HOUR);
    }


    public ProfileMobile getProfileMobile(String profileMobileId) {
        Object obj = manager.get(serverPrefix + KEY_PROFILEMOBILE + profileMobileId);

        if (obj != null) {
            return (ProfileMobile) obj;
        }

        return null;
    }

    public boolean deleteProfileMobile(String profileMobileId) {
        return manager.remove(serverPrefix + KEY_PROFILEMOBILE + profileMobileId);
    }

    //usercenterservice_gpcounter_2015042110
    public void addProfileCounter(Date date) {
        long i=manager.addOrIncr(serverPrefix + KEY_GENERATOR_PROFILE_COUNTER + DateUtil.formatDateToString(date, "yyyyMMddHH"), 1l, 60 * 60 * 2);
        return;
    }

    public Long getProfileCounter(Date date) {
        Object obj = manager.get(serverPrefix + KEY_GENERATOR_PROFILE_COUNTER + DateUtil.formatDateToString(date, "yyyyMMddHH"));

        if (obj != null) {
            return Long.valueOf(obj.toString());
        }

        return null;
    }

    public void addAuthCallsCounter(Date date) {
        long i = manager.addOrIncr(serverPrefix + KEY_AUTH_CALLS_COUNTER + DateUtil.formatDateToString(date, "yyyyMMddHH"), 1l, 60 * 60 * 2);
        return;
    }


}
