package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.gameres.privilege.PrivilegeCategory;
import com.enjoyf.platform.service.gameres.privilege.GroupProfilePrivilege;
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
 * Time: 下午2:29
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGroupProfilePrivilegeAccessor extends AbstractBaseTableAccessor<GroupProfilePrivilege> implements GroupProfilePrivilegeAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractGroupProfilePrivilegeAccessor.class);

    private static final String KEY_TABLE_NAME = "profile_privilege";

    @Override
    public GroupProfilePrivilege insert(GroupProfilePrivilege groupProfilePrivilege, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, groupProfilePrivilege.getUno());
            pstmt.setLong(2, groupProfilePrivilege.getDestId());
            pstmt.setInt(3, groupProfilePrivilege.getPrivilegeCategory().getCode());
            pstmt.setLong(4, groupProfilePrivilege.getGroupId());
            pstmt.setString(5, groupProfilePrivilege.getActStatus() == null ? ActStatus.UNACT.getCode() : groupProfilePrivilege.getActStatus().getCode());
            pstmt.setTimestamp(6, new Timestamp(groupProfilePrivilege.getCreateDate() == null ? System.currentTimeMillis() : groupProfilePrivilege.getCreateDate().getTime()));
            pstmt.setString(7, groupProfilePrivilege.getCreateIp());
            pstmt.setString(8, groupProfilePrivilege.getCreateUno());
            pstmt.setInt(9, groupProfilePrivilege.getExtInt1());
            pstmt.setInt(10, groupProfilePrivilege.getExtInt2());
            pstmt.setString(11, groupProfilePrivilege.getExtString1());
            pstmt.setString(12, groupProfilePrivilege.getExtString2());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                groupProfilePrivilege.setProfilePrivilegeId(rs.getLong(1));
            }

        } catch (SQLException e) {
            GAlerter.lab("On insert Privilege,SQLException:" + e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return groupProfilePrivilege;
    }

    @Override
    public GroupProfilePrivilege get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<GroupProfilePrivilege> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<GroupProfilePrivilege> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected GroupProfilePrivilege rsToObject(ResultSet rs) throws SQLException {
        GroupProfilePrivilege profilePrivilege = new GroupProfilePrivilege();
        profilePrivilege.setProfilePrivilegeId(rs.getLong("profile_privilege_id"));
        profilePrivilege.setUno(rs.getString("uno"));
        profilePrivilege.setDestId(rs.getLong("dest_id"));
        profilePrivilege.setPrivilegeCategory(PrivilegeCategory.getByCode(rs.getInt("dest_type")));
        profilePrivilege.setGroupId(rs.getLong("group_id"));
        profilePrivilege.setActStatus(ActStatus.getByCode(rs.getString("act_status")));
        profilePrivilege.setCreateDate(rs.getTimestamp("create_time"));
        profilePrivilege.setCreateIp(rs.getString("create_ip"));
        profilePrivilege.setCreateUno(rs.getString("create_uno"));
        profilePrivilege.setLastModifyDate(rs.getTimestamp("last_modify_time"));
        profilePrivilege.setLastModifyIp(rs.getString("last_modify_ip"));
        profilePrivilege.setLastModifyUno(rs.getString("last_modify_uno"));
        profilePrivilege.setExtInt1(rs.getInt("extInt1"));
        profilePrivilege.setExtInt2(rs.getInt("extInt2"));
        profilePrivilege.setExtString1(rs.getString("extStr1"));
        profilePrivilege.setExtString2(rs.getString("extStr2"));
        return profilePrivilege;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + "(uno,dest_id,dest_type,group_id,act_status,create_time,create_ip,create_uno,extInt1,extInt2,extStr1,extStr2) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("insert UserPrivilege sql:" + insertSql);
        }
        return insertSql;
    }
}
