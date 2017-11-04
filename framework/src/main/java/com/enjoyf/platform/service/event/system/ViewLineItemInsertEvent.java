/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.viewline.ViewCategoryAspect;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:15
 * Description:
 */
public class ViewLineItemInsertEvent extends SystemEvent {
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
    public ViewLineItemInsertEvent() {
        super(SystemEventType.VIEWLINE_ITEM_INSERT_EVENT);
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public String getLineLocationCode() {
        return lineLocationCode;
    }

    public void setLineLocationCode(String lineLocationCode) {
        this.lineLocationCode = lineLocationCode;
    }

    public ViewCategoryAspect getViewCategoryAspect() {
        return viewCategoryAspect;
    }

    public void setViewCategoryAspect(ViewCategoryAspect viewCategoryAspect) {
        this.viewCategoryAspect = viewCategoryAspect;
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
