package com.enjoyf.webapps.tools.weblogic.viewline;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ViewLineHotdeployConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.viewline.ViewCategory;
import com.enjoyf.platform.service.viewline.ViewCategoryAspect;
import com.enjoyf.platform.service.viewline.ViewCategoryField;
import com.enjoyf.platform.service.viewline.ViewItemType;
import com.enjoyf.platform.service.viewline.ViewLine;
import com.enjoyf.platform.service.viewline.ViewLineAutoFillRule;
import com.enjoyf.platform.service.viewline.ViewLineField;
import com.enjoyf.platform.service.viewline.ViewLineItem;
import com.enjoyf.platform.service.viewline.ViewLineItemField;
import com.enjoyf.platform.service.viewline.ViewLineServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.RangeRows;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.dto.ViewLineItemDTO;
import com.enjoyf.platform.webapps.common.viewline.ViewLineWebDateProcessorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import serp.util.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: taijunli
 * Date: 2012-1-18 15:27:48
 * Desc: 登录日志与操作日志
 */
@Service(value = "viewLineWebLogic")
public class ViewLineWebLogic {
    //
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    private ViewLineHotdeployConfig hotdeployConfig = HotdeployConfigFactory.get().getConfig(ViewLineHotdeployConfig.class);

    /////////////////The util methods//////////////////
    public ViewLineAutoFillRule generateAutoFillRule(ViewItemType lineItemType, Map<String, String> autoFillRuleKeyValues,
                                                     Map<String, String> errorMsgMap, Map<String, Object> msgMap) {
        return ViewLineWebDateProcessorFactory.get().factory(lineItemType).generateAutoFillRule(autoFillRuleKeyValues, errorMsgMap, msgMap);
    }

    public void autoFillRuleToInput(ViewItemType lineItemType, ViewLineAutoFillRule rule, Map<String, Object> msgMap) {
        //
        ViewLineWebDateProcessorFactory.get().factory(lineItemType).autoFillRuleToInput(rule, msgMap);
    }


    public ViewLineItem generateAddLineItem(ViewItemType lineItemType, Map<String, String> lineItemKeyValues,
                                            Map<String, String> errorMsgMap, Map<String, Object> msgMap) {
        return ViewLineWebDateProcessorFactory.get().factory(lineItemType).generateAddLineItem(lineItemKeyValues, errorMsgMap, msgMap);
    }


    public List<ViewLineItemDTO> buildLineItemDTOs(ViewItemType lineItemType, List<ViewLineItem> lineItems) {
        return ViewLineWebDateProcessorFactory.get().factory(lineItemType).buildViewLineItemDTOs(lineItems);
    }


