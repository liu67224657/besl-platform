/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.content.social.*;
import com.enjoyf.platform.service.content.wall.WallLayout;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.point.ActivityHotRanks;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.NextPageRows;
import com.enjoyf.platform.util.NextPagination;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.io.Serializable;
import java.util.*;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a> ,zx
 * Create time: 11-8-21 下午4:18
 * Description:         没有UNO？？？？
 */
class ContentServiceImpl implements ContentService {
    private ReqProcessor reqProcessor = null;
    private int numOfPartitions;

    public ContentServiceImpl(ServiceConfig scfg) {
        if (scfg == null) {
            throw new RuntimeException("ContentServiceImpl.ctor: ServiceConfig is null!");
        }

        //
        reqProcessor = scfg.getReqProcessor();
        numOfPartitions = EnvConfig.get().getServicePartitionNum(ContentConstants.SERVICE_SECTION);
    }

    @Override
    public Content postContent(Content content) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(content);

        Request req = new Request(ContentConstants.CONTENT_POST, wp);
        req.setPartition(Math.abs(content.getUno().hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (Content) rp.readSerializable();
    }

    @Override
    public List<Content> queryContentByQuery(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request req = new Request(ContentConstants.QUERY_CONTENT_BY_QUERY, wp);
        RPacket rp = reqProcessor.process(req);

        return (List<Content>) rp.readSerializable();
    }




    @Override
    public PageRows<Content> queryContentsByUno(String uno, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(uno);
        wp.writeSerializable(page);

        Request req = new Request(ContentConstants.CONTENT_QUERY_BY_UNO, wp);
        req.setPartition(Math.abs(uno.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<Content>) rp.readSerializable();
    }

    @Override
    public PageRows<Content> queryContentsByDateStep(Date startDate, Date endDate, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(startDate);
        wp.writeSerializable(endDate);
        wp.writeSerializable(page);

        Request req = new Request(ContentConstants.CONTENT_QUERY_BY_DATE_STEP, wp);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<Content>) rp.readSerializable();
    }

    @Override
    public Map<String, List<Content>> queryLastestContentsByUno(Set<String> unos, Integer size) throws ServiceException {
        Map<String, List<Content>> returnValue = new HashMap<String, List<Content>>();

        ///
        Map<Integer, Set<String>> unosMap = mapUnoList(unos);

        //loop to query the contents.
        for (Map.Entry<Integer, Set<String>> entry : unosMap.entrySet()) {
            Map<String, List<Content>> list = queryContents(entry.getKey(), entry.getValue(), size);

            for (Map.Entry<String, List<Content>> entry2 : list.entrySet()) {
                returnValue.put(entry2.getKey(), entry2.getValue());
            }
        }

        return returnValue;
    }

    private Map<Integer, Set<String>> mapUnoList(Set<String> unos) {
        Map<Integer, Set<String>> returnValue = new HashMap<Integer, Set<String>>();

        for (String uno : unos) {
            int idx = Math.abs(uno.hashCode()) % numOfPartitions;

            Set<String> list = returnValue.get(idx);
            if (list == null) {
                list = new HashSet<String>();

                returnValue.put(idx, list);
            }

            list.add(uno);
        }

        return returnValue;
    }

    private Map<String, List<Content>> queryContents(Integer idx, Set<String> unos, Integer size) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable((Serializable) unos);
        wp.writeSerializable(size);

        Request req = new Request(ContentConstants.CONTENT_QUERY_BY_LASTEST_UNOS, wp);
        req.setPartition(idx);

        RPacket rp = reqProcessor.process(req);

        return (Map<String, List<Content>>) rp.readSerializable();
    }

