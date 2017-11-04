package com.enjoyf.platform.serv.misc;

import com.enjoyf.platform.service.ask.WanbaProfileClassify;
import com.enjoyf.platform.service.misc.MiscConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.rpc.RPC;
import com.enjoyf.platform.service.usercenter.UserLogin;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.redis.RedisManager;
import com.google.gson.Gson;

import java.util.*;

/**
 * Created by zhimingli on 2016/11/11 0011.
 */
public class MiscRedis {
    private static final String KEY_OAUTH2ME_ = MiscConstants.SERVICE_SECTION + "_oauth2me_";

    private static final String KEY_MISCVALUE_ = MiscConstants.SERVICE_SECTION + "_miscvalue_";

    private RedisManager manager;

    public MiscRedis(FiveProps p) {
        manager = new RedisManager(p);
    }

    public UserLogin getUserLogin(String openid) {
        String objStr = manager.get(KEY_OAUTH2ME_ + openid);

        if (StringUtil.isEmpty(objStr)) {
            return null;
        }

        return new Gson().fromJson(objStr, UserLogin.class);
    }


    public void setUserLogin(String openid, UserLogin userLogin) {
        manager.set(KEY_OAUTH2ME_ + openid, new Gson().toJson(userLogin));
    }

    public boolean deleteUserLogin(String openid) {
        return manager.remove(KEY_OAUTH2ME_ + openid) > 0;
    }


    public List<String> getRedisListKey(String key) throws ServiceException {
        List<String> strLists = manager.lrange(key, 0, -1);
        if (CollectionUtil.isEmpty(strLists)) {
            return null;
        }
        List<String> returnSets = new ArrayList<String>();
        for (String str : strLists) {
            returnSets.add(str);
        }
        return returnSets;
    }

    public boolean updateRedisListKey(String key, Collection<String> keys) throws ServiceException {
        manager.remove(key);
        for (String pid : keys) {
            manager.rpush(key, pid);
        }
        return true;
    }

    public void saveRedisMiscValue(String key, String value, int timoutSec) {
        manager.setSec(KEY_MISCVALUE_ + key, value, timoutSec);
    }

    public String getRedisMiscValue(String key) {
        String objStr = manager.get(KEY_MISCVALUE_ + key);
        return objStr;
    }

    public boolean removeRedisMiscValue(String key) {
        return manager.remove(KEY_MISCVALUE_ + key) > 0;
    }

    public Map<String, String> hgetAll(String key) {
        return manager.hgetAll(key);
    }

    public Long hset(String key, String field, String value) {
        return manager.hset(key, field, value);
    }

    public String hget(String key, String field) {
        return manager.hget(key, field);
    }


    public Long hdel(String key, String field) {
        return manager.delHash(key, field);
    }
}
