package com.enjoyf.platform.service.search;

import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Author: zhaoxin
 * Date: 11-11-9
 * Time: 下午3:49
 * Desc:
 */
public class SearchContentEntry implements Serializable {

    //CONTENTID,UNO,CONTENTSUBJECT,CONTENTTAG,SEARCHTAG,CONTENTBODY,PUBLISHDATE,UPDATEDATE,REMOVESTATUS

    private String indexId;

    private String contentUno;
    private String contentId;

    private String directUno;
    private String directId;

    //
    private String parentUno;
    private String parentId;


    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public String getContentUno() {
        return contentUno;
    }

    public void setContentUno(String contentUno) {
        this.contentUno = contentUno;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getDirectUno() {
        return directUno;
    }

    public void setDirectUno(String directUno) {
        this.directUno = directUno;
    }

    public String getDirectId() {
        return directId;
    }

    public void setDirectId(String directId) {
        this.directId = directId;
    }

    public String getParentUno() {
        return parentUno;
    }

    public void setParentUno(String parentUno) {
        this.parentUno = parentUno;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
