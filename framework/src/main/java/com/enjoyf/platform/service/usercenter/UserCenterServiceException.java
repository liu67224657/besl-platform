package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.service.service.ServiceException;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/10/23
 * Description:
 */
public class UserCenterServiceException extends ServiceException {

    public static final UserCenterServiceException USERLOGIN_NOT_EXISTS =
            new UserCenterServiceException(BASE_USERCENTER + 0, "userlogin not exists");

    public static final UserCenterServiceException USERLOGIN_HAS_EXISTS =
            new UserCenterServiceException(BASE_USERCENTER + 1, "userlogin has exists");

    public static final UserCenterServiceException PROFILE_NOT_EXISTS =
            new UserCenterServiceException(BASE_USERCENTER + 2, "profile not exists");

    public static final UserCenterServiceException PROFILE_HAS_EXISTS =
            new UserCenterServiceException(BASE_USERCENTER + 3, "profile has exists");

    public static final UserCenterServiceException UID_GET_NULL =
            new UserCenterServiceException(BASE_USERCENTER + 4, "uid get null");

    public static final UserCenterServiceException USERLOGIN_WRONG_LOGINDOMAIN =
            new UserCenterServiceException(BASE_USERCENTER + 5, "wrong login domain");

    public static final UserCenterServiceException BIND_PHONE_ERROR_PHONE_HAS_BINDED =
            new UserCenterServiceException(BASE_USERCENTER + 6, "phone bind error. phone has binded");


    public static final UserCenterServiceException BIND_PHONE_ERROR_PRFOILE_HAS_BINDED =
            new UserCenterServiceException(BASE_USERCENTER + 7, "phone bind error. profile has binded");

    public static final UserCenterServiceException ACCOUNT_NOT_EXISTS =
            new UserCenterServiceException(BASE_USERCENTER + 8, "account not exists");

    public static final UserCenterServiceException NICK_HAS_EXIST= new UserCenterServiceException(BASE_USERCENTER+9,"nick has exist");

    public static final UserCenterServiceException PHONE_VERIFY_CODE_ERROR = new UserCenterServiceException(BASE_USERCENTER+10,"mobile code is error");

    public static final UserCenterServiceException PHONE_CODE_LIMIT = new UserCenterServiceException(BASE_USERCENTER+11,"sendsms.times.outlimit");

    public static final UserCenterServiceException EMAIL_AUTH_NOT_EXISTS = new UserCenterServiceException(BASE_ACCOUNT + 12, "error email auth has exists");
    public static final UserCenterServiceException EMAIL_AUTH_CODE_ERROR = new UserCenterServiceException(BASE_ACCOUNT + 13, "error authcode error");
    public static final UserCenterServiceException EMAIL_AUTH_TIMEOUT = new UserCenterServiceException(BASE_ACCOUNT + 14, "error auth email timeout");
    public static final UserCenterServiceException EMAIL_AUTH_AUTHSTAUS_ERROR = new UserCenterServiceException(BASE_ACCOUNT + 15, "error auth email status error");

    public UserCenterServiceException(int i, String s) {
        super(i, s);
    }
}
