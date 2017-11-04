package com.enjoyf.webapps.joyme.dto.profile;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-24 下午2:18
 * Description:
 */
public class MobileCodeDTO {
    public static final int RS_ERROR_OUT_LIMIT = -2;
     public static final int RS_ERROR_SEND = -1;
    public static final int RS_SUCCESS = 1;
    private String code;
    private int rs;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getRs() {
        return rs;
    }

    public void setRs(int rs) {
        this.rs = rs;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
