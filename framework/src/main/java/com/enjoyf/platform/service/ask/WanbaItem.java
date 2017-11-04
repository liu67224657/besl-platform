package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/12
 */
public class WanbaItem implements Serializable {
    private String itemId;//(md5 linekey+destId+type)
    private String lineKey;//(md5 ownProfileId+itemDomain)
    private String ownProfileId;
    private ItemType itemType;
    private String destId;
    private String destProfileId;// extr 扩展字段
    private double score;
    private WanbaItemDomain itemDomain;
    private Date createTime;
    private ValidStatus validStatus = ValidStatus.VALID;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getOwnProfileId() {
        return ownProfileId;
    }

    public void setOwnProfileId(String ownProfileId) {
        this.ownProfileId = ownProfileId;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public WanbaItemDomain getItemDomain() {
        return itemDomain;
    }

    public void setItemDomain(WanbaItemDomain itemDomain) {
        this.itemDomain = itemDomain;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDestProfileId() {
        return destProfileId;
    }

    public void setDestProfileId(String destProfileId) {
        this.destProfileId = destProfileId;
    }

    public String getLineKey() {
        return lineKey;
    }

    public void setLineKey(String lineKey) {
        this.lineKey = lineKey;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
