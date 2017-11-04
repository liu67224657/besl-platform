package com.enjoyf.webapps.tools.webpage.controller.viewline;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ViewLineHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileDomain;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.service.viewline.ViewCategory;
import com.enjoyf.platform.service.viewline.ViewCategoryPrivacy;
import com.enjoyf.platform.service.viewline.ViewCategoryPrivacyField;
import com.enjoyf.platform.service.viewline.ViewCategoryPrivacyLevel;
import com.enjoyf.platform.service.viewline.ViewItemType;
import com.enjoyf.platform.service.viewline.ViewLine;
import com.enjoyf.platform.service.viewline.ViewLineAutoFillType;
import com.enjoyf.platform.service.viewline.ViewLineField;
import com.enjoyf.platform.service.viewline.ViewLineItem;
import com.enjoyf.platform.service.viewline.ViewLineItemDisplayInfo;
import com.enjoyf.platform.service.viewline.ViewLineItemDisplayType;
import com.enjoyf.platform.service.viewline.ViewLineItemField;
import com.enjoyf.platform.service.viewline.ViewLineLocation;
import com.enjoyf.platform.service.viewline.ViewLineServiceSngl;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.RangeRows;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterionRelation;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.dto.ViewCategoryPrivacyDTO;
import com.enjoyf.webapps.tools.weblogic.viewline.ViewLineWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.common.base.Strings;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * line: taijunli
 * Date: 11-12-7
 * Time: 上午9:37
 */
@Controller
@RequestMapping(value = "/viewline")
public class ViewLineController extends ToolsBaseController {

    @Resource(name = "viewLineWebLogic")
    private ViewLineWebLogic viewLineWebLogic;

    //
    private ViewLineHotdeployConfig hotdeployConfig = HotdeployConfigFactory.get().getConfig(ViewLineHotdeployConfig.class);

    private static List<ActStatus> publishStatus = new ArrayList<ActStatus>();
    private static List<ValidStatus> validStatus = new ArrayList<ValidStatus>();

    static {
        validStatus.add(ValidStatus.VALID);
        validStatus.add(ValidStatus.REMOVED);

        publishStatus.add(ActStatus.UNACT);
        publishStatus.add(ActStatus.ACTED);

    }

    @RequestMapping(value = "/linelist")
    public ModelAndView lineList(
            @RequestParam(value = "categoryId", required = true) int categoryId) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        try {
            //
            QueryExpress queryExpress = new QueryExpress();

            queryExpress.add(QueryCriterions.eq(ViewLineField.CATEGORYID, categoryId));
            queryExpress.add(QuerySort.add(ViewLineField.DISPLAYORDER));

            //
            Map<String, ViewLine> rows = viewLineWebLogic.queryLines(queryExpress);

            List<ViewLine> list = new ArrayList<ViewLine>();

            list.addAll(rows.values());
            //
            mapMsg.put("rows", list);
            mapMsg.put("categoryId", categoryId);
            mapMsg.put("validStatuses", ValidStatus.getAll());

        } catch (ServiceException e) {
            GAlerter.lab("lineItemList Exception :", e);
        }

