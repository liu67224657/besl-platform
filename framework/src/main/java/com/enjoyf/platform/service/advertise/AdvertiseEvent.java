/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.advertise;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;


/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-6-25 下午5:51
 * Description:
 */
public class AdvertiseEvent implements Serializable {
    //
    private long eventId;

    //
    private AdvertiseEventType eventType;

    //the advertise info.
    private String publishId;
    private String locationCode;

    //the advertise user info.
    private String uno;
    private String sessionId;
    private String globalId;

    //
    private long eventCount;
    private String eventDesc;

    //
    private Date eventDate;
    private String eventIp;

    //
    public AdvertiseEvent() {
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public AdvertiseEventType getEventType() {
        return eventType;
    }

    public void setEventType(AdvertiseEventType eventType) {
        this.eventType = eventType;
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

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getGlobalId() {
        return globalId;
    }

    public void setGlobalId(String globalId) {
        this.globalId = globalId;
    }

    public long getEventCount() {
        return eventCount;
    }

    public void setEventCount(long eventCount) {
        this.eventCount = eventCount;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventIp() {
        return eventIp;
    }

    public void setEventIp(String eventIp) {
        this.eventIp = eventIp;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
