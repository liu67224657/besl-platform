package com.enjoyf.platform.serv.ask;

import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.ask.wiki.*;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.redis.ScoreRange;
import com.enjoyf.util.MD5Util;
import com.google.gson.Gson;

import java.util.*;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/12
 */
public class AskRedis {

    private static final String KEY_ITEM_LINE = AskConstants.SERVICE_SECTION + "_line_";
    private static final String KEY_QUESTIONOBJ = AskConstants.SERVICE_SECTION + "_qobj_";
    private static final String KEY_ANSWEROBJ = AskConstants.SERVICE_SECTION + "_aobj_";
    private static final String KEY_QUESTIONSUM = AskConstants.SERVICE_SECTION + "_qsum_";

    private static final String KEY_PROFILESUM = AskConstants.SERVICE_SECTION + "_psum_";
    private static final String KEY_PROFILE = AskConstants.SERVICE_SECTION + "_profile_";
    private static final String KEY_VERIFY = AskConstants.SERVICE_SECTION + "_verify_";

    private static final String KEY_ASKUSERACTION = AskConstants.SERVICE_SECTION + "_aua_";
    private static final String KEY_ANSWERSUM = AskConstants.SERVICE_SECTION + "_asum_";

    private static final String KEY_LIST_VERIFYPROFILE = AskConstants.SERVICE_SECTION + "_vplist_";

    private static final String KEY_QUESTION_P2Q_FOLLOWLIST = AskConstants.SERVICE_SECTION + "_fp2qlist_";//profile--->questions
    private static final String KEY_QUESTION_Q2P_FOLLOWLIST = AskConstants.SERVICE_SECTION + "_fq2plist_";//question--->profiles

    private static final String KEY_CHECK_FOLLOW_QUESTION_TEMP = AskConstants.SERVICE_SECTION + "_cfqtemp_";//question--->profiles
    private static final String KEY_CHECK_FOLLOW_QUESTION_TEMPRESULT = AskConstants.SERVICE_SECTION + "_cfqtempresult_";//question--->profiles

    private static final String KEY_INVITE_ANSWERQUESTION_PROFILELIST = AskConstants.SERVICE_SECTION + "_iaqpset_";//profile+question-->profiles
    private static final String KEY_CHECK_INVITE_PROFILE_TEMP = AskConstants.SERVICE_SECTION + "_ciptemp_";//question--->profiles
    private static final String KEY_CHECK_INVITE_PROFILE_TEMPRESULT = AskConstants.SERVICE_SECTION + "_ciprtempreslt_";//question--->profiles


    private static final String KEY_RECOMMENT_PROFILES = AskConstants.SERVICE_SECTION + "_recomment_profiles_";//推荐列表中的达人推荐


    private static final String KEY_ASKREPORTOBJ = AskConstants.SERVICE_SECTION + "_askreportbj_";

    private static final String KEY_WANBAPROFILECLASSIFY = AskConstants.SERVICE_SECTION + "_WanbaProfileClassifyobj_";

    private static final String KEY_TIMEDRELEASETRIGGER = AskConstants.SERVICE_SECTION + "_timedreleasetrigger_";

    private static final String KEY_VERIFYNOTICETRIGGER = AskConstants.SERVICE_SECTION + "_verifynoticetrigger_";


    //所有文章、游戏对应的文章，永久保存
    private static final String KEY_CONTEN_LINEKEY = AskConstants.SERVICE_SECTION + "_contentlinekey_";


    private static final String KEY_CONTEN_OBJ = AskConstants.SERVICE_SECTION + "_contentobj_";


    private static final String KEY_CONTEN_COMMENTID = AskConstants.SERVICE_SECTION + "_contentcommentid_";


    private static final String KEY_CONTENSUM_OBJ = AskConstants.SERVICE_SECTION + "_contensumobj_";

    private static final String KEY_CONTENSUM_LINE = AskConstants.SERVICE_SECTION + "_contensumline_";

    //广告obj
    private static final String KEY_ADVERTISE_OBJ = AskConstants.SERVICE_SECTION + "_adobj_";

    //广告line
    private static final String KEY_ADVERTISE_LINE = AskConstants.SERVICE_SECTION + "_adline_";

    //标签obj
    private static final String KEY_CONTENTTAG_OBJ = AskConstants.SERVICE_SECTION + "_tagobj_";

    //标签line
    private static final String KEY_CONTENTTAG_LINE = AskConstants.SERVICE_SECTION + "_tagline_";

    //推荐游戏的ID
    private static final String KEY_RECOMMEND_GAMES = AskConstants.SERVICE_SECTION + "_recommend_games_";

    //
    private static final String KEY_WIKI_GAME_INFO = AskConstants.SERVICE_SECTION + "_wiki_game_info_";

    private static final String KEY_USER_FOLLOW_GAMES = AskConstants.SERVICE_SECTION + "_user_follow_game_list_";

    private static final String KEY_USER_COLLECT_LIST = AskConstants.SERVICE_SECTION + "_user_collect_list_";
    private static final String KEY_USER_COLLECT = AskConstants.SERVICE_SECTION + "_user_collect_";

