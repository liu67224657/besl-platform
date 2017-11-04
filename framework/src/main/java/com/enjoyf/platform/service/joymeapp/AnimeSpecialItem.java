package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="pengxu@staff.joyme.com">PengXu</a>
 * Create time:  14-10-27 下午5:51
 * Description:
 */
public class AnimeSpecialItem implements Serializable {
    private Long specialItemId;
    private Long specialId;
    private Long tvId;
    private String linkUrl;
    private String title;
    private String desc;
    private String pic;
    private int displayOrder;
    private Date updateTime;
    private String updateUser;
    private ValidStatus removeStatus;
    private int replyNum;

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public Long getSpecialItemId() {
        return specialItemId;
    }

    public void setSpecialItemId(Long specialItemId) {
        this.specialItemId = specialItemId;
    }

    public Long getSpecialId() {
        return specialId;
    }

    public void setSpecialId(Long specialId) {
        this.specialId = specialId;
    }

    public Long getTvId() {
        return tvId;
    }

    public void setTvId(Long tvId) {
        this.tvId = tvId;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public ValidStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ValidStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
