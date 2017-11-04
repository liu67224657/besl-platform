package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.service.oauth.OAuthInfo;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.webapps.common.oauth.OAuthException;
import com.enjoyf.webapps.joyme.weblogic.oauth.OAuthWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.oauth.OAuthInfoController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;


/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-1-22
 * Time: 下午1:43
 * To change this template use File | Settings | File Templates.
 */
public class OAuthinfoInterceptor extends HandlerInterceptorAdapter {

    @Resource(name = "oauthWebLogic")
    private OAuthWebLogic oauthWebLogic;

    private static Logger logger = LoggerFactory.getLogger(OAuthinfoInterceptor.class);


    public boolean preHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, java.lang.Object handler) throws java.lang.Exception {
        String url = request.getRequestURL().toString();
        String at = request.getParameter("at");
        String ak = request.getParameter("ak");

        if (ak == null) {//分享接口那边参数是appkey
            ak = request.getParameter("appkey");
        }
        OAuthInfo oAuthInfo = null;
        ResultObjectMsg msg = new ResultObjectMsg();

        //截取最后一位
        ak = getAppKey(ak);

        //验证appkey的合法性
        String returnAppkey = OAuthInfoController.checkAppkey(ak);

        try {
            if (!StringUtil.isEmpty(returnAppkey) && (url.contains("/oauth/accesstoken") || url.contains("/json/share"))) {
                if (logger.isDebugEnabled()) {
                    logger.debug("OAuthinfoInterceptor preHandle_function,returnAppkey:" + returnAppkey + ", at:" + at + ", ak:" + ak);
                }
                oAuthInfo = oauthWebLogic.getOAuthInfoByAccessToken(at, returnAppkey, "1");
                if (oAuthInfo != null) {
                    boolean ok = checkExpireLongTime(oAuthInfo);
                    if (ok) {
                        msg.setMsg(ResultCodeConstants.ACCESSTOKEN_OVERDUE.getMsg());
                        msg.setRs(ResultCodeConstants.ACCESSTOKEN_OVERDUE.getCode());
                        HTTPUtil.writeJson(response, JsonBinder.buildNonNullBinder().toJson(msg));
                        return false;
                    }
                }
            }
        } catch (OAuthException e) {
            GAlerter.lan("OAuthinfoInterceptor OAuthException preHandle.e: oAuthInfo is null ");
        } catch (Exception e) {
            GAlerter.lan("OAuthinfoInterceptor preHandle.e:", e);
        }

        return super.preHandle(request, response, handler);
    }


    private static boolean checkExpireLongTime(OAuthInfo oAuthInfo) {
        Long now = System.currentTimeMillis();
        Long after = oAuthInfo.getExpire_longtime() + 1000L * 60L * 30L;
        if (now > after) {
            return true;
        }
        return false;
    }

    private static String getAppKey(String appKey) {
        if (StringUtil.isEmpty(appKey)) {
            return "";
        }
        if (appKey.length() < 23) {
            return appKey;
        }
        return appKey.substring(0, appKey.length() - 1);
    }

}
