package com.enjoyf.platform.service.event;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-29
 * Time: 上午11:59
 * To change this template use File | Settings | File Templates.
 */
public class AwardResult {
    private String code;
    private int status;//0-失败 1-成功

    public String getCode() {
        return code;
    }

    public void setCode(String resultid) {
        this.code = resultid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
