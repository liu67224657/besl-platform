package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.privilege.GroupProfile;
import com.enjoyf.platform.service.gameres.privilege.GroupProfileStatus;
import com.enjoyf.platform.service.gameres.privilege.RoleLevel;
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
 * Date: 13-10-15
 * Time: 下午1:59
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGroupProfileAccessor extends AbstractBaseTableAccessor<GroupProfile> implements GroupProfileAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractGroupPrivilegeAccessor.class);

    private static final String KEY_TABLE_NAME = "group_profile";

    @Override
    public GroupProfile insert(GroupProfile groupProfile, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setLong(1, groupProfile.getGroupId());
            pstmt.setString(2, groupProfile.getUno());
            pstmt.setInt(3, groupProfile.getRoleLevel().getCode());
            pstmt.setInt(4, groupProfile.getStatus() == null ? GroupProfileStatus.DEFAULT.getCode() : groupProfile.getStatus().getCode());
            pstmt.setTimestamp(5, new Timestamp(groupProfile.getCreateDate() == null ? System.currentTimeMillis() : groupProfile.getCreateDate().getTime()));
            pstmt.setString(6, groupProfile.getCreateIp());
            pstmt.setString(7, groupProfile.getCreateUno());
            pstmt.setInt(8, groupProfile.getExtInt1());
            pstmt.setInt(9, groupProfile.getExtInt2());
            pstmt.setString(10, groupProfile.getExtString1());
            pstmt.setString(11, groupProfile.getExtString2());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert groupProfile,SQLException:" + e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return groupProfile;
    }

    @Override
    public GroupProfile get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<GroupProfile> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<GroupProfile> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected GroupProfile rsToObject(ResultSet rs) throws SQLException {
        GroupProfile groupProfile = new GroupProfile();
        groupProfile.setGroupProfileId(rs.getLong("group_profile_id"));
        groupProfile.setGroupId(rs.getLong("group_id"));
        groupProfile.setUno(rs.getString("uno"));
        groupProfile.setRoleLevel(RoleLevel.getByCode(rs.getInt("profile_type")));
        groupProfile.setStatus(GroupProfileStatus.getByCode(rs.getInt("status")));
        groupProfile.setCreateDate(rs.getTimestamp("create_time"));
        groupProfile.setCreateIp(rs.getString("create_ip"));
        groupProfile.setCreateUno(rs.getString("create_uno"));
        groupProfile.setLastModifyDate(rs.getTimestamp("last_modify_time"));
        groupProfile.setLastModifyIp(rs.getString("last_modify_ip"));
        groupProfile.setLastModifyUno(rs.getString("last_modify_uno"));
        groupProfile.setSilencedDate(rs.getTimestamp("silenced_time"));
        groupProfile.setSilencedEndDate(rs.getTimestamp("silenced_end_time"));
        groupProfile.setLastLoginDate(rs.getTimestamp("last_login_time"));
        groupProfile.setSilencedReason(rs.getString("silenced_reason"));
        groupProfile.setExtInt1(rs.getInt("extInt1"));
        groupProfile.setExtInt2(rs.getInt("extInt2"));
        groupProfile.setExtString1(rs.getString("extStr1"));
        groupProfile.setExtString2(rs.getString("extStr2"));
        return groupProfile;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + "(group_id,uno,profile_type,status,create_time,create_ip,create_uno,extInt1,extInt2,extStr1,extStr2) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("insert groupProfile sql:" + insertSql);
        }
        return insertSql;
    }
}
