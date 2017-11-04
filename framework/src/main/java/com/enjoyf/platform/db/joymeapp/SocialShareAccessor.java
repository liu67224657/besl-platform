package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.SocialShare;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-8-21
 * Time: 下午2:01
 * To change this template use File | Settings | File Templates.
 */
public interface SocialShareAccessor {
	public SocialShare insert(SocialShare socialShare, Connection connection) throws DbException;

	public SocialShare get(QueryExpress queryExpress,Connection conn)throws DbException;

	public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection connection) throws DbException;

	public List<SocialShare> query(QueryExpress queryExpress, Connection conn) throws DbException;

	public List<SocialShare> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;
}
