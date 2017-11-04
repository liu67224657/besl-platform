package com.enjoyf.webapps.tools.webpage.controller.privilege;

import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.weblogic.privilege.PrivilegeWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.common.base.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: taijunli
 * Date: 11-12-7
 * Time: 上午9:37
 */
@Controller
@RequestMapping(value = "/privilege/roles")
public class PrivilegeRoleController extends ToolsBaseController {

    @Resource(name = "privilegeWebLogic")
    private PrivilegeWebLogic privilegeWebLogic;

    private JsonBinder jsonBinder = JsonBinder.buildNormalBinder();

    //************************************
    //********增加角色操作****************
    //************************************

    /**
     * @return 增加角色界面
     */
    @RequestMapping(value = "/createrolespage")
    public ModelAndView createRolesPage() {
        return new ModelAndView("/privilege/createrolespage");
    }

    /**
     * 添加角色
     *
     * @param roleName
     * @param description
     * @param status
     * @param type
     * @return
     */
    @RequestMapping(value = "/saverolespage")
    public ModelAndView saveRolesPage(
            @RequestParam(value = "rolename", required = false) String roleName,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "type", required = false) String type) {

        Map<String, String> errorMsgMap = new HashMap<String, String>();
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        PrivilegeRole entity = new PrivilegeRole();

        if (!Strings.isNullOrEmpty(roleName)) {
            entity.setRoleName(roleName);
        }else {
            errorMsgMap.put("rolename", "error.privilege.role.name.null");
        }

        if (!Strings.isNullOrEmpty(status)) {
            if (ActStatus.ACTED.getCode().equals(status)) {
                entity.setStatus(ActStatus.ACTED);
            } else if (ActStatus.UNACT.getCode().equals(status)) {
                entity.setStatus(ActStatus.UNACT);
            }
        } else {
            errorMsgMap.put("status", "error.privilege.role.status.null");
        }

        if (!Strings.isNullOrEmpty(description)) {
            entity.setDescription(description);
        }

        if (!Strings.isNullOrEmpty(type)) {
            if (PrivilegeRoleType.ROLE1.getCode().equals(type)) {
                entity.setType(PrivilegeRoleType.ROLE1);
            } else if (PrivilegeRoleType.ROLE2.getCode().equals(type)) {
                entity.setType(PrivilegeRoleType.ROLE2);
            }
        }
        mapMsg.put("errorMsgMap", errorMsgMap);
        mapMsg.put("entity", entity);

        if(!errorMsgMap.isEmpty()){
            return new ModelAndView("/privilege/createrolespage", mapMsg);
        }

        try {
            boolean exist = privilegeWebLogic.isRolesExist(roleName);

            if (exist) {
                mapMsg.put("message", 0);
                return new ModelAndView("/privilege/createrolespage", mapMsg);
            } else {
                if (privilegeWebLogic.saveRole(entity)) {
                    ToolsLog log = new ToolsLog();

                    log.setOpUserId(getCurrentUser().getUserid());
                    log.setOperType(LogOperType.ROLES_SAVEROLESPAGE);
                    log.setOpTime(new Date());
                    log.setOpIp(getIp());
                    log.setOpAfter(StringUtil.truncate(entity.toString(), ToolsConstants.SPLIT_SIZE));

                    addLog(log);

                    return new ModelAndView("redirect:/privilege/roles/roleslist");
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab("saveRolesPage a roles SQLException :", e);
        }
        return new ModelAndView("/privilege/createrolespage", mapMsg);
    }

    //************************************
    //********修改角色操作****************
    //************************************

    /**
     * @return 编辑角色界面
     */
    @RequestMapping(value = "/preeditrolespage")
    public ModelAndView preEditRolesPage(
            @RequestParam(value = "rid", required = true) Integer rid) {
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        PrivilegeRole entity = new PrivilegeRole();

        try {
            entity = privilegeWebLogic.getRoleByRid(rid);
        } catch (ServiceException e) {
            GAlerter.lab("preEditRolesPage caught an Exception:" + e);
        }
        mapMsg.put("entity", entity);
        return new ModelAndView("/privilege/preeditrolespage", mapMsg);
    }

    /**
     * @return 修改角色
     */
    @RequestMapping(value = "/editrolespage")
    public ModelAndView modifyRolesPage(
            @RequestParam(value = "rolename", required = false) String roleName,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "rid", required = false) Integer rid) {
        PrivilegeRole entity = new PrivilegeRole();
        Map<ObjectField, Object> rolesMap = new HashMap<ObjectField, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        if (rid != null) {
            entity.setRid(rid);
        }
        if(!StringUtil.isEmpty(roleName)){
            entity.setRoleName(roleName);
        }else{
            errorMsgMap.put("rolename", "error.privilege.role.name.null");
        }

        entity.setDescription(description);
        if(!StringUtil.isEmpty(status)){
            entity.setStatus(ActStatus.getByCode(status));
        } else {
            errorMsgMap.put("status", "error.privilege.role.status.null");
        }

        entity.setType(PrivilegeRoleType.getByCode(type));

        rolesMap.put(PrivilegeRoleField.ROLENAME, roleName);
        rolesMap.put(PrivilegeRoleField.DESCRIPTION, description);

        if (ActStatus.ACTED.getCode().equals(status)) {
            rolesMap.put(PrivilegeRoleField.RSTATUS, ActStatus.ACTED.getCode());
        } else if (ActStatus.UNACT.getCode().equals(status)) {
            rolesMap.put(PrivilegeRoleField.RSTATUS, ActStatus.UNACT.getCode());
        }

        if (PrivilegeRoleType.ROLE1.getCode().equals(type)) {
            rolesMap.put(PrivilegeRoleField.RTYPE, PrivilegeRoleType.ROLE1.getCode());
        } else if (PrivilegeRoleType.ROLE2.getCode().equals(type)) {
            rolesMap.put(PrivilegeRoleField.RTYPE, PrivilegeRoleType.ROLE2.getCode());
        } else {
            rolesMap.put(PrivilegeRoleField.RTYPE, "");
        }

        mapMsg.put("errorMsgMap", errorMsgMap);
        mapMsg.put("entity", entity);

        if(!errorMsgMap.isEmpty()){
            return new ModelAndView("/privilege/preeditrolespage", mapMsg);
        }

        try {
            if(privilegeWebLogic.modifyRole(entity, rolesMap)){
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.ROLES_MODIFYROLESPAGE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setSrcId(rid.toString());
                log.setOpAfter(StringUtil.truncate(entity.toString(), ToolsConstants.SPLIT_SIZE));

                addLog(log);
            }

        } catch (Exception e) {
            GAlerter.lab("editUserPage a role Exception :", e);
        }
        return new ModelAndView("redirect:/privilege/roles/roleslist");
    }

    //************************************
    //********角色删除操作****************
    //************************************

    @RequestMapping(value = "batchupdate")
    public ModelAndView batchUpdateRoles(@RequestParam(value = "rids", required = false)Integer[] rids,
                                         @RequestParam(value = "updateRemoveStatusCode", required = false)String updateRemoveStatusCode,
                                         @RequestParam(value = "rolename", required = false)String roleName,
                                         @RequestParam(value = "status", required = false)String status,
                                         @RequestParam(value = "update", required = false)String update,
                                         @RequestParam(value = "delete", required = false)String delete,
                                         @RequestParam(value = "items", required = false, defaultValue = "0") int items,                   //总记录数
                                         @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,      //数据库记录索引
                                         @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems){    //每页记录数

        if(rids != null && rids.length > 0){
            if(!StringUtil.isEmpty(update)){
                for(Integer rid : rids){
                    updateRoleStatus(rid, updateRemoveStatusCode);
                }
            }else if(!StringUtil.isEmpty(delete)){
                for(Integer rid : rids){
                    deleteRole(rid);
                }
            }
        }

        Pagination pagination = new Pagination(items, (pagerOffset / maxPageItems) + 1, WebappConfig.get().getHomePageSize());
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        PrivilegeUser entity = new PrivilegeUser();

        if (!Strings.isNullOrEmpty(roleName)) {
            entity.setUsername(roleName);
        }
        if (!Strings.isNullOrEmpty(status)) {
            if (ActStatus.ACTED.getCode().equals(status)) { //标识可用状态"y"
                entity.setUstatus(ActStatus.ACTED);
            } else if (ActStatus.UNACT.getCode().equals(status)) {//标识不可用状态"n"
                entity.setUstatus(ActStatus.UNACT);
            }
        }

        mapMsg.put("page", pagination);
        mapMsg.put("entity", entity);

        return new ModelAndView("forward:/privilege/roles/roleslist", mapMsg);

    }


    //************************************
    //********查询角色操作****************
    //************************************

    /**
     * 按条件查询角色列表
     *
     * @param roleName
     * @param status
     * @param items
     * @param pagerOffset
     * @param maxPageItems
     * @return
     */
    @RequestMapping(value = "/roleslist")
    public ModelAndView queryRolesList(
            @RequestParam(value = "rolename", required = false) String roleName,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "items", required = false, defaultValue = "0") int items,                   //总记录数
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,      //数据库记录索引
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems) {   //每页记录数

        Pagination pagination = new Pagination(items, (pagerOffset / maxPageItems) + 1, WebappConfig.get().getHomePageSize());
        PrivilegeRole entity = new PrivilegeRole();

        if (!Strings.isNullOrEmpty(roleName)) {
            entity.setRoleName(roleName);
        }
        if (!Strings.isNullOrEmpty(status)) {
            if (ActStatus.ACTED.getCode().equals(status)) {
                entity.setStatus(ActStatus.ACTED);
            } else if (ActStatus.UNACT.getCode().equals(status)) {
                entity.setStatus(ActStatus.UNACT);
            }
        }

        Map<String, Object> mapMsg = new HashMap<String, Object>();

        try {
            PageRows<PrivilegeRole> pageRows = privilegeWebLogic.queryRolesList(entity, pagination);

            mapMsg.put("rows", pageRows.getRows());
            mapMsg.put("page", pageRows.getPage());
            mapMsg.put("entity", entity);
            mapMsg.put("rolename", roleName);
            mapMsg.put("status", status);

        } catch (ServiceException e) {
            GAlerter.lab("queryRolesList a Controller SQLException :", e);
        }

        return new ModelAndView("/privilege/roleslist", mapMsg);
    }

    //************************************
    //********分配菜单和资源操作**********
    //************************************
    @RequestMapping(value = "/rolesmenuassign")
    public ModelAndView preRoleResourceJosn(
            @RequestParam(value = "rid", required = true) Integer rid,
            @RequestParam(value = "rolename", required = false) String roleName,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "type", required = false) String type) {
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        PrivilegeRole entity = new PrivilegeRole();

        if (rid != null) {
            entity.setRid(rid);
        }
        if (!Strings.isNullOrEmpty(roleName)) {
            entity.setRoleName(roleName);
        }
        if (!Strings.isNullOrEmpty(description)) {
            entity.setDescription(description);
        }
        if (!Strings.isNullOrEmpty(status)) {
            if (ActStatus.ACTED.getCode().equals(status)) {
                entity.setStatus(ActStatus.ACTED);
            } else if (ActStatus.UNACT.getCode().equals(status)) {
                entity.setStatus(ActStatus.UNACT);
            }
        }
        if (!Strings.isNullOrEmpty(type)) {
            if (PrivilegeRoleType.ROLE1.getCode().equals(type)) {
                entity.setType(PrivilegeRoleType.ROLE1);
            } else if (PrivilegeRoleType.ROLE2.getCode().equals(type)) {
                entity.setType(PrivilegeRoleType.ROLE2);
            }
        }
        mapMsg.put("entity", entity);

        //获取角色对应的资源列表
        String menu = privilegeWebLogic.preRoleResourceJosn();
        mapMsg.put("menu", menu);

        return new ModelAndView("/privilege/assignrolersmenu", mapMsg);
    }

    @RequestMapping(value = "/systemrolesmenua")
    @ResponseBody
    public String preRoleResourceJosn(HttpServletResponse response) {
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        PrivilegeRole entity = new PrivilegeRole();
        mapMsg.put("entity", entity);

        try {
            String menu = privilegeWebLogic.preRoleResourceJosn();

            response.setCharacterEncoding("utf-8");
            response.getOutputStream().write(menu.getBytes("utf-8"));
            response.getOutputStream().flush();

        } catch (IOException e) {
            GAlerter.lab("preRoleResourceJosn a Controller SQLException :", e);
        }
        return null;

    }

    @RequestMapping(value = "/loaduserrolemenudata")
    @ResponseBody
    public String loadUserRoleMenuData(
            HttpServletResponse response,
            @RequestParam(value = "rid", required = true) Integer rid) {
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        PrivilegeRole entity = new PrivilegeRole();

        if (rid != null) {
            entity.setRid(rid);
        }
        mapMsg.put("entity", entity);

        String menuroles = null;
        try {
            menuroles = privilegeWebLogic.loadUserRoleMenu(entity);
            response.setCharacterEncoding("utf-8");
            response.getOutputStream().write(menuroles.getBytes("utf-8"));
            response.getOutputStream().flush();

        } catch (Exception e) {
            GAlerter.lab("loadUserRoleMenuData a Controller SQLException :", e);
        }
        return null;
    }

    @RequestMapping(value = "/saverolesmenu")
    @ResponseBody
    public String saveRoleMenu(
            @RequestParam(value = "rid", required = false) Integer rid,
            @RequestParam(value = "menuids", required = false) String menuids) {

        String[] meun_arr = menuids.split(",");
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);
        boolean insertdMenu = false;

        try {
            insertdMenu = privilegeWebLogic.insertRoleMenu(rid, meun_arr);

            ToolsLog log = new ToolsLog();

            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.ROLES_SAVEROLESMENU);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setSrcId(rid.toString());
            log.setOpAfter(StringUtil.truncate(menuids, ToolsConstants.SPLIT_SIZE));

            addLog(log);
            if (insertdMenu) {
                resultMsg.setStatus_code(JoymeResultMsg.CODE_S);
                return jsonBinder.toJson(resultMsg);
            }
        } catch (Exception e) {
            GAlerter.lab("insertRoleMenu a Controller SQLException :", e);
            return jsonBinder.toJson(resultMsg);
        }
        return null;
    }

    private void deleteRole(Integer rid) {
        try {
            privilegeWebLogic.deleteRole(rid);

            ToolsLog log = new ToolsLog();

            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.ROLES_REMOVEROLES);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setSrcId(rid.toString());
            log.setOpBefore(rid.toString());

            addLog(log);
        } catch (ServiceException e) {
            GAlerter.lab("deleteRole a roles SQLException :", e);
        }
    }

    private void updateRoleStatus(Integer rid, String updateRemoveStatusCode){
        Map<ObjectField, Object> map = new HashMap<ObjectField, Object>();
        PrivilegeRole role = new PrivilegeRole();

        role.setRid(rid);
        if (ActStatus.UNACT.getCode().equals(updateRemoveStatusCode)) {
            role.setStatus(ActStatus.ACTED);
        } else if (ActStatus.ACTED.getCode().equals(updateRemoveStatusCode)) {
            role.setStatus(ActStatus.UNACT);
        }

        map.put(PrivilegeRoleField.RSTATUS, role.getStatus().getCode());

        try {
            boolean  success = privilegeWebLogic.modifyRole(role, map);

            if(success){

                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.ROLES_MODIFYROLESPAGE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setSrcId(rid.toString());
                log.setOpAfter(StringUtil.truncate(role.toString(), ToolsConstants.SPLIT_SIZE));

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab("updateRoleStatus a roles SQLException :", e);
        }
    }

}
