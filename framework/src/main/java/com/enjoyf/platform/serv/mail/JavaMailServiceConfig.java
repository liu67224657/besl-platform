/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.mail;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-25 上午11:22
 * Description:
 */
public class JavaMailServiceConfig {
    //the key of the java mail service.
    private String senderMailAddress;

    //the smtp setting
    private String smtpHostname;
    private boolean needAuth = false;
    private String authUser;
    private String authPwd;

    //
    public JavaMailServiceConfig(String senderMailAddress) {
        this.senderMailAddress = senderMailAddress;
    }

    public String getSenderMailAddress() {
        return senderMailAddress;
    }

    public String getSmtpHostname() {
        return smtpHostname;
    }

    public void setSmtpHostname(String smtpHostname) {
        this.smtpHostname = smtpHostname;
    }

    public boolean isNeedAuth() {
        return needAuth;
    }

    public void setNeedAuth(boolean needAuth) {
        this.needAuth = needAuth;
    }

    public String getAuthUser() {
        return authUser;
    }

    public void setAuthUser(String authUser) {
        this.authUser = authUser;
    }

    public String getAuthPwd() {
        return authPwd;
    }

    public void setAuthPwd(String authPwd) {
        this.authPwd = authPwd;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
