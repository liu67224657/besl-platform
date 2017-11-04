package com.enjoyf.platform.serv.viewline.quartz;

import com.enjoyf.platform.serv.viewline.ViewLineLogic;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.stats.ContentStatDomain;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.stats.StatDateType;
import com.enjoyf.platform.service.stats.StatItem;
import com.enjoyf.platform.service.stats.StatSection;
import com.enjoyf.platform.service.stats.StatSectionDefault;
import com.enjoyf.platform.service.stats.StatServiceSngl;
import com.enjoyf.platform.service.viewline.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.quartz.FivewhQuartzJob;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

public class ViewLineAutoReloadQuartzJob extends FivewhQuartzJob {
    //
    private static final Logger logger = LoggerFactory.getLogger(ViewLineAutoReloadQuartzJob.class);

    //
    private static final int HOT_TOP_LINE_SIZE = 50;
    private static final int FAVOR_TOP_LINE_SIZE = 20;
    private static final int REPLY_TOP_LINE_SIZE = 20;

    private static final String INDEX_CATEGROY_TOP_HOT = "hot";
    private static final String INDEX_CATEGROY_TOP_FAVOR = "favor";
    private static final String INDEX_CATEGROY_TOP_REPLY = "reply";

    private static final int DISPLAY_ORDER_STEP = 10;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("ViewLineAutoReloadQuartzJob executing started.");

        Date today = new Date();

        Date statDate = StatDateType.DAY.getStartDateByType(today);

        //get logic by job param
        ViewLineLogic viewLineLogic = (ViewLineLogic) jobExecutionContext.getJobDetail().getJobDataMap().get(ViewLineLogic.class.getName());

