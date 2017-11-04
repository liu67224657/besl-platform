package com.enjoyf.platform.service.social;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import net.sf.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/10
 * Description:
 */
public class ObjectRelation implements Serializable {
    private String relationId;
    private String profileId;
    private String objectId;
    private ObjectRelationType type;

    private Date modifyTime = new Date();
    private String modifyIp;
    private IntValidStatus status = IntValidStatus.VALID;
    private String profileKey;
    private String extString;

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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

    public IntValidStatus getStatus() {
        return status;
    }

    public void setStatus(IntValidStatus status) {
        this.status = status;
    }

    public String getProfileKey() {
        return profileKey;
    }

    public void setProfileKey(String profileKey) {
        this.profileKey = profileKey;
    }

    public String getExtString() {
        return extString;
    }

    public void setExtString(String extString) {
        this.extString = extString;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public String toJson() {
        JSONObject jsonObject = JSONObject.fromObject(this);
        return jsonObject.toString();
    }

    public static ObjectRelation parse(Object obj){
        if(obj == null){
            return  null;
        }
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(jsonObject == null || jsonObject.isNullObject()){
            return null;
        }
        ObjectRelation objectRelation = new ObjectRelation();
        objectRelation.setRelationId(jsonObject.containsKey("relationId") ? jsonObject.getString("relationId") : "");
        objectRelation.setProfileId(jsonObject.containsKey("profileId") ? jsonObject.getString("profileId") : "");
        objectRelation.setObjectId(jsonObject.containsKey("objectId") ? jsonObject.getString("objectId") : "");
        ObjectRelationType type = null;
        if(jsonObject.containsKey("type")){
            JSONObject typeObj = jsonObject.getJSONObject("type");
            if(typeObj.containsKey("code")){
                type = ObjectRelationType.getByCode(typeObj.getInt("code"));
            }
        }
        objectRelation.setType(type);
        objectRelation.setModifyTime(jsonObject.containsKey("modifyTime") ? (jsonObject.getJSONObject("modifyTime").containsKey("time") ? new Date(jsonObject.getJSONObject("modifyTime").getLong("time")) : null) : null);
        objectRelation.setModifyIp(jsonObject.containsKey("modifyIp") ? jsonObject.getString("modifyIp") : "");
        IntValidStatus status = IntValidStatus.VALID;
        if(jsonObject.containsKey("status")){
            JSONObject statusObj = jsonObject.getJSONObject("status");
            if(statusObj.containsKey("code")){
                status = IntValidStatus.getByCode(statusObj.getInt("code"));
            }
        }
        objectRelation.setStatus(status);
        objectRelation.setProfileKey(jsonObject.containsKey("profileKey") ? jsonObject.getString("profileKey") : "");
        objectRelation.setExtString(jsonObject.containsKey("extString") ? jsonObject.getString("extString") : "");
        return objectRelation;
    }
}
