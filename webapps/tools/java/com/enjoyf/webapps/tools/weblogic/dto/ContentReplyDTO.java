package com.enjoyf.webapps.tools.weblogic.dto;

import com.enjoyf.platform.service.content.ContentQueryParam;
import com.enjoyf.platform.service.content.ContentReply;
import com.enjoyf.platform.service.profile.Profile;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 11-12-5
 * Time: 下午3:18
 * To change this template use File | Settings | File Templates.
 */
//todo 将要删除掉这个类
public class ContentReplyDTO {
    private ContentReply contentReply;
    private ContentQueryParam param ;
    private Profile profile;
    private Profile authorProfile;

    public ContentReply getContentReply() {
        return contentReply;
    }

    public void setContentReply(ContentReply contentReply) {
        this.contentReply = contentReply;
    }

    public ContentQueryParam getParam() {
        return param;
    }

    public void setParam(ContentQueryParam param) {
        this.param = param;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Profile getAuthorProfile() {
        return authorProfile;
    }

    public void setAuthorProfile(Profile authorProfile) {
        this.authorProfile = authorProfile;
    }
}
