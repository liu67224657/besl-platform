package com.enjoyf.platform.webapps.common;

import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.google.common.collect.Lists;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import java.util.List;

/**
 * 返回结果类
 * 主要用于ajax返回对象通过JsonBinder.buildNonNullBinder().toJson(resultMsg)返回
 *
 * @author xinzhao
 */
public class JoymeResultMsg {

    /**
     * 成功状态码
     */
    public static final String CODE_S = "1";
    public static final String CODE_HAS = "2";
    /**
     * 失败状态码
     */
    public static final String CODE_E = "0";

    public static final String CODE_NOT_LOGIN = "-1";
    public static final String CODE_PROFILE_BAN = "-2";

    public static final String CODE_FORBID_LOGIN = "-3";
    public static final String CODE_FORBID_POST = "-4";

    public static final String CODE_FORBIP = "-5";

    public static final String EXCHANG_POINT_NOT_ENOUGH = "-6";
    public static final String USER_GIFT_IP_EXCEED = "-8";
    public static final String USER_IS_NOT_BIND_PHONE = "-9";
    public static final String INTERVAL_TIME_ERROR = "-10";
    public static final String PHONE_IS_BIND = "-10";
    public static final String USERSSION_IS_NULL = "-11";


    /**
     * 状态码
     */
    public String status_code = JoymeResultMsg.CODE_E;

    /**
     * 返回结果描述
     */
    public String msg = null;

    /**
     * 返回结果
     */
    protected List result = Lists.newArrayList();

    /**
     *
     */
    public JoymeResultMsg() {
        super();
    }

    /**
     * @param status_code
     */
    public JoymeResultMsg(String status_code) {
        super();
        this.status_code = status_code;
    }

    /**
     * @param status_code
     * @param msg
     */
    public JoymeResultMsg(String status_code, String msg) {
        super();
        this.status_code = status_code;
        this.msg = msg;
    }

    /**
     * @param status_code
     * @param msg
     * @param result
     */
    public JoymeResultMsg(String status_code, String msg, List result) {
        super();
        this.status_code = status_code;
        this.msg = msg;
        this.result = result;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }

    public static JoymeResultMsg fromJson(String jsonString) throws JsonParseException, JsonMappingException {
        JoymeResultMsg msg = null;
        msg = JsonBinder.buildNormalBinder().fromJson(jsonString, JoymeResultMsg.class);

        return msg;
    }


}
