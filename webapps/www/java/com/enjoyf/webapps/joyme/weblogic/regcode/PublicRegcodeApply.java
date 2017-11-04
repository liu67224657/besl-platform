package com.enjoyf.webapps.joyme.weblogic.regcode;


import java.util.Date;

/**
 * 注册码申请的实体类
 */
public class PublicRegcodeApply implements java.io.Serializable {

    private Integer applyid;
    private String useremail;
    private String introduce;
    private Date applydate;
    private String ipaddress;
    private String regcode;

    public PublicRegcodeApply() {
    }

    public PublicRegcodeApply(String useremail, String introduce,
                              Date applydate) {
        this.useremail = useremail;
        this.introduce = introduce;
        this.applydate = applydate;
    }

    public PublicRegcodeApply(String useremail, String introduce,
                              Date applydate, String ipaddress, String regcode) {
        this.useremail = useremail;
        this.introduce = introduce;
        this.applydate = applydate;
        this.ipaddress = ipaddress;
        this.regcode = regcode;
    }

    public Integer getApplyid() {
        return this.applyid;
    }

    public void setApplyid(Integer applyid) {
        this.applyid = applyid;
    }

    public String getUseremail() {
        return this.useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getIntroduce() {
        return this.introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Date getApplydate() {
        return this.applydate;
    }

    public void setApplydate(Date applydate) {
        this.applydate = applydate;
    }

    public String getIpaddress() {
        return this.ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getRegcode() {
        return this.regcode;
    }

    public void setRegcode(String regcode) {
        this.regcode = regcode;
    }

}
