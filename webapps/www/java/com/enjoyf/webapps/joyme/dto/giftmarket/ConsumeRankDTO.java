package com.enjoyf.webapps.joyme.dto.giftmarket;

import com.enjoyf.platform.service.profile.ProfileBlogHeadIconSet;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-25
 * Time: 下午7:57
 * To change this template use File | Settings | File Templates.
 */
public class ConsumeRankDTO {
    private String icon;
    private String domain;
    private String screenName;
    private int point;
    private String sex;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
