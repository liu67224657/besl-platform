package com.enjoyf.webapps.tools.weblogic.dto;

import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentInteraction;
import com.enjoyf.platform.service.content.ContentQueryParam;
import com.enjoyf.platform.service.content.ContentReply;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.tools.AuditContent;
import com.enjoyf.platform.service.tools.ContentAuditStatus;

import java.io.Serializable;

/**
 * Author: zhaoxin
 * Date: 11-10-31
 * Time: 下午4:41
 * Desc:
 */

public class ContentDTO implements Serializable{

    private Content content;
    private ContentAuditStatus auditContent;
    private Profile profile ;
    private ContentInteraction contentReply;
    private Profile authorProfile;
    private ContentQueryParam param;

    public ContentInteraction getContentReply() {
        return contentReply;
    }

    public void setContentReply(ContentInteraction contentReply) {
        this.contentReply = contentReply;
    }

    public Profile getAuthorProfile() {
        return authorProfile;
    }

    public void setAuthorProfile(Profile authorProfile) {
        this.authorProfile = authorProfile;
    }

    public ContentAuditStatus getAuditContent() {
        return auditContent;
    }

    public void setAuditContent(ContentAuditStatus auditContent) {
        this.auditContent = auditContent;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public ContentQueryParam getParam() {
        return param;
    }

    public void setParam(ContentQueryParam param) {
        this.param = param;
    }
}