package com.enjoyf.webapps.joyme.webpage.controller.usercenter;

import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.WanbaResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.usercenter.UserinfoDTO;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserCenterWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterCookieUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-25
 * Time: 上午9:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/usercenter/account")
public class AccountSafeController extends BaseRestSpringController {
    private static final Logger logger = LoggerFactory.getLogger(AccountSafeController.class);


    @Resource(name = "userCenterWebLogic")
    private UserCenterWebLogic userCenterWebLogic;


    @RequestMapping(value = "/safe")
    public ModelAndView index(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null || StringUtil.isEmpty(userSession.getProfileId())) {
                mapMessage.put("isnotlogin", "true");
                return new ModelAndView("/views/jsp/usercenter/person-center", mapMessage);
            }
            String profileid = userSession.getProfileId();
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileid);
            int isunbind = 0;
            if (profile != null && profile.getFlag().hasFlag(ProfileFlag.FLAG_MOBILE)) {
                mapMessage.put("phoneBind", true);
                isunbind++;
                mapMessage.put("phone", profile.getMobile().replaceAll("(\\d{3})\\d{5}(\\d{3})", "$1****$2"));
            } else {
                mapMessage.put("phoneBind", false);
            }
            String errorcode = request.getParameter("errorcode");


            if (!StringUtil.isEmpty(errorcode)) {
                if (ResultCodeConstants.USERCENTER_PROFILE_HAS_BIND.getMsg().equals(errorcode)) {
                    errorcode = "该账号已与其他账号绑定";
                }
            }

            if (profile.getFlag().hasFlag(ProfileFlag.FLAG_QQ)) {
                isunbind++;
            }
            if (profile.getFlag().hasFlag(ProfileFlag.FLAG_SINA)) {
                isunbind++;
            }
            mapMessage.put("errorcode", errorcode);
            mapMessage.put("type", "account");
            mapMessage.put("qqBind", profile.getFlag().hasFlag(ProfileFlag.FLAG_QQ));
            mapMessage.put("weiboBind", profile.getFlag().hasFlag(ProfileFlag.FLAG_SINA));
            mapMessage.put("profile", profile);
            VerifyProfile verifyProfile = UserCenterServiceSngl.get().getVerifyProfileById(profile.getProfileId());
            //该用户获得的道具 todo 用户激励体系删除
//            Map<String, String> chooseMap = PointServiceSngl.get().getChooseLottery(profile.getProfileId());
            ProfileSum profileSum = UserCenterServiceSngl.get().getProfileSum(profile.getProfileId());
            UserinfoDTO userinfoDTO = userCenterWebLogic.buildUserInfoDTO(profile, verifyProfile, profileSum,true);
            mapMessage.put("userinfoDTO", userinfoDTO);

            mapMessage.put("isunbind", isunbind);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("/views/jsp/usercenter/safe", mapMessage);
    }

    //修改绑定的手机号页面
    @RequestMapping(value = "/modifyphonepage")
    public ModelAndView modifyphonepage(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null || StringUtil.isEmpty(userSession.getProfileId())) {
                mapMessage.put("isnotlogin", "true");
                return new ModelAndView("/views/jsp/usercenter/person-center", mapMessage);
            }
            String profileid = userSession.getProfileId();
            String step = request.getParameter("step");
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileid);

            mapMessage.put("type", "account");
            mapMessage.put("profile", profile);
            mapMessage.put("step", step);

            mapMessage.put("hidephone", profile.getMobile().replaceAll("(\\d{3})\\d{5}(\\d{3})", "$1****$2"));

            VerifyProfile verifyProfile = UserCenterServiceSngl.get().getVerifyProfileById(profile.getProfileId());

            //该用户获得的道具
