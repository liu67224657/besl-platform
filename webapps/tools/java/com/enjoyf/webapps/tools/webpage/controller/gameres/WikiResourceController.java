package com.enjoyf.webapps.tools.webpage.controller.gameres;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.gameres.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsConstants;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.weblogic.gameres.WikiResourceWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;

/**
 * User: taijunli
 * Date: 11-1-4
 * Time: 上午9:37
 */
@Deprecated
@Controller
@RequestMapping(value = "/wiki")
public class WikiResourceController extends ToolsBaseController {
    //
    private Logger logger = LoggerFactory.getLogger(WikiResourceController.class);

    //
    private JsonBinder jsonbinder = JsonBinder.buildNormalBinder();

    @Resource(name = "wikiResourceWebLogic")
    private WikiResourceWebLogic wikiResourceWebLogic;

    @RequestMapping(value = "/list")
    public ModelAndView queryGameResList(
            @RequestParam(value = "resourceName", required = false) String resourceName,
            @RequestParam(value = "removeStatusCode", required = false) String removeStatusCode,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,      //数据库记录索引
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int maxPageItems) {   //每页记录数
        //
        Pagination pagination = new Pagination(((pagerOffset / maxPageItems) + 1) * maxPageItems, (pagerOffset / maxPageItems) + 1, maxPageItems);

        Map<String, Object> mapMsg = new HashMap<String, Object>();

        mapMsg.put("resourceName", resourceName);
        mapMsg.put("removeStatusCode", removeStatusCode);
        //
        mapMsg.put("removeStatuses", ActStatus.getAll());

        //
        QueryExpress queryExpress = new QueryExpress();
        if (ActStatus.getByCode(removeStatusCode) != null) {
            queryExpress.add(QueryCriterions.eq(WikiResourceField.REMOVESTATUS, ActStatus.getByCode(removeStatusCode).getCode()));
        }


        if (!Strings.isNullOrEmpty(resourceName)) {
            queryExpress.add(QueryCriterions.like(WikiResourceField.WIKINAME, "%" + resourceName + "%"));
        }
        queryExpress.add(QuerySort.add(WikiResourceField.CREATEDATE, QuerySortOrder.DESC));

        //
        try {
            PageRows<WikiResource> pageRows = wikiResourceWebLogic.queryWikiResources(queryExpress, pagination);

            mapMsg.put("rows", pageRows.getRows());
            mapMsg.put("page", pageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab("queryUserList a Controller ServiceException :", e);
        }

        return new ModelAndView("/gameresource/wikiresourcelist", mapMsg);
    }


    //
    @RequestMapping(value = "/createpage")
    public ModelAndView preCreateGameResource() {
        return new ModelAndView("/gameresource/createwiki");
    }

    @RequestMapping(value = "/checkname")
    @ResponseBody
    public String checkName(
            @RequestParam(value = "wikiName", required = true) String wikiName,
            @RequestParam(value = "resourceId", required = false, defaultValue = "0") long resourceIdItself) {
        //供ajax使用，存在返回1 ;不存在返回0
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);

        if (isNameExist(wikiName, resourceIdItself)) {
            resultMsg.setStatus_code(JoymeResultMsg.CODE_S);
        } else {
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
        }

        return jsonbinder.toJson(resultMsg);
    }


    @RequestMapping(value = "/create")
    public ModelAndView createGameResource(@RequestParam(value = "wikiName", required = true) String wikiName,
                                           @RequestParam(value = "wikiDesc", required = false) String wikiDesc,
                                           @RequestParam(value = "wikiCode", required = true) String wikiCode,
                                           @RequestParam(value = "wikiUrl", required = true) String wikiUrl,
                                           @RequestParam(value = "thumbicon", required = false) String thumbIcon,
                                           @RequestParam(value = "icon", required = true) String icon) {
        WikiResource wikiResource = new WikiResource();
        wikiResource.setWikiCode(wikiCode);
        wikiResource.setWikiDesc(wikiDesc);
        wikiResource.setWikiName(wikiName);
        wikiResource.setWikiUrl(wikiUrl);
        wikiResource.setIcon(icon);
        wikiResource.setThumbIcon(thumbIcon);
        wikiResource.setCreateDate(new Date());
        wikiResource.setCreateUserid(getCurrentUser().getUserid());
        wikiResource.setRemoveStatus(ActStatus.UNACT);


        Map<String, Object> mapMessage = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();
        long resourceId = 0;

        if (isNameExist(wikiName, 0)) {
            mapMessage.put("entity", wikiResource);
            errorMsgMap.put("resourceName", "error.wikiresource.resourcename.exist");
            return new ModelAndView("/gameresource/createwiki", mapMessage);
        }

        if (isCodeExist(wikiCode, 0)) {
            mapMessage.put("entity", wikiResource);
            errorMsgMap.put("resourceName", "error.wikiresource.code.exist");
            return new ModelAndView("/gameresource/createwiki", mapMessage);
        }


        try {
            wikiResource=GameResourceServiceSngl.get().createWikiResource(wikiResource);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException,e:", e);
        }

        //
        ToolsLog log = new ToolsLog();

        log.setOpUserId(getCurrentUser().getUserid());
        log.setOperType(LogOperType.WIKIRESOURCE_SAVE);
        log.setOpTime(new Date());
        log.setOpIp(getIp());
        log.setOpAfter(StringUtil.truncate(wikiResource.toString(), ToolsConstants.SPLIT_SIZE));

        addLog(log);
        return new ModelAndView("redirect:/wiki/list");
    }

