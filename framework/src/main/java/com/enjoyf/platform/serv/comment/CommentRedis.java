/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.comment;


import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.service.comment.CommentReplyHash;
import com.enjoyf.platform.service.comment.CommentReplyHashField;
import com.enjoyf.platform.service.comment.CommentVideo;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author <a href=mailto:ericliu@staff.joyme.com>Eirc Liu</a>
 */

public class CommentRedis {
    private static final Logger logger = LoggerFactory.getLogger(CommentRedis.class);

    private static final int TIME_OUT_SEC = 24 * 60 * 60;

    private static final String KEY_PREFIX = CommentConstants.SERVICE_SECTION;
    private static final String JOYME_LIVE_BY_GROUP = "_joymelive_";
    private static final String COMMENT_REPLY_IDS = "_reply_list_";
    private static final String COMMENT_VIDEO_HOT_LIST = "_comment_video_hot_list_";
    private static final String COMMENT_VIDEO_HOTS = "_comment_video_hots_";
    private static final String LIVE_COMMENT_VIDEO_TRANSCODE = "_live_comment_video_transcode_";


    private static final String COMMENT_REPLY_FOLORNUM = KEY_PREFIX + "_comment_reply_folornum_";

    private static final String COMMENT_REPLY_ROOT_FOLORNUM = KEY_PREFIX + "_comment_reply_root_folornum_";

    private static final String KEY_HASH_COMMENTSUM = KEY_PREFIX + "_hrsum_";
    private static final String KEY_COMMENT_RIDS_ORDERBY = KEY_PREFIX + "_ridsorderby_";

    private static final String KEY_COMMENT_DELETE_REASON = "comment_micro_comment_delete_reason";


    private RedisManager manager;

    public CommentRedis(FiveProps p) {
        manager = new RedisManager(p);
    }

    //删除或者恢复点评原因
    public void addCommentDeleteReason(String id, String reason, int type) {
        manager.hset(KEY_COMMENT_DELETE_REASON + type, id, reason);
    }

    public String getCommentDeleteRason(String id, int type) {
        Map<String, String> reasonMap = manager.hgetAll(KEY_COMMENT_DELETE_REASON + type);
        if (reasonMap == null) {
            return "";
        }
        return reasonMap.get(id);
    }

    public List<Long> getCommentReplyIds(String commentId, Long rootId, Pagination page, Boolean desc) {
        List<Long> idList = null;
        String key = KEY_PREFIX + COMMENT_REPLY_IDS + commentId + "_" + rootId;
        Set<String> idSet = manager.zrange(key, page.getStartRowIdx(), page.getEndRowIdx(), desc ? RedisManager.RANGE_ORDERBY_DESC : RedisManager.RANGE_ORDERBY_ASC);
        if (idSet != null) {
            idList = new ArrayList<Long>();
            for (String id : idSet) {
                if (!StringUtil.isEmpty(id)) {
                    idList.add(Long.valueOf(id));
                }
            }
            page.setTotalRows((int) manager.zcard(key));
        }
        return idList;
    }

    public int getReplyIdIndex(String commentId, Long rootId, Long replyId, Boolean desc) {
        String key = KEY_PREFIX + COMMENT_REPLY_IDS + commentId + "_" + rootId;
        long index = manager.zrank(key, String.valueOf(replyId), desc ? RedisManager.RANGE_ORDERBY_DESC : RedisManager.RANGE_ORDERBY_ASC);
        return (int) index;
    }

    public int replyIdCount(String commentId, Long rootId, Long replyId) {
        String key = KEY_PREFIX + COMMENT_REPLY_IDS + commentId + "_" + rootId;
        return (int) manager.zcard(key);
    }


    public void putCommentReplyIds(String commentId, Long rootId, Long replyId, double score) {
        String key = KEY_PREFIX + COMMENT_REPLY_IDS + commentId + "_" + rootId;
        manager.zadd(key, score, String.valueOf(replyId), 0);
    }

    public boolean removeCommentReplyIds(String commentId, Long rootId, Long replyId) {
        String key = KEY_PREFIX + COMMENT_REPLY_IDS + commentId + "_" + rootId;
        return manager.zrem(key, String.valueOf(replyId)) > 0;
    }

    public void putJoymeLiveCache(CommentDomain domain, Long groupId, String commentId, double score) {
        String key = getJoymeLiveByGroupKey(domain.getCode(), groupId);
        manager.zadd(key, score, commentId, -1);
    }

