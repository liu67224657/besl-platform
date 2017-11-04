package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.ask.wiki.*;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/12
 */
public class AskHandler {

    private DataBaseType dataBaseType;
    private String dataSourceName;

    private WanbaItemAccessor wanbaItemAccessor;
    private QuestionAccessor questionAccessor;
    private AnswerAccessor answerAccessor;
    private WanbaProfileSumAccessor wanbaProfileSumAccessor;
    private QuestionSumAccessor questionSumAccessor;
    private AskUserActionAccessor askUserActionAccessor;
    private AnswerSumAccessor answerSumAccessor;
    private WanbaProfileClassifyAccessor wanbaProfileClassifyAccessor;
    private WikiGameresAccessor wikiGameresAccessor;
    private UserFollowGameAccessor userFollowGameAccessor;

    private AskReportAccessor askReportAccessor;

    private AskTimedReleaseAccessor askTimedReleaseAccessor;

    private ContentAccessor contentAccessor;
    private ContentLineAccessor contentLineAccessor;
    private AdvertiseAccessor advertiseAccessor;
    private ContentSumAccessor contentSumAccessor;
    private UserCollectAccessor userCollectAccessor;
    private ContentTagAccessor contentTagAccessor;

    private BlackListHistoryAccessor blackListHistoryAccessor;

