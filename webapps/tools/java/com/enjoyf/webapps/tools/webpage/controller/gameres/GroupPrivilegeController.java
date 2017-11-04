package com.enjoyf.webapps.tools.webpage.controller.gameres;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.gameres.GameResourceField;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.ResourceDomain;
import com.enjoyf.platform.service.gameres.privilege.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.tools.weblogic.dto.GroupRoleDTO;
import com.enjoyf.webapps.tools.weblogic.dto.game.GroupRolePrivilegeDTO;
import com.enjoyf.webapps.tools.weblogic.gameres.GameResourceWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-17
 * Time: 上午11:09
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
@Controller
@RequestMapping(value = "/gameresource/group")
public class GroupPrivilegeController extends ToolsBaseController {
    private Logger logger = LoggerFactory.getLogger(GameResourceController.class);

    @Resource(name = "gameResourceWebLogic")
    private GameResourceWebLogic gameResourceWebLogic;

    @RequestMapping(value = "/role/list")
    public ModelAndView list(@RequestParam(value = "groupid", required = false) Long groupId) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        try {
            List<GameResource> groupList = gameResourceWebLogic.queryGroupResources(new QueryExpress().add(QueryCriterions.eq(GameResourceField.REMOVESTATUS, ActStatus.UNACT.getCode())).add(QueryCriterions.eq(GameResourceField.RESOURCEDOMAIN, ResourceDomain.GROUP.getCode())));
            if (!CollectionUtil.isEmpty(groupList)) {
                msgMap.put("groupList", groupList);
            }
            if (groupId == null) {
                return new ModelAndView("/gameresource/group/rolelist", msgMap);
            }
            Long[] groupIds = new Long[2];
            groupIds[0] = 0l;
            if (groupId != null && groupId > 0) {
                msgMap.put("groupId", groupId);
                groupIds[1] = groupId;
            }
            List<Role> roleList = GameResourceServiceSngl.get().queryRole(new QueryExpress().add(QueryCriterions.in(RoleField.GROUP_ID, groupIds)));
            if (!CollectionUtil.isEmpty(roleList)) {
                msgMap.put("roleList", roleList);
            }
        } catch (ServiceException e) {
            GAlerter.lab("GameResourceWebLogic queryGroupResources, ServiceException :", e);
        }
        return new ModelAndView("/gameresource/group/rolelist", msgMap);
    }

    @RequestMapping(value = "/role/createpage")
    public ModelAndView createExpandPage(@RequestParam(value = "groupid", required = false) Long groupId) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        if (groupId == null) {
            return new ModelAndView("redirect:/gameresource/group/role/list", msgMap);
        }
        msgMap.put("profileTypeCollection", RoleLevel.getAll());
        msgMap.put("groupId", groupId);
        try {
            GameResource group = gameResourceWebLogic.getGameResourceById(groupId);
            if (group != null) {
                msgMap.put("group", group);
            }
        } catch (ServiceException e) {
            GAlerter.lab("GameResourceWebLogic getGameResourceById, ServiceException :", e);
        }
        return new ModelAndView("gameresource/group/createrole", msgMap);
    }

    @RequestMapping(value = "/role/create")
    public ModelAndView createExpand(@RequestParam(value = "groupid", required = false) Long groupId,
                                     @RequestParam(value = "rolename", required = false) String roleName,
                                     @RequestParam(value = "roledesc", required = false) String roleDesc,
                                     @RequestParam(value = "rolelevel", required = false) Integer roleLevel) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        if (groupId == null) {
            return new ModelAndView("redirect:/gameresource/group/role/list", msgMap);
        }
        try {
            Role existRole = GameResourceServiceSngl.get().getRole(new QueryExpress().add(QueryCriterions.eq(RoleField.GROUP_ID, groupId)).add(QueryCriterions.eq(RoleField.ROLE_NAME, roleName)).add(QueryCriterions.eq(RoleField.STATUS, ActStatus.UNACT.getCode())));
            if (existRole != null) {
                msgMap.put("errorMsg", "group.role.has.exist");
                msgMap.put("groupid", groupId);
                return new ModelAndView("forward:/gameresource/group/role/createexpandpage", msgMap);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " getRoleByQueryExpress, ServiceException :", e);
        }
        Role role = new Role();
        role.setGroupId(groupId);
        role.setRoleName(roleName);
        role.setRoleDesc(roleDesc);
        role.setRoleLevel(RoleLevel.getByCode(roleLevel));
        role.setRoleType(RoleType.EXPAND);
        role.setActStatus(ActStatus.UNACT);
        role.setCreateDate(new Date());
        role.setCreateIp(getIp());
        role.setCreateUserId(getCurrentUser().getUserid());
        try {
            GameResourceServiceSngl.get().createRole(role);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " createRole, ServiceException :", e);
        }
        return new ModelAndView("redirect:/gameresource/group/role/list?groupid=" + groupId, msgMap);
    }

    @RequestMapping(value = "/role/modifypage")
    public ModelAndView modifyRolePage(@RequestParam(value = "roleid", required = false) Long roleId) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        if (roleId == null) {
            return new ModelAndView("redirect:/gameresource/group/role/list", msgMap);
        }
        msgMap.put("profileTypeCollection", RoleLevel.getAll());
        try {
            Role role = GameResourceServiceSngl.get().getRole(new QueryExpress().add(QueryCriterions.eq(RoleField.ROLE_ID, roleId)));
            if (role == null) {
                return new ModelAndView("redirect:/gameresource/group/role/list", msgMap);
            }
            msgMap.put("role", role);

            GameResource group = gameResourceWebLogic.getGameResourceById(role.getGroupId());
            if (group != null) {
                msgMap.put("group", group);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " getRole, ServiceException :", e);
        }
        return new ModelAndView("/gameresource/group/modifyrole", msgMap);
    }

    @RequestMapping(value = "/role/modify")
    public ModelAndView modifyRole(@RequestParam(value = "groupid", required = false) Long groupId,
                                   @RequestParam(value = "roleid", required = false) Long roleId,
                                   @RequestParam(value = "rolename", required = false) String roleName,
                                   @RequestParam(value = "roledesc", required = false) String roleDesc,
                                   @RequestParam(value = "rolelevel", required = false) Integer roleLevel) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        Role existRole = null;
        try {
            existRole = GameResourceServiceSngl.get().getRole(new QueryExpress().add(QueryCriterions.eq(RoleField.GROUP_ID, groupId)).add(QueryCriterions.eq(RoleField.ROLE_NAME, roleName)).add(QueryCriterions.ne(RoleField.ROLE_ID, roleId)).add(QueryCriterions.eq(RoleField.STATUS, ActStatus.UNACT.getCode())));
            if (existRole != null) {
                msgMap.put("errorMsg", "group.role.has.exist");
                msgMap.put("roleid", roleId);
                return new ModelAndView("forward:/gameresource/group/role/modifypage", msgMap);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " getRoleByQueryExpress, ServiceException :", e);
        }
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(RoleField.ROLE_NAME, roleName);
        updateExpress.set(RoleField.ROLE_DESC, roleDesc);
        updateExpress.set(RoleField.ROLE_LEVEL, roleLevel);
        updateExpress.set(RoleField.LAST_MODIFY_TIME, new Date());
        updateExpress.set(RoleField.LAST_MODIFY_IP, getIp());
        updateExpress.set(RoleField.LAST_MODIFY_USERID, getCurrentUser().getUserid());
        try {
            boolean bool = GameResourceServiceSngl.get().modifyRole(roleId, groupId, updateExpress);
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.GROUP_MODIFY_ROLE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpUserId(getCurrentUser().getUserid());
                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " getRole, ServiceException :", e);
        }
        return new ModelAndView("redirect:/gameresource/group/role/list?groupid=" + groupId, msgMap);
    }

    @RequestMapping(value = "/role/delete")
    public ModelAndView deleteRole(@RequestParam(value = "groupid", required = false) Long groupId,
                                   @RequestParam(value = "roleid", required = false) Long roleId) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(RoleField.STATUS, ActStatus.ACTED.getCode());
        try {
            boolean bool = GameResourceServiceSngl.get().modifyRole(roleId, groupId, updateExpress);
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.GROUP_DELETE_ROLE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpUserId(getCurrentUser().getUserid());
                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " getRole, ServiceException :", e);
        }
        return new ModelAndView("redirect:/gameresource/group/role/list?groupid=" + groupId, msgMap);
    }

    @RequestMapping(value = "/role/recover")
    public ModelAndView recoverRole(@RequestParam(value = "groupid", required = false) Long groupId,
                                    @RequestParam(value = "roleid", required = false) Long roleId) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(RoleField.STATUS, ActStatus.UNACT.getCode());
        try {
            boolean bool = GameResourceServiceSngl.get().modifyRole(roleId, groupId, updateExpress);
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.GROUP_RECOVER_ROLE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpUserId(getCurrentUser().getUserid());
                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " getRole, ServiceException :", e);
        }
        return new ModelAndView("redirect:/gameresource/group/role/list?groupid=" + groupId, msgMap);
    }

    @RequestMapping(value = "/role/privilege/list")
    public ModelAndView bindPrivilegeList(@RequestParam(value = "roleid", required = false) Long roleId,
                                          @RequestParam(value = "groupid", required = false) Long groupId,
                                          @RequestParam(value = "privilegetype", required = false) Integer privilegeType) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put("roleId", roleId);
        msgMap.put("groupId", groupId);
        msgMap.put("typeCollection", GroupPrivilegeType.getAll());
        if (privilegeType == null) {
            return new ModelAndView("gameresource/group/roleprivilegelist", msgMap);
        }
        msgMap.put("privilegeType", privilegeType);
        try {
            List<GroupRolePrivilegeDTO> list = gameResourceWebLogic.queryRolePrivilege(groupId, roleId, privilegeType);
            if (CollectionUtil.isEmpty(list)) {
                return new ModelAndView("gameresource/group/roleprivilegelist", msgMap);
            }
            msgMap.put("list", list);
        } catch (ServiceException e) {
            GAlerter.lab("GameResourceWebLogic queryRolePrivilege, ServiceException :", e);
        }
        return new ModelAndView("/gameresource/group/roleprivilegelist", msgMap);
    }

    @RequestMapping(value = "/role/privilege/createpage")
    public ModelAndView bindPrivilegeCreatePage(@RequestParam(value = "groupid", required = false) Long groupId,
                                                @RequestParam(value = "roleid", required = false) Long roleId,
                                                @RequestParam(value = "privilegetype", required = false) Integer privilegeType) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put("groupId", groupId);
        msgMap.put("roleId", roleId);
        msgMap.put("privilegeType", privilegeType);
        try {
            GroupRoleDTO dto = gameResourceWebLogic.getGroupRoleDTO(groupId, roleId);
            if (dto != null) {
                msgMap.put("dto", dto);
            }
        } catch (ServiceException e) {
            GAlerter.lab("GameResourceWebLogic getGroupRoleDTO, ServiceException :", e);
        }
        try {
            List<GroupPrivilege> list = gameResourceWebLogic.queryGroupPrivilege(privilegeType, ActStatus.UNACT.getCode());
            if (!CollectionUtil.isEmpty(list)) {
                msgMap.put("list", list);
            }
        } catch (ServiceException e) {
            GAlerter.lab("GameResourceWebLogic queryRolePrivilege, ServiceException :", e);
        }
        return new ModelAndView("/gameresource/group/createroleprivilege", msgMap);
    }

    @RequestMapping(value = "/role/privilege/create")
    public ModelAndView bindPrivilegeCreate(@RequestParam(value = "groupid", required = false) Long groupId,
                                            @RequestParam(value = "roleid", required = false) Long roleId,
                                            @RequestParam(value = "privilegetype", required = false) Integer privilegeType,
                                            @RequestParam(value = "privilegeid", required = false) Long privilegeId) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        try {
            List<PrivilegeRoleRelation> existRelationList = GameResourceServiceSngl.get().queryPrivilegeRoleRelation(roleId);
            for (PrivilegeRoleRelation existRelation : existRelationList) {
                if (existRelation.getPrivilegeId() == privilegeId) {
                    msgMap.put("errorMsg", "group.role.privilege.has.exist");
                    msgMap.put("groupid", groupId);
                    msgMap.put("roleid", roleId);
                    msgMap.put("privilegetype", privilegeType);
                    return new ModelAndView("forward:/gameresource/group/role/privilege/createpage", msgMap);
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " queryPrivilegeRoleRelation, ServiceException :", e);
        }
        PrivilegeRoleRelation relation = new PrivilegeRoleRelation();
        relation.setRoleId(roleId);
        relation.setPrivilegeId(privilegeId);
        relation.setActStatus(ActStatus.UNACT);
        relation.setCreateDate(new Date());
        relation.setCreateIp(getIp());
        relation.setCreateUserId(getCurrentUser().getUserid());
        try {
            GameResourceServiceSngl.get().createPrivilegeRoleRelation(relation);
        } catch (ServiceException e) {
            GAlerter.lab("GameResourceWebLogic queryRolePrivilege, ServiceException :", e);
        }
        return new ModelAndView("redirect:/gameresource/group/role/privilege/list?roleid=" + roleId + "&groupid=" + groupId + "&privilegetype=" + privilegeType, msgMap);
    }

    @RequestMapping(value = "/role/privilege/modifypage")
    public ModelAndView bindPrivilegeModifyPage(@RequestParam(value = "relationid", required = false) Long relationId,
                                                @RequestParam(value = "groupid", required = false) Long groupId,
                                                @RequestParam(value = "roleid", required = false) Long roleId,
                                                @RequestParam(value = "privilegetype", required = false) Integer privilegeType,
                                                @RequestParam(value = "privilegeid", required = false) Long privilegeId) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put("groupId", groupId);
        msgMap.put("roleId", roleId);
        msgMap.put("privilegeType", privilegeType);
        msgMap.put("privilegeId", privilegeId);
        try {
            GroupRoleDTO dto = gameResourceWebLogic.getGroupRoleDTO(groupId, roleId);
            if (dto != null) {
                msgMap.put("dto", dto);
            }
        } catch (ServiceException e) {
            GAlerter.lab("GameResourceWebLogic getGroupRoleDTO, ServiceException :", e);
        }
        try {
            List<GroupPrivilege> list = gameResourceWebLogic.queryGroupPrivilege(privilegeType, ActStatus.UNACT.getCode());
            if (!CollectionUtil.isEmpty(list)) {
                msgMap.put("list", list);
            }
        } catch (ServiceException e) {
            GAlerter.lab("GameResourceWebLogic queryRolePrivilege, ServiceException :", e);
        }
        try {
            PrivilegeRoleRelation relation = GameResourceServiceSngl.get().getPrivilegeRoleRelation(new QueryExpress().add(QueryCriterions.eq(PrivilegeRoleRelationField.RELATION_ID, relationId)));
            if (relation != null) {
                msgMap.put("relation", relation);
            }
        } catch (ServiceException e) {
            GAlerter.lab("GameResourceWebLogic queryRolePrivilege, ServiceException :", e);
        }
        return new ModelAndView("/gameresource/group/modifyroleprivilege", msgMap);
    }

    @RequestMapping(value = "/role/privilege/modify")
    public ModelAndView bindPrivilegeModify(@RequestParam(value = "relationid", required = false) Long relationId,
                                            @RequestParam(value = "groupid", required = false) Long groupId,
                                            @RequestParam(value = "roleid", required = false) Long roleId,
                                            @RequestParam(value = "privilegetype", required = false) Integer privilegeType,
                                            @RequestParam(value = "privilegeid", required = false) Long privilegeId) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        try {
            List<PrivilegeRoleRelation> existRelationList = GameResourceServiceSngl.get().queryPrivilegeRoleRelation(roleId);
            for (PrivilegeRoleRelation existRelation : existRelationList) {
                if (existRelation.getRelationId() != relationId && existRelation.getPrivilegeId() == privilegeId) {
                    msgMap.put("errorMsg", "group.role.privilege.has.exist");
                    msgMap.put("groupid", groupId);
                    msgMap.put("roleid", roleId);
                    msgMap.put("privilegetype", privilegeType);
                    msgMap.put("relationid", relationId);
                    msgMap.put("privilegeid", privilegeId);
                    return new ModelAndView("forward:/gameresource/group/role/privilege/modifypage", msgMap);
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " queryPrivilegeRoleRelation, ServiceException :", e);
        }
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(PrivilegeRoleRelationField.PRIVILEGE_ID, privilegeId);
        updateExpress.set(PrivilegeRoleRelationField.LAST_MODIFY_TIME, new Date());
        updateExpress.set(PrivilegeRoleRelationField.LAST_MODIFY_IP, getIp());
        updateExpress.set(PrivilegeRoleRelationField.LAST_MODIFY_USERID, getCurrentUser().getUserid());
        try {
            boolean bool = GameResourceServiceSngl.get().modifyPrivilegeRoleRelation(relationId, roleId, updateExpress);
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.GROUP_MODIFY_ROLE_PRIVILEGE_RELATION);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpUserId(getCurrentUser().getUserid());
                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab("GameResourceWebLogic queryRolePrivilege, ServiceException :", e);
        }
        return new ModelAndView("redirect:/gameresource/group/role/privilege/list?roleid=" + roleId + "&groupid=" + groupId + "&privilegetype=" + privilegeType, msgMap);
    }

    @RequestMapping(value = "/role/privilege/delete")
    public ModelAndView bindPrivilegeDelete(@RequestParam(value = "relationid", required = false) Long relationId,
                                            @RequestParam(value = "groupid", required = false) Long groupId,
                                            @RequestParam(value = "roleid", required = false) Long roleId,
                                            @RequestParam(value = "privilegetype", required = false) Integer privilegeType) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(PrivilegeRoleRelationField.STATUS, ActStatus.ACTED.getCode());
        updateExpress.set(PrivilegeRoleRelationField.LAST_MODIFY_TIME, new Date());
        updateExpress.set(PrivilegeRoleRelationField.LAST_MODIFY_IP, getIp());
        updateExpress.set(PrivilegeRoleRelationField.LAST_MODIFY_USERID, getCurrentUser().getUserid());
        try {
            Boolean bool = GameResourceServiceSngl.get().modifyPrivilegeRoleRelation(relationId, roleId, updateExpress);
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.GROUP_DELETE_ROLE_PRIVILEGE_RELATION);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpUserId(getCurrentUser().getUserid());
                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab("GameResourceWebLogic queryRolePrivilege, ServiceException :", e);
        }
        return new ModelAndView("redirect:/gameresource/group/role/privilege/list?roleid=" + roleId + "&groupid=" + groupId + "&privilegetype=" + privilegeType, msgMap);
    }

    @RequestMapping(value = "/role/privilege/recover")
    public ModelAndView bindPrivilegeRecover(@RequestParam(value = "relationid", required = false) Long relationId,
                                             @RequestParam(value = "groupid", required = false) Long groupId,
                                             @RequestParam(value = "roleid", required = false) Long roleId,
                                             @RequestParam(value = "privilegetype", required = false) Integer privilegeType) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(PrivilegeRoleRelationField.STATUS, ActStatus.UNACT.getCode());
        updateExpress.set(PrivilegeRoleRelationField.LAST_MODIFY_TIME, new Date());
        updateExpress.set(PrivilegeRoleRelationField.LAST_MODIFY_IP, getIp());
        updateExpress.set(PrivilegeRoleRelationField.LAST_MODIFY_USERID, getCurrentUser().getUserid());
        try {
            boolean bool = GameResourceServiceSngl.get().modifyPrivilegeRoleRelation(relationId, roleId, updateExpress);
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.GROUP_RECOVER_ROLE_PRIVILEGE_RELATION);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpUserId(getCurrentUser().getUserid());
                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab("GameResourceWebLogic queryRolePrivilege, ServiceException :", e);
        }
        return new ModelAndView("redirect:/gameresource/group/role/privilege/list?roleid=" + roleId + "&groupid=" + groupId + "&privilegetype=" + privilegeType, msgMap);
    }

    @RequestMapping(value = "/privilege/list")
    public ModelAndView privilegeList(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                                      @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                                      @RequestParam(value = "privilegetype", required = false) Integer privilegeType) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put("privilegeTypeCollection", GroupPrivilegeType.getAll());
        if (privilegeType == null) {
            return new ModelAndView("gameresource/group/privilegelist", msgMap);
        }
        msgMap.put("privilegeType", privilegeType);

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(curPage * pageSize, curPage, pageSize);

        QueryExpress queryExpress = new QueryExpress();
        if (privilegeType != null) {
            queryExpress.add(QueryCriterions.eq(GroupPrivilegeField.PRIVILEGE_TYPE, privilegeType));
        }
        try {
            PageRows<GroupPrivilege> pageRows = GameResourceServiceSngl.get().queryGroupPrivilegeByPage(queryExpress, pagination);
            if (!CollectionUtil.isEmpty(pageRows.getRows())) {
                msgMap.put("list", pageRows.getRows());
                msgMap.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab("GameResourceWebLogic queryRolePrivilege, ServiceException :", e);
        }
        return new ModelAndView("gameresource/group/privilegelist", msgMap);
    }

    @RequestMapping(value = "/privilege/createpage")
    public ModelAndView privilegeCreatePage() {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put("privilegeTypeCollection", GroupPrivilegeType.getAll());
        msgMap.put("privilegeCodeCollection", GroupPrivilegeCode.getAll());
        return new ModelAndView("gameresource/group/createprivilege", msgMap);
    }

    @RequestMapping(value = "/privilege/create")
    public ModelAndView privilegeCreate(@RequestParam(value = "name", required = false) String privilegeName,
                                        @RequestParam(value = "code", required = false) String privilegeCode,
                                        @RequestParam(value = "desc", required = false) String privilegeDesc,
                                        @RequestParam(value = "type", required = false) Integer privilegeType) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        try {
            List<GroupPrivilege> existPrivilegeList = GameResourceServiceSngl.get().queryGroupPrivilege(new QueryExpress().add(QueryCriterions.eq(GroupPrivilegeField.PRIVILEGE_TYPE, privilegeType)).add(QueryCriterions.eq(GroupPrivilegeField.STATUS, ActStatus.UNACT.getCode())));
            for (GroupPrivilege existPrivilege : existPrivilegeList) {
                if (existPrivilege.getPrivilegeName().equals(privilegeName)) {
                    msgMap.put("errorMsg", "group.privilege.has.exist");
                    return new ModelAndView("forward:/gameresource/group/privilege/createpage", msgMap);
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " queryGroupPrivilege,ServiceException :", e);
        }
        GroupPrivilege privilege = new GroupPrivilege();
        privilege.setPrivilegeName(privilegeName);
        privilege.setPrivilegeCode(GroupPrivilegeCode.getByCode(privilegeCode));
        privilege.setPrivilegeDesc(privilegeDesc);
        privilege.setPrivilegeType(GroupPrivilegeType.getByCode(privilegeType));
        privilege.setActStatus(ActStatus.UNACT);
        privilege.setCreateDate(new Date());
        privilege.setCreateIp(getIp());
        privilege.setCreateUserId(getCurrentUser().getUserid());
        try {
            GameResourceServiceSngl.get().createGroupPrivilege(privilege);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " createGroupPrivilege, ServiceException :", e);
        }
        return new ModelAndView("redirect:/gameresource/group/privilege/list?privilegetype="+privilegeType);
    }

    @RequestMapping(value = "/privilege/modifypage")
    public ModelAndView privilegeModifyPage(@RequestParam(value = "privilegeid", required = false) Long privilegeId) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put("privilegeTypeCollection", GroupPrivilegeType.getAll());
        msgMap.put("privilegeCodeCollection", GroupPrivilegeCode.getAll());
        if (privilegeId == null) {
            return new ModelAndView("redirect:/gameresource/group/privilege/list");
        }
        try {
            GroupPrivilege privilege = GameResourceServiceSngl.get().getGroupPrivilege(new QueryExpress().add(QueryCriterions.eq(GroupPrivilegeField.PRIVILEGE_ID, privilegeId)));
            if (privilege == null) {
                return new ModelAndView("redirect:/gameresource/group/privilege/list");
            }
            msgMap.put("privilege", privilege);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " createGroupPrivilege, ServiceException :", e);
        }
        return new ModelAndView("/gameresource/group/modifyprivilege", msgMap);
    }

    @RequestMapping(value = "/privilege/modify")
    public ModelAndView privilegeModify(@RequestParam(value = "privilegeid", required = false) Long privilegeId,
                                        @RequestParam(value = "name", required = false) String privilegeName,
                                        @RequestParam(value = "code", required = false) String privilegeCode,
                                        @RequestParam(value = "desc", required = false) String privilegeDesc,
                                        @RequestParam(value = "type", required = false) Integer privilegeType) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        try {
            List<GroupPrivilege> existPrivilegeList = GameResourceServiceSngl.get().queryGroupPrivilege(new QueryExpress().add(QueryCriterions.eq(GroupPrivilegeField.PRIVILEGE_TYPE, privilegeType)).add(QueryCriterions.eq(GroupPrivilegeField.STATUS, ActStatus.UNACT.getCode())));
            for (GroupPrivilege existPrivilege : existPrivilegeList) {
                if (existPrivilege.getPrivilegeId() != privilegeId && existPrivilege.getPrivilegeName().equals(privilegeName)) {
                    msgMap.put("errorMsg", "group.privilege.has.exist");
                    msgMap.put("privilegeid", privilegeId);
                    return new ModelAndView("forward:/gameresource/group/privilege/modifypage", msgMap);
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " queryGroupPrivilege,ServiceException :", e);
        }
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(GroupPrivilegeField.PRIVILEGE_NAME, privilegeName);
        updateExpress.set(GroupPrivilegeField.PRIVILEGE_CODE, privilegeCode);
        updateExpress.set(GroupPrivilegeField.PRIVILEGE_DESC, privilegeDesc);
        updateExpress.set(GroupPrivilegeField.PRIVILEGE_TYPE, privilegeType);
        updateExpress.set(GroupPrivilegeField.LAST_MODIFY_DATE, new Date());
        updateExpress.set(GroupPrivilegeField.LAST_MODIFY_IP, getIp());
        updateExpress.set(GroupPrivilegeField.LAST_MODIFY_USERID, getCurrentUser().getUserid());
        try {
            GameResourceServiceSngl.get().modifyGroupPrivilege(new QueryExpress().add(QueryCriterions.eq(GroupPrivilegeField.PRIVILEGE_ID, privilegeId)), updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " createGroupPrivilege, ServiceException :", e);
        }
        return new ModelAndView("redirect:/gameresource/group/privilege/list");
    }

    @RequestMapping(value = "/privilege/delete")
    public ModelAndView privilegeDelete(@RequestParam(value = "privilegeid", required = false) Long privilegeId) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(GroupPrivilegeField.STATUS, ActStatus.ACTED.getCode());
        try {
            GameResourceServiceSngl.get().modifyGroupPrivilege(new QueryExpress().add(QueryCriterions.eq(GroupPrivilegeField.PRIVILEGE_ID, privilegeId)), updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " createGroupPrivilege, ServiceException :", e);
        }
        return new ModelAndView("redirect:/gameresource/group/privilege/list");
    }

    @RequestMapping(value = "/privilege/recover")
    public ModelAndView privilegeRecover(@RequestParam(value = "privilegeid", required = false) Long privilegeId) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(GroupPrivilegeField.STATUS, ActStatus.UNACT.getCode());
        try {
            GameResourceServiceSngl.get().modifyGroupPrivilege(new QueryExpress().add(QueryCriterions.eq(GroupPrivilegeField.PRIVILEGE_ID, privilegeId)), updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " createGroupPrivilege, ServiceException :", e);
        }
        return new ModelAndView("redirect:/gameresource/group/privilege/list");
    }
}
