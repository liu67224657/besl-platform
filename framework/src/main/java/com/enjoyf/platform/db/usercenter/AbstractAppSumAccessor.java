package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.usercenter.AppSum;
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

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/4/10
 * Description:
 */
public abstract class AbstractAppSumAccessor extends AbstractBaseTableAccessor<AppSum> implements AppSumAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAppSumAccessor.class);

    private static final String KEY_TABLE_NAME = "app_sum";

    @Override
    public AppSum insert(AppSum appSum, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {

            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, appSum.getAppSumId());
            pstmt.setLong(2, appSum.getActivitySum());
            pstmt.setLong(3, appSum.getActivityLogSum());
            pstmt.setString(4, appSum.getAppKey());
            pstmt.setString(5, appSum.getSubKey());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return appSum;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(app_sum_id,activity_usersum,activity_logsum,appkey,subkey) VALUES(?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AppSum insert sql" + sql);
        }
        return sql;
    }

    @Override
    public AppSum get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<AppSum> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<AppSum> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }


    @Override
    protected AppSum rsToObject(ResultSet rs) throws SQLException {

        AppSum appSum = new AppSum();
        appSum.setAppSumId(rs.getString("app_sum_id"));
        appSum.setActivitySum(rs.getInt("activity_usersum"));
        appSum.setAppKey(rs.getString("appkey"));
        appSum.setSubKey(rs.getString("subkey"));
        appSum.setActivitySum(rs.getInt("activity_logsum"));


        return appSum;
    }
}
