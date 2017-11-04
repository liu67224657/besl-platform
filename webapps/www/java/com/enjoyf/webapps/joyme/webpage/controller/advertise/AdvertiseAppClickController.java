package com.enjoyf.webapps.joyme.webpage.controller.advertise;


import com.enjoyf.platform.service.advertise.AdvertiseAppUrl;
import com.enjoyf.platform.service.advertise.AdvertiseAppUrlClickInfo;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterCookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 *
 */
@Controller
@RequestMapping("/appclick")
public class AdvertiseAppClickController extends BaseRestSpringController {


    @RequestMapping(value = "/{code}")
    public ModelAndView clickAppUrl(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable(value = "code") String code,
                                    @RequestParam(value = "sid",required = false) String sid) {
        try {
            AdvertiseAppUrl advertiseAppUrl = AdvertiseServiceSngl.get().getAppUrlByCode(code);
            if (advertiseAppUrl == null) {
                return new ModelAndView("/views/jsp/common/404");
            }


            String returnUrl = advertiseAppUrl.getAndroidUrl();
            AppPlatform appPlatform=AppUtil.checkIsIOS(request)?AppPlatform.IOS:(AppUtil.checkIsAndroid(request)?AppPlatform.ANDROID:AppPlatform.WEB);

            if (StringUtil.isEmpty(returnUrl)) {
                returnUrl = advertiseAppUrl.getIosUrl();
            }

            if (!StringUtil.isEmpty(advertiseAppUrl.getIosUrl()) && AppUtil.checkIsIOS(request)) {
                returnUrl = advertiseAppUrl.getIosUrl();
            }

            String appkey = UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_APPKEY);
            if("17yfn24TFexGybOF0PqjdYI".equals(appkey)){
                returnUrl = advertiseAppUrl.getIosUrl();
            }

            try {
                AdvertiseAppUrlClickInfo info=new AdvertiseAppUrlClickInfo();
                info.setCode(code);
                info.setPlatform(appPlatform);
                info.setRedirectUrl(returnUrl);
                info.setReportDate(new Date());
                info.setSid(sid);

                AdvertiseServiceSngl.get().reportAppUrlClick(info);
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName()+" report appclick occured ServiceException.e: ",e);
            }

            if(returnUrl.equalsIgnoreCase(".plist")){
                response.setContentType("text/xml");
            }

            return new ModelAndView("redirect:" + returnUrl);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceExcpetion.e");
            return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("system.error"));
        }
    }
}
