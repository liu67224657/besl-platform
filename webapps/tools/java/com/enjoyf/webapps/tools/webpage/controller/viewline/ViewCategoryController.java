package com.enjoyf.webapps.tools.webpage.controller.viewline;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ViewLineHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.viewline.ViewCategory;
import com.enjoyf.platform.service.viewline.ViewCategoryAspect;
import com.enjoyf.platform.service.viewline.ViewCategoryDisplaySetting;
import com.enjoyf.platform.service.viewline.ViewCategoryField;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.viewline.ViewLineContentWebDataProcessor;
import com.enjoyf.platform.webapps.common.viewline.ViewLineWebDataProcessor;
import com.enjoyf.webapps.tools.weblogic.viewline.ViewLineWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.common.base.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: taijunli
 * Date: 2012-2-21
 * Time: 22:25:52
 */
@Controller
@RequestMapping(value = "/viewline")
public class ViewCategoryController extends ToolsBaseController {

    private static final int CATEGORY_PAGE_SIZE = 5;

    //
    @Resource(name = "viewLineWebLogic")
    private ViewLineWebLogic viewLineWebLogic;

    private static List<ActStatus> publishStatus = new ArrayList<ActStatus>();
    private static List<ValidStatus> validStatus = new ArrayList<ValidStatus>();

    static {
        validStatus.add(ValidStatus.VALID);
        validStatus.add(ValidStatus.REMOVED);

        publishStatus.add(ActStatus.UNACT);
        publishStatus.add(ActStatus.ACTED);
    }

    //
    private ViewLineHotdeployConfig hotdeployConfig = HotdeployConfigFactory.get().getConfig(ViewLineHotdeployConfig.class);
    private ViewLineWebDataProcessor viewCategoryWebDataProcessor = new ViewLineContentWebDataProcessor();

    @RequestMapping(value = "/categorylist")
    public ModelAndView queryCategoryList(
            @RequestParam(value = "aspectCode", required = false) String aspectCode,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "items", required = false, defaultValue = "0") int items,                   //总记录数
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,      //数据库记录索引
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        Pagination pagination = new Pagination(items, (pagerOffset / maxPageItems) + 1, CATEGORY_PAGE_SIZE);

        //
        ViewCategoryAspect aspect = ViewCategoryAspect.getByCode(aspectCode);
        if (aspect == null) {
            aspect = ViewCategoryAspect.CONTENT_GAME;
            aspectCode = aspect.getCode();
        }

        //
        mapMsg.put("aspectCode", aspectCode);
        mapMsg.put("categoryName", categoryName);

        //the select option list.
        mapMsg.put("aspects", ViewCategoryAspect.getAll());

        try {
            PageRows<ViewCategory> categories = viewLineWebLogic.queryCategoryTree(aspect, categoryName, pagination);

            //
            mapMsg.put("rows", categories.getRows());
            mapMsg.put("page", categories.getPage());
            mapMsg.put("validStatuses", validStatus);
            mapMsg.put("publishStatuses", publishStatus);

        } catch (ServiceException e) {
            GAlerter.lab("queryCategoryList a Controller ServiceException :", e);
        }

