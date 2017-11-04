/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.service.event.client;

import com.enjoyf.platform.service.ServiceType;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public class ClientEventTypeConfig implements Serializable {
    //
    private ClientEventType eventType;

    //
    private Map<ServiceType, EventReceiver> receivers = new HashMap<ServiceType, EventReceiver>();


    public ClientEventTypeConfig(ClientEventType eventType) {
        this.eventType = eventType;
    }

    public ClientEventType getEventType() {
        return eventType;
    }

    public Map<ServiceType, EventReceiver> getReceivers() {
        return receivers;
    }

    @Override
    public int hashCode() {
        return eventType.hashCode();
    }

    // ///////////////////////////////////////////////////
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ClientEventTypeConfig)) {
            return false;
        }

        return eventType.equals(((ClientEventTypeConfig) obj).getEventType());
    }
}