        //1 get hot interaction
        try {
            ViewCategory viewCategory = getIndexCateoryByCategoryCodeAspect(viewLineLogic, INDEX_CATEGROY_TOP_HOT, ViewCategoryAspect.CUSTOM_INDEX);
            if (viewCategory == null) {
                GAlerter.lab("ViewLineAutoReloadQuartzJob get hotcategory is null");
            } else {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(ViewLineField.CATEGORYID, viewCategory.getCategoryId()));
                queryExpress.add(QueryCriterions.eq(ViewLineField.VALIDSTATUS, ValidStatus.VALID.getCode()));
                ViewLine viewLine = viewLineLogic.getViewLine(queryExpress);

                if (viewLine == null) {
                    GAlerter.lab("ViewLineAutoReloadQuartzJob get hottop viewline is null");
                } else {
                    Map<StatSection, StatItem> topSections = StatServiceSngl.get().queryStatItemsByDomain(ContentStatDomain.CONTENT_TOP_INTERACTION_3DAY, StatDateType.DAY, statDate);

                    //计算displayorder步长为了排序方便，每个displayorder之间相隔10
                    int orderStep = 0;
                    for (int i = 0; i < HOT_TOP_LINE_SIZE; i++) {
                        StatItem item = topSections.get(new StatSectionDefault(String.valueOf(i + 1)));

                        //
                        if (item == null) {
                            logger.warn("ViewLineAutoReloadQuartzJob query top item not enough.");
                            break;
                        }

                        //update or insert item
                        ViewLineItem viewLineItem = statItemToViewLineItem(item, viewCategory.getCategoryId(), viewCategory.getCategoryAspect(), viewLine.getLineId(), ValidStatus.INVALID, orderStep);
                        addLineItem(viewLineLogic, viewLineItem);

                        orderStep = orderStep - DISPLAY_ORDER_STEP;

                    }
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab("ViewLineAutoReloadQuartzJob queryHotTop error.", e);
        }

        //2
        try {
            ViewCategory viewCategory = getIndexCateoryByCategoryCodeAspect(viewLineLogic, INDEX_CATEGROY_TOP_FAVOR, ViewCategoryAspect.CUSTOM_INDEX);
            if (viewCategory == null) {
                GAlerter.lab("ViewLineAutoReloadQuartzJob get favorcategory is null");
            } else {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(ViewLineField.CATEGORYID, viewCategory.getCategoryId()));
                queryExpress.add(QueryCriterions.eq(ViewLineField.VALIDSTATUS, ValidStatus.VALID.getCode()));
                ViewLine viewLine = viewLineLogic.getViewLine(queryExpress);

                if (viewLine == null) {
                    GAlerter.lab("ViewLineAutoReloadQuartzJob get favorviewline is null");
                } else {
                    Map<StatSection, StatItem> topSections = StatServiceSngl.get().queryStatItemsByDomain(ContentStatDomain.CONTENT_TOP_FAVOR_3DAY, StatDateType.DAY, statDate);

                    int orderStep = 0;
                    for (int i = 0; i < FAVOR_TOP_LINE_SIZE; i++) {
                        StatItem item = topSections.get(new StatSectionDefault(String.valueOf(i + 1)));

                        //
                        if (item == null) {
                            logger.warn("ViewLineAutoReloadQuartzJob query top item not enough.");
                            break;
                        }

                        ViewLineItem viewLineItem = statItemToViewLineItem(item, viewCategory.getCategoryId(), viewCategory.getCategoryAspect(), viewLine.getLineId(), ValidStatus.VALID, orderStep);
                        addLineItem(viewLineLogic, viewLineItem);
                        orderStep = orderStep - DISPLAY_ORDER_STEP;
                    }
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab("ViewLineAutoReloadQuartzJob queryFavorTop error.", e);
        }

        //3
        try {
            ViewCategory viewCategory = getIndexCateoryByCategoryCodeAspect(viewLineLogic, INDEX_CATEGROY_TOP_REPLY, ViewCategoryAspect.CUSTOM_INDEX);
            if (viewCategory == null) {
                GAlerter.lab("ViewLineAutoReloadQuartzJob get replytopcategory is null");
            } else {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(ViewLineField.CATEGORYID, viewCategory.getCategoryId()));
                queryExpress.add(QueryCriterions.eq(ViewLineField.VALIDSTATUS, ValidStatus.VALID.getCode()));
                ViewLine viewLine = viewLineLogic.getViewLine(queryExpress);

                if (viewLine == null) {
                    GAlerter.lab("ViewLineAutoReloadQuartzJob get replyviewline is null");
                } else {


                    Map<StatSection, StatItem> topSections = StatServiceSngl.get().queryStatItemsByDomain(ContentStatDomain.CONTENT_TOP_REPLY_3DAY, StatDateType.DAY, statDate);

                    int orderStep = 0;
                    for (int i = 0; i < REPLY_TOP_LINE_SIZE; i++) {
                        StatItem item = topSections.get(new StatSectionDefault(String.valueOf(i + 1)));

                        //
                        if (item == null) {
                            logger.warn("ViewLineAutoReloadQuartzJob query top item not enough.");
                            break;
                        }

                        ViewLineItem viewLineItem = statItemToViewLineItem(item, viewCategory.getCategoryId(), viewCategory.getCategoryAspect(), viewLine.getLineId(), ValidStatus.VALID, orderStep);
                        addLineItem(viewLineLogic, viewLineItem);
                        orderStep = orderStep - DISPLAY_ORDER_STEP;
                    }
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab("ViewLineAutoReloadQuartzJob queryReplyTop error.", e);
        }


        logger.info("ViewLineAutoReloadQuartzJob executing finished.");
    }

    private ViewCategory getIndexCateoryByCategoryCodeAspect(ViewLineLogic logic, String categoryCode, ViewCategoryAspect aspect) throws ServiceException {
        ViewCategory returnObj = null;

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYCODE, categoryCode));
        queryExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYASPECT, aspect.getCode()));
        queryExpress.add(QueryCriterions.eq(ViewLineField.VALIDSTATUS, ValidStatus.VALID.getCode()));
        returnObj = logic.getCategory(queryExpress);

        return returnObj;
    }

    private ViewLineItem statItemToViewLineItem(StatItem item, int categoryId, ViewCategoryAspect aspect, int viewlineId, ValidStatus vaildStatus, int displayOrderStep) {
        ViewLineItem viewLineItem = new ViewLineItem();
        viewLineItem.setCategoryAspect(aspect);
        viewLineItem.setCategoryId(categoryId);
        viewLineItem.setCreateDate(new Date());
        viewLineItem.setCreateUno("ViewLineAutoReloadQuartzJob");
        viewLineItem.setDirectUno(item.getExtData().getExtValue01());
        viewLineItem.setDirectId(item.getExtData().getExtValue02());
        viewLineItem.setLineId(viewlineId);
        viewLineItem.setItemCreateDate(new Date());
        viewLineItem.setValidStatus(vaildStatus);
        viewLineItem.setDisplayOrder(Integer.MAX_VALUE - ((int) (System.currentTimeMillis() / 1000) + displayOrderStep));
        return viewLineItem;
    }

    private void addLineItem(ViewLineLogic logic, ViewLineItem lineItem) {


        try {
            logic.addLineItem(lineItem);
        } catch (ServiceException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("insert failed update item" + lineItem);
            }

            UpdateExpress updateExpress = new UpdateExpress();

            updateExpress.set(ViewLineItemField.DISPLAYORDER, lineItem.getDisplayOrder());

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ViewLineItemField.LINEID, lineItem.getLineId()));
            queryExpress.add(QueryCriterions.eq(ViewLineItemField.DIRECTID, lineItem.getDirectId()));


            try {
                logic.modifyLineItem(updateExpress, queryExpress);
            } catch (ServiceException e1) {
                GAlerter.lab("ViewLineAutoReloadQuartzJob add lineitem occured error.", e1);
            }
        }
    }
}
