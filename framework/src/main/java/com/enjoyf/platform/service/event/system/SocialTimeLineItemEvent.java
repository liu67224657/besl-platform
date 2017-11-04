package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.timeline.SocialTimeLineDomain;
import com.enjoyf.platform.service.timeline.SocialTimeLineItemType;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import net.sf.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-15
 * Time: 下午1:13
 * To change this template use File | Settings | File Templates.
 */
public class SocialTimeLineItemEvent extends SystemEvent {
    private String profileId;
    private String desProfileId;
    private SocialTimeLineDomain domain;
    private String otherId;
    private String desId;
    private SocialTimeLineItemType itemType;
    private ActStatus removeStatus = ActStatus.UNACT;
    private String expStr;

    public SocialTimeLineItemEvent() {
        super(SystemEventType.SOCIAL_TIMELINE_RELATION_CREATE);
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getDesProfileId() {
        return desProfileId;
    }

    public void setDesProfileId(String desProfileId) {
        this.desProfileId = desProfileId;
    }

    public SocialTimeLineDomain getDomain() {
        return domain;
    }

    public void setDomain(SocialTimeLineDomain domain) {
        this.domain = domain;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public String getDesId() {
        return desId;
    }

    public void setDesId(String desId) {
        this.desId = desId;
    }

    public SocialTimeLineItemType getItemType() {
        return itemType;
    }

    public void setItemType(SocialTimeLineItemType itemType) {
        this.itemType = itemType;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public String getExpStr() {
        return expStr;
    }

    public void setExpStr(String expStr) {
        this.expStr = expStr;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = JSONObject.fromObject(this);
        return jsonObject.toString();
    }

    @Override
    public int hashCode() {
        return profileId.hashCode();
    }
}
