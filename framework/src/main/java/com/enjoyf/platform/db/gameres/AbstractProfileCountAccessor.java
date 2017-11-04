package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.privilege.ProfileCount;
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
 * Date: 13-10-16
 * Time: 下午7:44
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractProfileCountAccessor extends AbstractBaseTableAccessor<ProfileCount> implements ProfileCountAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractGroupCountAccessor.class);

    private static final String KEY_TABLE_NAME = "profile_count";

    @Override
    public ProfileCount insert(ProfileCount profileCount, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setLong(1, profileCount.getGroupId());
            pstmt.setString(2, profileCount.getUno());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                profileCount.setProfileCountId(rs.getLong(1));
            }

        } catch (SQLException e) {
            GAlerter.lab("On insert Privilege,SQLException:" + e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return profileCount;
    }

    @Override
    public ProfileCount get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ProfileCount> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected ProfileCount rsToObject(ResultSet rs) throws SQLException {
        ProfileCount profileCount = new ProfileCount();
        profileCount.setProfileCountId(rs.getLong("profile_count_id"));
        profileCount.setGroupId(rs.getLong("group_id"));
        profileCount.setUno(rs.getString("uno"));
        profileCount.setPostNum(rs.getInt("post_num"));
        profileCount.setNewPostNum(rs.getInt("new_post_num"));
        profileCount.setReplyNum(rs.getInt("reply_num"));
        profileCount.setNewReplyNum(rs.getInt("new_reply_num"));
        profileCount.setDeleteNum(rs.getInt("delete_num"));
        profileCount.setNewDeleteNum(rs.getInt("new_delete_num"));
        profileCount.setExtInt1(rs.getInt("extInt1"));
        profileCount.setExtInt2(rs.getInt("extInt2"));
        return profileCount;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + "(group_id,uno) VALUES(?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("insert GroupCount sql:" + insertSql);
        }
        return insertSql;
    }
}
