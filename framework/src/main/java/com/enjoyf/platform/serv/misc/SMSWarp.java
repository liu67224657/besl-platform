package com.enjoyf.platform.serv.misc;

import java.io.Serializable;

class SMSWarp implements Serializable {
    private String phone;
    private String content;
    private String type;
    private String code;

    SMSWarp(String phone, String content, String type, String code) {
        this.phone = phone;
        this.content = content;
        this.type = type;
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }
}