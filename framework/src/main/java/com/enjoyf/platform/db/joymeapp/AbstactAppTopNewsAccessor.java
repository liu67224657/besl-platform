package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.JoymeAppTopNews;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-8-14
 * Time: 下午12:02
 * To change this template use File | Settings | File Templates.
 */
public class AbstactAppTopNewsAccessor extends AbstractSequenceBaseTableAccessor<JoymeAppTopNews> implements AppTopNewsAccessor {
	private static final Logger logger = LoggerFactory.getLogger(AbstractContentVersionInfoAccessor.class);


	protected static final String KEY_TABLE_NAME = "app_top_news";

	@Override
	public JoymeAppTopNews insert(JoymeAppTopNews joymeAppTopNews, Connection conn) throws DbException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			//joymeAppTopNews.setMenuId(getSeqNo(KEY_SEQUENCE_NAME, conn));

			//appkey,title,url,createdate,modifydate
			pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, joymeAppTopNews.getAppkey());
			pstmt.setString(2, joymeAppTopNews.getTitle());
			pstmt.setString(3, joymeAppTopNews.getUrl());
			pstmt.setString(4, joymeAppTopNews.getCreate_userid());
			pstmt.setTimestamp(5, new Timestamp(joymeAppTopNews.getCreatedate() == null ? System.currentTimeMillis() : joymeAppTopNews.getCreatedate().getTime()));
			pstmt.setTimestamp(6, new Timestamp(joymeAppTopNews.getModifydate() == null ? System.currentTimeMillis() : joymeAppTopNews.getModifydate().getTime()));
			pstmt.executeUpdate();

			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				long topnewsid = rs.getLong(1);
				joymeAppTopNews.setTop_news_id(topnewsid);
			}
		} catch (SQLException e) {
			GAlerter.lab("On insert JoymeAppTopNews, a SQLException occured.", e);
			throw new DbException(e);
		} finally {
			DataBaseUtil.closeStatment(pstmt);
		}
		return joymeAppTopNews;
	}

	private String getInsertSql() {
		String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (appkey,title,url,create_userid,createdate,modifydate) VALUES (?, ?, ?, ?,?,?)";

		if (logger.isDebugEnabled()) {
			logger.debug("JoymeAppTopNews INSERT Script:" + insertSql);
		}

		return insertSql;
	}

	@Override
	public JoymeAppTopNews get(QueryExpress queryExpress, Connection conn) throws DbException {
		return super.get(KEY_TABLE_NAME, queryExpress, conn);
	}

	@Override
	public List<JoymeAppTopNews> query(QueryExpress queryExpress, Connection conn) throws DbException {
		return super.query(KEY_TABLE_NAME, queryExpress, conn);
	}

	@Override
	public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
		return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
	}

	@Override
	public List<JoymeAppTopNews> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
		return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
	}

	@Override
	protected JoymeAppTopNews rsToObject(ResultSet rs) throws SQLException {
		JoymeAppTopNews returnObj = new JoymeAppTopNews();
		returnObj.setTop_news_id(rs.getLong("top_news_id"));
		returnObj.setAppkey(rs.getString("appkey"));
		returnObj.setTitle(rs.getString("title"));
		returnObj.setUrl(rs.getString("url"));
		returnObj.setCreate_userid(rs.getString("create_userid"));
		returnObj.setCreatedate(rs.getTimestamp("createdate"));
		returnObj.setModifydate(rs.getTimestamp("modifydate"));
		returnObj.setRemovestatus(ActStatus.getByCode(rs.getString("removestatus")));
		return returnObj;
	}
}
