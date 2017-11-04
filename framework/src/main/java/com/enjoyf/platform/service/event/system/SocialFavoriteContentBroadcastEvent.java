/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.content.ContentType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:15
 * Description:
 */
public class SocialFavoriteContentBroadcastEvent extends SystemEvent {

   private String favoriteUno;
    //
    private String contentUno;

    //
    private String contentId;

    //
    private String rootContentUno;
    private String rootContentId;

    //
    private String parentContentUno;
    private String parentContentId;

    private ContentType contentType;

    //
    private Date postDate;

    public SocialFavoriteContentBroadcastEvent() {
        super(SystemEventType.SOCIAL_CONTENT_FAVORITE_BROADCAST);
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

    public String getRootContentUno() {
        return rootContentUno;
    }

    public void setRootContentUno(String rootContentUno) {
        this.rootContentUno = rootContentUno;
    }

    public String getRootContentId() {
        return rootContentId;
    }

    public void setRootContentId(String rootContentId) {
        this.rootContentId = rootContentId;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getParentContentUno() {
        return parentContentUno;
    }

    public void setParentContentUno(String parentContentUno) {
        this.parentContentUno = parentContentUno;
    }

    public String getParentContentId() {
        return parentContentId;
    }

    public void setParentContentId(String parentContentId) {
        this.parentContentId = parentContentId;
    }

    public String getFavoriteUno() {
        return favoriteUno;
    }

    public void setFavoriteUno(String favoriteUno) {
        this.favoriteUno = favoriteUno;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
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
