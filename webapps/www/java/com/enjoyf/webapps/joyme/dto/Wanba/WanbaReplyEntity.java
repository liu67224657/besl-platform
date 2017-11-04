package com.enjoyf.webapps.joyme.dto.Wanba;

import com.enjoyf.platform.service.comment.ReplyBody;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by zhimingli on 2016/9/19 0019.
 */
public class WanbaReplyEntity implements Serializable {
    private long replyid;//评论ID--replyid

    private long parentid;//父类ID

    private long rootid;//跟ID 如果是评论=0

    private ReplyBody body;

    private long post_time;


    public long getReplyid() {
        return replyid;
    }

    public void setReplyid(long replyid) {
        this.replyid = replyid;
    }

    public long getParentid() {
        return parentid;
    }

    public void setParentid(long parentid) {
        this.parentid = parentid;
    }

    public long getRootid() {
        return rootid;
    }

    public void setRootid(long rootid) {
        this.rootid = rootid;
    }

    public ReplyBody getBody() {
        return body;
    }

    public void setBody(ReplyBody body) {
        this.body = body;
    }

    public long getPost_time() {
        return post_time;
    }

    public void setPost_time(long post_time) {
        this.post_time = post_time;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
