package com.enjoyf.platform.service.comment;

import com.enjoyf.platform.service.gameclient.dto.GameClientPicDTO;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.rpc.RPC;
import com.enjoyf.platform.service.vote.WikiVote;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.mongodb.BasicDBObject;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-11
 * Time: 上午9:42
 * To change this template use File | Settings | File Templates.
 */
public interface CommentService {

    /**
     * 通过缓存查询评论列表
     *
     * @param commentId
     * @param rootId
     * @param pagination
     * @param desc
     * @return
     * @throws ServiceException
     */
    @RPC
    public PageRows<CommentReply> queryCommentReplyByCache(String commentId, Long rootId, Pagination pagination, Boolean desc) throws ServiceException;

    /**
     * 根据 replyid定位到所在的页码,并且得到这也所在的评论
     *
     * @param commentId
     * @param rootId
     * @param replyId
     * @param size
     * @param desc
     * @return
     * @throws ServiceException
     */
    @RPC
    public PageRows<CommentReply> queryCommentReplyByReplyIdFromCache(String commentId, Long rootId, Long replyId, Integer size, Boolean desc) throws ServiceException;

    /**
     * @param commentIds
     * @param rootId
     * @param pagination
     * @param desc       true是desc ?
     * @return
     * @throws ServiceException
     */
    @RPC
    public Map<String, PageRows<CommentReply>> queryCommentReplyFromCacheByCommentIds(List<String> commentIds, long rootId, Pagination pagination, Boolean desc) throws ServiceException;

    @RPC
    public CommentBean getCommentBeanById(String commentId) throws ServiceException;

    @RPC
    public boolean modifyCommentBeanById(String commentId, UpdateExpress updateExpress) throws ServiceException;

    @RPC
    public CommentBean createCommentBean(CommentBean comment) throws ServiceException;

    @RPC
    public Map<String, CommentBean> queryCommentBeanByIds(Set<String> ids) throws ServiceException;

    @RPC
    public List<GameClientPicDTO> queryCommentBeanByCache(int page) throws ServiceException;

    @RPC
    public void refreshCommentBeanCache() throws ServiceException;

    /**
     * 发布评论
     *
     * @param reply
     * @param totalRows
     * @return
     * @throws ServiceException
     */
    @RPC
    public CommentReply createCommentReply(CommentReply reply, Integer totalRows) throws ServiceException;

    @RPC
    public CommentReply getCommentReplyById(String commentId, Long replyId) throws ServiceException;

    @RPC
    public boolean modifyCommentReplyById(String commentId, Long replyId, UpdateExpress updateExpress) throws ServiceException;

    @RPC
    public CommentHistory getCommentHistoryByProfileId(String commentId, CommentDomain domain, String profileId) throws ServiceException;

    @RPC
    public CommentHistory createCommentHistory(CommentHistory commentHistory, CommentBean commentBean, CommentReply reply) throws ServiceException;

    @RPC
    public boolean modifyCommentHistoryByProfileId(String commentId, CommentDomain domain, String profileId, UpdateExpress updateExpress) throws ServiceException;

    /**
     * 删除评论
     *
     * @param commentId
     * @param replyId
     * @param rootId
     * @param updateExpress
     * @throws ServiceException
     */
    @RPC
    public void removeCommentReply(String commentId, Long replyId, Long rootId, UpdateExpress updateExpress) throws ServiceException;

    @RPC
    public String getRightHtmlByArticleId(CommentBean commentBean) throws ServiceException;

    @RPC
    public List<CommentBean> queryCommentBeanByAvgScore(String keyWords) throws ServiceException;

