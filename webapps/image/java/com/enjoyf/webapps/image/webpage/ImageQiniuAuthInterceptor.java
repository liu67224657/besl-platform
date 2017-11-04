package com.enjoyf.webapps.image.webpage;

import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Token;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.webapps.image.webpage.bese.SessionConstants;
import net.sf.json.JSONObject;
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
public class ImageQiniuAuthInterceptor extends HandlerInterceptorAdapter {
    //
    Logger logger = LoggerFactory.getLogger(ImageQiniuAuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (logger.isDebugEnabled()) {
            logger.debug("auth user by upload");
        }

        String tokenString = request.getHeader("token");
        if (StringUtil.isEmpty(tokenString)) {
            tokenString = request.getParameter("token");
        }

        //TODO 兼容之前取at
        if(StringUtil.isEmpty(tokenString)){
            tokenString = StringUtil.isEmpty(request.getHeader(CookieUtil.ACCESS_TOKEN))?request.getParameter(CookieUtil.ACCESS_TOKEN):request.getHeader(CookieUtil.ACCESS_TOKEN);
        }

        if (tokenString.equals("joymeplatform")) {
            return true;
        }

        String appKey = request.getHeader("appkey");
        if (StringUtil.isEmpty(appKey)) {
            appKey = request.getParameter("appkey");
        }

        String uno = request.getHeader("uno");
        if (StringUtil.isEmpty(uno)) {
            uno = request.getParameter("uno");
        }

        JSONObject jsonObject = new JSONObject();
        if (StringUtil.isEmpty(uno)) {
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.APP_NOT_EXISTS.getCode()));
            jsonObject.put("msg", String.valueOf(ResultCodeConstants.APP_NOT_EXISTS.getMsg()));
            try {
                HTTPUtil.writeJson(response, jsonObject.toString());
                return false;
            } catch (IOException e) {
            }
        }

        if (StringUtil.isEmpty(appKey)) {
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.APP_NOT_EXISTS.getCode()));
            jsonObject.put("msg", String.valueOf(ResultCodeConstants.APP_NOT_EXISTS.getMsg()));
            try {
                HTTPUtil.writeJson(response, jsonObject.toString());
                return false;
            } catch (IOException e) {
            }
        }

//        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);
        try {
            try {
                AuthApp app = OAuthServiceSngl.get().getApp(appKey);
                if (app == null || StringUtil.isEmpty(app.getProfileKey())) {
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.APP_NOT_EXISTS.getCode()));
                    jsonObject.put("msg", String.valueOf(ResultCodeConstants.APP_NOT_EXISTS.getMsg()));
                    try {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                    } catch (IOException e) {
                    }
                    return false;
                }

                //token is null
                if (StringUtil.isEmpty(tokenString)) {
                    GAlerter.lan("token is null");
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg()));
                    HTTPUtil.writeJson(response, jsonObject.toString());
                    return false;
                }

                //auth token is null
                Token token = UserCenterServiceSngl.get().getToken(tokenString);
                if (token == null) {
                    GAlerter.lan("token auth failed. token:" + token);
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg()));
                    HTTPUtil.writeJson(response, jsonObject.toString());
                    return false;
                }

                if (!token.getUno().equals(uno)) {
                    GAlerter.lan("token auth failed uno not exists. uno:" + uno);
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg()));
                    HTTPUtil.writeJson(response, jsonObject.toString());
                    return false;
                }

                //将token存入session中
                request.setAttribute(SessionConstants.KEY_ACCESS_TOKEN, token);
            } catch (ServiceException e) {
                GAlerter.lab("ImageAuthInterceptor occured ServiceException.", e);
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                jsonObject.put("msg", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg()));
                HTTPUtil.writeJson(response, jsonObject.toString());
                return false;
            }
        } catch (IOException e) {
            GAlerter.lab("ImageAuthInterceptor occured IOException.", e);
        }

        return true;
    }
}
