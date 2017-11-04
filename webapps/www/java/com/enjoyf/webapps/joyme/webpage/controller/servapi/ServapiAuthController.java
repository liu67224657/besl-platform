package com.enjoyf.webapps.joyme.webpage.controller.servapi;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.service.usercenter.UserLogin;
import com.enjoyf.platform.userservice.client.model.*;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.luosimao.LuosimaoUtil;
import com.enjoyf.platform.util.sms.SMSSenderSngl;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.html.tag.ImageURLTag;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.webapps.joyme.dto.profile.MobileCodeDTO;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.weblogic.profile.MobileVerifyWebLogic;
import com.enjoyf.webapps.joyme.weblogic.user.DiscuzModifyField;
import com.enjoyf.webapps.joyme.weblogic.user.DiscuzUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterCookieUtil;
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
import java.util.*;


@Controller
@RequestMapping("/servapi/auth")
public class ServapiAuthController extends ServapiAbstractAuthController {

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
//        String vTemplate = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class)
//                .getVerifyMobileSmsTemplate();
        try {
            boolean bval = LuosimaoUtil.verify(luotest_response);
            if (!bval || StringUtil.isEmpty(luotest_response)) {
                return "platformauthcallback([" + ResultCodeConstants.LOGIN_LUOSIMAO_IS_ERROR.getJsonString() + "])";
            }

            //验证手机号是否能用
            String checkmobile = request.getParameter("checkmobile");
            if (!StringUtil.isEmpty(checkmobile) && checkmobile.equals("true")) {
                //String newLoginId = UserCenterUtil.getUserLoginId(mobile, LoginDomain.MOBILE);
                UserLogin newUserLogin = UserCenterServiceSngl.get().getUserLoginByLoginKey(mobile,LoginDomain.MOBILE);
                if (newUserLogin != null) {
                    return ResultCodeConstants.USERCENTER_USERLOGIN_HAS_EXISTS.getJsonString();
                }
            }

//todo 微服务改造
//            MobileCodeDTO dto = mobileVerifyWebLogic.sendCode(mobile, vTemplate, MobileVerifyWebLogic.VERIFY_REG_TIMES, SMSSenderSngl.CODE_DEFAULT);
//
//            if (dto.getRs() == MobileCodeDTO.RS_SUCCESS) {
            UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
            if(UserCenterServiceSngl.get().sendMobileCodeLogin(mobile)){
                //把生成code存入session
//                request.getSession().setAttribute(Constant.SESSION_MOBILE_CODE, dto.getCode());
//                UserCenterServiceSngl.get().saveMobileCode(mobile, dto.getCode());
                return ResultCodeConstants.SUCCESS.getJsonString();
            }
//            else if (dto.getRs() == MobileCodeDTO.RS_ERROR_OUT_LIMIT) {
//                return ResultCodeConstants.MOBILE_VERIFY_CODE_OUTLIMIT.getJsonString();
//            }
            else {
                return ResultCodeConstants.MOBILE_VERIFY_CODE_SENDERROR.getJsonString();
            }
        } catch (UserCenterServiceException uex){
            return ResultCodeConstants.MOBILE_VERIFY_CODE_OUTLIMIT.getJsonString();
        }
        catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
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
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            //
            if (StringUtil.isEmpty(nick) || StringUtil.isEmpty(nick.trim())) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            if (ContextFilterUtils.checkNickRegexs(nick)) {
                return ResultCodeConstants.USERCENTER_NICK_ILLEGAL.getJsonString();
            }

            //check checkNick is empty
//            if (UserCenterServiceSngl.get().getProfileByNick(nick) != null) {
//                return ResultCodeConstants.USERCENTER_NICK_HAS_EXISTS.getJsonString();
//            }

            String loginId = UserCenterUtil.getUserLoginId(loginKey, loginDomain);

            //check mobilecode
//            String mobileCodeByServ = UserCenterServiceSngl.get().getMobileCode(loginKey);
//            if (StringUtil.isEmpty(mobileCodeByServ) && !mobilecode.equals("111111")) {
//                return ResultCodeConstants.MOBILE_VERIFY_CODE_NULL.getJsonString();
//            }
//            if (!mobilecode.equalsIgnoreCase(mobileCodeByServ) && !mobilecode.equals("111111")) {
//                return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();
//            }
            //todo 微服务改造新增
            if(!UserCenterServiceSngl.get().checkMobileVerifyCode(loginKey,mobilecode))
                return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();

