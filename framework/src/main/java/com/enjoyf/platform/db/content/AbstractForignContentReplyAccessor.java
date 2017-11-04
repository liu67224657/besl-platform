package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ForignContentReply;
import com.enjoyf.platform.service.content.ForignContentReplyField;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-15 下午4:50
 * Description:
 */
public abstract class AbstractForignContentReplyAccessor extends AbstractBaseTableAccessor<ForignContentReply> implements ForignContentReplyAccessor {
	//
	private static final Logger logger = LoggerFactory.getLogger(AbstractForignContentReplyAccessor.class);

	private static final String TABLE_NAME = "forign_content_reply";

	@Override
	public ForignContentReply insert(ForignContentReply contentReply, Connection conn) throws DbException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

			//content_id,parent_id,parent_uno, root_id,root_uno,reply_num,ding_num,body,pic,create_time,create_ip,remove_status,replyuno
			pstmt.setLong(1, contentReply.getContentId());
			pstmt.setLong(2, contentReply.getPartentId());
			pstmt.setString(3, contentReply.getParentUno());
			pstmt.setLong(4, contentReply.getRootId());
			pstmt.setString(5, contentReply.getRootUno());
			pstmt.setInt(6, contentReply.getReplyNum());
			pstmt.setInt(7, contentReply.getAgreeNum());

			pstmt.setString(8, contentReply.getBody());
			pstmt.setString(9, contentReply.getPic());

			pstmt.setTimestamp(10, new Timestamp(contentReply.getCreateTime() == null ? System.currentTimeMillis() : contentReply.getCreateTime().getTime()));
			pstmt.setString(11, contentReply.getCreateIp());

			pstmt.setString(12, contentReply.getRemoveStatus().getCode());
			pstmt.setString(13, contentReply.getReplyUno());
			pstmt.setString(14, contentReply.getContentLink());

			pstmt.setInt(15, contentReply.getFloorNum());
			pstmt.setString(16, contentReply.getKeyWords());


