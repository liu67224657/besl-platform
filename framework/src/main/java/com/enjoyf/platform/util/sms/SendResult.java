package com.enjoyf.platform.util.sms;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-25 下午1:13
 * Description:
 */
public class SendResult {
    public static final int SEND_SUCESS = 0;
    public static final int URL_ERROR = -1;
    public static final int SEND_ERROR = -2;

    private int code;
    private String msg;
    private String thirdCode;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getThirdCode() {
        return thirdCode;
    }

    public void setThirdCode(String thirdCode) {
        this.thirdCode = thirdCode;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
