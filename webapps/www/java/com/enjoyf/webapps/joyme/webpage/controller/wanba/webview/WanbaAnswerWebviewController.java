package com.enjoyf.webapps.joyme.webpage.controller.wanba.webview;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaProfileDTO;
import com.enjoyf.webapps.joyme.weblogic.wanba.WanbaAskWebLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhimingli on 2016/10/20 0020.
 */

@Controller
@RequestMapping("/wanba/webview/answer")
public class WanbaAnswerWebviewController {

    @Resource
    private WanbaAskWebLogic wanbaAskWebLogic;

    @RequestMapping(value = "/detail")
    public ModelAndView detail(HttpServletRequest request,
                               @RequestParam(value = "aid", required = false) String answerid) {
        if (StringUtil.isEmpty(answerid)) {
            return new ModelAndView("/views/jsp/common/custompage");
        }

        String pid = HTTPUtil.getParam(request, "pid");
        if (StringUtil.isEmpty(pid)) {
            return new ModelAndView("/views/jsp/common/custompage");
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Answer answer = null;
        Question question = null;
        AnswerSum answersum = null;
        try {
            answer = AskServiceSngl.get().getAnswer(Long.valueOf(answerid));
            if (answer == null) {
                return new ModelAndView("/views/jsp/common/custompage");
            }

            question = AskServiceSngl.get().getQuestion(answer.getQuestionId());
            if (question == null) {
                return new ModelAndView("/views/jsp/common/custompage");
            }


            String loginDomain = HTTPUtil.getParam(request, "logindomain");
            resultMap.put("logindomain", loginDomain);

            List<Long> list = new ArrayList<Long>();
            list.add(answer.getAnswerId());
            Map<Long, AnswerSum> answerSumMap = AskServiceSngl.get().queryAnswerSumByAids(list);

            if (!CollectionUtil.isEmpty(answerSumMap)) {
                answersum = answerSumMap.get(answer.getAnswerId());
            }


            //查找答案的profile
            VerifyProfile wanbaProfile = UserCenterServiceSngl.get().getVerifyProfileById(answer.getAnswerProfileId());
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(answer.getAnswerProfileId());
            WanbaProfileDTO wanbaProfileDTO = wanbaAskWebLogic.wanbaProfileDTO(profile, wanbaProfile);
            resultMap.put("wanbaProfileDTO", wanbaProfileDTO);

            //判断当前用户是否点赞
            String askUserActionId = AskUtil.getAskUserActionId(pid, Long.valueOf(answerid), ItemType.ANSWER, AskUserActionType.AGREEMENT);
            AskUserAction askUserAction = AskServiceSngl.get().getAskUserAction(askUserActionId);
            resultMap.put("askUserAction", askUserAction);


            //判断答案是否收藏
            String favoriteActionId = AskUtil.getAskUserActionId(pid, Long.valueOf(answerid), ItemType.ANSWER, AskUserActionType.FAVORITE);
            AskUserAction favoriteAskUserAction = AskServiceSngl.get().getAskUserAction(favoriteActionId);
            resultMap.put("favoriteAskUserAction", favoriteAskUserAction != null ? "yes" : "no");

            resultMap.put("question", question);

            //answer.setRichText(AskUtil.getHtmlRichBody(answer.getRichText()));
            resultMap.put("pid", pid);
            answer.setRichText(AskUtil.getHtmlRichBody(answer.getRichText()));
            resultMap.put("answer", answer);
            resultMap.put("answersum", answersum);
            resultMap.put("shareurl", "http://api." + WebappConfig.get().getDomain() + "/wanba/webview/answer/sharedetail?aid=" + answerid);


            StringBuffer str = new StringBuffer();
            if (answer.getAskVoice() != null && !StringUtil.isEmpty(answer.getAskVoice().getUrl())) {
                str.append("[语音]");
            }
            if (answer.getBody() != null && !StringUtil.isEmpty(answer.getBody().getText())) {
                str.append(answer.getBody().getText());
            }
            resultMap.put("sharedesc", str.toString());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return new ModelAndView("/views/jsp/common/custompage");
        }
        return new ModelAndView("/views/jsp/wanba/answer/detail", resultMap);
    }


    @RequestMapping(value = "/sharedetail")
    public ModelAndView sharedetail(HttpServletRequest request,
                                    @RequestParam(value = "aid", required = false) String answerid) {
        if (StringUtil.isEmpty(answerid)) {
            return new ModelAndView("/views/jsp/common/custompage");
        }


        Map<String, Object> resultMap = new HashMap<String, Object>();
        Answer answer = null;
        Question question = null;
        AnswerSum answersum = null;
        try {
            answer = AskServiceSngl.get().getAnswer(Long.valueOf(answerid));
            if (answer == null) {
                return new ModelAndView("/views/jsp/common/custompage");
            }

            question = AskServiceSngl.get().getQuestion(answer.getQuestionId());
            if (question == null) {
                return new ModelAndView("/views/jsp/common/custompage");
            }


            VerifyProfile wanbaProfile = UserCenterServiceSngl.get().getVerifyProfileById(answer.getAnswerProfileId());
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(answer.getAnswerProfileId());

            WanbaProfileDTO wanbaProfileDTO = wanbaAskWebLogic.wanbaProfileDTO(profile, wanbaProfile);
            resultMap.put("wanbaProfileDTO", wanbaProfileDTO);


            List<Long> list = new ArrayList<Long>();
            list.add(answer.getAnswerId());
            Map<Long, AnswerSum> answerSumMap = AskServiceSngl.get().queryAnswerSumByAids(list);

            if (!CollectionUtil.isEmpty(answerSumMap)) {
                answersum = answerSumMap.get(answer.getAnswerId());
            }
            answer.setRichText(AskUtil.getHtmlRichBody(answer.getRichText()));
            resultMap.put("question", question);
            resultMap.put("answer", answer);
            resultMap.put("answersum", answersum);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return new ModelAndView("/views/jsp/common/custompage");
        }
        return new ModelAndView("/views/jsp/wanba/answer/sharedetail", resultMap);
    }
}