            AuthProfile profile = UserCenterServiceSngl.get().register(loginKey, pasword, loginDomain, profileKey, nick, ip, createDate, HTTPUtil.getRequestToMap(request));
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

//            if (loginDomain != null && loginDomain.equals(LoginDomain.MOBILE)) {
//                mobileVerifyWebLogic.modifyMobileByReg(profile.getProfile().getProfileId(), loginKey, profile.getProfile().getProfileKey(), ip);
//            }

            //UserCenterServiceSngl.get().removeMobileCode(loginKey);

            writeAuthCookie(request, response, profile, StringUtil.isEmpty(appkey) ? "default" : appkey, loginDomain);

            return getResultByAuthProfile(profile, loginId, loginDomain).toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);

            if (e.equals(UserCenterServiceException.NICK_HAS_EXIST)) {
                return ResultCodeConstants.USERCENTER_NICK_HAS_EXISTS.getJsonString();
            } else if (e.equals(UserCenterServiceException.PROFILE_NOT_EXISTS)) {
                return ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString();
            } else if (e.equals(UserCenterServiceException.USERLOGIN_WRONG_LOGINDOMAIN)) {
                return ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getJsonString();
            } else if (e.equals(UserCenterServiceException.BIND_PHONE_ERROR_PHONE_HAS_BINDED)) {
                return ResultCodeConstants.MOBILE_HAS_BINDED.getJsonString();
            } else if (e.equals(UserCenterServiceException.USERLOGIN_HAS_EXISTS)){
                return ResultCodeConstants.MOBILE_HAS_BINDED.getJsonString();
            }
            else {
                return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
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
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            //mobile
            String ip = getIp(request);
            Date createDate = new Date();
            LoginDomain loginDomain = LoginDomain.getLoginDomainByLoginKey(loginKey);
            if (loginDomain == null) {
                return ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getJsonString();
            }

            String loginId = UserCenterUtil.getUserLoginId(loginKey, loginDomain);

            AuthProfile profile = UserCenterServiceSngl.get().
                    login(loginKey, password, loginDomain, profileKey, ip, createDate, HTTPUtil.getRequestToMap(request));
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
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

                return jsonObject.toString();
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
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            Token token = UserCenterServiceSngl.get().getToken(tokenStr);


            if (token != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("profileid", token.getProfileId());
                jsonObject.put("result", map);
            }
            return jsonObject.toString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
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
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            //todo 微服务改造 check mobilecode
//            String mobileCodeByServ = UserCenterServiceSngl.get().getMobileCode(mobile);
//            if (StringUtil.isEmpty(mobileCodeByServ) && !mobilecode.equals("36996")) {
//                return ResultCodeConstants.MOBILE_VERIFY_CODE_NULL.getJsonString();
//            }
//            if (!mobileCodeByServ.equalsIgnoreCase(mobilecode) && !mobilecode.equals("36996")) {
//                return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();
//            }
            UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
            if(!UserCenterServiceSngl.get().verifyCodeLogin(mobile,mobilecode))
                return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();

            //check new login isexists if exists error
            //String newLoginId = UserCenterUtil.getUserLoginId(mobile, LoginDomain.MOBILE);
//            UserLogin newUserLogin = UserCenterServiceSngl.get().getUserLoginByLoginId(newLoginId);
//            if (newUserLogin != null) {
//                return ResultCodeConstants.USERCENTER_USERLOGIN_HAS_EXISTS.getJsonString();
//            }

            //check new login isexists.if not exists error
//            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
//            if (profile == null) {
//                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
//            }
//
//            if (profile.getFlag().hasFlag(ProfileFlag.FLAG_MOBILE)) {
//                return ResultCodeConstants.USERCENTER_PROFILE_HASBIND_MOBILE.getJsonString();
//            }

            AuthProfile authProfile = UserCenterServiceSngl.get().bind(mobile,
                    password, LoginDomain.MOBILE, profileKey, "", getIp(request), new Date(), "", "", new HashMap<String, String>());

            mobileVerifyWebLogic.modifyMobileByReg(profileId, mobile, getIp(request));


            pointWebLogic.modifyUserPoint(PointActionType.PHONE_LOGIN_AND_BIND, authProfile.getProfile().getProfileId(), DEFAULT_APPKEY, WanbaPointType.PHONE, null);

            UserCenterServiceSngl.get().removeMobileCode(mobile);

            return getResultByAuthProfile(authProfile, authProfile.getUserLogin().getLoginId(), LoginDomain.MOBILE).toString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }


    @RequestMapping(value = "/{apiCode}/unbind")
    @ResponseBody
    public String unbind(@PathVariable(value = "apiCode") String apiCode, @RequestParam(value = "profilekey", required = false, defaultValue = "www") String profileKey, HttpServletRequest request) {

        try {
            String uno = HTTPUtil.getParam(request, "uno");
            LoginDomain loginDomain = LoginDomain.getByCode(apiCode);
            if (StringUtil.isEmpty(uno) || loginDomain == null) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            //todo 微服务改造
            UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
            UserCenterServiceSngl.get().unbind(loginDomain, profileKey, uno, getIp(request), new Date());

            return ResultCodeConstants.SUCCESS.getJsonString();
        } catch (ServiceException e) {
            GAlerter.lab("unbind occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    @ResponseBody
    @RequestMapping("/existsmobile/sendcode")
    public String sendcode(HttpServletRequest request,
                           @RequestParam(value = "mobile", required = false) String mobile,
                           @RequestParam(value = "luotestresponse", required = false) String luotest_response) {
        String vTemplate = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class)
                .getVerifyMobileSmsTemplate();
        try {
            boolean bval = LuosimaoUtil.verify(luotest_response);
            if (!bval || StringUtil.isEmpty(luotest_response)) {
                return "platformauthcallback([" + ResultCodeConstants.LOGIN_LUOSIMAO_IS_ERROR.getJsonString() + "])";
            }


            UserLogin userLogin = UserCenterServiceSngl.get().getUserLoginByLoginKey(mobile, LoginDomain.MOBILE);
            if (userLogin == null) {
                return ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString();
            }

//            MobileCodeDTO dto = mobileVerifyWebLogic.sendCode(mobile, vTemplate, MobileVerifyWebLogic.VERIFY_REG_TIMES, SMSSenderSngl.CODE_DEFAULT);
//
//            if (dto.getRs() == MobileCodeDTO.RS_SUCCESS) {
//                //把生成code存入session
//                request.getSession().setAttribute(Constant.SESSION_MOBILE_CODE, dto.getCode());
//                UserCenterServiceSngl.get().saveMobileCode(mobile, dto.getCode());
            if(UserCenterServiceSngl.get().sendMobileNo(mobile)){
                return ResultCodeConstants.SUCCESS.getJsonString();
            }
//            else if (dto.getRs() == MobileCodeDTO.RS_ERROR_OUT_LIMIT) {
//                return ResultCodeConstants.MOBILE_VERIFY_CODE_OUTLIMIT.getJsonString();
//            }
            else {
                return ResultCodeConstants.MOBILE_VERIFY_CODE_SENDERROR.getJsonString();
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    @ResponseBody
    @RequestMapping("/recover/password")
    public String forgetPassword(HttpServletRequest request) {
        String password = HTTPUtil.getParam(request, "pwd");
        String repetPassowrd = HTTPUtil.getParam(request, "repeatpwd");
        String loginKey = HTTPUtil.getParam(request, "mobile");
        String mobileCode = HTTPUtil.getParam(request, "mobilecode");
        String profileid = HTTPUtil.getParam(request, "profileid");

        LoginDomain loginDomain = LoginDomain.MOBILE;

        if (StringUtil.isEmpty(password)
                || StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(password)
                || StringUtil.isEmpty(repetPassowrd)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        if (!password.equals(repetPassowrd)) {
            return ResultCodeConstants.USERCENTER_PASSWORD_AGAIN_ERROR.getJsonString();
        }

        try {
            //check mobilecode
            String mobileCodeByServ = UserCenterServiceSngl.get().getMobileCode(loginKey);
            if (StringUtil.isEmpty(mobileCodeByServ)) {
                return ResultCodeConstants.MOBILE_VERIFY_CODE_NULL.getJsonString();
            }
            if (!mobileCodeByServ.equalsIgnoreCase(mobileCode)) {
                return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();
            }

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

//            boolean bVal = UserCenterServiceSngl.get().modifyUserLogin(new UpdateExpress()
//                    .set(UserLoginField.LOGIN_PASSWORD, passwordSaveDb).set(UserLoginField.PASSWDTIME, passwordTime), userLogin.getLoginId());
             //todo 微服务改造
            UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
            UserCenterServiceSngl.get().changePassword(password);

            try {
                Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileid);
                if (profile != null) {
                    DiscuzUtil.modifyProfile(DiscuzModifyField.FIELD_PASSWORD, passwordSaveDb, profile.getUid());
                }
            } catch (Exception e) {
            }

           // if (bVal) {
                UserCenterServiceSngl.get().removeMobileCode(loginKey);
           // }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    @ResponseBody
    @RequestMapping("/modify/password")
    public String modifyPassword(HttpServletRequest request) {
        String password = HTTPUtil.getParam(request, "pwd");
        String oldPassword = HTTPUtil.getParam(request, "oldpwd");
        String profileId = HTTPUtil.getParam(request, "profileid");
        String loginDomainParam = HTTPUtil.getParam(request, "logindomain");
        if (StringUtil.isEmpty(password) || StringUtil.isEmpty(oldPassword)
                || StringUtil.isEmpty(profileId) || StringUtil.isEmpty(loginDomainParam)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            String passwordTime = String.valueOf(System.currentTimeMillis());
            String passwordSaveDb = UserCenterUtil.getPassowrd(password, passwordTime);

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            LoginDomain loginDomain = LoginDomain.getByCode(loginDomainParam);
            Set<LoginDomain> loginDomainSet = new HashSet<LoginDomain>();
            loginDomainSet.add(loginDomain);

            List<UserLogin> userLoginList = UserCenterServiceSngl.get().queryUserLoginUno(profile.getUno(), loginDomainSet);
            if (CollectionUtil.isEmpty(userLoginList)) {
                return ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString();
            }

            UserLogin userLogin = userLoginList.get(0);
            if (StringUtil.isEmpty(userLogin.getLoginPassword())) {
                return ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getJsonString();
            }

            //check oldpassword
            String oldPasswordDb = UserCenterUtil.getPassowrd(oldPassword, userLogin.getPasswdTime());
            if (!oldPasswordDb.equalsIgnoreCase(userLogin.getLoginPassword())) {
                return ResultCodeConstants.USERCENTER_PASSWORD_INCORRECT.getJsonString();
            }
            //todo 微服务改造
//            UserCenterServiceSngl.get().modifyUserLogin(new UpdateExpress()
//                    .set(UserLoginField.LOGIN_PASSWORD, passwordSaveDb).set(UserLoginField.PASSWDTIME, passwordTime), userLogin.getLoginId());
            UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
            UserCenterServiceSngl.get().changePassword(password);

            try {
                DiscuzUtil.modifyProfile(DiscuzModifyField.FIELD_PASSWORD, passwordSaveDb, profile.getUid());
            } catch (Exception e) {
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    @ResponseBody
    @RequestMapping("/verify/mobile")
    public String verifyMobile(HttpServletRequest request) {
        String mobile = HTTPUtil.getParam(request, "mobile");
        String mobilecode = HTTPUtil.getParam(request, "mobilecode");

        if (StringUtil.isEmpty(mobile) || StringUtil.isEmpty(mobilecode)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {

            //测试验证码
            if ("36996".equals(mobilecode)) {
                return ResultCodeConstants.SUCCESS.getJsonString();
            }

            //todo 微服务改造 check mobilecode
//            String mobileCodeByServ = UserCenterServiceSngl.get().getMobileCode(mobile);
//            if (StringUtil.isEmpty(mobileCodeByServ)) {
//                return ResultCodeConstants.MOBILE_VERIFY_CODE_NULL.getJsonString();
//            }
//            if (!mobileCodeByServ.equalsIgnoreCase(mobilecode)) {
//                return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();
//            }
            UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
            if( UserCenterServiceSngl.get().verifyCodeLogin(mobile,mobilecode))
              return ResultCodeConstants.SUCCESS.getJsonString();
            else
                return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }


    @ResponseBody
    @RequestMapping("/modify/mobile")
    public String modifyMobile(HttpServletRequest request) {
        String profileId = HTTPUtil.getParam(request, "profileid");
        String newMobile = HTTPUtil.getParam(request, "mobile");
        String oldMobile = HTTPUtil.getParam(request, "oldmobile");
        String loginDomainParam = HTTPUtil.getParam(request, "logindomain");

        if (StringUtil.isEmpty(newMobile)
                || StringUtil.isEmpty(profileId) || StringUtil.isEmpty(loginDomainParam)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            LoginDomain loginDomain = LoginDomain.getByCode(loginDomainParam);
            if (loginDomain == null) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
//todo 微服务改造
//            //check new login isexists if exists error
//            String newLoginId = UserCenterUtil.getUserLoginId(newMobile, loginDomain);
//            UserLogin newUserLogin = UserCenterServiceSngl.get().getUserLoginByLoginId(newLoginId);
//            if (newUserLogin != null) {
//                return ResultCodeConstants.USERCENTER_USERLOGIN_HAS_EXISTS.getJsonString();
//            }
//
//            //check new login isexists.if not exists error
//            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
//            if (profile == null) {
//                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
//            }
//
//            String oldLoginId = UserCenterUtil.getUserLoginId(oldMobile, loginDomain);
//            UserLogin oldUserLogin = UserCenterServiceSngl.get().getUserLoginByLoginId(oldLoginId);
//
//            //check userlogin and profile is same people
//            if (!oldUserLogin.getUno().equalsIgnoreCase(profile.getUno())) {
//                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
//            }
//
//            //modify loginid and mobile
//            boolean bval = UserCenterServiceSngl.get().modifyUserLogin(new UpdateExpress()
//                    .set(UserLoginField.LOGIN_KEY, newMobile)
//                    .set(UserLoginField.LOGIN_ID, newLoginId), oldLoginId);

//            if (bval) {
//                mobileVerifyWebLogic.modifyMobileByReg(profileId, newMobile, getIp(request));
//            }

//            if (bval) {
//                UserCenterServiceSngl.get().removeMobileCode(newMobile);
//                UserCenterServiceSngl.get().removeMobileCode(oldMobile);
//            }
            UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
            com.enjoyf.platform.userservice.client.model.UserLogin login= UserCenterServiceSngl.get().changeMobileNo(newMobile);
            JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("loginid", login.getId()+"");
            jsonObject.put("result", resultMap);

            return jsonObject.toString();
        } catch (UserCenterServiceException uex){
              return   ResultCodeConstants.USERCENTER_USERLOGIN_HAS_EXISTS.getJsonString();
        }
        catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    private Map<String, UserPoint> queryUserPoint(String appkey, Set<String> pids) throws ServiceException {
        Map<String, UserPoint> returnMap = new HashMap<String, UserPoint>();
        PageRows<UserPoint> pageRows = PointServiceSngl.get().queryUserPointByPage(new QueryExpress().add(QueryCriterions.eq(UserPointField.POINTKEY, PointKeyType.getByCode(DEFAULT_APPKEY).getValue()))
                .add(QueryCriterions.in(UserPointField.PROFILEID, pids.toArray())), new Pagination(pids.size()));
        if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
            for (String pid : pids) {
                for (UserPoint userPoint : pageRows.getRows()) {
                    if (pid.equals(userPoint.getProfileId())) {
                        if (pid.equals(userPoint.getProfileId())) {
                            if (returnMap.get(userPoint.getProfileId()) != null) {
                                if (userPoint.getUserPoint() > returnMap.get(userPoint.getProfileId()).getUserPoint()) {
                                    returnMap.put(pid, userPoint);
                                }
                            } else {
                                returnMap.put(pid, userPoint);
                            }
                            continue;
                        }
                    }
                }
            }
        }
        return returnMap;
    }

    @ResponseBody
    @RequestMapping("/getprofile")
    public String getPrifile(HttpServletRequest request) {
        String profileIds = HTTPUtil.getParam(request, "profileid");

        try {
            if (StringUtil.isEmpty(profileIds)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            String[] profileIdArray = profileIds.split(",");
            Set<String> profileIdQuerySet = new HashSet<String>();
            Collections.addAll(profileIdQuerySet, profileIdArray);

            Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileIdQuerySet);
            Map<String, Map<String, String>> chooseMap = PointServiceSngl.get().queryChooseLottery(profileIdQuerySet);
            Map<String, VerifyProfile> verifyProfileMap = UserCenterServiceSngl.get().queryProfileByIds(profileIdQuerySet);
            Map<String, UserPoint> userPointMap = queryUserPoint(DEFAULT_APPKEY, profileIdQuerySet);

            List<JSONObject> result = new ArrayList<JSONObject>();
            for (String pid : profileIdArray) {
                Profile profile = profileMap.get(pid);
                if (profile != null) {
                    JSONObject profileJson = new JSONObject();
                    profileJson.put("uno", profile.getUno());
                    profileJson.put("profileid", profile.getProfileId());
                    profileJson.put("icon", ImageURLTag.parseUserCenterHeadIcon(profile.getIcon(), profile.getSex(), "m", true));
                    profileJson.put("nick", profile.getNick() == null ? "" : profile.getNick());
                    profileJson.put("uid", String.valueOf(profile.getUid()));
                    profileJson.put("desc", profile.getDescription() == null ? "" : profile.getDescription());
                    profileJson.put("flag", profile.getFlag().getValue());
                    profileJson.put("mobile", profile.getMobile() == null ? "" : profile.getMobile());
                    profileJson.put("sex", profile.getSex() == null ? "" : profile.getSex());
                    profileJson.put("province", profile.getProvinceId() == null ? "" : profile.getProvinceId());
                    profileJson.put("city", profile.getCityId() == null ? "" : profile.getCityId());
                    profileJson.put("headskin", "");
                    profileJson.put("cardskin", "");
                    profileJson.put("bubbleskin", "");
                    profileJson.put("replyskin", "");

                    profileJson.put("point", userPointMap.get(pid) != null ? userPointMap.get(pid).getUserPoint() : 0);

                    profileJson.put("vdesc", "");//认证原因
                    profileJson.put("vtitle", "");//认证title
                    profileJson.put("vtype", 0);//认证类型
                    VerifyProfile verifyProfile = verifyProfileMap.get(profile.getProfileId());
                    if (verifyProfile != null) {
                        profileJson.put("vdesc", verifyProfile.getDescription());//认证原因
                        profileJson.put("vtitle", StringUtil.isEmpty(verifyProfile.getVerifyTitle()) ? "" : verifyProfile.getVerifyTitle());//认证title
                        profileJson.put("vtype", verifyProfile.getVerifyType());//认证类型
                    }

                    Map<String, String> map = chooseMap.get(pid);
                    if (map != null && !map.isEmpty()) {
                        if (!StringUtil.isEmpty(map.get(LOTTERY_TYPE + LotteryType.HEAD.getCode()))) {
                            profileJson.put("headskin", map.get(LOTTERY_TYPE + LotteryType.HEAD.getCode()));
                        }
                        if (!StringUtil.isEmpty(map.get(LOTTERY_TYPE + LotteryType.BG.getCode()))) {
                            profileJson.put("cardskin", map.get(LOTTERY_TYPE + LotteryType.BG.getCode()));
                        }
                        if (!StringUtil.isEmpty(map.get(LOTTERY_TYPE + LotteryType.CHAT.getCode()))) {
                            profileJson.put("bubbleskin", map.get(LOTTERY_TYPE + LotteryType.CHAT.getCode()));
                        }
                        if (!StringUtil.isEmpty(map.get(LOTTERY_TYPE + LotteryType.COMMENT.getCode()))) {
                            profileJson.put("replyskin", map.get(LOTTERY_TYPE + LotteryType.COMMENT.getCode()));
                        }
                    }

                    result.add(profileJson);
                }
            }
//            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("rs", 1);
            jsonObject.put("result", result);
            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    @ResponseBody
    @RequestMapping("/getprofilebyuid")
    public String getprofilebyuid(HttpServletRequest request) {
        String uid = HTTPUtil.getParam(request, "uid");

        try {
            if (StringUtil.isEmpty(uid)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            String[] uidArray = uid.split(",");
            Set<Long> uids = new HashSet<Long>();

            for (String uidStr : uidArray) {
                uids.add(Long.valueOf(uidStr));
            }


            Map<Long, Profile> profileMap = UserCenterServiceSngl.get().queryProfilesByUids(uids);
            Set<String> profileIdQuerySet = new HashSet<String>();
            for (Long uidLong : profileMap.keySet()) {
                Profile profile = profileMap.get(uidLong);
                if (profile != null) {
                    profileIdQuerySet.add(profile.getProfileId());
                }
            }


            Map<String, Map<String, String>> chooseMap = PointServiceSngl.get().queryChooseLottery(profileIdQuerySet);
            Map<String, VerifyProfile> verifyProfileMap = UserCenterServiceSngl.get().queryProfileByIds(profileIdQuerySet);


            List<JSONObject> result = new ArrayList<JSONObject>();
            for (Long uidLong : profileMap.keySet()) {
                Profile profile = profileMap.get(uidLong);
                if (profile != null) {
                    UserPoint userPoint = pointWebLogic.getUserPoint(DEFAULT_APPKEY, profile.getProfileId());
                    JSONObject profileJson = new JSONObject();
                    profileJson.put("uno", profile.getUno());
                    profileJson.put("profileid", profile.getProfileId());
                    profileJson.put("icon", ImageURLTag.parseUserCenterHeadIcon(profile.getIcon(), profile.getSex(), "m", true));
                    profileJson.put("nick", profile.getNick() == null ? "" : profile.getNick());
                    profileJson.put("uid", String.valueOf(profile.getUid()));
                    profileJson.put("desc", profile.getDescription() == null ? "" : profile.getDescription());
                    profileJson.put("flag", profile.getFlag().getValue());
                    profileJson.put("mobile", profile.getMobile() == null ? "" : profile.getMobile());
                    profileJson.put("sex", profile.getSex() == null ? "" : profile.getSex());
                    profileJson.put("province", profile.getProvinceId() == null ? "" : profile.getProvinceId());
                    profileJson.put("city", profile.getCityId() == null ? "" : profile.getCityId());
                    profileJson.put("headskin", "");
                    profileJson.put("cardskin", "");
                    profileJson.put("bubbleskin", "");
                    profileJson.put("replyskin", "");
                    profileJson.put("point", userPoint.getUserPoint());

                    profileJson.put("vdesc", "");//认证原因
                    profileJson.put("vtitle", "");//认证title
                    profileJson.put("vtype", 0);//认证类型
                    VerifyProfile verifyProfile = verifyProfileMap.get(profile.getProfileId());
                    if (verifyProfile != null) {
                        profileJson.put("vdesc", verifyProfile.getDescription());//认证原因
                        profileJson.put("vtitle", StringUtil.isEmpty(verifyProfile.getVerifyTitle()) ? "" : verifyProfile.getVerifyTitle());//认证title
                        profileJson.put("vtype", verifyProfile.getVerifyType());//认证类型
                    }

                    Map<String, String> map = chooseMap.get(profile.getProfileId());
                    if (map != null && !map.isEmpty()) {
                        if (!StringUtil.isEmpty(map.get(LOTTERY_TYPE + LotteryType.HEAD.getCode()))) {
                            profileJson.put("headskin", map.get(LOTTERY_TYPE + LotteryType.HEAD.getCode()));
                        }
                        if (!StringUtil.isEmpty(map.get(LOTTERY_TYPE + LotteryType.BG.getCode()))) {
                            profileJson.put("cardskin", map.get(LOTTERY_TYPE + LotteryType.BG.getCode()));
                        }
                        if (!StringUtil.isEmpty(map.get(LOTTERY_TYPE + LotteryType.CHAT.getCode()))) {
                            profileJson.put("bubbleskin", map.get(LOTTERY_TYPE + LotteryType.CHAT.getCode()));
                        }
                        if (!StringUtil.isEmpty(map.get(LOTTERY_TYPE + LotteryType.COMMENT.getCode()))) {
                            profileJson.put("replyskin", map.get(LOTTERY_TYPE + LotteryType.COMMENT.getCode()));
                        }
                    }

                    result.add(profileJson);
                }
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", 1);
            jsonObject.put("result", result);
            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }
}
