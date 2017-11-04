package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.service.content.ContentInteraction;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.viewline.ViewLineItem;
import com.enjoyf.webapps.joyme.weblogic.entity.BlogContent;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-14
 * Time: 下午1:14
 * To change this template use File | Settings | File Templates.
 */
public class LineItemContentDTO implements Serializable {
    private BlogContent blogContent;
    private ViewLineItem lineItem;
    private ContentInteraction contentInteraction;
    private ProfileBlog interactionProfileBlog;
    private VoteDto voteDto;

    // the getter and setter

    public BlogContent getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(BlogContent blogContent) {
        this.blogContent = blogContent;
    }

    public ViewLineItem getLineItem() {
        return lineItem;
    }

    public void setLineItem(ViewLineItem lineItem) {
        this.lineItem = lineItem;
    }

    public ContentInteraction getContentInteraction() {
        return contentInteraction;
    }

    public void setContentInteraction(ContentInteraction contentInteraction) {
        this.contentInteraction = contentInteraction;
    }

    public ProfileBlog getInteractionProfileBlog() {
        return interactionProfileBlog;
    }

    public void setInteractionProfileBlog(ProfileBlog interactionProfileBlog) {
        this.interactionProfileBlog = interactionProfileBlog;
    }

    public VoteDto getVoteDto() {
        return voteDto;
    }

    public void setVoteDto(VoteDto voteDto) {
        this.voteDto = voteDto;
    }
}
