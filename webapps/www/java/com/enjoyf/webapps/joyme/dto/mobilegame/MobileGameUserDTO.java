package com.enjoyf.webapps.joyme.dto.mobilegame;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-9-15
 * Time: 下午1:50
 * To change this template use File | Settings | File Templates.
 */
public class MobileGameUserDTO implements Serializable {
    //'header': '${URL_LIB}/static/theme/default/images/mobilegame/user.jpg',
    //'msg': '弹幕啊'
    private String name;
    private String header;
    private String msg;


    private int agreeNum;//支持
    private int disagreeNum;//不支持
    private double scope;//短评的评分
    private long replyId;

    public long getReplyId() {
        return replyId;
    }

    public void setReplyId(long replyId) {
        this.replyId = replyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getAgreeNum() {
        return agreeNum;
    }

    public void setAgreeNum(int agreeNum) {
        this.agreeNum = agreeNum;
    }

    public int getDisagreeNum() {
        return disagreeNum;
    }

    public void setDisagreeNum(int disagreeNum) {
        this.disagreeNum = disagreeNum;
    }

    public double getScope() {
        return scope;
    }

    public void setScope(double scope) {
        this.scope = scope;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
