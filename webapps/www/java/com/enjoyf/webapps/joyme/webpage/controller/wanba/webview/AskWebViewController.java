package com.enjoyf.webapps.joyme.webpage.controller.wanba.webview;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.comment.CommentForbid;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.WanbaResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.Wanba.AnswerDetailDTO;
import com.enjoyf.webapps.joyme.dto.Wanba.QuestionDetailDTO;
import com.enjoyf.webapps.joyme.weblogic.wanba.AbstractWanbaWebLogic;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by pengxu on 2016/10/19.
 */
@Controller
@RequestMapping(value = "/wanba/webview/ask")
public class AskWebViewController extends AbstractWanbaWebLogic {


    @RequestMapping(value = "/qdetail")
    public ModelAndView questionDetail(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {


            String qid = HTTPUtil.getParam(request, "qid");
            String loginDomain = HTTPUtil.getParam(request, "logindomain");
            String profileId = HTTPUtil.getParam(request, "pid");
            String appkey = HTTPUtil.getParam(request, "appkey");
            mapMessage.put("logindomain", loginDomain);
            mapMessage.put("pid", profileId);
            mapMessage.put("appkey", appkey);
            mapMessage.put("answertype", "0");//回答状态
            //禁言
            CommentForbid forbid = CommentServiceSngl.get().getCommentForbidByCache(profileId);
            if (forbid != null) {
                mapMessage.put("blackprofile", "1");
            } else {
                mapMessage.put("blackprofile", "0");
            }
            if (!StringUtil.isEmpty(profileId)) {
                Answer answerByProfile = AskServiceSngl.get().getAnswerByProfile(Long.parseLong(qid), profileId);
                if (answerByProfile != null) {
                    mapMessage.put("answertype", "1"); //已经回答过了
                }
            }
            if (StringUtil.isEmpty(qid) || StringUtil.isEmpty(appkey)) {
                GAlerter.lan(this.getClass().getName() + " questiondetail  param.is.null");
                mapMessage.put("errorMessage", "error.parameter.null");
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }

            Question question = AskServiceSngl.get().getQuestion(Long.parseLong(qid));
            if (question == null) {
                GAlerter.lan(this.getClass().getName() + " questiondetail  question is null");
                mapMessage.put("errorMessage", "system.error");
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }

            //问题已被采纳，不显示回答按钮
            if (question.getAcceptAnswerId() > 0) {
                mapMessage.put("answertype", "1"); //问题已被采纳
            }

            Set<String> profileSet = new HashSet<String>();
            profileSet.add(question.getAskProfileId());
            profileSet.add(question.getInviteProfileId());

            Set<Long> questionIds = new HashSet<Long>();
            questionIds.add(question.getQuestionId());
            Set<Long> followQuesitonIdSet = AskServiceSngl.get().checkFollowQuestion(questionIds, profileId);


            QuestionDetailDTO questionDetailDTO = questionToDetailDTO(question, null, followQuesitonIdSet);
            mapMessage.put("question", questionDetailDTO);

            //查询四个最新关注的人
            PageRows<AskUserAction> askUserActionPageRows = AskServiceSngl.get().queryAskUserActionByList(question.getQuestionId(), ItemType.QUESTION, AskUserActionType.FOLLOW, new Pagination(4, 1, 4));
            if (askUserActionPageRows != null && !CollectionUtil.isEmpty(askUserActionPageRows.getRows())) {
                for (AskUserAction askUserAction : askUserActionPageRows.getRows()) {
                    profileSet.add(askUserAction.getProfileId());
                }
                mapMessage.put("followInfo", askUserActionPageRows);
            }

            PageRows<Answer> answerPageRows = AskServiceSngl.get().queryAnswerByQuestionId(question.getQuestionId(), new Pagination(10, 1, 10));
            if (answerPageRows != null && !CollectionUtil.isEmpty(answerPageRows.getRows())) {
                Set<Long> answerIds = new HashSet<Long>();
                Map<Long, Answer> answerMap = null;
                if (question.getType().equals(QuestionType.TIMELIMIT)) {

                    if (answerPageRows.getRows().size() > 1 && question.getFirstAnswerId() != 0) {
                        answerIds.add(question.getFirstAnswerId());
                    }
                    if (question.getAcceptAnswerId() != 0) {
                        answerIds.add(question.getAcceptAnswerId());
                    }
                    answerMap = AskServiceSngl.get().queryAnswerByAnswerIds(answerIds);
                }

                for (Answer answer : answerPageRows.getRows()) {
                    profileSet.add(answer.getAnswerProfileId());
                    answerIds.add(answer.getAnswerId());
                }
                if (answerMap != null) {
                    for (Answer answer : answerMap.values()) {
                        profileSet.add(answer.getAnswerProfileId());
                    }
                }

                Map<Long, AskUserAction> agreeAskUserActionMap = AskServiceSngl.get().checkAgreeAskUserAction(answerIds, profileId);
                Map<Long, AnswerSum> answerSumMap = AskServiceSngl.get().queryAnswerSumByAids(answerIds);

                List<AnswerDetailDTO> answerDetailDTOList = new ArrayList<AnswerDetailDTO>();
                for (Answer answer : answerPageRows.getRows()) {
                    if (answerMap != null && answerMap.get(answer.getAnswerId()) != null) {
                        continue;
                    }
                    answerDetailDTOList.add(answerToDetailDTO(answer, answerSumMap.get(answer.getAnswerId()), agreeAskUserActionMap));
                }
                Map<Long, AnswerDetailDTO> topAnserMap = new HashMap<Long, AnswerDetailDTO>();
                if (answerMap != null) {
                    for (Answer answer : answerMap.values()) {
                        topAnserMap.put(answer.getAnswerId(), answerToDetailDTO(answer, answerSumMap.get(answer.getAnswerId()), agreeAskUserActionMap));
                    }
                    mapMessage.put("topAnserMap", topAnserMap);
                }

                Map<String, VerifyProfile> wanbaProfileMap = UserCenterServiceSngl.get().queryProfileByIds(profileSet);
                mapMessage.put("answerDetailDTOList", answerDetailDTOList);
                mapMessage.put("wanbaProfileMap", wanbaProfileMap);
                mapMessage.put("answerpage", answerPageRows.getPage());
            }

            Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileSet);
            mapMessage.put("profileMap", profileMap);


            //修改问题的阅读数
            AskServiceSngl.get().modifyQuestionSum(QuestionSumField.VIEWSUM, 1, Long.valueOf(qid));


            String share_url = ShortUrlUtils.getSinaURL("http://api." + WebappConfig.get().getDomain() + "/wanba/webview/ask/share/qdetail?qid=" + question.getQuestionId() + "&appkey=3iiv7VWfx84pmHgCUqRwun&jt=52&ji=" + question.getQuestionId());
            mapMessage.put("share_url", share_url);
            if (question.getType().equals(QuestionType.ONEONONE)) {

                if(AppUtil.getAppKey(appkey).equals("2ojbX21Pd7WqJJRWmIniM0")){
                    return new ModelAndView("/views/jsp/wanba/question_detail_wikiapp", mapMessage);
                }

                return new ModelAndView("/views/jsp/wanba/question_detail", mapMessage);
            } else {
                long timelimit = question.getTimeLimit();
                long now = new Date().getTime();
                if (now > timelimit) {
                    mapMessage.put("timeout", false);    //问题已过期
                } else {
                    mapMessage.put("timeout", true);     //问题未过期
                }

                Set<Long> tagidS = new HashSet<Long>();
                tagidS.add(question.getGameId());
                Map<Long, AnimeTag> gameAnimeTagMap = JoymeAppServiceSngl.get().queryAnimetags(tagidS);
                mapMessage.put("gameAnimeTagMap", gameAnimeTagMap);

                PageRows<Question> pageRows = AskServiceSngl.get().queryQuestion(new QueryExpress()
                        .add(QueryCriterions.eq(QuestionField.GAMEID, question.getGameId()))
                        .add(QueryCriterions.eq(QuestionField.QUESTIONSTATUS, QuestionStatus.ACCEPT.getCode()))
                        .add(QueryCriterions.eq(QuestionField.REMOVESTATUS, IntValidStatus.UNVALID.getCode())), new Pagination(1, 1, 1));
                if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                    mapMessage.put("questionPage", pageRows.getPage());
                }

                if(AppUtil.getAppKey(appkey).equals("2ojbX21Pd7WqJJRWmIniM0")){
                    return new ModelAndView("/views/jsp/wanba/question_detail_timelimit_wikiapp", mapMessage);
                }


                return new ModelAndView("/views/jsp/wanba/question_detail_timelimit", mapMessage);
            }
        } catch (ServiceException e) {
            GAlerter.lan(this.getClass().getName() + " questiondetail  question is null");
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }

    }


    @RequestMapping(value = "/reactivated")
    @ResponseBody
    public String reactivated(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            String qid = HTTPUtil.getParam(request, "qid");
            String profileId = HTTPUtil.getParam(request, "pid");

            Question question = AskServiceSngl.get().getQuestion(Long.parseLong(qid));
            if (question == null) {
                jsonObject.put("rs", -1);   //没有该问题
                return jsonObject.toString();
            }
            if (!question.getAskProfileId().equals(profileId)) {
                jsonObject.put("rs", -2);     //点击用户与提问人不匹配
                return jsonObject.toString();
            }
            if (question.getReactivated() == 1) {
                jsonObject.put("rs", -3);
                return jsonObject.toString();
            }

            long time = question.getTimeLimit() - question.getCreateTime().getTime();
            Date date = new Date();

            Question newQuestion = new Question();
            newQuestion.setGameId(question.getGameId());
            newQuestion.setAskProfileId(profileId);
            newQuestion.setType(QuestionType.TIMELIMIT);
            newQuestion.setTitle(question.getTitle());
            newQuestion.setCreateTime(date);
            newQuestion.setTimeLimit(date.getTime() / 1000 * 1000 + time);
            newQuestion.setRichText(question.getRichText());
            newQuestion.setBody(question.getBody());
            newQuestion.setAskVoice(question.getAskVoice());
            newQuestion.setQuestionPoint(question.getQuestionPoint());

            newQuestion = AskServiceSngl.get().postQuestion(newQuestion);
            if (newQuestion.getQuestionId() > 0) {
                jsonObject = WanbaResultCodeConstants.SUCCESS.getJsonObject();
                jsonObject.put("qid", newQuestion.getQuestionId());
                AskServiceSngl.get().modifyQuestion(new UpdateExpress().set(QuestionField.REACTIVATED, 1l), question.getQuestionId());
                return jsonObject.toString();
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return ResultCodeConstants.FAILED.getJsonString();
    }


    @RequestMapping(value = "/share/qdetail")
    public ModelAndView shareQuestionDetail(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String qid = HTTPUtil.getParam(request, "qid");
            String loginDomain = HTTPUtil.getParam(request, "logindomain");
            String profileId = HTTPUtil.getParam(request, "pid");
            String appkey = HTTPUtil.getParam(request, "appkey");
            mapMessage.put("logindomain", loginDomain);
            mapMessage.put("pid", profileId);
            mapMessage.put("appkey", appkey);
            if (StringUtil.isEmpty(qid) || StringUtil.isEmpty(appkey)) {
                GAlerter.lan(this.getClass().getName() + " questiondetail  param.is.null");
                mapMessage.put("errorMessage", "error.parameter.null");
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }

            Question question = AskServiceSngl.get().getQuestion(Long.parseLong(qid));
            if (question == null) {
                GAlerter.lan(this.getClass().getName() + " questiondetail  question is null");
                mapMessage.put("errorMessage", "system.error");
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
            Set<String> profileSet = new HashSet<String>();
            profileSet.add(question.getAskProfileId());
            profileSet.add(question.getInviteProfileId());

            Set<Long> questionIds = new HashSet<Long>();
            questionIds.add(question.getQuestionId());
            Set<Long> followQuesitonIdSet = AskServiceSngl.get().checkFollowQuestion(questionIds, profileId);


            QuestionDetailDTO questionDetailDTO = questionToDetailDTO(question, null, followQuesitonIdSet);
            mapMessage.put("question", questionDetailDTO);

            //查询四个最新关注的人
            PageRows<AskUserAction> askUserActionPageRows = AskServiceSngl.get().queryAskUserActionByList(question.getQuestionId(), ItemType.QUESTION, AskUserActionType.FOLLOW, new Pagination(4, 1, 4));
            if (askUserActionPageRows != null && !CollectionUtil.isEmpty(askUserActionPageRows.getRows())) {
                for (AskUserAction askUserAction : askUserActionPageRows.getRows()) {
                    profileSet.add(askUserAction.getProfileId());
                }
                mapMessage.put("followInfo", askUserActionPageRows);
            }

            PageRows<Answer> answerPageRows = AskServiceSngl.get().queryAnswerByQuestionId(question.getQuestionId(), new Pagination(10, 1, 10));
            if (answerPageRows != null && !CollectionUtil.isEmpty(answerPageRows.getRows())) {
                Set<Long> answerIds = new HashSet<Long>();
                Map<Long, Answer> answerMap = null;
                if (question.getType().equals(QuestionType.TIMELIMIT)) {
                    if (answerPageRows.getRows().size() > 1 && question.getFirstAnswerId() != 0) {
                        answerIds.add(question.getFirstAnswerId());
                    }
                    if (question.getAcceptAnswerId() != 0) {
                        answerIds.add(question.getAcceptAnswerId());
                    }
                    answerMap = AskServiceSngl.get().queryAnswerByAnswerIds(answerIds);
                }

                for (Answer answer : answerPageRows.getRows()) {
                    profileSet.add(answer.getAnswerProfileId());
                    answerIds.add(answer.getAnswerId());
                }
                if (answerMap != null) {
                    for (Answer answer : answerMap.values()) {
                        profileSet.add(answer.getAnswerProfileId());
                    }
                }

                Map<Long, AskUserAction> agreeAskUserActionMap = AskServiceSngl.get().checkAgreeAskUserAction(answerIds, profileId);
                Map<Long, AnswerSum> answerSumMap = AskServiceSngl.get().queryAnswerSumByAids(answerIds);

                List<AnswerDetailDTO> answerDetailDTOList = new ArrayList<AnswerDetailDTO>();
                for (Answer answer : answerPageRows.getRows()) {
                    if (answerMap != null && answerMap.get(answer.getAnswerId()) != null) {
                        continue;
                    }
                    answerDetailDTOList.add(answerToDetailDTO(answer, answerSumMap.get(answer.getAnswerId()), agreeAskUserActionMap));
                }
                Map<Long, AnswerDetailDTO> topAnserMap = new HashMap<Long, AnswerDetailDTO>();
                if (answerMap != null) {
                    for (Answer answer : answerMap.values()) {
                        topAnserMap.put(answer.getAnswerId(), answerToDetailDTO(answer, answerSumMap.get(answer.getAnswerId()), agreeAskUserActionMap));
                    }
                    mapMessage.put("topAnserMap", topAnserMap);
                }

                Map<String, VerifyProfile> wanbaProfileMap = UserCenterServiceSngl.get().queryProfileByIds(profileSet);
                mapMessage.put("answerDetailDTOList", answerDetailDTOList);
                mapMessage.put("wanbaProfileMap", wanbaProfileMap);
                mapMessage.put("answerpage", answerPageRows.getPage());
            }

            Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileSet);
            mapMessage.put("profileMap", profileMap);

            //修改问题的阅读数
            AskServiceSngl.get().modifyQuestionSum(QuestionSumField.VIEWSUM, 1, Long.valueOf(qid));

            if (question.getType().equals(QuestionType.ONEONONE)) {
                return new ModelAndView("/views/jsp/wanba/share/share_question_detail", mapMessage);
            } else {
                long timelimit = question.getTimeLimit();
                long now = new Date().getTime();
                if (now > timelimit) {
                    mapMessage.put("timeout", false);    //问题已过期
                } else {
                    mapMessage.put("timeout", true);     //问题未过期
                }

                Set<Long> tagidS = new HashSet<Long>();
                tagidS.add(question.getGameId());
                Map<Long, AnimeTag> gameAnimeTagMap = JoymeAppServiceSngl.get().queryAnimetags(tagidS);
                mapMessage.put("gameAnimeTagMap", gameAnimeTagMap);

                PageRows<Question> pageRows = AskServiceSngl.get().queryQuestion(new QueryExpress()
                        .add(QueryCriterions.eq(QuestionField.GAMEID, question.getGameId()))
                        .add(QueryCriterions.eq(QuestionField.QUESTIONSTATUS, QuestionStatus.ACCEPT.getCode()))
                        .add(QueryCriterions.eq(QuestionField.REMOVESTATUS, IntValidStatus.UNVALID.getCode())), new Pagination(1, 1, 1));
                if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                    mapMessage.put("questionPage", pageRows.getPage());
                }


                return new ModelAndView("/views/jsp/wanba/share/share_question_detail_timelimit", mapMessage);
            }
        } catch (ServiceException e) {
            GAlerter.lan(this.getClass().getName() + " questiondetail  question is null");
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }

    }
}
