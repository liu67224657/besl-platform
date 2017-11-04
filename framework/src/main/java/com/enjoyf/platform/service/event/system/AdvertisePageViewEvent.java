/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.event.pageview.PageViewEvent;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:15
 * Description:
 */
public class AdvertisePageViewEvent extends SystemEvent {
    //
    private String publishId;
    private String locationCode;

    //
    private PageViewEvent pageViewEvent;

    //
    public AdvertisePageViewEvent() {
        super(SystemEventType.ADV_PAGE_VIEW);
    }

    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public PageViewEvent getPageViewEvent() {
        return pageViewEvent;
    }

    public void setPageViewEvent(PageViewEvent pageViewEvent) {
        this.pageViewEvent = pageViewEvent;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return publishId.hashCode();
    }
}
