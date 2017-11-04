/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.client;

import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.system.SystemEventType;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:06
 * Description:
 */
public abstract class ClientEvent implements Event {
    //
    private ClientEventType eventType;

    protected ClientEvent(ClientEventType eventType) {
        this.eventType = eventType;
    }

    //
    public ClientEventType getEventType() {
        return eventType;
    }
}
