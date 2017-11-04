package com.enjoyf.platform.service.timeline;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import net.sf.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-14
 * Time: 下午1:57
 * SocialTimeLineItem
 * social_timeline_home
 * social_timeline_blog
 */
public class SocialTimeLineItem implements Serializable {
    private Long sid;

    private String profileId;

    private SocialTimeLineDomain domain;

    private String directId;//

    private String directProfileId;//

    private String otherId;//

    private SocialTimeLineItemType itemType;

    private Date createTime;//创建时间

    private ActStatus removeStatus = ActStatus.UNACT;//删除标志位

    private Date removeTime;//删除时间

    private String expStr;

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public SocialTimeLineDomain getDomain() {
        return domain;
    }

    public void setDomain(SocialTimeLineDomain domain) {
        this.domain = domain;
    }

    public String getDirectId() {
        return directId;
    }

    public void setDirectId(String directId) {
        this.directId = directId;
    }

    public String getDirectProfileId() {
        return directProfileId;
    }

    public void setDirectProfileId(String directProfileId) {
        this.directProfileId = directProfileId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public Date getRemoveTime() {
        return removeTime;
    }

    public void setRemoveTime(Date removeTime) {
        this.removeTime = removeTime;
    }

    public SocialTimeLineItemType getItemType() {
        return itemType;
    }

    public void setItemType(SocialTimeLineItemType itemType) {
        this.itemType = itemType;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public String getExpStr() {
        return expStr;
    }

    public void setExpStr(String expStr) {
        this.expStr = expStr;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public String toJson() {
        JSONObject jsonObject = JSONObject.fromObject(this);
        return jsonObject.toString();
    }

    public static SocialTimeLineItem parse(String objStr) {
        if (objStr == null) {
            return null;
        }
        JSONObject jsonObject = JSONObject.fromObject(objStr);
        if (jsonObject == null || jsonObject.isNullObject()) {
            return null;
        }
        SocialTimeLineItem socialTimeLineItem = new SocialTimeLineItem();
        socialTimeLineItem.setSid(jsonObject.containsKey("sid") ? jsonObject.getLong("sid") : 0l);
        socialTimeLineItem.setProfileId(jsonObject.containsKey("profileId") ? jsonObject.getString("profileId") : "");
        SocialTimeLineDomain domain = null;
        if (jsonObject.containsKey("domain")) {
            JSONObject domainObj = jsonObject.getJSONObject("domain");
            if (domainObj != null && domainObj.isNullObject()) {
                domain = domainObj.containsKey("code") ? SocialTimeLineDomain.getByCode(domainObj.getString("code")) : null;
            }
        }
        socialTimeLineItem.setDomain(domain);
        socialTimeLineItem.setDirectId(jsonObject.containsKey("directId") ? jsonObject.getString("directId") : "");
        socialTimeLineItem.setDirectProfileId(jsonObject.containsKey("directProfileId") ? jsonObject.getString("directProfileId") : "");
        socialTimeLineItem.setOtherId(jsonObject.containsKey("otherId") ? jsonObject.getString("otherId") : "");
        SocialTimeLineItemType itemType = null;
        if (jsonObject.containsKey("itemType")) {
            JSONObject typeObj = jsonObject.getJSONObject("itemType");
            if (typeObj != null && typeObj.isNullObject()) {
                itemType = typeObj.containsKey("code") ? SocialTimeLineItemType.getByCode(typeObj.getInt("code")) : null;
            }
        }
        socialTimeLineItem.setItemType(itemType);
        socialTimeLineItem.setCreateTime(jsonObject.containsKey("createTime") ? (jsonObject.getJSONObject("createTime").containsKey("time") ? new Date(jsonObject.getJSONObject("createTime").getLong("time")) : null) : null);
        ActStatus removeStatus = ActStatus.UNACT;
        if (jsonObject.containsKey("removeStatus")) {
            JSONObject statusObj = jsonObject.getJSONObject("removeStatus");
            if (statusObj != null && statusObj.isNullObject()) {
                removeStatus = statusObj.containsKey("code") ? ActStatus.getByCode(statusObj.getString("code")) : null;
            }
        }
        socialTimeLineItem.setRemoveStatus(removeStatus);
        socialTimeLineItem.setCreateTime(jsonObject.containsKey("removeTime") ? (jsonObject.getJSONObject("removeTime").containsKey("time") ? new Date(jsonObject.getJSONObject("removeTime").getLong("time")) : null) : null);
        socialTimeLineItem.setExpStr(jsonObject.containsKey("expStr") ? jsonObject.getString("expStr") : "");
        return socialTimeLineItem;
    }
}
