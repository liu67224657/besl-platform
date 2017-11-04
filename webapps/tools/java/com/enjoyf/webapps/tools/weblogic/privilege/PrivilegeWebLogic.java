package com.enjoyf.webapps.tools.weblogic.privilege;

import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.webapps.common.security.DES;
import com.enjoyf.webapps.tools.webpage.controller.SessionConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Author: zhaoxin
 * Date: 11-11-23
 * Time: 上午9:42
 * Desc: 权限，逻辑层实现
 */
@Service(value = "privilegeWebLogic")
public class PrivilegeWebLogic {

    Logger logger = LoggerFactory.getLogger(this.getClass());


    public Boolean isUserExist(String userId) throws ServiceException {

        return ToolsServiceSngl.get().getUserByLoginName(userId) != null;
    }

    public Boolean isRolesExist(String roleName) throws ServiceException {

        return ToolsServiceSngl.get().getRoleByRoleName(roleName) != null;
    }

    public Boolean isResExist(String rsurl, Integer rsid) throws ServiceException {

        PrivilegeResource privilegeResource = ToolsServiceSngl.get().getResByRsurl(rsurl);

        return privilegeResource == null ? false : (privilegeResource.getRsid().equals(rsid) ? false : true);
    }

    public PrivilegeUser getByUno(Integer uno) throws ServiceException {

        return ToolsServiceSngl.get().getByUno(uno);
    }

    public PageRows<PrivilegeUser> queryUserList(PrivilegeUser entity, Pagination p) throws ServiceException {

        return ToolsServiceSngl.get().queryUserByParam(entity, p);
    }

    public PageRows<PrivilegeRole> queryRolesList(PrivilegeRole entity, Pagination p) throws ServiceException {

        return ToolsServiceSngl.get().queryRoleByParam(entity, p);
    }

    public PageRows<PrivilegeResource> queryResList(PrivilegeResource entity, Pagination p) throws ServiceException {

        return ToolsServiceSngl.get().queryResByParam(entity, p);
    }

    public boolean modifyPwd(String pwd, Integer uno) throws ServiceException {

        return ToolsServiceSngl.get().modifyPwd(pwd, uno);
    }

    public boolean modifyUser(PrivilegeUser entity, Map<ObjectField, Object> map) throws ServiceException {

        return ToolsServiceSngl.get().modifyUser(entity, map);
    }

    public boolean modifyRole(PrivilegeRole entity, Map<ObjectField, Object> map) throws ServiceException {

        return ToolsServiceSngl.get().modifyRole(entity, map);
    }

    public boolean modifyRes(PrivilegeResource entity, Map<ObjectField, Object> map) throws ServiceException {

        return ToolsServiceSngl.get().modifyRes(entity, map);
    }

    public boolean switchUser(String ustatus, Integer uno) throws ServiceException {

        return ToolsServiceSngl.get().switchUser(ustatus, uno);
    }

    public boolean deleteUser(Integer uno) throws ServiceException {

        return ToolsServiceSngl.get().deleteUser(uno);
    }

    public boolean deleteRole(Integer rid) throws ServiceException {

        return ToolsServiceSngl.get().deleteRole(rid);
    }

    public boolean deleteRes(String status, Integer rsid) throws ServiceException {

        return ToolsServiceSngl.get().deleteRes(status, rsid);
    }

    public boolean saveUser(PrivilegeUser entity) throws ServiceException {

        return ToolsServiceSngl.get().saveUser(entity);
    }

    public boolean saveRole(PrivilegeRole entity) throws ServiceException {

        return ToolsServiceSngl.get().saveRole(entity);
    }

    public boolean saveRes(PrivilegeResource entity) throws ServiceException {

        return ToolsServiceSngl.get().saveRes(entity);
    }

    public Map login(PrivilegeUser entity) throws ServiceException {

        return loginGetResListAndP(entity);
    }

