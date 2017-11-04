package com.enjoyf.platform.service.social;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/10
 * Description:
 */
public class ProfileRelation implements Serializable {
    private String relationId;
    private String srcProfileId;
    private String destProfileId;
    private ObjectRelationType type;
    private IntValidStatus srcStatus = IntValidStatus.UNVALID;
    private IntValidStatus destStatus = IntValidStatus.UNVALID;

    private Date modifyTime;
    private String modifyIp;

    private String extString;
    private String profileKey;

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getSrcProfileId() {
        return srcProfileId;
    }

    public void setSrcProfileId(String srcProfileId) {
        this.srcProfileId = srcProfileId;
    }

    public String getDestProfileId() {
        return destProfileId;
    }

    public void setDestProfileId(String destProfileId) {
        this.destProfileId = destProfileId;
    }

    public IntValidStatus getSrcStatus() {
        return srcStatus;
    }

    public void setSrcStatus(IntValidStatus srcStatus) {
        this.srcStatus = srcStatus;
    }

    public IntValidStatus getDestStatus() {
        return destStatus;
    }

    public void setDestStatus(IntValidStatus destStatus) {
        this.destStatus = destStatus;
    }

    public ObjectRelationType getType() {
        return type;
    }

    public void setType(ObjectRelationType type) {
        this.type = type;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyIp() {
        return modifyIp;
    }

    public void setModifyIp(String modifyIp) {
        this.modifyIp = modifyIp;
    }

    public String getExtString() {
        return extString;
    }

    public void setExtString(String extString) {
        this.extString = extString;
    }

    public String getProfileKey() {
        return profileKey;
    }

    public void setProfileKey(String profileKey) {
        this.profileKey = profileKey;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
