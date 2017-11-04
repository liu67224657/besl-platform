package com.enjoyf.webapps.joyme.webpage.controller.joymeapp;

import com.enjoyf.platform.service.joymeapp.AppCertificateInfo;
import com.enjoyf.platform.service.joymeapp.AppDeployment;
import com.enjoyf.platform.service.joymeapp.AppDeploymentType;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultListMsg;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.dto.joymeapp.AppBBSDTO;
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
@RequestMapping(value = "/joymeapp/certificate")
public class JoymeAppCertificateController extends JoymeAppBaseController {

    @ResponseBody
    @RequestMapping(value = "/getproduct")
    public String reportInstall(HttpServletRequest request,
                                @RequestParam(value = "appkey", required = false) String appkey) {
        ResultObjectMsg resultMsg = null;
        try {
            resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);

            if (StringUtil.isEmpty(appkey)) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("param.appkey.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            int platform = getPlatformByAppKey(appkey);
            if (platform == -1) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("appkey.A/I.error");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            appkey = getAppKey(appkey);

            AppDeployment appCertificate = JoymeAppConfigServiceSngl.get().getAppDeploymentByCache(appkey, platform, AppDeploymentType.CERTIFICATE.getCode(), null,null);
            if (appCertificate != null) {
                AppCertificateInfo certificateInfo = new AppCertificateInfo(appCertificate.getAppkey(), appCertificate.getPath(), appCertificate.getPassword(), appCertificate.getIsProduct());
                resultMsg.setResult(certificateInfo);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setMsg("system.error");
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

}
