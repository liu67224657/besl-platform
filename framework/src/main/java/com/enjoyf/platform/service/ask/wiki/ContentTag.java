package com.enjoyf.platform.service.ask.wiki;

import com.enjoyf.platform.service.ValidStatus;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhimingli on 2017-3-31 0031.
 */
public class ContentTag implements Serializable {
    private Long id;
    private String name;
    private String target;//目标ji
    private ContentTagType tagType;//jt
    private ContentTagLine tagLine = ContentTagLine.RECOMMEND;
    private Long displayOrder;
    private ValidStatus validStatus = ValidStatus.VALID;
    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Long displayOrder) {
        this.displayOrder = displayOrder;
    }

    public ContentTagType getTagType() {
        return tagType;
    }

    public void setTagType(ContentTagType tagType) {
        this.tagType = tagType;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public ContentTagLine getTagLine() {
        return tagLine;
    }

    public void setTagLine(ContentTagLine tagLine) {
        this.tagLine = tagLine;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public ContentTag toObject(String jsonStr) {
        return new Gson().fromJson(jsonStr, ContentTag.class);
    }
}
