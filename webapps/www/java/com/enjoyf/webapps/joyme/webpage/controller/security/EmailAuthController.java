package com.enjoyf.webapps.joyme.webpage.controller.security;

import com.enjoyf.platform.crypto.MD5Cryptor;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.security.DES;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.platform.webapps.common.util.WebUtil;
import com.enjoyf.webapps.joyme.weblogic.comm.mail.MailEngine;
import com.enjoyf.webapps.joyme.weblogic.comm.mail.MailInfo;
import com.enjoyf.webapps.joyme.weblogic.user.EmailAuthWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p/>
 * Description: 邮箱验证的email
 * </p>
 *
 * @author: <a href=mailto: ericliu@enjoyfound.com> ericliu</a>
 */
@Controller
@RequestMapping(value = "/security/email")
public class EmailAuthController extends BaseRestSpringController {
    Logger logger = LoggerFactory.getLogger(EmailAuthController.class);

    @Resource(name = "emailAuthWebLogic")
    private EmailAuthWebLogic emailauthWebLogic;

//    @Resource(name = "userWebLogic")
//    private UserWebLogic userWebLogic;

    @Resource(name = "mailEngine")
    private MailEngine mailEngine;

    private TemplateHotdeployConfig config= HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);


    /**
     * 发送验证新的action,返回结果是json
     *
     * @param request
     * @return
     */
    @RequestMapping("/auth/send")
    public ModelAndView sendAuthEmail(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("nav", "email");

        mapMessage.put("reUrl","/index");

//        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_E);
        UserCenterSession session = getUserCenterSeesion(request);
//        String uno = session.getAccountUno();
//        Account account = null;
        try {
            if(!session.getFlag().hasFlag(ProfileFlag.FLAG_EMAIL)){
                return new ModelAndView("/views/jsp/customize/email-auth-send", mapMessage);
            }

            UserLogin userLogin=null;
            try {
                Set<LoginDomain> loginDomains=new HashSet<LoginDomain>();
                loginDomains.add(LoginDomain.EMAIL);
                List<UserLogin> userLogins= UserCenterServiceSngl.get().queryUserLoginUno(session.getUno(), loginDomains);

                if(!CollectionUtil.isEmpty(userLogins)){
                    userLogin=userLogins.get(0);
                }
            } catch (ServiceException e) {
                logger.error("emailModify occured ServiceException.e: ", e);
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
            if(userLogin==null){
                return new ModelAndView("/views/jsp/customize/email-auth-send", mapMessage);
            }
            mapMessage.put("userid",userLogin.getLoginKey());

            //倆小时内不能重复发
            if (userLogin.getAuthTime()!=null && userLogin.getAuthTime().getTime() + Constant.USER_EMAIL_AUTH_PROIED > new Date().getTime()) {
                mapMessage.put("errorInfo", "emailauth.error.timelimit");
                return new ModelAndView("/views/jsp/customize/email-auth-send", mapMessage);
            }

            //发送验证码
//            accountSecurity.setUrlKey(new MD5Cryptor().getMD5ofStr(new Date().toString()));
//            accountSecurity.setApplyDate(new Date());
//            accountSecurity = emailauthWebLogic.init(accountSecurity);

            Date date=new Date();
            String authCode=new MD5Cryptor().getMD5ofStr(date.toString());
            UserCenterServiceSngl.get().modifyUserLogin(new UpdateExpress()
                    .set(UserLoginField.AUTHCODE,authCode).set(UserLoginField.AUTHTIME,date) ,userLogin.getLoginId());


            MailInfo mail = new MailInfo();
            mail.setFromAdd(WebUtil.getVerifyMailFromAddr());
            mail.setFromName(WebUtil.getVerifyMailFromName());
            mail.setTo(new String[]{userLogin.getLoginKey()});
            mail.setSubject(WebUtil.getVerifyMailSubject());
            mail.setFtlUrl(config.getMailVerifyFtlUri());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("email", userLogin.getLoginKey());
            map.put("uno", userLogin.getLoginId());
            map.put("emailauth", authCode);
            mail.setParamMap(map);
            mailEngine.sendMailToFW(mail);



//            if(!AccountDomain.DEFAULT.equals(session.getAccountDomain()) ){
//                //查询该用户是否完善账号信息
//                QueryExpress queryExpress = new QueryExpress();
//                queryExpress.add(QueryCriterions.eq(AccountProfileField.UNO, session.getBlogwebsite().getUno()));
//                queryExpress.add(QueryCriterions.eq(AccountProfileField.ACCOUNTDOMAIN, AccountDomain.DEFAULT.getCode()));
//                try {
//                    AccountProfile accountProfile = AccountServiceSngl.get().getAccountProfile(queryExpress);
//                    account = AccountServiceSngl.get().getAccountByUno(accountProfile.getAccountUno());
//                } catch (ServiceException e) {
//                    GAlerter.lab(this.getClass().getName() + " occured ServiceException:", e);
//                    mapMessage.put("errorInfo", "system.error");
//                    return new ModelAndView("/views/jsp/customize/email-auth-send", mapMessage);
//                }
//            }else {
//                account = AccountServiceSngl.get().getAccountByUno(uno);
//            }
//
//            AccountSecurity accountSecurity = AccountServiceSngl.get().getSecurity(uno, AccountSecurityType.EMAIL_AUTH);

//            mapMessage.put("userid",account.getUserid());
//            if (accountSecurity == null) {
//                accountSecurity = new AccountSecurity();
//                accountSecurity.setUno(uno);
//                accountSecurity.setEmail(account.getUserid());
//                accountSecurity.setUrlKey(new MD5Cryptor().getMD5ofStr(new Date().toString()));
//                accountSecurity.setApplyDate(new Date());
//                accountSecurity = emailauthWebLogic.init(accountSecurity);
//            } else {
//                //倆小时内不能重复发
//                if (accountSecurity.getApplyDate().getTime() + Constant.USER_EMAIL_AUTH_PROIED > new Date().getTime()) {
//                    mapMessage.put("errorInfo", "emailauth.error.timelimit");
//                    return new ModelAndView("/views/jsp/customize/email-auth-send", mapMessage);
//                }
//                //发送验证码
//                accountSecurity.setUrlKey(new MD5Cryptor().getMD5ofStr(new Date().toString()));
//                accountSecurity.setApplyDate(new Date());
//                accountSecurity = emailauthWebLogic.init(accountSecurity);
//            }


//            if (accountSecurity != null) {
//                MailInfo mail = new MailInfo();
//                mail.setFromAdd(WebUtil.getVerifyMailFromAddr());
//                mail.setFromName(WebUtil.getVerifyMailFromName());
//                mail.setTo(new String[]{accountSecurity.getEmail()});
//                mail.setSubject(WebUtil.getVerifyMailSubject());
//                mail.setFtlUrl(config.getMailVerifyFtlUri());
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("email", accountSecurity.getEmail());
//                map.put("uno", accountSecurity.getUno());
//                map.put("emailauth", accountSecurity.getUrlKey());
//                mail.setParamMap(map);
//                mailEngine.sendMailToFW(mail);
//            }

            // user event
//            sendOutEmailAuthUserEvent(uno, new Date(), getIp(request), UserEventType.USER_ACCOUNT_EMAIL_AUTH_APPLY);

//            msg.setStatus_code(JoymeResultMsg.CODE_S);

        } catch (ServiceException e) {
            mapMessage.put("errorInfo", "system.error");
            return new ModelAndView("/views/jsp/customize/email-auth-send", mapMessage);
        }
        return new ModelAndView("/views/jsp/customize/email-auth-send", mapMessage);
    }


    /**
     * 邮件的验证链接
     *
     * @param loginId
     * @param authemail
     * @return
     */
    @RequestMapping("/auth/{loginId}/{authemail}")
    public ModelAndView authEmail(@PathVariable(value = "loginId") String loginId, @PathVariable(value = "authemail") String authemail, HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (StringUtil.isEmpty(loginId) || StringUtil.isEmpty(authemail)) {
            logger.debug("uno or authemail is empty");
            mapMessage.put("errorInfo", "emailauth.error.paramerror");
            return new ModelAndView("/views/jsp/email/email-error", mapMessage);
        }

        mapMessage.put("email",authemail);

        try {
            boolean b = emailauthWebLogic.authEmail(loginId, authemail);
            if (b) {
                //读取小纸条
//                MessageServiceSngl.get().readNoticeByType(loginId, NoticeType.MAIL_AUTH);

//                //更新状态
//                Map<ObjectField, Object> propsMap = new HashMap<ObjectField, Object>();
//                propsMap.put(AccountField.AUTHSTATUS, ActStatus.ACTED.getCode());
//
//                userWebLogic.updateAccountProps(uno, propsMap);
//                // user event
//                sendOutEmailAuthUserEvent(uno, new Date(), getIp(request), UserEventType.USER_ACCOUNT_EMAIL_AUTH_OK);
                UserCenterSession userCenterSession=getUserCenterSeesion(request);
                if(userCenterSession!=null){
                    userCenterSession.getAccountFlag().has(AccountFlag.FLAG_EMAIL_VERIFY);
                }

            } else {
                mapMessage.put("errorInfo", "system.error");
                return new ModelAndView("/views/jsp/email/email-error", mapMessage);
            }

        } catch (ServiceException e) {
            if (e.equals(UserCenterServiceException.EMAIL_AUTH_NOT_EXISTS)) {
                logger.error("email auth is not exists", e);
                mapMessage.put("errorInfo", "emailauth.error.paramerror");
            } else if (e.equals(UserCenterServiceException.EMAIL_AUTH_CODE_ERROR)) {
                logger.error("email auth code error", e);
                mapMessage.put("errorInfo", "emailauth.error.authcoderror");
            } else if (e.equals(UserCenterServiceException.EMAIL_AUTH_TIMEOUT)) {
                logger.error("email auth timeout", e);
                mapMessage.put("errorInfo", "emailauth.error.timeout");
            } else if (e.equals(UserCenterServiceException.EMAIL_AUTH_AUTHSTAUS_ERROR)) {
                logger.error("email auth status error", e);
                mapMessage.put("errorInfo", "emailauth.error.status");
            } else {
                logger.error("system error", e);
                mapMessage.put("errorInfo", "system.error");
            }

            return new ModelAndView("/views/jsp/email/email-error", mapMessage);
        }
        return new ModelAndView("/views/jsp/email/email-success");
    }

    /**
     * 修改邮箱并且认证
     *
     * @param loginId
     * @param k
     * @return
     */
    @RequestMapping("/modify/{loginId}/{authemail}")
    public ModelAndView modfiyUserEmail(@PathVariable(value = "loginId") String loginId, @PathVariable(value = "authemail") String authemail, @RequestParam(value = "k") String k, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        String newuserid = "";
        try {

            //解密邮箱认证
            newuserid = new String(DES.base64Decode(k.trim()));
            boolean bVal = emailauthWebLogic.modifyEmail(loginId, newuserid, authemail);


            // delete cookie
            CookieUtil.deleteALLCookies(request, response);
            request.getSession().invalidate();

            mapMessage.put("email", newuserid);
        } catch (ServiceException e) {
            if (e.equals(UserCenterServiceException.EMAIL_AUTH_NOT_EXISTS)) {
                logger.error("email auth is not exists", e);
                mapMessage.put("errorInfo", "emailauth.error.paramerror");
            } else if (e.equals(UserCenterServiceException.EMAIL_AUTH_CODE_ERROR)) {
                logger.error("email auth code error", e);
                mapMessage.put("errorInfo", "emailauth.error.authcoderror");
            } else if (e.equals(UserCenterServiceException.EMAIL_AUTH_TIMEOUT)) {
                logger.error("email auth timeout", e);
                mapMessage.put("errorInfo", "emailauth.error.timeout");
            } else if (e.equals(UserCenterServiceException.EMAIL_AUTH_AUTHSTAUS_ERROR)) {
                logger.error("email auth status error", e);
                mapMessage.put("errorInfo", "emailauth.error.status");
            } else {
                logger.error("system error", e);
                mapMessage.put("errorInfo", "system.error");
            }
            return new ModelAndView("/views/jsp/email/email-error", mapMessage);
        } catch (Exception e) {
            logger.error("system error.newuserid" + newuserid, e);
            mapMessage.put("errorInfo", "system.error");
            return new ModelAndView("/views/jsp/email/email-error", mapMessage);
        }
        // user event
//        sendOutEmailAuthUserEvent(uno, new Date(), getIp(request), UserEventType.USER_ACCOUNT_EMAIL_AUTH_OK);
        return new ModelAndView("/views/jsp/email/email-success", mapMessage);
    }


//    private void sendOutEmailAuthUserEvent(String uno, Date date, String ip, UserEventType uet) {
//        //post event
//        UserEvent userEvent = new UserEvent(uno);
//
//        userEvent.setEventType(uet);
//        userEvent.setCount(1l);
//        userEvent.setEventDate(date);
//        userEvent.setEventIp(ip);
//
//        try {
//            EventDispatchServiceSngl.get().dispatch(userEvent);
//        } catch (Exception e) {
//            logger.error("sendOutEmailAuthUserEvent error.", e);
//        }
//    }
}

