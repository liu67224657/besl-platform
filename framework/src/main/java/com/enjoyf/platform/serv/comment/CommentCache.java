/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.comment;

import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.gameclient.dto.GameClientPicDTO;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
public class CommentCache {

    private Logger logger = LoggerFactory.getLogger(CommentCache.class);

    private static final long TIME_OUT_SEC = 60l * 60l * 1l;
    //
    private static final String SERVICE_SECTION = "comment";

    private static final String KEY_CACHE_COMMENT_BEAN = "_bean_id_";

    private static final String KEY_CACHE_COMMENT_REPLY = "_reply_id_";

    private static final String KEY_CACHE_COMMENT_REPLY_IDS = "_reply_ids_";

    private static final String KEY_CACHE_COMMENT_HISTORY_UNO = "_history_uno_";

    private static final String KEY_CACHE_COMMENT_HISTORY = "_history_";

    private static final String KEY_CACHE_COMMENT_RIGHT_HTML = "_righthtml_";

    private static final String KEY_CACHE_COMMENT_BEAN_AVG_SCORE = "_bean_avgscore_";

    private static final String KEY_CACHE_COMMENT_HOT_REPLY_IDS = "_hot_reply_ids_";

    private static final String KEY_CACHE_COMMENT_ORDER_FIELD_IDS = "_field_reply_ids_";

    private static final String KEY_CACHE_COMMENT_FORBID = "_forbid_profileid_";

    private static final String KEY_CACHE_COMMENT_SUM = "_commentbean_sum_";

    private static final String KEY_CACHE_COMMENT_INTERVAL = "_post_interval_";

    private static final String KEY_CACHE_COMMENT_FIFTEEN_INTERVAL = "_post_fifteen_interval_";

    private static final String KEY_CACHE_LAST_COMMENT_REPLY = "_last_comment_reply_";

    private static final String KEY_CACHE_COMMENT_REPLY_BY_ARGEE = "comment_reply_by_argee_";

    private static final String COMMENT_SET_TOP = "comment_set_top_";

    private static final String KEY_COMMENT_PERMISSION = "_comment_permission_";

    private static final long COMMENT_TOP_TIME_OUT = 60l * 60l * 24l * 14l;

    private static final long COMMENT_PERMISSION_TIME_OUT = 60l * 60l * 24l * 365l;
    //
    private MemCachedConfig config;

    private MemCachedManager manager;

