package com.enjoyf.platform.service.search.redis;

import com.enjoyf.platform.db.comment.CommentHandler;
import com.enjoyf.platform.serv.comment.CommentCache;
import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.service.comment.CommentBeanField;
import com.enjoyf.platform.service.comment.CommentReply;
import com.enjoyf.platform.service.comment.CommentReplyField;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.profile.socialclient.SocialProfile;
import com.enjoyf.platform.service.search.solr.ProfileSolrServer;
import com.enjoyf.platform.service.search.solr.SolrCore;
import com.enjoyf.platform.service.search.solr.SolrServer;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-13
 * Time: 下午4:12
 * To change this template use File | Settings | File Templates.
 */
public class RedisServer {

    private static final Logger logger = LoggerFactory.getLogger(RedisServer.class);

    private static final String KEY = "searchjobs";
    private static final String DELIMITER = "|";
    private static final int seconds = 10;

    private static JedisPoolConfig config;
    private static String serverHost;
    private static int serverPort;
    private static JedisPool jedisPool;

    public RedisServer() {
    }

    public RedisServer(String host, int port) {
        try {
            config = new JedisPoolConfig();
            config.setMaxTotal(100);
            config.setMaxIdle(20);
            config.setMaxWaitMillis(1000l);
            serverHost = host;
            serverPort = port;
            jedisPool = new JedisPool(config, serverHost, serverPort);
        } catch (Exception e) {
            GAlerter.lad(this.getClass().getName() + " occur Exception.e:", e);
        }
    }

    public void lPush(SolrCore solrCore, String appKey, String uniqueParam) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.lpush(KEY, solrCore.getCore() + DELIMITER + appKey + DELIMITER + uniqueParam);
        } catch (Exception e) {
            GAlerter.lad("RedisServer lPush occur Exception.e:", e);
        } finally {
            if (jedis != null && jedisPool != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }


//    public void brPop(SolrServer solrjServer) throws ServiceException {
//        Jedis jedis = jedisPool.getResource();
//        for (; ; ) {
//            List<String> list = jedis.brpop(1, KEY);
//            if (list != null) {
//                System.out.println(list);
//                for (String s : list) {
//                    if (s.equals(KEY)) {
//                        continue;
//                    }
//                    String[] params = s.split("\\|");
//                    if(params.length < 3){
//                        continue;
//                    }
//                    String core = params[0];
//                    String appKey = params[1];
//                    String uno = params[2];
//                    SocialProfile profile = ProfileServiceSngl.get().getSocialProfileByUno(uno);
//                    if(profile == null){
//                        continue;
//                    }
//
//                    SolrInputDocument doc = new SolrInputDocument();
//                    doc.addField("uno", profile.getUno());
//                    doc.addField("appkey", appKey);
//                    doc.addField("age", 0);
//                    doc.addField("birthday", profile.getDetail().getBirthday() == null ? "" : profile.getDetail().getBirthday());
//                    doc.addField("searchtext", profile.getBlog().getScreenName() + profile.getBlog().getDescription());
//                    doc.addField("sex", profile.getDetail().getSex() == null ? "" : profile.getDetail().getSex());
//                    solrjServer.add(SolrCore.getByCore(core), doc);
//                }
//            }
//        }
//    }

    public void lPushIncComment(String key, String jsonBean) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.lpush(key, jsonBean);
        } catch (Exception e) {
            GAlerter.lad("RedisServer lPush occur Exception.e:", e);
        } finally {
            if (jedis != null && jedisPool != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public void brpopIncCommentBean(String key, CommentHandler writeAbleHandler, CommentCache commentCache) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            for (; ; ) {
                List<String> list = jedis.brpop(10, key);
                if (list != null) {
                    System.out.println(list.toArray());
                    for (String jsonCommentBean : list) {
                        if (jsonCommentBean.equals(key)) {
                            continue;
                        }
                        CommentBean commentBean = CommentBean.parse(jsonCommentBean);
                        QueryExpress queryExpress = new QueryExpress();
                        queryExpress.add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentBean.getCommentId()));
                        UpdateExpress updateExpress = new UpdateExpress();
                        updateExpress.increase(CommentBeanField.COMMENT_SUM, commentBean.getCommentSum());
                        updateExpress.increase(CommentBeanField.TOTAL_ROWS, commentBean.getTotalRows());
                        boolean bool = writeAbleHandler.modifyCommentBean(updateExpress, queryExpress);
                        if (bool) {
                            commentCache.removeCommentBean(commentBean.getCommentId());
                        }
                    }
                }
            }
        } catch (Exception e) {
            GAlerter.lab("RedisServer brpopIncCommentSumByReply occur Exception.e:", e);
        } finally {
            if (jedis != null && jedisPool != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public void brpopIncCommentReply(String key, CommentHandler writeAbleHandler, CommentCache commentCache) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            for (; ; ) {
                List<String> list = jedis.brpop(10, key);
                if (list != null) {
                    System.out.println(list.toArray());
                    for (String jsonCommentReply : list) {
                        if (jsonCommentReply.equals(key)) {
                            continue;
                        }
                        CommentReply commentReply = CommentReply.parse(jsonCommentReply);
                        QueryExpress queryExpress = new QueryExpress();
                        queryExpress.add(QueryCriterions.eq(CommentReplyField.REPLY_ID, commentReply.getReplyId()));
                        queryExpress.add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, commentReply.getCommentId()));
                        UpdateExpress updateExpress = new UpdateExpress();
                        updateExpress.increase(CommentReplyField.SUB_REPLY_SUM, commentReply.getSubReplySum());
                        updateExpress.increase(CommentReplyField.TOTAL_ROWS, commentReply.getTotalRows());
                        updateExpress.increase(CommentReplyField.REPLY_AGREE_SUM, commentReply.getReplyAgreeSum());
                        boolean bool = writeAbleHandler.modifyCommentReply(queryExpress, updateExpress);
                        if (bool) {
                            commentCache.removeCommentReply(commentReply.getCommentId(), commentReply.getReplyId());
                        }
                    }
                }
            }
        } catch (Exception e) {
            GAlerter.lab("RedisServer brpopIncCommentSumByReply occur Exception.e:", e);
        } finally {
            if (jedis != null && jedisPool != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

}
