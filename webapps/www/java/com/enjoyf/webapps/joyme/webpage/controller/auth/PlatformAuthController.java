package com.enjoyf.webapps.joyme.webpage.controller.auth;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.point.WanbaPointType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.luosimao.LuosimaoUtil;
import com.enjoyf.platform.util.sms.SMSSenderSngl;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.webapps.joyme.dto.profile.MobileCodeDTO;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.weblogic.profile.MobileVerifyWebLogic;
import com.enjoyf.webapps.joyme.weblogic.user.DiscuzModifyField;
import com.enjoyf.webapps.joyme.weblogic.user.DiscuzUtil;
import com.enjoyf.webapps.joyme.webpage.controller.servapi.ServapiAbstractAuthController;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/platform/auth")
public class PlatformAuthController extends ServapiAbstractAuthController {

//    private Logger logger = LoggerFactory.getLogger(ServapiAuthController.class);

    @Resource(name = "mobileVerifyWebLogic")
    private MobileVerifyWebLogic mobileVerifyWebLogic;


    @Resource(name = "pointWebLogic")
    private PointWebLogic pointWebLogic;

    @ResponseBody
    @RequestMapping
    public String auth(HttpServletRequest request,
                       @RequestParam(value = "otherid", required = false) String otherid,
                       @RequestParam(value = "logindomain", required = false) String logindomain,
                       @RequestParam(value = "nick", required = false, defaultValue = "") String nick,
                       @RequestParam(value = "icon", required = false, defaultValue = "") String icon,
                       @RequestParam(value = "profilekey", required = false) String profileKey) {

        String uno = HTTPUtil.getParam(request, "uno");

        GAlerter.lan(this.getClass().getName() + "  ApiAuth:nickname=" + nick + " uid=" + icon + " logindomain=" + logindomain);

        LoginDomain loginDomain = LoginDomain.getByCode(logindomain);
        String ip = getIp(request);
        Date createDate = new Date();

        try {
            if (StringUtil.isEmpty(otherid) || StringUtil.isEmpty(profileKey) || loginDomain == null) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            String loginId = UserCenterUtil.getUserLoginId(otherid, loginDomain);

            //profile不为空 且flag只有client 绑定操作
            HashMap<String, String> paramMap = HTTPUtil.getRequestToMap(request);
            if (!loginDomain.equals(LoginDomain.CLIENT) && !StringUtil.isEmpty(uno)) {
                Profile profileByUno = UserCenterServiceSngl.get().getProfileByProfileId(UserCenterUtil.getProfileId(uno, profileKey));
                if (profileByUno != null && profileByUno.getFlag().equalFlag(ProfileFlag.FLAG_CLIENTID)) {
                    try {

                        AuthProfile profile = UserCenterServiceSngl.get().bind(otherid, "", loginDomain, profileKey, uno, ip, createDate, icon, nick, paramMap);
                        if (profile != null) {
                            return getResultByAuthProfile(profile, loginId, loginDomain).toString();
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

            return getResultByAuthProfile(profile, loginId, loginDomain).toString();
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
    @RequestMapping("/mobile/sendcode")
    public String sendMobileCode(HttpServletRequest request,
                                 @RequestParam(value = "mobile", required = false) String mobile,
                                 @RequestParam(value = "luotestresponse", required = false) String luotest_response) {
        String vTemplate = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class)
                .getVerifyMobileSmsTemplate();
        try {
            boolean bval = LuosimaoUtil.verify(luotest_response);
            if (!bval || StringUtil.isEmpty(luotest_response)) {
                return "platformauthcallback([" + ResultCodeConstants.LOGIN_LUOSIMAO_IS_ERROR.getJsonString() + "])";
            }

            //验证手机号是否能用。如手机号已存在，就不发送验证码了。
//            String checkmobile = request.getParameter("checkmobile");
//            if (!StringUtil.isEmpty(checkmobile) && checkmobile.equals("true")) {
//                String newLoginId = UserCenterUtil.getUserLoginId(mobile, LoginDomain.MOBILE);
//                UserLogin newUserLogin = UserCenterServiceSngl.get().getUserLoginByLoginId(newLoginId);
//                if (newUserLogin != null) {
//                    return "platformauthcallback([" + ResultCodeConstants.USERCENTER_USERLOGIN_HAS_EXISTS.getJsonString() + "])";
//                }
//            }

            //MobileCodeDTO dto = mobileVerifyWebLogic.sendCode(mobile, vTemplate, MobileVerifyWebLogic.VERIFY_REG_TIMES, SMSSenderSngl.CODE_DEFAULT);
            boolean result = UserCenterServiceSngl.get().sendMobileNo(mobile);
            //if (dto.getRs() == MobileCodeDTO.RS_SUCCESS) {
            if(result){
                //把生成code存入session
//                request.getSession().setAttribute(Constant.SESSION_MOBILE_CODE, dto.getCode());
//                UserCenterServiceSngl.get().saveMobileCode(mobile, dto.getCode());

                return "platformauthcallback([" + ResultCodeConstants.SUCCESS.getJsonString() + "])";
            }
//            else if (dto.getRs() == MobileCodeDTO.RS_ERROR_OUT_LIMIT) {
//                return "platformauthcallback([" + ResultCodeConstants.MOBILE_VERIFY_CODE_OUTLIMIT.getJsonString() + "])";
//            }
            else {
                return "platformauthcallback([" + ResultCodeConstants.MOBILE_VERIFY_CODE_SENDERROR.getJsonString() + "])";
            }
        }catch (UserCenterServiceException ue){
            if(ue.equals(UserCenterServiceException.BIND_PHONE_ERROR_PHONE_HAS_BINDED))
                return "platformauthcallback([" + ResultCodeConstants.USERCENTER_USERLOGIN_HAS_EXISTS.getJsonString() + "])";
            else if(ue.equals(UserCenterServiceException.PHONE_CODE_LIMIT))
                return "platformauthcallback([" + ResultCodeConstants.MOBILE_VERIFY_CODE_OUTLIMIT.getJsonString() + "])";
            else
                return "platformauthcallback([" + ResultCodeConstants.MOBILE_VERIFY_CODE_SENDERROR.getJsonString() + "])";
        }
        catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return "platformauthcallback([" + ResultCodeConstants.SYSTEM_ERROR.getJsonString() + "])";
        }
    }


    @ResponseBody
    @RequestMapping("/register")
    public String register(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "loginkey", required = false) String loginKey,
                           @RequestParam(value = "password", required = false) String pasword,
                           @RequestParam(value = "profilekey", required = false, defaultValue = "www") String profileKey,
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
                return "platformauthcallback([" + ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
            }

            //
            if (StringUtil.isEmpty(nick) || StringUtil.isEmpty(nick.trim())) {
                return "platformauthcallback([" + ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
            }

            if (ContextFilterUtils.checkNickRegexs(nick)) {
                return "platformauthcallback([" + ResultCodeConstants.USERCENTER_NICK_ILLEGAL.getJsonString() + "])";
            }

            //check checkNick is empty
            if (UserCenterServiceSngl.get().getProfileByNick(nick) != null) {
                return "platformauthcallback([" + ResultCodeConstants.USERCENTER_NICK_HAS_EXISTS.getJsonString() + "])";
            }

            //todo 微服务改造
            // String loginId = UserCenterUtil.getUserLoginId(loginKey, loginDomain);

            //todo 微服务改造 check mobilecode
//            String mobileCodeByServ = UserCenterServiceSngl.get().getMobileCode(loginKey);
//            if (StringUtil.isEmpty(mobileCodeByServ) && !mobilecode.equals("36996")) {
//                return "platformauthcallback([" + ResultCodeConstants.MOBILE_VERIFY_CODE_NULL.getJsonString() + "])";
//            }
//            if (!mobilecode.equalsIgnoreCase(mobileCodeByServ) && !mobilecode.equals("36996")) {
//                return "platformauthcallback([" + ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString() + "])";
//            }
            //todo 微服务改造新增
            if(!UserCenterServiceSngl.get().checkMobileVerifyCode(loginKey,mobilecode)){
                return "platformauthcallback([" + ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString() + "])";
            }

            AuthProfile profile = UserCenterServiceSngl.get().register(loginKey, pasword, loginDomain, profileKey, nick, ip, createDate, HTTPUtil.getRequestToMap(request));
            if (profile == null) {
                return "platformauthcallback([" + ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString() + "])";
            }

            if (loginDomain != null && loginDomain.equals(LoginDomain.MOBILE)) {
                //判断手机号是否绑定别的账号
                //todo 微服务改造
                // mobileVerifyWebLogic.modifyMobileByReg(profile.getProfile().getProfileId(), loginKey, profile.getProfile().getProfileKey(), ip);

                pointWebLogic.modifyUserPoint(PointActionType.PHONE_LOGIN_AND_BIND, profile.getProfile().getProfileId(), DEFAULT_APPKEY, WanbaPointType.PHONE, null);
            }

            //UserCenterServiceSngl.get().removeMobileCode(loginKey);

            writeAuthCookie(request, response, profile, StringUtil.isEmpty(appkey) ? "default" : appkey, loginDomain);

            return "platformauthcallback([" + getResultByAuthProfile(profile, profile.getUserLogin().getLoginId(), loginDomain).toString() + "])";
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);

            if (e.equals(UserCenterServiceException.USERLOGIN_HAS_EXISTS)) {
                return "platformauthcallback([" + ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString() + "])";
            } else if (e.equals(UserCenterServiceException.PHONE_VERIFY_CODE_ERROR)) {
                return "platformauthcallback([" + ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString() + "])";
            } else if (e.equals(UserCenterServiceException.USERLOGIN_WRONG_LOGINDOMAIN)) {
                return "platformauthcallback([" + ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getJsonString() + "])";
            } else if (e.equals(UserCenterServiceException.BIND_PHONE_ERROR_PHONE_HAS_BINDED)) {
                return "platformauthcallback([" + ResultCodeConstants.MOBILE_HAS_BINDED.getJsonString() + "])";
            } else if (e.equals(UserCenterServiceException.NICK_HAS_EXIST))
                return "platformauthcallback([" + ResultCodeConstants.USERCENTER_NICK_HAS_EXISTS.getJsonString() + "])";
            else {
                return "platformauthcallback([" + ResultCodeConstants.SYSTEM_ERROR.getJsonString() + "])";
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return "platformauthcallback([" + ResultCodeConstants.SYSTEM_ERROR.getJsonString() + "])";
        }
    }

    private final String NOT_REMEMBER = "false";

    @ResponseBody
    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "loginkey", required = false) String loginKey,
                        @RequestParam(value = "password", required = false) String password,
                        @RequestParam(value = "profilekey", required = false, defaultValue = "www") String profileKey,
//                        @RequestParam(value = "logindomain", required = false) String logindomain,
                        @RequestParam(value = "remember", required = false) String remember,
                        @RequestParam(value = "appkey", required = false) String appkey) {

        try {
            if (StringUtil.isEmpty(loginKey)) {
                return "platformauthcallback([" + ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
            }
            //mobile
            String ip = getIp(request);
            Date createDate = new Date();
            LoginDomain loginDomain = LoginDomain.getLoginDomainByLoginKey(loginKey);
            if (loginDomain == null) {
                return "platformauthcallback([" + ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getJsonString() + "])";
            }

            String loginId = UserCenterUtil.getUserLoginId(loginKey, loginDomain);

            AuthProfile profile = UserCenterServiceSngl.get().
                    login(loginKey, password, loginDomain, profileKey, ip, createDate, HTTPUtil.getRequestToMap(request));
            if (profile == null) {
                return "platformauthcallback([" + ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString() + "])";
            }

            if (NOT_REMEMBER.equals(remember)) {//没有选中记住我
                writeAuthCookieByMaxAge(request, response, profile, StringUtil.isEmpty(appkey) ? "default" : appkey, loginDomain, -1);
            } else {
                writeAuthCookie(request, response, profile, StringUtil.isEmpty(appkey) ? "default" : appkey, loginDomain);
            }

            if (ContextFilterUtils.checkNickRegexs(profile.getProfile().getNick())) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.USERCENTER_NICK_ILLEGAL.getCode());
                jsonObject.put("msg", ResultCodeConstants.USERCENTER_NICK_ILLEGAL.getMsg());
                jsonObject.put("logindomain", loginDomain.getCode());

                return "platformauthcallback([" + jsonObject.toString() + "])";
            }


            pointWebLogic.modifyUserPoint(PointActionType.PHONE_LOGIN_AND_BIND, profile.getProfile().getProfileId(), DEFAULT_APPKEY, WanbaPointType.PHONE, null);

            return "platformauthcallback([" + getResultByAuthProfile(profile, loginId, loginDomain).toString() + "])";
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);

            if (e.equals(UserCenterServiceException.USERLOGIN_NOT_EXISTS)) {
                return "platformauthcallback([" + ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString() + "])";
            } else if (e.equals(UserCenterServiceException.PROFILE_NOT_EXISTS)) {
                return "platformauthcallback([" + ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString() + "])";
            } else if (e.equals(UserCenterServiceException.USERLOGIN_WRONG_LOGINDOMAIN)) {
                return "platformauthcallback([" + ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getJsonString() + "])";
            } else {
                return "platformauthcallback([" + ResultCodeConstants.SYSTEM_ERROR.getJsonString() + "])";
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return "platformauthcallback([" + ResultCodeConstants.SYSTEM_ERROR.getJsonString() + "])";
        }
    }

    @ResponseBody
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = HTTPUtil.getParam(request, "token");
            if (StringUtil.isEmpty(token)) {
                return ResultCodeConstants.SUCCESS.getJsonString();
            }
            UserCenterServiceSngl.get().deleteToken(token);
            CookieUtil.deleteALLCookies(request, response);
            if (request.getSession() != null) {
                request.getSession().invalidate();
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    @ResponseBody
    @RequestMapping("/getprofileid/token")
    public String getByToken(HttpServletRequest request) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            String tokenStr = HTTPUtil.getParam(request, "token");
            if (StringUtil.isEmpty(tokenStr)) {
                return "platformauthcallback([" + ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
            }

            Token token = UserCenterServiceSngl.get().getToken(tokenStr);


            if (token != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("profileid", token.getProfileId());
                jsonObject.put("result", map);
            }
            return "platformauthcallback([" + jsonObject.toString() + "])";

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return "platformauthcallback([" + ResultCodeConstants.SYSTEM_ERROR.getJsonString() + "])";
        }
    }


    @ResponseBody
    @RequestMapping("/bind/mobile")
    public String bindMobile(HttpServletRequest request, @RequestParam(value = "profilekey", required = false, defaultValue = "www") String profileKey) {
        String profileId = HTTPUtil.getParam(request, "profileid");
        String mobile = HTTPUtil.getParam(request, "mobile");
        String password = HTTPUtil.getParam(request, "password");
        String mobilecode = HTTPUtil.getParam(request, "mobilecode");

        if (StringUtil.isEmpty(mobile) || StringUtil.isEmpty(password) || StringUtil.isEmpty(mobilecode)
                || StringUtil.isEmpty(profileId)) {
            return "platformauthcallback([" + ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";

        }

        try {
            //check mobilecode
            String mobileCodeByServ = UserCenterServiceSngl.get().getMobileCode(mobile);
            if (StringUtil.isEmpty(mobileCodeByServ)) {
                return "platformauthcallback([" + ResultCodeConstants.MOBILE_VERIFY_CODE_NULL.getJsonString() + "])";

            }
            if (!mobileCodeByServ.equalsIgnoreCase(mobilecode)) {
                return "platformauthcallback([" + ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString() + "])";

            }

            //check new login isexists if exists error
            String newLoginId = UserCenterUtil.getUserLoginId(mobile, LoginDomain.MOBILE);
            UserLogin newUserLogin = UserCenterServiceSngl.get().getUserLoginByLoginKey(mobile,LoginDomain.MOBILE);
            if (newUserLogin != null) {
                return "platformauthcallback([" + ResultCodeConstants.USERCENTER_USERLOGIN_HAS_EXISTS.getJsonString() + "])";
            }

            //check new login isexists.if not exists error
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                return "platformauthcallback([" + ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString() + "])";

            }

            if (profile.getFlag().hasFlag(ProfileFlag.FLAG_MOBILE)) {
                return "platformauthcallback([" + ResultCodeConstants.USERCENTER_PROFILE_HASBIND_MOBILE.getJsonString() + "])";

            }

            AuthProfile authProfile = UserCenterServiceSngl.get().bind(mobile,
                    password, LoginDomain.MOBILE, profileKey, profile.getUno(), getIp(request), new Date(), "", "", new HashMap<String, String>());

            mobileVerifyWebLogic.modifyMobileByReg(profileId, mobile, getIp(request));


            UserCenterServiceSngl.get().removeMobileCode(mobile);

            return "platformauthcallback([" + getResultByAuthProfile(authProfile, newLoginId, LoginDomain.MOBILE).toString() + "])";

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return "platformauthcallback([" + ResultCodeConstants.SYSTEM_ERROR.getJsonString() + "])";
        }
    }


    @RequestMapping(value = "/{apiCode}/unbind")
    @ResponseBody
    public String unbind(@PathVariable(value = "apiCode") String apiCode, @RequestParam(value = "profilekey", required = false, defaultValue = "www") String profileKey, HttpServletRequest request) {

        try {
            String uno = HTTPUtil.getParam(request, "uno");
            LoginDomain loginDomain = LoginDomain.getByCode(apiCode);
            if (StringUtil.isEmpty(uno) || loginDomain == null) {
                return "platformauthcallback([" + ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";

            }

            UserCenterServiceSngl.get().unbind(loginDomain, profileKey, uno, getIp(request), new Date());

            return "platformauthcallback([" + ResultCodeConstants.SUCCESS.getJsonString() + "])";
        } catch (ServiceException e) {
            GAlerter.lab("unbind occured ServiceException.e:", e);
            return "platformauthcallback([" + ResultCodeConstants.SYSTEM_ERROR.getJsonString() + "])";
        }
    }

    @ResponseBody
    @RequestMapping("/existsmobile/sendcode")
    public String sendcode(HttpServletRequest request,
                           @RequestParam(value = "mobile", required = false) String mobile,
                           @RequestParam(value = "luotestresponse", required = false) String luotest_response) {
//        String vTemplate = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class)
//                .getVerifyMobileSmsTemplate();
        try {
            boolean bval = LuosimaoUtil.verify(luotest_response);
            if (!bval || StringUtil.isEmpty(luotest_response)) {
                return "platformauthcallback([" + ResultCodeConstants.LOGIN_LUOSIMAO_IS_ERROR.getJsonString() + "])";
            }

            UserLogin userLogin = UserCenterServiceSngl.get().getUserLoginByLoginKey(mobile, LoginDomain.MOBILE);
            if (userLogin == null) {
                return "platformauthcallback([" + ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString() + "])";

            }

            //MobileCodeDTO dto = mobileVerifyWebLogic.sendCode(mobile, vTemplate, MobileVerifyWebLogic.VERIFY_REG_TIMES, SMSSenderSngl.CODE_DEFAULT);
boolean result = UserCenterServiceSngl.get().sendMobileNo(mobile);
            //if (dto.getRs() == MobileCodeDTO.RS_SUCCESS) {
            if(result){
                //把生成code存入session
//                request.getSession().setAttribute(Constant.SESSION_MOBILE_CODE, dto.getCode());
//                UserCenterServiceSngl.get().saveMobileCode(mobile, dto.getCode());
                return "platformauthcallback([" + ResultCodeConstants.SUCCESS.getJsonString() + "])";

            }
//            else if (dto.getRs() == MobileCodeDTO.RS_ERROR_OUT_LIMIT) {
//                return "platformauthcallback([" + ResultCodeConstants.  MOBILE_VERIFY_CODE_OUTLIMIT.getJsonString() + "])";
//
//            }
            else {
                return "platformauthcallback([" + ResultCodeConstants.MOBILE_VERIFY_CODE_SENDERROR.getJsonString() + "])";

            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return "platformauthcallback([" + ResultCodeConstants.SYSTEM_ERROR.getJsonString() + "])";

        }
    }

    @ResponseBody
    @RequestMapping("/recover/password")
    public String forgetPassword(HttpServletRequest request,
                                 @RequestParam(value = "profilekey", required = false, defaultValue = "www") String profileKey) {
        String password = HTTPUtil.getParam(request, "pwd");
        // String repetPassowrd = HTTPUtil.getParam(request, "repeatpwd");
        String loginKey = HTTPUtil.getParam(request, "mobile");
        String mobileCode = HTTPUtil.getParam(request, "mobilecode");
        LoginDomain loginDomain = LoginDomain.MOBILE;

        if (StringUtil.isEmpty(password)
                || StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(password)
                ) {
            return "platformauthcallback([" + ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";

        }

//        if (!password.equals(repetPassowrd)) {
//            return "platformauthcallback([" + ResultCodeConstants.USERCENTER_PASSWORD_AGAIN_ERROR.getJsonString() + "])";
//
//        }

        try {
            //check mobilecode
//            String mobileCodeByServ = UserCenterServiceSngl.get().getMobileCode(loginKey);
//            if (StringUtil.isEmpty(mobileCodeByServ) && !mobileCode.equals("36996")) {
//                return "platformauthcallback([" + ResultCodeConstants.MOBILE_VERIFY_CODE_NULL.getJsonString() + "])";
//
//            }
//            if (!mobileCodeByServ.equalsIgnoreCase(mobileCode) && !mobileCode.equals("36996")) {
//                return "platformauthcallback([" + ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString() + "])";
//
//            }

            // todo 微服务改造 String loginId = UserCenterUtil.getUserLoginId(loginKey, loginDomain);

            String passwordTime = String.valueOf(System.currentTimeMillis());
            String passwordSaveDb = UserCenterUtil.getPassowrd(password, passwordTime);


            UserLogin userLogin = UserCenterServiceSngl.get().getUserLoginByLoginKey(loginKey,loginDomain);
            if (userLogin == null) {
                return "platformauthcallback([" + ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString() + "])";

            }

            //修改的密码和上一次密码一样
            String checkOldPwd = UserCenterUtil.getPassowrd(password, userLogin.getPasswdTime());
            if (checkOldPwd.equals(userLogin.getLoginPassword())) {
                return "platformauthcallback([" + ResultCodeConstants.USERCENTER_PASSWORD_SAME_OLD.getJsonString() + "])";

            }


//            boolean bVal = UserCenterServiceSngl.get().modifyUserLogin(new UpdateExpress()
//                    .set(UserLoginField.LOGIN_PASSWORD, passwordSaveDb).set(UserLoginField.PASSWDTIME, passwordTime), userLogin.getLoginId());
//
//            if (bVal) {
//                UserCenterServiceSngl.get().removeMobileCode(loginKey);
//            }
            UserCenterServiceSngl.get().forgetPassword(loginKey,mobileCode,password);

            Profile profile = UserCenterServiceSngl.get().getProfileByUno(userLogin.getUno(), profileKey);
            if (profile != null) {
                DiscuzUtil.modifyProfile(DiscuzModifyField.FIELD_PASSWORD, passwordSaveDb, profile.getUid());
            }
        } catch (UserCenterServiceException ue) {
            if (ue.equals(UserCenterServiceException.PHONE_VERIFY_CODE_ERROR))
                return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();
            else
                return "platformauthcallback([" + ResultCodeConstants.SYSTEM_ERROR.getJsonString() + "])";
        }
        catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return "platformauthcallback([" + ResultCodeConstants.SYSTEM_ERROR.getJsonString() + "])";

        }
        return "platformauthcallback([" + ResultCodeConstants.SUCCESS.getJsonString() + "])";

    }

}
