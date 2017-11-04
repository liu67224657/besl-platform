package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.AuthProfile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.UserCenterUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.util.CookieUtil;
import com.enjoyf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/3
 * Description:
 */
public class PassportWebAuthInterceptor extends AbstractPassportAuthInterceptor {
    private Logger logger = LoggerFactory.getLogger(PassportWebAuthInterceptor.class);

    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    protected boolean handle(HttpServletRequest request, HttpServletResponse response, ResultWap result) {
        if (result.getResult().getCode() == ResultCodeConstants.SUCCESS.getCode()) {
            String uidString = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_UID);
            if (StringUtil.isEmpty(uidString)) {
                try {
//                    HashMap<String, String> map = HTTPUtil.getRequestToMap(request);
//                    map.put(UserCenterUtil.TOKEN_STRING, CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_TOKEN));
//
//                    AuthProfile authProfile = UserCenterServiceSngl.get().getAuthProfileByUid(result.getUid(), map);
                    //todo 微服务改造
                    UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
                    AuthProfile authProfile = UserCenterServiceSngl.get().getCurrentAccount();
                    //TODO

                    UserCenterCookieUtil.writeAuthCookie(request, response, authProfile, result.getAppkey(), result.getLoginDomain());
                } catch (ServiceException e) {
                    GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
                }
            }
            return true;
        }

        try {

            String queryString = urlPathHelper.getOriginatingQueryString(request);
            queryString = StringUtil.isEmpty(queryString) ? "" : queryString;

            if (result.getResult().getCode() == ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()) {
                //手机客户端来的并且JParam不为空
                if ((AppUtil.checkIsIOS(request) || AppUtil.checkIsAndroid(request))
                        ) {
                    response.sendRedirect("/appredirect/tokenfailed?reurl=" + URLEncoder.encode(urlPathHelper.getRequestUri(request) + "?" + queryString, "UTF-8"));
                }
            }

            response.sendRedirect("http://www." + WebappConfig.get().getDomain());

//            if (LoginUtil.logindScrKeyExists(request)) {
//                response.sendRedirect("http://passport." + WebappConfig.get().getDomain() + "/auth/loginpage?reurl=" + URLEncoder.encode(urlPathHelper.getRequestUri(request) + "?" + queryString, "UTF-8"));
//
//            } else {
//                response.sendRedirect("http://passport." + WebappConfig.get().getDomain() + "/auth/loginpage?reurl=" + URLEncoder.encode(urlPathHelper.getRequestUri(request) + "?" + queryString, "UTF-8"));
//            }
        } catch (IOException e) {
            logger.error("handleAuthFailed sendRedirect /openlogin occured IOException:", e);
        }


        return false;
    }

}
