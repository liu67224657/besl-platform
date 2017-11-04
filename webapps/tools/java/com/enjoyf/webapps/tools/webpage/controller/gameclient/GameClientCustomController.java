package com.enjoyf.webapps.tools.webpage.controller.gameclient;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagAppType;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.URLUtils;
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
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/24
 * Description:
 */
//热门页轮播图，标签页轮播图，热门游戏右侧链接的controller
@Controller
@RequestMapping(value = "/gameclient/clientline/custom")
public class GameClientCustomController extends ToolsBaseController {

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "10") int pageSize) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();


        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        try {


            QueryExpress queryExpress = new QueryExpress();

            queryExpress.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.CUSTOM.getCode()));
            queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GAMECLIENT.getCode()));


            PageRows<ClientLine> pageRows = JoymeAppServiceSngl.get().queryClientLineByPage(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/custom/linelist", mapMessage);
        }
        return new ModelAndView("/gameclient/custom/linelist", mapMessage);
    }

    @RequestMapping(value = "/itemlist")
    public ModelAndView itemlist(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                 @RequestParam(value = "maxPageItems", required = false, defaultValue = "10") int pageSize,
                                 @RequestParam(value = "lineId", required = false) Long lineId,
                                 @RequestParam(value = "lineName", required = false) String lineName,
                                 @RequestParam(value = "lineCode", required = false, defaultValue = "") String lineCode,
                                 @RequestParam(value = "tagIdSearch", required = false) Long tagIdSearch) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineName", lineName);
        mapMessage.put("lineCode", lineCode);
        mapMessage.put("tagIdSearch", tagIdSearch);

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        if (lineId == null) {
            return new ModelAndView("redirect:/gameclient/clientline/custom/list");
        }
        try {

            Map<Long, String> tagMap = new HashMap<Long, String>();
            QueryExpress queryExpressForTag = new QueryExpress();
            queryExpressForTag.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.SYHB.getCode()));
            List<AnimeTag> tagList = JoymeAppServiceSngl.get().queryAnimeTag(queryExpressForTag);
            if (tagList != null) {
                for (int i = 0; i < tagList.size(); i++) {
                    tagMap.put(tagList.get(i).getTag_id(), tagList.get(i).getTag_name());
                }
            }

            mapMessage.put("tagMap", tagMap);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, lineId));
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.ITEM_TYPE, ClientItemType.CUSTOM.getCode()));
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));

            if (!StringUtil.isEmpty(lineCode) && lineCode.contains("gc_topicimgs")) {
                if (tagIdSearch != null && tagIdSearch != -100) {
                    queryExpress.add(QueryCriterions.eq(ClientLineItemField.CONTENTID, tagIdSearch));
                }
            }

            //按日期降序排列
            //   queryExpress.add(QuerySort.add(ClientLineItemField.ITEM_CREATE_DATE, QuerySortOrder.DESC));

            queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));

            //把属于这个clientline 的所有item都分页查出来
            PageRows<ClientLineItem> itemPageRows = JoymeAppServiceSngl.get().queryClientLineItemByPage(queryExpress, pagination);
            if (itemPageRows != null && !CollectionUtil.isEmpty(itemPageRows.getRows())) {
                List<ClientLineItem> list = itemPageRows.getRows();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getPicUrl() != null) {
                        list.get(i).setPicUrl(URLUtils.getJoymeDnUrl(list.get(i).getPicUrl()));
                    }
                    //category 字段用于存放标签页轮播图 的左上角的小图
                    if (list.get(i).getCategory() != null && list.get(i).getCategory().length() > 0 && lineCode.contains("gc_topicimgs")) {
                        list.get(i).setCategory(URLUtils.getJoymeDnUrl(list.get(i).getCategory()));
                    }

                }


                mapMessage.put("list", list);
                mapMessage.put("page", itemPageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/gameclient/custom/itemlist", mapMessage);
    }


    @RequestMapping(value = "/itemcreatepage")
    public ModelAndView itemcreatepage(@RequestParam(value = "lineId", required = false) Long lineId,
                                       @RequestParam(value = "lineCode", required = false) String lineCode,
                                       @RequestParam(value = "lineName", required = false) String lineName) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        Map<Integer, String> types = new HashMap<Integer, String>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineCode", lineCode);
        mapMessage.put("lineName", lineName);
        try {

            Map<Long, String> tagMap = new HashMap<Long, String>();
            QueryExpress queryExpressForTag = new QueryExpress();
            queryExpressForTag.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.SYHB.getCode()));
            List<AnimeTag> tagList = JoymeAppServiceSngl.get().queryAnimeTag(queryExpressForTag);
            if (tagList != null) {
                for (int i = 0; i < tagList.size(); i++) {
                    tagMap.put(tagList.get(i).getTag_id(), tagList.get(i).getTag_name());
                }
            }
            mapMessage.put("tagMap", tagMap);

            Class appRedirectType = AppRedirectType.class;
            Field[] fields = appRedirectType.getFields();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getType().equals(appRedirectType)) {
                    int code = ((AppRedirectType) fields[i].get(null)).getCode();
                    types.put(code, fields[i].getName());
                }

            }

            mapMessage.put("types", types);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (IllegalAccessException e) {
            GAlerter.lab(this.getClass().getName() + " occured IllegalAccessException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");

        }

        return new ModelAndView("/gameclient/custom/createitem", mapMessage);
    }


    @RequestMapping(value = "/itemcreate")
    public ModelAndView itemcreate(@RequestParam(value = "lineId", required = false) Long lineId,
                                   @RequestParam(value = "lineCode", required = false) String lineCode,
                                   @RequestParam(value = "lineName", required = false) String lineName,

                                   @RequestParam(value = "title", required = false) String title,
                                   @RequestParam(value = "lineDesc", required = false) String lineDesc,
                                   @RequestParam(value = "picUrl", required = false) String picUrl,
                                   @RequestParam(value = "category", required = false, defaultValue = "") String category,
                                   @RequestParam(value = "color", required = false, defaultValue = "") String color,
                                   @RequestParam(value = "url", required = false) String url,
                                   @RequestParam(value = "redirectType", required = false) String redirectType,
                                   @RequestParam(value = "gamePublicTime", required = false) String gamePublicTime,
                                   @RequestParam(value = "rate", required = false) String rate,//热门链接里的数值

                                   @RequestParam(value = "tagId", required = false) String tagId,
                                   @RequestParam(value = "platform", required = false) String platform,
                                   @RequestParam(value = "author", required = false) String author,
                                   @RequestParam(value = "isHot", required = false) String isHot,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineName", lineName);
        mapMessage.put("lineCode", lineCode);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long result = new Date().getTime() - sdf.parse("2010-01-01 00:00:00").getTime();
            result = result / 1000;
            int displayOrder = Integer.MAX_VALUE - result.intValue();     //0到1989158807 之间的一个数
            displayOrder= Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000);     //0到718238745 之间的一个值
            if (!StringUtil.isEmpty(lineCode) && lineCode.contains("gc_topicimgs")) {

                String[] tagIdArray = tagId.split(",");
                for (int i = 0; i < tagIdArray.length; i++) {
                    ClientLineItem item = new ClientLineItem();
                    item.setContentid(Long.valueOf(tagIdArray[i]));
                    item.setCategory(category);
                    item.setPicUrl(picUrl);

                    item.setLineId(lineId);
                    item.setTitle(title);
                    item.setDesc(lineDesc);
                    item.setUrl(url);
                    item.setItemType(ClientItemType.CUSTOM);
                    item.setRedirectType(AppRedirectType.getByCode(Integer.valueOf(redirectType)));
                    item.setItemDomain(ClientItemDomain.DEFAULT);

                    if (!StringUtil.isEmpty(gamePublicTime)) {
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        item.setItemCreateDate(format.parse(gamePublicTime));
                    }
                    item.setDisplayOrder(displayOrder - i * 10);
                    item.setValidStatus(ValidStatus.VALID);

                    JoymeAppServiceSngl.get().createClientLineItem(item);

                    //新增的时候也添加到另外一条line里
                    if (!StringUtil.isEmpty(platform) && platform.equals("all") && !StringUtil.isEmpty(lineCode) && lineId != null) {

                        long lineIdNew = 0;
                        QueryExpress queryExpressForPlatform = new QueryExpress();
                        queryExpressForPlatform.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.CUSTOM.getCode()));
                        queryExpressForPlatform.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GAMECLIENT.getCode()));
                        if (lineCode.endsWith("0")) {
                            queryExpressForPlatform.add(QueryCriterions.eq(ClientLineField.CODE, lineCode.substring(0, lineCode.length() - 1) + "1"));   //去掉lineCode最后的0或1
                        } else {
                            queryExpressForPlatform.add(QueryCriterions.eq(ClientLineField.CODE, lineCode.substring(0, lineCode.length() - 1) + "0"));   //去掉lineCode最后的0或1
                        }
                        List<ClientLine> platformList = JoymeAppServiceSngl.get().queryClientLineList(queryExpressForPlatform);

                        for (int j = 0; j < platformList.size(); j++) {
                            long lineIdTemp = platformList.get(j).getLineId();
                            if (lineIdTemp != lineId) {
                                lineIdNew = lineIdTemp;                       //拿到那个不同的lineId
                                break;
                            }
                        }

                        if (lineIdNew != 0) {
                            item.setLineId(lineIdNew);
                            JoymeAppServiceSngl.get().createClientLineItem(item);
                        }
                    }
                }

            } else if (!StringUtil.isEmpty(lineCode) && lineCode.contains("gc_hotgameimgs")) {

                ClientLineItem item = new ClientLineItem();
                item.setPicUrl(picUrl);

                item.setLineId(lineId);
                item.setTitle(title);
                item.setDesc(lineDesc);
                item.setUrl(url);
                item.setItemType(ClientItemType.CUSTOM);
                item.setRedirectType(AppRedirectType.getByCode(Integer.valueOf(redirectType)));
                item.setItemDomain(ClientItemDomain.DEFAULT);

                if (!StringUtil.isEmpty(gamePublicTime)) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    item.setItemCreateDate(format.parse(gamePublicTime));
                }
                item.setDisplayOrder(displayOrder);
                item.setValidStatus(ValidStatus.VALID);

                JoymeAppServiceSngl.get().createClientLineItem(item);


                //新增的时候也添加到另外一条line里
                if (!StringUtil.isEmpty(platform) && platform.equals("all") && lineId != null) {

                    long lineIdNew = 0L;
                    QueryExpress queryExpressForPlatform = new QueryExpress();
                    queryExpressForPlatform.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.CUSTOM.getCode()));
                    queryExpressForPlatform.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GAMECLIENT.getCode()));
                    if (lineCode.endsWith("0")) {
                        queryExpressForPlatform.add(QueryCriterions.eq(ClientLineField.CODE, lineCode.substring(0, lineCode.length() - 1) + "1"));   //去掉lineCode最后的0或1
                    } else {
                        queryExpressForPlatform.add(QueryCriterions.eq(ClientLineField.CODE, lineCode.substring(0, lineCode.length() - 1) + "0"));   //去掉lineCode最后的0或1
                    }
                    List<ClientLine> platformList = JoymeAppServiceSngl.get().queryClientLineList(queryExpressForPlatform);

                    for (int j = 0; j < platformList.size(); j++) {
                        long lineIdTemp = platformList.get(j).getLineId();
                        if (lineIdTemp != lineId) {
                            lineIdNew = lineIdTemp;                       //拿到那个不同的lineId
                            break;
                        }
                    }
                    if (lineIdNew != 0L) {
                        item.setLineId(lineIdNew);
                        JoymeAppServiceSngl.get().createClientLineItem(item);
                    }
                }
            } else if (!StringUtil.isEmpty(lineCode) && lineCode.contains("gc_hotgamelinks")) {

                ClientLineItem item = new ClientLineItem();
                //category 字段用于存放标签页轮播图 的左上角的小图    和  gc_hotgamelinks中的iconurl
                item.setCategory(category);
                // item.setPicUrl(picUrl);

                //只有属性是热门游戏右侧链接 时  才有categorycolor 这个属性

                item.setCategoryColor(color);
                item.setRate(rate);

                //玩霸2.0.3新增 热门下面标签的数量的外圈颜色
                item.setAuthor(author);

                item.setLineId(lineId);
                item.setTitle(title);
                item.setDesc(lineDesc);
                item.setUrl(url);
                item.setItemType(ClientItemType.CUSTOM);
                item.setRedirectType(AppRedirectType.getByCode(Integer.valueOf(redirectType)));
                item.setItemDomain(ClientItemDomain.DEFAULT);

                if (!StringUtil.isEmpty(gamePublicTime)) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    item.setItemCreateDate(format.parse(gamePublicTime));
                }
                item.setDisplayOrder(displayOrder);
                item.setValidStatus(ValidStatus.VALID);

                JoymeAppServiceSngl.get().createClientLineItem(item);

            }

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.GAMECLIENT_CUSTOM_ADD);  //操作的类型
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

            if (queryString.length() > 1950) {
                queryString = queryString.substring(0, 1950);
            }
            log.setOpAfter("玩霸轮播图添加queryString" + queryString); //描述 推荐用中文
            addLog(log);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/gameclient/clientline/custom/itemlist?lineId="+lineId+"&lineName="+lineName+"&lineCode="+lineCode);
    }


    @RequestMapping(value = "/sort/{sort}")
    @ResponseBody
    public String sort(@PathVariable(value = "sort") String sort,
                       @RequestParam(value = "lineid", required = false) Long lineId,
                       @RequestParam(value = "itemid", required = false) Long itemId,
                       @RequestParam(value = "sortType", required = false) String sortType) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
        if (lineId == null || itemId == null) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            return binder.toJson(resultObjectMsg);
        }
        Long returnItemId = null;
        try {
            if (!StringUtil.isEmpty(sortType)) {     //针对标签页轮播图
                returnItemId = sortService(sort, lineId, itemId, sortType.trim());
            } else {                             //针对热门页轮播图  和 热门页右侧链接
                returnItemId = ClientLineWebLogic.sort(sort, lineId, itemId);
            }
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
        mapMessage.put("itemid", itemId);
        mapMessage.put("returnitemid", returnItemId);
        resultObjectMsg.setResult(mapMessage);
        return binder.toJson(resultObjectMsg);
    }

    private Long sortService(String sort, Long lineId, Long itemId, String sortType) throws ServiceException {
        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, lineId));
        ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, itemId)));
        if (clientLineItem == null) {
            return null;
        }
        queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
        Long tagIdSearch = Long.valueOf(sortType);
        if (tagIdSearch != null && tagIdSearch != -100) {
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.CONTENTID, tagIdSearch));
        }
        if (sort.equals("up")) {
            queryExpress.add(QueryCriterions.lt(ClientLineItemField.DISPLAY_ORDER, clientLineItem.getDisplayOrder()));
            queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.DESC));
        } else {
            queryExpress.add(QueryCriterions.gt(ClientLineItemField.DISPLAY_ORDER, clientLineItem.getDisplayOrder()));
            queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));
        }

        PageRows<ClientLineItem> pageRows = JoymeAppServiceSngl.get().queryClientLineItemByPage(queryExpress, new Pagination(1, 1, 1));
        if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
            updateExpress1.set(ClientLineItemField.DISPLAY_ORDER, clientLineItem.getDisplayOrder());
            //  updateExpress1.set(ClientLineItemField.ITEM_CREATE_DATE, clientLineItem.getItemCreateDate());
            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress1, pageRows.getRows().get(0).getItemId());


            updateExpress2.set(ClientLineItemField.DISPLAY_ORDER, pageRows.getRows().get(0).getDisplayOrder());
            //    updateExpress2.set(ClientLineItemField.ITEM_CREATE_DATE, pageRows.getRows().get(0).getItemCreateDate());
            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress2, clientLineItem.getItemId());

        }
        return pageRows.getRows().get(0).getItemId();
    }


    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyLinePage(@RequestParam(value = "lineId", required = false) Long lineId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (lineId == null) {
            return new ModelAndView("forward:/gameclient/clientline/custom/list");
        }

        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (clientLine == null) {
                return new ModelAndView("forward:/gameclient/clientline/custom/list");
            }
            mapMessage.put("clientLine", clientLine);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/gameclient/custom/modifyline", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modifyLine(@RequestParam(value = "lineId", required = false) Long lineId,
                                   @RequestParam(value = "lineName", required = false) String lineName,
                                   @RequestParam(value = "lineCode", required = false) String lineCode) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();


        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineField.LINE_NAME, lineName);
        updateExpress.set(ClientLineField.CODE, lineCode);
        updateExpress.set(ClientLineField.MODIFY_DATE, new Date());
        updateExpress.set(ClientLineField.MODIFY_USERID, getCurrentUser().getUserid());

        try {
            ClientLine existLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.CODE, lineCode)).add(QueryCriterions.ne(ClientLineField.LINE_ID, lineId)));
            if (existLine != null) {
                mapMessage.put("lineId", lineId);
                mapMessage.put("codeExist", "line.code.exist");
                return new ModelAndView("forward:/gameclient/clientline/custom/modifypage", mapMessage);
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
        return new ModelAndView("forward:/gameclient/clientline/custom/list");
    }


    @RequestMapping(value = "/itemmodifypage")
    public ModelAndView itemmodifypage(@RequestParam(value = "itemId", required = true) Long itemId,
                                       @RequestParam(value = "lineId", required = false) Long lineId,
                                       @RequestParam(value = "lineName", required = false) String lineName,
                                       @RequestParam(value = "startRowIdx", required = false) int startRowIdx,
                                       @RequestParam(value = "lineCode", required = false) String lineCode) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineName", lineName);
        mapMessage.put("lineCode", lineCode);
        mapMessage.put("startRowIdx", startRowIdx);
        Map<Integer, String> types = new HashMap<Integer, String>();
        try {

            Map<Long, String> tagMap = new HashMap<Long, String>();
            QueryExpress queryExpressForTag = new QueryExpress();
            queryExpressForTag.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.SYHB.getCode()));
            List<AnimeTag> tagList = JoymeAppServiceSngl.get().queryAnimeTag(queryExpressForTag);
            if (tagList != null) {
                for (int i = 0; i < tagList.size(); i++) {
                    tagMap.put(tagList.get(i).getTag_id(), tagList.get(i).getTag_name());
                }
            }
            mapMessage.put("tagMap", tagMap);


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
            if (clientLineItem != null && clientLineItem.getPicUrl() != null) {
                clientLineItem.setPicUrl(URLUtils.getJoymeDnUrl(clientLineItem.getPicUrl()));
            }
            //category 字段用于存放标签页轮播图 的左上角的小图
            if (clientLineItem != null && lineCode.contains("gc_topicimgs") && clientLineItem.getCategory() != null) {
                clientLineItem.setCategory(URLUtils.getJoymeDnUrl(clientLineItem.getCategory()));
            }
            mapMessage.put("item", clientLineItem);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (IllegalAccessException e) {
            GAlerter.lab(this.getClass().getName() + " occured IllegalAccessException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");

        }
        return new ModelAndView("/gameclient/custom/modifyitem", mapMessage);
    }

    @RequestMapping(value = "/itemmodify")
    public ModelAndView itemmodify(@RequestParam(value = "itemId", required = false) Long itemId,
                                   @RequestParam(value = "lineId", required = false) Long lineId,
                                   @RequestParam(value = "lineName", required = false) String lineName,
                                   @RequestParam(value = "lineCode", required = false) String lineCode,

                                   @RequestParam(value = "title", required = false) String title,
                                   @RequestParam(value = "lineDesc", required = false) String lineDesc,
                                   @RequestParam(value = "picUrl", required = false) String picUrl,
                                   @RequestParam(value = "category", required = false) String category,
                                   @RequestParam(value = "color", required = false, defaultValue = "") String color,
                                   @RequestParam(value = "url", required = false) String url,
                                   @RequestParam(value = "redirectType", required = false) String redirectType,
                                   @RequestParam(value = "gamePublicTime", required = false) String gamePublicTime,
                                   @RequestParam(value = "rate", required = false) String rate,

                                   @RequestParam(value = "tagId", required = false) Long tagId,
                                   @RequestParam(value = "author", required = false) String author,
                                   @RequestParam(value = "isHot", required = false) String isHot,
                                   @RequestParam(value = "startRowIdx", required = false) int startRowIdx,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineName", lineName);
        mapMessage.put("lineCode", lineCode);
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            if (!StringUtil.isEmpty(lineCode) && (lineCode.contains("gc_topicimgs") || lineCode.contains("gc_hotgameimgs"))) {
                updateExpress.set(ClientLineItemField.PIC_URL, picUrl);
            }
            //category 字段用于存放标签页轮播图 的左上角的小图
            if (!StringUtil.isEmpty(lineCode) && (lineCode.contains("gc_topicimgs") || lineCode.contains("gc_hotgamelinks"))) {
                updateExpress.set(ClientLineItemField.CATEGORY, category);
            } else {
                updateExpress.set(ClientLineItemField.CATEGORY, "");
            }

            //只有当属性是热门游戏右侧链接时才有这个链接颜色的属性
            if (!StringUtil.isEmpty(lineCode) && lineCode.contains("gc_hotgamelinks")) {
                updateExpress.set(ClientLineItemField.CATEGORY_COLOR, color);
                updateExpress.set(ClientLineItemField.RATE, rate);
                //玩霸2.0.3新增 热门下面标签的数量的外圈颜色
                updateExpress.set(ClientLineItemField.AUTHOR, author);
            }
            if (!StringUtil.isEmpty(lineCode) && lineCode.contains("gc_topicimgs")) {
                updateExpress.set(ClientLineItemField.CONTENTID, tagId);
            }

            updateExpress.set(ClientLineItemField.TITLE, title);
            updateExpress.set(ClientLineItemField.DESC, lineDesc);
            updateExpress.set(ClientLineItemField.URL, url);
            updateExpress.set(ClientLineItemField.REDIRCT_TYPE, Integer.valueOf(redirectType));

            if (!StringUtil.isEmpty(gamePublicTime)) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date itemCreateDate = format.parse(gamePublicTime);
                updateExpress.set(ClientLineItemField.ITEM_CREATE_DATE, itemCreateDate);
            }

            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);

            //以下为添加修改日志
            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.GAMECLIENT_CUSTOM_MODIFY);  //操作的类型
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

            if (queryString.length() > 1950) {
                queryString = queryString.substring(0, 1950);
            }
            log.setOpAfter("玩霸轮播图修改,queryString" + queryString); //描述 推荐用中文
            addLog(log);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/gameclient/clientline/custom/itemlist?lineId="+lineId+"&lineName="+lineName+"&lineCode="+lineCode+"&pager.offset="+startRowIdx);
    }

    @RequestMapping(value = "/itemdelete")
    public ModelAndView itemdelete(@RequestParam(value = "itemId", required = false) Long itemId,
                                   @RequestParam(value = "lineId", required = false) Long lineId,
                                   @RequestParam(value = "lineCode", required = false) String lineCode,
                                   @RequestParam(value = "startRowIdx", required = false) int startRowIdx,
                                   @RequestParam(value = "lineName", required = false) String lineName) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        if (itemId == null) {
            return new ModelAndView("redirect:/gameclient/clientline/custom/itemlist?lineId="+lineId+"&lineCode="+lineCode+"&lineName="+lineName+"&pager.offset="+startRowIdx);
        }
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineItemField.VALID_STATUS, ValidStatus.REMOVED.getCode());
        try {
            boolean bool = JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);
            if (bool) {
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.GAMECLIENT_CUSTOM_DELETE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("玩霸轮播图删除->itemId:" + itemId + ";lineId:" + lineId);
                addLog(log);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            return new ModelAndView("redirect:/gameclient/clientline/custom/itemlist?lineId="+lineId+"&lineCode="+lineCode+"&lineName="+lineName+"&errorMsg=system.error&pager.offset="+startRowIdx);
        }
        return new ModelAndView("redirect:/gameclient/clientline/custom/itemlist?lineId="+lineId+"&lineCode="+lineCode+"&lineName="+lineName+"&pager.offset="+startRowIdx);
    }
}