    @RPC
    public List<CommentBean> queryCommentBean(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public PageRows<CommentBean> queryCommentBeanByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public CommentHistory getCommentHistoryByCache(String profileId, String objectId, CommentHistoryType agree, CommentDomain commentDomain) throws ServiceException;

    @RPC
    public List<CommentHistory> queryCommentHistory(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public CommentBean getCommentBean(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public PageRows<CommentReply> queryCommentReplyByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public List<CommentReply> queryCommentReply(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public CommentReply getCommentReply(QueryExpress add) throws ServiceException;

    @RPC
    public boolean modifyCommentReply(QueryExpress add, UpdateExpress updateExpress) throws ServiceException;

    @RPC
    public void recoverCommentReply(String commentId, Long replyId, Long rootId, Date createTime, UpdateExpress updateExpress) throws ServiceException;

    @RPC
    public List<CommentReply> queryHotReplyByCache(String commentId, Pagination pagination, boolean isCache) throws ServiceException;

    @RPC
    public PageRows<CommentReply> queryCommentReplyByOrderField(String commentId, Long rootId, Pagination pagination, CommentReplyField orderField, QuerySortOrder order) throws ServiceException;


    //CommentForbid表 start

    @RPC
    public CommentForbid createCommentForbid(CommentForbid commentForbid) throws ServiceException;

    @RPC
    public CommentForbid getCommentForbid(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public List<CommentForbid> queryCommentForbid(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public PageRows<CommentForbid> queryCommentForbidByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public boolean modifyCommentForbid(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    @RPC
    public boolean deleteCommentForbid(String profileId) throws ServiceException;

    @RPC
    public CommentForbid getCommentForbidByCache(String profileId) throws ServiceException;

    /**
     * 禁止俩条评论内容一样的逻辑
     *
     * @param profileId
     * @return
     * @throws ServiceException
     */
    @RPC
    public String getCommentIntervalCache(String profileId) throws ServiceException;


    //CommentVoteOption  表

    @RPC
    public int countCommentVoteOption(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public CommentVoteOption createCommentVoteOption(CommentVoteOption commentVoteOption) throws ServiceException;

    @RPC
    public CommentVoteOption getCommentVoteOption(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public List<CommentVoteOption> queryCommentVoteOption(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public PageRows<CommentVoteOption> queryCommentVoteOptionByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public boolean modifyCommentVoteOption(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    @RPC
    public boolean deleteCommentVoteOption(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public WikiVote getWikiVote(String url, String pic, String name, String num, Long articleId, String keyWords, Integer voteSum) throws ServiceException;

    @RPC
    public boolean incWikiVote(String url, Long articleId, BasicDBObject query, BasicDBObject update) throws ServiceException;

    @RPC
    public List<WikiVote> queryWikiVotes(Set<Long> idSet) throws ServiceException;

    @RPC
    public List<WikiVote> queryWikiVoteByPage(MongoQueryExpress mongoQueryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public CommentReply getLastCommentReply(String commentId) throws ServiceException;


    //查询帖子点赞数最多的评论
    @RPC
    public Map<String, CommentReply> queryHotReplyCacheByAgreeSum(Set<String> commentIdSet) throws ServiceException;

    @RPC
    public CommentBean getCommentBeanByUniKey(String uniKey) throws ServiceException;

    @RPC
    public PageRows<CommentBean> queryCommentBeanByGroup(CommentDomain domain, Long groupId, Pagination page, String sort) throws ServiceException;

    //直播评论置顶
    @RPC
    public void setCommentBeanTop(String commentId) throws ServiceException;

    //获取直播置顶评论
    @RPC
    public CommentBean getCommentBeanTop(CommentDomain domain, Long groupId) throws ServiceException;

    //删除直播置顶评论
    @RPC
    public void deleteCommentBeanTop(CommentDomain domain, Long groupId) throws ServiceException;

    @RPC
    public List<CommentBean> queryCommentBeanByScore(CommentDomain domain, Long groupId, Long score) throws ServiceException;

    @RPC
    public boolean deleteCommentBean(String commentId) throws ServiceException;

    @RPC
    public PageRows<CommentVideo> queryCommentVideoPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public List<CommentVideo> queryCommentVideoByList(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public CommentVideo createCommentVideo(CommentVideo commentVideo) throws ServiceException;

    @RPC
    public CommentVideo getCommentVideo(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public boolean modifyCommentVideo(UpdateExpress updateExpress, String commentVideoId, String type) throws ServiceException;

    @RPC
    public void putHotVideoList(List<Long> commentVideoList) throws ServiceException;

    /**
     * @param gamesdk
     * @param type    1为后台工具调用redis 2为前台展示页面调用的redis 后台点击更新后 把数据放入前台redis做展示
     * @throws ServiceException
     */
    @RPC
    public List<CommentVideo> queryHotVideoListByRedis(String gamesdk, int type) throws ServiceException;

    @RPC
    public String updateHotList(String gameSdk) throws ServiceException;

    @RPC
    public void sortHotVideoListByRedis(String gameSdk, String oldIndex, String newIndex) throws ServiceException;

    @RPC
    public int countCommentReply(QueryExpress add) throws ServiceException;

    /**
     * 获取用户权限
     *
     * @param permissionId
     * @return
     * @throws ServiceException
     */
    @RPC
    public CommentPermission getPermissionByProfileId(String permissionId) throws ServiceException;

    /**
     * 获取权限列表
     *
     * @return
     * @throws ServiceException
     */
    @RPC
    public List<CommentPermission> queryCommentPermissionList() throws ServiceException;

    /**
     * 增加权限
     *
     * @param permissionId
     * @param profileId
     * @param permissionType
     * @param createUserId
     * @return
     * @throws ServiceException
     */
    @RPC
    public CommentPermission createCommentPermission(String permissionId, String profileId, CommentPermissionType permissionType, int createUserId) throws ServiceException;

    /**
     * 删除权限
     *
     * @param permissionId
     * @return
     * @throws ServiceException
     */
    @RPC
    public boolean deleteCommentPermission(String permissionId) throws ServiceException;

    /**
     * 转码视频
     *
     * @param commentId
     * @param persistentId 七牛查询转码视频查询id
     * @return
     * @throws ServiceException
     */
    @RPC
    public void createTranscodeVideo(String commentId, String persistentId) throws ServiceException;


    /**
     * 删除点评原因
     *
     * @param id
     * @param reason
     * @param type 0=删除 1=恢复
     */
    @RPC
    public void addCommentOperReason(String id, String reason, int type);


    /**
     * 被删除原因
     *
     * @param id
     *  @param type 0=删除 1=恢复
     * @return
     */
    @RPC
    public String getCommentOperReason(String id, int type);
}
