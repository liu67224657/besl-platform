/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.service.ContentTag;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@enjoyfound.com">ericliu</a>
 * Create time: 11-8-17 下午5:21
 * Description:热点文章
 */
public class HotContent implements Serializable {
    private String contentId;
    private String contentUno;

    private HotType hotType= HotType.HOT;

    private ContentType contentType = new ContentType();

    private ContentTag contentTag = new ContentTag();
    private ContentTag searchTag = new ContentTag();
    private ContentPublishType publishType = ContentPublishType.ORIGINAL;

    //转发时为原文ID。
    private String rootContentId;
    private String rootContentUno;

    private int hotRate;
    private Date hotDate;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentUno() {
        return contentUno;
    }

    public void setContentUno(String contentUno) {
        this.contentUno = contentUno;
    }

    public HotType getHotType() {
        return hotType;
    }

    public void setHotType(HotType hotType) {
        this.hotType = hotType;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public ContentTag getContentTag() {
        return contentTag;
    }

    public void setContentTag(ContentTag contentTag) {
        this.contentTag = contentTag;
    }

    public ContentTag getSearchTag() {
        return searchTag;
    }

    public void setSearchTag(ContentTag searchTag) {
        this.searchTag = searchTag;
    }

    public ContentPublishType getPublishType() {
        return publishType;
    }

    public void setPublishType(ContentPublishType publishType) {
        this.publishType = publishType;
    }

    public int getHotRate() {
        return hotRate;
    }

    public void setHotRate(int hotRate) {
        this.hotRate = hotRate;
    }

    public Date getHotDate() {
        return hotDate;
    }

    public void setHotDate(Date hotDate) {
        this.hotDate = hotDate;
    }

    public String getRootContentId() {
        return rootContentId;
    }

    public void setRootContentId(String rootContentId) {
        this.rootContentId = rootContentId;
    }

    public String getRootContentUno() {
        return rootContentUno;
    }

    public void setRootContentUno(String rootContentUno) {
        this.rootContentUno = rootContentUno;
    }

    @Override
    public int hashCode() {
        return contentId != null ? contentId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
