/**
 *
 */
package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.oauth.Sticket;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import com.enjoyf.webapps.joyme.webpage.util.VerifyHttpUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * 用户登录权限验证
 *
 * @author zhaoxin
 */
public class HttpSticketGeneratorInterceptor extends HandlerInterceptorAdapter {
    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserCenterSession loginUser = null;
        if (request.getSession() != null) {

            if (request.getSession().getAttribute(Constant.SESSION_USERCENTER) != null) {
                loginUser = (UserCenterSession) request.getSession().getAttribute(Constant.SESSION_USERCENTER);
            }
        }

        if (loginUser == null || StringUtil.isEmpty(loginUser.getUno())) {
            if (loginUser != null && StringUtil.isEmpty(loginUser.getUno())) {
                GAlerter.lan("loginUser not null but uno is empty:loginUser:" + loginUser);
            }

            String queryString = urlPathHelper.getOriginatingQueryString(request);
            queryString = StringUtil.isEmpty(queryString) ? "" : queryString;
            response.sendRedirect("/loginpage?reurl=" + URLEncoder.encode(urlPathHelper.getRequestUri(request) + "?" + queryString, "UTF-8"));
            return false;
        }


        try {
            Sticket st = OAuthServiceSngl.get().generatorSTicket(loginUser.getUno());
            VerifyHttpUtil.saveTicket(request, st);

        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return true;
    }
}
