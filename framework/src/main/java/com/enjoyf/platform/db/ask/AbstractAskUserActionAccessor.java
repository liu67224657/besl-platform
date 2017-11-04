package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.ask.AskUserAction;
import com.enjoyf.platform.service.ask.AskUserActionType;
import com.enjoyf.platform.service.ask.AskUtil;
import com.enjoyf.platform.service.ask.ItemType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractAskUserActionAccessor extends AbstractBaseTableAccessor<AskUserAction> implements AskUserActionAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAskUserActionAccessor.class);

    private static final String KEY_TABLE_NAME = "ask_user_action";

    @Override
    public AskUserAction insert(AskUserAction askUserAction, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            askUserAction.setAskUserActionId(AskUtil.getAskUserActionId(askUserAction.getProfileId(), askUserAction.getDestId(), askUserAction.getItemType(), askUserAction.getActionType()));
            pstmt.setString(1, askUserAction.getAskUserActionId());
            pstmt.setString(2, askUserAction.getProfileId());
            pstmt.setInt(3, askUserAction.getItemType().getCode());
            pstmt.setLong(4, askUserAction.getDestId());
            pstmt.setInt(5, askUserAction.getActionType().getCode());
            pstmt.setTimestamp(6, new Timestamp(askUserAction.getCreateTime().getTime()));
            pstmt.setString(7, askUserAction.getValue());

            pstmt.executeUpdate();

            return askUserAction;
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(ask_user_action_id,profile_id,item_type,dest_id,action_type,create_time,action_value) VALUES (?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AskUserAction insert sql" + sql);
        }
        return sql;
    }

    @Override
    public AskUserAction get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<AskUserAction> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<AskUserAction> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected AskUserAction rsToObject(ResultSet rs) throws SQLException {
        AskUserAction returnObject = new AskUserAction();
        returnObject.setAskUserActionId(rs.getString("ask_user_action_id"));
        returnObject.setProfileId(rs.getString("profile_id"));
        returnObject.setItemType(ItemType.getByCode(rs.getInt("item_type")));
        returnObject.setDestId(rs.getLong("dest_id"));
        returnObject.setActionType(AskUserActionType.getByCode(rs.getInt("action_type")));
        returnObject.setCreateTime(new Date(rs.getTimestamp("create_time").getTime()));
        returnObject.setValue(rs.getString("action_value"));
        return returnObject;
    }
}