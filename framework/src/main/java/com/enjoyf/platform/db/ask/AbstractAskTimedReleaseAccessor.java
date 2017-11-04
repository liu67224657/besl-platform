package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.AskTimedRelease;
import com.enjoyf.platform.service.ask.TimeRelseaseType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractAskTimedReleaseAccessor extends AbstractBaseTableAccessor<AskTimedRelease> implements AskTimedReleaseAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAskTimedReleaseAccessor.class);

    private static final String KEY_TABLE_NAME = "ask_timed_release";

    @Override
    public AskTimedRelease insert(AskTimedRelease askTimedRelease, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, askTimedRelease.getTimeRelseaseType().getCode());
            pstmt.setString(2, askTimedRelease.getTitle());
            pstmt.setString(3, askTimedRelease.getValidStatus().getCode());
            pstmt.setTimestamp(4, new Timestamp(askTimedRelease.getCreateTime().getTime()));
            pstmt.setTimestamp(5, new Timestamp(askTimedRelease.getReleaseTime().getTime()));
            pstmt.setInt(6, askTimedRelease.getReleaseTimes());
            pstmt.setString(7, askTimedRelease.getExtStr());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                askTimedRelease.setTimeid(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return askTimedRelease;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(time_relsease_type,title,valid_status,create_time,release_time,release_times,ext_str) VALUES (?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AskTimedRelease insert sql" + sql);
        }
        return sql;
    }

    @Override
    public AskTimedRelease get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<AskTimedRelease> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<AskTimedRelease> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected AskTimedRelease rsToObject(ResultSet rs) throws SQLException {

        AskTimedRelease returnObject = new AskTimedRelease();

        returnObject.setTimeid(rs.getLong("timeid"));
        returnObject.setTimeRelseaseType(TimeRelseaseType.getByCode(rs.getInt("time_relsease_type")));
        returnObject.setTitle(rs.getString("title"));
        returnObject.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        returnObject.setCreateTime(new Date(rs.getTimestamp("create_time").getTime()));
        returnObject.setReleaseTime(new Date(rs.getTimestamp("release_time").getTime()));
        returnObject.setReleaseTimes(rs.getInt("release_times"));
        returnObject.setExtStr(rs.getString("ext_str"));


        return returnObject;
    }
}