    @Override
    public boolean removeContent(String contentId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(contentId);

        Request req = new Request(ContentConstants.CONTENT_REMOVE, wp);
        req.setPartition(Math.abs(contentId.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public boolean modifyContent(String contentId, Map<ObjectField, Object> map) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(contentId);
        wp.writeSerializable((Serializable) map);

        Request req = new Request(ContentConstants.CONTENT_MODIFY, wp);
        req.setPartition(Math.abs(contentId.hashCode()) % numOfPartitions);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public boolean modifyContentNum(String contentId, ObjectField field, Integer value) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(contentId);
        wp.writeSerializable(field);
        wp.writeSerializable(value);

        Request req = new Request(ContentConstants.CONTENT_MODIFY_NUM, wp);
        req.setPartition(Math.abs(contentId.hashCode()) % numOfPartitions);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    /**
     * 查询单条记录，根据ID，UNO
     */
    public Content getContentById(String contentId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(contentId);

        Request req = new Request(ContentConstants.CONTENT_GET_BY_CNTID, wp);
        req.setPartition(Math.abs(contentId.hashCode()) % numOfPartitions);
        RPacket rp = reqProcessor.process(req);

        return (Content) rp.readSerializable();
    }

    @Override
    public ContentRelation createContentRelation(String contentId, ContentRelation contentRelation) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(contentId);
        wp.writeSerializable(contentRelation);

        Request req = new Request(ContentConstants.CONENT_RELATION_CREATE, wp);
        req.setPartition(Math.abs(contentId.hashCode()) % numOfPartitions);
        RPacket rp = reqProcessor.process(req);

        return (ContentRelation) rp.readSerializable();
    }

    @Override
    public boolean postResourceFile(ResourceFile file) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(file);

        Request req = new Request(ContentConstants.RESOURCE_FILE_POST, wp);
        req.setPartition(Math.abs(file.getOwnUno().hashCode()) % numOfPartitions);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public Map<Integer, WallLayout> queryWallLayoutMap(Integer mapSize) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(mapSize);

        Request req = new Request(ContentConstants.WALL_CONTENT_QUERY, wp);
        RPacket rp = reqProcessor.process(req);

        return (Map<Integer, WallLayout>) rp.readSerializable();
    }

    @Override
    public boolean auditContentReply(String replyId, String uno, String contentId, Map<ObjectField, Object> map) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(replyId);
        wp.writeStringUTF(uno);
        wp.writeStringUTF(contentId);
        wp.writeSerializable((Serializable) map);

        Request req = new Request(ContentConstants.CONTENT_REPLY_MODIFY, wp);
        req.setPartition(Math.abs(contentId.hashCode()) % numOfPartitions);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public PageRows<Activity> queryActivity(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);

        Request req = new Request(ContentConstants.QUERY_ACTIVITY_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);

        return (PageRows<Activity>) rp.readSerializable();
    }

    @Override
    public Activity insertActivity(Activity activity) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(activity);

        Request req = new Request(ContentConstants.INSERT_ACTIVITY, wp);
        RPacket rp = reqProcessor.process(req);

