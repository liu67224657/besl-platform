package com.enjoyf.webapps.joyme.weblogic.entity;

import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentInteraction;
import com.enjoyf.platform.service.content.ContentReply;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.timeline.TimeLineContentType;
import com.enjoyf.platform.service.timeline.TimeLineDomain;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-10-12
 * Time: 下午12:57
 * To change this template use File | Settings | File Templates.
 */
public class AtmeInfo {
    private Long tlId;
    private String directId;

    //the atme content type. content or reply.
    private TimeLineContentType contentType = TimeLineContentType.CONTENT;

    //atme in content:
    private BlogContent blogContent;

    //atme in reply:
    // the direct profile、contentReply
    private Profile directProfile;
    private ContentInteraction directContentReply;

    // the parent profile contentReply
    private Profile parentProfile;
    private ContentInteraction parentContentReply;

    // the relation content only use in reply
    private Profile relationProfile;
    private Content relationContent;
    private boolean relationFavorite = false;

    public AtmeInfo(){
    }

    public AtmeInfo(TimeLineContentType contentType, BlogContent blogContent){
        this.contentType = contentType;
        this.blogContent = blogContent;
    }

    public AtmeInfo(TimeLineContentType contentType,
                    Profile directProfile,ContentInteraction directContentReply,
                    Profile relationProfile,Content relationContent){
        this.contentType = contentType;
        this.directProfile = directProfile;
        this.directContentReply = directContentReply;
        this.relationProfile = relationProfile;
        this.relationContent = relationContent;
    }

    public TimeLineContentType getContentType() {
        return contentType;
    }

    public void setContentType(TimeLineContentType contentType) {
        this.contentType = contentType;
    }

    public Profile getDirectProfile() {
        return directProfile;
    }

    public void setDirectProfile(Profile directProfile) {
        this.directProfile = directProfile;
    }

    public ContentInteraction getDirectContentReply() {
        return directContentReply;
    }

    public void setDirectContentReply(ContentInteraction directContentReply) {
        this.directContentReply = directContentReply;
    }

    public Profile getParentProfile() {
        return parentProfile;
    }

    public void setParentProfile(Profile parentProfile) {
        this.parentProfile = parentProfile;
    }

    public ContentInteraction getParentContentReply() {
        return parentContentReply;
    }

    public void setParentContentReply(ContentInteraction parentContentReply) {
        this.parentContentReply = parentContentReply;
    }

    public Profile getRelationProfile() {
        return relationProfile;
    }

    public void setRelationProfile(Profile relationProfile) {
        this.relationProfile = relationProfile;
    }

    public Content getRelationContent() {
        return relationContent;
    }

    public void setRelationContent(Content relationContent) {
        this.relationContent = relationContent;
    }

    public boolean isRelationFavorite() {
        return relationFavorite;
    }

    public void setRelationFavorite(boolean relationFavorite) {
        this.relationFavorite = relationFavorite;
    }

    public BlogContent getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(BlogContent blogContent) {
        this.blogContent = blogContent;
    }

    public Long getTlId() {
        return tlId;
    }

    public void setTlId(Long tlId) {
        this.tlId = tlId;
    }

    public String getDirectId() {
        return directId;
    }

    public void setDirectId(String directId) {
        this.directId = directId;
    }
}
