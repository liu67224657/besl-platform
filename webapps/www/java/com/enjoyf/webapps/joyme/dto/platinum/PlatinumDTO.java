package com.enjoyf.webapps.joyme.dto.platinum;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-6-26
 * Time: 下午12:05
 * To change this template use File | Settings | File Templates.
 */
public class PlatinumDTO {

    private long itemId;
    private String title;
    private String description;
    private String pic;
    private String url;
    private String softwareSize;
    private Date startDate;
    private Date endDate;
    private boolean hot;

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSoftwareSize() {
        return softwareSize;
    }

    public void setSoftwareSize(String softwareSize) {
        this.softwareSize = softwareSize;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }
}
