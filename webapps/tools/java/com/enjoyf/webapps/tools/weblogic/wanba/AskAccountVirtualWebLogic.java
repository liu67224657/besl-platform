package com.enjoyf.webapps.tools.weblogic.wanba;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ask.Answer;
import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.AskUtil;
import com.enjoyf.platform.service.ask.Question;
import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.service.comment.CommentReply;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.comment.CommentUtil;
import com.enjoyf.platform.service.notice.NoticeServiceSngl;
import com.enjoyf.platform.service.notice.NoticeType;
import com.enjoyf.platform.service.notice.UserNotice;
import com.enjoyf.platform.service.notice.wanba.WanbaNoticeBodyType;
import com.enjoyf.platform.service.notice.wanba.WanbaQuestionNoticeBody;
import com.enjoyf.platform.service.notice.wanba.WanbaReplyBody;
import com.enjoyf.platform.service.notice.wanba.WanbaReplyBodyType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.webapps.tools.weblogic.dto.wanba.ReplyMessageListDTO;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhimingli on 2016/11/15 0015.
 */

@Service(value = "askAccountVirtualWebLogic")
public class AskAccountVirtualWebLogic {

    private static String WANBA_ASK_APPKEY = "3iiv7VWfx84pmHgCUqRwun";

    /**
     * 问答通知
     *
     * @param profileId
     * @param appkey
     * @param noticeType
     * @param page
     * @return
     * @throws ServiceException
     */
    public PageRows<UserNotice> queryUserNotice(String profileId, String appkey, NoticeType noticeType, Pagination page) throws ServiceException {

        PageRows<UserNotice> userNoticeList = NoticeServiceSngl.get().queryUserNotice(profileId, appkey, noticeType, page);

        Set<String> profileIdSet = new HashSet<String>();
        Set<Long> questionIdSet = new HashSet<Long>();
        Map<Long, WanbaQuestionNoticeBody> bodyMap = new HashMap<Long, WanbaQuestionNoticeBody>();
        for (UserNotice notice : userNoticeList.getRows()) {
            WanbaQuestionNoticeBody body = WanbaQuestionNoticeBody.fromJson(notice.getBody(), WanbaQuestionNoticeBody.class);
            if (body == null) {
                continue;
            }
            if (!StringUtil.isEmpty(body.getDestProfileId())) {
                profileIdSet.add(body.getDestProfileId());
            }
            if (body.getQuertionId() > 0l) {
                questionIdSet.add(body.getQuertionId());
            }
            bodyMap.put(notice.getUserNoticeId(), body);
        }
        Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileIdSet);
        Map<Long, Question> questionMap = AskServiceSngl.get().queryQuestionByIds(questionIdSet);

