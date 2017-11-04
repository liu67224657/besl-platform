package com.enjoyf.platform.webapps.common.dto;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.viewline.ViewLineItem;
import com.enjoyf.platform.service.viewline.ViewLineItemDisplayType;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-5-30
 * Time: 上午11:55
 * To change this template use File | Settings | File Templates.
 */
public class ViewLineItemCustomDTO implements ViewLineItemDTO{
    //
    private long lineItemId;

    //the line code
    private int lineId;

    //
    private String itemDesc;

    //
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

    public ViewLineItem getViewLineItem() {
        return viewLineItem;
    }

    public void setViewLineItem(ViewLineItem viewLineItem) {
        this.viewLineItem = viewLineItem;
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

    public ViewLineItemDisplayType getDisplayType() {
        return displayType;
    }

    public void setDisplayType(ViewLineItemDisplayType displayType) {
        this.displayType = displayType;
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
}