			pstmt.setDouble(17, contentReply.getScore());
			pstmt.setLong(18, contentReply.getDisplay_order());
			pstmt.executeUpdate();

			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				contentReply.setReplyId(rs.getLong(1));
			}
		} catch (SQLException e) {
			GAlerter.lab("On insert Content, a SQLException occured.", e);
			throw new DbException(e);
		} finally {
			DataBaseUtil.closeResultSet(rs);
			DataBaseUtil.closeStatment(pstmt);
		}

		return contentReply;
	}

	@Override
	protected ForignContentReply rsToObject(ResultSet rs) throws SQLException {
		ForignContentReply contentReply = new ForignContentReply();

		contentReply.setReplyId(rs.getLong("reply_id"));
		contentReply.setReplyUno(rs.getString("reply_uno"));
		contentReply.setContentId(rs.getLong("content_id"));
		contentReply.setPartentId(rs.getLong("parent_id"));
		contentReply.setParentUno(rs.getString("parent_uno"));
		contentReply.setRootId(rs.getLong("root_id"));
		contentReply.setRootUno(rs.getString("root_uno"));
		contentReply.setReplyNum(rs.getInt("reply_num"));
		contentReply.setAgreeNum(rs.getInt("agree_num"));
		contentReply.setDisagreeNum(rs.getInt("disagree_num"));
		contentReply.setBody(rs.getString("body"));
		contentReply.setPic(rs.getString("pic"));
		contentReply.setCreateTime(rs.getTimestamp("create_time"));
		contentReply.setCreateIp(rs.getString("create_ip"));
		contentReply.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));
		contentReply.setContentLink(rs.getString("content_link"));
		contentReply.setFloorNum(rs.getInt("floor_num"));
		contentReply.setTotalRows(rs.getInt("total_rows"));
		contentReply.setKeyWords(rs.getString("key_words"));
		contentReply.setScore(rs.getDouble("score"));
		contentReply.setDisplay_order(rs.getLong("display_order"));
		return contentReply;
	}

	@Override
	public ForignContentReply get(QueryExpress getExpress, Connection connection) throws DbException {
		return super.get(TABLE_NAME, getExpress, connection);
	}

	@Override
	public List<ForignContentReply> queryByRootId(long contentId, long rootId, Pagination page, Connection conn) throws DbException {

		//
		List<ForignContentReply> returnValue = new ArrayList<ForignContentReply>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE content_id=? AND root_id= ? ORDER BY reply_id ASC LIMIT ?, ?";
		if (logger.isDebugEnabled()) {
			logger.debug("The query sql:" + sql);
		}

		try {
			//
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, contentId);
			pstmt.setLong(2, rootId);
			pstmt.setInt(3, page.getStartRowIdx());
			pstmt.setInt(4, page.getPageSize());

			rs = pstmt.executeQuery();

			while (rs.next()) {
				returnValue.add(rsToObject(rs));
			}
		} catch (SQLException e) {
			GAlerter.lab("On query, a SQLException occured:", e);

			throw new DbException(DbException.SQL_GENERIC, e);
		} finally {
			DataBaseUtil.closeResultSet(rs);
			DataBaseUtil.closeStatment(pstmt);
		}

		return returnValue;
	}

	@Override
	public List<ForignContentReply> query(QueryExpress queryExpress, Connection conn) throws DbException {
		return super.query(TABLE_NAME, queryExpress, conn);
	}

	@Override
	public List<ForignContentReply> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
		return super.query(TABLE_NAME, queryExpress, pagination, conn);
	}


	@Override
	public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection connection) throws DbException {
		return super.update(TABLE_NAME, updateExpress, queryExpress, connection);
	}

	@Override
	public List<Long> queryHotReplyId(long contentId, int size, Connection conn) throws DbException {
		List<Long> returnValue = new ArrayList<Long>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT reply_id,SUM(reply_num+agree_num) as c from " + TABLE_NAME + " WHERE content_id=? AND root_id=? AND remove_status= ?   group by reply_id ORDER by c DESC LIMIT 0,?";
		if (logger.isDebugEnabled()) {
			logger.debug("The queryHotReplyId sql:" + sql);
		}

		try {
			//
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, contentId);
			pstmt.setLong(2, 0);
			pstmt.setString(3, ActStatus.UNACT.getCode());
			pstmt.setInt(4, size);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				returnValue.add(rs.getLong("reply_id"));
			}
		} catch (SQLException e) {
			GAlerter.lab("On query, a SQLException occured:", e);

			throw new DbException(DbException.SQL_GENERIC, e);
		} finally {
			DataBaseUtil.closeResultSet(rs);
			DataBaseUtil.closeStatment(pstmt);
		}

		return returnValue;
	}


	public String getInsertSql() {
		String sql = "INSERT INTO forign_content_reply (content_id,parent_id,parent_uno,root_id,root_uno,reply_num,agree_num,body,pic,create_time,create_ip,remove_status,reply_uno,content_link,floor_num,key_words,score,display_order) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		if (logger.isDebugEnabled()) {
			logger.debug(" the insert sql:" + sql);
		}

		return sql;
	}

	@Override
	public ForignContentReply getById(long replyId, Connection conn) throws DbException {
		return super.get(TABLE_NAME, new QueryExpress().add(QueryCriterions.eq(ForignContentReplyField.REPLY_ID, replyId)), conn);
	}

	@Override
	public int getMaxFollorNum(long rootId, long contentId, Connection conn) throws DbException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT MAX(floor_num) FROM " + TABLE_NAME + " WHERE root_id=? AND content_id=?";
		if (logger.isDebugEnabled()) {
			logger.debug("The get sql:" + sql);
		}

		try {
			//
			pstmt = conn.prepareStatement(sql);

			//
			pstmt.setLong(1, rootId);
			pstmt.setLong(2, contentId);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			GAlerter.lab("On get, a SQLException occured:", e);

			throw new DbException(DbException.SQL_GENERIC, e);
		} finally {
			DataBaseUtil.closeResultSet(rs);
			DataBaseUtil.closeStatment(pstmt);
		}

		return 0;
	}
}
