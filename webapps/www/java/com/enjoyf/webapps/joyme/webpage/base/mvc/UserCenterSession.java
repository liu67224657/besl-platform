package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.service.usercenter.AccountFlag;
import com.enjoyf.platform.service.usercenter.Icons;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.ProfileFlag;

import java.util.Date;


/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/5
 * Description:
 */
public class UserCenterSession {
    private String profileId;
    private long uid;
    private String uno;
    private String nick;
    private String domain;
    private String description;
    private String icon;
    private Icons icons;
    private String appkey;
    private String profileKey;
    private ProfileFlag flag;
    private String realName;
    private String sex;
    private String birthday;
    private int provinceId = 0;
    private int cityId = 0;
    private String qq;
    private String mobile;

    private Date createDate;

    private Long getCodeTime;
    private Long taoCodeTime;
    private int pointAmount = -1;
    //微信订阅号的用户是否绑定手机  n=没有绑定  y=已绑定
    private String weixinPhoneBindStatus;
    private AccountFlag accountFlag;
    private String token;
    private LoginDomain loginDomain;


    public Long getGetCodeTime() {
        return getCodeTime;
    }

    public void setGetCodeTime(Long getCodeTime) {
        this.getCodeTime = getCodeTime;
    }

    public Long getTaoCodeTime() {
        return taoCodeTime;
    }

    public void setTaoCodeTime(Long taoCodeTime) {
        this.taoCodeTime = taoCodeTime;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public ProfileFlag getFlag() {
        return flag;
    }

    public void setFlag(ProfileFlag flag) {
        this.flag = flag;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getPointAmount() {
        return pointAmount;
    }

    public void setPointAmount(int pointAmount) {
        this.pointAmount = pointAmount;
    }

    public String getWeixinPhoneBindStatus() {
        return weixinPhoneBindStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public AccountFlag getAccountFlag() {
        return accountFlag;
    }

    public void setAccountFlag(AccountFlag accountFlag) {
        this.accountFlag = accountFlag;
    }

    public void setWeixinPhoneBindStatus(String weixinPhoneBindStatus) {
        this.weixinPhoneBindStatus = weixinPhoneBindStatus;
    }

    public Icons getIcons() {
        return icons;
    }

    public void setIcons(Icons icons) {
        this.icons = icons;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProfileKey() {
        return profileKey;
    }

    public void setProfileKey(String profileKey) {
        this.profileKey = profileKey;
    }

    public LoginDomain getLoginDomain() {
        return loginDomain;
    }

    public void setLoginDomain(LoginDomain loginDomain) {
        this.loginDomain = loginDomain;
    }


}
