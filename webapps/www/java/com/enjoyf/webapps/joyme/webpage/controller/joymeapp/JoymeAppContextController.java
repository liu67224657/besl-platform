package com.enjoyf.webapps.joyme.webpage.controller.joymeapp;

import com.enjoyf.platform.service.joymeapp.AppDeployment;
import com.enjoyf.platform.service.joymeapp.AppDeploymentType;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultListMsg;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.webapps.joyme.dto.joymeapp.AppBBSDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-20
 * Time: 下午1:45
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/context")
public class JoymeAppContextController extends JoymeAppBaseController {

    private static final Logger logger = LoggerFactory.getLogger(JoymeAppContextController.class);

    private static final int NICKNAME = 1;   //昵称
    private static final int SIGNATURE = 2;    //签名
    private static final int POST_CONTENT = 3;   //发文章
    private static final int PLAY_GAMES = 4;       //正在玩的游戏

    //
    @ResponseBody
    @RequestMapping(value = "/check")
    public String contextCheck(HttpServletRequest request,
                               @RequestParam(value = "content", required = false) String content,
                               @RequestParam(value = "appkey", required = false) String appKey,
                               @RequestParam(value = "type", required = false) String type) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultCodeConstants.ERROR.getCode());
        try {
            if (StringUtil.isEmpty(content)) {
                resultMsg.setRs(ResultCodeConstants.CONTEXT_CONTENT_NULL.getCode());
                resultMsg.setMsg(ResultCodeConstants.CONTEXT_CONTENT_NULL.getMsg());
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            if (StringUtil.isEmpty(appKey)) {
                resultMsg.setRs(ResultCodeConstants.CONTEXT_APPKEY_NULL.getCode());
                resultMsg.setMsg(ResultCodeConstants.CONTEXT_APPKEY_NULL.getMsg());
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            if (StringUtil.isEmpty(type)) {
                resultMsg.setRs(ResultCodeConstants.CONTEXT_TYPE_NULL.getCode());
                resultMsg.setMsg(ResultCodeConstants.CONTEXT_TYPE_NULL.getMsg());
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            int platform = getPlatformByAppKey(appKey);
            appKey = getAppKey(appKey);
            int ct = Integer.parseInt(type);
            if (ct == NICKNAME) {
                //正则匹配
                if (!ContextFilterUtils.checkNickNames(content)) {
                    resultMsg.setRs(ResultCodeConstants.CONTEXT_REGULAR_ERROR.getCode());
                    resultMsg.setMsg(ResultCodeConstants.CONTEXT_REGULAR_ERROR.getMsg());
                    return JsonBinder.buildNormalBinder().toJson(resultMsg);
                }
                //昵称 敏感词
                if (ContextFilterUtils.checkNickNamesBlackList(content)) {
                    resultMsg.setRs(ResultCodeConstants.CONTEXT_CONTENT_ILLEGAL.getCode());
                    resultMsg.setMsg(ResultCodeConstants.CONTEXT_CONTENT_ILLEGAL.getMsg());
                    return JsonBinder.buildNormalBinder().toJson(resultMsg);
                }
                if (appKey.equals("0VsYSLLsN8CrbBSMUOlLNx")) {
                    //咔哒 保留字
                    if (ContextFilterUtils.checkKadaDomainBlackList(content)) {
                        resultMsg.setRs(ResultCodeConstants.CONTEXT_KADA_RESERVED.getCode());
                        resultMsg.setMsg(ResultCodeConstants.CONTEXT_KADA_RESERVED.getMsg());
                        return JsonBinder.buildNormalBinder().toJson(resultMsg);
                    }
                }
            } else if (ct == SIGNATURE) {
                //签名 敏感词
                if (ContextFilterUtils.checkSimpleEditorBlackList(content)) {
                    resultMsg.setRs(ResultCodeConstants.CONTEXT_CONTENT_ILLEGAL.getCode());
                    resultMsg.setMsg(ResultCodeConstants.CONTEXT_CONTENT_ILLEGAL.getMsg());
                    return JsonBinder.buildNormalBinder().toJson(resultMsg);
                }
            } else if (ct == POST_CONTENT) {
                //文章 敏感词
                if (ContextFilterUtils.postContainBlackList(content)) {
                    resultMsg.setRs(ResultCodeConstants.CONTEXT_CONTENT_ILLEGAL.getCode());
                    resultMsg.setMsg(ResultCodeConstants.CONTEXT_CONTENT_ILLEGAL.getMsg());
                    return JsonBinder.buildNormalBinder().toJson(resultMsg);
                }
            } else if (ct == PLAY_GAMES) {
                //正在玩 敏感词
                if (ContextFilterUtils.checkSimpleEditorBlackList(content)) {
                    resultMsg.setRs(ResultCodeConstants.CONTEXT_CONTENT_ILLEGAL.getCode());
                    resultMsg.setMsg(ResultCodeConstants.CONTEXT_CONTENT_ILLEGAL.getMsg());
                    return JsonBinder.buildNormalBinder().toJson(resultMsg);
                }
            }
            resultMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
            resultMsg.setMsg("success");
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            resultMsg.setRs(ResultCodeConstants.ERROR.getCode());
            resultMsg.setMsg("system.error");
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

}
