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
public class ResultObjectMsg {

    /**
     * 成功状态码
     */
    public static final int CODE_S = 1;
    /**
     * 失败状态码
     */
    public static final int CODE_E = 0;

    public static final int CODE_ERROR_APP_NOTEXISTS=-100;
    public static final int CODE_ERROR_SIGN_FAIED=-101;
    public static final int CODE_ERROR_SIGN_PARAMEMPTY=-102;
    public static final int CODE_ERROR_PARAM_EMPTY = -103;

    /**
     * 状态码
     */
    public int rs = ResultObjectMsg.CODE_E;

    /**
     * 返回结果描述
     */
    public String msg = null;

    /**
     * 返回结果
     */
    protected Object result;

    /**
     *
     */
    public ResultObjectMsg() {
        super();
    }

    /**
     * @param status_code
     */
    public ResultObjectMsg(int status_code) {
        super();
        this.rs = status_code;
    }

    /**
     * @param status_code
     * @param msg
     */
    public ResultObjectMsg(int status_code, String msg) {
        super();
        this.rs = status_code;
        this.msg = msg;
    }

    /**
     * @param rs
     * @param msg
     * @param result
     */
    public ResultObjectMsg(int rs, String msg, Object result) {
        super();
        this.rs = rs;
        this.msg = msg;
        this.result = result;
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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public static ResultObjectMsg fromJson(String jsonString) throws JsonParseException, JsonMappingException {
        ResultObjectMsg msg = null;
        msg = JsonBinder.buildNormalBinder().fromJson(jsonString, ResultObjectMsg.class);

        return msg;
    }

}
