package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-26
 * Time: 下午2:45
 * To change this template use File | Settings | File Templates.
 */
public interface AnimeTagAccessor {
	public AnimeTag insert(AnimeTag animeTag, Connection connection) throws DbException;

	public AnimeTag get(QueryExpress queryExpress, Connection conn)throws DbException;

	public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection connection) throws DbException;

	public List<AnimeTag> query(QueryExpress queryExpress, Connection conn) throws DbException;

	public List<AnimeTag> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;
}
