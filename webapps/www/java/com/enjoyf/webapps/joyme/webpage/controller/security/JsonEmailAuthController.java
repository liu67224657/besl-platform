package com.enjoyf.webapps.joyme.webpage.controller.security;

import com.enjoyf.platform.crypto.MD5Cryptor;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.user.UserEvent;
import com.enjoyf.platform.service.event.user.UserEventType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.webapps.common.security.DES;
import com.enjoyf.platform.webapps.common.util.WebUtil;
import com.enjoyf.webapps.joyme.weblogic.comm.mail.MailEngine;
import com.enjoyf.webapps.joyme.weblogic.comm.mail.MailInfo;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p/>
 * Description: 邮箱验证的email
 * </p>
 *
 * @author: <a href=mailto: ericliu@enjoyfound.com> ericliu</a>
 */
@Controller
@RequestMapping(value = "/json/security/email")
public class JsonEmailAuthController extends BaseRestSpringController {
    Logger logger = LoggerFactory.getLogger(JsonEmailAuthController.class);

    @Resource(name = "mailEngine")
    private MailEngine mailEngine;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    private TemplateHotdeployConfig config= HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

    //修改邮箱。。即登录名称
    @RequestMapping(value = "/modify")
    @ResponseBody
    public String jsonResetUserid(HttpServletRequest request,
                                  @RequestParam(value = "password", required = false) String password,
                                  @RequestParam(value = "newuserid", required = false) String newuserid) {


        if (StringUtil.isEmpty(password) || StringUtil.isEmpty(newuserid)) {
            return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("user.login.notnull", null, Locale.CHINA)));
        }

        //get userLogid
        UserCenterSession userSession = getUserCenterSeesion(request);
        UserLogin userLogin=null;
        try {
            Set<LoginDomain> loginDomains=new HashSet<LoginDomain>();
            loginDomains.add(LoginDomain.EMAIL);
            List<UserLogin> userLogins= UserCenterServiceSngl.get().queryUserLoginUno(userSession.getUno(), loginDomains);

            if(!CollectionUtil.isEmpty(userLogins)){
                userLogin=userLogins.get(0);
            }
        } catch (ServiceException e) {
            logger.error(" get userLogin " ,e);
            return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("json.setting.error", null, Locale.CHINA)));
        }
        if(userLogin==null){
            return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("json.setting.error", null, Locale.CHINA)));
        }

        //auth password
        if (userLogin.getLoginKey().equals(newuserid)){
            return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("userset.email.existst", null, Locale.CHINA)));
        }

        //modify userLoginByNewEmail
        try {
            UserLogin userLoginByNewEmail= UserCenterServiceSngl.get().getUserLoginByLoginKey(newuserid,LoginDomain.EMAIL);
            if (userLoginByNewEmail != null) {
                return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("userset.email.existst", null, Locale.CHINA)));
            }
        } catch (ServiceException e) {
            logger.error(" userLoginByNewEmail " ,e);
            return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("userset.email.existst", null, Locale.CHINA)));
        }

        if (!UserCenterUtil.getPassowrd(password, userLogin.getPasswdTime()).equalsIgnoreCase(userLogin.getLoginPassword())) {
            return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("userset.userpwd.error", null, Locale.CHINA)));
        }

        if (userLogin.getAuthTime()!=null && userLogin.getAuthTime().getTime() + Constant.USER_EMAIL_AUTH_PROIED > new Date().getTime()) {
            return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("emailauth.error.timelimit", null, Locale.CHINA)));
        }

        //为邮箱发送一封验证信，验证后才能修改邮箱
        Date date=new Date();
        String newCode = new MD5Cryptor().getMD5ofStr(date.toString());

        //修改认证信息

//        accountSecurity = AccountServiceSngl.get().applySecurity(accountSecurity);

        // user event
//        sendOutEmailAuthUserEvent(account.getAccountUno(), new Date(), getIp(request), UserEventType.USER_ACCOUNT_EMAIL_AUTH_APPLY);

        //注册成功发送mail
        try {
            UserCenterServiceSngl.get().modifyUserLogin(new UpdateExpress()
                    .set(UserLoginField.AUTHCODE,newCode).set(UserLoginField.AUTHTIME, date) ,userLogin.getLoginId());


            MailInfo mail = new MailInfo();
            mail.setFromName(WebUtil.getModifyMailFromName());
            mail.setFromAdd(WebUtil.getModifyMailFromAddr());
            mail.setTo(new String[]{newuserid});
            mail.setSubject(WebUtil.getModifyMailSubject());
            mail.setFtlUrl(config.getMailModifyFtlUri());
            Map<String, Object> map = new HashMap<String, Object>();
//            account.setUserid(newuserid);
            map.put("uno", userLogin.getLoginId());
            map.put("email", newuserid);
            map.put("newemail", newuserid);
            map.put("k", DES.base64Encode(newuserid.getBytes()));
            map.put("emailauth", newCode);
            mail.setParamMap(map);
            mailEngine.sendMailToFW(mail);
        } catch (Exception e) {
            logger.error("ajaxResetPwd occured Exception." + e);
        }

