/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.event.Event;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:06
 * Description:
 */
public abstract class SystemEvent implements Event {
    //
    private SystemEventType eventType;

    protected SystemEvent(SystemEventType eventType) {
        this.eventType = eventType;
    }

    //
    public SystemEventType getEventType() {
        return eventType;
    }
}
