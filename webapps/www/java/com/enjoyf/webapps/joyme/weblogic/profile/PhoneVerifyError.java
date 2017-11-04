package com.enjoyf.webapps.joyme.weblogic.profile;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-28 上午10:59
 * Description:
 */
public class PhoneVerifyError {
    public static final String ERROR_IMGCODE = "-1";
    public static final String ERROR_OUT_LIMIT = "-2";
    public static final String ERROR_VERIFY = "-3";
    public static final String ERROR_VERIFY_NULL = "-4";
    public static final String ERROR_PHONE_NULL = "-5";
    public static final String ERROR_VERIFY_NOT_SEND = "-6";
    public static final String ERROR_PHONE_HAS_BIND = "-7";
    public static final String ERROR_PHONE_MODIFY = "-8";

    public static final String SUCCESS = "1";

    public static Map<String, String> errorMap = new HashMap<String, String>();

    static {
        errorMap.put(ERROR_IMGCODE, "imgcode.error");
        errorMap.put(ERROR_OUT_LIMIT, "sms.out.limit");
        errorMap.put(ERROR_VERIFY, "verify.error");
        errorMap.put(ERROR_VERIFY_NULL, "verify.is.null");
        errorMap.put(ERROR_PHONE_NULL, "verify.phone.is.null");
        errorMap.put(ERROR_VERIFY_NOT_SEND, "verify.is.not.send");
        errorMap.put(ERROR_PHONE_HAS_BIND, "verify.phone.has.bind");
        errorMap.put(ERROR_PHONE_MODIFY, "modify.phone.error");
    }

    public static String getErrorInfoByCode(String code) {
        return errorMap.get(code);
    }

}
