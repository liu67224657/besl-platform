package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.*;

/**
 * <p/>
 * Description:游戏关系集合
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class GameRelationSet implements Serializable {


    private long gameResourceId;

    private GameRelation inviteRelation;
    private GameRelation boardRelation;
    private GameRelation baikeRelation;
    private GameRelation coverRelation;
    private Map<Long, GameRelation> gameMap = new LinkedHashMap<Long, GameRelation>();//相关游戏
    private Map<Long, GameRelation> groupMap = new LinkedHashMap<Long, GameRelation>();//相关小组
    private Map<Long, GameRelation> articleMap = new LinkedHashMap<Long, GameRelation>();
    private GameRelation downLoadRelation;
    private GameRelation groupContnetRelation;
    private GameRelation talentRelation;
    private GameRelation contributeRelation;
    private GameRelation headImageRelation;
    private GameRelation menuRelation;

    private List<GameRelation> gameRelationList = new ArrayList<GameRelation>();

    public GameRelation getBoardRelation() {
        return boardRelation;
    }

    public void setBoardRelation(GameRelation boardRelation) {
        this.boardRelation = boardRelation;
    }

    public GameRelation getBaikeRelation() {
        return baikeRelation;
    }

    public void setBaikeRelation(GameRelation baikeRelation) {
        this.baikeRelation = baikeRelation;
    }

    public GameRelation getCoverRelation() {
        return coverRelation;
    }

    public GameRelation getInviteRelation() {
        return inviteRelation;
    }

    public void setInviteRelation(GameRelation inviteRelation) {
        this.inviteRelation = inviteRelation;
    }

    public void setCoverRelation(GameRelation coverRelation) {
        this.coverRelation = coverRelation;
    }

    public List<GameRelation> getGameRelationList() {
        return gameRelationList;
    }

    public void setGameRelationList(List<GameRelation> gameRelationList) {
        this.gameRelationList = gameRelationList;
    }

    public long getGameResourceId() {
        return gameResourceId;
    }

    public void setGameResourceId(long gameResourceId) {
        this.gameResourceId = gameResourceId;
    }

    public void addGameRelation(GameRelation gameRelation) {
        this.gameRelationList.add(gameRelation);
        Collections.sort(gameRelationList);
    }

    public void addGameRelation(Collection<GameRelation> gameRelationList) {
        this.gameRelationList.addAll(gameRelationList);
        Collections.sort(this.gameRelationList);
    }

    public List<GameRelation> sortRelations() {
        Collections.sort(gameRelationList);
        return gameRelationList;
    }

    public Map<Long, GameRelation> getGameMap() {
        return gameMap;
    }

    public void setGameMap(Map<Long, GameRelation> gameMap) {
        this.gameMap = gameMap;
    }

    public Map<Long, GameRelation> getGroupMap() {
        return groupMap;
    }

    public void setGroupMap(Map<Long, GameRelation> groupMap) {
        this.groupMap = groupMap;
    }

    public Map<Long, GameRelation> getArticleMap() {
        return articleMap;
    }

    public void setArticleMap(Map<Long, GameRelation> articleMap) {
        this.articleMap = articleMap;
    }

    public GameRelation getDownLoadRelation() {
        return downLoadRelation;
    }

    public void setDownLoadRelation(GameRelation downLoadRelation) {
        this.downLoadRelation = downLoadRelation;
    }

    public GameRelation getGroupContnetRelation() {
        return groupContnetRelation;
    }

    public void setGroupContnetRelation(GameRelation groupContnetRelation) {
        this.groupContnetRelation = groupContnetRelation;
    }

    public GameRelation getTalentRelation() {
        return talentRelation;
    }

    public void setTalentRelation(GameRelation talentRelation) {
        this.talentRelation = talentRelation;
    }

    public GameRelation getContributeRelation() {
        return contributeRelation;
    }

    public void setContributeRelation(GameRelation contributeRelation) {
        this.contributeRelation = contributeRelation;
    }

    public GameRelation getHeadImageRelation() {
        return headImageRelation;
    }

    public void setHeadImageRelation(GameRelation headImageRelation) {
        this.headImageRelation = headImageRelation;
    }

    public GameRelation getMenuRelation() {
        return menuRelation;
    }

    public void setMenuRelation(GameRelation menuRelation) {
        this.menuRelation = menuRelation;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
