package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.gameres.privilege.PrivilegeRoleRelation;
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
 * Time: 下午1:38
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractPrivilegeRoleRelationAccessor extends AbstractBaseTableAccessor<PrivilegeRoleRelation> implements PrivilegeRoleRelationAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPrivilegeRoleRelationAccessor.class);

    private static final String KEY_TABLE_NAME = "privilege_role_relation";

    @Override
    public PrivilegeRoleRelation insert(PrivilegeRoleRelation privilegeRoleRelation, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setLong(1, privilegeRoleRelation.getPrivilegeId());
            pstmt.setLong(2, privilegeRoleRelation.getRoleId());
            pstmt.setString(3, privilegeRoleRelation.getActStatus() == null ? ActStatus.UNACT.getCode() : privilegeRoleRelation.getActStatus().getCode());
            pstmt.setTimestamp(4, new Timestamp(privilegeRoleRelation.getCreateDate() == null ? System.currentTimeMillis() : privilegeRoleRelation.getCreateDate().getTime()));
            pstmt.setString(5, privilegeRoleRelation.getCreateIp());
            pstmt.setString(6, privilegeRoleRelation.getCreateUserId());
            pstmt.setInt(7, privilegeRoleRelation.getExtInt1());
            pstmt.setInt(8, privilegeRoleRelation.getExtInt2());
            pstmt.setString(9, privilegeRoleRelation.getExtString1());
            pstmt.setString(10, privilegeRoleRelation.getExtString2());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                privilegeRoleRelation.setRelationId(rs.getLong(1));
            }

        } catch (SQLException e) {
            GAlerter.lab("On insert PrivilegeRoleRelation,SQLException:" + e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return privilegeRoleRelation;
    }

    @Override
    public PrivilegeRoleRelation get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<PrivilegeRoleRelation> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<PrivilegeRoleRelation> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected PrivilegeRoleRelation rsToObject(ResultSet rs) throws SQLException {
        PrivilegeRoleRelation privilegeRoleRelation = new PrivilegeRoleRelation();
        privilegeRoleRelation.setRelationId(rs.getLong("relation_id"));
        privilegeRoleRelation.setPrivilegeId(rs.getLong("privilege_id"));
        privilegeRoleRelation.setRoleId(rs.getLong("role_id"));
        privilegeRoleRelation.setActStatus(ActStatus.getByCode(rs.getString("status")));
        privilegeRoleRelation.setCreateDate(rs.getTimestamp("create_time"));
        privilegeRoleRelation.setCreateIp(rs.getString("create_ip"));
        privilegeRoleRelation.setCreateUserId(rs.getString("create_userid"));
        privilegeRoleRelation.setLastModifyDate(rs.getTimestamp("last_modify_time"));
        privilegeRoleRelation.setLastModifyIp(rs.getString("last_modify_ip"));
        privilegeRoleRelation.setLastModifyUserId(rs.getString("last_modify_userid"));
        privilegeRoleRelation.setExtInt1(rs.getInt("extInt1"));
        privilegeRoleRelation.setExtInt2(rs.getInt("extInt2"));
        privilegeRoleRelation.setExtString1(rs.getString("extStr1"));
        privilegeRoleRelation.setExtString2(rs.getString("extStr2"));
        return privilegeRoleRelation;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + "(privilege_id,role_id,status,create_time,create_ip,create_userid,extInt1,extInt2,extStr1,extStr2) VALUES(?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("insert PrivilegeRoleRelation sql:" + insertSql);
        }
        return insertSql;
    }
}
