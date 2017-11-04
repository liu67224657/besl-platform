package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.tools.*;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 下午2:55
 * Desc:
 */
public class ToolsHandler {
    //
    private DataBaseType dataBaseType;
    private String dataSourceName;

    //
    private ToolsAuditContentAccessor contentAccessor;
    private ToolsAuditUserAccessor userAccessor;

    //privilege
    private ToolsPrivilegeUserAccessor privilegeUserAccessor;
    private ToolsPrivilegeRoleAccessor privilegeRoleAccessor;
    private ToolsPrivilegeResAccessor privilegeResAccessor;
    private ToolsLogAccessor logAccessor;
    private ToolsLoginLogAccessor loginLogAccessor;

    //
    private StatsEditorAccessor statsEditorAccessor;
    private StatsEditorItemAccessor statsEditorItemAccessor;


    public ToolsHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        contentAccessor = ToolsAccessorFactory.auditContentFactory(dataBaseType);
        userAccessor = ToolsAccessorFactory.auditUserFactory(dataBaseType);

        // privilege
        privilegeUserAccessor = ToolsAccessorFactory.privileUserFactory(dataBaseType);
        privilegeRoleAccessor = ToolsAccessorFactory.privileRolesFactory(dataBaseType);
        privilegeResAccessor = ToolsAccessorFactory.privileResFactory(dataBaseType);
        logAccessor = ToolsAccessorFactory.privileLogFactory(dataBaseType);
        loginLogAccessor = ToolsAccessorFactory.privileLoginLogFactory(dataBaseType);

