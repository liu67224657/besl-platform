/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-28 下午9:44
 * Description:
 */
@Deprecated
public class Profile implements Serializable {
    //
    private String uno;

    //
    private ProfileBlog blog;
    private ProfileDetail detail;
    private ProfileSum sum;
    private ProfileSetting setting;

    //the online status
    private ProfileOnlineStatus onlineStatus = ProfileOnlineStatus.UNKNOWN;

    //
    public Profile(String uno) {
        this.uno = uno;
    }

    public String getUno() {
        return uno;
    }

    public ProfileBlog getBlog() {
        return blog;
    }

    public void setBlog(ProfileBlog blog) {
        this.blog = blog;
    }

    public ProfileDetail getDetail() {
        return detail;
    }

    public void setDetail(ProfileDetail detail) {
        this.detail = detail;
    }

    public ProfileSum getSum() {
        return sum;
    }

    public void setSum(ProfileSum sum) {
        this.sum = sum;
    }

    public ProfileSetting getSetting() {
        return setting;
    }

    public void setSetting(ProfileSetting setting) {
        this.setting = setting;
    }

    public ProfileOnlineStatus getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(ProfileOnlineStatus onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    //
    @Override
    public int hashCode() {
        return uno.hashCode();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
