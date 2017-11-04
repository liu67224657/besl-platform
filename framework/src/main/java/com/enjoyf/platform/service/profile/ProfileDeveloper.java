package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-12-20
 * Time: 上午11:09
 * To change this template use File | Settings | File Templates.
 */
public class ProfileDeveloper implements Serializable{

    private String uno;

    private String verifyDesc;    //认证说明
    /*联系人*/
    private String contacts;      //姓名

    private String email;      //邮箱

    private String qq;       //qq

    private String phone;    //电话
    /*公司*/
    private String company;     //公司

    private String licensePic;      //执照

    private DeveloperLocation location;    //地址

    /**/
    private DeveloperCategory category;  //类别

    private VerifyStatus verifyStatus = VerifyStatus.AUDIT;   //默认 审核中

    private Date createDate;

    private String createIp;

    private Date modifyDate;

    private String modifyIp;

    private Date verifyDate;

    private String verifyIp;

    private String verifyReason;   //认证 理由 & 原因

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getVerifyDesc() {
        return verifyDesc;
    }

    public void setVerifyDesc(String verifyDesc) {
        this.verifyDesc = verifyDesc;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLicensePic() {
        return licensePic;
    }

    public void setLicensePic(String licensePic) {
        this.licensePic = licensePic;
    }

    public DeveloperLocation getLocation() {
        return location;
    }

    public void setLocation(DeveloperLocation location) {
        this.location = location;
    }

    public DeveloperCategory getCategory() {
        return category;
    }

    public void setCategory(DeveloperCategory category) {
        this.category = category;
    }

    public VerifyStatus getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(VerifyStatus verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyIp() {
        return modifyIp;
    }

    public void setModifyIp(String modifyIp) {
        this.modifyIp = modifyIp;
    }

    public Date getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate) {
        this.verifyDate = verifyDate;
    }

    public String getVerifyIp() {
        return verifyIp;
    }

    public void setVerifyIp(String verifyIp) {
        this.verifyIp = verifyIp;
    }

    public String getVerifyReason() {
        return verifyReason;
    }

    public void setVerifyReason(String verifyReason) {
        this.verifyReason = verifyReason;
    }

    @Override
    public int hashCode() {
        return uno.hashCode();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
