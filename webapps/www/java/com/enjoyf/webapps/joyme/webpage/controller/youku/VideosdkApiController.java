package com.enjoyf.webapps.joyme.webpage.controller.youku;

import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.util.StringUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Created by xupeng on 16/4/7.
 */
@Controller
@RequestMapping(value = "/api/videosdk/")
public class VideosdkApiController {
    @RequestMapping(value = "/baseinfo")
    @ResponseBody
    public String baseInfo(HttpServletRequest request, HttpServletResponse response) {
        JSONObject returnObj = new JSONObject();
        try {
            String appkey = HTTPUtil.getParam(request, "appkey");
            String version = HTTPUtil.getParam(request, "version");
            String channel = HTTPUtil.getParam(request, "channelid");
            String platform = HTTPUtil.getParam(request, "platform");
            if (com.enjoyf.util.StringUtil.isEmpty(appkey)) {
                returnObj.put("rs", ResultCodeConstants.PARAM_EMPTY.getCode());
                returnObj.put("msg", ResultCodeConstants.PARAM_EMPTY.getMsg());
                returnObj.put("result", new JSONObject());
                return returnObj.toString();
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                returnObj.put("rs", ResultCodeConstants.APP_NOT_EXISTS.getCode());
                returnObj.put("msg", ResultCodeConstants.APP_NOT_EXISTS.getMsg());
                returnObj.put("result", new JSONObject());
                return returnObj.toString();
            }
            JSONObject result = new JSONObject();
            JSONObject appinfo = new JSONObject();
            appinfo.put("unuploadAdUrl", "http://joymepic.joyme.com/qiniu/original/2016/04/60/2c05972b0907f048eb0a4ce0847061d55078.jpg");

            result.put("appinfo", appinfo);


            returnObj.put("rs", ResultCodeConstants.SUCCESS.getCode());
            returnObj.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            returnObj.put("result", result);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            returnObj.put("rs", ResultCodeConstants.ERROR.getCode());
            returnObj.put("msg", ResultCodeConstants.ERROR.getMsg());
            returnObj.put("result", new JSONObject());
        }
        return returnObj.toString();
    }

    @RequestMapping(value="/verify")
    @ResponseBody
    public String comment(HttpServletRequest request, HttpServletResponse response) {
        String text = request.getParameter("text");

        if (StringUtil.isEmpty(text)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        //文章 敏感词
        Set<String> postKeyword = ContextFilterUtils.getPostContainBlackList(text);
        if (!CollectionUtil.isEmpty(postKeyword)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.CONTEXT_CONTENT_ILLEGAL.getCode()));
            jsonObject.put("msg", ResultCodeConstants.CONTEXT_CONTENT_ILLEGAL.getMsg());
            jsonObject.put("result", postKeyword);
            return jsonObject.toString();
        } else {
            return ResultCodeConstants.SUCCESS.getJsonString();
        }
    }


}
