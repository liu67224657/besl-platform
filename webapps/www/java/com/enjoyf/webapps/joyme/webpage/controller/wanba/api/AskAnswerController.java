package com.enjoyf.webapps.joyme.webpage.controller.wanba.api;

import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.point.WanbaPointType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.WanbaResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.Wanba.AnswerDetailDTO;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.weblogic.wanba.AbstractWanbaWebLogic;
import com.enjoyf.webapps.joyme.weblogic.wanba.WanbaAskWebLogic;
import net.sf.json.JSONObject;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/14
 */
@RequestMapping("/wanba/api/ask/answer")
@Controller
public class AskAnswerController extends AbstractWanbaWebLogic {

    @Resource
    private PointWebLogic pointWebLogic;

    @Resource
    private WanbaAskWebLogic wanbaAskWebLogic;


    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;


    //回答问题
    @RequestMapping("/post")
    @ResponseBody
    public String post(HttpServletRequest request) {
        String profileId = HTTPUtil.getParam(request, "pid");
        String text = request.getParameter("text");
        String qIdStr = request.getParameter("qid");
        String appkey = HTTPUtil.getParam(request, "appkey");

        //check param
        if (StringUtil.isEmpty(qIdStr) || StringUtil.isEmpty(appkey)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        appkey = AppUtil.getAppKey(appkey);

        String voice = request.getParameter("voice");
        String voiceTime = request.getParameter("vtime");

        AskVoice askVoice = null;
        try {
            askVoice = new AskVoice(voice, Long.parseLong(voiceTime));
        } catch (NumberFormatException ignored) {
        }
        //check voice and text 同时为空
        if (StringUtil.isEmpty(text) && askVoice == null) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        //todo check title length  check 问题描述
//        if (title.length() > 30) {
//            return WanbaResultCodeConstants.WANBA_ASK_TITLE_ILLEGLE.getJsonString();
//        }

        long qid = -1l;
        try {
            qid = Long.parseLong(qIdStr);
        } catch (NumberFormatException ignored) {
        }
        if (qid < -1l) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        Date now = new Date();
        try {
            //敏感词检验text
            String jsonString = wanbaAskWebLogic.checkContent(text);
            if (!StringUtil.isEmpty(jsonString)) {
                return jsonString;
            }

            //answer modify md5
            Answer answerByProfile = AskServiceSngl.get().getAnswerByProfile(qid, profileId);
            if (answerByProfile != null) {
                return WanbaResultCodeConstants.WANBA_ASK_QUESTION_HAS_ANSWER.getJsonString();
            }

            Question question = AskServiceSngl.get().getQuestion(qid);
            if (question == null) {
                return WanbaResultCodeConstants.WANBA_ASK_QUESTION_OUT_EXISTS.getJsonString();
            }

            if (QuestionType.ONEONONE.equals(question.getType()) && !question.getInviteProfileId().equals(profileId)) {
                return WanbaResultCodeConstants.WANBA_ASK_ANSWERPROFILE_NOT_INVITED.getJsonString();
            }


            //TODO 超时了也可以回答问题 和需求确认
//            if (QuestionType.TIMELIMIT.equals(question.getType()) && question.getTimeLimit() < now.getTime()) {
//                return WanbaResultCodeConstants.WANBA_ASK_QUESTION_OUT_TIMELIMIT.getJsonString();
//            }

            Answer answer = new Answer();
            answer.setAnswerProfileId(profileId);
            answer.setAnswerTime(now);
            AskBody askBody = AskUtil.getQuestionBody(text);
            answer.setRichText(text);
            answer.setBody(askBody);
            answer.setAskVoice(askVoice);
            answer.setQuestionId(qid);
            answer = AskServiceSngl.get().answer(answer);


            //1对1回答了加积分
            if (question.getType().equals(QuestionType.ONEONONE)) {
                pointWebLogic.reduceUserPoint(PointActionType.ANSWER, profileId, appkey, question.getQuestionPoint());
            }

            JSONObject jsonObject = WanbaResultCodeConstants.SUCCESS.getJsonObject();
            Map resultMap = new HashMap();
            resultMap.put("aid", answer.getAnswerId());
            jsonObject.put("result", resultMap);
            return jsonObject.toString();

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }


    @RequestMapping("/accept")
    @ResponseBody
    public String accept(HttpServletRequest request) {
        String profileId = HTTPUtil.getParam(request, "pid");
        String qIdStr = request.getParameter("qid");
        String aIdStr = request.getParameter("aid");
        String appkey = HTTPUtil.getParam(request, "appkey");

        //check param
        if (StringUtil.isEmpty(aIdStr) || StringUtil.isEmpty(qIdStr) || StringUtil.isEmpty(profileId) || StringUtil.isEmpty(appkey)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        appkey = AppUtil.getAppKey(appkey);

        long qid = -1l;
        try {
            qid = Long.parseLong(qIdStr);
        } catch (NumberFormatException ignored) {
        }
        if (qid < -1l) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long aid = -1l;
        try {
            aid = Long.parseLong(aIdStr);
        } catch (NumberFormatException ignored) {
        }
        if (aid < -1l) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            Answer answer = AskServiceSngl.get().getAnswer(aid);
            if (answer == null) {
                return WanbaResultCodeConstants.WANBA_ASK_ANSWERHAS_NOT_EXISTS.getJsonString();
            }

            Question question = AskServiceSngl.get().getQuestion(qid);
            if (question == null) {
                return WanbaResultCodeConstants.WANBA_ASK_QUESTION_OUT_EXISTS.getJsonString();
            }

            if (!question.getAskProfileId().equals(profileId)) {
                return WanbaResultCodeConstants.WANBA_ASK_ACCEPTPROFILE_ISNOT_ASKPROFILE.getJsonString();
            }

            // 错误
            if (!QuestionType.TIMELIMIT.equals(question.getType())) {
                return WanbaResultCodeConstants.WANBA_ASK_QUESTION_TYPE_ERROR.getJsonString();
            }

            boolean bval = AskServiceSngl.get().acceptAnswer(aid, qid);

            //限时被采纳了加积分
            if (bval && question.getType().equals(QuestionType.TIMELIMIT)) {
                pointWebLogic.reduceUserPoint(PointActionType.ANSWER, answer.getAnswerProfileId(), appkey, question.getQuestionPoint());
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return WanbaResultCodeConstants.SUCCESS.getJsonString();
    }

    @RequestMapping("/agree")
    @ResponseBody
    public String agreee(HttpServletRequest request) {
        String profileId = HTTPUtil.getParam(request, "pid");
        String aIdStr = request.getParameter("aid");
        String appkey = request.getParameter("appkey");

        JSONObject jsonObject = WanbaResultCodeConstants.SUCCESS.getJsonObject();
        long aid = -1l;
        try {
            aid = Long.parseLong(aIdStr);
        } catch (NumberFormatException ignored) {
        }
        if (aid < -1l) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        //check param
        if (StringUtil.isEmpty(aIdStr) || StringUtil.isEmpty(profileId)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        try {
            Answer answer = AskServiceSngl.get().getAnswer(aid);
            if (answer == null) {
                return WanbaResultCodeConstants.WANBA_ASK_ANSWERHAS_NOT_EXISTS.getJsonString();
            }

            AskUserAction askUserAction = new AskUserAction();
            askUserAction.setActionType(AskUserActionType.AGREEMENT);
            askUserAction.setCreateTime(new Date());
            askUserAction.setDestId(aid);
            askUserAction.setItemType(ItemType.ANSWER);
            askUserAction.setProfileId(profileId);
            askUserAction.setValue("1");

            boolean bval = AskServiceSngl.get().addAskUserAction(askUserAction);
            if (!bval) {
                return WanbaResultCodeConstants.WANBA_ASK_ANSWER_AGREE_FAILED.getJsonString();
            }
            int point = pointWebLogic.modifyUserPoint(PointActionType.WANBA_AGREE, profileId, AppUtil.getAppKey(appkey), WanbaPointType.AGREE, null);
            String pointtext = "";
            if (point > 0) {
                pointtext = i18nSource.getMessage("point.agree.success", new Object[]{WanbaPointType.AGREE}, Locale.CHINA);
            }
            jsonObject.put("pointtext", pointtext);


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }

    @RequestMapping("/answerlist")
    @ResponseBody
    public String answerList(HttpServletRequest request,
                             @RequestParam(value = "qid", required = false) Long qid,
                             @RequestParam(value = "pid", required = false) String pid,
                             @RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                             @RequestParam(value = "count", required = false, defaultValue = "10") Integer count
    ) {
        try {
            Pagination pagination = new Pagination(count * page, page, count);
            PageRows<Answer> answerPageRows = AskServiceSngl.get().queryAnswerByQuestionId(qid, pagination);
            Set<Long> answerIds = new HashSet<Long>();
            Set<String> profileSet = new HashSet<String>();
            if (answerPageRows != null && !CollectionUtil.isEmpty(answerPageRows.getRows())) {
                for (Answer answer : answerPageRows.getRows()) {
                    profileSet.add(answer.getAnswerProfileId());
                    answerIds.add(answer.getAnswerId());
                }

                Map<Long, AskUserAction> agreeAskUserActionMap = AskServiceSngl.get().checkAgreeAskUserAction(answerIds, pid);
                Map<Long, AnswerSum> answerSumMap = AskServiceSngl.get().queryAnswerSumByAids(answerIds);
                List<AnswerDetailDTO> answerDetailDTOList = new ArrayList<AnswerDetailDTO>();
                for (Answer answer : answerPageRows.getRows()) {
                    answerDetailDTOList.add(answerToDetailDTO(answer, answerSumMap.get(answer.getAnswerId()), agreeAskUserActionMap));
                }

                Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileSet);
                Map<String, VerifyProfile> wanbaProfileMap = UserCenterServiceSngl.get().queryProfileByIds(profileSet);

                JSONObject jsonObject = new JSONObject();
                Map<String, Object> messageMap = new HashMap<String, Object>();
                messageMap.put("answerDetailDTOList", answerDetailDTOList);
                messageMap.put("page", answerPageRows.getPage());
                messageMap.put("profileMap", profileMap);
                messageMap.put("wanbaProfileMap", wanbaProfileMap);
                jsonObject.put("rs", 1);
                jsonObject.put("result", messageMap);
                return jsonObject.toString();
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        return ResultCodeConstants.FAILED.getJsonString();
    }

    //判断用户是否回答此问题
    @RequestMapping("/isanswer")
    @ResponseBody
    public String isanswer(HttpServletRequest request) {
        String profileId = HTTPUtil.getParam(request, "pid");
        String qIdStr = HTTPUtil.getParam(request, "qid");
        if (StringUtil.isEmpty(qIdStr) || StringUtil.isEmpty(profileId)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long qid = -1l;
        try {
            qid = Long.parseLong(qIdStr);
        } catch (NumberFormatException ignored) {
        }

        Answer answerByProfile = null;
        try {
            answerByProfile = AskServiceSngl.get().getAnswerByProfile(qid, profileId);
            if (answerByProfile != null) {
                return WanbaResultCodeConstants.WANBA_ASK_QUESTION_HAS_ANSWER.getJsonString();
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }

}
