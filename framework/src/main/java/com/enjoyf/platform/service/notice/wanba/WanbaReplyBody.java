package com.enjoyf.platform.service.notice.wanba;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by zhimingli on 2016/9/23 0023.
 */
public class WanbaReplyBody implements Serializable {
    private long answerId; //答案id
    private String replyId;
    private long parentIdreplyId;
    private String replyProfileId;//回答者的id
    private String replyDesc;//回答的内容
    private String parentDesc;//回答的源内容
    private String commentId;//
    private WanbaReplyBodyType replyBodyType;

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public WanbaReplyBodyType getReplyBodyType() {
        return replyBodyType;
    }

    public void setReplyBodyType(WanbaReplyBodyType replyBodyType) {
        this.replyBodyType = replyBodyType;
    }

    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getReplyProfileId() {
        return replyProfileId;
    }

    public void setReplyProfileId(String replyProfileId) {
        this.replyProfileId = replyProfileId;
    }

    public String getReplyDesc() {
        return replyDesc;
    }

    public void setReplyDesc(String replyDesc) {
        this.replyDesc = replyDesc;
    }

    public String getParentDesc() {
        return parentDesc;
    }

    public void setParentDesc(String parentDesc) {
        this.parentDesc = parentDesc;
    }

    public long getParentIdreplyId() {
        return parentIdreplyId;
    }

    public void setParentIdreplyId(long parentIdreplyId) {
        this.parentIdreplyId = parentIdreplyId;
    }

    public static WanbaReplyBody fromJson(String jsonStr) {
        return new Gson().fromJson(jsonStr, WanbaReplyBody.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
