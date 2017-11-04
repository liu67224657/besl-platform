/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.webapps.common.dto;


import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.viewline.ViewLineItem;
import com.enjoyf.platform.service.viewline.ViewLineItemDisplayType;

import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-3-8 下午1:14
 * Description:
 */
public class ViewLineItemContentDTO implements ViewLineItemDTO {
    //
    private long lineItemId;

    //the line code
    private int lineId;

    //
    private String itemDesc;

    //
    private Content content;

    private ProfileBlog profile;

    private ViewLineItem viewLineItem;

    //一直按照display order asc排序。
    private int displayOrder;

    //是否有效.
    private ValidStatus validStatus = ValidStatus.INVALID;

    //置顶
    private ViewLineItemDisplayType displayType = new ViewLineItemDisplayType();

    //create info
    private Date createDate;
    private String createUno;

    public long getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(long lineItemId) {
        this.lineItemId = lineItemId;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public ProfileBlog getProfile() {
        return profile;
    }

    public void setProfile(ProfileBlog profile) {
        this.profile = profile;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUno() {
        return createUno;
    }

    public void setCreateUno(String createUno) {
        this.createUno = createUno;
    }

    public ViewLineItemDisplayType getDisplayType() {
        return displayType;
    }

    public void setDisplayType(ViewLineItemDisplayType displayType) {
        this.displayType = displayType;
    }

    public ViewLineItem getViewLineItem() {
        return viewLineItem;
    }

    public void setViewLineItem(ViewLineItem viewLineItem) {
        this.viewLineItem = viewLineItem;
    }
}
