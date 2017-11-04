package com.enjoyf.webapps.joyme.webpage.controller.auth;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ThirdApiHotdeployConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.AuthParam;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.thirdapi.AuthUrlParam;
import com.enjoyf.platform.thirdapi.oauth.IOauthApi;
import com.enjoyf.platform.thirdapi.oauth.OAuthInfo;
import com.enjoyf.platform.thirdapi.oauth.OAuthInterfaceSngl;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.webapps.joyme.weblogic.sync.SyncApiWebLogic;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ericliu on 14/10/22.
 */
@Controller
@RequestMapping("/videosdk/webview/auth")
public class VideosdkAuthController extends AbstractAuthController {

    private Logger logger = LoggerFactory.getLogger(VideosdkAuthController.class);

    @Resource(name = "syncApiWebLogic")
    private SyncApiWebLogic syncApiWebLogic;

    private ThirdApiHotdeployConfig config = HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class);

    /**
     * 跳转操作
     *
     * @param
     * @param
     */
    @RequestMapping(value = "/loginpage")
    public ModelAndView bind() {


        return new ModelAndView("/views/jsp/passport/webview/videosdk-loginpage");
    }

    /**
     * 跳转操作
     *
     * @param apiCode
     * @param response
     */
    @RequestMapping(value = "/{apiCode}/bind")
    public ModelAndView bind(@PathVariable(value = "apiCode") String apiCode,
                             HttpServletRequest request,
                             HttpServletResponse response) {

        try {

            LoginDomain loginDomain = LoginDomain.getByCode(apiCode);


            String reurl = getReurl(request);

            //write cookie
            AuthUrlParam authUrlParam = new AuthUrlParam(reurl, false, "", "");
            saveCookieReUrl(authUrlParam, request, response);

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

            //todo
            if (loginDomain == null) {
                ResultObjectMsg msg = new ResultObjectMsg();
                msg.setMsg(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getMsg());
                msg.setRs(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getCode());
                return null;
            }

            //todo
            String profileKey = "videosdk";
            IOauthApi oauthInterface = OAuthInterfaceSngl.get().getApiByProvider(loginDomain, config.getAuthVersionMap().get(loginDomain));
            AuthParam authParam = oauthInterface.getAuthParamByRequest(request, authParamUrl, isLoginAction, redirectType, appKey);

            if (authParam == null) {
                ResultObjectMsg msg = new ResultObjectMsg();
                msg.setMsg(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getMsg());
                msg.setRs(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getCode());
                return null;
            }

            OAuthInfo oAuthInfo = oauthInterface.getAccessToken(authParam);
            if (oAuthInfo == null) {
                ResultObjectMsg msg = new ResultObjectMsg();
                msg.setMsg(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getMsg());
                msg.setRs(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getCode());
                //todo
                return new ModelAndView("/views/jsp/appredirect/videosdk-result", putErrorMessage("ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getMsg()"));
            }

            AuthProfile authProfile = UserCenterServiceSngl.get().auth(oAuthInfo.getForignId(), loginDomain, oAuthInfo.getAccessToken(), "", oAuthInfo.getForignName() == null ? "" : oAuthInfo.getForignName(), profileKey, getIp(request), now, HTTPUtil.getRequestToMap(request));
            UpdateExpress updateExpress = new UpdateExpress().set(UserLoginField.TOKEN_INFO, oAuthInfo.getAccessToken().toJsonStr());
            UserCenterServiceSngl.get().modifyUserLogin(updateExpress, UserCenterUtil.getUserLoginId(oAuthInfo.getForignId(), loginDomain));
            deleteCookieReurl(request, response);

            Map mapMessage = new HashMap();
            mapMessage.put("token", authProfile.getToken().getToken());
            mapMessage.put("icon", authProfile.getProfile().getIcon());
            mapMessage.put("nick", authProfile.getProfile().getNick());
            mapMessage.put("uid", authProfile.getProfile().getUid());
            mapMessage.put("uno", authProfile.getProfile().getUno());
            mapMessage.put("pid", authProfile.getProfile().getProfileId());
            mapMessage.put("description", authProfile.getProfile().getDescription());
            mapMessage.put("logindomain", loginDomain.getCode());

            return new ModelAndView("/views/jsp/appredirect/videosdk-result", mapMessage);

            //todo
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

            return new ModelAndView("/views/jsp/appredirect/videosdk-result", putErrorMessage("system.error"));
        } catch (Exception e) {
            GAlerter.lab("accessToken occured Exception.e:", e);
            return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("sync.auth.error"));
        }

    }


    @ResponseBody
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
        jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
        try {
            String token = HTTPUtil.getParam(request, "token");
            if (com.enjoyf.util.StringUtil.isEmpty(token)) {
                return jsonObject.toString();
            }
            CookieUtil.deleteALLCookies(request, response);
            if (!StringUtil.isEmpty(token)) {
                UserCenterServiceSngl.get().deleteToken(token);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }
        return jsonObject.toString();
    }





}
