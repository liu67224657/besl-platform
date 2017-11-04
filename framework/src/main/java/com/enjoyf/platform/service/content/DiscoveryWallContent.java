/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.service.ContentTag;
import com.enjoyf.platform.service.content.wall.WallContentRule;
import com.enjoyf.platform.service.content.wall.WallContentType;
import com.enjoyf.platform.service.profile.VerifyType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午5:21
 * Description: 按照authorUno进行分类。
 */
public class DiscoveryWallContent implements Serializable {
    private static final Random random = new Random();

    //contentid
    private String contentUno;
    private String contentId;

    //domain
    private String domainName;
    private String screenName;

    //tag
    private ContentTag wallTag;

    //thumbImgLink
    private String thumbImgLink;

    private String wallSubject;
    //内容
    private String wallContent;

    //内容类型：一句话，图片；文章；etc
    private ContentType contentType = new ContentType();
    private WallContentType wallContentType;
    private WallContentRule wallContentRule;

    // 发布时间，IP
    private Date publishDate;
    private String dateStr;

    private VerifyType verifyType = VerifyType.N_VERIFY;

    private Integer width = 0;
    private Integer height = 0;

    //
    public DiscoveryWallContent() {
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
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

    public ContentTag getWallTag() {
        return wallTag;
    }

    public void setWallTag(ContentTag wallTag) {
        this.wallTag = wallTag;
    }

    public String getThumbImgLink() {
        return thumbImgLink;
    }

    public void setThumbImgLink(String thumbImgLink) {
        this.thumbImgLink = thumbImgLink;
    }

    public String getWallSubject() {
        return wallSubject;
    }

    public void setWallSubject(String wallSubject) {
        this.wallSubject = wallSubject;
    }

    public String getWallContent() {
        return wallContent;
    }

    public void setWallContent(String wallContent) {
        this.wallContent = wallContent;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public WallContentType getWallContentType() {
        return wallContentType;
    }

    public void setWallContentType(WallContentType wallContentType) {
        this.wallContentType = wallContentType;
    }

    public WallContentRule getWallContentRule() {
        return wallContentRule;
    }

    public void setWallContentRule(WallContentRule wallContentRule) {
        this.wallContentRule = wallContentRule;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public VerifyType getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(VerifyType verifyType) {
        this.verifyType = verifyType;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Override
    public int hashCode() {
        return random.nextInt();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
