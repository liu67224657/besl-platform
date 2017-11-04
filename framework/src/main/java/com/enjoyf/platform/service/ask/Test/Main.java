package com.enjoyf.platform.service.ask.Test;

import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.redis.ScoreRange;
import com.enjoyf.platform.util.redis.ScoreRangeRows;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/12
 */
public class Main {

    static String ASK_PROFILEID = "02d40db86df60181779d4d9fd9852ebf"; //口袋
    static String ANSWER_PROFILEID = "0287f091ec14aa1ce7b8936ea5745b3c";//越长大越孤独

    static String s = "这是第一张图<jmwbimg:pic1.jpg>,\n这是第二张图<jmwbimg:pic2.jpg>,\n音频来吧<jmwbvoice:v.mp3:jmwbvt:4600>";


    public static void main(String[] args) {


        try {
//            followQuestionList("0028bd96acd495c5f680801e77da9d5a")
            double lastId = -1;
            ScoreRange scoreRange = new ScoreRange(-1, lastId, 10);
            boolean hasNext=true;
            int i=0;
            do {
                scoreRange.setIsFirstPage(lastId == -1);
                ScoreRangeRows<Question> rows = AskServiceSngl.get().queryFollowQuestionByProfileId("0028bd96acd495c5f680801e77da9d5a", scoreRange);
                lastId = rows.getRange().getScoreflag();
                scoreRange = new ScoreRange(-1, lastId, 10);
                i+=rows.getRows().size();
                hasNext=rows.getRange().isHasnext();
            } while (hasNext);

            System.out.println("size"+i);

//            WanbaProfile wanbaProfile = new WanbaProfile();
//            wanbaProfile.setProfileId(ASK_PROFILEID);
//            wanbaProfile.setVerifyType(VerifyType.FAMOUS);
//            wanbaProfile.setDescription("达人口袋");
//            System.out.println(AskServiceSngl.get().verifyWanbaProfile(wanbaProfile, 5));
//
//            wanbaProfile = new WanbaProfile();
//            wanbaProfile.setProfileId(ANSWER_PROFILEID);
//            wanbaProfile.setVerifyType(VerifyType.FAMOUS);
//            wanbaProfile.setDescription("达人孤单");
//            System.out.println(AskServiceSngl.get().verifyWanbaProfile(wanbaProfile, 6));

//
//            Question question1 = postTimeQuestion();
//            System.out.println("====quesiont is :==" + question1);
//
////            AskServiceSngl.get().deleteQuestion(210l);
//
//            Question question2 = postOneOnOneQuestion();
//            System.out.println("====quesiont is :==" + question2);
//
//            Set<Long> querySet=new HashSet<Long>();
//            querySet.add(307l);
//            querySet.add(308l);
//            querySet.add(309l);
//            querySet.add(310l);
//            Set<Long> result=AskServiceSngl.get().checkFollowQuestion(querySet,"02d40db86df60181779d4d9fd9852ebf");
//            System.out.println("====check follow result is :==" +  result);
////
////            System.out.println();
////            System.out.println("====askme list==");
////            String askMeLineKey = AskUtil.getAskLineKey(ANSWER_PROFILEID, WanbaItemDomain.ASKME);
////            System.out.println(AskServiceSngl.get().queryQuestionByLineKey(askMeLineKey, new Pagination(100, 1, 100), true).getRows());
////            System.out.println();
////            System.out.println("====myask list==");
////            String myAskLineKey = AskUtil.getAskLineKey(ASK_PROFILEID, WanbaItemDomain.MYQUESTION);
////            System.out.println(AskServiceSngl.get().queryQuestionByLineKey(myAskLineKey, new Pagination(100, 1, 100),true).getRows());
//////            System.out.println();
////            Answer answer = answer(question.getQuestionId());
////            System.out.println("====answer==" + answer);
////            System.out.println();
////            System.out.println("====myanswer list==");
////            String myAnswerLine = AskUtil.getAskLineKey(ANSWER_PROFILEID, WanbaItemDomain.MYANSER);
////            System.out.println(AskServiceSngl.get().queryQuestionByLineKey(myAnswerLine, new Pagination(100, 1, 100),true).getRows());
////            System.out.println();
//
//
//
//
//
////            System.out.println("====after answer and askme list==");
////            System.out.println(AskServiceSngl.get().queryQuestionByLineKey(askMeLineKey, new Pagination(100, 1, 100)).getRows());
//
////            System.out.println(AskServiceSngl.get().acceptAnswer(answer.getAnswerId(), question.getQuestionId()));
////            answer = AskServiceSngl.get().getAnswer(answer.getAnswerId());
////            System.out.println("====after accept get answer==" + answer);
////
////            Answer answer2 = answer(question.getQuestionId(),"answerId2");
////            System.out.println();
////            System.out.println("==== this is seconde answer limit 2==" + answer2);
////            question=AskServiceSngl.get().getQuestion(question.getQuestionId());
////            System.out.println("==== after sencode question.question firstId is firstAnswer id==" + question);
//
////            String hotLineKey = AskUtil.getAskLineKey(AskUtil.KEY_ALLGAME, WanbaItemDomain.INVITE_QUESTIONLIST);
////            System.out.println();
////            System.out.println("==== this is host question==");
////            System.out.println(AskServiceSngl.get().queryQuestionByLineKey(hotLineKey, new Pagination(100, 1, 100), true).getRows().size());
////
////            System.out.println();
////            String timeLimitKey = AskUtil.getAskLineKey(AskUtil.KEY_ALLGAME, WanbaItemDomain.TIMELIMIT_ING);
////            System.out.println("==== this is timelinit question==");
////            System.out.println(AskServiceSngl.get().queryQuestionByLineKey(timeLimitKey, new Pagination(100, 1, 100), true).getRows().size());
////
////            System.out.println();
////            String timeGameLimitKey = AskUtil.getAskLineKey(String.valueOf(1l), WanbaItemDomain.TIMELIMIT_ING);
////            System.out.println("==== this is timelinit  game question== " + 1);
////            System.out.println(AskServiceSngl.get().queryQuestionByLineKey(timeGameLimitKey, new Pagination(100, 1, 100), true).getRows().size());


        } catch (ServiceException e) {
            e.printStackTrace();
        }
        while (true) {
        }
    }

