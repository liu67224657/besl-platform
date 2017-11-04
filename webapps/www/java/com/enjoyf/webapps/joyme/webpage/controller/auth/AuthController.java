package com.enjoyf.webapps.joyme.webpage.controller.auth;

import com.enjoyf.platform.crypto.MD5Cryptor;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.ask.QuestionType;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.platform.webapps.common.util.WebUtil;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.webapps.joyme.weblogic.user.DiscuzModifyField;
import com.enjoyf.webapps.joyme.weblogic.user.DiscuzUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterCookieUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ericliu on 14/10/22.
 */
@Controller
@RequestMapping("/auth")
public class AuthController extends AbstractAuthController {

    /**
     * 自动注册
     * clientid required
     * appkey  required
     * logindomain required
     *
     * @return
     */
    @RequestMapping
    public ModelAndView auth(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "appkey", required = false) String appkey,
                             @RequestParam(value = "reurl", required = false) String reurl) {
        LoginDomain loginDomain = LoginDomain.CLIENT;
        String clientid = getClietnIdByCookie(request, response);
        String ip = getIp(request);
        Date createDate = new Date();
        if (StringUtil.isEmpty(clientid) || StringUtil.isEmpty(appkey)) {
            //todo
        }

        try {
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                //todo
                return null;
            }

            String profileKey = authApp.getProfileKey();
            if (StringUtil.isEmpty(profileKey)) {
                //todo
                return null;
            }

            AuthProfile profile = UserCenterServiceSngl.get().auth(clientid, loginDomain, null, "", "", profileKey, ip, createDate, HTTPUtil.getRequestToMap(request));

            writeAuthCookie(request, response, profile, appkey, loginDomain);
            if (loginDomain.equals(LoginDomain.CLIENT)) {
                buildUserCenterSession(profile, request);
            }
            return getModelAndViewAfterLogin(profile, reurl, request, appkey, loginDomain);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            //todo error

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            //todo error
        }

        return new ModelAndView("redirect:" + getReurl(request));
    }

    /**
     * 绑定
     * clientid required
     * appkey  required
     * logindomain required
     *
     * @return
     */
    @RequestMapping("/bind")
    public ModelAndView bind(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "appkey", required = false) String appkey,
                             @RequestParam(value = "tokeninfo", required = false) String tokeinfo,
                             @RequestParam(value = "uno", required = false) String uno) {

        LoginDomain loginDomain = LoginDomain.EXPLORE;
        String ip = getIp(request);
        String clientid = getClietnIdByCookie(request, response);
        Date createDate = new Date();

        if (StringUtil.isEmpty(uno) || StringUtil.isEmpty(appkey)) {
            //todo
        }

        try {
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                //todo
                return null;
            }

            String profileKey = authApp.getProfileKey();
            if (StringUtil.isEmpty(profileKey)) {
                //todo
                return null;
            }
            AuthProfile profile = UserCenterServiceSngl.get().bind(clientid, "", loginDomain, profileKey, uno, ip, createDate, "", "", HTTPUtil.getRequestToMap(request));

            writeAuthCookie(request, response, profile, appkey, loginDomain);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            //todo error

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            //todo error
        }

        return new ModelAndView("redirect:" + getReurl(request));
    }


    @RequestMapping("/registerpage")
    public ModelAndView registerPage(HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam(value = "loginkey", required = false) String loginKey,
                                     @RequestParam(value = "of", required = false) String of,
                                     @RequestParam(value = "password", required = false) String pasword,
                                     @RequestParam(value = "reurl", required = false, defaultValue = "") String reurl,
                                     @RequestParam(value = "appkey", required = false) String appkey) {
        Map map = new HashMap();
        WebHotdeployConfig config = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class);
        if (!config.getUserLoginTrigger() && !"lh".equals(of)) {
            map.put("message", "userlogin.not.open");
            return new ModelAndView("/views/jsp/common/custompage", map);
        }


        if (!StringUtil.isEmpty(reurl) && reurl != "null") {
            map.put("reurl", reurl);
        }

        return new ModelAndView("/views/jsp/passport/registerpage", map);
    }

    /**
     * 注册
     * clientid required
     * appkey  required
     * logindomain required
     *
     * @return
     */
    @RequestMapping("/register")
    public ModelAndView register(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam(value = "loginkey", required = false, defaultValue = "") String loginKey,
                                 @RequestParam(value = "password", required = false, defaultValue = "") String pasword,
                                 @RequestParam(value = "appkey", required = false, defaultValue = "default") String appkey,
                                 @RequestParam(value = "nick", required = false, defaultValue = "") String nick) {
        LoginDomain loginDomain = LoginDomain.EMAIL;
        String ip = getIp(request);
        Date createDate = new Date();

        Map mapMessage = new HashMap();
        String reurl = getReurl(request);

        Map<String, Object> errorMap = new HashMap<String, Object>();
        loginKey = loginKey.trim();
        if (StringUtil.isEmpty(loginKey) || !WebUtil.verifyEmail(loginKey)) {
            errorMap.put("emailMsg", "user.email.wrong");
        }

        if (StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(appkey)) {
            errorMap.put("emailMsg", "user.email.wrong");
        }

        //验证用户密码是否为空
        if (StringUtil.isEmpty(pasword) || pasword.length() < 6 || pasword.length() > 18) {
            errorMap.put("passwordMsg", "user.userpwd.length");
        }

        try {
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                errorMap.put("emailMsg", "user.email.wrong");
            }

            String profileKey = authApp.getProfileKey();
            if (StringUtil.isEmpty(profileKey)) {
                errorMap.put("emailMsg", "user.email.wrong");
            }

            AuthProfile profile = UserCenterServiceSngl.get().register(loginKey, pasword, loginDomain, profileKey, nick, ip, createDate, HTTPUtil.getRequestToMap(request));



            writeAuthCookie(request, response, profile, appkey, loginDomain);

            if (!loginDomain.equals(LoginDomain.CLIENT)) {
                buildUserCenterSession(profile, request);
            }

            DiscuzUtil.register(profile.getProfile(), loginKey, new MD5Cryptor().getMD5ofStr(pasword).toLowerCase(), ip, new Date().getTime(), loginDomain, 0, request, response);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            if (e.equals(UserCenterServiceException.USERLOGIN_NOT_EXISTS)) {
                errorMap.put("emailMsg", "user.login.wrong");
            } else if (e.equals(UserCenterServiceException.PROFILE_NOT_EXISTS)) {
                errorMap.put("emailMsg", "user.login.wrong");
            } else if (e.equals(UserCenterServiceException.USERLOGIN_WRONG_LOGINDOMAIN)) {
                errorMap.put("emailMsg", ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getMsg());
            } else {
                errorMap.put("emailMsg", "system.error");
            }


        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            errorMap.put("emailMsg", "system.error");
        }

        if (!CollectionUtil.isEmpty(errorMap)) {
            mapMessage.put("errorMap", errorMap);
            mapMessage.put("reurl", reurl);
            return new ModelAndView("/views/jsp/passport/registerpage", mapMessage);
        }

        return new ModelAndView("redirect:" + reurl);
    }


    @RequestMapping("/loginpage")
    public ModelAndView loginPage(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam(value = "loginkey", required = false, defaultValue = "") String loginKey,
                                  @RequestParam(value = "of", required = false) String of,
                                  @RequestParam(value = "password", required = false, defaultValue = "") String pasword,
                                  @RequestParam(value = "reurl", required = false, defaultValue = "") String reurl,
                                  @RequestParam(value = "appkey", required = false, defaultValue = "default") String appkey) {

        Map map = new HashMap();
        WebHotdeployConfig config = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class);
        if (!config.getUserLoginTrigger() && !"lh".equals(of)) {
            map.put("message", "userlogin.not.open");
            return new ModelAndView("/views/jsp/common/custompage", map);
        }

        reurl = !StringUtil.isEmpty(reurl) ? reurl : request.getHeader("referer");
        if (!StringUtil.isEmpty(reurl) && reurl != "null") {
            try {
                map.put("reurl", URLEncoder.encode(reurl, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                GAlerter.lab(this.getClass().getName() + " URLEncoder encode url occur UnsupportedEncodingException.e", e);
            }
        }

        //wappc有值，手机跳PC
        if (AppUtil.checkMobile(request) && StringUtil.isEmpty(CookieUtil.getCookieValue(request, "jumpflag"))) {
            return new ModelAndView("/views/jsp/passport/loginpage-m", map);
        } else {
            return new ModelAndView("/views/jsp/passport/loginpage", map);
        }
    }

    @RequestMapping("/loginsimplepage")
    public ModelAndView loginSimplePage(HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam(value = "loginkey", required = false, defaultValue = "") String loginKey,
                                        @RequestParam(value = "of", required = false) String of,
                                        @RequestParam(value = "password", required = false, defaultValue = "") String pasword,
                                        @RequestParam(value = "reurl", required = false, defaultValue = "") String reurl,
                                        @RequestParam(value = "appkey", required = false, defaultValue = "default") String appkey) {
        Map map = new HashMap();
        WebHotdeployConfig config = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class);
        if (!config.getUserLoginTrigger() && !"lh".equals(of)) {
            map.put("message", "userlogin.not.open");
            return new ModelAndView("/views/jsp/common/custompage", map);
        }


        reurl = !StringUtil.isEmpty(reurl) ? reurl : request.getHeader("referer");
        if (!StringUtil.isEmpty(reurl) && reurl != "null") {
            try {
                map.put("reurl", URLEncoder.encode(reurl, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                GAlerter.lab(this.getClass().getName() + " URLEncoder encode url occur UnsupportedEncodingException.e", e);
            }
        }


        return new ModelAndView("/views/jsp/passport/login-simple", map);
    }

    @RequestMapping("/loginwappage")
    public ModelAndView loginWapPage(HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam(value = "loginkey", required = false, defaultValue = "") String loginKey,
                                     @RequestParam(value = "of", required = false) String of,
                                     @RequestParam(value = "password", required = false, defaultValue = "") String pasword,
                                     @RequestParam(value = "reurl", required = false, defaultValue = "") String reurl,
                                     @RequestParam(value = "appkey", required = false, defaultValue = "default") String appkey,
                                     @RequestParam(value = "channel", required = false, defaultValue = "default") String channel) {
        Map map = new HashMap();
        WebHotdeployConfig config = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class);
        if (!config.getUserLoginTrigger() && !"lh".equals(of)) {
            map.put("message", "userlogin.not.open");
            return new ModelAndView("/views/jsp/common/custompage", map);
        }

        reurl = !StringUtil.isEmpty(reurl) ? reurl : request.getHeader("referer");
        if (!StringUtil.isEmpty(reurl) && reurl != "null") {
            try {
                reurl = URLEncoder.encode(reurl, "UTF-8");
                map.put("reurl", reurl);
            } catch (UnsupportedEncodingException e) {
                GAlerter.lab(this.getClass().getName() + " URLEncoder encode url occur UnsupportedEncodingException.e", e);
            }
        }
        if (StringUtil.isEmpty(reurl)) {
            reurl = WebappConfig.get().getUrlWww();
        }

        //如果是wx渠道，ua判断其是在微信客户端打开，直接redirect 到微信的认证界面
        if (AppUtil.checkIsWeixin(request) && "wx".equals(channel)) {
            return new ModelAndView("redirect:/auth/thirdapi/wxserv/bind?reurl=" + reurl);
        }

        String rederurl = "views/jsp/passport/login/mlogin-" + channel;
        return new ModelAndView(rederurl, map);
    }


    @RequestMapping("/login")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "loginkey", required = false, defaultValue = "") String loginKey,
                              @RequestParam(value = "reurl", required = false, defaultValue = "") String reurl,
                              @RequestParam(value = "password", required = false, defaultValue = "") String pasword,
                              @RequestParam(value = "logindomain", required = false, defaultValue = "") String lDomainCode,
                              @RequestParam(value = "appkey", required = false, defaultValue = "default") String appkey) {

        LoginDomain loginDomain = StringUtil.isEmpty(lDomainCode) ? LoginDomain.EMAIL : (LoginDomain.getByCode(lDomainCode) == null ? LoginDomain.EMAIL : LoginDomain.getByCode(lDomainCode));
        String ip = getIp(request);
        Date createDate = new Date();

        Map mapMessage = new HashMap();
        reurl = getReurl(request);
        mapMessage.put("reurl", reurl);
        if (StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(appkey)) {
            mapMessage.put("message", "user.login.illegl");
            return new ModelAndView("/views/jsp/passport/loginpage", mapMessage);
        }

        try {
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                mapMessage.put("message", "user.login.illegl");
                return new ModelAndView("/views/jsp/passport/loginpage", mapMessage);
            }

            String profileKey = authApp.getProfileKey();
            if (StringUtil.isEmpty(profileKey)) {
                mapMessage.put("message", "user.login.illegl");
                return new ModelAndView("/views/jsp/passport/loginpage", mapMessage);
            }

            AuthProfile profile = UserCenterServiceSngl.get().login(loginKey, pasword, loginDomain, profileKey, ip, createDate, HTTPUtil.getRequestToMap(request));

            writeAuthCookie(request, response, profile, appkey, loginDomain);
            return getModelAndViewAfterLogin(profile, reurl, request, appkey, loginDomain);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            if (e.equals(UserCenterServiceException.USERLOGIN_NOT_EXISTS)) {
                mapMessage.put("message", "user.login.wrong");
                return new ModelAndView("/views/jsp/passport/loginpage", mapMessage);
            } else if (e.equals(UserCenterServiceException.PROFILE_NOT_EXISTS)) {
                mapMessage.put("message", "user.login.wrong");
                return new ModelAndView("/views/jsp/passport/loginpage", mapMessage);
            } else if (e.equals(UserCenterServiceException.USERLOGIN_WRONG_LOGINDOMAIN)) {
                mapMessage.put("message", ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getMsg());
                return new ModelAndView("/views/jsp/passport/loginpage", mapMessage);
            } else {
                mapMessage.put("message", "system.error");
                return new ModelAndView("/views/jsp/passport/loginpage", mapMessage);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            mapMessage.put("message", "system.error");
            return new ModelAndView("/views/jsp/passport/loginpage", mapMessage);
        }
    }


    @RequestMapping("/savenickpage")
    public ModelAndView saveNickPage(HttpServletRequest request, HttpServletResponse response) {
        Map mapMessage = new HashMap();
        UserCenterSession session = UserCenterCookieUtil.getUserCenterSeesion(request);
        if (session == null) {
            return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("profile.default.is.null"));
        }

        String ruerl = request.getParameter("reurl");
        String logindomain = request.getParameter("logindomain");
        String appKey = request.getParameter("appkey") == null ? "" : request.getParameter("appkey");
        mapMessage.put("nick", session.getNick());
        mapMessage.put("icon", session.getIcon());
        mapMessage.put("sex", session.getSex());
        mapMessage.put("uno", session.getUno());
        mapMessage.put("logindomain", logindomain);
        mapMessage.put("appkey", appKey);
        mapMessage.put("reurl", ruerl == null ? WebappConfig.get().getUrlWww() : ruerl);
        CookieUtil.deleteALLCookies(request, response);
        DiscuzUtil.logOutDiscuz(request, response);
        if (request.getSession() != null) {
            request.getSession().invalidate();
        }

        return new ModelAndView(getCompleteNickByRequest(request), mapMessage);
    }

    @RequestMapping("/savenick")
    public ModelAndView saveNick(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam(value = "nick", required = false) String nick) {
        Map mapMessage = new HashMap();
        String appkey = HTTPUtil.getParam(request, "appkey");
        String uno = HTTPUtil.getParam(request, "uno");
        String sex = HTTPUtil.getParam(request, "sex");
        String icon = HTTPUtil.getParam(request, "icon");
        String logindomain = HTTPUtil.getParam(request, "logindomain");

        String reurl = HTTPUtil.getParam(request, "reurl");
        mapMessage.put("nick", nick);
        mapMessage.put("uno", uno);
        mapMessage.put("icon", icon);
        mapMessage.put("sex", sex);
        mapMessage.put("logindomain", logindomain);
        mapMessage.put("reurl", reurl == null ? WebappConfig.get().getUrlWww() : reurl);

        appkey = StringUtil.isEmpty(appkey) ? "default" : appkey;
        try {
            if (StringUtil.isEmpty(nick)) {
                mapMessage.put("message", "nick.notempty");
                return new ModelAndView(getCompleteNickByRequest(request), mapMessage);
            }

            nick = nick.trim();
            if (StringUtil.isEmpty(nick)) {
                mapMessage.put("message", "nick.notempty");
                return new ModelAndView(getCompleteNickByRequest(request), mapMessage);
            }

            if (ContextFilterUtils.checkNickRegexs(nick)) {
                mapMessage.put("message", "nick.illegal");
                return new ModelAndView(getCompleteNickByRequest(request), mapMessage);
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                mapMessage.put("message", "app.notexists");
                return new ModelAndView(getCompleteNickByRequest(request), mapMessage);
            }

            String profileKey = authApp.getProfileKey();
            if (StringUtil.isEmpty(profileKey)) {
                mapMessage.put("message", "profilekey.notexists");
                return new ModelAndView(getCompleteNickByRequest(request), mapMessage);
            }

            String profileId = UserCenterUtil.getProfileId(uno, profileKey);
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);

            if (profile == null) {
                mapMessage.put("message", "profile.not.exists");
                return new ModelAndView(getCompleteNickByRequest(request), mapMessage);
            }

            if (!ContextFilterUtils.checkNickRegexs(profile.getNick())) {//不符合规则的老账号可以改一次
                if (profile.getFlag().hasFlag(ProfileFlag.FLAG_NICK_HASCOMPLETE)) {
                    return new ModelAndView("redirect:" + getReurl(request));
                }
            }

            Profile nickProfile = UserCenterServiceSngl.get().getProfileByNick(nick.toLowerCase());
            if (nickProfile != null && !nickProfile.getProfileId().equals(profileId)) {
                mapMessage.put("message", "nick.hadexists");
                return new ModelAndView(getCompleteNickByRequest(request), mapMessage);
            }

            UpdateExpress updateExpress = new UpdateExpress()
                    .set(ProfileField.NICK, nick)
                    .set(ProfileField.CHECKNICK, nick.toLowerCase());

            if (!profile.getFlag().hasFlag(ProfileFlag.FLAG_NICK_HASCOMPLETE)) {//已经修改过名字的
                updateExpress.set(ProfileField.FLAG, profile.getFlag().has(ProfileFlag.FLAG_NICK_HASCOMPLETE).getValue());
            }
            boolean bVal = UserCenterServiceSngl.get().modifyProfile(updateExpress, profile.getProfileId());

            if (bVal) {
                DiscuzUtil.modifyProfile(DiscuzModifyField.FIELD_SCREENNAME, nick, profile.getUid());
            }
            System.out.println("cookies token:"+UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_TOKEN));
            //todo 微服务改造
