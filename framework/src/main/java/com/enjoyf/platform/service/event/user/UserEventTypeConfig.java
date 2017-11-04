/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.service.event.user;

import com.enjoyf.platform.service.ServiceType;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public class UserEventTypeConfig implements Serializable {
    //
    private UserEventType eventType;

    //
    private Map<ServiceType, EventReceiver> receivers = new HashMap<ServiceType, EventReceiver>();

    //
    private boolean storeToDB = false;


    public UserEventTypeConfig(UserEventType eventType) {
        this.eventType = eventType;
    }

    public UserEventType getEventType() {
        return eventType;
    }

    public Map<ServiceType, EventReceiver> getReceivers() {
        return receivers;
    }

    public boolean isStoreToDB() {
        return storeToDB;
    }

    public void setStoreToDB(boolean storeToDB) {
        this.storeToDB = storeToDB;
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
        if ((obj == null) || !(obj instanceof UserEventTypeConfig)) {
            return false;
        }

        return eventType.equals(((UserEventTypeConfig) obj).getEventType());
    }
}
