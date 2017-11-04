package com.enjoyf.platform.service.tools;

import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 上午11:20
 * Desc:
 */
public class ToolsServiceImpl implements ToolsService {

    private ReqProcessor reqProcessor = null;
    private int numOfPartitions;

    public ToolsServiceImpl(ServiceConfig scfg) {
        if (scfg == null) {
            throw new RuntimeException("ToolsServiceImpl.ctor: ServiceConfig is null!");
        }

        //
        reqProcessor = scfg.getReqProcessor();
        numOfPartitions = EnvConfig.get().getServicePartitionNum(ToolsConstants.SERVICE_SECTION);
    }

    @Override
    public AuditUser createAuditUser(AuditUser auditUser) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(auditUser);

        Request req = new Request(ToolsConstants.AUDIT_CONTENT_USER_CREATE, wp);

        RPacket rp = reqProcessor.process(req);

        return (AuditUser) rp.readSerializable();
    }

    @Override
    public AuditContent createAuditContent(AuditContent auditContent) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(auditContent);

        Request req = new Request(ToolsConstants.AUDIT_CONTENT_CREATE, wp);

        RPacket rp = reqProcessor.process(req);

        return (AuditContent) rp.readSerializable();
    }

    @Override
    public List<AuditUser> queryAuditUserByUnos(List<AuditUserQueryEntity> auditUserQueryEntityList) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) auditUserQueryEntityList);

        Request req = new Request(ToolsConstants.AUDIT_CONTENT_USER_QUERY_UNOS, wp);

        RPacket rp = reqProcessor.process(req);

        return (List<AuditUser>) rp.readSerializable();

    }


    @Override
    public List<AuditContent> queryAuditContentByIds(List<AuditContentQueryEntity> auditContentQueryEntityList) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) auditContentQueryEntityList);

        Request req = new Request(ToolsConstants.AUDIT_CONTENT_QUERY_OBJS, wp);

        RPacket rp = reqProcessor.process(req);

        return (List<AuditContent>) rp.readSerializable();
    }

    @Override
    public PrivilegeUser getUserByLoginName(String loginName) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(loginName);

        Request req = new Request(ToolsConstants.PRIVILEGE_USER_FIND_BY_ID, wp);
        RPacket rp = reqProcessor.process(req);

        return (PrivilegeUser) rp.readSerializable();
    }

    @Override
    public PrivilegeRole getRoleByRoleName(String roleName) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(roleName);

        Request req = new Request(ToolsConstants.PRIVILEGE_ROLES_FIND_BY_ROLENAME, wp);
        RPacket rp = reqProcessor.process(req);

        return (PrivilegeRole) rp.readSerializable();
    }

    public PrivilegeRole getRoleByRid(Integer rid) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeIntNx(rid);

        Request request = new Request(ToolsConstants.PRIVILEGE_ROLES_FIND_BY_ROLEID, wp);
        RPacket rPacket = reqProcessor.process(request);

        return (PrivilegeRole) rPacket.readSerializable();
    }

    @Override
    public boolean insertRoleMenu(Integer rid, String[] menuids) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeIntNx(rid);
        wp.writeSerializable((Serializable) menuids);

        Request req = new Request(ToolsConstants.PRIVILEGE_ROLES_AND_RES_SAVE, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public List<PrivilegeRole> queryRoles() throws ServiceException {
        WPacket wp = new WPacket();

        Request req = new Request(ToolsConstants.PRIVILEGE_ROLES_SET_BY_STATUS, wp);
        RPacket rp = reqProcessor.process(req);

        return (List<PrivilegeRole>) rp.readSerializable();
    }

    @Override
    public boolean insertUserRole(Integer uno, String[] rolseids) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeIntNx(uno);
        wp.writeSerializable((Serializable) rolseids);

        Request req = new Request(ToolsConstants.PRIVILEGE_USER_ROLES_SAVE, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public boolean deleteUserRole(Integer uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeIntNx(uno);

        Request req = new Request(ToolsConstants.PRIVILEGE_USER_ROLES_DELETE, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public List<PrivilegeResource> queryResMenu(String rstype, String status) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(rstype);
        wp.writeStringUTF(status);

        Request req = new Request(ToolsConstants.PRIVILEGE_RES_MENU_FIND_BY_ENTITY, wp);
        RPacket rp = reqProcessor.process(req);

        return (List<PrivilegeResource>) rp.readSerializable();
    }

    @Override
    public PrivilegeResource getResByRsurl(String rsurl) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(rsurl);

        Request req = new Request(ToolsConstants.PRIVILEGE_RES_FIND_BY_RSURL, wp);
        RPacket rp = reqProcessor.process(req);

        return (PrivilegeResource) rp.readSerializable();
    }

    @Override
    public PrivilegeUser getByUno(Integer uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeIntNx(uno);

        Request req = new Request(ToolsConstants.PRIVILEGE_USER_FIND_BY_UNO, wp);
        RPacket rp = reqProcessor.process(req);

        return (PrivilegeUser) rp.readSerializable();
    }

    @Override
    public PageRows<PrivilegeUser> queryUserByParam(PrivilegeUser param, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(param);
        wp.writeSerializable(page);

        Request req = new Request(ToolsConstants.PRIVILEGE_USER_QUERY_BY_PARAM, wp);
        RPacket rp = reqProcessor.process(req);

        return (PageRows<PrivilegeUser>) rp.readSerializable();
    }

    @Override
    public PageRows<PrivilegeRole> queryRoleByParam(PrivilegeRole param, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(param);
        wp.writeSerializable(page);

        Request req = new Request(ToolsConstants.PRIVILEGE_ROLES_QUERY_BY_PARAM, wp);
        RPacket rp = reqProcessor.process(req);

        return (PageRows<PrivilegeRole>) rp.readSerializable();
    }

    @Override
    public PageRows<PrivilegeResource> queryResByParam(PrivilegeResource param, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(param);
        wp.writeSerializable(page);

        Request req = new Request(ToolsConstants.PRIVILEGE_RES_QUERY_BY_PARAM, wp);
        RPacket rp = reqProcessor.process(req);

        return (PageRows<PrivilegeResource>) rp.readSerializable();
    }

    public PrivilegeUser getUserByUserIdPwd(String userid, String pwd) throws ServiceException {

        WPacket wp = new WPacket();
        wp.writeStringUTF(userid);
        wp.writeStringUTF(pwd);

        Request req = new Request(ToolsConstants.PRIVILEGE_USER_FIND_BY_ID_PWD, wp);
        RPacket rp = reqProcessor.process(req);

        return (PrivilegeUser) rp.readSerializable();
    }

    @Override
    public Set<PrivilegeResource> queryResByRoleId(PrivilegeRole entity) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) entity);

        Request req = new Request(ToolsConstants.PRIVILEGE_RES_FIND_BY_ENTITY, wp);

        RPacket rp = reqProcessor.process(req);

        return (Set<PrivilegeResource>) rp.readSerializable();
    }

    public boolean modifyUser(PrivilegeUser entity, Map<ObjectField, Object> map) throws ServiceException {

        WPacket wp = new WPacket();
        wp.writeSerializable(entity);
        wp.writeSerializable((Serializable) map);

        Request req = new Request(ToolsConstants.PRIVILEGE_USER_MODIFY_BY_ENTITY, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    public boolean modifyPwd(String pwd, Integer uno) throws ServiceException {

        WPacket wp = new WPacket();

        wp.writeString(pwd);
        wp.writeIntNx(uno);

        Request req = new Request(ToolsConstants.PRIVILEGE_USER_MODIFY_PWD_BY_ENTITY, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    public boolean modifyRole(PrivilegeRole entity, Map<ObjectField, Object> map) throws ServiceException {

        WPacket wp = new WPacket();
        wp.writeSerializable(entity);
        wp.writeSerializable((Serializable) map);

        Request req = new Request(ToolsConstants.PRIVILEGE_ROLES_MODIFY_BY_ENTITY, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    public boolean modifyRes(PrivilegeResource entity, Map<ObjectField, Object> map) throws ServiceException {

        WPacket wp = new WPacket();
        wp.writeSerializable(entity);
        wp.writeSerializable((Serializable) map);

        Request req = new Request(ToolsConstants.PRIVILEGE_RES_MODIFY_BY_ENTITY, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    public boolean switchUser(String ustatus, Integer uno) throws ServiceException {

        WPacket wp = new WPacket();

        wp.writeString(ustatus);
        wp.writeIntNx(uno);

        Request req = new Request(ToolsConstants.PRIVILEGE_USER_SWITCH_BY_ENTITY, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }


    public boolean saveUser(PrivilegeUser entity) throws ServiceException {

        WPacket wp = new WPacket();
        wp.writeSerializable(entity);

        Request req = new Request(ToolsConstants.PRIVILEGE_USER_SAVE_BY_ENTITY, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    public boolean saveRole(PrivilegeRole entity) throws ServiceException {

        WPacket wp = new WPacket();
        wp.writeSerializable(entity);

        Request req = new Request(ToolsConstants.PRIVILEGE_ROLES_SAVE_BY_ENTITY, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    public boolean saveRes(PrivilegeResource entity) throws ServiceException {

        WPacket wp = new WPacket();
        wp.writeSerializable(entity);

        Request req = new Request(ToolsConstants.PRIVILEGE_RES_SAVE_BY_ENTITY, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    public boolean deleteUser(Integer uno) throws ServiceException {

        WPacket wp = new WPacket();
        wp.writeIntNx(uno);

        Request req = new Request(ToolsConstants.PRIVILEGE_USER_DELETE_BY_ENTITY, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    public boolean deleteRole(Integer rid) throws ServiceException {

        WPacket wp = new WPacket();
        wp.writeIntNx(rid);

        Request req = new Request(ToolsConstants.PRIVILEGE_ROLES_DELETE_BY_ENTITY, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    public boolean deleteRes(String status, Integer rsid) throws ServiceException {

        WPacket wp = new WPacket();

        wp.writeString(status);
        wp.writeIntNx(rsid);

        Request req = new Request(ToolsConstants.PRIVILEGE_RES_DELETE_BY_ENTITY, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    /**
     * 根据ID查询权限资源
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    @Override
    public PrivilegeResource findResById(Integer id) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeIntNx(id);

        Request req = new Request(ToolsConstants.PRIVILEGE_RES_FIND_BY_ID, wp);
        RPacket rp = reqProcessor.process(req);

        return (PrivilegeResource) rp.readSerializable();
    }

    /**
     * 根据状态查询权限资源
     *
     * @param status
     * @return
     * @throws ServiceException
     */
    @Override
    public List<PrivilegeResource> queryAllResByStatus(ActStatus status) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(status);

        Request req = new Request(ToolsConstants.PRIVILEGE_RES_QUERY_BY_STATUS, wp);
        RPacket rp = reqProcessor.process(req);

        return (List<PrivilegeResource>) rp.readSerializable();
    }


    // log
    public ToolsLog saveLog(ToolsLog entity) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(entity);

        Request req = new Request(ToolsConstants.TOOLS_LOG_ADD, wp);
        RPacket rp = reqProcessor.process(req);

        return (ToolsLog) rp.readSerializable();
    }

    public ToolsLog getLog(long id, Date date) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(id);
        wp.writeSerializable(date);

        Request req = new Request(ToolsConstants.TOOLS_LOG_QUERY, wp);
        RPacket rp = reqProcessor.process(req);

        return (ToolsLog) rp.readSerializable();
    }

    public ToolsLoginLog getLoginLog(long id) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(id);

        Request req = new Request(ToolsConstants.TOOLS_LOG_LOGIN_QUERY, wp);
        RPacket rp = reqProcessor.process(req);

        return (ToolsLoginLog) rp.readSerializable();
    }


    @Override
    public ToolsLoginLog saveLoginLog(ToolsLoginLog entity) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(entity);

        Request req = new Request(ToolsConstants.TOOLS_LOG_LOGIN_ADD, wp);
        RPacket rp = reqProcessor.process(req);

        return (ToolsLoginLog) rp.readSerializable();
    }

    @Override
    public PageRows<ToolsLog> queryLogs(ToolsLog param, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(param);
        wp.writeSerializable(page);

        Request req = new Request(ToolsConstants.TOOLS_LOG_QUE, wp);
        RPacket rp = reqProcessor.process(req);

        return (PageRows<ToolsLog>) rp.readSerializable();
    }

    @Override
    public PageRows<ToolsLoginLog> queryLoginLogs(ToolsLoginLog param, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(param);
        wp.writeSerializable(page);

        Request req = new Request(ToolsConstants.TOOLS_LOG_LOGIN_QUE, wp);
        RPacket rp = reqProcessor.process(req);

        return (PageRows<ToolsLoginLog>) rp.readSerializable();
    }

    public PrivilegeResource getResourceByRsid(int rsid) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeIntNx(rsid);

        Request req = new Request(ToolsConstants.PRIVILEGE_RES_GET_BY_RSID, wp);
        RPacket rp = reqProcessor.process(req);

        return (PrivilegeResource) rp.readSerializable();
    }


    @Override
    public PageRows<StatsEditor> queryStatsEditorByPage(QueryExpress queryExpress, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(page);

        Request req = new Request(ToolsConstants.EDITOR_QUERY_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);

        return (PageRows<StatsEditor>) rp.readSerializable();
    }

    @Override
    public List<StatsEditor> queryStatsEditor(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request req = new Request(ToolsConstants.EDITOR_QUERY, wp);
        RPacket rp = reqProcessor.process(req);

        return (List<StatsEditor>) rp.readSerializable();
    }

    @Override
    public boolean modifyStatsEditor(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeSerializable(queryExpress);

        Request req = new Request(ToolsConstants.EDITOR_MODIFY, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public StatsEditor getStatsEditor(QueryExpress getExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(getExpress);

        Request req = new Request(ToolsConstants.EDITOR_GET, wp);
        RPacket rp = reqProcessor.process(req);

        return (StatsEditor) rp.readSerializable();
    }

    @Override
    public StatsEditor createStatsEditor(StatsEditor statsEditor) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(statsEditor);

        Request req = new Request(ToolsConstants.EDITOR_CREATE, wp);
        RPacket rp = reqProcessor.process(req);

        return (StatsEditor) rp.readSerializable();
    }


    @Override
    public StatsEditorItem createStatsEditorItem(StatsEditorItem editorItem) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(editorItem);

        Request req = new Request(ToolsConstants.STATS_EDITOR_ITEM_CREATE, wp);
        RPacket rp = reqProcessor.process(req);

        return (StatsEditorItem) rp.readSerializable();
    }

    @Override
    public StatsEditorItem getStatsEditorItem(QueryExpress getExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(getExpress);

        Request req = new Request(ToolsConstants.STATS_EDITOR_ITEM_GET, wp);
        RPacket rp = reqProcessor.process(req);

        return (StatsEditorItem) rp.readSerializable();
    }

    @Override
    public boolean modifyStatsEditorItem(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeSerializable(queryExpress);

        Request req = new Request(ToolsConstants.STATS_EDITOR_ITEM_MODIFY, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public PageRows<StatsEditorItem> queryStatsEditorItemByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);

        Request req = new Request(ToolsConstants.STATS_EDITOR_ITEM_QUERY_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);

        return (PageRows<StatsEditorItem>) rp.readSerializable();
    }

    @Override
    public List<StatsEditorItem> queryStatsEditorItem(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request req = new Request(ToolsConstants.STATS_EDITOR_ITEM_QUERY, wp);
        RPacket rp = reqProcessor.process(req);

        return (List<StatsEditorItem>) rp.readSerializable();
    }
}
