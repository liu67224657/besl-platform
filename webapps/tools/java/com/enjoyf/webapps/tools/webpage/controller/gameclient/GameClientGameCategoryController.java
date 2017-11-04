package com.enjoyf.webapps.tools.webpage.controller.gameclient;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBCoverFieldJson;
import com.enjoyf.platform.service.joymeapp.*;
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
import com.enjoyf.webapps.tools.weblogic.dto.joymeApp.GameDBHotPageDTO;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.mongodb.BasicDBObject;
import net.sf.json.JSONObject;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tonydiao on 2015/03/25.
 * 父类别 public static final ClientItemType  HOT_PAGE_FIRST_CATEGORY = new ClientItemType(12);
 * 子类别 public static final ClientItemType HOT_PAGE_SECOND_CATEGORY = new ClientItemType(13);
 * 父类别 在client_line表中的code  gc_hotgc_1st_1_0     1st后从1开始自增   platform 字段和code的最后1位 用来区别ios或android
 * 子类别 在client_line表中的code  gc_hotgc_2nd_1_1_0   2nd后第一个数字代表父类的创建顺序，第二个数字代表子类别在父类别中的创建顺序 ，最后一位数字和platform字段代表ios或android
 * line_intro 字段存储的是父类别的code
 * 根据 ClientLineType 是gameclient ,ClientItemType 是HOT_PAGE_FIRST_CATEGORY 来查找一级分类 如角色精选，分类精选 等
 * 根据  ClientLineType 是gameclient ,ClientItemType 是HOT_PAGE_SECOND_CATEGORY 并且 line_intro 字段等于父类别的code 来查找二级 分类
 * lineName 字段 用于前端显示 可修改
 * code字段不可修改
 * lineid 字段 用于和 clientlineitem 表关联
 */

