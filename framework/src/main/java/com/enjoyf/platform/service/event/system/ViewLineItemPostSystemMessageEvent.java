/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.viewline.ViewCategoryAspect;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:15
 * Description:
 */
public class ViewLineItemPostSystemMessageEvent extends SystemEvent {
    //
    private int categoryId;
    private int lineId;
    private String lineLocationCode;
    private ViewCategoryAspect viewCategoryAspect;

    //the direct uno and id,
    // such the content id or the reply id.
    private String directUno;
    private String directId;

    //
    private String parentUno;
    private String parentId;

    //the relation uno and id,
    // such the original content id or the content id.
    private String relationUno;
    private String relationId;

    private Date itemCreateDate;
    private boolean delete = false;

    private String createUno;

    //
    public ViewLineItemPostSystemMessageEvent() {
        super(SystemEventType.VIEWLINE_POST_SYSTEM_MESSAGE_EVENT);
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getLineLocationCode() {
        return lineLocationCode;
    }

    public void setLineLocationCode(String lineLocationCode) {
        this.lineLocationCode = lineLocationCode;
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

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public ViewCategoryAspect getViewCategoryAspect() {
        return viewCategoryAspect;
    }

    public void setViewCategoryAspect(ViewCategoryAspect viewCategoryAspect) {
        this.viewCategoryAspect = viewCategoryAspect;
    }

    public String getCreateUno() {
        return createUno;
    }

    public void setCreateUno(String createUno) {
        this.createUno = createUno;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return categoryId;
    }
}
