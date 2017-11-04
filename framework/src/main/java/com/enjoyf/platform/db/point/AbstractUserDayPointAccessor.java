package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.point.UserDayPoint;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-13
 * Time: 上午10:50
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractUserDayPointAccessor extends AbstractBaseTableAccessor<UserDayPoint> implements UserDayPointAccessor {

    private Logger logger = LoggerFactory.getLogger(AbstractUserDayPointAccessor.class);

    private static final String KEY_SEQUENCE_NAME = "USER_DAY_POINT_ID";

    //组合表名
    protected static final String KEY_TABLE_NAME = "user_day_point_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";

    @Override
    public UserDayPoint insert(UserDayPoint userDayPoint, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {

            //user_day_pointid, user_no, point_value,action_type, point_date
            pstmt = conn.prepareStatement(getInsertSql(userDayPoint.getPointDate()));

            pstmt.setString(1, userDayPoint.getUserNo());
            pstmt.setInt(2, userDayPoint.getPointValue());
            pstmt.setInt(3, userDayPoint.getActionType().getCode());
            pstmt.setDate(4, new java.sql.Date(userDayPoint.getPointDate() == null ? System.currentTimeMillis() : userDayPoint.getPointDate().getTime()));
            pstmt.setString(5, userDayPoint.getProfileId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + "On insert , a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return userDayPoint;
    }

    @Override
    public UserDayPoint get(QueryExpress queryExpress, Date date, Connection conn) throws DbException {
        return super.get(getTableName(date), queryExpress, conn);
    }

    @Override
    public List<UserDayPoint> query(QueryExpress queryExpress, Date date, Connection conn) throws DbException {
        return super.query(getTableName(date), queryExpress, conn);
    }

    @Override
    public List<UserDayPoint> query(QueryExpress queryExpress, Date date, Pagination pagination, Connection conn) throws DbException {
        return super.query(getTableName(date), queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Date date, Connection conn) throws DbException {
        return super.update(getTableName(date), updateExpress, queryExpress, conn);
    }

    private String getInsertSql(Date createDate) {
        String insertSql = "INSERT INTO " + getTableName(createDate) + " (user_no, point_value,action_type, point_date,profileid) VALUES (?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("userDayPoint INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    private String getTableName(Date d) {
        return KEY_TABLE_NAME + TableUtil.getTableDateSuffix(d, KEY_TABLE_SUFFIX_FMT);
    }


    @Override
    protected UserDayPoint rsToObject(ResultSet rs) throws SQLException {

        UserDayPoint userDayPoint = new UserDayPoint();
        userDayPoint.setUserDayPointId(rs.getLong("user_day_pointid"));
        userDayPoint.setActionType(PointActionType.getByCode(rs.getInt("action_type")));
        userDayPoint.setUserNo(rs.getString("user_no"));
        userDayPoint.setPointValue(rs.getInt("point_value"));
        userDayPoint.setPointDate(rs.getDate("point_date"));
        userDayPoint.setProfileId(rs.getString("profileid"));

        return userDayPoint;
    }

}
