/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.viewline;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-2-15 下午1:13
 * Description:
 */
public class ViewLine implements Serializable {
    //sequence.
    private int lineId;

    //
    private String lineName;
    private String lineDesc;

    //对应的分类。
    private int categoryId;

    //the category aspect．
    private ViewCategoryAspect categoryAspect;

    //在什么位置放多少条什么内容？
    //在同一个category下，linelocation只能有一个。
    private String locationCode;
    private ViewItemType itemType = ViewItemType.CONTENT;
    private int itemMinCount;

    //一直按照display order asc排序。
    private int displayOrder;

    //填充方式
    private ViewLineAutoFillType autoFillType = ViewLineAutoFillType.AUTO_NONE;
    private ViewLineAutoFillRule autoFillRule;

    //create info
    private Date createDate;
    private String createUserid;

    //last update info
    private Date updateDate;
    private String updateUserid;

    //状态位
    private ValidStatus validStatus = ValidStatus.INVALID;

    //
    public ViewLine() {
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineDesc() {
        return lineDesc;
    }

    public void setLineDesc(String lineDesc) {
        this.lineDesc = lineDesc;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public ViewItemType getItemType() {
        return itemType;
    }

    public void setItemType(ViewItemType itemType) {
        this.itemType = itemType;
    }

    public int getItemMinCount() {
        return itemMinCount;
    }

    public void setItemMinCount(int itemMinCount) {
        this.itemMinCount = itemMinCount;
    }

    public ViewLineAutoFillType getAutoFillType() {
        return autoFillType;
    }

    public void setAutoFillType(ViewLineAutoFillType autoFillType) {
        this.autoFillType = autoFillType;
    }

    public ViewLineAutoFillRule getAutoFillRule() {
        return autoFillRule;
    }

    public void setAutoFillRule(ViewLineAutoFillRule autoFillRule) {
        this.autoFillRule = autoFillRule;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUserid() {
        return updateUserid;
    }

    public void setUpdateUserid(String updateUserid) {
        this.updateUserid = updateUserid;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public ViewCategoryAspect getCategoryAspect() {
        return categoryAspect;
    }

    public void setCategoryAspect(ViewCategoryAspect categoryAspect) {
        this.categoryAspect = categoryAspect;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
