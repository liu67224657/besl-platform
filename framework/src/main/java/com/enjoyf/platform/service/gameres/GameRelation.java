package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * <p/>
 * Description:游戏关系 sort字段越小在list中越在前面
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class GameRelation implements Serializable, Comparable<GameRelation> {
    private long relationId;
    private long resourceId;
    private GameRelationType gameRelationType;
     private String relationName;
    private String relationValue;
    private int sortNum;
    private ValidStatus validStatus=ValidStatus.VALID;

    public long getRelationId() {
        return relationId;
    }

    public void setRelationId(long relationId) {
        this.relationId = relationId;
    }

    public GameRelationType getGameRelationType() {
        return gameRelationType;
    }

    public void setGameRelationType(GameRelationType gameRelationType) {
        this.gameRelationType = gameRelationType;
    }

    public String getRelationValue() {
        return relationValue;
    }

    public void setRelationValue(String relationValue) {
        this.relationValue = relationValue;
    }

    public int getSortNum() {
        return sortNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int compareTo(GameRelation o) {
        return this.getSortNum() < o.getSortNum() ? -1 : (this.getSortNum() == o.getSortNum() ? 0 : 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameRelation that = (GameRelation) o;

        if (relationId != that.relationId) return false;
        if (resourceId != that.resourceId) return false;
        if (sortNum != that.sortNum) return false;
        if (gameRelationType != null ? !gameRelationType.equals(that.gameRelationType) : that.gameRelationType != null)
            return false;
        if (relationValue != null ? !relationValue.equals(that.relationValue) : that.relationValue != null)
            return false;
        if (validStatus != null ? !validStatus.equals(that.validStatus) : that.validStatus != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (relationId ^ (relationId >>> 32));
        result = 31 * result + (int) (resourceId ^ (resourceId >>> 32));
        result = 31 * result + (gameRelationType != null ? gameRelationType.hashCode() : 0);
        result = 31 * result + (relationValue != null ? relationValue.hashCode() : 0);
        result = 31 * result + sortNum;
        result = 31 * result + (validStatus != null ? validStatus.hashCode() : 0);
        return result;
    }
}
