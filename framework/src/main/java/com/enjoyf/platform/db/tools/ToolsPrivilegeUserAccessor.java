package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.PrivilegeUser;
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
interface ToolsPrivilegeUserAccessor {


    /**
     * 根据用户名查询用户
     *
     * @param loginName
     * @param conn
     * @return
     * @throws ServiceException
     */
    public PrivilegeUser getUserByLoginName(String loginName, Connection conn) throws DbException;

    /**
     * 通过uno查询实体
     *
     * @param uno
     * @param conn
     * @return
     * @throws DbException
     */
    public PrivilegeUser getByUno(int uno, Connection conn) throws DbException;

    /**
     * 根据用户名密码查询用户
     *
     * @param userid
     * @param pwd
     * @param conn
     * @return
     * @throws ServiceException
     */
    public PrivilegeUser getUserByUserIdPwd(String userid, String pwd, Connection conn) throws DbException;


    /**
     * 查询出用户列表
     *
     * @param param
     * @param p
     * @param conn
     * @return
     * @throws DbException
     */
    public List<PrivilegeUser> queryUserByParam(PrivilegeUser param, Pagination p, Connection conn) throws DbException;


    /**
     * 更新用户信息
     *
     * @param entity
     * @param conn
     * @return
     */
    public boolean updateUser(PrivilegeUser entity, Map<ObjectField, Object> map, Connection conn) throws DbException;

    /**
     * 修改密码
     *
     * @param pwd
     * @param uno
     * @param conn
     * @return
     * @throws DbException
     */
    public boolean updatePwd(String pwd, int uno, Connection conn) throws DbException;

    /**
     * 修改用户状态
     *
     * @param ustatus
     * @param uno
     * @param conn
     * @return
     * @throws DbException
     */
    public boolean switchUser(String ustatus, int uno, Connection conn) throws DbException;

    /**
     * 新增用户
     *
     * @param entity
     * @param conn
     * @return
     * @throws DbException
     */
    public boolean insertUser(PrivilegeUser entity, Connection conn) throws DbException;


    /**
     * 删除用户
     *
     * @param uno
     * @param conn
     * @return
     * @throws DbException
     */
    public boolean deleteUser(int uno, Connection conn) throws DbException;

    /**
     * 通过用户实体和选择的角色ids保存用户角色
     *
     * @param uno
     * @param rolseids
     * @param conn
     * @return
     * @throws DbException
     */
    public boolean insertUserRole(int uno, String[] rolseids, Connection conn) throws DbException;

    /**
     * 删除用户角色
     *
     * @param uno
     * @param conn
     * @return
     * @throws DbException
     */
    public boolean deleteUserRole(int uno, Connection conn) throws DbException;


}
