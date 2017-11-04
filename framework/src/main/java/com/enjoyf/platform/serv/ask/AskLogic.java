package com.enjoyf.platform.serv.ask;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.ask.AskHandler;
import com.enjoyf.platform.serv.ask.quatz.*;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.ask.search.*;
import com.enjoyf.platform.service.ask.wiki.*;
import com.enjoyf.platform.service.event.*;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.system.wanba.WanbaQuestionNoticeEvent;
import com.enjoyf.platform.service.event.system.wiki.WikiappSearchDeleteEvent;
import com.enjoyf.platform.service.event.system.wiki.WikiappSearchEvent;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagAppType;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.joymeapp.anime.GameClientTagType;
import com.enjoyf.platform.service.notice.NoticeType;
import com.enjoyf.platform.service.notice.wanba.WanbaNoticeBodyType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.BlackList;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.redis.ScoreRange;
import com.enjoyf.platform.util.redis.ScoreRangeRows;
import com.enjoyf.platform.util.sql.*;
import com.google.common.base.Splitter;
import com.google.gson.Gson;
import net.sf.json.JSONObject;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/12
 */
public class AskLogic implements AskService {

    private static final Logger logger = LoggerFactory.getLogger(AskLogic.class);

    private AskRedis askRedis;

    private AskHandler askWriteAbleHandler;

    private WanbaSearchService wanbaSearchService = new WanbaSearchService();

    private WikiappSearchService wikiappSearchService = new WikiappSearchService();

    //queue thread pool
    private QueueThreadN eventProcessQueueThreadN = null;

    private QuestionStatusCheckQuartzCronTrigger trigger = null;

    private ViewsumHotrankQuartzCronTrigger viewsumHotrankTrigger = null;

    private VerifyNoticeQuartzCronTrigger verifyNoticeTrigger = null;

    private TimedReleaseQuartzCronTrigger timedReleaseTrigger = null;

    private AdQuartzCronTrigger adQuartzCronTrigger = null;


    public AskLogic(AskConfig askConfig) {
        askRedis = new AskRedis(askConfig.getProps());

        try {
            askWriteAbleHandler = new AskHandler(askConfig.getWriteableDataSourceName(), askConfig.getProps());
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());
            Utility.sleep(5000);
            System.exit(0);
        }

