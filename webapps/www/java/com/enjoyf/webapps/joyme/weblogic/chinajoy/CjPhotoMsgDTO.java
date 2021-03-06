package com.enjoyf.webapps.joyme.weblogic.chinajoy;

import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.google.common.collect.Lists;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-5-24
 * Time: 下午2:35
 * To change this template use File | Settings | File Templates.
 */
public class CjPhotoMsgDTO {
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
    public String status_code = CjHomeMsgDTO.CODE_E;

    /**
     * 返回结果描述
     */
    public String msg = null;

    /**
     * 返回结果
     */
    protected List results = Lists.newArrayList();
    protected List tops = Lists.newArrayList();

    /**
     *
     */
    public CjPhotoMsgDTO() {
        super();
    }

    /**
     * @param status_code
     */
    public CjPhotoMsgDTO(String status_code) {
        super();
        this.status_code = status_code;
    }

    /**
     * @param status_code
     * @param msg
     */
    public CjPhotoMsgDTO(String status_code, String msg) {
        super();
        this.status_code = status_code;
        this.msg = msg;
    }

    /**
     * @param status_code
     * @param msg
     * @param results
     */
    public CjPhotoMsgDTO(String status_code, String msg, List results, List tops) {
        super();
        this.status_code = status_code;
        this.msg = msg;
        this.results = results;
        this.tops = tops;
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

    public List getTops() {
        return tops;
    }

    public void setTops(List tops) {
        this.tops = tops;
    }

    public static CjPhotoMsgDTO fromJson(String jsonString) throws JsonParseException, JsonMappingException {
        CjPhotoMsgDTO msg = null;
        msg = JsonBinder.buildNormalBinder().fromJson(jsonString, CjPhotoMsgDTO.class);

        return msg;
    }
}
