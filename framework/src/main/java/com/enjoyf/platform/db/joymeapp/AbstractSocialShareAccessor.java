package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.joymeapp.AppShareChannel;
import com.enjoyf.platform.service.joymeapp.SocialShare;
import com.enjoyf.platform.service.joymeapp.SocialShareType;
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
 * Date: 14-8-21
 * Time: 下午2:00
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSocialShareAccessor extends AbstractBaseTableAccessor<SocialShare> implements SocialShareAccessor {

	private Logger logger = LoggerFactory.getLogger(AbstractSocialShareAccessor.class);

	private static final String TABLE_NAME = "social_share_template";

	@Override
	public SocialShare insert(SocialShare socialShare, Connection connection) throws DbException {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			//activityid,share_type,platform,sharedomain,title,body,pic_url,remove_status,create_user,create_time,update_time
			pstmt = connection.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setLong(1, socialShare.getActivityid() == null ? -1l : socialShare.getActivityid());
			pstmt.setLong(2, socialShare.getShare_type().getCode());
			pstmt.setString(3, socialShare.getAppkey());
			pstmt.setInt(4, socialShare.getPlatform());
			pstmt.setString(5, socialShare.getSharedomain().getCode());
			pstmt.setString(6, socialShare.getTitle());
			pstmt.setString(7, socialShare.getBody());
			pstmt.setString(8, socialShare.getUrl());
			pstmt.setString(9, socialShare.getPic_url());
			pstmt.setString(10, socialShare.getRemove_status().getCode());
			pstmt.setString(11, socialShare.getCreate_user());
			pstmt.setTimestamp(12, new Timestamp(socialShare.getCreate_time() == null ? System.currentTimeMillis() : socialShare.getCreate_time().getTime()));
			pstmt.setTimestamp(13, new Timestamp(socialShare.getUpdate_time_flag() == null ? System.currentTimeMillis() : socialShare.getUpdate_time_flag().getTime()));
			pstmt.executeUpdate();

			resultSet = pstmt.getGeneratedKeys();
			if (resultSet.next()) {
				socialShare.setShare_id(resultSet.getLong(1));
			}

		} catch (Exception e) {
			GAlerter.lab(this.getClass().getName() + " occur SQLException.e", e);
		} finally {
			DataBaseUtil.closeResultSet(resultSet);
			DataBaseUtil.closeStatment(pstmt);
		}
		return socialShare;
	}

	@Override
	public SocialShare get(QueryExpress queryExpress, Connection conn) throws DbException {
		return super.get(TABLE_NAME, queryExpress, conn);
	}

	@Override
	public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection connection) throws DbException {
		return super.update(TABLE_NAME, updateExpress, queryExpress, connection);
	}

	@Override
	public List<SocialShare> query(QueryExpress queryExpress, Connection conn) throws DbException {
		return super.query(TABLE_NAME, queryExpress, conn);
	}

	@Override
	public List<SocialShare> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
		return query(TABLE_NAME, queryExpress, page, conn);
	}

	private String getInsertSql() {
		String sql = "INSERT INTO " + TABLE_NAME + "(activityid,share_type,appkey,platform,sharedomain,title,body,url,pic_url,remove_status,create_user,create_time,update_time_flag)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

		if (logger.isDebugEnabled()) {
			logger.debug("the insert sql script:" + sql);
		}
		return sql;
	}

	@Override
	protected SocialShare rsToObject(ResultSet rs) throws SQLException {
		SocialShare socialShare = new SocialShare();
		socialShare.setShare_id(rs.getLong("share_id"));
		socialShare.setActivityid(rs.getLong("activityid"));
		socialShare.setShare_type(SocialShareType.getByCode(rs.getInt("share_type")));
		socialShare.setAppkey(rs.getString("appkey"));
		socialShare.setPlatform(rs.getInt("platform"));
		socialShare.setSharedomain(AppShareChannel.getByCode(rs.getString("sharedomain")));
		socialShare.setTitle(rs.getString("title"));
		socialShare.setBody(rs.getString("body"));
		socialShare.setPic_url(rs.getString("pic_url"));
		socialShare.setUrl(rs.getString("url"));
		socialShare.setRemove_status(ActStatus.getByCode(rs.getString("remove_status")));
		socialShare.setCreate_user(rs.getString("create_user"));
		socialShare.setCreate_time(rs.getTimestamp("create_time"));
		socialShare.setUpdate_time_flag(rs.getTimestamp("update_time_flag"));
		return socialShare;
	}
}