        return new ModelAndView("/viewline/tab_linelist", mapMsg);
    }

    @RequestMapping(value = "/lineprecreate")
    public ModelAndView createLine(
            @RequestParam(value = "categoryId", required = true) int categoryId) {
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        Collection<ViewItemType> itemTypes = ViewItemType.getAll();

        //
        ViewLine line = new ViewLine();
//        Map<String, ViewLineLocation> locationMap = new HashMap<String, ViewLineLocation>();

//        try {
//            ViewCategoryConfig viewCategoryConfig = hotdeployConfig.getCategoryAspectConfig(viewLineWebLogic.getCategoryById(categoryId).getCategoryAspect());
//            locationMap = viewCategoryConfig.getLineLocations();
//        } catch (ServiceException e) {
//            GAlerter.lab("get Category by categoryId occurred an Exception: " + e);
//        }
        //
        mapMsg.put("line", line);
//        mapMsg.put("locationMap", locationMap);
        mapMsg.put("itemTypes", itemTypes);
        mapMsg.put("categoryId", categoryId);

        //
//        mapMsg.put("publishStatuses", ActStatus.getAll());
//        mapMsg.put("autoFillTypes", ViewLineAutoFillType.getAll());

        //
        return new ModelAndView("/viewline/precreateline", mapMsg);
    }


    @RequestMapping(value = "/refline")
    public ModelAndView referenceLine(
            @RequestParam(value = "categoryId", required = true) int categoryId,
            @RequestParam(value = "lineName") String lineName,
            @RequestParam(value = "lineDesc") String lineDesc,
            @RequestParam(value = "locationCode") String locationCode,
            @RequestParam(value = "itemMinCount", required = false, defaultValue = "0") Integer itemMinCount,
            @RequestParam(value = "itemType", required = false) String itemType) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();
        Collection<ViewItemType> itemTypes = ViewItemType.getAll();

        mapMsg.put("errorMsgMap", errorMsgMap);
        mapMsg.put("itemTypes", itemTypes);

        //
        ViewLine line = new ViewLine();
        ViewCategory category = null;

        mapMsg.put("categoryId", categoryId);

        try {
            category = viewLineWebLogic.getCategoryById(categoryId);
            line.setCategoryId(category.getCategoryId());

        } catch (ServiceException e) {
            GAlerter.lab("get Category by categoryId occurred an Exception: " + e);
            errorMsgMap.put("system", "error.exception");

            return new ModelAndView("forward:/viewline/lineprecreate", mapMsg);
        }

        if (!StringUtil.isEmpty(lineName)) {
            line.setLineName(lineName);
        } else {
            errorMsgMap.put("lineName", "error.viewline.line.linename.null");
        }

        if (!StringUtil.isEmpty(lineDesc)) {
            line.setLineDesc(lineDesc);
        }

        if (!StringUtil.isEmpty(locationCode)) {
            line.setLocationCode(locationCode);
        } else {
            errorMsgMap.put("locationCode", "error.viewline.line.locationcode.null");
        }

        if (!StringUtil.isEmpty(itemType)) {
            line.setItemType(ViewItemType.getByCode(itemType));
        } else {
            errorMsgMap.put("itemType", "error.viewline.line.itemType.null");
        }

        if (category != null) {
            line.setCategoryAspect(category.getCategoryAspect());
        }

        line.setItemMinCount(itemMinCount);
        line.setCreateDate(new Date());
        line.setCreateUserid(getCurrentUser().getUserid());
        line.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));

        if (!errorMsgMap.isEmpty()) {
            return new ModelAndView("forward:/viewline/lineprecreate", mapMsg);
        }

        try {
            viewLineWebLogic.saveLine(line);
        } catch (ServiceException e) {
            GAlerter.lab("save Viewline occurred an Exception: " + e);
            return new ModelAndView("forward:/viewline/lineprecreate", mapMsg);
        }


        return new ModelAndView("forward:/viewline/linelist", mapMsg);
    }


    @RequestMapping(value = "/createline")
    public ModelAndView createLine(
            //
            @RequestParam(value = "locationCode", required = false) String locationCode,
            @RequestParam(value = "lineName", required = false) String lineName,
            @RequestParam(value = "lineDesc", required = false) String lineDesc,
            @RequestParam(value = "seoKeyWord", required = false) String seoKeyWord,
            @RequestParam(value = "seoDesc", required = false) String seoDesc,
            @RequestParam(value = "lineTypeCode", required = false) String lineTypeCode,
            @RequestParam(value = "itemTypeCode", required = true) String itemTypeCode,
            @RequestParam(value = "autoFillTypeCode", required = false) String autoFillTypeCode,
            //the auto fill rule.
            @RequestParam(value = "afrkey01", required = false) String afrkey01,
            @RequestParam(value = "afrkey02", required = false) String afrkey02,
            @RequestParam(value = "afrkey03", required = false) String afrkey03,
            @RequestParam(value = "afrkey04", required = false) String afrkey04,
            @RequestParam(value = "afrkey05", required = false) String afrkey05,
            @RequestParam(value = "afrkey06", required = false) String afrkey06,
            @RequestParam(value = "afrkey07", required = false) String afrkey07,
            @RequestParam(value = "afrkey08", required = false) String afrkey08,
            @RequestParam(value = "afrkey09", required = false) String afrkey09,
            //display setting.
            @RequestParam(value = "templateCode", required = false) String templateCode,
            @RequestParam(value = "bgImageUrl", required = false) String bgImageUrl,
            //
            @RequestParam(value = "lineItemMixCount", required = false) Integer lineItemMixCount) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();

        //
        ViewLine line = new ViewLine();

        //full fill the line object.

        if (!Strings.isNullOrEmpty(lineName)) {
            line.setLineName(lineName);
        } else {
            errorMsgMap.put("lineName", "error.viewline.linename.blank");
        }

        if (!Strings.isNullOrEmpty(lineDesc)) {
            line.setLineDesc(lineDesc);
        }

        //
        line.setAutoFillType(ViewLineAutoFillType.getByCode(autoFillTypeCode));

        //
        Map<String, String> autoFillRuleKeyValues = new HashMap<String, String>();

        autoFillRuleKeyValues.put("afrkey01", afrkey01);
        autoFillRuleKeyValues.put("afrkey02", afrkey02);
        autoFillRuleKeyValues.put("afrkey03", afrkey03);
        autoFillRuleKeyValues.put("afrkey04", afrkey04);
        autoFillRuleKeyValues.put("afrkey05", afrkey05);
        autoFillRuleKeyValues.put("afrkey06", afrkey06);
        autoFillRuleKeyValues.put("afrkey07", afrkey07);
        autoFillRuleKeyValues.put("afrkey08", afrkey08);
        autoFillRuleKeyValues.put("afrkey09", afrkey09);

        //
        mapMsg.putAll(autoFillRuleKeyValues);

        //
        line.setAutoFillRule(viewLineWebLogic.generateAutoFillRule(line.getItemType(), autoFillRuleKeyValues, errorMsgMap, mapMsg));


        line.setCreateDate(new Date());
        line.setCreateUserid(String.valueOf(getCurrentUser().getUserid()));

        //return confirm.
        if (errorMsgMap.isEmpty()) {
            return new ModelAndView("redirect:/viewline/linedetail?lineId=" + line.getLineId());
        } else {
            //
            mapMsg.put("line", line);
            mapMsg.put("errorMsgMap", errorMsgMap);

            //
            mapMsg.put("autoFillTypes", ViewLineAutoFillType.getAll());

            //
            return new ModelAndView("/viewline/precreateline", mapMsg);
        }
    }

    @RequestMapping(value = "/batchstatuslines")
    public ModelAndView batchStatusLines(
            @RequestParam(value = "lineIds", required = false) String lineIds,
            //
            @RequestParam(value = "categoryId", required = true) String categoryId,
            @RequestParam(value = "lineName", required = false) String lineName,
            @RequestParam(value = "validStatusCode", required = false) String validStatusCode,
            @RequestParam(value = "publishStatusCode", required = false) String publishStatusCode,
            @RequestParam(value = "lineTypeCode", required = false) String lineTypeCode,
            @RequestParam(value = "lineItemTypeCode", required = false) String lineItemTypeCode,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
            //
            @RequestParam(value = "updateValidStatusCode", required = false) String updateValidStatusCode,
            @RequestParam(value = "updatePublishStatusCode", required = false) String updatePublishStatusCode) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        mapMsg.put("lineName", lineName);
        mapMsg.put("validStatusCode", validStatusCode);
        mapMsg.put("publishStatusCode", publishStatusCode);
        mapMsg.put("lineItemTypeCode", lineItemTypeCode);
        mapMsg.put("lineTypeCode", lineTypeCode);
        mapMsg.put("categoryId", categoryId);

        //
        UpdateExpress updateExpress = new UpdateExpress();

        updateExpress.set(ViewLineField.UPDATEDATE, new Date());
        updateExpress.set(ViewLineField.UPDATEUSERID, String.valueOf(getCurrentUser().getUserid()));

        if (!StringUtil.isEmpty(updateValidStatusCode)) {
            updateExpress.set(ViewLineField.VALIDSTATUS, updateValidStatusCode);
        }

        //
        try {
            if (!Strings.isNullOrEmpty(lineIds) && (ValidStatus.getByCode(updateValidStatusCode) != null || ActStatus.getByCode(updatePublishStatusCode) != null)) {
                //
                String[] lineIdSplits = lineIds.split(",");
                for (String lineId : lineIdSplits) {
                    //
                    QueryExpress queryExpress = new QueryExpress();
                    queryExpress.add(QueryCriterions.eq(ViewLineField.LINEID, Integer.valueOf(lineId)));

                    //
                    viewLineWebLogic.modifyLine(updateExpress, queryExpress);
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab("batchStatusLine, ServiceException :", e);
        }

        return new ModelAndView("forward:/viewline/linelist", mapMsg);
    }

    @RequestMapping(value = "/linepreedit")
    public ModelAndView preEditLine(
            @RequestParam(value = "lineId", required = true) int lineId) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();

//        Map<String, ViewLineLocation> locationMap = new HashMap<String, ViewLineLocation>();
        Collection<ViewItemType> itemTypes = ViewItemType.getAll();
        //
        ViewLine line = new ViewLine();

        try {
            line = viewLineWebLogic.getLineById(lineId);

//            ViewCategoryConfig viewCategoryConfig = hotdeployConfig.getCategoryAspectConfig(viewLineWebLogic.getCategoryById(line.getCategoryId()).getCategoryAspect());
//            locationMap = viewCategoryConfig.getLineLocations();
//
        } catch (ServiceException e) {
            GAlerter.lab("preEditLine a ServiceException :", e);
        }

        //
        mapMsg.put("line", line);
//        mapMsg.put("locationMap", locationMap);
        mapMsg.put("itemTypes", itemTypes);

        viewLineWebLogic.autoFillRuleToInput(line.getItemType(), line.getAutoFillRule(), mapMsg);

        //
        mapMsg.put("autoFillTypes", ViewLineAutoFillType.getAll());


        //
        return new ModelAndView("/viewline/preeditline", mapMsg);
    }

    /**
     * @return 修改用户
     */
    @RequestMapping(value = "/lineedit")
    public ModelAndView modifyLine(
            @RequestParam(value = "lineId", required = true) int lineId,
            @RequestParam(value = "categoryId", required = true, defaultValue = "0") int categoryId,
            //
            @RequestParam(value = "lineCode", required = false) String lineCode,
            @RequestParam(value = "lineName", required = false) String lineName,
            @RequestParam(value = "lineDesc", required = false) String lineDesc,
            @RequestParam(value = "displayOrder", required = false, defaultValue = "0") int displayOrder,
            @RequestParam(value = "seoKeyWord", required = false) String seoKeyWord,
            @RequestParam(value = "seoDesc", required = false) String seoDesc,
            @RequestParam(value = "lineTypeCode", required = false) String lineTypeCode,
            @RequestParam(value = "lineItemTypeCode", required = false) String lineItemTypeCode,
            @RequestParam(value = "autoFillTypeCode", required = false) String autoFillTypeCode,
            @RequestParam(value = "locationCode", required = false) String locationCode,
            @RequestParam(value = "itemType", required = false) String itemType,
            //the auto fill rule.
            @RequestParam(value = "afrkey01", required = false) String afrkey01,
            @RequestParam(value = "afrkey02", required = false) String afrkey02,
            @RequestParam(value = "afrkey03", required = false) String afrkey03,
            @RequestParam(value = "afrkey04", required = false) String afrkey04,
            @RequestParam(value = "afrkey05", required = false) String afrkey05,
            @RequestParam(value = "afrkey06", required = false) String afrkey06,
            @RequestParam(value = "afrkey07", required = false) String afrkey07,
            @RequestParam(value = "afrkey08", required = false) String afrkey08,
            @RequestParam(value = "afrkey09", required = false) String afrkey09,
            //display setting.
            @RequestParam(value = "templateCode", required = false) String templateCode,
            @RequestParam(value = "bgImageUrl", required = false) String bgImageUrl,
            //
            @RequestParam(value = "itemMinCount", required = false) Integer itemMinCount) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();

        //new the line and fill it.
        ViewLine line = new ViewLine();
        ViewLineLocation viewLineLocation = null;

        line.setLineName(lineName);
        line.setLineDesc(lineDesc);

        if (!StringUtil.isEmpty(locationCode)) {
            line.setLocationCode(locationCode);
        } else {
            errorMsgMap.put("locationCode", "error.viewline.line.locationcode.null");
        }

        if (!StringUtil.isEmpty(itemType)) {
            line.setItemType(ViewItemType.getByCode(itemType));
        } else {
            errorMsgMap.put("itemType", "error.viewline.line.itemType.null");
        }


