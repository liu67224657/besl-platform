package com.enjoyf.platform.service.ask.wiki;

import com.enjoyf.platform.service.ValidStatus;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhimingli on 2017-3-22 0022.
 */
public class Content implements Serializable {
    private Long id;
    private String commentId;
    private String title;
    private String description;
    private String pic;
    private String author;
    private String gameId;
    private Date publishTime;
    private String webUrl;
    private ContentSource source = ContentSource.CMS_ARCHIVE;
    private ValidStatus removeStatus = ValidStatus.VALID;
    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public ContentSource getSource() {
        return source;
    }

    public void setSource(ContentSource source) {
        this.source = source;
    }

    public ValidStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ValidStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