    private static final String KEY_GET_BLACKLIST_ = AskConstants.SERVICE_SECTION + "_get_blacklist_";

    private static final String KEY_GAME_FOLLOW_SUM = AskConstants.SERVICE_SECTION + "_game_follow_sum_";

    private static final String KEY_QUERY_BLACKLIST_PID = AskConstants.SERVICE_SECTION + "_query_blacklist_pid";


    private RedisManager manager;
    private boolean verifyNoticeTrigger;

    public AskRedis(FiveProps p) {
        manager = new RedisManager(p);
    }

    public long getLineSize(String lineKey) {
        return manager.zcard(getLineKey(lineKey));
    }

    public void addLine(WanbaItem item) {
        manager.zadd(getLineKey(item.getLineKey()), item.getScore(), String.valueOf(item.getDestId()) + "_" + item.getItemType().getCode(), -1);
    }

    public boolean removeItem(String lineKey, String destId, ItemType itemType) {
        return manager.zrem(getLineKey(lineKey), String.valueOf(destId) + "_" + itemType.getCode()) > 0;
    }

    public Map<String, ItemType> queryItemByLine(String lineKey, Pagination page, boolean isDesc) {
        String redisKey = getLineKey(lineKey);

        Set<String> stringSet = manager.zrange(redisKey, page.getStartRowIdx(), page.getEndRowIdx(), isDesc ? RedisManager.RANGE_ORDERBY_DESC : RedisManager.RANGE_ORDERBY_ASC);
        Map<String, ItemType> returnValue = new LinkedHashMap<String, ItemType>();
        for (String value : stringSet) {
            String[] valueArray = value.split("_");
            if (valueArray.length != 2) {
                manager.zrem(redisKey, value);
                continue;
            }

            try {
                String destId = valueArray[0];
                int askTypeIntValue = Integer.valueOf(valueArray[1]);
                ItemType itemType = ItemType.getByCode(askTypeIntValue);
                if (itemType == null) {
                    manager.zrem(redisKey, value);
                    continue;
                }
                returnValue.put(destId, itemType);
            } catch (NumberFormatException e) {
                manager.zrem(redisKey, value);
                continue;
            }
        }
        return returnValue;
    }


    public Map<String, ItemType> queryItemByLineSortRange(String lineKey, ScoreRange scoreRange) {
        String redisKey = getLineKey(lineKey);

        String orderBy = scoreRange.isDesc() ? RedisManager.RANGE_ORDERBY_DESC : RedisManager.RANGE_ORDERBY_ASC;


        Set<String> stringSet = manager.zrangeByScore(redisKey, scoreRange, orderBy);
        Map<String, ItemType> returnValue = new LinkedHashMap<String, ItemType>();
        for (String value : stringSet) {
            String[] valueArray = value.split("_");
            if (valueArray.length != 2) {
                manager.zrem(redisKey, value);
                continue;
            }

            try {
                String destId = valueArray[0];
                int askTypeIntValue = Integer.valueOf(valueArray[1]);
                ItemType itemType = ItemType.getByCode(askTypeIntValue);
                if (itemType == null) {
                    manager.zrem(redisKey, value);
                    continue;
                }
                returnValue.put(destId, itemType);
            } catch (NumberFormatException e) {
                manager.zrem(redisKey, value);
                continue;
            }
        }
        return returnValue;
    }


    public long countLine(String lineKey) {
        return manager.zcard(getLineKey(lineKey));
    }


    private String getLineKey(String lineKey) {
        return KEY_ITEM_LINE + lineKey;
    }

    public void setQuestion(Question question) {
        manager.set(KEY_QUESTIONOBJ + question.getQuestionId(), new Gson().toJson(question));
    }

    public Question getQuestion(long questionId) {
        String objStr = manager.get(KEY_QUESTIONOBJ + questionId);

        if (StringUtil.isEmpty(objStr)) {
            return null;
        }

        return new Gson().fromJson(objStr, Question.class);
    }


    public boolean delQuestion(long questionId) {
        return manager.remove(KEY_QUESTIONOBJ + questionId) > 0;
    }

    public void setAnswer(Answer answer) {
        manager.set(KEY_ANSWEROBJ + answer.getAnswerId(), new Gson().toJson(answer));
    }

    public Answer getAnswer(long answerId) {
        String objStr = manager.get(KEY_ANSWEROBJ + answerId);

        if (StringUtil.isEmpty(objStr)) {
            return null;
        }

        return new Gson().fromJson(objStr, Answer.class);
    }

    public boolean delAnswer(long answerId) {
        return manager.remove(KEY_ANSWEROBJ + answerId) > 0;
    }


    /////////////////////////////
    public boolean increaseWanbaProfileSum(String profileId, WanbaProfileSumField field, int value) {
        return manager.hinccrby(KEY_PROFILESUM + profileId, field.getColumn(), value) > 0;
    }

