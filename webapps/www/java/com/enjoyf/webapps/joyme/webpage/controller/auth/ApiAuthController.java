package com.enjoyf.webapps.joyme.webpage.controller.auth;

import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.qqv2.QqV2OpenId2UnionId;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 手游畫報接口，不維護
 * Created by ericliu on 14/10/22.
 */
@Deprecated
@Controller
@RequestMapping("/api/auth")
public class ApiAuthController extends AbstractAuthController {

    private Logger logger = LoggerFactory.getLogger(ApiAuthController.class);

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
                       @RequestParam(value = "openid", required = false) String openId,
                       @RequestParam(value = "logindomain", required = false) String logindomain,
                       @RequestParam(value = "tokeninfo", required = false) String tokeninfo,
                       @RequestParam(value = "nick", required = false, defaultValue = "") String nick,
                       @RequestParam(value = "icon", required = false, defaultValue = "") String icon,
                       @RequestParam(value = "appkey", required = false) String appkey) {

        JSONObject jsonObject = new JSONObject();

        String uno = HTTPUtil.getParam(request, "uno");

        //GAlerter.lan(this.getClass().getName() + "  ApiAuth:nickname=" + nick + " uid=" + icon + " logindomain=" + logindomain);

        LoginDomain loginDomain = LoginDomain.getByCode(logindomain);
        String ip = getIp(request);
        Date createDate = new Date();

        try {
            if (StringUtil.isEmpty(otherid) || StringUtil.isEmpty(appkey) || loginDomain == null) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                return ResultCodeConstants.APP_NOT_EXISTS.getJsonString();
            }

            String profileKey = authApp.getProfileKey();
            if (StringUtil.isEmpty(profileKey)) {
                return ResultCodeConstants.APP_NOT_EXISTS.getJsonString();
            }

            //qq登录如果openid和unionid都存在,判断是否是以前老用户(用openid做loginkey的用户)如果是,更新userlogin表改成unionid.
            if (LoginDomain.QQ.equals(loginDomain) && !StringUtil.isEmpty(openId)) {
                QqV2OpenId2UnionId.openId2UnionId(openId, otherid);
            }


            //profile不为空 且flag只有client 绑定操作
            icon = getIcon(icon, otherid, loginDomain, profileKey);

            HashMap<String, String> paramMap = HTTPUtil.getRequestToMap(request);
            if (!loginDomain.equals(LoginDomain.CLIENT) && !StringUtil.isEmpty(uno)) {
                Profile profileByUno = UserCenterServiceSngl.get().getProfileByProfileId(UserCenterUtil.getProfileId(uno, profileKey));
                if (profileByUno != null && profileByUno.getFlag().equalFlag(ProfileFlag.FLAG_CLIENTID)) {
                    try {

                        AuthProfile profile = UserCenterServiceSngl.get().bind(otherid, "", loginDomain, profileKey, uno, ip, createDate, icon, nick, paramMap);
                        if (profile != null) {
                            jsonObject = getResultByAuthProfile(profile);
                            return jsonObject.toString();
                        }
                    } catch (ServiceException e) {
                        GAlerter.lan(this.getClass().getName() + " /api/auth bind is falied.uno:" + uno + " e:", e);
                    }
                }
            }

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
    @RequestMapping("/youku")
    public String authYouku(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "otherid", required = false) String otherid,
                            @RequestParam(value = "logindomain", required = false) String logindomain,
                            @RequestParam(value = "tokeninfo", required = false) String tokeninfo,
                            @RequestParam(value = "nick", required = false, defaultValue = "") String nick,
                            @RequestParam(value = "icon", required = false, defaultValue = "") String icon,
                            @RequestParam(value = "appkey", required = false) String appkey) {

        logger.info(" this is youku auth api:  /api/auth/youku");

        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    /**
     * 绑定
     * clientid required
     * appkey  required
     * logindomain required
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/bind")
    public String bind(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "logindomain", required = false) String logindomain,
                       @RequestParam(value = "otherid", required = false) String otherid,
                       @RequestParam(value = "appkey", required = false) String appkey,
                       @RequestParam(value = "uno", required = false) String uno) {

        JSONObject jsonObject = new JSONObject();

        LoginDomain loginDomain = LoginDomain.getByCode(logindomain);
        String ip = getIp(request);
        Date createDate = new Date();

        try {
            if (StringUtil.isEmpty(otherid) || StringUtil.isEmpty(appkey) || StringUtil.isEmpty(logindomain)) {
                jsonObject.put("rs", ResultCodeConstants.PARAM_EMPTY.getCode());
                jsonObject.put("msg", ResultCodeConstants.PARAM_EMPTY.getMsg());
                return jsonObject.toString();
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                jsonObject.put("rs", ResultCodeConstants.APP_NOT_EXISTS.getCode());
                jsonObject.put("msg", ResultCodeConstants.APP_NOT_EXISTS.getMsg());
                return jsonObject.toString();
            }

            String profileKey = authApp.getProfileKey();
            if (StringUtil.isEmpty(profileKey)) {
                jsonObject.put("rs", ResultCodeConstants.APP_NOT_EXISTS.getCode());
                jsonObject.put("msg", ResultCodeConstants.APP_NOT_EXISTS.getMsg());
                return jsonObject.toString();
            }

            HashMap<String, String> paramMap = HTTPUtil.getRequestToMap(request);
            AuthProfile profile = UserCenterServiceSngl.get().bind(otherid, "", loginDomain, profileKey, uno, ip, createDate, "", "", paramMap);
            if (profile == null) {
                jsonObject.put("rs", ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getCode());
                jsonObject.put("msg", ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getMsg());
                return jsonObject.toString();
            }

            jsonObject = getResultByAuthProfile(profile);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);

            if (e.equals(UserCenterServiceException.USERLOGIN_NOT_EXISTS)) {
                jsonObject.put("rs", ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getCode());
                jsonObject.put("msg", ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getMsg());
            } else if (e.equals(UserCenterServiceException.PROFILE_NOT_EXISTS)) {
                jsonObject.put("rs", ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getCode());
                jsonObject.put("msg", ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getMsg());
            } else if (e.equals(UserCenterServiceException.USERLOGIN_WRONG_LOGINDOMAIN)) {
                jsonObject.put("rs", ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getCode());
                jsonObject.put("msg", ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getMsg());
            } else {
                jsonObject.put("rs", ResultCodeConstants.SYSTEM_ERROR.getCode());
                jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);

            jsonObject.put("rs", ResultCodeConstants.SYSTEM_ERROR.getCode());
            jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());

        }

        return jsonObject.toString();
    }

    /**
     * 注册
     * clientid required
     * appkey  required
     * logindomain required
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/register")
    public String register(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "loginkey", required = false) String loginKey,
                           @RequestParam(value = "password", required = false) String pasword,
                           @RequestParam(value = "appkey", required = false) String appkey,
                           @RequestParam(value = "nick", required = false) String nick,
                           @RequestParam(value = "logindomain", required = false) String logindomain) {


        JSONObject jsonObject = new JSONObject();

        LoginDomain loginDomain = LoginDomain.getByCode(logindomain);
        String ip = getIp(request);
        Date createDate = new Date();

        try {
            if (StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(appkey) || StringUtil.isEmpty(logindomain)) {
                jsonObject.put("rs", ResultCodeConstants.PARAM_EMPTY.getCode());
                jsonObject.put("msg", ResultCodeConstants.PARAM_EMPTY.getMsg());
                return jsonObject.toString();
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                jsonObject.put("rs", ResultCodeConstants.APP_NOT_EXISTS.getCode());
                jsonObject.put("msg", ResultCodeConstants.APP_NOT_EXISTS.getMsg());
                return jsonObject.toString();
            }

            String profileKey = authApp.getProfileKey();
            if (StringUtil.isEmpty(profileKey)) {
                jsonObject.put("rs", ResultCodeConstants.APP_NOT_EXISTS.getCode());
                jsonObject.put("msg", ResultCodeConstants.APP_NOT_EXISTS.getMsg());
                return jsonObject.toString();
            }

            AuthProfile profile = UserCenterServiceSngl.get().register(loginKey, pasword, loginDomain, profileKey, nick, ip, createDate, HTTPUtil.getRequestToMap(request));
            if (profile == null) {
                //todo
            }

            jsonObject = getResultByAuthProfile(profile);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            if (e.equals(UserCenterServiceException.USERLOGIN_NOT_EXISTS)) {
                jsonObject.put("rs", ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getCode());
                jsonObject.put("msg", ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getMsg());
            } else if (e.equals(UserCenterServiceException.PROFILE_NOT_EXISTS)) {
                jsonObject.put("rs", ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getCode());
                jsonObject.put("msg", ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getMsg());
            } else if (e.equals(UserCenterServiceException.USERLOGIN_WRONG_LOGINDOMAIN)) {
                jsonObject.put("rs", ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getCode());
                jsonObject.put("msg", ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getMsg());
            } else {
                jsonObject.put("rs", ResultCodeConstants.SYSTEM_ERROR.getCode());
                jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);

            jsonObject.put("rs", ResultCodeConstants.SYSTEM_ERROR.getCode());
            jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());

        }
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "loginkey", required = false) String loginKey,
                        @RequestParam(value = "password", required = false) String pasword,
                        @RequestParam(value = "appkey", required = false) String appkey,
                        @RequestParam(value = "logindomain", required = false) String logindomain) {

        JSONObject jsonObject = new JSONObject();

        LoginDomain loginDomain = LoginDomain.getByCode(logindomain);
        String ip = getIp(request);
        Date createDate = new Date();

        try {
            if (StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(appkey) || StringUtil.isEmpty(logindomain)) {
                jsonObject.put("rs", ResultCodeConstants.PARAM_EMPTY.getCode());
                jsonObject.put("msg", ResultCodeConstants.PARAM_EMPTY.getMsg());
                return jsonObject.toString();
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                jsonObject.put("rs", ResultCodeConstants.APP_NOT_EXISTS.getCode());
                jsonObject.put("msg", ResultCodeConstants.APP_NOT_EXISTS.getMsg());
                return jsonObject.toString();
            }

            String profileKey = authApp.getProfileKey();
            if (StringUtil.isEmpty(profileKey)) {
                jsonObject.put("rs", ResultCodeConstants.APP_NOT_EXISTS.getCode());
                jsonObject.put("msg", ResultCodeConstants.APP_NOT_EXISTS.getMsg());
                return jsonObject.toString();
            }

            AuthProfile profile = UserCenterServiceSngl.get().login(loginKey, pasword, loginDomain, profileKey, ip, createDate, HTTPUtil.getRequestToMap(request));
            if (profile == null) {
                //todo
            }

            jsonObject = getResultByAuthProfile(profile);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);

            if (e.equals(UserCenterServiceException.USERLOGIN_NOT_EXISTS)) {
                jsonObject.put("rs", ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getCode());
                jsonObject.put("msg", ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getMsg());
            } else if (e.equals(UserCenterServiceException.PROFILE_NOT_EXISTS)) {
                jsonObject.put("rs", ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getCode());
                jsonObject.put("msg", ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getMsg());
            } else if (e.equals(UserCenterServiceException.USERLOGIN_WRONG_LOGINDOMAIN)) {
                jsonObject.put("rs", ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getCode());
                jsonObject.put("msg", ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getMsg());
            } else {
                jsonObject.put("rs", ResultCodeConstants.SYSTEM_ERROR.getCode());
                jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);

            jsonObject.put("rs", ResultCodeConstants.SYSTEM_ERROR.getCode());
            jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());

        }

        return jsonObject.toString();
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
//            String token = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_TOKEN);
            CookieUtil.deleteALLCookies(request, response);
//            DiscuzUtil.logOutDiscuz(request, response);
            if (!StringUtil.isEmpty(token)) {
                UserCenterServiceSngl.get().deleteToken(token);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }
        return jsonObject.toString();
    }


    @ResponseBody
    @RequestMapping("/savenick")
    public String login(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "nick", required = false) String nick) {

        JSONObject jsonObject = new JSONObject();
        String appkey = HTTPUtil.getParam(request, "appkey");
        String uidString = HTTPUtil.getParam(request, "uid");
        String ip = getIp(request);
        Date createDate = new Date();
        try {
            nick = nick.trim();
            if (StringUtil.isEmpty(nick)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                return ResultCodeConstants.APP_NOT_EXISTS.getJsonString();
            }

            String profileKey = authApp.getProfileKey();
            if (StringUtil.isEmpty(profileKey)) {
                return ResultCodeConstants.APP_NOT_EXISTS.getJsonString();
            }

            long uid = -1;
            try {
                uid = Long.parseLong(uidString);
            } catch (NumberFormatException e) {
            }

            if (uid == -1) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            if (profile.getFlag().hasFlag(ProfileFlag.FLAG_NICK_HASCOMPLETE)) {
                return ResultCodeConstants.USERCENTER_NICK_HAS_MODIFY.getJsonString();
            }

            Profile nickProfile = UserCenterServiceSngl.get().getProfileByNick(nick);
            if (nickProfile != null && nickProfile.getUid() != uid) {
                return ResultCodeConstants.USERCENTER_NICK_HAS_EXISTS.getJsonString();
            }
            if (ContextFilterUtils.postContainBlackList(nick)) {
                return ResultCodeConstants.CONTEXT_CONTENT_ILLEGAL.getJsonString();
            }

            UserCenterServiceSngl.get().modifyProfile(new UpdateExpress()
                    .set(ProfileField.NICK, nick)
                    .set(ProfileField.CHECKNICK, nick.toLowerCase())
                    .set(ProfileField.FLAG, profile.getFlag().has(ProfileFlag.FLAG_NICK_HASCOMPLETE).getValue())
                    , profile.getProfileId());

            jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            Map map = new HashMap();
            map.put("flag", profile.getFlag().getValue());
            jsonObject.put("result", map);

            return jsonObject.toString();
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
            jsonObject.put("rs", ResultCodeConstants.SYSTEM_ERROR.getCode());
            jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());
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
