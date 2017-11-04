/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.shorturl;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-10-13 下午6:58
 * Description:
 */
public class ShortUrl implements Serializable {
    //
    private Long urlId;

    //the key is generate by the urlId.
    private String urlKey;
    private String url;

    //
    private ShortUrlProtocolType protocolType = ShortUrlProtocolType.HTTP;
    private ShortUrlFileType fileType = ShortUrlFileType.HTML;

    //
    private ShortUrlStatus urlStatus = ShortUrlStatus.VALID;

    //
    private Date initDate;
    private String initUno;

    //
    private Long clickTimes = 0l;


    public ShortUrl() {
    }

    public Long getUrlId() {
        return urlId;
    }

    public void setUrlId(Long urlId) {
        this.urlId = urlId;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ShortUrlProtocolType getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ShortUrlProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    public ShortUrlFileType getFileType() {
        return fileType;
    }

    public void setFileType(ShortUrlFileType fileType) {
        this.fileType = fileType;
    }

    public ShortUrlStatus getUrlStatus() {
        return urlStatus;
    }

    public void setUrlStatus(ShortUrlStatus urlStatus) {
        this.urlStatus = urlStatus;
    }

    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    public String getInitUno() {
        return initUno;
    }

    public void setInitUno(String initUno) {
        this.initUno = initUno;
    }

    public Long getClickTimes() {
        return clickTimes;
    }

    public void setClickTimes(Long clickTimes) {
        this.clickTimes = clickTimes;
    }

    //
    @Override
    public int hashCode() {
        return urlKey.hashCode();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
