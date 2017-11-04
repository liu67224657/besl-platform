package com.enjoyf.webapps.joyme.dto.joymeapp.wikiapp;

import com.enjoyf.platform.service.usercenter.activityuser.ActivityActionType;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityObjectType;

import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/4/13
 * Description:
 */
public class ActivityUserDTO {
    private String nick;
    private String iconurl;
    private String desc;
    private String profileid;
    private String uno;
    private int actiontype;
    private int objecttype;
    private String objectid;
    private long actiontime;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getProfileid() {
        return profileid;
    }

    public void setProfileid(String profileid) {
        this.profileid = profileid;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public int getActiontype() {
        return actiontype;
    }

    public void setActiontype(int actiontype) {
        this.actiontype = actiontype;
    }

    public int getObjecttype() {
        return objecttype;
    }

    public void setObjecttype(int objecttype) {
        this.objecttype = objecttype;
    }

    public String getObjectid() {
        return objectid;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }

    public long getActiontime() {
        return actiontime;
    }

    public void setActiontime(long actiontime) {
        this.actiontime = actiontime;
    }
}
