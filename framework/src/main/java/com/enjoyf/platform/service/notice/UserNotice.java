package com.enjoyf.platform.service.notice;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/10
 * Description:
 */
public class UserNotice implements Serializable {
    private long userNoticeId;
    private String profileId;
    private String appkey;
    private String noticeType;
    private String body;//json 对象
    private Date createTime;
    private String destId;//来源的ID


    public long getUserNoticeId() {
        return userNoticeId;
    }

    public void setUserNoticeId(long userNoticeId) {
        this.userNoticeId = userNoticeId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }


    public static void main(String[] args) {
        String s = "{\"userNoticeId\":180,\"profileId\":\"0d01678ac6139ad916b3a9ec5bb11caf\",\"appkey\":\"\",\"noticeType\":\"answer\",\"body\":\"{\"quertionId\":82,\"answerId\":0,\"bodyType\":3}\",\"createTime\":\"Oct 26, 2016 1:00:00 PM\",\"destId\":\"Oct 26, 2016 1:00:00 PM\"}";
        UserNotice notice = new Gson().fromJson(s, UserNotice.class);
        //   System.out.println(notice.getDestId());
        System.out.println(notice);

    }

}
