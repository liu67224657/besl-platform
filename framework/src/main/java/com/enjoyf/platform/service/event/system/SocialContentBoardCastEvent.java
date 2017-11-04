package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.timeline.SocialTimeLineDomain;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-14
 * Time: 下午9:22
 * To change this template use File | Settings | File Templates.
 */
public class SocialContentBoardCastEvent extends SystemEvent {

    private String ownUno;
    private SocialTimeLineDomain socialTimeLineDomain;
    private String contentId;

    public String getOwnUno() {
        return ownUno;
    }

    public void setOwnUno(String ownUno) {
        this.ownUno = ownUno;
    }

    public SocialTimeLineDomain getSocialTimeLineDomain() {
        return socialTimeLineDomain;
    }

    public void setSocialTimeLineDomain(SocialTimeLineDomain socialTimeLineDomain) {
        this.socialTimeLineDomain = socialTimeLineDomain;
    }

    public SocialContentBoardCastEvent() {
          super(SystemEventType.SOCIAL_CONTENT_RELATION_CREATE);
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return ownUno.hashCode();
    }
}
