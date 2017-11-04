package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.gameres.privilege.Role;
import com.enjoyf.platform.service.gameres.privilege.RoleLevel;
import com.enjoyf.platform.service.gameres.privilege.RoleType;
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
 * Time: 上午11:44
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractRoleAccessor extends AbstractBaseTableAccessor<Role> implements RoleAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRoleAccessor.class);

    private static final String KEY_TABLE_NAME = "role";

    @Override
    public Role insert(Role role, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, role.getRoleName());
            pstmt.setInt(2, role.getRoleLevel().getCode());
            pstmt.setString(3, role.getRoleDesc());
            pstmt.setInt(4, role.getRoleType().getCode());
            pstmt.setLong(5, role.getGroupId());
            pstmt.setString(6, role.getActStatus() == null ? ActStatus.UNACT.getCode() : role.getActStatus().getCode());
            pstmt.setTimestamp(7, new Timestamp(role.getCreateDate() == null ? System.currentTimeMillis() : role.getCreateDate().getTime()));
            pstmt.setString(8, role.getCreateIp());
            pstmt.setString(9, role.getCreateUserId());
            pstmt.setInt(10, role.getExtInt1());
            pstmt.setInt(11, role.getExtInt2());
            pstmt.setString(12, role.getExtString1());
            pstmt.setString(13, role.getExtString2());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                role.setRoleId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert Role,SQLException:" + e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return role;
    }

    @Override
    public List<Role> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public Role get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<Role> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected Role rsToObject(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setRoleId(rs.getLong("role_id"));
        role.setRoleName(rs.getString("role_name"));
        role.setRoleLevel(RoleLevel.getByCode(rs.getInt("role_level")));
        role.setRoleDesc(rs.getString("role_desc"));

        role.setRoleType(RoleType.getByCode(rs.getInt("role_type")));
        role.setGroupId(rs.getLong("group_id"));
        role.setActStatus(ActStatus.getByCode(rs.getString("status")));
        role.setCreateDate(rs.getTimestamp("create_time"));
        role.setCreateIp(rs.getString("create_ip"));
        role.setCreateUserId(rs.getString("create_userid"));
        role.setLastModifyDate(rs.getTimestamp("lsat_modify_time"));
        role.setLastModifyIp(rs.getString("last_modify_ip"));
        role.setLastModifyUserId(rs.getString("last_modify_userid"));
        role.setExtInt1(rs.getInt("extInt1"));
        role.setExtInt2(rs.getInt("extInt2"));
        role.setExtString1(rs.getString("extStr1"));
        role.setExtString2(rs.getString("extStr2"));
        return role;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + "(role_name,role_level,role_desc,role_type,group_id,status,create_time,create_ip,create_userid,extInt1,extInt2,extStr1,extStr2) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("insert role sql:" + insertSql);
        }
        return insertSql;
    }
}
