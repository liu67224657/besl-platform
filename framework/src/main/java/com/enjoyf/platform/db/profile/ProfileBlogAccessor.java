package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: zhaoxin
 * Date: 11-8-26
 * Time: 下午4:13
 * Desc:
 */
interface ProfileBlogAccessor {
    //insert
    public ProfileBlog insert(ProfileBlog profileBlog, Connection conn) throws DbException;

    //update
    public boolean update(ProfileBlog profileBlog, Connection conn) throws DbException;

    public boolean update(String uno, Map<ObjectField, Object> keyValues, Connection conn) throws DbException;

    //get by uno
    public ProfileBlog getByUno(String uno, Connection conn) throws DbException;

    public List<ProfileBlog> queryBlogByDateStep(Date startDate, Date endDate, Pagination page, Connection conn) throws DbException;

    //get by screen name
    public ProfileBlog getByScreenName(String screenName, Connection conn) throws DbException;

    //query by screen name
    public Map<String, ProfileBlog> queryProfileBlogsByLikeScreenName(String screenName, Connection conn) throws DbException;

    //get by screen names
    public Map<String, ProfileBlog> queryByScreenNameMaps(Set<String> screenNames, Connection conn) throws DbException;

    //get by domain
    public ProfileBlog getByDomain(String domain, Connection conn) throws DbException;

        //get by domain
    public ProfileBlog get(QueryExpress queryExpress, Connection conn) throws DbException;

    //search by key word
    public List<ProfileBlog> search(String keyWord, Pagination page, Connection conn) throws DbException;


    //tools    -- query view
//    public List<ViewAccountProfile> queryViewAccountProfileBlogListByAccount(ProfileQueryParam param, Pagination page, Connection conn) throws DbException;
//
//    public List<ViewAccountProfile> queryViewAccountProfileBlogListByBlog(ProfileQueryParam param, Pagination page, Connection conn) throws DbException;

    //common update
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    //common query
    public List<ProfileBlog> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

}
