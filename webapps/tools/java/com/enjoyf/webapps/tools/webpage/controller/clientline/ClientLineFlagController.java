package com.enjoyf.webapps.tools.webpage.controller.clientline;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-12-10
 * Time: 下午2:08
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/clientline/flag")
public class ClientLineFlagController extends ToolsBaseController {

    private static final int CLIENT_PLATFORM_ANDROID = ClientPlatform.ANDROID.getCode();

    @RequestMapping(value = "/list")
    public ModelAndView lineList(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                 @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                                 @RequestParam(value = "flagtype", required = false) Integer flagType) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("flagType", flagType);
        mapMessage.put("flagTypes", ClientLineFlagType.getAll());
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ClientLineFlagField.FLAG_TYPE, flagType));
        queryExpress.add(QueryCriterions.eq(ClientLineFlagField.VALID_STATUS, ValidStatus.VALID.getCode()));
        queryExpress.add(QuerySort.add(ClientLineFlagField.CREATE_DATE, QuerySortOrder.DESC));

        try {
            PageRows<ClientLineFlag> flagPageRows = JoymeAppServiceSngl.get().queryClientLineFlagByPage(queryExpress, pagination);
            if (flagPageRows != null) {
                mapMessage.put("list", flagPageRows.getRows());
                mapMessage.put("page", flagPageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/clientline/flag/flaglist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createLinePage(@RequestParam(value = "flagtype", required = false) Integer flagType) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (flagType == null) {
            return new ModelAndView("redirect:/clientline/flag/list");
        }
        mapMessage.put("flagType", flagType);
        mapMessage.put("clientLine", ClientLineFlagType.CLIENT_LINE.getCode());
        mapMessage.put("topMenu", ClientLineFlagType.TOP_MENU.getCode());
        try {
            if (ClientLineFlagType.CLIENT_LINE.equals(ClientLineFlagType.getByCode(flagType))) {
                List<ClientLine> lineList = JoymeAppServiceSngl.get().queryClientLineList(new QueryExpress().add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode())));
                if (CollectionUtil.isEmpty(lineList)) {
                    return new ModelAndView("redirect:/clientline/flag/list");
                }
                mapMessage.put("lineList", lineList);
            } else {
                mapMessage.put("platforms", ClientPlatform.getAll());
            }

            //得到存在的app列表
            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(new QueryExpress()
                    .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                    .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                    .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
            );
            mapMessage.put("appList", appList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/clientline/flag/createflag", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView createLine(@RequestParam(value = "flagtype", required = false) Integer flagType,
                                   @RequestParam(value = "flagdesc", required = false) String flagDesc,
                                   @RequestParam(value = "lineid", required = false) Long lineId,
                                   @RequestParam(value = "maxitemid", required = false) Long maxItemId,
                                   @RequestParam(value = "appid", required = false) String appId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            ClientLineFlag flag = new ClientLineFlag();
            flag.setFlagDesc(flagDesc);
            flag.setClientLineFlagType(ClientLineFlagType.getByCode(flagType));

            if (ClientLineFlagType.CLIENT_LINE.equals(ClientLineFlagType.getByCode(flagType))) {
                ClientLine line = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId))
                        .add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode())));
                if (line == null) {
                    return new ModelAndView("redirect:/clientline/flag/createpage?flagtype=" + flagType, mapMessage);
                }
                ClientLineFlag existFlag = JoymeAppServiceSngl.get().getClientLineFlag(new QueryExpress()
                        .add(QueryCriterions.eq(ClientLineFlagField.FLAG_TYPE, ClientLineFlagType.CLIENT_LINE.getCode()))
                        .add(QueryCriterions.eq(ClientLineFlagField.VALID_STATUS, ValidStatus.VALID.getCode()))
                        .add(QueryCriterions.eq(ClientLineFlagField.LINE_ID, lineId)));
                if (existFlag != null) {
                    mapMessage.put("flagType", flagType);
                    mapMessage.put("flagDesc", flagDesc);
                    mapMessage.put("maxItemId", maxItemId);
                    mapMessage.put("lineId", lineId);
                    mapMessage.put("clientLine", ClientLineFlagType.CLIENT_LINE.getCode());
                    mapMessage.put("topMenu", ClientLineFlagType.TOP_MENU.getCode());
                    if (ClientLineFlagType.CLIENT_LINE.equals(ClientLineFlagType.getByCode(flagType))) {
                        List<ClientLine> lineList = JoymeAppServiceSngl.get().queryClientLineList(new QueryExpress().add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode())));
                        if (CollectionUtil.isEmpty(lineList)) {
                            return new ModelAndView("redirect:/clientline/flag/list");
                        }
                        mapMessage.put("lineList", lineList);
                    }
                    mapMessage.put("existError", "client.flag.line.exist");
                    return new ModelAndView("/clientline/flag/createflag", mapMessage);
                }
                flag.setLineId(line.getLineId());
                flag.setLineCode(line.getCode());
            } else {
                ClientLineFlag existFlag = JoymeAppServiceSngl.get().getClientLineFlag(new QueryExpress()
                        .add(QueryCriterions.eq(ClientLineFlagField.LINE_CODE, appId))
                        .add(QueryCriterions.eq(ClientLineFlagField.FLAG_TYPE, ClientLineFlagType.TOP_MENU.getCode()))
                        .add(QueryCriterions.eq(ClientLineFlagField.VALID_STATUS, ValidStatus.VALID.getCode()))
                        .add(QueryCriterions.eq(ClientLineFlagField.LINE_ID, lineId)));
                if (existFlag != null) {
                    mapMessage.put("flagType", flagType);
                    mapMessage.put("flagDesc", flagDesc);
                    mapMessage.put("maxItemId", maxItemId);
                    mapMessage.put("lineId", lineId);
                    mapMessage.put("appId", appId);
                    mapMessage.put("clientLine", ClientLineFlagType.CLIENT_LINE.getCode());
                    mapMessage.put("topMenu", ClientLineFlagType.TOP_MENU.getCode());
                    mapMessage.put("platforms", ClientPlatform.getAll());
                    mapMessage.put("existError", "client.flag.platform.exist");
                    return new ModelAndView("/clientline/flag/createflag", mapMessage);
                }
                flag.setLineId(lineId);//platform 平台
                flag.setLineCode(appId);
            }

            flag.setMaxItemId(maxItemId);
            flag.setValidStatus(ValidStatus.VALID);
            flag.setCreateDate(new Date());
            flag.setCreateIp(getIp());
            flag.setCreateUserId(getCurrentUser().getUserid());

            JoymeAppServiceSngl.get().createClientLineFlag(flag);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("redirect:/clientline/flag/list?flagtype=" + flagType);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyLinePage(@RequestParam(value = "flagid", required = false) Long flagId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (flagId == null) {
            return new ModelAndView("redirect:/clientline/flag/list");
        }
        mapMessage.put("clientLine", ClientLineFlagType.CLIENT_LINE.getCode());
        mapMessage.put("topMenu", ClientLineFlagType.TOP_MENU.getCode());
        try {
            ClientLineFlag flag = JoymeAppServiceSngl.get().getClientLineFlag(new QueryExpress().add(QueryCriterions.eq(ClientLineFlagField.FLAG_ID, flagId)));
            if (flag == null) {
                return new ModelAndView("redirect:/clientline/flag/list");
            }
            mapMessage.put("flag", flag);

            if (ClientLineFlagType.CLIENT_LINE.equals(flag.getClientLineFlagType())) {
                List<ClientLine> lineList = JoymeAppServiceSngl.get().queryClientLineList(new QueryExpress().add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode())));
                if (CollectionUtil.isEmpty(lineList)) {
                    return new ModelAndView("redirect:/clientline/flag/list");
                }
                mapMessage.put("lineList", lineList);
            } else {
                mapMessage.put("platforms", ClientPlatform.getAll());
            }

            //得到存在的app列表
            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(new QueryExpress()
                    .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                    .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                    .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
            );
            mapMessage.put("appList", appList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/clientline/flag/modifyflag", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modifyLine(@RequestParam(value = "flagid", required = false) Long flagId,
                                   @RequestParam(value = "flagtype", required = false) Integer flagType,
                                   @RequestParam(value = "flagdesc", required = false) String flagDesc,
                                   @RequestParam(value = "lineid", required = false) Long lineId,
                                   @RequestParam(value = "maxitemid", required = false) Long maxItemId,
                                   @RequestParam(value = "appid", required = false) String appId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ClientLineFlagField.FLAG_DESC, flagDesc);
            if (ClientLineFlagType.CLIENT_LINE.equals(ClientLineFlagType.getByCode(flagType))) {
                ClientLine line = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId))
                        .add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode())));
                if (line == null) {
                    return new ModelAndView("redirect:/clientline/flag/createpage?flagtype=" + flagType, mapMessage);
                }
                ClientLineFlag existFlag = JoymeAppServiceSngl.get().getClientLineFlag(new QueryExpress()
                        .add(QueryCriterions.eq(ClientLineFlagField.LINE_ID, lineId))
                        .add(QueryCriterions.eq(ClientLineFlagField.FLAG_TYPE, ClientLineFlagType.CLIENT_LINE.getCode()))
                        .add(QueryCriterions.eq(ClientLineFlagField.VALID_STATUS, ValidStatus.VALID.getCode()))
                        .add(QueryCriterions.ne(ClientLineFlagField.FLAG_ID, flagId)));
                if (existFlag != null) {
                    ClientLineFlag flag = JoymeAppServiceSngl.get().getClientLineFlag(new QueryExpress().add(QueryCriterions.eq(ClientLineFlagField.FLAG_ID, flagId)));
                    if (flag == null) {
                        return new ModelAndView("redirect:/clientline/flag/list");
                    }
                    mapMessage.put("flag", flag);

                    List<ClientLine> lineList = JoymeAppServiceSngl.get().queryClientLineList(new QueryExpress().add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode())));
                    if (CollectionUtil.isEmpty(lineList)) {
                        return new ModelAndView("redirect:/clientline/flag/list");
                    }
                    mapMessage.put("lineList", lineList);

                    mapMessage.put("clientLine", ClientLineFlagType.CLIENT_LINE.getCode());
                    mapMessage.put("topMenu", ClientLineFlagType.TOP_MENU.getCode());
                    mapMessage.put("existError", "client.flag.line.exist");
                    return new ModelAndView("/clientline/flag/modifyflag", mapMessage);
                }
                updateExpress.set(ClientLineFlagField.LINE_ID, line.getLineId());
                updateExpress.set(ClientLineFlagField.LINE_CODE, line.getCode());
            } else {
                ClientLineFlag existFlag = JoymeAppServiceSngl.get().getClientLineFlag(new QueryExpress()
                        .add(QueryCriterions.eq(ClientLineFlagField.LINE_CODE, appId))
                        .add(QueryCriterions.eq(ClientLineFlagField.LINE_ID, lineId))
                        .add(QueryCriterions.eq(ClientLineFlagField.FLAG_TYPE, ClientLineFlagType.TOP_MENU.getCode()))
                        .add(QueryCriterions.eq(ClientLineFlagField.VALID_STATUS, ValidStatus.VALID.getCode()))
                        .add(QueryCriterions.ne(ClientLineFlagField.FLAG_ID, flagId)));
                if (existFlag != null) {
                    ClientLineFlag flag = JoymeAppServiceSngl.get().getClientLineFlag(new QueryExpress().add(QueryCriterions.eq(ClientLineFlagField.FLAG_ID, flagId)));
                    if (flag == null) {
                        return new ModelAndView("redirect:/clientline/flag/list");
                    }
                    mapMessage.put("flag", flag);
                    mapMessage.put("platforms", ClientPlatform.getAll());
                    mapMessage.put("clientLine", ClientLineFlagType.CLIENT_LINE.getCode());
                    mapMessage.put("topMenu", ClientLineFlagType.TOP_MENU.getCode());
                    mapMessage.put("existError", "client.flag.platform.exist");
                    return new ModelAndView("/clientline/flag/modifyflag", mapMessage);
                }
                updateExpress.set(ClientLineFlagField.LINE_ID, lineId);
                updateExpress.set(ClientLineFlagField.LINE_CODE, appId);
            }

            updateExpress.set(ClientLineFlagField.MAX_ITEM_ID, maxItemId);

            updateExpress.set(ClientLineFlagField.MODIFY_DATE, new Date());
            updateExpress.set(ClientLineFlagField.MODIFY_IP, getIp());
            updateExpress.set(ClientLineFlagField.MODIFY_USERID, getCurrentUser().getUserid());

            Boolean bool = JoymeAppServiceSngl.get().modifyClientLineFlag(updateExpress, flagId);
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_CLIENT_LINE_FLAG);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("clientLineFlagId:" + flagId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/clientline/flag/list?flagtype=" + flagType);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView deleteLine(@RequestParam(value = "flagid", required = false) Long flagId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        ClientLineFlag flag = null;
        try {
            flag = JoymeAppServiceSngl.get().getClientLineFlag(new QueryExpress().add(QueryCriterions.eq(ClientLineFlagField.FLAG_ID, flagId)));
            if (flag == null) {
                return new ModelAndView("redirect:/clientline/flag/list");
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineFlagField.VALID_STATUS, ValidStatus.REMOVED.getCode());
        updateExpress.set(ClientLineFlagField.MODIFY_DATE, new Date());
        updateExpress.set(ClientLineFlagField.MODIFY_IP, getIp());
        updateExpress.set(ClientLineFlagField.MODIFY_USERID, getCurrentUser().getUserid());

        try {
            Boolean bool = JoymeAppServiceSngl.get().modifyClientLineFlag(updateExpress, flagId);
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.DELETE_CLIENT_LINE_FLAG);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("clientLineFlagId:" + flagId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/clientline/flag/list?flagtype=" + flag.getClientLineFlagType().getCode());
    }

    @RequestMapping(value = "/recover")
    public ModelAndView modifyLine(@RequestParam(value = "flagid", required = false) Long flagId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        ClientLineFlag flag = null;
        try {
            flag = JoymeAppServiceSngl.get().getClientLineFlag(new QueryExpress().add(QueryCriterions.eq(ClientLineFlagField.FLAG_ID, flagId)));
            if (flag == null) {
                return new ModelAndView("redirect:/clientline/flag/list");
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineFlagField.VALID_STATUS, ValidStatus.VALID.getCode());
        updateExpress.set(ClientLineFlagField.MODIFY_DATE, new Date());
        updateExpress.set(ClientLineFlagField.MODIFY_IP, getIp());
        updateExpress.set(ClientLineFlagField.MODIFY_USERID, getCurrentUser().getUserid());

        try {

            Boolean bool = JoymeAppServiceSngl.get().modifyClientLineFlag(updateExpress, flagId);
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.RECOVER_CLIENT_LINE_FLAG);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("clientLineFlagId:" + flagId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/clientline/flag/list?flagtype=" + flag.getClientLineFlagType().getCode());
    }


}