    private static Answer answer(long questionId) throws ServiceException {
        Answer answer = new Answer();
        answer.setAnswerProfileId(ANSWER_PROFILEID);
        answer.setAnswerTime(new Date());

        answer.setBody(AskUtil.getQuestionBody(s));
        answer.setQuestionId(questionId);

        return AskServiceSngl.get().answer(answer);
    }


    private static Answer answer(long questionId, String answerPid) throws ServiceException {
        Answer answer = new Answer();
        answer.setAnswerProfileId(answerPid);
        answer.setAnswerTime(new Date());
        answer.setBody(AskUtil.getQuestionBody(s));
        answer.setQuestionId(questionId);

        return AskServiceSngl.get().answer(answer);
    }


    private static Question postTimeQuestion() throws ServiceException {
        Question question = new Question();
        Date date=new Date();
        question.setGameId(1);
        question.setAskProfileId(ASK_PROFILEID);
        question.setType(QuestionType.TIMELIMIT);
        question.setTitle("title" + QuestionType.TIMELIMIT + "");
        question.setTimeLimit(date.getTime()+ 3600000l);

        question.setBody(AskUtil.getQuestionBody(s));
        question.setCreateTime(date);
        question.setQuestionPoint(1000);
        question.setInviteProfileId(ANSWER_PROFILEID);
        question = AskServiceSngl.get().postQuestion(question);
        return question;
    }

    private static Question postOneOnOneQuestion() throws ServiceException {
        Question question = new Question();
        question.setGameId(1);
        question.setAskProfileId(ASK_PROFILEID);
        question.setType(QuestionType.ONEONONE);
        question.setTitle("title" + QuestionType.ONEONONE + "");
        question.setBody(AskUtil.getQuestionBody(s));
        question.setCreateTime(new Date());
        question.setQuestionPoint(1000);
        question.setInviteProfileId(ANSWER_PROFILEID);
        question = AskServiceSngl.get().postQuestion(question);
        return question;
    }

}
