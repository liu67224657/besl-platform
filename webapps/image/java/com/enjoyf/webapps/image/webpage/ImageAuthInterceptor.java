package com.enjoyf.webapps.image.webpage;

import com.enjoyf.platform.service.oauth.AuthToken;
import com.enjoyf.platform.service.oauth.AuthTokenType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.webapps.image.webpage.bese.SessionConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
@Component
public class ImageAuthInterceptor extends HandlerInterceptorAdapter {
    //
    Logger logger = LoggerFactory.getLogger(ImageAuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (logger.isDebugEnabled()) {
            logger.debug("auth user by upload");
        }


        GAlerter.lab(this.getClass().getName() + " ===== ImageAuthInterceptor preHandle method ");

        String token = request.getHeader(CookieUtil.ACCESS_TOKEN);
        if (StringUtil.isEmpty(token)) {
            token = request.getParameter(CookieUtil.ACCESS_TOKEN);
        }

        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);
        try {
            try {
                //token is null
                if (StringUtil.isEmpty(token)) {
                    GAlerter.lan("token is null");

                    resultMsg.setMsg("token_faild");
                    HTTPUtil.writeJson(response, JsonBinder.buildNonNullBinder().toJson(resultMsg));

                    return false;
                }

                //auth token is null
                AuthToken authToken = OAuthServiceSngl.get().getToken(token, AuthTokenType.ACCESS);
                if (authToken == null) {
                    GAlerter.lan("token auth failed. at:" + token);

                    resultMsg.setMsg("token_faild");
                    HTTPUtil.writeJson(response, JsonBinder.buildNonNullBinder().toJson(resultMsg));

                    return false;
                }

                //将token存入session中
//                new ImageBaseRestSpringController().saveAuthThokenInSession(request,authToken);
                request.setAttribute(SessionConstants.KEY_ACCESS_TOKEN, authToken);
            } catch (ServiceException e) {
                GAlerter.lab("ImageAuthInterceptor occured ServiceException.", e);
                HTTPUtil.writeJson(response, JsonBinder.buildNonNullBinder().toJson(resultMsg));

                return false;
            }
        } catch (IOException e) {
            GAlerter.lab("ImageAuthInterceptor occured IOException.", e);
        }

        return true;
    }
}
