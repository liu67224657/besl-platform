package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTV;
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
public interface AnimeTVAccessor {
	public AnimeTV insert(AnimeTV animeTV, Connection connection) throws DbException;

	public AnimeTV get(QueryExpress queryExpress,Connection conn)throws DbException;

	public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection connection) throws DbException;

	public List<AnimeTV> query(QueryExpress queryExpress, Connection conn) throws DbException;

	public List<AnimeTV> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;
}
