package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.tools.PrivilegeResource;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.ObjectField;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 下午2:58
 * Desc: 权限，资源
 */
interface ToolsPrivilegeResAccessor {


    /**
     * 根据资源ID查询资源
     *
     * @param id
     * @param conn
     * @return
     * @throws DbException
     */
    public PrivilegeResource findResById(int id, Connection conn) throws DbException;

    /**
     * 查询全部资源，根据状态
     * 状态为空，查询全部
     *
     * @param status
     * @return
     * @throws DbException
     */
    public List<PrivilegeResource> queryAllResByStatus(ActStatus status, Connection conn) throws DbException;

    /**
     * 查询资源列表
     *
     * @param param
     * @param p
     * @param conn
     * @return
     * @throws DbException
     */
    public List<PrivilegeResource> queryResByParam(PrivilegeResource param, Pagination p, Connection conn) throws DbException;

    /**
     * 通过角色类型和状态查询资源列表
     *
     * @param rstype
     * @param status
     * @param conn
     * @return
     * @throws DbException
     */
    public List<PrivilegeResource> queryResMenu(String rstype, String status, Connection conn) throws DbException;

    /**
     * 删除资源
     *
     * @param status
     * @param rsid
     * @param conn
     * @return
     * @throws DbException
     */
    public boolean deleteRes(String status, int rsid, Connection conn) throws DbException;

    /**
     * 更新资源
     *
     * @param entity
     * @param conn
     * @return
     * @throws DbException
     */
    public boolean updateRes(PrivilegeResource entity, Map<ObjectField, Object> map, Connection conn) throws DbException;

    /**
     * 添加资源
     *
     * @param entity
     * @param conn
     * @return
     * @throws DbException
     */
    public boolean insertRes(PrivilegeResource entity, Connection conn) throws DbException;

    /**
     * 查询是否存在该URL的记录
     *
     * @param roleName
     * @param conn
     * @return
     * @throws DbException
     */
    public PrivilegeResource getResByRsurl(String roleName, Connection conn) throws DbException;

    public PrivilegeResource getResourceByRsid(int rsid, Connection conn) throws DbException;

}
