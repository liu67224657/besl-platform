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
import com.enjoyf.webapps.tools.weblogic.privilege.PrivilegeWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: taijunli
 * Date: 11-12-7
 * Time: 上午9:37
 */
@Controller
@RequestMapping(value = "/privilege/res")
public class PrivilegeResourceController extends ToolsBaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private int pageNum = 100;
    @Resource(name = "privilegeWebLogic")
    private PrivilegeWebLogic privilegeWebLogic;

    //************************************
    //********增加角色操作****************
    //************************************

    /**
     * @return 增加角色界面
     */
    @RequestMapping(value = "/createrespage")
    public ModelAndView createResPage() {
        return new ModelAndView("/privilege/createrespage");
    }

    /**
     * 添加资源
     *
     * @param rsname
     * @param rsurl
     * @param rslevel
     * @param status
     * @param parentid
     * @param orderfield
     * @param iconurl
     * @param rstype
     * @param ismenu
     * @param description
     * @return
     */
    @RequestMapping(value = "/saverespage")
    public ModelAndView saveResPage(
            @RequestParam(value = "rsname", required = false) String rsname,
            @RequestParam(value = "rsurl", required = false) String rsurl,
            @RequestParam(value = "rslevel", required = false) Integer rslevel,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "parentid", required = false) Integer parentid,
            @RequestParam(value = "orderfield", required = false) Integer orderfield,
            @RequestParam(value = "iconurl", required = false) String iconurl,
            @RequestParam(value = "rstype", required = false) Integer rstype,
            @RequestParam(value = "ismenu", required = false) String ismenu,
            @RequestParam(value = "description", required = false) String description) {
        Map<String, String> errorMsgMap = new HashMap<String, String>();
        Map<String, Object> msgMap = new HashMap<String, Object>();
        PrivilegeResource entity = new PrivilegeResource();
        boolean urlExist = false;

        if (!Strings.isNullOrEmpty(rsname)) {
            entity.setRsname(rsname);
        } else {
            errorMsgMap.put("rsname", "error.privilege.resource.rsname.null");
        }
        if (!Strings.isNullOrEmpty(rsurl)) {
            try {
                urlExist = privilegeWebLogic.isResExist(rsurl, null);
                if (urlExist) {
                    errorMsgMap.put("rsurl.duplication", "error.privilege.resource.rsurl.duplication");
                }
            } catch (ServiceException e) {
                GAlerter.lab("saveResPage 's isResExist caught an Exception: ", e);
            }
            entity.setRsurl(rsurl);
        } else {
            errorMsgMap.put("rsurl.null", "error.privilege.resource.rsurl.null");
        }
        if (rslevel != null) {
            if (PrivilegeResourceLevel.LEVEL1.getCode().intValue() == rslevel.intValue()) {
                entity.setRslevel(PrivilegeResourceLevel.LEVEL1);
            } else if (PrivilegeResourceLevel.LEVEL2.getCode().intValue() == rslevel.intValue()) {
                entity.setRslevel(PrivilegeResourceLevel.LEVEL2);
            } else if (PrivilegeResourceLevel.LEVEL3.getCode().intValue() == rslevel.intValue()) {
                entity.setRslevel(PrivilegeResourceLevel.LEVEL3);
            } else if (PrivilegeResourceLevel.LEVEL4.getCode().intValue() == rslevel.intValue()) {
                entity.setRslevel(PrivilegeResourceLevel.LEVEL4);
            }
        } else {
            errorMsgMap.put("rslevel", "error.privilege.resource.rslevel.null");
        }

        if (!Strings.isNullOrEmpty(status)) {
            if (ActStatus.ACTED.getCode().equals(status)) {
                entity.setStatus(ActStatus.ACTED);
            } else if (ActStatus.UNACT.getCode().equals(status)) {
                entity.setStatus(ActStatus.UNACT);
            }
        } else {
            errorMsgMap.put("status", "error.privilege.resource.status.null");
        }

        if (parentid != null) {
            entity.setParentid(parentid);

        } else {
            errorMsgMap.put("parentid", "error.privilege.resource.parentid.valid");
        }

        if (orderfield != null) {
            entity.setOrderfield(orderfield);
        }

        if (!Strings.isNullOrEmpty(ismenu)) {
            if (ActStatus.ACTED.getCode().equals(ismenu)) {
                entity.setIsmenu(ActStatus.ACTED);
            } else if (ActStatus.UNACT.getCode().equals(ismenu)) {
                entity.setIsmenu(ActStatus.UNACT);
            }
        }

        if (!Strings.isNullOrEmpty(iconurl)) {
            entity.setIconurl(iconurl);
        }

        if (rstype != null) {
            if (PrivilegeResourceType.MENU.getCode().equals(rstype)) {
                entity.setRstype(PrivilegeResourceType.MENU);
            } else if (PrivilegeResourceType.URL.getCode().equals(rstype)) {
                entity.setRstype(PrivilegeResourceType.URL);
            }
        } else {
            errorMsgMap.put("rstype", "error.privilege.resource.rstype.null");
        }

        if (!Strings.isNullOrEmpty(description)) {
            entity.setDescription(description);
        }

        msgMap.put("errorMsgMap", errorMsgMap);
        msgMap.put("entity", entity);

        if (!errorMsgMap.isEmpty()) {
            return new ModelAndView("/privilege/createrespage", msgMap);
        }

        boolean bool = false;
        try {

            if (!urlExist) {
                bool = privilegeWebLogic.saveRes(entity);
            }
            if (bool) {
                return new ModelAndView("redirect:/privilege/res/reslist");
            }
        } catch (ServiceException e) {
            GAlerter.lab("saveResPage a res SQLException :", e);
        }
        return new ModelAndView("/privilege/createrespage");
    }

    //************************************
    //********修改角色操作****************
    //************************************

    /**
     * @return 编辑角色界面
     */
    @RequestMapping(value = "/preeditrespage")
    public ModelAndView preEditResPage(
            @RequestParam(value = "rsid", required = false) Integer rsid) {
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        PrivilegeResource entity = new PrivilegeResource();

        try {
            if (!StringUtil.isEmpty(rsid.toString())) {
                entity = privilegeWebLogic.getResourceByRsid(rsid);
            }

        } catch (ServiceException e) {
            GAlerter.lab("preEditResPage a res SQLException :", e);
        }

        mapMsg.put("entity", entity);

        return new ModelAndView("/privilege/preeditrespage", mapMsg);
    }

    /**
     * @return 修改角色
     */
    @RequestMapping(value = "/editrespage")
    public ModelAndView modifyResPage(
            @RequestParam(value = "rsid", required = false) Integer rsid,
            @RequestParam(value = "rsname", required = false) String rsname,
            @RequestParam(value = "rsurl", required = false) String rsurl,
            @RequestParam(value = "rslevel", required = false) Integer rslevel,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "parentid", required = false) Integer parentid,
            @RequestParam(value = "orderfield", required = false) Integer orderfield,
            @RequestParam(value = "iconurl", required = false) String iconurl,
            @RequestParam(value = "rstype", required = false) Integer rsTypeCode,
            @RequestParam(value = "ismenu", required = false) String ismenu,
            @RequestParam(value = "description", required = false) String description) {

        Map<String, String> errorMsgMap = new HashMap<String, String>();
        Map<String, Object> msgMap = new HashMap<String, Object>();
        Map<ObjectField, Object> resMap = new HashMap<ObjectField, Object>();
        PrivilegeResource entity = new PrivilegeResource();
        boolean urlExist = false;

        if (rsid != null) {
            entity.setRsid(rsid);
        }
        if (!StringUtil.isEmpty(rsname)) {
            entity.setRsname(rsname);
        } else {
            errorMsgMap.put("rsname", "error.privilege.resource.rsname.null");
        }

        PrivilegeResourceType resType = PrivilegeResourceType.getByCode(rsTypeCode);
        if (resType != null) {
            entity.setRstype(resType);
        } else {
            errorMsgMap.put("rstype", "error.privilege.resource.rstype.null");
        }

        if (!Strings.isNullOrEmpty(rsurl)) {
            try {
                urlExist = privilegeWebLogic.isResExist(rsurl, rsid);
                if (urlExist) {
                    errorMsgMap.put("rsurl.duplication", "error.privilege.resource.rsurl.duplication");
                }
            } catch (ServiceException e) {
                GAlerter.lab("saveResPage 's isResExist caught an Exception: ", e);
            }
            entity.setRsurl(rsurl);
        } else if (Strings.isNullOrEmpty(rsurl) && resType.equals(PrivilegeResourceType.URL)) {
            errorMsgMap.put("rsurl.null", "error.privilege.resource.rsurl.null");
        }

        PrivilegeResourceLevel resLevel=PrivilegeResourceLevel.getByCode(rslevel);

        if (resLevel != null) {
            entity.setRslevel(resLevel);
//            if (PrivilegeResourceLevel.LEVEL1.getCode().intValue() == rslevel.intValue()) {
//                entity.setRslevel(PrivilegeResourceLevel.LEVEL1);
//            } else if (PrivilegeResourceLevel.LEVEL2.getCode().intValue() == rslevel.intValue()) {
//                entity.setRslevel(PrivilegeResourceLevel.LEVEL2);
//            } else if (PrivilegeResourceLevel.LEVEL3.getCode().intValue() == rslevel.intValue()) {
//                entity.setRslevel(PrivilegeResourceLevel.LEVEL3);
//            } else if (PrivilegeResourceLevel.LEVEL4.getCode().intValue() == rslevel.intValue()) {
//                entity.setRslevel(PrivilegeResourceLevel.LEVEL4);
//            }
        } else {
            errorMsgMap.put("rslevel", "error.privilege.resource.rslevel.null");
        }

        if (!Strings.isNullOrEmpty(status)) {
            if (ActStatus.ACTED.getCode().equals(status)) {
                entity.setStatus(ActStatus.ACTED);
            } else if (ActStatus.UNACT.getCode().equals(status)) {
                entity.setStatus(ActStatus.UNACT);
            }
        } else {
            errorMsgMap.put("status", "error.privilege.resource.status.null");
        }
        if (parentid != null) {
            entity.setParentid(parentid);

        } else {
            errorMsgMap.put("parentid", "error.privilege.resource.parentid.valid");
        }
        if (orderfield != null) {
            entity.setOrderfield(orderfield);
        }

        if (!Strings.isNullOrEmpty(ismenu)) {
            if (ActStatus.ACTED.getCode().equals(ismenu)) {
                entity.setIsmenu(ActStatus.ACTED);
            } else if (ActStatus.UNACT.getCode().equals(ismenu)) {
                entity.setIsmenu(ActStatus.UNACT);
            }
        }

        if (!Strings.isNullOrEmpty(iconurl)) {
            entity.setIconurl(iconurl);
        }

        if (!Strings.isNullOrEmpty(description)) {
            entity.setDescription(description);
        }

        msgMap.put("errorMsgMap", errorMsgMap);
        msgMap.put("entity", entity);

        if (!errorMsgMap.isEmpty()) {
            return new ModelAndView("/privilege/preeditrespage", msgMap);
        }

        resMap.put(PrivilegeResourceField.RSNAME, rsname);
        resMap.put(PrivilegeResourceField.RSURL, rsurl);
        resMap.put(PrivilegeResourceField.RSLEVEL,resLevel.getCode().intValue());


        if (ActStatus.ACTED.getCode().equals(status)) {
            resMap.put(PrivilegeResourceField.RSSTATUS, ActStatus.ACTED.getCode());
        } else if (ActStatus.UNACT.getCode().equals(status)) {
            resMap.put(PrivilegeResourceField.RSSTATUS, ActStatus.UNACT.getCode());
        }
        resMap.put(PrivilegeResourceField.PARENTID, parentid.intValue());
        resMap.put(PrivilegeResourceField.ORDERFIELD, orderfield.intValue());
        resMap.put(PrivilegeResourceField.ICONURL, iconurl);
        if (PrivilegeResourceType.MENU.getCode().equals(rsTypeCode)) {
            resMap.put(PrivilegeResourceField.RSTYPE, PrivilegeResourceType.MENU.getCode().intValue());
        } else if (PrivilegeResourceType.URL.getCode().equals(rsTypeCode)) {
            resMap.put(PrivilegeResourceField.RSTYPE, PrivilegeResourceType.URL.getCode().intValue());
        }
        if (ActStatus.ACTED.getCode().equals(ismenu)) {
            resMap.put(PrivilegeResourceField.ISMENU, ActStatus.ACTED.getCode());
        } else if (ActStatus.UNACT.getCode().equals(ismenu)) {
            resMap.put(PrivilegeResourceField.ISMENU, ActStatus.UNACT.getCode());
        }

        resMap.put(PrivilegeResourceField.DESCRIPTION, description);

        try {
            privilegeWebLogic.modifyRes(entity, resMap);

            ToolsLog log = new ToolsLog();

            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.RESOURCE_MODIFYRESPAGE);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setSrcId(rsid.toString());
            log.setOpAfter(StringUtil.truncate(entity.toString(), ToolsConstants.SPLIT_SIZE));

            addLog(log);
        } catch (ServiceException e) {
            GAlerter.lab("editResPage a res SQLException :", e);
        }
        return new ModelAndView("redirect:/privilege/res/reslist");
    }

    //************************************
    //********角色删除操作****************
    //************************************

    /**
     * @return 删除角色
     */
    @RequestMapping(value = "/batchupdate")
    public ModelAndView batchUpdateResources(@RequestParam(value = "status", required = false) String status,
                                             @RequestParam(value = "rsids", required = false) Integer[] rsids,
                                             @RequestParam(value = "updateRemoveStatusCode", required = false) String updateRemoveStatusCode,
                                             @RequestParam(value = "rsname", required = false) String rsname,
                                             @RequestParam(value = "rstype", required = false) String rstype,
                                             @RequestParam(value = "items", required = false, defaultValue = "0") int items,                   //总记录数
                                             @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,      //数据库记录索引
                                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems) {
        Pagination pagination = new Pagination(items, (pagerOffset / maxPageItems) + 1, WebappConfig.get().getHomePageSize());
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        Map<String, Integer> errorMsgMap = new HashMap<String, Integer>();
        boolean successful = false;

        if (rsids != null && rsids.length > 0) {
            int failedIndex = 0;
            for (Integer rsid : rsids) {
                PrivilegeResource entity = new PrivilegeResource();

                if (ActStatus.UNACT.getCode().equals(updateRemoveStatusCode)) {
                    entity.setStatus(ActStatus.ACTED);
                } else if (ActStatus.ACTED.getCode().equals(updateRemoveStatusCode)) {
                    entity.setStatus(ActStatus.UNACT);
                }
                entity.setRsid(rsid);

                try {
                    successful = privilegeWebLogic.deleteRes(entity.getStatus().getCode(), rsid);
                } catch (ServiceException e) {
                    GAlerter.lab("deleteRes a roles SQLException :", e);
                }

                if (successful) {
                    ToolsLog log = new ToolsLog();

                    log.setOpUserId(getCurrentUser().getUserid());
                    log.setOperType(LogOperType.RESOURCE_REMOVERES);
                    log.setOpTime(new Date());
                    log.setOpIp(getIp());
                    log.setSrcId(rsid.toString());
                    log.setOpBefore(updateRemoveStatusCode);

                    addLog(log);
                } else {
                    failedIndex++;
                }
            }
            errorMsgMap.put("failedNum", failedIndex);
        }
        mapMsg.put("rsname", rsname);
        mapMsg.put("rstype", rstype);
        mapMsg.put("status", status);
        mapMsg.put("page", pagination);
        mapMsg.put("errorMsgMap", errorMsgMap);

        return new ModelAndView("forward:/privilege/res/reslist", mapMsg);
    }

    //************************************
    //********查询资源操作****************
    //************************************

    /**
     * 按条件查询资源列表
     *
     * @param rsname
     * @param rstype
     * @param status
     * @param items
     * @param pagerOffset
     * @param maxPageItems
     * @return
     */
    @RequestMapping(value = "/reslist")
    public ModelAndView queryResList(
            @RequestParam(value = "rsname", required = false) String rsname,
            @RequestParam(value = "rstype", required = false) Integer rstype,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "items", required = false, defaultValue = "0") int items,                   //总记录数
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,      //数据库记录索引
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems) {   //每页记录数
        Pagination pagination = new Pagination(items, (pagerOffset / maxPageItems) + 1, WebappConfig.get().getHomePageSize());
        PrivilegeResource entity = new PrivilegeResource();

        if (!Strings.isNullOrEmpty(rsname)) {
            entity.setRsname(rsname);
        }
        if (rstype != null) {
            if (PrivilegeResourceType.MENU.getCode().intValue() == rstype.intValue()) {
                entity.setRstype(PrivilegeResourceType.MENU);
            } else if (PrivilegeResourceType.URL.getCode().intValue() == rstype.intValue()) {
                entity.setRstype(PrivilegeResourceType.URL);
            }
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
            PageRows<PrivilegeResource> pageRows = privilegeWebLogic.queryResList(entity, pagination);
            mapMsg.put("rows", pageRows.getRows());
            mapMsg.put("page", pageRows.getPage());
            mapMsg.put("rsname", rsname);
            mapMsg.put("rstype", rstype);
            mapMsg.put("status", status);
        } catch (ServiceException e) {
            GAlerter.lab("queryResList a Controller SQLException :", e);
        }

        return new ModelAndView("/privilege/reslist", mapMsg);
    }
}
