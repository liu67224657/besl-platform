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
public class ViewLineItem implements Serializable {
    //
    private long itemId;

    //the line id
    private int lineId;
    private int categoryId;

    //the category aspect．
    private ViewCategoryAspect categoryAspect;

    private ViewLineItemDisplayInfo displayInfo;

    //the direct uno and id,
    // such the content id or the reply id.
    private String directUno;
    private String directId;

    //
    private String parentUno;
    private String parentId;

    //the relation uno and id,
    //such the original content id or the content id.
    private String relationUno;
    private String relationId;

    //
    private Date itemCreateDate;

    //一直按照display order asc排序。
    private int displayOrder;

    //元素展示类型。
    private ViewLineItemDisplayType displayType = new ViewLineItemDisplayType();

    //是否有效.
    private ValidStatus validStatus = ValidStatus.VALID;

    //
    private String itemDesc;

    //create info
    private Date createDate;
    private String createUno;

    //
    public ViewLineItem() {
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
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

    public ViewLineItemDisplayInfo getDisplayInfo() {
        return displayInfo;
    }

    public void setDisplayInfo(ViewLineItemDisplayInfo displayInfo) {
        this.displayInfo = displayInfo;
    }

    public String getDirectUno() {
        return directUno;
    }

    public void setDirectUno(String directUno) {
        this.directUno = directUno;
    }

    public String getDirectId() {
        return directId;
    }

    public void setDirectId(String directId) {
        this.directId = directId;
    }

    public String getParentUno() {
        return parentUno;
    }

    public void setParentUno(String parentUno) {
        this.parentUno = parentUno;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRelationUno() {
        return relationUno;
    }

    public void setRelationUno(String relationUno) {
        this.relationUno = relationUno;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public Date getItemCreateDate() {
        return itemCreateDate;
    }

    public void setItemCreateDate(Date itemCreateDate) {
        this.itemCreateDate = itemCreateDate;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public ViewLineItemDisplayType getDisplayType() {
        return displayType;
    }

    public void setDisplayType(ViewLineItemDisplayType displayType) {
        this.displayType = displayType;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public ViewCategoryAspect getCategoryAspect() {
        return categoryAspect;
    }

    public void setCategoryAspect(ViewCategoryAspect categoryAspect) {
        this.categoryAspect = categoryAspect;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
