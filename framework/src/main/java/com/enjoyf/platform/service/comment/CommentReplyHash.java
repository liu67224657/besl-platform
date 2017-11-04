package com.enjoyf.platform.service.comment;/**
 * Created by ericliu on 16/8/30.
 */

import java.io.Serializable;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/8/30
 */
public class CommentReplyHash implements Serializable {
    private String commentId;
    private long rootId;
    private int commentSum;//评论
    private int commentTotal;//总评论数
    private int agreeSum;
    private int hot;

    public CommentReplyHash(String commentId, long rootId) {
        this.rootId = rootId;
        this.commentId = commentId;
    }

    public String getCommentId() {
        return commentId;
    }

    public int getCommentSum() {
        return commentSum;
    }

    public void setCommentSum(int commentSum) {
        this.commentSum = commentSum;
    }

    public int getCommentTotal() {
        return commentTotal;
    }

    public void setCommentTotal(int commentTotal) {
        this.commentTotal = commentTotal;
    }

    public long getRootId() {
        return rootId;
    }

    public int getAgreeSum() {
        return agreeSum;
    }

    public void setAgreeSum(int agreeSum) {
        this.agreeSum = agreeSum;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }
}
