package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.timeline.SocialTimeLineDomain;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-5-14
 * Time: 下午4:20
 * To change this template use File | Settings | File Templates.
 */
public class SocialTimeLineItemRemoveEvent extends SystemEvent {
     //
    private Long contentid;

    //
    private String ownUno;

    //the time line domain.
    private SocialTimeLineDomain domain;

    private ActStatus removeStatus;

    public SocialTimeLineItemRemoveEvent() {
        super(SystemEventType.SOCIAL_TIMELINE_RELATION_REMOVE);
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public String getOwnUno() {
        return ownUno;
    }

    public void setOwnUno(String ownUno) {
        this.ownUno = ownUno;
    }

    public SocialTimeLineDomain getDomain() {
        return domain;
    }

    public void setDomain(SocialTimeLineDomain domain) {
        this.domain = domain;
    }

    public Long getContentid() {
        return contentid;
    }

    public void setContentid(Long contentid) {
        this.contentid = contentid;
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
