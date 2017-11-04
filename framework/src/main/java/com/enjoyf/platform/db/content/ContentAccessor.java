/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ContentTag;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentPublishType;
import com.enjoyf.platform.service.content.ContentQueryParam;
import com.enjoyf.platform.service.content.ContentType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a> ,zx
 */
interface ContentAccessor {
    //insert
    public Content insert(Content content, Connection conn) throws DbException;

    //remove
    public boolean remove(String contentId, Connection conn) throws DbException;

    //更新内容中的数，收藏数、转发数、回复数等。
    public boolean updateContentNum(String contentId, ObjectField field, Integer value, Connection conn) throws DbException;

    //更新博文内容
    public boolean updateContent(String contentId, Map<ObjectField, Object> map, Connection conn) throws DbException;

    //更新博客
    public int updateContnet(String uno, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    //查询单条记录，根据ID，UNO
    public Content getByCidUno(String contentId, Connection conn) throws DbException;

    // 查询一批博文
    public List<Content> queryContents(Set<String> contentIds, Connection conn) throws DbException;

    // 查询某人的博文
    public List<Content> queryContents(String uno, Pagination page, Connection conn) throws DbException;

    //根据最近更新时间段查询博文---分页
    public List<Content> queryContentsByTimeStep(Date startDate, Date endDate, Pagination page, Connection conn) throws DbException;

    //query some type contents
    public List<Content> queryLastestContents(ContentPublishType publishType, ContentType contentType, Integer size, Connection conn) throws DbException;

    //query some body's lastest content
    public List<Content> queryLastestContents(String uno, Integer size, Connection conn) throws DbException;

    // query content by tools app
    public List<Content> queryContentByParam(ContentQueryParam param, Pagination p, Connection conn) throws DbException;

    // query by queryExpress
    public List<Content> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

}
