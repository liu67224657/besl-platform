package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.json;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.UserCenterSumIncreaseEvent;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveContentType;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveRelationType;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchivesFiled;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.timeline.SocialTimeLineDomain;
import com.enjoyf.platform.service.timeline.SocialTimeLineItem;
import com.enjoyf.platform.service.timeline.TimeLineServiceSngl;
import com.enjoyf.platform.service.usercenter.ModifyTimeJson;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.*;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.webapps.joyme.dto.comment.MainReplyDTO;
import com.enjoyf.webapps.joyme.weblogic.comment.CommentWebLogic;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.socialclient.SocialClientWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/6/30
 * Description:
 */
@Controller
@RequestMapping("/joymeapp/gameclient/json/miyou")
public class GameClientJsonMiyouController extends AbstractGameClientBaseController {


    @Resource(name = "commentWebLogic")
    private CommentWebLogic commentWebLogic;


    @Resource(name = "socialClientWebLogic")
    private SocialClientWebLogic socialClientWebLogic;

    @RequestMapping("/like")
    @ResponseBody
    public String like(HttpServletRequest request, HttpServletResponse response) {
        return "";
    }

    @RequestMapping("/unlike")
    @ResponseBody
    public String unlike(HttpServletRequest request, HttpServletResponse response) {
        return "";
    }