    public AskHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);

        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        questionAccessor = TableAccessorFactory.get().factoryAccessor(QuestionAccessor.class, dataBaseType);
        wanbaItemAccessor = TableAccessorFactory.get().factoryAccessor(WanbaItemAccessor.class, dataBaseType);
        answerAccessor = TableAccessorFactory.get().factoryAccessor(AnswerAccessor.class, dataBaseType);
        wanbaProfileSumAccessor = TableAccessorFactory.get().factoryAccessor(WanbaProfileSumAccessor.class, dataBaseType);
        questionSumAccessor = TableAccessorFactory.get().factoryAccessor(QuestionSumAccessor.class, dataBaseType);
        askUserActionAccessor = TableAccessorFactory.get().factoryAccessor(AskUserActionAccessor.class, dataBaseType);
        answerSumAccessor = TableAccessorFactory.get().factoryAccessor(AnswerSumAccessor.class, dataBaseType);
        wanbaProfileClassifyAccessor = TableAccessorFactory.get().factoryAccessor(WanbaProfileClassifyAccessor.class, dataBaseType);

        askReportAccessor = TableAccessorFactory.get().factoryAccessor(AskReportAccessor.class, dataBaseType);
        askTimedReleaseAccessor = TableAccessorFactory.get().factoryAccessor(AskTimedReleaseAccessor.class, dataBaseType);

        contentAccessor = TableAccessorFactory.get().factoryAccessor(ContentAccessor.class, dataBaseType);
        contentLineAccessor = TableAccessorFactory.get().factoryAccessor(ContentLineAccessor.class, dataBaseType);
        advertiseAccessor = TableAccessorFactory.get().factoryAccessor(AdvertiseAccessor.class, dataBaseType);
        contentSumAccessor = TableAccessorFactory.get().factoryAccessor(ContentSumAccessor.class, dataBaseType);
        wikiGameresAccessor = TableAccessorFactory.get().factoryAccessor(WikiGameresAccessor.class, dataBaseType);
        userFollowGameAccessor = TableAccessorFactory.get().factoryAccessor(UserFollowGameAccessor.class, dataBaseType);
        userCollectAccessor = TableAccessorFactory.get().factoryAccessor(UserCollectAccessor.class, dataBaseType);
        contentTagAccessor = TableAccessorFactory.get().factoryAccessor(ContentTagAccessor.class, dataBaseType);

        blackListHistoryAccessor = TableAccessorFactory.get().factoryAccessor(BlackListHistoryAccessor.class, dataBaseType);

    }

    public WanbaItem insertAskItem(WanbaItem askItem) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return wanbaItemAccessor.insert(askItem, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyAskItem(UpdateExpress updateExpress, String item_id) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return wanbaItemAccessor.update(updateExpress, new QueryExpress().add(QueryCriterions.eq(WanbaItemField.ID, item_id)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public WanbaItem getAskItem(String item_id) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return wanbaItemAccessor.get(new QueryExpress().add(QueryCriterions.eq(WanbaItemField.ID, item_id)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<WanbaItem> queryWanbaItem(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<WanbaItem> wanbaItemList = wanbaItemAccessor.query(queryExpress, page, conn);

            PageRows<WanbaItem> pageRows = new PageRows<WanbaItem>();
            pageRows.setPage(page);
            pageRows.setRows(wanbaItemList);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Question insertQuestion(Question question) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return questionAccessor.insert(question, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Question getQuestionById(long questionId, IntValidStatus validStatus) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return questionAccessor.get(new QueryExpress()
                    .add(QueryCriterions.eq(QuestionField.QUESTIONID, questionId))
                    .add(QueryCriterions.eq(QuestionField.REMOVESTATUS, validStatus.getCode())), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<Question> queryQuestionById(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return questionAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<Question> queryQuestion(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<Question> questionList = questionAccessor.query(queryExpress, page, conn);

            PageRows<Question> pageRows = new PageRows<Question>();
            pageRows.setPage(page);
            pageRows.setRows(questionList);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean updateQuestionById(UpdateExpress updateExpress, long questionId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return questionAccessor.update(updateExpress, new QueryExpress().add(QueryCriterions.eq(QuestionField.QUESTIONID, questionId)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Answer insertAnswer(Answer answer) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return answerAccessor.insert(answer, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Answer getAnswerById(long answerId, IntValidStatus validStatus) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return answerAccessor.get(new QueryExpress()
                            .add(QueryCriterions.eq(AnswerField.ANSWERID, answerId))
                            .add(QueryCriterions.eq(AnswerField.REMOVESTATUS, validStatus.getCode()))
                    , conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Answer getAnswer(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return answerAccessor.get(queryExpress
                    , conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Answer getAnswerByQidAndProfileId(long questionId, String profileId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return answerAccessor.get(new QueryExpress()
                            .add(QueryCriterions.eq(AnswerField.QUESTIONID, questionId))
                            .add(QueryCriterions.eq(AnswerField.ANSWERPROFILEID, profileId))
                    , conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateAnswerById(UpdateExpress updateExpress, long answerId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return answerAccessor.update(updateExpress, new QueryExpress().add(QueryCriterions.eq(AnswerField.ANSWERID, answerId)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public List<Answer> queryAnswerById(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return answerAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<Answer> queryAnswer(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<Answer> answerList = answerAccessor.query(queryExpress, page, conn);

            PageRows<Answer> pageRows = new PageRows<Answer>();
            pageRows.setPage(page);
            pageRows.setRows(answerList);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    ///////////
    public boolean increaseWanbaProfileSum(WanbaProfileSumField field, int increaseValue, String profileId) throws DbException {
        Connection conn = null;

        QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(WanbaProfileSumField.PROFILEID, profileId));
        try {
            conn = DbConnFactory.factory(dataSourceName);
            UpdateExpress updateExpress = new UpdateExpress().increase(field, increaseValue);
            boolean success = wanbaProfileSumAccessor.update(updateExpress, queryExpress, conn) > 0;
            if (!success) {
                WanbaProfileSum sum = new WanbaProfileSum();
                sum.setProfileId(profileId);
                if (field.equals(WanbaProfileSumField.AWARDPOINT)) {
                    sum.setAwardPoint(increaseValue);
                } else if (field.equals(WanbaProfileSumField.ANSWERSUM)) {
                    sum.setAnswerSum(increaseValue);
                } else if (field.equals(WanbaProfileSumField.QUESIONFOLLOWSUM)) {
                    sum.setQuestionFollowSum(increaseValue);
                } else if (field.equals(WanbaProfileSumField.FAVORITESUM)) {
                    sum.setFavoriteSum(increaseValue);
                }
                wanbaProfileSumAccessor.insert(sum, conn);
                return true;
            }
            return success;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public WanbaProfileSum insertWanbaProfileSum(WanbaProfileSum sum) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return wanbaProfileSumAccessor.insert(sum, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public WanbaProfileSum getWanbaProfileSum(String profileId) throws DbException {
        Connection conn = null;
        try {
            QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(WanbaProfileSumField.PROFILEID, profileId));
            conn = DbConnFactory.factory(dataSourceName);
            return wanbaProfileSumAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /////////////


    ///
    public boolean increaseQuestionSum(QuestionSumField field, int increaseValue, long questionId) throws DbException {
        Connection conn = null;
        QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(QuestionSumField.QUESTIONID, questionId));
        try {
            conn = DbConnFactory.factory(dataSourceName);
            UpdateExpress updateExpress = new UpdateExpress().increase(field, increaseValue);
            boolean success = questionSumAccessor.update(updateExpress, queryExpress, conn) > 0;

            if (!success) {
                QuestionSum sum = new QuestionSum();
                sum.setQuestionCreateTime(new Date());
                sum.setQuestionId(questionId);
                if (field.equals(QuestionSumField.VIEWSUM)) {
                    sum.setViewSum(increaseValue);
                } else if (field.equals(QuestionSumField.ANSEWERSUM)) {
                    sum.setAnsewerSum(increaseValue);
                }
                questionSumAccessor.insert(sum, conn);
                return true;
            }
            return success;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean updateQuestionSum(UpdateExpress updateExpress, long qid) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return questionSumAccessor.update(updateExpress, new QueryExpress().add(QueryCriterions.eq(QuestionSumField.QUESTIONID, qid)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<QuestionSum> queryQuestionSum(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return questionSumAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    ////////////////////////////////////////////////
    public AskUserAction insertAskUserAction(AskUserAction askUserAction) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return askUserActionAccessor.insert(askUserAction, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AskUserAction getAskUserAction(String askUserActionId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(AskUserActionField.ASKUSERACTIONID, askUserActionId));
            return askUserActionAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean deleteAskUserAction(String askUserActionId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(AskUserActionField.ASKUSERACTIONID, askUserActionId));
            return askUserActionAccessor.delete(queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<AskUserAction> queryAskUserAction(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return askUserActionAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AskUserAction> queryAskUserAction(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<AskUserAction> askUserActionList = askUserActionAccessor.query(queryExpress, page, conn);

            PageRows<AskUserAction> pageRows = new PageRows<AskUserAction>();
            pageRows.setPage(page);
            pageRows.setRows(askUserActionList);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////////////////////////////////////
    public boolean increaseAnswerSum(AnswerSumField field, int increaseValue, long answerId) throws DbException {
        Connection conn = null;
        QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(AnswerSumField.ANSWERID, answerId));
        try {
            conn = DbConnFactory.factory(dataSourceName);

            UpdateExpress updateExpress = new UpdateExpress().increase(field, increaseValue);
            boolean success = answerSumAccessor.update(updateExpress, queryExpress, conn) > 0;
            if (!success) {
                AnswerSum sum = new AnswerSum();
                sum.setAnswerId(answerId);
                if (field.equals(AnswerSumField.VIEWSUM)) {
                    sum.setViewSum(increaseValue);
                } else if (field.equals(AnswerSumField.REPLYSUM)) {
                    sum.setReplySum(increaseValue);
                } else if (field.equals(AnswerSumField.AGREESUM)) {
                    sum.setAgreeSum(increaseValue);
                }
                answerSumAccessor.insert(sum, conn);
                return true;
            }
            return success;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public WanbaProfileClassify insertWanbaProfileClassify(WanbaProfileClassify wanbaProfileClassify) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return wanbaProfileClassifyAccessor.insert(wanbaProfileClassify, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyWanbaProfileClassify(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return wanbaProfileClassifyAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public WanbaProfileClassify getWanbaProfileClassify(String classifyid) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return wanbaProfileClassifyAccessor.get(new QueryExpress().add(QueryCriterions.eq(WanbaProfileClassifyField.CLASSIFY_ID, classifyid)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<WanbaProfileClassify> queryWanbaProfileClassify(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<WanbaProfileClassify> wanbaProfileClassifyList = wanbaProfileClassifyAccessor.query(queryExpress, page, conn);

            PageRows<WanbaProfileClassify> pageRows = new PageRows<WanbaProfileClassify>();
            pageRows.setPage(page);
            pageRows.setRows(wanbaProfileClassifyList);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<WanbaProfileClassify> queryWanbaProfileClassifyList(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<WanbaProfileClassify> wanbaProfileClassifyList = wanbaProfileClassifyAccessor.query(queryExpress, conn);

            return wanbaProfileClassifyList;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
    ///////////////


    public AskReport insertAskReport(AskReport askReport) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return askReportAccessor.insert(askReport, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyAskReport(UpdateExpress updateExpress, String report_id) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return askReportAccessor.update(updateExpress, new QueryExpress().add(QueryCriterions.eq(AskReportField.REPORTID, report_id)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AskReport getAskReport(String report_id) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return askReportAccessor.get(new QueryExpress().add(QueryCriterions.eq(AskReportField.REPORTID, report_id)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AskReport> queryAskReport(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<AskReport> askReportList = askReportAccessor.query(queryExpress, page, conn);

            PageRows<AskReport> pageRows = new PageRows<AskReport>();
            pageRows.setPage(page);
            pageRows.setRows(askReportList);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    ///////////////

    public AskTimedRelease insertAskTimedRelease(AskTimedRelease askTimedRelease) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return askTimedReleaseAccessor.insert(askTimedRelease, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyAskTimedRelease(UpdateExpress updateExpress, Long timeid) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return askTimedReleaseAccessor.update(updateExpress, new QueryExpress().add(QueryCriterions.eq(AskTimedReleaseField.TIMEID, timeid)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AskTimedRelease getAskTimedRelease(Long timeid) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return askTimedReleaseAccessor.get(new QueryExpress().add(QueryCriterions.eq(AskTimedReleaseField.TIMEID, timeid)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AskTimedRelease> queryAskTimedRelease(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<AskTimedRelease> timedReleases = askTimedReleaseAccessor.query(queryExpress, page, conn);

            PageRows<AskTimedRelease> pageRows = new PageRows<AskTimedRelease>();
            pageRows.setPage(page);
            pageRows.setRows(timedReleases);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<AskTimedRelease> queryAskTimedReleaseList(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<AskTimedRelease> askTimedReleaseList = askTimedReleaseAccessor.query(queryExpress, conn);

            return askTimedReleaseList;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public Content insertContent(Content content) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return contentAccessor.insert(content, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateContent(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return contentAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Content getContent(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return contentAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<Content> queryContent(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return contentAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ContentLine insertContentLine(ContentLine contentLine) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return contentLineAccessor.insert(contentLine, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean updateContentLine(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return contentLineAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<Advertise> queryAdvertise(QueryExpress queryExpress)throws DbException{
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<Advertise> advertiseList = advertiseAccessor.query(queryExpress, conn);
            return advertiseList;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public PageRows<Advertise> queryAdvertise(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<Advertise> advertiseList = advertiseAccessor.query(queryExpress, page, conn);
            PageRows<Advertise> pageRows = new PageRows<Advertise>();
            pageRows.setPage(page);
            pageRows.setRows(advertiseList);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Advertise insertAdvertise(Advertise advertise) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return advertiseAccessor.insert(advertise, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Advertise getAdvertise(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return advertiseAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateAdvertise(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return advertiseAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ContentSum insertContentSum(ContentSum contentSum) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return contentSumAccessor.insert(contentSum, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ContentSum getContentSum(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return contentSumAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean increaseContentSum(ContentSumField contentSumField, int increaseValue, long contengId) throws DbException {
        Connection conn = null;
        QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(ContentSumField.ID, contengId));
        try {
            conn = DbConnFactory.factory(dataSourceName);
            UpdateExpress updateExpress = new UpdateExpress().increase(contentSumField, increaseValue);
            boolean bval = contentSumAccessor.update(updateExpress, queryExpress, conn) > 0;
            if (!bval) {
                ContentSum sum = new ContentSum();
                sum.setCreateDate(new Date());
                sum.setId(contengId);
                if (contentSumField.equals(ContentSumField.AGREE_NUM)) {
                    sum.setAgree_num(increaseValue);
                }
                contentSumAccessor.insert(sum, conn);
                return true;
            }
            return bval;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public WikiGameres createWikiGameres(WikiGameres wikiGameres) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return wikiGameresAccessor.insert(wikiGameres, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyWikiGameres(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return wikiGameresAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public WikiGameres getWikiGameRes(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return wikiGameresAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<WikiGameres> queryWikiGameresByPage(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<WikiGameres> WikiGameres = wikiGameresAccessor.query(queryExpress, page, conn);

            PageRows<WikiGameres> pageRows = new PageRows<WikiGameres>();
            pageRows.setPage(page);
            pageRows.setRows(WikiGameres);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<WikiGameres> queryWikiGameres(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<WikiGameres> askTimedReleaseList = wikiGameresAccessor.query(queryExpress, conn);

            return askTimedReleaseList;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////////////////////////////////////////////

    public UserFollowGame createUserFollowGame(UserFollowGame userFollowGame) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userFollowGameAccessor.insert(userFollowGame, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyUserFollowGame(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userFollowGameAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserFollowGame getUserFollowGame(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userFollowGameAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<UserFollowGame> queryUserFollowGameByPage(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<UserFollowGame> UserFollowGames = userFollowGameAccessor.query(queryExpress, page, conn);

            PageRows<UserFollowGame> pageRows = new PageRows<UserFollowGame>();
            pageRows.setPage(page);
            pageRows.setRows(UserFollowGames);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<UserFollowGame> queryUserFollowGames(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<UserFollowGame> UserFollowGames = userFollowGameAccessor.query(queryExpress, conn);

            return UserFollowGames;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public UserCollect createUserCollect(UserCollect userCollect) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userCollectAccessor.insert(userCollect, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyUserCollect(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userCollectAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean deleteUserCollect(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userCollectAccessor.delete(queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserCollect getUserCollect(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userCollectAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<UserCollect> queryUserCollectByPage(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<UserCollect> UserFollowGames = userCollectAccessor.query(queryExpress, page, conn);

            PageRows<UserCollect> pageRows = new PageRows<UserCollect>();
            pageRows.setPage(page);
            pageRows.setRows(UserFollowGames);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<UserCollect> queryUserCollect(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<UserCollect> userCollectList = userCollectAccessor.query(queryExpress, conn);
            return userCollectList;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    ///
    public PageRows<ContentTag> queryContentTag(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<ContentTag> contentTagList = contentTagAccessor.query(queryExpress, page, conn);
            PageRows<ContentTag> pageRows = new PageRows<ContentTag>();
            pageRows.setPage(page);
            pageRows.setRows(contentTagList);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ContentTag insertContentTag(ContentTag contentTag) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return contentTagAccessor.insert(contentTag, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ContentTag getContentTag(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return contentTagAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateContentTag(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return contentTagAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public PageRows<BlackListHistory> queryBlackListHistory(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<BlackListHistory> contentTagList = blackListHistoryAccessor.query(queryExpress, page, conn);
            PageRows<BlackListHistory> pageRows = new PageRows<BlackListHistory>();
            pageRows.setPage(page);
            pageRows.setRows(contentTagList);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public BlackListHistory insertBlackListHistory(BlackListHistory blackListHistory) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return blackListHistoryAccessor.insert(blackListHistory, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public BlackListHistory getBlackListHistory(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return blackListHistoryAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateBlackListHistory(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return blackListHistoryAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


}
