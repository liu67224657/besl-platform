package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.gameres.privilege.GroupPrivilege;
import com.enjoyf.platform.service.gameres.privilege.GroupPrivilegeCode;
import com.enjoyf.platform.service.gameres.privilege.GroupPrivilegeType;
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
public abstract class AbstractGroupPrivilegeAccessor extends AbstractBaseTableAccessor<GroupPrivilege> implements GroupPrivilegeAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractGroupPrivilegeAccessor.class);

    private static final String KEY_TABLE_NAME = "group_privilege";

    @Override
    public GroupPrivilege insert(GroupPrivilege privilege, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, privilege.getPrivilegeName());
            pstmt.setString(2, privilege.getPrivilegeCode().getCode());
            pstmt.setString(3, privilege.getPrivilegeDesc());
            pstmt.setInt(4, privilege.getPrivilegeType().getCode());
            pstmt.setString(5, privilege.getActStatus() == null ? ActStatus.UNACT.getCode() : privilege.getActStatus().getCode());
            pstmt.setTimestamp(6, new Timestamp(privilege.getCreateDate() == null ? System.currentTimeMillis() : privilege.getCreateDate().getTime()));
            pstmt.setString(7, privilege.getCreateIp());
            pstmt.setString(8, privilege.getCreateUserId());
            pstmt.setInt(9, privilege.getExtInt1());
            pstmt.setInt(10, privilege.getExtInt2());
            pstmt.setString(11, privilege.getExtString1());
            pstmt.setString(12, privilege.getExtString2());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                privilege.setPrivilegeId(rs.getLong(1));
            }

        } catch (SQLException e) {
            GAlerter.lab("On insert Privilege,SQLException:" + e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return privilege;
    }

    @Override
    public GroupPrivilege get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<GroupPrivilege> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<GroupPrivilege> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected GroupPrivilege rsToObject(ResultSet rs) throws SQLException {
        GroupPrivilege privilege = new GroupPrivilege();
        privilege.setPrivilegeId(rs.getLong("privilege_id"));
        privilege.setPrivilegeName(rs.getString("privilege_name"));
        privilege.setPrivilegeCode(GroupPrivilegeCode.getByCode(rs.getString("privilege_code")));
        privilege.setPrivilegeDesc(rs.getString("privilege_desc"));
        privilege.setPrivilegeType(GroupPrivilegeType.getByCode(rs.getInt("privilege_type")));
        privilege.setActStatus(ActStatus.getByCode(rs.getString("status")));
        privilege.setCreateDate(rs.getTimestamp("create_time"));
        privilege.setCreateIp(rs.getString("create_ip"));
        privilege.setCreateUserId(rs.getString("create_userid"));
        privilege.setLastModifyDate(rs.getTimestamp("last_modify_time"));
        privilege.setLastModifyIp(rs.getString("last_modify_ip"));
        privilege.setLastModifyUserId(rs.getString("last_modify_userid"));
        privilege.setExtInt1(rs.getInt("extInt1"));
        privilege.setExtInt2(rs.getInt("extInt2"));
        privilege.setExtString1(rs.getString("extStr1"));
        privilege.setExtString2(rs.getString("extStr2"));
        return privilege;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + "(privilege_name,privilege_code,privilege_desc,privilege_type,status,create_time,create_ip,create_userid,extInt1,extInt2,extStr1,extStr2) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("insert privilege sql:" + insertSql);
        }
        return insertSql;
    }
}