//        String accountUno ;

//        try {
////            if (!AccountDomain.DEFAULT.equals(userSession.getAccountDomain())) {
////                QueryExpress queryExpress = new QueryExpress();
////                queryExpress.add(QueryCriterions.eq(AccountProfileField.UNO, userSession.getBlogwebsite().getUno()));
////                queryExpress.add(QueryCriterions.eq(AccountProfileField.ACCOUNTDOMAIN, AccountDomain.DEFAULT.getCode()));
////                try {
////                    AccountProfile accountProfile = AccountServiceSngl.get().getAccountProfile(queryExpress);
////                    accountUno= accountProfile.getAccountUno();
////                } catch (ServiceException e) {
////                    logger.error("ajaxResetPwd occured ServiceException.e: ", e);
////                    return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("json.setting.error", null, Locale.CHINA)));
////                }
////            }else{
////                accountUno = userSession.getAccountUno();
////            }
////            Account account = AccountServiceSngl.get().getAccountByUno(accountUno);
////             //老邮箱相等
////            if (account.getUserid().equals(newuserid)){
////                return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("userset.email.existst", null, Locale.CHINA)));
////            }
//
////            Account userExists = userWebLogic.findUserByUserId(newuserid);
////            if (userExists != null) {
////                return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("userset.email.existst", null, Locale.CHINA)));
////            }
//
//            logger.debug("ajaxResetUserid login account：" + account);
//            if (!new MD5Cryptor().getMD5ofStr(password).equals(account.getUserpwd())) {
//                return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("userset.userpwd.error", null, Locale.CHINA)));
//            }
//
//            //为邮箱发送一封验证信，验证后才能修改邮箱
//            String newCode = new MD5Cryptor().getMD5ofStr(new Date().toString());
//
//            //修改认证信息
//            AccountSecurity accountSecurity = new AccountSecurity();
//            accountSecurity.setEmail(newuserid);
//            accountSecurity.setSecurityType(AccountSecurityType.EMAIL_MNODIFY);
//            accountSecurity.setUno(account.getAccountUno());
//            accountSecurity.setUrlKey(newCode);
//            accountSecurity.setApplyDate(new Date());
//            accountSecurity = AccountServiceSngl.get().applySecurity(accountSecurity);
//
//            // user event
//            sendOutEmailAuthUserEvent(account.getAccountUno(), new Date(), getIp(request), UserEventType.USER_ACCOUNT_EMAIL_AUTH_APPLY);
//
//            //注册成功发送mail
//            try {
//                MailInfo mail = new MailInfo();
//                mail.setFromName(WebUtil.getModifyMailFromName());
//                mail.setFromAdd(WebUtil.getModifyMailFromAddr());
//                mail.setTo(new String[]{newuserid});
//                mail.setSubject(WebUtil.getModifyMailSubject());
//                mail.setFtlUrl(config.getMailModifyFtlUri());
//                Map<String, Object> map = new HashMap<String, Object>();
//                account.setUserid(newuserid);
//                map.put("uno", account.getAccountUno());
//                map.put("email", account.getUserid());
//                map.put("newemail", newuserid);
//                map.put("k", DES.base64Encode(newuserid.getBytes()));
//                map.put("emailauth", newCode);
//                mail.setParamMap(map);
//                mailEngine.sendMailToFW(mail);
//            } catch (Exception e) {
//                logger.error("ajaxResetPwd occured Exception." + e);
//            }
//
//        } catch (ServiceException e) {
//            logger.error("ajaxResetPwd occured ServiceException." + e);
//            return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("json.setting.error", null, Locale.CHINA)));
//        }

        return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_S, i18nSource.getMessage("json.setting.success", null, Locale.CHINA)));
    }

    private void sendOutEmailAuthUserEvent(String uno, Date date, String ip, UserEventType uet) {
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

