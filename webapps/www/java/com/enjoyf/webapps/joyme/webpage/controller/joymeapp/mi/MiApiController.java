package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.mi;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.joymeapp.AppRedirectType;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.*;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.webapps.common.html.tag.ImageURLTag;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.comment.MainReplyDTO;
import com.enjoyf.webapps.joyme.dto.comment.ReplyDTO;
import com.enjoyf.webapps.joyme.dto.comment.ReplyEntity;
import com.enjoyf.webapps.joyme.dto.comment.UserEntity;
import com.enjoyf.webapps.joyme.dto.joymeapp.gameclient.api.MessageListDTO;
import com.enjoyf.webapps.joyme.dto.usercenter.ProfileDTO;
import com.enjoyf.webapps.joyme.weblogic.comment.CommentWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 15-11-11
 * Time: 下午2:28
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/mi/api")
public class MiApiController extends BaseRestSpringController {


    @Resource(name = "commentWebLogic")
    private CommentWebLogic commentWebLogic;

    @RequestMapping(value = "/replylist")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                       @RequestParam(value = "count", required = false, defaultValue = "10") Integer count,
                       @RequestParam(value = "cid", required = false) String commentId,
                       @RequestParam(value = "uid", required = false) String uid) {
        ResultObjectPageMsg resultMsg = new ResultObjectPageMsg(ResultPageMsg.CODE_S);
        try {
            if (StringUtil.isEmpty(commentId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            Map<String, Object> mapMessage = new HashMap<String, Object>();
            CommentBean commentBean = new CommentBean();
            commentBean.setCommentId(commentId);
            commentBean.setDomain(CommentDomain.ZONGYI_MI);
            PageRows<MainReplyDTO> mainReplyRows = commentWebLogic.queryMainReplyDTO(commentBean, page, count, true);

            if (CollectionUtil.isEmpty(mainReplyRows.getRows())) {
                mapMessage.put("hotreplylist", "");
                mapMessage.put("replylist", "");
                resultMsg.setResult(mapMessage);
                resultMsg.setPage(new JsonPagination(mainReplyRows.getPage()));
                resultMsg.setMsg("success");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            List<MainReplyDTO> hotList = commentWebLogic.queryHotMainReplyDTO(commentBean, 5, false);
            List<MainReplyDTO> returnHotList = new ArrayList<MainReplyDTO>();
            if (CollectionUtil.isEmpty(hotList)) {
                mapMessage.put("hotreplylist", "");
            } else {
                for (MainReplyDTO mainReplyDTO : hotList) {
                    if (mainReplyDTO.getReply().getReply().getAgree_sum() > 0) {
                        returnHotList.add(mainReplyDTO);
                    }
                }
            }

            //把hotreply
            List<String> historySet = new ArrayList<String>();
            List<MainReplyDTO> replyList = mainReplyRows.getRows();
            int i = 0;
            List<Integer> intlist = new ArrayList<Integer>();
            if (!CollectionUtil.isEmpty(replyList) && !CollectionUtil.isEmpty(returnHotList)) {
                for (MainReplyDTO mainReplyDTO : replyList) {
                    historySet.add(String.valueOf(mainReplyDTO.getReply().getReply().getRid()));
                    for (MainReplyDTO hotMainReplyDTO : returnHotList) {
                        historySet.add(String.valueOf(hotMainReplyDTO.getReply().getReply().getRid()));
                        if (mainReplyDTO.getReply().getReply().getRid() == hotMainReplyDTO.getReply().getReply().getRid()) {
                            intlist.add(i);
                        }
                    }
                    i++;
                }

                if (!CollectionUtil.isEmpty(intlist)) {
                    for (int a = intlist.size() - 1; a >= 0; a--) {
                        replyList.remove((int) intlist.get(a));
                    }
                }
            }


            //   查询用户点过赞的

            //查询用户点过赞的
            if (!CollectionUtil.isEmpty(historySet) && !StringUtil.isEmpty(uid)) {
                List<CommentHistory> commentHistories = CommentServiceSngl.get().queryCommentHistory(new QueryExpress().add(QueryCriterions.eq(CommentHistoryField.PROFILE_ID, String.valueOf(uid)))
                        .add(QueryCriterions.eq(CommentHistoryField.DOMAIN, CommentDomain.ZONGYI_MI.getCode()))
                        .add(QueryCriterions.in(CommentHistoryField.OBJECT_ID, historySet.toArray())));
                if (!CollectionUtil.isEmpty(commentHistories)) {
                    for (CommentHistory commentHistory : commentHistories) {
                        for (MainReplyDTO mainReplyDTO : replyList) {
                            if (commentHistory.getObjectId().equals(String.valueOf(mainReplyDTO.getReply().getReply().getRid()))) {
                                mainReplyDTO.getReply().getReply().setIs_agree(1);
                            }
                        }
                        if (!CollectionUtil.isEmpty(returnHotList)) {
                            for (MainReplyDTO hotReplyDTO : returnHotList) {
                                if (commentHistory.getObjectId().equals(String.valueOf(hotReplyDTO.getReply().getReply().getRid()))) {
                                    hotReplyDTO.getReply().getReply().setIs_agree(1);
                                }
                            }
                        }
                    }
                }
            }


            mapMessage.put("replylist", replyList);
            mapMessage.put("hotreplylist", returnHotList);
            resultMsg.setResult(mapMessage);
            resultMsg.setPage(new JsonPagination(mainReplyRows.getPage()));
            resultMsg.setMsg("success");

            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);

            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    @RequestMapping(value = "/sublist")
    @ResponseBody
    public String subList(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                          @RequestParam(value = "count", required = false, defaultValue = "10") Integer count,
                          @RequestParam(value = "cid", required = false) String commentId,
                          @RequestParam(value = "rid", required = false) String rid,
                          @RequestParam(value = "uid", required = false) String uid) {
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        Map<String, Object> returnMap = new HashMap<String, Object>();
        try {
            if (StringUtil.isEmpty(commentId) || StringUtil.isEmpty(rid)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            CommentReply rootReply = CommentServiceSngl.get().getCommentReplyById(commentId, Long.parseLong(rid));

            if (rootReply == null) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_ROOT_REPLY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_ROOT_REPLY_NULL.getMsg());
                return JsonBinder.buildNormalBinder().toJson(resultObjectMsg);
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(rootReply.getReplyProfileId());
            Map<String, String> chooseMap = PointServiceSngl.get().getChooseLottery(profile.getProfileId());

            VerifyProfile verifyProfile = UserCenterServiceSngl.get().getVerifyProfileById(profile.getProfileId());

            UserEntity replyUser = commentWebLogic.buildUserEntity(profile, chooseMap, verifyProfile);
            ReplyEntity rootReplyEntity = commentWebLogic.buildReplyEntry(rootReply);
            ReplyDTO rootReplyDTO = new ReplyDTO();
            rootReplyDTO.setUser(replyUser);
            rootReplyDTO.setReply(rootReplyEntity);


            //查询用户点过赞的
            Set<String> historySet = new HashSet<String>();
            historySet.add(String.valueOf(rootReplyDTO.getReply().getRid()));
            if (!CollectionUtil.isEmpty(historySet) && !StringUtil.isEmpty(uid)) {
                List<CommentHistory> commentHistories = CommentServiceSngl.get().queryCommentHistory(new QueryExpress().add(QueryCriterions.eq(CommentHistoryField.PROFILE_ID, String.valueOf(uid)))
                        .add(QueryCriterions.eq(CommentHistoryField.DOMAIN, CommentDomain.ZONGYI_MI.getCode()))
                        .add(QueryCriterions.in(CommentHistoryField.OBJECT_ID, historySet.toArray())));
                if (!CollectionUtil.isEmpty(commentHistories)) {
                    for (CommentHistory commentHistory : commentHistories) {
                        if (commentHistory.getObjectId().equals(String.valueOf(rootReplyDTO.getReply().getRid()))) {
                            rootReplyDTO.getReply().setIs_agree(1);
                        }
                    }
                }
            }
            returnMap.put("rootreply", rootReplyDTO);

            PageRows<CommentReply> subReplyRows = commentWebLogic.buildCommentReply(commentId, rootReply.getReplyId(), rootReply.getTotalRows(), page, count, false);
            List<ReplyDTO> returnList = new ArrayList<ReplyDTO>();
            PageRows<ReplyDTO> returnRows = new PageRows<ReplyDTO>();
            if (subReplyRows != null && !CollectionUtil.isEmpty(subReplyRows.getRows())) {
                Set<String> profileIdSet = new HashSet<String>();
                for (CommentReply subReply : subReplyRows.getRows()) {
                    profileIdSet.add(subReply.getReplyProfileId());
                    if (subReply.getParentId() != 0 && subReply.getParentId() != subReply.getRootId()) {
                        profileIdSet.add(subReply.getParentProfileId());
                    }
                }

                Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileIdSet);
                Map<String, Map<String, String>> profileChooseMap = PointServiceSngl.get().queryChooseLottery(profileIdSet);
                Map<String, VerifyProfile> verifyProfileMap = UserCenterServiceSngl.get().queryProfileByIds(profileIdSet);

                for (CommentReply subReply : subReplyRows.getRows()) {
                    UserEntity user = commentWebLogic.buildUserEntity(profileMap.get(subReply.getReplyProfileId()), profileChooseMap.get(subReply.getReplyProfileId()), verifyProfileMap.get(subReply.getReplyProfileId()));
                    if (user == null) {
                        continue;
                    }
                    ReplyEntity replyEntity = commentWebLogic.buildReplyEntry(subReply);

                    ReplyDTO replyDTO = new ReplyDTO();
                    replyDTO.setUser(user);
                    replyDTO.setReply(replyEntity);

                    if (subReply.getParentId() != 0 && subReply.getParentId() != subReply.getRootId()) {
                        UserEntity puser = commentWebLogic.buildUserEntity(profileMap.get(subReply.getParentProfileId()), profileChooseMap.get(subReply.getParentProfileId()), verifyProfileMap.get(subReply.getParentProfileId()));
                        replyDTO.setPuser(puser);
                    }
                    returnList.add(replyDTO);
                }

                returnRows.setPage(subReplyRows.getPage());
                returnRows.setRows(returnList);

            }

            returnMap.put("subreplys", returnRows);
            resultObjectMsg.setResult(returnMap);
            resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
            resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
            return JsonBinder.buildNormalBinder().toJson(resultObjectMsg);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);

            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    @RequestMapping("/agree")
    @ResponseBody
    public String agree(HttpServletRequest request, HttpServletResponse response) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        binder.setDateFormat("yyyy-MM-dd");

        String commentId = request.getParameter("cid");
        String uidParam = HTTPUtil.getParam(request, "uid");
        String rid = HTTPUtil.getParam(request, "rid");
        try {
            if (StringUtil.isEmpty(uidParam) || StringUtil.isEmpty(commentId) || StringUtil.isEmpty(rid)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(Long.valueOf(uidParam));
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            CommentHistory commentHistory = CommentServiceSngl.get().getCommentHistoryByCache(String.valueOf(profile.getUid()), String.valueOf(rid), CommentHistoryType.AGREE, CommentDomain.ZONGYI_MI);
            if (commentHistory != null) {
                return ResultCodeConstants.COMMENT_HAS_AGREE.getJsonString();
            }

            UpdateExpress updateReplyExpress = new UpdateExpress();
            updateReplyExpress.increase(CommentReplyField.AGREE_SUM, 1);
            updateReplyExpress.increase(CommentReplyField.REPLY_AGREE_SUM, 1);
            boolean bool = CommentServiceSngl.get().modifyCommentReplyById(commentId, Long.parseLong(rid), updateReplyExpress);
            if (bool) {
                commentHistory = new CommentHistory();
                commentHistory.setProfileId(uidParam);
                commentHistory.setObjectId(String.valueOf(rid));
                commentHistory.setHistoryType(CommentHistoryType.AGREE);
                commentHistory.setDomain(CommentDomain.ZONGYI_MI);
                commentHistory.setActionIp(getIp(request));
                commentHistory.setActionDate(new Date());
                commentHistory.setActionTimes(1);
                CommentServiceSngl.get().createCommentHistory(commentHistory, null, null);
            }

            return ResultCodeConstants.SUCCESS.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);

            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

    }

    @RequestMapping(value = "/messagelist")
    @ResponseBody
    public String messageList(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                              @RequestParam(value = "count", required = false, defaultValue = "10") Integer count,
                              @RequestParam(value = "uid", required = false) String uid) {
        ResultObjectPageMsg resultMsg = new ResultObjectPageMsg(ResultPageMsg.CODE_S);
        if (StringUtil.isEmpty(uid)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            Pagination pagination = new Pagination(page * count, page, count);
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(Long.parseLong(uid));

            PageRows<CommentReply> pageRows = CommentServiceSngl.get().queryCommentReplyByPage(new QueryExpress()
                    .add(QueryCriterions.eq(CommentReplyField.PARENT_PROFILEID, profile.getProfileId()))
                    .add(QueryCriterions.eq(CommentReplyField.DOMAIN, CommentDomain.ZONGYI_MI.getCode()))
                    .add(QuerySort.add(CommentReplyField.CREATE_TIME, QuerySortOrder.DESC)), pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                List<CommentReply> commentReplyList = pageRows.getRows();
                Set<String> profileIdSet = new HashSet<String>();
                Set<Long> parentIdSet = new HashSet<Long>();
                for (CommentReply commentReply : commentReplyList) {
                    parentIdSet.add(commentReply.getParentId());
                    profileIdSet.add(commentReply.getReplyProfileId());
                }

                Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileIdSet);
                List<CommentReply> replyList = CommentServiceSngl.get().queryCommentReply(new QueryExpress().add(QueryCriterions.in(CommentReplyField.REPLY_ID, parentIdSet.toArray())));
                List<MessageListDTO> returnList = new ArrayList<MessageListDTO>();
                for (CommentReply commentReply : commentReplyList) {
                    for (CommentReply myReply : replyList) {
                        if (commentReply.getParentId() == myReply.getReplyId()) {
                            Profile puser = profileMap.get(commentReply.getReplyProfileId());
                            MessageListDTO dto = new MessageListDTO();
                            dto.setJt(String.valueOf(AppRedirectType.DEFAULT_WEBVIEW.getCode()));
                            dto.setJi(commentReply.getCommentId());
                            dto.setCommentid(commentReply.getCommentId());
                            dto.setReplyid(String.valueOf(commentReply.getReplyId()));
                            dto.setUid(String.valueOf(profile.getUid()));
                            dto.setNick(puser.getNick());
                            dto.setPicurl(URLUtils.getJoymeDnUrl(ImageURLTag.parseUserCenterHeadIcon(puser.getIcon(), puser.getSex(), "m", true)));

                            dto.setReplydesc(commentReply.getBody().getText());
                            dto.setDesc(myReply.getBody().getText());
                            dto.setTime(String.valueOf(commentReply.getCreateTime().getTime()));
                            returnList.add(dto);
                        }
                    }
                }
                if (!CollectionUtil.isEmpty(returnList)) {
                    resultMsg.setResult(returnList);
                    resultMsg.setPage(new JsonPagination(pageRows.getPage()));
                    resultMsg.setMsg("success");
                    return JsonBinder.buildNormalBinder().toJson(resultMsg);
                }
            }

            resultMsg.setResult("");
            resultMsg.setPage(new JsonPagination(pageRows.getPage()));
            resultMsg.setMsg("notice.is.null");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);

        } catch (ServiceException e) {

            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    @RequestMapping(value = "/comment")
    @ResponseBody
    public String comment(HttpServletRequest request, HttpServletResponse response) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultCodeConstants.SUCCESS.getCode());
        binder.setDateFormat("yyyy-MM-dd");

        String contentId = request.getParameter("cid");
        String rid = request.getParameter("oid");
        String pid = request.getParameter("pid");
        String uidParam = HTTPUtil.getParam(request, "uid");
        String text = request.getParameter("text");
        String appkey = HTTPUtil.getParam(request, "appkey");

        try {
            long uid = Long.parseLong(uidParam);
            if (StringUtil.isEmpty(contentId) || StringUtil.isEmpty(rid) ||
                    StringUtil.isEmpty(pid) || StringUtil.isEmpty(uidParam) ||
                    StringUtil.isEmpty(text) || StringUtil.isEmpty(appkey)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);

            if (com.enjoyf.platform.util.StringUtil.isEmpty(text)) {
                return ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getJsonString();
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            ReplyBody replyBody = new ReplyBody();
            replyBody.setText(text);
            if (replyBody == null || com.enjoyf.platform.util.StringUtil.isEmpty(replyBody.getText())) {
                return ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getJsonString();
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

            long rootId = 0l;
            try {
                rootId = Long.valueOf(rid);
            } catch (NumberFormatException e) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_OID_ERROR.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_OID_ERROR.getMsg());
                return binder.toJson(resultObjectMsg);
            }
            String rootUno = "";
            String rootProfileId = "";
            String rootProfileKey = "";
            CommentReply rootReply = null;
            if (rootId > 0l) {
                rootReply = CommentServiceSngl.get().getCommentReplyById(contentId, rootId);
                if (rootReply == null || com.enjoyf.platform.util.StringUtil.isEmpty(rootReply.getReplyUno())) {
                    resultObjectMsg.setRs(ResultCodeConstants.COMMENT_ROOT_REPLY_NULL.getCode());
                    resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_ROOT_REPLY_NULL.getMsg());
                    return binder.toJson(resultObjectMsg);
                }
                rootUno = rootReply.getReplyUno();
                rootProfileId = rootReply.getReplyProfileId();
                rootProfileKey = rootReply.getReplyProfileKey();
            }

            long parentId = 0l;
            try {
                if (!com.enjoyf.platform.util.StringUtil.isEmpty(pid)) {
                    parentId = Long.valueOf(pid);
                }
            } catch (NumberFormatException e) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_PID_ERROR.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_PID_ERROR.getMsg());
                return binder.toJson(resultObjectMsg);
            }
            String parentUno = "";
            String parentProfileId = "";
            String parentProfileKey = "";
            if (parentId > 0l) {
                CommentReply parentReply = CommentServiceSngl.get().getCommentReplyById(contentId, parentId);
                if (parentReply == null || com.enjoyf.platform.util.StringUtil.isEmpty(parentReply.getReplyUno())) {
                    resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARENT_REPLY_NULL.getCode());
                    resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARENT_REPLY_NULL.getMsg());
                    return binder.toJson(resultObjectMsg);

                }
                parentUno = parentReply.getReplyUno();
                parentProfileId = parentReply.getReplyProfileId();
                parentProfileKey = parentReply.getReplyProfileKey();
            }

            CommentReply reply = new CommentReply();
            reply.setCommentId(contentId);
            reply.setSubKey(appkey);
            reply.setReplyUno(profile.getUno());
            reply.setReplyProfileId(profile.getProfileId());
            reply.setReplyProfileKey(authApp.getProfileKey());

            reply.setRootId(rootId);
            reply.setRootUno(rootUno);
            reply.setRootProfileId(rootProfileId);
            reply.setRootProfileKey(rootProfileKey);

            reply.setParentId(parentId);
            reply.setParentUno(parentUno);
            reply.setParentProfileId(parentProfileId);
            reply.setParentProfileKey(parentProfileKey);

            reply.setAgreeSum(0);
            reply.setDisagreeSum(0);
            reply.setSubReplySum(0);
            reply.setBody(replyBody);
            reply.setCreateTime(new Date());
            reply.setCreateIp(getIp(request));
            reply.setRemoveStatus(ActStatus.UNACT);
            reply.setTotalRows(0);

            reply.setDomain(CommentDomain.ZONGYI_MI);
            reply = CommentServiceSngl.get().createCommentReply(reply, 0);

            return ResultCodeConstants.SUCCESS.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }


    @RequestMapping(value = "/userinfo")
    @ResponseBody
    public String userInfo(@RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                           @RequestParam(value = "count", required = false, defaultValue = "20") Integer count,
                           @RequestParam(value = "logindomain", required = false) String loginDomain) {
        ResultObjectPageMsg resultMsg = new ResultObjectPageMsg(ResultPageMsg.CODE_S);
        try {
            Pagination pagination = new Pagination(count * page, page, count);
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ProfileField.PROFILEKEY, "zym"));
            if (StringUtil.isEmpty(loginDomain)) {
                queryExpress.add(QueryCriterions.gt(ProfileField.FLAG, 2));
            } else {
                LoginDomain domain = LoginDomain.getByCode(loginDomain);
                if (domain == null) {
                    resultMsg.setRs(-1);
                    resultMsg.setMsg("domain.is.null");
                    resultMsg.setResult("");
                    return JsonBinder.buildNormalBinder().toJson(resultMsg);
                }
                int flag = ProfileFlag.getFlagByLoginDomain(domain);
                queryExpress.add(QueryCriterions.bitwiseAnd(ProfileField.FLAG, QueryCriterionRelation.GT, 1 << flag, 0));
            }
            queryExpress.add(QuerySort.add(ProfileField.CREATETIME, QuerySortOrder.DESC));
            PageRows<Profile> pageRows = UserCenterServiceSngl.get().queryProfileByPage(queryExpress, pagination);
            List<ProfileDTO> returnList = new ArrayList<ProfileDTO>();
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                for (Profile profile : pageRows.getRows()) {
                    ProfileDTO profileDTO = new ProfileDTO();
                    profileDTO.setUid(profile.getUid());
                    profileDTO.setIconurl(profile.getIcon());
                    profileDTO.setNick(profile.getNick());
                    profileDTO.setNoticetime(profile.getCreateTime().getTime());
                    Set<LoginDomain> loginDomains = profile.getFlag().getLoginDomain();
                    for (LoginDomain domain : loginDomains) {
                        if (LoginDomain.QQ.equals(domain)) {
                            profileDTO.setLogindomain("qq");
                        } else if (LoginDomain.SINAWEIBO.equals(domain)) {
                            profileDTO.setLogindomain("新浪微博");
                        } else if (LoginDomain.WEIXIN.equals(domain)) {
                            profileDTO.setLogindomain("微信");
                        } else {
                            profileDTO.setLogindomain(domain.getCode());
                        }
                        break;
                    }
                    returnList.add(profileDTO);
                }
            }
            resultMsg.setResult(returnList);
            resultMsg.setPage(new JsonPagination(pageRows.getPage()));
            resultMsg.setMsg("success");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        } catch (ServiceException e) {
            resultMsg.setRs(-1000);
            resultMsg.setMsg("system.error");
            resultMsg.setResult("");
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

    }
}
