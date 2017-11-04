package com.enjoyf.webapps.joyme.webpage.controller.comment.json;

import com.enjoyf.platform.cloudfile.BucketInfo;
import com.enjoyf.platform.cloudfile.FileHandlerFactory;
import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.UserCenterUtil;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.util.sql.UUIDUtil;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.util.CookieUtil;
import com.enjoyf.webapps.joyme.dto.comment.LiveCommentDTO;
import com.enjoyf.webapps.joyme.dto.comment.ReplyDTO;
import com.enjoyf.webapps.joyme.dto.comment.ReplyEntity;
import com.enjoyf.webapps.joyme.dto.comment.UserEntity;
import com.enjoyf.webapps.joyme.weblogic.comment.AllowCommentStatus;
import com.enjoyf.webapps.joyme.weblogic.comment.CommentWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.comment.AbstractCommentController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * comment api
 * User: zhitaoshi
 * Date: 14-11-10
 * Time: 下午5:20
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/comment/bean/json")
public class CommentBeanJsonController extends AbstractCommentController {

    @Resource(name = "commentWebLogic")
    private CommentWebLogic commentWebLogic;

    /**
     * 发布内容
     *
     * @param domain      业务类型
     * @param groupId     圈子ID
     * @param pic         图片地址
     * @param description 表述
     * @param expStr      扩展字段
     */
    @ResponseBody
    @RequestMapping("/post")
    public String post(HttpServletRequest request,
                       @RequestParam(value = "domain", required = false) String domain,
                       @RequestParam(value = "groupid", required = false) String groupId,
                       @RequestParam(value = "pic", required = false) String pic,
                       @RequestParam(value = "description", required = false) String description,
                       @RequestParam(value = "expstr", required = false) String expStr,
                       @RequestParam(value = "profileid", required = false) String profileId
    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        try {
            if (StringUtil.isEmpty(profileId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }
//            if (StringUtil.isEmpty(description)) {
//				return ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getJsonString(callback);
//			}
            if (!hasCommentPermission(profileId)) {
                return ResultCodeConstants.COMMENT_PROFILE_FORBID.getJsonString(callback);
            }

            JSONObject jsonObject = new JSONObject();
            if (StringUtil.isEmpty(domain) || CommentDomain.getByCode(Integer.valueOf(domain)) == null) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }

            String uniKey = UUIDUtil.getShortUUID();
            if (!StringUtil.isEmpty(uniKey)) {
                CommentBean commentBean = new CommentBean();
                String commentId = MD5Util.Md5(uniKey + domain);
                commentBean.setCommentId(commentId);
                commentBean.setUniqueKey(uniKey);
                commentBean.setDomain(CommentDomain.getByCode(Integer.valueOf(domain)));
                commentBean.setPic(pic);
                commentBean.setDescription(description);
                commentBean.setGroupId(Long.valueOf(groupId));
                commentBean.setCreateTime(new Date());
                commentBean.setExpandstr(expStr);

                commentBean = CommentServiceSngl.get().createCommentBean(commentBean);
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                if (commentBean != null) {
                    LiveCommentDTO liveCommentDTO = buildLiveCommentDTO(commentBean);
                    jsonObject.put("result", liveCommentDTO);

                    JSONObject expandJsonObject = JSONObject.fromObject(expStr);
                    if (null != expandJsonObject) {
                        String videoUrl = expandJsonObject.getString("videoUrl");
                        if (!StringUtil.isEmpty(videoUrl) && videoUrl.indexOf("iframe") < 0) {
                            String key = videoUrl.replace("http://joymepic.joyme.com/", "");
                            transcode(commentId, key);
                        }
                    }
                }
                if (StringUtil.isEmpty(callback)) {
                    return jsonObject.toString();
                } else {
                    return callback + "([" + jsonObject.toString() + "])";
                }
            } else {
                return ResultCodeConstants.ERROR.getJsonString(callback);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            return ResultCodeConstants.ERROR.getJsonString(callback);
        }
    }

    /**
     * 回复直播内容
     */
    @ResponseBody
    @RequestMapping("/reply")
    public String reply(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "cid", required = false) String commentId,
                        @RequestParam(value = "pid", required = false, defaultValue = "0") String pid,
                        @RequestParam(value = "body", required = false) String body,
                        @RequestParam(value = "uno", required = false) String uno,
                        @RequestParam(value = "domain", required = false) String domain,
                        @RequestParam(value = "appkey", required = false, defaultValue = "default") String appkey

    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        JsonBinder binder = JsonBinder.buildNormalBinder();
        binder.setDateFormat("yyyy-MM-dd");
        try {
            if (StringUtil.isEmpty(uno)) {
                uno = CookieUtil.getCookieValue(request, COOKIEKEY_UNO);
            }
            if (StringUtil.isEmpty(uno)) {
                return ResultCodeConstants.USER_NOT_LOGIN.getJsonString(callback);
            }

            if (StringUtil.isEmpty(commentId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }

            if (StringUtil.isEmpty(body)) {
                return ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getJsonString(callback);
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null || StringUtil.isEmpty(authApp.getProfileKey())) {
                return ResultCodeConstants.APP_NOT_EXISTS.getJsonString(callback);
            }

            String profileId = UserCenterUtil.getProfileId(uno, authApp.getProfileKey());
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString(callback);
            }
            ReplyBody replyBody = ReplyBody.parse(body);
            if (replyBody == null || StringUtil.isEmpty(replyBody.getText())) {
                return ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getJsonString(callback);
            }

            AllowCommentStatus allowStatus = checkPostReplyForbid(profileId, replyBody.getText());
            if (allowStatus.equals(AllowCommentStatus.NO_ALLOW)) {
                return ResultCodeConstants.ERROR.getJsonString(callback);
            } else if (allowStatus.equals(AllowCommentStatus.FORBID_UNO)) {
                return ResultCodeConstants.COMMENT_PROFILE_FORBID.getJsonString(callback);
            } else if (allowStatus.equals(AllowCommentStatus.NOT_POST_SAME_TEXT_FIFTEEN_INTERVAL)) {
                return ResultCodeConstants.COMMENT_POST_SAME_TEXT_FIFTEEN_INTERVAL.getJsonString(callback);
            } else if (allowStatus.equals(AllowCommentStatus.NOT_POST_SAME_TEXT_INTERVAL)) {
                return ResultCodeConstants.COMMENT_POST_SAME_TEXT_INTERVAL.getJsonString(callback);
            }

            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            if (commentBean == null) {
                return ResultCodeConstants.COMMENT_BEAN_NULL.getJsonString(callback);
            }

            long parentId = 0l;
            try {
                if (!StringUtil.isEmpty(pid)) {
                    parentId = Long.valueOf(pid);
                }
            } catch (NumberFormatException e) {
                return ResultCodeConstants.COMMENT_PARAM_PID_ERROR.getJsonString(callback);
            }
            String parentUno = "";
            String parentProfileId = "";
            String parentProfileKey = "";
            if (parentId > 0l) {
                CommentReply parentReply = CommentServiceSngl.get().getCommentReplyById(commentId, parentId);
                if (parentReply == null || StringUtil.isEmpty(parentReply.getReplyUno())) {
                    return ResultCodeConstants.COMMENT_PARENT_REPLY_NULL.getJsonString(callback);
                }
                parentUno = parentReply.getReplyUno();
                parentProfileId = parentReply.getReplyProfileId();
                parentProfileKey = parentReply.getReplyProfileKey();
            }

            //内容中的img标签不判断敏感词
            String illegeText = replyBody.getText();
            String regex = "<(img|IMG)[^>]*>";
            Pattern imgPattern = Pattern.compile(regex);
            Matcher matcher = imgPattern.matcher(illegeText);
            while (matcher.find()) {
                String img = matcher.group(0);
                illegeText = illegeText.replace(img, "");
            }
            Set<String> simpleKeyword = ContextFilterUtils.getSimpleEditorBlackList(illegeText);
            Set<String> postKeyword = ContextFilterUtils.getPostContainBlackList(illegeText);
            ResultObjectMsg resultObjectMsg = new ResultObjectMsg();
            if (!CollectionUtil.isEmpty(simpleKeyword) || !CollectionUtil.isEmpty(postKeyword)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getMsg());
                Set<String> keyword = new HashSet<String>();
                keyword.addAll(simpleKeyword);
                keyword.addAll(postKeyword);
                resultObjectMsg.setResult(keyword);
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            CommentReply reply = new CommentReply();
            reply.setCommentId(commentId);
            reply.setReplyUno(uno);
            reply.setReplyProfileId(profileId);
            reply.setReplyProfileKey(authApp.getProfileKey());
            reply.setRootId(0l);
            reply.setRootUno("");
            reply.setRootProfileId("");
            reply.setRootProfileKey("");

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
            reply.setFloorNum(commentBean.getTotalRows() + 1);//todo 以后删掉
            reply.setDomain(CommentDomain.getByCode(Integer.valueOf(domain)));
            reply = CommentServiceSngl.get().createCommentReply(reply, commentBean.getTotalRows());

            Set<String> profileIdSet = new HashSet<String>();
            profileIdSet.add(reply.getReplyProfileId());
            if (!StringUtil.isEmpty(reply.getParentProfileId())) {
                profileIdSet.add(reply.getParentProfileId());
            }
            Map<String, UserEntity> userMap = commentWebLogic.queryUserEntity(profileIdSet);
            ReplyDTO replyDTO = commentWebLogic.buildReplyDTO(reply, userMap);

            resultObjectMsg.setResult(replyDTO);
            resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
            resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
            if (StringUtil.isEmpty(callback)) {
                return binder.toJson(resultObjectMsg);
            } else {
                return callback + "([" + binder.toJson(resultObjectMsg) + "])";
            }
        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " occur Exception.e:", e);
            return ResultCodeConstants.ERROR.getJsonString(callback);
        }
    }

    /**
     * 修改发布内容
     */
    @ResponseBody
    @RequestMapping("/modify")
    public String modify(HttpServletRequest request,
                         @RequestParam(value = "cid", required = false) String commentId,
                         @RequestParam(value = "domain", required = false) String domain,
                         @RequestParam(value = "description", required = false) String description,
                         @RequestParam(value = "expstr", required = false) String expStr,
                         @RequestParam(value = "profileid", required = false) String profileId,
                         @RequestParam(value = "groupid", required = false) String groupId

    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        try {
            if (StringUtil.isEmpty(profileId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }
            if (!hasCommentPermission(profileId)) {
                return ResultCodeConstants.COMMENT_PROFILE_FORBID.getJsonString(callback);
            }
            if (StringUtil.isEmpty(commentId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }
            if (StringUtil.isEmpty(domain) || CommentDomain.getByCode(Integer.valueOf(domain)) == null) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }

            UpdateExpress updateExpress = new UpdateExpress();

            if (!StringUtil.isEmpty(expStr)) {
                updateExpress.set(CommentBeanField.EXPANDSTR, expStr);
            }

            if (!StringUtil.isEmpty(description)) {
                updateExpress.set(CommentBeanField.DESCRIPTION, description);
            }

            boolean isModifySuccess = CommentServiceSngl.get().modifyCommentBeanById(commentId, updateExpress);
            if (isModifySuccess) {
                CommentBean commentBeanTop = CommentServiceSngl.get().getCommentBeanTop(CommentDomain.getByCode(Integer.valueOf(domain)), Long.valueOf(groupId));
                if (commentBeanTop != null && commentBeanTop.getCommentId().equals(commentId)) {
                    CommentServiceSngl.get().setCommentBeanTop(commentId);
                }
                return ResultCodeConstants.SUCCESS.getJsonString(callback);
            } else {
                return ResultCodeConstants.ERROR.getJsonString(callback);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            return ResultCodeConstants.ERROR.getJsonString(callback);
        }
    }

    private LiveCommentDTO buildLiveCommentDTO(CommentBean commentBean) {
        if (commentBean == null) {
            return null;
        }
        LiveCommentDTO liveCommentDTO = new LiveCommentDTO();
        liveCommentDTO.setCommentId(commentBean.getCommentId());
        liveCommentDTO.setCreateTime(commentBean.getCreateTime().getTime());
        liveCommentDTO.setDescription(commentBean.getDescription());
        liveCommentDTO.setDomain(commentBean.getDomain().getCode());
        liveCommentDTO.setGroupId(commentBean.getGroupId());
        liveCommentDTO.setPic(commentBean.getPic());
        liveCommentDTO.setTitle(commentBean.getTitle());
        liveCommentDTO.setUniqueKey(commentBean.getUniqueKey());
        liveCommentDTO.setUri(commentBean.getUri());
        liveCommentDTO.setDateStr(DateUtil.parseDateByLive(commentBean.getCreateTime()));
        liveCommentDTO.setExpandstr(commentBean.getExpandstr());
        liveCommentDTO.setScoreCommentSum(commentBean.getScoreCommentSum());
        return liveCommentDTO;
    }

    @ResponseBody
    @RequestMapping("/querybygroup")
    public String queryByGroup(HttpServletRequest request,
                               @RequestParam(value = "domain", required = false) String domain,
                               @RequestParam(value = "groupid", required = false) String groupId,
                               @RequestParam(value = "pnum", required = false, defaultValue = "1") String pnum,
                               @RequestParam(value = "psize", required = false, defaultValue = "10") String psize,
                               @RequestParam(value = "sort", required = false) String sort,
                               @RequestParam(value = "profileid", required = false) String profileId,
                               @RequestParam(value = "type", required = false) String deviceType//todo 这是什么意思
    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        try {

            if (StringUtil.isEmpty(domain) || CommentDomain.getByCode(Integer.valueOf(domain)) == null || StringUtil.isEmpty(groupId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }

            int cp = Integer.valueOf(pnum);
            int pageSize = Integer.valueOf(psize);
            Pagination page = new Pagination(pageSize * cp, cp, pageSize);
            //call api query live commentbean
            PageRows<CommentBean> liveCommentBeanPageRows = CommentServiceSngl.get().queryCommentBeanByGroup(CommentDomain.getByCode(Integer.valueOf(domain)), Long.valueOf(groupId), page, sort);


            GAlerter.lan("querybygroup liveCommentBeanPageRows size" + liveCommentBeanPageRows == null ? " is null" :
                    String.valueOf(liveCommentBeanPageRows.getRows().size()));

            JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
            List<LiveCommentDTO> list = new ArrayList<LiveCommentDTO>();
            if (StringUtil.isEmpty(deviceType)) {//兼容老版本
                if (liveCommentBeanPageRows != null && !CollectionUtil.isEmpty(liveCommentBeanPageRows.getRows())) {
                    for (CommentBean commentBean : liveCommentBeanPageRows.getRows()) {
                        LiveCommentDTO liveCommentDTO = buildLiveCommentDTO(commentBean);
                        if (liveCommentDTO != null) {
                            list.add(liveCommentDTO);
                        }
                    }
                    JSONObject result = new JSONObject();
                    result.put("page", liveCommentBeanPageRows.getPage());
                    result.put("rows", list);
                    jsonObject.put("result", result);
                }
            } else {
                CommentBean commentBeanTop = null;
                if (cp == 1) {
                    commentBeanTop = CommentServiceSngl.get().getCommentBeanTop(CommentDomain.getByCode(Integer.valueOf(domain)), Long.valueOf(groupId));
                }
                if (commentBeanTop != null) {
                    list.add(buildLiveCommentDTO(commentBeanTop));
                }
                if (liveCommentBeanPageRows != null && !CollectionUtil.isEmpty(liveCommentBeanPageRows.getRows())) {
                    //build live dto
                    for (CommentBean commentBean : liveCommentBeanPageRows.getRows()) {
                        LiveCommentDTO liveCommentDTO = buildLiveCommentDTO(commentBean);
                        if (liveCommentDTO != null) {
                            list.add(liveCommentDTO);
                        }
                    }

                    //load live replys
                    List<LiveCommentDTO> newLiveCommentDTOs = commentWebLogic.queryReplyDTOList(list, Integer.parseInt(deviceType));
                    JSONObject result = new JSONObject();
                    result.put("page", liveCommentBeanPageRows.getPage());
                    result.put("rows", newLiveCommentDTOs);
                    result.put("top", commentBeanTop == null ? null : commentBeanTop.getCommentId());
                    jsonObject.put("result", result);
                }

            }

            if (StringUtil.isEmpty(callback)) {
                return jsonObject.toString();
            } else {
                return callback + "([" + jsonObject.toString() + "])";
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            e.printStackTrace();
            return ResultCodeConstants.ERROR.getJsonString(callback);
        }
    }

    /**
     * 图文直播楼中楼列表
     *
     * @param request
     * @param response
     * @param pNum
     * @param pSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/livesublist")
    public String liveSublist(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "cid", required = false) String commentId,
                              @RequestParam(value = "pnum", required = false, defaultValue = "1") String pNum,
                              @RequestParam(value = "psize", required = false, defaultValue = "10") String pSize,
                              @RequestParam(value = "ordertype", required = false, defaultValue = "DESC") String orderType

    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        JsonBinder binder = JsonBinder.buildNormalBinder();
        binder.setDateFormat("yyyy-MM-dd");
        try {
            if (StringUtil.isEmpty(commentId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }

            int pageNum = Integer.valueOf(pNum);
            int pageSize = Integer.valueOf(pSize);

            PageRows<ReplyDTO> returnRows = new PageRows<ReplyDTO>();
            List<ReplyDTO> returnList = new ArrayList<ReplyDTO>();

            boolean desc = true;
            if (QuerySortOrder.DESC.getCode().equalsIgnoreCase(orderType)) {
                desc = true;
            } else if (QuerySortOrder.ASC.getCode().equalsIgnoreCase(orderType)) {
                desc = false;
            }
            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            if (null == commentBean) {
                return ResultCodeConstants.COMMENT_BEAN_NULL.getJsonString(callback);
            }
            PageRows<CommentReply> subReplyRows = commentWebLogic.buildCommentReply(commentId, 0l, commentBean.getCommentSum(), pageNum, pageSize, desc);
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
            ResultObjectMsg resultObjectMsg = new ResultObjectMsg();

            resultObjectMsg.setResult(returnRows);
            resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
            resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());

            if (StringUtil.isEmpty(callback)) {
                return binder.toJson(resultObjectMsg);
            } else {
                return callback + "([" + binder.toJson(resultObjectMsg) + "])";
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception:", e);
            return ResultCodeConstants.ERROR.getJsonString(callback);
        }
    }

    /**
     * 直播评论置顶
     *
     * @param commentId ID
     */
    @ResponseBody
    @RequestMapping("/settop")
    public String setTop(HttpServletRequest request,
                         @RequestParam(value = "cid", required = false) String commentId,
                         @RequestParam(value = "profileid", required = false) String profileId
    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        try {
            if (StringUtil.isEmpty(profileId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }
            if (!hasCommentPermission(profileId)) {
                return ResultCodeConstants.COMMENT_PROFILE_FORBID.getJsonString(callback);
            }
            if (StringUtil.isEmpty(commentId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }
            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            CommentServiceSngl.get().deleteCommentBeanTop(commentBean.getDomain(), commentBean.getGroupId());
            CommentServiceSngl.get().setCommentBeanTop(commentId);

            return ResultCodeConstants.SUCCESS.getJsonString(callback);


        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            return ResultCodeConstants.ERROR.getJsonString(callback);
        }
    }

    @ResponseBody
    @RequestMapping("/del")
    public String del(HttpServletRequest request,
                      @RequestParam(value = "cid", required = false) String commentId,
                      @RequestParam(value = "profileid", required = false) String profileId,
                      @RequestParam(value = "groupid", required = false) String groupId,
                      @RequestParam(value = "domain", required = false) String domain

    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        try {
            if (StringUtil.isEmpty(profileId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }
            if (!hasCommentPermission(profileId)) {
                return ResultCodeConstants.COMMENT_PROFILE_FORBID.getJsonString(callback);
            }
            if (StringUtil.isEmpty(commentId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }

            CommentBean commentBeanTop = CommentServiceSngl.get().getCommentBeanTop(CommentDomain.getByCode(Integer.parseInt(domain)), Long.valueOf(groupId));
            if (commentBeanTop != null && commentBeanTop.getCommentId().equals(commentId)) {
                CommentServiceSngl.get().deleteCommentBeanTop(CommentDomain.getByCode(Integer.parseInt(domain)), Long.valueOf(groupId));
            }
            boolean bool = CommentServiceSngl.get().deleteCommentBean(commentId);
            if (bool) {
                return ResultCodeConstants.SUCCESS.getJsonString(callback);
            } else {
                return ResultCodeConstants.COMMENT_DELETE_FAILED.getJsonString(callback);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            return ResultCodeConstants.ERROR.getJsonString(callback);
        }
    }

    @ResponseBody
    @RequestMapping("/querybyscore")
    public String queryByScore(HttpServletRequest request,
                               @RequestParam(value = "domain", required = false) String domain,
                               @RequestParam(value = "groupid", required = false) String groupId,
                               @RequestParam(value = "flag", required = false) String flag,
                               @RequestParam(value = "type", required = false) String deviceType,
                               @RequestParam(value = "topcommentid", required = false) String topCommentId
    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        try {
            if (StringUtil.isEmpty(groupId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }
            if (StringUtil.isEmpty(flag)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }
            if (StringUtil.isEmpty(domain) || CommentDomain.getByCode(Integer.valueOf(domain)) == null) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }
            long timeFlag = -1l;
            try {
                timeFlag = Long.valueOf(flag);
            } catch (NumberFormatException e) {
            }
            if (timeFlag < 0l) {
                return ResultCodeConstants.ERROR.getJsonString(callback);
            }


            List<CommentBean> list = CommentServiceSngl.get().queryCommentBeanByScore(CommentDomain.getByCode(Integer.valueOf(domain)), Long.valueOf(groupId), timeFlag);

            GAlerter.lan("querybyscore list size" + list == null ? " is null" : String.valueOf(list.size()));

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());

            CommentBean commentBeanTop = CommentServiceSngl.get().getCommentBeanTop(CommentDomain.getByCode(Integer.valueOf(domain)), Long.valueOf(groupId));

            GAlerter.lan("querybyscore commentBeanTop" + commentBeanTop == null ? " is null" : "not null");

            List<LiveCommentDTO> dtoList = new ArrayList<LiveCommentDTO>();
            if (StringUtil.isEmpty(topCommentId) && null != commentBeanTop) {
                dtoList.add(buildLiveCommentDTO(commentBeanTop));
            }
            if (null != commentBeanTop && !StringUtil.isEmpty(topCommentId)) {
                if (!topCommentId.equals(commentBeanTop.getCommentId())) {
                    dtoList.add(buildLiveCommentDTO(commentBeanTop));
                }
            }
            if (!CollectionUtil.isEmpty(list)) {
                for (CommentBean bean : list) {
                    LiveCommentDTO liveCommentDTO = buildLiveCommentDTO(bean);
                    if (liveCommentDTO != null) {
                        dtoList.add(liveCommentDTO);
                    }
                }
            }

            List<LiveCommentDTO> newLiveCommentDTOs = commentWebLogic.queryReplyDTOList(dtoList,
                    Integer.parseInt(deviceType));

            JSONObject result = new JSONObject();
            result.put("rows", newLiveCommentDTOs);
            result.put("top", commentBeanTop == null ? null : commentBeanTop.getCommentId());
            jsonObject.put("result", result);
            if (StringUtil.isEmpty(callback)) {
                return jsonObject.toString();
            } else {
                return callback + "([" + jsonObject.toString() + "])";
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            e.printStackTrace();
            return ResultCodeConstants.ERROR.getJsonString(callback);
        }
    }


    @ResponseBody
    @RequestMapping("/bind")
    public String bindProfilePermission(HttpServletRequest request,
                                        @RequestParam(value = "nick", required = false) String nick
    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        try {
            if (StringUtil.isEmpty(nick)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }

            nick = URLDecoder.decode(nick, "utf8");
            Profile profile = UserCenterServiceSngl.get().getProfileByNick(nick);
            if (null == profile) {
                return ResultCodeConstants.FAILED.getJsonString(callback);
            }
            String commentPermissionId = getPermissionId(profile.getProfileId(), CommentPermissionType.LIVE_COMMENT);
            CommentPermission permission = CommentServiceSngl.get().createCommentPermission(commentPermissionId, profile.getProfileId(), CommentPermissionType.LIVE_COMMENT, 0);
            if (null == permission) {
                return ResultCodeConstants.COMMENT_PROFILE_FORBID.getJsonString(callback);
            }
            return ResultCodeConstants.SUCCESS.getJsonString(callback);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            return ResultCodeConstants.ERROR.getJsonString(callback);
        }
    }

    /**
     * 用户权限检查接口
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/check")
    public String checkCommentPermission(HttpServletRequest request,
                                         @RequestParam(value = "profileid", required = false) String profileId
    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        try {
            if (StringUtil.isEmpty(profileId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }
            BucketInfo bucketInfo = BucketInfo.getByCode(WebappConfig.get().getDefaultUploadBucket());
            String token = FileHandlerFactory.getToken(bucketInfo);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uploadtoken", token);

            if (!hasCommentPermission(profileId)) {
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.COMMENT_PROFILE_FORBID.getCode()));
                jsonObject.put("msg", ResultCodeConstants.COMMENT_PROFILE_FORBID.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return jsonObject.toString();
                } else {
                    return callback + "([" + jsonObject.toString() + "])";
                }
            } else {
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return jsonObject.toString();
                } else {
                    return callback + "([" + jsonObject.toString() + "])";
                }
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            return ResultCodeConstants.ERROR.getJsonString(callback);
        }
    }


    /**
     * 赞
     *
     * @param request
     * @param response
     * @param uniKey
     * @param domain
     * @param rid
     * @return
     */
    @ResponseBody
    @RequestMapping("/agree")
    public String agree(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "unikey", required = false) String uniKey,
                        @RequestParam(value = "domain", required = false) String domain,
                        @RequestParam(value = "appkey", required = false, defaultValue = "default") String appkey,
                        @RequestParam(value = "rid", required = false) String rid
    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        JsonBinder binder = JsonBinder.buildNormalBinder();
        binder.setDateFormat("yyyy-MM-dd");
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultCodeConstants.SUCCESS.getCode());
        if (!StringUtil.isEmpty(uniKey)) {
            if (uniKey.indexOf("?") >= 0) {
                uniKey = uniKey.substring(0, uniKey.indexOf("?"));
            }
        }
        try {
            String uno = request.getParameter("uno");
            if (StringUtil.isEmpty(uno)) {
                uno = CookieUtil.getCookieValue(request, COOKIEKEY_UNO);
            }
            if (StringUtil.isEmpty(uno)) {
                resultObjectMsg.setRs(ResultCodeConstants.USER_NOT_LOGIN.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.USER_NOT_LOGIN.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            if (StringUtil.isEmpty(uniKey)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            Pattern pattern = Pattern.compile("[0-9]+");
            if (StringUtil.isEmpty(domain) || (!pattern.matcher(domain).matches())) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            if (StringUtil.isEmpty(rid)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_RID_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_RID_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            String commentId = MD5Util.Md5(uniKey + domain);
            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            if (commentBean == null) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            long replyId = 0l;
            try {
                replyId = Long.valueOf(rid);
            } catch (NumberFormatException e) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_RID_ERROR.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_RID_ERROR.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            CommentReply reply = CommentServiceSngl.get().getCommentReplyById(commentId, replyId);
            if (reply == null || reply.getRemoveStatus().equals(ActStatus.ACTED)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_REPLY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_REPLY_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null || StringUtil.isEmpty(authApp.getProfileKey())) {
                resultObjectMsg.setRs(ResultCodeConstants.APP_NOT_EXISTS.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.APP_NOT_EXISTS.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            CommentDomain commentDomain = CommentDomain.getByCode(Integer.valueOf(domain));
            String profileId = UserCenterUtil.getProfileId(uno, authApp.getProfileKey());
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                resultObjectMsg.setRs(ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            CommentHistory history = CommentServiceSngl.get().getCommentHistoryByCache(profileId, String.valueOf(replyId), CommentHistoryType.AGREE, commentDomain);
            if (history != null) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_HAS_AGREE.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_HAS_AGREE.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.increase(CommentReplyField.AGREE_SUM, 1);
            updateExpress.increase(CommentReplyField.REPLY_AGREE_SUM, 1);
            CommentServiceSngl.get().modifyCommentReplyById(commentId, replyId, updateExpress);

            CommentHistory commentHistory = new CommentHistory();
            commentHistory.setProfileId(profileId);
            commentHistory.setObjectId(String.valueOf(replyId));
            commentHistory.setHistoryType(CommentHistoryType.AGREE);
            commentHistory.setDomain(commentDomain);
            commentHistory.setActionIp(getIp(request));
            commentHistory.setActionDate(new Date());
            commentHistory.setActionTimes(1);
            CommentServiceSngl.get().createCommentHistory(commentHistory, commentBean, reply);

            resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
            resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception:", e);
        }
        if (StringUtil.isEmpty(callback)) {
            return binder.toJson(resultObjectMsg);
        } else {
            return callback + "([" + binder.toJson(resultObjectMsg) + "])";
        }
    }

    /**
     * 删除评论
     *
     * @param request
     * @param response
     * @param uniKey
     * @param domain
     * @param rid
     * @return
     */
    @ResponseBody
    @RequestMapping("/remove")
    public String remove(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "unikey", required = false) String uniKey,
                         @RequestParam(value = "domain", required = false) String domain,
                         @RequestParam(value = "appkey", required = false, defaultValue = "default") String appkey,
                         @RequestParam(value = "rid", required = false) String rid
    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        JsonBinder binder = JsonBinder.buildNormalBinder();
        binder.setDateFormat("yyyy-MM-dd");
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultCodeConstants.SUCCESS.getCode());
        if (!StringUtil.isEmpty(uniKey)) {
            if (uniKey.indexOf("?") >= 0) {
                uniKey = uniKey.substring(0, uniKey.indexOf("?"));
            }
        }
        try {
            String uno = request.getParameter("uno");
            if (StringUtil.isEmpty(uno)) {
                uno = CookieUtil.getCookieValue(request, COOKIEKEY_UNO);
            }
            if (StringUtil.isEmpty(uno)) {
                resultObjectMsg.setRs(ResultCodeConstants.USER_NOT_LOGIN.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.USER_NOT_LOGIN.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            if (StringUtil.isEmpty(uniKey)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            Pattern pattern = Pattern.compile("[0-9]+");
            if (StringUtil.isEmpty(domain) || (!pattern.matcher(domain).matches())) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null || StringUtil.isEmpty(authApp.getProfileKey())) {
                resultObjectMsg.setRs(ResultCodeConstants.APP_NOT_EXISTS.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.APP_NOT_EXISTS.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            String profileId = UserCenterUtil.getProfileId(uno, authApp.getProfileKey());
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                resultObjectMsg.setRs(ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            if (StringUtil.isEmpty(rid)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_RID_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_RID_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            String commentId = MD5Util.Md5(uniKey + domain);
            long replyId = 0l;
            try {
                replyId = Long.valueOf(rid);
                CommentReply reply = CommentServiceSngl.get().getCommentReplyById(commentId, replyId);
                if (reply != null && reply.getReplyProfileId().equals(profileId) && !reply.getRemoveStatus().equals(ActStatus.ACTED)) {
                    CommentServiceSngl.get().removeCommentReply(commentId, replyId, reply.getRootId(), null);
                }
            } catch (NumberFormatException e) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_RID_ERROR.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_RID_ERROR.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

//            CommentReply lastReply = CommentServiceSngl.get().getLastCommentReply(commentId);
//            if(lastReply != null){
//                Profile lastProfile = UserCenterServiceSngl.get().getProfileByProfileId(lastReply.getReplyProfileId());
//                UserEntity userEntity = commentWebLogic.buildUserEntity(lastProfile);
//                ReplyEntity replyEntity = commentWebLogic.buildReplyEntry(lastReply);
//
//                ReplyDTO reply = new ReplyDTO();
//                reply.setUser(userEntity);
//                reply.setReply(replyEntity);
//                resultObjectMsg.setResult(reply);
//            }

            resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
            resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception:", e);
        }
        if (StringUtil.isEmpty(callback)) {
            return binder.toJson(resultObjectMsg);
        } else {
            return callback + "([" + binder.toJson(resultObjectMsg) + "])";
        }
    }

    /**
     * 视频转码
     *
     * @param commentId
     * @param key
     */
    public void transcode(String commentId, String key) {
        try {

            BucketInfo bucketInfo = BucketInfo.getByCode(WebappConfig.get().getDefaultUploadBucket());

            List<String> pids = FileHandlerFactory.getPersistentIds(bucketInfo, key);

            CommentServiceSngl.get().createTranscodeVideo(commentId, pids.toString().replace("[", "").replace("]", ""));

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
    }

    private boolean hasCommentPermission(String profileId) {
        String permissionId = getPermissionId(profileId, CommentPermissionType.LIVE_COMMENT);
        try {
            CommentPermission permission = CommentServiceSngl.get().getPermissionByProfileId(permissionId);
            return permission != null;
        } catch (ServiceException e) {
            return false;
        }
    }

    private String getPermissionId(String profileId, CommentPermissionType permissionType) {
        return MD5Util.Md5(profileId + permissionType.getCode());
    }
}