        eventProcessQueueThreadN = new QueueThreadN(askConfig.getEventQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                try {
                    if (obj instanceof WanbaSearchEntry) {
                        wanbaSearchService.saveObject((WanbaSearchEntry) obj);
                    } else if (obj instanceof WanbaSearchDeleteEntry) {
                        wanbaSearchService.deleteObject((WanbaSearchDeleteEntry) obj);
                    } else if (obj instanceof NoticeBoardEntry) {
                        boardFollowQuestionHasAccept((NoticeBoardEntry) obj);
                    } else if (obj instanceof WikiappSearchDeleteEvent) {
                        WikiappSearchDeleteEvent event = (WikiappSearchDeleteEvent) obj;
                        wikiappSearchService.deleteObject(event.getWikiappSearchDeleteEntry());
                    } else if (obj instanceof WikiappSearchEvent) {
                        WikiappSearchEvent event = (WikiappSearchEvent) obj;
                        wikiappSearchService.saveObject(event.getWikiappSearchEntry());
                    } else {
                        GAlerter.lab("In timeLineProcessQueueThreadN, there is a unknown obj.");
                    }
                } catch (Exception e) {
                    GAlerter.lab(this.getClass().getName() + " eventProcessQueueThreadN occured error.e: ", e);
                }
            }
        }, new FQueueQueue(askConfig.getQueueDiskStorePath(), "askeventProcessQueueThreadN"));

        if (askConfig.isCheckExpireQuesitonTrigger()) {
            try {
                trigger = new QuestionStatusCheckQuartzCronTrigger(this, askConfig.getCheckExpireQuestionCronExp());
                trigger.init();
                trigger.start();
            } catch (SchedulerException e) {
                GAlerter.lab("init trigger error.", e);
                Utility.sleep(5000);
                System.exit(0);
            }
        }


        //热度排行
        if (askConfig.isViewsumHotrankTrigger()) {
            try {
                viewsumHotrankTrigger = new ViewsumHotrankQuartzCronTrigger(this, askConfig.getViewsumHotrankCronExp());
                viewsumHotrankTrigger.init();
                viewsumHotrankTrigger.start();
            } catch (SchedulerException e) {
                GAlerter.lab("init trigger error.", e);
                Utility.sleep(5000);
                System.exit(0);
            }
        }

        //向对应游戏下的达人推送
        if (askConfig.isVerifyNoticeTrigger()) {
            try {
                askRedis.setVerifyNoticeTrigger(false);
                verifyNoticeTrigger = new VerifyNoticeQuartzCronTrigger(this, askConfig.getKeyVerifynoticeCronexp());
                verifyNoticeTrigger.init();
                verifyNoticeTrigger.start();
            } catch (SchedulerException e) {
                GAlerter.lab("init trigger error.", e);
                Utility.sleep(5000);
                System.exit(0);
            }
        }


        //定时任务：活动、问题
        if (askConfig.isTimedReleaseTrigger()) {
            try {
                askRedis.setTimedReleaseTriggerRunning(false);
                timedReleaseTrigger = new TimedReleaseQuartzCronTrigger(this, askConfig.getKeyTimedreleaseCronexp());
                timedReleaseTrigger.init();
                timedReleaseTrigger.start();

            } catch (SchedulerException e) {
                GAlerter.lab("init trigger error.", e);
                Utility.sleep(5000);
                System.exit(0);
            }
        }

        try {
            adQuartzCronTrigger = new AdQuartzCronTrigger(this);
        } catch (SchedulerException e) {
            GAlerter.lab("init adQuartzCronTrigger error.", e);
            Utility.sleep(5000);
            System.exit(0);
        }

        if (askConfig.isAdTrigger()) {
            this.initAdQuartzTrigger();
        }

    }

    private void initAdQuartzTrigger() {
        if (logger.isDebugEnabled()) {
            logger.debug(" initAdQuartzTrigger. event:");
        }

        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.in(AdvertiseField.REMOVE_STATUS, new String[]{ValidStatus.INVALID.getCode(), ValidStatus.VALID.getCode()}));
            List<Advertise> list = askWriteAbleHandler.queryAdvertise(queryExpress);
            if (!CollectionUtil.isEmpty(list)) {
                for (Advertise advertise : list) {
                    updateAdvertiseQuartzCronTrigger(advertise.getId());
                }
            }
        } catch (DbException e) {
            GAlerter.lab("this.initAdQuartzTrigger error :", e);
        } catch (ServiceException e) {
            GAlerter.lab("this.initAdQuartzTrigger error :", e);
        }

    }

    /**
     * @param jobName     任务名称
     * @param date        时间
     * @param id          id
     * @param validStatus 上架或者下架
     * @throws SchedulerException
     */
    private void buildAdQuartzCronTrigger(String jobName, Date date, long id, String validStatus) throws SchedulerException {
        adQuartzCronTrigger.setJobName(jobName + id);
        adQuartzCronTrigger.setCronExp(date);
        adQuartzCronTrigger.setId(id);
        adQuartzCronTrigger.setValidStatus(validStatus);
        adQuartzCronTrigger.init();
    }


    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" receiveEvent. event:" + event);
        }

        eventProcessQueueThreadN.add(event);
        return true;
    }

    public Question getQuestion(Long questionId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getQuestion questionId:" + questionId);
        }

        Question question = askRedis.getQuestion(questionId);
        if (question == null) {
            question = askWriteAbleHandler.getQuestionById(questionId, IntValidStatus.UNVALID);
            if (question != null) {
                askRedis.setQuestion(question);
            }
        }
        return question;
    }

    public boolean modifyQuestion(UpdateExpress updateExpress, Long questionId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyQuestion updateExpress:" + updateExpress + " questionId:" + questionId);
        }
        boolean bval = askWriteAbleHandler.updateQuestionById(updateExpress, questionId);
        if (bval) {
            bval = askRedis.delQuestion(questionId);
        }
        return bval;
    }

    @Override
    public Question postQuestion(Question question) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " post Question question:" + question);
        }

        //write to db and redis cache object
        question = askWriteAbleHandler.insertQuestion(question);
        askRedis.setQuestion(question);

        //write to redis and db myquestionlist
        writeToMyQuestionLine(question);

        if (QuestionType.ONEONONE.equals(question.getType())) {
            processOneOnOneAsk(question);
        } else if (QuestionType.TIMELIMIT.equals(question.getType())) {
            processLimitAsk(question);
        }

        //ask profile follow question
        followQuestion(question.getQuestionId(), question.getAskProfileId());
        return question;
    }

    //write to db and redis askme list
    private boolean processOneOnOneAsk(Question question) throws DbException {
        String askMeLineKey = AskUtil.getAskLineKey(question.getInviteProfileId(), WanbaItemDomain.ASKME);
        WanbaItem askMeItem = new WanbaItem();
        askMeItem.setOwnProfileId(question.getInviteProfileId());
        askMeItem.setItemType(ItemType.QUESTION);
        askMeItem.setCreateTime(question.getCreateTime());
        askMeItem.setItemDomain(WanbaItemDomain.ASKME);
        askMeItem.setDestId(String.valueOf(question.getQuestionId()));
        askMeItem.setDestProfileId(question.getAskProfileId());
        askMeItem.setLineKey(askMeLineKey);
        askMeItem.setScore(question.getCreateTime().getTime());
        askWriteAbleHandler.insertAskItem(askMeItem);
        askRedis.addLine(askMeItem);

        //sendout notice
        try {
            WanbaQuestionNoticeEvent wbNsIEvent = new WanbaQuestionNoticeEvent();
            wbNsIEvent.setProfileId(question.getInviteProfileId());
            wbNsIEvent.setType(NoticeType.ANSWER);
            wbNsIEvent.setQuestionId(question.getQuestionId());
            wbNsIEvent.setBodyType(WanbaNoticeBodyType.QUESTION_ONEONONE_ASK);
            wbNsIEvent.setCreateTime(question.getCreateTime());
            wbNsIEvent.setDestProfileId(question.getAskProfileId());
            EventDispatchServiceSngl.get().dispatch(wbNsIEvent);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " sendout WanbaNoticeSumIncreaseEvent answer error,e: ", e);
        }

        return true;
    }

    //write to db and redis timelimit asking
    private boolean processLimitAsk(Question question) throws DbException {
        String allGame = AskUtil.KEY_ALLGAME;
        String allTimeLineKey = AskUtil.getAskLineKey(allGame, WanbaItemDomain.TIMELIMIT_ING);

        WanbaItem allTimeItem = new WanbaItem();
        allTimeItem.setOwnProfileId(allGame);
        allTimeItem.setItemType(ItemType.QUESTION);
        allTimeItem.setCreateTime(question.getCreateTime());
        allTimeItem.setDestId(String.valueOf(question.getQuestionId()));
        allTimeItem.setItemDomain(WanbaItemDomain.TIMELIMIT_ING);
        allTimeItem.setLineKey(allTimeLineKey);
        allTimeItem.setScore(question.getTimeLimit() + Double.parseDouble("0." + String.valueOf(question.getQuestionId())));
        askWriteAbleHandler.insertAskItem(allTimeItem);
        askRedis.addLine(allTimeItem);

        String gameIdString = String.valueOf(question.getGameId());
        String gameTimeLineKey = AskUtil.getAskLineKey(gameIdString, WanbaItemDomain.TIMELIMIT_ING);
        WanbaItem gameTimeItem = new WanbaItem();
        gameTimeItem.setOwnProfileId(String.valueOf(question.getGameId()));
        gameTimeItem.setItemType(ItemType.QUESTION);
        gameTimeItem.setCreateTime(question.getCreateTime());
        gameTimeItem.setDestId(String.valueOf(question.getQuestionId()));
        gameTimeItem.setItemDomain(WanbaItemDomain.TIMELIMIT_ING);
        gameTimeItem.setLineKey(gameTimeLineKey);
        gameTimeItem.setScore(question.getTimeLimit() + Double.parseDouble("0." + String.valueOf(question.getQuestionId())));
        askWriteAbleHandler.insertAskItem(gameTimeItem);
        askRedis.addLine(gameTimeItem);
        return true;
    }


    private boolean writeToMyQuestionLine(Question question) throws DbException {
        String myAskLineKey = AskUtil.getAskLineKey(question.getAskProfileId(), WanbaItemDomain.MYQUESTION);
        WanbaItem myAskItem = new WanbaItem();
        myAskItem.setOwnProfileId(question.getAskProfileId());
        myAskItem.setItemType(ItemType.QUESTION);
        myAskItem.setCreateTime(question.getCreateTime());
        myAskItem.setItemDomain(WanbaItemDomain.MYQUESTION);
        myAskItem.setDestId(String.valueOf(question.getQuestionId()));
        myAskItem.setDestProfileId(question.getInviteProfileId());
        myAskItem.setLineKey(myAskLineKey);
        myAskItem.setScore(question.getCreateTime().getTime());
        askWriteAbleHandler.insertAskItem(myAskItem);
        askRedis.addLine(myAskItem);
        return true;
    }

    @Override
    public PageRows<Question> queryQuestionByLineKey(String lineKey, Pagination pagination, boolean isDesc) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryQuestionByLineKey lineKey:" + lineKey + " pagination:" + pagination + " isDesc:" + isDesc);
        }
        PageRows<Question> pageRows = new PageRows<Question>();

        //build page
        int size = (int) askRedis.getLineSize(lineKey);
        pagination.setTotalRows(size);
        pageRows.setPage(pagination);

        //get id by line
        Map<String, ItemType> idMap = askRedis.queryItemByLine(lineKey, pagination, isDesc);
        if (CollectionUtil.isEmpty(idMap)) {
            return pageRows;
        }

        //build queryidSet and get QuestionMap
        Set<Long> queryIdSet = new LinkedHashSet<Long>();
        for (String id : idMap.keySet()) {
            queryIdSet.add(Long.valueOf(id));
        }
        Map<Long, Question> questionMap = queryQuestionByIds(queryIdSet);

        //build result
        List<Question> questionList = new ArrayList<Question>();
        for (Long id : queryIdSet) {
            if (questionMap.containsKey(id)) {
                questionList.add(questionMap.get(id));
            }
        }

        pageRows.setRows(questionList);
        return pageRows;
    }

    @Override
    public ScoreRangeRows<Question> queryQuestionByLineKeyRange(String lineKey, ScoreRange range) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryQuestionByLineKeyRange lineKey:" + lineKey + " range:" + range);
        }


        ScoreRangeRows<Question> rows = new ScoreRangeRows<Question>();
        rows.setRange(range);
        List<Question> questionList = new ArrayList<Question>();
        Map<String, ItemType> idMap = askRedis.queryItemByLineSortRange(lineKey, range);
        if (CollectionUtil.isEmpty(idMap)) {
            return rows;
        }

        //build queryidSet and get QuestionMap
        Set<Long> queryIdSet = new LinkedHashSet<Long>();
        for (String id : idMap.keySet()) {
            queryIdSet.add(Long.valueOf(id));
        }
        Map<Long, Question> questionMap = queryQuestionByIds(queryIdSet);

        //build result

        for (Long id : queryIdSet) {
            if (questionMap.containsKey(id)) {
                questionList.add(questionMap.get(id));
            }
        }

        rows.setRange(range);
        rows.setRows(questionList);

        return rows;
    }

    @Override
    public Map<Long, Question> queryQuestionByIds(Collection<Long> questionIdSet) throws ServiceException {
        Map<Long, Question> returnMap = new LinkedHashMap<Long, Question>();
        Set<Long> queryDbId = new HashSet<Long>();

        //query by cache
        for (Long id : questionIdSet) {
            Question question = askRedis.getQuestion(id);
            if (question != null) {
                returnMap.put(id, question);
            } else {
                queryDbId.add(id);
            }
        }

        //query by db
        if (!CollectionUtil.isEmpty(queryDbId)) {
            List<Question> questionListByDb = askWriteAbleHandler.queryQuestionById(new QueryExpress().add(QueryCriterions.
                    in(QuestionField.QUESTIONID, queryDbId.toArray())).add(QueryCriterions.eq(QuestionField.REMOVESTATUS, IntValidStatus.UNVALID.getCode())));
            for (Question questionByDb : questionListByDb) {
                returnMap.put(questionByDb.getQuestionId(), questionByDb);
            }
        }

        return returnMap;
    }

    @Override
    public PageRows<Question> queryQuestion(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyQuestion updateExpress:" + queryExpress + " page:" + page);
        }
        return askWriteAbleHandler.queryQuestion(queryExpress, page);
    }

    @Override
    public boolean deleteQuestion(Long questionId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " deleteQuestion questionId:" + questionId);
        }

        Question question = getQuestion(questionId);
        if (question == null) {
            return false;
        }

        boolean bval = askWriteAbleHandler.updateQuestionById(new UpdateExpress().set(QuestionField.REMOVESTATUS, IntValidStatus.VALID.getCode()), questionId);
        if (bval) {
            askRedis.delQuestion(questionId);

            //删除我的提问
            String myAskLineKey = AskUtil.getAskLineKey(question.getAskProfileId(), WanbaItemDomain.MYQUESTION);
            askRedis.removeItem(myAskLineKey, String.valueOf(questionId), ItemType.QUESTION);


            //删除推荐列表里的
            String linekey = AskUtil.getAskLineKey("-10000", WanbaItemDomain.RECOMMEND);
            String itemid = AskUtil.getAskItemId(String.valueOf(questionId), linekey, ItemType.QUESTION);
            AskServiceSngl.get().removeItemByLineKey(itemid);


            //处理游戏标签
            if (question != null && question.getGameId() > 0) {
                String gameIdString = String.valueOf(question.getGameId());
                String gameRecommendKey = AskUtil.getAskLineKey(gameIdString, WanbaItemDomain.RECOMMEND);
                askRedis.removeItem(gameRecommendKey, String.valueOf(question.getQuestionId()), ItemType.QUESTION);

                //修改wanba_item状态
                askWriteAbleHandler.modifyAskItem(new UpdateExpress().set(WanbaItemField.VALIDSTATUS,
                        ValidStatus.REMOVED.getCode()), AskUtil.getAskItemId(String.valueOf(question.getQuestionId()), gameRecommendKey, ItemType.QUESTION));
            }


            //删除各列表的数据 todo 是否删除个列表数据? 如果删除列表数据, 我的回答的列表数据如何处理
            if (QuestionType.ONEONONE.equals(question.getType())) {
                //1对1 主页：我问 他问
                String askMeLineKey = AskUtil.getAskLineKey(question.getInviteProfileId(), WanbaItemDomain.ASKME);
                askRedis.removeItem(askMeLineKey, String.valueOf(questionId), ItemType.QUESTION);
            } else if (QuestionType.TIMELIMIT.equals(question.getType())) {
                //所有问题
                String allGame = AskUtil.KEY_ALLGAME;
                String allTimeLineKey = AskUtil.getAskLineKey(allGame, WanbaItemDomain.TIMELIMIT_ING);
                askRedis.removeItem(allTimeLineKey, String.valueOf(questionId), ItemType.QUESTION);


                //对应的标签
                String gameIdString = String.valueOf(question.getGameId());
                String gameTimeLineKey = AskUtil.getAskLineKey(gameIdString, WanbaItemDomain.RECOMMEND);
                askRedis.removeItem(gameTimeLineKey, String.valueOf(questionId), ItemType.QUESTION);
            }


            //删除搜索引擎问题
            WanbaSearchDeleteEntry deleteEntry = new WanbaSearchDeleteEntry();
            deleteEntry.setQid(String.valueOf(question.getQuestionId()));
            eventProcessQueueThreadN.add(deleteEntry);
        }
        return bval;
    }

    @Override
    public boolean modifyAnswer(UpdateExpress updateExpress, Long answerId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyQuestion updateExpress:" + updateExpress + " answerId:" + answerId);
        }

        boolean bval = askWriteAbleHandler.updateAnswerById(updateExpress, answerId);
        if (bval) {
            bval = askRedis.delAnswer(answerId);
        }
        return bval;
    }

    @Override
    public Answer answer(Answer answer) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " post answer answer:" + answer);
        }
        //one on one
        Question question = getQuestion(answer.getQuestionId());
        answer.setIsAccept(QuestionType.ONEONONE.equals(question.getType()));

        //write to db save answer
        answer = askWriteAbleHandler.insertAnswer(answer);

        //modify quetion num and profilesum
        modifyQuestionSum(QuestionSumField.ANSEWERSUM, 1, answer.getQuestionId());
        modifyWanbaProfileSum(WanbaProfileSumField.ANSWERSUM, 1, answer.getAnswerProfileId());

        writeAnswerIdToQuestionList(answer);
        writeQuestionIdToMyAnswerLine(answer);
        if (QuestionType.ONEONONE.equals(question.getType())) {
            processOneOnOneAnswer(answer, question);
        } else if (QuestionType.TIMELIMIT.equals(question.getType())) {
            processLimitAnswer(answer);
        }

        try {
            //只有1对1，回答才有问答通知和推送，哎
            //  if (question.getType().equals(QuestionType.ONEONONE)) {
            WanbaQuestionNoticeEvent wbNsIEvent = new WanbaQuestionNoticeEvent();
            wbNsIEvent.setProfileId(question.getAskProfileId());
            wbNsIEvent.setType(NoticeType.ANSWER);
            wbNsIEvent.setAnswerId(answer.getAnswerId());
            wbNsIEvent.setDestProfileId(answer.getAnswerProfileId());
            wbNsIEvent.setQuestionId(question.getQuestionId());
            wbNsIEvent.setBodyType(WanbaNoticeBodyType.QUESTION_NEWANSWER);
            wbNsIEvent.setCreateTime(answer.getAnswerTime());
            EventDispatchServiceSngl.get().dispatch(wbNsIEvent);
            // }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " sendout WanbaNoticeSumIncreaseEvent answer error,e: ", e);
        }

        return answer;
    }

    private void processOneOnOneAnswer(Answer answer, Question question) throws ServiceException {
        //remove question me line
        String questionMeLineKey = AskUtil.getAskLineKey(answer.getAnswerProfileId(), WanbaItemDomain.ASKME);
        askRedis.removeItem(questionMeLineKey, String.valueOf(answer.getQuestionId()), ItemType.QUESTION);

        //modify question
        UpdateExpress updateExpress = new UpdateExpress()
                .set(QuestionField.ACCEPTANSWERID, answer.getAnswerId())
                .set(QuestionField.QUESTIONSTATUS, QuestionStatus.ACCEPT.getCode());
        modifyQuestion(updateExpress, answer.getQuestionId());

        //问他列表
        String inviteQuestionList = AskUtil.getAskLineKey(AskUtil.KEY_ALLGAME, WanbaItemDomain.INVITE_QUESTIONLIST);
        WanbaItem myAskItem = new WanbaItem();
        myAskItem.setOwnProfileId(AskUtil.KEY_ALLGAME);
        myAskItem.setItemType(ItemType.QUESTION);
        myAskItem.setCreateTime(question.getCreateTime());
        myAskItem.setItemDomain(WanbaItemDomain.INVITE_QUESTIONLIST);
        myAskItem.setDestId(String.valueOf(question.getQuestionId()));
        myAskItem.setDestProfileId(question.getAskProfileId());
        myAskItem.setLineKey(inviteQuestionList);
        double score = Double.parseDouble("0." + String.valueOf(question.getQuestionId()));
        myAskItem.setScore(score);
        askWriteAbleHandler.insertAskItem(myAskItem);
        askRedis.addLine(myAskItem);


        GAlerter.lan("WanbaSearchService-processOneOnOneAnswer saveObject---start=====");
        WanbaSearchEntry entry = new WanbaSearchEntry();
        entry.setEntryid(AskUtil.getWanbaSearchEntryId(String.valueOf(answer.getQuestionId()), WanbaSearchType.QUESION));
        entry.setId(String.valueOf(answer.getQuestionId()));
        entry.setTitle(question.getTitle());
        entry.setType(WanbaSearchType.QUESION.getCode());
        eventProcessQueueThreadN.add(entry);
        GAlerter.lan("WanbaSearchService-processOneOnOneAnswer saveObject---end=====entry-->" + entry.toString());
    }

    private void processLimitAnswer(Answer answer) throws ServiceException {
        //修改第一个答案的逻辑
        String questionListLineKey = AskUtil.getAskLineKey(String.valueOf(answer.getQuestionId()), WanbaItemDomain.ANSERLIST);
        if (askRedis.countLine(questionListLineKey) == 1l) {
            modifyQuestion(new UpdateExpress().set(QuestionField.FIRSTANSWERID, answer.getAnswerId()), answer.getQuestionId());
        }
    }


    private boolean writeAnswerIdToQuestionList(Answer answer) throws ServiceException {
        String questionListLineKey = AskUtil.getAskLineKey(String.valueOf(answer.getQuestionId()), WanbaItemDomain.ANSERLIST);
        WanbaItem answerList = new WanbaItem();
        answerList.setOwnProfileId(answer.getAnswerProfileId());
        answerList.setItemType(ItemType.ANSWER);
        answerList.setCreateTime(answer.getAnswerTime());
        answerList.setItemDomain(WanbaItemDomain.ANSERLIST);
        answerList.setDestId(String.valueOf(answer.getAnswerId()));
        answerList.setLineKey(questionListLineKey);
        answerList.setScore(answer.getAnswerTime().getTime());
        askWriteAbleHandler.insertAskItem(answerList);
        askRedis.addLine(answerList);

        return true;
    }

    private boolean writeQuestionIdToMyAnswerLine(Answer answer) throws DbException {
        String myAnswerLineKey = AskUtil.getAskLineKey(answer.getAnswerProfileId(), WanbaItemDomain.MYANSER);
        WanbaItem myAskItem = new WanbaItem();
        myAskItem.setOwnProfileId(answer.getAnswerProfileId());
        myAskItem.setItemType(ItemType.ANSWER);
        myAskItem.setCreateTime(answer.getAnswerTime());
        myAskItem.setItemDomain(WanbaItemDomain.MYANSER);
        myAskItem.setDestId(String.valueOf(answer.getAnswerId()));
        myAskItem.setLineKey(myAnswerLineKey);
        myAskItem.setScore(answer.getAnswerTime().getTime() + Double.parseDouble("0." + String.valueOf(answer.getAnswerId())));
        askWriteAbleHandler.insertAskItem(myAskItem);
        askRedis.addLine(myAskItem);
        return true;
    }


    private boolean modifyWanbaProfileSum(WanbaProfileSumField field, int value, String profileId) throws DbException {
        boolean bval = askWriteAbleHandler.increaseWanbaProfileSum(field, value, profileId);
        if (bval) {
            bval = askRedis.increaseWanbaProfileSum(profileId, field, value);
        }
        return bval;
    }

    @Override
    public boolean acceptAnswer(Long answerId, Long questionId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " acceptAnswer answerId:" + answerId);
        }

        Date now = new Date();

        //修改问题
        UpdateExpress questionUpdateExpress = new UpdateExpress()
                .set(QuestionField.ACCEPTANSWERID, answerId)
                .set(QuestionField.QUESTIONSTATUS, QuestionStatus.ACCEPT.getCode());
        boolean bval = modifyQuestion(questionUpdateExpress, questionId);

        //修改问题
        if (bval) {
            UpdateExpress answerUpdateExpress = new UpdateExpress().set(AnswerField.IS_ACCEPT, true);
            bval = modifyAnswer(answerUpdateExpress, answerId);
        }

        Question question = getQuestion(questionId);
        Answer answer = getAnswer(answerId);
        processAcceptAnswer(question);

        //发送通知
        try {
            WanbaQuestionNoticeEvent wbNsIEvent = new WanbaQuestionNoticeEvent();
            wbNsIEvent.setProfileId(answer.getAnswerProfileId());
            wbNsIEvent.setType(NoticeType.ANSWER);
            wbNsIEvent.setQuestionId(question.getQuestionId());
            wbNsIEvent.setAnswerId(answerId);
            wbNsIEvent.setBodyType(WanbaNoticeBodyType.QUESTION_ACCEPTANSWER);
            wbNsIEvent.setCreateTime(now);
            EventDispatchServiceSngl.get().dispatch(wbNsIEvent);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " sendout WanbaNoticeSumIncreaseEvent answer error,e: ", e);
        }

        Set<String> set = new HashSet<String>();
        set.add(question.getAskProfileId());
        set.add(answer.getAnswerProfileId());
        eventProcessQueueThreadN.add(new NoticeBoardEntry(questionId, now, set));

        return bval;
    }


    @Override
    public PageRows<Answer> queryAnswer(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAnswer queryExpress:" + queryExpress + " page:" + page);
        }
        return askWriteAbleHandler.queryAnswer(queryExpress, page);
    }

    //处理采纳问题逻辑
    //从ing列表移除,放入到accept列表;从ing列表移除,放入到accept列表;进入搜索引擎
    private boolean processAcceptAnswer(Question question) throws ServiceException {
        //从ing列表移除,放入到accept列表
        String allIngLineKey = AskUtil.getAskLineKey(AskUtil.KEY_ALLGAME, WanbaItemDomain.TIMELIMIT_ING);
        String allIngItemId = AskUtil.getAskItemId(String.valueOf(question.getQuestionId()), allIngLineKey, ItemType.QUESTION);
        removeItemByLineKey(allIngItemId);

        String allAcceptLineKey = AskUtil.getAskLineKey(AskUtil.KEY_ALLGAME, WanbaItemDomain.TIMELIMIT_ACCEPT);
        WanbaItem allAcceptItem = new WanbaItem();
        allAcceptItem.setOwnProfileId(AskUtil.KEY_ALLGAME);
        allAcceptItem.setItemType(ItemType.QUESTION);
        allAcceptItem.setCreateTime(question.getCreateTime());
        allAcceptItem.setItemDomain(WanbaItemDomain.TIMELIMIT_ACCEPT);
        allAcceptItem.setDestId(String.valueOf(question.getQuestionId()));
        allAcceptItem.setDestProfileId(question.getAskProfileId());
        allAcceptItem.setLineKey(allAcceptLineKey);
        allAcceptItem.setScore(System.currentTimeMillis() + Double.parseDouble("0." + String.valueOf(question.getQuestionId())));
        addItemByLineKey(allAcceptItem);

        //从ing列表移除,放入到accept列表
        String gameIdString = String.valueOf(question.getGameId());
        String gameIngLineKey = AskUtil.getAskLineKey(gameIdString, WanbaItemDomain.TIMELIMIT_ING);
        String gameIngItemId = AskUtil.getAskItemId(String.valueOf(question.getQuestionId()), gameIngLineKey, ItemType.QUESTION);
        removeItemByLineKey(gameIngItemId);

        String gameAcceptLinkeKey = AskUtil.getAskLineKey(gameIdString, WanbaItemDomain.TIMELIMIT_ACCEPT);
        WanbaItem gameAcceptItem = new WanbaItem();
        gameAcceptItem.setOwnProfileId(gameIdString);
        gameAcceptItem.setItemType(ItemType.QUESTION);
        gameAcceptItem.setCreateTime(question.getCreateTime());
        gameAcceptItem.setItemDomain(WanbaItemDomain.TIMELIMIT_ACCEPT);
        gameAcceptItem.setDestId(String.valueOf(question.getQuestionId()));
        gameAcceptItem.setDestProfileId(question.getAskProfileId());
        gameAcceptItem.setLineKey(gameAcceptLinkeKey);
        gameAcceptItem.setScore(System.currentTimeMillis() + Double.parseDouble("0." + String.valueOf(question.getQuestionId())));
        addItemByLineKey(gameAcceptItem);

        //进入到游戏下面的推荐列表 TODO 改成手动添加到
//        if (question.getGameId() > 0l) {
//            QuestionSum questionSum = getQuestionSum(question.getQuestionId());
//            String gameRecommendKey = AskUtil.getAskLineKey(gameIdString, WanbaItemDomain.RECOMMEND);
//            WanbaItem gameRecommendItem = new WanbaItem();
//            gameRecommendItem.setOwnProfileId(gameIdString);
//            gameRecommendItem.setItemType(ItemType.QUESTION);
//            gameRecommendItem.setCreateTime(question.getCreateTime());
//            gameRecommendItem.setItemDomain(WanbaItemDomain.RECOMMEND);
//            gameRecommendItem.setDestId(String.valueOf(question.getQuestionId()));
//            gameRecommendItem.setDestProfileId(question.getAskProfileId());
//            gameRecommendItem.setLineKey(gameRecommendKey);
//            double score = questionSum != null ? questionSum.getViewSum() : 0;
//            score += Double.parseDouble("0." + String.valueOf(question.getQuestionId()));
//            gameRecommendItem.setScore(score);
//            addItemByLineKey(gameRecommendItem);
//        }

        //问题过期被采纳时，问题已过期，需要将过期列表移除
        if (question.getTimeLimit() < System.currentTimeMillis()) {
            //从所有的里面删除
            String allExpireLinkeKey = AskUtil.getAskLineKey(AskUtil.KEY_ALLGAME, WanbaItemDomain.TIMELIMIT_EXPIRE);
            boolean allBval = askRedis.removeItem(allExpireLinkeKey, String.valueOf(question.getQuestionId()), ItemType.QUESTION);
            if (allBval) {
                //过期的游戏里面删除
                if (question.getGameId() > 0l) {
                    String gameExpireLinkeKey = AskUtil.getAskLineKey(gameIdString, WanbaItemDomain.TIMELIMIT_EXPIRE);
                    askRedis.removeItem(gameExpireLinkeKey, String.valueOf(question.getQuestionId()), ItemType.QUESTION);
                }
            }

        }


        //进入搜索引擎
        WanbaSearchEntry entry = new WanbaSearchEntry();
        entry.setEntryid(AskUtil.getWanbaSearchEntryId(String.valueOf(question.getQuestionId()), WanbaSearchType.QUESION));
        entry.setId(String.valueOf(question.getQuestionId()));
        entry.setTitle(question.getTitle());
        entry.setType(WanbaSearchType.QUESION.getCode());
        entry.setTag(String.valueOf(question.getGameId()));
        eventProcessQueueThreadN.add(entry);
        return true;
    }


    @Override
    public Answer getAnswer(Long answerId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAnswer answerId:" + answerId);
        }

        Answer answer = askRedis.getAnswer(answerId);
        if (answer == null) {
            answer = askWriteAbleHandler.getAnswerById(answerId, IntValidStatus.UNVALID);
            if (answer != null) {
                askRedis.setAnswer(answer);
            }
        }
        return answer;
    }

    @Override
    public Map<Long, Answer> queryAnswerByAnswerIds(Collection<Long> answerIds) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAnswerByAnswerIds anserId:" + answerIds);
        }

        Map<Long, Answer> returnMap = new HashMap<Long, Answer>();
        Set<Long> queryDbId = new HashSet<Long>();

        //query by cache
        for (Long id : answerIds) {
            Answer answer = askRedis.getAnswer(id);
            if (answer != null) {
                returnMap.put(id, answer);
            } else {
                queryDbId.add(id);
            }
        }

        //query by db
        if (!CollectionUtil.isEmpty(queryDbId)) {
            List<Answer> answerListByDB = askWriteAbleHandler.queryAnswerById(new QueryExpress()
                    .add(QueryCriterions.eq(AnswerField.REMOVESTATUS, IntValidStatus.UNVALID.getCode()))
                    .add(QueryCriterions.in(AnswerField.ANSWERID, queryDbId.toArray())));
            for (Answer answerByDb : answerListByDB) {
                askRedis.setAnswer(answerByDb);
                returnMap.put(answerByDb.getAnswerId(), answerByDb);
            }
        }

        return returnMap;
    }


    @Override
    public boolean deleteAnswer(Long answerId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " addItemByLineKey anserId:" + answerId);
        }
        boolean bval = askWriteAbleHandler.updateAnswerById(new UpdateExpress().set(AnswerField.REMOVESTATUS, IntValidStatus.VALID.getCode()),
                answerId);
        if (bval) {
            Answer answer = askWriteAbleHandler.getAnswer(new QueryExpress().add(QueryCriterions.eq(AnswerField.ANSWERID, answerId)));
            String questionListLineKey = AskUtil.getAskLineKey(String.valueOf(answer.getQuestionId()), WanbaItemDomain.ANSERLIST);
            askRedis.removeItem(questionListLineKey, String.valueOf(answerId), ItemType.ANSWER);
            bval = askRedis.delAnswer(answerId);
        }

        return bval;
    }

    @Override
    public PageRows<Answer> queryAnswerByQuestionId(Long questionId, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " addItemByLineKey questionId:" + questionId);
        }
        PageRows<Answer> pageRows = new PageRows<Answer>();

        String questionListLineKey = AskUtil.getAskLineKey(String.valueOf(questionId), WanbaItemDomain.ANSERLIST);
        int size = (int) askRedis.getLineSize(questionListLineKey);
        page.setTotalRows(size);
        pageRows.setPage(page);
        Map<String, ItemType> idMap = askRedis.queryItemByLine(questionListLineKey, page, true);
        if (CollectionUtil.isEmpty(idMap)) {
            return pageRows;
        }

        //build
        Set<Long> answerIds = new LinkedHashSet<Long>();
        for (String id : idMap.keySet()) {
            answerIds.add(Long.valueOf(id));
        }
        Map<Long, Answer> answerMap = queryAnswerByAnswerIds(answerIds);

        //build result
        List<Answer> answerList = new ArrayList<Answer>();
        for (Long id : answerIds) {
            if (answerMap.containsKey(id)) {
                answerList.add(answerMap.get(id));
            }
        }
        pageRows.setRows(answerList);
        return pageRows;
    }


    @Override
    public PageRows<Answer> queryAnswerByLineKeyRange(String lineKey, Pagination page, boolean isDesc) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryQuestionByLineKeyRange lineKey:" + lineKey + " page:" + page + " isDesc:" + isDesc);
        }


        PageRows<Answer> rows = new PageRows<Answer>();

        List<Answer> questionList = new ArrayList<Answer>();
        Map<String, ItemType> idMap = askRedis.queryItemByLine(lineKey, page, true);
        if (CollectionUtil.isEmpty(idMap)) {
            return rows;
        }

        //build page
        int size = (int) askRedis.getLineSize(lineKey);
        page.setTotalRows(size);

        //build queryidSet and get QuestionMap
        Set<Long> queryIdSet = new LinkedHashSet<Long>();
        for (String id : idMap.keySet()) {
            queryIdSet.add(Long.valueOf(id));
        }
        Map<Long, Answer> questionMap = queryAnswerByAnswerIds(queryIdSet);

        //build result

        for (Long id : queryIdSet) {
            if (questionMap.containsKey(id)) {
                questionList.add(questionMap.get(id));
            }
        }

        rows.setPage(page);
        rows.setRows(questionList);

        return rows;
    }

    @Override
    public Map<String, ItemType> queryItemByLineKey(String lineKey, Pagination page, boolean isDesc) throws ServiceException {
        return askRedis.queryItemByLine(lineKey, page, isDesc);
    }

    @Override
    public Long getItemByLineKeySize(String lineKey) throws ServiceException {
        return askRedis.countLine(lineKey);
    }

    @Override
    public boolean addItemByLineKey(WanbaItem wanbaItem) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " addItemByLineKey wanbaItem:" + wanbaItem);
        }
        String lineKey = AskUtil.getAskLineKey(wanbaItem.getOwnProfileId(), wanbaItem.getItemDomain());

        WanbaItem askMeItem = new WanbaItem();
        askMeItem.setOwnProfileId(wanbaItem.getOwnProfileId());
        askMeItem.setItemType(wanbaItem.getItemType());
        askMeItem.setCreateTime(new Date());
        askMeItem.setItemDomain(wanbaItem.getItemDomain());
        askMeItem.setDestId(wanbaItem.getDestId());
        askMeItem.setDestProfileId(wanbaItem.getDestProfileId());
        askMeItem.setLineKey(lineKey);
        askMeItem.setScore(wanbaItem.getScore());
        askWriteAbleHandler.insertAskItem(askMeItem);

        askRedis.addLine(wanbaItem);
        return true;
    }


    @Override
    public boolean removeItemByLineKey(String wanbaItemId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " removeItemByLineKey wanbaItem:" + wanbaItemId);
        }
        WanbaItem wanbaItem = askWriteAbleHandler.getAskItem(wanbaItemId);
        if (wanbaItem != null) {
            boolean bval = askWriteAbleHandler.modifyAskItem(new UpdateExpress().set(WanbaItemField.VALIDSTATUS, ValidStatus.REMOVED.getCode()), wanbaItem.getItemId());
            if (bval) {

                //特殊，推荐标签
                String lineKey = AskUtil.getAskLineKey(wanbaItem.getOwnProfileId(), wanbaItem.getItemDomain());
                askRedis.removeItem(lineKey, wanbaItem.getDestId(), wanbaItem.getItemType());

                //处理游戏标签
                Question question = getQuestion(Long.valueOf(wanbaItem.getDestId()));
                if (question != null && question.getGameId() > 0) {
                    String gameIdString = String.valueOf(question.getGameId());
                    String gameRecommendKey = AskUtil.getAskLineKey(gameIdString, WanbaItemDomain.RECOMMEND);
                    askRedis.removeItem(gameRecommendKey, String.valueOf(question.getQuestionId()), ItemType.QUESTION);

                    //修改wanba_item状态
                    askWriteAbleHandler.modifyAskItem(new UpdateExpress().set(WanbaItemField.VALIDSTATUS,
                            ValidStatus.REMOVED.getCode()), AskUtil.getAskItemId(wanbaItem.getDestId(), gameRecommendKey, ItemType.QUESTION));
                }
            }
        }
        return false;
    }


    //////////////////
    @Override
    public Map<Long, QuestionSum> queryQuestionSumByQids(Collection<Long> qids) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryProfileByIds qids:" + qids);
        }

        Map<Long, QuestionSum> returnMap = new LinkedHashMap<Long, QuestionSum>();
        for (Long qid : qids) {
            returnMap.put(qid, getQuestionSum(qid));
        }
        return returnMap;
    }


    public boolean modifyQuestionSum(QuestionSumField field, int value, long questionId) throws ServiceException {
        boolean bval = askWriteAbleHandler.increaseQuestionSum(field, value, questionId);
        if (bval) {
            bval = askRedis.increaseQuestionSum(questionId, field, value);
        }
        return bval;
    }


    @Override
    public List<WanbaProfileClassify> queryWanbaProfileClassifyList(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryWanbaProfileClassify queryExpress:" + queryExpress);
        }
        return askWriteAbleHandler.queryWanbaProfileClassifyList(queryExpress);
    }

    @Override
    public PageRows<WanbaProfileClassify> queryWanbaProfileClassify(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryWanbaProfileClassify queryExpress:" + queryExpress + ",pagination=" + pagination);
        }
        return askWriteAbleHandler.queryWanbaProfileClassify(queryExpress, pagination);
    }

    @Override
    public WanbaProfileClassify getWanbaProfileClassify(String classifyid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getWanbaProfileClassify classifyid:" + classifyid);
        }
        WanbaProfileClassify classify = askRedis.getWanbaProfileClassify(classifyid);
        if (classify == null) {
            classify = askWriteAbleHandler.getWanbaProfileClassify(classifyid);
            if (classify != null) {
                askRedis.setWanbaProfileClassify(classify);
            }
        }
        return classify;
    }


    @Override
    public Map<String, WanbaProfileClassify> getWanbaProfileClassifyMap(Set<String> pidSet) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getWanbaProfileClassifyMap pidSet:" + pidSet);
        }
        Map<String, WanbaProfileClassify> returnMap = new LinkedHashMap<String, WanbaProfileClassify>();
        for (String pid : pidSet) {
            WanbaProfileClassify classify = getWanbaProfileClassify(AskUtil.getWanbaProfileClassifyId(pid, WanbaProfileClassifyType.WANBA_ASK_VIRTUAL));
            if (classify != null) {
                returnMap.put(pid, classify);
            }
        }
        return returnMap;
    }

    @Override
    public boolean modifyWanbaProfileClassify(UpdateExpress updateExpress, String classifyid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyWanbaProfileClassify updateExpress:" + updateExpress + ",classifyid=" + classifyid);
        }
        boolean bval = askWriteAbleHandler.modifyWanbaProfileClassify(updateExpress, new QueryExpress().add(QueryCriterions.eq(WanbaProfileClassifyField.CLASSIFY_ID, classifyid)));
        if (bval) {
            askRedis.delWanbaProfileClassify(classifyid);
        }
        return bval;
    }

    @Override
    public WanbaProfileClassify insertWanbaProfileClassify(WanbaProfileClassify wanbaProfileClassify) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " insertWanbaProfileClassify wanbaProfileClassify:" + wanbaProfileClassify);
        }
        wanbaProfileClassify = askWriteAbleHandler.insertWanbaProfileClassify(wanbaProfileClassify);
        askRedis.setWanbaProfileClassify(wanbaProfileClassify);
        return wanbaProfileClassify;
    }

    private QuestionSum getQuestionSum(long qid) {
        //todo 容错
        return askRedis.getQuestionSumObjById(qid);
    }

    @Override
    public Answer getAnswerByProfile(Long qid, String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAnswerByProfile qid:" + qid + "profileId:" + profileId);
        }

        return askWriteAbleHandler.getAnswerByQidAndProfileId(qid, profileId);
    }

    @Override
    public Boolean addAskUserAction(AskUserAction askUserAction) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " agreeAnswer askUserAction:" + askUserAction);
        }
        askUserAction.setAskUserActionId(AskUtil.getAskUserActionId(askUserAction.getProfileId(), askUserAction.getDestId(), askUserAction.getItemType(), askUserAction.getActionType()));
        AskUserAction actionByQuey = getAskUserAction(askUserAction.getAskUserActionId());
        if (actionByQuey != null) {
            return false;
        }

        if (askUserAction.getActionType().equals(AskUserActionType.AGREEMENT)) {
            modifyAnswerSum(AnswerSumField.AGREESUM, 1, askUserAction.getDestId());
        } else if (askUserAction.getActionType().equals(AskUserActionType.FAVORITE)) {
            modifyWanbaProfileSum(WanbaProfileSumField.FAVORITESUM, 1, askUserAction.getProfileId());
        }


        askUserAction = askWriteAbleHandler.insertAskUserAction(askUserAction);
        askRedis.setAskUserAction(askUserAction);
        return askUserAction != null;
    }

    @Override
    public Boolean removeAskUserAction(AskUserAction askUserAction) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " removeAskUserAction askUserAction:" + askUserAction);
        }
        askUserAction.setAskUserActionId(AskUtil.getAskUserActionId(askUserAction.getProfileId(), askUserAction.getDestId(), askUserAction.getItemType(), askUserAction.getActionType()));
        AskUserAction actionByQuey = getAskUserAction(askUserAction.getAskUserActionId());
        if (actionByQuey == null) {
            return false;
        }
        boolean bvale = askWriteAbleHandler.deleteAskUserAction(askUserAction.getAskUserActionId());

        if (bvale) {
            if (askUserAction.getActionType().equals(AskUserActionType.FAVORITE)) {
                modifyWanbaProfileSum(WanbaProfileSumField.FAVORITESUM, -1, askUserAction.getProfileId());
            }
            askRedis.deleteUserAskAction(askUserAction.getAskUserActionId());
        }
        return bvale;
    }

    public AskUserAction getAskUserAction(String askUserActionId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAskUserAction askUserActionId:" + askUserActionId);
        }
        AskUserAction askUserAction = askRedis.getUserAskAction(askUserActionId);
        if (askUserAction == null) {
            askUserAction = askWriteAbleHandler.getAskUserAction(askUserActionId);
            if (askUserAction != null) {
                askRedis.setAskUserAction(askUserAction);
            }
        }
        return askUserAction;
    }

    @Override
    public Map<Long, AskUserAction> checkAgreeAskUserAction(Collection<Long> answerIds, String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " checkAgreeAskUserAction answerIds:" + answerIds + ",profileId:" + profileId);
        }

        Map<Long, AskUserAction> returnMap = new HashMap<Long, AskUserAction>();
        for (Long answerId : answerIds) {
            AskUserAction askUserAction = getAskUserAction(AskUtil.getAskUserActionId(profileId,
                    answerId, ItemType.ANSWER, AskUserActionType.AGREEMENT));
            if (askUserAction != null) {
                returnMap.put(answerId, askUserAction);
            }
        }
        return returnMap;
    }

    @Override
    public PageRows<AskUserAction> queryAskUserAction(String profileid, ItemType itemType, AskUserActionType askUserActionType, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAskUserAction profileid:" + profileid + ",profileid=" + page);
        }

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AskUserActionField.PROFILEID, profileid));
        queryExpress.add(QueryCriterions.eq(AskUserActionField.ITEMTYPE, itemType.getCode()));
        queryExpress.add(QueryCriterions.eq(AskUserActionField.ACTIONTYPE, askUserActionType.getCode()));
        queryExpress.add(QuerySort.add(AskUserActionField.CREATETIME, QuerySortOrder.DESC));
        return askWriteAbleHandler.queryAskUserAction(queryExpress, page);
    }

    @Override
    public PageRows<AskUserAction> queryAskUserActionByList(Long destId, ItemType itemType, AskUserActionType askUserActionType, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAskUserAction itemType:" + itemType + ",askUserActionType=" + askUserActionType.getCode() + " destId=" + destId);
        }

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AskUserActionField.ITEMTYPE, itemType.getCode()));
        queryExpress.add(QueryCriterions.eq(AskUserActionField.ACTIONTYPE, askUserActionType.getCode()));
        queryExpress.add(QueryCriterions.eq(AskUserActionField.DESTID, destId));
        queryExpress.add(QuerySort.add(AskUserActionField.CREATETIME, QuerySortOrder.DESC));
        return askWriteAbleHandler.queryAskUserAction(queryExpress, page);
    }

    @Override
    public boolean modifyAnswerSum(AnswerSumField field, int value, long answerId) throws ServiceException {
        boolean bval = askWriteAbleHandler.increaseAnswerSum(field, value, answerId);
        if (bval) {
            bval = askRedis.increaseAnswerSum(answerId, field, value);
        }
        return bval;
    }

    @Override
    public Map<Long, AnswerSum> queryAnswerSumByAids(Collection<Long> aids) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAnswerSumByaids aids:" + aids);
        }

        Map<Long, AnswerSum> returnMap = new LinkedHashMap<Long, AnswerSum>();
        for (Long aid : aids) {
            returnMap.put(aid, getAnswerSum(aid));
        }
        return returnMap;
    }

    private AnswerSum getAnswerSum(long aid) {
        //todo 容错
        return askRedis.getAnswerSumObjById(aid);
    }

    ////////////////////////////////////////////////////////////////////////


    @Override
    public boolean followQuestion(Long questionId, String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " followQuestion questionId:" + questionId + " profileId:" + profileId);
        }

        boolean exists = askRedis.profileExistsFollowQuestionList(questionId, profileId);
        if (exists) {
            return false;
        }

        Date createTime = new Date();
        AskUserAction askUserAction = new AskUserAction();
        askUserAction.setActionType(AskUserActionType.FOLLOW);
        askUserAction.setCreateTime(createTime);
        askUserAction.setDestId(questionId);
        askUserAction.setItemType(ItemType.QUESTION);
        askUserAction.setProfileId(profileId);
        askUserAction.setValue(String.valueOf(questionId));
        askWriteAbleHandler.insertAskUserAction(askUserAction);

        //insert follow list
        askRedis.followQuestion(questionId, profileId, createTime.getTime());

        //modify follow sum
        modifyWanbaProfileSum(WanbaProfileSumField.QUESIONFOLLOWSUM, 1, profileId);
        modifyQuestionSum(QuestionSumField.FOLLOWSUM, 1, questionId);
        return true;
    }

    @Override
    public boolean unfollowQuestion(Long questionId, String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " unfollowQuestion questionId:" + questionId + " profileId:" + profileId);
        }

        //移除列表
        boolean bval = askRedis.unFollowQuestion(questionId, profileId);
        if (bval) {
            bval = askWriteAbleHandler.deleteAskUserAction(AskUtil.getAskUserActionId(profileId, questionId, ItemType.QUESTION, AskUserActionType.FOLLOW));
            modifyWanbaProfileSum(WanbaProfileSumField.QUESIONFOLLOWSUM, -1, profileId);
            modifyQuestionSum(QuestionSumField.FOLLOWSUM, -1, questionId);
        }
        return bval;
    }

    @Override
    public Set<String> queryFollowProfileIds(Long questionId, ScoreRange range) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryFollowProfile questionId:" + questionId + " range:" + range);
        }

        Set<String> idSet = askRedis.queryFollowProfileIdsByQuestionId(questionId, range);
        return idSet;
    }

    @Override
    public ScoreRangeRows<Question> queryFollowQuestionByProfileId(String profileId, ScoreRange range) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryFollowQuestionByProfileId profileId:" + profileId + " range:" + range);
        }


        Set<String> idSet = askRedis.queryFollowQuestionByProfileId(profileId, range);
        ScoreRangeRows<Question> pageRows = new ScoreRangeRows<Question>();
        pageRows.setRange(range);
        if (CollectionUtil.isEmpty(idSet)) {
            return pageRows;
        }

        Set<Long> queryIdSet = new LinkedHashSet<Long>();
        for (String id : idSet) {
            queryIdSet.add(Long.valueOf(id));
        }
        Map<Long, Question> questionMap = queryQuestionByIds(queryIdSet);

        //build result
        List<Question> questionList = new ArrayList<Question>();
        for (Long id : queryIdSet) {
            if (questionMap.containsKey(id)) {
                questionList.add(questionMap.get(id));
            }
        }
        pageRows.setRows(questionList);
        return pageRows;
    }

    @Override
    public Set<Long> checkFollowQuestion(Collection<Long> questionIds, String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " checkFollowQuestion questionIds:" + questionIds + " profileId:" + profileId);
        }
        Set<Long> result = new HashSet<Long>();
        Set<String> questionIdStrSet = askRedis.checkFollowQuestion(questionIds, profileId);
        for (String idStr : questionIdStrSet) {
            result.add(Long.parseLong(idStr));
        }
        return result;
    }


    @Override
    public boolean inviteAnswerQuestion(Long quesitonId, String inviteProfile, String invitedProfile) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " inviteAnswerQuestion quesitonId:" + quesitonId + " invitedProfiles:" + inviteProfile + " invitedProfile:" + invitedProfile);
        }

        Date createTime = new Date();
        AskUserAction askUserAction = new AskUserAction();
        askUserAction.setAskUserActionId(AskUtil.getAskUserActionId(invitedProfile, quesitonId, ItemType.QUESTION, AskUserActionType.INIVITED));
        askUserAction.setActionType(AskUserActionType.INIVITED);
        askUserAction.setCreateTime(createTime);
        askUserAction.setDestId(quesitonId);
        askUserAction.setItemType(ItemType.QUESTION);
        askUserAction.setProfileId(invitedProfile);
        askUserAction.setValue(inviteProfile);

        //sendout notice
        try {
            askUserAction = askWriteAbleHandler.insertAskUserAction(askUserAction);
            askRedis.addInvitePofile(quesitonId, invitedProfile, createTime.getTime());


            WanbaQuestionNoticeEvent wbNsIEvent = new WanbaQuestionNoticeEvent();
            wbNsIEvent.setProfileId(invitedProfile);
            wbNsIEvent.setType(NoticeType.ANSWER);
            wbNsIEvent.setQuestionId(quesitonId);
            wbNsIEvent.setBodyType(WanbaNoticeBodyType.QUESTION_INVITE_ANSWERQUESTION);
            wbNsIEvent.setCreateTime(createTime);
            wbNsIEvent.setDestProfileId(inviteProfile);
            EventDispatchServiceSngl.get().dispatch(wbNsIEvent);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " sendout WanbaNoticeSumIncreaseEvent answer error,e: ", e);
            return false;
        }

        followQuestion(quesitonId, inviteProfile);
        return true;
    }

    @Override
    public Set<String> checkInviteProfiles(Collection<String> inviteProfiles, long questionId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " checkInviteProfiles profileIds:" + inviteProfiles + " questionId:" + questionId);
        }

        Set<String> profileIds = askRedis.checkInviteProfile(inviteProfiles, questionId);
        return new HashSet<String>(profileIds);
    }

    @Override
    public PageRows<WanbaItem> queryWanbaItem(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryWanbaItem tagId:" + queryExpress + " queryExpress:" + pagination);
        }
        return askWriteAbleHandler.queryWanbaItem(queryExpress, pagination);
    }

    @Override
    public WanbaItem getWanbaItem(String itemId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getWanbaItem itemId:" + itemId);
        }
        return askWriteAbleHandler.getAskItem(itemId);
    }

    @Override
    public boolean modifyWanbaItem(String itemId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " updateWanbaItem itemId:" + itemId + ",updateExpress:" + updateExpress);
        }
        boolean bval = askWriteAbleHandler.modifyAskItem(updateExpress, itemId);
        if (bval) {
            WanbaItem wanbaItem = getWanbaItem(itemId);
            if (wanbaItem != null && wanbaItem.getValidStatus().equals(ValidStatus.VALID)) {
                askRedis.addLine(wanbaItem);

                //处理游戏标签aa
                Question question = getQuestion(Long.valueOf(wanbaItem.getDestId()));
                if (question != null && question.getGameId() > 0) {
                    String gameIdString = String.valueOf(question.getGameId());
                    String gameRecommendKey = AskUtil.getAskLineKey(gameIdString, WanbaItemDomain.RECOMMEND);

                    String gameItemId = AskUtil.getAskItemId(wanbaItem.getDestId(), gameRecommendKey, ItemType.QUESTION);
                    WanbaItem gameWanbaItem = getWanbaItem(gameItemId);

                    if (gameWanbaItem != null) {
                        askRedis.addLine(gameWanbaItem);

                        //修改wanba_item状态
                        askWriteAbleHandler.modifyAskItem(new UpdateExpress().set(WanbaItemField.VALIDSTATUS,
                                ValidStatus.VALID.getCode()), gameItemId);
                    }
                }
            } else {
                askRedis.removeItem(wanbaItem.getLineKey(), String.valueOf(wanbaItem.getDestId()), ItemType.QUESTION);

                //处理游戏标签
                Question question = getQuestion(Long.valueOf(wanbaItem.getDestId()));
                if (question != null && question.getGameId() > 0) {
                    String gameIdString = String.valueOf(question.getGameId());
                    String gameRecommendKey = AskUtil.getAskLineKey(gameIdString, WanbaItemDomain.RECOMMEND);
                    askRedis.removeItem(gameRecommendKey, String.valueOf(question.getQuestionId()), ItemType.QUESTION);

                    //修改wanba_item状态
                    askWriteAbleHandler.modifyAskItem(new UpdateExpress().set(WanbaItemField.VALIDSTATUS,
                            ValidStatus.REMOVED.getCode()), AskUtil.getAskItemId(wanbaItem.getDestId(), gameRecommendKey, ItemType.QUESTION));
                }

            }
        }
        return bval;
    }

    @Override
    public WanbaItem addWanbaItem(WanbaItem wanbaItem) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " addWanbaItem wanbaItem:" + wanbaItem);
        }
        wanbaItem = askWriteAbleHandler.insertAskItem(wanbaItem);

        askRedis.addLine(wanbaItem);


        return wanbaItem;
    }

    private void boardFollowQuestionHasAccept(NoticeBoardEntry entry) {
        ScoreRange scoreRange = new ScoreRange(-1, System.currentTimeMillis(), 200);
        do {
            Set<String> profileIdSet = askRedis.queryFollowProfileIdsByQuestionId(entry.getQuestionId(), scoreRange);

            for (String pid : profileIdSet) {
                if (!CollectionUtil.isEmpty(entry.getExceptProfileIds()) && !entry.getExceptProfileIds().contains(pid)) {
                    try {
                        WanbaQuestionNoticeEvent wbNsIEvent = new WanbaQuestionNoticeEvent();
                        wbNsIEvent.setProfileId(pid);
                        wbNsIEvent.setType(NoticeType.ANSWER);
                        wbNsIEvent.setQuestionId(entry.getQuestionId());
                        wbNsIEvent.setBodyType(WanbaNoticeBodyType.QUESTION_FOLLOWQUESIONACCEPT);
                        wbNsIEvent.setCreateTime(entry.getCreateTime());
                        EventDispatchServiceSngl.get().dispatch(wbNsIEvent);
                    } catch (ServiceException e) {
                        GAlerter.lab(this.getClass().getName() + " sendout WanbaNoticeSumIncreaseEvent answer error,e: ", e);
                    }
                }
            }

            if (scoreRange.isHasnext()) {
                break;
            } else {
                scoreRange.setMax(scoreRange.getScoreflag());
            }
        } while (true);
    }

    public void checkExpireQuestion() throws ServiceException {
        long now = System.currentTimeMillis();
        Pagination page = new Pagination(200, 1, 200);

        do {
            PageRows<Question> questionPageRows = askWriteAbleHandler.queryQuestion(new QueryExpress()
                    .add(QueryCriterions.leq(QuestionField.TIMELIMIT, now))
                    .add(QueryCriterions.eq(QuestionField.TYPE, QuestionType.TIMELIMIT.getCode()))
                    .add(QueryCriterions.eq(QuestionField.QUESTIONSTATUS, QuestionStatus.INIT.getCode()))
                    .add(QueryCriterions.eq(QuestionField.REMOVESTATUS, IntValidStatus.UNVALID.getCode())), page);

            for (Question question : questionPageRows.getRows()) {
                //从ing列表移除,放入到accept列表
                String allIngLineKey = AskUtil.getAskLineKey(AskUtil.KEY_ALLGAME, WanbaItemDomain.TIMELIMIT_ING);
                String allIngItemId = AskUtil.getAskItemId(String.valueOf(question.getQuestionId()), allIngLineKey, ItemType.QUESTION);
                boolean bval = removeItemByLineKey(allIngItemId);
                if (bval) {
                    String allExpireLinkeKey = AskUtil.getAskLineKey(AskUtil.KEY_ALLGAME, WanbaItemDomain.TIMELIMIT_EXPIRE);
                    WanbaItem allExpireItem = new WanbaItem();
                    allExpireItem.setOwnProfileId(AskUtil.KEY_ALLGAME);
                    allExpireItem.setItemType(ItemType.QUESTION);
                    allExpireItem.setCreateTime(question.getCreateTime());
                    allExpireItem.setItemDomain(WanbaItemDomain.TIMELIMIT_EXPIRE);
                    allExpireItem.setDestId(String.valueOf(question.getQuestionId()));
                    allExpireItem.setDestProfileId(question.getAskProfileId());
                    allExpireItem.setLineKey(allExpireLinkeKey);
                    allExpireItem.setScore(question.getTimeLimit() + Double.parseDouble("0." + String.valueOf(question.getQuestionId())));
                    addItemByLineKey(allExpireItem);
                }

                //从ing列表移除,放入到accept列表
                if (question.getGameId() > 0l) {
                    String gameIdString = String.valueOf(question.getGameId());
                    String gameIngLineKey = AskUtil.getAskLineKey(gameIdString, WanbaItemDomain.TIMELIMIT_ING);
                    String gameIngItemId = AskUtil.getAskItemId(String.valueOf(question.getQuestionId()), gameIngLineKey, ItemType.QUESTION);
                    bval = removeItemByLineKey(gameIngItemId);

                    if (bval) {
                        String gameExpireLinkeKey = AskUtil.getAskLineKey(gameIdString, WanbaItemDomain.TIMELIMIT_EXPIRE);
                        WanbaItem gameExpireItem = new WanbaItem();
                        gameExpireItem.setOwnProfileId(gameIdString);
                        gameExpireItem.setItemType(ItemType.QUESTION);
                        gameExpireItem.setCreateTime(question.getCreateTime());
                        gameExpireItem.setItemDomain(WanbaItemDomain.TIMELIMIT_EXPIRE);
                        gameExpireItem.setDestId(String.valueOf(question.getQuestionId()));
                        gameExpireItem.setDestProfileId(question.getAskProfileId());
                        gameExpireItem.setLineKey(gameExpireLinkeKey);
                        gameExpireItem.setScore(question.getTimeLimit() + Double.parseDouble("0." + String.valueOf(question.getQuestionId())));
                        addItemByLineKey(gameExpireItem);
                    }
                }
                modifyQuestion(new UpdateExpress().set(QuestionField.QUESTIONSTATUS, QuestionStatus.TIMEOUT.getCode()), question.getQuestionId());

                //通知过期
                try {
                    WanbaQuestionNoticeEvent wbNsIEvent = new WanbaQuestionNoticeEvent();
                    wbNsIEvent.setProfileId(question.getAskProfileId());
                    wbNsIEvent.setType(NoticeType.ANSWER);
                    wbNsIEvent.setQuestionId(question.getQuestionId());
                    wbNsIEvent.setBodyType(WanbaNoticeBodyType.QUESTION_EXPIRE);
                    wbNsIEvent.setCreateTime(new Date());
                    EventDispatchServiceSngl.get().dispatch(wbNsIEvent);
                } catch (ServiceException e) {
                    GAlerter.lab(this.getClass().getName() + " sendout WanbaNoticeSumIncreaseEvent answer error,e: ", e);
                }
            }


            if (page.isLastPage()) {
                break;
            }
        } while (true);
    }

    @Override
    public WanbaProfileSum getWanProfileSum(String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getWanProfileSum profileId:" + profileId);
        }
        WanbaProfileSum wanbaProfileSum = askRedis.getWanbaSumObjById(profileId);
        if (wanbaProfileSum == null) {
            wanbaProfileSum = askWriteAbleHandler.getWanbaProfileSum(profileId);
            if (wanbaProfileSum != null) {
                askRedis.setWanbaSumObjById(profileId, wanbaProfileSum);
            }
        }
        return wanbaProfileSum;
    }

    @Override
    public WanbaProfileSum insertWanbaProfileSum(WanbaProfileSum wanbaProfileSum) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " WanbaProfileSum wanbaProfileSum:" + wanbaProfileSum.toString());
        }
        return askWriteAbleHandler.insertWanbaProfileSum(wanbaProfileSum);
    }

    @Override
    public Map<String, WanbaProfileSum> queryWanbaProfileSumByPids(Collection<String> profileIds) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryWanbaProfileSumByPids profileIds:" + profileIds);
        }
        Map<String, WanbaProfileSum> returnMap = new LinkedHashMap<String, WanbaProfileSum>();
        for (String profilie : profileIds) {
            returnMap.put(profilie, getWanProfileSum(profilie));
        }
        return returnMap;
    }

    @Override
    public List<String> getWanbaRecommentProfiles() throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getWanbaRecommentProfiles ");
        }
        return askRedis.getWanbaRecommentProfiles();
    }

    @Override
    public boolean updateWanbaRecommentProfiles(Collection<String> profileIds) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " updateWanbaRecommentProfiles profileIds:" + profileIds);
        }
        return askRedis.updateWanbaRecommentProfiles(profileIds);
    }


    @Override
    public AskReport insertAskReport(AskReport askReport) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " insertAskReport askReport:" + askReport);
        }
        AskReport returnAskReport = askRedis.getAskReport(askReport.getReportId());
        if (returnAskReport == null) {
            returnAskReport = askWriteAbleHandler.insertAskReport(askReport);
            if (returnAskReport != null) {
                askRedis.setAskReport(returnAskReport);
            }
        }
        return returnAskReport;
    }

    @Override
    public boolean modifyAskReport(UpdateExpress updateExpress, String report_id) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyAskReport report_id:" + report_id);
        }
        boolean bval = askWriteAbleHandler.modifyAskReport(updateExpress, report_id);
        if (bval) {
            AskReport askReport = askWriteAbleHandler.getAskReport(report_id);
            if (askReport != null) {
                askRedis.setAskReport(askReport);
            }
        }
        return bval;
    }

    @Override
    public AskReport getAskReport(String report_id) throws ServiceException {
        AskReport askReport = askRedis.getAskReport(report_id);
        if (askReport == null) {
            askReport = askWriteAbleHandler.getAskReport(report_id);
            if (askReport != null) {
                askRedis.setAskReport(askReport);
            }
        }
        return askReport;
    }

    @Override
    public PageRows<AskReport> queryAskReport(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAskReport queryExpress:" + queryExpress);
        }
        return askWriteAbleHandler.queryAskReport(queryExpress, pagination);
    }

    @Override
    public PageRows<AskTimedRelease> queryAskTimedRelease(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAskTimedRelease queryExpress:" + queryExpress);
        }
        return askWriteAbleHandler.queryAskTimedRelease(queryExpress, pagination);
    }

    @Override
    public AskTimedRelease insertAskTimedRelease(AskTimedRelease askTimedRelease) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " insertAskTimedRelease askTimedRelease:" + askTimedRelease);
        }
        return askWriteAbleHandler.insertAskTimedRelease(askTimedRelease);
    }

    @Override
    public boolean modifyAskTimedRelease(UpdateExpress updateExpress, Long timeid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyAskTimedRelease timeid:" + timeid);
        }
        return askWriteAbleHandler.modifyAskTimedRelease(updateExpress, timeid);
    }

    @Override
    public AskTimedRelease getAskTimedRelease(Long timeid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAskTimedRelease timeid:" + timeid);
        }
        return askWriteAbleHandler.getAskTimedRelease(timeid);
    }

    @Override
    public Content postContentWiki(Content content) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " postContentWiki content:" + content);
        }
        //wiki提交
        content = askWriteAbleHandler.insertContent(content);

        askRedis.setContent(content);

        return content;
    }

    @Override
    public Content postContent(Content content) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " post Content content:" + content);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ContentField.COMMENT_ID, content.getCommentId()));
        Content oldContent = askWriteAbleHandler.getContent(queryExpress);
        //更新
        if (oldContent != null) {
            processUpdateContent(oldContent, content);
        } else {
            //write to db and redis cache object
            content = askWriteAbleHandler.insertContent(content);
            askRedis.setContent(content);

            processInsertContent(content);


            //solor
            addSearchEvent(content);
        }

        //有新文章发布
        //asRedis.removeAllGameAndArchiveLinekey();
        return content;
    }

    @Override
    public Content getContent(String commentid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getContent commentid:" + commentid);
        }
        Content content = askRedis.getContentByCommentid(commentid);
        if (content == null) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ContentField.COMMENT_ID, commentid));
            queryExpress.add(QueryCriterions.eq(ContentField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            content = askWriteAbleHandler.getContent(queryExpress);

            if (content != null) {
                askRedis.setContent(content);
            }
        }
        return content;
    }

    @Override
    public boolean updateContentStatus(String commentId, ValidStatus validStatus) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " deleteContent commentId:" + commentId);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ContentField.COMMENT_ID, commentId));
        Content content = askWriteAbleHandler.getContent(queryExpress);
        if (content == null) {
            return false;
        }

        boolean bval = askWriteAbleHandler.updateContent(new UpdateExpress().set(ContentField.REMOVE_STATUS, validStatus.getCode()),
                new QueryExpress().add(QueryCriterions.eq(ContentField.ID, content.getId())));
        if (bval) {
            String allLineKey = AskUtil.getContentLineKey(ContentLineOwn.ALL_ARCHIVE, "");

            QueryExpress allQuery = new QueryExpress();
            allQuery.add(QueryCriterions.eq(ContentLineField.LINE_KEY, allLineKey));
            allQuery.add(QueryCriterions.eq(ContentLineField.DEST_ID, content.getId()));
            if (validStatus.getCode().equals(ValidStatus.VALID.getCode())) {
                //更新commnet_line
                askWriteAbleHandler.updateContentLine(new UpdateExpress().set(ContentLineField.VALID_STATUS, validStatus.getCode()), allQuery);

                //放入redis
                askRedis.setContent(content);

                //加入所有文章下
                askRedis.addContentLine(allLineKey, content.getPublishTime().getTime() + Double.parseDouble("0." + String.valueOf(content.getId())), content.getId());

                //solr
                addSearchEvent(content);
            } else {
                //更新commnet_line
                askWriteAbleHandler.updateContentLine(new UpdateExpress().set(ContentLineField.VALID_STATUS, validStatus.getCode()), allQuery);

                //删除单个对象
                askRedis.delContent(content.getId());

                //删除所有文章下
                askRedis.removeContentLine(allLineKey, content.getId());

                //solr
                delSearchEvent(content);
            }

        }
        return bval;
    }

    @Override
    public ScoreRangeRows<Content> queryContentByScoreRangeRows(String linekey, ScoreRange scoreRange, Integer pnum, String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryContentByScoreRangeRows linekey:" + linekey);
        }
        ScoreRangeRows<Content> returnRows = new ScoreRangeRows<Content>();

        Set<String> followGameSet = null;


        if (isToday(scoreRange.getMax())) {
            followGameSet = askRedis.queryFollowGameListByPid(profileId);
        }

        //有游戏
        if (!CollectionUtil.isEmpty(followGameSet)) {

            //取今天的文章
            String todayLineKey = AskUtil.getContentLineKey(ContentLineOwn.TODAY_ALL_ARCHIVE, "");
            Set<String> stringSet = askRedis.zrangeContentLine(todayLineKey);

            //今天没文章，取所有文章
            if (CollectionUtil.isEmpty(stringSet)) {
                return getAll(scoreRange);
            }

            //关注的文章
            List<Content> followContentList = new ArrayList<Content>();

            //未关的文章
            List<Content> unfollowContentList = new ArrayList<Content>();

            //取出用户下游戏集合
            for (String value : stringSet) {
                Content content = getContent(Long.valueOf(value));
                if (content != null) {
                    List<String> contentGameId = Splitter.on(',').splitToList(content.getGameId());
                    boolean exist = false;
                    for (String game : contentGameId) {
                        if (followGameSet.contains(game)) {
                            if (!followContentList.contains(content)) {
                                followContentList.add(content);
                                exist = true;
                            }
                        }
                    }
                    if (!exist) {//未关注文章
                        unfollowContentList.add(content);
                    }
                }
            }

            //关注文章排序
            if (!CollectionUtil.isEmpty(followContentList)) {
                Collections.sort(followContentList, new Comparator<Content>() {
                    @Override
                    public int compare(Content a, Content b) {
                        return a.getPublishTime().getTime() < b.getPublishTime().getTime() ? 1 : (a == b ? 0 : -1);
                    }
                });
            }

            List<Content> todayAllContentList = new ArrayList<Content>();
            todayAllContentList.addAll(followContentList);
            todayAllContentList.addAll(unfollowContentList);

            int start = (pnum - 1) * scoreRange.getSize();
            int end = (pnum - 1) * scoreRange.getSize() + scoreRange.getSize();
            end = todayAllContentList.size() < end ? todayAllContentList.size() : end;

            List<Content> contents = new ArrayList<Content>();
            for (int i = start; i < end; i++) {
                contents.add(todayAllContentList.get(i));
            }

            //所有文章大小
            int sumSize = askRedis.getContentLineSize(AskUtil.getContentLineKey(ContentLineOwn.ALL_ARCHIVE, ""));

            scoreRange.setScoreflag(System.currentTimeMillis());
            scoreRange.setHasnext(sumSize > end ? true : false);

            returnRows = getYesterday(contents, scoreRange);
        } else {
            //直接取所有文章列表
            returnRows = getAll(scoreRange);
        }
        return returnRows;
    }

    @Override
    public PageRows<Advertise> queryAdvertise(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAdvertise queryExpress:" + queryExpress + ",page:" + page);
        }
        return askWriteAbleHandler.queryAdvertise(queryExpress, page);
    }

    private final String AD_QUARTZCRON_START_TIME = "_adQuartzCronStartTimeId_";
    private final String AD_QUARTZCRON_END_TIME = "_adQuartzCronEndTimeId_";

    @Override
    public Advertise postAdvertise(Advertise advertise) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " postAdvertise advertise:" + advertise);
        }
        try {
            //如果开始时间大于当前时间 则改为启用状态
            if (new Date().getTime() > advertise.getStartTime().getTime()) {
                advertise.setRemoveStatus(ValidStatus.VALID);
            }
            advertise = askWriteAbleHandler.insertAdvertise(advertise);
            if (!advertise.getRemoveStatus().equals(ValidStatus.VALID)) {
                buildAdQuartzCronTrigger(AD_QUARTZCRON_START_TIME, advertise.getStartTime(), advertise.getId(), ValidStatus.VALID.getCode());
            }

            askRedis.setAdvertise(advertise);
            askRedis.addAdvertiseLine(AskUtil.getAdvertiseLinekey(advertise.getAppkey(), advertise.getPlatform(), advertise.getDomain()), advertise);

            buildAdQuartzCronTrigger(AD_QUARTZCRON_END_TIME, advertise.getEndTime(), advertise.getId(), ValidStatus.REMOVED.getCode());
        } catch (SchedulerException e) {
            GAlerter.lab("adQuartzCronTrigger error", e);
        }
        return advertise;
    }

    public static void main(String agre[]) {
        FiveProps props = Props.instance().getServProps();
        AskConfig askConfig = new AskConfig(props);
        AskLogic askLogic = new AskLogic(askConfig);
//        askLogic.postAdvertise();
    }

    @Override
    public Advertise getAdvertise(Long id) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAdvertise advertiseid:" + id);
        }
        Advertise advertise = askRedis.getAdvertise(id);
        if (advertise == null) {
            advertise = askWriteAbleHandler.getAdvertise(new QueryExpress().add(QueryCriterions.eq(AdvertiseField.ID, id)));
            if (advertise != null) {
                askRedis.setAdvertise(advertise);
            }
        }
        return advertise;
    }

    @Override
    public boolean updateAdvertise(Long id, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " updateAdvertise id:" + id);
        }
        boolean bval = askWriteAbleHandler.updateAdvertise(updateExpress, new QueryExpress().add(QueryCriterions.eq(AdvertiseField.ID, id)));
        if (bval) {
            askRedis.delAdvertise(id);

            Advertise advertise = getAdvertise(id);

            if (advertise.getRemoveStatus().getCode().equals(ValidStatus.VALID.getCode())) {
                askRedis.addAdvertiseLine(AskUtil.getAdvertiseLinekey(advertise.getAppkey(), advertise.getPlatform(), advertise.getDomain()), advertise);
            } else {
                askRedis.removeAdvertiseLine(AskUtil.getAdvertiseLinekey(advertise.getAppkey(), advertise.getPlatform(), advertise.getDomain()), id);
            }
        }
        return bval;
    }

    @Override
    public void updateAdvertiseQuartzCronTrigger(Long id) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " updateAdvertiseQuartzCronTrigger id:" + id);
        }
        Advertise advertise = askWriteAbleHandler.getAdvertise(new QueryExpress().add(QueryCriterions.eq(AdvertiseField.ID, id)));
        ValidStatus status = null;
        if (new Date().getTime() > advertise.getStartTime().getTime()) {
            status = ValidStatus.VALID;
        } else if (new Date().getTime() < advertise.getStartTime().getTime()) {//当前时间小于开始时间
            status = ValidStatus.INVALID;
        }

        if (new Date().getTime() > advertise.getEndTime().getTime()) {
            status = ValidStatus.REMOVED;
        }

        if (!status.equals(advertise.getRemoveStatus())) {
            updateAdvertise(advertise.getId(), new UpdateExpress().set(AdvertiseField.REMOVE_STATUS, status.getCode()));
        }

        if (status.equals(ValidStatus.INVALID)) {
            adQuartzCronTrigger.modifyJobTime(AD_QUARTZCRON_START_TIME + id, DateUtil.getCron(advertise.getStartTime()), advertise.getId(), ValidStatus.VALID.getCode());
        }

        if (!status.equals(ValidStatus.REMOVED)) {
            adQuartzCronTrigger.modifyJobTime(AD_QUARTZCRON_END_TIME + id, DateUtil.getCron(advertise.getEndTime()), advertise.getId(), ValidStatus.REMOVED.getCode());
        }
    }

    @Override
    public List<Advertise> queryAdvertiseByLineKey(String linekey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAdvertiseByLineKey linekey:" + linekey);
        }
        Set<String> advertiseLine = askRedis.zrangeAdvertiseLine(linekey);

        List<Advertise> returnAdList = new ArrayList<Advertise>();
        for (String strid : advertiseLine) {
            Advertise advertise = getAdvertise(Long.valueOf(strid));
            if (advertise != null && advertise.getRemoveStatus().getCode().equals(ValidStatus.VALID.getCode())) {
                returnAdList.add(advertise);
            }
        }
        return returnAdList;
    }

    @Override
    public boolean addContentSum(Long contentId, ContentSumField contentSumField, String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " addContentSum contentId:" + contentId + ",profileId=" + profileId);
        }
        boolean bval = askRedis.checkContentSum(contentId, contentSumField, profileId);
        if (bval) {
            return false;
        }

        bval = askWriteAbleHandler.increaseContentSum(contentSumField, 1, contentId);
        if (bval) {
            bval = askRedis.increaseContentSum(contentId, contentSumField, 1, profileId);
        }
        return bval;
    }


    @Override
    public boolean checkContentSum(Long contentId, ContentSumField contentSumField, String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " checkContentSum contentId:" + contentId + ",profileId=" + profileId);
        }
        boolean bval = askRedis.checkContentSum(contentId, contentSumField, profileId);
        if (bval) {
            return true;
        }
        return false;
    }

    @Override
    public Map<Long, ContentSum> queryContentSumByids(Collection<Long> contentIds) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryContentSumByids contentIds:" + contentIds);
        }
        Map<Long, ContentSum> returnMap = new LinkedHashMap<Long, ContentSum>();
        for (Long contengId : contentIds) {
            ContentSum contentSum = askRedis.getContentSumById(contengId);
            if (contentSum == null) {
                contentSum = askWriteAbleHandler.getContentSum(new QueryExpress().add(QueryCriterions.eq(ContentSumField.ID, contengId)));
                if (contentSum != null) {
                    askRedis.setContentSumById(contengId, contentSum);
                }
            }

            if (contentSum != null) {
                returnMap.put(contengId, contentSum);
            }
        }
        return returnMap;
    }

    @Override
    public Map<Long, Content> queryContentByids(Collection<Long> contentIds) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryContentByids contentIds:" + contentIds);
        }

        Map<Long, Content> returnMap = new LinkedHashMap<Long, Content>();
        for (Long contengId : contentIds) {
            Content content = getContent(contengId);
            if (content != null) {
                returnMap.put(contengId, content);
            }
        }
        return returnMap;
    }

    @Override
    public Map<Long, Content> queryContentByUserCollect(Set<Long> contentIds) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryContentByids contentIds:" + contentIds);
        }
        Map<Long, Content> map = new HashMap<Long, Content>();
        List<Content> list = askWriteAbleHandler.queryContent(new QueryExpress().add(QueryCriterions.in(ContentField.ID, contentIds.toArray())));
        if (!CollectionUtil.isEmpty(list)) {
            for (Content content : list) {
                map.put(content.getId(), content);
            }
        }
        return map;
    }

    private void processUpdateContent(Content oldContent, Content content) throws DbException {
        content.setId(oldContent.getId());
        askRedis.setContent(content);

        UpdateExpress up = new UpdateExpress();
        up.set(ContentField.TITLE, content.getTitle());
        up.set(ContentField.DESCRIPTION, content.getDescription());
        up.set(ContentField.PIC, content.getPic());
        up.set(ContentField.AUTHOR, content.getAuthor());
        up.set(ContentField.GAME_ID, content.getGameId());
        up.set(ContentField.PUBLISH_TIME, content.getPublishTime());
        askWriteAbleHandler.updateContent(up, new QueryExpress().add(QueryCriterions.eq(ContentField.ID, oldContent.getId())));

        //solr
        addSearchEvent(content);


        //修改了发布时间
        if (content.getPublishTime().getTime() != oldContent.getPublishTime().getTime()) {
            double score = content.getPublishTime().getTime() + Double.parseDouble("0." + String.valueOf(oldContent.getId()));
            //进入所有文章
            String allLineKey = AskUtil.getContentLineKey(ContentLineOwn.ALL_ARCHIVE, "");
            QueryExpress allQuery = new QueryExpress();
            allQuery.add(QueryCriterions.eq(ContentLineField.LINE_KEY, allLineKey));
            allQuery.add(QueryCriterions.eq(ContentLineField.DEST_ID, oldContent.getId()));
            askWriteAbleHandler.updateContentLine(new UpdateExpress().set(ContentLineField.SCORE, score), allQuery);
            askRedis.addContentLine(allLineKey, score, oldContent.getId());


            //进入游戏下文章
            String gameLineKey = AskUtil.getContentLineKey(ContentLineOwn.GAME_ALL_ARCHIVE, content.getGameId());
            QueryExpress gameLineQuery = new QueryExpress();
            gameLineQuery.add(QueryCriterions.eq(ContentLineField.LINE_KEY, gameLineKey));
            gameLineQuery.add(QueryCriterions.eq(ContentLineField.DEST_ID, oldContent.getId()));
            askWriteAbleHandler.updateContentLine(new UpdateExpress().set(ContentLineField.SCORE, score), gameLineQuery);
            askRedis.addContentLine(gameLineKey, score, oldContent.getId());

            //进入今天的文章
//            String todayLineKey = AskUtil.getContentLineKey(ContentLineOwn.TODAY_ALL_ARCHIVE, "");
//            QueryExpress todayLineQuery = new QueryExpress();
//            todayLineQuery.add(QueryCriterions.eq(ContentLineField.LINE_KEY, todayLineKey));
//            todayLineQuery.add(QueryCriterions.eq(ContentLineField.DEST_ID, oldContent.getId()));
//            askWriteAbleHandler.updateContentLine(new UpdateExpress().set(ContentLineField.SCORE, score), todayLineQuery);
//            askRedis.addContentLine(todayLineKey, score, oldContent.getId());

            //修改文章对应的
            String todayLineKey = com.enjoyf.util.MD5Util.Md5(ContentLineOwn.TODAY_ALL_ARCHIVE.getCode() +
                    DateUtil.formatDateToString(content.getPublishTime(), DateUtil.PATTERN_DATE_SHORT) + ContentLineType.CONTENTLINE_ARCHIVE.getCode());

            String oldLineKey = com.enjoyf.util.MD5Util.Md5(ContentLineOwn.TODAY_ALL_ARCHIVE.getCode() +
                    DateUtil.formatDateToString(oldContent.getPublishTime(), DateUtil.PATTERN_DATE_SHORT) + ContentLineType.CONTENTLINE_ARCHIVE.getCode());

            askRedis.addContentLine(todayLineKey, score, oldContent.getId());

            if (!todayLineKey.equals(oldLineKey)) {
                askRedis.removeContentLine(oldLineKey, oldContent.getId());
            }

        }
    }

    private void addSearchEvent(Content content) {
        WikiappSearchEvent searchEvent = new WikiappSearchEvent();

        WikiappSearchEntry entry = new WikiappSearchEntry();
        entry.setEntryid(AskUtil.getWikiappSearchEntryId(String.valueOf(content.getId()), WikiappSearchType.ARCHIVE));
        entry.setId(String.valueOf(content.getId()));
        entry.setType(WikiappSearchType.ARCHIVE.getCode());
        entry.setTitle(content.getTitle());
        entry.setCreatetime(content.getPublishTime().getTime());

        searchEvent.setWikiappSearchEntry(entry);
        eventProcessQueueThreadN.add(searchEvent);
    }

    private void delSearchEvent(Content content) {
        WikiappSearchDeleteEvent deleteEvent = new WikiappSearchDeleteEvent();

        WikiappSearchDeleteEntry entry = new WikiappSearchDeleteEntry();
        entry.setId(String.valueOf(content.getId()));
        entry.setType(WikiappSearchType.ARCHIVE);

        deleteEvent.setWikiappSearchDeleteEntry(entry);
        eventProcessQueueThreadN.add(deleteEvent);
    }

    private void processInsertContent(Content content) throws DbException {
        //进入所有文章
        String allLineKey = AskUtil.getContentLineKey(ContentLineOwn.ALL_ARCHIVE, "");
        ContentLine contentLine = new ContentLine();
        contentLine.setLinekey(allLineKey);
        contentLine.setOwnId(ContentLineOwn.ALL_ARCHIVE);
        contentLine.setDestId(content.getId());
        contentLine.setScore(content.getPublishTime().getTime() + Double.parseDouble("0." + String.valueOf(content.getId())));
        contentLine.setCreateTime(new Date());
        contentLine = askWriteAbleHandler.insertContentLine(contentLine);
        askRedis.addContentLine(allLineKey, contentLine.getScore(), content.getId());

        //进入游戏下文章
        String gameLineKey = AskUtil.getContentLineKey(ContentLineOwn.GAME_ALL_ARCHIVE, content.getGameId());
        ContentLine gameLine = new ContentLine();
        gameLine.setLinekey(gameLineKey);
        gameLine.setOwnId(ContentLineOwn.ALL_ARCHIVE);
        gameLine.setDestId(content.getId());
        gameLine.setScore(content.getPublishTime().getTime() + Double.parseDouble("0." + String.valueOf(content.getId())));
        gameLine.setCreateTime(new Date());
        askWriteAbleHandler.insertContentLine(gameLine);
        askRedis.addContentLine(gameLineKey, contentLine.getScore(), content.getId());

        //进入今天的文章
//        String todayLineKey = AskUtil.getContentLineKey(ContentLineOwn.TODAY_ALL_ARCHIVE, "");
//        ContentLine todayLine = new ContentLine();
//        todayLine.setLinekey(todayLineKey);
//        todayLine.setOwnId(ContentLineOwn.ALL_ARCHIVE);
//        todayLine.setDestId(content.getId());
//        todayLine.setScore(content.getPublishTime().getTime() + Double.parseDouble("0." + String.valueOf(content.getId())));
//        todayLine.setCreateTime(new Date());
//        askWriteAbleHandler.insertContentLine(todayLine);

        String todayLineKey = com.enjoyf.util.MD5Util.Md5(ContentLineOwn.TODAY_ALL_ARCHIVE.getCode() +
                DateUtil.formatDateToString(content.getPublishTime(), DateUtil.PATTERN_DATE_SHORT) + ContentLineType.CONTENTLINE_ARCHIVE.getCode());
        askRedis.addContentLine(todayLineKey, contentLine.getScore(), content.getId());
    }

    private ScoreRangeRows<Content> getYesterday(List<Content> contentList, ScoreRange scoreRange) throws ServiceException {
        ScoreRangeRows<Content> returnRows = new ScoreRangeRows<Content>();
        returnRows.setRange(scoreRange);
        returnRows.setRows(contentList);

        //如果数量不足，需要不足剩余部分
        if (contentList.size() < scoreRange.getSize()) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
            scoreRange.setMax(DateUtil.dayLastTime(cal.getTime()).getTime());
            scoreRange.setMin(-1);
            String linekey = AskUtil.getContentLineKey(ContentLineOwn.ALL_ARCHIVE, "");
            Set<String> stringSet = askRedis.queryContentByLineSortRange(linekey, scoreRange);
            for (String value : stringSet) {
                Content content = getContent(Long.valueOf(value));
                if (content != null) {
                    contentList.add(content);
                }
            }
            returnRows.setRange(scoreRange);
            returnRows.setRows(contentList);
        }
        return returnRows;
    }

    //获取所有文章
    private ScoreRangeRows<Content> getAll(ScoreRange scoreRange) throws ServiceException {
        ScoreRangeRows<Content> returnRows = new ScoreRangeRows<Content>();
        List<Content> contentList = new ArrayList<Content>();
        String allLineKey = AskUtil.getContentLineKey(ContentLineOwn.ALL_ARCHIVE, "");
        Set<String> stringSet = askRedis.queryContentByLineSortRange(allLineKey, scoreRange);
        for (String value : stringSet) {
            Content content = getContent(Long.valueOf(value));
            if (content != null) {
                contentList.add(content);
            }
        }
        returnRows.setRange(scoreRange);
        returnRows.setRows(contentList);
        return returnRows;
    }

    private Content getContent(Long contentId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getContent contentId:" + contentId);
        }
        Content content = askRedis.getContent(contentId);
        if (content == null) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ContentField.ID, contentId));
            queryExpress.add(QueryCriterions.eq(ContentField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            content = askWriteAbleHandler.getContent(queryExpress);
            if (content != null) {
                askRedis.setContent(content);
            }
        }
        return content;
    }

    private boolean isToday(double timeStamp) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
        long yesterdayLastTime = DateUtil.dayLastTime(cal.getTime()).getTime();
        if (timeStamp < yesterdayLastTime) {
            return false;
        }
        return true;
    }

    public WikiGameres insertWikiGameres(WikiGameres wikiGameres) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " wikiGameres wikiGameres:" + wikiGameres.toString());
        }
        return askWriteAbleHandler.createWikiGameres(wikiGameres);
    }

    @Override
    public WikiGameres getWikiGameresByQueryExpress(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getWikiGameresByQueryExpress queryExpress:" + queryExpress);
        }
        return askWriteAbleHandler.getWikiGameRes(queryExpress);
    }

    @Override
    public WikiGameres getWikiGameresByGameId(long gameId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getWikiGameresByGameId gameId:" + gameId);
        }

        WikiGameres wikiGameres = askRedis.getWikiGameres(gameId);
        if (wikiGameres == null) {
            wikiGameres = askWriteAbleHandler.getWikiGameRes(new QueryExpress().add(QueryCriterions.eq(WikiGameresField.GAMEID, gameId)));
            if (wikiGameres != null) {
                askRedis.setWikiGameInfo(wikiGameres);
            }
        }
        return wikiGameres;
    }

    @Override
    public PageRows<WikiGameres> queryGamesByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryGamesByPage queryExpress:" + queryExpress + " pagination=" + pagination);
        }
        return askWriteAbleHandler.queryWikiGameresByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyWikiGameres(Long id, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyWikiGameres id:" + id + " updateExpress=" + updateExpress);
        }


        boolean bool = askWriteAbleHandler.modifyWikiGameres(updateExpress, new QueryExpress().add(QueryCriterions.eq(WikiGameresField.ID, id)));
        if (bool) {
            WikiGameres wikiGameres = getWikiGameresByQueryExpress(new QueryExpress().add(QueryCriterions.eq(WikiGameresField.ID, id)));
            askRedis.delWikiGameres(wikiGameres.getGameId());
        }
        return bool;
    }

    @Override
    public boolean modifyRecommend(Long id, boolean flag, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + "modifyWikiGameres id:{0} updateExpress={1}", id, flag);
        }
        boolean bool = modifyWikiGameres(id, updateExpress);

        if (bool) {
            WikiGameres wikiGameres = getWikiGameresByQueryExpress(new QueryExpress().add(QueryCriterions.eq(WikiGameresField.ID, id)));
            if (flag) {
                askRedis.addRecommendGame(wikiGameres);
            } else {
                askRedis.remRecommendGame(wikiGameres);
            }
        }

        return bool;
    }

    @Override
    public Map<Long, WikiGameres> queryRecommendGame() throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("{} queryRecommendGame", this.getClass().getName());
        }
        Set<Long> result = new LinkedHashSet<Long>();
        Set<String> ids = askRedis.queryRecommentGames();
        if (CollectionUtil.isEmpty(ids)) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(WikiGameresField.RECOMMEND, 1));
            queryExpress.add(QueryCriterions.eq(WikiGameresField.VALIDSTATUS, ValidStatus.VALID.getCode()));
            queryExpress.add(QuerySort.add(WikiGameresField.DISPLAYORDER, QuerySortOrder.DESC));
            List<WikiGameres> list = askWriteAbleHandler.queryWikiGameres(queryExpress);
            if (!CollectionUtil.isEmpty(list)) {
                for (WikiGameres wikiGameres : list) {
                    askRedis.addRecommendGame(wikiGameres);
                    result.add(wikiGameres.getGameId());
                }
            }
        } else {
            for (String idStr : ids) {
                result.add(Long.parseLong(idStr));
            }
        }
        return queryWikiGameresByIds(result);
    }


    @Override
    public List<WikiGameres> queryAllWikiGameres() throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("{} queryAllWikiGameres", this.getClass().getName());
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(WikiGameresField.VALIDSTATUS, ValidStatus.VALID.getCode()));
        queryExpress.add(QuerySort.add(WikiGameresField.RECOMMEND, QuerySortOrder.DESC));
        queryExpress.add(QuerySort.add(WikiGameresField.DISPLAYORDER, QuerySortOrder.ASC));
        List<WikiGameres> list = askWriteAbleHandler.queryWikiGameres(queryExpress);
        if (!CollectionUtil.isEmpty(list)) {
            Set<Long> gameIds = new HashSet<Long>();
            for (WikiGameres wikiGameres : list) {
                gameIds.add(wikiGameres.getGameId());
            }
            Map<Long, Integer> gamefollowSumMap = askRedis.queryGameFollowSum(gameIds);
            if (gamefollowSumMap != null && !gamefollowSumMap.isEmpty()) {
                for (WikiGameres wikiGameres : list) {
                    if (gamefollowSumMap.get(wikiGameres.getGameId()) != null) {
                        wikiGameres.setFocusSum(gamefollowSumMap.get(wikiGameres.getGameId()));
                    }
                }
            }
        }
        return list;
    }

    @Override
    public Map<Long, WikiGameres> queryWikiGameresByIds(Set<Long> gameIds) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("{} queryWikiGameresByIds gameIds={}", this.getClass().getName(), gameIds);
        }
        Map<Long, WikiGameres> map = new HashMap<Long, WikiGameres>();
        if (CollectionUtil.isEmpty(gameIds)) {
            return map;
        }
        Set<Long> queryIds = new HashSet<Long>();
        for (Long id : gameIds) {
            WikiGameres gamedb = askRedis.getWikiGameres(id);
            if (gamedb == null) {
                queryIds.add(id);
            } else {
                map.put(id, gamedb);
            }
        }
        if (!CollectionUtil.isEmpty(queryIds)) {
            List<WikiGameres> wikiGameresList = askWriteAbleHandler.queryWikiGameres(new QueryExpress()
                    .add(QueryCriterions.eq(WikiGameresField.VALIDSTATUS, ValidStatus.VALID.getCode()))
                    .add(QueryCriterions.in(WikiGameresField.GAMEID, queryIds.toArray())));
            if (!CollectionUtil.isEmpty(wikiGameresList)) {
                for (WikiGameres game : wikiGameresList) {
                    askRedis.setWikiGameInfo(game);
                    map.put(game.getGameId(), game);
                }
            }
        }
        Map<Long, Integer> gameFollowSumMap = askRedis.queryGameFollowSum(gameIds);//查询游戏的关注数
        //按传进来的ID顺序排序
        Map<Long, WikiGameres> returnMap = new LinkedHashMap<Long, WikiGameres>();
        for (Long id : gameIds) {
            WikiGameres wikiGameres = map.get(id);
            if (wikiGameres != null) {
                Integer sum = gameFollowSumMap.get(id);
                if (sum != null) {
                    wikiGameres.setFocusSum(sum);
                }
                returnMap.put(id, wikiGameres);
            }
        }

        return returnMap;
    }

    @Override
    public void batchFollow(String[] gameIds, String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("askLogic batchFollow gameIds={},profileid={}", gameIds, profileId);
        }
        int i = 0;
        for (String id : gameIds) {
            boolean exisBoolean = askRedis.querFollowIsExis(profileId, id);
            if (exisBoolean) {
                continue;
            }
            UserFollowGame userFollowGame = new UserFollowGame();
            userFollowGame.setValidStatus(ValidStatus.VALID);
            userFollowGame.setCreateTime(new Date());
            userFollowGame.setGameId(Long.parseLong(id));
            userFollowGame.setProfileId(profileId);
            try {
                boolean bool = askWriteAbleHandler.modifyUserFollowGame(new UpdateExpress().set(UserFollowGameField.VALID_STATUS, ValidStatus.VALID.getCode())
                        .set(UserFollowGameField.MODIFYTIME, new Date()), new QueryExpress()
                        .add(QueryCriterions.eq(UserFollowGameField.PROFILEID, profileId))
                        .add(QueryCriterions.eq(UserFollowGameField.GAMEID, Long.parseLong(id))));
                if (!bool) {
                    userFollowGame = askWriteAbleHandler.createUserFollowGame(userFollowGame);
                    if (userFollowGame.getId() > 0) {
                        askRedis.addFollowGameIdByPid(profileId, id, System.currentTimeMillis() + i); //放入缓存
                    }
                } else {
                    askRedis.addFollowGameIdByPid(profileId, id, System.currentTimeMillis() + i);
                }
            } catch (DbException e) {
                GAlerter.lab("DbException e", e);
            } catch (NumberFormatException e) {
                GAlerter.lab("NumberFormatException e", e);
            }
            i++;
        }
    }

    @Override
    public boolean unFollow(String[] gameIds, String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("askLogic unFollow gameIds={},profileid={}", gameIds, profileId);
        }
        Long[] idsLong = new Long[gameIds.length];
        for (int i = 0; i < gameIds.length; i++) {
            idsLong[i] = Long.parseLong(gameIds[i]);
        }

        boolean bool = askWriteAbleHandler.modifyUserFollowGame(new UpdateExpress().set(UserFollowGameField.VALID_STATUS, ValidStatus.REMOVED.getCode())
                .set(UserFollowGameField.MODIFYTIME, new Date()), new QueryExpress()
                .add(QueryCriterions.eq(UserFollowGameField.PROFILEID, profileId))
                .add(QueryCriterions.in(UserFollowGameField.GAMEID, idsLong)));
        if (bool) {
            for (String gameId : gameIds) {
                askRedis.removeFollowGameId(profileId, gameId);
            }
        }
        return bool;
    }

    @Override
    public PageRows<WikiGameres> queryFollowGameList(String pid, Pagination pagination, boolean isDesc) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("askLogic queryFollowGameList profileid={}", pid);
        }

        Set<String> gameIds = askRedis.queryFollowGameList(pid, pagination, isDesc);
        if (CollectionUtil.isEmpty(gameIds)) {
            return null;
        }
        PageRows<WikiGameres> pageRows = new PageRows<WikiGameres>();
        int sum = (int) askRedis.getUserFollowNum(pid); //获得用户关注总数
        pagination.setTotalRows(sum);
        pageRows.setPage(pagination);

        Set<Long> gameIdsLong = new LinkedHashSet<Long>();
        for (String gameId : gameIds) {
            gameIdsLong.add(Long.parseLong(gameId));
        }
        Map<Long, WikiGameres> map = queryWikiGameresByIds(gameIdsLong);//查询游戏表信息
        List<WikiGameres> list = new ArrayList<WikiGameres>();
        for (long gameId : gameIdsLong) {
            if (map.get(gameId) != null) {
                list.add(map.get(gameId));
            }
        }
        pageRows.setRows(list);
        return pageRows;
    }

    @Override
    public UserCollect insertUserCollect(UserCollect userCollect) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("askLogic insertUserCollect userCollect={}", userCollect.toString());
        }

        UserCollect collect = askWriteAbleHandler.getUserCollect(new QueryExpress().add(QueryCriterions.eq(UserCollectField.PROFILEID, userCollect.getProfileId()))
                .add(QueryCriterions.eq(UserCollectField.CONTENT_ID, userCollect.getContentId())));
        if (collect != null) {
            throw AskServiceException.USER_COLLECT_EXIST;
        }

        userCollect = askWriteAbleHandler.createUserCollect(userCollect);
        if (userCollect.getId() > 0) {
            askRedis.addUserCollectList(userCollect);
        }
        return userCollect;
    }

    @Override
    public boolean deleteUserCollect(Set<Long> ids, String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("askLogic deleteUserCollect ids={}", ids.toString());
        }
        boolean bool = false;
        if (!CollectionUtil.isEmpty(ids)) {
            bool = askWriteAbleHandler.deleteUserCollect(new QueryExpress().add(QueryCriterions.in(UserCollectField.ID, ids.toArray())));
        }
        if (bool) {
            for (Long id : ids) {
                //把成功从缓存清除的数据放入List 在数据库进行物理删除
                if (askRedis.removeUserCollectList(profileId, String.valueOf(id))) {
                    askRedis.delUserCollect(id);
                }
            }
        }

        return bool;
    }

    @Override
    public PageRows<UserCollect> queryCollectByCache(String pid, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("askLogic queryCollectByCache pid={}", pid);
        }
        Set<String> ids = askRedis.queryUserCollectList(pid, pagination, true);
        if (CollectionUtil.isEmpty(ids)) {
            return null;
        }
        PageRows<UserCollect> pageRows = new PageRows<UserCollect>();
        int sum = (int) askRedis.getUserCollectNum(pid); //获得总数
        pagination.setTotalRows(sum);
        pageRows.setPage(pagination);

        Set<Long> idsLong = new LinkedHashSet<Long>();
        for (String id : ids) {
            idsLong.add(Long.parseLong(id));
        }
        Map<Long, UserCollect> map = queryUserCollect(idsLong);//查询游戏表信息
        List<UserCollect> list = new ArrayList<UserCollect>();
        for (long id : idsLong) {
            if (map.get(id) != null) {
                list.add(map.get(id));
            }
        }
        pageRows.setRows(list);
        return pageRows;
    }

    @Override
    public UserCollect getCollect(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("askLogic getCollect queryExpress={}", queryExpress);
        }

        UserCollect collect = askWriteAbleHandler.getUserCollect(queryExpress);
        return collect;
    }

    private Map<Long, UserCollect> queryUserCollect(Set<Long> ids) throws ServiceException {
        Map<Long, UserCollect> map = new HashMap<Long, UserCollect>();

        Set<Long> queryIds = new HashSet<Long>();
        for (Long id : ids) {
            UserCollect userCollect = askRedis.getUserCollect(id);
            if (userCollect == null) {
                queryIds.add(id);
            } else {
                map.put(id, userCollect);
            }
        }
        if (!CollectionUtil.isEmpty(queryIds)) {
            List<UserCollect> userCollectList = askWriteAbleHandler.queryUserCollect(new QueryExpress()
                    .add(QueryCriterions.in(WikiGameresField.ID, queryIds.toArray())));
            if (!CollectionUtil.isEmpty(userCollectList)) {
                for (UserCollect userCollect : userCollectList) {
                    askRedis.setUserCollect(userCollect);
                    map.put(userCollect.getId(), userCollect);
                }
            }
        }

        return map;
    }


    @Override
    public PageRows<ContentTag> queryContentTag(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("askLogic queryContentTag:", queryExpress, page);
        }
        return askWriteAbleHandler.queryContentTag(queryExpress, page);
    }

    @Override
    public ContentTag postContentTag(ContentTag contentTag) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("askLogic postContentTag,contentTag:", contentTag);
        }

        contentTag = askWriteAbleHandler.insertContentTag(contentTag);

        askRedis.setContentTag(contentTag);

        askRedis.addContentTag(contentTag.getTagLine(), contentTag);

        return contentTag;
    }

    @Override
    public ContentTag getContentTag(Long id) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("askLogic getContentTag,id:", id);
        }
        ContentTag contentTag = askRedis.getContentTag(id);
        if (contentTag == null) {
            contentTag = askWriteAbleHandler.getContentTag(new QueryExpress().add(QueryCriterions.eq(ContentTagField.ID, id)));
            if (contentTag != null) {
                askRedis.setContentTag(contentTag);
            }
        }
        return contentTag;
    }

    @Override
    public boolean updateContentTag(Long id, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " updateContentTag id:" + id);
        }
        boolean bval = askWriteAbleHandler.updateContentTag(updateExpress, new QueryExpress().add(QueryCriterions.eq(ContentTagField.ID, id)));
        if (bval) {
            askRedis.delContentTag(id);

            ContentTag contentTag = getContentTag(id);

            if (contentTag.getValidStatus().getCode().equals(ValidStatus.VALID.getCode())) {
                askRedis.addContentTag(contentTag.getTagLine(), contentTag);
            } else {
                askRedis.removeContentTag(contentTag.getTagLine(), contentTag.getId());
            }
        }
        return bval;
    }

    @Override
    public List<ContentTag> queryContentTagByTagType(ContentTagLine tagLine) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryContentTagByTagType tagLine:" + tagLine);
        }
        Set<String> contentTag = askRedis.zrangeContentTag(tagLine);
        List<ContentTag> returnList = new ArrayList<ContentTag>();
        for (String strid : contentTag) {
            ContentTag tag = getContentTag(Long.valueOf(strid));
            if (tag != null && tag.getValidStatus().getCode().equals(ValidStatus.VALID.getCode())) {
                returnList.add(tag);
            }
        }
        return returnList;
    }

    @Override
    public List<UserFollowGame> queryFollowGame(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + "queryFollowGame queryExpress:" + queryExpress);
        }
        return askWriteAbleHandler.queryUserFollowGames(queryExpress);
    }

    @Override
    public BlackListHistory getBlackListHistory(String pid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getBlackListHistory pid:" + pid);
        }

        BlackListHistory blackListHistory = askRedis.getBlackListHistory(pid);
        return blackListHistory;
    }

    @Override
    public Map<String, BlackListHistory> queryBlackHisotryById(Set<String> pids) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryBlackHisotryById pids:" + pids);
        }
        Map<String, BlackListHistory> returnMap = new HashMap<String, BlackListHistory>();
        BlackListHistory blackListHistory;
        for (String pid : pids) {
            blackListHistory = getBlackListHistory(pid);
            if (blackListHistory != null) {
                returnMap.put(pid, blackListHistory);
            }
        }
        return returnMap;
    }

    @Override
    public BlackListHistory addBlackListHisotry(BlackListHistory blackListHistory) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " blackListHistory blackListHistory:" + blackListHistory);
        }

        BlackListHistory history = askRedis.getBlackListHistory(blackListHistory.getProfileId());
        if (history != null) {
            return null;
        }

        blackListHistory = askWriteAbleHandler.insertBlackListHistory(blackListHistory);
        if (blackListHistory.getId() > 0) {
            askRedis.setBlackListHisotry(blackListHistory);
        }

        return blackListHistory;
    }

    @Override
    public boolean removeBlackListByPid(String pid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " removeBlackListByPid pid:" + pid);
        }
        BlackListHistory history = askRedis.getBlackListHistory(pid);
        if (history == null) {
            return true;
        }
        boolean bool = askWriteAbleHandler.updateBlackListHistory(new UpdateExpress().set(BlackListHistoryField.ENDTIME, new Date())
                , new QueryExpress().add(QueryCriterions.eq(BlackListHistoryField.ID, history.getId())));
        if (bool) {
            askRedis.delBlackListHistory(pid);
        }
        return bool;
    }

    @Override
    public PageRows<BlackListHistory> queryBlackListHistoryList(String pid, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryBlackListHistoryList pid:" + pid);
        }
        return askWriteAbleHandler.queryBlackListHistory(new QueryExpress()
                .add(QueryCriterions.eq(BlackListHistoryField.PROFILEID, pid))
                .add(QuerySort.add(BlackListHistoryField.CREATE_TIME, QuerySortOrder.DESC)), pagination);
    }

    @Override
    public Set<String> queryBlackListPid() throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryBlackListPid");
        }
        Set<String> profileIds = new HashSet<String>();
        profileIds.addAll(askRedis.queryBlackListPid());
        return profileIds;
    }

    //热度排行更新
    public void viewsumHotrank() {
        //当前时间
        Date curTime = Calendar.getInstance().getTime();

        //25小时前
        Calendar calendar25 = Calendar.getInstance();
        calendar25.set(Calendar.HOUR_OF_DAY, calendar25.get(Calendar.HOUR_OF_DAY) - 25);

        //24小时之前
        Calendar calendar24 = Calendar.getInstance();
        calendar24.set(Calendar.HOUR_OF_DAY, calendar24.get(Calendar.HOUR_OF_DAY) - 24);

        QueryExpress queryExpress = new QueryExpress();
        try {
            queryExpress.add(QueryCriterions.geq(QuestionSumField.QUESTIONCREATETIME, calendar25.getTime()));
            queryExpress.add(QueryCriterions.leq(QuestionSumField.QUESTIONCREATETIME, curTime));
            List<QuestionSum> questionSums = askWriteAbleHandler.queryQuestionSum(queryExpress);
            for (QuestionSum qsum : questionSums) {
                Question question = getQuestion(qsum.getQuestionId());

                //有可能是问题被删除
                if (question == null) {

                    //游戏标签line_key
                    String gameIdString = String.valueOf(question.getGameId());
                    String gameRecommendKey = AskUtil.getAskLineKey(gameIdString, WanbaItemDomain.RECOMMEND);
                    askRedis.removeItem(gameRecommendKey, String.valueOf(question.getQuestionId()), ItemType.QUESTION);

                    continue;
                }

                //如果答案未被采纳
                if (question.getAcceptAnswerId() <= 0) {
                    continue;
                }

                //对游戏列表进行处理
                if (question.getType().equals(QuestionType.TIMELIMIT)) {
                    String gameIdString = String.valueOf(question.getGameId());
                    String gameRecommendKey = AskUtil.getAskLineKey(gameIdString, WanbaItemDomain.RECOMMEND);

                    //如果答案被删除
                    Answer answer = getAnswer(question.getAcceptAnswerId());
                    if (answer == null) {
                        askRedis.removeItem(gameRecommendKey, String.valueOf(question.getQuestionId()), ItemType.QUESTION);
                        continue;
                    }

//                    QueryExpress tagquery = new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, question.getGameId()));
//                    AnimeTag animetag = JoymeAppServiceSngl.get().getAnimeTag(question.getGameId(), tagquery);
//                    if (animetag == null) {
//                        continue;
//                    }
//                    //有可能是活动标签，活动标签是按顺序排列
//                    if (!animetag.getApp_type().equals(AnimeTagAppType.WANBA_ASK)) {
//                        continue;
//                    }


//                    WanbaItem gameRecommendItem = new WanbaItem();
//                    gameRecommendItem.setOwnProfileId(gameIdString);
//                    gameRecommendItem.setItemType(ItemType.QUESTION);
//                    gameRecommendItem.setCreateTime(question.getCreateTime());
//                    gameRecommendItem.setItemDomain(WanbaItemDomain.RECOMMEND);
//                    gameRecommendItem.setDestId(String.valueOf(question.getQuestionId()));
//                    gameRecommendItem.setDestProfileId(question.getAskProfileId());
//                    gameRecommendItem.setLineKey(gameRecommendKey);
//                    double score = qsum.getViewSum();
//                    score += Double.parseDouble("0." + String.valueOf(question.getQuestionId()));
//                    gameRecommendItem.setScore(score);
//
//                    //如果是24小时前，移除
//                    if (qsum.getQuestionCreateTime().before(calendar24.getTime())) {
//                        askRedis.removeItem(gameRecommendKey, String.valueOf(question.getQuestionId()), ItemType.QUESTION);
//                    } else {
//                        askRedis.addLine(gameRecommendItem);
//                    }
                } else {
                    //问他列表
                    String inviteQuestionLineKey = AskUtil.getAskLineKey(AskUtil.KEY_ALLGAME, WanbaItemDomain.INVITE_QUESTIONLIST);
                    WanbaItem myAskItem = new WanbaItem();
                    myAskItem.setOwnProfileId(AskUtil.KEY_ALLGAME);
                    myAskItem.setItemType(ItemType.QUESTION);
                    myAskItem.setCreateTime(question.getCreateTime());
                    myAskItem.setItemDomain(WanbaItemDomain.INVITE_QUESTIONLIST);
                    myAskItem.setDestId(String.valueOf(question.getQuestionId()));
                    myAskItem.setDestProfileId(question.getAskProfileId());
                    myAskItem.setLineKey(inviteQuestionLineKey);
                    double score = qsum.getViewSum();
                    score += Double.parseDouble("0." + String.valueOf(question.getQuestionId()));
                    myAskItem.setScore(score);
                    //如果是24小时前，移除
                    if (qsum.getQuestionCreateTime().before(calendar24.getTime())) {
                        askRedis.removeItem(inviteQuestionLineKey, String.valueOf(question.getQuestionId()), ItemType.QUESTION);
                    } else {
                        askRedis.addLine(myAskItem);
                    }
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " viewsumHotrank  error,e: ", e);
        }
    }

    public boolean updateQuestionSum(UpdateExpress updateExpress, long qid) throws ServiceException {
        return askWriteAbleHandler.updateQuestionSum(updateExpress, qid);
    }

    public void verifyNotice() {

        if (!askRedis.getVerifyNoticeTrigger()) {
            askRedis.setVerifyNoticeTrigger(true);
            // GAlerter.lan("===============verifyNotice==============");
            try {
                String KEY_LIST_VERIFY_PROFILE_TAGS = "usercenterservice_verifyprofiletags_";
                String KEY_LIST_VERIFY_PROFILES = "usercenterservice_verifyprofiles_";

                //TODO
                //http://xuewb.com/lua/if_else.html
                //大于5分钟的
                double fiveMin = Double.valueOf(System.currentTimeMillis() + 5 * 60 * 1000);

                //人的集合
                Set<String> pidSet = askRedis.smembers(KEY_LIST_VERIFY_PROFILES);

                if (CollectionUtil.isEmpty(pidSet)) {
                    askRedis.setVerifyNoticeTrigger(false);
                    return;
                }

                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.WANBA_ASK.getCode()));
                queryExpress.add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode()));

                //游戏标签
                List<AnimeTag> taglist = JoymeAppServiceSngl.get().queryAnimeTag(queryExpress);

                if (CollectionUtil.isEmpty(taglist)) {
                    askRedis.setVerifyNoticeTrigger(false);
                    return;
                }

                //遍历游戏标签
                outer:
                for (AnimeTag animeTag : taglist) {
                    if (animeTag.getTag_id() < 0) {
                        continue;
                    }

                    //
                    if (animeTag.getPicjson() == null || !animeTag.getPicjson().getType().equals(GameClientTagType.DEFAULT.getCode())) {
                        continue;
                    }

                    //如果遍历到发过一遍
                    if (CollectionUtil.isEmpty(pidSet)) {
                        break outer;
                    }

                    String gameIdString = String.valueOf(animeTag.getTag_id());
                    String gameTimeLineKey = AskUtil.getAskLineKey(gameIdString, WanbaItemDomain.TIMELIMIT_ING);
                    Set<String> qidSet = askRedis.zrangeByScore(gameTimeLineKey, fiveMin, Double.MAX_VALUE, "asc");
                    //如果此标签下面没有问题，直接跳珠while循环
                    if (CollectionUtil.isEmpty(qidSet)) {
                        continue;
                    }

                    //取第一个元素即可
                    String qidValue = qidSet.iterator().next();
                    String[] valueArray = qidValue.split("_");
                    if (valueArray.length != 2) {
                        continue;
                    }

                    Question question = getQuestion(Long.valueOf(valueArray[0]));
                    if (question == null) {
                        continue;
                    }


                    Iterator<String> it = pidSet.iterator();
                    while (it.hasNext()) {
                        String pid = it.next();
                        //如果此标签下面没有问题，直接跳珠while循环
                        if (CollectionUtil.isEmpty(qidSet)) {
                            break outer;
                        }
                        try {
                            //某个人的标签
                            Set<String> tagSet = askRedis.zrange(KEY_LIST_VERIFY_PROFILE_TAGS + pid);
                            if (tagSet.contains(String.valueOf(animeTag.getTag_id()))) {

                                if (!pid.equals(question.getAskProfileId())) {
                                    //TODO 发事件消息
                                    WanbaQuestionNoticeEvent wbNsIEvent = new WanbaQuestionNoticeEvent();
                                    wbNsIEvent.setProfileId(pid);
                                    wbNsIEvent.setDestProfileId(pid);
                                    wbNsIEvent.setType(NoticeType.ANSWER);
                                    wbNsIEvent.setQuestionId(question.getQuestionId());
                                    wbNsIEvent.setBodyType(WanbaNoticeBodyType.QUESTION_VERIFY);
                                    wbNsIEvent.setCreateTime(new Date());
                                    wbNsIEvent.setExtStr(animeTag.getTag_name());
                                    EventDispatchServiceSngl.get().dispatch(wbNsIEvent);

                                    it.remove();
                                }
                            }
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }
                }
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " viewsumHotrank  error,e: ", e);
            } finally {
                askRedis.setVerifyNoticeTrigger(false);
            }
        }

    }

    public void timedRelease() {

        if (!askRedis.gettimedReleaseTriggerRunning()) {
            askRedis.setTimedReleaseTriggerRunning(true);
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AskTimedReleaseField.VALIDSTATUS, ValidStatus.VALID.getCode()));
            queryExpress.add(QueryCriterions.leq(AskTimedReleaseField.RELEASETIME, new Date()));
            try {
                //GAlerter.lan("timedRelease---------->" + DateUtil.formatDateToString(new Date(), DateUtil.PATTERN_DATE_TIME));
                List<AskTimedRelease> releaseList = askWriteAbleHandler.queryAskTimedReleaseList(queryExpress);
                for (AskTimedRelease release : releaseList) {
                    //5次发布都失败，就不发了
                    if (release.getReleaseTimes() > 5) {
                        modifyAskTimedRelease(new UpdateExpress().set(AskTimedReleaseField.VALIDSTATUS, ValidStatus.REMOVED.getCode()), release.getTimeid());
                        continue;
                    }
                    //活动
                    if (release.getTimeRelseaseType().equals(TimeRelseaseType.ACTIVITY)) {
                        AnimeTag animeTag = new Gson().fromJson(release.getExtStr(), AnimeTag.class);
                        animeTag = JoymeAppServiceSngl.get().createAnimeTag(animeTag);
                        if (animeTag.getTag_id() > 0) {
                            modifyAskTimedRelease(new UpdateExpress().set(AskTimedReleaseField.VALIDSTATUS, ValidStatus.REMOVED.getCode()), release.getTimeid());
                        } else {
                            modifyAskTimedRelease(new UpdateExpress().increase(AskTimedReleaseField.RELEASETIMES, 1), release.getTimeid());
                        }
                    } else if (release.getTimeRelseaseType().equals(TimeRelseaseType.QUESTION)) {
                        QuestionRelease r1 = new Gson().fromJson(release.getExtStr(), QuestionRelease.class);

                        //String title,String text, String askpid, String timelimit, String tagid
                        String resultObj = AskUtil.postTimeQuestion(r1.getTitle(), r1.getText(), r1.getAskpid(), r1.getTimelimit(), r1.getTagid());
                        JSONObject jsonObject = JSONObject.fromObject(resultObj);
                        if (jsonObject.getString("rs").equals("1")) {
                            modifyAskTimedRelease(new UpdateExpress().set(AskTimedReleaseField.VALIDSTATUS, ValidStatus.REMOVED.getCode()), release.getTimeid());
                        } else {
                            modifyAskTimedRelease(new UpdateExpress().increase(AskTimedReleaseField.RELEASETIMES, 1), release.getTimeid());
                        }
                    }

                }
                askRedis.setTimedReleaseTriggerRunning(false);
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " viewsumHotrank  error,e: ", e);
                askRedis.setTimedReleaseTriggerRunning(false);
            }
        }
    }
}
