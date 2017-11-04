package com.enjoyf.platform.service.search;

import com.enjoyf.platform.service.content.ContentPublishType;
import com.enjoyf.platform.service.content.ContentType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Author: zhaoxin
 * Date: 11-11-9
 * Time: 下午3:49
 * Desc:
 */
public class SearchProfileCriteria implements Serializable{
    private Map<String,String> keys;
    private boolean allTextSearch=false;

    public Map<String,String> getKeys() {
        return keys;
    }

    public void setKeys(Map<String,String> keys) {
        this.keys = keys;
    }

    public boolean isAllTextSearch() {
        return allTextSearch;
    }

    public void setAllTextSearch(boolean allTextSearch) {
        this.allTextSearch = allTextSearch;
    }



    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
