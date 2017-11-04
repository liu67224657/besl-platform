package com.enjoyf.webapps.tools.webpage.controller.wanba;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagAppType;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/26
 */
@Controller
@RequestMapping("/wanba/ask")
public class AskController extends ToolsBaseController {

    @RequestMapping("/question/list")
    public ModelAndView questionList(
            @RequestParam(value = "qtype", required = false) Integer qtype,
            @RequestParam(value = "rstatus", required = false) Integer rstatus,
            @RequestParam(value = "qstatus", required = false) Integer qstatus,
            @RequestParam(value = "tagid", required = false) String tagid,
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "nick", required = false) String nick,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination page = new Pagination(pageSize * curPage, curPage, pageSize);

        QueryExpress queryExpress = new QueryExpress();
        if (qtype != null) {
            QuestionType questionType = QuestionType.getByCode(qtype);
            if (questionType != null) {
                queryExpress.add(QueryCriterions.eq(QuestionField.TYPE, qtype));
            }
            mapMessage.put("qtype", qtype);
        }

        if (rstatus != null) {
            IntValidStatus removeStatus = IntValidStatus.getByCode(rstatus);
            if (removeStatus != null) {
                queryExpress.add(QueryCriterions.eq(QuestionField.REMOVESTATUS, rstatus));
            }
            mapMessage.put("rstatus", rstatus);
        }

        if (qstatus != null) {
            QuestionStatus questionStatus = QuestionStatus.getByCode(qstatus);
            if (questionStatus != null) {
                queryExpress.add(QueryCriterions.eq(QuestionField.QUESTIONSTATUS, qstatus));
            }
            mapMessage.put("qstatus", qstatus);
        }

        if (!StringUtil.isEmpty(text)) {
            queryExpress.add(QueryCriterions.like(QuestionField.TITLE, "%" + text + "%"));
            mapMessage.put("text", text);
        }

        if (!StringUtil.isEmpty(tagid) && qtype != null && QuestionType.TIMELIMIT.getCode() == qtype) {
            queryExpress.add(QueryCriterions.eq(QuestionField.GAMEID, Long.valueOf(tagid)));
        }


        queryExpress.add(QuerySort.add(QuestionField.QUESTIONID, QuerySortOrder.DESC));
        try {
            if (!StringUtil.isEmpty(nick)) {
                Profile profile = UserCenterServiceSngl.get().getProfileByNick(nick);
                if (profile != null) {
                    queryExpress.add(QueryCriterions.eq(QuestionField.ASKPROFILEID, profile.getProfileId()));
                }
                mapMessage.put("profile", profile);
            }

            PageRows<Question> questionPageRows = AskServiceSngl.get().queryQuestion(queryExpress, page);

            if (!CollectionUtil.isEmpty(questionPageRows.getRows())) {
                Set<Long> qidSet = new HashSet<Long>();
                Set<String> pidSet = new HashSet<String>();
                for (Question question : questionPageRows.getRows()) {
                    qidSet.add(question.getQuestionId());
                    pidSet.add(question.getAskProfileId());
                }

                Map<Long, QuestionSum> questionSumMap = AskServiceSngl.get().queryQuestionSumByQids(qidSet);
                mapMessage.put("questionSumMap", questionSumMap);


                Map<String, WanbaProfileClassify> profileClassifyMap = AskServiceSngl.get().getWanbaProfileClassifyMap(pidSet);
                mapMessage.put("profileClassifyMap", profileClassifyMap);
            }

            List<AnimeTag> returnTagList = getAnimetag();
            mapMessage.put("tagList", returnTagList);
            mapMessage.put("tagid", tagid);

            mapMessage.put("list", questionPageRows.getRows());
            mapMessage.put("page", questionPageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/wanba/ask/question-list", mapMessage);
        }

        return new ModelAndView("/wanba/ask/question-list", mapMessage);
    }


