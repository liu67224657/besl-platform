package com.enjoyf.webapps.joyme.webpage.controller.joymeapp;

import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.PushMessage;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
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
@RequestMapping(value = "/joymeapp/message")
public class JoymeAppMessageController extends JoymeAppBaseController {

    @ResponseBody
    @RequestMapping(value = "/getlastmsg")
    public String getLastMsg(HttpServletRequest request,
                             @RequestParam(value = "appkey", required = false) String appkey,
                             @RequestParam(value = "client_id", required = false) String clientId,
                             @RequestParam(value = "client_token", required = false) String clientToken,
                             @RequestParam(value = "msgid", required = false) Long msgid,
                             @RequestParam(value = "version", required = false, defaultValue = "1.3.001") String version) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);

        try {

            if (StringUtil.isEmpty(appkey) || StringUtil.isEmpty(clientId) || StringUtil.isEmpty(clientToken)) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("param.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            appkey = getAppKey(appkey);
//            PushMessage message = JoymeAppServiceSngl.get().getLastPushMessage(appkey, version, clientId, clientToken, msgid);
            resultMsg.setResult(null);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setMsg("system.error");
        }


        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

}
