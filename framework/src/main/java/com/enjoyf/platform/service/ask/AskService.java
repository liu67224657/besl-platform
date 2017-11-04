package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.wiki.*;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.rpc.RPC;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.redis.ScoreRange;
import com.enjoyf.platform.util.redis.ScoreRangeRows;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.*;

/**
 * ask service api
 *
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/12
 */
public interface AskService extends EventReceiver {

    @RPC
    @Override
    boolean receiveEvent(Event event) throws ServiceException;

    @RPC
    Question getQuestion(Long questionId) throws ServiceException;

    @RPC
    boolean modifyQuestion(UpdateExpress updateExpress, Long questionId) throws ServiceException;

    @RPC
    Question postQuestion(Question question) throws ServiceException;

    @RPC
    PageRows<Question> queryQuestionByLineKey(String lineKey, Pagination pagination, boolean isDesc) throws ServiceException;

    @RPC
    ScoreRangeRows<Question> queryQuestionByLineKeyRange(String lineKey, ScoreRange range) throws ServiceException;

    @RPC
    Map<Long, Question> queryQuestionByIds(Collection<Long> qids) throws ServiceException;

    @RPC
    PageRows<Question> queryQuestion(QueryExpress queryExpress, Pagination page) throws ServiceException;

    @RPC
    boolean deleteQuestion(Long questionId) throws ServiceException;

    ////////////////////////////////
    @RPC
    boolean modifyAnswer(UpdateExpress updateExpress, Long answerId) throws ServiceException;

    @RPC
    Answer answer(Answer answer) throws ServiceException;

    @RPC
    Answer getAnswer(Long answer) throws ServiceException;

    @RPC
    Map<Long, Answer> queryAnswerByAnswerIds(Collection<Long> answerIds) throws ServiceException;

    @RPC
    boolean deleteAnswer(Long answerId) throws ServiceException;

    @RPC
    PageRows<Answer> queryAnswerByQuestionId(Long questionId, Pagination page) throws ServiceException;

    @RPC
    PageRows<Answer> queryAnswerByLineKeyRange(String lineKey, Pagination page, boolean isDesc) throws ServiceException;

    ///////////////////////////////
    @RPC
    boolean acceptAnswer(Long answerId, Long questionId) throws ServiceException;

    @RPC
    PageRows<Answer> queryAnswer(QueryExpress queryExpress, Pagination page) throws ServiceException;

    @RPC
    Map<String, ItemType> queryItemByLineKey(String lineKey, Pagination pagination, boolean isDesc) throws ServiceException;

    @RPC
    Long getItemByLineKeySize(String lineKey) throws ServiceException;

    @RPC
    boolean addItemByLineKey(WanbaItem wanbaItem) throws ServiceException;

    @RPC
    boolean removeItemByLineKey(String wanbaItemId) throws ServiceException;


    @RPC
    Map<Long, QuestionSum> queryQuestionSumByQids(Collection<java.lang.Long> qids) throws ServiceException;

    /////////////////////////////
    @RPC
    Answer getAnswerByProfile(Long qid, String profileId) throws ServiceException;


    @RPC
    Boolean addAskUserAction(AskUserAction askUserAction) throws ServiceException;


    @RPC
    Boolean removeAskUserAction(AskUserAction askUserAction) throws ServiceException;

    @RPC
    AskUserAction getAskUserAction(String askUserActionId) throws ServiceException;


    @RPC
    Map<Long, AskUserAction> checkAgreeAskUserAction(Collection<Long> answerIds, String profileId) throws ServiceException;

    @RPC
    PageRows<AskUserAction> queryAskUserAction(String profileid, ItemType itemType, AskUserActionType askUserActionType, Pagination page) throws ServiceException;

    @RPC
    public PageRows<AskUserAction> queryAskUserActionByList(Long destId, ItemType itemType, AskUserActionType askUserActionType, Pagination page) throws ServiceException;

    /////////////////////////////
    @RPC
    boolean modifyAnswerSum(AnswerSumField field, int value, long answerId) throws ServiceException;

    @RPC
    Map<Long, AnswerSum> queryAnswerSumByAids(Collection<Long> aids) throws ServiceException;


    ///////////////////////////

    /**
     * 关注问题
     *
     * @param profileId
     * @param questionId
     * @return
     * @throws ServiceException
     */
    @RPC
    boolean followQuestion(Long questionId, String profileId) throws ServiceException;

    /**
     * 取消关注
     *
     * @param profileId
     * @param questionId
     * @return
     * @throws ServiceException
     */
    @RPC
    boolean unfollowQuestion(Long questionId, String profileId) throws ServiceException;

    /**
     * 查询关注问题的人
     *
     * @param questionId
     * @return
     * @throws ServiceException
     */
    @RPC
    Set<String> queryFollowProfileIds(Long questionId, ScoreRange range) throws ServiceException;

    /**
     * 查询人的关注问题列表
     *
     * @param profileId
     * @return
     * @throws ServiceException
     */
    @RPC
    ScoreRangeRows<Question> queryFollowQuestionByProfileId(String profileId, ScoreRange range) throws ServiceException;


