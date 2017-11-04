package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.HistoryActionType;
import com.enjoyf.platform.service.point.PointActionHistory;
import com.enjoyf.platform.service.point.PointActionType;
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
 * User: zhitaoshi
 * Date: 13-6-7
 * Time: 下午7:51
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractPointActionHistoryAccessor extends AbstractBaseTableAccessor<PointActionHistory> implements PointActionHistoryAccessor {
    private Logger logger = LoggerFactory.getLogger(AbstractPointActionHistoryAccessor.class);

    protected static final String KEY_TABLE_NAME = "point_action_history";

    @Override
    public PointActionHistory insert(PointActionHistory actionHistory, Connection conn) throws DbException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            //pstmt setting
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, actionHistory.getUserNo());
            pstmt.setInt(2, actionHistory.getActionType().getCode());
            pstmt.setString(3, actionHistory.getActionDescription());
            pstmt.setTimestamp(4, new Timestamp(actionHistory.getCreateDate() == null ? System.currentTimeMillis() : actionHistory.getCreateDate().getTime()));
            pstmt.setDate(5, new Date(actionHistory.getActionDate() == null ? System.currentTimeMillis() : actionHistory.getActionDate().getTime()));
            pstmt.setInt(6, actionHistory.getPointValue());
            pstmt.setString(7, actionHistory.getDestId());
            pstmt.setString(8, actionHistory.getDestUno());
            pstmt.setString(9, actionHistory.getProfileId());
            pstmt.setInt(10, actionHistory.getPrestige());
            pstmt.setInt(11, actionHistory.getHistoryActionType() == null ? HistoryActionType.POINT.getCode() : actionHistory.getHistoryActionType().getCode());
            pstmt.setString(12, actionHistory.getAppkey());
            pstmt.setString(13, actionHistory.getPointkey());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                actionHistory.setActionHistoryId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + "On insert , a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return actionHistory;
    }

    @Override
    public PointActionHistory get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<PointActionHistory> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public List<PointActionHistory> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected PointActionHistory rsToObject(ResultSet rs) throws SQLException {
        PointActionHistory actionHistory = new PointActionHistory();
        actionHistory.setActionHistoryId(rs.getLong("action_history_id"));
        actionHistory.setUserNo(rs.getString("user_no"));
        actionHistory.setActionType(PointActionType.getByCode(rs.getInt("action_type")));
        actionHistory.setActionDescription(rs.getString("action_description"));
        if (rs.getTimestamp("action_timestamp") != null) {
            actionHistory.setCreateDate(new Date(rs.getTimestamp("action_timestamp").getTime()));
        }
        if (rs.getDate("action_time") != null) {
            actionHistory.setActionDate(rs.getDate("action_time"));
        }
        actionHistory.setPointValue(rs.getInt("point_value"));
        actionHistory.setDestId(rs.getString("destid"));
        actionHistory.setDestUno(rs.getString("destuno"));

        actionHistory.setProfileId(rs.getString("profileid"));
        actionHistory.setPrestige(rs.getInt("prestige"));
        actionHistory.setHistoryActionType(HistoryActionType.getByCode(rs.getInt("history_type")));
        actionHistory.setAppkey(rs.getString("appkey"));
        actionHistory.setPointkey(rs.getString("pointkey"));
        return actionHistory;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(user_no,action_type,action_description,action_timestamp,action_time,point_value,destid,destuno,profileid,prestige,history_type,appkey,pointkey) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("PointActionHistory insert sql:" + sql);
        }

        return sql;

    }
}
