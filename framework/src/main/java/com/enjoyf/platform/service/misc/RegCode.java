/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.service.UseStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-5 下午3:05
 * Description:
 */
public class RegCode implements Serializable {
    //
    private String regCode;

    private UseStatus useStatus = UseStatus.INIT;

    private String applyEmail;
    private String applyReason;
    private Date applyDate;
    private Date confirmDate;

    private String useUno;
    private String useUserid;
    private Date useDate;

    public RegCode() {
    }

    public String getRegCode() {
        return regCode;
    }

    public void setRegCode(String regCode) {
        this.regCode = regCode;
    }

    public UseStatus getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(UseStatus useStatus) {
        this.useStatus = useStatus;
    }

    public String getApplyEmail() {
        return applyEmail;
    }

    public void setApplyEmail(String applyEmail) {
        this.applyEmail = applyEmail;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getUseUno() {
        return useUno;
    }

    public void setUseUno(String useUno) {
        this.useUno = useUno;
    }

    public String getUseUserid() {
        return useUserid;
    }

    public void setUseUserid(String useUserid) {
        this.useUserid = useUserid;
    }

    public Date getUseDate() {
        return useDate;
    }

    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
