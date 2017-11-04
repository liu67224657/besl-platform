package com.enjoyf.platform.service.search;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Author: zhaoxin
 * Date: 11-11-9
 * Time: 下午3:49
 * Desc:
 */
public class SearchBlogResultEntry implements Serializable {

    //CONTENTID,UNO,CONTENTSUBJECT,CONTENTTAG,SEARCHTAG,CONTENTBODY,PUBLISHDATE,UPDATEDATE,REMOVESTATUS

    private String indexId;

    private String uno;

    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
