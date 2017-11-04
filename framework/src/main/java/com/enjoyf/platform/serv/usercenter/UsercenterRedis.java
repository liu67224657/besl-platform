/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.usercenter;


import com.enjoyf.platform.service.usercenter.UserPrivacy;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class UsercenterRedis {
    //
    private static final Logger logger = LoggerFactory.getLogger(UsercenterRedis.class);

    private static final String KEY_HASH_PROFILESUM = UserCenterConstants.SERVICE_SECTION + "_hpsum_";
    private static final String KEY_PROFILE = UserCenterConstants.SERVICE_SECTION + "_profile_";
    private static final String KEY_VERIFY = UserCenterConstants.SERVICE_SECTION + "_verify_";
    private static final String KEY_LIST_VERIFYPROFILE = UserCenterConstants.SERVICE_SECTION + "_vplist_";

    private static final String KEY_LIST_VERIFY_PROFILE_TAGS = UserCenterConstants.SERVICE_SECTION + "_verifyprofiletags_";

    private static final String KEY_LIST_VERIFY_PROFILES = UserCenterConstants.SERVICE_SECTION + "_verifyprofiles_";
    private static final String KEY_USER_PRIVACY = UserCenterConstants.SERVICE_SECTION + "_user_privacy_";
    private RedisManager manager;

    public UsercenterRedis(FiveProps p) {
        manager = new RedisManager(p);
    }

    public Long remove(String key) {
        return manager.remove(key);
    }

    public long length(String key) {
        return manager.length(key);
    }

    public void lpush(String key, List<String> list) {
        manager.lpush(key, list);
    }

    /////
    public VerifyProfile getProfileById(String profileId) {
        String profileStr = manager.get(KEY_PROFILE + profileId);
        if (StringUtil.isEmpty(profileStr)) {
            return null;
        }
        VerifyProfile returnObj = null;
        try {
            returnObj = VerifyProfile.getByJson(profileStr);
        } catch (Exception e) {
            delProfile(profileId);
        }
        return returnObj;
    }

    public boolean setProfile(VerifyProfile wanbaProfile) {
        manager.set(KEY_PROFILE + wanbaProfile.getProfileId(), wanbaProfile.toJson());
        return true;
    }

    public boolean delProfile(String profileId) {
        return remove(KEY_PROFILE + profileId) > 0;
    }


    //////////////
    public Verify getWanbaVerify(Long verifyId) {
        String verifyStr = manager.get(KEY_VERIFY + verifyId);
        if (StringUtil.isEmpty(verifyStr)) {
            return null;
        }
        Verify returnObj = null;
        try {
            returnObj = Verify.getByJson(verifyStr);
        } catch (Exception e) {
            delVerify(verifyId);
        }
        return returnObj;
    }

    public boolean setVerify(Verify wanbaVerify) {
        manager.set(KEY_VERIFY + wanbaVerify.getVerifyId(), wanbaVerify.toJson());
        return true;
    }


    public boolean delVerify(Long verifyId) {
        return remove(KEY_VERIFY + verifyId) > 0;
    }


    ///////////////////////////////
    public Set<String> getProfileIdByTagId(long tagId, Pagination page) {
        return manager.zrange(KEY_LIST_VERIFYPROFILE + tagId, page.getStartRowIdx(), page.getEndRowIdx(), RedisManager.RANGE_ORDERBY_DESC);
    }

    public Set<String> queryAllProfileIdByTagId(long tagId) {
        return manager.zrange(KEY_LIST_VERIFYPROFILE + tagId, 0, -1, RedisManager.RANGE_ORDERBY_DESC);
    }

    public void addProfileIdByTagId(long tagId, String profileId, double socre) {
        manager.zadd(KEY_LIST_VERIFYPROFILE + tagId, socre, profileId, -1);

        //addVerifyprofiletags(tagId, profileId, socre);
    }

    public void addVerifyprofiletags(long tagId, String profileId, double socre) {
        if (tagId < 0) {
            return;
        }
        manager.zadd(KEY_LIST_VERIFY_PROFILE_TAGS + profileId, socre, String.valueOf(tagId), -1);

        //方便定时任务通过标签找到人
        manager.sadd(KEY_LIST_VERIFY_PROFILES, profileId);
    }


    public long removePrifileIdByTagId(long tagId, String profileId) {
        long i = manager.zrem(KEY_LIST_VERIFYPROFILE + tagId, profileId);

        manager.zrem(KEY_LIST_VERIFY_PROFILE_TAGS + profileId, String.valueOf(tagId));

        return i;
    }

    public boolean existsPrifileIdByTagId(long tagId, String profileId) {
        return manager.zscore(KEY_LIST_VERIFYPROFILE + tagId, profileId) != null;
    }

    public Double profileidByTagId(long tagId, String profileId) {
        return manager.zscore(KEY_LIST_VERIFYPROFILE + tagId, profileId);
    }

    public long profileZrank(long tagId, String profileId) {
        return manager.zrank(KEY_LIST_VERIFYPROFILE + tagId, profileId, RedisManager.RANGE_ORDERBY_DESC);
    }


    public long getProfileIdByTagIdSize(long tagId) {
        return manager.zcard(KEY_LIST_VERIFYPROFILE + tagId);
    }


    public Set<String> getVerifyProfileTagsByProfileId(String profileId) {
        Set<String> returnSet = new HashSet<String>();
        Set<String> strings = manager.zrange(KEY_LIST_VERIFY_PROFILE_TAGS + profileId, 0, -1, RedisManager.RANGE_ORDERBY_DESC);
        if (CollectionUtil.isEmpty(strings)) {
            return returnSet;
        }

        for (String str : strings) {
            returnSet.add(str);
        }
        return returnSet;
    }

    public UserPrivacy getWikiUserPrivacy(String profileId) {
        String userPrivacyStr = manager.get(KEY_USER_PRIVACY + profileId);
        if (StringUtil.isEmpty(userPrivacyStr)) {
            return null;
        }
        UserPrivacy returnObj = null;
        try {
            returnObj = UserPrivacy.getByJson(userPrivacyStr);
        } catch (Exception e) {
            delWikiUserPrivacy(profileId);
        }
        return returnObj;
    }

    public boolean setWikiUserPrivacy(UserPrivacy userPrivacy) {
        manager.set(KEY_USER_PRIVACY + userPrivacy.getProfileId(), userPrivacy.toJson());
        return true;
    }

    public boolean delWikiUserPrivacy(String profileId) {
        return remove(KEY_USER_PRIVACY + profileId) > 0;
    }

    //////////////////////////////
    public void increaseProfileSum(String profileId, ProfileSumField sumFiled, int value) {
        manager.hinccrby(KEY_HASH_PROFILESUM + profileId, sumFiled.getColumn(), value);
    }

    public ProfileSum getProfileSum(String profileId) {
        Map<String, String> result = manager.hgetAll(KEY_HASH_PROFILESUM + profileId);

        ProfileSum sum = new ProfileSum();
        sum.setProfileId(profileId);

        if(result.containsKey(ProfileSumField.FOLLOWSUM.getColumn())){
            try {
                int followSum=Integer.parseInt(result.get(ProfileSumField.FOLLOWSUM.getColumn()));
                if(followSum<0){
                    manager.hinccrby(KEY_HASH_PROFILESUM + profileId, ProfileSumField.FOLLOWSUM.getColumn(), Math.abs(followSum));
                }
                sum.setFollowSum(followSum);
            } catch (NumberFormatException e) {
                manager.hset(KEY_HASH_PROFILESUM + profileId,ProfileSumField.FOLLOWSUM.getColumn(),"0");
            }
        }

        if(result.containsKey(ProfileSumField.FANSSUM.getColumn())){
            try {
                int fansSum=Integer.parseInt(result.get(ProfileSumField.FANSSUM.getColumn()));
                if(fansSum<0){
                    manager.hinccrby(KEY_HASH_PROFILESUM + profileId, ProfileSumField.FANSSUM.getColumn(), Math.abs(fansSum));
                }
                sum.setFansSum(Integer.parseInt(result.get(ProfileSumField.FANSSUM.getColumn())));
            } catch (NumberFormatException e) {
                manager.hset(KEY_HASH_PROFILESUM + profileId,ProfileSumField.FANSSUM.getColumn(),"0");
            }
        }
        return sum;
    }

}


