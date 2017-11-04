package com.enjoyf.webapps.joyme.weblogic.wanba;

import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.html.tag.ImageURLTag;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.webapps.joyme.dto.Wanba.*;
import com.enjoyf.webapps.joyme.webpage.util.DateTag;
import net.sf.json.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/14
 */
public abstract class AbstractWanbaWebLogic {

    //isIgnoreAnswer 为true表示答案不存在的时候，此条数据不返回
    //false表示不忽略===答案被删也会返回
    protected List<QuestionDTO> queryQuetionDtoListByQuestionList(Collection<Question> queryQuestionList, String queryProfileId, boolean isIgnoreAnswer) throws ServiceException {
        Set<Long> answerIds = new HashSet<Long>();
        Set<String> profileIds = new HashSet<String>();
        Set<Long> questionIds = new HashSet<Long>();
        Set<Long> tagidS = new HashSet<Long>();
        for (Question question : queryQuestionList) {
            questionIds.add(question.getQuestionId());
            if (question.getAcceptAnswerId() > 0l) {
                answerIds.add(question.getAcceptAnswerId());
            }

            //邀请人的profileid
            if (!StringUtil.isEmpty(question.getInviteProfileId())) {
                profileIds.add(question.getInviteProfileId());
            }

            //游戏标签
            if (question.getGameId() > 0) {
                tagidS.add(question.getGameId());
            }

            profileIds.add(question.getAskProfileId());
        }


        Map<Long, Answer> answerMap = new HashMap<Long, Answer>();
        if (!CollectionUtil.isEmpty(answerIds)) {
            answerMap.putAll(AskServiceSngl.get().queryAnswerByAnswerIds(answerIds));
        }
        for (Answer answer : answerMap.values()) {
            profileIds.add(answer.getAnswerProfileId());
        }
        Map<Long, AnswerSum> answerSumMap = AskServiceSngl.get().queryAnswerSumByAids(answerIds);
        Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileIds);
        Map<String, VerifyProfile> wanbaProfileMap = UserCenterServiceSngl.get().queryProfileByIds(profileIds);
        Map<Long, QuestionSum> questionSumMap = AskServiceSngl.get().queryQuestionSumByQids(questionIds);
        Map<Long, AnimeTag> gameAnimeTagMap = JoymeAppServiceSngl.get().queryAnimetags(tagidS);
        Set<Long> followQuesitonIdSet = new HashSet<Long>();
        if (!StringUtil.isEmpty(queryProfileId) && !CollectionUtil.isEmpty(questionIds)) {
            followQuesitonIdSet = AskServiceSngl.get().checkFollowQuestion(questionIds, queryProfileId);
        }

        Map<Long, AskUserAction> agreeAskUserActionMap = AskServiceSngl.get().checkAgreeAskUserAction(answerIds, queryProfileId);