    public CommentCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(config);
    }

    //comment reply
    public void putCommentReplyIdList(String commentId, Long rootId, int curPage, List<Long> replyIdList) {
        manager.put(SERVICE_SECTION + KEY_CACHE_COMMENT_REPLY_IDS + commentId + rootId + curPage, replyIdList, TIME_OUT_SEC);
    }


    public List<Long> getCommentReplyIdList(String commentId, Long rootId, int curPage) {
        Object idList = manager.get(SERVICE_SECTION + KEY_CACHE_COMMENT_REPLY_IDS + commentId + rootId + curPage);
        if (idList == null) {
            return null;
        }
        return (List<Long>) idList;
    }

    public void removeCommentReplyIdList(String commentId, Long rootId, int curPage) {
        manager.remove(SERVICE_SECTION + KEY_CACHE_COMMENT_REPLY_IDS + commentId + rootId + curPage);
    }

    public CommentReply getCommentReply(String commentId, Long replyId) {
        Object reply = manager.get(SERVICE_SECTION + KEY_CACHE_COMMENT_REPLY + commentId + replyId);
        if (reply == null) {
            return null;
        }
        return (CommentReply) reply;
    }

    public void putCommentReply(String commentId, long replyId, CommentReply reply) {
        manager.put(SERVICE_SECTION + KEY_CACHE_COMMENT_REPLY + commentId + replyId, reply, TIME_OUT_SEC);
    }

    public void removeCommentReply(String commentId, Long replyId) {
        manager.remove(SERVICE_SECTION + KEY_CACHE_COMMENT_REPLY + commentId + replyId);
    }

    public void putCommentBean(String commentId, CommentBean commentBean) {
        manager.put(SERVICE_SECTION + KEY_CACHE_COMMENT_BEAN + commentId, commentBean, TIME_OUT_SEC);
    }

    public void setCommentBeanTop(CommentBean commentBean) {
        manager.put(COMMENT_SET_TOP + commentBean.getDomain().getCode() + "_" + commentBean.getGroupId(), commentBean, COMMENT_TOP_TIME_OUT);//todo 7day 定义成常量
    }

    public CommentBean getCommentBeanTop(CommentDomain domain, Long groupId) {
        Object commentBean = manager.get(COMMENT_SET_TOP + domain.getCode() + "_" + groupId);
        if (null == commentBean) {
            return null;
        }
        return (CommentBean) commentBean;
    }

    public void deleteCommentBeanTop(CommentDomain domain, Long groupId) {
        manager.remove(COMMENT_SET_TOP + domain.getCode() + "_" + groupId);
    }

    public CommentBean getCommentBean(String commentId) {
        Object commentBean = manager.get(SERVICE_SECTION + KEY_CACHE_COMMENT_BEAN + commentId);
        if (commentBean == null) {
            return null;
        }
        return (CommentBean) commentBean;
    }

    public void removeCommentBean(String commentId) {
        manager.remove(SERVICE_SECTION + KEY_CACHE_COMMENT_BEAN + commentId);
    }

    public void putCommentHistoryByUno(String commentId, Integer domain, String profileId, CommentHistory history) {
        manager.put(SERVICE_SECTION + KEY_CACHE_COMMENT_HISTORY_UNO + commentId + domain + profileId, history, TIME_OUT_SEC);
    }


    public CommentHistory getCommentHistoryByUno(String commentId, Integer domain, String profileId) {
        Object history = manager.get(SERVICE_SECTION + KEY_CACHE_COMMENT_HISTORY_UNO + commentId + domain + profileId);
        if (history == null) {
            return null;
        }
        try {
            return (CommentHistory) history;
        } catch (Exception e) {
            logger.error("get CommentHistory error.e: ", e);
            removeCommentHistoryByUno(commentId, domain, profileId);
            return null;
        }
    }

    public void removeCommentHistoryByUno(String commentId, Integer domain, String profileId) {
        manager.remove(SERVICE_SECTION + KEY_CACHE_COMMENT_HISTORY_UNO + commentId + domain + profileId);
    }

    public void putRightHtml(String commentId, String html) {
        manager.put(SERVICE_SECTION + KEY_CACHE_COMMENT_RIGHT_HTML + commentId, html, TIME_OUT_SEC);
    }

    public String getRightHtml(String commentId) {
        Object html = manager.get(SERVICE_SECTION + KEY_CACHE_COMMENT_RIGHT_HTML + commentId);
        if (html == null) {
            return null;
        }
        return (String) html;
    }

    public List<CommentBean> getCommentBeanByAvgScore(String keyWords) {
        Object list = manager.get(SERVICE_SECTION + KEY_CACHE_COMMENT_BEAN_AVG_SCORE + keyWords);
        if (list == null) {
            return null;
        }
        return (List<CommentBean>) list;
    }

    public void putCommentBeanByAvgScore(String keyWords, List<CommentBean> list) {
        manager.put(SERVICE_SECTION + KEY_CACHE_COMMENT_BEAN_AVG_SCORE + keyWords, list, 30l * 60l);
    }

    public boolean removeCommentBeanByAvgScore(String keyWords) {
        return manager.remove(SERVICE_SECTION + KEY_CACHE_COMMENT_BEAN_AVG_SCORE + keyWords);
    }

    public void putCommentHistory(String profileId, String objectId, Integer type, Integer domain, CommentHistory history) {
        manager.put(SERVICE_SECTION + KEY_CACHE_COMMENT_HISTORY + profileId + objectId + type + domain, history, 5l * 60l);
    }

    public CommentHistory getCommentHistory(String profileId, String objectId, Integer type, Integer domain) {
        Object commentHistory = manager.get(SERVICE_SECTION + KEY_CACHE_COMMENT_HISTORY + profileId + objectId + type + domain);
        if (commentHistory == null) {
            return null;
        }
        return (CommentHistory) commentHistory;
    }

    public boolean removeCommentHistory(String profileId, String objectId, Integer type, Integer domain) {
        return manager.remove(SERVICE_SECTION + KEY_CACHE_COMMENT_HISTORY + profileId + objectId + type + domain);
    }

    public void putHotReplyIdList(String commentId, List<Long> idList) {
        manager.put(SERVICE_SECTION + KEY_CACHE_COMMENT_HOT_REPLY_IDS + commentId, idList, 30l * 60l);
    }

    public List<Long> getHotReplyIdList(String commentId) {
        Object hotList = manager.get(SERVICE_SECTION + KEY_CACHE_COMMENT_HOT_REPLY_IDS + commentId);
        if (hotList == null) {
            return null;
        }
        return (List<Long>) hotList;
    }

    public boolean removeHotReplyIdList(String commentId) {
        return manager.remove(SERVICE_SECTION + KEY_CACHE_COMMENT_HOT_REPLY_IDS + commentId);
    }

    public List<Long> getReplyIdListByOrderField(String commentId, long rootId, String column, String order, int curPage) {
        Object idList = manager.get(SERVICE_SECTION + KEY_CACHE_COMMENT_ORDER_FIELD_IDS + commentId + rootId + column + order + curPage);
        if (idList == null) {
            return null;
        }
        return (List<Long>) idList;
    }

    public void putReplyIdListByOrderField(String commentId, long rootId, String column, String order, int curPage, List<Long> idList) {
        manager.put(SERVICE_SECTION + KEY_CACHE_COMMENT_ORDER_FIELD_IDS + commentId + rootId + column + order + curPage, idList, 30l * 60l);
    }

    public boolean removeReplyIdListByOrderField(String commentId, long rootId, String column, String order, int curPage) {
        return manager.remove(SERVICE_SECTION + KEY_CACHE_COMMENT_ORDER_FIELD_IDS + commentId + rootId + column + order + curPage);
    }

    public CommentForbid getCommentForbid(String profileId) {
        Object forbid = manager.get(SERVICE_SECTION + KEY_CACHE_COMMENT_FORBID + profileId);
        if (forbid == null) {
            return null;
        }
        return (CommentForbid) forbid;
    }

    public void putCommentForbid(String profileId, CommentForbid commentForbid) {
        manager.put(SERVICE_SECTION + KEY_CACHE_COMMENT_FORBID + profileId, commentForbid, 30l * 60l);
    }

    public boolean removeCommentForbid(String profileId) {
        return manager.remove(SERVICE_SECTION + KEY_CACHE_COMMENT_FORBID + profileId);
    }

    public Map<Integer, Integer> getCommentBeanSum() {
        Object object = manager.get(SERVICE_SECTION + KEY_CACHE_COMMENT_SUM);
        if (object == null) {
            return new HashMap<Integer, Integer>();
        }
        return (Map<Integer, Integer>) object;
    }

    public void putCommentBeanSum(Map<Integer, Integer> sum) {
        manager.put(SERVICE_SECTION + KEY_CACHE_COMMENT_SUM, sum, 60l * 180l);
    }

    public boolean removeCommentBeanSum() {
        return manager.remove(SERVICE_SECTION + KEY_CACHE_COMMENT_SUM);
    }


    public void putCommentBeanList(String code, List<GameClientPicDTO> commentBeanList) {
        manager.put(SERVICE_SECTION + code, commentBeanList, 180l * 60l);
    }

    public List<GameClientPicDTO> getCommentBeanList(String code) {
        Object commentBeans = manager.get(SERVICE_SECTION + code);
        if (commentBeans == null) {
            return null;
        }
        return (List<GameClientPicDTO>) commentBeans;
    }

    public boolean removeCommentBeanList(String code) {
        return manager.remove(SERVICE_SECTION + code);
    }

    /**
     * 用于校验内容重复的评论
     *
     * @param profileId
     * @param text
     */
    public void putCommentInterval(String profileId, String text) {
        manager.put(SERVICE_SECTION + KEY_CACHE_COMMENT_INTERVAL + profileId, text, 60l);
    }

    public String getCommentInterval(String profileId) {
        Object text = manager.get(SERVICE_SECTION + KEY_CACHE_COMMENT_INTERVAL + profileId);
        if (text == null) {
            return null;
        }
        return (String) text;
    }

    /**
     * 用于校验15秒内不能发言
     *
     * @param profileId
     */
    public void putCommentSeconds(String profileId) {
        manager.put(SERVICE_SECTION + KEY_CACHE_COMMENT_FIFTEEN_INTERVAL + profileId, profileId, 15l);
    }

    public boolean getCommentSeconds(String profileId) {
        Object text = manager.get(SERVICE_SECTION + KEY_CACHE_COMMENT_FIFTEEN_INTERVAL + profileId);
        if (text == null) {
            return false;
        }
        return true;
    }

    public CommentReply getLastCommentReply(String commentId) {
        Object reply = manager.get(SERVICE_SECTION + KEY_CACHE_LAST_COMMENT_REPLY + commentId);
        if (reply == null) {
            return null;
        }
        return (CommentReply) reply;
    }

    public void putLastCommentReply(String commentId, CommentReply reply) {
        manager.put(SERVICE_SECTION + KEY_CACHE_LAST_COMMENT_REPLY + commentId, reply, 60l * 60l * 24l);
    }

    public boolean removeLastCommentReply(String commentId) {
        return manager.remove(SERVICE_SECTION + KEY_CACHE_LAST_COMMENT_REPLY + commentId);
    }

    //按点赞数去
    public CommentReply getCommentReplyByAgree(String commentId) {
        Object reply = manager.get(KEY_CACHE_COMMENT_REPLY_BY_ARGEE + commentId);
        if (reply == null) {
            return null;
        }
        return (CommentReply) reply;

    }

    public void putCommentReplyByAgree(String commentId, CommentReply reply) {
        manager.put(KEY_CACHE_COMMENT_REPLY_BY_ARGEE + commentId, reply, 60l * 30l);
    }

    public boolean removeCommentReplyByAgree(String commentId) {
        return manager.remove(KEY_CACHE_COMMENT_REPLY_BY_ARGEE + commentId);
    }

    public CommentPermission getCommentPermission(String permissionId) {
        Object object = manager.get(KEY_COMMENT_PERMISSION + permissionId);
        if (null == object) {
            return null;
        }
        return (CommentPermission) object;
    }

    public void putCommentPermission(CommentPermission permission) {
        manager.put(KEY_COMMENT_PERMISSION + permission.getPermissionId(), permission, COMMENT_PERMISSION_TIME_OUT);
    }

    public boolean removeCommentPermission(String permissionId) {
        return manager.remove(KEY_COMMENT_PERMISSION + permissionId);
    }
}
