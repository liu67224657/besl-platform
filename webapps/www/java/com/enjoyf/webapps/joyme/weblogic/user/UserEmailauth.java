package com.enjoyf.webapps.joyme.weblogic.user;


import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 */
public class UserEmailauth implements java.io.Serializable {

    private Integer emailauthid;
    private String uno;
    private String useremail;
    private String emailauth;
    private String authstatus;
    private Date senddate;
    private Date authdate;

    public UserEmailauth() {
    }

    public UserEmailauth(String uno, String useremail, String emailauth,
                         String authstatus, Date senddate) {
        this.uno = uno;
        this.useremail = useremail;
        this.emailauth = emailauth;
        this.authstatus = authstatus;
        this.senddate = senddate;
    }

    public UserEmailauth(String uno, String useremail, String emailauth,
                         String authstatus, Date senddate, Date authdate) {
        this.uno = uno;
        this.useremail = useremail;
        this.emailauth = emailauth;
        this.authstatus = authstatus;
        this.senddate = senddate;
        this.authdate = authdate;
    }

    public Integer getEmailauthid() {
        return this.emailauthid;
    }

    public void setEmailauthid(Integer emailauthid) {
        this.emailauthid = emailauthid;
    }

    public String getUno() {
        return this.uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getUseremail() {
        return this.useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getEmailauth() {
        return this.emailauth;
    }

    public void setEmailauth(String emailauth) {
        this.emailauth = emailauth;
    }

    public String getAuthstatus() {
        return this.authstatus;
    }

    public void setAuthstatus(String authstatus) {
        this.authstatus = authstatus;
    }

    public Date getSenddate() {
        return this.senddate;
    }

    public void setSenddate(Date senddate) {
        this.senddate = senddate;
    }

    public Date getAuthdate() {
        return this.authdate;
    }

    public void setAuthdate(Date authdate) {
        this.authdate = authdate;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