        return new ModelAndView("/viewline/categorylist", mapMsg);
    }

    @RequestMapping(value = "/categoryprecreate")
    public ModelAndView preCreateCategory(
            @RequestParam(value = "aspectCode", required = true) String aspectCode,
            @RequestParam(value = "parentCategoryId", required = false, defaultValue = "0") int parentCategoryId) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        //
        ViewCategory category = new ViewCategory();

        //
        category.setCategoryAspect(ViewCategoryAspect.getByCode(aspectCode));
        try {
            category.setParentCategory(viewLineWebLogic.getCategoryById(parentCategoryId));
        } catch (ServiceException e) {
            GAlerter.lab("Call the viewLineWebLogic get category error.", e);
        }

        //
        mapMsg.put("category", category);

        //
        return new ModelAndView("/viewline/categoryprecreate", mapMsg);
    }

    @RequestMapping(value = "/categorycreate")
    public ModelAndView createCategory(
            @RequestParam(value = "aspectCode", required = true) String aspectCode,
            @RequestParam(value = "parentCategoryId", required = false, defaultValue = "0") int parentCategoryId,
            @RequestParam(value = "categoryCode", required = false) String categoryCode,
            @RequestParam(value = "locationCode", required = false) String locationCode,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "categoryDesc", required = false) String categoryDesc,
            @RequestParam(value = "seoKeyWord", required = false) String seoKeyWord,
            @RequestParam(value = "seoDesc", required = false) String seoDesc,
            @RequestParam(value = "iconURL", required = false)String iconURL,
            @RequestParam(value = "url", required = false)String url,
            @RequestParam(value = "subject", required = false)String subject,
            @RequestParam(value = "summary", required = false)String summary,
            @RequestParam(value = "extrafield1", required = false)String extraField1,
            @RequestParam(value = "extrafield2", required = false)String extraField2,
            @RequestParam(value = "extrafield3", required = false)String extraField3,
            @RequestParam(value = "extrafield4", required = false)String extraField4,
            @RequestParam(value = "extrafield5", required = false)String extraField5) {

        Map<String, Object> mapMsg = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();

        //
        ViewCategory category = new ViewCategory();
        ViewCategoryDisplaySetting displaySetting = new ViewCategoryDisplaySetting();

        //
        category.setParentCategoryId(parentCategoryId);
        category.setCategoryAspect(ViewCategoryAspect.getByCode(aspectCode));

        if (!Strings.isNullOrEmpty(categoryCode)) {
            ViewCategory viewCategory = null;
            //
            if(!categoryCode.matches("^[a-zA-Z0-9]*$")){
                errorMsgMap.put("categoryCodeMatch", "error.viewline.category.categorycode.notmatch");
            }
            try {
                viewCategory = viewLineWebLogic.getCategoryByCodeAndAspect(categoryCode, aspectCode);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            if(viewCategory != null){
                errorMsgMap.put("categoryCode", "error.viewline.category.categorycode.exist");
            }
            category.setCategoryCode(categoryCode);
        }
        if (!Strings.isNullOrEmpty(categoryName)) {
            category.setCategoryName(categoryName);
        } else{
            errorMsgMap.put("categoryName", "error.viewline.category.categoryname.notnull");
        }
        if (!Strings.isNullOrEmpty(categoryDesc)) {
            category.setCategoryDesc(categoryDesc);
        }
        if (!Strings.isNullOrEmpty(seoKeyWord)) {
            category.setSeoKeyWord(seoKeyWord);
        }
        if (!Strings.isNullOrEmpty(seoDesc)) {
            category.setSeoDesc(seoDesc);
        }
        if(!Strings.isNullOrEmpty(locationCode)){
            category.setLocationCode(locationCode);
        }

        //wrap the DisplaySetting
        if(!StringUtil.isEmpty(iconURL)){
            displaySetting.setIconURL(iconURL);
        }
        if(!StringUtil.isEmpty(url)){
            displaySetting.setUrl(url);
        }
        if(!StringUtil.isEmpty(subject)){
            displaySetting.setSubject(subject);
        }
        if(!StringUtil.isEmpty(summary)){
            displaySetting.setSummary(summary);
        }
        if(!StringUtil.isEmpty(extraField1)){
            displaySetting.setExtraField1(extraField1);
        }
        if(!StringUtil.isEmpty(extraField2)){
            displaySetting.setExtraField2(extraField2);
        }
        if(!StringUtil.isEmpty(extraField3)){
            displaySetting.setExtraField3(extraField3);
        }
        if(!StringUtil.isEmpty(extraField4)){
            displaySetting.setExtraField4(extraField4);
        }
        if(!StringUtil.isEmpty(extraField5)){
            displaySetting.setExtraField5(extraField5);
        }

        mapMsg.put("errorMsgMap", errorMsgMap);
        mapMsg.put("parentCategoryId", parentCategoryId);
        mapMsg.put("aspectCode", aspectCode);
        mapMsg.put("category", category);

        if(!errorMsgMap.isEmpty()){
            return new ModelAndView("forward:/viewline/categoryprecreate", mapMsg);
        }

        //
        category.setValidStatus(ValidStatus.VALID);
        category.setPublishStatus(ActStatus.UNACT);
        category.setDisplaySetting(displaySetting);

        //
        category.setCreateDate(new Date());
        category.setCreateUserid(String.valueOf(getCurrentUser().getUserid()));
        category.setDisplayOrder(Integer.MAX_VALUE - (int)(System.currentTimeMillis()/1000));

        try {
            category = viewLineWebLogic.saveCategory(category);
        } catch (ServiceException e) {
            GAlerter.lab("saveCategory a category Exception :", e);

            errorMsgMap.put("system", "error.exception");
            return new ModelAndView("forward:/viewline/categoryprecreate", mapMsg);
        }


//        ToolsLog log = new ToolsLog();
//
//        log.setOpUserId(getCurrentUser().getUserid());
//        log.setOperType(LogOperType.LINE_SAVELINE);
//        log.setOpTime(new Date());
//        log.setOpIp(getIp());
//        log.setSrcId(null);
//        log.setOpBefore(null);
//        log.setOpAfter(entity.toString());
//
//        addLog(log);

        return new ModelAndView("redirect:/viewline/categorydetail?categoryId=" + category.getCategoryId());
    }

    @RequestMapping(value = "/categorybatchstatus")
    @ResponseBody
    public ModelAndView batchStatusCategorys(
            @RequestParam(value = "categoryIds", required = false) String categoryIds,
            //
            @RequestParam(value = "aspectCode", required = false) String aspectCode,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "validStatusCode", required = false) String validStatusCode,
            @RequestParam(value = "publishStatusCode", required = false) String publishStatusCode,
            @RequestParam(value = "categoryTypeCode", required = false) String categoryTypeCode,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
            //
            @RequestParam(value = "updateValidStatusCode", required = false) String updateValidStatusCode,
            @RequestParam(value = "updatePublishStatusCode", required = false) String updatePublishStatusCode) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        mapMsg.put("lineName", categoryName);
        mapMsg.put("validStatusCode", validStatusCode);
        mapMsg.put("publishStatusCode", publishStatusCode);
        mapMsg.put("categoryTypeCode", categoryTypeCode);

        //
        UpdateExpress updateExpress = new UpdateExpress();

        if (ValidStatus.getByCode(updateValidStatusCode) != null) {
            updateExpress.set(ViewCategoryField.VALIDSTATUS, ValidStatus.getByCode(updateValidStatusCode).getCode());
        }
        if (ActStatus.getByCode(updatePublishStatusCode) != null) {
            updateExpress.set(ViewCategoryField.PUBLISHSTATUS, ActStatus.getByCode(updatePublishStatusCode).getCode());
        }

        updateExpress.set(ViewCategoryField.UPDATEDATE, new Date());
        updateExpress.set(ViewCategoryField.UPDATEUSERID, String.valueOf(getCurrentUser().getUserid()));

        //
        try {
            if (!Strings.isNullOrEmpty(categoryIds) && (ValidStatus.getByCode(updateValidStatusCode) != null || ActStatus.getByCode(updatePublishStatusCode) != null)) {
                //
                String[] categoryIdSplits = categoryIds.split(",");
                for (String categoryId : categoryIdSplits) {
                    //
                    QueryExpress queryExpress = new QueryExpress();
                    queryExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, Integer.valueOf(categoryId)));

                    //
                    viewLineWebLogic.modifyCategory(updateExpress, queryExpress);
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab("batchStatusCategorys, ServiceException :", e);
        }

        return new ModelAndView("forward:/viewline/categorylist", mapMsg);
    }

    @RequestMapping(value = "/categorypreedit")
    public ModelAndView preEditCategory(
            @RequestParam(value = "categoryId", required = true) int categoryId) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        //
        ViewCategory category = new ViewCategory();
        try {
            category = viewLineWebLogic.getCategoryById(categoryId);
        } catch (ServiceException e) {
            GAlerter.lab("preEditCategory a category ServiceException :", e);
        }

        //
        mapMsg.put("category", category);

        //
        mapMsg.put("validStatuses", validStatus);

        //
        return new ModelAndView("/viewline/categorypreedit", mapMsg);
    }

    @RequestMapping(value = "/categoryedit")
    public ModelAndView modifyCategory(
            @RequestParam(value = "categoryId", required = true) int categoryId,
            //
            @RequestParam(value = "categoryCode", required = true) String categoryCode,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "locationCode", required = false) String locationCode,
            @RequestParam(value = "categoryDesc", required = false) String categoryDesc,
            @RequestParam(value = "seoKeyWord", required = false) String seoKeyWord,
            @RequestParam(value = "seoDesc", required = false) String seoDesc,
            @RequestParam(value = "displayOrder", required = false, defaultValue = "0") int displayOrder,
            @RequestParam(value = "categoryTypeCode", required = false) String categoryTypeCode,
            @RequestParam(value = "templateCode", required = false) String templateCode,
            @RequestParam(value = "bgImageUrl", required = false) String bgImageUrl,
            @RequestParam(value = "iconURL", required = false)String iconURL,
            @RequestParam(value = "url", required = false)String url,
            @RequestParam(value = "subject", required = false)String subject,
            @RequestParam(value = "summary", required = false)String summary,
            @RequestParam(value = "extrafield1", required = false)String extraField1,
            @RequestParam(value = "extrafield2", required = false)String extraField2,
            @RequestParam(value = "extrafield3", required = false)String extraField3,
            @RequestParam(value = "extrafield4", required = false)String extraField4,
            @RequestParam(value = "extrafield5", required = false)String extraField5) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();

        ViewCategory category = new ViewCategory();

        category.setCategoryId(categoryId);
        category.setCategoryCode(categoryCode);
        category.setCategoryName(categoryName);
        category.setCategoryDesc(categoryDesc);
        category.setSeoDesc(seoDesc);
        category.setSeoKeyWord(seoKeyWord);
        category.setLocationCode(locationCode);

        //模板
        ViewCategoryDisplaySetting displaySetting = new ViewCategoryDisplaySetting();

        if (!Strings.isNullOrEmpty(bgImageUrl)) {
            displaySetting.setBgImageUrl(bgImageUrl);
        }
        if(!StringUtil.isEmpty(iconURL)){
            displaySetting.setIconURL(iconURL);
        }
        if(!StringUtil.isEmpty(url)){
            displaySetting.setUrl(url);
        }
        if(!StringUtil.isEmpty(subject)){
            displaySetting.setSubject(subject);
        }
        if(!StringUtil.isEmpty(summary)){
            displaySetting.setSummary(summary);
        }
        if(!StringUtil.isEmpty(extraField1)){
            displaySetting.setExtraField1(extraField1);
        }
        if(!StringUtil.isEmpty(extraField2)){
            displaySetting.setExtraField2(extraField2);
        }
        if(!StringUtil.isEmpty(extraField3)){
            displaySetting.setExtraField3(extraField3);
        }
        if(!StringUtil.isEmpty(extraField4)){
            displaySetting.setExtraField4(extraField4);
        }
        if(!StringUtil.isEmpty(extraField5)){
            displaySetting.setExtraField5(extraField5);
        }

        category.setDisplaySetting(displaySetting);

        //
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, categoryId));

        UpdateExpress updateExpress = new UpdateExpress();

        updateExpress.set(ViewCategoryField.CATEGORYCODE, category.getCategoryCode());
        updateExpress.set(ViewCategoryField.CATEGORYNAME, category.getCategoryName());
        updateExpress.set(ViewCategoryField.LOCATIONCODE, category.getLocationCode());
        updateExpress.set(ViewCategoryField.CATEGORYDESC, category.getCategoryDesc());
        updateExpress.set(ViewCategoryField.SEOKEYWORD, category.getSeoKeyWord());
        updateExpress.set(ViewCategoryField.SEODESC, category.getSeoDesc());
        updateExpress.set(ViewCategoryField.DISPLAYORDER, displayOrder);
        updateExpress.set(ViewCategoryField.DISPLAYSETTING, displaySetting.toJson());

        updateExpress.set(ViewCategoryField.UPDATEDATE, new Date());
        updateExpress.set(ViewCategoryField.UPDATEUSERID, String.valueOf(getCurrentUser().getUserid()));

        boolean bool = false;

        try {
            bool = viewLineWebLogic.modifyCategory(updateExpress, queryExpress);

        } catch (ServiceException e) {
            GAlerter.lab("modifyCategory a category ServiceException :", e);
        }

