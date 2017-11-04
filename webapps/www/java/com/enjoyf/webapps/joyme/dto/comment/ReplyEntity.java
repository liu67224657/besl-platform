package com.enjoyf.webapps.joyme.dto.comment;

import com.enjoyf.platform.service.comment.ReplyBody;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-12
 * Time: 下午8:32
 * To change this template use File | Settings | File Templates.
 */
public class ReplyEntity {

    private long rid;//评论ID

    private long pid;//父类ID

    private long oid;//跟ID 如果是评论=0

    private int sub_reply_sum;//回复数
    private int agree_sum;//支持
    private int disagree_sum;//不支持

    private ReplyBody body;

    private String post_date;

    private long post_time;

    private int floor_num;
    private int is_agree = 0;//0未赞 1已赞

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public int getSub_reply_sum() {
        return sub_reply_sum;
    }

    public void setSub_reply_sum(int sub_reply_sum) {
        this.sub_reply_sum = sub_reply_sum;
    }

    public int getAgree_sum() {
        return agree_sum;
    }

    public void setAgree_sum(int agree_sum) {
        this.agree_sum = agree_sum;
    }

    public int getDisagree_sum() {
        return disagree_sum;
    }

    public void setDisagree_sum(int disagree_sum) {
        this.disagree_sum = disagree_sum;
    }

    public ReplyBody getBody() {
        return body;
    }

    public void setBody(ReplyBody body) {
        this.body = body;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public long getPost_time() {
        return post_time;
    }

    public void setPost_time(long post_time) {
        this.post_time = post_time;
    }

    public int getFloor_num() {
        return floor_num;
    }

    public void setFloor_num(int floor_num) {
        this.floor_num = floor_num;
    }

    public int getIs_agree() {
        return is_agree;
    }

    public void setIs_agree(int is_agree) {
        this.is_agree = is_agree;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
