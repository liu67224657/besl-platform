package com.enjoyf.platform.service.comment;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-11
 * Time: 上午11:08
 * To change this template use File | Settings | File Templates.
 */
public class CommentForbid implements Serializable {

    //用户profile_id
    private String profileid;
    //禁言起始时间
    private Date startTime;

    //禁言持续时间,单位秒,默认是0,代表永久禁言
    private long length;

    private String nickName="";

    private Date endTime;


    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProfileid() {
        return profileid;
    }

    public void setProfileid(String profileid) {
        this.profileid = profileid;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
