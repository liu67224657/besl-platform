package com.enjoyf.platform.service.event.user;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
@SuppressWarnings("serial")
public class UserEventEntry extends UserEvent {
    //the sequence id.
    private Long eventId;

    //
    public UserEventEntry() {
    }

    public UserEventEntry(String srcUno) {
        super(srcUno);
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    /////////////////////////////////////////////////////
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
