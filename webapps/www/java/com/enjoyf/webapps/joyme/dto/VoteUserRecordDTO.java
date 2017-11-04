package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.vote.VoteUserRecord;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-10-9
 * Time: 上午9:48
 * To change this template use File | Settings | File Templates.
 */
public class VoteUserRecordDTO {
    private ProfileBlog blog;
    private VoteUserRecord voteUserRecord;

    public VoteUserRecordDTO(){

    }

    public ProfileBlog getBlog() {
        return blog;
    }

    public void setBlog(ProfileBlog blog) {
        this.blog = blog;
    }

    public VoteUserRecord getVoteUserRecord() {
        return voteUserRecord;
    }

    public void setVoteUserRecord(VoteUserRecord voteUserRecord) {
        this.voteUserRecord = voteUserRecord;
    }
}
