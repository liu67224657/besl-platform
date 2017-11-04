package com.enjoyf.platform.webapps.common;

import com.enjoyf.platform.util.Pagination;
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
public class ResultPageMsg {

    /**
     * 成功状态码
     */
    public static final int CODE_S = 1;
    /**
     * 失败状态码
     */
    public static final int CODE_E = 0;

    /**
     * 状态码
     */
    public int rs = ResultPageMsg.CODE_E;

    /**
     * 返回结果描述
     */
    public String msg = null;

    /**
     * 返回结果
     */
    protected List result = Lists.newArrayList();
    private JsonPagination page;

    /**
     *
     */
    public ResultPageMsg() {
        super();
    }

    /**
     * @param status_code
     */
    public ResultPageMsg(int status_code) {
        super();
        this.rs = status_code;
    }

    /**
     * @param status_code
     * @param msg
     */
    public ResultPageMsg(int status_code, String msg) {
        super();
        this.rs = status_code;
        this.msg = msg;
    }

    /**
     * @param rs
     * @param msg
     * @param result
     */
    public ResultPageMsg(int rs, String msg, List result) {
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

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }

    public JsonPagination getPage() {
        return page;
    }

    public void setPage(JsonPagination page) {
        this.page = page;
    }

    public static ResultPageMsg fromJson(String jsonString) throws JsonParseException, JsonMappingException {
        ResultPageMsg msg = null;
        msg = JsonBinder.buildNormalBinder().fromJson(jsonString, ResultPageMsg.class);

        return msg;
    }

}
