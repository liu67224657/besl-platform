/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.social.SocialRelation;
import com.enjoyf.platform.service.timeline.TimeLineContentType;
import com.enjoyf.platform.service.timeline.TimeLineDomain;
import com.enjoyf.platform.service.timeline.TimeLineFilterType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:15
 * Description:
 */
public class TimeLineFavoriteContentRemoveEvent extends SystemEvent {
    private String ownUno;

    //the time line domain.
    private TimeLineDomain domain;

    //the direct id
    private String directId;

    private String favoriteUno;

    //
    public TimeLineFavoriteContentRemoveEvent() {
        super(SystemEventType.TIMELINE_FAVORITE_CONTENT_REMOVE);
    }

    public String getOwnUno() {
        return ownUno;
    }

    public void setOwnUno(String ownUno) {
        this.ownUno = ownUno;
    }

    public TimeLineDomain getDomain() {
        return domain;
    }

    public void setDomain(TimeLineDomain domain) {
        this.domain = domain;
    }

    public String getDirectId() {
        return directId;
    }

    public void setDirectId(String directId) {
        this.directId = directId;
    }

    public String getFavoriteUno() {
        return favoriteUno;
    }

    public void setFavoriteUno(String favoriteUno) {
        this.favoriteUno = favoriteUno;
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
