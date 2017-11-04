package com.enjoyf.platform.service.tools;

import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 上午9:57
 * Desc: 后台工具接口
 */
public interface ToolsService {

    public AuditUser createAuditUser(AuditUser auditUser) throws ServiceException;

    public AuditContent createAuditContent(AuditContent auditContent) throws ServiceException;

    public List<AuditUser> queryAuditUserByUnos(List<AuditUserQueryEntity> auditUserQueryEntityList) throws ServiceException;

    public List<AuditContent> queryAuditContentByIds(List<AuditContentQueryEntity> auditContentQueryEntityList) throws ServiceException;

    /**
     * 根据用户名查询用户
     *
     * @param loginName
     * @return
     * @throws ServiceException
     */
    public PrivilegeUser getUserByLoginName(String loginName) throws ServiceException;

    /**
     * 按照角色名查询角色
     *
     * @param roleName
     * @return
     * @throws ServiceException
     */
    public PrivilegeRole getRoleByRoleName(String roleName) throws ServiceException;

    /**
     * 根据角色id查询角色
     *
     * @param rid
     * @return
     * @throws ServiceException
     */
    public PrivilegeRole getRoleByRid(Integer rid) throws ServiceException;

    /**
     * 保存角色资源
     *
     * @param rid
     * @param menuids
     * @return
     * @throws ServiceException
     */
    public boolean insertRoleMenu(Integer rid, String[] menuids) throws ServiceException;

    /**
     * 查询角色列表
     *
     * @return
     * @throws ServiceException
     */
    public List<PrivilegeRole> queryRoles() throws ServiceException;

    /**
     * 保存用户角色
     *
     * @param uno
     * @param rolseids
     * @return
     * @throws ServiceException
     */
    public boolean insertUserRole(Integer uno, String[] rolseids) throws ServiceException;

    /**
     * 删除用户角色
     *
     * @param uno
     * @return
     * @throws ServiceException
     */
    public boolean deleteUserRole(Integer uno) throws ServiceException;

    /**
     * 查询资源列表
     *
     * @param rstype
     * @param status
     * @return
     * @throws ServiceException
     */
    public List<PrivilegeResource> queryResMenu(String rstype, String status) throws ServiceException;

    /**
     * 通过url获取资源
     *
     * @param rsurl
     * @return
     * @throws ServiceException
     */
    public PrivilegeResource getResByRsurl(String rsurl) throws ServiceException;

    /**
     * 按照uno查询用户
     *
     * @param uno
     * @return
     * @throws ServiceException
     */
    public PrivilegeUser getByUno(Integer uno) throws ServiceException;

    /**
     * 通过查询条件类获取用户对象
     *
     * @param param
     * @param page
     * @return
     * @throws ServiceException
     */
    public PageRows<PrivilegeUser> queryUserByParam(PrivilegeUser param, Pagination page) throws ServiceException;

    /**
     * 查询角色列表
     *
     * @param param
     * @param page
     * @return
     * @throws ServiceException
     */
    public PageRows<PrivilegeRole> queryRoleByParam(PrivilegeRole param, Pagination page) throws ServiceException;

    /**
     * 查询资源
     *
     * @param param
     * @param page
     * @return
     * @throws ServiceException
     */
    public PageRows<PrivilegeResource> queryResByParam(PrivilegeResource param, Pagination page) throws ServiceException;

    /**
     * 修改用户信息
     *
     * @param entity
     * @return
     * @throws ServiceException
     */
    public boolean modifyUser(PrivilegeUser entity, Map<ObjectField, Object> map) throws ServiceException;

    /**
     * 修改密码
     *
     * @param pwd
     * @param uno
     * @return
     * @throws ServiceException
     */
    public boolean modifyPwd(String pwd, Integer uno) throws ServiceException;

    /**
     * 停用账户
     *
     * @param ustatus
     * @param uno
     * @return
     * @throws ServiceException
     */
    public boolean switchUser(String ustatus, Integer uno) throws ServiceException;

    /**
     * 删除用户
     *
     * @param uno
     * @return
     * @throws ServiceException
     */
    public boolean deleteUser(Integer uno) throws ServiceException;

    /**
     * 删除角色
     *
     * @param rid
     * @return
     * @throws ServiceException
     */
    public boolean deleteRole(Integer rid) throws ServiceException;

