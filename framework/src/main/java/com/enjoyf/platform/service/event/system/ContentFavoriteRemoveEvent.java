/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:15
 * Description:
 */
public class ContentFavoriteRemoveEvent extends SystemEvent {
    //
    private String contentUno;
    private String contentId;
    private String favoriteUno;

    //
    public ContentFavoriteRemoveEvent() {
        super(SystemEventType.CONTENT_FAVORITE_REMOVE);
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
