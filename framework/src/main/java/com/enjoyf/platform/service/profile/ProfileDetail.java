/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午4:29
 * Description:
 */
public class ProfileDetail implements Serializable {
    //
    private String uno;

    //
    private String realName;

    private String sex;
    private String birthday;

    private Integer provinceId = 0;
    private Integer cityId = 0;

    private String qq;
    private String msn;

    private Set<String> interest = new HashSet<String>();

    private ActStatus completeStatus = ActStatus.UNACT;

    //+V认证
    private VerifyType verifyType = VerifyType.N_VERIFY;
    private String verifyDesc;

    private Date createDate;//init日期
    private Date updateDate;//更新日期

    //
    public ProfileDetail() {
    }

    public ProfileDetail(String uno) {
        this.uno = uno;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
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

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
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

    public String getMsn() {
        return msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    public String interestToString() {
        return CollectionUtil.join(interest, ",");
    }

    public Set<String> getInterest() {
        return interest;
    }

    public void addInterest(String s) {
        if (Strings.isNullOrEmpty(s)) {
            return;
        }

        StringTokenizer tokenizer = new StringTokenizer(s, ",");

        while (tokenizer.hasMoreTokens()) {
            interest.add(tokenizer.nextToken());
        }
    }

    public void setInterest(Set<String> interest) {
        this.interest = interest;
    }

    public ActStatus getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(ActStatus completeStatus) {
        this.completeStatus = completeStatus;
    }

    public VerifyType getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(VerifyType verifyType) {
        this.verifyType = verifyType;
    }

    public String getVerifyDesc() {
        return verifyDesc;
    }

    public void setVerifyDesc(String verifyDesc) {
        this.verifyDesc = verifyDesc;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    //
    @Override
    public int hashCode() {
        return uno.hashCode();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
