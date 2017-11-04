/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.timeline.TimeLineDomain;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:15
 * Description:
 */
public class TimeLineRemoveEvent extends SystemEvent {
    //
    private String ownUno;

    //the time line domain.
    private TimeLineDomain domain;

    //the direct id
    private String directId;

    //
    public TimeLineRemoveEvent() {
        super(SystemEventType.TIMELINE_REMOVE);
    }

    public String getOwnUno() {
        return ownUno;
    }

    public void setOwnUno(String ownUno) {
        this.ownUno = ownUno;
    }

    public TimeLineDomain getDomain() {
        return domain;
    }

    public void setDomain(TimeLineDomain domain) {
        this.domain = domain;
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
        return ownUno.hashCode();
    }
}
