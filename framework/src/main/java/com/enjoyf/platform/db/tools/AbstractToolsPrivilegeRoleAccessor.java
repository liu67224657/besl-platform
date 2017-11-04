package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.tools.PrivilegeResource;
import com.enjoyf.platform.service.tools.PrivilegeRole;
import com.enjoyf.platform.service.tools.PrivilegeRoleType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Author: zhaoxin
 * Date: 11-11-2
 * Time: 上午11:39
 * Desc:
 */
class AbstractToolsPrivilegeRoleAccessor implements ToolsPrivilegeRoleAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractToolsPrivilegeRoleAccessor.class);
    //
    private static final String KEY_TABLE_NAME_ROLE = "PRIVILEGE_ROLE"; // 角色
    private static final String KEY_TABLE_NAME_ROLE_RS = "PRIVILEGE_ROLE_RS"; //角色资源
    private static final String KEY_SEQUENCE_NAME = "SEQ_ROLE_ID";

    public long getSeqNo(Connection conn) throws DbException {
        try {
            return TableSequenceFetcher.get().generate(KEY_SEQUENCE_NAME, conn);
        } catch (TableSequenceException e) {
            throw new DbException(DbException.TABLE_SEQUENCE_ERROR, "Fetch sequence value error, sequence:" + KEY_SEQUENCE_NAME);
        }
    }

    @Override
    public PrivilegeRole findRoleById(int id, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PrivilegeRole privilegeRole = null;

        String sql = "SELECT * FROM " + getRolesTableName() + " WHERE RID = ? AND RSTATUS = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("tools privilege roles findRoleById  Script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);
            pstmt.setString(2,ActStatus.ACTED.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                privilegeRole = rsToObject(rs);
            }
            if (privilegeRole != null) {
                privilegeRole.setPrivilegeResources(findRoleResByRid(privilegeRole.getRid(), conn));
            }
        } catch (SQLException e) {
            GAlerter.lab("On findRoleById , a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return privilegeRole;
    }


    private Set<PrivilegeResource> findRoleResByRid(int rid, Connection conn) throws DbException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Set<PrivilegeResource> set = new HashSet<PrivilegeResource>(0);

        String sql = "SELECT * FROM " + getRolesRsTableName() + " WHERE RID = ? ";

        if (logger.isDebugEnabled()) {
            logger.debug("tools privilege roles  findRoleResByRid script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, rid);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                set.add(rsToResObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On findRoleResByRid , a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return set;
    }


    protected String getRolesTableName() throws DbException {
        return KEY_TABLE_NAME_ROLE;
    }

    protected String getRolesRsTableName() throws DbException {
        return KEY_TABLE_NAME_ROLE_RS;
    }


    protected PrivilegeRole rsToObject(ResultSet rs) throws SQLException {
        //RID, ROLENAME, DESCRIPTION, STATUS,TYPE
        PrivilegeRole entity = new PrivilegeRole();

        entity.setRid(rs.getInt("RID"));
        entity.setRoleName(rs.getString("ROLENAME"));
        entity.setDescription(rs.getString("DESCRIPTION"));
        entity.setStatus(ActStatus.getByCode(rs.getString("RSTATUS")));
        entity.setType(PrivilegeRoleType.getByCode(rs.getString("RTYPE")));

        return entity;
    }

    // mini rolesEntity
    protected PrivilegeResource rsToResObject(ResultSet rs) throws SQLException {
        PrivilegeResource entity = new PrivilegeResource();

        entity.setRsid(rs.getInt("RSID"));

        return entity;
    }

    @Override
    public List<PrivilegeRole> queryRoleByParam(PrivilegeRole param, Pagination p, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public PrivilegeRole getRoleByRoleName(String roleName, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PrivilegeRole privilegeRole = null;

        String sql = "SELECT * FROM " + getRolesTableName() + " WHERE ROLENAME = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("tools privilege roles getRoleByRoleName  Script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roleName);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                privilegeRole = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On getUserByLoginName , a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return privilegeRole;
    }

    public PrivilegeRole getRoleByRoleId(Integer rid, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PrivilegeRole privilegeRole = null;

        String sql = "SELECT * FROM " + getRolesTableName() + " WHERE RID = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("tools privilege roles getRoleByRoleId  Script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, rid);

            rs = pstmt.executeQuery();
            if(rs.next()){
                privilegeRole = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On getRoleByRoleId , a SQLException occurred.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return privilegeRole;
    }

    @Override
    public boolean deleteRoleMenu(int rid, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        StringBuffer sql = new StringBuffer();
        boolean bool = false;

        String delsql = "DELETE FROM " + getRolesRsTableName() + " WHERE RID = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("Privilege insertRoleMenu script:" + sql + "DELSQL:" + delsql);
        }
        //删除该角色的原有菜单
        try {
            pstmt = conn.prepareStatement(delsql);

            pstmt.setInt(1, rid);

            pstmt.execute();
            bool = true;
        } catch (SQLException e) {
            GAlerter.lab("On mothod delete RESANDROLES, a SQLException occured.", e);
            throw new DbException(e);
        }

        return bool;
    }

    @Override
    public boolean insertRoleMenu(int rid, String[] menuids, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();
        boolean bool = false;

        //添加角色菜单
        sql.append("INSERT INTO ").append(getRolesRsTableName()).append(" (RID, RSID) " + "VALUES (?, ?)");
        try {
            if (menuids.length > 2) {
                pstmt = conn.prepareStatement(sql.toString());
                for (String menuid : menuids) {
                    if (!menuid.equals("-1")) {
                        pstmt.setInt(1, rid);
                        pstmt.setInt(2, Integer.parseInt(menuid));
                        pstmt.addBatch();
                    }
                }
                pstmt.executeBatch();

                bool = true;
            }
        } catch (SQLException e) {
            GAlerter.lab("On mothod insertRes add a res, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return bool;
    }

    @Override
    public boolean insertRole(PrivilegeRole entity, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        boolean bool = false;

        sql = "INSERT INTO " + getRolesTableName() + " (RID,ROLENAME, DESCRIPTION, RSTATUS, RTYPE) VALUES (?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("Privilege insertRole script:" + sql);
        }
        try {
            pstmt = conn.prepareStatement(sql);

            if (entity != null) {
                pstmt.setInt(1, (int) getSeqNo(conn));
                pstmt.setString(2, entity.getRoleName());
                pstmt.setString(3, entity.getDescription());
                pstmt.setString(4, entity.getStatus().getCode());
                pstmt.setString(5, entity.getType().getCode());
            }
            pstmt.execute();
            bool = true;
        } catch (SQLException e) {
            GAlerter.lab("On mothod insertRole add a role, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return bool;
    }

    @Override
    public boolean updateRole(PrivilegeRole entity, Map<ObjectField, Object> map, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        boolean bool = false;

        //修改角色
        sql = "UPDATE " + getRolesTableName() + " SET " + ObjectFieldUtil.generateMapSetClause(map) + " WHERE RID = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("Privilege updateRole update role script:" + sql);
        }
        try {
            pstmt = conn.prepareStatement(sql);

            int index = ObjectFieldUtil.setStmtParams(pstmt, 1, map);
            pstmt.setInt(index, entity.getRid());

            bool = pstmt.execute();
        } catch (SQLException e) {
            GAlerter.lab("On mothod updateRole update a role, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return bool;
    }

    @Override
    public boolean deleteRole(int rid, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        boolean bool = false;

        sql = "DELETE FROM " + getRolesTableName() + " WHERE RID = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("Privilege deleteRole delete role script:" + sql);
        }
        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, rid);

            bool = pstmt.execute();
        } catch (SQLException e) {
            GAlerter.lab("On mothod deleteRole delete a role, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return bool;
    }

    @Override
    public List<PrivilegeRole> queryRoles(Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<PrivilegeRole> list = new ArrayList<PrivilegeRole>(0);

        String sql = "SELECT * FROM " + getRolesTableName() + " WHERE RSTATUS = ? ";

        if (logger.isDebugEnabled()) {
            logger.debug("tools privilege roles  queryRoles script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1,ActStatus.ACTED.getCode());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryRoles , a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return list;
    }
}