        return (Activity) rp.readSerializable();
    }

    @Override
    public Activity getActivityById(Long activityId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(activityId);

        Request req = new Request(ContentConstants.GET_ACTIVITY_BY_ID, wp);
        RPacket rp = reqProcessor.process(req);

        return (Activity) rp.readSerializable();
    }

    @Override
    public boolean modifyActivity(UpdateExpress updateExpress, long activityId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeLongNx(activityId);
        Request req = new Request(ContentConstants.MODIFY_ACTIVITY_BY_ID, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public ActivityRelation insertActivityRelation(ActivityRelation activityRelation) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(activityRelation);

        Request req = new Request(ContentConstants.INSERT_ACTIVITY_RELATION, wp);
        RPacket rp = reqProcessor.process(req);

        return (ActivityRelation) rp.readSerializable();
    }

    @Override
    public List<ActivityRelation> listActivityRelation(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request req = new Request(ContentConstants.QUERY_ACTIVITY_RELATION_BY_LIST, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<ActivityRelation>) rp.readSerializable();
    }

    @Override
    public ActivityRelation getActivityRelation(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request req = new Request(ContentConstants.GET_ACTIVITY_RELATION, wp);
        RPacket rp = reqProcessor.process(req);
        return (ActivityRelation) rp.readSerializable();
    }

    @Override
    public List<Activity> queryHotActivity() throws ServiceException {
        WPacket wp = new WPacket();
        Request req = new Request(ContentConstants.QUERY_LASTEST_ACTIVITY, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<Activity>) rp.readSerializable();
    }

    @Override
    public Map<Long, Activity> queryActivityByActivityId(Set<Long> activityId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) activityId);
        Request req = new Request(ContentConstants.QUERY_ACTIVITY_BY_ACTIVITYIDS, wp);
        RPacket rp = reqProcessor.process(req);
        return (Map<Long, Activity>) rp.readSerializable();
    }

    @Override
    public Map<Long, Activity> queryActivityByRelations(Set<Long> relationId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) relationId);
        Request req = new Request(ContentConstants.QUERY_ACTIVITY_BY_REALTIONIDS, wp);
        RPacket rp = reqProcessor.process(req);
        return (Map<Long, Activity>) rp.readSerializable();
    }

    @Override
    public PageRows<Content> queryContentByQueryExpress(QueryExpress queryExpress, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(queryExpress);
        wp.writeSerializable(page);

        Request req = new Request(ContentConstants.QUERY_CONTENT_BY_QUERYEXPRESS, wp);
        RPacket rp = reqProcessor.process(req);

        return (PageRows<Content>) rp.readSerializable();
    }

    @Override
    public PageRows<ContentInteraction> queryContentReply(QueryExpress queryExpress, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(queryExpress);
        wp.writeSerializable(page);

        Request req = new Request(ContentConstants.QUERY_CONTENT_REPLY_BY_QUERYEXPRESS, wp);
        RPacket rp = reqProcessor.process(req);

        return (PageRows<ContentInteraction>) rp.readSerializable();
    }

    @Override
    public long queryContentReplyTimes(String contentId, Date from, Date to) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeString(contentId);
        wp.writeSerializable(from);
        wp.writeSerializable(to);

        Request req = new Request(ContentConstants.QUERY_CONTENT_REPLYTIMES, wp);
        req.setPartition(Math.abs(contentId.hashCode()) % numOfPartitions);
        RPacket rp = reqProcessor.process(req);

        return rp.readLongNx();
    }

    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(event);

        Request req = new Request(ContentConstants.RECIEVE_EVENT, wp);
        req.setPartition(Math.abs(event.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public ContentInteraction createInteraction(ContentInteraction interaction) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(interaction);

        Request req = new Request(ContentConstants.CONTENT_INTERACTION_CREATE, wp);
        req.setPartition(Math.abs(interaction.getContentId().hashCode()) % numOfPartitions);
        RPacket rp = reqProcessor.process(req);

        return (ContentInteraction) rp.readSerializable();
    }

    @Override
    public boolean removeInteraction(String interactionId, String contentId, String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(interactionId);
        wp.writeStringUTF(contentId);
        wp.writeStringUTF(uno);

        Request req = new Request(ContentConstants.CONTENT_INTERACTION_REMOVE, wp);
        req.setPartition(Math.abs(contentId.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public ContentInteraction getInteractionByIidCidUno(String interactionId, String contentId, String contentUno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(interactionId);
        wp.writeStringUTF(contentId);
        wp.writeStringUTF(contentUno);

        Request req = new Request(ContentConstants.CONTENT_REPLY_GET_BY_RID_CID_UNO, wp);
        req.setPartition(Math.abs(contentId.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (ContentInteraction) rp.readSerializable();
    }

    @Override
    public List<ContentInteraction> queryInteractionByCidIUno(String interactionUno, String contentId, InteractionType type) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(interactionUno);
        wp.writeStringUTF(contentId);
        wp.writeSerializable(type);

        Request req = new Request(ContentConstants.CONTENT_REPLY_QUERY_BY_CID_IUNO, wp);
        req.setPartition(Math.abs(contentId.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (List<ContentInteraction>) rp.readSerializable();
    }

    @Override
    public Map<String, ContentInteraction> queryInteractionsMapByRids(ContentInteractionQueryMap queryMap) throws ServiceException {
        Map<String, ContentInteraction> returnValue = new HashMap<String, ContentInteraction>();

        //loop to query the content replies.
        for (int idx : queryMap.getKeys()) {
            List<ContentInteraction> list = queryInteractionsByIids(idx, queryMap.getMapByIndex(idx));
            for (ContentInteraction r : list) {
                returnValue.put(r.getInteractionId(), r);
            }
        }

        return returnValue;
    }

    private List<ContentInteraction> queryInteractionsByIids(Integer idx, Map<String, List<String>> iids) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) iids);

        Request req = new Request(ContentConstants.CONTENT_INTERACTION_QUERY_BY_CIDRIDS_MAP, wp);
        req.setPartition(idx);

        RPacket rp = reqProcessor.process(req);

        return (List<ContentInteraction>) rp.readSerializable();
    }

    @Override
    public PageRows<ContentInteraction> queryInteractionsByCidType(String contentId, String contentUno, InteractionType interactionType, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(contentId);
        wp.writeStringUTF(contentUno);
        wp.writeSerializable(interactionType);
        wp.writeSerializable(page);

        Request req = new Request(ContentConstants.CONTENT_INTERACTION_QUERY_BY_CID_AND_TYPE, wp);
        req.setPartition(Math.abs(contentId.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<ContentInteraction>) rp.readSerializable();
    }

    @Override
    public PageRows<ContentInteraction> queryCurrentInteractionsByInteractionIdCidType(String interactionId, String contentId, String contentUno, QueryExpress queryExpress, int pageSize) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(interactionId);
        wp.writeStringUTF(contentId);
        wp.writeStringUTF(contentUno);
        wp.writeSerializable(queryExpress);
        wp.writeIntNx(pageSize);

        Request req = new Request(ContentConstants.CURRENTPAGE_CONTENT_INTERACTION_QUERY_BY_EXPRESS, wp);
        req.setPartition(Math.abs(contentId.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<ContentInteraction>) rp.readSerializable();
    }

    @Override
    public PageRows<ContentInteraction> queryCurrentChildrenInteractionsByChildrenInteractionIdCidType(String interactionId, String contentId, String contentUno, QuerySortOrder querySortOrder, int pageSize) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(interactionId);
        wp.writeStringUTF(contentId);
        wp.writeStringUTF(contentUno);
        wp.writeSerializable(querySortOrder);
        wp.writeIntNx(pageSize);

        Request req = new Request(ContentConstants.CURRENTPAGE_CONTENT_INTERACTION_CHILDREN_QUERY_BY_EXPRESS, wp);
        req.setPartition(Math.abs(contentId.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<ContentInteraction>) rp.readSerializable();
    }

    @Override
    public PageRows<ContentInteraction> queryInteractionsByCid(String contentId, String contentUno, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(contentId);
        wp.writeStringUTF(contentUno);
        wp.writeSerializable(page);

        Request req = new Request(ContentConstants.CONTENT_INTERACTION_QUERY_BY_CID, wp);
        req.setPartition(Math.abs(contentId.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<ContentInteraction>) rp.readSerializable();
    }

    @Override
    public PageRows<ContentInteraction> queryInteractionsByExpress(String contentId, String contentUno, QueryExpress queryExpress, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(contentId);
        wp.writeStringUTF(contentUno);
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(page);

        Request req = new Request(ContentConstants.CONTENT_INTERACTION_QUERY_BY_EXPRESS, wp);
        req.setPartition(Math.abs(contentId.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<ContentInteraction>) rp.readSerializable();
    }
    @Override
    public ForignContent getForignContent(String fid, String url, String title, String content, ForignContentDomain domain, String keyWords) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(fid);
        wp.writeStringUTF(url);
        wp.writeStringUTF(title);
        wp.writeStringUTF(content);
        wp.writeSerializable(domain);
        wp.writeStringUTF(keyWords);

        Request req = new Request(ContentConstants.GET_FORIGNCONTENT_BY_URL, wp);
        RPacket rp = reqProcessor.process(req);

        return (ForignContent) rp.readSerializable();
    }

    @Override
    public ForignContentReply postForignReply(ForignContentReply forignContentReply) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(forignContentReply);

        Request req = new Request(ContentConstants.POST_FORIGNREPLY, wp);
        RPacket rp = reqProcessor.process(req);

        return (ForignContentReply) rp.readSerializable();
    }

    @Override
    public boolean modifyForignReplyById(UpdateExpress updateExpress, long replyId, ForignContentReplyLog replyLog) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeLongNx(replyId);
        wp.writeSerializable(replyLog);

        Request req = new Request(ContentConstants.MODIFY_FORIGNREPLY_BY_REPLYID, wp);
        RPacket rp = reqProcessor.process(req);

        return (boolean) rp.readBooleanNx();
    }

    @Override
    public ForignContent getForignContentById(long contentId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(contentId);

        Request req = new Request(ContentConstants.GET_FORIGNCONTENT_BY_ID, wp);
        RPacket rp = reqProcessor.process(req);

        return (ForignContent) rp.readSerializable();
    }

    @Override
    public boolean updateForignContent(UpdateExpress updateExpress, QueryExpress queryExpress, long contentId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeSerializable(queryExpress);
        wp.writeLongNx(contentId);

        Request req = new Request(ContentConstants.UPDATE_FORIGN_CONTENT, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public PageRows<ForignContentReply> queryForignReplays(long contentId, long rootId, Pagination pagination, boolean desc) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(contentId);
        wp.writeLongNx(rootId);
        wp.writeSerializable(pagination);
        wp.writeBooleanNx(desc);

        Request req = new Request(ContentConstants.GET_FORIGNREPLYLIST_BY_CONETNETID_ROOTID, wp);
        RPacket rp = reqProcessor.process(req);

        return (PageRows<ForignContentReply>) rp.readSerializable();
    }

    @Override
    public List<ForignContentReply> queryHotForignReply(long contentId, int size) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(contentId);
        wp.writeIntNx(size);

        Request req = new Request(ContentConstants.QUERY_HOTFROIGNHOTREPLY_BYCONTENTID, wp);
        RPacket rp = reqProcessor.process(req);

        return (List<ForignContentReply>) rp.readSerializable();
    }

    @Override
    public List<ForignContentReply> queryMobileGameGagForignReply(long contentId, int size) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(contentId);
        wp.writeIntNx(size);

        Request req = new Request(ContentConstants.QUERY_MOBILE_GAME_GAG_FORIGNREPLY, wp);
        RPacket rp = reqProcessor.process(req);

        return (List<ForignContentReply>) rp.readSerializable();
    }

    @Override
    public boolean removeForignReply(long replyId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(replyId);

        Request req = new Request(ContentConstants.REMOVE_FORIGNCONTENT_REPLY, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public PageRows<ForignContentReply> queryForignContentReplyByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);

        Request req = new Request(ContentConstants.QUERY_FORIGN_CONTENT_REPLY_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<ForignContentReply>) rp.readSerializable();
    }

    @Override
    public boolean modifyForignContentReply(Long replyId, UpdateExpress updateExpress, Long contentid, ForignContentDomain forignContentDomain) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(replyId);
        wp.writeSerializable(updateExpress);
        wp.writeLongNx(contentid);
        wp.writeSerializable(forignContentDomain);

        Request req = new Request(ContentConstants.MODIFY_FORIGN_CONTENT_REPLY, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public ForignContentReply getForignContentReply(Long replyId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(replyId);

        Request req = new Request(ContentConstants.GET_FORIGN_CONTENT_REPLY, wp);
        RPacket rp = reqProcessor.process(req);
        return (ForignContentReply) rp.readSerializable();
    }

    @Override
    public List<ForignContentReply> queryForignContentReply(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request req = new Request(ContentConstants.QUERY_FORIGN_CONTENT_REPLY, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<ForignContentReply>) rp.readSerializable();
    }

    @Override
    public String getRightHtmlByArticleId(ForignContent forignContent) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(forignContent);

        Request req = new Request(ContentConstants.GET_CMSRIGHT_HTML, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readStringUTF();
    }

    @Override
    public ForignContentReplyLog createForignContentReplyLog(ForignContentReplyLog replyLog) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(replyLog);

        Request req = new Request(ContentConstants.CREATE_FORIGN_CONTENT_REPLY_LOG, wp);
        RPacket rp = reqProcessor.process(req);
        return (ForignContentReplyLog) rp.readSerializable();
    }

    @Override
    public ForignContentReplyLog getForignContentReplyLog(long replyId, String uno, int logType) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(replyId);
        wp.writeStringUTF(uno);
        wp.writeIntNx(logType);

        Request req = new Request(ContentConstants.GET_FORIGN_CONTENT_REPLY_LOG, wp);
        RPacket rp = reqProcessor.process(req);
        return (ForignContentReplyLog) rp.readSerializable();
    }

    @Override
    public int agreeForignCotnentReply(long replyId, String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(replyId);
        wp.writeStringUTF(uno);

        Request req = new Request(ContentConstants.AGREE_FORIGN_CONTENT_REPLY, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readIntNx();
    }


    @Override
    public Map<Long, ForignContentReply> queryForignContentReplyBySet(Set<Long> replyIdSet) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) replyIdSet);

        Request req = new Request(ContentConstants.QUERY_FORIGN_CONTENTREPLY_BY_SET, wp);
        RPacket rp = reqProcessor.process(req);
        return (Map<Long, ForignContentReply>) rp.readSerializable();
    }

    @Override
    public Map<Long, ForignContent> getForignContentBySet(Set<Long> contentIds) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) contentIds);

        Request req = new Request(ContentConstants.GET_FORIGNCONTENT_BY_SET, wp);
        RPacket rp = reqProcessor.process(req);
        return (Map<Long, ForignContent>) rp.readSerializable();
    }

    @Override
    public ForignContent getForignContentByFidCdomain(String forignId, ForignContentDomain byCode) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(forignId);
        wp.writeSerializable(byCode);

        Request req = new Request(ContentConstants.GET_FORIGN_CONTENT_BY_FID_CDOMAIN, wp);
        RPacket rp = reqProcessor.process(req);
        return (ForignContent) rp.readSerializable();
    }

    @Override
    public SocialContent postSocialContent(SocialContent socialContent) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(socialContent);
        Request req = new Request(ContentConstants.POST_SOCIALCONTENT, wp);
        req.setPartition(Math.abs(socialContent.getUno().hashCode()) % numOfPartitions);
        RPacket rp = reqProcessor.process(req);
        return (SocialContent) rp.readSerializable();
    }

    @Override
    public SocialContent getSocialContentByContentId(long contentId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(contentId);

        Request req = new Request(ContentConstants.GET_SOCIALCONTENT_BYCONTENTID, wp);
        req.setPartition((int) (contentId % numOfPartitions));
        RPacket rp = reqProcessor.process(req);
        return (SocialContent) rp.readSerializable();
    }


    @Override
    public boolean removeSocialContent(String uno, long contentId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        wp.writeLongNx(contentId);

        Request req = new Request(ContentConstants.REMOVE_SOCIALCONTENT, wp);
        req.setPartition((int) (contentId % numOfPartitions));
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public boolean modifySocialContent(UpdateExpress updateExpress, long contentId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeLongNx(contentId);

        Request req = new Request(ContentConstants.MODIFY_SOCIALCONTENT, wp);
        req.setPartition((int) (contentId % numOfPartitions));
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public PageRows<SocialContent> querySocialContentByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(ContentConstants.QUERY_SOCIALCONTENT_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<SocialContent>) rp.readSerializable();
    }

    @Override
    public SocialContentReply postSocialContentReply(SocialContentReply reply) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(reply);

        Request req = new Request(ContentConstants.POST_SOCIALCONTENT_REPLY, wp);
        req.setPartition((int) (reply.getContentId() % numOfPartitions));
        RPacket rp = reqProcessor.process(req);
        return (SocialContentReply) rp.readSerializable();
    }

    @Override
    public boolean removeSocialContentReply(long contentId, long replyId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(contentId);
        wp.writeLongNx(replyId);

        Request req = new Request(ContentConstants.REMOVE_SOCIALCONTENT_REPLY, wp);
        req.setPartition((int) (contentId % numOfPartitions));
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public boolean recoverSocialContentReply(long contentId, long replyId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(contentId);
        wp.writeLongNx(replyId);

        Request req = new Request(ContentConstants.RECOVER_SOCIALCONTENTR_EPLY, wp);
        req.setPartition((int) (contentId % numOfPartitions));
        RPacket rp = reqProcessor.process(req);

        return (Boolean) rp.readSerializable();
    }

    @Override
    public PageRows<SocialContentReply> querySocialContentReply(long contentId, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(contentId);
        wp.writeSerializable(page);

        Request req = new Request(ContentConstants.QUERY_SOCIALCONTENT_REPLY, wp);
        req.setPartition((int) (contentId % numOfPartitions));
        RPacket rp = reqProcessor.process(req);
        return (PageRows<SocialContentReply>) rp.readSerializable();
    }

    @Override
    public PageRows<SocialContentReply> querySocialContentReplyByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(ContentConstants.QUERY_SOCIALCONTENT_REPLY_BYPAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<SocialContentReply>) rp.readSerializable();
    }

    @Override
    public SocialContentAction createSocialContentAction(SocialContentAction action) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(action);

        Request req = new Request(ContentConstants.CREATE_SOCIALCONTENT_ACTION, wp);
        req.setPartition((int) (action.getContentId() % numOfPartitions));
        RPacket rp = reqProcessor.process(req);
        return (SocialContentAction) rp.readSerializable();
    }

    @Override
    public boolean removeSocialContentAction(long contentId, String uno, SocialContentActionType socialContentActionType) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(contentId);
        wp.writeStringUTF(uno);
        wp.writeSerializable(socialContentActionType);
        Request req = new Request(ContentConstants.REMOVE_SOCIALCONTENT_ACTION, wp);
        req.setPartition((int) (contentId % numOfPartitions));
        RPacket rp = reqProcessor.process(req);
        return (Boolean) rp.readSerializable();
    }

    @Override
    public NextPageRows<SocialContentAction> querySocialContentAction(long contentId, SocialContentActionType type, NextPagination page) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(contentId);
        wp.writeSerializable(type);
        wp.writeSerializable(page);
        Request req = new Request(ContentConstants.QUERY_SOCIALCONTENT_ACTION, wp);
        req.setPartition((int) (contentId % numOfPartitions));
        RPacket rp = reqProcessor.process(req);
        return (NextPageRows<SocialContentAction>) rp.readSerializable();
    }

    @Override
    public SocialHotContent insertSocialHotContent(SocialHotContent socialHotContent) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(socialHotContent);
        Request req = new Request(ContentConstants.INSERT_SOCIAL_HOT_CONTENT, wp);
        RPacket rp = reqProcessor.process(req);

        return (SocialHotContent) rp.readSerializable();
    }


    @Override
    public SocialHotContent getSocialHotContent(long socialContentId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(socialContentId);
        Request req = new Request(ContentConstants.GET_SOCIAL_HOT_CONTENT, wp);
        RPacket rp = reqProcessor.process(req);
        return (SocialHotContent) rp.readSerializable();
    }

    @Override
    public NextPageRows<SocialHotContent> querySocialHotContentByPage(NextPagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(pagination);
        Request req = new Request(ContentConstants.QUERY_SOCIAL_HOT_CONTENT_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (NextPageRows<SocialHotContent>) rp.readSerializable();
    }

    @Override
    public boolean modifySocialHotContent(UpdateExpress updateExpress, long socialContentId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeLongNx(socialContentId);
        Request req = new Request(ContentConstants.MODIFY_SOCIAL_HOT_CONTENT, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public Set<Long> checkSocialContentAction(String uno, SocialContentActionType actionType, Set<Long> contentIdSet) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        wp.writeSerializable(actionType);
        wp.writeSerializable((Serializable) contentIdSet);
        Request req = new Request(ContentConstants.CHECK_SOCIAL_CONTENT_ACTION, wp);
        RPacket rp = reqProcessor.process(req);
        return (Set<Long>) rp.readSerializable();
    }

    @Override
    public List<SocialContent> querySocialContentByIdSet(Set<Long> contentIdSet) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) contentIdSet);
        Request req = new Request(ContentConstants.QUERY_SOCIAL_CONTENT_BY_IDSET, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<SocialContent>) rp.readSerializable();
    }

    @Override
    public Map<Long, SocialContent> querySocialContentMapByIdSet(Set<Long> contentIdSet) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) contentIdSet);
        Request req = new Request(ContentConstants.QUERY_SOCIAL_CONTENT_MAP_BY_IDSET, wp);
        RPacket rp = reqProcessor.process(req);
        return (Map<Long, SocialContent>) rp.readSerializable();
    }

    @Override
    public PageRows<SocialReport> querySocialReportByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(ContentConstants.QUERY_SOCIALREPORT_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<SocialReport>) rp.readSerializable();
    }

    @Override
    public SocialReport insertSocialReport(SocialReport socialReport) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(socialReport);
        Request req = new Request(ContentConstants.CREATE_SOCIALREPORT, wp);
        RPacket rp = reqProcessor.process(req);
        return (SocialReport) rp.readSerializable();
    }

    @Override
    public boolean modifySocialReport(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeSerializable(queryExpress);
        Request req = new Request(ContentConstants.MODIFY_SOCIALREPORT, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public SocialReport getSocialReport(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(ContentConstants.GET_SOCIALREPORTY, wp);
        RPacket rp = reqProcessor.process(req);
        return (SocialReport) rp.readSerializable();
    }

    @Override
    public boolean insertSocialContentPlay(List<SocialContentPlay> socialContentPlayList) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) socialContentPlayList);
        Request req = new Request(ContentConstants.INSERT_SOCIALCONTENT_PLAY, wp);
        RPacket rp = reqProcessor.process(req);
        return (Boolean) rp.readSerializable();
    }

    @Override
    public SocialContent getSocialContent(long cid) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(cid);
        Request req = new Request(ContentConstants.GET_SOCIALCONTENT_BY_ID, wp);
        RPacket rp = reqProcessor.process(req);
        return (SocialContent) rp.readSerializable();
    }

    @Override
    public List<SocialHotContent> querySocialHotContent(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(ContentConstants.QUERY_SOCIALHOTCONTENT_LIST, wp);
        RPacket rp = reqProcessor.process(req);

        return (List<SocialHotContent>) rp.readSerializable();
    }

    @Override
    public PageRows<SocialHotContent> querySocialHotContentPageRows(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(ContentConstants.QUERY_SOCIALHOTCONTENT_PAGEROWS, wp);
        RPacket rp = reqProcessor.process(req);

        return (PageRows<SocialHotContent>) rp.readSerializable();
    }


    @Override
    public SocialContentAction getSocialContentAction(Long contentId, String actionUno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(contentId);
        wp.writeStringUTF(actionUno);
        Request req = new Request(ContentConstants.GET_SOCIAL_CONTENT_ACTION, wp);
        RPacket rp = reqProcessor.process(req);
        return (SocialContentAction) rp.readSerializable();
    }

    @Override
    public SocialActivity insertSocialActivity(SocialActivity socialActivity) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(socialActivity);
        Request req = new Request(ContentConstants.INSERT_SOCIAL_ACTIVITY, wp);
        RPacket rp = reqProcessor.process(req);
        return (SocialActivity) rp.readSerializable();
    }

    @Override
    public SocialActivity getSocialActivity(long activityId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(activityId);
        Request req = new Request(ContentConstants.GET_SOCIAL_ACTIVITY, wp);
        RPacket rp = reqProcessor.process(req);
        return (SocialActivity) rp.readSerializable();
    }

    @Override
    public List<SocialActivity> querySocialActivity(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(ContentConstants.QUERY_SOCIAL_ACTIVITY, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<SocialActivity>) rp.readSerializable();
    }

    @Override
    public PageRows<SocialActivity> querySocialActivityByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(ContentConstants.QUERY_SOCIAL_ACTIVITY_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<SocialActivity>) rp.readSerializable();
    }

    @Override
    public boolean modifySocialActivity(long activityId, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(activityId);
        wp.writeSerializable(updateExpress);
        Request req = new Request(ContentConstants.MODIFY_SOCIAL_ACTIVITY, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public SocialContentActivity getSocialContentActivity(long activityId, long contentId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(activityId);
        wp.writeLongNx(contentId);
        Request req = new Request(ContentConstants.GET_SOCIAL_CONTENT_ACTIVITY, wp);
        RPacket rp = reqProcessor.process(req);
        return (SocialContentActivity) rp.readSerializable();
    }

    @Override
    public PageRows<SocialContentActivity> querySocialContentActivity(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(ContentConstants.QUERY_SOCIAL_CONTENT_ACTIVITY_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<SocialContentActivity>) rp.readSerializable();
    }

    @Override
    public PageRows<SocialTag> querySocialTagByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(ContentConstants.QUERY_SOCIAL_TAG_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<SocialTag>) rp.readSerializable();
    }

    @Override
    public PageRows<SocialWatermark> querySocialWatermarkByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(ContentConstants.QUERY_SOCIAL_WATERMARK_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<SocialWatermark>) rp.readSerializable();
    }

    @Override
    public SocialWatermark insertSocialWatermark(SocialWatermark watermark) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(watermark);
        Request req = new Request(ContentConstants.INSERT_SOCIAL_WATERMARK, wp);
        RPacket rp = reqProcessor.process(req);
        return (SocialWatermark) rp.readSerializable();
    }

    @Override
    public SocialWatermark getSocialWatermark(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(ContentConstants.GET_SOCIAL_WATERMARK, wp);
        RPacket rp = reqProcessor.process(req);
        return (SocialWatermark) rp.readSerializable();
    }

    @Override
    public boolean modifySocialWatermark(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(updateExpress);
        Request req = new Request(ContentConstants.MODIFY_SOCIAL_WATERMARK, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public NextPageRows<SocialWatermark> querySocialWatermarkByNext(NextPagination nextPagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(nextPagination);
        Request req = new Request(ContentConstants.QUERY_SOCIAL_WATERMARK_BY_NEXT, wp);
        RPacket rp = reqProcessor.process(req);
        return (NextPageRows<SocialWatermark>) rp.readSerializable();
    }

    @Override
    public NextPageRows<SocialActivity> querySocialActivityByNext(NextPagination nextPagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(nextPagination);
        Request req = new Request(ContentConstants.QUERY_SOCIAL_ACTIVITY_BY_NEXT, wp);
        RPacket rp = reqProcessor.process(req);
        return (NextPageRows<SocialActivity>) rp.readSerializable();
    }


    @Override
    public PageRows<SocialBackgroundAudio> querySocialBgAudio(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(ContentConstants.QUERY_SOCIAL_BGAUDIO_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<SocialBackgroundAudio>) rp.readSerializable();
    }

    @Override
    public SocialBackgroundAudio insertSocialBgAudio(SocialBackgroundAudio bgAudio) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(bgAudio);
        Request req = new Request(ContentConstants.INSERT_SOCIAL_BGAUDIO, wp);
        RPacket rp = reqProcessor.process(req);
        return (SocialBackgroundAudio) rp.readSerializable();
    }

    @Override
    public SocialBackgroundAudio getSocialBgAudio(long audioId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(audioId);
        Request req = new Request(ContentConstants.GET_SOCIAL_BGAUDIO, wp);
        RPacket rp = reqProcessor.process(req);
        return (SocialBackgroundAudio) rp.readSerializable();
    }

    @Override
    public boolean modifySocialBgAudio(long audioId, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(audioId);
        wp.writeSerializable(updateExpress);
        Request req = new Request(ContentConstants.MODIFY_SOCIAL_BGAUDIO, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public NextPageRows<SocialBackgroundAudio> querySocialBgAudioByNext(NextPagination nextPagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(nextPagination);
        Request req = new Request(ContentConstants.QUERY_SOCIAL_BGAUDIO_BY_NEXT, wp);
        RPacket rp = reqProcessor.process(req);
        return (NextPageRows<SocialBackgroundAudio>) rp.readSerializable();
    }

    @Override
    public NextPageRows<SocialContentActivity> querySocialContentActivityByNext(long activityId, NextPagination nextPagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(activityId);
        wp.writeSerializable(nextPagination);
        Request req = new Request(ContentConstants.QUERY_SOCIAL_CONTENT_ACTIVITY_BY_NEXT, wp);
        RPacket rp = reqProcessor.process(req);
        return (NextPageRows<SocialContentActivity>) rp.readSerializable();
    }

    @Override
    public Map<Long, SocialActivity> querySocialActivityByIdSet(Set<Long> activityIdSet) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) activityIdSet);
        Request req = new Request(ContentConstants.QUERY_SOCIAL_ACTIVITY_BY_IDSET, wp);
        RPacket rp = reqProcessor.process(req);
        return (Map<Long, SocialActivity>) rp.readSerializable();
    }

    @Override
    public boolean modifySocialContentActivity(long activityId, long contentId, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(activityId);
        wp.writeLongNx(contentId);
        wp.writeSerializable(updateExpress);
        Request req = new Request(ContentConstants.MODIFY_SOCIAL_CONTENT_ACTIVITY, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public boolean sendOutSocialLogEvent(long foreignId, String uno, long contentId, SocialLogType logType, SocialLogCategory logCategory, int increaseValue, int platform, String channel) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(foreignId);
        wp.writeStringUTF(uno);
        wp.writeLongNx(contentId);
        wp.writeSerializable(logType);
        wp.writeSerializable(logCategory);
        wp.writeIntNx(increaseValue);
        wp.writeIntNx(platform);
        wp.writeStringUTF(channel == null ? "" : channel);
        Request req = new Request(ContentConstants.SEND_OUT_SOCIAL_LOG_EVENT, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public ForignContent getWikiContent(String wikiId, String wikiKey) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(wikiId);
        wp.writeStringUTF(wikiKey);
        Request req = new Request(ContentConstants.GET_WIKI_CONTENT, wp);
        RPacket rp = reqProcessor.process(req);
        return (ForignContent) rp.readSerializable();
    }

    @Override
    public List<Activity> queryActivityByList(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(ContentConstants.QUERY_ACTIVITY_BY_LIST, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<Activity>) rp.readSerializable();
    }
}