    /**
     * 检查questionIds有多少是被关注的
     *
     * @param questionIds
     * @param profileId
     * @return
     * @throws ServiceException
     */
    @RPC
    Set<Long> checkFollowQuestion(Collection<Long> questionIds, String profileId) throws ServiceException;
    //////////////////////////////

    /**
     * 邀请回答问题
     *
     * @param quesitonId
     * @param inviteProfile
     * @param invitedProfile
     * @return
     * @throws ServiceException
     */
    @RPC
    boolean inviteAnswerQuestion(Long quesitonId, String inviteProfile, String invitedProfile) throws ServiceException;

    /**
     * 查询邀请列表
     *
     * @param questionId
     * @param page
     * @return
     * @throws ServiceException
     */


    /**
     * 检查有没有被邀请过,返回的是邀请过的人
     *
     * @param profileIds 要check的profid结合
     * @param questionId 问题ID
     * @return 返回邀请过的人是profileIds的子集
     * @throws ServiceException
     */
    @RPC
    Set<String> checkInviteProfiles(Collection<String> profileIds, long questionId) throws ServiceException;

    @RPC
    public PageRows<WanbaItem> queryWanbaItem(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public WanbaItem addWanbaItem(WanbaItem wanbaItem) throws ServiceException;

    @RPC
    public WanbaItem getWanbaItem(String itemId) throws ServiceException;

    @RPC
    public boolean modifyWanbaItem(String itemId, UpdateExpress updateExpress) throws ServiceException;

    @RPC
    public WanbaProfileSum getWanProfileSum(String profileId) throws ServiceException;

    @RPC
    public WanbaProfileSum insertWanbaProfileSum(WanbaProfileSum wanbaProfileSum) throws ServiceException;

    @RPC
    Map<String, WanbaProfileSum> queryWanbaProfileSumByPids(Collection<String> profileIds) throws ServiceException;

    @RPC
    List<String> getWanbaRecommentProfiles() throws ServiceException;

    @RPC
    boolean updateWanbaRecommentProfiles(Collection<String> profileIds) throws ServiceException;


    @RPC
    public boolean modifyQuestionSum(QuestionSumField field, int value, long questionId) throws ServiceException;

    @RPC
    public List<WanbaProfileClassify> queryWanbaProfileClassifyList(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public PageRows<WanbaProfileClassify> queryWanbaProfileClassify(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    WanbaProfileClassify getWanbaProfileClassify(String classifyid) throws ServiceException;

    @RPC
    Map<String, WanbaProfileClassify> getWanbaProfileClassifyMap(Set<String> pidSet) throws ServiceException;

    @RPC
    boolean modifyWanbaProfileClassify(UpdateExpress updateExpress, String classifyid) throws ServiceException;

    @RPC
    WanbaProfileClassify insertWanbaProfileClassify(WanbaProfileClassify wanbaProfileClassify) throws ServiceException;

    @RPC
    AskReport insertAskReport(AskReport askReport) throws ServiceException;

    @RPC
    boolean modifyAskReport(UpdateExpress updateExpress, String report_id) throws ServiceException;

    @RPC
    AskReport getAskReport(String report_id) throws ServiceException;

    @RPC
    public PageRows<AskReport> queryAskReport(QueryExpress queryExpress, Pagination pagination) throws ServiceException;


    @RPC
    public PageRows<AskTimedRelease> queryAskTimedRelease(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    AskTimedRelease insertAskTimedRelease(AskTimedRelease askTimedRelease) throws ServiceException;

    @RPC
    boolean modifyAskTimedRelease(UpdateExpress updateExpress, Long timeid) throws ServiceException;

    @RPC
    AskTimedRelease getAskTimedRelease(Long timeid) throws ServiceException;

    @RPC
    Content postContentWiki(Content content) throws ServiceException;

    @RPC
    Content postContent(Content content) throws ServiceException;

    @RPC
    Content getContent(String commentid) throws ServiceException;

    @RPC
    boolean updateContentStatus(String commentId, ValidStatus validStatus) throws ServiceException;

    @RPC
    ScoreRangeRows<Content> queryContentByScoreRangeRows(String linekey, ScoreRange scoreRange, Integer pnum, String profileId) throws ServiceException;

    @RPC
    PageRows<Advertise> queryAdvertise(QueryExpress queryExpress, Pagination page) throws ServiceException;

    @RPC
    Advertise postAdvertise(Advertise advertise) throws ServiceException;

    @RPC
    Advertise getAdvertise(Long id) throws ServiceException;

    @RPC
    boolean updateAdvertise(Long id, UpdateExpress updateExpress) throws ServiceException;

    @RPC
    void updateAdvertiseQuartzCronTrigger(Long id)throws ServiceException;

    @RPC
    List<Advertise> queryAdvertiseByLineKey(String linekey) throws ServiceException;

    @RPC
    boolean addContentSum(Long contentId, ContentSumField contentSumField, String profileId) throws ServiceException;

    @RPC
    boolean checkContentSum(Long contentId, ContentSumField contentSumField, String profileId) throws ServiceException;

    @RPC
    Map<Long, ContentSum> queryContentSumByids(Collection<Long> contentIds) throws ServiceException;

    @RPC
    Map<Long, Content> queryContentByids(Collection<Long> contentIds) throws ServiceException;

    @RPC
    Map<Long, Content> queryContentByUserCollect(Set<Long> contentIds) throws ServiceException;

    @RPC
    WikiGameres insertWikiGameres(WikiGameres wikiGameres) throws ServiceException;

    @RPC
    WikiGameres getWikiGameresByQueryExpress(QueryExpress queryExpress) throws ServiceException;

    @RPC
    WikiGameres getWikiGameresByGameId(long gameId) throws ServiceException;

    @RPC
    PageRows<WikiGameres> queryGamesByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    boolean modifyWikiGameres(Long id, UpdateExpress updateExpress) throws ServiceException;

    /**
     * 推荐游戏/修改推荐游戏排序
     *
     * @param id   主键ID
     * @param flag true=加入推荐 false=取消推荐
     * @return updateExpress更新条件
     * @throws ServiceException
     */
    @RPC
    boolean modifyRecommend(Long id, boolean flag, UpdateExpress updateExpress) throws ServiceException;

    /**
     * 推荐游戏
     *
     * @return
     * @throws ServiceException
     */
    @RPC
    Map<Long, WikiGameres> queryRecommendGame() throws ServiceException;

    /**
     * 通过游戏ID查询游戏信息
     *
     * @param gameIds
     * @return
     * @throws ServiceException
     */
    @RPC
    Map<Long, WikiGameres> queryWikiGameresByIds(Set<Long> gameIds) throws ServiceException;


    /**
     * 查询所有的游戏
     *
     * @return
     * @throws ServiceException
     */
    @RPC
    List<WikiGameres> queryAllWikiGameres() throws ServiceException;

    /**
     * 关注游戏接口
     *
     * @param gameIds   游戏ID  用【|】分隔
     * @param profileId 用户PID
     * @throws ServiceException
     */
    @RPC
    void batchFollow(String[] gameIds, String profileId) throws ServiceException;


    /**
     * 取消关注游戏接口
     *
     * @param gameIds
     * @param profileId
     * @return
     * @throws ServiceException
     */
    @RPC
    boolean unFollow(String[] gameIds, String profileId) throws ServiceException;

    /**
     * @param pid        用户PID
     * @param pagination
     * @param isDesc     排序 true=desc false=asc
     * @return
     * @throws ServiceException
     */
    @RPC
    PageRows<WikiGameres> queryFollowGameList(String pid, Pagination pagination, boolean isDesc) throws ServiceException;

    /**
     * 收藏文章/wiki
     *
     * @param userCollect
     * @return
     * @throws ServiceException
     */
    @RPC
    UserCollect insertUserCollect(UserCollect userCollect) throws ServiceException;

    /**
     * 批量删除收藏的文章
     *
     * @param ids 收藏的ID
     * @return
     * @throws ServiceException
     */
    @RPC
    boolean deleteUserCollect(Set<Long> ids, String profileId) throws ServiceException;


    @RPC
    PageRows<UserCollect> queryCollectByCache(String pid, Pagination pagination) throws ServiceException;

    @RPC
    UserCollect getCollect(QueryExpress queryExpress) throws ServiceException;

    @RPC
    PageRows<ContentTag> queryContentTag(QueryExpress queryExpress, Pagination page) throws ServiceException;

    @RPC
    ContentTag postContentTag(ContentTag contentTag) throws ServiceException;

    @RPC
    ContentTag getContentTag(Long id) throws ServiceException;

    @RPC
    boolean updateContentTag(Long id, UpdateExpress updateExpress) throws ServiceException;

    @RPC
    List<ContentTag> queryContentTagByTagType(ContentTagLine tagLine) throws ServiceException;

    @RPC
    List<UserFollowGame> queryFollowGame(QueryExpress queryExpress) throws ServiceException;

    /**
     * 查询用户是否被封禁 为null表示未封禁
     *
     * @param pid
     * @return
     * @throws ServiceException
     */
    @RPC
    BlackListHistory getBlackListHistory(String pid) throws ServiceException;

    @RPC
    Map<String, BlackListHistory> queryBlackHisotryById(Set<String> pids) throws ServiceException;

    /**
     * 封禁用户
     *
     * @param blackListHistory
     * @return
     * @throws ServiceException
     */
    @RPC
    BlackListHistory addBlackListHisotry(BlackListHistory blackListHistory) throws ServiceException;

    @RPC
    boolean removeBlackListByPid(String pid) throws ServiceException;

    @RPC
    PageRows<BlackListHistory> queryBlackListHistoryList(String pid, Pagination pagination) throws ServiceException;

    /**
     * 查询被封禁的PID
     *
     * @return
     * @throws ServiceException
     */
    @RPC
    Set<String> queryBlackListPid() throws ServiceException;

}
