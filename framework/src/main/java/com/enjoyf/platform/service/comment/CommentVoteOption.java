package com.enjoyf.platform.service.comment;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Diao
 * Date: 15-1-6
 * Time: 下午12:36
 * To change this template use File | Settings | File Templates.
 */
public class CommentVoteOption implements Serializable {

    private String voteOptionId;
    private String commentId;
    private String title;
    private String pic;
    private long optionTotal;
    private String removeStatus;
    private long displayOrder;
    private Date createTime;
    private String createUser;

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getVoteOptionId() {
        return voteOptionId;
    }

    public void setVoteOptionId(String voteOptionId) {
        this.voteOptionId = voteOptionId;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public long getOptionTotal() {
        return optionTotal;
    }

    public void setOptionTotal(long optionTotal) {
        this.optionTotal = optionTotal;
    }

    public String getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(String removeStatus) {
        this.removeStatus = removeStatus;
    }

    public long getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(long displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
