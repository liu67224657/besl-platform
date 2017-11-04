package com.enjoyf.webapps.tools.webpage.controller.privilege;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.*;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.webapps.common.security.DES;
import com.enjoyf.webapps.tools.weblogic.privilege.PrivilegeWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.SessionConstants;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * User: taijunli
 * Date: 11-12-7
 * Time: 上午9:37
 */
@Controller
@RequestMapping(value = "/privilege/user")
public class PrivilegeUserController extends ToolsBaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final int USER_LIST_PER_PAGE = 20;

    private static Collection<ActStatus> useStatuses = new ArrayList<ActStatus>();

    static {
        useStatuses.add(ActStatus.ACTED);
    }

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @Resource(name = "privilegeWebLogic")
    private PrivilegeWebLogic privilegeWebLogic;

    private JsonBinder jsonbinder = JsonBinder.buildNormalBinder();

    //************************************
    //********增加用户操作****************
    //************************************

    /**
     * @return 增加用户界面
     */
    @RequestMapping(value = "/createuserpage")
    public ModelAndView createUserPage() {
        return new ModelAndView("/privilege/createuserpage");
    }

    /**
     * 增加用户
     *
     * @param userid
     * @param username
     * @param ustatus
     * @return
     */
    @RequestMapping(value = "/saveuserpage")
    public ModelAndView saveUserPage(
            @RequestParam(value = "userid", required = false) String userid,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "ustatus", required = false) String ustatus,
            @RequestParam(value = "limitLocation", required = false) String limitLocation) {

        Map<String, String> errorMsgMap = new HashMap<String, String>();
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        PrivilegeUser entity = new PrivilegeUser();

        try {
            if (!Strings.isNullOrEmpty(userid)) {
                entity.setUserid(userid);
            } else {
                errorMsgMap.put("userid", "error.privilege.user.userid.null");
            }
            if (!Strings.isNullOrEmpty(username)) {
                entity.setUsername(username);
            }
//            else {
//                errorMsgMap.put("username", "error.privilege.user.username.null");
//            }
            if (!Strings.isNullOrEmpty(password)) {
                password = DES.encrypt(password);
                entity.setPassword(password);
            } else {
                errorMsgMap.put("password", "error.privilege.user.pwd.null");
            }

            if (!Strings.isNullOrEmpty(limitLocation)) {
                if (ActStatus.ACTED.getCode().equals(limitLocation)) {
                    entity.setLimitLocation(ActStatus.ACTED);
                } else if (ActStatus.UNACT.getCode().equals(limitLocation)) {
                    entity.setLimitLocation(ActStatus.UNACT);
                }
            }

            if (!Strings.isNullOrEmpty(ustatus)) {
                if (ActStatus.ACTED.getCode().equals(ustatus)) {
                    entity.setUstatus(ActStatus.ACTED);
                } else if (ActStatus.UNACT.getCode().equals(ustatus)) {
                    entity.setUstatus(ActStatus.UNACT);
                }
            }

            if (privilegeWebLogic.isUserExist(userid)) {
                errorMsgMap.put("duplication", "error.privilege.user.userid.duplication");
            }

            mapMsg.put("errorMsgMap", errorMsgMap);
            mapMsg.put("entity", entity);

            if (!errorMsgMap.isEmpty()) {
                return new ModelAndView("/privilege/createuserpage", mapMsg);
            }

            boolean bool = privilegeWebLogic.saveUser(entity);

            ToolsLog log = new ToolsLog();

            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.USER_SAVEUSERPAGE);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setSrcId(null);
            log.setOpBefore(null);
            //log.setOpAfter(entity.toString());
            log.setOpAfter("保存用户,userid" + entity.getUserid());

            addLog(log);

            if (bool) {
                return new ModelAndView("redirect:/privilege/user/userlist");
            }
        } catch (Exception e) {
            GAlerter.lab("saveUserPage a user Exception :", e);
            errorMsgMap.put("system", "error.exception");

        }
        return new ModelAndView("/privilege/createuserpage", mapMsg);
    }

    //************************************
    //********修改用户操作****************
    //************************************

    /**
     * @return 编辑用户界面
     */
    @RequestMapping(value = "/preedituserpage")
    public ModelAndView preEditUserPage(
            @RequestParam(value = "uno", required = true) Integer uno) {

        PrivilegeUser entity = new PrivilegeUser();
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        try {
            entity = privilegeWebLogic.getByUno(uno);
        } catch (ServiceException e) {
            GAlerter.lab("preEditUserPage query a Privilege User caught an Exception.", e);
        }

        mapMsg.put("entity", entity);

        return new ModelAndView("/privilege/preedituserpage", mapMsg);
    }

    /**
     * @return 修改用户
     */
    @RequestMapping(value = "/edituserpage")
    public ModelAndView modifyUserPage(
            @RequestParam(value = "userid", required = false) String userid,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "ustatus", required = false) String ustatus,
            @RequestParam(value = "limitLocation", required = false) String limitLocation,
            @RequestParam(value = "uno", required = false) Integer uno) {
        PrivilegeUser entity = new PrivilegeUser();
        Map<ObjectField, Object> userMap = new HashMap<ObjectField, Object>();
        boolean bool = false;

        entity.setUno(uno);

        userMap.put(PrivilegeUserField.USERID, userid);
        userMap.put(PrivilegeUserField.USERNAME, username);

        if (ActStatus.ACTED.getCode().equals(ustatus)) {
            userMap.put(PrivilegeUserField.USTATUS, ActStatus.ACTED.getCode());
        } else if (ActStatus.UNACT.getCode().equals(ustatus)) {
            userMap.put(PrivilegeUserField.USTATUS, ActStatus.UNACT.getCode());
        }

        if (ActStatus.ACTED.getCode().equals(limitLocation)) {
            userMap.put(PrivilegeUserField.LIMITLOCATION, ActStatus.ACTED.getCode());
        } else if (ActStatus.UNACT.getCode().equals(limitLocation)) {
            userMap.put(PrivilegeUserField.LIMITLOCATION, ActStatus.UNACT.getCode());
        }

        try {
            bool = privilegeWebLogic.modifyUser(entity, userMap);
        } catch (ServiceException e) {
            GAlerter.lab("editUserPage a user ServiceException :", e);
        }

        ToolsLog log = new ToolsLog();

        log.setOpUserId(getCurrentUser().getUserid());
        log.setOperType(LogOperType.USER_MODIFYUSERPAGE);
        log.setOpTime(new Date());
        log.setOpIp(getIp());
        log.setSrcId(uno.toString());

        try {
            // log.setOpAfter(privilegeWebLogic.getByUno(uno).toString());
            log.setOpAfter("编辑用户,userid=" + userid + ",username=" + username + ",ustatus=" + ustatus + ",limitLocation=" + limitLocation);
        } catch (Exception e) {
            GAlerter.lab("modifyUserPage setOpAfter ServiceException :", e);
        }

        addLog(log);
        return new ModelAndView("redirect:/privilege/user/userlist");
    }

    //************************************
    //********用户状态操作****************
    //************************************


    @RequestMapping(value = "/batchupdate")
    public ModelAndView batchUpdateStatus(@RequestParam(value = "unos", required = false) Integer[] unos,
                                          @RequestParam(value = "updateRemoveStatusCode", required = false) String updateRemoveStatusCode,
                                          @RequestParam(value = "userid", required = false) String userid,
                                          @RequestParam(value = "update", required = false) String update,
                                          @RequestParam(value = "delete", required = false) String delete,
                                          @RequestParam(value = "username", required = false) String username,
                                          @RequestParam(value = "ustatus", required = false) String ustatus,
                                          @RequestParam(value = "items", required = false, defaultValue = "0") int items,                   //总记录数
                                          @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,      //数据库记录索引
                                          @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems) {    //每页记录数

        if (!StringUtil.isEmpty(update)) {
            if (unos != null && unos.length > 0) {
                for (int uno : unos) {
                    updateStatus(uno, updateRemoveStatusCode);
                }
            }
        } else if (!StringUtil.isEmpty(delete)) {
            if (unos != null && unos.length > 0) {
                for (int uno : unos) {
                    deleteUser(uno);
                }
            }
        }
        Pagination pagination = new Pagination(items, (pagerOffset / maxPageItems) + 1, USER_LIST_PER_PAGE);
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        PrivilegeUser entity = new PrivilegeUser();

        if (!Strings.isNullOrEmpty(userid)) {
            entity.setUserid(userid);
        }
        if (!Strings.isNullOrEmpty(username)) {
            entity.setUsername(username);
        }
        if (!Strings.isNullOrEmpty(ustatus)) {
            if (ActStatus.ACTED.getCode().equals(ustatus)) { //标识可用状态"y"
                entity.setUstatus(ActStatus.ACTED);
            } else if (ActStatus.UNACT.getCode().equals(ustatus)) {//标识不可用状态"n"
                entity.setUstatus(ActStatus.UNACT);
            }
        }

        mapMsg.put("page", pagination);
        mapMsg.put("entity", entity);

        return new ModelAndView("forward:/privilege/user/userlist", mapMsg);
    }

    //************************************
    //********修改密码操作****************
    //************************************

    /**
     * 修改用户密码
     *
     * @return
     */
    @RequestMapping(value = "/modifypwd")
    public ModelAndView modifyPwd(HttpServletRequest request,
                                  @RequestParam(value = "result", required = false) String result) {
        Map map = new HashMap();
        HttpSession session = request.getSession(false);
        PrivilegeUser entity = (PrivilegeUser) session.getAttribute(SessionConstants.CURRENT_USER);
        if (!StringUtil.isEmpty(result)) {
            map.put("result", result);
        }
        boolean isadmin = false;
        for (PrivilegeRole privilegeRoles : entity.getPrivilegeRoleses()) {
            if ("admin".equals(privilegeRoles.getRoleName())) {
                isadmin = true;
                break;
            }
        }

        map.put("entity", entity);
        map.put("isadmin", isadmin);

        return new ModelAndView("/privilege/modifypwd", map);
    }

    @RequestMapping(value = "/savepwd")
    public ModelAndView savePwd(
            HttpServletRequest request,
            @RequestParam(value = "oldpwd", required = false) String oldpwd,
            @RequestParam(value = "newpwd", required = false) String newpwd,
            @RequestParam(value = "affirmnewpwd", required = false) String affirmnewpwd,
            @RequestParam(value = "userid", required = false, defaultValue = "") String userid) {
        HttpSession session = request.getSession(false);
        PrivilegeUser entity = (PrivilegeUser) session.getAttribute(SessionConstants.CURRENT_USER);


        try {
            boolean bval = false;
            //修改他人密码
            if (!StringUtil.isEmpty(userid) && newpwd.equals(affirmnewpwd)) {
                entity = ToolsServiceSngl.get().getUserByLoginName(userid);
                bval = privilegeWebLogic.modifyPwd(DES.encrypt(newpwd), entity.getUno());
            } else if (entity.getPassword().equals(DES.encrypt(oldpwd)) && newpwd.equals(affirmnewpwd)) {
                bval = privilegeWebLogic.modifyPwd(DES.encrypt(newpwd), entity.getUno());
            }


            if (bval) {
                ToolsLog log = new ToolsLog();
                log.setOpUserId(entity.getUserid());
                log.setOperType(LogOperType.USER_SAVEPWD);
                log.setOpTime(new Date());
                log.setOpIp(request.getRemoteAddr());
                log.setSrcId(entity.getUno().toString());
                log.setOpAfter("修改用户密码,userid:" + entity.getUserid());
                addLog(log);
                return new ModelAndView("forward:/privilege/user/modifypwd?result=success");
            }
        } catch (Exception e) {
            GAlerter.lab("savepwd a user SQLException :", e);
        }

        return new ModelAndView("forward:/privilege/user/modifypwd?result=failed");
    }

    //************************************
    //********查询用户操作****************
    //************************************

    /**
     * 按条件查询用户列表
     *
     * @param userid
     * @param ustatus
     * @param items
     * @param pagerOffset
     * @param maxPageItems
     * @return
     */
    @RequestMapping(value = "/userlist")
    public ModelAndView queryUserList(
            @RequestParam(value = "userid", required = false) String userid,
            @RequestParam(value = "ustatus", required = false) String ustatus,
            @RequestParam(value = "items", required = false, defaultValue = "0") int items,                   //总记录数
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,      //数据库记录索引
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems) {   //每页记录数

        Pagination pagination = new Pagination(items, (pagerOffset / maxPageItems) + 1, USER_LIST_PER_PAGE);
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        PrivilegeUser entity = new PrivilegeUser();

        if (!Strings.isNullOrEmpty(userid)) {
            entity.setUserid(userid);
        }
        if (!Strings.isNullOrEmpty(userid)) {
            entity.setUsername(userid);
        }
        if (!Strings.isNullOrEmpty(ustatus)) {
            if (ActStatus.ACTED.getCode().equals(ustatus)) { //标识可用状态"y"
                entity.setUstatus(ActStatus.ACTED);
            } else if (ActStatus.UNACT.getCode().equals(ustatus)) {//标识不可用状态"n"
                entity.setUstatus(ActStatus.UNACT);
            }
        }
        try {
            PageRows<PrivilegeUser> pageRows = privilegeWebLogic.queryUserList(entity, pagination);

            mapMsg.put("rows", pageRows.getRows());
            mapMsg.put("page", pageRows.getPage());
            mapMsg.put("entity", entity);
        } catch (ServiceException e) {
            GAlerter.lab("queryUserList a Controller ServiceException :", e);
        }

        return new ModelAndView("/privilege/userlist", mapMsg);
    }
    //************************************
    //********人员删除操作****************
    //************************************

