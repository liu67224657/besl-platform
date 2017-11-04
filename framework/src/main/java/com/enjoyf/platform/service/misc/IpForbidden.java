package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-4-6
 * Time: 上午10:57
 * To change this template use File | Settings | File Templates.
 */
public class IpForbidden implements Serializable {
    private long ipId;

    private long startIP;
    private long endIp;
    private Date createDate;
    private String createUserid;
    private Date utillDate;
    private ValidStatus status;

    private String description;

    public long getIpId() {
        return ipId;
    }

    public void setIpId(long ipId) {
        this.ipId = ipId;
    }

    public long getStartIP() {
        return startIP;
    }

    public void setStartIP(long startIP) {
        this.startIP = startIP;
    }

    public long getEndIp() {
        return endIp;
    }

    public void setEndIp(long endIp) {
        this.endIp = endIp;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid;
    }

    public Date getUtillDate() {
        return utillDate;
    }

    public void setUtillDate(Date utillDate) {
        this.utillDate = utillDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ValidStatus getStatus() {
        return status;
    }

    public void setStatus(ValidStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IpForbidden that = (IpForbidden) o;


        return ipId == that.ipId;

    }

    @Override
    public int hashCode() {
        return (int) (ipId ^ (ipId >>> 32));
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