//            UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_TOKEN));
//            AuthProfile authProfile = UserCenterServiceSngl.get().getCurrentAccount();

            AuthProfile authProfile = UserCenterServiceSngl.get().getAuthProfileByUid(profile.getUid(), new HashMap<String, String>());


            writeAuthCookie(request, response, authProfile, appkey, LoginDomain.getByCode(logindomain));

            if ((AppUtil.checkIsAndroid(request) || AppUtil.checkIsIOS(request)) && !AppUtil.checkIsWeixin(request)) {
                return new ModelAndView("redirect:http://passport." + WebappConfig.get().getDomain() + "/appredirect/loginsuccess?reurl=" + URLEncoder.encode(reurl, "UTF-8"));
            } else {
                return new ModelAndView("redirect:" + getReurl(request));
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            mapMessage.put("message", "system.error");
            return new ModelAndView(getCompleteNickByRequest(request), mapMessage);
        }
    }


    private String getCompleteNickByRequest(HttpServletRequest request) {
        if (AppUtil.checkIsAndroid(request) || AppUtil.checkIsIOS(request)) {
            return "/views/jsp/passport/complete-nick-wap";
        } else {
            return "/views/jsp/passport/complete-nick";
        }
    }


    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        String returnUrl = null;
        try {
            try {
                String token = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_TOKEN);
                if (!StringUtil.isEmpty(token)) {
                    UserCenterServiceSngl.get().deleteToken(token);
                }
            } catch (Exception e) {
                GAlerter.lab(" logout clear token error.e:", e);
            }

            CookieUtil.deleteALLCookies(request, response);
            DiscuzUtil.logOutDiscuz(request, response);
            if (request.getSession() != null) {
                request.getSession().invalidate();
            }

            String reurl = getReurl(request);
            Pattern chinesePattern = Pattern.compile("[\\u0391-\\uFFE5]+");
            Matcher matcher = chinesePattern.matcher(reurl);

            returnUrl = reurl;
            while (matcher.find()) {
                String chineseUrl = matcher.group(0);
                returnUrl = returnUrl.replace(chineseUrl, URLEncoder.encode(chineseUrl, "UTF-8"));
            }
        } catch (Exception e) {
            GAlerter.lab(" logout error .e:", e);
        }
//        CookieUtil.setCookie(request,response,"JSESSIONID", UUID.randomUUID().toString(),-1);

//        return new ModelAndView("redirect:" + returnUrl);

        try {
            response.sendRedirect(returnUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
