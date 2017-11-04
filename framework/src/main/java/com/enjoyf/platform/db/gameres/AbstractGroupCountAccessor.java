package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.privilege.GroupCount;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-16
 * Time: 下午7:18
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGroupCountAccessor extends AbstractBaseTableAccessor<GroupCount> implements GroupCountAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractGroupCountAccessor.class);

    private static final String KEY_TABLE_NAME = "group_count";

    @Override
    public GroupCount insert(GroupCount groupCount, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setLong(1, groupCount.getGroupId());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                groupCount.setGroupCountId(rs.getLong(1));
            }

        } catch (SQLException e) {
            GAlerter.lab("On insert Privilege,SQLException:" + e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return groupCount;
    }

    @Override
    public GroupCount get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected GroupCount rsToObject(ResultSet rs) throws SQLException {
        GroupCount groupCount = new GroupCount();
        groupCount.setGroupCountId(rs.getLong("group_count_id"));
        groupCount.setGroupId(rs.getLong("group_id"));
        groupCount.setProfileNum(rs.getInt("profile_num"));
        groupCount.setNewProfileNum(rs.getInt("new_profile_num"));
        groupCount.setNoteNum(rs.getInt("note_num"));
        groupCount.setNewNoteNum(rs.getInt("new_note_num"));
        groupCount.setVisitNum(rs.getInt("visit_num"));
        groupCount.setNewVisitNum(rs.getInt("new_visit_num"));
        groupCount.setExtInt1(rs.getInt("extInt1"));
        groupCount.setExtInt2(rs.getInt("extInt2"));
        return groupCount;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + "(group_id) VALUES(?)";
        if (logger.isDebugEnabled()) {
            logger.debug("insert GroupCount sql:" + insertSql);
        }
        return insertSql;
    }
}
