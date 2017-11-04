package com.enjoyf.webapps.joyme.weblogic.wanba;

import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.Question;
import com.enjoyf.platform.service.ask.WanbaJt;
import com.enjoyf.platform.service.notice.*;
import com.enjoyf.platform.service.notice.wanba.WanbaNoticeBodyType;
import com.enjoyf.platform.service.notice.wanba.WanbaQuestionNoticeBody;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaNoticeDTO;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaNoticeSumDTO;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/22
 */
@Service
public class WanbaNoticeWebLogic {

    private static Set<NoticeType> types = new HashSet<NoticeType>();

    static {
        types.add(NoticeType.ANSWER);
        types.add(NoticeType.REPLY);
    }

    public List<WanbaNoticeSumDTO> queryAppSumByProfile(String profileId, String version, String platfrom, String appKey) throws ServiceException {
        Map<String, AppNoticeSum> appSumMap = NoticeServiceSngl.get().queryAppNoticeSum(profileId, "", types);

        AppNoticeSum systemNoticeSum = NoticeServiceSngl.get().querySystemNoticeSum(profileId, version, platfrom, appKey);
        List<WanbaNoticeSumDTO> returnList = new ArrayList<WanbaNoticeSumDTO>();

        for (AppNoticeSum sum : appSumMap.values()) {
            returnList.add(buildNoticeSumDTO(sum));
        }
        if (systemNoticeSum != null) {
            returnList.add(buildNoticeSumDTO(systemNoticeSum));
        }
        return returnList;
    }

    public PageRows<WanbaNoticeDTO> queryQuestionNotice(String profileId, String appkey, NoticeType noticeType, Pagination page) throws ServiceException {

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

        List<WanbaNoticeDTO> returnList = new ArrayList<WanbaNoticeDTO>();
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
            } else {
                text = "未知";
            }

            WanbaNoticeDTO wanbaNoticeDTO = new WanbaNoticeDTO();
            wanbaNoticeDTO.setNoticeid(notice.getUserNoticeId());

            wanbaNoticeDTO.setJi(String.valueOf(question.getQuestionId()));
            wanbaNoticeDTO.setJt(String.valueOf(WanbaJt.ASK_QUESTION_PAGE.getCode()));
            wanbaNoticeDTO.setText(text);
            wanbaNoticeDTO.setTime(String.valueOf(notice.getCreateTime().getTime()));
            wanbaNoticeDTO.setTitle("");
            returnList.add(wanbaNoticeDTO);
        }

        PageRows<WanbaNoticeDTO> pageRows = new PageRows<WanbaNoticeDTO>();
        pageRows.setPage(userNoticeList.getPage());
        pageRows.setRows(returnList);

        return pageRows;
    }

    public PageRows<WanbaNoticeDTO> querySystemNotice(String appkey, String version, String platform, Pagination page) throws ServiceException {

        PageRows<SystemNotice> systemNoitceList = NoticeServiceSngl.get().querySystemNoitce(appkey, version, platform, page);

        List<WanbaNoticeDTO> returnList = new ArrayList<WanbaNoticeDTO>();
        for (SystemNotice notice : systemNoitceList.getRows()) {

            WanbaNoticeDTO wanbaNoticeDTO = new WanbaNoticeDTO();
            wanbaNoticeDTO.setNoticeid(notice.getSystemNoticeId());
            wanbaNoticeDTO.setJi(notice.getJi());//todo wanba
            wanbaNoticeDTO.setJt(notice.getJt());
            wanbaNoticeDTO.setText(notice.getText());
            wanbaNoticeDTO.setTitle(notice.getTitle());
            wanbaNoticeDTO.setTime(String.valueOf(notice.getCreateTime().getTime()));
            returnList.add(wanbaNoticeDTO);
        }


        PageRows<WanbaNoticeDTO> pageRows = new PageRows<WanbaNoticeDTO>();
        pageRows.setPage(systemNoitceList.getPage());
        pageRows.setRows(returnList);

        return pageRows;
    }


    private WanbaNoticeSumDTO buildNoticeSumDTO(AppNoticeSum sum) {
        WanbaNoticeSumDTO dto = new WanbaNoticeSumDTO();
        dto.setDtype(sum.getDtype());
        dto.setType(sum.getType());
        dto.setValue(sum.getValue());
        return dto;
    }

}

