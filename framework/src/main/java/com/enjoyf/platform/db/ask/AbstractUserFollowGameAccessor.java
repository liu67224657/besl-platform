package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.UserFollowGame;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractUserFollowGameAccessor extends AbstractBaseTableAccessor<UserFollowGame> implements UserFollowGameAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractUserFollowGameAccessor.class);

    private static final String KEY_TABLE_NAME = "user_follow_game";

    @Override
    public UserFollowGame insert(UserFollowGame userFollowGame, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, userFollowGame.getGameId());
            pstmt.setString(2, userFollowGame.getProfileId());
            pstmt.setTimestamp(3, new Timestamp(userFollowGame.getCreateTime().getTime()));
            pstmt.setString(4, userFollowGame.getValidStatus().getCode());
            pstmt.setTimestamp(5, new Timestamp(new Date().getTime()));
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                userFollowGame.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return userFollowGame;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(game_id,profile_id,create_time,valid_status,modify_time) VALUES (?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("UserFollowGame insert sql" + sql);
        }
        return sql;
    }

    @Override
    public UserFollowGame get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<UserFollowGame> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<UserFollowGame> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected UserFollowGame rsToObject(ResultSet rs) throws SQLException {

        UserFollowGame returnObject = new UserFollowGame();

        returnObject.setId(rs.getLong("id"));
        returnObject.setGameId(rs.getLong("game_id"));
        returnObject.setProfileId(rs.getString("profile_id"));
        returnObject.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        returnObject.setCreateTime(new Date(rs.getTimestamp("create_time").getTime()));
        returnObject.setModifyTime(new Date(rs.getTimestamp("modify_time").getTime()));


        return returnObject;
    }
}