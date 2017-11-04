package com.enjoyf.platform.service.search;

import com.enjoyf.platform.service.content.ContentPublishType;
import com.enjoyf.platform.service.content.ContentType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Set;

/**
 * <p/>
 * Description:搜索条件类
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class SearchContentCriteria implements Serializable{
    private Set<String> keys;
    private boolean allTextSearch=true;
    private ContentType contentType;
    private ContentPublishType publishType;

    public Set<String> getKeys() {
        return keys;
    }

    public void setKeys(Set<String> keys) {
        this.keys = keys;
    }

    public boolean isAllTextSearch() {
        return allTextSearch;
    }

    public void setAllTextSearch(boolean allTextSearch) {
        this.allTextSearch = allTextSearch;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public ContentPublishType getPublishType() {
        return publishType;
    }

    public void setPublishType(ContentPublishType publishType) {
        this.publishType = publishType;
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
