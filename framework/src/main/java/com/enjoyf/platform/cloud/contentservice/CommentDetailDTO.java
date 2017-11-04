package com.enjoyf.platform.cloud.contentservice;

import com.enjoyf.platform.cloud.contentservice.game.GameVM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengxu on 2017/6/21.
 */
public class CommentDetailDTO {
    private GameVM game;//所属游戏
    private CommentDTO comment;//点评内容
    private List<ProfileDTO> profile = new ArrayList<ProfileDTO>();//点赞用户列表

    public GameVM getGame() {
        return game;
    }

    public void setGame(GameVM game) {
        this.game = game;
    }

    public CommentDTO getComment() {
        return comment;
    }

    public void setComment(CommentDTO comment) {
        this.comment = comment;
    }

    public List<ProfileDTO> getProfile() {
        return profile;
    }

    public void setProfile(List<ProfileDTO> profile) {
        this.profile = profile;
    }
}
