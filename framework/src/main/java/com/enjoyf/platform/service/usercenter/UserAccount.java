package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/10/28
 * Description:
 */
public class UserAccount implements Serializable {
    private String uno;
    private Address address;
    private Date createTime;
    private String createIp;


    //    private String mobile;
    private AccountFlag accountFlag;

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

//    public String getMobile() {
//        return mobile;
//    }
//
//    public void setMobile(String mobile) {
//        this.mobile = mobile;
//    }

    public AccountFlag getAccountFlag() {
        return accountFlag;
    }

    public void setAccountFlag(AccountFlag accountFlag) {
        this.accountFlag = accountFlag;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
