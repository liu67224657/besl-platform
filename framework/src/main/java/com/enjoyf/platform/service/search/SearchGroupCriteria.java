package com.enjoyf.platform.service.search;

import com.enjoyf.platform.service.gameres.ResourceDomain;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Set;

/**
 * Author: zhaoxin
 * Date: 11-11-9
 * Time: 下午3:49
 * Desc:
 */
public class SearchGroupCriteria implements Serializable{
    private Set<String> keys;
//    private ResourceDomain resourceDomain;
    private boolean allTextSearch=false;

    public Set<String> getKeys() {
        return keys;
    }

    public void setKeys(Set<String> keys) {
        this.keys = keys;
    }

    //todo
//    public ResourceDomain getResourceDomain() {
//        return resourceDomain;
//    }
//
//    public void setResourceDomain(ResourceDomain resourceDomain) {
//        this.resourceDomain = resourceDomain;
//    }

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
