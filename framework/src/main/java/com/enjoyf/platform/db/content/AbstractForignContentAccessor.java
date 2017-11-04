package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ForignContent;
import com.enjoyf.platform.service.content.ForignContentDomain;
import com.enjoyf.platform.service.content.ForignContentField;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-15 下午4:50
 * Description:
 */
public abstract class AbstractForignContentAccessor extends AbstractBaseTableAccessor<ForignContent> implements ForignContentAccessor {

	private Logger logger = LoggerFactory.getLogger(AbstractForignContentAccessor.class);

	private static final String TABLE_NAME = "forign_content";


	@Override
	public ForignContent insert(ForignContent forignContent, Connection conn) throws DbException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

			//content_url,content_domain,reply_sum,content_title,content_desc,remove_status
			pstmt.setString(1, forignContent.getContentUrl());
			pstmt.setInt(2, forignContent.getContentDomain().getCode());
			pstmt.setInt(3, forignContent.getReplyNum());
			pstmt.setString(4, forignContent.getContentTitle());
			pstmt.setString(5, forignContent.getContentDesc());

			pstmt.setString(6, forignContent.getRemoveStatus().getCode());
			pstmt.setTimestamp(7, new Timestamp(forignContent.getCreateTime() == null ? System.currentTimeMillis() : forignContent.getCreateTime().getTime()));
			pstmt.setString(8, forignContent.getForignId());
			pstmt.setString(9, forignContent.getKeyWords());

			//long_comment_num,average_score,display_order
			pstmt.setInt(10, forignContent.getLong_comment_num());
			pstmt.setDouble(11, forignContent.getAverage_score());
			pstmt.setLong(12, forignContent.getDisplay_order());
			pstmt.setInt(13, forignContent.getScorereply_num());

			pstmt.executeUpdate();

			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				forignContent.setContentId(rs.getLong(1));
			}
		} catch (SQLException e) {
			GAlerter.lab("On insert Content, a SQLException occured.", e);
			throw new DbException(e);
		} finally {
			DataBaseUtil.closeResultSet(rs);
			DataBaseUtil.closeStatment(pstmt);
		}

		return forignContent;
	}

	private String getInsertSql() {
		String sql = "INSERT INTO forign_content(content_url,content_domain,reply_num,content_title,content_desc,remove_status,create_time,forign_id,key_words,long_comment_num,average_score,display_order,scorereply_num)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

		if (logger.isDebugEnabled()) {
			logger.debug("the insert sql script:" + sql);
		}
		return sql;
	}

	@Override
	protected ForignContent rsToObject(ResultSet rs) throws SQLException {
		ForignContent forignContent = new ForignContent();

		forignContent.setContentId(rs.getLong("content_id"));
		forignContent.setContentUrl(rs.getString("content_url"));
		forignContent.setContentDomain(ForignContentDomain.getByCode(rs.getInt("content_domain")));
		forignContent.setReplyNum(rs.getInt("reply_num"));
		forignContent.setContentTitle(rs.getString("content_title"));
		forignContent.setContentDesc(rs.getString("content_desc"));
		forignContent.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));
		forignContent.setCreateTime(rs.getTimestamp("create_time"));
		forignContent.setForignId(rs.getString("forign_id"));
		forignContent.setTotalRows(rs.getInt("total_rows"));
		forignContent.setKeyWords(rs.getString("key_words"));

		//long_comment_num,average_score,display_order
		forignContent.setLong_comment_num(rs.getInt("long_comment_num"));
		forignContent.setAverage_score(rs.getDouble("average_score"));
		forignContent.setDisplay_order(rs.getLong("display_order"));
		forignContent.setScorereply_num(rs.getInt("scorereply_num"));
		return forignContent;
	}

	@Override
	public ForignContent getByContentId(long contentId, Connection connection) throws DbException {
		return super.get(TABLE_NAME, new QueryExpress().add(QueryCriterions.eq(ForignContentField.CONTENT_ID, contentId)), connection);
	}

	@Override
	public ForignContent getByFroignId(String fid, ForignContentDomain domain, Connection connection) throws DbException {
		return super.get(TABLE_NAME, new QueryExpress()
				.add(QueryCriterions.eq(ForignContentField.FORIGN_ID, fid))
				.add(QueryCriterions.eq(ForignContentField.CONTENT_DOMAIN, domain.getCode())), connection);
	}

	@Override
	public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection connection) throws DbException {
		return super.update(TABLE_NAME, updateExpress, queryExpress, connection);
	}

	@Override
	public List<ForignContent> query(QueryExpress queryExpress, Connection conn) throws DbException {
		return super.query(TABLE_NAME, queryExpress, conn);
	}

    @Override
    public List<ForignContent> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, pagination, conn);
    }

}
