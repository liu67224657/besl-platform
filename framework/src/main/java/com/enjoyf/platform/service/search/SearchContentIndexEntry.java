package com.enjoyf.platform.service.search;

import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentPublishType;
import com.enjoyf.platform.service.content.ContentType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: zhaoxin
 * Date: 11-11-9
 * Time: 下午3:49
 * Desc:
 */
public class SearchContentIndexEntry implements Serializable {

    public static final String ID = "id";
    public static final String UNO = "uno";
    public static final String CONTENTID = "contentid";
    public static final String PARENTUNO = "parentuno";
    public static final String PARENTID = "parentid";
    public static final String DIRECTUNO = "directuno";
    public static final String DIRECTID = "directid";
    public static final String TYPE = "type";
    public static final String PUBLISHTYPE = "publishtype";
    public static final String SUBJECT = "subject";
    public static final String BODY = "body";
    public static final String TAG = "tag";
    public static final String STAG = "stag";
    public static final String CREATEDATE = "createdate";
    public static final String UPDATEDATE = "updatedate";

     //CONTENTID,UNO,CONTENTSUBJECT,CONTENTTAG,SEARCHTAG,CONTENTBODY,PUBLISHDATE,UPDATEDATE,REMOVESTATUS

    private String indexId;

    private String contentUno;
    private String contentId;

    private String contentSubject;

    private String contentTag;
    private String searchTag;

    private String contentBody;

   //private Integer contentType;
    private ContentType contentType;
    private ContentPublishType publishType = ContentPublishType.ORIGINAL;


    //转发时为原文ID。
    private String rootContentId;
    private String rootContentUno;

    //parent content info.
    private String parentContentId;
    private String parentContentUno;

    private Date publishDate;
    private Date updateDate;


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

    public String getContentSubject() {
        return contentSubject;
    }

    public void setContentSubject(String contentSubject) {
        this.contentSubject = contentSubject;
    }

    public String getContentTag() {
        return contentTag;
    }

    public void setContentTag(String contentTag) {
        this.contentTag = contentTag;
    }

    public String getSearchTag() {
        return searchTag;
    }

    public void setSearchTag(String searchTag) {
        this.searchTag = searchTag;
    }

    public String getContentBody() {
        return contentBody;
    }

    public void setContentBody(String contentBody) {
        this.contentBody = contentBody;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getRootContentId() {
        return rootContentId;
    }

    public void setRootContentId(String rootContentId) {
        this.rootContentId = rootContentId;
    }

    public String getRootContentUno() {
        return rootContentUno;
    }

    public void setRootContentUno(String rootContentUno) {
        this.rootContentUno = rootContentUno;
    }

    public String getParentContentId() {
        return parentContentId;
    }

    public void setParentContentId(String parentContentId) {
        this.parentContentId = parentContentId;
    }

    public String getParentContentUno() {
        return parentContentUno;
    }

    public void setParentContentUno(String parentContentUno) {
        this.parentContentUno = parentContentUno;
    }


    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }


    public ContentPublishType getPublishType() {
        return publishType;
    }

    public void setPublishType(ContentPublishType publishType) {
        this.publishType = publishType;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
