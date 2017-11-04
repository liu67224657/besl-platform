package com.enjoyf.webapps.tools.weblogic.dto.game;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameRelation;
import com.enjoyf.platform.service.gameres.GameRelationType;
import com.enjoyf.platform.service.gameres.GameViewLayout;
import com.enjoyf.platform.service.gameres.GameViewLayoutType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-4-11
 * Time: 下午6:13
 * To change this template use File | Settings | File Templates.
 */
public class GameRelationDTO {
    private long relationId;
    private long resourceId;
    private GameRelationType gameRelationType;
    private GameViewLayoutType layoutType;
    private String relationName;
    private String relationValue;
    private int sortNum;
    private ValidStatus validStatus = ValidStatus.VALID;

    public long getRelationId() {
        return relationId;
    }

    public void setRelationId(long relationId) {
        this.relationId = relationId;
    }

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public GameRelationType getGameRelationType() {
        return gameRelationType;
    }

    public void setGameRelationType(GameRelationType gameRelationType) {
        this.gameRelationType = gameRelationType;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
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

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public GameViewLayoutType getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(GameViewLayoutType layoutType) {
        this.layoutType = layoutType;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
