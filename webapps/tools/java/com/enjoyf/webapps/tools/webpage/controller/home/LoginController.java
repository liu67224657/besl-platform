package com.enjoyf.webapps.tools.webpage.controller.home;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ToolsHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.oauth.AuthToken;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.*;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.oauth.OAuthException;
import com.enjoyf.platform.webapps.common.oauth.OAuthTokenGenerator;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.webapps.tools.weblogic.privilege.*;
import com.enjoyf.webapps.tools.webpage.controller.SessionConstants;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;

/**
 * Author: zhaoxin
 * Date: 11-10-27
 * Time: 上午10:49
 * Desc:
 */
@Controller
public class LoginController extends ToolsBaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @Resource(name = "privilegeWebLogic")
    private PrivilegeWebLogic privilegeWebLogic;


    /**
     * 登录
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/loginpage")
    public ModelAndView loginPage(HttpServletRequest request,
                                  @RequestParam(value = "loginName", required = false) String loginName,
                                  @RequestParam(value = "loginPwd", required = false) String loginPwd,
                                  @RequestParam(value = "reurl", required = false, defaultValue = "") String reurl) {

        logger.debug("进入 /loginPage.loginName=" + loginName + " ip:" + getIp());
        Map map = new HashMap();
        map.put("reurl", reurl);
        return new ModelAndView("/login", map);

    }

    /**
     * 退出
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout")
    public ModelAndView logout(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestParam(value = "reurl", required = false, defaultValue = "") String reurl) {
        request.getSession().invalidate();

        String cookieMessage = CookieUtil.getCookieValue(request, CacheUtil.TOOLS_COOKIEKEY_MESSAGE);
        if (!StringUtil.isEmpty(cookieMessage)) {
            String cookieKey = MD5Util.Md5(CacheUtil.getToolsCookeySecretKey() + cookieMessage);
            CacheUtil.removeToolsUserInfoCache(cookieKey);
        }

        //clear cookie
        try {
            CookieUtil.setCookie(request, response, CacheUtil.TOOLS_COOKIEKEY_MESSAGE, null, 0);
            CookieUtil.setCookie(request, response, CacheUtil.TOOLS_COOKIEKEY_ENCRYPT, null, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!StringUtil.isEmpty(reurl)) {
            return new ModelAndView("redirect:" + reurl);
        }


        return new ModelAndView("redirect:/loginpage");

    }

    /**
     * 登录
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/login")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "loginName", required = false) String loginName,
                              @RequestParam(value = "loginPwd", required = false) String loginPwd,
                              @RequestParam(value = "reurl", required = false, defaultValue = "") String reurl) {

        String reString = "redirect:/home";
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        if (Strings.isNullOrEmpty(loginName) || Strings.isNullOrEmpty(loginPwd)) {
            logger.debug("用户名密码为空");
            mapMessage.put("message", "user.login.notnull");
            mapMessage.put("reurl", reurl);
            reString = "/login";
            return new ModelAndView(reString, mapMessage);
        }

        PrivilegeUser entity = new PrivilegeUser();
        entity.setUserid(loginName);
        entity.setPassword(loginPwd);

        entity.setAccessip(getIp());


        Map loginReturnMap = null;
        try {
            loginReturnMap = privilegeWebLogic.login(entity);

        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }


        if (loginReturnMap == null) {
            mapMessage.put("msg", i18nSource.getMessage("error.tools.login.loginname.pwd.error", null, Locale.CHINA));
            reString = "/login";
            mapMessage.put("reurl", reurl);
            return new ModelAndView(reString, mapMessage);
        }

        List menuList = (List) loginReturnMap.get(SessionConstants.LIST_RESOURCE);//该用户具有的全部菜单集合
        Map mapUserPID = (Map) loginReturnMap.get(SessionConstants.USER_SESSION_PRIVILEGE_ID);
        //mapUserPID是登录用户具有权限资源id集合的map,会存放到用户的session中，当用户访问某个actionA的时候，系统权限filter会根据访问的anctionA
        //得到actionA所对应的rsid(资源id),然后比较用户session中存放的”权限资源id集合的map“中是否包含这个id,来决定是否可以访问这个actionA.
        PrivilegeUser privilegeUser = (PrivilegeUser) loginReturnMap.get(SessionConstants.CURRENT_USER);
        //privilegeUser 用户的PrivilegeUserEntity实体

        if (menuList != null && menuList.size() == 0) {
            logger.error("该用户没有被赋予任何系统权限或菜单,请联系系统管理员");
            reString = "/noPrivilegeURL";
            return new ModelAndView(reString);
        }

        List rootMenuList = generateRootList(menuList); //根据全部菜单集合生成一级菜单集合
        MenuTree rootTree = generateRootTree(menuList);    //根据全部菜单生成一级菜单的树
        HashMap treeMap = this.generateSecondTree(menuList, rootMenuList);//二级菜单树的map,map 的key 是一级菜单的rsId

        request.getSession().setAttribute(SessionConstants.TOP_ROOT_MENU_TREE, rootTree);  //该用户具有的一级菜单
        request.getSession().setAttribute(SessionConstants.LEFT_ROOT_MENU_TREE, treeMap);  //该用户具有的二级菜单树集合map、map的key 是一级菜单的id
        request.getSession().setAttribute(SessionConstants.USER_SESSION_PRIVILEGE_ID, mapUserPID);//用户权限 filter 过滤使用，参考上面mapUserPID注释
        request.getSession().setAttribute(SessionConstants.CURRENT_USER, privilegeUser);

        AuthToken authToken = null;
        String uploadUno = HotdeployConfigFactory.get().getConfig(ToolsHotdeployConfig.class).getUploadTokenUno();
        if (!StringUtil.isEmpty(uploadUno)) {
            try {
                authToken = OAuthTokenGenerator.generateAccessToken(EnvConfig.get().getOauthAppId(), uploadUno);
                request.getSession().setAttribute(SessionConstants.UPLOAD_TOKEN, authToken);
            } catch (OAuthException e) {
                GAlerter.lab(this.getClass().getName() + " occured OAuthException.", e);
            }
        }

        String cookieKey = writeCookie(request, response, privilegeUser);
        if (!StringUtil.isEmpty(cookieKey)) {
            ToolsUserInfo toolsUserInfo = new ToolsUserInfo();
            toolsUserInfo.setMenuTree(rootTree);
            toolsUserInfo.setTreeMap(treeMap);
            toolsUserInfo.setPrivilegeIds((HashMap) mapUserPID);
            toolsUserInfo.setPrivilegeUser(privilegeUser);
            toolsUserInfo.setUploadToken(authToken);
            CacheUtil.putToolsUserInfoCache(cookieKey, toolsUserInfo);
        }


        if (!StringUtil.isEmpty(reurl)) {
            return new ModelAndView("redirect:" + reurl);
        }

        return new ModelAndView(reString);
    }

    /**
     * 递归判断 child 是否 属于 parent
     *
     * @param parent
     * @param child
     * @param menulist
     * @return
     */
    private static boolean isExit(PrivilegeResource parent, PrivilegeResource child, List menulist) {
        boolean result = false;

        if (child == null) {//如果孩子实体为空,说明记录有问题,退出这个递归循环.防止有死循环
            result = false;
            return result;
        }

        if (child.getParentid().intValue() == -1) {//如果孩子实体的父ID等于-1代表它为一级菜单了返回false
            result = false;
            return result;
        }

        if (child.getParentid().intValue() == parent.getRsid().intValue()) {//如果孩子实体的父ID等于父实体的ID则返回true
            result = true;
            return result;
        }

        PrivilegeResource temp = new PrivilegeResource();
        temp.setParentid(child.getParentid());
        temp.setRslevel(child.getRslevel());
        for (Object obj1 : menulist) {
            PrivilegeResource RS = (PrivilegeResource) obj1; //一级菜单对象
            if (temp.getParentid().intValue() == RS.getRsid().intValue()) {
                if (RS.getRsid().intValue() == parent.getRsid().intValue()) {
                    result = true;
                } else {
                    temp.setParentid(RS.getParentid());
                    temp.setRslevel(RS.getRslevel());
                    result = isExit(parent, temp, menulist);//递归循环
                }
            }
        }

        return result;
    }

    /**
     * 根据该用户的全部菜单menuList,一级菜单集合生成包含二级菜单Tree的map.
     * 支持4级菜单
     *
     * @param menuList     该用户具有的全部菜单
     * @param rootMenuList 该用户具有的一级菜单
     * @return treeMap 包含二级菜单Tree的map.
     */
    private HashMap generateSecondTree(List menuList, List rootMenuList) {
        HashMap treeMap = new HashMap();
        for (Object obj1 : rootMenuList) {//循环一级菜单集合
            PrivilegeResource firstRS = (PrivilegeResource) obj1; //一级菜单对象
            MenuTree tree = MenuTree.getInstance();
            tree.setBaseTarget("incFrame");
            MenuTreeNode tn = null;
            tn = new MenuTreeNode(String.valueOf(firstRS.getRsid()), String.valueOf(firstRS.getParentid()), firstRS.getRsname());
            tree.addNode(tn);
            for (Object objMenu : menuList) {//循环菜单集合
                PrivilegeResource tempMenu = (PrivilegeResource) objMenu; //孩子菜单对象,可能是1,2,3,4..级
                if (isExit(firstRS, tempMenu, menuList)) {//判断这个菜单是否属于这个一级菜单
                    tn = new MenuTreeNode(String.valueOf(tempMenu.getRsid()), String.valueOf(tempMenu.getParentid()), tempMenu.getRsname());
                    if (tempMenu.getRsurl() != null && !tempMenu.getRsurl().trim().equals("nopage") && !tempMenu.getRsurl().trim().equals("stop")) {
                        tn.setUrl(tempMenu.getRsurl());
                    } else {
                        if (tempMenu.getRsurl() != null && tempMenu.getRsurl().trim().equals("stop")) {
                            tn.setUrl("javascript:alert(\"此功能尚未开通\")");
                        }
                    }
                    tn.setIconUrl("/static/images/tree/docfolder.gif");
                    tree.addNode(tn);
                }
            }
            treeMap.put(firstRS.getRsid().toString(), tree);
        }

        return treeMap;
    }


    private String writeCookie(HttpServletRequest request, HttpServletResponse response, PrivilegeUser privilegeUser) {
        String cookieKey = null;
        Set<PrivilegeRole> privilegeRoleses = privilegeUser.getPrivilegeRoleses();
        StringBuffer cookieStr = new StringBuffer();
        StringBuffer stringBuffer = new StringBuffer();
        for (PrivilegeRole role : privilegeRoleses) {
            stringBuffer.append(role.getRid() + ",");
        }
        long currTime = new Date().getTime();
        try {
            // role+"|"+uno+"|"+userid+"|"+username+"|"+time+"|"+encrypt
            cookieStr.append(stringBuffer.toString().substring(0, stringBuffer.length() - 1) + "|");
            cookieStr.append(privilegeUser.getUno() + "|");
            cookieStr.append(privilegeUser.getUserid() + "|");
            cookieStr.append(privilegeUser.getUsername() + "|");
            cookieStr.append(currTime);

            CookieUtil.setCookie(request, response, CacheUtil.TOOLS_COOKIEKEY_UID, privilegeUser.getUserid(), -1);

            String cookieMessage = URLEncoder.encode(cookieStr.toString(), "utf-8");
            CookieUtil.setCookie(request, response, CacheUtil.TOOLS_COOKIEKEY_MESSAGE, cookieMessage, -1);

            cookieKey = MD5Util.Md5(CacheUtil.getToolsCookeySecretKey() + cookieMessage);
            CookieUtil.setCookie(request, response, CacheUtil.TOOLS_COOKIEKEY_ENCRYPT, cookieKey, -1);
            return cookieKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cookieKey;
    }


    /**
     * 生成一级菜单的list
     *
     * @param meunList
     * @return
     */

    private List generateRootList(List meunList) {
        List rootMenuList = null;
        if (meunList == null || meunList.size() == 0) {
            return rootMenuList;
        }

        rootMenuList = new ArrayList();

        for (int i = 0; i < meunList.size(); i++) {
            PrivilegeResource menuF = (PrivilegeResource) meunList.get(i);
            try {
                if (menuF.getRslevel().equals(PrivilegeResourceLevel.LEVEL1) && menuF.getIsmenu().equals(ActStatus.ACTED)) {//菜单级别为1:代表一级菜单,ismenu:"A"代表在页面上显示
                    rootMenuList.add(menuF);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return rootMenuList;
    }


    /**
     * 生成Root菜单的Tree
     *
     * @param meunList
     * @return
     */
    private MenuTree generateRootTree(List meunList) {
        MenuTree menuTree = MenuTree.getInstance();
        menuTree.setBaseTarget("leftMenuFrame");
        MenuTreeNode rtn = null;
        if (meunList == null || meunList.size() == 0) {
            return null;
        }
        for (int i = 0; i < meunList.size(); i++) {
            PrivilegeResource menuF = (PrivilegeResource) meunList.get(i);
            if (menuF.getParentid().intValue() == -1 && menuF.getIsmenu().equals(ActStatus.ACTED)) {
                rtn = new MenuTreeNode(String.valueOf(menuF.getRsid()), String.valueOf(MenuTree.TREE_ROOT_ID), menuF.getRsname());
                rtn.setUrl(menuF.getRsurl());
                rtn.setIconUrl("/static/images/tree/docfolder.gif");
                menuTree.addNode(rtn);
            }
        }
        if (menuTree.checkTree()) {
            return menuTree;
        } else {
            return null;
        }
    }

}
