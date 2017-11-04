package com.enjoyf.webapps.joyme.weblogic.entity;

import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.webapps.joyme.dto.RelationMiniResourceDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p/>
 * Description:用于页面展示，包括Content和profile
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class BlogContent {
    private Profile profile;
    private Content content;
    private Content rootContent;
    private Profile rootProfile;

    private boolean favorite = false;
    private Date favoriteDate;
    private boolean rootFavorite = false;
    private Date rootFavoriteDate;

    private List<Profile> blogFavProfiles = new ArrayList<Profile>();
    private int followFavSums = 0;

    private RelationMiniResourceDTO board;
    private RelationMiniResourceDTO rootBoard;

    private RelationMiniResourceDTO game;
    private RelationMiniResourceDTO rootGame;

    public BlogContent() {
    }

    public BlogContent(Profile profileBlog, Content content) {
        this.profile = profileBlog;
        this.content = content;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Content getRootContent() {
        return rootContent;
    }

    public void setRootContent(Content rootContent) {
        this.rootContent = rootContent;
    }

    public Profile getRootProfile() {
        return rootProfile;
    }

    public void setRootProfile(Profile rootProfile) {
        this.rootProfile = rootProfile;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean getRootFavorite() {
        return rootFavorite;
    }

    public void setRootFavorite(boolean rootFavorite) {
        this.rootFavorite = rootFavorite;
    }

    public List<Profile> getBlogFavProfiles() {
        return blogFavProfiles;
    }

    public void setBlogFavProfiles(List<Profile> blogFavProfiles) {
        this.blogFavProfiles = blogFavProfiles;
    }

    public Date getFavoriteDate() {
        return favoriteDate;
    }

    public void setFavoriteDate(Date favoriteDate) {
        this.favoriteDate = favoriteDate;
    }

    public Date getRootFavoriteDate() {
        return rootFavoriteDate;
    }

    public void setRootFavoriteDate(Date rootFavoriteDate) {
        this.rootFavoriteDate = rootFavoriteDate;
    }

    public int getFollowFavSums() {
        return followFavSums;
    }

    public void setFollowFavSums(int followFavSums) {
        this.followFavSums = followFavSums;
    }

    public RelationMiniResourceDTO getBoard() {
        return board;
    }

    public void setBoard(RelationMiniResourceDTO board) {
        this.board = board;
    }

    public RelationMiniResourceDTO getRootBoard() {
        return rootBoard;
    }

    public void setRootBoard(RelationMiniResourceDTO rootBoard) {
        this.rootBoard = rootBoard;
    }

    public RelationMiniResourceDTO getGame() {
        return game;
    }

    public void setGame(RelationMiniResourceDTO game) {
        this.game = game;
    }

    public RelationMiniResourceDTO getRootGame() {
        return rootGame;
    }

    public void setRootGame(RelationMiniResourceDTO rootGame) {
        this.rootGame = rootGame;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
