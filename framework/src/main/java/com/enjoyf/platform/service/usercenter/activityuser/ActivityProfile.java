package com.enjoyf.platform.service.usercenter.activityuser;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/4/9
 * Description:
 */
public class ActivityProfile implements Serializable {
    private String nick;
    private String iconurl;
    private String desc;

    private ActivityUser activityUser;


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

    public ActivityUser getActivityUser() {
        return activityUser;
    }

    public void setActivityUser(ActivityUser activityUser) {
        this.activityUser = activityUser;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
