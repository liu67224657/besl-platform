package com.enjoyf.webapps.tools.webpage.controller.joymeapp.mobilegame;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.ForignContentDomain;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.weblogic.clientline.ClientLineWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by xupeng on 14-9-10.
 */
@Controller
@RequestMapping(value = "/mobile/top")
public class MobileGameTopController extends ToolsBaseController {

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "platform", required = false) String platform) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            int curPage = (pageStartIndex / pageSize) + 1;

            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.MobileGame.getCode()))
                    .add(QueryCriterions.in(ClientLineField.ITEM_TYPE, new Object[]{ClientItemType.GAMETOP.getCode(), ClientItemType.ADVERT.getCode()}))
                    .add(QuerySort.add(ClientLineField.DISPLAY_ORDER, QuerySortOrder.ASC));
            if (!StringUtil.isEmpty(platform)) {
                queryExpress.add(QueryCriterions.eq(ClientLineField.PLATFORM, Integer.parseInt(platform)));
                mapMessage.put("platform", platform);
            }
            PageRows<ClientLine> pageRows = JoymeAppServiceSngl.get().queryClientLineByPage(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/activitytopmenulist", mapMessage);
        }

        return new ModelAndView("/joymeapp/mobilegame/mobilegametop/mobilegametoplist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        List<Integer> list = new ArrayList<Integer>();

        list.add(ClientItemType.GAMETOP.getCode());
        list.add(ClientItemType.ADVERT.getCode());
        mapMessage.put("itemtypes", list);

        return new ModelAndView("/joymeapp/mobilegame/mobilegametop/creategametop", mapMessage);
    }

    @RequestMapping(value = "create")
    public ModelAndView create(HttpServletRequest request, @RequestParam(value = "linename", required = false) String lineName,
                               @RequestParam(value = "linecode", required = false) String lineCode,
                               @RequestParam(value = "linedesc", required = false) String lineDesc,
                               @RequestParam(value = "picurl", required = false) String picUrl,
                               @RequestParam(value = "platform", required = false) String platform,
                               @RequestParam(value = "picurl2", required = false) String picUrl2,
                               @RequestParam(value = "picurl3", required = false) String picUrl3,
                               @RequestParam(value = "intro", required = false) String intro,
                               @RequestParam(value = "hot", required = false) String hot) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        ClientLine clientLine = new ClientLine();
        try {
            String itemType = request.getParameter("itemtype");
            if (StringUtil.isEmpty(itemType)) {
                mapMessage = putErrorMessage(mapMessage, "类型为空");
                return new ModelAndView("/joymeapp/mobilegame/mobilegametop/creategametop", mapMessage);

            }
            if ("2".equals(platform)) {
                clientLine.setLineName(lineName);
                clientLine.setCode(lineCode + "_0");
                clientLine.setItemType(ClientItemType.getByCode(Integer.parseInt(itemType)));
                clientLine.setCreateDate(new Date());
                clientLine.setCreateUserid(getCurrentUser().getUserid());
                clientLine.setLine_desc(lineDesc);
                clientLine.setBigpic(picUrl);
                clientLine.setSmallpic(picUrl2);
                clientLine.setValidStatus(ValidStatus.INVALID);
                clientLine.setLineType(ClientLineType.MobileGame);
                clientLine.setPlatform(0);
                clientLine.setDisplay_order(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                clientLine.setHot(Integer.parseInt(hot));
                clientLine.setSharePic(picUrl3);
                clientLine.setLine_intro(intro);
                ClientLine returnLine = JoymeAppServiceSngl.get().createClientLine(clientLine);
                if (returnLine != null) {
                    ContentServiceSngl.get().getForignContent(returnLine.getLineId() + "", "", returnLine.getLineName(), returnLine.getLine_desc(), ForignContentDomain.GAG, "");
                }
                clientLine.setDisplay_order(clientLine.getDisplay_order() + 1);
                clientLine.setCode(lineCode + "_1");
                clientLine.setPlatform(1);
                ClientLine returnLine2 = JoymeAppServiceSngl.get().createClientLine(clientLine);
                if (returnLine2 != null) {
                    ContentServiceSngl.get().getForignContent(returnLine2.getLineId() + "", "", returnLine2.getLineName(), returnLine2.getLine_desc(), ForignContentDomain.GAG, "");
                }
            } else {
                clientLine.setLineName(lineName);
                clientLine.setCode(lineCode);
                clientLine.setItemType(ClientItemType.getByCode(Integer.parseInt(itemType)));
                clientLine.setCreateDate(new Date());
                clientLine.setCreateUserid(getCurrentUser().getUserid());
                clientLine.setLine_desc(lineDesc);
                clientLine.setBigpic(picUrl);
                clientLine.setSmallpic(picUrl2);
                clientLine.setValidStatus(ValidStatus.INVALID);
                clientLine.setLineType(ClientLineType.MobileGame);
                clientLine.setPlatform(Integer.parseInt(platform));
                clientLine.setDisplay_order(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                clientLine.setHot(Integer.parseInt(hot));
                clientLine.setSharePic(picUrl3);
                clientLine.setLine_intro(intro);
                ClientLine returnLine = JoymeAppServiceSngl.get().createClientLine(clientLine);
                if (returnLine != null) {
                    ContentServiceSngl.get().getForignContent(returnLine.getLineId() + "", "", returnLine.getLineName(), returnLine.getLine_desc(), ForignContentDomain.GAG, "");
                }
            }


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/activitytopmenulist", mapMessage);
        }

        return new ModelAndView("redirect:/mobile/top/list");
    }

    @RequestMapping(value = "/delete")
    public ModelAndView deleteLine(@RequestParam(value = "lineid", required = false) Long lineId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineField.VALID_STATUS, ValidStatus.REMOVED.getCode());
        updateExpress.set(ClientLineField.MODIFY_DATE, new Date());
        updateExpress.set(ClientLineField.MODIFY_USERID, getCurrentUser().getUserid());

        try {
            Boolean bool = JoymeAppServiceSngl.get().modifyClientLine(updateExpress, new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.DELETE_CLIENT_LINE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("clientLineId:" + lineId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/mobile/top/list");
    }

    @RequestMapping(value = "/recover")
    public ModelAndView modifyLine(@RequestParam(value = "lineid", required = false) Long lineId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode());
        updateExpress.set(ClientLineField.MODIFY_DATE, new Date());
        updateExpress.set(ClientLineField.MODIFY_USERID, getCurrentUser().getUserid());

        try {
            Boolean bool = JoymeAppServiceSngl.get().modifyClientLine(updateExpress, new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.RECOVER_CLIENT_LINE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("clientLineId:" + lineId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/mobile/top/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyLinePage(@RequestParam(value = "lineid", required = true) Long lineId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (lineId == null) {
            return new ModelAndView("redirect:/mobile/top/list");
        }

        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (clientLine == null) {
                return new ModelAndView("redirect:/mobile/top/list");
            }
            mapMessage.put("clientLine", clientLine);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/joymeapp/mobilegame/mobilegametop/modifygametop", mapMessage);
    }


    @RequestMapping(value = "/modify")
    public ModelAndView modifyLine(@RequestParam(value = "lineid", required = false) Long lineId,
                                   @RequestParam(value = "linename", required = false) String lineName,
                                   @RequestParam(value = "linecode", required = false) String lineCode,
                                   @RequestParam(value = "linedesc", required = false) String lineDesc,
                                   @RequestParam(value = "picurl", required = false) String picUrl,
                                   @RequestParam(value = "platform", required = false) String platform,
                                   @RequestParam(value = "picurl2", required = false) String picUrl2,
                                   @RequestParam(value = "picurl3", required = false) String picUrl3,
                                   @RequestParam(value = "intro", required = false) String intro,
                                   @RequestParam(value = "hot", required = false) String hot) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineField.LINE_NAME, lineName);
        updateExpress.set(ClientLineField.CODE, lineCode);
        updateExpress.set(ClientLineField.LINE_DESC, lineDesc);
        updateExpress.set(ClientLineField.BIGPIC, picUrl);
        updateExpress.set(ClientLineField.SHARE_PIC, picUrl3);
        updateExpress.set(ClientLineField.SMALLPIC, picUrl2);
        updateExpress.set(ClientLineField.MODIFY_DATE, new Date());
        updateExpress.set(ClientLineField.MODIFY_USERID, getCurrentUser().getUserid());
        updateExpress.set(ClientLineField.PLATFORM, Integer.parseInt(platform));
        updateExpress.set(ClientLineField.HOT, Integer.parseInt(hot));
        updateExpress.set(ClientLineField.LINE_INTRO, intro);
        try {
            ClientLine existLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.CODE, lineCode)).add(QueryCriterions.ne(ClientLineField.LINE_ID, lineId)));
            if (existLine != null) {
                mapMessage.put("lineid", lineId);
                mapMessage.put("codeExist", "line.code.exist");
                return new ModelAndView("forward:/clientline/iphone/news/modifypage", mapMessage);
            }

            Boolean bool = JoymeAppServiceSngl.get().modifyClientLine(updateExpress, new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_CLIENT_LINE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("clientLineId:" + lineId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/mobile/top/list");
    }

    @RequestMapping(value = "/sort/{sort}")
    @ResponseBody
    public String sort(@PathVariable(value = "sort") String sort,
                       @RequestParam(value = "lineid", required = false) Long lineId,
                       @RequestParam(value = "platform", required = false) String platform) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
        if (lineId == null) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            return binder.toJson(resultObjectMsg);
        }
        Long returnItemId = null;
        try {
            returnItemId = ClientLineWebLogic.clientLineSort(sort, lineId, ClientLineType.MobileGame, platform);
            if (returnItemId == null) {
                resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
                return binder.toJson(resultObjectMsg);
            }
        } catch (Exception e) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            resultObjectMsg.setMsg(i18nSource.getMessage("system.error", null, null));
            return binder.toJson(resultObjectMsg);
        }
        mapMessage.put("sort", sort);
        mapMessage.put("lineid", lineId);
        mapMessage.put("returnitemid", returnItemId);
        resultObjectMsg.setResult(mapMessage);
        return binder.toJson(resultObjectMsg);
    }

}
