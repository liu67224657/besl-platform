package com.enjoyf.webapps.joyme.webpage.controller.joymeapp;

import com.enjoyf.platform.service.advertise.app.AppAdvertisePublishType;
import com.enjoyf.platform.service.joymeapp.AppEnterpriserType;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.AppConfigUtil;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.dto.joymeapp.AppAdvertiseDTO;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.JoymeAppWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-14
 * Time: 下午12:27
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/advertise")
public class JoymeAppAdvertiseController extends BaseRestSpringController {

    @Resource(name = "joymeAppWebLogic")
    private JoymeAppWebLogic joymeAppWebLogic;

    @ResponseBody
    @RequestMapping(value = "/list")
    public String list(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "version", required = false) String version,
                       @RequestParam(value = "appkey", required = false) String appId,
                       @RequestParam(value = "adtype", required = false) String advertiseTypeStr,
                       @RequestParam(value = "platform", required = false) String platformStr,
                       @RequestParam(value = "channelid", required = false) String channel
    ) {
        ResultObjectMsg resultMsg = new ResultObjectMsg();
        if (StringUtil.isEmpty(appId)) {
            resultMsg.setRs(ResultCodeConstants.SOCIAL_APPKEY_IS_NULL.getCode());
            resultMsg.setMsg(ResultCodeConstants.SOCIAL_APPKEY_IS_NULL.getMsg());
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(advertiseTypeStr)) {
            resultMsg.setRs(ResultCodeConstants.SOCIAL_ADTYPE_IS_NULL.getCode());
            resultMsg.setMsg(ResultCodeConstants.SOCIAL_ADTYPE_IS_NULL.getMsg());
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(platformStr)) {
            resultMsg.setRs(ResultCodeConstants.SOCIAL_PLATFORM_IS_NULL.getCode());
            resultMsg.setMsg(ResultCodeConstants.SOCIAL_PLATFORM_IS_NULL.getMsg());
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        try {
            //审核期间，去掉广告
            AppConfig appConfig = JoymeAppConfigServiceSngl.get().getAppConfig(AppConfigUtil.getAppConfigId(AppUtil.getAppKey(appId), platformStr, version, channel, String.valueOf(AppEnterpriserType.getEnterpriser(appId))));
            if (appConfig != null && appConfig.getInfo() != null && !StringUtil.isEmpty(appConfig.getInfo().getDefad_url())) {
                resultMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
                resultMsg.setMsg("result.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            appId = getAppKey(appId);
            int publishType = Integer.valueOf(advertiseTypeStr);
            AppAdvertisePublishType advertisePublishType = AppAdvertisePublishType.getByCode(publishType);
            int platform = Integer.valueOf(platformStr);

            if ("17yfn24TFexGybOF0PqjdY".equals(appId) && "1.3.001".equals(version) && platform == 1) {
                channel = "";
            } else if ("17yfn24TFexGybOF0PqjdY".equals(appId) && "1.3.1".equals(version) && platform == 0) {
                channel = "";
            } else if ("0VsYSLLsN8CrbBSMUOlLNx".equals(appId) && "1.3.3".equals(version) && platform == 0) {
                channel = "";
            }


            List<AppAdvertiseDTO> result = joymeAppWebLogic.queryAppAdvertiseList(appId, advertisePublishType, platform, channel);
            if (result == null || CollectionUtil.isEmpty(result)) {
                resultMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
                resultMsg.setMsg("result.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            if (publishType == 5 || publishType == 6 || publishType == 7) {
                int randomNum = (int) (Math.random() * result.size());
                List<AppAdvertiseDTO> returnList = new ArrayList<AppAdvertiseDTO>();
                returnList.add(result.get(randomNum));
                resultMsg.setResult(returnList);
                resultMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
                resultMsg.setMsg("success");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            resultMsg.setResult(result);
            resultMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
            resultMsg.setMsg("success");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
    }

    private static String getAppKey(String appKey) {
        if (com.enjoyf.platform.util.StringUtil.isEmpty(appKey)) {
            return "";
        }
        if (appKey.length() < 23) {
            return appKey;
        }
        return appKey.substring(0, appKey.length() - 1);
    }

//    public static void main(String[] args) {
//        List<Integer> list = new ArrayList<Integer>();
//        list.add(10);
//        list.add(11);
//        list.add(12);
//        list.add(13);
//        for (; ; ) {
//            int a = (int) (Math.random() * 1);
//            System.out.println(list.get(a));
//        }
//
//    }

}
