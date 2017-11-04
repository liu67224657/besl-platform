package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.profile.socialclient.SocialProfileBlog;
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
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-7-17
 * Time: 下午6:46
 * To change this template use File | Settings | File Templates.
 */
public interface SocialProfileBlogAccessor {
	//insert
	public SocialProfileBlog insert(SocialProfileBlog profileBlog, Connection conn) throws DbException;

	//update
	public boolean update(SocialProfileBlog profileBlog, Connection conn) throws DbException;

	public boolean update(String uno, Map<ObjectField, Object> keyValues, Connection conn) throws DbException;

	//get by uno
	public SocialProfileBlog getByUno(String uno, Connection conn) throws DbException;

	public List<SocialProfileBlog> queryBlogByDateStep(Date startDate, Date endDate, Pagination page, Connection conn) throws DbException;

	//get by screen name
	public SocialProfileBlog getByScreenName(String screenName, Connection conn) throws DbException;

	//query by screen name
	public Map<String, SocialProfileBlog> queryProfileBlogsByLikeScreenName(String screenName, Connection conn) throws DbException;

	//get by screen names
	public Map<String, SocialProfileBlog> queryByScreenNameMaps(Set<String> screenNames, Connection conn) throws DbException;

	//get by domain
	public SocialProfileBlog getByDomain(String domain, Connection conn) throws DbException;

	//get by domain
	public SocialProfileBlog get(QueryExpress queryExpress, Connection conn) throws DbException;

	//search by key word
	public List<SocialProfileBlog> search(String keyWord, Pagination page, Connection conn) throws DbException;


	//tools    -- query view
//    public List<ViewAccountProfile> queryViewAccountProfileBlogListByAccount(ProfileQueryParam param, Pagination page, Connection conn) throws DbException;
//
//    public List<ViewAccountProfile> queryViewAccountProfileBlogListByBlog(ProfileQueryParam param, Pagination page, Connection conn) throws DbException;

	//common update
	public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

	//common query
	public List<SocialProfileBlog> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;


}
