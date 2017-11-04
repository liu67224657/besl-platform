package com.enjoyf.platform.thirdapi;

import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * <p/>
 * Description: 第三方接口属性类
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class ThirdApiProps implements Serializable {
    private LoginDomain loginDomain;
    private boolean bindDisplay = true;
    private boolean loginDisplay = true;
    private boolean supportSync = true;
     private boolean supportShare = false;
    private String defaultRurl = "/home";

    public boolean isBindDisplay() {
        return bindDisplay;
    }

    public void setBindDisplay(boolean bindDisplay) {
        this.bindDisplay = bindDisplay;
    }

    public boolean isLoginDisplay() {
        return loginDisplay;
    }

    public void setLoginDisplay(boolean loginDisplay) {
        this.loginDisplay = loginDisplay;
    }

    public boolean isSupportSync() {
        return supportSync;
    }

    public void setSupportSync(boolean supportSync) {
        this.supportSync = supportSync;
    }

    public void setLoginDomain(LoginDomain loginDomain) {
        this.loginDomain = loginDomain;
    }

    public void setDefaultRurl(String defaultRurl) {
        this.defaultRurl = defaultRurl;
    }

    public LoginDomain getLoginDomain() {
        return loginDomain;
    }

    public String getDefaultRurl() {
        return defaultRurl;
    }

    public boolean isSupportShare() {
        return supportShare;
    }

    public void setSupportShare(boolean supportShare) {
        this.supportShare = supportShare;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
