package com.enjoyf.webapps.joyme.webpage.controller.joymeapp;

import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.webapps.common.ResultListMsg;
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
@RequestMapping(value = "/joymeapp/api/update/")
public class JoymeAppUpdateController extends BaseRestSpringController {

    @ResponseBody
    @RequestMapping(value = "/getversion")
    public String reportInstall(HttpServletRequest request,
                                @RequestParam(value = "appkey", required = false) String appkey) {
        ResultListMsg resultMsg = new ResultListMsg(ResultListMsg.CODE_S);

        if (StringUtil.isEmpty(appkey)) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/getcontent")
    public String reportPageView(HttpServletRequest request,
                                 @RequestParam(value = "appkey", required = false) String appkey,
                                 @RequestParam(value = "versionnum", required = false) String version) {
        ResultListMsg resultMsg = new ResultListMsg(ResultListMsg.CODE_S);

        if ( StringUtil.isEmpty(appkey) || StringUtil.isEmpty(version)) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

}