//热门页游戏分类的controller
@Controller
@RequestMapping(value = "/gameclient/clientline/gamecategory")
public class GameClientGameCategoryController extends ToolsBaseController {


    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;


    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "100") int pageSize) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.HOT_PAGE_FIRST_CATEGORY.getCode()));
            queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GAMECLIENT.getCode()));
            queryExpress.add(QuerySort.add(ClientLineField.DISPLAY_ORDER, QuerySortOrder.ASC));

            PageRows<ClientLine> pageRows = JoymeAppServiceSngl.get().queryClientLineByPage(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/gamecategory/linelist", mapMessage);
        }
        return new ModelAndView("/gameclient/gamecategory/linelist", mapMessage);
    }


    @RequestMapping(value = "/createpage")
    public ModelAndView createpage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        return new ModelAndView("/gameclient/gamecategory/linecreate", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "lineName", required = true) String lineName,
                               @RequestParam(value = "platform", required = true) String platform, //platform 等于2代表android和ios双平台都添加
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineName", lineName);
        try {

            ClientLine clientLineIos = null;
            ClientLine clientLineAndroid = null;
            if (!StringUtil.isEmpty(platform) && (platform.equals("0") || platform.equals("2"))) {

                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.HOT_PAGE_FIRST_CATEGORY.getCode()));
                queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GAMECLIENT.getCode()));
                queryExpress.add(QueryCriterions.eq(ClientLineField.PLATFORM, 0));
                List<ClientLine> list = JoymeAppServiceSngl.get().queryClientLineList(queryExpress);
                String temp;
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        temp = list.get(i).getLineName();
                        if (!StringUtil.isEmpty(temp) && temp.equals(lineName)) {
                            mapMessage.put("errorMsg", "ios平台lineName重复,请重新设定");
                            mapMessage.put("platform", platform);
                            return new ModelAndView("/gameclient/gamecategory/linecreate", mapMessage);
                        }
                    }
                }

                ClientLine clientLine = new ClientLine();
                clientLine.setPlatform(0);
                String code = "gc_hotgc_1st";
                if (list == null || list.size() == 0) {
                    code += "_1";
                } else {
                    code += "_" + (list.size() + 1);
                }

                code += "_" + 0;

                clientLine.setCode(code);
                clientLine.setLine_intro(code);
                clientLine.setLineName(lineName);
                clientLine.setCreateUserid(this.getCurrentUser().getUserid());
                clientLine.setCreateDate(new Date());
                clientLine.setValidStatus(ValidStatus.REMOVED); //新建的应该是未启用状态 0009505
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Long result = new Date().getTime() - sdf.parse("2010-01-01 00:00:00").getTime();
                result = result / 1000;
                int displayOrder = Integer.MAX_VALUE - result.intValue();     //0到1989158807 之间的一个数
                clientLine.setDisplay_order(displayOrder);
                clientLine.setItemType(ClientItemType.HOT_PAGE_FIRST_CATEGORY);
                clientLine.setLineType(ClientLineType.GAMECLIENT);
                clientLineIos = clientLine;

            }

            if (!StringUtil.isEmpty(platform) && (platform.equals("1") || platform.equals("2"))) {

                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.HOT_PAGE_FIRST_CATEGORY.getCode()));
                queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GAMECLIENT.getCode()));
                queryExpress.add(QueryCriterions.eq(ClientLineField.PLATFORM, 1));
                List<ClientLine> list = JoymeAppServiceSngl.get().queryClientLineList(queryExpress);
                String temp;
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        temp = list.get(i).getLineName();
                        if (!StringUtil.isEmpty(temp) && temp.equals(lineName)) {
                            mapMessage.put("errorMsg", "android平台lineName重复,请重新设定");
                            mapMessage.put("platform", platform);
                            return new ModelAndView("/gameclient/gamecategory/linecreate", mapMessage);
                        }
                    }
                }

                ClientLine clientLine = new ClientLine();
                clientLine.setPlatform(1);
                String code = "gc_hotgc_1st";
                if (list == null || list.size() == 0) {
                    code += "_1";
                } else {
                    code += "_" + (list.size() + 1);
                }

                code += "_" + 1;

                clientLine.setCode(code);
                clientLine.setLine_intro(code);
                clientLine.setLineName(lineName);
                clientLine.setCreateUserid(this.getCurrentUser().getUserid());
                clientLine.setCreateDate(new Date());
                clientLine.setValidStatus(ValidStatus.REMOVED); //新建的应该是未启用状态 0009505
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Long result = new Date().getTime() - sdf.parse("2010-01-01 00:00:00").getTime();
                result = result / 1000;
                int displayOrder = Integer.MAX_VALUE - result.intValue();     //0到1989158807 之间的一个数
                clientLine.setDisplay_order(displayOrder);
                clientLine.setItemType(ClientItemType.HOT_PAGE_FIRST_CATEGORY);
                clientLine.setLineType(ClientLineType.GAMECLIENT);
                clientLineAndroid = clientLine;
            }

            //如果platform =2,那么应该是要么都添加，要么都不添加

            if (!StringUtil.isEmpty(platform) && (platform.equals("0") || platform.equals("2"))) {
                JoymeAppServiceSngl.get().createClientLine(clientLineIos);

            }
            if (!StringUtil.isEmpty(platform) && (platform.equals("1") || platform.equals("2"))) {
                JoymeAppServiceSngl.get().createClientLine(clientLineAndroid);

            }

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.GAMECLIENT_GAME_CATEGORY_FIRST_ADD);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP

            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1900) {
                queryString = queryString.substring(0, 1900);
            }

            log.setOpAfter("玩霸热门页游戏分类父类别添加方法-->queryString:" + queryString); //描述 推荐用中文
            addLog(log);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/gamecategory/linecreate", mapMessage);
        }

        return new ModelAndView("redirect:/gameclient/clientline/gamecategory/list", mapMessage);
    }


    @RequestMapping(value = "/modifypage")
    public ModelAndView modifypage(@RequestParam(value = "lineId", required = false) Long lineId,
                                   @RequestParam(value = "lineName", required = true) String lineName,
                                   @RequestParam(value = "platform", required = true) String platform,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineName", lineName);
        mapMessage.put("platform", platform);
        return new ModelAndView("/gameclient/gamecategory/linemodify", mapMessage);

    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "lineId", required = false) Long lineId,
                               @RequestParam(value = "lineName", required = false) String lineName,
                               @RequestParam(value = "platform", required = true) String platform,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineName", lineName);
        mapMessage.put("platform", platform);
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.HOT_PAGE_FIRST_CATEGORY.getCode()));
            queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GAMECLIENT.getCode()));
            queryExpress.add(QueryCriterions.eq(ClientLineField.PLATFORM, Integer.valueOf(platform)));
            queryExpress.add(QueryCriterions.ne(ClientLineField.LINE_ID, lineId));
            List<ClientLine> list = JoymeAppServiceSngl.get().queryClientLineList(queryExpress);
            String temp;
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    temp = list.get(i).getLineName();
                    if (!StringUtil.isEmpty(temp) && temp.equals(lineName)) {
                        mapMessage.put("errorMsg", "lineName重复,请重新设定");
                        return new ModelAndView("/gameclient/gamecategory/linemodify", mapMessage);
                    }
                }
            }

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ClientLineField.LINE_NAME, lineName);
            JoymeAppServiceSngl.get().modifyClientLine(updateExpress, new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            JoymeAppServiceSngl.get().removeClientLineCustomCache("gamecategory", platform);  //移除热门页 游戏分类的缓存


            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.GAMECLIENT_GAME_CATEGORY_FIRST_MODIFY);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP

            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1900) {
                queryString = queryString.substring(0, 1900);
            }

            log.setOpAfter("玩霸热门页游戏分类父类别modify方法-->queryString:" + queryString); //描述 推荐用中文
            addLog(log);


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/gameclient/clientline/gamecategory/list", mapMessage);
    }


    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "lineId", required = false) Long lineId,
                               @RequestParam(value = "type", required = false) String type,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);

        try {
            UpdateExpress updateExpress = new UpdateExpress();
            if (!StringUtil.isEmpty(type) && type.equals("1")) {
                updateExpress.set(ClientLineField.VALID_STATUS, ValidStatus.REMOVED.getCode());
            } else {
                updateExpress.set(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode());
            }
            JoymeAppServiceSngl.get().modifyClientLine(updateExpress, new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));

            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            String platform = clientLine.getCode().substring(clientLine.getCode().length() - 1);  //最后一位是0或1代表平台
            JoymeAppServiceSngl.get().removeClientLineCustomCache("gamecategory", platform);

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.GAMECLIENT_GAME_CATEGORY_FIRST_ENABLED_OR_DISABLED);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP

            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1900) {
                queryString = queryString.substring(0, 1900);
            }

            log.setOpAfter("玩霸热门页游戏分类父类别启用或禁用方法-->queryString:" + queryString); //描述 推荐用中文
            addLog(log);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/gameclient/clientline/gamecategory/list", mapMessage);
    }


    @RequestMapping(value = "/sublist")
    public ModelAndView sublist(@RequestParam(value = "lineCode", required = true) String lineCode, //父类别的lineCode
                                @RequestParam(value = "lineName", required = true) String lineName,  //父类别的lineName
                                @RequestParam(value = "platform", required = true) String platform,  //父类别所属平台
                                @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                @RequestParam(value = "maxPageItems", required = false, defaultValue = "100") int pageSize) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineCode", lineCode);
        mapMessage.put("lineName", lineName);
        mapMessage.put("platform", platform);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.HOT_PAGE_SECOND_CATEGORY.getCode()));
            queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GAMECLIENT.getCode()));
            queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_INTRO, lineCode));

            queryExpress.add(QuerySort.add(ClientLineField.DISPLAY_ORDER, QuerySortOrder.ASC));

            PageRows<ClientLine> pageRows = JoymeAppServiceSngl.get().queryClientLineByPage(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/gamecategory/linelist", mapMessage);
        }
        return new ModelAndView("/gameclient/gamecategory/sublinelist", mapMessage);
    }


    @RequestMapping(value = "/subcreatepage")
    public ModelAndView createpage(@RequestParam(value = "lineCode", required = true) String lineCode,  //父类别的lineCode
                                   @RequestParam(value = "lineName", required = true) String lineName,    //父类别的lineName
                                   @RequestParam(value = "platform", required = true) String platform) {      //父类别所属平台
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineCode", lineCode);
        mapMessage.put("lineName", lineName);
        mapMessage.put("platform", platform);
        return new ModelAndView("/gameclient/gamecategory/sublinecreate", mapMessage);
    }

    @RequestMapping(value = "/subcreate")
    public ModelAndView subcreate(@RequestParam(value = "lineCode", required = true) String lineCode, //父类别的lineCode
                                  @RequestParam(value = "lineName", required = true) String lineName,   //父类别的lineName
                                  @RequestParam(value = "subLineName", required = true) String subLineName,
                                  @RequestParam(value = "platform", required = true) String platform, HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("lineCode", lineCode);
        mapMessage.put("lineName", lineName);
        mapMessage.put("platform", platform);

        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.HOT_PAGE_SECOND_CATEGORY.getCode()));
            queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GAMECLIENT.getCode()));
            queryExpress.add(QueryCriterions.eq(ClientLineField.PLATFORM, Integer.valueOf(platform)));
            queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_INTRO, lineCode));

            List<ClientLine> list = JoymeAppServiceSngl.get().queryClientLineList(queryExpress);
            String temp;
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    temp = list.get(i).getLineName();
                    if (!StringUtil.isEmpty(temp) && temp.equals(subLineName)) {
                        mapMessage.put("subLineName", subLineName);
                        mapMessage.put("errorMsg", "lineName重复,请重新设定");
                        return new ModelAndView("/gameclient/gamecategory/sublinecreate", mapMessage);
                    }
                }
            }

            ClientLine clientLine = new ClientLine();
            clientLine.setPlatform(Integer.valueOf(platform));
            String code = "gc_hotgc_2nd";
            code += lineCode.substring(12, lineCode.lastIndexOf('_'));

            if (list == null || list.size() == 0) {
                code += "_1";
            } else {
                code += "_" + (list.size() + 1);
            }

            code += "_" + platform;

            clientLine.setCode(code);
            clientLine.setLine_intro(lineCode);
            clientLine.setLineName(subLineName);
            clientLine.setCreateUserid(this.getCurrentUser().getUserid());
            clientLine.setCreateDate(new Date());
            clientLine.setValidStatus(ValidStatus.REMOVED);  //0009505 新建的应该是未启用状态
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long result = new Date().getTime() - sdf.parse("2010-01-01 00:00:00").getTime();
            result = result / 1000;
            int displayOrder = Integer.MAX_VALUE - result.intValue();     //0到1989158807 之间的一个数

            clientLine.setDisplay_order(displayOrder);
            clientLine.setItemType(ClientItemType.HOT_PAGE_SECOND_CATEGORY);
            clientLine.setLineType(ClientLineType.GAMECLIENT);

            JoymeAppServiceSngl.get().createClientLine(clientLine);


            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.GAMECLIENT_GAME_CATEGORY_SECOND_ADD);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP

            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1900) {
                queryString = queryString.substring(0, 1900);
            }

            log.setOpAfter("玩霸热门页游戏分类子类别添加方法-->queryString:" + queryString); //描述 推荐用中文
            addLog(log);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/gamecategory/sublinecreate", mapMessage);
        }

        return new ModelAndView("redirect:/gameclient/clientline/gamecategory/sublist", mapMessage);
    }


    @RequestMapping(value = "/submodifypage")
    public ModelAndView submodifypage(@RequestParam(value = "subLineId", required = false) Long subLineId,
                                      @RequestParam(value = "subLineName", required = true) String subLineName,
                                      @RequestParam(value = "subLineCode", required = true) String subLineCode,
                                      @RequestParam(value = "lineCode", required = true) String lineCode, //父类别的lineCode
                                      @RequestParam(value = "lineName", required = true) String lineName,   //父类别的lineName
                                      @RequestParam(value = "platform", required = true) String platform,
                                      HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("subLineId", subLineId);
        mapMessage.put("subLineName", subLineName);
        mapMessage.put("subLineCode", subLineCode);
        mapMessage.put("lineName", lineName);
        mapMessage.put("lineCode", lineCode);
        mapMessage.put("platform", platform);
        return new ModelAndView("/gameclient/gamecategory/sublinemodify", mapMessage);

    }

    @RequestMapping(value = "/submodify")
    public ModelAndView submodify(@RequestParam(value = "subLineId", required = false) Long subLineId,
                                  @RequestParam(value = "subLineName", required = true) String subLineName,
                                  @RequestParam(value = "subLineCode", required = true) String subLineCode,
                                  @RequestParam(value = "lineCode", required = true) String lineCode, //父类别的lineCode
                                  @RequestParam(value = "lineName", required = true) String lineName,   //父类别的lineName
                                  @RequestParam(value = "platform", required = true) String platform,
                                  HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineCode", lineCode);
        mapMessage.put("lineName", lineName);
        mapMessage.put("platform", platform);
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.HOT_PAGE_SECOND_CATEGORY.getCode()));
            queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GAMECLIENT.getCode()));
            queryExpress.add(QueryCriterions.eq(ClientLineField.PLATFORM, Integer.valueOf(platform)));
            queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_INTRO, lineCode));
            queryExpress.add(QueryCriterions.ne(ClientLineField.LINE_ID, subLineId));
            List<ClientLine> list = JoymeAppServiceSngl.get().queryClientLineList(queryExpress);
            String temp;
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    temp = list.get(i).getLineName();
                    if (!StringUtil.isEmpty(temp) && temp.equals(subLineName)) {
                        mapMessage.put("subLineName", subLineName);
                        mapMessage.put("subLineId", subLineId);
                        mapMessage.put("subLineCode", subLineCode);
                        mapMessage.put("errorMsg", "lineName重复,请重新设定");
                        return new ModelAndView("/gameclient/gamecategory/sublinemodify", mapMessage);
                    }
                }
            }

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ClientLineField.LINE_NAME, subLineName);
            JoymeAppServiceSngl.get().modifyClientLine(updateExpress, new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, subLineId)));
            //以下是清除缓存
            JoymeAppServiceSngl.get().removeClientLineCustomCache("gamecategory", platform);
            JoymeAppServiceSngl.get().removeClientLineFromCache(subLineCode);
            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.GAMECLIENT_GAME_CATEGORY_SECOND_MODIFY);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP

            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1900) {
                queryString = queryString.substring(0, 1900);
            }

            log.setOpAfter("玩霸热门页游戏分类子类别modify方法-->queryString:" + queryString); //描述 推荐用中文
            addLog(log);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/gameclient/clientline/gamecategory/sublist", mapMessage);
    }


    @RequestMapping(value = "/subdelete")
    public ModelAndView subdelete(@RequestParam(value = "subLineId", required = false) Long subLineId,
                                  @RequestParam(value = "type", required = false) String type,
                                  @RequestParam(value = "lineCode", required = true) String lineCode, //父类别的lineCode
                                  @RequestParam(value = "lineName", required = true) String lineName,   //父类别的lineName
                                  @RequestParam(value = "platform", required = true) String platform,
                                  HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineCode", lineCode);
        mapMessage.put("lineName", lineName);
        mapMessage.put("platform", platform);
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            if (!StringUtil.isEmpty(type) && type.equals("1")) {
                updateExpress.set(ClientLineField.VALID_STATUS, ValidStatus.REMOVED.getCode());
            } else {
                updateExpress.set(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode());
            }
            JoymeAppServiceSngl.get().modifyClientLine(updateExpress, new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, subLineId)));
            JoymeAppServiceSngl.get().removeClientLineCustomCache("gamecategory", platform);

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.GAMECLIENT_GAME_CATEGORY_SECOND_ENABLED_OR_DISABLED);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP

            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1900) {
                queryString = queryString.substring(0, 1900);
            }

            log.setOpAfter("玩霸热门页游戏分类子类别启用或禁用方法-->queryString:" + queryString); //描述 推荐用中文
            addLog(log);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/gameclient/clientline/gamecategory/sublist", mapMessage);
    }


    @RequestMapping(value = "/itemlist")
    public ModelAndView itemlist(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                 @RequestParam(value = "maxPageItems", required = false, defaultValue = "100") int pageSize,
                                 @RequestParam(value = "subLineId", required = false) Long subLineId,
                                 @RequestParam(value = "subLineName", required = false) String subLineName,
                                 @RequestParam(value = "subLineCode", required = false) String subLineCode,
                                 @RequestParam(value = "lineCode", required = true) String lineCode,  //父类别的lineCode
                                 @RequestParam(value = "lineName", required = true) String lineName,  //父类别的lineName
                                 @RequestParam(value = "platform", required = true) String platform) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("subLineId", subLineId);
        mapMessage.put("subLineName", subLineName);
        mapMessage.put("subLineCode", subLineCode);

        mapMessage.put("lineCode", lineCode);
        mapMessage.put("lineName", lineName);
        mapMessage.put("platform", platform);

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);


        try {
            Map<Integer, String> types = new HashMap<Integer, String>();
            Class appRedirectType = AppRedirectType.class;
            Field[] fields = appRedirectType.getFields();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getType().equals(appRedirectType)) {
                    int code = ((AppRedirectType) fields[i].get(null)).getCode();
                    types.put(code, fields[i].getName());
                }
            }
            mapMessage.put("types", types);


            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, subLineId));
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
            // queryExpress.add(QuerySort.add(ClientLineItemField.ITEM_CREATE_DATE, QuerySortOrder.DESC));

            queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));

            PageRows<ClientLineItem> itemPageRows = JoymeAppServiceSngl.get().queryClientLineItemByPage(queryExpress, pagination);
            if (itemPageRows != null && !CollectionUtil.isEmpty(itemPageRows.getRows())) {
                List<ClientLineItem> clientLineItemList = itemPageRows.getRows();

                Set<Long> directIdSet = new HashSet<Long>();
                Map<Long, ClientLineItem> clientLineItemMap = new HashMap<Long, ClientLineItem>();

                for (int i = 0; i < clientLineItemList.size(); i++) {
                    Long longTemp = Long.valueOf(clientLineItemList.get(i).getDirectId());
                    directIdSet.add(longTemp);
                    clientLineItemMap.put(longTemp, clientLineItemList.get(i));
                }

                //根据set查询mongodb的game库 的game_db表
                Map<Long, GameDB> gameDBMap = GameResourceServiceSngl.get().queryGameDBSet(directIdSet);

                //添充GameDBSmallDTO 形成的List
                List<GameDBHotPageDTO> smallList = new ArrayList<GameDBHotPageDTO>();

                for (int i = 0; i < clientLineItemList.size(); i++) {
                    Long longTemp = Long.valueOf(clientLineItemList.get(i).getDirectId());
                    GameDB gameDB = gameDBMap.get(longTemp);
                    if (gameDB != null) {
                        GameDBHotPageDTO temp = getDTOFromJson(clientLineItemList.get(i).getDesc());
                        temp.setGameDbId(gameDB.getGameDbId());
                        temp.setLikeNum(gameDB.getGameDBCover().getCoverAgreeNum());
                        temp.setIcon(gameDB.getGameIcon());
                        temp.setGameRate(average(gameDB));
                        temp.setItemCreateDate(clientLineItemMap.get(longTemp).getItemCreateDate());
                        temp.setItemId(clientLineItemMap.get(longTemp).getItemId());
                        temp.setDisplayOrder(clientLineItemMap.get(longTemp).getDisplayOrder());

                        smallList.add(temp);
                    }
                }

                //按 gamePublicTime字段由大到小排序---逆自然顺序
                //如果是新游开测按日期降序排列，热门和正在玩按display_order排序
                //                if (!StringUtil.isEmpty(lineCode) && lineCode.contains("gc_newgame")) {
                //                    Collections.sort(smallList);
                //                }

                mapMessage.put("list", smallList);
                mapMessage.put("page", itemPageRows.getPage());
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/gameclient/gamecategory/itemlist", mapMessage);
    }


    //clientlineitem 表中的desc字段存储的是json格式的数据，从json字段串中获得java对象
    private static GameDBHotPageDTO getDTOFromJson(String jsonString) {
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        Map<String, String> map = new HashMap<String, String>();
        for (Iterator it = jsonObject.keys(); it.hasNext(); ) {
            String key = (String) it.next();
            map.put(key, (String) jsonObject.get(key));
        }
        GameDBHotPageDTO temp = new GameDBHotPageDTO();
        temp.setShowType(map.get("showType"));
        temp.setGameName(map.get("gameName"));
        temp.setGameTypeDesc(map.get("gameTypeDesc"));
        temp.setJt(map.get("jt"));
        temp.setJi(map.get("ji"));
        temp.setDownloadRecommend(map.get("downloadRecommend"));
        temp.setTag(map.get("tag"));
        temp.setJumpTarget(map.get("jumpTarget"));

        return temp;
    }

    //根据游戏资料库的5个评分，求算术平均值
    public static String average(GameDB gameDB) {
        String returnObj = "";
        GameDBCoverFieldJson fieldJson = gameDB.getGameDBCoverFieldJson();
        if (fieldJson != null) {
            double sum = Double.valueOf(fieldJson.getValue1()) + Double.valueOf(fieldJson.getValue2()) + Double.valueOf(fieldJson.getValue3()) + Double.valueOf(fieldJson.getValue4()) + Double.valueOf(fieldJson.getValue5());
            DecimalFormat df = new DecimalFormat("0.0");
            returnObj = df.format(sum / 5.0);
        }
        return returnObj;
    }


    @RequestMapping(value = "/itemcreatepage")
    public ModelAndView itemcreatepage(@RequestParam(value = "subLineId", required = false) Long subLineId,
                                       @RequestParam(value = "subLineName", required = false) String subLineName,
                                       @RequestParam(value = "subLineCode", required = false) String subLineCode,
                                       @RequestParam(value = "lineCode", required = true) String lineCode,  //父类别的lineCode
                                       @RequestParam(value = "lineName", required = true) String lineName,  //父类别的lineName
                                       @RequestParam(value = "platform", required = true) String platform) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("subLineId", subLineId);
        mapMessage.put("subLineName", subLineName);
        mapMessage.put("subLineCode", subLineCode);

        mapMessage.put("lineCode", lineCode);
        mapMessage.put("lineName", lineName);
        mapMessage.put("platform", platform);
        try {
            Map<Integer, String> types = new HashMap<Integer, String>();
            Class appRedirectType = AppRedirectType.class;
            Field[] fields = appRedirectType.getFields();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getType().equals(appRedirectType)) {
                    int code = ((AppRedirectType) fields[i].get(null)).getCode();
                    types.put(code, fields[i].getName());
                }
            }
            mapMessage.put("types", types);
        } catch (IllegalAccessException e) {
            GAlerter.lab(this.getClass().getName() + " occured IllegalAccessException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/gameclient/gamecategory/itemcreate", mapMessage);
    }


    @RequestMapping(value = "/itemcreate")
    public ModelAndView itemcreate(@RequestParam(value = "subLineId", required = false) Long subLineId,
                                   @RequestParam(value = "subLineName", required = false) String subLineName,
                                   @RequestParam(value = "subLineCode", required = false) String subLineCode,
                                   @RequestParam(value = "lineCode", required = true) String lineCode,  //父类别的lineCode
                                   @RequestParam(value = "lineName", required = true) String lineName,  //父类别的lineName
                                   @RequestParam(value = "platform", required = true) String platform,
                                   @RequestParam(value = "gameDbId", required = false) String gameDbId,
                                   //      @RequestParam(value = "itemCreateDate", required = false) String itemCreateDate,
                                   @RequestParam(value = "showType", required = false) String showType,
                                   @RequestParam(value = "gameName", required = false) String gameName,
                                   @RequestParam(value = "gameTypeDesc", required = false) String gameTypeDesc,
                                   @RequestParam(value = "jumpTarget", required = false) String jumpTarget,
                                   @RequestParam(value = "jt", required = false) String jt,
                                   @RequestParam(value = "ji", required = false) String ji,
                                   @RequestParam(value = "downloadRecommend", required = false) String downloadRecommend,
                                   @RequestParam(value = "tag", required = false) String tag,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("subLineId", subLineId);
        mapMessage.put("subLineName", subLineName);
        mapMessage.put("subLineCode", subLineCode);

        mapMessage.put("lineCode", lineCode);
        mapMessage.put("lineName", lineName);
        mapMessage.put("platform", platform);


        try {
            QueryExpress queryExpressIfExist = new QueryExpress();
            queryExpressIfExist.add(QueryCriterions.eq(ClientLineItemField.DIRECT_ID, gameDbId));
            queryExpressIfExist.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, subLineId));
            ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(queryExpressIfExist);
            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", Long.valueOf(gameDbId)), false);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (clientLineItem != null && clientLineItem.getValidStatus().equals(ValidStatus.REMOVED)) {
                //update  更新
                UpdateExpress updateExpress = new UpdateExpress();


                //   updateExpress.set(ClientLineItemField.ITEM_CREATE_DATE, itemCreateDateDate);


                JSONObject jsonObject = new JSONObject();
                jsonObject.put("gameName", gameName);
                jsonObject.put("showType", showType);
                jsonObject.put("gameTypeDesc", gameTypeDesc);
                jsonObject.put("jumpTarget", jumpTarget);

                if (jumpTarget.equals("cover")) {
                    jsonObject.put("jt", "23");
                    jsonObject.put("ji", "http://api." + WebappConfig.get().getDomain() + "/joymeapp/gameclient/webview/game/cover?gameid=" + gameDB.getGameDbId());
                } else if (jumpTarget.equals("poster")) {
                    jsonObject.put("jt", "23");
                    jsonObject.put("ji", "http://api." + WebappConfig.get().getDomain() + "/joymeapp/gameclient/webview/game/poster?gameid=" + gameDB.getGameDbId());
                } else if (jumpTarget.equals("detail")) {
                    jsonObject.put("jt", "24");
                    jsonObject.put("ji", gameDbId);
                } else {
                    jsonObject.put("jt", jt);
                    jsonObject.put("ji", ji);
                }

                jsonObject.put("downloadRecommend", downloadRecommend);
                jsonObject.put("tag", tag);


                updateExpress.set(ClientLineItemField.DESC, jsonObject.toString());
                updateExpress.set(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode());
                JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, clientLineItem.getItemId());


                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());//用户的ID
                log.setOperType(LogOperType.GAMECLIENT_GAME_CATEGORY_ITEM_MODIFY);  //操作的类型
                log.setOpTime(new Date());//操作时间
                log.setOpIp(getIp());//用户IP

                Map<String, String[]> params = request.getParameterMap();
                String queryString = "  ";
                for (String key : params.keySet()) {
                    String[] values = params.get(key);
                    for (int i = 0; i < values.length; i++) {
                        queryString += key + "=" + values[i] + "&";
                    }
                }
                // 去掉最后一个空格
                queryString = queryString.substring(0, queryString.length() - 1);

                if (queryString.length() > 1900) {
                    queryString = queryString.substring(0, 1900);
                }

                log.setOpAfter("热门页游戏分类recover已删除游戏-->queryString:" + queryString); //描述 推荐用中文
                addLog(log);
            } else {
                ClientLineItem clientLineItemTemp = new ClientLineItem();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("gameName", gameName);
                jsonObject.put("showType", showType);
                jsonObject.put("gameTypeDesc", gameTypeDesc);

                if (jumpTarget.equals("cover")) {
                    jsonObject.put("jt", "23");
                    jsonObject.put("ji", "http://api." + WebappConfig.get().getDomain() + "/joymeapp/gameclient/webview/game/cover?gameid=" + gameDB.getGameDbId());
                } else if (jumpTarget.equals("poster")) {
                    jsonObject.put("jt", "23");
                    jsonObject.put("ji", "http://api." + WebappConfig.get().getDomain() + "/joymeapp/gameclient/webview/game/poster?gameid=" + gameDB.getGameDbId());
                } else if (jumpTarget.equals("detail")) {
                    jsonObject.put("jt", "24");
                    jsonObject.put("ji", gameDbId);
                } else {
                    jsonObject.put("jt", jt);
                    jsonObject.put("ji", ji);
                }
                jsonObject.put("jumpTarget", jumpTarget);
                jsonObject.put("downloadRecommend", downloadRecommend);
                jsonObject.put("tag", tag);

                clientLineItemTemp.setValidStatus(ValidStatus.VALID);
                clientLineItemTemp.setDesc(jsonObject.toString());
                clientLineItemTemp.setDirectId(gameDbId);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Long result = new Date().getTime() - sdf.parse("2010-01-01 00:00:00").getTime();
                result = result / 1000;
                int displayOrder = Integer.MAX_VALUE - result.intValue();     //0到1989158807 之间的一个数

                clientLineItemTemp.setDisplayOrder(displayOrder);
                clientLineItemTemp.setItemCreateDate(new Date());
                clientLineItemTemp.setLineId(subLineId);
                clientLineItemTemp.setItemType(ClientItemType.HOT_PAGE_SECOND_CATEGORY);
                clientLineItemTemp.setItemDomain(ClientItemDomain.GAME);

                JoymeAppServiceSngl.get().createClientLineItem(clientLineItemTemp);
                JoymeAppServiceSngl.get().removeClientLineCustomCache("gamecategory", platform);

                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());//用户的ID
                log.setOperType(LogOperType.GAMECLIENT_GAME_CATEGORY_ITEM_ADD);  //操作的类型
                log.setOpTime(new Date());//操作时间
                log.setOpIp(getIp());//用户IP

                Map<String, String[]> params = request.getParameterMap();
                String queryString = "  ";
                for (String key : params.keySet()) {
                    String[] values = params.get(key);
                    for (int i = 0; i < values.length; i++) {
                        queryString += key + "=" + values[i] + "&";
                    }
                }
                // 去掉最后一个空格
                queryString = queryString.substring(0, queryString.length() - 1);

                if (queryString.length() > 1900) {
                    queryString = queryString.substring(0, 1900);
                }

                log.setOpAfter("热门页游戏分类添加游戏-->queryString:" + queryString); //描述 推荐用中文
                addLog(log);

            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/gameclient/clientline/gamecategory/itemlist", mapMessage);
    }


    @RequestMapping(value = "/itemmodifypage")
    public ModelAndView itemmodifypage(@RequestParam(value = "itemId", required = true) Long itemId,
                                       @RequestParam(value = "subLineId", required = false) Long subLineId,     //子类别的lineId
                                       @RequestParam(value = "subLineName", required = false) String subLineName,//子类别的lineName
                                       @RequestParam(value = "subLineCode", required = false) String subLineCode,  //子类别的lineCode
                                       @RequestParam(value = "lineCode", required = true) String lineCode,  //父类别的lineCode
                                       @RequestParam(value = "lineName", required = true) String lineName,  //父类别的lineName
                                       @RequestParam(value = "platform", required = true) String platform) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("itemId", itemId);
        mapMessage.put("subLineId", subLineId);
        mapMessage.put("subLineName", subLineName);
        mapMessage.put("subLineCode", subLineCode);

        mapMessage.put("lineCode", lineCode);
        mapMessage.put("lineName", lineName);
        mapMessage.put("platform", platform);

        try {
            Map<Integer, String> types = new HashMap<Integer, String>();
            Class appRedirectType = AppRedirectType.class;
            Field[] fields = appRedirectType.getFields();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getType().equals(appRedirectType)) {
                    int code = ((AppRedirectType) fields[i].get(null)).getCode();
                    types.put(code, fields[i].getName());
                }
            }
            mapMessage.put("types", types);

            ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, itemId)));
            mapMessage.put("item", clientLineItem);

            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", Long.valueOf(clientLineItem.getDirectId())), false);
            mapMessage.put("game", gameDB);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (IllegalAccessException e) {
            GAlerter.lab(this.getClass().getName() + " occured IllegalAccessException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/gameclient/gamecategory/itemmodify", mapMessage);
    }

    @RequestMapping(value = "/itemmodify")
    public ModelAndView itemmodify(@RequestParam(value = "itemId", required = false) Long itemId,
                                   @RequestParam(value = "gameDbId", required = false) String gameDbId,
                                   @RequestParam(value = "subLineId", required = false) Long subLineId,
                                   @RequestParam(value = "subLineName", required = false) String subLineName,
                                   @RequestParam(value = "subLineCode", required = false) String subLineCode,
                                   @RequestParam(value = "lineCode", required = true) String lineCode,  //父类别的lineCode
                                   @RequestParam(value = "lineName", required = true) String lineName,  //父类别的lineName
                                   @RequestParam(value = "platform", required = true) String platform,
                                   //  @RequestParam(value = "itemCreateDate", required = false) String itemCreateDate,
                                   @RequestParam(value = "showType", required = false) String showType,
                                   @RequestParam(value = "gameName", required = false) String gameName,
                                   @RequestParam(value = "gameTypeDesc", required = false) String gameTypeDesc,
                                   @RequestParam(value = "jumpTarget", required = false) String jumpTarget,
                                   @RequestParam(value = "jt", required = false) String jt,
                                   @RequestParam(value = "ji", required = false) String ji,
                                   @RequestParam(value = "downloadRecommend", required = false) String downloadRecommend,
                                   @RequestParam(value = "tag", required = false) String tag,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("subLineId", subLineId);
        mapMessage.put("subLineName", subLineName);
        mapMessage.put("subLineCode", subLineCode);

        mapMessage.put("lineCode", lineCode);
        mapMessage.put("lineName", lineName);
        mapMessage.put("platform", platform);

        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            UpdateExpress updateExpress = new UpdateExpress();
            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", Long.valueOf(gameDbId)), false);

            //    updateExpress.set(ClientLineItemField.ITEM_CREATE_DATE, itemCreateDateDate);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("gameName", gameName);
            jsonObject.put("showType", showType);
            jsonObject.put("gameTypeDesc", gameTypeDesc);

            if (jumpTarget.equals("cover")) {
                jsonObject.put("jt", "23");
                jsonObject.put("ji", "http://api." + WebappConfig.get().getDomain() + "/joymeapp/gameclient/webview/game/cover?gameid=" + gameDB.getGameDbId());
            } else if (jumpTarget.equals("poster")) {
                jsonObject.put("jt", "23");
                jsonObject.put("ji", "http://api." + WebappConfig.get().getDomain() + "/joymeapp/gameclient/webview/game/poster?gameid=" + gameDB.getGameDbId());
            } else if (jumpTarget.equals("detail")) {
                jsonObject.put("jt", "24");
                jsonObject.put("ji", gameDbId);
            } else {
                jsonObject.put("jt", jt);
                jsonObject.put("ji", ji);
            }
            jsonObject.put("jumpTarget", jumpTarget);
            jsonObject.put("downloadRecommend", downloadRecommend);
            jsonObject.put("tag", tag);

            updateExpress.set(ClientLineItemField.DESC, jsonObject.toString());
            updateExpress.set(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode());
            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);


            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.GAMECLIENT_GAME_CATEGORY_ITEM_MODIFY);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP

            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1900) {
                queryString = queryString.substring(0, 1900);
            }

            log.setOpAfter("热门页游戏分类修改游戏-->queryString:" + queryString); //描述 推荐用中文
            addLog(log);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/gameclient/clientline/gamecategory/itemlist", mapMessage);
    }

    @RequestMapping(value = "/itemdelete")
    public ModelAndView itemdelete(@RequestParam(value = "itemId", required = false) Long itemId,
                                   @RequestParam(value = "subLineId", required = false) Long subLineId,
                                   @RequestParam(value = "subLineName", required = false) String subLineName,
                                   @RequestParam(value = "subLineCode", required = false) String subLineCode,
                                   @RequestParam(value = "lineCode", required = true) String lineCode,  //父类别的lineCode
                                   @RequestParam(value = "lineName", required = true) String lineName,  //父类别的lineName
                                   @RequestParam(value = "platform", required = true) String platform,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("subLineId", subLineId);
        mapMessage.put("subLineName", subLineName);
        mapMessage.put("subLineCode", subLineCode);

        mapMessage.put("lineCode", lineCode);
        mapMessage.put("lineName", lineName);
        mapMessage.put("platform", platform);

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineItemField.VALID_STATUS, ValidStatus.REMOVED.getCode());
        try {
            boolean bool = JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);

            if (bool) {
                JoymeAppServiceSngl.get().removeClientLineCustomCache("gamecategory", platform);

                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());//用户的ID
                log.setOperType(LogOperType.GAMECLIENT_GAME_CATEGORY_ITEM_DELETE);  //操作的类型
                log.setOpTime(new Date());//操作时间
                log.setOpIp(getIp());//用户IP

                Map<String, String[]> params = request.getParameterMap();
                String queryString = "  ";
                for (String key : params.keySet()) {
                    String[] values = params.get(key);
                    for (int i = 0; i < values.length; i++) {
                        queryString += key + "=" + values[i] + "&";
                    }
                }
                // 去掉最后一个空格
                queryString = queryString.substring(0, queryString.length() - 1);

                if (queryString.length() > 1900) {
                    queryString = queryString.substring(0, 1900);
                }

                log.setOpAfter("热门页游戏分类之最终页删除游戏-->queryString:" + queryString); //描述 推荐用中文
                addLog(log);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/gameclient/clientline/gamecategory/itemlist", mapMessage);
    }

    //在本分页内交换任意两行的排序
    @RequestMapping(value = "/sort")
    @ResponseBody
    public String sort(@RequestParam(value = "type", required = false) String type,
                       @RequestParam(value = "fromId", required = false) Long fromId,
                       @RequestParam(value = "fromOrder", required = false) int fromOrder,
                       @RequestParam(value = "toId", required = false) Long toId,
                       @RequestParam(value = "toOrder", required = false) int toOrder) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_E);

        if (StringUtil.isEmpty(type)) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            resultObjectMsg.setMsg("type can't be empty string");
            return binder.toJson(resultObjectMsg);
        }
        try {
            UpdateExpress updateExpressFrom = new UpdateExpress();
            UpdateExpress updateExpressTo = new UpdateExpress();
            if (type.equals("clientline")) {
                updateExpressFrom.set(ClientLineField.DISPLAY_ORDER, toOrder);
                JoymeAppServiceSngl.get().modifyClientLine(updateExpressFrom, new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, fromId)));

                updateExpressTo.set(ClientLineField.DISPLAY_ORDER, fromOrder);
                JoymeAppServiceSngl.get().modifyClientLine(updateExpressTo, new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, toId)));

                ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, fromId)));
                String platform = clientLine.getCode().substring(clientLine.getCode().length() - 1);  //最后一位是0或1代表平台
                JoymeAppServiceSngl.get().removeClientLineCustomCache("gamecategory", platform);

            } else if (type.equals("clientlineitem")) {
                updateExpressFrom.set(ClientLineItemField.DISPLAY_ORDER, toOrder);
                JoymeAppServiceSngl.get().modifyClientLineItem(updateExpressFrom, fromId);

                updateExpressTo.set(ClientLineItemField.DISPLAY_ORDER, fromOrder);
                JoymeAppServiceSngl.get().modifyClientLineItem(updateExpressTo, toId);

                //确定不了平台，都更新缓存吧
                JoymeAppServiceSngl.get().removeClientLineCustomCache("gamecategory", "0");
                JoymeAppServiceSngl.get().removeClientLineCustomCache("gamecategory", "1");
            }
            resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
            resultObjectMsg.setResult(mapMessage);


        } catch (Exception e) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            resultObjectMsg.setMsg(i18nSource.getMessage("system.error", null, null));
            return binder.toJson(resultObjectMsg);
        }
        return binder.toJson(resultObjectMsg);
    }


}
