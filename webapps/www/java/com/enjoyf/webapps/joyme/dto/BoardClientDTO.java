package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentInteraction;
import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.social.SocialRelation;
import com.enjoyf.platform.service.viewline.ViewCategory;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-12
 * Time: 下午5:15
 * To change this template use File | Settings | File Templates.
 */
public class BoardClientDTO implements Serializable {
    private GameResource gameResource;
    private SocialRelation socialRelation;

    private ViewCategory viewCategory;
    private Content content;
    private Profile contentProfile;
    private ContentInteraction contentInteraction;
    private Profile interactionProfile;
    private ViewCategory boardCategory;
    
    private int todayNum = 0;
    private int totalNum = 0;

    // the getter and setter


    public GameResource getGameResource() {
        return gameResource;
    }

    public void setGameResource(GameResource gameResource) {
        this.gameResource = gameResource;
    }

    public ViewCategory getViewCategory() {
        return viewCategory;
    }

    public void setViewCategory(ViewCategory viewCategory) {
        this.viewCategory = viewCategory;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Profile getContentProfile() {
        return contentProfile;
    }

    public void setContentProfile(Profile contentProfile) {
        this.contentProfile = contentProfile;
    }

    public ContentInteraction getContentInteraction() {
        return contentInteraction;
    }

    public void setContentInteraction(ContentInteraction contentInteraction) {
        this.contentInteraction = contentInteraction;
    }

    public Profile getInteractionProfile() {
        return interactionProfile;
    }

    public void setInteractionProfile(Profile interactionProfile) {
        this.interactionProfile = interactionProfile;
    }

    public int getTodayNum() {
        return todayNum;
    }

    public void setTodayNum(int todayNum) {
        this.todayNum = todayNum;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public ViewCategory getBoardCategory() {
        return boardCategory;
    }

    public void setBoardCategory(ViewCategory boardCategory) {
        this.boardCategory = boardCategory;
    }

    public SocialRelation getSocialRelation() {
        return socialRelation;
    }

    public void setSocialRelation(SocialRelation socialRelation) {
        this.socialRelation = socialRelation;
    }
}