    public PageRows<String> getJoymeLiveCache(CommentDomain domain, Long groupId, Pagination page, String sort) {
        PageRows<String> pageRows = null;

        String key = getJoymeLiveByGroupKey(domain.getCode(), groupId);
        String orderBy;
        if (StringUtil.isEmpty(sort)) {
            orderBy = RedisManager.RANGE_ORDERBY_DESC;
        } else {
            orderBy = RedisManager.RANGE_ORDERBY_ASC;
        }
        Set<String> idSet = manager.zrange(key, page.getStartRowIdx(), page.getEndRowIdx(), orderBy);
        if (!CollectionUtil.isEmpty(idSet)) {
            pageRows = new PageRows<String>();
            List<String> idList = new ArrayList<String>();
            for (String commentId : idSet) {
                idList.add(commentId);
            }

            page.setTotalRows((int) manager.zcard(key));

            pageRows.setRows(idList);
            pageRows.setPage(page);
        }
        return pageRows;
    }

    public Set<String> getJoymeLiveCacheByScore(CommentDomain domain, Long groupId, double score) {
        String key = getJoymeLiveByGroupKey(domain.getCode(), groupId);
        return manager.zrevrangeByScore(key, "+inf", "(" + score);
    }

    private String getJoymeLiveByGroupKey(Integer domainCode, Long groupId) {
        return KEY_PREFIX + JOYME_LIVE_BY_GROUP + domainCode + "_" + groupId;
    }

    public void putCommentVideoHotList(CommentVideo video) {
        String key = KEY_PREFIX + COMMENT_VIDEO_HOT_LIST + video.getSdk_key();
        manager.lpush(key, video.toJson());
    }

    //后台用缓存，后台点击更新后 把数据放入另一个redis做前提展示
    public List<CommentVideo> queryCommentVideoHotList(String gameSdk) {
        //todo code review 建议key的名字取得贴近业务
        String key = KEY_PREFIX + COMMENT_VIDEO_HOT_LIST + gameSdk;
        List<String> jsonList = manager.lrange(key, 0, -1);
        List<CommentVideo> returnList = new ArrayList<CommentVideo>();
        if (!CollectionUtil.isEmpty(jsonList)) {
            for (String jsonString : jsonList) {
                returnList.add(CommentVideo.parse(jsonString));
            }
        }
        return returnList;
    }

    //前台缓存
    public List<CommentVideo> queryCommetVideoHots(String gameSdk) {
        String key = KEY_PREFIX + COMMENT_VIDEO_HOTS + gameSdk;
        List<String> jsonList = manager.lrange(key, 0, -1);
        List<CommentVideo> returnList = new ArrayList<CommentVideo>();
        if (!CollectionUtil.isEmpty(jsonList)) {
            for (String jsonString : jsonList) {
                returnList.add(CommentVideo.parse(jsonString));
            }
        }

        return returnList;
    }

    public void putCommentVideoHots(CommentVideo video) {
        String key = KEY_PREFIX + COMMENT_VIDEO_HOTS + video.getSdk_key();
        manager.rpush(key, video.toJson());
    }

    public boolean deleteCommentVideoHots(String gameSdk) {
        String key = KEY_PREFIX + COMMENT_VIDEO_HOTS + gameSdk;
        return manager.remove(key) > 0;
    }

    public boolean deleteCommenVideoHotList(CommentVideo commentVideo) {
        String key = KEY_PREFIX + COMMENT_VIDEO_HOT_LIST + commentVideo.getSdk_key();
        return manager.lrem(key, 1, commentVideo.toJson()) > 0;
    }

    public void CommentVideoSort(String gameSdk, String oldIndex, String newIndex) {
        String key = KEY_PREFIX + COMMENT_VIDEO_HOT_LIST + gameSdk;
        String oldInfo = manager.lindex(key, Integer.parseInt(oldIndex));
        String newInfo = manager.lindex(key, Integer.parseInt(newIndex));

        manager.lset(key, Long.parseLong(oldIndex), newInfo);
        manager.lset(key, Long.parseLong(newIndex), oldInfo);
        //    manager.lset(key, index, video.toJson());
    }


    public void removeJoymeLiveCache(CommentDomain domain, Long groupId, String commentId) {
        String key = getJoymeLiveByGroupKey(domain.getCode(), groupId);
        GAlerter.lab("========================live:" + key);
        manager.zrem(key, commentId);
    }

