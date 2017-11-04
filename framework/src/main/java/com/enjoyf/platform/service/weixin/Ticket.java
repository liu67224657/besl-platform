package com.enjoyf.platform.service.weixin;

import java.io.Serializable;

/**
 * Created by zhimingli on 2016/6/17 0017.
 */
public class Ticket implements Serializable {
    private String errcode;//错误码
    private String errmsg;//错误信息
    private String ticket;//api_ticket，卡券接口中签名所需凭证
    private int expires_in;//有效时间

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
