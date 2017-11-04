package com.enjoyf.webapps.image.webpage;

import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.security.DESUtil;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-16
 * Time: 下午5:18
 * To change this template use File | Settings | File Templates.
 */
public class SocialclientUploadInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    ////验证 uno time ak secret,secret是uno+time加密的结果，秘钥根据ak查询
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ResultObjectMsg msg = new ResultObjectMsg();
        String secret = request.getParameter("secret");//加密之后的结果
        if (StringUtil.isEmpty(secret)) {
            msg.setMsg(ResultCodeConstants.INTERCEPT_SECRET_IS_NULL.getMsg());
            msg.setRs(ResultCodeConstants.INTERCEPT_SECRET_IS_NULL.getCode());
            HTTPUtil.writeJson(response, JsonBinder.buildNonNullBinder().toJson(msg));
            return false;
        }
        String time = request.getParameter("time");//时间戳
        if (StringUtil.isEmpty(secret)) {
            msg.setMsg(ResultCodeConstants.INTERCEPT_TIME_IS_NULL.getMsg());
            msg.setRs(ResultCodeConstants.INTERCEPT_TIME_IS_NULL.getCode());
            HTTPUtil.writeJson(response, JsonBinder.buildNonNullBinder().toJson(msg));
            return false;
        }

        String uno = request.getParameter("uno");//uno
        if (StringUtil.isEmpty(uno)) {
            msg.setMsg(ResultCodeConstants.INTERCEPT_UNO_IS_NULL.getMsg());
            msg.setRs(ResultCodeConstants.INTERCEPT_UNO_IS_NULL.getCode());
            HTTPUtil.writeJson(response, JsonBinder.buildNonNullBinder().toJson(msg));
            return false;
        }

        String ak = request.getParameter("appkey");//appkey
        String appkey = checkAppkey(ak);
        if (StringUtil.isEmpty(appkey)) {
            msg.setMsg(ResultCodeConstants.INTERCEPT_APPKEY_IS_NULL.getMsg());
            msg.setRs(ResultCodeConstants.INTERCEPT_APPKEY_IS_NULL.getCode());
            HTTPUtil.writeJson(response, JsonBinder.buildNonNullBinder().toJson(msg));
            return false;
        }
        String cecheTime = OAuthServiceSngl.get().getSocialAPI(uno, time);
        if (!StringUtil.isEmpty(cecheTime)) {
            msg.setMsg(ResultCodeConstants.INTERCEPT_FORBID.getMsg());
            msg.setRs(ResultCodeConstants.INTERCEPT_FORBID.getCode());
            HTTPUtil.writeJson(response, JsonBinder.buildNonNullBinder().toJson(msg));
            return false;
        }
        //appkey做为秘钥，
        String encryptParam = DESUtil.desEncrypt(appkey, uno + time);
        if (!encryptParam.equals(secret)) {
            msg.setMsg(ResultCodeConstants.INTERCEPT_SECRET_IS_ERROR.getMsg());
            msg.setRs(ResultCodeConstants.INTERCEPT_SECRET_IS_ERROR.getCode());
            HTTPUtil.writeJson(response, JsonBinder.buildNonNullBinder().toJson(msg));
            return false;
        }
        return true;
    }


    public String checkAppkey(String appKey) throws ServiceException {
        AuthApp authApp = null;
        try {
            appKey = getAppKey(appKey);
            authApp = OAuthServiceSngl.get().getApp(appKey);
            if (authApp == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("checkAppkey authApp is null");
                }
                return "";
            }
        } catch (ServiceException e) {
            GAlerter.lab("checkAppkey ServiceException.e:", e);
            return "";
        }
        return authApp.getAppKey();
    }

    private String getAppKey(String appKey) {
        if (StringUtil.isEmpty(appKey)) {
            return "";
        }
        if (appKey.length() < 23) {
            return appKey;
        }
        return appKey.substring(0, appKey.length() - 1);
    }
}
