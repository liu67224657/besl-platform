package com.enjoyf.webapps.joyme.webpage.controller.auth;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ThirdApiHotdeployConfig;
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
import javax.servlet.http.HttpServlet;
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
@RequestMapping("/api/videosdk/auth")
public class VideosdkAuthApiController extends AbstractAuthController {

    private Logger logger = LoggerFactory.getLogger(VideosdkAuthApiController.class);

    @Resource(name = "syncApiWebLogic")
    private SyncApiWebLogic syncApiWebLogic;


    /**
     * 自动注册
     * clientid required
     * appkey  required
     * logindomain required
     *
     * @return
     */
    @ResponseBody
    @RequestMapping
    public String auth(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "otherid", required = false) String otherid,
                       @RequestParam(value = "logindomain", required = false) String logindomain,
                       @RequestParam(value = "tokeninfo", required = false) String tokeninfo,
                       @RequestParam(value = "nick", required = false, defaultValue = "") String nick,
                       @RequestParam(value = "icon", required = false, defaultValue = "") String icon) {

        JSONObject jsonObject = new JSONObject();

        String uno = HTTPUtil.getParam(request, "uno");

        GAlerter.lan(this.getClass().getName() + "  videoSdkApiAuth:nickname=" + nick + " uid=" + icon + " logindomain=" + logindomain);

        LoginDomain loginDomain = LoginDomain.getByCode(logindomain);
        String ip = getIp(request);
        Date createDate = new Date();

        try {
            if (StringUtil.isEmpty(otherid) || loginDomain == null) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            String profileKey = "videosdk";

            //profile不为空 且flag只有client 绑定操作
//            icon = getIcon(icon, otherid, loginDomain, profileKey);
//
            HashMap<String, String> paramMap = HTTPUtil.getRequestToMap(request);
//            if (!loginDomain.equals(LoginDomain.CLIENT) && !StringUtil.isEmpty(uno)) {
//                Profile profileByUno = UserCenterServiceSngl.get().getProfileByProfileId(UserCenterUtil.getProfileId(uno, profileKey));
//                if (profileByUno != null && profileByUno.getFlag().equalFlag(ProfileFlag.FLAG_CLIENTID)) {
//                    try {
//
//                        AuthProfile profile = UserCenterServiceSngl.get().bind(otherid, loginDomain, profileKey, uno, ip, createDate, icon, nick, paramMap);
//                        if (profile != null) {
//                            jsonObject = getResultByAuthProfile(profile);
//                            return jsonObject.toString();
//                        }
//                    } catch (ServiceException e) {
//                        GAlerter.lan(this.getClass().getName() + " /api/auth bind is falied.uno:" + uno + " e:", e);
//                    }
//                }
//            }

            AuthProfile profile = UserCenterServiceSngl.get().auth(otherid, loginDomain, null, icon, nick, profileKey, ip, createDate, paramMap);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            return getResultByAuthProfile(profile).toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            if (e.equals(UserCenterServiceException.USERLOGIN_NOT_EXISTS)) {
                return ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString();
            } else if (e.equals(UserCenterServiceException.PROFILE_NOT_EXISTS)) {
                return ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString();
            } else if (e.equals(UserCenterServiceException.USERLOGIN_WRONG_LOGINDOMAIN)) {
                return ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getJsonString();
            } else {
                return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
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
                jsonObject.put("rs", ResultCodeConstants.PARAM_EMPTY.getCode());
                jsonObject.put("msg", ResultCodeConstants.PARAM_EMPTY.getMsg());
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



    private String getIcon(String icon, String otherid, LoginDomain loginDomain, String profileKey) {
        String headIcon = "";
        //空间默认头像不上传
        if ("http://qzapp.qlogo.cn/qzapp/1103841359/AC88E83C185344EF9C85FE6BED6D67DA/100".equals(icon)) {
            icon = "";
        }
        try {
            if (!StringUtil.isEmpty(icon)) {
                UserLogin userLogin = UserCenterServiceSngl.get().getUserLoginByLoginKey(otherid, loginDomain);
                if (userLogin != null) {
                    Profile profile = UserCenterServiceSngl.get().getProfileByUno(userLogin.getUno(), profileKey);
                    if (profile == null) {
                        headIcon = uploadThirdIconByUrl(icon);
                    }
                } else {
                    headIcon = uploadThirdIconByUrl(icon);
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " upload icon to qiniu error.e: ", e);
        }
        return headIcon;
    }


}
