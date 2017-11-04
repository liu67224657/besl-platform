package com.enjoyf.platform.service.ask;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/12
 */
public class AskUserAction implements Serializable {
    private String askUserActionId;//md5 (profileId+destId+askType+actionType)
    private String profileId;
    private ItemType itemType;
    private long destId;
    private AskUserActionType actionType;
    private Date createTime;
    private String value;//如果是 邀请,value代表邀请人的profileId

    public String getAskUserActionId() {
        return askUserActionId;
    }

    public void setAskUserActionId(String askUserActionId) {
        this.askUserActionId = askUserActionId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public long getDestId() {
        return destId;
    }

    public void setDestId(long destId) {
        this.destId = destId;
    }

    public AskUserActionType getActionType() {
        return actionType;
    }

    public void setActionType(AskUserActionType actionType) {
        this.actionType = actionType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
