package com.enjoyf.platform.service.search;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Author: zhaoxin
 * Date: 11-11-9
 * Time: 下午3:49
 * Desc: 搜索全部内容的返回类
 */
public class SearchResultEntry implements Serializable {

    private SearchBlogResultEntry searchBlogResultEntry;
    private SearchContentEntry searchContentEntry;

    public SearchBlogResultEntry getSearchBlogResultEntry() {
        return searchBlogResultEntry;
    }

    public void setSearchBlogResultEntry(SearchBlogResultEntry searchBlogResultEntry) {
        this.searchBlogResultEntry = searchBlogResultEntry;
    }

    public SearchContentEntry getSearchContentEntry() {
        return searchContentEntry;
    }

    public void setSearchContentEntry(SearchContentEntry searchContentEntry) {
        this.searchContentEntry = searchContentEntry;
    }
}
