package com.enjoyf.webapps.joyme.weblogic.user;

import com.enjoyf.platform.service.usercenter.UserCenterUtil;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto: ericliu@enjoyfound.com> ericliu</a>
 */
@Service(value = "emailAuthWebLogic")
public class EmailAuthWebLogic {
    Logger logger = LoggerFactory.getLogger(EmailAuthWebLogic.class);


    /**
     * 认证邮箱修改account的认证状态
     *
     * @param loginId
     * @param emailAuth
     * @return
     * @throws ServiceException
     */
    public boolean authEmail(String loginId, String emailAuth) throws ServiceException {
        //用户信息不存在
        UserLogin userLogin = UserCenterServiceSngl.get().getUserLoginByLoginId(loginId);
        if (userLogin == null) {
            throw new ServiceException(UserCenterServiceException.USERLOGIN_NOT_EXISTS, "user not exists" + loginId);
        }

        UserAccount userAccount = UserCenterServiceSngl.get().getUserAccount(userLogin.getUno());
        //验证状态
        if (userAccount.getAccountFlag().hasFlag(AccountFlag.FLAG_EMAIL_VERIFY) || !userLogin.getAuthCode().equals(emailAuth)) {
            throw new ServiceException(UserCenterServiceException.EMAIL_AUTH_AUTHSTAUS_ERROR, "email auth status error" + loginId);
        }
        //验证连接是否过期
        if (userLogin.getAuthTime() != null && userLogin.getAuthTime().getTime() + Constant.USER_EMAIL_AUTH_TIMEOUT < new Date().getTime()) {
            throw new ServiceException(UserCenterServiceException.EMAIL_AUTH_TIMEOUT, "email auth timeout.");
        }

        //
        UserCenterServiceSngl.get().modifyUserLogin(new UpdateExpress().set(UserLoginField.AUTHCODE, ""), userLogin.getLoginId());

        return UserCenterServiceSngl.get().modifyUserAccount(new UpdateExpress().set(UserAccountField.FLAG, userAccount.getAccountFlag().has(AccountFlag.FLAG_EMAIL_VERIFY).getValue()), userAccount.getUno());
    }

    /**
     * 修改邮箱，改完后直接是认证状态
     *
     * @param loginId
     * @param newEmail
     * @param emailAuth
     * @return
     * @throws ServiceException
     */
    public boolean modifyEmail(String loginId, String newEmail, String emailAuth) throws ServiceException {

        //用户信息不存在
        UserLogin userLogin = UserCenterServiceSngl.get().getUserLoginByLoginId(loginId);
        if (userLogin == null) {
            throw new ServiceException(UserCenterServiceException.USERLOGIN_NOT_EXISTS, "user not exists" + loginId);
        }

        //验证连接是否过期
        if (userLogin.getAuthTime() != null && userLogin.getAuthTime().getTime() + Constant.USER_EMAIL_AUTH_TIMEOUT < new Date().getTime()) {
            throw new ServiceException(UserCenterServiceException.EMAIL_AUTH_TIMEOUT, "email auth timeout.");
        }

        UserAccount userAccount = UserCenterServiceSngl.get().getUserAccount(userLogin.getUno());


        //验证状态
        if (!userLogin.getAuthCode().equals(emailAuth)) {
            throw new ServiceException(UserCenterServiceException.EMAIL_AUTH_AUTHSTAUS_ERROR, "email auth status error" + loginId);
        }


        UserCenterServiceSngl.get().modifyUserLogin(new UpdateExpress()
                .set(UserLoginField.LOGIN_ID, UserCenterUtil.getUserLoginId(newEmail, LoginDomain.EMAIL))
                .set(UserLoginField.LOGIN_KEY, newEmail)
                .set(UserLoginField.AUTHCODE, ""), loginId);

        if (!userAccount.getAccountFlag().hasFlag(AccountFlag.FLAG_EMAIL_VERIFY)) {
            UserCenterServiceSngl.get().
                    modifyUserAccount(new UpdateExpress().
                                    set(UserAccountField.FLAG, userAccount.getAccountFlag().has(AccountFlag.FLAG_EMAIL_VERIFY).getValue())
                            , userAccount.getUno());
        }

        return true;
    }


}