//            Map<String, String> chooseMap = PointServiceSngl.get().getChooseLottery(profile.getProfileId());
            ProfileSum profileSum = UserCenterServiceSngl.get().getProfileSum(profile.getProfileId());
            UserinfoDTO userinfoDTO = userCenterWebLogic.buildUserInfoDTO(profile, verifyProfile,profileSum,true);
            mapMessage.put("userinfoDTO", userinfoDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("/views/jsp/usercenter/modifyphonepage", mapMessage);
    }


    @ResponseBody
    @RequestMapping(value = "/modifyphonestep1")
    public String modifyphonestep1(HttpServletRequest request) {
        String mobile = HTTPUtil.getParam(request, "mobile");
        String mcode = HTTPUtil.getParam(request, "mcode");
        if (StringUtil.isEmpty(mobile) || StringUtil.isEmpty(mcode)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        try {
            //测试验证码
            if ("36996".equals(mcode)) {
                return WanbaResultCodeConstants.SUCCESS.getJsonString();
            }
            //todo 微服务改造
            UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
            if(!UserCenterServiceSngl.get().verifyCodeLogin(mobile,mcode))
                return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();

//            String mobileCodeByServ = UserCenterServiceSngl.get().getMobileCode(mobile);
//            if (StringUtil.isEmpty(mobileCodeByServ) || !mcode.equals(mobileCodeByServ)) {
//                return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return WanbaResultCodeConstants.SUCCESS.getJsonString();
    }

    //绑定手机页面
    @RequestMapping(value = "/bindphone")
    public ModelAndView bindphone(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null || StringUtil.isEmpty(userSession.getProfileId())) {
                mapMessage.put("isnotlogin", "true");
                return new ModelAndView("/views/jsp/usercenter/person-center", mapMessage);
            }

            String profileid = userSession.getProfileId();
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileid);
            mapMessage.put("type", "account");
            mapMessage.put("profile", profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("/views/jsp/usercenter/bindphone", mapMessage);
    }


    //验证手机
    @RequestMapping(value = "/verifytel")
    public ModelAndView verifytel(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String profileId = getProfileIdBySession(request);
            String tel = request.getParameter("tel");
            String sendCode = request.getParameter("sendCode");
            if (true) {
                UpdateExpress updateExpress = new UpdateExpress()
                        .set(ProfileField.MOBILE, tel);
                UserCenterServiceSngl.get().modifyProfile(updateExpress, profileId);
                mapMessage.put("verifyFlag", true);
            } else {
                mapMessage.put("verifyFlag", false);
            }
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            mapMessage.put("type", "account");
            mapMessage.put("profile", profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("/views/jsp/usercenter/account-safe-tel", mapMessage);
    }

    //显示修改密码页面
    @RequestMapping(value = "/modifypwd")
    public ModelAndView modifypwd(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String profileId = request.getParameter("profileid");
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);


            if (profile != null && profile.getFlag().hasFlag(ProfileFlag.FLAG_MOBILE)) {
                mapMessage.put("logindomain", LoginDomain.MOBILE);
            }
            profile.getFlag().getLoginDomain();
            mapMessage.put("type", "account");
            mapMessage.put("profile", profile);

            VerifyProfile verifyProfile = UserCenterServiceSngl.get().getVerifyProfileById(profile.getProfileId());
            //该用户获得的道具
//            Map<String, String> chooseMap = PointServiceSngl.get().getChooseLottery(profile.getProfileId());
            ProfileSum profileSum = UserCenterServiceSngl.get().getProfileSum(profile.getProfileId());

            UserinfoDTO userinfoDTO = userCenterWebLogic.buildUserInfoDTO(profile, verifyProfile,profileSum,true);
            mapMessage.put("userinfoDTO", userinfoDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("/views/jsp/usercenter/modifypwd", mapMessage);
    }

    //保存密码
    @RequestMapping(value = "/modifyPwd")
    public ModelAndView modifyPwd(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            //String profileId = getProfileIdBySession(request);
            String profileId = request.getParameter("profileId");
            //String profileId = "91da4e84629fba26dd4b9bc66d25ba50";
            String oldPassword = request.getParameter("oldPwd");
            String newPwd = request.getParameter("newPwd");
            String loginDomainParam = request.getParameter("logindomain");

            String passwordTime = String.valueOf(System.currentTimeMillis());
            String passwordSaveDb = UserCenterUtil.getPassowrd(newPwd, passwordTime);

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                mapMessage.put("message", "用户不存在");
            }

            LoginDomain loginDomain = LoginDomain.getByCode(loginDomainParam);
            Set<LoginDomain> loginDomainSet = new HashSet<LoginDomain>();
            loginDomainSet.add(loginDomain);

            List<UserLogin> userLoginList = UserCenterServiceSngl.get().queryUserLoginUno(profile.getUno(), loginDomainSet);
            if (CollectionUtil.isEmpty(userLoginList)) {
                mapMessage.put("message", "用户登录不存在");
            }

            UserLogin userLogin = userLoginList.get(0);
            if (StringUtil.isEmpty(userLogin.getLoginPassword())) {
                mapMessage.put("message", "用户域错误");
            }

            //check oldpassword
            String oldPasswordDb = UserCenterUtil.getPassowrd(oldPassword, userLogin.getPasswdTime());
            if (!oldPasswordDb.equalsIgnoreCase(userLogin.getLoginPassword())) {
                mapMessage.put("message", "用户密码不正确");
            }
            boolean flag = UserCenterServiceSngl.get().modifyUserLogin(new UpdateExpress()
                    .set(UserLoginField.LOGIN_PASSWORD, passwordSaveDb).set(UserLoginField.PASSWDTIME, passwordTime), userLogin.getLoginId());
            if (flag)
                mapMessage.put("message", "修改密码成功");
            mapMessage.put("type", "account");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("/views/jsp/usercenter/account-safe", mapMessage);
    }

    private String getProfileIdBySession(HttpServletRequest request) {
        UserCenterSession userSession = getUserCenterSeesion(request);
        if (userSession == null || StringUtil.isEmpty(userSession.getProfileId())) {
            return null;
        }
        return userSession.getProfileId();
    }
}