    @RequestMapping("/answer/list")
    public ModelAndView answerList(
            @RequestParam(value = "qid", required = false) Long qid,
            @RequestParam(value = "rstatus", required = false,defaultValue = "0") Integer rstatus,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination page = new Pagination(pageSize * curPage, curPage, pageSize);

        QueryExpress queryExpress = new QueryExpress();
        if (rstatus != null) {
            IntValidStatus removeStatus = IntValidStatus.getByCode(rstatus);
            if (removeStatus != null) {
                queryExpress.add(QueryCriterions.eq(AnswerField.REMOVESTATUS, rstatus));
            }
            mapMessage.put("rstatus", rstatus);
        }
        queryExpress.add(QueryCriterions.eq(AnswerField.QUESTIONID, qid));
        queryExpress.add(QuerySort.add(AnswerField.ANSWERID, QuerySortOrder.DESC));
        try {
            PageRows<Answer> questionPageRows = AskServiceSngl.get().queryAnswer(queryExpress, page);
            Set<String> pids = new HashSet<String>();
            Question question = AskServiceSngl.get().getQuestion(qid);
            if (question != null) {
                pids.add(question.getAskProfileId());
                pids.add(question.getInviteProfileId());
            }
            if (questionPageRows != null && !CollectionUtil.isEmpty(questionPageRows.getRows())) {

                for (Answer answer : questionPageRows.getRows()) {
                    pids.add(answer.getAnswerProfileId());
                }

                mapMessage.put("list", questionPageRows.getRows());
                mapMessage.put("page", questionPageRows.getPage());
            }

            Map<String, Profile> profileMap = new HashMap<String, Profile>();
            if (!CollectionUtil.isEmpty(pids)) {
                profileMap.putAll(UserCenterServiceSngl.get().queryProfiles(pids));
            }
            mapMessage.put("profileMap", profileMap);

            mapMessage.put("qid", qid);

            mapMessage.put("question", question);


            //如果答案未采纳
            if (question != null && question.getAcceptAnswerId() <= 0) {
                WanbaProfileClassify classify =
                        AskServiceSngl.get().getWanbaProfileClassify(AskUtil.getWanbaProfileClassifyId(question.getAskProfileId(), WanbaProfileClassifyType.WANBA_ASK_VIRTUAL));
                mapMessage.put("classify", classify);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/wanba/ask/answer-list", mapMessage);
        }

        return new ModelAndView("/wanba/ask/answer-list", mapMessage);
    }


    //用户的回答列表
    @RequestMapping("/profile/answer/list")
    public ModelAndView profileAnswerList(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "nick", required = false) String nick,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination page = new Pagination(pageSize * curPage, curPage, pageSize);

        QueryExpress queryExpress = new QueryExpress();


        if (!StringUtil.isEmpty(text)) {
            queryExpress.add(QueryCriterions.like(AnswerField.RICHTEXT, "%" + text + "%"));
            mapMessage.put("text", text);
        }

        queryExpress.add(QuerySort.add(AnswerField.ANSWERID, QuerySortOrder.DESC));
        try {
            if (!StringUtil.isEmpty(nick)) {
                Profile profile = UserCenterServiceSngl.get().getProfileByNick(nick);
                if (profile != null) {
                    queryExpress.add(QueryCriterions.eq(AnswerField.ANSWERPROFILEID, profile.getProfileId()));
                }
                mapMessage.put("profile", profile);
            }

            PageRows<Answer> answerPageRows = AskServiceSngl.get().queryAnswer(queryExpress, page);

            if (!CollectionUtil.isEmpty(answerPageRows.getRows())) {
                Set<Long> qidSet = new HashSet<Long>();
                Set<String> pidSet = new HashSet<String>();
                for (Answer answer : answerPageRows.getRows()) {
                    qidSet.add(answer.getQuestionId());
                    // answer.setRichText(AskUtil.getHtmlRichBody());
                }
                mapMessage.put("questionMap", AskServiceSngl.get().queryQuestionByIds(qidSet));
            }

            List<AnimeTag> returnTagList = getAnimetag();
            mapMessage.put("tagList", returnTagList);

            mapMessage.put("list", answerPageRows.getRows());
            mapMessage.put("page", answerPageRows.getPage());
            mapMessage.put("nick", nick);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/wanba/ask/profile-answer-list", mapMessage);
    }


    private List<AnimeTag> getAnimetag() {
        List<AnimeTag> returnTagList = new ArrayList<AnimeTag>();

        QueryExpress tagquery = new QueryExpress();
        tagquery.add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.DESC));
        tagquery.add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
        tagquery.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.WANBA_ASK.getCode()));
        List<AnimeTag> tagList = null;
        try {
            tagList = JoymeAppServiceSngl.get().queryAnimeTag(tagquery);

            for (AnimeTag tag : tagList) {
                if (tag.getTag_id() > 0 && tag.getPicjson() != null && tag.getPicjson().getType().equals("0")) {
                    returnTagList.add(tag);
                }
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return returnTagList;
    }


}
