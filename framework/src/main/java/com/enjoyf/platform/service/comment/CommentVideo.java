package com.enjoyf.platform.service.comment;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xupeng on 16/3/18.
 */

public class CommentVideo implements Serializable {
    public Long commentVideoId;
    public String videoTitle;
    public String videoDesc;
    public String profileid;
    public String appkey; //appkey
    public String sdk_key;//游戏key
    public CommentVideoUrl jsonUrl;// json格式的视频url
    public CommentVideoType commentVideoType;
    public ActStatus actStatus;
    public Date createTime;
    public String createIp;
    public String videoPic;

    public Long getCommentVideoId() {
        return commentVideoId;
    }

    public void setCommentVideoId(Long commentVideoId) {
        this.commentVideoId = commentVideoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDesc() {
        return videoDesc;
    }

    public void setVideoDesc(String videoDesc) {
        this.videoDesc = videoDesc;
    }

    public String getProfileid() {
        return profileid;
    }

    public void setProfileid(String profileid) {
        this.profileid = profileid;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getSdk_key() {
        return sdk_key;
    }

    public void setSdk_key(String sdk_key) {
        this.sdk_key = sdk_key;
    }


    public CommentVideoType getCommentVideoType() {
        return commentVideoType;
    }

    public void setCommentVideoType(CommentVideoType commentVideoType) {
        this.commentVideoType = commentVideoType;
    }

    public ActStatus getActStatus() {
        return actStatus;
    }

    public void setActStatus(ActStatus actStatus) {
        this.actStatus = actStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public CommentVideoUrl getJsonUrl() {
        return jsonUrl;
    }

    public void setJsonUrl(CommentVideoUrl jsonUrl) {
        this.jsonUrl = jsonUrl;
    }

    public String getVideoPic() {
        return videoPic;
    }

    public void setVideoPic(String videoPic) {
        this.videoPic = videoPic;
    }

    public String toJson() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static CommentVideo parse(String jsonStr) {
        CommentVideo returnValue = null;
        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<CommentVideo>() {
                });
            } catch (IOException e) {
                GAlerter.lab("ReplyBody parse error, jsonStr:" + jsonStr, e);
            }
        }
        return returnValue;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
