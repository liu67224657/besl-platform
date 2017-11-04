package com.enjoyf.platform.db.comment;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.CommentPermission;
import com.enjoyf.platform.service.comment.CommentPermissionType;
import com.enjoyf.platform.service.usercenter.Icon;
import com.enjoyf.platform.service.usercenter.Icons;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.ProfileFlag;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.profile.ProfileDomainGenerator;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * 
 * @author huazhang
 *
 */
public abstract class AbstractProfilePermissionAccessor extends AbstractBaseTableAccessor<CommentPermission> implements CommentPermissionAccessor {

    private static final String KEY_TABLE_NAME = "comment_permission";

    private static Logger logger = LoggerFactory.getLogger(AbstractProfilePermissionAccessor.class);

    @Override
    public CommentPermission insert(CommentPermission permission, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {

            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, permission.getPermissionId());
            pstmt.setString(2, permission.getProfileId());
            pstmt.setInt(3, permission.getPermissionType().getCode());
            pstmt.setTimestamp(4, new Timestamp(permission.getCreateTime() == null ? System.currentTimeMillis() : permission.getCreateTime().getTime()));
            pstmt.setString(5, permission.getStatus().getCode());
            pstmt.setInt(6, permission.getCreateUserId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert permission, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return permission;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(permission_id,profile_id,permission_type,create_time,status,create_user_id) VALUES(?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("profile insert sql" + sql);
        }
        return sql;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public CommentPermission get(QueryExpress queryExpess, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpess, conn);
    }

    @Override
    public List<CommentPermission> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<CommentPermission> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    protected CommentPermission rsToObject(ResultSet rs) throws SQLException {

    	CommentPermission permission = new CommentPermission();

    	permission.setPermissionId(rs.getString("permission_id"));
    	permission.setProfileId(rs.getString("profile_id"));
    	permission.setPermissionType(CommentPermissionType.getByCode(rs.getInt("permission_type")));
        permission.setStatus(ValidStatus.getByCode(rs.getString("status")));
        permission.setCreateTime(rs.getTimestamp("create_time"));
        permission.setCreateUserId(rs.getInt("create_user_id"));

        return permission;
    }

}