/*        line.setAutoFillType(ViewLineAutoFillType.getByCode(autoFillTypeCode));

        //
        Map<String, String> autoFillRuleKeyValues = new HashMap<String, String>();
        autoFillRuleKeyValues.put("afrkey01", afrkey01);
        autoFillRuleKeyValues.put("afrkey02", afrkey02);
        autoFillRuleKeyValues.put("afrkey03", afrkey03);
        autoFillRuleKeyValues.put("afrkey04", afrkey04);
        autoFillRuleKeyValues.put("afrkey05", afrkey05);
        autoFillRuleKeyValues.put("afrkey06", afrkey06);
        autoFillRuleKeyValues.put("afrkey07", afrkey07);
        autoFillRuleKeyValues.put("afrkey08", afrkey08);
        autoFillRuleKeyValues.put("afrkey09", afrkey09);

        mapMsg.putAll(autoFillRuleKeyValues);

        //the auto fill rule setting.
        line.setAutoFillRule(viewLineWebLogic.generateAutoFillRule(line.getItemType(), autoFillRuleKeyValues, errorMsgMap, mapMsg));*/


        //
        UpdateExpress updateExpress = new UpdateExpress();

        updateExpress.set(ViewLineField.LOCATIONCODE, line.getLocationCode());
        updateExpress.set(ViewLineField.LINENAME, line.getLineName());
        updateExpress.set(ViewLineField.LINEDESC, line.getLineDesc());

        updateExpress.set(ViewLineField.ITEMTYPE, line.getItemType().getCode());
        updateExpress.set(ViewLineField.DISPLAYORDER, displayOrder);
