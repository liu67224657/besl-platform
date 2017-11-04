package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.event.Activity;
import com.enjoyf.platform.service.event.ActivityAwardType;
import com.enjoyf.platform.service.event.ActivityLimit;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-5
 * Time: 下午5:27
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractActivityAccessor extends AbstractBaseTableAccessor<Activity> implements ActivityAccessor{

    private Logger logger= LoggerFactory.getLogger(AbstractActivityAccessor.class);

    private static final String KEY_TABLE_NAME="activity";

    @Override
    public Activity insert(Activity activity, Connection conn) throws DbException {
        //name, description, startdate, enddate, validstatus, createtime, createip, createuserid, limit, award_type, award_id
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, activity.getName());
            pstmt.setString(2, activity.getDescription());
            if(activity.getStartDate()!=null){
                pstmt.setTimestamp(3,new Timestamp(activity.getStartDate().getTime()));
            }else{
                pstmt.setNull(3, Types.TIMESTAMP);
            }
            if(activity.getEndDate()!=null){
                pstmt.setTimestamp(4,new Timestamp(activity.getEndDate().getTime()));
            }else{
                pstmt.setNull(4, Types.TIMESTAMP);
            }
            pstmt.setString(5, activity.getValidStatus().getCode());
            pstmt.setTimestamp(6, new Timestamp(activity.getCreateDate()!=null?activity.getCreateDate().getTime():System.currentTimeMillis()));
            pstmt.setString(7, activity.getCreateIp());
            pstmt.setString(8, activity.getCreateUserId());
            pstmt.setInt(9, activity.getLimit().getCode());
            pstmt.setInt(10,activity.getAwardType().getCode());
            pstmt.setLong(11,activity.getAwardId());
            pstmt.setLong(12,activity.getCount());
            pstmt.setLong(13,activity.getRestamount());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                activity.setActivityId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert activity,SQLException:" + e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }


        return activity;
    }

    @Override
    protected Activity rsToObject(ResultSet rs) throws SQLException {
        Activity activity=new Activity();
        activity.setActivityId(rs.getLong("activityid"));
        activity.setName(rs.getString("name"));
        activity.setName(rs.getString("description"));
        activity.setStartDate(rs.getTimestamp("startdate"));
        activity.setEndDate(rs.getTimestamp("enddate"));
        activity.setValidStatus(ValidStatus.getByCode(rs.getString("validstatus")));
        activity.setCreateDate(rs.getTimestamp("createtime"));
        activity.setCreateIp(rs.getString("createip"));
        activity.setCreateDate(rs.getTimestamp("createuserid"));
        activity.setLimit(ActivityLimit.getByCode(rs.getInt("activity_limit")));
        activity.setAwardType(ActivityAwardType.getByCode(rs.getInt("award_type")));
        activity.setAwardId(rs.getLong("award_id"));
        activity.setCount(rs.getInt("award_count"));
        activity.setRestamount(rs.getInt("award_restamount"));

        return activity;
    }

    @Override
    public Activity get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME,queryExpress,conn);
    }

    @Override
    public List<Activity> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME,queryExpress,pagination,conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME,updateExpress,queryExpress,conn);  
    }

    private String getInsertSql(){
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (name, description, startdate, enddate, validstatus, createtime, createip, createuserid, activity_limit, award_type, award_id,award_count,award_restamount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("Activity INSERT Script:" + insertSql);
        }

        return insertSql;
    }
}
