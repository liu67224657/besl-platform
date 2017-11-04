/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.content;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.content.ContentConstants;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */

/**
 * This interface receives packets from the remote clients. It
 * translates them into method calls into the business logic.
 * <p/>
 * The logicProcess() method may be called reentrantly, so it and any
 * handlers must be thread-safe.
 */
class ContentPacketDecoder extends PacketDecoder {
    //
    private ContentLogic processLogic;

    /**
     * Constructor takes the UserPropsLogic object that we're going to use
     * to logicProcess the packets.
     *
     * @param logic our logical friend
     */
    ContentPacketDecoder(ContentLogic logic) {
        processLogic = logic;

        setTransContainer(ContentConstants.getTransContainer());
    }

    /**
     * Called when ThreadSampleInfo packet arrives. This routine will
     * just forward the call to the logic object which
     * will take care of actually decoding the packet.
     */
    protected WPacket logicProcess(ConnThreadBase conn, RPacket rPacket) throws ServiceException {
        byte type = rPacket.getType();

        WPacket wp = new WPacket();
        wp.writeByteNx(ServiceConstants.OK);

//        switch (type) {
            //
//            case ContentConstants.CONTENT_POST:
//                wp.writeSerializable(processLogic.postContent((Content) rPacket.readSerializable()));
//                break;
//            case ContentConstants.CONTENT_MODIFY:
//                wp.writeBooleanNx(processLogic.modifyContent(rPacket.readStringUTF(), ((Map<ObjectField, Object>) rPacket.readSerializable())));
//                break;
//            case ContentConstants.CONTENT_MODIFY_NUM:
//                wp.writeBooleanNx(processLogic.modifyContentNum(rPacket.readStringUTF(), ((ObjectField) rPacket.readSerializable()), (Integer) rPacket.readSerializable()));
//                break;
//            case ContentConstants.CONTENT_REMOVE:
//                wp.writeBooleanNx(processLogic.removeContent(rPacket.readStringUTF()));
//                break;
//            case ContentConstants.CONTENT_QUERY_BY_CNTIDS_SET:
//                wp.writeSerializable((Serializable) processLogic.queryContents((Set<String>) rPacket.readSerializable()));
//                break;
//            case ContentConstants.CONTENT_QUERY_BY_LASTEST_UNOS:
//                wp.writeSerializable((Serializable) processLogic.queryContents((Set<String>) rPacket.readSerializable(), (Integer) rPacket.readSerializable()));
//                break;
//            case ContentConstants.CONTENT_GET_BY_CNTID:
//                wp.writeSerializable(processLogic.getContentById(rPacket.readStringUTF()));
//                break;
//            case ContentConstants.CONTENT_QUERY_BY_UNO:
//                wp.writeSerializable(processLogic.queryContentsByUno(rPacket.readStringUTF(), (Pagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.CONTENT_QUERY_BY_DATE_STEP:
//                wp.writeSerializable(processLogic.queryContentsByDateStep((Date) rPacket.readSerializable(), (Date) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//
//            case ContentConstants.CONENT_RELATION_CREATE:
//                wp.writeSerializable(processLogic.createContentRelation(rPacket.readStringUTF(), (ContentRelation) rPacket.readSerializable()));
//                break;
//
//            //content reply
//            case ContentConstants.CONTENT_INTERACTION_CREATE:
////                wp.writeSerializable(processLogic.postReply((ContentReply) rPacket.readSerializable()));
//                wp.writeSerializable(processLogic.createInteraction((ContentInteraction) rPacket.readSerializable()));
//                break;
//            case ContentConstants.CONTENT_INTERACTION_REMOVE:
//                wp.writeBooleanNx(processLogic.removeInteraction(rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF()));
//                break;
//            case ContentConstants.CONTENT_REPLY_GET_BY_RID_CID_UNO:
//                wp.writeSerializable(processLogic.getInteractionByIidCidUno(rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF()));
//                break;
//            case ContentConstants.CONTENT_REPLY_QUERY_BY_CID_IUNO:
//                wp.writeSerializable((Serializable) processLogic.queryInteractionByCidIUno(rPacket.readStringUTF(), rPacket.readStringUTF(), (InteractionType) rPacket.readSerializable()));
//                break;
//            case ContentConstants.CONTENT_INTERACTION_QUERY_BY_CID_AND_TYPE:
//                wp.writeSerializable(processLogic.queryInteractionsByCidType(rPacket.readStringUTF(), rPacket.readStringUTF(), (InteractionType) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.CURRENTPAGE_CONTENT_INTERACTION_QUERY_BY_EXPRESS:
//                wp.writeSerializable(processLogic.queryCurrentInteractionsByInteractionIdCidType(rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF(), (QueryExpress) rPacket.readSerializable(), rPacket.readIntNx()));
//                break;
//            case ContentConstants.CONTENT_INTERACTION_QUERY_BY_CID:
//                wp.writeSerializable(processLogic.queryInteractionsByCid(rPacket.readStringUTF(), rPacket.readStringUTF(), (Pagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.CURRENTPAGE_CONTENT_INTERACTION_CHILDREN_QUERY_BY_EXPRESS:
//                wp.writeSerializable(processLogic.queryCurrentChildrenInteractionsByChildrenInteractionIdCidType(rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF(), (QuerySortOrder) rPacket.readSerializable(), rPacket.readIntNx()));
//                break;
//            case ContentConstants.CONTENT_INTERACTION_QUERY_BY_EXPRESS:
//                wp.writeSerializable(processLogic.queryInteractionsByExpress(rPacket.readStringUTF(), rPacket.readStringUTF(), (QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.CONTENT_INTERACTION_QUERY_BY_CIDRIDS_MAP:
//                wp.writeSerializable((Serializable) processLogic.queryInteractionsByCidRidsMap((Map<String, List<String>>) rPacket.readSerializable()));
//                break;
//            //file post.
//            case ContentConstants.RESOURCE_FILE_POST:
//                wp.writeSerializable(processLogic.postResourceFile((ResourceFile) rPacket.readSerializable()));
//                break;
//            //wall content
//            case ContentConstants.WALL_CONTENT_QUERY:
//                wp.writeSerializable((Serializable) processLogic.queryWallLayoutMap((Integer) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_CONTENT_REPLYTIMES:
//                wp.writeLongNx(processLogic.queryContentReplyTimes(rPacket.readString(), (Date) rPacket.readSerializable(), (Date) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_CONTENT_BY_QUERYEXPRESS:
//                wp.writeSerializable(processLogic.queryContentByQueryExpress((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_CONTENT_REPLY_BY_QUERYEXPRESS:
//                wp.writeSerializable(processLogic.queryContentReply((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.CONTENT_REPLY_MODIFY:
//                wp.writeSerializable(processLogic.auditContentReply(rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF(), ((Map<ObjectField, Object>) rPacket.readSerializable())));
//                break;
//            //event receive.
//            case ContentConstants.RECIEVE_EVENT:
//                wp.writeBooleanNx(processLogic.receiveEvent((Event) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_ACTIVITY_BY_PAGE:
//                wp.writeSerializable(processLogic.queryActivity((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.INSERT_ACTIVITY:
//                wp.writeSerializable(processLogic.insertActivity((Activity) rPacket.readSerializable()));
//                break;
//            case ContentConstants.GET_ACTIVITY_BY_ID:
//                wp.writeSerializable(processLogic.getActivityById((rPacket.readLongNx())));
//                break;
//            case ContentConstants.MODIFY_ACTIVITY_BY_ID:
//                wp.writeSerializable(processLogic.modifyActivity((UpdateExpress) rPacket.readSerializable(), rPacket.readLongNx()));
//                break;
//            case ContentConstants.INSERT_ACTIVITY_RELATION:
//                wp.writeSerializable(processLogic.insertActivityRelation((ActivityRelation) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_ACTIVITY_RELATION_BY_LIST:
//                wp.writeSerializable((Serializable) processLogic.listActivityRelation((QueryExpress) rPacket.readSerializable()));
//                break;
//            case ContentConstants.GET_ACTIVITY_RELATION:
//                wp.writeSerializable(processLogic.getActivityRelation((QueryExpress) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_LASTEST_ACTIVITY:
//                wp.writeSerializable((Serializable) processLogic.queryHotActivity());
//                break;
//            case ContentConstants.QUERY_ACTIVITY_BY_REALTIONIDS:
//                wp.writeSerializable((Serializable) processLogic.queryActivityByRelations((Set<Long>) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_ACTIVITY_BY_ACTIVITYIDS:
//                wp.writeSerializable((Serializable) processLogic.queryActivityByActivityId((Set<Long>) rPacket.readSerializable()));
//                break;
//            //forcontent
//            case ContentConstants.GET_FORIGNCONTENT_BY_URL:
//                wp.writeSerializable(processLogic.getForignContent(rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF(), (ForignContentDomain) rPacket.readSerializable(), rPacket.readStringUTF()));
//                break;
//            case ContentConstants.GET_FORIGNCONTENT_BY_ID:
//                wp.writeSerializable(processLogic.getForignContentById(rPacket.readLongNx()));
//                break;
//            case ContentConstants.POST_FORIGNREPLY:
//                wp.writeSerializable(processLogic.postForignReply((ForignContentReply) rPacket.readSerializable()));
//                break;
//            case ContentConstants.GET_FORIGNREPLYLIST_BY_CONETNETID_ROOTID:
//                wp.writeSerializable(processLogic.queryForignReplays(rPacket.readLongNx(), rPacket.readLongNx(), (Pagination) rPacket.readSerializable(), rPacket.readBooleanNx()));
//                break;
//            case ContentConstants.QUERY_HOTFROIGNHOTREPLY_BYCONTENTID:
//                wp.writeSerializable((Serializable) processLogic.queryHotForignReply(rPacket.readLongNx(), rPacket.readIntNx()));
//                break;
//            case ContentConstants.REMOVE_FORIGNCONTENT_REPLY:
//                wp.writeBooleanNx(processLogic.removeForignReply(rPacket.readLongNx()));
//                break;
//            case ContentConstants.QUERY_CONTENT_BY_QUERY:
//                wp.writeSerializable((Serializable) processLogic.queryContentByQuery((QueryExpress) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_FORIGN_CONTENT_REPLY_BY_PAGE:
//                wp.writeSerializable(processLogic.queryForignContentReplyByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.MODIFY_FORIGN_CONTENT_REPLY:
//                wp.writeBooleanNx(processLogic.modifyForignContentReply(rPacket.readLongNx(), (UpdateExpress) rPacket.readSerializable(), rPacket.readLongNx(), (ForignContentDomain) rPacket.readSerializable()));
//                break;
//            case ContentConstants.GET_FORIGN_CONTENT_REPLY:
//                wp.writeSerializable(processLogic.getForignContentReply(rPacket.readLongNx()));
//                break;
//            case ContentConstants.QUERY_FORIGN_CONTENT_REPLY:
//                wp.writeSerializable((Serializable) processLogic.queryForignContentReply((QueryExpress) rPacket.readSerializable()));
//                break;
//            case ContentConstants.MODIFY_FORIGNREPLY_BY_REPLYID:
//                wp.writeBooleanNx(processLogic.modifyForignReplyById((UpdateExpress) rPacket.readSerializable(), rPacket.readLongNx(), (ForignContentReplyLog) rPacket.readSerializable()));
//                break;
//            case ContentConstants.GET_CMSRIGHT_HTML:
//                wp.writeStringUTF(processLogic.getRightHtmlByArticleId((ForignContent) rPacket.readSerializable()));
//                break;
//            case ContentConstants.CREATE_FORIGN_CONTENT_REPLY_LOG:
//                wp.writeSerializable(processLogic.createForignContentReplyLog((ForignContentReplyLog) rPacket.readSerializable()));
//                break;
//            case ContentConstants.GET_FORIGN_CONTENT_REPLY_LOG:
//                wp.writeSerializable(processLogic.getForignContentReplyLog(rPacket.readLongNx(), rPacket.readStringUTF(), rPacket.readIntNx()));
//                break;
//            case ContentConstants.AGREE_FORIGN_CONTENT_REPLY:
//                wp.writeIntNx(processLogic.agreeForignCotnentReply(rPacket.readLongNx(), rPacket.readStringUTF()));
//                break;
//            case ContentConstants.QUERY_FORIGN_CONTENTREPLY_BY_SET:
//                wp.writeSerializable((Serializable) processLogic.queryForignContentReplyBySet((Set<Long>) rPacket.readSerializable()));
//                break;
//            case ContentConstants.GET_FORIGNCONTENT_BY_SET:
//                wp.writeSerializable((Serializable) processLogic.getForignContentBySet((Set<Long>) rPacket.readSerializable()));
//                break;
//            case ContentConstants.GET_FORIGN_CONTENT_BY_FID_CDOMAIN:
//                wp.writeSerializable(processLogic.getForignContentByFidCdomain(rPacket.readStringUTF(), (ForignContentDomain) rPacket.readSerializable()));
//                break;
//            case ContentConstants.POST_SOCIALCONTENT:
//                wp.writeSerializable(processLogic.postSocialContent((SocialContent) rPacket.readSerializable()));
//                break;
//            case ContentConstants.GET_SOCIALCONTENT_BYCONTENTID:
//                wp.writeSerializable(processLogic.getSocialContentByContentId(rPacket.readLongNx()));
//                break;
//            case ContentConstants.REMOVE_SOCIALCONTENT:
//                wp.writeSerializable(processLogic.removeSocialContent(rPacket.readStringUTF(), rPacket.readLongNx()));
//                break;
//            case ContentConstants.MODIFY_SOCIALCONTENT:
//                wp.writeSerializable(processLogic.modifySocialContent((UpdateExpress) rPacket.readSerializable(), rPacket.readLongNx()));
//                break;
//            case ContentConstants.POST_SOCIALCONTENT_REPLY:
//                wp.writeSerializable(processLogic.postSocialContentReply((SocialContentReply) rPacket.readSerializable()));
//                break;
//            case ContentConstants.REMOVE_SOCIALCONTENT_REPLY:
//                wp.writeSerializable(processLogic.removeSocialContentReply(rPacket.readLongNx(), rPacket.readLongNx()));
//                break;
//            case ContentConstants.QUERY_SOCIALCONTENT_REPLY:
//                wp.writeSerializable(processLogic.querySocialContentReply(rPacket.readLongNx(), (Pagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.CREATE_SOCIALCONTENT_ACTION:
//                wp.writeSerializable(processLogic.createSocialContentAction((SocialContentAction) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_SOCIALCONTENT_ACTION:
//                wp.writeSerializable(processLogic.querySocialContentAction(rPacket.readLongNx(), (SocialContentActionType) rPacket.readSerializable(), (NextPagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.REMOVE_SOCIALCONTENT_ACTION:
//                wp.writeSerializable(processLogic.removeSocialContentAction(rPacket.readLongNx(), rPacket.readStringUTF(), (SocialContentActionType) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_SOCIAL_HOT_CONTENT_BY_PAGE:
//                wp.writeSerializable(processLogic.querySocialHotContentByPage((NextPagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.INSERT_SOCIAL_HOT_CONTENT:
//                wp.writeSerializable(processLogic.insertSocialHotContent((SocialHotContent) rPacket.readSerializable()));
//                break;
//            case ContentConstants.CHECK_SOCIAL_CONTENT_ACTION:
//                wp.writeSerializable((Serializable) processLogic.checkSocialContentAction(rPacket.readStringUTF(), (SocialContentActionType) rPacket.readSerializable(), (Set<Long>) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_SOCIAL_CONTENT_BY_IDSET:
//                wp.writeSerializable((Serializable) processLogic.querySocialContentByIdSet((Set<Long>) rPacket.readSerializable()));
//                break;
//            case ContentConstants.GET_SOCIAL_HOT_CONTENT:
//                wp.writeSerializable(processLogic.getSocialHotContent(rPacket.readLongNx()));
//                break;
//            case ContentConstants.MODIFY_SOCIAL_HOT_CONTENT:
//                wp.writeBooleanNx(processLogic.modifySocialHotContent((UpdateExpress) rPacket.readSerializable(), rPacket.readLongNx()));
//                break;
//            case ContentConstants.QUERY_SOCIAL_CONTENT_MAP_BY_IDSET:
//                wp.writeSerializable((Serializable) processLogic.querySocialContentMapByIdSet((Set<Long>) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_SOCIALREPORT_BY_PAGE:
//                wp.writeSerializable(processLogic.querySocialReportByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.CREATE_SOCIALREPORT:
//                wp.writeSerializable(processLogic.insertSocialReport((SocialReport) rPacket.readSerializable()));
//                break;
//            case ContentConstants.MODIFY_SOCIALREPORT:
//                wp.writeSerializable(processLogic.modifySocialReport((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
//                break;
//            case ContentConstants.GET_SOCIALREPORTY:
//                wp.writeSerializable(processLogic.getSocialReport((QueryExpress) rPacket.readSerializable()));
//                break;
//            case ContentConstants.INSERT_SOCIALCONTENT_PLAY:
//                wp.writeSerializable(processLogic.insertSocialContentPlay((List<SocialContentPlay>) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_SOCIALCONTENT_BY_PAGE:
//                wp.writeSerializable(processLogic.querySocialContentByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.GET_SOCIALCONTENT_BY_ID:
//                wp.writeSerializable(processLogic.getSocialContent(rPacket.readLongNx()));
//                break;
//            case ContentConstants.QUERY_SOCIAL_HOT_CONTENT:
//                wp.writeSerializable((Serializable) processLogic.querySocialHotContent((QueryExpress) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_SOCIALCONTENT_REPLY_BYPAGE:
//                wp.writeSerializable(processLogic.querySocialContentReplyByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.RECOVER_SOCIALCONTENTR_EPLY:
//                wp.writeSerializable(processLogic.recoverSocialContentReply(rPacket.readLongNx(), rPacket.readLongNx()));
//                break;
//            case ContentConstants.QUERY_SOCIALHOTCONTENT_PAGEROWS:
//                wp.writeSerializable(processLogic.querySocialHotContentPageRows((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_SOCIALHOTCONTENT_LIST:
//                wp.writeSerializable((Serializable) processLogic.querySocialHotContent((QueryExpress) rPacket.readSerializable()));
//                break;
//            case ContentConstants.GET_SOCIAL_CONTENT_ACTION:
//                wp.writeSerializable(processLogic.getSocialContentAction(rPacket.readLongNx(), rPacket.readStringUTF()));
//                break;
//            case ContentConstants.INSERT_SOCIAL_ACTIVITY:
//                wp.writeSerializable(processLogic.insertSocialActivity((SocialActivity) rPacket.readSerializable()));
//                break;
//            case ContentConstants.GET_SOCIAL_ACTIVITY:
//                wp.writeSerializable(processLogic.getSocialActivity(rPacket.readLongNx()));
//                break;
//            case ContentConstants.QUERY_SOCIAL_ACTIVITY:
//                wp.writeSerializable((Serializable) processLogic.querySocialActivity((QueryExpress) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_SOCIAL_ACTIVITY_BY_PAGE:
//                wp.writeSerializable(processLogic.querySocialActivityByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.MODIFY_SOCIAL_ACTIVITY:
//                wp.writeBooleanNx(processLogic.modifySocialActivity(rPacket.readLongNx(), (UpdateExpress) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_SOCIAL_CONTENT_ACTIVITY_BY_PAGE:
//                wp.writeSerializable(processLogic.querySocialContentActivity((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_SOCIAL_TAG_BY_PAGE:
//                wp.writeSerializable(processLogic.querySocialTagByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_SOCIAL_WATERMARK_BY_PAGE:
//                wp.writeSerializable(processLogic.querySocialWatermarkByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.INSERT_SOCIAL_WATERMARK:
//                wp.writeSerializable(processLogic.insertSocialWatermark((SocialWatermark) rPacket.readSerializable()));
//                break;
//            case ContentConstants.GET_SOCIAL_WATERMARK:
//                wp.writeSerializable(processLogic.getSocialWatermark((QueryExpress) rPacket.readSerializable()));
//                break;
//            case ContentConstants.MODIFY_SOCIAL_WATERMARK:
//                wp.writeBooleanNx(processLogic.modifySocialWatermark((QueryExpress) rPacket.readSerializable(), (UpdateExpress) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_SOCIAL_WATERMARK_BY_NEXT:
//                wp.writeSerializable(processLogic.querySocialWatermarkByNext((NextPagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_SOCIAL_ACTIVITY_BY_NEXT:
//                wp.writeSerializable(processLogic.querySocialActivityByNext((NextPagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_SOCIAL_BGAUDIO_BY_PAGE:
//                wp.writeSerializable(processLogic.querySocialBgAudio((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.INSERT_SOCIAL_BGAUDIO:
//                wp.writeSerializable(processLogic.insertSocialBgAudio((SocialBackgroundAudio) rPacket.readSerializable()));
//                break;
//            case ContentConstants.GET_SOCIAL_BGAUDIO:
//                wp.writeSerializable(processLogic.getSocialBgAudio(rPacket.readLongNx()));
//                break;
//            case ContentConstants.MODIFY_SOCIAL_BGAUDIO:
//                wp.writeSerializable(processLogic.modifySocialBgAudio(rPacket.readLongNx(), (UpdateExpress) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_SOCIAL_BGAUDIO_BY_NEXT:
//                wp.writeSerializable(processLogic.querySocialBgAudioByNext((NextPagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.GET_SOCIAL_CONTENT_ACTIVITY:
//                wp.writeSerializable(processLogic.getSocialContentActivity(rPacket.readLongNx(), rPacket.readLongNx()));
//                break;
//            case ContentConstants.QUERY_SOCIAL_CONTENT_ACTIVITY_BY_NEXT:
//                wp.writeSerializable(processLogic.querySocialContentActivityByNext(rPacket.readLongNx(), (NextPagination) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_SOCIAL_ACTIVITY_BY_IDSET:
//                wp.writeSerializable((Serializable) processLogic.querySocialActivityByIdSet((Set<Long>) rPacket.readSerializable()));
//                break;
//            case ContentConstants.MODIFY_SOCIAL_CONTENT_ACTIVITY:
//                wp.writeBooleanNx(processLogic.modifySocialContentActivity(rPacket.readLongNx(), rPacket.readLongNx(), (UpdateExpress) rPacket.readSerializable()));
//                break;
//            case ContentConstants.SEND_OUT_SOCIAL_LOG_EVENT:
//                wp.writeBooleanNx(processLogic.sendOutSocialLogEvent(rPacket.readLongNx(), rPacket.readStringUTF(), rPacket.readLongNx(), (SocialLogType) rPacket.readSerializable(), (SocialLogCategory) rPacket.readSerializable(), rPacket.readIntNx(), rPacket.readIntNx(), rPacket.readStringUTF()));
//                break;
//            case ContentConstants.QUERY_ACTIVITY_BY_LIST:
//                wp.writeSerializable((Serializable) processLogic.queryActivityByList((QueryExpress) rPacket.readSerializable()));
//                break;
//            case ContentConstants.QUERY_MOBILE_GAME_GAG_FORIGNREPLY:
//                wp.writeSerializable((Serializable) processLogic.queryMobileGameGagForignReply(rPacket.readLongNx(), rPacket.readIntNx()));
//                break;
//            case ContentConstants.UPDATE_FORIGN_CONTENT:
//                wp.writeBooleanNx(processLogic.updateForignContent((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable(), rPacket.readLongNx()));
//                break;
//            case ContentConstants.GET_WIKI_CONTENT:
//                wp.writeSerializable(processLogic.getWikiContent(rPacket.readStringUTF(), rPacket.readStringUTF()));
//                break;
//
//            default:
//                GAlerter.lab("ContentPacketDecoder.logicProcess: Unrecognized type: " + type);
//                throw new ServiceException(ServiceException.BAD_PACKET);
//        }

        return wp;
    }
}
