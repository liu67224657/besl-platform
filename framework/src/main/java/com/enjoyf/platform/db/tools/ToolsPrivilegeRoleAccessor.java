package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.tools.PrivilegeRole;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.ObjectField;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 下午2:58
 * Desc:
 */
interface ToolsPrivilegeRoleAccessor {


    /**
     * 根据角色ID查询角色
     *
     * @param id
     * @param conn
     * @return
     * @throws com.enjoyf.platform.db.DbException
     *
     */
    public PrivilegeRole findRoleById(int id, Connection conn) throws DbException;

    /**
     * 查询角色列表
     *
     * @param param
     * @param p
     * @param conn
     * @return
     * @throws DbException
     */
    public List<PrivilegeRole> queryRoleByParam(PrivilegeRole param, Pagination p, Connection conn) throws DbException;

    /**
     * 查询是否存在该角色
     *
     * @param roleName
     * @param conn
     * @return
     * @throws DbException
     */
    public PrivilegeRole getRoleByRoleName(String roleName, Connection conn) throws DbException;

    /**
     * 保存角色菜单
     *
     * @param rid
     * @param menuids
     * @param conn
     * @return
     * @throws DbException
     */
    public boolean insertRoleMenu(int rid, String[] menuids, Connection conn) throws DbException;

    /**
     * 删除属于rid的角色资源表中的资源
     *
     * @param rid
     * @param conn
     * @return
     * @throws DbException
     */
    public boolean deleteRoleMenu(int rid, Connection conn) throws DbException;

    /**
     * 增加角色
     *
     * @param entity
     * @param conn
     * @return
     * @throws DbException
     */
    public boolean insertRole(PrivilegeRole entity, Connection conn) throws DbException;

    /**
     * 修改角色
     *
     * @param entity
     * @param conn
     * @return
     * @throws DbException
     */
    public boolean updateRole(PrivilegeRole entity, Map<ObjectField, Object> map, Connection conn) throws DbException;

    /**
     * 删除角色
     *
     * @param rid
     * @param conn
     * @return
     * @throws DbException
     */
    public boolean deleteRole(int rid, Connection conn) throws DbException;

    /**
     * 查询所有的角色列表
     *
     * @param conn
     * @return
     * @throws DbException
     */
    public List<PrivilegeRole> queryRoles(Connection conn) throws DbException;

    public PrivilegeRole getRoleByRoleId(Integer rid, Connection conn) throws DbException;
}
