package com.enjoyf.platform.serv.vote;

import com.enjoyf.platform.service.vote.VoteConstants;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.util.MD5Util;

/**
 * Created by xupeng on 16/7/12.
 */
public class VoteRedis {
    private static final String KEY_PREFIX = VoteConstants.SERVICE_SECTION;

    private static final String VOTE_KEY = KEY_PREFIX + "_vote_title_";
    private static final String VOTE_HISTORY = KEY_PREFIX + "_vote_history_";

    private RedisManager manager;

    public VoteRedis(FiveProps p) {
        manager = new RedisManager(p);
    }

    public void putVoteTitle(String key) {
        int value = getVoteTitle(key);
        manager.set(VOTE_KEY + key, String.valueOf(value + 1));
    }

    public Long incrVote(String key) {
        return manager.incr(VOTE_KEY + key, 1, -1);
    }

    public void putVoteHistory(String profileId, String key) {
        manager.set(VOTE_HISTORY + historyKeyMD5(profileId, key), profileId);
    }

    /**
     * 查询该用户有没有给对应的key投过票
     *
     * @param profileId
     * @param key
     * @return
     */
    public boolean getVoteHistory(String profileId, String key) {
        String value = manager.get(VOTE_HISTORY + historyKeyMD5(profileId, key));
        if (StringUtil.isEmpty(value)) {
            putVoteHistory(profileId, key);
            return false;
        }
        return true;
    }

    private String historyKeyMD5(String profileId, String key) {
        return MD5Util.Md5(profileId + key);
    }

    public int getVoteTitle(String key) {
        String value = manager.get(VOTE_KEY + key);
        if (StringUtil.isEmpty(value)) {
            return 0;
        }
        return Integer.parseInt(value);
    }


}
