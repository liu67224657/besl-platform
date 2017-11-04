package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.service.ActStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/19
 * Description:
 */
public class UidProfileIdx implements Serializable {
    private long uid;
    private String uno;
    private Date createTime;
    private ActStatus status = ActStatus.UNACT;
    private String profileId;
    private String profileKey;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public ActStatus getStatus() {
        return status;
    }

    public void setStatus(ActStatus status) {
        this.status = status;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getProfileKey() {
        return profileKey;
    }

    public void setProfileKey(String profileKey) {
        this.profileKey = profileKey;
    }
}
