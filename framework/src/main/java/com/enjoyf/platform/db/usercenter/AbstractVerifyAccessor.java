package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.usercenter.Verify;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractVerifyAccessor extends AbstractBaseTableAccessor<Verify> implements VerifyAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractVerifyAccessor.class);

    private static final String KEY_TABLE_NAME = "verify";

    @Override
    public Verify insert(Verify wanbaVerify, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, wanbaVerify.getVerifyName());
            pstmt.setString(2, wanbaVerify.getValidStatus().getCode());
            pstmt.setTimestamp(3, new Timestamp(wanbaVerify.getCreateDate().getTime()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return wanbaVerify;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(verify_name,valid_status,create_date) VALUES (?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("WanbaVerify insert sql" + sql);
        }
        return sql;
    }

    @Override
    public Verify get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<Verify> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<Verify> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected Verify rsToObject(ResultSet rs) throws SQLException {

        Verify returnObject = new Verify();

        returnObject.setVerifyId(rs.getLong("verify_id"));
        returnObject.setVerifyName(rs.getString("verify_name"));
        returnObject.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        returnObject.setCreateDate(new Date(rs.getTimestamp("create_date").getTime()));


        return returnObject;
    }
}