package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-10 下午6:07
 * Description:
 */
public class SocialProfileRecommend implements Serializable {
    private long recommendId;
    private String uno;
    private int display_order;
    private Date createTime;
    private ActStatus actStatus;

    private String createUserId;
    private Date modifyDate;
    private String modifyUserId;
    private SocialProfileRecommendType recommendType;

    public long getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(long recommendId) {
        this.recommendId = recommendId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public int getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(int display_order) {
        this.display_order = display_order;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public ActStatus getActStatus() {
        return actStatus;
    }

    public void setActStatus(ActStatus actStatus) {
        this.actStatus = actStatus;
    }

    public SocialProfileRecommendType getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(SocialProfileRecommendType recommendType) {
        this.recommendType = recommendType;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
