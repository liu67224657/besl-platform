package com.enjoyf.webapps.joyme.webpage.controller.joymeapp;

import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.usercenter.AuthProfile;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultListMsg;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-20
 * Time: 下午1:45
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/user")
public class JoymeAppUserController extends JoymeAppBaseController {

    @ResponseBody
    @RequestMapping(value = "/getprofile")
    public String reportInstall(HttpServletRequest request,
                                @RequestParam(value = "appkey", required = false) String appkey,
                                @RequestParam(value = "channelid", required = false) String channelId,
                                @RequestParam(value = "client_id", required = false) String client_id,
                                @RequestParam(value = "clientid", required = false) String clientId,
                                @RequestParam(value = "client_token", required = false) String client_token,
                                @RequestParam(value = "clienttoken", required = false) String clientToken,
                                @RequestParam(value = "platform", required = false) Integer platform) {
        ResultObjectMsg resultMsg = null;
        try {
            resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);

            if (StringUtil.isEmpty(appkey)) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("param.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            if (StringUtil.isEmpty(channelId)) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("param.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            if (StringUtil.isEmpty(clientId) && StringUtil.isEmpty(client_id)) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("param.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            if (StringUtil.isEmpty(clientToken) && StringUtil.isEmpty(client_token)) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("param.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            if (platform == null) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("param.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            LoginDomain loginDomain = LoginDomain.CLIENT;

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                return ResultCodeConstants.APP_NOT_EXISTS.getJsonString();
            }

            String profileKey = authApp.getProfileKey();
            if (StringUtil.isEmpty(profileKey)) {
                return ResultCodeConstants.APP_NOT_EXISTS.getJsonString();
            }

            HashMap<String, String> paramMap = HTTPUtil.getRequestToMap(request);

            AuthProfile profile = UserCenterServiceSngl.get().auth((StringUtil.isEmpty(clientId) ? client_id : clientId), loginDomain, null, "", "", profileKey, getIp(request), new Date(), paramMap);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("uno", profile == null ? "" : profile.getUserAccount().getUno());
            resultMsg.setResult(map);
        } catch (Exception e) {
//            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setMsg("system.error");
        }

        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }


}