    @RequestMapping("/comment")
    @ResponseBody
    public String comment(HttpServletRequest request, HttpServletResponse response) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultCodeConstants.SUCCESS.getCode());
        binder.setDateFormat("yyyy-MM-dd");

        String contentId = request.getParameter("cid");
        String uidParam = HTTPUtil.getParam(request, "uid");
        String text = request.getParameter("text");
        String appkey = HTTPUtil.getParam(request, "appkey");


        try {
            long uid = Long.parseLong(uidParam);

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);

            if (StringUtil.isEmpty(text)) {
                return ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getJsonString();
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            //禁言
            CommentForbid forbid = CommentServiceSngl.get().getCommentForbidByCache(profile.getProfileId());
            if (forbid != null) {
                return ResultCodeConstants.COMMENT_PROFILE_FORBID.getJsonString();
            }

            ReplyBody replyBody = ReplyBody.parse(commentWebLogic.postreply(text));
            if (replyBody == null || StringUtil.isEmpty(replyBody.getText())) {
                return ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getJsonString();
            }

            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(contentId);
            if (commentBean == null) {
                return ResultCodeConstants.COMMENT_BEAN_NULL.getJsonString();
            }


            Set<String> simpleKeyword = ContextFilterUtils.getSimpleEditorBlackList(replyBody.getText());
            Set<String> postKeyword = ContextFilterUtils.getPostContainBlackList(replyBody.getText());
            if (!CollectionUtil.isEmpty(simpleKeyword) || !CollectionUtil.isEmpty(postKeyword)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getMsg());
                Set<String> keyword = new HashSet<String>();
                keyword.addAll(simpleKeyword);
                keyword.addAll(postKeyword);
                resultObjectMsg.setResult(keyword);
                return binder.toJson(resultObjectMsg);
            }

            CommentReply reply = new CommentReply();
            reply.setCommentId(commentBean.getCommentId());
            reply.setSubKey(commentBean.getUniqueKey());
            reply.setReplyUno(profile.getUno());
            reply.setReplyProfileId(profile.getProfileId());
            reply.setReplyProfileKey(authApp.getProfileKey());

            reply.setAgreeSum(0);
            reply.setDisagreeSum(0);
            reply.setSubReplySum(0);
            reply.setBody(replyBody);
            reply.setCreateTime(new Date());
            reply.setCreateIp(getIp(request));
            reply.setRemoveStatus(ActStatus.UNACT);
            reply.setTotalRows(0);

            reply.setDomain(CommentDomain.GAMECLIENT_MIYOU);
            reply = CommentServiceSngl.get().createCommentReply(reply, commentBean.getTotalRows());

            if (!profile.getProfileId().equals(commentBean.getUri())) {
                UserCenterSumIncreaseEvent ucsiEvent = new UserCenterSumIncreaseEvent();
                ucsiEvent.setProfileId(commentBean.getUri());
                ModifyTimeJson json = new ModifyTimeJson();
                json.setMiyouModifyTime(System.currentTimeMillis());
                ucsiEvent.setModifyTimeJson(json);
                EventDispatchServiceSngl.get().dispatch(ucsiEvent);
            }
            JoymeAppServiceSngl.get().modifyAnimeTag(commentBean.getGroupId(), new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, commentBean.getGroupId())),
                    new UpdateExpress().increase(AnimeTagField.FAVORITE_NUM, 1l));

            socialClientWebLogic.sendWanbaMessage(commentBean, reply);

            return ResultCodeConstants.SUCCESS.getJsonString();
        } catch (Exception e) {

            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

    }

    @RequestMapping("/miyoulist")
    @ResponseBody
    public String miYouList(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                            @RequestParam(value = "count", required = false, defaultValue = "10") Integer count,
                            @RequestParam(value = "uid", required = false) String uid
    ) {
        ResultObjectPageMsg resultMsg = new ResultObjectPageMsg(ResultPageMsg.CODE_S);
        Pagination pagination = new Pagination(count * page, page, count);
        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, TAGID))
                .add(QueryCriterions.eq(TagDedearchivesFiled.ARCHIVE_CONTENT_TYPE, ArchiveContentType.MIYOU_COMMENT.getCode()))
                .add(QueryCriterions.eq(TagDedearchivesFiled.ARCHIVE_RELATION_TYPE, ArchiveRelationType.TAG_RELATION.getCode()))
                .add(QueryCriterions.eq(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.VALID.getCode()))
                .add(QuerySort.add(TagDedearchivesFiled.DISPLAY_ORDER, QuerySortOrder.ASC));
        try {
            PageRows<TagDedearchives> tagPageRows = JoymeAppServiceSngl.get().queryTagDedearchivesByPage(true, TAGID, 0, queryExpress, pagination);
            if (tagPageRows != null && !CollectionUtil.isEmpty(tagPageRows.getRows())) {
                Set<String> tagSet = new HashSet<String>();
                for (TagDedearchives tag : tagPageRows.getRows()) {
                    tagSet.add(tag.getDede_archives_id());
                }
                Map<String, CommentBean> commentBeanMap = CommentServiceSngl.get().queryCommentBeanByIds(tagSet);
                if (commentBeanMap != null) {
                    List<CommentBean> miyouList = new ArrayList<CommentBean>(commentBeanMap.values());
                    Set<String> commentIdSet = new HashSet<String>();
                    Set<String> historySet = new HashSet<String>();

                    Set<String> profileSet = new HashSet<String>();
                    for (CommentBean commentBean : miyouList) {
                        commentBean.setPic(URLUtils.getJoymeDnUrl(commentBean.getPic()));
                        profileSet.add(commentBean.getUri());
                        historySet.add(commentBean.getCommentId());
                        if (commentBean.getCommentSum() > 0) {
                            commentIdSet.add(commentBean.getCommentId());
                        }
                    }
                    List<CommentBean> returnList = new ArrayList<CommentBean>();
                    for (TagDedearchives tagDedearchives : tagPageRows.getRows()) {
                        for (CommentBean commentBean : miyouList) {
                            commentBean.setDescription(replaceHtmlText(commentBean.getDescription()));
                            commentBean.setPic(URLUtils.getJoymeDnUrl(commentBean.getPic()));
                            if (tagDedearchives.getDede_archives_id().equals(commentBean.getCommentId())) {
                                returnList.add(commentBean);
                            }
                        }
                    }

                    //查询帖子点赞数最多的评论
                    Map<String, CommentReply> map = CommentServiceSngl.get().queryHotReplyCacheByAgreeSum(commentIdSet);
                    if (map != null) {
                        for (CommentReply commentReply : map.values()) {
                            profileSet.add(commentReply.getReplyProfileId());
                        }
                    }

                    List<CommentHistory> commentHistories = null;
                    if (!CollectionUtil.isEmpty(historySet)) {
                        commentHistories = CommentServiceSngl.get().queryCommentHistory(new QueryExpress().add(QueryCriterions.eq(CommentHistoryField.PROFILE_ID, uid))
                                .add(QueryCriterions.eq(CommentHistoryField.DOMAIN, CommentDomain.GAMECLIENT_MIYOU.getCode()))
                                .add(QueryCriterions.in(CommentHistoryField.OBJECT_ID, historySet.toArray())));
                    }
                    Map<String, Profile> profilesMap = UserCenterServiceSngl.get().queryProfiles(profileSet);
                    Map<String, Object> mapMessage = new HashMap<String, Object>();
                    mapMessage.put("miyoulist", returnList);
                    mapMessage.put("hotReply", map == null ? "" : map.values());
                    mapMessage.put("profiles", profilesMap == null ? "" : profilesMap.values());
                    mapMessage.put("commentHistories", commentHistories);
                    mapMessage.put("page", tagPageRows.getPage());


                    resultMsg.setResult(mapMessage);
                    resultMsg.setPage(new JsonPagination(tagPageRows.getPage()));
                }
            }
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        } catch (Exception e) {
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

    }

    @RequestMapping("/mymiyoulist")
    @ResponseBody
    public String mymiYouList(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                              @RequestParam(value = "count", required = false, defaultValue = "10") Integer count,
                              @RequestParam(value = "uid", required = false) String uid
    ) {
        ResultObjectPageMsg resultMsg = new ResultObjectPageMsg(ResultPageMsg.CODE_S);
        try {
            Pagination pagination = new Pagination(count * page, page, count);
            //存放点赞数最多的评论ID
            Set<String> commentIdSet = new HashSet<String>();
            //存放我回复过的帖子ID
            Set<String> historySet = new HashSet<String>();
            //取用户头像信息ID
            Set<String> profileSet = new HashSet<String>();
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(Long.parseLong(uid));
            PageRows<SocialTimeLineItem> timeLinePageRows = TimeLineServiceSngl.get().querySocialTimeLineItemList(SocialTimeLineDomain.MY_MIYOU, profile.getProfileId(), pagination);
            if (timeLinePageRows != null && !CollectionUtil.isEmpty(timeLinePageRows.getRows())) {
                Set<String> lineSet = new HashSet<String>();
                for (SocialTimeLineItem timeline : timeLinePageRows.getRows()) {
                    lineSet.add(timeline.getDirectId());
                }
                Map<String, CommentBean> commentBeanMap = CommentServiceSngl.get().queryCommentBeanByIds(lineSet);
                if (commentBeanMap != null) {
                    List<CommentBean> mymiyouList = new ArrayList<CommentBean>(commentBeanMap.values());
                    for (CommentBean commentBean : mymiyouList) {
                        profileSet.add(commentBean.getUri());
                        historySet.add(commentBean.getCommentId());
                        if (commentBean.getCommentSum() > 0) {
                            commentIdSet.add(commentBean.getCommentId());
                        }
                    }
                    //重新排序
                    Collections.sort(mymiyouList, new Comparator() {
                        public int compare(Object o1, Object o2) {
                            if (((CommentBean) o1).getCreateTime().getTime() < ((CommentBean) o2).getCreateTime().getTime()) {
                                return 1;
                            }
                            return -1;
                        }
                    });
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar today = Calendar.getInstance();
                    today.add(Calendar.DATE, 0);
                    Calendar today2 = Calendar.getInstance();
                    today2.add(Calendar.DATE, -1);
                    Calendar today3 = Calendar.getInstance();
                    today3.add(Calendar.DATE, -2);

                    String todayString = sdf.format(today.getTime());
                    String today2String = sdf.format(today2.getTime());
                    String today3String = sdf.format(today3.getTime());
                    List<CommentBean> todayList = new ArrayList<CommentBean>();
                    List<CommentBean> yesterdayList = new ArrayList<CommentBean>();
                    List<CommentBean> dayList = new ArrayList<CommentBean>();
                    List<CommentBean> moredayList = new ArrayList<CommentBean>();
                    String postTime = "";
                    for (CommentBean commentBean : mymiyouList) {
                        commentBean.setDescription(replaceHtmlText(commentBean.getDescription()));
                        postTime = sdf.format(commentBean.getCreateTime());
                        if (postTime.contains(todayString)) {
                            todayList.add(commentBean);
                        } else if (postTime.contains(today2String)) {
                            yesterdayList.add(commentBean);
                        } else if (postTime.contains(today3String)) {
                            dayList.add(commentBean);
                        } else {
                            moredayList.add(commentBean);
                        }
                    }
                    Map<String, Object> mapMessage = new HashMap<String, Object>();
                    mapMessage.put("todayList", todayList);
                    mapMessage.put("yesterdayList", yesterdayList);
                    mapMessage.put("dayList", dayList);
                    mapMessage.put("moredayList", moredayList);
                    mapMessage.put("mymiyouList", mymiyouList);
                    mapMessage.put("mymiyoupage", timeLinePageRows.getPage());


                    //查询帖子点赞数最多的评论
                    Map<String, CommentReply> map = CommentServiceSngl.get().queryHotReplyCacheByAgreeSum(commentIdSet);
                    if (map != null) {
                        for (CommentReply commentReply : map.values()) {
                            profileSet.add(commentReply.getReplyProfileId());
                        }
                    }
                    //查询用户点过赞的
                    List<CommentHistory> commentHistories = null;
                    //查询用户点过赞的
                    if (!CollectionUtil.isEmpty(historySet)) {
                        commentHistories = CommentServiceSngl.get().queryCommentHistory(new QueryExpress().add(QueryCriterions.eq(CommentHistoryField.PROFILE_ID, uid))
                                .add(QueryCriterions.eq(CommentHistoryField.DOMAIN, CommentDomain.GAMECLIENT_MIYOU.getCode()))
                                .add(QueryCriterions.in(CommentHistoryField.OBJECT_ID, historySet.toArray())));
                    }
                    Map<String, Profile> profilesMap = UserCenterServiceSngl.get().queryProfiles(profileSet);

                    mapMessage.put("hotReply", map == null ? "" : map.values());
                    mapMessage.put("profiles", profilesMap == null ? "" : profilesMap.values());
                    mapMessage.put("commentHistories", commentHistories);

                    resultMsg.setResult(mapMessage);
                    resultMsg.setPage(new JsonPagination(timeLinePageRows.getPage()));
                }
            }
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        } catch (Exception e) {
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

    }

    @RequestMapping("/replylist")
    @ResponseBody
    public String replyList(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                            @RequestParam(value = "count", required = false, defaultValue = "10") Integer count,
                            @RequestParam(value = "cid", required = false) String commentId,
                            @RequestParam(value = "uid", required = false) String uid
    ) {
        ResultObjectPageMsg resultMsg = new ResultObjectPageMsg(ResultPageMsg.CODE_S);
        try {
            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            if (commentBean == null) {

            }
            PageRows<MainReplyDTO> mainReplyRows = commentWebLogic.queryMainReplyDTO(commentBean, page, count, true);

            Set<String> historySet = new HashSet<String>();

            if (mainReplyRows != null && !CollectionUtil.isEmpty(mainReplyRows.getRows())) {
                for (MainReplyDTO mainReplyDTO : mainReplyRows.getRows()) {
                    historySet.add(String.valueOf(mainReplyDTO.getReply().getReply().getRid()));
                }
            }

            //查询用户点过赞的
            List<CommentHistory> commentHistories = null;
            //查询用户点过赞的
            if (!CollectionUtil.isEmpty(historySet)) {
                commentHistories = CommentServiceSngl.get().queryCommentHistory(new QueryExpress().add(QueryCriterions.eq(CommentHistoryField.PROFILE_ID, uid))
                        .add(QueryCriterions.eq(CommentHistoryField.DOMAIN, CommentDomain.GAMECLIENT_MIYOU.getCode()))
                        .add(QueryCriterions.in(CommentHistoryField.OBJECT_ID, historySet.toArray())));
            }

            Map<String, Object> mapMessage = new HashMap<String, Object>();
            mapMessage.put("replylist", mainReplyRows.getRows());
            mapMessage.put("commentHistories", commentHistories);
            resultMsg.setResult(mapMessage);
            resultMsg.setPage(new JsonPagination(mainReplyRows.getPage()));

            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        } catch (Exception e) {
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

    }


    @RequestMapping("/agree")
    @ResponseBody
    public String agree(HttpServletRequest request, HttpServletResponse response) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        binder.setDateFormat("yyyy-MM-dd");

        String contentId = request.getParameter("cid");
        String uidParam = HTTPUtil.getParam(request, "uid");
        String rid = HTTPUtil.getParam(request, "rid");


        try {
            if (uidParam == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(contentId);
            if (commentBean == null) {
                return ResultCodeConstants.COMMENT_BEAN_NULL.getJsonString();
            }

            if (StringUtil.isEmpty(rid)) {
                CommentHistory commentHistory = CommentServiceSngl.get().getCommentHistoryByCache(uidParam, contentId, CommentHistoryType.AGREE, CommentDomain.GAMECLIENT_MIYOU);
                if (commentHistory != null) {
                    return ResultCodeConstants.COMMENT_HAS_AGREE.getJsonString();
                }

                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(CommentBeanField.SCORE_COMMENT_SUM, 1);
                updateExpress.increase(CommentBeanField.FOUR_USER_SUM, 1);
                boolean bool = CommentServiceSngl.get().modifyCommentBeanById(contentId, updateExpress);
                if (bool) {
                    commentHistory = new CommentHistory();
                    commentHistory.setProfileId(uidParam);
                    commentHistory.setObjectId(contentId);
                    commentHistory.setHistoryType(CommentHistoryType.AGREE);
                    commentHistory.setDomain(CommentDomain.GAMECLIENT_MIYOU);
                    commentHistory.setActionIp(getIp(request));
                    commentHistory.setActionDate(new Date());
                    commentHistory.setActionTimes(1);
                    commentHistory.setCommentId(contentId);
                    CommentServiceSngl.get().createCommentHistory(commentHistory, commentBean, null);
                } else {
                    return ResultCodeConstants.COMMENT_BEAN_NULL.getJsonString();
                }
            } else {
                CommentHistory commentHistory = CommentServiceSngl.get().getCommentHistoryByCache(uidParam, String.valueOf(rid), CommentHistoryType.AGREE, CommentDomain.GAMECLIENT_MIYOU);
                if (commentHistory != null) {
                    return ResultCodeConstants.COMMENT_HAS_AGREE.getJsonString();
                }

                UpdateExpress updateReplyExpress = new UpdateExpress();
                updateReplyExpress.increase(CommentReplyField.AGREE_SUM, 1);
                updateReplyExpress.increase(CommentReplyField.REPLY_AGREE_SUM, 1);
                boolean bool = CommentServiceSngl.get().modifyCommentReplyById(contentId, Long.parseLong(rid), updateReplyExpress);
                if (bool) {
                    commentHistory = new CommentHistory();
                    commentHistory.setProfileId(uidParam);
                    commentHistory.setObjectId(String.valueOf(rid));
                    commentHistory.setHistoryType(CommentHistoryType.AGREE);
                    commentHistory.setDomain(CommentDomain.GAMECLIENT_MIYOU);
                    commentHistory.setActionIp(getIp(request));
                    commentHistory.setActionDate(new Date());
                    commentHistory.setActionTimes(1);
                    CommentServiceSngl.get().createCommentHistory(commentHistory, commentBean, null);
                }
            }
            return ResultCodeConstants.SUCCESS.getJsonString();
        } catch (Exception e) {
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

    }


    @RequestMapping("/shorten")
    @ResponseBody
    public String shorten(HttpServletRequest request, HttpServletResponse response) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        binder.setDateFormat("yyyy-MM-dd");
        ResultObjectPageMsg resultMsg = new ResultObjectPageMsg(ResultPageMsg.CODE_S);
        String url = request.getParameter("url");
        url = ShortUrlUtils.getSinaURL(url);
        resultMsg.setResult(url);
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }
}
