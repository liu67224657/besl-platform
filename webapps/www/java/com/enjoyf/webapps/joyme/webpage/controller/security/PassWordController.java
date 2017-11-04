package com.enjoyf.webapps.joyme.webpage.controller.security;

import com.enjoyf.platform.crypto.MD5Cryptor;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.user.UserEvent;
import com.enjoyf.platform.service.event.user.UserEventType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.CommonMail;
import com.enjoyf.platform.webapps.common.util.WebUtil;
import com.enjoyf.webapps.joyme.weblogic.comm.mail.MailEngine;
import com.enjoyf.webapps.joyme.weblogic.comm.mail.MailInfo;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-9-20
 * Time: 上午10:06
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/security/pwd")
public class PassWordController extends BaseRestSpringController {
    private Logger logger = LoggerFactory.getLogger(PassWordController.class);


    @Resource(name = "mailEngine")
    private MailEngine mailEngine;

    private TemplateHotdeployConfig config = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

    private static final String pwd_resetkety = "&(*^^&%XXXldfd@2123";

    /**
     * <pre>
     * 忘记密码 / pwd / forgot
     * </pre>
     */
    @RequestMapping(value = "/forgot")
    public ModelAndView pwdForgot(HttpServletRequest request, HttpServletResponse response) {

        String sReString = "/views/jsp/pwdforgot";
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UserLogin userLogin = null;

        String userid = request.getParameter("userid");
        if (WebUtil.stringFieldIsEmpty(userid) || !WebUtil.verifyEmail(userid)) {
            if (logger.isDebugEnabled()) {
                logger.debug("userid is empty");
            }

            if(!WebUtil.stringFieldIsEmpty(userid)){
                mapMessage.put("userid", userid);
                mapMessage.put("message", "user.pwd.forgot.wrong");
            }

            return new ModelAndView(sReString, mapMessage);
        }

        try {
            userLogin = UserCenterServiceSngl.get().getUserLoginByLoginKey(userid, LoginDomain.EMAIL);
            if (userLogin == null) {
                mapMessage.put("userid", userid);
                mapMessage.put("message", "user.pwd.forgot.notexists");
                return new ModelAndView(sReString, mapMessage);
            }

            String loginId = userLogin.getLoginId();
            Long passwordTime = null;
            passwordTime = UserCenterServiceSngl.get().getPassordCode(loginId);

//            if (passwordTime != null && passwordTime + Constant.USER_EMAIL_AUTH_PROIED > new Date().getTime()) {
//                mapMessage.put("message", "emailauth.error.timelimit");
//                return new ModelAndView(sReString, mapMessage);
//            }

            passwordTime = System.currentTimeMillis();
            String userPwdForgotStatus = new MD5Cryptor().getMD5ofStr(passwordTime + loginId + pwd_resetkety);
            // 用户密码找回findurl
            MailInfo mailInfo = new MailInfo();
            mailInfo.setFromName(WebUtil.getPwdForgotMailFromName());
            mailInfo.setFromAdd(WebUtil.getPwdForgotMailFromAddr());
            mailInfo.setTo(new String[]{userLogin.getLoginKey()});
            mailInfo.setSubject(WebUtil.getPwdForgotMailSubject());
            mailInfo.setFtlUrl(config.getMailPwdForgetFtlUri());
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("userLogin", userLogin);
            paramMap.put("findurl", userPwdForgotStatus);
            mailInfo.setParamMap(paramMap);
            mailEngine.sendMailToFW(mailInfo);

            mapMessage.put("message", "user.pwd.forgot.success");
            mapMessage.put("userid", userid);
            UserCenterServiceSngl.get().savePasswordCode(loginId, passwordTime);

            CommonMail commonMail = CommonMail.getByMail(userid);
            if (commonMail != null) {
                mapMessage.put("hrefMail", commonMail);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " send forgetMail occured ServiceException.e", e);
        }
        return new ModelAndView(sReString, mapMessage);

//
//            try {
//                // ---------------------------------------------
////                AccountSecurity accountSecurity = new AccountSecurity();
////                accountSecurity.setUno(userLogin.get());
////                accountSecurity.setSecurityType(AccountSecurityType.FIND_PWD);
////                AccountSecurity reAccountSecurity = userWebLogic.queryUserFindpasswd(accountSecurity);
//
//                // ---------------------------------------------
//                String findUrl = "";
//
//                boolean bVal = false;
//                String userPwdForgotStatus = "";
//                if (reAccountSecurity == null) {
//
//                    // 用户密码找回状态字段
//                    userPwdForgotStatus = new MD5Cryptor().getMD5ofStr(System.currentTimeMillis() + loginId+pwd_resetkety);
//                    // 用户密码找回findurl
//                    findUrl = Constant.URL_WWW + "/security/pwd/reset/" + loginId
//                            + "/" + userPwdForgotStatus;
////
////                    AccountSecurity accountSecurity1 = new AccountSecurity();
////                    accountSecurity1.setSecurityType(AccountSecurityType.FIND_PWD);
////                    accountSecurity1.setUrlKey(userPwdForgotStatus);
////                    accountSecurity1.setUno(account.getAccountUno());
////
////                    accountSecurity1 = userWebLogic.createUserFindpasswd(accountSecurity1);
////                    if (accountSecurity1 != null) {
////                        bVal = true;
////                    }
//                } else {
//                    //倆小时内不能重复发
//                    if (reAccountSecurity.getApplyDate().getTime() + Constant.USER_EMAIL_AUTH_PROIED > new Date().getTime()) {
//                        mapMessage.put("message", "emailauth.error.timelimit");
//                        return new ModelAndView(sReString, mapMessage);
//                    }
//                    //重新设置时间+KEY
//                    userPwdForgotStatus = new MD5Cryptor().getMD5ofStr(System.currentTimeMillis() + "");
//                    reAccountSecurity.setUrlKey(userPwdForgotStatus);
//
//                    // 用户密码找回findurl
//                    findUrl = Constant.URL_WWW + "/security/pwd/reset/" + account.getAccountUno()
//                            + "/" + userPwdForgotStatus;
//                    reAccountSecurity = AccountServiceSngl.get().applySecurity(reAccountSecurity);
//                    if (reAccountSecurity != null) {
//                        bVal = true;
//                    }
//                }
//
//                if (bVal) {
//                    MailInfo mailInfo = new MailInfo();
//                    mailInfo.setFromName(WebUtil.getPwdForgotMailFromName());
//                    mailInfo.setFromAdd(WebUtil.getPwdForgotMailFromAddr());
//                    mailInfo.setTo(new String[]{account.getUserid()});
//                    mailInfo.setSubject(WebUtil.getPwdForgotMailSubject());
//                    mailInfo.setFtlUrl(config.getMailPwdForgetFtlUri());
//                    Map<String, Object> paramMap = new HashMap<String, Object>();
//                    paramMap.put("account", account);
//                    paramMap.put("findurl", userPwdForgotStatus);
//                    mailInfo.setParamMap(paramMap);
//                    mailEngine.sendMailToFW(mailInfo);
//
//                    mapMessage.put("message", "user.pwd.forgot.success");
//                    mapMessage.put("userLogin", userLogin);
//                    mapMessage.put("userid", userid);
//
//                    CommonMail commonMail = CommonMail.getByMail(userid);
//                    if (commonMail != null) {
//                        mapMessage.put("hrefMail", commonMail);
//                    }
//                    return new ModelAndView(sReString, mapMessage);
//                }
//            } catch (ServiceException e) {
//                GAlerter.lab(this.getClass().getName() + " send forgetMail occured ServiceException.e", e);
//            }
        // post user event
//            sendOutPwdResetUserEvent(account.getAccountUno(), new Date(), getIp(request), UserEventType.USER_ACCOUNT_CHPWD_APPLY);
//            mapMessage.put("message", "system.error");
//            return new ModelAndView(sReString, mapMessage);
    }

