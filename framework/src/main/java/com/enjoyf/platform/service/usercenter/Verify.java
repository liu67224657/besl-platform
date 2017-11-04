package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/13
 */
public class Verify implements Serializable {
    private Long verifyId;
    private String verifyName;
    private ValidStatus validStatus;
    private Date createDate;

    public Long getVerifyId() {
        return verifyId;
    }

    public void setVerifyId(Long verifyId) {
        this.verifyId = verifyId;
    }

    public String getVerifyName() {
        return verifyName;
    }

    public void setVerifyName(String verifyName) {
        this.verifyName = verifyName;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public static Verify getByJson(String json) {
        return new Gson().fromJson(json, Verify.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
