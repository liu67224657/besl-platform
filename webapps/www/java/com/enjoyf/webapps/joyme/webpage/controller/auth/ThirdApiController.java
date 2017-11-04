package com.enjoyf.webapps.joyme.webpage.controller.auth;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ThirdApiHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.point.WanbaPointType;
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
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.weblogic.sync.SyncApiWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterCookieUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
@RequestMapping(value = "/auth/thirdapi")
public class ThirdApiController extends AbstractAuthController {
    Logger logger = LoggerFactory.getLogger(ThirdApiController.class);

    @Resource(name = "syncApiWebLogic")
    private SyncApiWebLogic syncApiWebLogic;


    @Resource(name = "pointWebLogic")
    private PointWebLogic pointWebLogic;


    private ThirdApiHotdeployConfig config = HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class);

    /**
     * 跳转操作
     *
     * @param apiCode
     * @param response
     */
    @RequestMapping(value = "/{apiCode}/bind")
    public ModelAndView bind(@PathVariable(value = "apiCode") String apiCode,
                             @RequestParam(value = "rl", required = false) boolean rl,
                             @RequestParam(value = "rt", defaultValue = "") String redirectType,
                             @RequestParam(value = "ak", defaultValue = "") String appKey,
                             HttpServletRequest request,
                             HttpServletResponse response) {

        try {
            Map map = new HashMap();
            WebHotdeployConfig webConfig = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class);
            if (!webConfig.getUserLoginTrigger()) {
                map.put("message", "userlogin.not.open");
                return new ModelAndView("/views/jsp/common/custompage", map);
            }


            LoginDomain loginDomain = LoginDomain.getByCode(apiCode);


            String reurl = getReurl(request);

            appKey = getAppKey(appKey);

            //write cookie
            AuthUrlParam authUrlParam = new AuthUrlParam(reurl, rl, redirectType, appKey);
            saveCookieReUrl(new AuthUrlParam(reurl, rl, redirectType, appKey), request, response);

            response.sendRedirect(syncApiWebLogic.getAuthorizeUrl(loginDomain, config.getAuthVersionMap().get(loginDomain), authUrlParam));
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
                                    HttpServletRequest request, HttpServletResponse response) {


        Date now = new Date();
        try {
            if (apiCode.equals(AccountDomain.SYNC_QQ_WEIBO.getCode())) {
                return new ModelAndView("redirect:http://bbs.joyme.com/thread-8484-1-1.html");
            }

            String authParamUrl = "";
            if (!StringUtil.isEmpty(request.getParameter("reurl"))) {
                authParamUrl = request.getParameter("reurl");
            }

            String redirectType = "";
            String appKey = "default";
            boolean isLoginAction = false;

            AuthUrlParam urlParam = getCookieReurl(request);
            if (urlParam != null) {
                redirectType = urlParam.getRedirectType();
                appKey = StringUtil.isEmpty(urlParam.getAppKey()) ? "default" : urlParam.getAppKey();
                isLoginAction = urlParam.isRequiredLogin();
                if (StringUtil.isEmpty(authParamUrl)) {
                    authParamUrl = urlParam.getRurl();
                }
            }

            if (StringUtil.isEmpty(authParamUrl)) {
                authParamUrl = WebappConfig.get().getUrlWww();
            }
            GAlerter.lan("ThirdApiController access==>");

            LoginDomain loginDomain = LoginDomain.getByCode(apiCode);
            if (loginDomain == null) {
                ResultObjectMsg msg = new ResultObjectMsg();
                msg.setMsg(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getMsg());
                msg.setRs(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getCode());
                writeResponse(response, redirectType, msg, authParamUrl);
                GAlerter.lan("ThirdApiController access==> loginDomain is null==>apiCode=" + apiCode);
                return null;
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appKey);
            if (authApp == null) {
                ResultObjectMsg msg = new ResultObjectMsg();
                msg.setMsg(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getMsg());
                msg.setRs(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getCode());
                writeResponse(response, redirectType, msg, authParamUrl);
                GAlerter.lan("ThirdApiController access==> authApp is null==>appKey=" + appKey);
                return null;
            }

            String profileKey = authApp.getProfileKey();
            if (StringUtil.isEmpty(profileKey)) {
                ResultObjectMsg msg = new ResultObjectMsg();
                msg.setMsg(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getMsg());
                msg.setRs(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getCode());
                writeResponse(response, redirectType, msg, authParamUrl);
                GAlerter.lan("ThirdApiController access==> profileKey is null==>profileKey=" + profileKey);
                return null;
            }


            IOauthApi oauthInterface = OAuthInterfaceSngl.get().getApiByProvider(loginDomain, config.getAuthVersionMap().get(loginDomain));
            AuthParam authParam = oauthInterface.getAuthParamByRequest(request, authParamUrl, isLoginAction, redirectType, appKey);

            if (authParam == null) {
                ResultObjectMsg msg = new ResultObjectMsg();
                msg.setMsg(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getMsg());
                msg.setRs(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getCode());
                writeResponse(response, redirectType, msg, authParamUrl);
                GAlerter.lan("ThirdApiController access==> authParam is null==>authParamUrl=" + authParamUrl);
                return null;
            }

            OAuthInfo oAuthInfo = oauthInterface.getAccessToken(authParam);

            if (oAuthInfo == null) {
                ResultObjectMsg msg = new ResultObjectMsg();
                msg.setMsg(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getMsg());
                msg.setRs(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getCode());
                writeResponse(response, redirectType, msg, authParamUrl);
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
                writeAuthCookie(request, response, authProfile, appKey, loginDomain);
            } else {
                //绑定操作自行处理异常
                try {
                    UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
                    authProfile = UserCenterServiceSngl.get().bind(oAuthInfo.getForignId(), "", loginDomain, profileKey, userSession.getUno(), getIp(request), now, "", "", HTTPUtil.getRequestToMap(request));
                } catch (ServiceException e) {
                    GAlerter.lab("accessToken bind occured ServiceException.e:", e);

                    String errorCode = ResultCodeConstants.SYSTEM_ERROR.getMsg();
                    if (e.equals(UserCenterServiceException.PROFILE_HAS_EXISTS)) {
                        errorCode = ResultCodeConstants.USERCENTER_PROFILE_HAS_BIND.getMsg();
                    }
                    return new ModelAndView("redirect:" + HTTPUtil.encodeUrl(HTTPUtil.appendParam(authParamUrl, "errorcode=" + errorCode)));
                }
            }
//todo 微服务改造
//            UpdateExpress updateExpress = new UpdateExpress().set(UserLoginField.TOKEN_INFO, oAuthInfo.getAccessToken().toJsonStr());
//            UserCenterServiceSngl.get().modifyUserLogin(updateExpress, UserCenterUtil.getUserLoginId(oAuthInfo.getForignId(), loginDomain));
            deleteCookieReurl(request, response);

            //TODO 绑定第三方成功加积分
            if (!loginDomain.equals(LoginDomain.CLIENT)) {
                //第三方首次登录或绑定
                pointWebLogic.modifyUserPoint(PointActionType.OTHER_LOGIN_AND_BIND, authProfile.getProfile().getProfileId(), DEFAULT_APPKEY, WanbaPointType.THIRD, loginDomain);
            }

            return getModelAndViewAfterLogin(authProfile, authParamUrl, request, appKey, loginDomain);

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

    private void writeResponse(HttpServletResponse response, String redirectType, ResultObjectMsg msg, String reurl) {
        if ("json".equals(redirectType)) {
            try {
                HTTPUtil.writeJson(response, JsonBinder.buildNonNullBinder().toJson(msg));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                response.sendRedirect(HTTPUtil.encodeUrl(reurl));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
