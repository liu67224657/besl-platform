package com.enjoyf.platform.service.search;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: zhaoxin
 * Date: 11-11-9
 * Time: 下午3:49
 * Desc: 博客索引
 */
public class SearchBlogIndexEntry implements Serializable {

    public static final String ID = "id";
    public static final String UNO = "uno";
    public static final String USERID = "userid";
    public static final String NAME = "name";
    public static final String DOMAIN = "domain";
    public static final String DESC = "desc";
    public static final String CREATEDATE = "createdate";
    public static final String UPDATEDATE = "updatedate";

    //UNO,USERID,SCREENNAME,DOMAINNAME,BLOGDESCRIPTION,CREATEDATE,UPDATEDATE
    private String indexId;
    private String uno;
    private String userId;
    private String screenName;
    private String domainName;
    private String blogDescription;
    private Date createDate;
    private Date updateDate;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getBlogDescription() {
        return blogDescription;
    }

    public void setBlogDescription(String blogDescription) {
        this.blogDescription = blogDescription;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
