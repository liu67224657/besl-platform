package com.enjoyf.webapps.joyme.weblogic.chinajoy;

import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.google.common.collect.Lists;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-5-23
 * Time: 下午6:06
 * To change this template use File | Settings | File Templates.
 */
public class CjMsgDTO {
    /**
     * 成功状态码
     */
    public static final String CODE_S = "1";
    public static final String CODE_HAS = "2";
    public static final String CODE_ENFORCE = "3";
    /**
     * 失败状态码
     */
    public static final String CODE_E = "0";

    /**
     * 状态码
     */
    public String status_code = CjMsgDTO.CODE_E;

    /**
     * 返回结果描述
     */
    public String msg = null;

    /**
     * 返回结果
     */
    protected List results = Lists.newArrayList();

    /**
     *
     */
    public CjMsgDTO() {
        super();
    }

    /**
     * @param status_code
     */
    public CjMsgDTO(String status_code) {
        super();
        this.status_code = status_code;
    }

    /**
     * @param status_code
     * @param msg
     */
    public CjMsgDTO(String status_code, String msg) {
        super();
        this.status_code = status_code;
        this.msg = msg;
    }

    /**
     * @param status_code
     * @param msg
     * @param results
     */
    public CjMsgDTO(String status_code, String msg, List results) {
        super();
        this.status_code = status_code;
        this.msg = msg;
        this.results = results;
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

    public List getResults() {
        return results;
    }

    public void setResults(List results) {
        this.results = results;
    }

    public static CjMsgDTO fromJson(String jsonString) throws JsonParseException, JsonMappingException {
        CjMsgDTO msg = null;
        msg = JsonBinder.buildNormalBinder().fromJson(jsonString, CjMsgDTO.class);

        return msg;
    }
}
