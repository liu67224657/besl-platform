package com.enjoyf.platform.serv.vote;

import com.enjoyf.platform.service.vote.Vote;
import com.enjoyf.platform.service.vote.WikiVote;
import com.enjoyf.platform.util.collection.MemDiskCacheConfig;
import com.enjoyf.platform.util.collection.MemDiskCacheManager;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;
import net.sf.ehcache.Element;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-21
 * Time: 下午5:06
 * To change this template use File | Settings | File Templates.
 */
public class VoteCache {
    //
    private static final String SERVICE_SECTION = "vote";
    private static final String KEY_CACHE_VOTE_NAME = "_vote_";
    private static final String KEY_CACHE_WIKI_VOTE = "_wiki_vote_";

    private MemCachedConfig config;

    private MemCachedManager manager;

    //
    public VoteCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(this.config);
    }

    public Vote getVote(String key) {
        Object vote = manager.get(SERVICE_SECTION + KEY_CACHE_VOTE_NAME);
        if (vote == null) {
            return null;
        }
        return (Vote) vote;
    }

    public void putVote(String key, Vote vote) {
        manager.put(SERVICE_SECTION + KEY_CACHE_VOTE_NAME, vote, 5l * 60l);
    }

    public boolean removeVote(String key) {
        return manager.remove(SERVICE_SECTION + KEY_CACHE_VOTE_NAME);
    }

//    public int getVoteSize() {
//        return manager.getMcc(SERVICE_SECTION + KEY_CACHE_VOTE_NAME);
//    }

    public void putWikiVote(long articleId, WikiVote wikiVote) {
        manager.put(SERVICE_SECTION + KEY_CACHE_WIKI_VOTE + articleId, wikiVote, 5l * 60l);
    }

    public WikiVote getWikiVote(long articleId) {
        Object object = manager.get(SERVICE_SECTION + KEY_CACHE_WIKI_VOTE + articleId);
        if (object == null) {
            return null;
        }
        return (WikiVote) object;
    }

    public boolean removeWikiVote(long articleId) {
        return manager.remove(SERVICE_SECTION + KEY_CACHE_WIKI_VOTE + articleId);
    }
}
