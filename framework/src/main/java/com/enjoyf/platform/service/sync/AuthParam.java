package com.enjoyf.platform.service.sync;

import java.io.Serializable;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class AuthParam implements Serializable{
    //oauth2.0 code
    private String authCode;

    //oauth1.0
    private String verifyCode;
    private String requestToken;

    //用于oauth2.0
    private boolean  rl;
    private String rurl;
    private boolean sc;

    //扩展参数
    private Long extNum01;
    private String extString01;
    private String extString02;

    //用于判断是否是pc
    private String redirectType;
    private String appKey;

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public boolean isRl() {
        return rl;
    }

    public void setRl(boolean rl) {
        this.rl = rl;
    }

    public String getRurl() {
        return rurl;
    }

    public void setRurl(String rurl) {
        this.rurl = rurl;
    }

    public String getExtString01() {
        return extString01;
    }

    public void setExtString01(String extString01) {
        this.extString01 = extString01;
    }

    public String getExtString02() {
        return extString02;
    }

    public void setExtString02(String extString02) {
        this.extString02 = extString02;
    }

    public Long getExtNum01() {
        return extNum01;
    }

    public void setExtNum01(Long extNum01) {
        this.extNum01 = extNum01;
    }

    public boolean isSc() {
        return sc;
    }

    public void setSc(boolean sc) {
        this.sc = sc;
    }

    public String getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(String redirectType) {
        this.redirectType = redirectType;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Override
    public String toString() {
        return "AuthParam{" +
                "authCode='" + authCode + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                ", requestToken='" + requestToken + '\'' +
                '}';
    }
}
