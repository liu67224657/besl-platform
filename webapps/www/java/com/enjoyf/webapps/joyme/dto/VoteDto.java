package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.vote.Vote;
import com.enjoyf.platform.service.vote.VoteUserRecord;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-28
 * Time: 上午9:47
 * To change this template use File | Settings | File Templates.
 */
public class VoteDto {
    private Vote vote;
    private List<VoteUserRecordDTO> userRecordDTOList;
    private VoteUserRecord voteUserRecord;
    private Content voteContent;
    private ProfileBlog voteBlog;
    //
    public VoteDto(){

    }

    // getter and setter

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public List<VoteUserRecordDTO> getUserRecordDTOList() {
        return userRecordDTOList;
    }

    public void setUserRecordDTOList(List<VoteUserRecordDTO> userRecordDTOList) {
        this.userRecordDTOList = userRecordDTOList;
    }

    public VoteUserRecord getVoteUserRecord() {
        return voteUserRecord;
    }

    public void setVoteUserRecord(VoteUserRecord voteUserRecord) {
        this.voteUserRecord = voteUserRecord;
    }

    public Content getVoteContent() {
        return voteContent;
    }

    public void setVoteContent(Content voteContent) {
        this.voteContent = voteContent;
    }

    public ProfileBlog getVoteBlog() {
        return voteBlog;
    }

    public void setVoteBlog(ProfileBlog voteBlog) {
        this.voteBlog = voteBlog;
    }
}
