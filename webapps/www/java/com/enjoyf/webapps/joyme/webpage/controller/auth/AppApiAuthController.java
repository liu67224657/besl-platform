package com.enjoyf.webapps.joyme.webpage.controller.auth;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.point.WanbaPointType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.qqv2.QqV2OpenId2UnionId;
import com.enjoyf.platform.util.sms.SMSSenderSngl;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.platform.webapps.common.wordfilter.WanbaResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.profile.MobileCodeDTO;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.weblogic.profile.MobileVerifyWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterCookieUtil;
import com.enjoyf.webapps.joyme.webpage.cache.SysCommCache;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by ericliu on 14/10/22.
 */
@Controller
@RequestMapping("/app/api")
public class AppApiAuthController extends AbstractAuthController {

    private Logger logger = LoggerFactory.getLogger(AppApiAuthController.class);

    @Resource(name = "mobileVerifyWebLogic")
    private MobileVerifyWebLogic mobileVerifyWebLogic;

    @Resource(name = "pointWebLogic")
    private PointWebLogic pointWebLogic;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    private final static String WANBA_APPKEY = "3iiv7VWfx84pmHgCUqRwun";

    /**
     * 自动注册
     * clientid required
     * appkey  required
     * logindomain required
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/auth")
    public String auth(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "otherid", required = false) String otherid,
                       @RequestParam(value = "logindomain", required = false) String logindomain,
                       @RequestParam(value = "openid", required = false) String openId,
                       @RequestParam(value = "nick", required = false, defaultValue = "") String nick,
                       @RequestParam(value = "icon", required = false, defaultValue = "") String icon,
                       @RequestParam(value = "appkey", required = false) String appkey) {

        JSONObject jsonObject = new JSONObject();

        LoginDomain loginDomain = LoginDomain.getByCode(logindomain);
        String ip = getIp(request);
        Date createDate = new Date();

        try {
            if (StringUtil.isEmpty(otherid) || StringUtil.isEmpty(appkey) || loginDomain == null) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(AppUtil.getAppKey(appkey));
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
            if (paramMap != null) {
                paramMap.remove(UserCenterUtil.UNO_STRING);
            }

            //注册放入appkey
            paramMap.put(UserCenterUtil.PROFILE_TABLE_APPKEY, authApp.getAppId());

            AuthProfile profile = UserCenterServiceSngl.get().auth(otherid, loginDomain, null, icon, nick, profileKey, ip, createDate, paramMap);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            //todo 微服务改造
            writeAuthCookieByMaxAge(request,response,profile,appkey,loginDomain,profile.getToken().getTokenExpires());
            int loginPoint = 0;
            if (!loginDomain.equals(LoginDomain.CLIENT) && AppUtil.getAppKey(appkey).equals(WANBA_APPKEY)) {
                //第三方首次登录或绑定
                loginPoint = pointWebLogic.modifyUserPoint(PointActionType.OTHER_LOGIN_AND_BIND, profile.getProfile().getProfileId(), AppUtil.getAppKey(appkey), WanbaPointType.THIRD, loginDomain);
            }

            jsonObject = getResultByAuthProfile(profile);
            String pointText = "";
            if (loginPoint > 0 && AppUtil.getAppKey(appkey).equals(WANBA_APPKEY)) {
                String domainText = "qq";
                if (loginDomain.equals(LoginDomain.SINAWEIBO)) {
                    domainText = "新浪微博";
                } else if (loginDomain.equals(LoginDomain.WXLOGIN)) {
                    domainText = "微信";
                }
                pointText = i18nSource.getMessage("point.bind.other.success", new Object[]{domainText, WanbaPointType.THIRD}, Locale.CHINA);
            }
            jsonObject.put("pointtext", pointText);
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
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

    }

    @RequestMapping(value = "/sign")
    @ResponseBody
    public String sign(@RequestParam(value = "appkey", required = false) String appkey,
                       @RequestParam(value = "profileid", required = false) String profileId,
                       @RequestParam(value = "logindomain", required = false) String logindomain) {
        try {
            LoginDomain loginDomain = LoginDomain.getByCode(logindomain);
            if (StringUtil.isEmpty(appkey) || StringUtil.isEmpty(profileId) || loginDomain == null || loginDomain.equals(LoginDomain.CLIENT)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }


            int value = 0;
            if (AppUtil.getAppKey(appkey).equals(WANBA_APPKEY)) {
                value = pointWebLogic.modifyUserPoint(PointActionType.WANBA_SIGN, profileId, appkey, WanbaPointType.SIGN, null);
            }
            JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
            String pointText = "";
            if (value > 0) {
                pointText = i18nSource.getMessage("point.sign.success", new Object[]{WanbaPointType.SIGN}, Locale.CHINA);
            }
            jsonObject.put("pointtext", pointText);
            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
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
                       @RequestParam(value = "uno", required = false) String uno,
                       @RequestParam(value = "code", required = false) String code,
                       @RequestParam(value = "password", required = false) String passWord,
                       @RequestParam(value = "appkey", required = false) String appkey
    ) {

        JSONObject jsonObject = new JSONObject();

        LoginDomain loginDomain = LoginDomain.getByCode(logindomain);
        String ip = getIp(request);
        Date createDate = new Date();
        HashMap<String, String> paramMap = HTTPUtil.getRequestToMap(request);


        try {
            if (StringUtil.isEmpty(otherid) || StringUtil.isEmpty(appkey) || loginDomain == null || loginDomain.equals(LoginDomain.CLIENT) || StringUtil.isEmpty(uno)) {
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
            //绑定手机号
            //todo 微服务改造
            UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
            if (LoginDomain.MOBILE.equals(loginDomain)) {
                if (StringUtil.isEmpty(passWord) || StringUtil.isEmpty(code)) {
                    jsonObject.put("rs", ResultCodeConstants.PARAM_EMPTY.getCode());
                    jsonObject.put("msg", ResultCodeConstants.PARAM_EMPTY.getMsg());
                    return jsonObject.toString();
                }
                //check mobilecode
//                String mobileCodeByServ = UserCenterServiceSngl.get().getMobileCode(otherid);
//                if (StringUtil.isEmpty(mobileCodeByServ) && !code.equals("36996")) {
//                    return ResultCodeConstants.MOBILE_VERIFY_CODE_NULL.getJsonString();
//                }
//                if (!code.equalsIgnoreCase(mobileCodeByServ) && !code.equals("36996")) {
//                    return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();
//                }
                //todo 微服务改造新增
                if(!UserCenterServiceSngl.get().checkMobileVerifyCode(otherid,code))
                    return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();

                String loginId = UserCenterUtil.getUserLoginId(otherid, LoginDomain.MOBILE);
                UserLogin userLogin = UserCenterServiceSngl.get().getUserLoginByLoginKey(otherid,LoginDomain.MOBILE);
                if (userLogin != null) {
                    return ResultCodeConstants.MOBILE_HAS_BINDED.getJsonString();
                }

                AuthProfile authProfile = UserCenterServiceSngl.get().bind(otherid, passWord, LoginDomain.MOBILE, profileKey, uno, ip, createDate, "", "", paramMap);
                if (authProfile == null) {
                    return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
                }
                int point = 0;
                String pointText = "";
                if (!loginDomain.equals(LoginDomain.CLIENT)) {
                    //判断手机号是否绑定别的账号
                    //mobileVerifyWebLogic.modifyMobileByReg(authProfile.getProfile().getProfileId(), otherid, authProfile.getProfile().getProfileKey(), ip);

                    //手机号首次登录或绑定
                    if (AppUtil.getAppKey(appkey).equals(WANBA_APPKEY)) {
                        point = pointWebLogic.modifyUserPoint(PointActionType.PHONE_LOGIN_AND_BIND, authProfile.getProfile().getProfileId(), AppUtil.getAppKey(appkey), WanbaPointType.PHONE, null);
                    }
                }
                if (point > 0) {
                    pointText = i18nSource.getMessage("point.bind.phone.success", new Object[]{WanbaPointType.PHONE}, Locale.CHINA);
                }
                JSONObject jsonObject1 = getResultByAuthProfile(authProfile);
                jsonObject1.put("pointtext", pointText);

                return jsonObject1.toString();
            } else {
                AuthProfile profile = UserCenterServiceSngl.get().bind(otherid, "", loginDomain, profileKey, uno, ip, createDate, "", "", paramMap);
                if (profile == null) {
                    jsonObject.put("rs", ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getCode());
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getMsg());
                    return jsonObject.toString();
                }
                int point = 0;
                String pointText = "";
                if (!loginDomain.equals(LoginDomain.CLIENT) && AppUtil.getAppKey(appkey).equals(WANBA_APPKEY)) {
                    //第三方首次登录或绑定
                    point = pointWebLogic.modifyUserPoint(PointActionType.OTHER_LOGIN_AND_BIND, profile.getProfile().getProfileId(), AppUtil.getAppKey(appkey), WanbaPointType.THIRD, loginDomain);
                }
                if (point > 0 && AppUtil.getAppKey(appkey).equals(WANBA_APPKEY)) {
                    String domainText = "qq";
                    if (loginDomain.equals(LoginDomain.SINAWEIBO)) {
                        domainText = "新浪微博";
                    } else if (loginDomain.equals(LoginDomain.WXLOGIN)) {
                        domainText = "微信";
                    }
                    pointText = i18nSource.getMessage("point.bind.other.success", new Object[]{domainText, WanbaPointType.THIRD}, Locale.CHINA);
                }
                JSONObject jsonObject1 = getResultByAuthProfile(profile);
                jsonObject1.put("pointtext", pointText);

                return jsonObject1.toString();
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            if (e.equals(UserCenterServiceException.PROFILE_HAS_EXISTS)) {
                return ResultCodeConstants.USERCENTER_PROFILE_HAS_EXISTS.getJsonString();
            } else if (e.equals(UserCenterServiceException.USERLOGIN_NOT_EXISTS)) {
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
            return jsonObject.toString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);

            jsonObject.put("rs", ResultCodeConstants.SYSTEM_ERROR.getCode());
            jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());
            return jsonObject.toString();
        }
    }

    @RequestMapping(value = "/unbind")
    @ResponseBody
    public String unbind(@RequestParam(value = "appkey", required = false) String appkey, HttpServletRequest request) {

        try {
            JSONObject jsonObject = new JSONObject();
            String uno = HTTPUtil.getParam(request, "uno");
            String logindomain = HTTPUtil.getParam(request, "logindomain");

            LoginDomain loginDomain = LoginDomain.getByCode(logindomain);
            if (StringUtil.isEmpty(uno) || loginDomain == null || loginDomain.equals(LoginDomain.MOBILE) || StringUtil.isEmpty(appkey)) {

                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
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
            String profileId = UserCenterUtil.getProfileId(uno, profileKey);
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                jsonObject.put("rs", ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getCode());
                jsonObject.put("msg", ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getMsg());
                return jsonObject.toString();
            }
            //判断能不能取消绑定
            if (!checkunbind(profile.getFlag())) {
                return WanbaResultCodeConstants.AUTH_UNBIND_ERROR_ONLYONE.getJsonString();
            }
            //todo 微服务改造
            UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
            UserCenterServiceSngl.get().unbind(loginDomain, profileKey, uno, getIp(request), new Date());

            jsonObject.put("rs", 1);
            jsonObject.put("msg", "success");
            jsonObject.put("flag", profile.getFlag().reduce(ProfileFlag.getFlagByLoginDomain(loginDomain)).getValue());

            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab("unbind occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }


    private boolean checkunbind(ProfileFlag profileFlag) throws ServiceException {
        if (profileFlag.hasFlag(ProfileFlag.FLAG_MOBILE)) {
            return true;
        }
        int i = 0;
        for (LoginDomain loginDomain : profileFlag.getLoginDomain()) {
            if (profileFlag.hasFlag(ProfileFlag.getFlagByLoginDomain(loginDomain))) {
                i++;
            }
            if (i > 1) {
                return true;
            }
        }
        return false;
    }


    /**
     * 发送手机验证码
     *
     * @param request
     * @param mobile
     * @return
     */
    @ResponseBody
    @RequestMapping("/mobile/sendcode")
    public String sendMobileCode(HttpServletRequest request,
                                 @RequestParam(value = "mobile", required = false) String mobile) {
        String vTemplate = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class)
                .getVerifyMobileSmsTemplate();
        try {
            UserLogin userLogin = UserCenterServiceSngl.get().getUserLoginByLoginKey(mobile, LoginDomain.MOBILE);
            if (userLogin != null) {
                return ResultCodeConstants.USERCENTER_USERLOGIN_HAS_EXISTS.getJsonString();
            }

            //发送验证码
            //MobileCodeDTO dto = mobileVerifyWebLogic.sendCode(mobile, vTemplate, MobileVerifyWebLogic.VERIFY_REG_TIMES, SMSSenderSngl.CODE_WANBA);

            //if (dto.getRs() == MobileCodeDTO.RS_SUCCESS) {
            boolean result = UserCenterServiceSngl.get().sendMobileNo(mobile);
            if(result){
                //UserCenterServiceSngl.get().saveMobileCode(mobile, dto.getCode());
                return ResultCodeConstants.SUCCESS.getJsonString();
            }
//            else if (dto.getRs() == MobileCodeDTO.RS_ERROR_OUT_LIMIT) {
//                return ResultCodeConstants.MOBILE_VERIFY_CODE_OUTLIMIT.getJsonString();
//            }
            else {
                return ResultCodeConstants.MOBILE_VERIFY_CODE_OUTLIMIT.getJsonString();
            }
        }catch (UserCenterServiceException uex){
            if(uex.equals(UserCenterServiceException.PHONE_CODE_LIMIT))
                return ResultCodeConstants.MOBILE_VERIFY_CODE_OUTLIMIT.getJsonString();
            else if(uex.equals(UserCenterServiceException.BIND_PHONE_ERROR_PHONE_HAS_BINDED))
                return ResultCodeConstants.USERCENTER_USERLOGIN_HAS_EXISTS.getJsonString();
            else

                return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
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
                           @RequestParam(value = "nick", required = false) String nick,
                           @RequestParam(value = "mobilecode", required = false) String mobilecode,
                           @RequestParam(value = "logindomain", required = false) String logindomain,
                           @RequestParam(value = "appkey", required = false) String appkey) {

        LoginDomain loginDomain = LoginDomain.getByCode(logindomain);
        String ip = getIp(request);
        Date createDate = new Date();

        try {
            if (StringUtil.isEmpty(loginKey)
                    || StringUtil.isEmpty(logindomain)
                    || StringUtil.isEmpty(mobilecode)) {
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

            //
            if (StringUtil.isEmpty(nick) || StringUtil.isEmpty(nick.trim())) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            if (ContextFilterUtils.checkNickRegexs(nick)) {
                return ResultCodeConstants.USERCENTER_NICK_ILLEGAL.getJsonString();
            }

            //check checkNick is empty
            if (UserCenterServiceSngl.get().getProfileByNick(nick) != null) {
                return ResultCodeConstants.USERCENTER_NICK_HAS_EXISTS.getJsonString();
            }

            String loginId = UserCenterUtil.getUserLoginId(loginKey, loginDomain);

            //check mobilecode
//            String mobileCodeByServ = UserCenterServiceSngl.get().getMobileCode(loginKey);
//            if (StringUtil.isEmpty(mobileCodeByServ) && !mobilecode.equals("36996")) {
//                return ResultCodeConstants.MOBILE_VERIFY_CODE_NULL.getJsonString();
//            }
//            if (!mobilecode.equalsIgnoreCase(mobileCodeByServ) && !mobilecode.equals("36996")) {
//                return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();
//            }
            //todo 微服务改造新增
            if(!UserCenterServiceSngl.get().checkMobileVerifyCode(loginKey,mobilecode))
                return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();
            HashMap<String, String> paramMap = HTTPUtil.getRequestToMap(request);
            //注册放入appkey
            paramMap.put(UserCenterUtil.PROFILE_TABLE_APPKEY, authApp.getAppId());

            AuthProfile profile = UserCenterServiceSngl.get().register(loginKey, pasword, loginDomain, profileKey, nick, ip, createDate, paramMap);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            //todo 微服务改造
            writeAuthCookieByMaxAge(request,response,profile,appkey,loginDomain,profile.getToken().getTokenExpires());

            int loginPoint = 0;
            if (loginDomain != null && loginDomain.equals(LoginDomain.MOBILE)) {
                //判断手机号是否绑定别的账号 todo 可以去掉
//                mobileVerifyWebLogic.modifyMobileByReg(profile.getProfile().getProfileId(), loginKey, profile.getProfile().getProfileKey(), ip);
                if (AppUtil.getAppKey(appkey).equals(WANBA_APPKEY)) {
                    loginPoint = pointWebLogic.modifyUserPoint(PointActionType.PHONE_LOGIN_AND_BIND, profile.getProfile().getProfileId(), com.enjoyf.platform.util.http.AppUtil.getAppKey(appkey), WanbaPointType.PHONE, null);
                }
            }
            String pointText = "";
            if (loginPoint > 0 && AppUtil.getAppKey(appkey).equals(WANBA_APPKEY)) {
                pointText = i18nSource.getMessage("point.bind.phone.success", new Object[]{WanbaPointType.PHONE}, Locale.CHINA);
            }
            JSONObject jsonObject = getResultByAuthProfile(profile);
            jsonObject.put("pointtext", pointText);
            jsonObject.put("extmsg",i18nSource.getMessage("register.success", new Object[]{}, Locale.CHINA));

            UserCenterServiceSngl.get().removeMobileCode(loginKey);

            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            if (e.equals(UserCenterServiceException.USERLOGIN_HAS_EXISTS)) {
                return ResultCodeConstants.USERCENTER_USERLOGIN_HAS_EXISTS.getJsonString();
            } else if (e.equals(UserCenterServiceException.USERLOGIN_NOT_EXISTS)) {
                return ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString();
            } else if (e.equals(UserCenterServiceException.PROFILE_NOT_EXISTS)) {
                return ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString();
            } else if (e.equals(UserCenterServiceException.USERLOGIN_WRONG_LOGINDOMAIN)) {
                return ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getJsonString();
            } else if (e.equals(UserCenterServiceException.BIND_PHONE_ERROR_PHONE_HAS_BINDED)) {
                return ResultCodeConstants.MOBILE_HAS_BINDED.getJsonString();
            } else if(e.equals(UserCenterServiceException.PHONE_VERIFY_CODE_ERROR))
                return  ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();
            else {
                return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    @ResponseBody
    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "loginkey", required = false) String loginKey,
                        @RequestParam(value = "password", required = false) String password,
                        @RequestParam(value = "appkey", required = false) String appkey) {

        try {
            if (StringUtil.isEmpty(loginKey)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            //mobile
            String ip = getIp(request);
            Date createDate = new Date();
            LoginDomain loginDomain = LoginDomain.getLoginDomainByLoginKey(loginKey);
            if (loginDomain == null) {
                return ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getJsonString();
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                return ResultCodeConstants.APP_NOT_EXISTS.getJsonString();
            }

            String profileKey = authApp.getProfileKey();
            if (StringUtil.isEmpty(profileKey)) {
                return ResultCodeConstants.APP_NOT_EXISTS.getJsonString();
            }

            AuthProfile profile = UserCenterServiceSngl.get().login(loginKey, password, loginDomain, profileKey, ip, createDate, HTTPUtil.getRequestToMap(request));
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            //todo 微服务改造
            writeAuthCookieByMaxAge(request,response,profile,appkey,loginDomain,profile.getToken().getTokenExpires());

//            if (ContextFilterUtils.checkNickRegexs(profile.getProfile().getNick())) {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("rs", ResultCodeConstants.USERCENTER_NICK_ILLEGAL.getCode());
//                jsonObject.put("msg", ResultCodeConstants.USERCENTER_NICK_ILLEGAL.getMsg());
//                jsonObject.put("logindomain", loginDomain.getCode());
//                return jsonObject.toString();
//            }
            //手机号首次登录或绑定
            int loginPoint = 0;
            if (AppUtil.getAppKey(appkey).equals(WANBA_APPKEY)) {
                loginPoint = pointWebLogic.modifyUserPoint(PointActionType.PHONE_LOGIN_AND_BIND, profile.getProfile().getProfileId(), AppUtil.getAppKey(appkey), WanbaPointType.PHONE, null);
            }
            JSONObject jsonObject = getResultByAuthProfile(profile);
            String pointText = "";
            if (loginPoint > 0 && AppUtil.getAppKey(appkey).equals(WANBA_APPKEY)) {
                pointText = i18nSource.getMessage("point.bind.phone.success", new Object[]{WanbaPointType.PHONE}, Locale.CHINA);
            }
            jsonObject.put("pointtext", pointText);

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
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    /**
     * 修改绑定手机-发送验证码
     *
     * @param request
     * @param response
     * @param mobile
     * @return
     */

    private final String PATH_VALIDATE_OLD_PHONE = "validop"; //验证旧手机
    private final String PATH_VALIDATE_NEW_PHONE = "validnp"; //验证新手机
    private final String VALID = "validate_";

    @ResponseBody
    @RequestMapping(value = "/validate/sendcode")
    public String validateOldPhoneSendCode(HttpServletRequest request, HttpServletResponse response,
                                           @RequestParam(value = "type", required = false) String type,
                                           @RequestParam(value = "mobile", required = false) String mobile) {
        try {
            String uno = HTTPUtil.getParam(request, "uno");
            if (StringUtil.isEmpty(mobile) || StringUtil.isEmpty(uno)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            String tmpe = "";
            if (PATH_VALIDATE_OLD_PHONE.equals(type)) {

                //验证本账号绑定的手机是否与要修改的手机一致 不一致不发送验证码并返回错误提示
                Set<LoginDomain> loginDomains = new HashSet<LoginDomain>();
                loginDomains.add(LoginDomain.MOBILE);
                List<UserLogin> userLogins = UserCenterServiceSngl.get().queryUserLoginUno(uno, loginDomains);
                if (CollectionUtil.isEmpty(userLogins)) {
                    return WanbaResultCodeConstants.WANBA_VALIDATE_OLD_PHONE_FAIL.getJsonString();
                }
                UserLogin userLogin = userLogins.get(0);
                if (!userLogin.getLoginKey().equals(mobile)) {
                    return WanbaResultCodeConstants.WANBA_VALIDATE_OLD_PHONE_FAIL.getJsonString();
                }
                tmpe = PATH_VALIDATE_OLD_PHONE;
            } else if (PATH_VALIDATE_NEW_PHONE.equals(type)) {
                //验证手机号是否已经被注册
                //String loginId = UserCenterUtil.getUserLoginId(mobile, LoginDomain.MOBILE);
                UserLogin userLogin = UserCenterServiceSngl.get().getUserLoginByLoginKey(mobile,LoginDomain.MOBILE);
                if (userLogin != null) {
                    return ResultCodeConstants.MOBILE_HAS_BINDED.getJsonString();
                }
                //todo 微服务改造
//                String oldValidCode = UserCenterServiceSngl.get().getMobileCode(VALID + uno);
//                if (StringUtil.isEmpty(oldValidCode)) {
//                    //没有验证旧手机或者已过期
//                    return WanbaResultCodeConstants.VALIDATE_OLD_PHONE_EMPTY.getJsonString();
//                }
                tmpe = PATH_VALIDATE_NEW_PHONE;

            }
//            String vTemplate = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class)
//                    .getVerifyMobileSmsTemplate();
//            //发送验证码
//            MobileCodeDTO dto = mobileVerifyWebLogic.sendCode(mobile, vTemplate, MobileVerifyWebLogic.VERIFY_REG_TIMES, SMSSenderSngl.CODE_WANBA);
//
//            if (dto.getRs() == MobileCodeDTO.RS_SUCCESS) {
//                UserCenterServiceSngl.get().saveMobileCode(tmpe + mobile, dto.getCode());
            //todo 微服务改造
            UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
            boolean result = UserCenterServiceSngl.get().sendMobileCodeLogin(mobile);
            if(result){
                return ResultCodeConstants.SUCCESS.getJsonString();
            }
//            else if (dto.getRs() == MobileCodeDTO.RS_ERROR_OUT_LIMIT) {
//                return ResultCodeConstants.MOBILE_VERIFY_CODE_OUTLIMIT.getJsonString();
//            }
            else {
                return ResultCodeConstants.MOBILE_VERIFY_CODE_OUTLIMIT.getJsonString();
            }
        } catch (UserCenterServiceException ue){
            if(ue.equals(UserCenterServiceException.PHONE_CODE_LIMIT))
                return ResultCodeConstants.MOBILE_VERIFY_CODE_OUTLIMIT.getJsonString();
            if(ue.equals(UserCenterServiceException.BIND_PHONE_ERROR_PHONE_HAS_BINDED))
                return ResultCodeConstants.MOBILE_HAS_BINDED.getJsonString();
            else
                return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    /**
     * 更换绑定手机
     *
     * @param request
     * @param response
     * @param path     path=validop （验证旧手机验证码是否正确）
     *                 path=validnp (验证新手机验证码是否正确 正确则把绑定的手机换替换为新手机)
     * @param mobile
     * @param code     验证码
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{path}")
    public String validPhone(HttpServletRequest request, HttpServletResponse response,
                             @PathVariable String path,
                             @RequestParam(value = "mobile", required = false) String mobile,
                             @RequestParam(value = "code", required = false) String code) {
        String uno = HTTPUtil.getParam(request, "uno");
        if (StringUtil.isEmpty(code) || StringUtil.isEmpty(mobile) || StringUtil.isEmpty(uno)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        try {
            String temp = "";
            if (PATH_VALIDATE_OLD_PHONE.equals(path)) {
                temp = PATH_VALIDATE_OLD_PHONE;
            } else if (PATH_VALIDATE_NEW_PHONE.equals(path)) {
                temp = PATH_VALIDATE_NEW_PHONE;
            }
            //check mobilecode
//            String mobileCodeByServ = UserCenterServiceSngl.get().getMobileCode(temp + mobile);
//            if (StringUtil.isEmpty(mobileCodeByServ) && !code.equals("36996")) {
//                return ResultCodeConstants.MOBILE_VERIFY_CODE_NULL.getJsonString();
//            }
//            if (!code.equalsIgnoreCase(mobileCodeByServ) && !code.equals("36996")) {
//                return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();
//            }
            //todo 微服务改造
            UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
            if(!UserCenterServiceSngl.get().verifyCodeLogin(mobile,code))
                return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();
            if (PATH_VALIDATE_OLD_PHONE.equals(path)) {
                //UserCenterServiceSngl.get().saveMobileCode(VALID + uno, code);
                return ResultCodeConstants.SUCCESS.getJsonString();
            } else if (PATH_VALIDATE_NEW_PHONE.equals(path)) {
                //todo 微服务改造
//                String validCode = UserCenterServiceSngl.get().getMobileCode(VALID + uno);
//                if (StringUtil.isEmpty(validCode)) {
//                    //没有验证旧手机或者已过期
//                    return WanbaResultCodeConstants.VALIDATE_OLD_PHONE_EMPTY.getJsonString();
//                }

//                Set<LoginDomain> loginDomains = new HashSet<LoginDomain>();
//                loginDomains.add(LoginDomain.MOBILE);
//                List<UserLogin> userLogins = UserCenterServiceSngl.get().queryUserLoginUno(uno, loginDomains);
//                if (CollectionUtil.isEmpty(userLogins)) {
//                    return WanbaResultCodeConstants.WANBA_VALIDATE_OLD_PHONE_FAIL.getJsonString();
//                }
                //todo 微服务改造
//                UserLogin oldUserLogin = userLogins.get(0);
//                if (oldUserLogin == null) {
//                    return WanbaResultCodeConstants.WANBA_VALIDATE_OLD_PHONE_FAIL.getJsonString();
//                }

//                String loginId = UserCenterUtil.getUserLoginId(mobile, LoginDomain.MOBILE);
//                UserLogin userLogin = UserCenterServiceSngl.get().getUserLoginByLoginId(loginId);
//                if (userLogin != null) {
//                    return ResultCodeConstants.MOBILE_HAS_BINDED.getJsonString();
//                }
//                //修改绑定的手机号
//                boolean bool = UserCenterServiceSngl.get().modifyUserLogin(new UpdateExpress()
//                        .set(UserLoginField.LOGIN_KEY, mobile)
//                        .set(UserLoginField.LOGIN_ID, loginId), oldUserLogin.getLoginId());
                UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
                com.enjoyf.platform.userservice.client.model.UserLogin login = UserCenterServiceSngl.get().changeMobileNo(mobile);

                if (login!=null) {
                    //Profile profile = UserCenterServiceSngl.get().getProfileByUno(oldUserLogin.getUno(), "www");
                    mobileVerifyWebLogic.modifyMobileByReg(HTTPUtil.getParam(request, "profileid"), mobile, getIp(request));

                    UserCenterServiceSngl.get().removeMobileCode(VALID + uno);
                    return ResultCodeConstants.SUCCESS.getJsonString();
                } else {
                    return ResultCodeConstants.FAILED.getJsonString();
                }
            }
        } catch (UserCenterServiceException uex){
            return ResultCodeConstants.MOBILE_HAS_BINDED.getJsonString();
        }
        catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return ResultCodeConstants.FAILED.getJsonString();
    }

    private final String MODIFY_PASS = "modifypassword";

    @ResponseBody
    @RequestMapping("/existsmobile/sendcode")
    public String sendcode(HttpServletRequest request,
                           @RequestParam(value = "mobile", required = false) String mobile,
                           @RequestParam(value = "uno", required = false) String uno,
                           @RequestParam(value = "type", required = false) String type) {
//        String vTemplate = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class)
//                .getVerifyMobileSmsTemplate();
        try {

            if (StringUtil.isEmpty(type) || StringUtil.isEmpty(mobile) || StringUtil.isEmpty(uno)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            UserLogin userLogin = UserCenterServiceSngl.get().getUserLoginByLoginKey(mobile, LoginDomain.MOBILE);
            if (userLogin == null) {
                return ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString();
            }
            if (type.equals(MODIFY_PASS)) {
                if (!uno.equals(userLogin.getUno())) {
                    return WanbaResultCodeConstants.MODIFY_MOBILE_ERROR.getJsonString();
                }
            }

//            MobileCodeDTO dto = mobileVerifyWebLogic.sendCode(mobile, vTemplate, MobileVerifyWebLogic.VERIFY_REG_TIMES, SMSSenderSngl.CODE_WANBA);
//
//            if (dto.getRs() == MobileCodeDTO.RS_SUCCESS) {
//                UserCenterServiceSngl.get().saveMobileCode(mobile, dto.getCode());
            if(UserCenterServiceSngl.get().sendMobileNo(mobile)){
                return ResultCodeConstants.SUCCESS.getJsonString();
            }
//            else if (dto.getRs() == MobileCodeDTO.RS_ERROR_OUT_LIMIT) {
//                return ResultCodeConstants.MOBILE_VERIFY_CODE_OUTLIMIT.getJsonString();
//
//            }
            else {
                return ResultCodeConstants.MOBILE_VERIFY_CODE_SENDERROR.getJsonString();

            }
        } catch (UserCenterServiceException ue){
            if(ue.equals(UserCenterServiceException.PHONE_CODE_LIMIT))
                return ResultCodeConstants.MOBILE_VERIFY_CODE_OUTLIMIT.getJsonString();
            else
                return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();

        }
    }

    @ResponseBody
    @RequestMapping("/recover/password")
    public String forgetPassword(HttpServletRequest request,HttpServletResponse response,
                                 @RequestParam(value = "appkey", required = false) String appkey) {
        String password = HTTPUtil.getParam(request, "password");
        String loginKey = HTTPUtil.getParam(request, "mobile");
        String mobileCode = HTTPUtil.getParam(request, "mobilecode");
        String domain = HTTPUtil.getParam(request,"jmuc_lgdomain");
        LoginDomain loginDomain = LoginDomain.MOBILE;

        if (StringUtil.isEmpty(password)
                || StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(password) || StringUtil.isEmpty(appkey)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        try {
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                return ResultCodeConstants.APP_NOT_EXISTS.getJsonString();
            }

            String profileKey = authApp.getProfileKey();
            if (StringUtil.isEmpty(profileKey)) {
                return ResultCodeConstants.APP_NOT_EXISTS.getJsonString();
            }

            //check mobilecode
            //todo 微服务改造
//            String mobileCodeByServ = UserCenterServiceSngl.get().getMobileCode(loginKey);
//            if (StringUtil.isEmpty(mobileCodeByServ) && !mobileCode.equals("36996")) {
//                return ResultCodeConstants.MOBILE_VERIFY_CODE_NULL.getJsonString();
//
//            }
//            if (!mobileCode.equalsIgnoreCase(mobileCodeByServ) && !mobileCode.equals("36996")) {
//                return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();
//
//            }

            //todo 微服务改造
            String loginId = UserCenterUtil.getUserLoginId(loginKey, loginDomain);

            String passwordTime = String.valueOf(System.currentTimeMillis());
            String passwordSaveDb = UserCenterUtil.getPassowrd(password, passwordTime);


            UserLogin userLogin = UserCenterServiceSngl.get().getUserLoginByLoginKey(loginKey,loginDomain);
            if (userLogin == null) {
                return ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString();

            }

            //修改的密码和上一次密码一样
            String checkOldPwd = UserCenterUtil.getPassowrd(password, userLogin.getPasswdTime());
            if (checkOldPwd.equals(userLogin.getLoginPassword())) {
                return ResultCodeConstants.USERCENTER_PASSWORD_SAME_OLD.getJsonString();

            }
            //todo 微服务改造

            UserCenterServiceSngl.get().forgetPassword(loginKey,mobileCode,password);
//            boolean bVal = UserCenterServiceSngl.get().modifyUserLogin(new UpdateExpress()
//                    .set(UserLoginField.LOGIN_PASSWORD, passwordSaveDb).set(UserLoginField.PASSWDTIME, passwordTime), userLogin.getLoginId());

            //if (bVal) {
              //  UserCenterServiceSngl.get().removeMobileCode(loginKey);
            //}

            //AuthProfile profile = UserCenterServiceSngl.get().getAuthProfileByUno(userLogin.getUno(), profileKey, HTTPUtil.getRequestToMap(request));
            //todo 微服务改造
            AuthProfile profile= UserCenterServiceSngl.get().login(loginKey,password,loginDomain,profileKey,null,null,HTTPUtil.getRequestToMap(request));
            writeAuthCookieByMaxAge(request,response,profile,appkey,loginDomain,profile.getToken().getTokenExpires());
            return getResultByAuthProfile(profile).toString();

        } catch (UserCenterServiceException ue){
            if (ue.equals(UserCenterServiceException.PHONE_VERIFY_CODE_ERROR))
                return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();
            else
                return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    @RequestMapping(value = "/savebaseinfo")
    @ResponseBody
    public String ajaxsavebaseinfo(HttpServletRequest request,
                                   @RequestParam(value = "icon", required = false) String icon,
                                   @RequestParam(value = "description", required = false) String description,
                                   @RequestParam(value = "sex", required = false) String sex,
                                   @RequestParam(value = "provinceid", required = false) String provinceId,
                                   @RequestParam(value = "logindomain", required = false) String logindDomain,
                                   @RequestParam(value = "cityid", required = false) String cityId,
                                   @RequestParam(value = "appkey", required = false) String appkey,
                                   @RequestParam(value = "uno", required = false) String uno) {
        try {
            LoginDomain loginDomain = LoginDomain.getByCode(logindDomain);

            if (StringUtil.isEmpty(uno) || StringUtil.isEmpty(appkey) || loginDomain == null || logindDomain.equals(LoginDomain.CLIENT)) {
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

            String profileId = UserCenterUtil.getProfileId(uno, profileKey);


            UpdateExpress updateExpress = new UpdateExpress()
                    .set(ProfileField.DESCRIPTION, StringUtil.isEmpty(description) ? "" : HtmlUtils.htmlEscape(description))
                    .set(ProfileField.UPDATETIME, new Timestamp(new Date().getTime()))
                    .set(ProfileField.PROVINCEID, StringUtil.isEmpty(provinceId) ? null : Integer.parseInt(provinceId))
                    .set(ProfileField.CITYID, StringUtil.isEmpty(cityId) ? null : Integer.parseInt(cityId))

                    .set(ProfileField.SEX, StringUtil.isEmpty(sex) ? "" : sex);

            if (!StringUtil.isEmpty(icon)) {
                updateExpress.set(ProfileField.ICON, icon);
            }

            boolean bool = UserCenterServiceSngl.get().modifyProfile(updateExpress, profileId);
            if (bool) {
                //AuthProfile profile = UserCenterServiceSngl.get().getAuthProfileByUno(uno, profileKey, HTTPUtil.getRequestToMap(request));
                //todo 微服务改造
                UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
                AuthProfile profile = UserCenterServiceSngl.get().getCurrentAccount();
                if (profile == null) {
                    return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
                }
                Set<LoginDomain> loginDomains = new HashSet<LoginDomain>();
                loginDomains.add(loginDomain);

                List<UserLogin> userLogins = UserCenterServiceSngl.get().queryUserLoginUno(uno, loginDomains);
                if (CollectionUtil.isEmpty(userLogins)) {
                    return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
                }
                profile.setUserLogin(userLogins.get(0));
                return getResultByAuthProfile(profile).toString();

            } else {
                return ResultCodeConstants.FAILED.getJsonString();
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
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

//            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
//            if (authApp == null) {
//                return ResultCodeConstants.APP_NOT_EXISTS.getJsonString();
//            }
//
//            String profileKey = authApp.getProfileKey();
//            if (StringUtil.isEmpty(profileKey)) {
//                return ResultCodeConstants.APP_NOT_EXISTS.getJsonString();
//            }
//
//            long uid = -1;
//            try {
//                uid = Long.parseLong(uidString);
//            } catch (NumberFormatException e) {
//            }
//
//            if (uid == -1) {
//                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
//            }
//
//            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
//            if (profile == null) {
//                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
//            }
//
//            if (profile.getFlag().hasFlag(ProfileFlag.FLAG_NICK_HASCOMPLETE)) {
//                return ResultCodeConstants.USERCENTER_NICK_HAS_MODIFY.getJsonString();
//            }
//
//            Profile nickProfile = UserCenterServiceSngl.get().getProfileByNick(nick);
//            if (nickProfile != null && nickProfile.getUid() != uid) {
//                return ResultCodeConstants.USERCENTER_NICK_HAS_EXISTS.getJsonString();
//            }
//            if (ContextFilterUtils.checkNickRegexs(nick)) {
//                return ResultCodeConstants.CONTEXT_CONTENT_ILLEGAL.getJsonString();
//            }
//
//            UserCenterServiceSngl.get().modifyProfile(new UpdateExpress()
//                            .set(ProfileField.NICK, nick)
//                            .set(ProfileField.CHECKNICK, nick.toLowerCase())
//                            .set(ProfileField.FLAG, profile.getFlag().has(ProfileFlag.FLAG_NICK_HASCOMPLETE).getValue())
//                    , profile.getProfileId());
            //todo 微服务改造
            UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
            Profile profile = UserCenterServiceSngl.get().updateNick(nick);

            jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            jsonObject.put("flag", profile.getFlag().getValue());

            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);

            if (e.equals(UserCenterServiceException.USERLOGIN_NOT_EXISTS)) {
                return ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString();
            } else if (e.equals(UserCenterServiceException.NICK_HAS_EXIST)) {
                return ResultCodeConstants.USERCENTER_NICK_HAS_EXISTS.getJsonString();
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


    @RequestMapping(value = "/cityinfo")
    @ResponseBody
    public String ciryInfo(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("regionlist", SysCommCache.get().getRegionMap().values());

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
