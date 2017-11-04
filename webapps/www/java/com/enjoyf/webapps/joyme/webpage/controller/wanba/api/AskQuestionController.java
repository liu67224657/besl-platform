package com.enjoyf.webapps.joyme.webpage.controller.wanba.api;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.point.UserPoint;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.redis.ScoreRange;
import com.enjoyf.platform.util.redis.ScoreRangeDTO;
import com.enjoyf.platform.util.redis.ScoreRangeRows;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.WanbaResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.Wanba.QuestionDTO;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaProfileDTO;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.weblogic.wanba.WanbaAskWebLogic;
import com.enjoyf.webapps.joyme.weblogic.wanba.WanbaProfileWebLogic;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/14
 */
@RequestMapping("/wanba/api/ask/question")
@Controller
public class AskQuestionController {

    @Resource
    private WanbaAskWebLogic wanbaAskWebLogic;

    @Resource
    private WanbaProfileWebLogic wanbaProfileWebLogic;

    @Resource
    private PointWebLogic pointWebLogic;

    //1对1提问
    @RequestMapping("/post/invite")
    @ResponseBody
    public String postOneOnOne(HttpServletRequest request) {
        String profileId = HTTPUtil.getParam(request, "pid");
        String title = request.getParameter("title");//30
        String text = request.getParameter("text");
        String inviteProfileId = request.getParameter("invite");
        String voice = request.getParameter("voice");
        String voiceTime = request.getParameter("vtime");
        String appkey = HTTPUtil.getParam(request, "appkey");
        String tagid = HTTPUtil.getParam(request, "tagid");

        //check param
        if (StringUtil.isEmpty(title) || StringUtil.isEmpty(inviteProfileId) || StringUtil.isEmpty(appkey)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        AskVoice askVoice = null;
        try {
            askVoice = new AskVoice(voice, Long.parseLong(voiceTime));
        } catch (NumberFormatException ignored) {
        }

        //check voice and text 同时为空
        if (StringUtil.isEmpty(text) && askVoice == null) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        if (title.length() > 30) {
            return WanbaResultCodeConstants.WANBA_ASK_TITLE_ILLEGLE.getJsonString();
        }

        try {
            //敏感词检验title
            String jsonString = wanbaAskWebLogic.checkContent(title);
            if (!StringUtil.isEmpty(jsonString)) {
                return jsonString;
            }

            //敏感词检验text
            jsonString = wanbaAskWebLogic.checkContent(text);
            if (!StringUtil.isEmpty(jsonString)) {
                return jsonString;
            }

            UserPoint userPoint = pointWebLogic.getUserPoint(AppUtil.getAppKey(appkey), profileId);
            VerifyProfile inviteProfile = UserCenterServiceSngl.get().getVerifyProfileById(inviteProfileId);
            if (userPoint == null || inviteProfile.getAskPoint() > userPoint.getUserPoint()) {
                return WanbaResultCodeConstants.WANBA_USER_POINT_NOT_ENOUGH.getJsonString();
            }

            Question question = new Question();
            question.setAskProfileId(profileId);
            question.setType(QuestionType.ONEONONE);
            question.setTitle(title);

            AskBody askBody = AskUtil.getQuestionBody(text);
            question.setRichText(text);
            question.setBody(askBody);
            question.setAskVoice(askVoice);
            question.setCreateTime(new Date());
            question.setQuestionPoint(inviteProfile.getAskPoint());
            question.setInviteProfileId(inviteProfileId);
            if (!StringUtil.isEmpty(tagid)) {
                question.setGameId(Long.valueOf(tagid));
            }

            question = AskServiceSngl.get().postQuestion(question);

            //如果tagid不为空，说明是在活动中提问，需要额外将问题加到活动的列表
            if (!StringUtil.isEmpty(tagid)) {
                wanbaAskWebLogic.addItemByLineKey(tagid, WanbaItemDomain.RECOMMEND, question);
            }


            //1对1扣积分
            if (inviteProfile.getAskPoint() > 0) {
                pointWebLogic.reduceUserPoint(PointActionType.WANBA_REDUCE_POINT, profileId, AppUtil.getAppKey(appkey), -inviteProfile.getAskPoint());
            }


            JSONObject jsonObject = WanbaResultCodeConstants.SUCCESS.getJsonObject();
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("qid", question.getQuestionId());
            jsonObject.put("result", resultMap);
            return jsonObject.toString();

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    @RequestMapping("/post/timelimit")
    @ResponseBody
    public String postTimeLimit(HttpServletRequest request) {
        String profileId = HTTPUtil.getParam(request, "pid");
        String title = request.getParameter("title");//30
        String text = request.getParameter("text");
        String gameidStr = request.getParameter("tagid");
        String timelimitStr = request.getParameter("timelimit");
        String appkey = HTTPUtil.getParam(request, "appkey");
//        String tagid = HTTPUtil.getParam(request, "tagid");
        //check param
        if (StringUtil.isEmpty(title) || StringUtil.isEmpty(timelimitStr) || StringUtil.isEmpty(gameidStr)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        String voice = request.getParameter("voice");
        String voiceTime = request.getParameter("vtime");

        AskVoice askVoice = null;
        try {
            askVoice = new AskVoice(voice, Long.parseLong(voiceTime));
        } catch (NumberFormatException e) {
        }

        //check voice and text 同时为空
        if (StringUtil.isEmpty(text) && askVoice == null) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        //check title length todo 问题描述
        if (title.length() > 30) {
            return WanbaResultCodeConstants.WANBA_ASK_TITLE_ILLEGLE.getJsonString();
        }


        //check gameid
        long gameId = -1l;
        try {
            gameId = Long.parseLong(gameidStr);
        } catch (NumberFormatException ignored) {
        }
        if (gameId < 0l) {
            return WanbaResultCodeConstants.WANBA_TAG_NOTEXISTS.getJsonString();
        }

        //check timelimit
        long timelimit = -1l;
        try {
            timelimit = Long.parseLong(timelimitStr);
        } catch (NumberFormatException ignored) {
        }
        if (timelimit < 0l) {
            return WanbaResultCodeConstants.WANBA_ASK_QUESTION_OUT_TIMELIMIT.getJsonString();
        }

        //时间与积分的关系
        QuestionConfig questionConfig = QuestionConfigType.getByCode(timelimit);
        if (questionConfig == null) {
            return WanbaResultCodeConstants.WANBA_USER_QUESTION_POINT_CONFIG.getJsonString();
        }

        //check point illegl
        int point = questionConfig.getQuestionPoint();

        try {
            //敏感词检验title
            String jsonString = wanbaAskWebLogic.checkContent(title);
            if (!StringUtil.isEmpty(jsonString)) {
                return jsonString;
            }

            //敏感词检验text
            jsonString = wanbaAskWebLogic.checkContent(text);
            if (!StringUtil.isEmpty(jsonString)) {
                return jsonString;
            }


            UserPoint userPoint = pointWebLogic.getUserPoint(AppUtil.getAppKey(appkey), profileId);
            if (userPoint == null || point > userPoint.getUserPoint()) {
                return WanbaResultCodeConstants.WANBA_USER_POINT_NOT_ENOUGH.getJsonString();
            }

            Date now = new Date();
            Question question = new Question();
            question.setGameId(gameId);
            question.setAskProfileId(profileId);
            question.setType(QuestionType.TIMELIMIT);
            question.setTitle(title);
            question.setTimeLimit(now.getTime() / 1000 * 1000 + timelimit);
            AskBody askBody = AskUtil.getQuestionBody(text);
            question.setRichText(text);
            question.setBody(askBody);
            question.setAskVoice(askVoice);
            question.setCreateTime(now);
            question.setQuestionPoint(point);

            question = AskServiceSngl.get().postQuestion(question);

            JSONObject jsonObject = WanbaResultCodeConstants.SUCCESS.getJsonObject();

            //1对多扣积分
            if (questionConfig.getQuestionPoint() > 0) {
                pointWebLogic.reduceUserPoint(PointActionType.WANBA_REDUCE_POINT, profileId, AppUtil.getAppKey(appkey), -questionConfig.getQuestionPoint());
            }


            Map resultMap = new HashMap();
            resultMap.put("qid", question.getQuestionId());
            jsonObject.put("result", resultMap);
            return jsonObject.toString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }


    /**
     * 个人主页 我问 他问
     */
    @RequestMapping("/mylist")
    @ResponseBody
    public String myQuestionList(HttpServletRequest request,
                                 @RequestParam(value = "pcount", required = false, defaultValue = "10") String pcount,
                                 @RequestParam(value = "pnum", required = false, defaultValue = "1") String pnum) {
        String profileId = HTTPUtil.getParam(request, "querypid");
        String pid = HTTPUtil.getParam(request, "pid");
        int pageNo = 1;
        int pageSize = 10;
        try {
            pageNo = Integer.parseInt(pnum);
            pageSize = Integer.parseInt(pcount);
        } catch (NumberFormatException e) {
        }


        String linekey = AskUtil.getAskLineKey(profileId, WanbaItemDomain.MYQUESTION);

        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            WanbaProfileDTO profileDTO = wanbaProfileWebLogic.getProfileDTOByProfileId(profileId);

            PageRows<QuestionDTO> pageRows = wanbaAskWebLogic.queryQuestionByLineKey(linekey, new Pagination(pageNo * pageSize, pageNo, pageSize), true, pid, false);
            Map map = new HashMap();
            map.put("profile", profileDTO);
            map.put("page", pageRows.getPage());
            map.put("rows", pageRows.getRows());
            jsonObject.put("result", JsonBinder.buildNonNullBinder(map));
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }


    /**
     * 个人主页 我答 他答
     */
    @RequestMapping("/myanswerlist")
    @ResponseBody
    public String myAnswers(HttpServletRequest request,
                            @RequestParam(value = "pcount", required = false, defaultValue = "10") String pcount,
                            @RequestParam(value = "pnum", required = false, defaultValue = "1") String pnum) {
        String profileId = HTTPUtil.getParam(request, "querypid");
        String pid = HTTPUtil.getParam(request, "pid");
        int pageNo = 1;
        int pageSize = 10;
        try {
            pageNo = Integer.parseInt(pnum);
            pageSize = Integer.parseInt(pcount);
        } catch (NumberFormatException e) {
        }


        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            WanbaProfileDTO profileDTO = wanbaProfileWebLogic.getProfileDTOByProfileId(profileId);

            PageRows<QuestionDTO> pageRows = wanbaAskWebLogic.queryMyAnswerByLineKey(profileId, new Pagination(pageNo * pageSize, pageNo, pageSize), true, pid);
            Map map = new HashMap();
            map.put("profile", profileDTO);
            map.put("page", pageRows.getPage());
            map.put("rows", pageRows.getRows());
            jsonObject.put("result", JsonBinder.buildNonNullBinder(map));
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }


    //问我
    @RequestMapping("/askmelist")
    @ResponseBody
    public String askMe(HttpServletRequest request,
                        @RequestParam(value = "pcount", required = false, defaultValue = "10") String pcount,
                        @RequestParam(value = "pnum", required = false, defaultValue = "1") String pnum) {
        String profileId = HTTPUtil.getParam(request, "querypid");
        String pid = HTTPUtil.getParam(request, "pid");
        int pageNo = 1;
        int pageSize = 10;
        try {
            pageNo = Integer.parseInt(pnum);
            pageSize = Integer.parseInt(pcount);
        } catch (NumberFormatException e) {
        }
        String linekey = AskUtil.getAskLineKey(profileId, WanbaItemDomain.ASKME);

        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            WanbaProfileDTO profileDTO = wanbaProfileWebLogic.getProfileDTOByProfileId(profileId);

            PageRows<QuestionDTO> pageRows = wanbaAskWebLogic.queryQuestionByLineKey(linekey, new Pagination(pageNo * pageSize, pageNo, pageSize), true, pid, false);
            Map map = new HashMap();
            map.put("profile", profileDTO);
            map.put("page", pageRows.getPage());
            map.put("rows", pageRows.getRows());
            jsonObject.put("result", JsonBinder.buildNonNullBinder(map));
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }

    //问答页面 达人左边 那个
    @RequestMapping("/invitelist")
    @ResponseBody
    public String invitelist(HttpServletRequest request,
                             @RequestParam(value = "pcount", required = false, defaultValue = "10") String pcount,
                             @RequestParam(value = "pnum", required = false, defaultValue = "1") String pnum) {
        String pid = HTTPUtil.getParam(request, "pid");
        int pageNo = 1;
        int pageSize = 10;
        try {
            pageNo = Integer.parseInt(pnum);
            pageSize = Integer.parseInt(pcount);
        } catch (NumberFormatException e) {
        }


        String linekey = AskUtil.getAskLineKey(AskUtil.KEY_ALLGAME, WanbaItemDomain.INVITE_QUESTIONLIST);

        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            PageRows<QuestionDTO> pageRows = wanbaAskWebLogic.queryQuestionByLineKey(linekey, new Pagination(pageNo * pageSize, pageNo, pageSize), true, pid, true);
            Map map = new HashMap();

            //热门轮播图
//            List<ActivityTopMenuDTO> hotActivityMenuList = wanbaAskWebLogic.queryActivityTopMenu(request, WanbaMenuDomain.HOT.getCode());
//            map.put("head", hotActivityMenuList);
            map.put("page", pageRows.getPage());
            map.put("rows", pageRows.getRows());
            jsonObject.put("result", JsonBinder.buildNonNullBinder(map));
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }

    @RequestMapping("/timelist/{type}")
    @ResponseBody
    public String timeListIng(HttpServletRequest request,
                              @PathVariable(value = "type") String type,
                              @RequestParam(value = "pcount", required = false, defaultValue = "10") String pcount) {
        String pid = HTTPUtil.getParam(request, "pid");
        int pageSize = 10;
        try {
            pageSize = Integer.parseInt(pcount);
        } catch (NumberFormatException e) {
        }

        long tagId = -1l;
        String tagIdStr = HTTPUtil.getParam(request, "tagid");
        if (!StringUtil.isEmpty(tagIdStr)) {
            try {
                try {
                    tagId = Integer.parseInt(tagIdStr);
                } catch (NumberFormatException e) {
                }
            } catch (Exception e) {
            }
        }

        if (tagId == -1) {
            tagIdStr = AskUtil.KEY_ALLGAME;
        }


        String queryFlag = HTTPUtil.getParam(request, "queryflag");//score

        //添加个默认值
        queryFlag = StringUtil.isEmpty(queryFlag) ? "-1" : queryFlag;

        //
        double timeLimit = -1D;
        if (Double.valueOf(queryFlag) < 0) {//第一页
            timeLimit = System.currentTimeMillis();
        } else {
            timeLimit = new BigDecimal(queryFlag).doubleValue();
        }

        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            ScoreRange range;
            WanbaItemDomain itemDomain;
            if (type.equalsIgnoreCase("accept")) {//sorce降序
                range = new ScoreRange(-1, timeLimit, pageSize, true);
                itemDomain = WanbaItemDomain.TIMELIMIT_ACCEPT;
            } else if (type.equalsIgnoreCase("expire")) {//sorce降序
                itemDomain = WanbaItemDomain.TIMELIMIT_EXPIRE;
                range = new ScoreRange(-1, timeLimit, pageSize, true);
            } else {
                range = new ScoreRange(timeLimit, -1, pageSize, false);//socre升序
                itemDomain = WanbaItemDomain.TIMELIMIT_ING;
            }
            range.setIsFirstPage(Double.valueOf(queryFlag) < 0);
            String linekey = AskUtil.getAskLineKey(tagIdStr, itemDomain);

            ScoreRangeRows<QuestionDTO> list = wanbaAskWebLogic.queryLineKeyQuestionByTimeLimit(linekey, range, pid);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("range", new ScoreRangeDTO(list.getRange()));
            map.put("rows", list.getRows());
            jsonObject.put("result", JsonBinder.buildNonNullBinder(map));
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }

    ////////////
    @RequestMapping("/follow")
    @ResponseBody
    public String follow(HttpServletRequest request) {
        String profileId = HTTPUtil.getParam(request, "pid");
        String qid = HTTPUtil.getParam(request, "qid");
        if (StringUtil.isEmpty(profileId) && StringUtil.isEmpty(qid)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long questionId = -1l;
        if (!StringUtil.isEmpty(qid)) {
            try {
                try {
                    questionId = Integer.parseInt(qid);
                } catch (NumberFormatException e) {
                }
            } catch (Exception e) {
            }
        }
        if (questionId < 0l) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            boolean bval = AskServiceSngl.get().followQuestion(questionId, profileId);
            return bval ? WanbaResultCodeConstants.SUCCESS.getJsonString() : WanbaResultCodeConstants.WANBA_ASK_QUESTIONFOLLOW_FAILED.getJsonString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }


    @RequestMapping("/unfollow")
    @ResponseBody
    public String unfollow(HttpServletRequest request) {
        String profileId = HTTPUtil.getParam(request, "pid");
        String qid = HTTPUtil.getParam(request, "qid");
        if (StringUtil.isEmpty(profileId) && StringUtil.isEmpty(qid)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long questionId = -1l;
        if (!StringUtil.isEmpty(qid)) {
            try {
                try {
                    questionId = Integer.parseInt(qid);
                } catch (NumberFormatException e) {
                }
            } catch (Exception e) {
            }
        }
        if (questionId < 0l) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            boolean bval = AskServiceSngl.get().unfollowQuestion(questionId, profileId);
            return bval ? WanbaResultCodeConstants.SUCCESS.getJsonString() : WanbaResultCodeConstants.WANBA_ASK_QUESTIONFOLLOW_FAILED.getJsonString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }


    @RequestMapping("/follow/questionlist")
    @ResponseBody
    public String followQuestionList(HttpServletRequest request,
                                     @RequestParam(value = "pcount", required = false, defaultValue = "10") String pcount) {
        String profileId = HTTPUtil.getParam(request, "pid");
        if (StringUtil.isEmpty(profileId)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        int pageSize = 10;
        try {
            pageSize = Integer.parseInt(pcount);
        } catch (NumberFormatException e) {
        }

        String queryFlag = HTTPUtil.getParam(request, "queryflag");//score
        double lastId = -1l;
        if (!StringUtil.isEmpty(queryFlag)) {
            lastId = Double.parseDouble(queryFlag);
        }
        ScoreRange range = new ScoreRange(-1, lastId, pageSize);
        range.setIsFirstPage(lastId < 0l);

        try {
            ScoreRangeRows<QuestionDTO> pageRows = wanbaAskWebLogic.queryFollowQuestionList(profileId, range);
            JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
            Map map = new HashMap();
            map.put("range", new ScoreRangeDTO(pageRows.getRange()));
            map.put("rows", pageRows.getRows());
            jsonObject.put("result", JsonBinder.buildNonNullBinder(map));
            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    @RequestMapping("/follow/profilelist")
    @ResponseBody
    public String followProfileList(HttpServletRequest request,
                                    @RequestParam(value = "pcount", required = false, defaultValue = "10") String pcount) {
        String profileId = HTTPUtil.getParam(request, "pid");
        String qid = HTTPUtil.getParam(request, "qid");
        if (StringUtil.isEmpty(profileId) || StringUtil.isEmpty(qid)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        int pageSize = 10;
        try {
            pageSize = Integer.parseInt(pcount);
        } catch (NumberFormatException e) {
        }

        long questionId = -1l;
        try {
            questionId = Long.parseLong(qid);
        } catch (NumberFormatException ignored) {
        }
        if (questionId < 0l) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        String queryFlag = HTTPUtil.getParam(request, "queryflag");//score
        double lastId = -1l;
        if (!StringUtil.isEmpty(queryFlag)) {
            lastId = Double.parseDouble(queryFlag);
        }
        ScoreRange range = new ScoreRange(-1, lastId, pageSize);
        range.setIsFirstPage(lastId < 0l);
        try {
            ScoreRangeRows<WanbaProfileDTO> pageRows = wanbaProfileWebLogic.queryFollowProfileByQuestionId(questionId, range);
            JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
            Map map = new HashMap();
            map.put("range", new ScoreRangeDTO(pageRows.getRange()));
            map.put("rows", pageRows.getRows());
            jsonObject.put("result", JsonBinder.buildNonNullBinder(map));
            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    /////////////////////
    @RequestMapping("/invite/profile")
    @ResponseBody
    public String inviteProfile(HttpServletRequest request) {
        String profileId = HTTPUtil.getParam(request, "pid");
        String qid = HTTPUtil.getParam(request, "qid");
        String invitedProfileId = HTTPUtil.getParam(request, "invitedpid");
        if (StringUtil.isEmpty(profileId) || StringUtil.isEmpty(qid) || StringUtil.isEmpty(invitedProfileId)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        long questionId = -1l;
        try {
            questionId = Long.parseLong(qid);
        } catch (NumberFormatException ignored) {
        }
        if (questionId < 0l) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            boolean result = AskServiceSngl.get().inviteAnswerQuestion(questionId, profileId, invitedProfileId);
            if (!result) {
                return WanbaResultCodeConstants.WANBA_ASK_QUESTIONINVITE_FAILED.getJsonString();
            }

            return WanbaResultCodeConstants.SUCCESS.getJsonString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }


    @RequestMapping(value = "/checkask/acceptstatus")
    @ResponseBody
    public String checkStatus(HttpServletRequest request) {
        String qid = HTTPUtil.getParam(request, "qid");
        if (StringUtil.isEmpty(qid)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        long questionId = -1l;
        try {
            questionId = Long.parseLong(qid);
        } catch (NumberFormatException ignored) {
        }
        if (questionId < 0l) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            Question question = AskServiceSngl.get().getQuestion(questionId);
            if (question == null) {
                return WanbaResultCodeConstants.WANBA_ASK_QUESTION_OUT_EXISTS.getJsonString();
            }
            if (question.getQuestionStatus().equals(QuestionStatus.ACCEPT)) {
                return WanbaResultCodeConstants.WANBA_ASK_ANSWER_ACCEPT.getJsonString();
            } else {
                return ResultCodeConstants.SUCCESS.getJsonString();
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

}
