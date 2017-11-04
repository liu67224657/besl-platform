package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ask.BlackListHistory;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractBlackListHistoryAccessor extends AbstractBaseTableAccessor<BlackListHistory> implements BlackListHistoryAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractBlackListHistoryAccessor.class);

    private static final String KEY_TABLE_NAME = "black_list_history";

    @Override
    public BlackListHistory insert(BlackListHistory blackListHistory, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setTimestamp(1, new Timestamp(blackListHistory.getStartTime().getTime()));
            pstmt.setTimestamp(2, new Timestamp(blackListHistory.getEndTime().getTime()));
            pstmt.setString(3, blackListHistory.getProfileId());
            pstmt.setString(4, blackListHistory.getReason());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                blackListHistory.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return blackListHistory;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(start_time,end_time,profile_id,reason) VALUES (?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("BlackListHistory insert sql" + sql);
        }
        return sql;
    }

    @Override
    public BlackListHistory get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<BlackListHistory> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<BlackListHistory> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected BlackListHistory rsToObject(ResultSet rs) throws SQLException {

        BlackListHistory returnObject = new BlackListHistory();

        returnObject.setId(rs.getLong("id"));
        returnObject.setStartTime(new Date(rs.getTimestamp("start_time").getTime()));
        returnObject.setEndTime(new Date(rs.getTimestamp("end_time").getTime()));
        returnObject.setProfileId(rs.getString("profile_id"));
        returnObject.setReason(rs.getString("reason"));


        return returnObject;
    }
}