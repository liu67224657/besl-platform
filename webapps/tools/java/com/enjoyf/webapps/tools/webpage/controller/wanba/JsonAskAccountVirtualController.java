package com.enjoyf.webapps.tools.webpage.controller.wanba;

import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.service.comment.CommentReply;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.comment.CommentUtil;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.webapps.tools.weblogic.wanba.AskAccountVirtualWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.gson.Gson;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhimingli on 2016/11/17 0017.
 */
@Controller
@RequestMapping(value = "/wanba/virtual")
public class JsonAskAccountVirtualController extends AbstractWanbaController {

    @Resource(name = "askAccountVirtualWebLogic")
    private AskAccountVirtualWebLogic askAccountVirtualWebLogic;


    // 限时提问
    @ResponseBody
    @RequestMapping(value = "/postquestion")
    public String postquestion(@RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "text", required = false) String text,
                               @RequestParam(value = "askpid", required = false) String askpid,
                               @RequestParam(value = "timelimit", required = false) String timelimit,
                               @RequestParam(value = "tagid", required = false) String tagid,
                               @RequestParam(value = "releasetime", required = false) String releasetime,
                               @RequestParam(value = "timeid", required = false) String timeid,
                               HttpServletRequest request, HttpServletResponse response) {
        String returnStr = null;
        if (StringUtil.isEmpty(releasetime)) {
            returnStr = askAccountVirtualWebLogic.postTimeQuestion(title, text, askpid, timelimit, tagid);
        } else {
            QuestionRelease questionRelease = new QuestionRelease();

            //发布时间
            Date reletime = null;
            try {
                //组装questionRelease对象
                questionRelease.setTitle(title);
                questionRelease.setText(text);
                questionRelease.setAskpid(askpid);
                questionRelease.setTimelimit(timelimit);
                questionRelease.setTagid(tagid);


                reletime = DateUtil.formatStringToDate(releasetime, DateUtil.DEFAULT_DATE_FORMAT2);

                //定时发布
                if (StringUtil.isEmpty(timeid)) {

                    AskTimedRelease release = new AskTimedRelease();
                    release.setTimeRelseaseType(TimeRelseaseType.QUESTION);
                    release.setTitle(title);
                    release.setCreateTime(new Date());
                    release.setReleaseTime(reletime);
                    release.setExtStr(questionRelease.toJson());

                    AskServiceSngl.get().insertAskTimedRelease(release);
                } else {
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.set(AskTimedReleaseField.TITLE, title);
                    updateExpress.set(AskTimedReleaseField.RELEASETIME, reletime);
                    updateExpress.set(AskTimedReleaseField.EXTSTR, new Gson().toJson(questionRelease));
                    AskServiceSngl.get().modifyAskTimedRelease(updateExpress, Long.valueOf(timeid));
                }
                returnStr = ResultCodeConstants.SUCCESS.getJsonString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        CookieUtil.setCookie(request, response, TIME_VIRTUAL_ASKPID, askpid);
        CookieUtil.setCookie(request, response, TIME_VIRTUAL_TAGID, tagid);
        CookieUtil.setCookie(request, response, TIME_VIRTUAL_TIMELIMIT, timelimit);

        return returnStr;
    }

    //1对1 提问
    @ResponseBody
    @RequestMapping(value = "/postquestioninvite")
    public String postquestioninvite(@RequestParam(value = "title", required = false) String title,
                                     @RequestParam(value = "text", required = false) String text,
                                     @RequestParam(value = "pid", required = false) String pid,
                                     @RequestParam(value = "invitepid", required = false) String invitepid,
                                     @RequestParam(value = "tagid", required = false) String tagid,
                                     HttpServletRequest request, HttpServletResponse response) {
        String returnStr = askAccountVirtualWebLogic.postOneOnOneQuestion(title, text, pid, invitepid);
        CookieUtil.setCookie(request, response, ONE_VIRTUAL_ASKPID, pid);
        CookieUtil.setCookie(request, response, ONE_VIRTUAL_TAGID, tagid);
        CookieUtil.setCookie(request, response, ONE_VIRTUAL_INVITEPID, invitepid);
        return returnStr;
    }


    //1对1 提问
    @ResponseBody
    @RequestMapping(value = "/listbytagid")
    public String listbytagid(@RequestParam(value = "tagid", required = false) String tagid,
                              HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        int i = 0;
        Pagination pagination = null;
        Set<String> profileSet = new HashSet<String>();
        JSONObject result = new JSONObject();
        try {
            do {
                i = i + 1;
                pagination = new Pagination(200 * i, i, 200);
                PageRows<VerifyProfile> pageRows = UserCenterServiceSngl.get().queryVerifyProfileByTag(Long.valueOf(tagid), pagination);
                if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                    for (VerifyProfile verifyProfile : pageRows.getRows()) {
                        profileSet.add(verifyProfile.getNick() + "____" + verifyProfile.getProfileId());
                    }
                }
            } while (!pagination.isLastPage());

            result.put("profileSet", profileSet);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
        jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
        jsonObject.put("result", result);
        return jsonObject.toString();
    }


    // 虚拟用户回答
    @ResponseBody
    @RequestMapping(value = "/answerpost")
    public String answerpost(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "pid", required = false) String pid,
            @RequestParam(value = "questionid", required = false) String questionId,
            HttpServletRequest request) {

        try {
            Question question = AskServiceSngl.get().getQuestion(Long.valueOf(questionId));
            if (question != null) {
                //不能自己回答自己的问题
                if (pid.equals(question.getAskProfileId())) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", "-1");
                    return jsonObject.toString();
                }
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        String returnStr = askAccountVirtualWebLogic.answerpost(text, pid, questionId);
        return returnStr;
    }


    @ResponseBody
    @RequestMapping(value = "/postreply")
    public String postreply(@RequestParam(value = "profilieid") String profilieid,
                            @RequestParam(value = "parentid", defaultValue = "0") Integer parentid,
                            @RequestParam(value = "body") String body,
                            @RequestParam(value = "answerid") String answerid,
                            HttpServletRequest request) {

        String parentnick = "";
        if (parentid > 0) {
            try {
                String commentBeanId = CommentUtil.genCommentId(answerid, CommentDomain.WAN_ASK_COMMENT);
                CommentReply parentReply = CommentServiceSngl.get().getCommentReplyById(commentBeanId, Long.valueOf(parentid));
                if (parentReply != null) {
                    Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(parentReply.getReplyProfileId());
                    if (profile != null) {
                        parentnick = profile.getNick();
                    }
                }
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }

        String returnStr = askAccountVirtualWebLogic.postComment(answerid, body, profilieid, parentid, parentnick);
        return returnStr;
    }


    //重新激活
    @ResponseBody
    @RequestMapping(value = "/reactivated")
    public String reactivated(
            @RequestParam(value = "pid", required = false) String pid,
            @RequestParam(value = "questionid", required = false) String questionId,
            HttpServletRequest request) {
        String returnStr = askAccountVirtualWebLogic.reactivated(pid, questionId);
        return returnStr;
    }

}
