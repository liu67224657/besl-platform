package com.enjoyf.platform.service.social;

import com.enjoyf.platform.service.RelationStatus;
import com.enjoyf.platform.service.usercenter.Verify;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.gson.Gson;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserRelation implements Serializable {
    private String relationId;
    private String srcProfileid;
    private String destProfileid;
    private RelationStatus srcStatus;
    private RelationStatus destStatus;
    private String profilekey;
    private ObjectRelationType relationType;
    private String extstring;
    private String modifyIp;
    private Timestamp modifyTime;

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getSrcProfileid() {
        return srcProfileid;
    }

    public void setSrcProfileid(String srcProfileid) {
        this.srcProfileid = srcProfileid;
    }

    public String getDestProfileid() {
        return destProfileid;
    }

    public void setDestProfileid(String destProfileid) {
        this.destProfileid = destProfileid;
    }

    public RelationStatus getSrcStatus() {
        return srcStatus;
    }

    public void setSrcStatus(RelationStatus srcStatus) {
        this.srcStatus = srcStatus;
    }

    public RelationStatus getDestStatus() {
        return destStatus;
    }

    public void setDestStatus(RelationStatus destStatus) {
        this.destStatus = destStatus;
    }

    public String getProfilekey() {
        return profilekey;
    }

    public void setProfilekey(String profilekey) {
        this.profilekey = profilekey;
    }

    public ObjectRelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(ObjectRelationType relationType) {
        this.relationType = relationType;
    }

    public String getExtstring() {
        return extstring;
    }

    public void setExtstring(String extstring) {
        this.extstring = extstring;
    }

    public String getModifyIp() {
        return modifyIp;
    }

    public void setModifyIp(String modifyIp) {
        this.modifyIp = modifyIp;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public static UserRelation getByJson(String json) {
        return new Gson().fromJson(json, UserRelation.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}


