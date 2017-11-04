package com.enjoyf.webapps.joyme.webpage.controller.profile;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.UserCenterServiceException;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.html.ValidateImageUtil;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.webapps.joyme.weblogic.profile.MobileVerifyWebLogic;
import com.enjoyf.webapps.joyme.weblogic.profile.PhoneVerifyError;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import com.enjoyf.webapps.joyme.webpage.util.VerifyHttpUtil;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-25 下午4:17
 * Description:
 */

@RequestMapping(value = "/profile/mobile")
@Controller
public class MobileVerifyController extends BaseRestSpringController {
    private JsonBinder jsonBinder = JsonBinder.buildNormalBinder();

    TemplateHotdeployConfig templateConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);


    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    private static final String REDIRECT_MODIFY_TYPE = "1";

    @Resource(name = "mobileVerifyWebLogic")
    private MobileVerifyWebLogic mobileVerifyWebLogic;

    @RequestMapping(value = "/gopage")
    public ModelAndView goPage(HttpServletRequest request,
                               @RequestParam(value = "phone", required = false) String mobile,
                               @RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "type", required = false) String type,
                               @RequestParam(value = "reurl", required = false) String reurl) {


        UserCenterSession userSession = getUserCenterSeesion(request);
        if (userSession != null && StringUtil.isEmpty(type)) {
            //判断是否已经绑定过
            if (!StringUtil.isEmpty(userSession.getMobile())) {
                Map map = new HashMap();
                mobile = userSession.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                map.put("nav", "mobile");
                map.put("phone", mobile);
                return new ModelAndView("/views/jsp/customize/mobile-result", map);
            }
        }

        String queryString = "";
        if (!StringUtil.isEmpty(mobile)) {
            if (StringUtil.isEmpty(queryString)) {
                queryString += "?phone=" + mobile;
            } else {
                queryString += "&phone=" + mobile;
            }
        }
        if (!StringUtil.isEmpty(error)) {
            if (StringUtil.isEmpty(queryString)) {
                queryString += "?error=" + error;
            } else {
                queryString += "&error=" + error;
            }
        }
        if (!StringUtil.isEmpty(type)) {
            if (StringUtil.isEmpty(queryString)) {
                queryString += "?type=" + type;
            } else {
                queryString += "&type=" + type;
            }
        }
        if (!StringUtil.isEmpty(reurl)) {
            try {
                reurl = URLEncoder.encode(reurl, "UTF-8");
                if (StringUtil.isEmpty(queryString)) {
                    queryString += "?reurl=" + reurl;
                } else {
                    queryString += "&reurl=" + reurl;
                }
            } catch (UnsupportedEncodingException e) {
            }
        }
        return new ModelAndView("redirect:/profile/mobile/page" + queryString);
    }


    @RequestMapping(value = "/page")
    public ModelAndView page(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "phone", required = false) String phone,
                             @RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "type", required = false) String type,
                             @RequestParam(value = "reurl", required = false) String reurl) {
        Map map = new HashMap();
        map.put("nav", "mobile");
        map.put("phone", phone);

        UserCenterSession userSession = getUserCenterSeesion(request);
        if (!StringUtil.isEmpty(userSession.getMobile()) && StringUtil.isEmpty(type)) {
            phone = userSession.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            map.put("phone", phone);
            return new ModelAndView("/views/jsp/customize/mobile-result", map);
        }

        if (!StringUtil.isEmpty(error) && !StringUtil.isEmpty(PhoneVerifyError.getErrorInfoByCode(error))) {
            map.put("codeError", PhoneVerifyError.getErrorInfoByCode(error));
        }

        String lastObj = getTimeInCookie(request);
        if (!StringUtil.isEmpty(lastObj)) {
            long now = System.currentTimeMillis();
            long last = Long.parseLong(lastObj);

            long intravel = 60 - ((now - last) / 1000);
            if (intravel >= 0) {
                map.put("intravel", intravel);
            } else {
                deleteTimeInCookie(request, response);
            }
        }
        if (!StringUtil.isEmpty(type)) {
            map.put("type", type);
            return new ModelAndView("/views/jsp/customize/modifymobile", map);
        }
        if (!StringUtil.isEmpty(reurl)) {
            map.put("reurl", reurl);
        }

        return new ModelAndView("/views/jsp/customize/mobile", map);
    }


    @RequestMapping(value = "/verify")
    public ModelAndView verifyMobile(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(value = "phone", required = false) String phone,
                                     @RequestParam(value = "verifycode", required = false) String verifyCode,
                                     @RequestParam(value = "type", required = false) String type,
                                     @RequestParam(value = "reurl", required = false) String reurl) {

        Map map = new HashMap();
        //取出存入的code 和 用户输入的code进行对比 一致则存入
        if (StringUtil.isEmpty(phone)) {
            return new ModelAndView("redirect:/profile/mobile/gopage?error=" + PhoneVerifyError.ERROR_PHONE_NULL + "&type=" + type + "&reurl=" + reurl);
        }
        if (StringUtil.isEmpty(verifyCode)) {
            return new ModelAndView("redirect:/profile/mobile/gopage?error=" + PhoneVerifyError.ERROR_VERIFY_NULL + "&type=" + type + "&reurl=" + reurl);
        }
        String sessionCode = null;
        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null) {
                return new ModelAndView("redirect:/profile/mobile/gopage?phone=" + phone + "&type=" + type);
            }
            sessionCode = UserCenterServiceSngl.get().getMobileCode(userSession.getProfileId());
            if (StringUtil.isEmpty(sessionCode)) {
                return new ModelAndView("redirect:/profile/mobile/gopage?phone=" + phone + "&error=" + PhoneVerifyError.ERROR_VERIFY_NOT_SEND + "&type=" + type + "&reurl=" + reurl);
            }

            String result = mobileVerifyWebLogic.verifyMobile(userSession.getProfileId(), phone, verifyCode, sessionCode, getIp(request));

            if (!result.equals(PhoneVerifyError.SUCCESS)) {
                return new ModelAndView("redirect:/profile/mobile/gopage?phone=" + phone + "&error=" + result + "&type=" + type + "&reurl=" + reurl);
            } else {
                UserCenterServiceSngl.get().removeMobileCode(userSession.getProfileId());
                deleteTimeInCookie(request, response);
                userSession.setMobile(phone);

                phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                map.put("phone", phone);
                map.put("nav", "mobile");
                if (!StringUtil.isEmpty(reurl)) {
                    try {
                        reurl = URLDecoder.decode(reurl, "UTF-8");
                        return new ModelAndView("redirect:" + reurl);
                    } catch (UnsupportedEncodingException e) {
                        return new ModelAndView("/views/jsp/customize/mobile-result", map);
                    }
                }
            }
        } catch (ServiceException e) {
            if (e.equals(UserCenterServiceException.BIND_PHONE_ERROR_PHONE_HAS_BINDED)
                    || e.equals(UserCenterServiceException.BIND_PHONE_ERROR_PRFOILE_HAS_BINDED)) {
                GAlerter.lan(this.getClass().getName() + " occured ServiceException.e", e);
                return new ModelAndView("redirect:/profile/mobile/gopage?phone=" + phone + "&error=" + PhoneVerifyError.ERROR_PHONE_HAS_BIND + "&type=" + type + "&reurl=" + reurl);
            } else {
                GAlerter.lab(this.getClass().getName() + " occured ServiceException.e", e);
                return new ModelAndView("redirect:/profile/mobile/gopage?phone=" + phone + "&type=" + type + "&reurl=" + reurl);
            }
        }

        return new ModelAndView("/views/jsp/customize/mobile-result", map);
    }


    @RequestMapping(value = "/modify")
    public ModelAndView modifyMobile(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(value = "phone", required = false) String phone,
                                     @RequestParam(value = "verifycode", required = false) String verifyCode,
                                     @RequestParam(value = "type", required = false) String type,
                                     @RequestParam(value = "reurl", required = false) String reurl) {

        Map map = new HashMap();
        //取出存入的code 和 用户输入的code进行对比 一致则存入
        if (StringUtil.isEmpty(phone)) {
            return new ModelAndView("redirect:/profile/mobile/gopage?error=" + PhoneVerifyError.ERROR_PHONE_NULL + "&type=" + type + "&reurl=" + reurl);
        }
        if (StringUtil.isEmpty(verifyCode)) {
            return new ModelAndView("redirect:/profile/mobile/gopage?error=" + PhoneVerifyError.ERROR_VERIFY_NULL + "&type=" + type + "&reurl=" + reurl);
        }
        String sessionCode = null;
        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null) {
                return new ModelAndView("redirect:/profile/mobile/gopage?phone=" + phone + "&type=" + type);
            }
            sessionCode = UserCenterServiceSngl.get().getMobileCode(userSession.getProfileId());
            if (StringUtil.isEmpty(sessionCode)) {
                return new ModelAndView("redirect:/profile/mobile/gopage?phone=" + phone + "&error=" + PhoneVerifyError.ERROR_VERIFY_NOT_SEND + "&type=" + type + "&reurl=" + reurl);
            }

            String result = mobileVerifyWebLogic.modifyMobile(userSession.getProfileId(), phone, verifyCode, sessionCode, getIp(request));

            if (!result.equals(PhoneVerifyError.SUCCESS)) {
                return new ModelAndView("redirect:/profile/mobile/gopage?phone=" + phone + "&error=" + result + "&type=" + type + "&reurl=" + reurl);
            } else {
                UserCenterServiceSngl.get().removeMobileCode(userSession.getProfileId());
                deleteTimeInCookie(request, response);
                userSession.setMobile(phone);

                phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                map.put("phone", phone);
                map.put("nav", "mobile");
                if (!StringUtil.isEmpty(reurl)) {
                    try {
                        reurl = URLDecoder.decode(reurl, "UTF-8");
                        return new ModelAndView("redirect:" + reurl);
                    } catch (UnsupportedEncodingException e) {
                        return new ModelAndView("/views/jsp/customize/mobile-result", map);
                    }
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e", e);

            if (e.equals(UserCenterServiceException.BIND_PHONE_ERROR_PHONE_HAS_BINDED)) {
                return new ModelAndView("redirect:/profile/mobile/gopage?phone=" + phone + "&error=" + PhoneVerifyError.ERROR_PHONE_HAS_BIND + "&type=" + type + "&reurl=" + reurl);
            } else {
                return new ModelAndView("redirect:/profile/mobile/gopage?phone=" + phone + "&type=" + type + "&reurl=" + reurl);
            }
        }

        return new ModelAndView("/views/jsp/customize/mobile-result", map);
    }

    @RequestMapping(value = "/send")
    public ModelAndView send(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "phone", required = false) String phone,
                             @RequestParam(value = "vcode", required = false) String vcode,
                             @RequestParam(value = "type", required = false) String type,
                             @RequestParam(value = "reurl", required = false) String reurl) {
        if (StringUtil.isEmpty(phone)) {
            return new ModelAndView("redirect:/profile/mobile/gopage?phone=" + phone + "&error=" + PhoneVerifyError.ERROR_PHONE_NULL + "&type=" + type + "&reurl=" + reurl);
        }


        if (StringUtil.isEmpty(vcode) || !ValidateImageUtil.checkImage(vcode, request)) {
            return new ModelAndView("redirect:/profile/mobile/gopage?phone=" + phone + "&error=" + PhoneVerifyError.ERROR_IMGCODE + "&type=" + type + "&reurl=" + reurl);
        }

        UserCenterSession userSession = getUserCenterSeesion(request);
        if (userSession == null) {
            return new ModelAndView("redirect:/profile/mobile/gopage?phone=" + phone + "&type=" + type + "&reurl=" + reurl);
        }

        boolean bval = false;
        try {
            bval = VerifyHttpUtil.verifyHttpRequest(request, userSession.getUno());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e", e);
        }

        if (!bval) {
            Map map = new HashMap();
            map.put("codeError", "request.invalid");
            return new ModelAndView("/views/jsp/customize/mobile", map);
        }

        try {

            //TODO 主站绑定手机号页面已移到wiki下。后续用户激励体系由java接过来。
//            String vTemplate = templateConfig.getVerifyMobileSmsTemplate();
//            MobileCodeDTO dto = mobileVerifyWebLogic.generatorCode(userSession.getUno(), phone, vTemplate, MobileVerifyWebLogic.VERIFYSMS_TIMES);
//
//            if (dto.getRs() == MobileCodeDTO.RS_SUCCESS) {
//                saveTimeInCookie(request, response);
//                //把生成code存入session
//                request.getSession().setAttribute(Constant.SESSION_MOBILE_CODE, dto.getCode());
//                UserCenterServiceSngl.get().saveMobileCode(userSession.getProfileId(), dto.getCode());
//            } else if (dto.getRs() == MobileCodeDTO.RS_ERROR_OUT_LIMIT) {
//                return new ModelAndView("redirect:/profile/mobile/gopage?phone=" + phone + "&error=" + PhoneVerifyError.ERROR_OUT_LIMIT + "&type=" + type + "&reurl=" + reurl);
//            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e", e);
            return new ModelAndView("redirect:/profile/mobile/gopage?phone=" + phone + "&type=" + type + "&reurl=" + reurl);
        }

        return new ModelAndView("redirect:/profile/mobile/gopage?phone=" + phone + "&type=" + type + "&reurl=" + reurl);
    }

    @RequestMapping(value = "/unbind")
    public ModelAndView unBind(HttpServletRequest request) {
        UserCenterSession userSession = getUserCenterSeesion(request);
        try {
            if (userSession != null) {
                boolean bval = mobileVerifyWebLogic.unbindMobile(userSession.getProfileId(), getIp(request));

                if (bval) {
                    userSession.setMobile(null);
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e", e);
            return new ModelAndView("redirect:/profile/mobile/gopage");
        }
        return new ModelAndView("redirect:/profile/mobile/gopage");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(HttpServletRequest request) {
        UserCenterSession userSession = getUserCenterSeesion(request);
        if (userSession == null) {
            return new ModelAndView("redirect:/profile/mobile/gopage");
        }

        return new ModelAndView("redirect:/profile/mobile/gopage?type=" + REDIRECT_MODIFY_TYPE);
    }

    @RequestMapping(value = "/checkphone")
    @ResponseBody
    public String checkPhone(@RequestParam(value = "phone", required = false) String phone,
                             @RequestParam(value = "profilekey", required = false, defaultValue = "www") String profileKey) {
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        try {
            String verifyPhoneStatus = mobileVerifyWebLogic.verifyMobileHasBinded(phone, profileKey);
            if (!verifyPhoneStatus.equals(PhoneVerifyError.SUCCESS)) {
                resultMsg.setMsg(i18nSource.getMessage("verify.phone.has.bind", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e", e);
        }
        return jsonBinder.toJson(resultMsg);
    }


    ///////////////////////////////////////////////////
    private void saveTimeInCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.setCookie(request, response, Constant.SESSION_SEND_INTRAV, String.valueOf(System.currentTimeMillis()), 60 * 1000);
    }

    private String getTimeInCookie(HttpServletRequest request) {
        if (CookieUtil.getCookie(request, Constant.SESSION_SEND_INTRAV) == null) {
            return null;
        }

        return CookieUtil.getCookie(request, Constant.SESSION_SEND_INTRAV).getValue();
    }

    private void deleteTimeInCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.setCookie(request, response, Constant.SESSION_SEND_INTRAV, String.valueOf(System.currentTimeMillis()), 0);
    }

}
