/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.content.ContentField;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.ObjectField;

import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:15
 * Description:
 */
public class ContentFavoriteEvent extends SystemEvent {
    //
    private String contentUno;
    private String contentId;
    private String favoriteUno;
    private String favoriteIp;
    private Date favoriteDate;

    //
    public ContentFavoriteEvent() {
        super(SystemEventType.CONTENT_FAVORITE);
    }

    public String getContentUno() {
        return contentUno;
    }

    public void setContentUno(String contentUno) {
        this.contentUno = contentUno;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getFavoriteUno() {
        return favoriteUno;
    }

    public void setFavoriteUno(String favoriteUno) {
        this.favoriteUno = favoriteUno;
    }

    public String getFavoriteIp() {
        return favoriteIp;
    }

    public void setFavoriteIp(String favoriteIp) {
        this.favoriteIp = favoriteIp;
    }

    public Date getFavoriteDate() {
        return favoriteDate;
    }

    public void setFavoriteDate(Date favoriteDate) {
        this.favoriteDate = favoriteDate;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return contentUno.hashCode();
    }
}