    /**
     * 删除资源
     *
     * @param status
     * @param rsid
     * @return
     * @throws ServiceException
     */
    public boolean deleteRes(String status, Integer rsid) throws ServiceException;

    /**
     * 增加用户
     *
     * @param entity
     * @return
     * @throws ServiceException
     */
    public boolean saveUser(PrivilegeUser entity) throws ServiceException;

    /**
     * 增加角色
     *
     * @param entity
     * @return
     * @throws ServiceException
     */
    public boolean saveRole(PrivilegeRole entity) throws ServiceException;

    /**
     * 增加资源
     *
     * @param entity
     * @return
     * @throws ServiceException
     */
    public boolean saveRes(PrivilegeResource entity) throws ServiceException;

    /**
     * 修改角色
     *
     * @param entity
     * @return
     * @throws ServiceException
     */
    public boolean modifyRole(PrivilegeRole entity, Map<ObjectField, Object> map) throws ServiceException;

    /**
     * 修改资源
     *
     * @param entity
     * @return
     * @throws ServiceException
     */
    public boolean modifyRes(PrivilegeResource entity, Map<ObjectField, Object> map) throws ServiceException;

    /**
     * 根据用户名密码查询用户
     *
     * @param userid
     * @param pwd
     * @return
     * @throws ServiceException
     */
    public PrivilegeUser getUserByUserIdPwd(String userid, String pwd) throws ServiceException;

    /**
     * 拼接资源树需要的数据
     *
     * @param entity
     * @return
     * @throws ServiceException
     */
    public Set<PrivilegeResource> queryResByRoleId(PrivilegeRole entity) throws ServiceException;

    // res

    /**
     * 根据ID查询权限资源
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    public PrivilegeResource findResById(Integer id) throws ServiceException;

    /**
     * 根据状态查询权限资源
     *
     * @param status
     * @return
     * @throws ServiceException
     */
    public List<PrivilegeResource> queryAllResByStatus(ActStatus status) throws ServiceException;

    /**
     * 保存日志                                                                                         z
     *
     * @param entity
     * @throws ServiceException
     */
    public ToolsLog saveLog(ToolsLog entity) throws ServiceException;

    /**
     * 查询操作日志
     *
     * @param id
     * @param date
     * @return
     * @throws ServiceException
     */
    public ToolsLog getLog(long id, Date date) throws ServiceException;

    /**
     * 查询登录日志
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    public ToolsLoginLog getLoginLog(long id) throws ServiceException;

    /**
     * 保存登录日志
     *
     * @param entity
     * @return
     * @throws ServiceException
     */
    public ToolsLoginLog saveLoginLog(ToolsLoginLog entity) throws ServiceException;

    /**
     * 查询操作列表
     *
     * @param entity
     * @param p
     * @return
     * @throws ServiceException
     */
    public PageRows<ToolsLog> queryLogs(ToolsLog entity, Pagination p) throws ServiceException;

    /**
     * 查询登录列表
     *
     * @param entity
     * @param p
     * @return
     * @throws ServiceException
     */
    public PageRows<ToolsLoginLog> queryLoginLogs(ToolsLoginLog entity, Pagination p) throws ServiceException;


    public PrivilegeResource getResourceByRsid(int rsid) throws ServiceException;


    //
    public StatsEditor createStatsEditor(StatsEditor statsEditor) throws ServiceException;

    public StatsEditor getStatsEditor(QueryExpress getExpress) throws ServiceException;

    public boolean modifyStatsEditor(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    public PageRows<StatsEditor> queryStatsEditorByPage(QueryExpress queryExpress, Pagination page) throws ServiceException;

    public List<StatsEditor> queryStatsEditor(QueryExpress queryExpress) throws ServiceException;


    public StatsEditorItem createStatsEditorItem(StatsEditorItem editorItem) throws ServiceException;

    public StatsEditorItem getStatsEditorItem(QueryExpress getExpress) throws ServiceException;

    public boolean modifyStatsEditorItem(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    public List<StatsEditorItem> queryStatsEditorItem(QueryExpress queryExpress) throws ServiceException;

    public PageRows<StatsEditorItem> queryStatsEditorItemByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;
}