    private Map loginGetResListAndP(PrivilegeUser entity) throws ServiceException {

        String userid = entity.getUserid();
        String pwd = entity.getPassword();

        ArrayList listRs = new ArrayList();//用户具有所有"菜单资源实体"的集合

        Map mapSession = new HashMap();
        Map<Integer, Integer> mapRs = new HashMap<Integer, Integer>();//每个用户具有"权限资源ID"的map对象。
        Map<Integer, PrivilegeResource> mapListRsForMenu = new HashMap<Integer, PrivilegeResource>();

        try {
            pwd = DES.encrypt(pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PrivilegeUser privilegeUser = ToolsServiceSngl.get().getUserByUserIdPwd(userid, pwd);
        if (privilegeUser == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("user passwd is failure:" + userid);
            }

            return null;
        }
        //登陆日志
        ToolsLoginLog pll = new ToolsLoginLog();

        pll.setAccIp(entity.getAccessip());
        pll.setSuccess(ActStatus.UNACT);//登陆状态，初始失败
        pll.setLoginTime(new Date());
        pll.setPassWord(pwd);
        pll.setUserId(userid);

        //取用户对应的角色
        Set<PrivilegeRole> setRole = privilegeUser.getPrivilegeRoleses();

        if (CollectionUtil.isEmpty(setRole)) {
            if (logger.isDebugEnabled()) {
                logger.debug("user list is null:" + setRole);
            }

            return null;
        }

        //取角色对应的资源［菜单+action］
        for (PrivilegeRole privilegeRoles : setRole) {
            if ("admin".equals(privilegeRoles.getRoleName())) {
                List<PrivilegeResource> tempRes = ToolsServiceSngl.get().queryAllResByStatus(ActStatus.ACTED);

                //获取该角色admin对应的资源列表
                for (PrivilegeResource privilegeResource : tempRes) {
                    mapRs.put(privilegeResource.getRsid(), privilegeResource.getRsid());
                    if (privilegeResource.getRstype().equals(PrivilegeResourceType.MENU)) {
                        mapListRsForMenu.put(privilegeResource.getRsid(), privilegeResource);
                    }
                }
                break;
            } else {
                if (!privilegeRoles.getStatus().equals(ActStatus.ACTED))
                    continue;//角色不可用，取不到资源
                Set<PrivilegeResource> setPrivilegeResource = privilegeRoles.getPrivilegeResources();

                if (!CollectionUtil.isEmpty(setPrivilegeResource))
                    for (PrivilegeResource privilegeResource : setPrivilegeResource) {
                        mapRs.put(privilegeResource.getRsid(), privilegeResource.getRsid());
                        if (privilegeResource.getRstype().equals(PrivilegeResourceType.MENU))
                            mapListRsForMenu.put(privilegeResource.getRsid(), privilegeResource);
                    }
            }
        }

        listRs.addAll(mapListRsForMenu.values());

        if (1 > mapRs.size()) {
            if (logger.isDebugEnabled()) {
                logger.debug("no this role :" + mapRs);
            }

            return null;
        }

        Collections.sort(listRs);  //排序一级菜单
        pll.setSuccess(ActStatus.ACTED);//登陆状态，调为成功！

        //添加登录日志
        ToolsServiceSngl.get().saveLoginLog(pll);

        mapSession.put(SessionConstants.LIST_RESOURCE, listRs);//用户具有所有"菜单资源实体"的集合
        mapSession.put(SessionConstants.USER_SESSION_PRIVILEGE_ID, mapRs);//每个用户具有"权限资源ID"的map对象。
        mapSession.put(SessionConstants.CURRENT_USER, privilegeUser);//放置当前登录用户的实体对象

        return mapSession;
    }

    /**
     * 角色分配资源
     * 加载整个系统具有的菜单资源信息
     */
    public String preRoleResourceJosn() {

        String reStr = "[[-1,-2,'系统菜单']";
        List<PrivilegeResource> resourceList = null;

        try {
            resourceList = ToolsServiceSngl.get().queryResMenu("1", "y");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        if ((resourceList == null) || (resourceList.size() < 1)) {
            reStr = "";
        } else {
            for (PrivilegeResource pr : resourceList) {
                reStr = reStr + ",['" + pr.getRsid() + "','" + pr.getParentid() + "','" + pr.getRsname() + "']";
            }
            reStr = reStr + "]";
        }

        return reStr;
    }

    /**
     * 获得该角色具有的菜单
     */
    public String loadUserRoleMenu(PrivilegeRole entity) throws ServiceException {
        String reStr = "[";

        Set<PrivilegeResource> setRes = ToolsServiceSngl.get().queryResByRoleId(entity);

        if (setRes == null || setRes.size() < 1) {
            reStr = "[]";
        } else {
            for (PrivilegeResource pre : setRes) {
                if (pre != null) {
                    reStr += pre.getRsid() + ",";
                }
//                else {
//                    reStr += pre.getRsid();
//                }
            }
            reStr += "]";
        }

        return reStr;
    }

    public List<PrivilegeRole> getUserRole() throws ServiceException {

        return ToolsServiceSngl.get().queryRoles();
    }

    public boolean insertRoleMenu(Integer rid, String[] menuids) throws ServiceException {

        return ToolsServiceSngl.get().insertRoleMenu(rid, menuids);
    }

    public boolean insertUserRole(Integer uno, String[] rolseids) throws ServiceException {

        return ToolsServiceSngl.get().insertUserRole(uno, rolseids);
    }

    public PrivilegeRole getRoleByRid(Integer rid) throws ServiceException {
        return ToolsServiceSngl.get().getRoleByRid(rid);
    }

    public PrivilegeResource getResourceByRsid(int rsid) throws ServiceException {
        return ToolsServiceSngl.get().getResourceByRsid(rsid);
    }
}