        List<QuestionDTO> returnList = new ArrayList<QuestionDTO>();
        for (Question question : queryQuestionList) {

            QuestionDTO dto = buildDTO(question, questionSumMap, answerMap, answerSumMap, profileMap, wanbaProfileMap,
                    gameAnimeTagMap, followQuesitonIdSet, agreeAskUserActionMap, isIgnoreAnswer);
            if (dto != null) {
                returnList.add(dto);
            }
        }
        return returnList;
    }

    protected List<QuestionDTO> queryQuetionDtoListByAnswerList(List<Answer> answersList, String queryProfileId) throws ServiceException {
        Set<Long> answerIds = new HashSet<Long>();
        Set<String> profileIds = new HashSet<String>();
        Set<Long> questionIds = new HashSet<Long>();
        Set<Long> tagidS = new HashSet<Long>();

        if (CollectionUtil.isEmpty(answersList)) {
            return Collections.emptyList();
        }

        Map<Long, Answer> answerMap = new HashMap<Long, Answer>();
        for (Answer answer : answersList) {
            answerMap.put(answer.getAnswerId(), answer);
            questionIds.add(answer.getQuestionId());
            answerIds.add(answer.getAnswerId());
        }

        Map<Long, Question> queryQuestionMap = AskServiceSngl.get().queryQuestionByIds(questionIds);
        for (Question question : queryQuestionMap.values()) {
            questionIds.add(question.getQuestionId());
            //邀请人的profileid
            if (!StringUtil.isEmpty(question.getInviteProfileId())) {
                profileIds.add(question.getInviteProfileId());
            }

            //游戏标签
            if (question.getGameId() > 0) {
                tagidS.add(question.getGameId());
            }

            profileIds.add(question.getAskProfileId());
        }

        for (Answer answer : answerMap.values()) {
            profileIds.add(answer.getAnswerProfileId());
        }
        Map<Long, AnswerSum> answerSumMap = AskServiceSngl.get().queryAnswerSumByAids(answerIds);
        Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileIds);
        Map<String, VerifyProfile> wanbaProfileMap = UserCenterServiceSngl.get().queryProfileByIds(profileIds);
        Map<Long, QuestionSum> questionSumMap = AskServiceSngl.get().queryQuestionSumByQids(questionIds);
        Map<Long, AnimeTag> gameAnimeTagMap = JoymeAppServiceSngl.get().queryAnimetags(tagidS);
        Set<Long> followQuesitonIdSet = new HashSet<Long>();
        if (!StringUtil.isEmpty(queryProfileId) && !CollectionUtil.isEmpty(questionIds)) {
            followQuesitonIdSet = AskServiceSngl.get().checkFollowQuestion(questionIds, queryProfileId);
        }

        Map<Long, AskUserAction> agreeAskUserActionMap = AskServiceSngl.get().checkAgreeAskUserAction(answerIds, queryProfileId);

        List<QuestionDTO> returnList = new ArrayList<QuestionDTO>();
        for (Answer answer : answersList) {
            Question question = queryQuestionMap.get(answer.getQuestionId());
            if (question == null || answer == null) {
                continue;
            }
            QuestionDTO dto = buildDTOByAnswer(question, questionSumMap, answer, answerSumMap, profileMap, wanbaProfileMap,
                    gameAnimeTagMap, followQuesitonIdSet, agreeAskUserActionMap);
            if (dto != null) {
                returnList.add(dto);
            }
        }
        return returnList;
    }


    protected QuestionDTO buildDTO(Question question, Map<Long, QuestionSum> quesionSumMap, Map<Long, Answer> answerMap, Map<Long, AnswerSum> answerSumMap,
                                   Map<String, Profile> profileMap, Map<String, VerifyProfile> wanbaProfileMap,
                                   Map<Long, AnimeTag> gameAnimeTagMap, Set<Long> followQuesitonIdSet,
                                   Map<Long, AskUserAction> agreeAskUserActionMap, boolean isIgnoreAnswer) {
        List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();
        Profile qProfile = profileMap.get(question.getAskProfileId());
        VerifyProfile wanbaProfile = wanbaProfileMap.get(question.getAskProfileId());
        if (qProfile == null) {
            return null;
        }
        QuestionSum questionSum = quesionSumMap.get(question.getQuestionId());
        QuestionDTO dto = new QuestionDTO();
        dto.setQuestion(questionToDetailDTO(question, questionSum, followQuesitonIdSet));
        dto.setQuestionprofile(wanbaProfileDTO(qProfile, wanbaProfile));

        AnimeTag animeTag = gameAnimeTagMap.get(question.getGameId());
        dto.setGametag(questionToGameTagDTO(question, animeTag));

        questionDTOList.add(dto);

        //oneonone inviteprofile和answerid
        if (QuestionType.ONEONONE.equals(question.getType())) {
            Answer answer = answerMap.get(question.getAcceptAnswerId());
            if (answer == null) {
                if (!isIgnoreAnswer) {
                    return dto;
                }
                return null;
            }
            //
            if (question.getAcceptAnswerId() > 0l) {

                Profile answerProfile = profileMap.get(answer.getAnswerProfileId());
                VerifyProfile answerWanbaProfile = wanbaProfileMap.get(answer.getAnswerProfileId());
                if (answerProfile != null) {
                    AnswerSum answerSum = answerSumMap.get(answer.getAnswerId());
                    dto.setAnswer(answerToDetailDTO(answer, answerSum, agreeAskUserActionMap));
                    dto.setAnswerprofile(wanbaProfileDTO(answerProfile, answerWanbaProfile));
                }
            } else {
                if (!StringUtil.isEmpty(question.getInviteProfileId())) {
                    Profile inviteProfile = profileMap.get(question.getInviteProfileId());
                    VerifyProfile inviteWanbaProfile = wanbaProfileMap.get(question.getInviteProfileId());
                    if (inviteProfile != null) {
                        dto.setAnswerprofile(wanbaProfileDTO(inviteProfile, inviteWanbaProfile));
                    }
                }
            }
        } else {
            // timelimit
            if (question.getAcceptAnswerId() > 0l) {
                Answer answer = answerMap.get(question.getAcceptAnswerId());
                if (answer == null) {
                    if (!isIgnoreAnswer) {
                        return dto;
                    }
                    return null;
                }
                Profile answerProfile = profileMap.get(answer.getAnswerProfileId());
                VerifyProfile answerWanbaProfile = wanbaProfileMap.get(answer.getAnswerProfileId());
                if (answerProfile != null) {
                    AnswerSum answerSum = answerSumMap.get(answer.getAnswerId());
                    dto.setAnswer(answerToDetailDTO(answer, answerSum, agreeAskUserActionMap));
                    dto.setAnswerprofile(wanbaProfileDTO(answerProfile, answerWanbaProfile));
                }
            }
        }


        return dto;
    }

    protected QuestionDTO buildDTOByAnswer(Question question, Map<Long, QuestionSum> quesionSumMap, Answer answer, Map<Long, AnswerSum> answerSumMap,
                                           Map<String, Profile> profileMap, Map<String, VerifyProfile> wanbaProfileMap,
                                           Map<Long, AnimeTag> gameAnimeTagMap, Set<Long> followQuesitonIdSet,
                                           Map<Long, AskUserAction> agreeAskUserActionMap) {
        List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();
        Profile qProfile = profileMap.get(question.getAskProfileId());
        VerifyProfile wanbaProfile = wanbaProfileMap.get(question.getAskProfileId());
        if (qProfile == null) {
            return null;
        }
        QuestionSum questionSum = quesionSumMap.get(question.getQuestionId());
        QuestionDTO dto = new QuestionDTO();
        dto.setQuestion(questionToDetailDTO(question, questionSum, followQuesitonIdSet));
        dto.setQuestionprofile(wanbaProfileDTO(qProfile, wanbaProfile));

        AnimeTag animeTag = gameAnimeTagMap.get(question.getGameId());
        dto.setGametag(questionToGameTagDTO(question, animeTag));

        questionDTOList.add(dto);

        //oneonone inviteprofile和answerid
        if (QuestionType.ONEONONE.equals(question.getType())) {
            //
            if (question.getAcceptAnswerId() > 0l) {

                Profile answerProfile = profileMap.get(answer.getAnswerProfileId());
                VerifyProfile answerWanbaProfile = wanbaProfileMap.get(answer.getAnswerProfileId());

                if (answer != null && answerProfile != null) {
                    AnswerSum answerSum = answerSumMap.get(answer.getAnswerId());
                    dto.setAnswer(answerToDetailDTO(answer, answerSum, agreeAskUserActionMap));
                    dto.setAnswerprofile(wanbaProfileDTO(answerProfile, answerWanbaProfile));
                }
            } else {
                if (!StringUtil.isEmpty(question.getInviteProfileId())) {
                    Profile inviteProfile = profileMap.get(question.getInviteProfileId());
                    VerifyProfile inviteWanbaProfile = wanbaProfileMap.get(question.getInviteProfileId());
                    if (inviteProfile != null) {
                        dto.setAnswerprofile(wanbaProfileDTO(inviteProfile, inviteWanbaProfile));
                    }
                }
            }
        } else {
            // timelimit
            Profile answerProfile = profileMap.get(answer.getAnswerProfileId());
            VerifyProfile answerWanbaProfile = wanbaProfileMap.get(answer.getAnswerProfileId());
            if (answer != null && answerProfile != null) {
                AnswerSum answerSum = answerSumMap.get(answer.getAnswerId());
                dto.setAnswer(answerToDetailDTO(answer, answerSum, agreeAskUserActionMap));
                dto.setAnswerprofile(wanbaProfileDTO(answerProfile, answerWanbaProfile));
            }
        }


        return dto;
    }


    public WanbaProfileDTO wanbaProfileDTO(Profile profile, VerifyProfile wanbaProfile) {
        WanbaProfileDTO profileDTO = new WanbaProfileDTO();
        profileDTO.setDesc(profile.getDescription());
        profileDTO.setNick(profile.getNick());
        profileDTO.setPid(profile.getProfileId());
        profileDTO.setUid(profile.getUid());
        profileDTO.setSex(profile.getSex() == null ? "" : profile.getSex());
        //默认头像
        profileDTO.setIcon(ImageURLTag.parseUserCenterHeadIcon(profile.getIcon(), profile.getSex(), "m", true));
        if (wanbaProfile != null) {
            profileDTO.setPoint(wanbaProfile.getAskPoint());
            profileDTO.setVtype(wanbaProfile.getVerifyType());
            profileDTO.setVdesc(wanbaProfile.getDescription());
            profileDTO.setVtitle(StringUtil.isEmpty(wanbaProfile.getVerifyTitle()) ? "" : wanbaProfile.getVerifyTitle());
            //wanbaProfile.getVerifyType()
        }
        return profileDTO;
    }

    protected QuestionDetailDTO questionToDetailDTO(Question question, QuestionSum questionSum, Set<Long> followQuesitonIdSet) {
        QuestionDetailDTO detailDTO = new QuestionDetailDTO();
        detailDTO.setTitle(StringUtil.isEmpty(question.getTitle()) ? "" : question.getTitle());
        detailDTO.setBody(question.getBody());
        //detailDTO.setGameid(question.getGameId());
        detailDTO.setQuestionid(question.getQuestionId());
        detailDTO.setQuestiontime(question.getCreateTime().getTime());
        detailDTO.setTimelimit(question.getTimeLimit());
        detailDTO.setAccepaid(question.getAcceptAnswerId());
        detailDTO.setTagid(question.getGameId());
        detailDTO.setQtype(question.getType().getCode());
        detailDTO.setFisrtaid(question.getFirstAnswerId());
        detailDTO.setVoice(question.getAskVoice());
        detailDTO.setReactivated(question.getReactivated());
        if (questionSum != null) {
            detailDTO.setAnswersum(questionSum.getAnsewerSum());
            detailDTO.setViewsum(questionSum.getViewSum());
            detailDTO.setFollowsum(questionSum.getFollowSum());
        }
        detailDTO.setFollowstatus(followQuesitonIdSet.contains(question.getQuestionId()) ? QuestionDetailDTO.DTO_QUESTION_FOLLOW_STATUS_FOLLOW : QuestionDetailDTO.DTO_QUESTION_FOLLOW_STATUS_UNFOLLOW);
        detailDTO.setPoint(question.getQuestionPoint());
        detailDTO.setQuestionstatus(question.getQuestionStatus().getCode());
        detailDTO.setAskprofileid(StringUtil.isEmpty(question.getAskProfileId()) ? "" : question.getAskProfileId());
        detailDTO.setInviteprofileid(StringUtil.isEmpty(question.getInviteProfileId()) ? "" : question.getInviteProfileId());
        detailDTO.setRichbody(AskUtil.getHtmlRichBody(question.getRichText()));
        return detailDTO;
    }

    protected AnswerDetailDTO answerToDetailDTO(Answer answer, AnswerSum answerSum, Map<Long, AskUserAction> agreeAskUserActionMap) {
        AnswerDetailDTO detailDTO = new AnswerDetailDTO();
        detailDTO.setBody(answer.getBody());
        detailDTO.setAnswertime(answer.getAnswerTime().getTime());
        detailDTO.setAnswerid(answer.getAnswerId());
        detailDTO.setVoice(answer.getAskVoice());
        detailDTO.setRichbody(AskUtil.getHtmlRichBody(answer.getRichText()));
        detailDTO.setProfileid(StringUtil.isEmpty(answer.getAnswerProfileId()) ? "" : answer.getAnswerProfileId());
        detailDTO.setAnswertimeString(DateTag.parseWanbaDate(answer.getAnswerTime().getTime()));
        if (answerSum != null) {
            detailDTO.setAgreesum(answerSum.getAgreeSum());
            detailDTO.setReplysum(answerSum.getReplySum());
            detailDTO.setViewsum(answerSum.getViewSum());
        }
        if (!CollectionUtil.isEmpty(agreeAskUserActionMap)) {
            detailDTO.setAgreestatus(agreeAskUserActionMap.containsKey(answer.getAnswerId()) ? AnswerDetailDTO.DTO_ANSWER_AGREE_STATUS_YES : AnswerDetailDTO.DTO_ANSWER_AGREE_STATUS_NO);
        }
        return detailDTO;
    }


    protected GameTagDTO questionToGameTagDTO(Question question, AnimeTag animeTag) {
        GameTagDTO gameTagDTO = new GameTagDTO();
        gameTagDTO.setTagid(String.valueOf(question.getGameId()));
        gameTagDTO.setTagname(animeTag == null ? "" : animeTag.getTag_name());
        return gameTagDTO;
    }


    //敏感词验证
    public String checkContent(String content) {
        String illegeText = illegeText(content);
        Set<String> simpleKeyword = ContextFilterUtils.getSimpleEditorBlackList(illegeText);
        Set<String> postKeyword = ContextFilterUtils.getPostContainBlackList(illegeText);
        if (!CollectionUtil.isEmpty(simpleKeyword) || !CollectionUtil.isEmpty(postKeyword)) {
            JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getCode()));
            jsonObject.put("msg", ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getMsg());

            Map<String, Set<String>> map = new HashMap<String, Set<String>>();

            Set<String> keyword = new HashSet<String>();
            keyword.addAll(simpleKeyword);
            keyword.addAll(postKeyword);
            map.put("rows", keyword);

            jsonObject.put("result", map);
            return jsonObject.toString();
        }
        return null;
    }


    //内容中的img标签不判断敏感词
    private String illegeText(String body) {
        String illegeText = body;
        String regex = "<(img|IMG)[^>]*>";
        Pattern imgPattern = Pattern.compile(regex);
        Matcher matcher = imgPattern.matcher(illegeText);
        while (matcher.find()) {
            String img = matcher.group(0);
            illegeText = illegeText.replace(img, "");
        }
        return illegeText;
    }

}
