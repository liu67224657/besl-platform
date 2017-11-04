package com.enjoyf.webapps.joyme.webpage.controller.profile;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ThirdApiHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.AuthParam;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.thirdapi.AuthUrlParam;
import com.enjoyf.platform.thirdapi.oauth.IOauthApi;
import com.enjoyf.platform.thirdapi.oauth.OAuthInfo;
import com.enjoyf.platform.thirdapi.oauth.OAuthInterfaceSngl;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.regex.RegexUtil;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.webapps.joyme.weblogic.sync.SyncApiWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.controller.auth.AbstractAuthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * <p/>
 * Description:第三方api接口认证
 * </p>
 * <pre>
 * /sync/{apiCode}/bind 申请key
 * /sync/{apiCode}/unbind 取消绑定
 * /sync/{apiCode}/access 申请后回调绑定
 * </pre>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
@Controller
@RequestMapping(value = "/profile/sync")
public class SyncController extends AbstractAuthController {
    Logger logger = LoggerFactory.getLogger(SyncController.class);

    @Resource(name = "syncApiWebLogic")
    private SyncApiWebLogic syncApiWebLogic;

    private ThirdApiHotdeployConfig config = HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class);

    /**
     * requestToken 操作
     *
     * @param apiCode
     * @param response
     */
    @RequestMapping(value = "/{apiCode}/bind")
    public ModelAndView bind(@PathVariable(value = "apiCode") String apiCode,
                             @RequestParam(value = "rurl", required = false) String rurl,
                             @RequestParam(value = "rl", required = false) boolean rl,
//                             @RequestParam(value = "inviteid", required = false) Long inviteid,
//                             @RequestParam(value = "gid", required = false) String gid,
//                             @RequestParam(value = "icn", defaultValue = "false") boolean ignoreCheckName,
                             @RequestParam(value = "rt", defaultValue = "") String redirectType,
                             @RequestParam(value = "ak", defaultValue = "default") String appKey,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        WebHotdeployConfig webHotdeployConfig = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class);
        if (!webHotdeployConfig.getUserLoginTrigger()) {
            map.put("message", "userlogin.not.open");
            return new ModelAndView("/views/jsp/common/custompage", map);
        }


        try {
            LoginDomain loginDomain = LoginDomain.getByCode(apiCode);

            //由于新浪和人人不支持中文所以如果rurl有中文跳转到首页。
            if (StringUtil.isEmpty(rurl)) {
                rurl = WebappConfig.get().getUrlWww();
            } else {
                Matcher matcher = RegexUtil.URL_ENCODE_PATTERN.matcher(rurl);
                rurl = matcher.find() ? WebappConfig.get().getUrlWww() : rurl;
            }

            if (!StringUtil.isEmpty(rurl) && !apiCode.equals("renren")) {
                try {
                    rurl = URLEncoder.encode(rurl, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    logger.error("bind occured UnsupportedEncodingException.e:", e);
                }
            }
            appKey = getAppKey(appKey);

            response.sendRedirect(syncApiWebLogic.getAuthorizeUrl(loginDomain, config.getAuthVersionMap().get(loginDomain), new AuthUrlParam(rurl, rl, redirectType, appKey)));
        } catch (ServiceException e) {
            logger.error("bind occured ServiceException.e:", e);
        } catch (IOException e) {
            logger.error("bind occured IOException.e:", e);
        }
        return null;
    }

    /**
     * call back and access token
     *
     * @param apiCode
     * @param request
     * @return
     */
    @RequestMapping(value = "/{apiCode}/access")
    public ModelAndView accessToken(@PathVariable(value = "apiCode") String apiCode,
                                    @RequestParam(value = "inviteid", required = false) Long inviteId,
                                    @RequestParam(value = "gid", required = false) String gid,
                                    @RequestParam(value = "rl", defaultValue = "false") boolean isLoginAction,
                                    @RequestParam(value = "icn", defaultValue = "false") boolean ignorCheckName,
                                    @RequestParam(value = "rt", defaultValue = "") String redirectType,
                                    @RequestParam(value = "callbackparam", defaultValue = "false") String callbackParam,
                                    @RequestParam(value = "ak", required = false, defaultValue = "default") String appKey,
                                    HttpServletRequest request, HttpServletResponse response) {


        Date now = new Date();
        try {
            if (apiCode.equals(AccountDomain.SYNC_QQ_WEIBO.getCode())) {
                return new ModelAndView("redirect:http://bbs.joyme.com/thread-8484-1-1.html");
            }

            String authParamUrl = "";
            if (!StringUtil.isEmpty(request.getParameter("rurl"))) {
                authParamUrl = request.getParameter("rurl");
            } else {
            }

            if (!StringUtil.isEmpty(callbackParam)) {
                String[] params = callbackParam.split("-_-_-_-");
                for (String param : params) {
                    String[] paramArray = param.split("_-_-_-_");
                    if (paramArray.length == 2) {
                        if (paramArray[0].equalsIgnoreCase("rurl")) {
                            authParamUrl = paramArray[1];
                        } else if (paramArray[0].equalsIgnoreCase("rl")) {
                            redirectType = paramArray[1];
                        } else if (paramArray[0].equalsIgnoreCase("ak")) {
                            appKey = paramArray[1];
                        }
                    }
                }
            }

            LoginDomain loginDomain = LoginDomain.getByCode(apiCode);
            if (loginDomain == null) {
                ResultObjectMsg msg = new ResultObjectMsg();
                msg.setMsg(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getMsg());
                msg.setRs(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getCode());
                writeResponse(response, redirectType, msg, null);
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appKey);
            if (authApp == null) {
                ResultObjectMsg msg = new ResultObjectMsg();
                msg.setMsg(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getMsg());
                msg.setRs(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getCode());
                writeResponse(response, redirectType, msg, null);
            }

            String profileKey = authApp.getProfileKey();
            if (StringUtil.isEmpty(profileKey)) {
                ResultObjectMsg msg = new ResultObjectMsg();
                msg.setMsg(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getMsg());
                msg.setRs(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getCode());
                writeResponse(response, redirectType, msg, null);
            }


            IOauthApi oauthInterface = OAuthInterfaceSngl.get().getApiByProvider(loginDomain, config.getAuthVersionMap().get(loginDomain));
            AuthParam authParam = oauthInterface.getAuthParamByRequest(request, authParamUrl, isLoginAction, redirectType, appKey);

            if (authParam == null) {
                ResultObjectMsg msg = new ResultObjectMsg();
                msg.setMsg(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getMsg());
                msg.setRs(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getCode());
                writeResponse(response, redirectType, msg, null);
            }

            OAuthInfo oAuthInfo = oauthInterface.getAccessToken(authParam);

            if (oAuthInfo == null) {
                ResultObjectMsg msg = new ResultObjectMsg();
                msg.setMsg(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getMsg());
                msg.setRs(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getCode());
                writeResponse(response, redirectType, msg, null);
            }

            UserCenterSession userSession = getUserCenterSeesion(request);
            AuthProfile authProfile = null;
            if (userSession == null || !isLoginAction) {
                UserLogin userLogin = UserCenterServiceSngl.get().getUserLoginByLoginKey(oAuthInfo.getForignId(), loginDomain);
                String icon = null;
//                //profile为空 抓取头像
                if (userLogin != null) {
                    Profile profile = UserCenterServiceSngl.get().getProfileByUno(userLogin.getUno(), profileKey);
                    if (profile == null) {
                        icon = uploadThirdIconByUrl(oAuthInfo.getFigureUrl());
                    }
                } else {
                    icon = uploadThirdIconByUrl(oAuthInfo.getFigureUrl());
                }

                authProfile = UserCenterServiceSngl.get().auth(oAuthInfo.getForignId(), loginDomain, oAuthInfo.getAccessToken(), icon == null ? "" : icon, oAuthInfo.getForignName() == null ? "" : oAuthInfo.getForignName(), profileKey, getIp(request), now, HTTPUtil.getRequestToMap(request));
            } else {
                authProfile = UserCenterServiceSngl.get().bind(oAuthInfo.getForignId(),"", loginDomain, profileKey, userSession.getUno(), getIp(request), now, "", "", HTTPUtil.getRequestToMap(request));
            }


            if ("json".equals(redirectType)) {
                ResultObjectMsg objectMsg = getSyncJson(oAuthInfo, authProfile, apiCode);
                try {
                    HTTPUtil.writeJson(response, JsonBinder.buildNonNullBinder().toJson(objectMsg));
                } catch (IOException e) {
                }
            }

            UpdateExpress updateExpress = new UpdateExpress().set(UserLoginField.TOKEN_INFO, oAuthInfo.getAccessToken().toJsonStr());
            UserCenterServiceSngl.get().modifyUserLogin(updateExpress, UserCenterUtil.getUserLoginId(oAuthInfo.getForignId(), loginDomain));


            writeAuthCookie(request, response, authProfile, appKey, loginDomain);
            buildUserCenterSession(authProfile, request);

            return getModelAndViewAfterLogin(authProfile, authParamUrl, request, appKey,loginDomain);
        } catch (ServiceException e) {
            GAlerter.lab("accessToken occured ServiceException.e:", e);

            if (e.equals(UserCenterServiceException.USERLOGIN_NOT_EXISTS)) {
                return new ModelAndView("/views/jsp/common/custompage", putErrorMessage(ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getMsg()));
            } else if (e.equals(UserCenterServiceException.PROFILE_NOT_EXISTS)) {
                return new ModelAndView("/views/jsp/common/custompage", putErrorMessage(ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getMsg()));
            } else if (e.equals(UserCenterServiceException.PROFILE_HAS_EXISTS)) {
                return new ModelAndView("/views/jsp/common/custompage", putErrorMessage(ResultCodeConstants.USERCENTER_PROFILE_HAS_BIND.getMsg()));
            } else if (e.equals(UserCenterServiceException.USERLOGIN_WRONG_LOGINDOMAIN)) {
                return new ModelAndView("/views/jsp/common/custompage", putErrorMessage(ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getMsg()));
            }

            return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("system.error"));
        } catch (Exception e) {
            GAlerter.lab("accessToken occured Exception.e:", e);
            return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("sync.auth.error"));
        }
    }

    /**
     * delete  token操作
     *
     * @param apiCode
     * @param request
     * @return
     */
    @RequestMapping(value = "/{apiCode}/unbind")
    public ModelAndView unbind(@PathVariable(value = "apiCode") String apiCode,
                               @RequestParam(value = "ak", defaultValue = "default") String appKey,
                               HttpServletRequest request) {

        try {
            UserCenterSession userSession = this.getUserCenterSeesion(request);

            LoginDomain provider = LoginDomain.getByCode(apiCode);
            if (provider == null) {
                GAlerter.lan(this.getClass().getName() + " worng loginDomain." + apiCode);
                return new ModelAndView("/views/jsp/common/custompage", putErrorMessage(ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getMsg()));
            }

            int flag = ProfileFlag.getFlagByLoginDomain(provider);
            AuthApp authApp = OAuthServiceSngl.get().getApp(appKey);
            if (authApp == null) {
                ResultObjectMsg msg = new ResultObjectMsg();
                return new ModelAndView("/views/jsp/common/custompage", putErrorMessage(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getMsg()));
            }

            boolean bVal = UserCenterServiceSngl.get().unbind(provider, authApp.getProfileKey(), userSession.getUno(), getIp(request), new Date());
            if (bVal && userSession.getFlag().hasFlag(flag)) {
                userSession.getFlag().reduce(flag);
            }

        } catch (ServiceException e) {
            GAlerter.lab("unbind occured ServiceException.e:", e);
            if (e.equals(UserCenterServiceException.USERLOGIN_WRONG_LOGINDOMAIN)) {
                return new ModelAndView("/views/jsp/common/custompage", putErrorMessage(ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getMsg()));
            } else {
                return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("system.error"));
            }


        }
        return new ModelAndView("redirect:/profile/customize/bind");
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


    private void writeResponse(HttpServletResponse response, String redirectType, ResultObjectMsg msg, String reurl) {

        if ("json".equals(redirectType)) {

            try {
                HTTPUtil.writeJson(response, JsonBinder.buildNonNullBinder().toJson(msg));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                response.sendRedirect(reurl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
