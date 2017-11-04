/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.user;

import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.util.MetaPairs;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:16
 * Description:
 */
public class UserEvent implements Event {
    //the source user no.
    private String srcUno;

    //the dest user no.
    private String destUno;

    //
    private UserEventType eventType;

    //the description
    private String description;

    //the contents
    private MetaPairs meta;

    //the event count num.
    private Long count;

    //
    private Date eventDate;
    private String eventIp;

    //
    public UserEvent() {

    }

    public UserEvent(String srcUno) {
        this.srcUno = srcUno;
    }

    public void setSrcUno(String srcUno) {
        this.srcUno = srcUno;
    }

    public String getSrcUno() {
        return srcUno;
    }

    public String getDestUno() {
        return destUno;
    }

    public void setDestUno(String destUno) {
        this.destUno = destUno;
    }

    public UserEventType getEventType() {
        return eventType;
    }

    public void setEventType(UserEventType eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MetaPairs getMeta() {
        return meta;
    }

    public void setMeta(MetaPairs meta) {
        this.meta = meta;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void setCount(Integer count) {
        this.count = count.longValue();
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

    @Override
    public int hashCode() {
        return srcUno.hashCode();
    }
}
