package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.profile.socialclient.SocialProfileBlog;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-7-17
 * Time: 下午6:52
 * To change this template use File | Settings | File Templates.
 */
public class SocialProfileBlogAccessorMySql extends AbstractSocialProfileBlogAccessor {
	private static final Logger logger = LoggerFactory.getLogger(SocialProfileBlogAccessorMySql.class);

	@Override
	public List<SocialProfileBlog> search(String keyWord, Pagination page, Connection conn) throws DbException {
		List<SocialProfileBlog> returnValue = new ArrayList<SocialProfileBlog>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM " + getTableName("")
				+ " WHERE (SCREENNAME LIKE ? OR BLOGDESCRIPTION LIKE ?) LIMIT ?, ?";
		if(logger.isDebugEnabled()){
			logger.debug("SocialProfileBlog search : " + sql);
		}

		try {
			page.setTotalRows(queryRowSize(keyWord, conn));

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, "%" + keyWord + "%");
			pstmt.setString(2, "%" + keyWord + "%");

			pstmt.setInt(3, page.getStartRowIdx());
			pstmt.setInt(4, page.getPageSize());

			rs = pstmt.executeQuery();
			while (rs.next()) {
				returnValue.add(rsToObject(rs));
			}
		} catch (SQLException e) {
			GAlerter.lab("SocialProfileBlog On search, a SQLException occured.", e);
			throw new DbException(e);
		} finally {
			DataBaseUtil.closeStatment(pstmt);
		}

		return returnValue;
	}

	@Override
	public List<SocialProfileBlog> queryBlogByDateStep(Date startDate, Date endDate, Pagination page, Connection conn) throws DbException {
		List<SocialProfileBlog> returnValue = new ArrayList<SocialProfileBlog>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM " + getTableName("")
				+ " WHERE (UPDATEDATE BETWEEN ? AND ?) LIMIT ?, ?";
		if(logger.isDebugEnabled()){
			logger.debug("SocialProfileBlog queryBlogByDateStep : " + sql);
		}
		try {
			page.setTotalRows(queryRowSizeByDateStep(startDate, endDate, conn));

			pstmt = conn.prepareStatement(sql);

			pstmt.setTimestamp(1, new java.sql.Timestamp(startDate.getTime()));
			pstmt.setTimestamp(2, new java.sql.Timestamp(endDate.getTime()));
			pstmt.setInt(3, page.getStartRowIdx());
			pstmt.setInt(4, page.getPageSize());

			rs = pstmt.executeQuery();
			while (rs.next()) {
				returnValue.add(rsToObject(rs));
			}
		} catch (SQLException e) {
			GAlerter.lab("SocialProfileBlog On queryBlogByDateStep, a SQLException occured.", e);
			throw new DbException(e);
		} finally {
			DataBaseUtil.closeResultSet(rs);
			DataBaseUtil.closeStatment(pstmt);
		}

		return returnValue;
	}

	private int queryRowSize(String keyWord, Connection conn) throws DbException {
		int size = 0;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT COUNT(1) FROM " + getTableName("") + " WHERE (SCREENNAME LIKE ? OR BLOGDESCRIPTION LIKE ?)";

		if(logger.isDebugEnabled()){
			logger.debug("SocialProfileBlog queryRowSize:" + sql);
		}

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, "%" + keyWord + "%");
			pstmt.setString(2, "%" + keyWord + "%");

			rs = pstmt.executeQuery();

			if (rs.next()) {
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
			GAlerter.lab("SocialProfileBlog On queryRowSize, a SQLException occured.", e);
			throw new DbException(e);
		} finally {
			DataBaseUtil.closeResultSet(rs);
			DataBaseUtil.closeStatment(pstmt);
		}

		return size;
	}

	private int queryRowSizeByDateStep(Date startDate, Date endDate, Connection conn) throws DbException {
		int size = 0;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT COUNT(1) FROM " + getTableName("") + " WHERE UPDATEDATE BETWEEN ? AND ?";

		if(logger.isDebugEnabled()){
			logger.debug("SocialProfileBlog queryRowSizeByDateStep ; " + sql);
		}

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setTimestamp(1, new java.sql.Timestamp(startDate.getTime()));
			pstmt.setTimestamp(2, new java.sql.Timestamp(endDate.getTime()));

			rs = pstmt.executeQuery();

			if (rs.next()) {
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
			GAlerter.lab("SocialProfileBlog On queryRowSizeByDateStep, a SQLException occured.", e);
			throw new DbException(e);
		} finally {
			DataBaseUtil.closeResultSet(rs);
			DataBaseUtil.closeStatment(pstmt);
		}

		return size;
	}


	@Override
	public List<SocialProfileBlog> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
		return query(getTableName(""), queryExpress, page, conn);
	}
}
