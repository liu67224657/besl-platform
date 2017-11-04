/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.viewline;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-2-15 下午1:13
 * Description: view category
 */
public class ViewCategory implements Serializable {
    //squence．
    private int categoryId;

    //the category aspect．
    private ViewCategoryAspect categoryAspect;

    //在同一树(aspect)下，唯一key，同时也是为了SEO的url显示。
    private String categoryCode;
    private String locationCode;
    //
    private String categoryName;
    private String categoryDesc;

    //SEO相关数据。
    private String seoKeyWord;
    private String seoDesc;

    //上级或根节点。
    private int parentCategoryId;

    //the following 3 not store in db.
    private ViewCategory parentCategory;

    private int displayLevel = 0;
    private List<ViewCategory> childrenCategories;

    //模板对象．
    private ViewCategoryDisplaySetting displaySetting;

    //排序,在同一上级下排序。
    private int displayOrder;

    //是否有效．
    private ValidStatus validStatus = ValidStatus.INVALID;

    //是否发布。
    private ActStatus publishStatus = ActStatus.UNACT;

    //create info．
    private Date createDate;
    private String createUserid;

    //last update info．
    private Date updateDate;
    private String updateUserid;

    //
    public ViewCategory() {
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

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public String getSeoKeyWord() {
        return seoKeyWord;
    }

    public void setSeoKeyWord(String seoKeyWord) {
        this.seoKeyWord = seoKeyWord;
    }

    public String getSeoDesc() {
        return seoDesc;
    }

    public void setSeoDesc(String seoDesc) {
        this.seoDesc = seoDesc;
    }

    public int getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(int parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public ViewCategory getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(ViewCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    public int getDisplayLevel() {
        return displayLevel;
    }

    public void setDisplayLevel(int displayLevel) {
        this.displayLevel = displayLevel;
    }

    public List<ViewCategory> getChildrenCategories() {
        return childrenCategories;
    }

    public void setChildrenCategories(List<ViewCategory> childrenCategories) {
        this.childrenCategories = childrenCategories;
    }

    public ViewCategoryDisplaySetting getDisplaySetting() {
        return displaySetting;
    }

    public void setDisplaySetting(ViewCategoryDisplaySetting displaySetting) {
        this.displaySetting = displaySetting;
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

    public ActStatus getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(ActStatus publishStatus) {
        this.publishStatus = publishStatus;
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

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    @Override
    public String toString() {
        return "ViewCategory{" +
                "categoryId=" + categoryId +
                ", categoryAspect=" + categoryAspect +
                ", categoryCode='" + categoryCode + '\'' +
                ", locationCode='" + locationCode + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", categoryDesc='" + categoryDesc + '\'' +
                ", seoKeyWord='" + seoKeyWord + '\'' +
                ", seoDesc='" + seoDesc + '\'' +
                ", displayLevel=" + displayLevel +
                ", displaySetting=" + displaySetting +
                ", displayOrder=" + displayOrder +
                ", validStatus=" + validStatus +
                ", publishStatus=" + publishStatus +
                ", createDate=" + createDate +
                ", createUserid='" + createUserid + '\'' +
                ", updateDate=" + updateDate +
                ", updateUserid='" + updateUserid + '\'' +
                ", parentCategoryId=" + parentCategoryId +
                '}';
    }
}