        //
        statsEditorAccessor = TableAccessorFactory.get().factoryAccessor(StatsEditorAccessor.class, dataBaseType);
        statsEditorItemAccessor = TableAccessorFactory.get().factoryAccessor(StatsEditorItemAccessor.class, dataBaseType);
    }


    public AuditUser insertAuditUser(AuditUser auditUser) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return userAccessor.insertAuditUser(auditUser, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AuditContent insertAuditContent(AuditContent auditContent) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentAccessor.insertAuditContent(auditContent, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean updateAuditUser(Long auditId, String uno, Map<ObjectField, Object> map) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return userAccessor.updateAuditUser(auditId, uno, map, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateAuditContent(Long auditId, String uno, Map<ObjectField, Object> map) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentAccessor.updateAuditContent(auditId, uno, map, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AuditUser findAuditUserByUno(String uno) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return userAccessor.findAuditUserByUno(uno, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public AuditContent findAuditContentByCIdCt(String cid, ContentType contentType, String uno) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentAccessor.findAuditContentByCIdCT(cid, contentType, uno, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AuditContent findAuditContentById(Long aid, String cid, String uno) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentAccessor.findAuditContentById(aid, cid, uno, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public PrivilegeUser getUserByLoginName(String userId) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeUserAccessor.getUserByLoginName(userId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PrivilegeRole getRoleByRoleName(String rolesName) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeRoleAccessor.getRoleByRoleName(rolesName, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean insertRoleMenu(Integer rid, String[] menuids) throws DbException {
        Connection conn = null;
        boolean bool = false;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);
            if (privilegeRoleAccessor.deleteRoleMenu(rid, conn)) {
                bool = true;
            }
            if (bool && menuids != null && menuids.length > 0) {
                bool = privilegeRoleAccessor.insertRoleMenu(rid, menuids, conn);
            }
            if (bool) {
                conn.commit();
            } else {
                conn.rollback();
            }

        } catch (SQLException e) {
            GAlerter.lab("ToolsHandler insertRoleMenu a SQLException occured:" + e);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return bool;
    }

    public boolean insertUserRole(Integer uno, String[] rolseids) throws DbException {
        Connection conn = null;
        boolean bool = false;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);
            if (privilegeUserAccessor.deleteUserRole(uno, conn)) {
                bool = true;
            }
            if (bool && rolseids != null && rolseids.length > 0) {
                bool = privilegeUserAccessor.insertUserRole(uno, rolseids, conn);
            }
            if (bool) {
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (SQLException e) {
            GAlerter.lab("ToolsHandler insertUserRole a SQLException occured:" + e);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return bool;
    }

    public boolean deleteUserRole(Integer uno) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeUserAccessor.deleteUserRole(uno, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<PrivilegeRole> queryRoles() throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeRoleAccessor.queryRoles(conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<PrivilegeResource> queryResMenu(String rstype, String status) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeResAccessor.queryResMenu(rstype, status, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PrivilegeResource getResByRsurl(String rsurl) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeResAccessor.getResByRsurl(rsurl, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PrivilegeUser getByUno(Integer uno) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeUserAccessor.getByUno(uno, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PrivilegeUser getUserByUserIdPwd(String userId, String pwd) throws DbException {
        Connection conn = null;
        PrivilegeUser entity = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            entity = privilegeUserAccessor.getUserByUserIdPwd(userId, pwd, conn);

            Set<PrivilegeRole> roleSet = new HashSet<PrivilegeRole>();

            if (entity != null) {
                // 查用户对应的角色
                for (PrivilegeRole roles : entity.getPrivilegeRoleses()) {
                    PrivilegeRole role = privilegeRoleAccessor.findRoleById(roles.getRid(), conn);

                    // 查角色对应的资源
                    Set<PrivilegeResource> resourceSet = new HashSet<PrivilegeResource>();
                    for (PrivilegeResource rs : role.getPrivilegeResources()) {
                        PrivilegeResource resource = privilegeResAccessor.findResById(rs.getRsid(), conn);
                        if (resource != null) {
                            resourceSet.add(resource);
                        }
                    }
                    role.setPrivilegeResources(resourceSet);
                    roleSet.add(role);
                }
                entity.setPrivilegeRoleses(roleSet);
            }
            return entity;

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Set<PrivilegeResource> queryResByRoleId(PrivilegeRole entity) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            PrivilegeRole role = privilegeRoleAccessor.findRoleById(entity.getRid(), conn);

            Set<PrivilegeResource> resourceSet = new HashSet<PrivilegeResource>();
            if (role != null) {
                for (PrivilegeResource rs : role.getPrivilegeResources()) {
                    PrivilegeResource resource = privilegeResAccessor.findResById(rs.getRsid(), conn);
                    resourceSet.add(resource);
                }
                role.setPrivilegeResources(resourceSet);
            }
            return resourceSet;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<PrivilegeUser> queryUserByParam(PrivilegeUser param, Pagination p) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return privilegeUserAccessor.queryUserByParam(param, p, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<PrivilegeRole> queryRoleByParam(PrivilegeRole param, Pagination p) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return privilegeRoleAccessor.queryRoleByParam(param, p, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<PrivilegeResource> queryResByParam(PrivilegeResource param, Pagination p) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return privilegeResAccessor.queryResByParam(param, p, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PrivilegeResource findResById(Integer id) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeResAccessor.findResById(id, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateUser(PrivilegeUser entity, Map<ObjectField, Object> map) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeUserAccessor.updateUser(entity, map, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updatePwd(String pwd, Integer uno) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeUserAccessor.updatePwd(pwd, uno, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateRole(PrivilegeRole entity, Map<ObjectField, Object> map) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeRoleAccessor.updateRole(entity, map, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateRes(PrivilegeResource entity, Map<ObjectField, Object> map) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeResAccessor.updateRes(entity, map, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean switchUser(String ustatus, Integer uno) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeUserAccessor.switchUser(ustatus, uno, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean saveUser(PrivilegeUser entity) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeUserAccessor.insertUser(entity, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean saveRole(PrivilegeRole entity) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeRoleAccessor.insertRole(entity, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean saveRes(PrivilegeResource entity) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeResAccessor.insertRes(entity, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean deleteRole(Integer rid) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeRoleAccessor.deleteRole(rid, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean deleteUser(Integer uno) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeUserAccessor.deleteUser(uno, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean deleteRes(String status, Integer rsid) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeResAccessor.deleteRes(status, rsid, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<PrivilegeResource> queryAllResByStatus(ActStatus status) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return privilegeResAccessor.queryAllResByStatus(status, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    // --   privilege log-----------
    public ToolsLog insertLog(ToolsLog entity) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return logAccessor.insertLog(entity, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public ToolsLog getLog(long id, Date date) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return logAccessor.getLog(id, date, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public ToolsLoginLog getLoginLog(long id) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return loginLogAccessor.getLoginLog(id, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public ToolsLoginLog insertLoginLog(ToolsLoginLog entity) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return loginLogAccessor.insertLoginLog(entity, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public List<ToolsLog> queryLogs(ToolsLog entity, Pagination p) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return logAccessor.queryLogs(entity, p, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ToolsLoginLog> queryLoginLogs(ToolsLoginLog entity, Pagination p) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return loginLogAccessor.queryLoginLogs(entity, p, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PrivilegeRole getRoleByRoleId(Integer rid) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return privilegeRoleAccessor.getRoleByRoleId(rid, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PrivilegeResource getResourceByRsid(int rsid) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return privilegeResAccessor.getResourceByRsid(rsid, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //the apis for stats editor table rows operating.
    public StatsEditor insertStatsEditor(StatsEditor entry) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return statsEditorAccessor.insert(entry, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public StatsEditor getStatsEditor(QueryExpress getExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return statsEditorAccessor.get(getExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<StatsEditor> queryStatsEditors(QueryExpress queryExpress) throws DbException {
        List<StatsEditor> returnValue = new ArrayList<StatsEditor>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue = statsEditorAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public PageRows<StatsEditor> queryStatsEditorsByPage(QueryExpress queryExpress, Pagination page) throws DbException {
        PageRows<StatsEditor> returnValue = new PageRows<StatsEditor>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<StatsEditor> list = statsEditorAccessor.query(queryExpress, page, conn);

            returnValue.setPage(page);
            returnValue.setRows(list);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public int updateStatsEditor(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return statsEditorAccessor.update(updateExpress, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int deleteStatsEditor(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return statsEditorAccessor.delete(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //the apis for stats editor item table rows operating.
    public StatsEditorItem insertStatsEditorItem(StatsEditorItem entry) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return statsEditorItemAccessor.insert(entry, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public StatsEditorItem getStatsEditorItem(QueryExpress getExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return statsEditorItemAccessor.get(getExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<StatsEditorItem> queryStatsEditorItems(QueryExpress queryExpress) throws DbException {
        List<StatsEditorItem> returnValue = new ArrayList<StatsEditorItem>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue = statsEditorItemAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public PageRows<StatsEditorItem> queryStatsEditorItemsByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<StatsEditorItem> returnValue = new PageRows<StatsEditorItem>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<StatsEditorItem> list= statsEditorItemAccessor.query(queryExpress,pagination,conn);
            returnValue.setPage(pagination);
            returnValue.setRows(list);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public int updateStatsEditorItem(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return statsEditorItemAccessor.update(updateExpress, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int deleteStatsEditorItem(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return statsEditorItemAccessor.delete(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
}
