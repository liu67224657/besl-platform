/**
 * (c) 2008 Operation platform platform.com
 */
package com.enjoyf.platform.serv.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @Auther: <a mailto:yinpy@platform.com>Yin Pengyi</a>
 */
public class SmtpAuthenticator extends Authenticator {
    private String authUser = null;
    private String authPwd = null;

    public SmtpAuthenticator(String user, String pass) {
        super();

        authUser = user;
        authPwd = pass;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(authUser, authPwd);
    }
}
