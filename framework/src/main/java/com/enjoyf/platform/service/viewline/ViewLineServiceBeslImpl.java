/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.service.viewline;


import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.viewline.FeintCache;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.RangeRows;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto:taijunli@staff.joyme.com>Li TaiJun</a>
 */
class ViewLineServiceBeslImpl implements ViewLineService {
    //
    private ReqProcessor reqProcessor = null;

    public ViewLineServiceBeslImpl(ServiceConfig scfg) {
        if (scfg == null) {
            throw new RuntimeException("ViewLineServiceBeslImpl.ctor: ServiceConfig is null!");
        }

        reqProcessor = scfg.getReqProcessor();
    }

    //
    @Override
    public ViewCategory createCategory(ViewCategory category) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(category);

        Request req = new Request(ViewLineConstants.CATEGORY_CREATE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (ViewCategory) rPacket.readSerializable();
    }

    @Override
    public ViewCategory getCategory(QueryExpress getExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(getExpress);

        Request req = new Request(ViewLineConstants.CATEGORY_GET, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (ViewCategory) rPacket.readSerializable();
    }

    @Override
    public PageRows<ViewCategory> queryCategory(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);
        wPacket.writeSerializable(pagination);

        Request req = new Request(ViewLineConstants.CATEGORY_QUERY_BY_PAGE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (PageRows<ViewCategory>) rPacket.readSerializable();
    }

    @Override
    public List<ViewCategory> queryCategoryTreeByAspectParent(ViewCategoryAspect aspect, int parentCategoryId) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(aspect);
        wPacket.writeIntNx(parentCategoryId);

        Request req = new Request(ViewLineConstants.CATEGORY_TREE_QUERY, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (List<ViewCategory>) rPacket.readSerializable();
    }

    @Override
    public PageRows<ViewCategory> queryCategoryTreeByAspectParentPaging(ViewCategoryAspect aspect, int parentCategoryId, String categoryName, Pagination pagination) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(aspect);
        wPacket.writeIntNx(parentCategoryId);
        wPacket.writeStringUTF(categoryName);
        wPacket.writeSerializable(pagination);

        Request req = new Request(ViewLineConstants.CATEGORY_TREE_QUERY_PAGE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (PageRows<ViewCategory>) rPacket.readSerializable();
    }

    @Override
    public boolean modifyCategory(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(updateExpress);
        wPacket.writeSerializable(queryExpress);

        Request req = new Request(ViewLineConstants.CATEGORY_MODIFY, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }

    @Override
    public ViewCategoryPrivacy createCategoryPrivacy(ViewCategoryPrivacy privacy) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(privacy);

        Request req = new Request(ViewLineConstants.PRIVACY_CREATE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (ViewCategoryPrivacy) rPacket.readSerializable();
    }

    @Override
    public ViewCategoryPrivacy getCategoryPrivacy(QueryExpress getExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(getExpress);

        Request req = new Request(ViewLineConstants.PRIVACY_GET, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (ViewCategoryPrivacy) rPacket.readSerializable();
    }

    @Override
    public List<ViewCategoryPrivacy> queryCategoryPrivacies(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);

        Request req = new Request(ViewLineConstants.PRIVACY_QUERY, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (List<ViewCategoryPrivacy>) rPacket.readSerializable();
    }

    @Override
    public boolean modifyCategoryPrivacy(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(updateExpress);
        wPacket.writeSerializable(queryExpress);

        Request req = new Request(ViewLineConstants.PRIVACY_MODIFY, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }

    @Override
    public boolean deleteCategoryPrivacy(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);

        Request req = new Request(ViewLineConstants.PRIVACY_REMOVE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }

    @Override
    public boolean deleteCategory(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);

        Request req = new Request(ViewLineConstants.CATEGORY_REMOVE, wPacket);

        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }

    //
    @Override
    public ViewLine createViewLine(ViewLine line) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(line);

        Request req = new Request(ViewLineConstants.LINE_CREATE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (ViewLine) rPacket.readSerializable();
    }

    @Override
    public ViewLine getViewLine(QueryExpress getExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(getExpress);

        Request req = new Request(ViewLineConstants.LINE_GET, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (ViewLine) rPacket.readSerializable();
    }

    @Override
    public Map<String, ViewLine> queryViewLines(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);

        Request req = new Request(ViewLineConstants.LINE_QUERY, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (Map<String, ViewLine>) rPacket.readSerializable();
    }

    @Override
    public PageRows<ViewLine> queryViewLinesByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);
        wPacket.writeSerializable(pagination);
        Request req = new Request(ViewLineConstants.LINE_QUERY_BY_PAGE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (PageRows<ViewLine>) rPacket.readSerializable();
    }

    @Override
    public RangeRows<ViewLine> queryViewLinesByRange(QueryExpress queryExpress, Rangination range) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);
        wPacket.writeSerializable(range);
        Request req = new Request(ViewLineConstants.LINE_QUERY_BY_RANGE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (RangeRows<ViewLine>) rPacket.readSerializable();
    }


    @Override
    public boolean modifyViewLine(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(updateExpress);
        wPacket.writeSerializable(queryExpress);

        Request req = new Request(ViewLineConstants.LINE_MODIFY, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }

    @Override
    public boolean deleteViewLine(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);

        Request req = new Request(ViewLineConstants.LINE_REMOVE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }

    //#################################ViewLineItem##################################################
    @Override
    public ViewLineItem addLineItem(ViewLineItem item) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(item);

        Request req = new Request(ViewLineConstants.LINEITEM_ADD, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (ViewLineItem) rPacket.readSerializable();
    }

    @Override
    public ViewLineItem getLineItem(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);

        Request req = new Request(ViewLineConstants.LINEITEM_GET, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (ViewLineItem) rPacket.readSerializable();
    }

    @Override
    public List<ViewLineItem> queryLineItems(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);

        Request req = new Request(ViewLineConstants.LINEITEM_QUERY, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (List<ViewLineItem>) rPacket.readSerializable();
    }

    @Override
    public PageRows<ViewLineItem> queryLineItems(QueryExpress queryExpress, Pagination page) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);
        wPacket.writeSerializable(page);

        Request req = new Request(ViewLineConstants.LINEITEM_QUERY_PAGE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (PageRows<ViewLineItem>) rPacket.readSerializable();
    }

    @Override
    public RangeRows<ViewLineItem> queryLineItems(QueryExpress queryExpress, Rangination range) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);
        wPacket.writeSerializable(range);

        Request req = new Request(ViewLineConstants.LINEITEM_QUERY_RANGE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (RangeRows<ViewLineItem>) rPacket.readSerializable();
    }

    @Override
    public boolean modifyLineItem(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(updateExpress);
        wPacket.writeSerializable(queryExpress);

        Request req = new Request(ViewLineConstants.LINEITEM_MODIFY, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }

    @Override
    public boolean removeLineItem(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);

        Request req = new Request(ViewLineConstants.LINEITEM_REMOVE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }

    @Override
    public ViewLineSumData getViewLineSumData(QueryExpress getExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(getExpress);

        Request req = new Request(ViewLineConstants.SUMDATE_GET, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (ViewLineSumData) rPacket.readSerializable();
    }

    //todo
    @Override
    public ViewLineLog createLog(ViewLineLog viewLineLog) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(viewLineLog);

        Request req = new Request(ViewLineConstants.VIEWLINE_LOG_CREATE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (ViewLineLog) rPacket.readSerializable();
    }

    @Override
    public List<ViewLineLog> queryLineLogs(int srcId, ViewLineLogDomain logDomain) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeIntNx(srcId);
        wPacket.writeSerializable(logDomain);

        Request req = new Request(ViewLineConstants.VIEWLINE_LOG_QUERY_BY_SRCID, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (List<ViewLineLog>) rPacket.readSerializable();
    }

    @Override
    public ViewLineLog getLineLog(long logId) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeLongNx(logId);

        Request req = new Request(ViewLineConstants.VIEWLINE_LOG_GET_BY_LOGID, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (ViewLineLog) rPacket.readSerializable();
    }

    @Override
    public List<ViewCategory> queryCategoryTreeByItem(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);

        Request req = new Request(ViewLineConstants.CATEGORY_QUERY_TREE_BY_ITEM, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (List<ViewCategory>) rPacket.readSerializable();
    }

    @Override
    public List<ViewCategory> queryCategoryTreeByPrivacy(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);

        Request req = new Request(ViewLineConstants.CATEGORY_QUERY_TREE_BY_PRIVACY, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (List<ViewCategory>) rPacket.readSerializable();
    }

    @Override
    public boolean gatherLineItem(ViewItemType itemType, ViewLineItem lineItem, Set<Integer> selectedCategoryIds, Set<Integer> allCategoryIds) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(itemType);
        wPacket.writeSerializable(lineItem);
        wPacket.writeSerializable((Serializable) selectedCategoryIds);
        wPacket.writeSerializable((Serializable) allCategoryIds);

        Request req = new Request(ViewLineConstants.LINEITEM_GATHER, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }

    @Override
    public ViewCategory getCategoryByCategoryIdFromCache(int categoryId) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeIntNx(categoryId);

        Request req = new Request(ViewLineConstants.CATEGORY_QUERY_TREE_BY_ID, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (ViewCategory) rPacket.readSerializable();
    }

    @Override
    public boolean feintContentFavorite(String contentId, FeintCache feintCache) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(contentId);
        wp.writeSerializable(feintCache);

        Request req = new Request(ViewLineConstants.FEINT_CONTENT_FAVORITE, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public Set<String> queryArticleIds(Set<Integer> categoryIds, Set<Integer> lineIds) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable((Serializable) categoryIds);
        wPacket.writeSerializable((Serializable) lineIds);

        Request req = new Request(ViewLineConstants.VIEWLINE_ITEM_ARTICLE_IDS_QUERY, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (Set<String>) rPacket.readSerializable();
    }


    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(event);

        Request req = new Request(ViewLineConstants.RECIEVE_EVENT, wp);
        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }
}
