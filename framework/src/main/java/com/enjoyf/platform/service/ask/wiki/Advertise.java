package com.enjoyf.platform.service.ask.wiki;

import com.enjoyf.platform.service.ValidStatus;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhimingli on 2017-3-22 0022.
 * <p/>
 */
public class Advertise implements Serializable {
    private Long id;
    private String appkey;
    private String title;
    private String description;
    private AdvertiseDomain domain;  //位置：轮播、广告
    private Integer platform;
    private Integer type;  //jt
    private String target; //ji
    private String pic;
    private String extend;
    private ValidStatus removeStatus = ValidStatus.VALID;
    private Long displayOrder;
    private Date createDate;

    private Date startTime;
    private Date endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
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

    public AdvertiseDomain getDomain() {
        return domain;
    }

    public void setDomain(AdvertiseDomain domain) {
        this.domain = domain;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public ValidStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ValidStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public Long getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Long displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