//        ToolsLog log = new ToolsLog();
//
//        log.setOpUserId(getCurrentUser().getUserid());
//        log.setOperType(LogOperType.USER_MODIFYUSERPAGE);
//        log.setOpTime(new Date());
//        log.setOpIp(getIp());
//        log.setSrcId(uno.toString());
//
//        try {
//            log.setOpAfter(privilegeWebLogic.getByUno(uno).toString());
//        } catch (ServiceException e) {
//            GAlerter.lab("modifyUserPage setOpAfter ServiceException :", e);
//        }

        mapMsg.put("category", category);

        //

//        addLog(log);
        return new ModelAndView("redirect:/viewline/categorydetail?categoryId=" + categoryId);
    }

    @RequestMapping(value = "/categorydetail")
    public ModelAndView categoryDetail(
            @RequestParam(value = "categoryId", required = true) int categoryId) {
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        ViewCategory category = new ViewCategory();

        try {
            category = viewLineWebLogic.getCategoryById(categoryId);

        } catch (ServiceException e) {
            GAlerter.lab("categoryDetail Exception :", e);
        }

        mapMsg.put("category", category);

        return new ModelAndView("/viewline/categorydetail", mapMsg);
    }

    @RequestMapping(value = "/listcategoryitem")
    public ModelAndView listCategoryItem(
            @RequestParam(value = "categoryId", required = true) int categoryId) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        //
        try {
            //get the category
            ViewCategory category = viewLineWebLogic.getCategoryById(categoryId);

            //
            mapMsg.put("category", category);

            //
        } catch (ServiceException e) {
            GAlerter.lab("saveLine setOpAfter ServiceException :", e);
        }

        return new ModelAndView("/viewcategory/tab_listcategoryitem", mapMsg);
    }

    @RequestMapping(value = "/preaddcategoryitem")
    public ModelAndView preAddCategoryItem(
            @RequestParam(value = "categoryId", required = true) int categoryId,
            @RequestParam(value = "templateLocationCode", required = false) String templateLocationCode) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        ViewCategory category = new ViewCategory();

        //
        try {
            //
            category = viewLineWebLogic.getCategoryById(categoryId);


        } catch (ServiceException e) {
            GAlerter.lab("preAddCategoryItem, Exception :", e);
        }

        //
        mapMsg.put("category", category);

        return new ModelAndView("/viewcategory/tab_preaddcategoryitem", mapMsg);
    }

    @RequestMapping(value = "/addcategoryitem")
    public ModelAndView addCategoryItem(
            @RequestParam(value = "categoryId", required = true) int categoryId,
            @RequestParam(value = "templateLocationCode", required = true) String templateLocationCode,
            @RequestParam(value = "itemId", required = false, defaultValue = "0") int itemId) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();


        //
        if (errorMsgMap.isEmpty()) {
            return new ModelAndView("redirect:/viewcategory/listcategoryitem?categoryId=" + categoryId);
        } else {
            //

            //
            mapMsg.put("errorMsgMap", errorMsgMap);

            //
            return new ModelAndView("/viewcategory/tab_preaddcategoryitem", mapMsg);
        }
    }

    @RequestMapping(value = "/removecategoryitem")
    public ModelAndView removeCategoryItem(
            @RequestParam(value = "categoryId", required = true) int categoryId,
            @RequestParam(value = "templateLocationCode", required = true) String templateLocationCode,
            @RequestParam(value = "itemId", required = true) int itemId) {


        return new ModelAndView("redirect:/viewcategory/listcategoryitem?categoryId=" + categoryId);
    }
}
