package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.GroupUser;
import com.enjoyf.platform.service.gameres.GroupUserField;
import com.enjoyf.platform.service.gameres.GroupValidStatus;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-21
 * Time: 下午1:58
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGroupUserAccessor extends AbstractBaseTableAccessor<GroupUser> implements GroupUserAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractGroupUserAccessor.class);

    private static final String KEY_TABLE = "group_user";


    @Override
    public GroupUser insert(GroupUser groupUser, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            //group_id,uno,valid_status,
            // create_time,create_ip,valid_userid,valid_uno,
            // last_post_contentid,last_post_date

            pstmt.setLong(1, groupUser.getGroupId());
            pstmt.setString(2, groupUser.getUno());
            pstmt.setInt(3, groupUser.getValidStatus().getCode());
            pstmt.setTimestamp(4, new Timestamp(groupUser.getCreateTime() != null ? groupUser.getCreateTime().getTime() : System.currentTimeMillis()));
            pstmt.setString(5, groupUser.getCreateIp());
            pstmt.setString(6, groupUser.getValidUserid());
            pstmt.setString(7, groupUser.getValidUno());
            if (groupUser.getValidTime() != null) {
                pstmt.setTimestamp(8, new Timestamp(groupUser.getValidTime().getTime()));
            } else {
                pstmt.setNull(8, Types.TIMESTAMP);
            }
            pstmt.setString(9, groupUser.getCreateReason());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                groupUser.setGroupId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return groupUser;
    }


    @Override
    public GroupUser getByGroupIdUno(String uno, long groupid, Connection conn) throws DbException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(GroupUserField.UNO, uno));
        queryExpress.add(QueryCriterions.eq(GroupUserField.GROUP_ID, groupid));
        return super.get(KEY_TABLE, queryExpress, conn);
    }

    @Override
    public List<GroupUser> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE, queryExpress, pagination, conn);
    }

    @Override
    public List<GroupUser> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE, updateExpress, queryExpress, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE, queryExpress, conn);
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE + "(group_id,uno,valid_status,create_time,create_ip,valid_userid,valid_uno,valid_time,create_reason) VALUES(?, ?, ? ,?, ?, ?, ?, ?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("GroupUser getInsertSql:" + sql);
        }

        return sql;
    }


    @Override
    protected GroupUser rsToObject(ResultSet rs) throws SQLException {
        GroupUser groupUser = new GroupUser();

        groupUser.setGroupUserId(rs.getLong("group_user_id"));

        groupUser.setGroupId(rs.getLong("group_id"));
        groupUser.setUno(rs.getString("uno"));
        groupUser.setValidStatus(GroupValidStatus.getByCode(rs.getInt("valid_status")));

        groupUser.setValidTime(rs.getTimestamp("valid_time"));
        groupUser.setValidUno(rs.getString("valid_uno"));
        groupUser.setValidUserid(rs.getString("valid_userid"));

        groupUser.setCreateIp(rs.getString("create_ip"));
        groupUser.setCreateTime(rs.getTimestamp("create_time"));
        groupUser.setCreateReason(rs.getString("create_reason"));

        groupUser.setLastContentId(rs.getString("last_post_contentid"));
        groupUser.setLastContentDate(rs.getTimestamp("last_post_date"));

        groupUser.setLastReplyContentId(rs.getString("last_reply_contentid"));
        groupUser.setLastReplyId(rs.getString("last_replyid"));
        groupUser.setLastReplyTime(rs.getTimestamp("last_reply_time"));
        return groupUser;
    }
}
