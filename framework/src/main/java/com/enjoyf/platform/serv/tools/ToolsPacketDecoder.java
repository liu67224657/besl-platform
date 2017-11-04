package com.enjoyf.platform.serv.tools;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 下午2:22
 * Desc:
 */
public class ToolsPacketDecoder extends PacketDecoder {

    private ToolsLogic processLogic;


    ToolsPacketDecoder(ToolsLogic logic) {
        processLogic = logic;

        setTransContainer(ToolsConstants.getTransContainer());
    }


    /**
     * This function should be implemented to call the server's logic.
     */
    @Override
    protected WPacket logicProcess(ConnThreadBase conn, RPacket rPacket) throws ServiceException {
        byte type = rPacket.getType();

        WPacket wp = new WPacket();
        wp.writeByteNx(ServiceConstants.OK);

        switch (type) {

            case ToolsConstants.AUDIT_CONTENT_USER_CREATE:
                wp.writeSerializable(processLogic.createAuditUser((AuditUser) rPacket.readSerializable()));
                break;
            case ToolsConstants.AUDIT_CONTENT_CREATE:
                wp.writeSerializable(processLogic.createAuditContent((AuditContent) rPacket.readSerializable()));

                break;
            case ToolsConstants.AUDIT_CONTENT_USER_MODIFY:
                wp.writeSerializable(processLogic.createAuditUser((AuditUser) rPacket.readSerializable()));
                break;
            case ToolsConstants.AUDIT_CONTENT_MODIFY:
                wp.writeSerializable(processLogic.createAuditContent((AuditContent) rPacket.readSerializable()));
                break;
            case ToolsConstants.AUDIT_CONTENT_USER_QUERY_UNOS:
                wp.writeSerializable((Serializable) processLogic.queryAuditUserByUnos((List) rPacket.readSerializable()));
                break;
            case ToolsConstants.AUDIT_CONTENT_QUERY_OBJS:
                wp.writeSerializable((Serializable) processLogic.queryAuditContentByIds((List) rPacket.readSerializable()));
                break;
            case ToolsConstants.PRIVILEGE_USER_FIND_BY_ID:
                wp.writeSerializable(processLogic.getUserByLoginName(rPacket.readStringUTF()));
                break;
            case ToolsConstants.PRIVILEGE_RES_FIND_BY_ENTITY:
                wp.writeSerializable((Serializable) processLogic.queryResByRoleId((PrivilegeRole) rPacket.readSerializable()));
                break;
            case ToolsConstants.PRIVILEGE_ROLES_FIND_BY_ROLENAME:
                wp.writeSerializable(processLogic.getRoleByRoleName(rPacket.readStringUTF()));
                break;
            case ToolsConstants.PRIVILEGE_USER_ROLES_SAVE:
                wp.writeSerializable(processLogic.insertUserRole(rPacket.readIntNx(), (String[]) rPacket.readSerializable()));
                break;
            case ToolsConstants.PRIVILEGE_USER_ROLES_DELETE:
                wp.writeSerializable(processLogic.deleteUserRole(rPacket.readIntNx()));
                break;
            case ToolsConstants.PRIVILEGE_ROLES_AND_RES_SAVE:
                wp.writeSerializable(processLogic.insertRoleMenu(rPacket.readIntNx(), (String[]) rPacket.readSerializable()));
                break;
            case ToolsConstants.PRIVILEGE_ROLES_SET_BY_STATUS:
                wp.writeSerializable((Serializable) processLogic.queryRoles());
                break;
            case ToolsConstants.PRIVILEGE_RES_MENU_FIND_BY_ENTITY:
                wp.writeSerializable((Serializable) processLogic.queryResMenu(rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case ToolsConstants.PRIVILEGE_RES_FIND_BY_RSURL:
                wp.writeSerializable(processLogic.getResByRsurl(rPacket.readStringUTF()));
                break;

            case ToolsConstants.PRIVILEGE_USER_FIND_BY_ID_PWD:
                wp.writeSerializable(processLogic.getUserByUserIdPwd(rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;

            case ToolsConstants.PRIVILEGE_RES_FIND_BY_ID:
                wp.writeSerializable(processLogic.findResById(Integer.valueOf(rPacket.readIntNx())));
                break;
            case ToolsConstants.PRIVILEGE_RES_QUERY_BY_STATUS:
                wp.writeSerializable((Serializable) processLogic.queryAllResByStatus((ActStatus) (rPacket.readSerializable())));
                break;
            case ToolsConstants.TOOLS_LOG_ADD:
                wp.writeSerializable(processLogic.saveLog((ToolsLog) rPacket.readSerializable()));
                break;
            case ToolsConstants.TOOLS_LOG_QUERY:
                wp.writeSerializable(processLogic.getLog(rPacket.readLongNx(), (Date) rPacket.readSerializable()));
                break;
            case ToolsConstants.TOOLS_LOG_LOGIN_QUERY:
                wp.writeSerializable(processLogic.getLoginLog(rPacket.readLongNx()));
                break;
            case ToolsConstants.TOOLS_LOG_LOGIN_ADD:
                wp.writeSerializable((Serializable) processLogic.saveLoginLog((ToolsLoginLog) rPacket.readSerializable()));
                break;
            case ToolsConstants.TOOLS_LOG_QUE:
                wp.writeSerializable(processLogic.queryLogs((ToolsLog) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case ToolsConstants.TOOLS_LOG_LOGIN_QUE:
                wp.writeSerializable(processLogic.queryLoginLogs((ToolsLoginLog) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case ToolsConstants.PRIVILEGE_USER_QUERY_BY_PARAM:
                wp.writeSerializable(processLogic.queryUserByParam((PrivilegeUser) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case ToolsConstants.PRIVILEGE_ROLES_QUERY_BY_PARAM:
                wp.writeSerializable(processLogic.queryRoleByParam((PrivilegeRole) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case ToolsConstants.PRIVILEGE_RES_QUERY_BY_PARAM:
                wp.writeSerializable(processLogic.queryResByParam((PrivilegeResource) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case ToolsConstants.PRIVILEGE_USER_MODIFY_BY_ENTITY:
                wp.writeSerializable(processLogic.modifyUser((PrivilegeUser) rPacket.readSerializable(), (Map<ObjectField, Object>) rPacket.readSerializable()));
                break;
            case ToolsConstants.PRIVILEGE_USER_MODIFY_PWD_BY_ENTITY:
                wp.writeSerializable(processLogic.modifyPwd(rPacket.readString(), rPacket.readIntNx()));
                break;
            case ToolsConstants.PRIVILEGE_ROLES_MODIFY_BY_ENTITY:
                wp.writeSerializable(processLogic.modifyRole((PrivilegeRole) rPacket.readSerializable(), (Map<ObjectField, Object>) rPacket.readSerializable()));
                break;
            case ToolsConstants.PRIVILEGE_RES_MODIFY_BY_ENTITY:
                wp.writeSerializable(processLogic.modifyRes((PrivilegeResource) rPacket.readSerializable(), (Map<ObjectField, Object>) rPacket.readSerializable()));
                break;
            case ToolsConstants.PRIVILEGE_USER_SAVE_BY_ENTITY:
                wp.writeSerializable(processLogic.saveUser((PrivilegeUser) rPacket.readSerializable()));
                break;
            case ToolsConstants.PRIVILEGE_ROLES_SAVE_BY_ENTITY:
                wp.writeSerializable(processLogic.saveRole((PrivilegeRole) rPacket.readSerializable()));
                break;
            case ToolsConstants.PRIVILEGE_RES_SAVE_BY_ENTITY:
                wp.writeSerializable(processLogic.saveRes((PrivilegeResource) rPacket.readSerializable()));
                break;
            case ToolsConstants.PRIVILEGE_ROLES_DELETE_BY_ENTITY:
                wp.writeSerializable(processLogic.deleteRole(rPacket.readIntNx()));
                break;
            case ToolsConstants.PRIVILEGE_USER_DELETE_BY_ENTITY:
                wp.writeSerializable(processLogic.deleteUser(rPacket.readIntNx()));
                break;
            case ToolsConstants.PRIVILEGE_RES_DELETE_BY_ENTITY:
                wp.writeSerializable(processLogic.deleteRes(rPacket.readString(), rPacket.readIntNx()));
                break;
            case ToolsConstants.PRIVILEGE_USER_FIND_BY_UNO:
                wp.writeSerializable(processLogic.getByUno(rPacket.readIntNx()));
                break;
            case ToolsConstants.PRIVILEGE_USER_SWITCH_BY_ENTITY:
                wp.writeSerializable(processLogic.switchUser(rPacket.readString(), rPacket.readIntNx()));
                break;
            case ToolsConstants.PRIVILEGE_ROLES_FIND_BY_ROLEID:
                wp.writeSerializable(processLogic.getRoleByRid(rPacket.readIntNx()));
                break;
            case ToolsConstants.PRIVILEGE_RES_GET_BY_RSID:
                wp.writeSerializable(processLogic.getResourceByRsid(rPacket.readIntNx()));
                break;
            ////////////////////////////////////////////////////////////
            case ToolsConstants.EDITOR_CREATE:
                wp.writeSerializable(processLogic.createStatsEditor((StatsEditor) rPacket.readSerializable()));
                break;
            case ToolsConstants.EDITOR_MODIFY:
                wp.writeSerializable(processLogic.modifyStatsEditor((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            case ToolsConstants.EDITOR_GET:
                wp.writeSerializable(processLogic.getStatsEditor((QueryExpress) rPacket.readSerializable()));
                break;
            case ToolsConstants.EDITOR_QUERY_BY_PAGE:
                wp.writeSerializable(processLogic.queryStatsEditorByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case ToolsConstants.EDITOR_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryStatsEditor((QueryExpress) rPacket.readSerializable()));
                break;

            case ToolsConstants.STATS_EDITOR_ITEM_CREATE:
                wp.writeSerializable(processLogic.createStatsEditorItem((StatsEditorItem) rPacket.readSerializable()));
                break;
            case ToolsConstants.STATS_EDITOR_ITEM_MODIFY:
                wp.writeSerializable(processLogic.modifyStatsEditorItem((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            case ToolsConstants.STATS_EDITOR_ITEM_GET:
                wp.writeSerializable(processLogic.getStatsEditorItem((QueryExpress) rPacket.readSerializable()));
                break;
            case ToolsConstants.STATS_EDITOR_ITEM_QUERY_BY_PAGE:
                wp.writeSerializable(processLogic.queryStatsEditorItemByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case ToolsConstants.STATS_EDITOR_ITEM_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryStatsEditorItem((QueryExpress) rPacket.readSerializable()));
                break;
            default:
                GAlerter.lab("ToolsPacketDecoder.logicProcess: Unrecognized type: " + type);
                throw new ServiceException(ServiceException.BAD_PACKET);
        }

        return wp;
    }
}
