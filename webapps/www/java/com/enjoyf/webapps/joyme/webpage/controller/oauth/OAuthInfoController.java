package com.enjoyf.webapps.joyme.webpage.controller.oauth;

import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthInfo;
import com.enjoyf.platform.service.oauth.OAuthInfoResult;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.webapps.common.oauth.OAuthException;
import com.enjoyf.webapps.joyme.weblogic.oauth.OAuthWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-1-14
 * Time: 上午10:49
 * oauth2
 */
@Controller
@RequestMapping(value = "/oauth")
public class OAuthInfoController extends BaseRestSpringController {
    private static Logger logger = LoggerFactory.getLogger(OAuthInfoController.class);

    @Resource(name = "oauthWebLogic")
    private OAuthWebLogic oauthWebLogic;


    @ResponseBody
    @RequestMapping(value = "/accesstoken")
    public String accessToken(@RequestParam(value = "at", defaultValue = "", required = true) String accessToken, @RequestParam(value = "ak", defaultValue = "", required = true) String appKey, HttpServletRequest request,
                              HttpServletResponse response) {
        ResultObjectMsg msg = new ResultObjectMsg();
        try {
            if (StringUtil.isEmpty(accessToken) || StringUtil.isEmpty(appKey)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("accessToken or appKey is null");
                }
                msg.setMsg(ResultCodeConstants.ACCESSTOKEN_APPKEY_IS_NULL.getMsg());
                msg.setRs(ResultCodeConstants.ACCESSTOKEN_APPKEY_IS_NULL.getCode());
                return JsonBinder.buildNonNullBinder().toJson(msg);
            }

            //截取最后一个appkey
            appKey = getAppKey(appKey);

            //验证appkey的合法性
            String returnAppkey = checkAppkey(appKey);

            if (StringUtil.isEmpty(returnAppkey)) {
                msg.setMsg(ResultCodeConstants.APPKEY_ERROR.getMsg());
                msg.setRs(ResultCodeConstants.APPKEY_ERROR.getCode());
                return JsonBinder.buildNonNullBinder().toJson(msg);
            }

            //
            OAuthInfo oAuthInfo = oauthWebLogic.getOAuthInfoByAccessToken(accessToken, returnAppkey, "0");
            OAuthInfoResult oAuthInfoResult = new OAuthInfoResult();
            if (logger.isDebugEnabled()) {
                logger.debug("accessToken function,returnAppkey:" + returnAppkey + ", oAuthInfo:" + oAuthInfo);
            }

            if (null != oAuthInfo) {
                msg.setMsg(ResultCodeConstants.OAUTH_SUCCESS.getMsg());
                msg.setRs(ResultCodeConstants.OAUTH_SUCCESS.getCode());
                oAuthInfoResult.setUno(oAuthInfo.getUno());
                oAuthInfoResult.setExpire_date(oAuthInfo.getExpire_date());
                oAuthInfoResult.setExpire_longtime(oAuthInfo.getExpire_longtime());
                msg.setResult(oAuthInfoResult);
            } else {
                msg.setMsg(ResultCodeConstants.UNO_IS_NULL.getMsg());
                msg.setRs(ResultCodeConstants.UNO_IS_NULL.getCode());
            }
            return JsonBinder.buildNonNullBinder().toJson(msg);
        } catch (OAuthException e) {
            GAlerter.lab("accessToken OAuthException.e:", e);
        } catch (ServiceException e) {
            GAlerter.lab("accessToken ServiceException.e:", e);
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/refreshtoken")
    public String refreshToken(@RequestParam(value = "rt", defaultValue = "", required = true) String refreshToken, @RequestParam(value = "ak", defaultValue = "", required = true) String appKey, HttpServletRequest request,
                               HttpServletResponse response) {
        ResultObjectMsg msg = new ResultObjectMsg();
        try {
            if (StringUtil.isEmpty(refreshToken) || StringUtil.isEmpty(appKey)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("refreshToken or appKey is null");
                }
                msg.setMsg(ResultCodeConstants.REFRESHTOKEN_APPKEY_IS_NULL.getMsg());
                msg.setRs(ResultCodeConstants.REFRESHTOKEN_APPKEY_IS_NULL.getCode());
                return JsonBinder.buildNonNullBinder().toJson(msg);
            }

            //截取最后一个appkey
            appKey = getAppKey(appKey);
            
            //验证appkey的合法性
            String returnAppkey = checkAppkey(appKey);
            if (StringUtil.isEmpty(returnAppkey)) {
                msg.setMsg(ResultCodeConstants.APPKEY_ERROR.getMsg());
                msg.setRs(ResultCodeConstants.APPKEY_ERROR.getCode());
                return JsonBinder.buildNonNullBinder().toJson(msg);
            }

            OAuthInfo oAuthInfo = oauthWebLogic.getOAuthInfoByRereshToken(refreshToken, returnAppkey);
            OAuthInfoResult oAuthInfoResult = new OAuthInfoResult();
            if (logger.isDebugEnabled()) {
                logger.debug("refreshToken function,returnAppkey:" + returnAppkey + ", oAuthInfo:" + oAuthInfo);
            }
            if (null != oAuthInfo) {
                msg.setMsg(ResultCodeConstants.OAUTH_SUCCESS.getMsg());
                msg.setRs(ResultCodeConstants.OAUTH_SUCCESS.getCode());
                oAuthInfoResult.setUno(oAuthInfo.getUno());
                oAuthInfoResult.setAccesstoken(oAuthInfo.getAccess_token());
                oAuthInfoResult.setRefreshtoken(oAuthInfo.getRefresh_token());
                oAuthInfoResult.setExpire_date(oAuthInfo.getExpire_date());
                oAuthInfoResult.setExpire_longtime(oAuthInfo.getExpire_longtime());
                msg.setResult(oAuthInfoResult);
            } else {
                msg.setMsg(ResultCodeConstants.UNO_IS_NULL.getMsg());
                msg.setRs(ResultCodeConstants.UNO_IS_NULL.getCode());
            }
            return JsonBinder.buildNonNullBinder().toJson(msg);
        } catch (OAuthException e) {
            GAlerter.lab("refreshtoken OAuthException.e:", e);
        } catch (ServiceException e) {
            GAlerter.lab("refreshtoken ServiceException.e:", e);
        }
        return null;
    }


    public static String checkAppkey(String appKey) throws ServiceException {
        AuthApp authApp = null;
        try {
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
