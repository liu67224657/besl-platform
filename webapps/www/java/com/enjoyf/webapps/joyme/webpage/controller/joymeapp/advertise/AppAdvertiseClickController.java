package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.advertise;

import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.advertise.app.AppAdvertise;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePv;
import com.enjoyf.platform.service.advertise.app.AppAdvertiseRedirectType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.weblogic.advertise.AppAdvertiseWebLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  2014/6/10 11:37
 * Description:
 */
@Controller
@RequestMapping(value = "/joymeapp/app/advertise")
public class AppAdvertiseClickController extends Thread {

    @Resource(name = "appAdvertiseWebLogic")
    private AppAdvertiseWebLogic appAdvertiseWebLogic;

    private AppAdvertisePv appAdvertisePv;

    public AppAdvertiseClickController() {
    }

    public AppAdvertiseClickController(AppAdvertisePv appAdvertisePv) {
        this.appAdvertisePv = appAdvertisePv;
    }

    @RequestMapping("/click/{publishid}/{adId}")
    public ModelAndView click(@RequestParam(value = "appkey", required = false) String appkey,
                              @RequestParam(value = "platform", required = false) String platform,
                              @RequestParam(value = "deviceid", required = false) String deviceId,
                              @PathVariable(value = "publishid") String publishid,
                              @PathVariable(value = "adId") String adId,
                              @RequestParam(value = "access_token", required = false) String access_token,
                              @RequestParam(value = "token_secr", required = false) String token_secr, HttpServletRequest request, HttpServletResponse response) {
        String redirectUrl = null;
        try {
            long advertiseId = Long.parseLong(adId);
            AppAdvertise appAdvertise = AdvertiseServiceSngl.get().getAppAdvertise(advertiseId);
            if (appAdvertise == null) {
                return new ModelAndView("/views/jsp/common/404");
            }
            redirectUrl = appAdvertise.getUrl();
            if (StringUtil.isEmpty(redirectUrl.trim())) {
                return new ModelAndView("/views/jsp/common/404");
            }

            insertAppAdvertisePv(appkey, platform, deviceId, publishid, adId, request);

            if (appAdvertise.getAppAdvertiseRedirectType().equals(AppAdvertiseRedirectType.APPSTORE)) {
                HTTPUtil.writeJson(response, redirectUrl);
                return null;
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception,e: ", e);
        }
        return new ModelAndView("redirect:" + redirectUrl);
    }

    private void insertAppAdvertisePv(String appkey, String platform, String deviceId, String publishid, String adId, HttpServletRequest request) {
        appAdvertisePv = new AppAdvertisePv();
        try {
            appAdvertisePv.setAppkey(appkey);
            appAdvertisePv.setPlatform(Integer.valueOf(platform));
            appAdvertisePv.setDeviceId(deviceId);
            appAdvertisePv.setPublishid(Long.valueOf(publishid));
            appAdvertisePv.setAdId(Long.valueOf(adId));
            appAdvertisePv.setIp(request.getRemoteAddr());
        } catch (Exception e) {
            return;
        }
        AppAdvertiseClickController ad = new AppAdvertiseClickController(appAdvertisePv);
        ad.start();
    }

    @Override
    public void run() {
        try {
            this.insertPV();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertPV() {
        try {
            AdvertiseServiceSngl.get().createAppAdvertisePvMongo(appAdvertisePv);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " insertPV Exception", e);
        }
    }

    public AppAdvertisePv getAppAdvertisePv() {
        return appAdvertisePv;
    }

    public void setAppAdvertisePv(AppAdvertisePv appAdvertisePv) {
        this.appAdvertisePv = appAdvertisePv;
    }
}
