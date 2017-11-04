package com.enjoyf.webapps.joyme.weblogic.chinajoy;

import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.google.common.collect.Lists;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-5-28
 * Time: 上午11:03
 * To change this template use File | Settings | File Templates.
 */
public class CjForumMsgDTO {
    /**
     * 成功状态码
     */
    public static final String CODE_S = "1";
    public static final String CODE_HAS = "2";
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

    public String des = null;

    /**
     *
     */
    public CjForumMsgDTO() {
        super();
    }

    /**
     * @param status_code
     */
    public CjForumMsgDTO(String status_code) {
        super();
        this.status_code = status_code;
    }

    /**
     * @param status_code
     * @param msg
     */
    public CjForumMsgDTO(String status_code, String msg) {
        super();
        this.status_code = status_code;
        this.msg = msg;
    }

    /**
     * @param status_code
     * @param msg
     * @param results
     */
    public CjForumMsgDTO(String status_code, String msg, List results) {
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

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public static CjForumMsgDTO fromJson(String jsonString) throws JsonParseException, JsonMappingException {
        CjForumMsgDTO msg = null;
        msg = JsonBinder.buildNormalBinder().fromJson(jsonString, CjForumMsgDTO.class);

        return msg;
    }
}
