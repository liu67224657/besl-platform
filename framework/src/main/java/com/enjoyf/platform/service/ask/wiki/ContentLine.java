package com.enjoyf.platform.service.ask.wiki;

import com.enjoyf.platform.service.ValidStatus;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhimingli on 2017-3-22 0022.
 */
public class ContentLine implements Serializable {
    private Long id;
    private String linekey;
    private ContentLineOwn ownId;
    private ContentLineType lineType = ContentLineType.CONTENTLINE_ARCHIVE;
    private Long destId;
    private double score;
    private Date createTime;
    private ValidStatus validStatus = ValidStatus.VALID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLinekey() {
        return linekey;
    }

    public void setLinekey(String linekey) {
        this.linekey = linekey;
    }

    public ContentLineOwn getOwnId() {
        return ownId;
    }

    public void setOwnId(ContentLineOwn ownId) {
        this.ownId = ownId;
    }

    public ContentLineType getLineType() {
        return lineType;
    }

    public void setLineType(ContentLineType lineType) {
        this.lineType = lineType;
    }

    public Long getDestId() {
        return destId;
    }

    public void setDestId(Long destId) {
        this.destId = destId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
