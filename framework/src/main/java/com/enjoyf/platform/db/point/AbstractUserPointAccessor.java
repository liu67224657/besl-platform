package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.UserPoint;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: liangtang
 * Date: 13-5-29
 * Time: 下午2:17
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractUserPointAccessor extends AbstractBaseTableAccessor<UserPoint> implements UserPointAccessor {

    private Logger logger = LoggerFactory.getLogger(AbstractUserPointAccessor.class);

    //    private static final String KEY_SEQUENCE_NAME = "USER_POINT_ID";
    protected static final String KEY_TABLE_NAME = "user_point";


    @Override
    public UserPoint insert(UserPoint userPoint, Connection conn) throws DbException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, userPoint.getUserNo());
            pstmt.setInt(2, userPoint.getUserPoint());
            pstmt.setString(3, userPoint.getProfileId());
            pstmt.setString(4, userPoint.getPointKey());
            pstmt.setInt(5, userPoint.getPrestige());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                userPoint.setUserPointId(rs.getLong(1));
            }

        } catch (SQLException e) {
            GAlerter.lab("On insert AppErrorInfo, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return userPoint;
    }

    @Override
    public UserPoint get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<UserPoint> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected UserPoint rsToObject(ResultSet rs) throws SQLException {
        UserPoint userPoint = new UserPoint();
        userPoint.setUserPointId(rs.getLong("user_point_id"));
        userPoint.setUserNo(rs.getString("user_no"));
        userPoint.setUserPoint(rs.getInt("user_point"));
        userPoint.setConsumeAmount(rs.getInt("consume_amount"));
        userPoint.setConsumeExchange(rs.getInt("consume_exchange"));
        userPoint.setExtInt1(rs.getInt("extint1"));
        userPoint.setExtInt2(rs.getInt("extint2"));
        userPoint.setProfileId(rs.getString("profileid"));
        userPoint.setPointKey(rs.getString("pointkey"));
        userPoint.setPrestige(rs.getInt("prestige"));
        return userPoint;
    }

    public String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(user_no,user_point,profileid,pointkey,prestige) VALUES(?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("userPoint insert sql:" + sql);
        }
        return sql;
    }
}
