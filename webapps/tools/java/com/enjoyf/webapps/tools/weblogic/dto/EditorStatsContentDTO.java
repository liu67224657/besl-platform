package com.enjoyf.webapps.tools.weblogic.dto;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-4-1
 * Time: 下午1:48
 * To change this template use File | Settings | File Templates.
 */
public class EditorStatsContentDTO {
    private String contentId;
    private String linkurl;
    private String subType;
    private Date createDate;

    private long pv;
    private long uv;
    private long cpv;
    private long cmt;
    private long post;

    public EditorStatsContentDTO(String contentId) {
        this.contentId = contentId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public long getPv() {
        return pv;
    }

    public void setPv(long pv) {
        this.pv = pv;
    }

    public long getUv() {
        return uv;
    }

    public void setUv(long uv) {
        this.uv = uv;
    }

    public long getCpv() {
        return cpv;
    }

    public void setCpv(long cpv) {
        this.cpv = cpv;
    }

    public long getCmt() {
        return cmt;
    }

    public void setCmt(long cmt) {
        this.cmt = cmt;
    }

    public long getPost() {
        return post;
    }

    public void setPost(long post) {
        this.post = post;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
