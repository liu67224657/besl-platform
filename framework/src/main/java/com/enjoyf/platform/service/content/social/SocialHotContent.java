package com.enjoyf.platform.service.content.social;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-10 下午6:07
 * Description:
 */
public class SocialHotContent implements Serializable {
    private long contentId;
    private String uno;
    private int displayOrder;

    private Date createDate;
    private String createIp;

    private Date modifyDate;
    private String modifyIp;

    private ActStatus removeStatus = ActStatus.UNACT;

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }



    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyIp() {
        return modifyIp;
    }

    public void setModifyIp(String modifyIp) {
        this.modifyIp = modifyIp;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