    public void putTranscodeVideo(String commentId, String persistentId) {
        manager.hset(LIVE_COMMENT_VIDEO_TRANSCODE, commentId, persistentId);
    }

    public long removeTranscodeVideo(String commentId) {
        return manager.delHash(LIVE_COMMENT_VIDEO_TRANSCODE, commentId);
    }

    public Map<String, String> getTranscodeVideoMap() {
        return manager.hgetAll(LIVE_COMMENT_VIDEO_TRANSCODE);
    }

    ///////////////////////////////////////////////////////
    //主楼的楼层号
    public long incrFolorNum(String commentBeanId) {
        return manager.incr(COMMENT_REPLY_FOLORNUM + commentBeanId, 1, -1);
    }

    //楼中楼的楼层号
    public long incrRootFolorNum(String commentBeanId, long rootId) {
        return manager.incr(COMMENT_REPLY_ROOT_FOLORNUM + commentBeanId + rootId, 1, -1);
    }

    /////////////////////////////////////
    public long incrCommentReplyHash(String commentId, long rootId, CommentReplyHashField field, int value) {
        return manager.hinccrby(getCommentSumHashKey(commentId, rootId), field.getColumn(), value);
    }

    public long setCommentReplyHash(String commentId, long rootId, CommentReplyHashField field, int value) {
        return manager.hset(getCommentSumHashKey(commentId, rootId), field.getColumn(), String.valueOf(value));
    }

    public CommentReplyHash getCommentSum(String commentId, long rootId) {
        Map<String, String> map = manager.hgetAll(getCommentSumHashKey(commentId, rootId));
        if (CollectionUtil.isEmpty(map)) {
            return null;
        }

        CommentReplyHash commentReplyHash = new CommentReplyHash(commentId, rootId);
        if (map.containsKey(CommentReplyHashField.COMMENTSUM.getColumn())) {
            commentReplyHash.setCommentSum(Integer.parseInt(map.get(CommentReplyHashField.COMMENTSUM.getColumn())));
        }
        if (map.containsKey(CommentReplyHashField.TOTAL.getColumn())) {
            commentReplyHash.setCommentTotal(Integer.parseInt(map.get(CommentReplyHashField.TOTAL.getColumn())));
        }
        if (map.containsKey(CommentReplyHashField.AGREESUM.getColumn())) {
            commentReplyHash.setAgreeSum(Integer.parseInt(map.get(CommentReplyHashField.AGREESUM.getColumn())));
        }
        if (map.containsKey(CommentReplyHashField.HOTSUM.getColumn())) {
            commentReplyHash.setHot(Integer.parseInt(map.get(CommentReplyHashField.HOTSUM.getColumn())));
        }
        return commentReplyHash;
    }


    private String getCommentSumHashKey(String commentId, long rootId) {
        return KEY_HASH_COMMENTSUM + commentId + "_" + rootId;
    }

    /////////////////////////////////////////////
    public List<Long> getReplyIdListByOrderField(String commentId, long rootId, CommentReplyHashField column, Pagination page, boolean isDesc) {
        Set<String> idSet = manager.zrange(getReplyIdListOrderKey(commentId, rootId, column), page.getStartRowIdx(), page.getEndRowIdx(), isDesc ? RedisManager.RANGE_ORDERBY_DESC : RedisManager.RANGE_ORDERBY_ASC);

        if (idSet == null) {
            return new ArrayList<Long>();
        }
        List<Long> returnList = new ArrayList<Long>();
        for (String id : idSet) {
            try {
                returnList.add(Long.valueOf(id));
            } catch (NumberFormatException e) {
            }
        }

        return returnList;
    }

    public void putReplyIdListByOrderField(String commentId, long rootId, CommentReplyHashField column, double scrore, long replyId) {
        manager.zadd(getReplyIdListOrderKey(commentId, rootId, column), scrore, String.valueOf(replyId), -1);
    }

    public boolean removeReplyIdListByOrderField(String commentId, long rootId, CommentReplyHashField column) {
        return manager.remove(getReplyIdListOrderKey(commentId, rootId, column)) > 0;
    }

    private String getReplyIdListOrderKey(String commentId, long rootId, CommentReplyHashField column) {
        return KEY_COMMENT_RIDS_ORDERBY + commentId + rootId + column.getColumn();
    }


}

