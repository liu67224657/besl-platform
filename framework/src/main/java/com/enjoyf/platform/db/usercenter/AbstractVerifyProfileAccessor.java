package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractVerifyProfileAccessor extends AbstractBaseTableAccessor<VerifyProfile> implements VerifyProfileAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractVerifyProfileAccessor.class);

    private static final String KEY_TABLE_NAME = "verify_profile";

    @Override
    public VerifyProfile insert(VerifyProfile wanbaProfile, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, wanbaProfile.getProfileId());
            pstmt.setString(2, wanbaProfile.getDescription());
            pstmt.setLong(3, wanbaProfile.getVerifyType());
            pstmt.setInt(4, wanbaProfile.getAskPoint());
            pstmt.setString(5, wanbaProfile.getNick());
            pstmt.setString(6, wanbaProfile.getAppkey());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return wanbaProfile;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(profile_id,description,verify_type,ask_point,nick,appkey) VALUES (?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("WanbaProfile insert sql" + sql);
        }
        return sql;
    }

    @Override
    public VerifyProfile get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<VerifyProfile> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<VerifyProfile> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected VerifyProfile rsToObject(ResultSet rs) throws SQLException {
        VerifyProfile returnObject = new VerifyProfile();

        returnObject.setProfileId(rs.getString("profile_id"));
        returnObject.setDescription(rs.getString("description"));
        returnObject.setVerifyType(rs.getLong("verify_type"));
        returnObject.setAskPoint(rs.getInt("ask_point"));
        returnObject.setNick(rs.getString("nick"));
        returnObject.setAppkey(rs.getString("appkey"));
        return returnObject;
    }
}