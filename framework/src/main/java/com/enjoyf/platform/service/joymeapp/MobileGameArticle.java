package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ValidStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xupeng on 14-9-15.
 */
public class MobileGameArticle implements Serializable {
    private long id ;
    private String title;
    private String desc;
    private String articleUrl;
    private String authorName;
    private Date createTime;
    private ValidStatus validStatus;
    private int displayOrder;
    private long gameDbId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
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

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public long getGameDbId() {
        return gameDbId;
    }

    public void setGameDbId(long gameDbId) {
        this.gameDbId = gameDbId;
    }

    @Override
    public String toString() {
        return "MobileGameArticle{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", articleUrl='" + articleUrl + '\'' +
                ", authorName='" + authorName + '\'' +
                ", createTime=" + createTime +
                ", validStatus=" + validStatus +
                ", displayOrder=" + displayOrder +
                ", gameDbId=" + gameDbId +
                '}';
    }

}
