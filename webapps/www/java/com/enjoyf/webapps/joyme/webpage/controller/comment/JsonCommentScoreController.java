package com.enjoyf.webapps.joyme.webpage.controller.comment;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.UserCenterUtil;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.service.comment.CommentBeanField;
import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.util.CookieUtil;
import com.enjoyf.webapps.joyme.dto.comment.CommentJsonParam;
import com.enjoyf.webapps.joyme.weblogic.comment.AllowCommentStatus;
import com.enjoyf.webapps.joyme.weblogic.comment.CommentFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-10
 * Time: 下午5:20
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/jsoncomment/score")
public class JsonCommentScoreController extends AbstractCommentController {

    @ResponseBody
    @RequestMapping("/query")
    public String query(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "unikey", required = false) String uniKey,
                        @RequestParam(value = "domain", required = false) String domain,
                        @RequestParam(value = "jsonparam", required = false) String jsonParam,
                        @RequestParam(value = "pnum", required = false, defaultValue = "1") String pNum,
                        @RequestParam(value = "psize", required = false, defaultValue = "100") String pSize

    ) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        binder.setDateFormat("yyyy-MM-dd");
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultCodeConstants.SUCCESS.getCode());
        if (!StringUtil.isEmpty(uniKey)) {
            if (uniKey.indexOf("?") >= 0) {
                uniKey = uniKey.substring(0, uniKey.indexOf("?"));
            }
        }
        try {
            if (StringUtil.isEmpty(uniKey)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getMsg());
                return "commentscorelistcallback([" + binder.toJson(resultObjectMsg) + "])";
            }

            if (StringUtil.isEmpty(domain)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getMsg());
                return "commentscorelistcallback([" + binder.toJson(resultObjectMsg) + "])";
            }

            CommentDomain commentDomain = CommentDomain.getByCode(Integer.valueOf(domain));
            int pageNum = Integer.valueOf(pNum);
            int pageSize = Integer.valueOf(pSize);

            resultObjectMsg = CommentFactory.get().factory(commentDomain).buildResultObjectMsg(resultObjectMsg, uniKey, commentDomain, jsonParam, pageNum, pageSize, 0l, true);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception:", e);
            resultObjectMsg.setRs(ResultCodeConstants.ERROR.getCode());
            resultObjectMsg.setMsg(ResultCodeConstants.ERROR.getMsg());
            return "commentscorelistcallback([" + binder.toJson(resultObjectMsg) + "])";
        }
        return "commentscorelistcallback([" + binder.toJson(resultObjectMsg) + "])";
    }

    @ResponseBody
    @RequestMapping("/post")
    public String post(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "unikey", required = false) String uniKey,
                       @RequestParam(value = "appkey", required = false, defaultValue = "default") String appkey,
                       @RequestParam(value = "domain", required = false) String domain,
                       @RequestParam(value = "score", required = false) String score,
                       @RequestParam(value = "jsonparam", required = false) String jsonParam

    ) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        binder.setDateFormat("yyyy-MM-dd");
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultCodeConstants.SUCCESS.getCode());
        if (!StringUtil.isEmpty(uniKey)) {
            if (uniKey.indexOf("?") >= 0) {
                uniKey = uniKey.substring(0, uniKey.indexOf("?"));
            }
        }
        try {
            String uno = request.getParameter("uno");
            if (StringUtil.isEmpty(uno)) {
                uno = CookieUtil.getCookieValue(request, COOKIEKEY_UNO);
            }
            if (StringUtil.isEmpty(uno)) {
                resultObjectMsg.setRs(ResultCodeConstants.USER_NOT_LOGIN.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.USER_NOT_LOGIN.getMsg());
                return "commentscorepostcallback([" + binder.toJson(resultObjectMsg) + "])";
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null || StringUtil.isEmpty(authApp.getProfileKey())) {
                resultObjectMsg.setRs(ResultCodeConstants.APP_NOT_EXISTS.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.APP_NOT_EXISTS.getMsg());
                return "commentscorepostcallback([" + binder.toJson(resultObjectMsg) + "])";
            }

            if (StringUtil.isEmpty(uniKey)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getMsg());
                return "commentscorepostcallback([" + binder.toJson(resultObjectMsg) + "])";
            }

            if (StringUtil.isEmpty(domain)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getMsg());
                return "commentscorepostcallback([" + binder.toJson(resultObjectMsg) + "])";
            }
            CommentDomain commentDomain = null;
            try {
                commentDomain = CommentDomain.getByCode(Integer.valueOf(domain));
                if (commentDomain == null) {
                    resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getCode());
                    resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getMsg());
                    return "commentscorepostcallback([" + binder.toJson(resultObjectMsg) + "])";
                }
            } catch (NumberFormatException e) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_DOMAIN_ERROR.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_DOMAIN_ERROR.getMsg());
                return "commentscorepostcallback([" + binder.toJson(resultObjectMsg) + "])";
            }

            String profileId = UserCenterUtil.getProfileId(uno, authApp.getProfileKey());
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                resultObjectMsg.setRs(ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getMsg());
                return "commentscorepostcallback([" + binder.toJson(resultObjectMsg) + "])";
            }

            String ip = getIp(request);
            AllowCommentStatus allowStatus = checkScoreAllowStatus(uniKey, commentDomain, profileId, ip);
            if (allowStatus.equals(AllowCommentStatus.NO_ALLOW)) {
                resultObjectMsg.setRs(ResultCodeConstants.ERROR.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.ERROR.getMsg());
                return "commentscorepostcallback([" + binder.toJson(resultObjectMsg) + "])";
            } else if (allowStatus.equals(AllowCommentStatus.FORBID_IP)) {
                resultObjectMsg.setRs(ResultCodeConstants.ERROR.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.ERROR.getMsg());
                return "commentscorepostcallback([" + binder.toJson(resultObjectMsg) + "])";
            } else if (allowStatus.equals(AllowCommentStatus.FORBID_UNO)) {
                resultObjectMsg.setRs(ResultCodeConstants.ERROR.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.ERROR.getMsg());
                return "commentscorepostcallback([" + binder.toJson(resultObjectMsg) + "])";
            } else if (allowStatus.equals(AllowCommentStatus.TWENTY_TIMES_A_DAY_ONE_COMMENT)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_SCORE_TWENTY_TIMES_A_DAY_ONE_COMMENT.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_SCORE_TWENTY_TIMES_A_DAY_ONE_COMMENT.getMsg());
                return "commentscorepostcallback([" + binder.toJson(resultObjectMsg) + "])";
            }

            if (StringUtil.isEmpty(score)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_SCORE_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_SCORE_NULL.getMsg());
                return "commentscorepostcallback([" + binder.toJson(resultObjectMsg) + "])";
            }
            int scoreNum = Integer.valueOf(score);

            String commentId = MD5Util.Md5(uniKey + domain);

            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            if (commentBean != null) {
                UpdateExpress updateExpress = new UpdateExpress();
                if (scoreNum > 0 && scoreNum <= 2) {
                    updateExpress.increase(CommentBeanField.ONE_USER_SUM, 1);
                } else if (scoreNum > 2 && scoreNum <= 4) {
                    updateExpress.increase(CommentBeanField.TWO_USER_SUM, 1);
                } else if (scoreNum > 4 && scoreNum <= 6) {
                    updateExpress.increase(CommentBeanField.THREE_USER_SUM, 1);
                } else if (scoreNum > 6 && scoreNum <= 8) {
                    updateExpress.increase(CommentBeanField.FOUR_USER_SUM, 1);
                } else if (scoreNum > 8 && scoreNum <= 10) {
                    updateExpress.increase(CommentBeanField.FIVE_USER_SUM, 1);
                }
                updateExpress.increase(CommentBeanField.SCORE_SUM, scoreNum);
                updateExpress.increase(CommentBeanField.SCORE_TIMES, 1);

                CommentServiceSngl.get().modifyCommentBeanById(commentId, updateExpress);
            } else {
                if (StringUtil.isEmpty(jsonParam)) {
                    resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_JSONPARAM_NULL.getCode());
                    resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_JSONPARAM_NULL.getMsg());
                    return "commentscorepostcallback([" + binder.toJson(resultObjectMsg) + "])";
                }
                CommentJsonParam param = null;
                try {
                    param = CommentJsonParam.parse(jsonParam);
                } catch (Exception e) {
                    resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_COMMENTPARAM_ERROR.getCode());
                    resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_COMMENTPARAM_ERROR.getMsg());
                    return "commentscorepostcallback([" + binder.toJson(resultObjectMsg) + "])";
                }
                CommentBean comment = new CommentBean();
                comment.setUniqueKey(uniKey);
                comment.setDomain(commentDomain);
                if (param != null) {
                    comment.setUri(param.getUri());
                    comment.setTitle(param.getTitle());
                    comment.setPic(param.getPic());
                    comment.setDescription(param.getDescription());
                }
                comment.setCreateTime(new Date());
                comment.setRemoveStatus(ActStatus.UNACT);
                comment.setTotalRows(1);
                comment.setCommentSum(1);
                comment.setScoreSum(scoreNum);
                comment.setScoreTimes(1);
                if (scoreNum > 0 && scoreNum <= 2) {
                    comment.setOneUserSum(1);
                } else if (scoreNum > 2 && scoreNum <= 4) {
                    comment.setTwoUserSum(1);
                } else if (scoreNum > 4 && scoreNum <= 6) {
                    comment.setThreeUserSum(1);
                } else if (scoreNum > 6 && scoreNum <= 8) {
                    comment.setFourUserSum(1);
                } else if (scoreNum > 8 && scoreNum <= 10) {
                    comment.setFiveUserSum(1);
                }
                CommentServiceSngl.get().createCommentBean(comment);
            }
            resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
            resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception:", e);
            resultObjectMsg.setRs(ResultCodeConstants.ERROR.getCode());
            resultObjectMsg.setMsg(ResultCodeConstants.ERROR.getMsg());
            return "commentscorepostcallback([" + binder.toJson(resultObjectMsg) + "])";
        }
        return "commentscorepostcallback([" + binder.toJson(resultObjectMsg) + "])";
    }

}
