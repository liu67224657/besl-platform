package com.enjoyf.platform.service.content;

import com.enjoyf.platform.service.content.social.*;
import com.enjoyf.platform.service.content.wall.WallLayout;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.NextPageRows;
import com.enjoyf.platform.util.NextPagination;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ContentService extends EventReceiver {

	/**
	 * post a content.
	 */
	public Content postContent(Content content) throws ServiceException;

	public List<Content> queryContentByQuery(QueryExpress queryExpress) throws ServiceException;

	//query someone's content.
	public PageRows<Content> queryContentsByUno(String uno, Pagination page) throws ServiceException;

	//query content by date step
	public PageRows<Content> queryContentsByDateStep(Date startDate, Date endDate, Pagination page) throws ServiceException;

	//query someone's content.
	public Map<String, List<Content>> queryLastestContentsByUno(Set<String> uno, Integer size) throws ServiceException;

	/**
	 * remove content
	 */
	public boolean removeContent(String contentId) throws ServiceException;

	/**
	 * modify content
	 */
	public boolean modifyContent(String contentId, Map<ObjectField, Object> map) throws ServiceException;

	/**
	 * 更新内容中的数，收藏数、转发数、回复数
	 */
	public boolean modifyContentNum(String contentId, ObjectField field, Integer value) throws ServiceException;

	/**
	 * 查询单条记录，根据ID，UNO
	 */
	public Content getContentById(String contentId) throws ServiceException;


	public ContentRelation createContentRelation(String contentId, ContentRelation contentRelation) throws ServiceException;


	///////////////////////////////////////////////////
	//The resource file apis.
	///////////////////////////////////////////////////
	//post resource file.
	public boolean postResourceFile(ResourceFile file) throws ServiceException;

	///////////////////////////////////////////////////////////////
	public Map<Integer, WallLayout> queryWallLayoutMap(Integer mapSize) throws ServiceException;

	///////////////////////////////////////////////////////////////

	///////////////////////////////////////////////////
	//The tools content apis.
	///////////////////////////////////////////////////
	public PageRows<Content> queryContentByQueryExpress(QueryExpress queryExpress, Pagination page) throws ServiceException;

	///////////////////////////////////////////////////
	//The content reply apis.
	///////////////////////////////////////////////////

	/**
	 * 创建一条ContentIneraction
	 *
	 * @param interaction
	 * @return
	 * @throws ServiceException
	 */
	public ContentInteraction createInteraction(ContentInteraction interaction) throws ServiceException;

	/**
	 * 删除一条ContentIneraction
	 *
	 * @param interactionId contentId  uno
	 * @return
	 * @throws ServiceException
	 */
	public boolean removeInteraction(String interactionId, String contentId, String uno) throws ServiceException;

	//查询一条回应，根据主键
	public ContentInteraction getInteractionByIidCidUno(String interactionId, String contentId, String contentUno) throws ServiceException;

	public List<ContentInteraction> queryInteractionByCidIUno(String interactionUno, String contentId, InteractionType type) throws ServiceException;

	//query replies by the reply ids
	public Map<String, ContentInteraction> queryInteractionsMapByRids(ContentInteractionQueryMap queryMap) throws ServiceException;

	//query the replys by the content id order by reply date desc.
	public PageRows<ContentInteraction> queryInteractionsByCidType(String contentId, String contentUno, InteractionType interactionType, Pagination page) throws ServiceException;

	public PageRows<ContentInteraction> queryCurrentInteractionsByInteractionIdCidType(String interactionId, String contentId, String contentUno, QueryExpress queryExpress, int pageSize) throws ServiceException;

	public PageRows<ContentInteraction> queryCurrentChildrenInteractionsByChildrenInteractionIdCidType(String interactionId, String contentId, String contentUno, QuerySortOrder querySortOrder, int pageSize) throws ServiceException;

	public PageRows<ContentInteraction> queryInteractionsByCid(String contentId, String contentUno, Pagination page) throws ServiceException;

	public PageRows<ContentInteraction> queryInteractionsByExpress(String contentId, String contentUno, QueryExpress queryExpress, Pagination page) throws ServiceException;

	public PageRows<ContentInteraction> queryContentReply(QueryExpress queryExpress, Pagination page) throws ServiceException;

	public long queryContentReplyTimes(String contentId, Date from, Date to) throws ServiceException;

	public boolean auditContentReply(String replyId, String uno, String contentId, Map<ObjectField, Object> map) throws ServiceException;

	public List<Activity> queryActivityByList(QueryExpress queryExpress) throws ServiceException;

	public PageRows<Activity> queryActivity(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

	public Activity insertActivity(Activity activity) throws ServiceException;

	public Activity getActivityById(Long activityId) throws ServiceException;

	public boolean modifyActivity(UpdateExpress updateExpress, long activityId) throws ServiceException;

	public ActivityRelation insertActivityRelation(ActivityRelation activityRelation) throws ServiceException;

	public List<ActivityRelation> listActivityRelation(QueryExpress queryExpress) throws ServiceException;

	public ActivityRelation getActivityRelation(QueryExpress queryExpress) throws ServiceException;

	public List<Activity> queryHotActivity() throws ServiceException;

	public Map<Long, Activity> queryActivityByActivityId(Set<Long> activityId) throws ServiceException;

	public Map<Long, Activity> queryActivityByRelations(Set<Long> relationId) throws ServiceException;


	/**
	 * 通过URL得到 文章
	 *
	 * @param fid
	 * @param keyWords
	 * @return
	 * @throws ServiceException
	 */
	public ForignContent getForignContent(String fid, String url, String title, String content, ForignContentDomain domain, String keyWords) throws ServiceException;

	public ForignContent getForignContentById(long contentId) throws ServiceException;

	public boolean updateForignContent(UpdateExpress updateExpress, QueryExpress queryExpress, long contentId) throws ServiceException;


	/**
	 * 发送评论
	 *
	 * @param forignContentReply
	 * @return
	 * @throws ServiceException
	 */
	public ForignContentReply postForignReply(ForignContentReply forignContentReply) throws ServiceException;

	/**
	 * @param updateExpress
	 * @param replyId
	 * @return
	 * @throws ServiceException
	 */
	public boolean modifyForignReplyById(UpdateExpress updateExpress, long replyId, ForignContentReplyLog replyLog) throws ServiceException;

	/**
	 * @param contentId
	 * @param rootId
	 * @param pagination
	 * @return
	 * @throws ServiceException
	 */
	public PageRows<ForignContentReply> queryForignReplays(long contentId, long rootId, Pagination pagination, boolean desc) throws ServiceException;

	/**
	 * @param contentId
	 * @return
	 * @throws ServiceException
	 */
	public List<ForignContentReply> queryHotForignReply(long contentId, int size) throws ServiceException;

	public List<ForignContentReply> queryMobileGameGagForignReply(long contentId, int size) throws ServiceException;

	/**
	 * 删除评论
	 */
	public boolean removeForignReply(long replyId) throws ServiceException;


	public PageRows<ForignContentReply> queryForignContentReplyByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

	public boolean modifyForignContentReply(Long replyId, UpdateExpress updateExpress, Long contentid, ForignContentDomain forignContentDomain) throws ServiceException;

	public ForignContentReply getForignContentReply(Long replyId) throws ServiceException;

	public List<ForignContentReply> queryForignContentReply(QueryExpress add) throws ServiceException;

	public String getRightHtmlByArticleId(ForignContent forignContent) throws ServiceException;

	public ForignContentReplyLog createForignContentReplyLog(ForignContentReplyLog replyLog) throws ServiceException;

	public ForignContentReplyLog getForignContentReplyLog(long replyId, String uno, int logType) throws ServiceException;

	public int agreeForignCotnentReply(long reply, String uno) throws ServiceException;

	public Map<Long, ForignContentReply> queryForignContentReplyBySet(Set<Long> replyIdSet) throws ServiceException;

	public Map<Long, ForignContent> getForignContentBySet(Set<Long> contentIds) throws ServiceException;

	public ForignContent getForignContentByFidCdomain(String forignId, ForignContentDomain byCode) throws ServiceException;

	////////////////////////////////////////////
	public SocialContent postSocialContent(SocialContent socialContent) throws ServiceException;

	public SocialContent getSocialContentByContentId(long contentId) throws ServiceException;

	public boolean removeSocialContent(String uno, long contentId) throws ServiceException;

	public boolean modifySocialContent(UpdateExpress updateExpress, long contentId) throws ServiceException;

	public PageRows<SocialContent> querySocialContentByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

	public SocialContentReply postSocialContentReply(SocialContentReply reply) throws ServiceException;

	public boolean removeSocialContentReply(long contentId, long replyId) throws ServiceException;

	public boolean recoverSocialContentReply(long contentId, long replyId) throws ServiceException;

	public PageRows<SocialContentReply> querySocialContentReply(long contentId, Pagination page) throws ServiceException;

	public PageRows<SocialContentReply> querySocialContentReplyByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

	public SocialContentAction createSocialContentAction(SocialContentAction action) throws ServiceException;

	public boolean removeSocialContentAction(long contentId, String uno, SocialContentActionType socialContentActionType) throws ServiceException;

	public NextPageRows<SocialContentAction> querySocialContentAction(long contentId, SocialContentActionType type, NextPagination page) throws ServiceException;

	public SocialHotContent insertSocialHotContent(SocialHotContent socialHotContent) throws ServiceException;

	public List<SocialHotContent> querySocialHotContent(QueryExpress queryExpress) throws ServiceException;

	public SocialHotContent getSocialHotContent(long socialContentId) throws ServiceException;

	public NextPageRows<SocialHotContent> querySocialHotContentByPage(NextPagination pagination) throws ServiceException;

	public boolean modifySocialHotContent(UpdateExpress updateExpress, long socialContentId) throws ServiceException;

	public Set<Long> checkSocialContentAction(String uno, SocialContentActionType agree, Set<Long> contentIdSet) throws ServiceException;

	public List<SocialContent> querySocialContentByIdSet(Set<Long> contentIdSet) throws ServiceException;

	public Map<Long, SocialContent> querySocialContentMapByIdSet(Set<Long> contentIdSet) throws ServiceException;

	public PageRows<SocialReport> querySocialReportByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

	public SocialReport insertSocialReport(SocialReport socialReport) throws ServiceException;

	public boolean modifySocialReport(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

	public SocialReport getSocialReport(QueryExpress queryExpress) throws ServiceException;

	public boolean insertSocialContentPlay(List<SocialContentPlay> socialContentPlayList) throws ServiceException;


	public SocialContent getSocialContent(long cid) throws ServiceException;

	public PageRows<SocialHotContent> querySocialHotContentPageRows(QueryExpress queryExpress, Pagination pagination) throws ServiceException;


	public SocialContentAction getSocialContentAction(Long contentId, String actionUno) throws ServiceException;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public SocialActivity insertSocialActivity(SocialActivity socialActivity) throws ServiceException;

	public SocialActivity getSocialActivity(long activityId) throws ServiceException;

	public List<SocialActivity> querySocialActivity(QueryExpress queryExpress) throws ServiceException;

	public PageRows<SocialActivity> querySocialActivityByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

	public boolean modifySocialActivity(long activityId, UpdateExpress updateExpress) throws ServiceException;

	public SocialContentActivity getSocialContentActivity(long activityId, long contentId) throws ServiceException;

	public PageRows<SocialContentActivity> querySocialContentActivity(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

	public PageRows<SocialTag> querySocialTagByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

	public PageRows<SocialWatermark> querySocialWatermarkByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

	public SocialWatermark insertSocialWatermark(SocialWatermark watermark) throws ServiceException;

	public SocialWatermark getSocialWatermark(QueryExpress queryExpress) throws ServiceException;

	public boolean modifySocialWatermark(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException;

	public NextPageRows<SocialWatermark> querySocialWatermarkByNext(NextPagination nextPagination) throws ServiceException;

	public NextPageRows<SocialActivity> querySocialActivityByNext(NextPagination nextPagination) throws ServiceException;

	public PageRows<SocialBackgroundAudio> querySocialBgAudio(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

	public SocialBackgroundAudio insertSocialBgAudio(SocialBackgroundAudio bgAudio) throws ServiceException;

	public SocialBackgroundAudio getSocialBgAudio(long audioId) throws ServiceException;

	public boolean modifySocialBgAudio(long audioId, UpdateExpress updateExpress) throws ServiceException;

	public NextPageRows<SocialBackgroundAudio> querySocialBgAudioByNext(NextPagination nextPagination) throws ServiceException;

	public NextPageRows<SocialContentActivity> querySocialContentActivityByNext(long activityId, NextPagination nextPagination) throws ServiceException;

	public Map<Long, SocialActivity> querySocialActivityByIdSet(Set<Long> activityIdSet) throws ServiceException;

	public boolean modifySocialContentActivity(long activityId, long contentId, UpdateExpress updateExpress) throws ServiceException;

	public boolean sendOutSocialLogEvent(long activityId, String uno, long contentId, SocialLogType logType, SocialLogCategory logCategory, int increaseValue, int platform, String channel) throws ServiceException;

    public ForignContent getWikiContent(String wikiId, String wikiKey) throws ServiceException;
}
