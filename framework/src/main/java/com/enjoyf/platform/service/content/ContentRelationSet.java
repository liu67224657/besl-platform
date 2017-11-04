package com.enjoyf.platform.service.content;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 文章关系实体类
 */
public class ContentRelationSet implements Serializable {
    private Map<String,ContentRelation> gameRelationMap=new HashMap<String, ContentRelation>();
    private ContentRelation groupRelation;
    private ContentRelation groupPointRelation;



    public Map<String, ContentRelation> getGameRelationMap() {
        return gameRelationMap;
    }

    public void setGameRelationMap(Map<String, ContentRelation> gameRelationMap) {
        this.gameRelationMap = gameRelationMap;
    }

    public ContentRelation getGroupRelation() {
        return groupRelation;
    }

    public void setGroupRelation(ContentRelation groupRelation) {
        this.groupRelation = groupRelation;
    }

    public ContentRelation getGroupPointRelation() {
        return groupPointRelation;
    }

    public void setGroupPointRelation(ContentRelation groupPointRelation) {
        this.groupPointRelation = groupPointRelation;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