//    /**
//     * @return 删除人员
//     */
//    @RequestMapping(value = "/deleteuser")
//    public ModelAndView removeUser(
//            @RequestParam(value = "uno", required = true) Integer uno) {
//        try {
//            privilegeWebLogic.deleteUser(uno);
//
//            ToolsLog log = new ToolsLog();
//
//            log.setOpUserId(getCurrentUser().getUserid());
//            log.setOperType(LogOperType.USER_REMOVEUSER);
//            log.setOpTime(new Date());
//            log.setOpIp(getIp());
//            log.setSrcId(uno.toString());
//            log.setOpBefore(uno.toString());
//
//            addLog(log);
//        } catch (ServiceException e) {
//            GAlerter.lab("deleteUser a user ServiceException :", e);
//        }
//        return new ModelAndView("redirect:/privilege/user/preuserlist");
//    }

    //************************************
    //********人员角色分配****************
    //************************************

    @RequestMapping(value = "/assignuserroles")
    public ModelAndView assignUserRole(@RequestParam(value = "uno", required = false) Integer uno) {
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        PrivilegeUser entity = null;
        List<PrivilegeRole> listRoles = null;

        try {

            listRoles = privilegeWebLogic.getUserRole();
            entity = privilegeWebLogic.getByUno(uno);
        } catch (ServiceException e) {
            GAlerter.lab("assignUserRole a user role Exception :", e);
        }

        mapMsg.put("listRoles", listRoles);
        mapMsg.put("entity", entity);

        return new ModelAndView("/privilege/assignuseroles", mapMsg);
    }

    @RequestMapping(value = "/saveuserroles")
    @ResponseBody
    public String saveUserRole(
            HttpServletRequest request,
            @RequestParam(value = "uno", required = false) Integer uno,
            @RequestParam(value = "ids", required = false) String ids) {
        PrivilegeUser entity = new PrivilegeUser();
        StringBuffer sb = new StringBuffer();

        String[] cids = StringUtil.splitString(ids, ",");
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);

        boolean bool = false;

        entity.setUno(uno);
        try {
            bool = privilegeWebLogic.insertUserRole(uno, cids);

            ToolsLog log = new ToolsLog();

            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.USER_SAVEUSERROLE);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setSrcId(uno.toString());

            try {
                log.setOpAfter(StringUtil.truncate(privilegeWebLogic.getByUno(uno).toString(), ToolsConstants.SPLIT_SIZE));
            } catch (ServiceException e) {
                GAlerter.lab("modifyUserPage setOpAfter ServiceException :", e);
            }

            addLog(log);

            if (bool) {
                resultMsg.setStatus_code(JoymeResultMsg.CODE_S);
                return jsonbinder.toJson(resultMsg);
            } else {
                return jsonbinder.toJson(resultMsg);
            }
        } catch (Exception e) {
            GAlerter.lab("saveUserRole a user roles Exception :", e);
            return jsonbinder.toJson(resultMsg);
        }
    }

    private void updateStatus(Integer uno, String updateRemoveStatusCode) {
        PrivilegeUser entity = new PrivilegeUser();

        if (ActStatus.UNACT.getCode().equals(updateRemoveStatusCode)) {
            entity.setUstatus(ActStatus.ACTED);
        } else if (ActStatus.ACTED.getCode().equals(updateRemoveStatusCode)) {
            entity.setUstatus(ActStatus.UNACT);
        }
        try {
            privilegeWebLogic.switchUser(entity.getUstatus().getCode(), uno);

            ToolsLog log = new ToolsLog();

            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.USER_MODIFYUSER);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setSrcId(uno.toString());
            log.setOpBefore(updateRemoveStatusCode);
            log.setOpAfter(entity.getUstatus().getCode());

            addLog(log);
        } catch (ServiceException e) {
            GAlerter.lab("switchuser a user ServiceException :", e);
        }
    }

    private void deleteUser(Integer uno) {
        try {
            privilegeWebLogic.deleteUser(uno);

            ToolsLog log = new ToolsLog();

            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.USER_REMOVEUSER);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setSrcId(uno.toString());
            log.setOpBefore(uno.toString());

            addLog(log);
        } catch (ServiceException e) {
            GAlerter.lab("deleteUser a user ServiceException :", e);
        }
    }
}