    /**
     * <pre>
     * 忘记密码,重置密码
     * /security/pwd/reset
     * </pre>
     */
    @RequestMapping(value = "/reset/{loginId}/{k}")
    public ModelAndView pwdReset(HttpServletRequest request, HttpServletResponse response, @PathVariable String loginId,
                                 @PathVariable String k) {

        logger.debug("pwdReset");
        String sReString = "/views/jsp/pwdreset";
        Map mapMessage = new HashMap();

        logger.debug("uno:" + loginId);
        logger.debug("k:" + k);

        // 密码找回功能： 字段属性：md5(当前时间) or ''
        // {k}的值是用户表中一个“密码找回”字段的MD5加密。
        // 通过这个值与数据库中的md5(值) 比较，相同则拿数据库中的值（long型的时间字段）进行过期判断。
        // 全是正确，则清空字段。

        if (WebUtil.stringFieldIsEmpty(loginId) || WebUtil.stringFieldIsEmpty(k)) {
            logger.debug("参数错误，请检查链接地址是否正确");
            mapMessage.put("message", "user.pwd.reset.wrong");// 参数错误，请检查链接地址是否正确
            return new ModelAndView(sReString, mapMessage);
        }

        try {
            Long passwordTime = UserCenterServiceSngl.get().getPassordCode(loginId);
            String userPwdForgotStatus = new MD5Cryptor().getMD5ofStr(passwordTime.longValue() + loginId + pwd_resetkety);
            if (!k.equalsIgnoreCase(userPwdForgotStatus)) {
                mapMessage.put("message", "user.pwd.reset.wrong");// 参数错误，请检查链接地址是否正确
                return new ModelAndView(sReString, mapMessage);
            }
            mapMessage.put("uno", loginId);
            mapMessage.put("k", k);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView(sReString, mapMessage);

//
//        Account account = null;
//        AccountSecurity reAccountSecurity = null;
//        try {
//            account = userWebLogic.findAccountByUno(uno);
//            AccountSecurity accountSecurity = new AccountSecurity();
//            accountSecurity.setUno(uno);
//            reAccountSecurity = userWebLogic.queryUserFindpasswd(accountSecurity);
//
//        } catch (ServiceException e) {
//            logger.error(e.getMessage(), e);
//        }
//
//        if (reAccountSecurity == null) {
//            logger.debug("没有查到用户账户");
//            mapMessage.put("message", "user.pwd.reset.url.illegl");// 链接已失效，请重新找回密码。
//            return new ModelAndView(sReString, mapMessage);
//        }
//
//        // if equals
//        if (reAccountSecurity != null && k.equals(reAccountSecurity.getUrlKey())) {
//            // 如果当前时间 - cTime <2 day 返回成功
//            Date appDate = DateUtil.dateAdd(new Date(), -Constant.USER_PWD_FORGOT_MAX, DateUtil.DATE_TYPE_HOUR);
//
//            if (!appDate.after(reAccountSecurity.getApplyDate())) {
//                mapMessage.put("uno", uno);
//                mapMessage.put("k", k);
//                return new ModelAndView(sReString, mapMessage);
//            }
//        } else {
//            mapMessage.put("message", "user.pwd.reset.url.illegl");// 参数错误，请检查链接地址是否正确
//        }


//        return new ModelAndView(sReString, mapMessage);
    }

    @RequestMapping(value = "/reset")
    public ModelAndView pwdNewReset(HttpServletRequest request, HttpServletResponse response) {
        String sReString = "/views/jsp/pwdreset";
        Map mapMessage = new HashMap();

        // 校验两次密码
        String sPwd = request.getParameter("password");
        String sTwoPwd = request.getParameter("repassword");
        // 用户UNo
        String loginId = request.getParameter("uno");
        // 通过地址带过来的K。
        String sK = request.getParameter("k");

        if (WebUtil.stringFieldIsEmpty(loginId) || WebUtil.stringFieldIsEmpty(sK)) {
            mapMessage.put("message", "user.pwd.reset.wrong");// 参数错误，请检查链接地址是否正确
            return new ModelAndView(sReString, mapMessage);
        }

        if (!sPwd.equals(sTwoPwd)) {
            mapMessage.put("message", "user.pwd.reset.wrong");// 参数错误
            return new ModelAndView(sReString, mapMessage);
        }

        try {
            Long passwordTime = UserCenterServiceSngl.get().getPassordCode(loginId);
            String userPwdForgotStatus = new MD5Cryptor().getMD5ofStr(passwordTime + loginId + pwd_resetkety);
            if (!sK.equalsIgnoreCase(userPwdForgotStatus)) {
                mapMessage.put("message", "user.pwd.reset.wrong");// 参数错误，请检查链接地址是否正确
                return new ModelAndView(sReString, mapMessage);
            }

            String passwdTime = String.valueOf(System.currentTimeMillis());
            String password = UserCenterUtil.getPassowrd(sPwd, passwdTime);

            UserLogin userLogin = UserCenterServiceSngl.get().getUserLoginByLoginId(loginId);
            if (userLogin == null) {
                logger.debug("没有查到用户");
                mapMessage.put("message", "user.pwd.forgot.notexists");
                return new ModelAndView(sReString, mapMessage);
            }
            UserCenterServiceSngl.get().removePasswordCode(loginId);
            UserCenterServiceSngl.get().modifyUserLogin(new UpdateExpress()
                    .set(UserLoginField.LOGIN_PASSWORD, password).set(UserLoginField.PASSWDTIME, passwdTime), loginId);
            mapMessage.put("message", "user.pwd.reset.success");// 新密码保存成功，请重新登JM网
            return new ModelAndView(sReString, mapMessage);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage.put("message", "system.error");
            return new ModelAndView(sReString, mapMessage);
        }


//        Account account = null;
//        try {
//            account = userWebLogic.findAccountByUno(sUno);
//        } catch (ServiceException e) {
//            logger.error(e.toString());
//        }
//
//        if (account == null) {
//            logger.debug("没有查到用户");
//            mapMessage.put("message", "user.pwd.forgot.notexists");
//            return new ModelAndView(sReString, mapMessage);
//        }
//
//        AccountSecurity accountSecurity = new AccountSecurity();
//        accountSecurity.setUno(sUno);
//        accountSecurity.setUseStatus(ActStatus.ACTED);
//
//        AccountSecurity accountSecurity1 = null;
//        try {
//            accountSecurity1 = userWebLogic.queryUserFindpasswd(accountSecurity);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }
//        if (accountSecurity1 != null && accountSecurity1.getUrlKey().equals(sK) && accountSecurity1.getUseStatus().equals(ActStatus.UNACT)) {
//
//            try {
//                account.setUserpwd(new MD5Cryptor().getMD5ofStr(sPwd));
//                userWebLogic.updateUserPwdByUno(account);
//
//
////                AccountSecurity accountSecurity2 = new AccountSecurity();
////                accountSecurity2.setUno(account.getUno());
////                accountSecurity2.setUseStatus(ActStatus.ACTED);
////                accountSecurity2.setApplyDate(new Date());
//
//                userWebLogic.updateUserPwdForgotStatusForUno(accountSecurity1);
//                mapMessage.put("message", "user.pwd.reset.success");// 新密码保存成功，请重新登JM网
//
//                sendOutPwdResetUserEvent(account.getAccountUno(), new Date(), getIp(request), UserEventType.USER_ACCOUNT_CHPWD_OK);
//                return new ModelAndView(sReString, mapMessage);
//
//            } catch (ServiceException e) {
//                logger.error(e.getMessage(), e);
//                // 没有查到有效的K
//                mapMessage.put("message", "user.pwd.reset.wrong");// 没有查到有效的K
//                return new ModelAndView(sReString, mapMessage);
//            }
//
//        } else {
//            // 没有查到有效的K
//            mapMessage.put("message", "user.pwd.reset.wrong");// 没有查到有效的K
//            return new ModelAndView(sReString, mapMessage);
//        }

    }

    private void sendOutPwdResetUserEvent(String uno, Date date, String ip, UserEventType uet) {
        //post event
        UserEvent userEvent = new UserEvent(uno);

        userEvent.setEventType(uet);
        userEvent.setCount(1l);
        userEvent.setEventDate(date);
        userEvent.setEventIp(ip);

        try {
            EventDispatchServiceSngl.get().dispatch(userEvent);
        } catch (Exception e) {
            logger.error("sendOutEmailAuthUserEvent error.", e);
        }
    }
}
