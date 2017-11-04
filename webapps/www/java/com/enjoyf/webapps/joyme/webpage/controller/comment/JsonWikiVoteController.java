package com.enjoyf.webapps.joyme.webpage.controller.comment;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.util.MD5Util;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import net.sf.json.JSONObject;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by tonydiao on 2015/1/10.
 */

@Controller
@RequestMapping("/json/comment/wiki/vote/api")
public class JsonWikiVoteController extends BaseRestSpringController {

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    //获取投票信息接口
    @RequestMapping("/getvote")
    @ResponseBody
    public String getvote(@RequestParam(value = "unikey", required = false) String unikey,
                          @RequestParam(value = "callback", required = false) String callback,
                          @RequestParam(value = "uid", required = false) String uid,
                          HttpServletRequest request) {

        if (StringUtil.isEmpty(callback)) {
            callback = "callback_getvote";
        }

        ResultObjectMsg msg = new ResultObjectMsg();
        JSONObject jsonObject = new JSONObject();
        if (StringUtil.isEmpty(unikey)) {
            return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_UNIKEY_ERROR.getJsonString() + ")";
        }
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentBeanField.DOMAIN, CommentDomain.WIKI_VOTE.getCode()));
            queryExpress.add(QueryCriterions.eq(CommentBeanField.UNI_KEY, unikey));
            queryExpress.add(QueryCriterions.eq(CommentBeanField.REMOVE_STATUS, ActStatus.ACTING.getCode()));
            CommentBean commentBean = CommentServiceSngl.get().getCommentBean(queryExpress);

            if (commentBean == null) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_VOTE_NOT_FOUND_ERROR.getJsonString() + ")";
            }
            Map resultMap = new LinkedHashMap();
            resultMap.put("voteid", commentBean.getCommentId());
            resultMap.put("unikey", commentBean.getUniqueKey());
            resultMap.put("title", commentBean.getTitle());

            if (!StringUtil.isEmpty(commentBean.getUri())) {
                Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(commentBean.getUri());
                if (profile != null) {
                    resultMap.put("nick", profile.getNick());
                    resultMap.put("profileid", profile.getProfileId());
                } else {
                    resultMap.put("nick", "");
                    resultMap.put("profileid", "");
                }
            } else {
                resultMap.put("nick", "");
                resultMap.put("profileid", "");
            }
            resultMap.putAll(getMapFromJson(commentBean.getDescription()));
            resultMap.put("totalvotes", commentBean.getTotalRows());
            resultMap.put("createtime", commentBean.getCreateTime().getTime());


            //加入能否投票的信息
            String ipaddr = this.getIp(request);
            int restrict = Integer.valueOf((String) resultMap.get("restrict"));
            if (restrict == 0) {   //无限制
                resultMap.put("canvoteflag", "yes");
            } else if (restrict == 1) {   //一个ip每天投一次
                if (StringUtil.isEmpty(ipaddr)) {
                    return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_IP_ERROR.getJsonString() + ")";
                }
                String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
                if (!Pattern.compile(regex).matcher(ipaddr).matches()) {
                    return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_IP_ERROR.getJsonString() + ")";
                }
                Date todayBegin = DateUtil.ignoreTime(new Date());     //得到今日0:00:00的date值
                QueryExpress queryExpressIP = new QueryExpress();
                queryExpressIP.add(QueryCriterions.eq(CommentHistoryField.DOMAIN, CommentDomain.WIKI_VOTE.getCode()));
                queryExpressIP.add(QueryCriterions.eq(CommentHistoryField.ACTION_TYPE, CommentHistoryType.VOTE.getCode()));
                queryExpressIP.add(QueryCriterions.eq(CommentHistoryField.ACTION_IP, ipaddr));
                queryExpressIP.add(QueryCriterions.geq(CommentHistoryField.ACTION_DATE, todayBegin));
                queryExpressIP.add(QueryCriterions.eq(CommentHistoryField.COMMENT_ID, commentBean.getCommentId()));
                List<CommentHistory> listIP = CommentServiceSngl.get().queryCommentHistory(queryExpressIP);

                if (listIP != null && listIP.size() > 0) {
                    resultMap.put("canvoteflag", "no");
                } else {
                    resultMap.put("canvoteflag", "yes");
                }
            } else if (restrict == 2) {   //一个用户投一次
                if (StringUtil.isEmpty(uid)) {
                    return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_UID_ERROR.getJsonString() + ")";
                }
                long uidLong = -1;
                try {
                    uidLong = Long.parseLong(uid);
                } catch (NumberFormatException e) {
                }
                if (uidLong == -1) {
                    return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_UID_ERROR.getJsonString() + ")";
                }

                Profile profile = UserCenterServiceSngl.get().getProfileByUid(uidLong);

                if (profile == null) {
                    return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_UID_ERROR.getJsonString() + ")";
                }

                QueryExpress queryExpressUser = new QueryExpress();
                queryExpressUser.add(QueryCriterions.eq(CommentHistoryField.DOMAIN, CommentDomain.WIKI_VOTE.getCode()));
                queryExpressUser.add(QueryCriterions.eq(CommentHistoryField.ACTION_TYPE, CommentHistoryType.VOTE.getCode()));
                queryExpressUser.add(QueryCriterions.eq(CommentHistoryField.PROFILE_ID, profile.getProfileId()));
                queryExpressUser.add(QueryCriterions.eq(CommentHistoryField.COMMENT_ID, commentBean.getCommentId()));
                List<CommentHistory> listUser = CommentServiceSngl.get().queryCommentHistory(queryExpressUser);

                if (listUser != null && listUser.size() > 0) {
                    resultMap.put("canvoteflag", "no");
                } else {
                    resultMap.put("canvoteflag", "yes");
                }
            } else if (restrict == 3) {   //一个浏览器限投一次
                resultMap.put("canvoteflag", "uncertain");
            }


            //加入投票选项信息
            QueryExpress queryExpressOption = new QueryExpress();
            queryExpressOption.add(QueryCriterions.eq(CommentVoteOptionField.COMMENT_ID, commentBean.getCommentId()));
            queryExpressOption.add(QueryCriterions.eq(CommentVoteOptionField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            queryExpressOption.add(new QuerySort(CommentVoteOptionField.DISPLAY_ORDER, QuerySortOrder.ASC));

            List<CommentVoteOption> optionList = CommentServiceSngl.get().queryCommentVoteOption(queryExpressOption);
            if (optionList != null && optionList.size() > 0) {
                resultMap.put("optionnums", optionList.size());
                List<Map> optionMapList = new ArrayList<Map>();
                for (int i = 0; i < optionList.size(); i++) {
                    CommentVoteOption temp = optionList.get(i);
                    Map tempMap = new LinkedHashMap();
                    tempMap.put("optionid", temp.getVoteOptionId());
                    tempMap.put("voteid", temp.getCommentId());
                    tempMap.put("title", temp.getTitle());
                    tempMap.put("pic", temp.getPic());
                    tempMap.put("optiontotal", temp.getOptionTotal());
                    tempMap.put("displayorder", temp.getDisplayOrder());
                    tempMap.put("createtime", temp.getCreateTime().getTime());
                    optionMapList.add(tempMap);
                }
                resultMap.put("options", optionMapList);
            } else {
                resultMap.put("optionnums", 0);
                resultMap.put("options", "");
            }
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            jsonObject.put("result", resultMap);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            msg.setResult("");
            return callback + "(" + com.enjoyf.platform.service.JsonBinder.buildNormalBinder().toJson(msg) + ")";
        }
        return callback + "(" + jsonObject.toString() + ")";
    }


    //获取参与某个投票的所有用户
    @RequestMapping("/getallusers")
    @ResponseBody
    public String getallusers(@RequestParam(value = "unikey", required = false) String unikey,
                              @RequestParam(value = "callback", required = false) String callback,
                              HttpServletRequest request) {

        if (StringUtil.isEmpty(callback)) {
            callback = "callback_getallusers";
        }

        ResultObjectMsg msg = new ResultObjectMsg();
        JSONObject jsonObject = new JSONObject();
        if (StringUtil.isEmpty(unikey)) {
            return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_UNIKEY_ERROR.getJsonString() + ")";
        }
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentBeanField.DOMAIN, CommentDomain.WIKI_VOTE.getCode()));
            queryExpress.add(QueryCriterions.eq(CommentBeanField.UNI_KEY, unikey));
            queryExpress.add(QueryCriterions.eq(CommentBeanField.REMOVE_STATUS, ActStatus.ACTING.getCode()));
            CommentBean commentBean = CommentServiceSngl.get().getCommentBean(queryExpress);

            if (commentBean == null) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_VOTE_NOT_FOUND_ERROR.getJsonString() + ")";
            }
            Map resultMap = new LinkedHashMap();


            resultMap.put("voteid", commentBean.getCommentId());
            resultMap.put("unikey", commentBean.getUniqueKey());
            resultMap.put("title", commentBean.getTitle());


            Map descriptionMap = getMapFromJson(commentBean.getDescription());

            int restrict = Integer.valueOf((String) descriptionMap.get("restrict"));
            resultMap.put("restrict", restrict);

            if (restrict == 2) {
                QueryExpress queryExpressUsers = new QueryExpress();
                queryExpressUsers.add(QueryCriterions.eq(CommentHistoryField.DOMAIN, CommentDomain.WIKI_VOTE.getCode()));
                queryExpressUsers.add(QueryCriterions.eq(CommentHistoryField.ACTION_TYPE, CommentHistoryType.VOTE.getCode()));
                queryExpressUsers.add(QueryCriterions.eq(CommentHistoryField.ACTION_TIMES,2));
                queryExpressUsers.add(QueryCriterions.eq(CommentHistoryField.COMMENT_ID, commentBean.getCommentId()));
                List<CommentHistory> listUsers = CommentServiceSngl.get().queryCommentHistory(queryExpressUsers);

                if(listUsers!=null) {

                    List<JSONObject>  jsonList=new ArrayList<JSONObject>();
                    List<String>  ids=new ArrayList<String>();

                    for(int i=0;i<listUsers.size();i++){

                       Profile profile= UserCenterServiceSngl.get().getProfileByProfileId(listUsers.get(i).getProfileId());

                        if(profile!=null&& !ids.contains(profile.getProfileId())) {
                            JSONObject jsonObjectTemp = new JSONObject();
                            jsonObjectTemp.put("profileid", profile.getProfileId());
                            jsonObjectTemp.put("uid", profile.getUid());
                            jsonObjectTemp.put("nick", profile.getNick());
                            jsonObjectTemp.put("uno", profile.getUno());
                            jsonObjectTemp.put("profilekey", profile.getProfileKey());
                            jsonList.add(jsonObjectTemp);
                            ids.add(profile.getProfileId());
                        }
                    }
                    resultMap.put("total", ids.size());
                    resultMap.put("users",jsonList);

                }
            }
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            jsonObject.put("result", resultMap);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            msg.setResult("");
            return callback + "(" + com.enjoyf.platform.service.JsonBinder.buildNormalBinder().toJson(msg) + ")";
        }
        return callback + "(" + jsonObject.toString() + ")";
    }


    //获取投票选项及各选项得票数接口 ----暂时无用
    @RequestMapping("/getitem")
    @ResponseBody
    public String getitem(@RequestParam(value = "unikey", required = false) String unikey,
                          @RequestParam(value = "callback", required = false) String callback,
                          HttpServletRequest request) {

        if (StringUtil.isEmpty(callback)) {
            callback = "callback_getitem";
        }

        ResultObjectMsg msg = new ResultObjectMsg();
        JSONObject jsonObject = new JSONObject();
        if (StringUtil.isEmpty(unikey)) {
            return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_UNIKEY_ERROR.getJsonString() + ")";
        }
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentBeanField.DOMAIN, CommentDomain.WIKI_VOTE.getCode()));
            queryExpress.add(QueryCriterions.eq(CommentBeanField.UNI_KEY, unikey));
            queryExpress.add(QueryCriterions.eq(CommentBeanField.REMOVE_STATUS, ActStatus.ACTING.getCode()));
            CommentBean commentBean = CommentServiceSngl.get().getCommentBean(queryExpress);

            if (commentBean == null) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_VOTE_NOT_FOUND_ERROR.getJsonString() + ")";
            }
            Map resultMap = new LinkedHashMap();
            resultMap.put("voteid", commentBean.getCommentId());
            resultMap.put("unikey", commentBean.getUniqueKey());
            resultMap.put("title", commentBean.getTitle());

            if (!StringUtil.isEmpty(commentBean.getUri())) {
                Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(commentBean.getUri());
                if (profile != null) {
                    resultMap.put("nick", profile.getNick());
                    resultMap.put("profileid", profile.getProfileId());
                } else {
                    resultMap.put("nick", "");
                    resultMap.put("profileid", "");
                }
            } else {
                resultMap.put("nick", "");
                resultMap.put("profileid", "");
            }

            resultMap.putAll(getMapFromJson(commentBean.getDescription()));

            resultMap.put("totalvotes", commentBean.getTotalRows());
            resultMap.put("createtime", commentBean.getCreateTime().getTime());

            QueryExpress queryExpressOption = new QueryExpress();
            queryExpressOption.add(QueryCriterions.eq(CommentVoteOptionField.COMMENT_ID, commentBean.getCommentId()));
            queryExpressOption.add(QueryCriterions.eq(CommentVoteOptionField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            queryExpressOption.add(new QuerySort(CommentVoteOptionField.DISPLAY_ORDER, QuerySortOrder.ASC));

            List<CommentVoteOption> optionList = CommentServiceSngl.get().queryCommentVoteOption(queryExpressOption);
            if (optionList != null && optionList.size() > 0) {
                resultMap.put("optionnums", optionList.size());
                List<Map> optionMapList = new ArrayList<Map>();
                for (int i = 0; i < optionList.size(); i++) {
                    CommentVoteOption temp = optionList.get(i);
                    Map tempMap = new LinkedHashMap();
                    tempMap.put("optionid", temp.getVoteOptionId());
                    tempMap.put("voteid", temp.getCommentId());
                    tempMap.put("title", temp.getTitle());
                    tempMap.put("pic", temp.getPic());
                    tempMap.put("optiontotal", temp.getOptionTotal());
                    tempMap.put("displayorder", temp.getDisplayOrder());
                    tempMap.put("createtime", temp.getCreateTime().getTime());
                    optionMapList.add(tempMap);
                }
                resultMap.put("options", optionMapList);
            } else {
                resultMap.put("optionnums", 0);
                resultMap.put("options", "");
            }
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            jsonObject.put("result", resultMap);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            msg.setResult("");
            return callback + "(" + com.enjoyf.platform.service.JsonBinder.buildNormalBinder().toJson(msg) + ")";
        }
        return callback + "(" + jsonObject.toString() + ")";
    }


    //是否可以再次投票接口
    @RequestMapping("/canvote")
    @ResponseBody
    public String canvote(@RequestParam(value = "unikey", required = false) String unikey,
                          @RequestParam(value = "uid", required = false) String uid,
                          @RequestParam(value = "callback", required = false) String callback,
                          @RequestParam(value = "ipaddr", required = false) String ipaddr,
                          HttpServletRequest request) {
        if (StringUtil.isEmpty(callback)) {
            callback = "callback_canvote";
        }
        ResultObjectMsg msg = new ResultObjectMsg();
        JSONObject jsonObject = new JSONObject();
        if (StringUtil.isEmpty(unikey)) {
            return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_UNIKEY_ERROR.getJsonString() + ")";
        }
        try {

            ipaddr = this.getIp(request);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentBeanField.DOMAIN, CommentDomain.WIKI_VOTE.getCode()));
            queryExpress.add(QueryCriterions.eq(CommentBeanField.UNI_KEY, unikey));
            CommentBean commentBean = CommentServiceSngl.get().getCommentBean(queryExpress);

            if (commentBean == null) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_VOTE_NOT_FOUND_ERROR.getJsonString() + ")";
            }
            Map descriptionMap = getMapFromJson(commentBean.getDescription());

            int restrict = Integer.valueOf((String) descriptionMap.get("restrict"));
            Map resultMap = new HashMap();

            if (restrict == 0) {   //无限制
                resultMap.put("canvoteflag", "yes");
            } else if (restrict == 1) {   //一个ip每天投一次
                if (StringUtil.isEmpty(ipaddr)) {
                    return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_IP_ERROR.getJsonString() + ")";
                }

                String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
                if (!Pattern.compile(regex).matcher(ipaddr).matches()) {
                    return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_IP_ERROR.getJsonString() + ")";
                }
                Date todayBegin = DateUtil.ignoreTime(new Date());     //得到今日0:00:00的date值
                QueryExpress queryExpressIP = new QueryExpress();
                queryExpressIP.add(QueryCriterions.eq(CommentHistoryField.DOMAIN, CommentDomain.WIKI_VOTE.getCode()));
                queryExpressIP.add(QueryCriterions.eq(CommentHistoryField.ACTION_TYPE, CommentHistoryType.VOTE.getCode()));
                queryExpressIP.add(QueryCriterions.eq(CommentHistoryField.ACTION_IP, ipaddr));
                queryExpressIP.add(QueryCriterions.geq(CommentHistoryField.ACTION_DATE, todayBegin));
                queryExpressIP.add(QueryCriterions.eq(CommentHistoryField.COMMENT_ID, commentBean.getCommentId()));
                List<CommentHistory> listIP = CommentServiceSngl.get().queryCommentHistory(queryExpressIP);

                if (listIP != null && listIP.size() > 0) {
                    resultMap.put("canvoteflag", "no");
                } else {
                    resultMap.put("canvoteflag", "yes");
                }

            } else if (restrict == 2) {   //一个用户投一次
                if (StringUtil.isEmpty(uid)) {
                    return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_UID_ERROR.getJsonString() + ")";
                }
                long uidLong = -1;
                try {
                    uidLong = Long.parseLong(uid);
                } catch (NumberFormatException e) {
                }
                if (uidLong == -1) {
                    return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_UID_ERROR.getJsonString() + ")";
                }

                Profile profile = UserCenterServiceSngl.get().getProfileByUid(uidLong);

                if (profile == null) {
                    return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_UID_ERROR.getJsonString() + ")";
                }

                QueryExpress queryExpressUser = new QueryExpress();
                queryExpressUser.add(QueryCriterions.eq(CommentHistoryField.DOMAIN, CommentDomain.WIKI_VOTE.getCode()));
                queryExpressUser.add(QueryCriterions.eq(CommentHistoryField.ACTION_TYPE, CommentHistoryType.VOTE.getCode()));
                queryExpressUser.add(QueryCriterions.eq(CommentHistoryField.PROFILE_ID, profile.getProfileId()));
                queryExpressUser.add(QueryCriterions.eq(CommentHistoryField.COMMENT_ID, commentBean.getCommentId()));
                List<CommentHistory> listUser = CommentServiceSngl.get().queryCommentHistory(queryExpressUser);

                if (listUser != null && listUser.size() > 0) {
                    resultMap.put("canvoteflag", "no");
                } else {
                    resultMap.put("canvoteflag", "yes");
                }
            } else if (restrict == 3) {   //一个浏览器限投一次
                resultMap.put("canvoteflag", "uncertain");
            }
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            jsonObject.put("result", resultMap);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            msg.setResult("");
            return callback + "(" + com.enjoyf.platform.service.JsonBinder.buildNormalBinder().toJson(msg) + ")";
        }

        return callback + "(" + jsonObject.toString() + ")";
    }


    //投票接口
    @RequestMapping("/vote")
    @ResponseBody
    public String vote(@RequestParam(value = "unikey", required = false) String unikey,
                       @RequestParam(value = "uid", required = false) String uid,
                       @RequestParam(value = "ipaddr", required = false) String ipaddr,
                       @RequestParam(value = "callback", required = false) String callback,
                       @RequestParam(value = "voteoptionids", required = false) String voteoptionids,
                       HttpServletRequest request) {
        if (StringUtil.isEmpty(callback)) {
            callback = "callback_vote";
        }
        ResultObjectMsg msg = new ResultObjectMsg();
        JSONObject jsonObject = new JSONObject();

        try {
            if (StringUtil.isEmpty(unikey)) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_UNIKEY_ERROR.getJsonString() + ")";
            }

            ipaddr = this.getIp(request);
            if (StringUtil.isEmpty(ipaddr)) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_IP_ERROR.getJsonString() + ")";
            }
            String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
            if (!Pattern.compile(regex).matcher(ipaddr).matches()) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_IP_ERROR.getJsonString() + ")";
            }


            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentBeanField.DOMAIN, CommentDomain.WIKI_VOTE.getCode()));
            queryExpress.add(QueryCriterions.eq(CommentBeanField.UNI_KEY, unikey));
            CommentBean commentBean = CommentServiceSngl.get().getCommentBean(queryExpress);

            if (commentBean == null) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_VOTE_NOT_FOUND_ERROR.getJsonString() + ")";
            }
            Map descriptionMap = getMapFromJson(commentBean.getDescription());

            Date starttimeDate = new Date((Long) descriptionMap.get("starttime"));
            Date endTimeDate = new Date((Long) descriptionMap.get("endtime"));
            Date now = new Date();

            //投票时间必须在限定时间区间内
            if (now.before(starttimeDate) || now.after(endTimeDate)) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_VOTE_TIME_EXCEED_LIMIT_ERROR.getJsonString() + ")";
            }

            if (StringUtil.isEmpty(voteoptionids)) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_VOTEOPTIONIDS_EMPTY_ERROR.getJsonString() + ")";
            }

            //如果是多选，但只传过来一个id,这样是可以的。 如果是单选，但传过来多余一个id,这是不允许的。
            String choosetype = (String) descriptionMap.get("choosetype");
            String[] voteOptionIdArray = voteoptionids.split("_");
            if (choosetype.equals("0") && voteOptionIdArray.length > 1) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_VOTEOPTIONIDS_AMOUNT_ERROR.getJsonString() + ")";
            }


            QueryExpress queryExpressForOption = new QueryExpress();
            queryExpressForOption.add(QueryCriterions.eq(CommentVoteOptionField.COMMENT_ID, commentBean.getCommentId()));
            queryExpressForOption.add(QueryCriterions.eq(CommentVoteOptionField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            List<CommentVoteOption> optionList = CommentServiceSngl.get().queryCommentVoteOption(queryExpressForOption);

            List<String> optionIdList = new ArrayList<String>();
            if (optionList == null || optionList.size() == 0) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_VOTE_HAS_NO_OPTIONS_ERROR.getJsonString() + ")";
            }
            for (int i = 0; i < optionList.size(); i++) {
                optionIdList.add(optionList.get(i).getVoteOptionId());
            }

            for (int i = 0; i < voteOptionIdArray.length; i++) {
                if (!optionIdList.contains(voteOptionIdArray[i])) {
                    return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_VOTEOPTIONIDS_VALID_ERROR.getJsonString() + ")";
                }
            }

            int restrict = Integer.valueOf((String) descriptionMap.get("restrict"));
            Profile profile = null;

            if (restrict == 1) {
                Date todayBegin = DateUtil.ignoreTime(new Date());     //得到今日0:00:00的date值
                QueryExpress queryExpressIP = new QueryExpress();
                queryExpressIP.add(QueryCriterions.eq(CommentHistoryField.DOMAIN, CommentDomain.WIKI_VOTE.getCode()));
                queryExpressIP.add(QueryCriterions.eq(CommentHistoryField.ACTION_TYPE, CommentHistoryType.VOTE.getCode()));
                queryExpressIP.add(QueryCriterions.eq(CommentHistoryField.ACTION_IP, ipaddr));
                queryExpressIP.add(QueryCriterions.geq(CommentHistoryField.ACTION_DATE, todayBegin));
                queryExpressIP.add(QueryCriterions.eq(CommentHistoryField.COMMENT_ID, commentBean.getCommentId()));
                List<CommentHistory> listIP = CommentServiceSngl.get().queryCommentHistory(queryExpressIP);
                if (listIP != null && listIP.size() > 0) {
                    return callback + "(" + ResultCodeConstants.WIKI_VOTE_VOTE_IP_RESTRICT_VIOLATE_ERROR.getJsonString() + ")";
                }

            } else if (restrict == 2) {    //一个用户只能投一次,此时校验uid,其他时间不校验
                if (StringUtil.isEmpty(uid)) {
                    return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_UID_ERROR.getJsonString() + ")";
                }
                long uidLong = -1;
                try {
                    uidLong = Long.parseLong(uid);
                } catch (NumberFormatException e) {
                }
                if (uidLong == -1) {
                    return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_UID_ERROR.getJsonString() + ")";
                }
                profile = UserCenterServiceSngl.get().getProfileByUid(uidLong);
                if (profile == null) {
                    return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_UID_ERROR.getJsonString() + ")";
                }

                QueryExpress queryExpressUser = new QueryExpress();
                queryExpressUser.add(QueryCriterions.eq(CommentHistoryField.DOMAIN, CommentDomain.WIKI_VOTE.getCode()));
                queryExpressUser.add(QueryCriterions.eq(CommentHistoryField.ACTION_TYPE, CommentHistoryType.VOTE.getCode()));
                queryExpressUser.add(QueryCriterions.eq(CommentHistoryField.PROFILE_ID, profile.getProfileId()));
                queryExpressUser.add(QueryCriterions.eq(CommentHistoryField.COMMENT_ID, commentBean.getCommentId()));
                List<CommentHistory> listUser = CommentServiceSngl.get().queryCommentHistory(queryExpressUser);

                if (listUser != null && listUser.size() > 0) {
                    return callback + "(" + ResultCodeConstants.WIKI_VOTE_VOTE_PROFILE_ID_RESTRICT_VIOLATE_ERROR.getJsonString() + ")";
                }

            }

            for (int i = 0; i < voteOptionIdArray.length; i++) {
                CommentHistory commentHistory = new CommentHistory();
                commentHistory.setDomain(CommentDomain.WIKI_VOTE);
                commentHistory.setHistoryType(CommentHistoryType.VOTE);
                if (restrict == 2) {
                    commentHistory.setProfileId(profile.getProfileId());
                } else {
                    commentHistory.setProfileId("");
                }
                commentHistory.setCommentId(commentBean.getCommentId());
                commentHistory.setActionIp(ipaddr);
                commentHistory.setActionDate(now);
                commentHistory.setActionTimes(restrict);     //与Restrict的值相同
                commentHistory.setObjectId(voteOptionIdArray[i]);
                CommentServiceSngl.get().createCommentHistory(commentHistory, commentBean, null);

                //comment_vote_option表option_total+1
                UpdateExpress updateExpressForOption = new UpdateExpress();
                updateExpressForOption.increase(CommentVoteOptionField.OPTION_TOTAL, 1L);
                QueryExpress queryExpressForOptionUpdate = new QueryExpress();
                queryExpressForOptionUpdate.add(QueryCriterions.eq(CommentVoteOptionField.VOTE_OPTION_ID, voteOptionIdArray[i]));
                boolean resultForOption = CommentServiceSngl.get().modifyCommentVoteOption(updateExpressForOption, queryExpressForOptionUpdate);

            }

            //comment_bean表 total_rows+1
            UpdateExpress updateExpressForVote = new UpdateExpress();
            updateExpressForVote.increase(CommentBeanField.TOTAL_ROWS, 1);
            boolean resultForVote = CommentServiceSngl.get().modifyCommentBeanById(commentBean.getCommentId(), updateExpressForVote);

            Map resultMap = new HashMap();
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            //  jsonObject.put("result", resultMap);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            msg.setResult("");
            return callback + "(" + com.enjoyf.platform.service.JsonBinder.buildNormalBinder().toJson(msg) + ")";
        }
        return callback + "(" + jsonObject.toString() + ")";
    }

    //自助创建投票接口
    @RequestMapping("/create")
    @ResponseBody
    public String create(@RequestParam(value = "uid", required = false) String uid,
                         @RequestParam(value = "votetitle", required = false) String votetitle,
                         @RequestParam(value = "choosetype", required = false) String choosetype,
                         @RequestParam(value = "maxchooseitems", required = false) String maxchooseitems,
                         @RequestParam(value = "restrict", required = false) String restrict,
                         @RequestParam(value = "resultvisible", required = false) String resultvisible,
                         @RequestParam(value = "starttime", required = false) String starttime,
                         @RequestParam(value = "endtime", required = false) String endtime,
                         @RequestParam(value = "optiontitles", required = false) String optiontitles,
                         @RequestParam(value = "pics", required = false) String pics,
                         @RequestParam(value = "callback", required = false) String callback,
                         HttpServletRequest request) {
        if (StringUtil.isEmpty(callback)) {
            callback = "callback_create";
        }
        ResultObjectMsg msg = new ResultObjectMsg();
        JSONObject jsonObject = new JSONObject();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            CommentBean commentBean = new CommentBean();
            if (StringUtil.isEmpty(uid)) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_UID_ERROR.getJsonString() + ")";
            }
            long uidLong = -1;
            try {
                uidLong = Long.parseLong(uid);
            } catch (NumberFormatException e) {
            }
            if (uidLong == -1) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_UID_ERROR.getJsonString() + ")";
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uidLong);
            if (profile == null) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_PARAM_UID_ERROR.getJsonString() + ")";
            }

            commentBean.setUri(profile.getProfileId());
            if (StringUtil.isEmpty(votetitle) || votetitle.trim().equals("")) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_CREATE_VOTETITLE_ERROR.getJsonString() + ")";
            }

            if (StringUtil.isEmpty(choosetype) || !(choosetype.equals("0") || choosetype.equals("1"))) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_CREATE_CHOOSETYPE_ERROR.getJsonString() + ")";
            }
            if (StringUtil.isEmpty(maxchooseitems)) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_CREATE_MAXCHOOSEITEMS_ERROR.getJsonString() + ")";
            }
            int maxchooseitemsInt = 0;
            try {
                maxchooseitemsInt = Integer.valueOf(maxchooseitems);

            } catch (NumberFormatException e) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_CREATE_MAXCHOOSEITEMS_ERROR.getJsonString() + ")";
            }
            if (maxchooseitemsInt > 10 || maxchooseitemsInt <= 0) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_CREATE_MAXCHOOSEITEMS_ERROR.getJsonString() + ")";
            }

            if (StringUtil.isEmpty(restrict) || !(restrict.equals("0") || restrict.equals("1") || restrict.equals("2") || restrict.equals("3"))) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_CREATE_RESTRICT_ERROR.getJsonString() + ")";
            }

            if (StringUtil.isEmpty(resultvisible) || !(resultvisible.equals("0") || resultvisible.equals("1") || resultvisible.equals("2"))) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_CREATE_RESULTVISIBLE_ERROR.getJsonString() + ")";
            }
            if (StringUtil.isEmpty(starttime)) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_CREATE_STARTTIME_ERROR.getJsonString() + ")";
            }

            Date todayBegin = DateUtil.ignoreTime(new Date());
            Date starttimeDate = null;
            Long starttimeLong = 0L;
            try {
                // starttimeDate = sdf.parse(starttime);
                starttimeLong = Long.parseLong(starttime);
            } catch (NumberFormatException e) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_CREATE_STARTTIME_ERROR.getJsonString() + ")";
            }
            if (starttimeLong < todayBegin.getTime()) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_CREATE_STARTTIME_ERROR.getJsonString() + ")";
            }

            starttimeDate = new Date(starttimeLong);

            if (StringUtil.isEmpty(endtime)) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_CREATE_ENDTIME_ERROR.getJsonString() + ")";
            }

            Date endtimeDate = null;
            Long endtimeLong = 0L;
            try {
                //  endtimeDate = sdf.parse(endtime);
                endtimeLong = Long.parseLong(endtime);
            } catch (NumberFormatException e) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_CREATE_ENDTIME_ERROR.getJsonString() + ")";
            }

            if (endtimeLong > 2145887999000L) {     //不能大于  2037-12-31 23:59:59
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_CREATE_ENDTIME_ERROR.getJsonString() + ")";
            }

            endtimeDate = new Date(endtimeLong);

            if (endtimeDate.getTime() <= starttimeDate.getTime()) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_CREATE_ENDTIME_LESS_THAN_STARTTIME_ERROR.getJsonString() + ")";
            }


            String description = "{\"choosetype\":\"" + choosetype + "\",\"maxchooseitems\":\"" + maxchooseitems + "\",\"restrict\":\"" + restrict + "\",\"resultvisible\":\"" + resultvisible;
            description += "\",\"starttime\":\"" + sdf.format(starttimeDate) + "\",\"endtime\":\"" + sdf.format(endtimeDate) + "\"}";

            commentBean.setUniqueKey(UUID.randomUUID().toString());   //UUID 结果
            commentBean.setDomain(CommentDomain.WIKI_VOTE);
            commentBean.setTitle(votetitle);
            commentBean.setRemoveStatus(ActStatus.ACTING);
            commentBean.setDescription(description);

            if (StringUtil.isEmpty(optiontitles)) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_CREATE_OPTIONTITLES_ERROR.getJsonString() + ")";
            }
            String[] optiontitlesArray = null;
            optiontitlesArray = optiontitles.split(",");

            String[] picsArray = null;
            if (!StringUtil.isEmpty(pics)) {
                picsArray = pics.split(",");
            }

            if (optiontitlesArray == null || (picsArray != null && optiontitlesArray.length < picsArray.length)) {
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_CREATE_OPTIONTITLES_NOT_MATCH_PICS_ERROR.getJsonString() + ")";
            }


            Set<String> optiontitlesSet = new HashSet<String>();
            for (int i = 0; i < optiontitlesArray.length; i++) {
                optiontitlesSet.add(optiontitlesArray[i]);
            }

            if (optiontitlesSet.size() < optiontitlesArray.length) {    //判断是否有重复的title
                return callback + "(" + ResultCodeConstants.WIKI_VOTE_CREATE_OPTIONTITLES_ERROR.getJsonString() + ")";
            }

            commentBean = CommentServiceSngl.get().createCommentBean(commentBean);

            String commentBeanId = MD5Util.Md5(commentBean.getUniqueKey() + commentBean.getDomain().getCode());
            Long result = new Date().getTime() - sdf.parse("2010-01-01 00:00:00").getTime();
            result = result / 1000;
            int autoDisplayOrder = Integer.MAX_VALUE - result.intValue();     //0到1989158807 之间的一个数

            for (int i = 0; i < optiontitlesArray.length; i++) {
                CommentVoteOption commentVoteOption = new CommentVoteOption();
                commentVoteOption.setVoteOptionId(UUID.randomUUID().toString());
                commentVoteOption.setCommentId(commentBeanId);
                commentVoteOption.setDisplayOrder(Long.valueOf(autoDisplayOrder + 10 * i));
                commentVoteOption.setCreateTime(new Date());
                //新增投票选项的status都是valid
                commentVoteOption.setRemoveStatus(ValidStatus.VALID.getCode());
                commentVoteOption.setTitle(optiontitlesArray[i]);
                if (picsArray != null && i < picsArray.length) {
                    commentVoteOption.setPic(picsArray[i]);
                }
                commentVoteOption.setCreateUser(profile.getProfileId());
                CommentServiceSngl.get().createCommentVoteOption(commentVoteOption);
            }

            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            Map resultMap = new HashMap();
            resultMap.put("voteid", commentBeanId);
            resultMap.put("unikey", commentBean.getUniqueKey());
            resultMap.put("title", commentBean.getTitle());
            jsonObject.put("result", resultMap);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            msg.setResult("");
            return callback + "(" + com.enjoyf.platform.service.JsonBinder.buildNormalBinder().toJson(msg) + ")";
        }

        return callback + "(" + jsonObject.toString() + ")";
    }

    private static Map getMapFromJson(String jsonString) {
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map map = new LinkedHashMap();
        for (Iterator iter = jsonObject.keys(); iter.hasNext(); ) {
            String key = (String) iter.next();
            if (key.equals("starttime") || key.equals("endtime")) {
                String dateStr = (String) jsonObject.get(key);
                try {
                    map.put(key, sdf.parse(dateStr).getTime());
                } catch (ParseException e) {
                    map.put(key, jsonObject.get(key));
                }
            } else {
                map.put(key, jsonObject.get(key));
            }
        }
        return map;
    }
}
