package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.usercenter.ModifyTimeJson;
import com.enjoyf.platform.service.usercenter.ProfileSum;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/10
 * Description:
 */
public abstract class AbstractProfileSumAccessor extends AbstractBaseTableAccessor<ProfileSum> implements ProfileSumAccessor {

	private static final String KEY_TABLE_NAME = "profilesum";

	private static Logger logger = LoggerFactory.getLogger(AbstractProfileSumAccessor.class);

	@Override
	public ProfileSum insert(ProfileSum sum, Connection conn) throws DbException {
		PreparedStatement pstmt = null;
		try {
			//picurl,remove_status,createtime,createip
			pstmt = conn.prepareStatement(getInsertSql());
			pstmt.setString(1, sum.getProfileId());
			pstmt.setInt(2, sum.getFollowSum());
			pstmt.setInt(3, sum.getFansSum());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.error("On insert profilesum, a SQLException occured.", e);
			throw new DbException(e);
		} finally {
			DataBaseUtil.closeStatment(pstmt);
		}

		return sum;
	}


	@Override
	public ProfileSum get(QueryExpress queryExpress, Connection conn) throws DbException {
		return super.get(KEY_TABLE_NAME, queryExpress, conn);
	}

	@Override
	public List<ProfileSum> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
		return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
	}


	@Override
	public List<ProfileSum> query(QueryExpress queryExpress, Connection conn) throws DbException {
		return super.query(KEY_TABLE_NAME, queryExpress, conn);
	}


	@Override
	public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
		return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
	}

	private String getInsertSql() {
		String sql = "INSERT INTO " + KEY_TABLE_NAME + "(profileid,followsum,fanssum) VALUES(?,?,?)";
		if (logger.isDebugEnabled()) {
			logger.debug("profile insert sql" + sql);
		}
		return sql;
	}

	@Override
	protected ProfileSum rsToObject(ResultSet rs) throws SQLException {
		ProfileSum sum = new ProfileSum();

		sum.setProfileId(rs.getString("profileid"));
        sum.setFollowSum(rs.getInt("followsum"));
        sum.setFansSum(rs.getInt("fanssum"));
		return sum;
	}
}
