/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-23 下午1:47
 * Description:
 */
public class ProfileClientDTO {
    //
    private String uno;
    private String screenName;
    private String headIcon;

    private String loginName ;
    private String sex;
    //
    private Integer focusSum = 0;
    private Integer fansSum = 0;
    private Integer blogSum = 0;
    private Integer forwardSum = 0;
    private Integer favorSum = 0;


    //constructor
    public ProfileClientDTO() {
    }

    //the getter and setter
    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getFocusSum() {
        return focusSum;
    }

    public void setFocusSum(Integer focusSum) {
        this.focusSum = focusSum;
    }

    public Integer getFansSum() {
        return fansSum;
    }

    public void setFansSum(Integer fansSum) {
        this.fansSum = fansSum;
    }

    public Integer getBlogSum() {
        return blogSum;
    }

    public void setBlogSum(Integer blogSum) {
        this.blogSum = blogSum;
    }

    public Integer getForwardSum() {
        return forwardSum;
    }

    public void setForwardSum(Integer forwardSum) {
        this.forwardSum = forwardSum;
    }

    public Integer getFavorSum() {
        return favorSum;
    }

    public void setFavorSum(Integer favorSum) {
        this.favorSum = favorSum;
    }

    //to string
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
