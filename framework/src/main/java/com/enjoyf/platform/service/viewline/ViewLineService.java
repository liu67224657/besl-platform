/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.viewline;

import com.enjoyf.platform.serv.viewline.FeintCache;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.RangeRows;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-2-15 下午1:14
 * Description:
 */
public interface ViewLineService extends EventReceiver {
    //the category apis
    public ViewCategory createCategory(ViewCategory category) throws ServiceException;

    public ViewCategory getCategory(QueryExpress getExpress) throws ServiceException;

    public PageRows<ViewCategory> queryCategory(QueryExpress getExpress, Pagination pagination) throws ServiceException;

    public List<ViewCategory> queryCategoryTreeByAspectParent(ViewCategoryAspect aspect, int parentCategoryId) throws ServiceException;

    public PageRows<ViewCategory> queryCategoryTreeByAspectParentPaging(ViewCategoryAspect aspect, int parentCategoryId, String categoryName, Pagination pagination) throws ServiceException;

    public boolean modifyCategory(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    //the category privacy apis
    public ViewCategoryPrivacy createCategoryPrivacy(ViewCategoryPrivacy privacy) throws ServiceException;

    public ViewCategoryPrivacy getCategoryPrivacy(QueryExpress getExpress) throws ServiceException;

    public List<ViewCategoryPrivacy> queryCategoryPrivacies(QueryExpress queryExpress) throws ServiceException;

    public boolean modifyCategoryPrivacy(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    public boolean deleteCategoryPrivacy(QueryExpress queryExpress) throws ServiceException;

    public boolean deleteCategory(QueryExpress queryExpress) throws ServiceException;

    //the view line apis
    public ViewLine createViewLine(ViewLine line) throws ServiceException;

    public ViewLine getViewLine(QueryExpress getExpress) throws ServiceException;

    public Map<String, ViewLine> queryViewLines(QueryExpress queryExpress) throws ServiceException;

    public PageRows<ViewLine> queryViewLinesByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public RangeRows<ViewLine> queryViewLinesByRange(QueryExpress queryExpress, Rangination range) throws ServiceException;


    public boolean modifyViewLine(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    public boolean deleteViewLine(QueryExpress express) throws ServiceException;

    //the view line items apis.
    public ViewLineItem addLineItem(ViewLineItem item) throws ServiceException;

    public ViewLineItem getLineItem(QueryExpress queryExpress) throws ServiceException;

    public List<ViewLineItem> queryLineItems(QueryExpress queryExpress) throws ServiceException;

    public PageRows<ViewLineItem> queryLineItems(QueryExpress queryExpress, Pagination page) throws ServiceException;

    public RangeRows<ViewLineItem> queryLineItems(QueryExpress queryExpress, Rangination range) throws ServiceException;

    public boolean modifyLineItem(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    public boolean removeLineItem(QueryExpress queryExpress) throws ServiceException;

    //the sum data apis
    public ViewLineSumData getViewLineSumData(QueryExpress getExpress) throws ServiceException;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ViewLineLog createLog(ViewLineLog viewLineLog) throws ServiceException;

    public List<ViewLineLog> queryLineLogs(int srcId, ViewLineLogDomain logDomain) throws ServiceException;

    public ViewLineLog getLineLog(long logId) throws ServiceException;


    //综合的apis.
    public List<ViewCategory> queryCategoryTreeByItem(QueryExpress queryExpress) throws ServiceException;

    public List<ViewCategory> queryCategoryTreeByPrivacy(QueryExpress queryExpress) throws ServiceException;

    public boolean gatherLineItem(ViewItemType itemType, ViewLineItem lineItem, Set<Integer> selectedCategoryIds, Set<Integer> allCategoryIds) throws ServiceException;

    public ViewCategory getCategoryByCategoryIdFromCache(int categoryId) throws ServiceException;

    // feint content favorite
    public boolean feintContentFavorite(String contentId, FeintCache feintCache) throws ServiceException;

    //query all the article ids in a category
    public Set<String> queryArticleIds(Set<Integer> categoryIds, Set<Integer> lineIds) throws ServiceException;
}
