package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.tools.PrivilegeRole;
import com.enjoyf.platform.service.tools.PrivilegeUser;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
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
class AbstractToolsPrivilegeUserAccessor implements ToolsPrivilegeUserAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractToolsPrivilegeUserAccessor.class);

    //
    private static final String KEY_TABLE_NAME_USER = "PRIVILEGE_USER";
    private static final String KEY_TABLE_NAME_USER_ROLE = "PRIVILEGE_USER_ROLE";
    private static final String KEY_SEQUENCE_NAME = "SEQ_USER_ID";

    public long getSeqNo(Connection conn) throws DbException {
        try {
            return TableSequenceFetcher.get().generate(KEY_SEQUENCE_NAME, conn);
        } catch (TableSequenceException e) {
            throw new DbException(DbException.TABLE_SEQUENCE_ERROR, "Fetch sequence value error, sequence:" + KEY_SEQUENCE_NAME);
        }
    }

    @Override
    public PrivilegeUser getUserByLoginName(String loginName, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PrivilegeUser privilegeUser = null;

        String sql = "SELECT * FROM " + getUserTableName() + " WHERE USERID = ? AND USTATUS = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("tools privilege user getUserByLoginName  Script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, loginName);
            pstmt.setString(2, ActStatus.ACTED.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                privilegeUser = rsToObject(rs);
            }

            if (privilegeUser != null) {
                privilegeUser.setPrivilegeRoleses(selectUserRoleByUno(privilegeUser.getUno(), conn));
            }

        } catch (SQLException e) {
            GAlerter.lab("On getUserByLoginName , a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return privilegeUser;
    }

    @Override
    public PrivilegeUser getByUno(int uno, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PrivilegeUser privilegeUser = null;

        String sql = "SELECT * FROM " + getUserTableName() + " WHERE UNO = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("tools privilege user getByUno  Script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, uno);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                privilegeUser = rsToObject(rs);
            }

            if (privilegeUser != null) {
                privilegeUser.setPrivilegeRoleses(selectUserRoleByUno(privilegeUser.getUno(), conn));
            }

        } catch (SQLException e) {
            GAlerter.lab("On getByUno , a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return privilegeUser;
    }

    @Override
    public PrivilegeUser getUserByUserIdPwd(String userId, String pwd, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PrivilegeUser privilegeUser = null;

        String sql = "SELECT * FROM " + getUserTableName() + " WHERE USERID = ? AND PASSWORD = ? AND USTATUS = ? ";

        if (logger.isDebugEnabled()) {
            logger.debug("tools privilege getUserByUserIdPwd script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, userId);
            pstmt.setString(2, pwd);
            pstmt.setString(3, ActStatus.ACTED.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                privilegeUser = rsToObject(rs);
            }

            if (privilegeUser != null) {
                privilegeUser.setPrivilegeRoleses(selectUserRoleByUno(privilegeUser.getUno(), conn));
            }
        } catch (SQLException e) {
            GAlerter.lab("On getUserByUserIdPwd , a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return privilegeUser;
    }

    @Override
    public List<PrivilegeUser> queryUserByParam(PrivilegeUser param, Pagination p, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    /**
     * 修改用户
     *
     * @param entity
     * @param conn
     * @return
     * @throws DbException
     */
    @Override
    public boolean updateUser(PrivilegeUser entity, Map<ObjectField, Object> map, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        boolean bool = false;

        //修改用户
        sql = "UPDATE " + getUserTableName() + " SET " + ObjectFieldUtil.generateMapSetClause(map) + " WHERE UNO = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("user updateUser script:" + sql);
        }
        try {
            pstmt = conn.prepareStatement(sql);

            int index = ObjectFieldUtil.setStmtParams(pstmt, 1, map);
            pstmt.setInt(index, entity.getUno());

            bool = pstmt.execute();
        } catch (SQLException e) {
            GAlerter.lab("On mothod updateUser update a user, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return bool;
    }

    @Override
    public boolean updatePwd(String pwd, int uno, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        boolean bool = false;

        //修改用户
        sql = "UPDATE " + getUserTableName() + " SET PASSWORD = ? WHERE UNO = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("Privilege updatePwd update password script:" + sql);
        }
        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, pwd);
            pstmt.setInt(2, uno);

            pstmt.execute();
            bool = true;
        } catch (SQLException e) {
            GAlerter.lab("On mothod updatePwd update a password, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return bool;
    }

    @Override
    public boolean switchUser(String ustatus, int uno, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();
        boolean bool = false;

        //修改用户
        sql.append("UPDATE ").append(getUserTableName()).append(" SET USTATUS = ? WHERE UNO = ?");

        if (logger.isDebugEnabled()) {
            logger.debug("Privilege switchUser script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql.toString());

            pstmt.setString(1, ustatus);
            pstmt.setInt(2, uno);
            bool = pstmt.execute();
        } catch (SQLException e) {
            GAlerter.lab("On mothod switchUser, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return bool;
    }

    @Override
    public boolean deleteUser(int uno, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        boolean bool = false;

        sql = "DELETE FROM " + getUserTableName() + " WHERE UNO = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("Privilege deleteUser delete user script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, uno);

            bool = pstmt.execute();
        } catch (SQLException e) {
            GAlerter.lab("On mothod deleteUser delete a user, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return bool;
    }


    @Override
    public boolean deleteUserRole(int uno, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean bool = false;

        String delsql = "DELETE FROM " + getUserRolesTableName() + " WHERE UNO = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("Privilege deleteUserRole script:" + delsql);
        }
        //删除该用户的原有角色
        try {
            pstmt = conn.prepareStatement(delsql);

            pstmt.setInt(1, uno);

            pstmt.executeUpdate();
            bool = true;
        } catch (SQLException e) {
            GAlerter.lab("On mothod delete sql, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return bool;
    }

    @Override
    public boolean insertUserRole(int uno, String[] rolseids, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        boolean bool = false;

        try {
            if (rolseids != null) {
                //添加角色菜单
                sql = "INSERT INTO " + getUserRolesTableName() + " (UNO, RID) VALUES (?, ?)";

                pstmt = conn.prepareStatement(sql);

                for (String rolseid : rolseids) {
                    pstmt.setInt(1, uno);
                    pstmt.setString(2, rolseid);

                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }

            bool = true;
        } catch (SQLException e) {
            GAlerter.lab("On mothod insertUserRole add a roles, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return bool;
    }

    @Override
    public boolean insertUser(PrivilegeUser entity, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        boolean bool = false;

        sql = "INSERT INTO " + getUserTableName() + " (UNO,USERID, USERNAME, PASSWORD, USTATUS, LIMITLOCATION) VALUES (?,?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("Privilege insertUser script:" + sql);
        }

        try {
            int i = 1;
            pstmt = conn.prepareStatement(sql);

            if (entity != null) {
                pstmt.setInt(i++, (int) getSeqNo(conn));
                pstmt.setString(i++, entity.getUserid());
                pstmt.setString(i++, entity.getUsername());
                pstmt.setString(i++, entity.getPassword());
                pstmt.setString(i++, entity.getUstatus().getCode());
                pstmt.setString(i++, entity.getLimitLocation().getCode());
            }

            pstmt.execute();
            bool = true;
        } catch (SQLException e) {
            GAlerter.lab("On mothod insertUser add a user, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return bool;
    }

    private Set<PrivilegeRole> selectUserRoleByUno(int uno, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Set<PrivilegeRole> set = new HashSet<PrivilegeRole>(0);

        String sql = "SELECT * FROM " + getUserRolesTableName() + " WHERE UNO = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("tools privilege selectUserRoleByUno script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, uno);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                set.add(rsToRole(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On selectUserRoleByUno , a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return set;
    }


    protected String getUserTableName() throws DbException {
        return KEY_TABLE_NAME_USER;
    }

    protected String getUserRolesTableName() throws DbException {
        return KEY_TABLE_NAME_USER_ROLE;
    }

    protected PrivilegeUser rsToObject(ResultSet rs) throws SQLException {
        PrivilegeUser entity = new PrivilegeUser();

        entity.setUno(rs.getInt("UNO"));
        entity.setUserid(rs.getString("USERID"));
        entity.setUsername(rs.getString("USERNAME"));
        entity.setUstatus(ActStatus.getByCode(rs.getString("USTATUS")));
        entity.setPassword(rs.getString("PASSWORD"));
        entity.setLimitLocation(rs.getString("LIMITLOCATION") == null ? ActStatus.UNACT :ActStatus.getByCode(rs.getString("LIMITLOCATION")));

        return entity;
    }

    protected PrivilegeRole rsToRole(ResultSet rs) throws SQLException {
        PrivilegeRole entity = new PrivilegeRole();

        entity.setRid(rs.getInt("RID"));

        return entity;
    }
}
