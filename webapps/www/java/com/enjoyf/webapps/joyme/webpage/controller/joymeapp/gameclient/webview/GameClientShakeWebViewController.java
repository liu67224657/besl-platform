package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.webview;

import com.enjoyf.platform.service.joymeapp.AppEnterpriserType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.config.*;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tonydiao on 2014/12/23.
 */

@Controller
@RequestMapping("/joymeapp/gameclient/webview/shake")
public class GameClientShakeWebViewController extends AbstractGameClientBaseController {

    @RequestMapping("/page")
    public ModelAndView shakePage(HttpServletRequest request) {

        return new ModelAndView("/views/jsp/gameclient/webview/shakepage");

    }


    @RequestMapping
    public ModelAndView shake(HttpServletRequest request) {
        String appkey = HTTPUtil.getParam(request, "appkey");
        String platform = HTTPUtil.getParam(request, "platform");
        String uidParam = HTTPUtil.getParam(request, "uid");
        String version = HTTPUtil.getParam(request, "version");
        String channel = HTTPUtil.getParam(request, "channelid");
        String shaketype = HTTPUtil.getParam(request, "shaketype");

        AppPlatform appPlatform = null;
        try {
            appPlatform = AppPlatform.getByCode(Integer.parseInt(platform));
        } catch (Exception e) {
        }
        long uid = -1;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
        }
        Integer type = null;
        try {
            type = Integer.valueOf(shaketype);
        } catch (NumberFormatException e) {
        }

        //todo error
        if (uid < 0l || appPlatform == null || com.enjoyf.platform.util.StringUtil.isEmpty(appkey)) {
            return null;
        }

        try {
            AppConfig appConfig = JoymeAppConfigServiceSngl.get().getAppConfig(AppConfigUtil.getAppConfigId(AppUtil.getAppKey(appkey), platform, version, channel, String.valueOf(AppEnterpriserType.getEnterpriser(appkey))));

            ShakeItem shakeItem = JoymeAppConfigServiceSngl.get().shake(appConfig.getConfigId(), type != null ? null : ShakeType.getByCode(type), null, new Date());

            Map map = new HashMap();
            map.put("item", shakeItem);

            return new ModelAndView("/views/jsp/gameclient/shakepage", map);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e: ", e);
        }

        return null;
    }
}