        List<UserNotice> returnList = new ArrayList<UserNotice>();
        for (UserNotice notice : userNoticeList.getRows()) {
            WanbaQuestionNoticeBody body = bodyMap.get(notice.getUserNoticeId());
            if (body == null) {
                continue;
            }

            Question question = null;
            if (body.getQuertionId() > 0l) {
                question = questionMap.get(body.getQuertionId());
            }
            if (question == null) {
                continue;
            }

            String text = "";
            if (WanbaNoticeBodyType.QUESTION_ACCEPTANSWER.getCode() == body.getBodyType()) {
                text = "恭喜你在“" + question.getTitle() + "”问题中的答案被采纳为最佳，快去看看吧";
            } else if (WanbaNoticeBodyType.QUESTION_NEWANSWER.getCode() == body.getBodyType()) {
                String nick = "";
                if (!StringUtil.isEmpty(body.getDestProfileId())) {
                    Profile profile = profileMap.get(body.getDestProfileId());
                    if (profile != null) {
                        nick = profile.getNick();
                    }
                }
                text = nick + "已经回答了你的问题“" + question.getTitle() + "”，快去看看吧";
            } else if (WanbaNoticeBodyType.QUESTION_EXPIRE.getCode() == body.getBodyType()) {
                text = "你的问题“" + question.getTitle() + "”已过期，快去看看有没有满意的答案";
            } else if (WanbaNoticeBodyType.QUESTION_FOLLOWQUESIONACCEPT.getCode() == body.getBodyType()) {
                text = "你关注的问题“" + question.getTitle() + "”已经被解决了，快去看看吧";
            } else if (WanbaNoticeBodyType.QUESTION_INVITE_ANSWERQUESTION.getCode() == body.getBodyType()) {
                String nick = "";
                if (!StringUtil.isEmpty(body.getDestProfileId())) {
                    Profile profile = profileMap.get(body.getDestProfileId());
                    if (profile != null) {
                        nick = profile.getNick();
                    }
                }
                text = nick + "邀请你回答问题“" + question.getTitle() + "”，快去看看吧";
            } else if (WanbaNoticeBodyType.QUESTION_ONEONONE_ASK.getCode() == body.getBodyType()) {
                String nick = "";
                if (!StringUtil.isEmpty(body.getDestProfileId())) {
                    Profile profile = profileMap.get(body.getDestProfileId());
                    if (profile != null) {
                        nick = profile.getNick();
                    }
                }
                text = nick + "向你提问“" + question.getTitle() + "”，快去看看吧";
            } else if (WanbaNoticeBodyType.QUESTION_VERIFY.getCode() == body.getBodyType()) {

                text = "大神，《" + body.getExtStr() + "》里有问题“" + question.getTitle() + "”尚未被解决，等待您去抢答。";
            }else {
                text = "未知";
            }

            UserNotice wanbaNoticeDTO = new UserNotice();
            wanbaNoticeDTO.setUserNoticeId(notice.getUserNoticeId());

            wanbaNoticeDTO.setDestId(String.valueOf(question.getQuestionId()));
            wanbaNoticeDTO.setBody(text);
            wanbaNoticeDTO.setCreateTime(notice.getCreateTime());
            returnList.add(wanbaNoticeDTO);
        }
        PageRows<UserNotice> pageRows = new PageRows<UserNotice>();
        pageRows.setPage(userNoticeList.getPage());
        pageRows.setRows(returnList);
        return pageRows;
    }


    /**
     * 回复我的
     *
     * @param pid
     * @param pagination
     * @return
     * @throws ServiceException
     */
    public PageRows<ReplyMessageListDTO> queryWanbaSocialMessageList(String pid, Pagination pagination) throws ServiceException {
        PageRows<UserNotice> userNoticeList = NoticeServiceSngl.get().queryUserNotice(pid, "", NoticeType.REPLY, pagination);

        Set<String> profileIdSet = new HashSet<String>();
        Map<Long, WanbaReplyBody> bodyMap = new HashMap<Long, WanbaReplyBody>();
        for (UserNotice notice : userNoticeList.getRows()) {
            WanbaReplyBody body = WanbaReplyBody.fromJson(notice.getBody());
            if (body == null) {
                continue;
            }
            profileIdSet.add(body.getReplyProfileId());
            profileIdSet.add(notice.getProfileId());
            bodyMap.put(notice.getUserNoticeId(), body);
        }
        Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileIdSet);

        Answer answer = null;
        CommentReply commentReply = null;
        CommentReply parentReply = null;
        List<ReplyMessageListDTO> returnList = new ArrayList<ReplyMessageListDTO>();
        for (UserNotice notice : userNoticeList.getRows()) {
            WanbaReplyBody body = bodyMap.get(notice.getUserNoticeId());
            if (body == null) {
                continue;
            }
            ReplyMessageListDTO dto = new ReplyMessageListDTO();
            dto.setMsgid(notice.getUserNoticeId());
            dto.setAnswid(String.valueOf(body.getAnswerId()));
            dto.setReplyid(body.getReplyId());

            dto.setType(String.valueOf(body.getReplyBodyType().getCode()));
            //查询answer
            answer = AskServiceSngl.get().getAnswer(body.getAnswerId());


            if (body.getReplyBodyType().equals(WanbaReplyBodyType.REPLY_ANSWER)) {
                if (answer == null) {
                    dto.setReplydesc("该答案已删除");
                } else {
                    dto.setReplydesc(body.getParentDesc());
                }
            } else {
                commentReply = CommentServiceSngl.get().getCommentReplyById(CommentUtil.genCommentId(String.valueOf(body.getAnswerId()), CommentDomain.WAN_ASK_COMMENT),
                        Long.valueOf(body.getParentIdreplyId()));

                if (commentReply == null) {
                    dto.setReplydesc("该评论已删除");
                } else {
                    dto.setReplydesc(body.getParentDesc());
                }
            }
            dto.setDesc(body.getReplyDesc());
            dto.setDate(notice.getCreateTime());
            dto.setProfile(profileMap.get(body.getReplyProfileId()));
            returnList.add(dto);
        }

        PageRows<ReplyMessageListDTO> pageRows = new PageRows<ReplyMessageListDTO>();
        pageRows.setPage(userNoticeList.getPage());
        pageRows.setRows(returnList);

        return pageRows;
    }

    /**
     * 评论接口
     *
     * @param answerid
     * @param body
     * @param pid
     * @param parentid
     * @return
     */
    public String postComment(String answerid, String body, String pid, Integer parentid, String parentnick) {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://api." + WebappConfig.get().getDomain() + "/wanba/api/comment/post", new HttpParameter[]{
                new HttpParameter("unikey", answerid),
                new HttpParameter("pid", pid),
                new HttpParameter("appkey", WANBA_ASK_APPKEY),
                new HttpParameter("parentid", parentid),
                new HttpParameter("parentnick", parentnick),
                new HttpParameter("body", body)
        }, null);
        return httpResult.getResult();
    }


    /**
     * 限时提问
     *
     * @return
     */
    public String postTimeQuestion(String title, String text, String askpid, String timelimit, String tagid) {
        boolean bval = checkContent(title);
        if (bval) {
            return ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getJsonString();
        }

        bval = checkContent(text);
        if (bval) {
            return ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getJsonString();
        }

        return AskUtil.postTimeQuestion(title, text, askpid, timelimit, tagid);
    }

    /**
     * 一对一提问
     *
     * @return
     */
    public String postOneOnOneQuestion(String title, String text, String pid, String invitepid) {

        boolean bval = checkContent(title);
        if (bval) {
            return ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getJsonString();
        }

        bval = checkContent(text);
        if (bval) {
            return ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getJsonString();
        }

        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://api." + WebappConfig.get().getDomain() + "/wanba/api/ask/question/post/invite", new HttpParameter[]{
                new HttpParameter("title", title),
                new HttpParameter("text", AskUtil.textReplace(text)),
                new HttpParameter("pid", pid),
                new HttpParameter("invite", invitepid),
                new HttpParameter("appkey", WANBA_ASK_APPKEY)

        }, null);
        return httpResult.getResult();
    }


    /**
     * 虚拟用户回答
     *
     * @return
     */
    public String answerpost(String text, String pid, String questionId) {

        boolean bval = checkContent(text);
        if (bval) {
            return ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getJsonString();
        }

        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://api." + WebappConfig.get().getDomain() + "/wanba/api/ask/answer/post", new HttpParameter[]{
                new HttpParameter("text", AskUtil.textReplace(text)),
                new HttpParameter("pid", pid),
                new HttpParameter("appkey", WANBA_ASK_APPKEY),
                new HttpParameter("qid", questionId)
        }, null);
        return httpResult.getResult();
    }


    /**
     * 采纳答案
     *
     * @param questionId
     * @param answerid
     * @param questionPid
     * @return
     */
    public String accept(String questionId, String answerid, String questionPid) {
        HttpClientManager httpClient = new HttpClientManager();
        return httpClient.post("http://api." + WebappConfig.get().getDomain() + "/wanba/api/ask/answer/accept", new HttpParameter[]{
                new HttpParameter("pid", questionPid),
                new HttpParameter("qid", questionId),
                new HttpParameter("appkey", WANBA_ASK_APPKEY),
                new HttpParameter("aid", answerid)
        }, null).getResult();
    }


    //true表示有敏感词
    private boolean checkContent(String content) {
        //敏感词
        String illegeText = illegeText(content);
        Set<String> simpleKeyword = ContextFilterUtils.getSimpleEditorBlackList(illegeText);
        Set<String> postKeyword = ContextFilterUtils.getPostContainBlackList(illegeText);
        if (!CollectionUtil.isEmpty(simpleKeyword) || !CollectionUtil.isEmpty(postKeyword)) {
            return true;
        }
        return false;
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

    public String reactivated(String pid, String questionId) {
        HttpClientManager httpClient = new HttpClientManager();
        return httpClient.post("http://api." + WebappConfig.get().getDomain() + "/wanba/webview/ask/reactivated", new HttpParameter[]{
                new HttpParameter("pid", pid),
                new HttpParameter("qid", questionId),
                new HttpParameter("appkey", WANBA_ASK_APPKEY),
        }, null).getResult();
    }
}
