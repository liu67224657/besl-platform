package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.JoymeAppTopMenu;
import com.enjoyf.platform.service.joymeapp.JoymeAppTopNews;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-8-14
 * Time: 下午12:03
 * To change this template use File | Settings | File Templates.
 */
public interface AppTopNewsAccessor {
	public JoymeAppTopNews insert(JoymeAppTopNews joymeAppTopNews, Connection conn) throws DbException;

	public JoymeAppTopNews get(QueryExpress queryExpress, Connection conn) throws DbException;

	public List<JoymeAppTopNews> query(QueryExpress queryExpress, Connection conn) throws DbException;

	public int update(QueryExpress queryExpress,UpdateExpress updateExpress, Connection conn) throws DbException;

	public List<JoymeAppTopNews> query(QueryExpress queryExpress, Pagination pagination, Connection conn)throws DbException;
}
