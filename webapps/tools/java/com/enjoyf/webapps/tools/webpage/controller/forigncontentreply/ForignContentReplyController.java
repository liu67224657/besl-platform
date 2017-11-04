package com.enjoyf.webapps.tools.webpage.controller.forigncontentreply;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.MoodHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.ToolsHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.content.Mood;
import com.enjoyf.platform.service.content.MoodJson;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-2 下午3:22
 * Description:
 */
@Controller
@RequestMapping(value = "/forign/content/reply")
public class ForignContentReplyController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "15") int pageSize,
                             @RequestParam(value = "screenname", required = false) String screenName,
                             @RequestParam(value = "unikey", required = false) String uniKey,
                             @RequestParam(value = "subkey", required = false) String subKey,
                             @RequestParam(value = "refreshStatus", required = false, defaultValue = "0") String refreshStatus,
                             @RequestParam(value = "hide15Status", required = false, defaultValue = "show") String hide15Status,
                             @RequestParam(value = "replyStatus", required = false) String replyStatus,
                             @RequestParam(value = "startdate", required = false) Date startDate,
                             @RequestParam(value = "enddate", required = false) Date endDate,
                             @RequestParam(value = "statuscode", required = false, defaultValue = "n") String statusCode,
                             @RequestParam(value = "bodytext", required = false) String bodyText,
                             @RequestParam(value = "domain", required = false) Integer domain,
                             @RequestParam(value = "commentid", required = false) String commentId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("screenName", screenName);
        mapMessage.put("uniKey", uniKey);
        mapMessage.put("subKey", subKey);
        mapMessage.put("startDate", startDate);
        mapMessage.put("endDate", endDate);
        mapMessage.put("statusCode", statusCode);
        mapMessage.put("domainSet", CommentDomain.getAll());
        mapMessage.put("domainCode", domain);
        mapMessage.put("refreshStatus", refreshStatus);
        mapMessage.put("hide15Status", hide15Status);
        mapMessage.put("replyStatus", replyStatus);
        mapMessage.put("bodyText", bodyText);
        mapMessage.put("commentId", commentId);

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QuerySort.add(CommentReplyField.CREATE_TIME, QuerySortOrder.DESC));

            //      refreshStatus是null或者不等于1的时候执行其他条件过滤
            if (refreshStatus == null || !refreshStatus.equals("1")) {

                if (!StringUtil.isEmpty(screenName)) {
                    Profile profile = UserCenterServiceSngl.get().getProfileByNick(screenName);
                    if (profile == null) {
                        return new ModelAndView("/forigncontent/replylist", mapMessage);
                    }
                    String profileId = profile.getProfileId();
                    queryExpress.add(QueryCriterions.eq(CommentReplyField.REPLY_PROFILEID, profileId));
                }
                if (StringUtil.isEmpty(commentId)) {
                    if (!StringUtil.isEmpty(uniKey) && domain != null && domain > 0) {
                        queryExpress.add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, MD5Util.Md5(uniKey + domain)));
                    }
                } else {
                    queryExpress.add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, commentId));
                }

                if (!StringUtil.isEmpty(subKey)) {
                    queryExpress.add(QueryCriterions.like(CommentReplyField.SUBKEY, "%" + subKey + "%"));
                }
                if (startDate != null && endDate != null) {
                    queryExpress.add(QueryCriterions.between(CommentReplyField.CREATE_TIME, startDate, DateUtil.StringTodate(DateUtil.DateToString(endDate, DateUtil.DATE_FORMAT) + " 23:59:59", DateUtil.DEFAULT_DATE_FORMAT2)));
                }
                if (!StringUtil.isEmpty(statusCode)) {
                    queryExpress.add(QueryCriterions.eq(CommentReplyField.REMOVE_STATUS, statusCode));
                }
                if (!StringUtil.isEmpty(replyStatus)) {
                    if (replyStatus.equals("noReply")) {        //无回复
                        queryExpress.add(QueryCriterions.eq(CommentReplyField.SUB_REPLY_SUM, 0));
                    } else if (replyStatus.equals("hasReply")) {  //有回复
                        queryExpress.add(QueryCriterions.gt(CommentReplyField.SUB_REPLY_SUM, 0));
                    } else if (replyStatus.equals("hasCsReply")) {  //有来自客服的回复
                        queryExpress.add(QueryCriterions.eq(CommentReplyField.CUSTOMER_STATUS, 1));
                    } else if (replyStatus.equals("csReply")) {          //查询客服的回复

                        queryExpress.add(QueryCriterions.eq(CommentReplyField.CUSTOMER_STATUS, 2));
                    }

                }
                if (domain != null && domain > 0) {
                    queryExpress.add(QueryCriterions.eq(CommentReplyField.DOMAIN, domain));
                }
                if (!StringUtil.isEmpty(bodyText)) {
                    queryExpress.add(QueryCriterions.like(CommentReplyField.REPLY_BODY, "%" + bodyText + "%"));
                }

            }
            PageRows<CommentReply> pageRows = CommentServiceSngl.get().queryCommentReplyByPage(queryExpress, pagination);

            Map<String, Profile> profileMap = new HashMap<String, Profile>();
            Map<String, CommentBean> commentBeanMap = new HashMap<String, CommentBean>();
            for (CommentReply reply : pageRows.getRows()) {
                String profileIdTemp = reply.getReplyProfileId();
                if (StringUtil.isEmpty(profileIdTemp)) {

                    Object errorMsg = mapMessage.get("errorMsg");
                    String errorMsgStr = errorMsg == null ? "" : errorMsg.toString();
                    mapMessage.put("errorMsg", errorMsgStr + "<br/>reply id 为" + reply.getReplyId() + "的评论的replyProfileId为空");
                    continue;
                }
                try {
                    Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileIdTemp);
                    if (profile != null) {
                        if (!profileMap.containsKey(profile.getProfileId())) {
                            profileMap.put(profile.getProfileId(), profile);
                        }
                    }
                    CommentBean bean = CommentServiceSngl.get().getCommentBeanById(reply.getCommentId());
                    if (bean != null) {
                        if (!commentBeanMap.containsKey(bean.getCommentId())) {
                            commentBeanMap.put(bean.getCommentId(), bean);
                        }
                    }
                } catch (ServiceException e) {
                    GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);

                    Object errorMsg = mapMessage.get("errorMsg");
                    String errorMsgStr = errorMsg == null ? "" : errorMsg.toString();
                    mapMessage.put("errorMsg", errorMsgStr + "<br/>" + this.getClass().getName() + " occured ServiceException.e: " + e);
                }
            }
            mapMessage.put("profileMap", profileMap);
            mapMessage.put("commentBeanMap", commentBeanMap);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/forigncontent/replylist", mapMessage);
    }

    @RequestMapping(value = "/batchupdate")
    public ModelAndView batchUpdate(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                                    @RequestParam(value = "maxPageItems", required = false, defaultValue = "15") int pageSize,
                                    @RequestParam(value = "replyids", required = false) String idsStr,
                                    @RequestParam(value = "updatestatuscode", required = false) String updateStatusCode,
                                    @RequestParam(value = "screenname", required = false) String screenName,
                                    @RequestParam(value = "unikey", required = false) String uniKey,
                                    @RequestParam(value = "subkey", required = false) String subKey,
                                    @RequestParam(value = "refreshStatus", required = false, defaultValue = "0") String refreshStatus,
                                    @RequestParam(value = "hide15Status", required = false, defaultValue = "show") String hide15Status,
                                    @RequestParam(value = "replyStatus", required = false) String replyStatus,
                                    @RequestParam(value = "startdate", required = false) Date startDate,
                                    @RequestParam(value = "enddate", required = false) Date endDate,
                                    @RequestParam(value = "statuscode", required = false) String statusCode,
                                    @RequestParam(value = "domain", required = false) Integer domain,
                                    @RequestParam(value = "bodytext", required = false) String bodyText,
                                    @RequestParam(value = "commentid", required = false) String commentId,

                                    @RequestParam(value = "rootid", required = false) String oid,
                                    @RequestParam(value = "rurl", required = false) String rurl,
                                    HttpServletRequest request) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("screenName", screenName);
        mapMessage.put("uniKey", uniKey);
        mapMessage.put("subKey", subKey);
        mapMessage.put("startDate", startDate);
        mapMessage.put("endDate", endDate);
        mapMessage.put("statusCode", statusCode);
        mapMessage.put("domain", domain);
        mapMessage.put("refreshStatus", refreshStatus);
        mapMessage.put("hide15Status", hide15Status);
        mapMessage.put("replyStatus", replyStatus);
        mapMessage.put("bodyText", bodyText);
        mapMessage.put("commentid", commentId);

        mapMessage.put("rootid", oid);

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", pagination);
        try {
            String[] idsArr = idsStr.split("@");
            Set<Long> idsSet = new HashSet<Long>();
            for (String idStr : idsArr) {
                String[] strs = idStr.split("_");
                long replyId = Long.parseLong(strs[0]);
                long rootId = Long.parseLong(strs[1]);


                String preCommentId = strs[3];
                ActStatus preStatusCode = ActStatus.getByCode(strs[4]);   //原来的状态 ,是审核,未审核,已删除等


                //删除操作
                if (updateStatusCode.equals(ActStatus.ACTED.getCode()) && !preStatusCode.equals(updateStatusCode)) {
                    idsSet.add(replyId);
                    if (rootId == 0) {
                        int pnum = 0;
                        Pagination page = null;
                        do {
                            pnum += 1;
                            page = new Pagination(100 * pnum, pnum, 100);
                            PageRows<CommentReply> childList = CommentServiceSngl.get().queryCommentReplyByPage(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.ROOT_ID, replyId))
                                    .add(QueryCriterions.ne(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode())), page);
                            page = (childList == null ? page : childList.getPage());
                            if (childList != null && !CollectionUtil.isEmpty(childList.getRows())) {
                                for (CommentReply childReply : childList.getRows()) {
                                    idsSet.add(childReply.getReplyId());
                                }
                            }
                        } while (!page.isLastPage());
                    }
                    if (!CollectionUtil.isEmpty(idsSet)) {
                        UpdateExpress updateExpress = new UpdateExpress();
                        updateExpress.set(CommentReplyField.REMOVE_STATUS, updateStatusCode);
                        updateExpress.set(CommentReplyField.CREATE_IP, getCurrentUser().getUserid());
                        for (Long id : idsSet) {
                            CommentReply reply = CommentServiceSngl.get().getCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.REPLY_ID, id)));
                            CommentServiceSngl.get().removeCommentReply(reply.getCommentId(), reply.getReplyId(), reply.getRootId(), updateExpress);
                        }
                    }
                } else { //恢复 到审核 状态的操作

                    //如果原来是未审核
                    if (preStatusCode.equals(ActStatus.UNACT)) {
                        UpdateExpress updateExpress = new UpdateExpress();
                        updateExpress.set(CommentReplyField.REMOVE_STATUS, ActStatus.ACTING.getCode());
                        updateExpress.set(CommentReplyField.CREATE_IP, getCurrentUser().getUserid());

                        CommentServiceSngl.get().modifyCommentReplyById(preCommentId, replyId, updateExpress);

                    } else if (preStatusCode.equals(ActStatus.ACTED)) {
                        //如果原来是已删除
                        if (rootId == 0) {
                            idsSet.add(replyId);
                        } else {
                            CommentReply rootReply = CommentServiceSngl.get().getCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.REPLY_ID, rootId)));
                            if (rootReply != null && !ActStatus.ACTED.equals(rootReply.getRemoveStatus())) {
                                idsSet.add(replyId);
                            } else {
                                mapMessage = putErrorMessage(mapMessage, "部分评论主评论已删除，无法恢复");
                            }
                        }
                        if (!CollectionUtil.isEmpty(idsSet)) {
                            UpdateExpress updateExpress = new UpdateExpress();
                            updateExpress.set(CommentReplyField.REMOVE_STATUS, updateStatusCode);
                            updateExpress.set(CommentReplyField.CREATE_IP, getCurrentUser().getUserid());
                            for (Long id : idsSet) {
                                CommentReply reply = CommentServiceSngl.get().getCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.REPLY_ID, id)));
                                CommentServiceSngl.get().recoverCommentReply(reply.getCommentId(), reply.getReplyId(), reply.getRootId(), reply.getCreateTime(), updateExpress);
                            }
                        }
                    }
                }
            }


            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.COMMENT_BATCHUPDATE);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP
            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1950) {
                queryString = queryString.substring(0, 1950);
            }
            log.setOpAfter("评论批量修改方法,queryString" + queryString); //描述 推荐用中文
            addLog(log);


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        if(StringUtil.isEmpty(rurl)){
            return new ModelAndView("forward:/forign/content/reply/list", mapMessage);
        }else {
            return new ModelAndView("forward:"+rurl, mapMessage);
        }


    }

    @RequestMapping(value = "/allremove")
    public ModelAndView allRemove(@RequestParam(value = "screenname", required = false) String screenName,
                                  @RequestParam(value = "unikey", required = false) String uniKey,
                                  @RequestParam(value = "subkey", required = false) String subKey,
                                  @RequestParam(value = "startdate", required = false) Date startDate,
                                  @RequestParam(value = "enddate", required = false) Date endDate,
                                  @RequestParam(value = "statuscode", required = false) String statusCode,
                                  @RequestParam(value = "refreshStatus", required = false, defaultValue = "0") String refreshStatus,
                                  @RequestParam(value = "hide15Status", required = false, defaultValue = "show") String hide15Status,
                                  @RequestParam(value = "replyStatus", required = false) String replyStatus,
                                  @RequestParam(value = "domain", required = false) Integer domain,
                                  @RequestParam(value = "bodytext", required = false) String bodyText,
                                  HttpServletRequest request) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("screenname", screenName);
        mapMessage.put("unikey", uniKey);
        mapMessage.put("startdate", startDate);
        mapMessage.put("enddate", endDate);
        mapMessage.put("statuscode", statusCode);
        mapMessage.put("domain", domain);
        mapMessage.put("subKey", subKey);
        mapMessage.put("refreshStatus", refreshStatus);
        mapMessage.put("hide15Status", hide15Status);
        mapMessage.put("replyStatus", replyStatus);
        mapMessage.put("bodyText", bodyText);
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            if (!StringUtil.isEmpty(screenName)) {
                Profile profile = UserCenterServiceSngl.get().getProfileByNick(screenName);
                if (profile != null) {
                    String profileId = profile.getProfileId();
                    queryExpress.add(QueryCriterions.eq(CommentReplyField.REPLY_PROFILEID, profileId));
                }
            } else {
                return new ModelAndView("forward:/forign/content/reply/list", mapMessage);
            }
            int p = 0;
            Pagination page = null;
            do {
                p += 1;
                page = new Pagination(100 * p, p, 100);
                PageRows<CommentReply> pageRows = CommentServiceSngl.get().queryCommentReplyByPage(queryExpress, page);
                page = (pageRows == null ? page : pageRows.getPage());
                if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                    Set<Long> idsSet = new HashSet<Long>();
                    Map<Long, CommentReply> replyMap = new HashMap<Long, CommentReply>();
                    for (CommentReply reply : pageRows.getRows()) {
                        idsSet.add(reply.getReplyId());
                        replyMap.put(reply.getReplyId(), reply);
                        if (reply.getRootId() == 0) {
                            int p2 = 0;
                            Pagination pagination = null;
                            do {
                                p2 += 1;
                                pagination = new Pagination(100 * p2, p2, 100);
                                PageRows<CommentReply> childList = CommentServiceSngl.get().queryCommentReplyByPage(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.ROOT_ID, reply.getReplyId()))
                                        .add(QueryCriterions.eq(CommentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode())), pagination);
                                pagination = (childList == null ? pagination : childList.getPage());
                                if (childList != null && !CollectionUtil.isEmpty(childList.getRows())) {
                                    for (CommentReply childReply : childList.getRows()) {
                                        idsSet.add(childReply.getReplyId());
                                    }
                                }
                            } while (!pagination.isLastPage());
                        }
                    }
                    if (!CollectionUtil.isEmpty(idsSet)) {
                        UpdateExpress updateExpress = new UpdateExpress();
                        updateExpress.set(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode());
                        updateExpress.set(CommentReplyField.CREATE_IP, getCurrentUser().getUserid());
                        for (Long replyId : idsSet) {
                            CommentReply rep = replyMap.get(replyId);
                            if (rep != null) {
                                CommentServiceSngl.get().removeCommentReply(rep.getCommentId(), rep.getReplyId(), rep.getRootId(), updateExpress);
                                ToolsLog log = new ToolsLog();
                                log.setOpUserId(getCurrentUser().getUserid());
                                log.setOperType(LogOperType.MODIFY_FORIGN_CONTENT_REPLAY);
                                log.setOpTime(new Date());
                                log.setOpIp(getIp());
                                log.setOpAfter("ForignContentReply.replyId:" + replyId);

                                addLog(log);
                            }
                        }
                    }
                }
            } while (!page.isLastPage());


            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.COMMENT_ALLREMOVE);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP
            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1950) {
                queryString = queryString.substring(0, 1950);
            }
            log.setOpAfter("评论Allremove方法,queryString" + queryString); //描述 推荐用中文
            addLog(log);


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("forward:/forign/content/reply/list", mapMessage);
    }

    @RequestMapping(value = "/detail")
    public ModelAndView detail(@RequestParam(value = "replyid", required = false) Long replyId,
                               @RequestParam(value = "screenname", required = false) String screenName,
                               @RequestParam(value = "unikey", required = false) String uniKey,
                               @RequestParam(value = "subkey", required = false) String subKey,
                               @RequestParam(value = "startdate", required = false) Date startDate,
                               @RequestParam(value = "enddate", required = false) Date endDate,
                               @RequestParam(value = "statuscode", required = false) String statusCode,
                               @RequestParam(value = "refreshStatus", required = false, defaultValue = "0") String refreshStatus,
                               @RequestParam(value = "hide15Status", required = false, defaultValue = "show") String hide15Status,
                               @RequestParam(value = "replyStatus", required = false) String replyStatus,
                               @RequestParam(value = "bodytext", required = false) String bodyText,
                               @RequestParam(value = "domain", required = false) Integer domain) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("screenname", screenName);
        mapMessage.put("unikey", uniKey);
        mapMessage.put("startdate", startDate);
        mapMessage.put("enddate", endDate);
        mapMessage.put("statuscode", statusCode);
        mapMessage.put("domain", domain);
        mapMessage.put("refreshStatus", refreshStatus);
        mapMessage.put("hide15Status", hide15Status);
        mapMessage.put("replyStatus", replyStatus);
        mapMessage.put("subKey", subKey);
        mapMessage.put("bodyText", bodyText);
        if (replyId == null) {
            return new ModelAndView("forward:/forign/content/reply/list", mapMessage);
        }
        try {
            CommentReply reply = CommentServiceSngl.get().getCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.REPLY_ID, replyId)));
            if (reply == null) {
                return new ModelAndView("forward:/forign/content/reply/list", mapMessage);
            }
            mapMessage.put("reply", reply);
            CommentBean comment = CommentServiceSngl.get().getCommentBeanById(reply.getCommentId());
            mapMessage.put("comment", comment);
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(reply.getReplyProfileId());
            mapMessage.put("profile", profile);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/forigncontent/replydetail", mapMessage);
    }

    @RequestMapping(value = "/sublist")
    public ModelAndView subList(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                                @RequestParam(value = "maxPageItems", required = false, defaultValue = "15") int pageSize,
                                @RequestParam(value = "rootid", required = false) long rootId,
                                @RequestParam(value = "commentid", required = false) String commentId
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("rootId", rootId);
        mapMessage.put("commentId", commentId);

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        try {
            //查询
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, commentId));
            queryExpress.add(QueryCriterions.eq(CommentReplyField.ROOT_ID, rootId));
            queryExpress.add(QuerySort.add(CommentReplyField.CREATE_TIME, QuerySortOrder.DESC));

            PageRows<CommentReply> replyRows = CommentServiceSngl.get().queryCommentReplyByPage(queryExpress, pagination);
            if(replyRows != null){
                Map<String, Profile> profileMap = new HashMap<String, Profile>();
                Map<String, CommentBean> commentBeanMap = new HashMap<String, CommentBean>();
                for (CommentReply reply : replyRows.getRows()) {
                    String profileIdTemp = reply.getReplyProfileId();
                    if (StringUtil.isEmpty(profileIdTemp)) {

                        Object errorMsg = mapMessage.get("errorMsg");
                        String errorMsgStr = errorMsg == null ? "" : errorMsg.toString();
                        mapMessage.put("errorMsg", errorMsgStr + "<br/>reply id 为" + reply.getReplyId() + "的评论的replyProfileId为空");
                        continue;
                    }
                    try {
                        Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileIdTemp);
                        if (profile != null) {
                            if (!profileMap.containsKey(profile.getProfileId())) {
                                profileMap.put(profile.getProfileId(), profile);
                            }
                        }
                        CommentBean bean = CommentServiceSngl.get().getCommentBeanById(reply.getCommentId());
                        if (bean != null) {
                            if (!commentBeanMap.containsKey(bean.getCommentId())) {
                                commentBeanMap.put(bean.getCommentId(), bean);
                            }
                        }
                    } catch (ServiceException e) {
                        GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);

                        Object errorMsg = mapMessage.get("errorMsg");
                        String errorMsgStr = errorMsg == null ? "" : errorMsg.toString();
                        mapMessage.put("errorMsg", errorMsgStr + "<br/>" + this.getClass().getName() + " occured ServiceException.e: " + e);
                    }
                }
                mapMessage.put("profileMap", profileMap);
                mapMessage.put("commentBeanMap", commentBeanMap);

                mapMessage.put("list", replyRows.getRows());
                mapMessage.put("page", replyRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/forigncontent/sublist", mapMessage);
    }

    @RequestMapping(value = "/replypage")
    public ModelAndView replypage(@RequestParam(value = "replyid", required = false) Long replyId,
                                  @RequestParam(value = "screenname", required = false) String screenName,
                                  @RequestParam(value = "unikey", required = false) String uniKey,
                                  @RequestParam(value = "subkey", required = false) String subKey,
                                  @RequestParam(value = "startdate", required = false) Date startDate,
                                  @RequestParam(value = "enddate", required = false) Date endDate,
                                  @RequestParam(value = "statuscode", required = false) String statusCode,
                                  @RequestParam(value = "refreshStatus", required = false, defaultValue = "0") String refreshStatus,
                                  @RequestParam(value = "hide15Status", required = false, defaultValue = "show") String hide15Status,
                                  @RequestParam(value = "replyStatus", required = false) String replyStatus,
                                  @RequestParam(value = "bodytext", required = false) String bodyText,
                                  @RequestParam(value = "domain", required = false) Integer domain,

                                  @RequestParam(value = "commentid", required = false) String commentId,
                                  @RequestParam(value = "rootid", required = false) String oid,
                                  @RequestParam(value = "rurl", required = false) String rurl) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("screenname", screenName);
        mapMessage.put("unikey", uniKey);
        mapMessage.put("subKey", subKey);
        mapMessage.put("startdate", startDate);
        mapMessage.put("enddate", endDate);
        mapMessage.put("statuscode", statusCode);
        mapMessage.put("domain", domain);
        mapMessage.put("refreshStatus", refreshStatus);
        mapMessage.put("hide15Status", hide15Status);
        mapMessage.put("replyStatus", replyStatus);
        mapMessage.put("replyid", replyId);
        mapMessage.put("bodyText", bodyText);

        mapMessage.put("commentid", commentId);
        mapMessage.put("rootid", oid);
        mapMessage.put("rurl", rurl);

        Map<String, List<Mood>> moodMap = HotdeployConfigFactory.get().getConfig(MoodHotdeployConfig.class).getImageMap();
        Map<String, List<MoodJson>> moodJsonMap = new TreeMap<String, List<MoodJson>>(new Comparator<String>() {
            public int compare(String obj1, String obj2) {
                //降序排序
                return obj2.compareTo(obj1);
            }
        });

        for (Map.Entry<String, List<Mood>> entry : moodMap.entrySet()) {
            List<MoodJson> list = new ArrayList<MoodJson>();
            for (Mood mood : entry.getValue()) {

                MoodJson moodJson = new MoodJson(mood.getCode(), mood.getImgUrl());
                list.add(moodJson);
            }
            moodJsonMap.put(entry.getKey(), list);
        }

        mapMessage.put("moodJsonMap", moodJsonMap);

        ToolsHotdeployConfig hotdeployConfig = HotdeployConfigFactory.get().getConfig(ToolsHotdeployConfig.class);
        mapMessage.put("csList", hotdeployConfig.getCustomerList());

        if (replyId == null) {
            Object errorMsg = mapMessage.get("errorMsg");
            String errorMsgStr = errorMsg == null ? "" : errorMsg.toString();
            mapMessage.put("errorMsg", errorMsgStr + "<br/>replyId为空,无法回复");
            return new ModelAndView("forward:/forign/content/reply/list", mapMessage);
        }
        try {
            CommentReply reply = CommentServiceSngl.get().getCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.REPLY_ID, replyId)));
            if (reply == null) {
                return new ModelAndView("forward:/forign/content/reply/list", mapMessage);
            }
            mapMessage.put("reply", reply);
            CommentBean comment = CommentServiceSngl.get().getCommentBeanById(reply.getCommentId());
            mapMessage.put("comment", comment);
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(reply.getReplyProfileId());
            mapMessage.put("profile", profile);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/forigncontent/replypage", mapMessage);
    }

    @RequestMapping(value = "/reply")
    public ModelAndView reply(@RequestParam(value = "replyids", required = false) String replyids,
                              @RequestParam(value = "screenname", required = false) String screenName,
                              @RequestParam(value = "unikey", required = false) String uniKey,
                              @RequestParam(value = "subkey", required = false) String subKey,
                              @RequestParam(value = "startdate", required = false) Date startDate,
                              @RequestParam(value = "enddate", required = false) Date endDate,
                              @RequestParam(value = "statuscode", required = false) String statusCode,
                              @RequestParam(value = "refreshStatus", required = false, defaultValue = "0") String refreshStatus,
                              @RequestParam(value = "hide15Status", required = false, defaultValue = "show") String hide15Status,
                              @RequestParam(value = "replyStatus", required = false) String replyStatus,
                              @RequestParam(value = "replyCsNickName", required = false) String replyCsNickName,
                              @RequestParam(value = "replyContent", required = false) String replyContent,
                              @RequestParam(value = "domain", required = false) Integer domain,
                              @RequestParam(value = "bodytext", required = false) String bodyText,

                              @RequestParam(value = "commentid", required = false) String commentId,
                              @RequestParam(value = "rootid", required = false) String oid,
                              @RequestParam(value = "rurl", required = false) String rurl,
                              HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("screenname", screenName);
        mapMessage.put("unikey", uniKey);
        mapMessage.put("subKey", subKey);
        mapMessage.put("startdate", startDate);
        mapMessage.put("enddate", endDate);
        mapMessage.put("statuscode", statusCode);
        mapMessage.put("domain", domain);
        mapMessage.put("refreshStatus", refreshStatus);
        mapMessage.put("hide15Status", hide15Status);
        mapMessage.put("replyStatus", replyStatus);
        mapMessage.put("bodyText", bodyText);

        mapMessage.put("commentid", commentId);
        mapMessage.put("rootid", oid);

        ToolsHotdeployConfig hotdeployConfig = HotdeployConfigFactory.get().getConfig(ToolsHotdeployConfig.class);
        List<String> csList = hotdeployConfig.getCustomerList();
        try {

            if (!csList.contains(replyCsNickName)) {
                Object errorMsg = mapMessage.get("errorMsg");
                String errorMsgStr = errorMsg == null ? "" : errorMsg.toString();
                mapMessage.put("errorMsg", errorMsgStr + "<br/>呢称不在允许的列表中,回复失败!");
                return new ModelAndView("forward:/forign/content/reply/list", mapMessage);
            }

            String[] strs = replyids.split("_");
            long preReplyId = Long.parseLong(strs[0]);
            String preRootId = strs[1];
            long preParentId = Long.parseLong(strs[2]);

            String preCommentId = strs[3];
            int preDomain = Integer.parseInt(strs[4]);

            Profile profile = UserCenterServiceSngl.get().getProfileByNick(replyCsNickName);

            String uno = profile.getUno();

            String profileId = profile.getProfileId();

            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(preCommentId);

            long rootId = 0L;
            if (preRootId == null || preRootId.equals("0")) {
                rootId = preReplyId;
            } else {
                rootId = Long.parseLong(preRootId);
            }

            String rootUno = "";
            String rootProfileId = "";
            String rootProfileKey = "";
            int rootTotalRows = 0;
            if (rootId > 0L) {
                CommentReply rootReply = CommentServiceSngl.get().getCommentReplyById(preCommentId, rootId);
                if (rootReply != null && !StringUtil.isEmpty(rootReply.getReplyUno())) {
                    rootUno = rootReply.getReplyUno();
                    rootProfileId = rootReply.getReplyProfileId();
                    rootProfileKey = rootReply.getReplyProfileKey();
                    rootTotalRows = rootReply.getTotalRows();
                }

            }

            long parentId = preReplyId;
            String parentUno = "";
            String parentProfileId = "";
            String parentProfileKey = "";
            if (parentId > 0l) {
                CommentReply parentReply = CommentServiceSngl.get().getCommentReplyById(preCommentId, parentId);
                if (parentReply != null && !StringUtil.isEmpty(parentReply.getReplyUno())) {
                    parentUno = parentReply.getReplyUno();
                    parentProfileId = parentReply.getReplyProfileId();
                    parentProfileKey = parentReply.getReplyProfileKey();
                }

            }

            ReplyBody replyBody = new ReplyBody();
            replyBody.setText(replyContent);
            replyBody.setPic("");

            CommentReply reply = new CommentReply();
            reply.setCommentId(preCommentId);
            if (uniKey.contains("|")) {
                reply.setSubKey(uniKey.substring(0, uniKey.indexOf("|")));
            }

            reply.setReplyUno(uno);
            reply.setReplyProfileId(profileId);
            reply.setReplyProfileKey(profile.getProfileKey());

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
            reply.setCreateIp(getIp());
            reply.setRemoveStatus(ActStatus.ACTING);                 //客服的回复是已审核
            reply.setTotalRows(0);
            //  reply.setFloorNum(commentBean.getTotalRows() + 1);         //楼中楼不在意楼号
            reply.setFloorNum(rootTotalRows + 1);//todo 以后删掉
            reply.setDomain(CommentDomain.getByCode(preDomain));
            reply.setCustomerStatus(2);     //本条即是客服回复
            CommentServiceSngl.get().createCommentReply(reply, commentBean.getTotalRows());

            //原评论的状态,设为客服已回复
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(CommentReplyField.CUSTOMER_STATUS, 1);

            CommentServiceSngl.get().modifyCommentReplyById(preCommentId, preReplyId, updateExpress);

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.COMMENT_REPLY);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP
            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1950) {
                queryString = queryString.substring(0, 1950);
            }
            log.setOpAfter("评论客服回复方法,queryString" + queryString); //描述 推荐用中文
            addLog(log);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        if(StringUtil.isEmpty(rurl)){
            return new ModelAndView("forward:/forign/content/reply/list", mapMessage);
        }else {
            return new ModelAndView("forward:"+rurl, mapMessage);
        }

    }

    @RequestMapping(value = "/hotlist")
    public ModelAndView hotList() {

        //todo
        return new ModelAndView();
    }

    @RequestMapping(value = "/removehot")
    public ModelAndView removeHot() {

        //todo
        return new ModelAndView();
    }


}