/*        updateExpress.set(ViewLineField.AUTOFILLTYPE, line.getAutoFillType().getCode());
        updateExpress.set(ViewLineField.AUTOFILLRULE, line.getAutoFillRule().toJson());*/

        updateExpress.set(ViewLineField.UPDATEDATE, new Date());
        updateExpress.set(ViewLineField.UPDATEUSERID, String.valueOf(getCurrentUser().getUserid()));
        updateExpress.set(ViewLineField.ITEMMINCOUNT, itemMinCount);

        //fullfill the query express.
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ViewLineField.LINEID, lineId));


        //
        if (errorMsgMap.isEmpty()) {

            try {
                boolean bool = viewLineWebLogic.modifyLine(updateExpress, queryExpress);

                if (bool) {
                    //todo log
                }
            } catch (ServiceException e) {
                GAlerter.lab("line edit a ServiceException :", e);
            }

            return new ModelAndView("redirect:/viewline/linelist?categoryId=" + categoryId);
        } else {
            //
            mapMsg.put("errorMsgMap", errorMsgMap);
            mapMsg.put("line", line);

            return new ModelAndView("/viewline/preeditline", mapMsg);
        }
    }

    @RequestMapping(value = "/linedetail")
    public ModelAndView preDetailLine(
            @RequestParam(value = "lineId", required = true) int lineId) {
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        ViewLine line = null;

        //
        try {
            line = viewLineWebLogic.getLineById(lineId);
        } catch (ServiceException e) {
            GAlerter.lab("linedetail, ServiceException :", e);
        }

        //
        mapMsg.put("line", line);
        viewLineWebLogic.autoFillRuleToInput(line.getItemType(), line.getAutoFillRule(), mapMsg);

        //
        return new ModelAndView("/viewline/detailline", mapMsg);
    }

    @RequestMapping(value = "/listlineitem")
    public ModelAndView lineItemList(
            @RequestParam(value = "lineId", required = true) int lineId,
            @RequestParam(value = "istop", required = false) String istop,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "searchValidStatus", required = false) String searchValidStatus,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        int curPage = (pageStartIndex / pageSize) + 1;

        //
        ViewLine line = null;

        try {
            line = viewLineWebLogic.getLineById(lineId);

            //
            Pagination page = new Pagination(curPage * pageSize, curPage, pageSize);

            //
            QueryExpress queryExpress = new QueryExpress();

            if (!StringUtil.isEmpty(startDate)) {
                queryExpress.add(QueryCriterions.geq(ViewLineItemField.CREATEDATE, Strings.isNullOrEmpty(startDate) ? DateUtil.adjustDate(DateUtil.ignoreTime(new Date()), Calendar.DAY_OF_MONTH, -5)
                        : DateUtil.StringTodate(startDate, DateUtil.PATTERN_DATE)));
            }
            if (!StringUtil.isEmpty(endDate)) {
                queryExpress.add(QueryCriterions.leq(ViewLineItemField.CREATEDATE, Strings.isNullOrEmpty(endDate) ? DateUtil.StringTodate(DateUtil.DateToString(new Date(), DateUtil.DATE_FORMAT) + " 23:59:59", DateUtil.DEFAULT_DATE_FORMAT2)
                        : DateUtil.StringTodate((endDate + ToolsLog.END_TIME_PRIFIX), DateUtil.PATTERN_DATE_TIME)));
            }
            if (!StringUtil.isEmpty(searchValidStatus)) {
                queryExpress.add(QueryCriterions.eq(ViewLineItemField.VALIDSTATUS, ValidStatus.getByCode(searchValidStatus).getCode()));
            }
            if (!StringUtil.isEmpty(istop)) {
                if ("y".equals(istop)) {
                    queryExpress.add(QueryCriterions.bitwiseAnd(ViewLineItemField.DISPLAYTYPE, QueryCriterionRelation.EQ, ViewLineItemDisplayType.TOP, ViewLineItemDisplayType.TOP));
                } else if ("n".equals(istop)) {
                    queryExpress.add(QueryCriterions.bitwiseAnd(ViewLineItemField.DISPLAYTYPE, QueryCriterionRelation.LT, ViewLineItemDisplayType.TOP, ViewLineItemDisplayType.TOP));
                }
            }


            queryExpress.add(QueryCriterions.eq(ViewLineItemField.LINEID, lineId));
            queryExpress.add(QuerySort.add(ViewLineItemField.DISPLAYORDER));
            if (StringUtil.isEmpty(istop)) {
                queryExpress.add(QueryCriterions.bitwiseAnd(ViewLineItemField.DISPLAYTYPE, QueryCriterionRelation.LT, ViewLineItemDisplayType.TOP, ViewLineItemDisplayType.TOP));
            }

            //
            PageRows<ViewLineItem> pageRows = viewLineWebLogic.queryLineItems(queryExpress, page);

            if (StringUtil.isEmpty(istop)) {
                QueryExpress topQueryExpress = new QueryExpress();
                topQueryExpress.add(QueryCriterions.eq(ViewLineItemField.LINEID, lineId))
                        .add(QuerySort.add(ViewLineItemField.DISPLAYORDER));
                topQueryExpress.add(QueryCriterions.bitwiseAnd(ViewLineItemField.DISPLAYTYPE, QueryCriterionRelation.EQ, ViewLineItemDisplayType.TOP, ViewLineItemDisplayType.TOP));

                List<ViewLineItem> nonTopList = viewLineWebLogic.queryLineItems(topQueryExpress, page).getRows();

                mapMsg.put("nonTopItems", viewLineWebLogic.buildLineItemDTOs(line.getItemType(), nonTopList));
            }


            mapMsg.put("lineItems", viewLineWebLogic.buildLineItemDTOs(line.getItemType(), pageRows.getRows()));
            mapMsg.put("page", pageRows.getPage());
            mapMsg.put("line", line);

            //
            mapMsg.put("validStatuses", validStatus);
            mapMsg.put("allValidStatus", ValidStatus.getAll());
            mapMsg.put("startDate", startDate);
            mapMsg.put("endDate", endDate);
            mapMsg.put("istop", istop);
            mapMsg.put("searchValidStatus", searchValidStatus);
        } catch (ServiceException e) {
            GAlerter.lab("lineItemList Exception :", e);
        }

        return new ModelAndView("/viewline/tab_listlineitem", mapMsg);
    }


    @RequestMapping(value = "/preaddlineitem")
    public ModelAndView preaddlineitem(
            @RequestParam(value = "lineId", required = false) int lineId) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        ViewLine line = null;
        try {
            line = viewLineWebLogic.getLineById(lineId);

            mapMsg.put("line", line);
        } catch (ServiceException e) {
            GAlerter.lab("preaddlineitem Exception :", e);
        }

        return new ModelAndView("/viewline/tab_preaddlineitem", mapMsg);
    }

    @RequestMapping(value = "/addlineitem")
    public ModelAndView addLineItem(
            @RequestParam(value = "step", required = false, defaultValue = "0") int step,
            @RequestParam(value = "validStatus", required = false) String validStatus,
            @RequestParam(value = "lineId", required = true) int lineId,
            @RequestParam(value = "itemDesc", required = false) String itemDesc,
            @RequestParam(value = "srcId1", required = false) String srcId1,
            @RequestParam(value = "srcId2", required = false) String srcId2,
            @RequestParam(value = "srcId3", required = false) String srcId3,
            @RequestParam(value = "srcId4", required = false) String srcId4,
            @RequestParam(value = "customUrl", required = false) String customUrl,
            @RequestParam(value = "customSubject", required = false) String customSubject,
            @RequestParam(value = "customIcon", required = false) String customIcon,
            @RequestParam(value = "customIcon2", required = false) String customIcon2,
            @RequestParam(value = "customDesc", required = false) String customDesc,
            @RequestParam(value = "extrafield1", required = false) String extraField1,
            @RequestParam(value = "extrafield2", required = false) String extraField2,
            @RequestParam(value = "extrafield3", required = false) String extraField3,
            @RequestParam(value = "extrafield4", required = false) String extraField4,
            @RequestParam(value = "extrafield5", required = false) String extraField5,
            @RequestParam(value = "scale", required = false) String scale,
            @RequestParam(value = "img", required = false) String img

    )

    {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();

        mapMsg.put("scale", scale);
        mapMsg.put("img", img);

        //
        Map<String, String> lineItemKeyValues = new HashMap<String, String>();

        //
        lineItemKeyValues.put("srcId1", srcId1);
        lineItemKeyValues.put("srcId2", srcId2);
        lineItemKeyValues.put("srcId3", srcId3);
        lineItemKeyValues.put("srcId4", srcId4);

        mapMsg.putAll(lineItemKeyValues);

        //
        ViewLine line = null;
        ViewLineItem lineItem = null;

        try {
            //
            line = viewLineWebLogic.getLineById(lineId);

            mapMsg.put("line", line);

            //
            lineItem = viewLineWebLogic.generateAddLineItem(line.getItemType(), lineItemKeyValues, errorMsgMap, mapMsg);

            if (line != null) {
                lineItem.setCategoryAspect(line.getCategoryAspect());
                lineItem.setCategoryId(line.getCategoryId());

            }

            if (!StringUtil.isEmpty(validStatus) && ValidStatus.INVALID.getCode().equals(validStatus)) {
                lineItem.setValidStatus(ValidStatus.INVALID);
            }

            //
            if (!StringUtil.isEmpty(customUrl)) {
                lineItem.getDisplayInfo().setLinkUrl(customUrl);
            }
            if (!StringUtil.isEmpty(customSubject)) {
                lineItem.getDisplayInfo().setSubject(customSubject);
            }
            if (!StringUtil.isEmpty(customIcon)) {
                lineItem.getDisplayInfo().setIconUrl(customIcon);
            }
            if (!StringUtil.isEmpty(customIcon2)) {
                lineItem.getDisplayInfo().setIconUrl2(customIcon2);
            }
            if (!StringUtil.isEmpty(customDesc)) {
                lineItem.getDisplayInfo().setDesc(customDesc);
            }

            if (!StringUtil.isEmpty(extraField1)) {
                lineItem.getDisplayInfo().setExtraField1(extraField1);
            }
            if (!StringUtil.isEmpty(extraField2)) {
                lineItem.getDisplayInfo().setExtraField2(extraField2);
            }
            if (!StringUtil.isEmpty(extraField3)) {
                lineItem.getDisplayInfo().setExtraField3(extraField3);
            }
            if (!StringUtil.isEmpty(extraField4)) {
                lineItem.getDisplayInfo().setExtraField4(extraField4);
            }
            if (!StringUtil.isEmpty(extraField5)) {
                lineItem.getDisplayInfo().setExtraField5(extraField5);
            }

            //
            lineItem.setCreateDate(new Date());
            lineItem.setCreateUno(this.getCurrentUser().getUno().toString());
            lineItem.setLineId(line.getLineId());
            lineItem.setItemDesc(itemDesc);

            mapMsg.put("srcId1", srcId1);
            mapMsg.put("srcId2", srcId2);
            mapMsg.put("lineItem", lineItem);


            if (step == 1 && line.getItemType().equals(ViewItemType.CONTENT)) {

                String contentId = lineItem.getDirectId();
                String profileUno = lineItem.getDirectUno();

                Content content = ContentServiceSngl.get().getContentById(contentId);

                mapMsg.put("content", content);

//                return new ModelAndView("/viewline/tab_preaddlineitem_imgchoice", mapMsg);
                return new ModelAndView("/viewline/tab_preaddlineitem_content2", mapMsg);

//                return new ModelAndView("/viewline/tab_preaddlineitem_content2", mapMsg);
            } else if (step == 2) {
                return new ModelAndView("/viewline/tab_preaddlineitem_content2", mapMsg);
            }

            //
            if (errorMsgMap.isEmpty()) {
                //
                lineItem.setItemId(viewLineWebLogic.addLineItem(lineItem).getItemId());
            }
        } catch (ServiceException e) {
            GAlerter.lab("addLineItem error:", e);

            try {
                RangeRows<ViewLineItem> rangeRows = viewLineWebLogic.queryLineItems(new QueryExpress()
                        .add(QueryCriterions.eq(ViewLineItemField.DIRECTID, lineItem.getDirectId()))
                        .add(QueryCriterions.eq(ViewLineItemField.LINEID, line.getLineId())), new Rangination(1));

                if (rangeRows != null && rangeRows.getRows().size() > 0) {
                    lineItem = rangeRows.getRows().get(0);
                    mapMsg.put("lineItem", lineItem);
                }
            } catch (ServiceException e1) {
                GAlerter.lab("query ViewLineItem caught an exception: " + e);
            }

            if (e.getCause() instanceof MySQLIntegrityConstraintViolationException) {
                if (StringUtil.isEmpty(customSubject) && line.getItemType().equals(ViewItemType.PROFILE)) {
                    lineItem.getDisplayInfo().setSubject(null);
                }
                if (StringUtil.isEmpty(customDesc) && line.getItemType().equals(ViewItemType.PROFILE)) {
                    lineItem.getDisplayInfo().setDesc(null);
                }
                errorMsgMap.put("duplicateEntry", "error.viewline.item.duplicate");

            } else {
                errorMsgMap.put("system", "error.exception");
            }

        }

        //return confirm.
        if (errorMsgMap.isEmpty() && line != null) {
//            if (ViewItemType.CONTENT.equals(line.getItemType())) {
//                return new ModelAndView("redirect:/viewline/predisplayinfoadd?ItemId=" + lineItem.getItemId());
//            } else {
            return new ModelAndView("redirect:/viewline/listlineitem?lineId=" + line.getLineId());
//            }

        } else {
            //
            mapMsg.put("errorMsgMap", errorMsgMap);

            //
            return new ModelAndView("/viewline/tab_preaddlineitem", mapMsg);
        }
    }

    @RequestMapping(value = "/predisplayinfoadd")
    public ModelAndView perAddLineItemDisplayInfo(@RequestParam(value = "ItemId", required = true) long ItemId) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();

        //
        ViewLineItem lineItem = null;

        try {
            lineItem = viewLineWebLogic.getLineItemByItemId(ItemId);

            mapMsg.put("lineItem", lineItem);

        } catch (ServiceException e) {

            GAlerter.lab("preAddLineItemDisplayInfo error: " + e);
            errorMsgMap.put("system", "error.exception");

            return new ModelAndView("");
        }

        return new ModelAndView("/viewline/tab_preaddlineitem_content2", mapMsg);
    }

    @RequestMapping(value = "/displayinfoadd")
    public ModelAndView addLineItemDisplayInfo(@RequestParam(value = "ItemId", required = true) long ItemId,
                                               @RequestParam(value = "customUrl", required = false) String customUrl,
                                               @RequestParam(value = "customSubject", required = false) String customSubject,
                                               @RequestParam(value = "customIcon", required = false) String customIcon,
                                               @RequestParam(value = "customDesc", required = false) String customDesc) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();

        //
        ViewLineItem lineItem = null;

        mapMsg.put("ItemId", ItemId);
        mapMsg.put("customUrl", customUrl);
        mapMsg.put("customSubject", customSubject);
        mapMsg.put("customIcon", customIcon);
        mapMsg.put("customDesc", customDesc);

        try {
            lineItem = viewLineWebLogic.getLineItemByItemId(ItemId);

            //
            ViewLineItemDisplayInfo viewLineItemDisplayInfo = new ViewLineItemDisplayInfo();
            if (!StringUtil.isEmpty(customUrl)) {
                viewLineItemDisplayInfo.setLinkUrl(customUrl);
            }
            if (!StringUtil.isEmpty(customSubject)) {
                viewLineItemDisplayInfo.setSubject(customSubject);
            }
            if (!StringUtil.isEmpty(customIcon)) {
                viewLineItemDisplayInfo.setIconUrl(customIcon);
            }
            if (!StringUtil.isEmpty(customDesc)) {
                viewLineItemDisplayInfo.setDesc(customDesc);
            }

            lineItem.setDisplayInfo(viewLineItemDisplayInfo);

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ViewLineItemField.DISPLAYINFO, viewLineItemDisplayInfo.toJson());

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ViewLineItemField.ITEMID, Long.valueOf(ItemId)));

            viewLineWebLogic.modifyLineItem(updateExpress, queryExpress);

        } catch (ServiceException e) {

            GAlerter.lab("addLineItemDisplayInfo error: " + e);
            errorMsgMap.put("system", "error.exception");

            return new ModelAndView("");
        }

        return new ModelAndView("redirect:/viewline/listlineitem?lineId=" + lineItem.getLineId());
    }


    @RequestMapping(value = "/batchstatuslineitems")
    public ModelAndView batchStatusLineItems(
            @RequestParam(value = "lineId", required = true) int lineId,
            @RequestParam(value = "lineItemIds", required = false) String lineItemIds,
            @RequestParam(value = "validStatusCode", required = true) String validStatusCode,
            @RequestParam(value = "pager.offset", required = false) int pageStartIdx) {
        //
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ViewLineItemField.VALIDSTATUS, ValidStatus.getByCode(validStatusCode).getCode());

        try {
            if (!Strings.isNullOrEmpty(lineItemIds)) {
                String[] lineItemIdSplits = lineItemIds.split(",");
                for (String lineItemId : lineItemIdSplits) {
                    //
                    QueryExpress queryExpress = new QueryExpress();
                    queryExpress.add(QueryCriterions.eq(ViewLineItemField.ITEMID, Long.valueOf(lineItemId)));

                    //
                    viewLineWebLogic.modifyLineItem(updateExpress, queryExpress);
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab("batchStatusLineItems a lineitem ServiceException :", e);
        }

        return new ModelAndView("redirect:/viewline/listlineitem?lineId=" + lineId + "&pager.offset=" + pageStartIdx);
    }

    @RequestMapping(value = "/preeditlineitem")
    public ModelAndView preEditLineItem(
            @RequestParam(value = "lineItemId", required = true) long lineItemId) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        ViewLineItem lineItem = null;
        ViewLine viewLine = null;

        try {
            lineItem = viewLineWebLogic.getLineItemByItemId(lineItemId);
            viewLine = viewLineWebLogic.getLineById(lineItem.getLineId());

            String contentId = lineItem.getDirectId();
            String profileUno = lineItem.getDirectUno();

            Content content = ContentServiceSngl.get().getContentById(contentId);

            mapMsg.put("content", content);
        } catch (ServiceException e) {
            GAlerter.lab("presortlineitem a Controller ServiceException :", e);
        }

        //
        mapMsg.put("lineItem", lineItem);
        mapMsg.put("line", viewLine);

        //
        mapMsg.put("validStatuses", validStatus);

        //
        return new ModelAndView("/viewline/tab_preeditlineitem", mapMsg);
    }

    @RequestMapping(value = "/editlineitem")
    public ModelAndView editLineItem(
            @RequestParam(value = "lineId", required = true) int lineId,
            @RequestParam(value = "lineItemId", required = true) long lineItemId,
            @RequestParam(value = "displayOrder", required = true) int displayOrder,
            @RequestParam(value = "validStatusCode", required = false) String validStatusCode,
            @RequestParam(value = "displyType", required = false) String displyType,
            @RequestParam(value = "itemDesc", required = false) String itemDesc,
            @RequestParam(value = "customUrl", required = false) String customUrl,
            @RequestParam(value = "customSubject", required = false) String customSubject,
            @RequestParam(value = "customIcon", required = false) String customIcon,
           @RequestParam(value = "customIcon2", required = false) String customIcon2,
            @RequestParam(value = "customDesc", required = false) String customDesc,
            @RequestParam(value = "extrafield1", required = false) String extraField1,
            @RequestParam(value = "extrafield2", required = false) String extraField2,
            @RequestParam(value = "extrafield3", required = false) String extraField3,
            @RequestParam(value = "extrafield4", required = false) String extraField4,
            @RequestParam(value = "extrafield5", required = false) String extraField5,
            @RequestParam(value = "icontype", required = false) String iconType) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();

        //
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ViewLineItemField.ITEMID, lineItemId));

        //
        UpdateExpress updateExpress = new UpdateExpress();

        updateExpress.set(ViewLineItemField.DISPLAYORDER, displayOrder);
        updateExpress.set(ViewLineItemField.ITEMDESC, itemDesc);

        if (ValidStatus.getByCode(validStatusCode) != null) {
            updateExpress.set(ViewLineItemField.VALIDSTATUS, ValidStatus.getByCode(validStatusCode).getCode());
        }

        ViewLineItem viewLineItem = null;

        try {
            viewLineItem = viewLineWebLogic.getLineItemByItemId(lineItemId);
        } catch (ServiceException e) {
            GAlerter.lab("get LineItem by Item id occurred a exception:" + e);
            viewLineItem = new ViewLineItem();
        }

        int displyTypeValue = 0;

        if (!StringUtil.isEmpty(displyType)) {

            if (!viewLineItem.getDisplayType().isTop() && "top".equals(displyType)) {

                displyTypeValue = ViewLineItemDisplayType.TOP;
//                try {
//                    RangeRows<ViewLineItem> rangeRows = viewLineWebLogic.queryLineItems(
//                            new QueryExpress()
//                                    .add(QueryCriterions.eq(ViewLineItemField.LINEID, lineId))
//                                    .add(QueryCriterions.bitwiseAnd(ViewLineItemField.DISPLAYTYPE, QueryCriterionRelation.GT, displyTypeValue, 0))
//                            , new Rangination(0, Integer.MAX_VALUE));
//
//                    if(rangeRows.getRows().size() >= 2){
//                        errorMsgMap.put("topge2", "置顶的元素已经超过2个，请先取消至少一个置顶元素");
//                    }
//                } catch (ServiceException e) {
//                    GAlerter.lab("queryLineItems occurred a ServiceException :", e);
//                }
            } else if (viewLineItem.getDisplayType().isTop() && "notop".equals(displyType)) {
                displyTypeValue = 0 - ViewLineItemDisplayType.TOP;
            } else if (!viewLineItem.getDisplayType().isHot() && "hot".equals(displyType)) {
                displyTypeValue = ViewLineItemDisplayType.HOT;
            } else if (!viewLineItem.getDisplayType().isNew() && "new".equals(displyType)) {
                displyTypeValue = ViewLineItemDisplayType.NEW;
            }

            updateExpress.increase(ViewLineItemField.DISPLAYTYPE, displyTypeValue);


        } else {
            updateExpress.set(ViewLineItemField.DISPLAYTYPE, displyTypeValue);
        }

        ViewLineItemDisplayInfo viewLineItemDisplayInfo = new ViewLineItemDisplayInfo();

        if (!StringUtil.isEmpty(customUrl)) {
            viewLineItemDisplayInfo.setLinkUrl(customUrl);
        }
        if (!StringUtil.isEmpty(customSubject)) {
            viewLineItemDisplayInfo.setSubject(customSubject);
        }
        if (!StringUtil.isEmpty(customIcon)) {
            viewLineItemDisplayInfo.setIconUrl(customIcon);
        }
        if (!StringUtil.isEmpty(customIcon2)) {
            viewLineItemDisplayInfo.setIconUrl2(customIcon2);
        }
        if (!StringUtil.isEmpty(customDesc)) {
            viewLineItemDisplayInfo.setDesc(customDesc);
        }
        if (!StringUtil.isEmpty(extraField1)) {
            viewLineItemDisplayInfo.setExtraField1(extraField1);
        }
        if (!StringUtil.isEmpty(extraField2)) {
            viewLineItemDisplayInfo.setExtraField2(extraField2);
        }
        if (!StringUtil.isEmpty(extraField3)) {
            viewLineItemDisplayInfo.setExtraField3(extraField3);
        }
        if (!StringUtil.isEmpty(extraField4)) {
            viewLineItemDisplayInfo.setExtraField4(extraField4);
        }
        if (!StringUtil.isEmpty(extraField5)) {
            viewLineItemDisplayInfo.setExtraField5(extraField5);
        }
        if (!StringUtil.isEmpty(iconType)) {
            viewLineItemDisplayInfo.setExtraField5(extraField5);
        }

        updateExpress.set(ViewLineItemField.DISPLAYINFO, viewLineItemDisplayInfo.toJson());

        //
        if (errorMsgMap.size() > 0) {
            //
            mapMsg.put("validStatuses", validStatus);

            //put the error msg to mapMsg
            mapMsg.put("errorMsgMap", errorMsgMap);

            mapMsg.put("lineItemId", lineItemId);

            //
            return new ModelAndView("forward:/viewline/preeditlineitem", mapMsg);
        } else {
            //
            boolean bool = false;
            try {

                bool = viewLineWebLogic.modifyLineItem(updateExpress, queryExpress);

                if (bool) {
                    ToolsLog log = new ToolsLog();

                    log.setOpUserId(getCurrentUser().getUserid());
                    log.setOperType(LogOperType.LINEITEM_SORTLINEITEM);
                    log.setOpTime(new Date());
                    log.setOpIp(getIp());
                    log.setSrcId(String.valueOf(displayOrder));

                    addLog(log);
                } else {
                    mapMsg.put("system", "error.exception");
                }
            } catch (ServiceException e) {
                GAlerter.lab("removeLineItem a lineitem ServiceException :", e);

                mapMsg.put("system", "error.exception");
            }

            return new ModelAndView("redirect:/viewline/listlineitem?lineId=" + lineId, mapMsg);
        }
    }

    @RequestMapping(value = "/removelineitem")
    public ModelAndView removeLineItem(
            @RequestParam(value = "lineId", required = true) int lineId,
            @RequestParam(value = "lineItemId", required = true) int lineItemId) {

        try {
            viewLineWebLogic.removeLineItem(lineId, lineItemId);
        } catch (ServiceException e) {
            //
            GAlerter.lab("removeLineItem, ServiceException :", e);
        }

        return new ModelAndView("redirect:/viewline/listlineitem?lineId=" + lineId);
    }


    @RequestMapping(value = "/listprivacy")
    public ModelAndView listPrivace(@RequestParam(value = "categoryId", required = true, defaultValue = "0") int categoryId) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        try {

            if (categoryId != 0) {
                List<ViewCategoryPrivacy> privacyList = ViewLineServiceSngl.get().queryCategoryPrivacies(new QueryExpress().add(QueryCriterions.eq(ViewCategoryPrivacyField.CATEGORYID, categoryId)));
                List<ViewCategoryPrivacyDTO> dtoList = new ArrayList<ViewCategoryPrivacyDTO>();

                for (ViewCategoryPrivacy categoryPrivacy : privacyList) {
                    ViewCategoryPrivacyDTO privacyDTO = new ViewCategoryPrivacyDTO();

                    ProfileBlog profileBlog = ProfileServiceSngl.get().getProfileBlogByUno(categoryPrivacy.getUno());

                    privacyDTO.setProfileBlog(profileBlog);
                    privacyDTO.setCategoryPrivacy(categoryPrivacy);

                    dtoList.add(privacyDTO);
                }

                mapMsg.put("privacies", dtoList);
                mapMsg.put("categoryId", categoryId);

            }

        } catch (ServiceException e) {
            //
            GAlerter.lab("listprivacy a Controller ServiceException :", e);
        }

        return new ModelAndView("/viewline/tab_privacy", mapMsg);
    }


    @RequestMapping(value = "/privacybatchstatus")
    public ModelAndView batchStatusPrivacy(@RequestParam(value = "unos", required = false) String unos,
                                           @RequestParam(value = "categoryId", required = false) int categoryId) {

        Map<String, Object> mapMsg = new HashMap<String, Object>();
        mapMsg.put("categoryId", categoryId);

        try {
            if (!StringUtil.isEmpty(unos)) {
                String[] unoArray = unos.split(",");

                QueryExpress queryExpress = new QueryExpress();

                for (String uno : unoArray) {
                    queryExpress.add(QueryCriterions.eq(ViewCategoryPrivacyField.CATEGORYID, categoryId))
                            .add(QueryCriterions.eq(ViewCategoryPrivacyField.UNO, uno));

                    ViewLineServiceSngl.get().deleteCategoryPrivacy(queryExpress);

                    if (!mapMsg.containsKey("categoryId")) {
                        mapMsg.put("categoryId", categoryId);
                    }

                }
            }
        } catch (ServiceException e) {
            GAlerter.lab("batch delete privacy, ServiceException :", e);
        }

        return new ModelAndView("forward:/viewline/listprivacy", mapMsg);

    }


    @RequestMapping(value = "/presetprivacy")
    public ModelAndView preSetPrivacy(@RequestParam(value = "categoryId", required = true) int categoryId) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        mapMsg.put("privacyLevels", ViewCategoryPrivacyLevel.getAll());

        mapMsg.put("categoryId", categoryId);

        return new ModelAndView("/viewline/tab_preaddprivacy", mapMsg);
    }

    @RequestMapping(value = "/setprivacy")
    public ModelAndView setPrivacy(@RequestParam(value = "categoryId", required = true, defaultValue = "0") int categoryId,
                                   @RequestParam(value = "privacy", required = false) String privacy,
                                   @RequestParam(value = "editor", required = false) String screenName) {

        //
        Map<String, Object> msgMap = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();

        ProfileBlog profileBlog = null;
        ViewCategoryPrivacy categoryPrivacy = new ViewCategoryPrivacy();

        try {
            profileBlog = ProfileServiceSngl.get().getProfileBlogByScreenName(screenName);

            if (profileBlog != null) {

                if (ProfileDomain.EF_EDITOR.equals(profileBlog.getProfileDomain())) {
                    //
                    categoryPrivacy.setCategoryId(categoryId);
                    categoryPrivacy.setUno(profileBlog.getUno());
                    categoryPrivacy.setCreateDate(new Date());
                    categoryPrivacy.setCreateUserid(getCurrentUser().getUserid());
                    categoryPrivacy.setPrivacyLevel(ViewCategoryPrivacyLevel.getByCode(StringUtil.isEmpty(privacy) ? "one" : privacy));

                    categoryPrivacy = ViewLineServiceSngl.get().createCategoryPrivacy(categoryPrivacy);

                    //todo log
                    return new ModelAndView("forward:/viewline/listprivacy", msgMap);
                } else {
                    errorMsgMap.put("noteditor", "error.viewline.category.privacy.editor.notexist");
                }

            } else {
                errorMsgMap.put("notprofile", "error.viewline.category.privacy.editor.notuser");
            }

        } catch (ServiceException e) {
            GAlerter.lab("setPrivacy caught an Exception:", e);
            errorMsgMap.put("noteditor", "error.viewline.category.privacy.editor.duplicate");
        }


        msgMap.put("errorMsgMap", errorMsgMap);
        msgMap.put("privacyLevels", ViewCategoryPrivacyLevel.getAll());
        msgMap.put("categoryId", categoryId);
        msgMap.put("editor", screenName);

        return new ModelAndView("/viewline/tab_preaddprivacy", msgMap);
    }

    @RequestMapping(value = "/preeditprivacy")
    public ModelAndView preEditPrivacy(@RequestParam(value = "categoryId", required = false, defaultValue = "0") int categoryId,
                                       @RequestParam(value = "uno", required = false) String uno) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        ViewCategoryPrivacy viewCategoryPrivacy = null;

        ViewCategoryPrivacyDTO categoryPrivacyDTO = new ViewCategoryPrivacyDTO();

        if (!StringUtil.isEmpty(uno)) {
            try {
                viewCategoryPrivacy = ViewLineServiceSngl.get().getCategoryPrivacy(new QueryExpress()
                        .add(QueryCriterions.eq(ViewCategoryPrivacyField.CATEGORYID, categoryId))
                        .add(QueryCriterions.eq(ViewCategoryPrivacyField.UNO, uno)));

                categoryPrivacyDTO.setCategoryPrivacy(viewCategoryPrivacy);

                categoryPrivacyDTO.setProfileBlog(ProfileServiceSngl.get().getProfileBlogByUno(uno));

            } catch (ServiceException e) {

                GAlerter.lab("pre editPrivacy caught an Exception:", e);
            }
        }


        mapMsg.put("privacyLevels", ViewCategoryPrivacyLevel.getAll());
        mapMsg.put("privacy", categoryPrivacyDTO);
        mapMsg.put("categoryId", categoryId);
        mapMsg.put("uno", uno);

        return new ModelAndView("/viewline/tab_preeditprivacy", mapMsg);
    }

    @RequestMapping(value = "/editprivacy")
    public ModelAndView editPrivacy(@RequestParam(value = "privacy", required = false) String privacy,
                                    @RequestParam(value = "categoryId", required = false, defaultValue = "0") int categoryId,
                                    @RequestParam(value = "uno", required = false) String uno) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();

        if (!StringUtil.isEmpty(privacy)) {

            updateExpress.set(ViewCategoryPrivacyField.PRIVACYLEVEL, privacy);
            updateExpress.set(ViewCategoryPrivacyField.UPDATEDATE, new Date());
            updateExpress.set(ViewCategoryPrivacyField.UPDATEUSERID, getCurrentUser().getUserid());
        }
        if (!StringUtil.isEmpty(uno)) {
            queryExpress.add(QueryCriterions.eq(ViewCategoryPrivacyField.UNO, uno));
        }
        queryExpress.add(QueryCriterions.eq(ViewCategoryPrivacyField.CATEGORYID, categoryId));

        boolean bool = false;
        try {
            bool = ViewLineServiceSngl.get().modifyCategoryPrivacy(updateExpress, queryExpress);

            if (bool) {
                //todo log
            }
        } catch (ServiceException e) {

            GAlerter.lab("editPrivacy caught an Exception:", e);
        }

        return new ModelAndView("forward:/viewline/listprivacy", mapMsg);

    }


    @RequestMapping(value = "/cropper")
    public ModelAndView imgCropper(@RequestParam(value = "scale", required = false) String scale,
                                   @RequestParam(value = "img", required = false) String img,
                                   @RequestParam(value = "lineId", required = false) int lineId,
                                   @RequestParam(value = "itemDesc", required = false) String itemDesc,
                                   @RequestParam(value = "step", required = false) int step,
                                   @RequestParam(value = "validStatus", required = false) String validStatus,
                                   @RequestParam(value = "srcId1", required = false) String srcId1,
                                   @RequestParam(value = "srcId2", required = false) String srcId2,
                                   @RequestParam(value = "srcId3", required = false) String srcId3,
                                   @RequestParam(value = "srcId4", required = false) String srcId4) {

        Map<String, Object> mapMsg = new HashMap<String, Object>();

        if (!StringUtil.isEmpty(scale)) {
            mapMsg.put("scale", scale);
        }
        if (!StringUtil.isEmpty(img)) {
            mapMsg.put("img", img);
        }
        if (lineId != 0) {
            mapMsg.put("lineId", lineId);
        }
        if (!StringUtil.isEmpty(itemDesc)) {
            mapMsg.put("itemDesc", itemDesc);
        }
        if (!StringUtil.isEmpty(srcId1)) {
            mapMsg.put("srcId1", srcId1);
        }
        if (step != 2) {
            mapMsg.put("step", step);
        }
        if (!StringUtil.isEmpty(srcId2)) {
            mapMsg.put("srcId2", srcId2);
        }
        if (!StringUtil.isEmpty(srcId3)) {
            mapMsg.put("srcId3", srcId3);
        }
        if (!StringUtil.isEmpty(srcId4)) {
            mapMsg.put("srcId4", srcId4);
        }
        if (!StringUtil.isEmpty(validStatus)) {
            mapMsg.put("validStatus", validStatus);
        }

        return new ModelAndView("forward:/viewline/addlineitem", mapMsg);
    }


    @RequestMapping(value = "/copypage")
    public ModelAndView copyLinePage(@RequestParam(value = "lineId", required = true) int lineId) {
        Map<String, Object> msgMap = new HashMap<String, Object>();

        msgMap.put("lineId", lineId);

        return new ModelAndView("/viewline/tab_copylinepage", msgMap);
    }

    @RequestMapping(value = "/dropimg")
    public ModelAndView dropImage(@RequestParam(value = "lineItemId", required = true) String lineItemId) {
        Map<String, Object> msgMap = new HashMap<String, Object>();

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ViewLineItemField.ITEMID, Long.parseLong(lineItemId)));

        ViewLineItem viewLineItem = null;
        try {

            viewLineItem = ViewLineServiceSngl.get().getLineItem(queryExpress);

            viewLineItem.getDisplayInfo().setIconUrl(null);

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ViewLineItemField.DISPLAYINFO, viewLineItem.getDisplayInfo().toJson());

            ViewLineServiceSngl.get().modifyLineItem(updateExpress, queryExpress);

        } catch (ServiceException e) {
            GAlerter.lab("drop viewlineitem image occurred an exception: ", e);
        }

        return new ModelAndView("redirect:/viewline/linedetail?lineId=" + viewLineItem.getLineId());
    }

    @RequestMapping(value = "/copyline")
    public ModelAndView copyLine(@RequestParam(value = "lineId", required = true) int lineId,
                                 @RequestParam(value = "targetLineId", required = true) int targetLineId) {
        Map<String, Object> msgMap = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();

        msgMap.put("errorMsgMap", errorMsgMap);
        msgMap.put("lineId", lineId);
        msgMap.put("targetLineId", targetLineId);

        int duplicatingNumber = 0;
        RangeRows<ViewLine> rangeRows = null;

        try {
            rangeRows = ViewLineServiceSngl.get().queryViewLinesByRange(new QueryExpress().add(QueryCriterions.eq(ViewLineField.LINEID, targetLineId)),
                    new Rangination(1));

        } catch (ServiceException e) {
            GAlerter.lab("query an Viewline occurred an exception: ", e);

        }

        if (rangeRows.getRows().size() != 0) {
            ViewLine viewLine = rangeRows.getRows().get(0);

            List<ViewLineItem> list = null;
            try {
                list = ViewLineServiceSngl.get().queryLineItems(new QueryExpress().add(QueryCriterions.eq(ViewLineItemField.LINEID, lineId)));

            } catch (ServiceException e) {
                GAlerter.lab("query viewline items occurred an exception: ", e);
            }

            int categoryId = 0;

            for (ViewLineItem viewLineItem : list) {

                categoryId = viewLineItem.getCategoryId();

                viewLineItem.setLineId(targetLineId);
                viewLineItem.setCreateDate(new Date());
                viewLineItem.setCategoryId(viewLine.getCategoryId());
                viewLineItem.setCategoryAspect(viewLine.getCategoryAspect());

                try {
                    ViewLineServiceSngl.get().addLineItem(viewLineItem);
                } catch (ServiceException e) {
                    GAlerter.lab("insert into a viewline item occurred an exception: ", e);

                    if (e.getCause() instanceof MySQLIntegrityConstraintViolationException) {
                        duplicatingNumber++;
                    }
                }
            }

            msgMap.put("categoryId", categoryId);

        } else {
            errorMsgMap.put("targetLineId", "error.viewline.line.lineid.notexist");

            return new ModelAndView("/viewline/tab_copylinepage", msgMap);
        }


        msgMap.put("failure", duplicatingNumber);

        //todo log

        return new ModelAndView("/viewline/tab_copysuccesspage", msgMap);
    }
}