    public void setWanbaProfileSum(String profileId, WanbaProfileSumField field, int value) {
        manager.hset(KEY_PROFILESUM + profileId, field.getColumn(), String.valueOf(value));
    }


    public int getWanbaProfileSumValue(String profileId, WanbaProfileSumField field) {
        String val = manager.hget(KEY_PROFILESUM + profileId, field.getColumn());
        if (StringUtil.isEmpty(val)) {
            return 0;
        }

        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            removeWanbaProfileSum(profileId, field);
            return 0;
        }
    }

    public boolean removeWanbaProfileSum(String profileId, WanbaProfileSumField field) {
        return manager.delHash(KEY_PROFILESUM + profileId, field.getColumn()) > 0;
    }

    public WanbaProfileSum getWanbaSumObjById(String profileId) {
        WanbaProfileSum sum = new WanbaProfileSum();
        sum.setProfileId(profileId);
        sum.setAnswerSum(getWanbaProfileSumValue(profileId, WanbaProfileSumField.ANSWERSUM));
        sum.setAwardPoint(getWanbaProfileSumValue(profileId, WanbaProfileSumField.AWARDPOINT));
        sum.setQuestionFollowSum(getWanbaProfileSumValue(profileId, WanbaProfileSumField.QUESIONFOLLOWSUM));
        sum.setFavoriteSum(getWanbaProfileSumValue(profileId, WanbaProfileSumField.FAVORITESUM));
        return sum;
    }

    public void setWanbaSumObjById(String profileId, WanbaProfileSum wanbaProfileSum) {
        setWanbaProfileSum(profileId, WanbaProfileSumField.ANSWERSUM, wanbaProfileSum.getAnswerSum());
        setWanbaProfileSum(profileId, WanbaProfileSumField.AWARDPOINT, wanbaProfileSum.getAwardPoint());
        setWanbaProfileSum(profileId, WanbaProfileSumField.QUESIONFOLLOWSUM, wanbaProfileSum.getQuestionFollowSum());
        setWanbaProfileSum(profileId, WanbaProfileSumField.FAVORITESUM, wanbaProfileSum.getFavoriteSum());
    }


    public boolean delProfile(String profileId) {
        return manager.remove(KEY_PROFILE + profileId) > 0;
    }

    /////////////////////////////
    public boolean increaseQuestionSum(long questionId, QuestionSumField field, int value) {
        return manager.hinccrby(KEY_QUESTIONSUM + questionId, field.getColumn(), value) > 0;
    }

    public int getQuesstionSumValue(long qeuestionId, QuestionSumField field) {
        String val = manager.hget(KEY_QUESTIONSUM + qeuestionId, field.getColumn());
        if (StringUtil.isEmpty(val)) {
            return 0;
        }

        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            removeQuestionSum(qeuestionId, field);
            return 0;
        }
    }

    public boolean removeQuestionSum(long qeuestionId, QuestionSumField field) {
        return manager.delHash(KEY_QUESTIONSUM + qeuestionId, field.getColumn()) > 0;
    }

    public QuestionSum getQuestionSumObjById(long questionId) {
        QuestionSum questionSum = new QuestionSum();
        questionSum.setQuestionId(questionId);
        questionSum.setAnsewerSum(getQuesstionSumValue(questionId, QuestionSumField.ANSEWERSUM));
        questionSum.setViewSum(getQuesstionSumValue(questionId, QuestionSumField.VIEWSUM));
        questionSum.setFollowSum(getQuesstionSumValue(questionId, QuestionSumField.FOLLOWSUM));
        return questionSum;
    }


    public void setAskUserAction(AskUserAction askUserAction) {
        manager.setSec(KEY_ASKUSERACTION + askUserAction.getAskUserActionId(), new Gson().toJson(askUserAction), 3600);
    }

    public AskUserAction getUserAskAction(String actionId) {
        String objStr = manager.get(KEY_ASKUSERACTION + actionId);

        if (StringUtil.isEmpty(objStr)) {
            return null;
        }

        return new Gson().fromJson(objStr, AskUserAction.class);
    }

    public void deleteUserAskAction(String actionId) {
        manager.remove(KEY_ASKUSERACTION + actionId);
    }

    ///////////////////////////
    public boolean increaseAnswerSum(long questionId, AnswerSumField field, int value) {
        return manager.hinccrby(KEY_ANSWERSUM + questionId, field.getColumn(), value) > 0;
    }

    public int getAnswerSumValue(long qeuestionId, AnswerSumField field) {
        String val = manager.hget(KEY_ANSWERSUM + qeuestionId, field.getColumn());
        if (StringUtil.isEmpty(val)) {
            return 0;
        }

        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            removeAnswerSum(qeuestionId, field);
            return 0;
        }
    }

    public boolean removeAnswerSum(long qeuestionId, AnswerSumField field) {
        return manager.delHash(KEY_ANSWERSUM + qeuestionId, field.getColumn()) > 0;
    }

    public AnswerSum getAnswerSumObjById(long answerId) {
        AnswerSum answerSum = new AnswerSum();
        answerSum.setAnswerId(answerId);
        answerSum.setAgreeSum(getAnswerSumValue(answerId, AnswerSumField.AGREESUM));
        answerSum.setViewSum(getAnswerSumValue(answerId, AnswerSumField.VIEWSUM));
        answerSum.setReplySum(getAnswerSumValue(answerId, AnswerSumField.REPLYSUM));
        return answerSum;
    }


    ///////////////////////////////
    public Set<String> getProfileIdByTagId(long tagId, Pagination page) {
        return manager.zrange(KEY_LIST_VERIFYPROFILE + tagId, page.getStartRowIdx(), page.getEndRowIdx(), RedisManager.RANGE_ORDERBY_DESC);
    }

    public Set<String> queryAllProfileIdByTagId(long tagId) {
        return manager.zrange(KEY_LIST_VERIFYPROFILE + tagId, 0, -1, RedisManager.RANGE_ORDERBY_DESC);
    }

    public void addProfileIdByTagId(long tagId, String profileId, double socre) {
        manager.zadd(KEY_LIST_VERIFYPROFILE + tagId, socre, profileId, -1);
    }

    public long removePrifileIdByTagId(long tagId, String profileId) {
        return manager.zrem(KEY_LIST_VERIFYPROFILE + tagId, profileId);
    }

    public boolean existsPrifileIdByTagId(long tagId, String profileId) {
        return manager.zscore(KEY_LIST_VERIFYPROFILE + tagId, profileId) != null;
    }

    public Double profileidByTagId(long tagId, String profileId) {
        return manager.zscore(KEY_LIST_VERIFYPROFILE + tagId, profileId);
    }

    public long profileZrank(long tagId, String profileId) {
        return manager.zrank(KEY_LIST_VERIFYPROFILE + tagId, profileId, RedisManager.RANGE_ORDERBY_DESC);
    }

    public static void main(String[] ages) {
        FiveProps f = Props.instance().getServProps();
        AskRedis askRedis = new AskRedis(f);

//        long score = Math.round(scoreDouble);
//        System.out.println(score);
        boolean a = askRedis.querFollowIsExis("886f8fb76c0cc3ca3e7d9fb2552bed09", "101160");
        System.out.println(a);

//        askRedis.addProfileIdByTagId(58l,"9ace5d1cf0f2db95165d97a9759e6c75",10);

    }

    public long getProfileIdByTagIdSize(long tagId) {
        return manager.zcard(KEY_LIST_VERIFYPROFILE + tagId);
    }


    //////////////////////////////
    public void followQuestion(Long questionId, String profileId, long createTimestamp) {
        manager.zadd(KEY_QUESTION_P2Q_FOLLOWLIST + profileId, createTimestamp + Double.parseDouble("0." + String.valueOf(questionId)), String.valueOf(questionId), -1);
        manager.zadd(KEY_QUESTION_Q2P_FOLLOWLIST + questionId, createTimestamp + Double.parseDouble("0." + String.valueOf(Math.abs(profileId.hashCode()))), profileId, -1);
    }

    public boolean unFollowQuestion(Long questionId, String profileId) {
        return manager.zrem(KEY_QUESTION_P2Q_FOLLOWLIST + profileId, String.valueOf(questionId)) > 0 && manager.zrem(KEY_QUESTION_Q2P_FOLLOWLIST + questionId, profileId) > 0;
    }

    public Set<String> queryFollowProfileIdsByQuestionId(Long questionId, ScoreRange range) {
        return manager.zrangeByScore(KEY_QUESTION_Q2P_FOLLOWLIST + questionId, range, RedisManager.RANGE_ORDERBY_DESC);
    }

    public Set<String> queryFollowQuestionByProfileId(String profileId, ScoreRange range) {
        return manager.zrangeByScore(KEY_QUESTION_P2Q_FOLLOWLIST + profileId, range, RedisManager.RANGE_ORDERBY_DESC);
    }

    public boolean profileExistsFollowQuestionList(Long questionId, String profileId) {
        return manager.zscore(KEY_QUESTION_Q2P_FOLLOWLIST + questionId, profileId) != null;
    }

    public boolean questionExistsFollowProfileList(String profileId, Long questionId) {
        return manager.zscore(KEY_QUESTION_P2Q_FOLLOWLIST + profileId, String.valueOf(questionId)) != null;
    }

    /**
     * check 问题关注状态
     *
     * @param questions
     * @param profileId
     * @return
     */
    public Set<String> checkFollowQuestion(Collection<Long> questions, String profileId) {
        String keySuffix = MD5Util.Md5(profileId + UUID.randomUUID().toString());
        String tempKey = KEY_CHECK_FOLLOW_QUESTION_TEMP + keySuffix;
        String tempResulKey = KEY_CHECK_FOLLOW_QUESTION_TEMPRESULT + keySuffix;

        Map<String, Double> questionMap = new HashMap<String, Double>();
        for (long questionId : questions) {
            questionMap.put(String.valueOf(questionId), (double) questionId);
        }

        manager.zadd(tempKey, questionMap, 3600);
        manager.zinterstore(tempResulKey, null, 3600, tempKey, KEY_QUESTION_P2Q_FOLLOWLIST + profileId);
        Set<String> result = manager.zrange(tempResulKey, 0, -1, RedisManager.RANGE_ORDERBY_ASC);
        manager.remove(tempResulKey);
        manager.remove(tempKey);
        return result;
    }


    //////////////////////////////////////////////

    /**
     * 添加到 profilId+questionid ,inviteId 排序 createTime+0.questionId
     *
     * @param questionId       问题ID
     * @param invitedProfileId 被邀请人PID
     * @param score            排序值
     */
    public void addInvitePofile(long questionId, String invitedProfileId, double score) {
        manager.zadd(getInvitePofileKey(questionId), score, invitedProfileId, -1);
    }

    /**
     * 移除
     *
     * @param questionId       问题ID
     * @param invitedProfileId 被邀请人PID
     */
    public boolean removeInviteProfile(long questionId, String invitedProfileId) {
        return manager.zrem(getInvitePofileKey(questionId), invitedProfileId) > 0;
    }


    /**
     * check邀请状态,即invitedProfiles和这个人在这个问题的请集合取交集,返回值就是邀请结果
     *
     * @param invitedProfiles check的人的列表
     * @param quesitonId      问题ID
     * @return 交集
     */
    public Set<String> checkInviteProfile(Collection<String> invitedProfiles, Long quesitonId) {
        String keySuffix = MD5Util.Md5(quesitonId + UUID.randomUUID().toString());
        String tempKey = KEY_CHECK_INVITE_PROFILE_TEMP + keySuffix;
        String tempResulKey = KEY_CHECK_INVITE_PROFILE_TEMPRESULT + keySuffix;

        Map<String, Double> checkPidMap = new HashMap<String, Double>();
        for (String pid : invitedProfiles) {
            checkPidMap.put(pid, (double) invitedProfiles.hashCode());
        }

        manager.zadd(tempKey, checkPidMap, 3600);
        manager.zinterstore(tempResulKey, null, 3600, tempKey, getInvitePofileKey(quesitonId));
        Set<String> result = manager.zrange(tempResulKey, 0, -1, RedisManager.RANGE_ORDERBY_ASC);
        manager.remove(tempResulKey);
        manager.remove(tempKey);
        return result;
    }

    /**
     * 分页查询
     *
     * @param questionId 问题ID
     * @param scoreRange 分页参数
     */
    public Set<String> queryInviteProfile(long questionId, ScoreRange scoreRange) {
        return manager.zrangeByScore(getInvitePofileKey(questionId), scoreRange, RedisManager.RANGE_ORDERBY_DESC);
    }

    public boolean existsInviteProfile(long questionId, String invitedProfileId) {
        return manager.zscore(getInvitePofileKey(questionId), invitedProfileId) != null;
    }

    private String getInvitePofileKey(long questionId) {
        return KEY_INVITE_ANSWERQUESTION_PROFILELIST + questionId;
    }


    ///
    public List<String> getWanbaRecommentProfiles() {
        List<String> strLists = manager.lrange(KEY_RECOMMENT_PROFILES, 0, -1);
        if (CollectionUtil.isEmpty(strLists)) {
            return null;
        }
        List<String> returnSets = new ArrayList<String>();
        for (String str : strLists) {
            returnSets.add(str);
        }
        return returnSets;
    }

    public boolean updateWanbaRecommentProfiles(Collection<String> profileIds) {
        manager.remove(KEY_RECOMMENT_PROFILES);
        for (String pid : profileIds) {
            manager.rpush(KEY_RECOMMENT_PROFILES, pid);
        }
        return true;
    }


    public void setAskReport(AskReport askReport) {
        manager.set(KEY_ASKREPORTOBJ + askReport.getReportId(), new Gson().toJson(askReport));
    }

    public AskReport getAskReport(String reportId) {
        String objStr = manager.get(KEY_ASKREPORTOBJ + reportId);
        if (StringUtil.isEmpty(objStr)) {
            return null;
        }
        return new Gson().fromJson(objStr, AskReport.class);
    }

    public boolean delAskReport(String reportId) {
        return manager.remove(KEY_ASKREPORTOBJ + reportId) > 0;
    }


    public void setWanbaProfileClassify(WanbaProfileClassify wanbaProfileClassify) {
        manager.set(KEY_WANBAPROFILECLASSIFY + wanbaProfileClassify.getClassifyid(), new Gson().toJson(wanbaProfileClassify));
    }

    public WanbaProfileClassify getWanbaProfileClassify(String classifyid) {
        String objStr = manager.get(KEY_WANBAPROFILECLASSIFY + classifyid);
        if (StringUtil.isEmpty(objStr)) {
            return null;
        }
        return new Gson().fromJson(objStr, WanbaProfileClassify.class);
    }

    public boolean delWanbaProfileClassify(String classifyid) {
        return manager.remove(KEY_WANBAPROFILECLASSIFY + classifyid) > 0;
    }


    public Set<String> smembers(String key) {
        return manager.smembers(key);
    }

    public Set<String> zrange(String key) {
        return manager.zrange(key, 0, -1, RedisManager.RANGE_ORDERBY_DESC);
    }

    public Set<String> zrangeByScore(String lineKey, double min, double max, String sort) {
        return manager.zrangeByScore(getLineKey(lineKey), min, max, sort);
    }


    public void setTimedReleaseTriggerRunning(boolean isRunning) {
        manager.set(KEY_TIMEDRELEASETRIGGER, Boolean.toString(isRunning));
    }


    public boolean gettimedReleaseTriggerRunning() {
        String isRuning = manager.get(KEY_TIMEDRELEASETRIGGER);
        if (StringUtil.isEmpty(isRuning)) {
            return false;
        }
        return Boolean.valueOf(isRuning);
    }

    public void setVerifyNoticeTrigger(boolean verifyNoticeTrigger) {
        manager.set(KEY_VERIFYNOTICETRIGGER, Boolean.toString(verifyNoticeTrigger));
    }

    public boolean getVerifyNoticeTrigger() {
        String isRuning = manager.get(KEY_VERIFYNOTICETRIGGER);
        if (StringUtil.isEmpty(isRuning)) {
            return false;
        }
        return Boolean.valueOf(isRuning);
    }


    //contentline
    public void addContentLine(String linekey, double score, Long destId) {
        manager.zadd(KEY_CONTEN_LINEKEY + linekey, score, String.valueOf(destId), -1);
    }

    public int getContentLineSize(String linekey) {
        return (int) manager.zcard(KEY_CONTEN_LINEKEY + linekey);
    }

    public boolean removeContentLine(String lineKey, Long contenId) {
        return manager.zrem(KEY_CONTEN_LINEKEY + lineKey, String.valueOf(contenId)) > 0;
    }

    public void setContent(Content content) {
        manager.set(KEY_CONTEN_OBJ + content.getId(), new Gson().toJson(content));

        manager.set(KEY_CONTEN_COMMENTID + content.getCommentId(), new Gson().toJson(content));
    }

    public Content getContent(long contentid) {
        String objStr = manager.get(KEY_CONTEN_OBJ + contentid);
        if (StringUtil.isEmpty(objStr)) {
            return null;
        }
        return new Gson().fromJson(objStr, Content.class);
    }

    public Content getContentByCommentid(String commentId) {
        String objStr = manager.get(KEY_CONTEN_COMMENTID + commentId);
        if (StringUtil.isEmpty(objStr)) {
            return null;
        }
        return new Gson().fromJson(objStr, Content.class);
    }

    public boolean delContent(long contentid) {
        Content content = getContent(contentid);
        if (content == null) {
            return false;
        }
        manager.remove(KEY_CONTEN_COMMENTID + content.getCommentId());
        manager.remove(KEY_CONTEN_OBJ + contentid);
        return true;
    }

    public Set<String> queryContentByLineSortRange(String lineKey, ScoreRange scoreRange) {

        Set<String> stringSet = manager.zrangeByScore(KEY_CONTEN_LINEKEY + lineKey, scoreRange, RedisManager.RANGE_ORDERBY_DESC);

        return stringSet;
    }

    public Set<String> zrangeContentLine(String lineKey) {

        Set<String> stringSet = manager.zrange(KEY_CONTEN_LINEKEY + lineKey, 0L, -1L, RedisManager.RANGE_ORDERBY_DESC);

        return stringSet;
    }


    public boolean delAdvertise(long advertiseId) {
        return manager.remove(KEY_ADVERTISE_OBJ + advertiseId) > 0;
    }

    public void setAdvertise(Advertise advertise) {
        manager.set(KEY_ADVERTISE_OBJ + advertise.getId(), new Gson().toJson(advertise));
    }

    public Advertise getAdvertise(long advertiseId) {
        String objStr = manager.get(KEY_ADVERTISE_OBJ + advertiseId);
        if (StringUtil.isEmpty(objStr)) {
            return null;
        }
        return new Gson().fromJson(objStr, Advertise.class);
    }

    public void addAdvertiseLine(String lineKey, Advertise advertise) {
        manager.zadd(KEY_ADVERTISE_LINE + lineKey, advertise.getDisplayOrder(), String.valueOf(advertise.getId()), -1);
    }

    public boolean removeAdvertiseLine(String lineKey, Long advertiseId) {
        return manager.zrem(KEY_ADVERTISE_LINE + lineKey, String.valueOf(advertiseId)) > 0;
    }

    public Set<String> zrangeAdvertiseLine(String lineKey) {
        return manager.zrangeByScore(KEY_ADVERTISE_LINE + lineKey, Double.MAX_VALUE, 0D, RedisManager.RANGE_ORDERBY_DESC);
    }


    public boolean increaseContentSum(long contentId, ContentSumField field, int value, String profileid) {
        boolean bval = manager.hinccrby(KEY_CONTENSUM_OBJ + contentId, field.getColumn(), value) > 0;

        //文章对应的人的列表
        manager.zadd(KEY_CONTENSUM_LINE + contentId + field.getColumn(), System.currentTimeMillis(), profileid, -1);
        return bval;
    }

    public boolean checkContentSum(long contentId, ContentSumField field, String profileid) {
        Double result = manager.zscore(KEY_CONTENSUM_LINE + contentId + field.getColumn(), profileid);
        if (result == null) {
            return false;
        }
        return true;
    }

    public void setContentSum(long contentId, ContentSumField field, int value) {
        manager.hset(KEY_CONTENSUM_OBJ + contentId, field.getColumn(), String.valueOf(value));
    }

    private int getContentSumValue(long contentId, ContentSumField field) {
        String val = manager.hget(KEY_CONTENSUM_OBJ + contentId, field.getColumn());
        if (StringUtil.isEmpty(val)) {
            return 0;
        }
        return Integer.parseInt(val);
    }

    public ContentSum getContentSumById(long contentId) {
        ContentSum contentSum = new ContentSum();
        contentSum.setId(contentId);
        contentSum.setAgree_num(getContentSumValue(contentId, ContentSumField.AGREE_NUM));
        return contentSum;
    }

    public void setContentSumById(Long contentId, ContentSum contentSum) {
        setContentSum(contentId, ContentSumField.AGREE_NUM, contentSum.getAgree_num());
    }


    ///////////////////////////////////////推荐游戏ID
    public void addRecommendGame(WikiGameres wikiGameres) {
        if (wikiGameres == null) {
            return;
        }
        manager.zadd(KEY_RECOMMEND_GAMES, wikiGameres.getDisplayOrder(), String.valueOf(wikiGameres.getGameId()), -1);
    }

    public boolean remRecommendGame(WikiGameres wikiGameres) {
        return manager.zrem(KEY_RECOMMEND_GAMES, String.valueOf(wikiGameres.getGameId())) > 0;
    }

    public Set<String> queryRecommentGames() {
        return manager.zrange(KEY_RECOMMEND_GAMES, 0, -1, RedisManager.RANGE_ORDERBY_DESC);
    }

    //////=========================================游戏信息
    public void setWikiGameInfo(WikiGameres wikiGameres) {
        manager.set(KEY_WIKI_GAME_INFO + wikiGameres.getGameId(), new Gson().toJson(wikiGameres));
    }

    public WikiGameres getWikiGameres(long gameId) {
        String objStr = manager.get(KEY_WIKI_GAME_INFO + gameId);

        if (StringUtil.isEmpty(objStr)) {
            return null;
        }

        return new Gson().fromJson(objStr, WikiGameres.class);
    }

    public boolean delWikiGameres(long gameId) {
        return manager.remove(KEY_WIKI_GAME_INFO + gameId) > 0;
    }

    ////////=====================用户关注列表

    /**
     * 查询用户是否关注过该游戏
     *
     * @param pid
     * @param gameId
     * @return
     */
    public boolean querFollowIsExis(String pid, String gameId) {
        return manager.zrank(KEY_USER_FOLLOW_GAMES + pid, gameId, RedisManager.RANGE_ORDERBY_DESC) > -1;
    }


    public void addFollowGameIdByPid(String profileId, String gameId, long score) {
        manager.zadd(KEY_USER_FOLLOW_GAMES + profileId, score, gameId, -1);
        incrGameSum(gameId, 1);
    }

    public boolean removeFollowGameId(String profileId, String gameId) {
        boolean bool = manager.zrem(KEY_USER_FOLLOW_GAMES + profileId, gameId) > 0;
        if (bool) {
            incrGameSum(gameId, -1);
        }
        return bool;
    }

    public Set<String> queryFollowGameList(String pid, Pagination page, boolean isDesc) {
//        Set<String> stringSet = manager.zrange(KEY_USER_FOLLOW_GAMES + pid, page.getStartRowIdx(), page.getEndRowIdx(), isDesc ? RedisManager.RANGE_ORDERBY_DESC : RedisManager.RANGE_ORDERBY_ASC);
        Set<String> stringSet = manager.zrange(KEY_USER_FOLLOW_GAMES + pid, 0, -1, isDesc ? RedisManager.RANGE_ORDERBY_DESC : RedisManager.RANGE_ORDERBY_ASC);
        return stringSet;
    }


    //返回人关注的所有游戏
    public Set<String> queryFollowGameListByPid(String pid) {
        Set<String> stringSet = manager.zrange(KEY_USER_FOLLOW_GAMES + pid, 0, -1, RedisManager.RANGE_ORDERBY_DESC);
        return stringSet;
    }

    public long getUserFollowNum(String pid) {
        return manager.zcard(KEY_USER_FOLLOW_GAMES + pid);
    }

    /////////////////用户收藏的列表ID
    public void addUserCollectList(UserCollect userCollect) {
        manager.zadd(KEY_USER_COLLECT_LIST + userCollect.getProfileId(), System.currentTimeMillis(), String.valueOf(userCollect.getId()), -1);
    }

    public boolean removeUserCollectList(String pid, String id) {
        return manager.zrem(KEY_USER_COLLECT_LIST + pid, id) > 0;
    }

    public Set<String> queryUserCollectList(String pid, Pagination page, boolean isDesc) {
        Set<String> stringSet = manager.zrange(KEY_USER_COLLECT_LIST + pid, page.getStartRowIdx(), page.getEndRowIdx(), isDesc ? RedisManager.RANGE_ORDERBY_DESC : RedisManager.RANGE_ORDERBY_ASC);
        return stringSet;
    }


    public long getUserCollectNum(String pid) {
        return manager.zcard(KEY_USER_COLLECT_LIST + pid);
    }


    ///////////////////////////收藏的实体
    public void setUserCollect(UserCollect userCollect) {
        manager.set(KEY_USER_COLLECT + userCollect.getId(), new Gson().toJson(userCollect));
    }

    public UserCollect getUserCollect(long id) {
        String objStr = manager.get(KEY_USER_COLLECT + id);

        if (StringUtil.isEmpty(objStr)) {
            return null;
        }

        return new Gson().fromJson(objStr, UserCollect.class);
    }

    public boolean delUserCollect(long id) {
        return manager.remove(KEY_USER_COLLECT + id) > 0;
    }


    public boolean delContentTag(long tagid) {
        return manager.remove(KEY_CONTENTTAG_OBJ + tagid) > 0;
    }

    public void setContentTag(ContentTag contentTag) {
        manager.set(KEY_CONTENTTAG_OBJ + contentTag.getId(), new Gson().toJson(contentTag));
    }

    public ContentTag getContentTag(long tagid) {
        String objStr = manager.get(KEY_CONTENTTAG_OBJ + tagid);
        if (StringUtil.isEmpty(objStr)) {
            return null;
        }
        return new Gson().fromJson(objStr, ContentTag.class);
    }

    public void addContentTag(ContentTagLine tagLine, ContentTag contentTag) {
        manager.zadd(KEY_CONTENTTAG_LINE + tagLine.getCode(), contentTag.getDisplayOrder(), String.valueOf(contentTag.getId()), -1);
    }

    public boolean removeContentTag(ContentTagLine tagLine, Long tagid) {
        return manager.zrem(KEY_CONTENTTAG_LINE + tagLine.getCode(), String.valueOf(tagid)) > 0;
    }

    public Set<String> zrangeContentTag(ContentTagLine tagLine) {
        return manager.zrangeByScore(KEY_CONTENTTAG_LINE + tagLine.getCode(), Double.MAX_VALUE, 0D, RedisManager.RANGE_ORDERBY_DESC);
    }


    public void setBlackListHisotry(BlackListHistory blackList) {
        manager.setSec(KEY_GET_BLACKLIST_ + blackList.getProfileId(), new Gson().toJson(blackList), (int) (blackList.getEndTime().getTime() - new Date().getTime()) / 1000);
        setKeyQueryBlacklistPid(blackList);
    }


    public BlackListHistory getBlackListHistory(String pid) {
        String objStr = manager.get(KEY_GET_BLACKLIST_ + pid);

        if (StringUtil.isEmpty(objStr)) {
            return null;
        }

        return new Gson().fromJson(objStr, BlackListHistory.class);
    }


    public boolean delBlackListHistory(String pid) {
        delBlackListPid(pid);
        return manager.remove(KEY_GET_BLACKLIST_ + pid) > 0;
    }


    public void setKeyQueryBlacklistPid(BlackListHistory blackList) {
        manager.zadd(KEY_QUERY_BLACKLIST_PID, blackList.getStartTime().getTime(), blackList.getProfileId(), (int) (blackList.getEndTime().getTime() - new Date().getTime()) / 1000);
    }

    public Set<String> queryBlackListPid() {
        Set<String> ids = manager.zrange(KEY_QUERY_BLACKLIST_PID, 0, -1, RedisManager.RANGE_ORDERBY_DESC);
        if (CollectionUtil.isEmpty(ids)) {
            ids = new HashSet<String>();
        }
        return ids;
    }

    public boolean delBlackListPid(String pid) {
        return manager.zrem(KEY_QUERY_BLACKLIST_PID, pid) > 0;
    }


    public int incrGameSum(String gameId, int value) {
        Long num = manager.incr(KEY_GAME_FOLLOW_SUM + gameId, value, 0);
        if (num < 0) {
            manager.set(KEY_GAME_FOLLOW_SUM + gameId, "0");
            return 0;
        }
        return num.intValue();
    }

    public Map<Long, Integer> queryGameFollowSum(Set<Long> gameIds) {
        Map<Long, Integer> returnMap = new HashMap<Long, Integer>();
        if (CollectionUtil.isEmpty(gameIds)) {
            return returnMap;
        }
        for (Long id : gameIds) {
            String value = manager.get(KEY_GAME_FOLLOW_SUM + id);
            if (!StringUtil.isEmpty(value)) {
                returnMap.put(id, Integer.parseInt(value));
            }
        }

        return returnMap;
    }
}

