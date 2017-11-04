package com.enjoyf.platform.serv.viewline.messageprocessor;

import com.enjoyf.platform.serv.viewline.ViewLineLogic;
import com.enjoyf.platform.service.event.system.ViewLineItemPostSystemMessageEvent;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.viewline.ViewCategory;
import com.enjoyf.platform.service.viewline.ViewCategoryField;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;

/**
 * ViewLine系统通知
 */
public abstract class AbstractMessageProcessor {
    protected ViewLineLogic viewLineLogic;

    public AbstractMessageProcessor(ViewLineLogic viewLineLogic) {
        this.viewLineLogic = viewLineLogic;
    }

    public abstract void postSystemMessage(ViewLineItemPostSystemMessageEvent event);

    protected ViewCategory getCategory(int categoryId) throws ServiceException {
        ViewCategory returnObj = null;
        returnObj = viewLineLogic.getCategoryByCategoryIdFromCache(categoryId);
        if (returnObj == null) {
            returnObj = viewLineLogic.getCategory(new QueryExpress().add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, categoryId)));
        }
        return returnObj;
    }

    protected ViewCategory getTopCategory(int categoryId) throws ServiceException {
        ViewCategory returnObj = null;
        ViewCategory viewCategory = viewLineLogic.getCategoryByCategoryIdFromCache(categoryId);
        if (viewCategory != null) {
            returnObj = getTopCategoryByCache(viewCategory);
        } else {
            viewCategory = viewLineLogic.getCategory(new QueryExpress().add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, categoryId)));
            if (viewCategory != null) {
                returnObj = getTopCategoryByDb(viewCategory);
            }
        }
        return returnObj;

    }

    protected ViewCategory getTopCategoryByDb(ViewCategory viewCategory) throws ServiceException {
        ViewCategory returnObj = viewCategory;

        if (viewCategory.getParentCategoryId() > 0) {
            QueryExpress parentGetExpress = new QueryExpress();
            parentGetExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, viewCategory.getParentCategoryId()));

            //
            ViewCategory patentCategory = viewLineLogic.getCategory(parentGetExpress);

            //
            returnObj = getTopCategoryByDb(patentCategory);
        }

        return returnObj;
    }

    protected ViewCategory getTopCategoryByCache(ViewCategory viewCategory) throws ServiceException {
        ViewCategory returnObj = viewCategory;
        if (viewCategory.getParentCategory() != null) {
            returnObj = getTopCategoryByCache(viewCategory.getParentCategory());
        }
        return returnObj;
    }
}