    //#################################ViewCategory##################################################
    public ViewCategory saveCategory(ViewCategory category) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("ViewCategory:" + category);
        }

        return ViewLineServiceSngl.get().createCategory(category);
    }

    public ViewCategory getCategoryById(int categoryId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getCategoryById method categoryId:" + categoryId);
        }

        QueryExpress getExpress = new QueryExpress();
        getExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, categoryId));

        return ViewLineServiceSngl.get().getCategory(getExpress);
    }

    public ViewCategory getCategoryByCodeAndAspect(String categoryCode, String aspectCode) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getCategoryByCode method categoryCode:" + categoryCode);
        }

        QueryExpress getExpress = new QueryExpress();
        getExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYCODE, categoryCode));
        getExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYASPECT, aspectCode));

        return ViewLineServiceSngl.get().getCategory(getExpress);
    }

    public PageRows<ViewCategory> queryCategoryTree(ViewCategoryAspect aspect, String categoryName, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryCategoryTree method  aspect:" + aspect + ", Pagination:" + pagination);
        }
        List<ViewCategory> valueList = new ArrayList<ViewCategory>();

        PageRows<ViewCategory> pageRows = ViewLineServiceSngl.get().queryCategoryTreeByAspectParentPaging(aspect, 0, StringUtil.isEmpty(categoryName) ? "" : categoryName, pagination);

        //
        exploreCategoryTree(pageRows.getRows(), valueList, 0);

        PageRows<ViewCategory> returnValue = new PageRows<ViewCategory>();
        returnValue.setRows(valueList);
        returnValue.setPage(pageRows.getPage());

        return returnValue;
    }

    private void exploreCategoryTree(List<ViewCategory> tree, List<ViewCategory> list, int currentLevel) {
        if (!CollectionUtil.isEmpty(tree)) {
            for (ViewCategory category : tree) {
                //
                category.setDisplayLevel(currentLevel);
                list.add(category);

                //
                if (!CollectionUtil.isEmpty(category.getChildrenCategories())) {
                    exploreCategoryTree(category.getChildrenCategories(), list, currentLevel + 1);
                }
            }
        }
    }

    public boolean modifyCategory(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("modifyViewCategory method  UpdateExpress:" + updateExpress + ",QueryExpress :" + queryExpress);
        }

        return ViewLineServiceSngl.get().modifyCategory(updateExpress, queryExpress);
    }

    //#################################ViewLine##################################################
    public ViewLine saveLine(ViewLine line) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("ViewLine:" + line);
        }

        return ViewLineServiceSngl.get().createViewLine(line);
    }

    public ViewLine getLineById(int lineId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getLineById, lineId:" + lineId);
        }

        QueryExpress getExpress = new QueryExpress();
        getExpress.add(QueryCriterions.eq(ViewLineField.LINEID, lineId));

        return ViewLineServiceSngl.get().getViewLine(getExpress);
    }


    public Map<String, ViewLine> queryLines(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryLines method  QueryExpress:" + queryExpress);
        }

        return ViewLineServiceSngl.get().queryViewLines(queryExpress);
    }

    public boolean modifyLine(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("modifyLine method  UpdateExpress:" + updateExpress + ",QueryExpress :" + queryExpress);
        }

        return ViewLineServiceSngl.get().modifyViewLine(updateExpress, queryExpress);
    }


    //#################################ViewLineItem##################################################
    public ViewLineItem addLineItem(ViewLineItem item) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("ViewLineItem:" + item);
        }

        return ViewLineServiceSngl.get().addLineItem(item);
    }

    public PageRows<ViewLineItem> queryLineItems(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryLineItems method  QueryExpress:" + queryExpress + ",Pagination :" + page);
        }

        return ViewLineServiceSngl.get().queryLineItems(queryExpress, page);
    }

    public ViewLineItem getLineItemByItemId(long itemId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getLineItemByItemId method  lineItemId:" + itemId);
        }

        ViewLineItem returnValue = new ViewLineItem();

        Pagination page = new Pagination(100, 1, WebappConfig.get().getHomePageSize());

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ViewLineItemField.ITEMID, itemId));

        try {
            returnValue = ViewLineServiceSngl.get().queryLineItems(queryExpress, page).getRows().get(0);
        } catch (ServiceException e) {
            GAlerter.lab("presortlineitem a Controller ServiceException :", e);
        }

        return returnValue;
    }

    public RangeRows<ViewLineItem> queryLineItems(QueryExpress queryExpress, Rangination range) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryLineItems method  QueryExpress:" + queryExpress + ",Rangination :" + range);
        }

        return ViewLineServiceSngl.get().queryLineItems(queryExpress, range);
    }

    public boolean modifyLineItem(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("modifyLineItem method  UpdateExpress:" + updateExpress + ",QueryExpress :" + queryExpress);
        }

        return ViewLineServiceSngl.get().modifyLineItem(updateExpress, queryExpress);
    }

    public boolean removeLineItem(int lineId, int itemId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("removeLineItem method  lineId:" + lineId + ",itemId :" + itemId);
        }

        //
        QueryExpress queryExpress = new QueryExpress();

        queryExpress.add(QueryCriterions.eq(ViewLineItemField.LINEID, lineId));
        queryExpress.add(QueryCriterions.eq(ViewLineItemField.ITEMID, itemId));

        return ViewLineServiceSngl.get().removeLineItem(queryExpress);
    }

    public boolean removeCategory(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("removeCategory method  queryExpress:" + queryExpress );
        }

        return ViewLineServiceSngl.get().deleteCategory(queryExpress);
    }
}
