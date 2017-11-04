package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTV;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTVDomain;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTvIsNewType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-26
 * Time: 下午2:46
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAnimeTVAccessor extends AbstractBaseTableAccessor<AnimeTV> implements AnimeTVAccessor {
	private static final Logger logger = LoggerFactory.getLogger(AbstractAppTipsAccessor.class);
	private static final String KEY_TABLE_NAME = "anime_tv";

	@Override
	public AnimeTV insert(AnimeTV animeTV, Connection conn) throws DbException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(getSql(), Statement.RETURN_GENERATED_KEYS);

			//domain,domain_param,url,m3u8,tv_pic,tv_name,tags,play_num,favorite_num,remove_status,update_date,create_date,create_user
			pstmt.setInt(1, animeTV.getDomain() == null ? 0 : animeTV.getDomain().getCode());
			pstmt.setString(2, StringUtil.isEmpty(animeTV.getDomain_param()) ? "" : animeTV.getDomain_param());
			pstmt.setString(3, StringUtil.isEmpty(animeTV.getUrl()) ? "" : animeTV.getUrl());
			pstmt.setString(4, StringUtil.isEmpty(animeTV.getM3u8()) ? "" : animeTV.getM3u8());
			pstmt.setString(5, StringUtil.isEmpty(animeTV.getTv_pic()) ? "" : animeTV.getTv_pic());
			pstmt.setString(6, animeTV.getTv_name());
			pstmt.setString(7, animeTV.getTags());
			pstmt.setLong(8, animeTV.getPlay_num() == null ? 0 : animeTV.getPlay_num());
			//
			pstmt.setLong(9, animeTV.getFavorite_num() == null ? 0 : animeTV.getFavorite_num());
			pstmt.setString(10, animeTV.getRemove_status().getCode());
			pstmt.setTimestamp(11, new Timestamp(animeTV.getCreate_date() == null ? System.currentTimeMillis() : animeTV.getCreate_date().getTime()));
			pstmt.setTimestamp(12, animeTV.getUpdate_date() == null ? null : new Timestamp(animeTV.getUpdate_date().getTime()));

			pstmt.setString(13, StringUtil.isEmpty(animeTV.getCreate_user()) ? "" : animeTV.getCreate_user());

			pstmt.setString(14, StringUtil.isEmpty(animeTV.getSpace()) ? "" : animeTV.getSpace());
			pstmt.setInt(15, animeTV.getAnimeTvIsNewType().getCode());
			pstmt.setInt(16, animeTV.getTv_number() == null ? 0 : animeTV.getTv_number());
			pstmt.setLong(17, animeTV.getDisplay_order() == null ? 0 : animeTV.getDisplay_order());

			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				animeTV.setTv_id(rs.getLong(1));
			}

		} catch (SQLException e) {
			GAlerter.lab(this.getClass().getName() + " insert occur Exception.e:", e);
			throw new DbException(e);
		} finally {
			DataBaseUtil.closeResultSet(rs);
			DataBaseUtil.closeStatment(pstmt);
		}
		return animeTV;  //To change body of implemented methods use File | Settings | File Templates.
	}

	private String getSql() {
		String sql = "INSERT INTO " + KEY_TABLE_NAME + "(domain,domain_param,url,m3u8," +
				"tv_pic,tv_name,tags,play_num,favorite_num,remove_status,update_date,create_date,create_user,space,isnew,tv_number,display_order) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if (logger.isDebugEnabled()) {
			logger.debug(this.getClass().getName() + " insert sql:" + sql);
		}
		return sql;
	}

	@Override
	public AnimeTV get(QueryExpress queryExpress, Connection conn) throws DbException {
		return super.get(KEY_TABLE_NAME, queryExpress, conn);
	}

	@Override
	public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
		return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
	}

	@Override
	public List<AnimeTV> query(QueryExpress queryExpress, Connection conn) throws DbException {
		return super.query(KEY_TABLE_NAME, queryExpress, conn);
	}

	@Override
	public List<AnimeTV> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
		return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
	}

	@Override
	protected AnimeTV rsToObject(ResultSet rs) throws SQLException {
		AnimeTV animeTV = new AnimeTV();

		animeTV.setTv_id(rs.getLong("tv_id"));
		animeTV.setDomain(AnimeTVDomain.getByCode(rs.getInt("domain")));
		animeTV.setDomain_param(rs.getString("domain_param"));
		animeTV.setUrl(rs.getString("url"));
		animeTV.setM3u8(rs.getString("m3u8"));
		animeTV.setTv_pic(rs.getString("tv_pic"));
		animeTV.setTv_name(rs.getString("tv_name"));
		animeTV.setTags(rs.getString("tags"));
		animeTV.setPlay_num(rs.getLong("play_num"));
		animeTV.setFavorite_num(rs.getLong("favorite_num"));
		animeTV.setRemove_status(ValidStatus.getByCode(rs.getString("remove_status")));
		animeTV.setCreate_date(rs.getTimestamp("create_date"));
		animeTV.setUpdate_date(rs.getTimestamp("update_date"));
		animeTV.setCreate_user(rs.getString("create_user"));

		animeTV.setSpace(rs.getString("space"));
		animeTV.setAnimeTvIsNewType(AnimeTvIsNewType.getByCode(rs.getInt("isnew")));
		animeTV.setTv_number(rs.getInt("tv_number"));

		animeTV.setDisplay_order(rs.getLong("display_order"));
		return animeTV;
	}
}