    @RequestMapping(value = "/editpage")
    public ModelAndView preEditGameResPage(
            @RequestParam(value = "resourceId", required = false) Long resourceId) {  //游戏条目ID
        //
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            WikiResource wikiResource = wikiResourceWebLogic.getWikiReosurce(resourceId);

            mapMessage.put("resourceId", wikiResource.getResourceId());
            mapMessage.put("wikiName", wikiResource.getWikiName());
            mapMessage.put("wikiCode", wikiResource.getWikiCode());
            mapMessage.put("wikiUrl", wikiResource.getWikiUrl());
            mapMessage.put("icon", wikiResource.getIcon());
            mapMessage.put("thumbIcon", wikiResource.getThumbIcon());
            mapMessage.put("wikiDesc", wikiResource.getWikiDesc());

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException,e: ", e);
            return new ModelAndView("/common/error", mapMessage);
        }

        return new ModelAndView("/gameresource/editwiki", mapMessage);
    }


    @RequestMapping(value = "/edit")
    public ModelAndView editGameResource(
            @RequestParam(value = "resourceId", required = true) long resourceId,
            @RequestParam(value = "wikiName", required = true) String wikiName,
            @RequestParam(value = "wikiDesc", required = false) String wikiDesc,
            @RequestParam(value = "wikiCode", required = true) String wikiCode,
            @RequestParam(value = "wikiUrl", required = true) String wikiUrl,
            @RequestParam(value = "thumbicon", required = false) String thumbIcon,
            @RequestParam(value = "icon", required = true) String icon) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();
        if (isNameExist(wikiName, resourceId)) {
            mapMessage.put("resourceId", resourceId);
            mapMessage.put("wikiName", wikiName);
            mapMessage.put("wikiCode", wikiCode);
            mapMessage.put("wikiUrl", wikiUrl);
            mapMessage.put("icon", icon);
            mapMessage.put("thumbIcon", thumbIcon);
            mapMessage.put("wikiDesc", wikiDesc);
            errorMsgMap.put("resourceName", "error.wikiresource.resourcename.exist");
            mapMessage.put("errorMsgMap", errorMsgMap);
            return new ModelAndView("/gameresource/editwiki", mapMessage);
        }

        if (isCodeExist(wikiCode, resourceId)) {
            mapMessage.put("resourceId", resourceId);
            mapMessage.put("wikiName", wikiName);
            mapMessage.put("wikiCode", wikiCode);
            mapMessage.put("wikiUrl", wikiUrl);
            mapMessage.put("icon", icon);
            mapMessage.put("thumbIcon", thumbIcon);
            mapMessage.put("wikiDesc", wikiDesc);
            errorMsgMap.put("resourceName", "error.wikiresource.code.exist");
            mapMessage.put("errorMsgMap", errorMsgMap);
            return new ModelAndView("/gameresource/editwiki", mapMessage);
        }


        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(WikiResourceField.WIKINAME, wikiName);
        updateExpress.set(WikiResourceField.WIKIDESC, wikiDesc);
        updateExpress.set(WikiResourceField.WIKICODE, wikiCode);
        updateExpress.set(WikiResourceField.WIKIURL, wikiUrl);
        updateExpress.set(WikiResourceField.THUMBICON, thumbIcon);
        updateExpress.set(WikiResourceField.ICON, icon);

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(WikiResourceField.RESOURCEID, resourceId));

        try {
            GameResourceServiceSngl.get().modifyWikiResource(updateExpress, queryExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            mapMessage.put("resourceId", resourceId);
            mapMessage.put("wikiName", wikiName);
            mapMessage.put("wikiCode", wikiCode);
            mapMessage.put("wikiUrl", wikiUrl);
            mapMessage.put("icon", icon);
            mapMessage.put("thumbIcon", thumbIcon);
            mapMessage.put("wikiDesc", wikiDesc);
            errorMsgMap.put("resourceName", "system.exception");
            mapMessage.put("errorMsgMap", errorMsgMap);
            return new ModelAndView("/gameresource/editwiki", mapMessage);
        }

        //
        ToolsLog log = new ToolsLog();

        log.setOpUserId(getCurrentUser().getUserid());
        log.setOperType(LogOperType.WIKIRESOURCE_MODIFY);
        log.setOpTime(new Date());
        log.setOpIp(getIp());
        log.setOpAfter(String.valueOf(resourceId));

        addLog(log);
        return new ModelAndView("redirect:/wiki/list");
    }


    @RequestMapping(value = "/batchmodifystatus")
    public ModelAndView batchWikiStatus(
            @RequestParam(value = "resourceIds", required = false) String resourceIds,
            //
            @RequestParam(value = "resourceName", required = false) String resourceName,
            @RequestParam(value = "removeStatusCode", required = false) String removeStatusCode,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int maxPageItems,
            //
            @RequestParam(value = "updateRemoveStatusCode", required = false) String updateRemoveStatusCode) {
        //

        Map<String, Object> mapMsg = new HashMap<String, Object>();

        mapMsg.put("resourceName", resourceName);
        mapMsg.put("removeStatusCode", removeStatusCode);
        mapMsg.put("updateRemoveStatusCode", updateRemoveStatusCode);
        mapMsg.put("removeStatuses", ActStatus.getAll());

        //
        ActStatus updateStatus = ActStatus.getByCode(updateRemoveStatusCode);
        try {
            if (!Strings.isNullOrEmpty(resourceIds) && updateStatus != null) {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(WikiResourceField.REMOVESTATUS, updateStatus.getCode());
                updateExpress.set(WikiResourceField.LASTMODIFYDATE, new Date());
                updateExpress.set(WikiResourceField.MODIFYUSERID, String.valueOf(getCurrentUser().getUserid()));

                //
                String[] resourceIdSplits = resourceIds.split(",");
                for (String resourceId : resourceIdSplits) {
                    //
                    GameResourceServiceSngl.get().modifyWikiResource(updateExpress, new QueryExpress()
                            .add(QueryCriterions.eq(WikiResourceField.RESOURCEID, Long.parseLong(resourceId))));
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab("batchStatusLine, ServiceException :", e);
        }

        ToolsLog log = new ToolsLog();

        log.setOpUserId(getCurrentUser().getUserid());
        log.setOperType(LogOperType.WIKIRESOURCE_MODIFY);
        log.setOpTime(new Date());
        log.setOpIp(getIp());
        log.setOpAfter(String.valueOf(resourceIds));

        addLog(log);
        return new ModelAndView("forward:/wiki/list", mapMsg);
    }

    @RequestMapping(value = "/stat")
    public ModelAndView wikiStat(
            @RequestParam(value = "resid") long resourceId) {

        try {
            GameResourceServiceSngl.get().statWiki(resourceId);
        } catch (ServiceException e) {
            GAlerter.lab("batchStatusLine, ServiceException :", e);
        }

        return new ModelAndView("redirect:/wiki/detail?resid=" + resourceId);
    }


    @RequestMapping(value = "/detail")
    public ModelAndView resDetail(@RequestParam(value = "resid", defaultValue = "0") long resourceId) {
        if (logger.isDebugEnabled()) {
            logger.debug("resourceId is =" + resourceId);
        }

        Map<String, Object> msgMap = new HashMap<String, Object>();

        if (resourceId != 0) {
            try {
                WikiResource wikiResource = wikiResourceWebLogic.getWikiReosurce(resourceId);

                msgMap.put("wiki", wikiResource);
            } catch (ServiceException e) {

                GAlerter.lab("query a GameResource caught an exception:", e);
            }
        }

        return new ModelAndView("/gameresource/wikidetail", msgMap);

    }


    @RequestMapping(value = "/relationlist")
    public ModelAndView relationList(@RequestParam(value = "resid", defaultValue = "0") long resourceId) {
        if (logger.isDebugEnabled()) {
            logger.debug("resourceId is =" + resourceId);
        }

        Map<String, Object> msgMap = new HashMap<String, Object>();
//
//        if (resourceId != 0) {
//            try {
//                List<GameRelationDTO> relationList = gameResourceWebLogic.queryGameRelationDTObyResourceId(resourceId);
//
//                msgMap.put("relationList", relationList);
//                msgMap.put("resid", resourceId);
//                msgMap.put("validStatuses", ValidStatus.getAll());
//            } catch (ServiceException e) {
//
//                GAlerter.lab("query a GameResource caught an exception:", e);
//            }
//        }

        return new ModelAndView("/gameresource/tab_relationlist", msgMap);
    }

    @RequestMapping(value = "/addrelationpage")
    public ModelAndView preAddResRelation(@RequestParam(value = "resid", defaultValue = "0") long resourceId) {
        if (logger.isDebugEnabled()) {
            logger.debug("resourceId is =" + resourceId);
        }

        Map<String, Object> msgMap = new HashMap<String, Object>();


        return new ModelAndView("/gameresource/preaddresrelation", msgMap);
    }

    @RequestMapping(value = "/addresrelation")
    public ModelAndView addResRelation(@RequestParam(value = "resid", defaultValue = "0") long resourceId,
                                       @RequestParam(value = "relationType", required = false) String relationType,
                                       @RequestParam(value = "relationValue", required = false) String relationValue,
                                       @RequestParam(value = "relationName", required = false) String relationName,
                                       @RequestParam(value = "sortNum", required = false) Integer sortNum,
                                       @RequestParam(value = "validStatus", required = false) String validStatus) {
        if (logger.isDebugEnabled()) {
            logger.debug("resourceId is =" + resourceId);
        }

        relationValue = relationValue.trim();

        Map<String, Object> msgMap = new HashMap<String, Object>();


        return new ModelAndView("forward:/gameresource/relationlist", msgMap);
    }


    @RequestMapping(value = "/batchupdaterelation")
    public ModelAndView batchUpdateRelationStatus(@RequestParam(value = "resid", defaultValue = "0") long resourceId,
                                                  @RequestParam(value = "relationids", required = false) String relationids,
                                                  @RequestParam(value = "updateValidStatusCode", required = false) String updateValidStatusCode) {

        Map<String, Object> mapMsg = new HashMap<String, Object>();


        return new ModelAndView("forward:/gameresource/relationlist", mapMsg);

    }

    @RequestMapping(value = "/preupdaterelation")
    public ModelAndView preUpdateRelation(
            @RequestParam(value = "resid") Long resourceId,
            @RequestParam(value = "relationid") Long relationId) {
        Map<String, Object> msgMap = new HashMap<String, Object>();


        return new ModelAndView("/gameresource/preupdaterelation", msgMap);
    }

    @RequestMapping(value = "/updaterelation")
    public ModelAndView updateRelation(@RequestParam(value = "resid", defaultValue = "0") long resourceId,
                                       @RequestParam(value = "relationid", defaultValue = "0") long relationId,
                                       @RequestParam(value = "relationType", required = false) String relationType,
                                       @RequestParam(value = "relationValue", required = false) String relationValue,
                                       @RequestParam(value = "relationName", required = false) String relationName,
                                       @RequestParam(value = "sortNum", defaultValue = "0") int sortNum) {
        Map<String, Object> msgMap = new HashMap<String, Object>();


        return new ModelAndView("forward:/gameresource/relationlist", msgMap);
    }

    private List<String> getJsonListValue(Map<String, Boolean> checkMap) {
        List<String> returnList = new ArrayList<String>();
        //过滤掉不存在的
        Iterator<Map.Entry<String, Boolean>> iterator = checkMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Boolean> entry = iterator.next();
            if (!entry.getValue()) {
                iterator.remove();
            }
        }

        if (!checkMap.isEmpty()) {
            for (Map.Entry<String, Boolean> object : checkMap.entrySet()) {
                returnList.add(object.getKey());
            }
        }

        return returnList;
    }

    private boolean isNameExist(String resourceName, long resourceId) {
        //准备参数

        try {
            return wikiResourceWebLogic.isNameExist(resourceName, resourceId);
        } catch (ServiceException e) {
            GAlerter.lab("GameResourceController caught an Exception :" + e);
        }

        return true;   //true or false?

    }

    private boolean isCodeExist(String resourceName, long resourceId) {
        //准备参数

        try {
            return wikiResourceWebLogic.isCodeExist(resourceName, resourceId);
        } catch (ServiceException e) {
            GAlerter.lab("GameResourceController caught an Exception :" + e);
        }

        return true;   //true or false?

    }

}
