/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.pageview;

import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.util.MetaPairs;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-6 下午1:47
 * Description:
 */
public class PageViewEvent implements Event {
    //the sequence id of the page view.
    private Long eventId;

    //the pageview locaiton and location id.
    private String locationUrl;
    private String locationId;

    //the user unfo, the session id or the uno.
    private String sessionId;
    private String globalId;
    private String uno;

    //the refer info
    private String refer;
    private String referId;
    private String subReferId;

    //the client datas
    private String os;
    private String screen;
    private String explorerType;
    private String explorerVersion;
    private MetaPairs meta = new MetaPairs();

    //the event date and ip;
    private Date eventDate;
    private String eventIp;

    // adv
    private String publishId;
    private String locationCode;

    //
    public PageViewEvent() {
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getLocationUrl() {
        return locationUrl;
    }

    public void setLocationUrl(String locationUrl) {
        this.locationUrl = locationUrl;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
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

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public String getReferId() {
        return referId;
    }

    public void setReferId(String referId) {
        this.referId = referId;
    }

    public String getSubReferId() {
        return subReferId;
    }

    public void setSubReferId(String subReferId) {
        this.subReferId = subReferId;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getExplorerType() {
        return explorerType;
    }

    public void setExplorerType(String explorerType) {
        this.explorerType = explorerType;
    }

    public String getExplorerVersion() {
        return explorerVersion;
    }

    public void setExplorerVersion(String explorerVersion) {
        this.explorerVersion = explorerVersion;
    }

    public MetaPairs getMeta() {
        return meta;
    }

    public void setMeta(MetaPairs meta) {
        this.meta = meta;
    }

    public String getEventIp() {
        return eventIp;
    }

    public void setEventIp(String eventIp) {
        this.eventIp = eventIp;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
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

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return eventId == null ? 0 : eventId.hashCode();
    }
}
