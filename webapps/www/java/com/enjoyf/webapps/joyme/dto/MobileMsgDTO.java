package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-8-17
 * Time: 下午8:07
 * To change this template use File | Settings | File Templates.
 */
public class MobileMsgDTO {
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
    public String sts = MobileMsgDTO.CODE_E;

    /**
     * 返回结果描述
     */
    public String msg = null;

    /**
     * 返回结果
     */
    protected Object rs;

    /**
     *
     */
    public MobileMsgDTO() {
    }

    /**
     * @param status_code
     */
    public MobileMsgDTO(String status_code) {
        this.sts = status_code;
    }

    /**
     * @param status_code
     * @param msg
     */
    public MobileMsgDTO(String status_code, String msg) {
        this.sts = status_code;
        this.msg = msg;
    }

    /**
     * @param status_code
     * @param msg
     * @param rs
     */
    public MobileMsgDTO(String status_code, String msg, Object rs) {
        this.sts = status_code;
        this.msg = msg;
        this.rs = rs;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }

    public Object getRs() {
        return rs;
    }

    public void setRs(Object rs) {
        this.rs = rs;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public static MobileMsgDTO fromJson(String jsonString) throws JsonParseException, JsonMappingException {
        MobileMsgDTO msg = null;
        msg = JsonBinder.buildNormalBinder().fromJson(jsonString, MobileMsgDTO.class);

        return msg;
    }
}
