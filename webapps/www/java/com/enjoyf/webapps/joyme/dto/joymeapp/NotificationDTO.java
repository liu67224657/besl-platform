package com.enjoyf.webapps.joyme.dto.joymeapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-7
 * Time: 下午5:22
 * To change this template use File | Settings | File Templates.
 */
public class NotificationDTO implements Serializable {

    private String msgid;
    private String title;

    private String desc;
    // private String icon;
    private String time;
    private String jt;
    private String ji;

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

//
//	public String getIcon() {
//		return icon;
//	}
//
//	public void setIcon(String icon) {
//		this.icon = icon;
//	}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getJt() {
        return jt;
    }

    public void setJt(String jt) {
        this.jt = jt;
    }

    public String getJi() {
        return ji;
    }

    public void setJi(String ji) {
        this.ji = ji;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }


}
