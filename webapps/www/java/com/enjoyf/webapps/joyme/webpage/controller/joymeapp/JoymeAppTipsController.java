package com.enjoyf.webapps.joyme.webpage.controller.joymeapp;

import com.enjoyf.platform.service.joymeapp.AppDeployment;
import com.enjoyf.platform.service.joymeapp.AppDeploymentType;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultListMsg;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.dto.joymeapp.AppBBSDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.AppTipsDTO;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.JoymeAppWebLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-20
 * Time: 下午1:45
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/tips")
public class JoymeAppTipsController extends JoymeAppBaseController {

    @Resource(name = "joymeAppWebLogic")
    private JoymeAppWebLogic joymeAppWebLogic;

    @ResponseBody
    @RequestMapping(value = "/tipslist")
    public String list(HttpServletRequest request,
                       @RequestParam(value = "appkey", required = false) String appKey,
                       @RequestParam(value = "platform", required = false) String platformStr,
                       @RequestParam(value = "clientid", required = false) String clientId,
                       @RequestParam(value = "clienttoken", required = false) String clientToken
    ) {
        ResultObjectMsg resultMsg = null;
        try {
            resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);

            if (StringUtil.isEmpty(appKey)) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("param.appkey.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            int platform = 0;
            try {
                platform = Integer.valueOf(platformStr);
            } catch (Exception e) {
            }
            appKey = getAppKey(appKey);

            AppTipsDTO tipsDTO = joymeAppWebLogic.getLastAppTips(appKey, platform, clientId, clientToken);
            if (tipsDTO == null) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("result.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            resultMsg.setRs(ResultObjectMsg.CODE_S);
            resultMsg.setMsg("success");
            resultMsg.setResult(tipsDTO);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setMsg("system.error");
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

}
