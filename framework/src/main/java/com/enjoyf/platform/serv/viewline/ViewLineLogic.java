package com.enjoyf.platform.serv.viewline;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.db.viewline.ViewLineHandler;
import com.enjoyf.platform.serv.viewline.messageprocessor.*;
import com.enjoyf.platform.serv.viewline.viewlineitemprocessor.*;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentField;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.system.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.viewline.*;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author litaijun
 * @since 2012-2-9 17:33:20
 */
public class ViewLineLogic implements ViewLineService {
    //
    private static final Logger logger = LoggerFactory.getLogger(ViewLineLogic.class);

    //
    private ViewLineConfig config;

    //the event queue thread n.
    private QueueThreadN eventProcessQueueThreadN = null;

    //
    private ViewCategoryCache categoryCache;

    // feint date map cache
    private Map<String, FeintCache> feintContentCacheMap = null;
    private Map<Integer, String> feintProfileCacheMap = null;
    private int sequence = 0;
    private String feintCategoryCode = "feint";
    private Refresher profileRefresher;

    //the handler's
    private static final String CATEGORY_INDEX_TALK = "talk";
    private static final String HOT_CONTENT_LINE_LOCATIONCODE = "hot";
    private ViewLineHandler writeAbleViewLineHandler;
    private HandlerPool<ViewLineHandler> readonlyViewLineHandlersPool;

    private int hotContentViewLineId;

    //todo
    private Pattern articleUrlPattern = Pattern.compile("/note/([0-9a-zA-Z\\-_]+)");

    private Map<ViewCategoryAspect, AbstractMessageProcessor> messageProcessorMap = new HashMap<ViewCategoryAspect, AbstractMessageProcessor>();

    private InsertChainProcessor viewLineItemInsertProcessor = null;

    private RemoveChainProcessor viewLineItemRemoveProcessor = null;

    public ViewLineLogic(ViewLineConfig cfg) {
        //
        config = cfg;

        //initialize the handler.
        try {
            writeAbleViewLineHandler = new ViewLineHandler(config.getWriteableDataSourceName(), config.getProps());
            readonlyViewLineHandlersPool = new HandlerPool<ViewLineHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyViewLineHandlersPool.add(new ViewLineHandler(dsn, config.getProps()));
            }
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());
            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        //the queue thread n initialize.
        eventProcessQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                //
                if (obj instanceof Event) {
                    //
                    processQueuedEvent((Event) obj);
                } else {
                    GAlerter.lab("In eventProcessQueueThreadN, there is a unknown obj.");
                }
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessQueue"));

        //init the category cache
        categoryCache = new ViewCategoryCache(this);

        // feint date map cache
        feintContentCacheMap = new HashMap<String, FeintCache>();
        feintProfileCacheMap = new HashMap<Integer, String>();

        //the feint profile cache refresher.
        profileRefresher = new Refresher(1000 * 60 * 10);

        messageProcessorMap.put(ViewCategoryAspect.CONTENT_BOARD, new GameBoardMessageProcessor(this));
        messageProcessorMap.put(ViewCategoryAspect.CUSTOM_BAIKE, new BaikeMessageProcessor(this));
        messageProcessorMap.put(ViewCategoryAspect.CONTENT_MAGAZINE, new MagazineMessageProcessor(this));
        messageProcessorMap.put(ViewCategoryAspect.CUSTOM_GAME_THUMB, new GameCoverMessageProcessor(this));
        messageProcessorMap.put(ViewCategoryAspect.CUSTOM_INDEX, new IndexMessageProcessor(this));

        //
        viewLineItemInsertProcessor = new InsertChainProcessor();
        viewLineItemInsertProcessor.addProcessor(new SendoutCreateGroupContentRelationProcessor(this));
        viewLineItemInsertProcessor.addProcessor(new SendoutCreateGameContentRelationProcessor(this));

        viewLineItemRemoveProcessor = new RemoveChainProcessor();
        viewLineItemRemoveProcessor.addProcessor(new SendoutRemoveGroupContentRelationProcessor(this));
        viewLineItemRemoveProcessor.addProcessor(new SendoutRemoveGameContentRelationProcessor(this));

        viewLineItemRemoveProcessor.addProcessor(new RemoveHotContentProcessor(this));
    }

    //
    private void processQueuedEvent(Event event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start to processQueuedEvent:" + event);
        }

        //check the event type.
        if (event instanceof ContentRepliedEvent) {
            //
            ContentRepliedEvent contentRepliedEvent = (ContentRepliedEvent) event;

            try {
                //
                QueryExpress lineItemQueryExpress = new QueryExpress();

                //
                lineItemQueryExpress.add(QueryCriterions.eq(ViewLineItemField.CATEGORYASPECT, ViewCategoryAspect.CONTENT_BOARD.getCode()))
                        .add(QueryCriterions.eq(ViewLineItemField.DIRECTID, contentRepliedEvent.getContentId()))
                        .add(QueryCriterions.eq(ViewLineItemField.DIRECTUNO, contentRepliedEvent.getContentUno()));

                List<ViewLineItem> lineItems = readonlyViewLineHandlersPool.getHandler().queryLineItems(lineItemQueryExpress);
                for (ViewLineItem item : lineItems) {
                    //
                    QueryExpress lineGetExpress = new QueryExpress();
                    lineGetExpress.add(QueryCriterions.eq(ViewLineField.LINEID, item.getLineId()));

                    //check the line props.
                    //todo
                    ViewLine line = readonlyViewLineHandlersPool.getHandler().getLine(lineGetExpress);
                    if (line != null && line.getItemType().equals(ViewItemType.CONTENT) && line.getLocationCode().equals(LocationCode.DEFAULT.getCode())) {
                        //get the category from the cache or the db.
                        ViewCategory category = categoryCache.getCategory(item.getCategoryId());
                        if (category == null) {
                            QueryExpress categoryGetExpress = new QueryExpress();
                            categoryGetExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, item.getCategoryId()));

                            category = readonlyViewLineHandlersPool.getHandler().getCategory(categoryGetExpress);
                        }

                        //check the category props.
                        //todo
                        if (LocationCode.TALK_BOARD.getCode().equals(category.getLocationCode())) {
                            //
                            QueryExpress whereExpress = new QueryExpress();
                            whereExpress.add(QueryCriterions.eq(ViewLineItemField.ITEMID, item.getItemId()));

                            //
                            UpdateExpress updateExpress = new UpdateExpress();
                            updateExpress.set(ViewLineItemField.DISPLAYORDER, Integer.MAX_VALUE - (int) (contentRepliedEvent.getReplyDate().getTime() / 1000));

                            //update the line item display order.
                            writeAbleViewLineHandler.updateLineItem(updateExpress, whereExpress);
                        } else {
                            logger.warn("The replied content's category is not exit, item:" + item);
                        }
                    } else {
                        logger.warn("The replied content's line is not exit, item:" + item);
                    }
                }
            } catch (Exception e) {
                //
                GAlerter.lab("ViewLineLogic processQueuedEvent error.", e);
            }
        } else if (event instanceof ViewLineItemSumIncreaseEvent) {
            //
            ViewLineItemSumIncreaseEvent viewLineItemInsertEvent = (ViewLineItemSumIncreaseEvent) event;

            modifyInsertOrDeleteContentItemSum(viewLineItemInsertEvent);

        } else if (event instanceof ViewLineItemPostSystemMessageEvent) {
            ViewLineItemPostSystemMessageEvent postSystemMessageEvent = (ViewLineItemPostSystemMessageEvent) event;

            try {
                postSystemMessage(postSystemMessageEvent);
            } catch (Exception e) {
                GAlerter.lab("process ViewLineItemPostSystemMessageEvent occured error.e: ", e);
            }
        } else if (event instanceof ContentSumIncreaseEvent) {
            //
            ContentSumIncreaseEvent contentSumIncreaseEvent = (ContentSumIncreaseEvent) event;

            //
            ViewLineSumDataField field = null;
            if (contentSumIncreaseEvent.getField().equals(ContentField.REPLYTIMES)) {
                field = ViewLineSumDataField.REPLYTIMES;
            } else if (contentSumIncreaseEvent.getField().equals(ContentField.FAVORTIMES)) {
                field = ViewLineSumDataField.FAVORTIMES;
            } else if (contentSumIncreaseEvent.getField().equals(ContentField.FORWARDTIMES)) {
                field = ViewLineSumDataField.FORWARDTIMES;
            } else if (contentSumIncreaseEvent.getField().equals(ContentField.VIEWTIMES)) {
                field = ViewLineSumDataField.VIEWTIMES;
            }

            //
            if (field != null) {
                processViewLineItemInteract(contentSumIncreaseEvent.getContentId(), contentSumIncreaseEvent.getContentUno(), field, contentSumIncreaseEvent.getCount());
            }
        } else if (event instanceof ViewLineItemRemoveEvent) {
            ViewLineItemRemoveEvent removeEvent = (ViewLineItemRemoveEvent) event;

            viewLineItemRemoveProcessor.process(removeEvent);

        } else if (event instanceof ViewLineItemInsertEvent) {
            ViewLineItemInsertEvent viewLineItemInsertEvent = (ViewLineItemInsertEvent) event;
            viewLineItemInsertProcessor.process(viewLineItemInsertEvent);
        } else {
            logger.info("ViewLineLogic discard the unknown event:" + event);
        }
    }

    //the content is insert into the line.
    private void modifyInsertOrDeleteContentItemSum(ViewLineItemSumIncreaseEvent event) {
        //query the category of the content inset.
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, event.getCategoryId()));

        QueryExpress queryLineExpress = new QueryExpress();
        queryLineExpress.add(QueryCriterions.eq(ViewLineField.LINEID, event.getLineId()));

        //
        Map<ViewLineSumDataField, Integer> columnValues = new HashMap<ViewLineSumDataField, Integer>();

        //
        columnValues.put(ViewLineSumDataField.POSTTIMES, event.isDelete() ? -1 : 1);

        //the interact sum process.
        try {
            Content content = ContentServiceSngl.get().getContentById(event.getDirectId());

            if (content != null) {
                columnValues.put(ViewLineSumDataField.REPLYTIMES, (event.isDelete() ? -1 : 1) * content.getReplyTimes());
                columnValues.put(ViewLineSumDataField.VIEWTIMES, (event.isDelete() ? -1 : 1) * content.getViewTimes());
                columnValues.put(ViewLineSumDataField.FAVORTIMES, (event.isDelete() ? -1 : 1) * content.getFavorTimes());
                columnValues.put(ViewLineSumDataField.FORWARDTIMES, (event.isDelete() ? -1 : 1) * content.getForwardTimes());
            }
        } catch (ServiceException e) {
            //
            GAlerter.lab("ViewLineLogic processViewLineItemValidStatus error, update the valid content sum data.", e);
        }

        //update the sum.
        try {
            increaseCategorySumData(getCategory(queryExpress), columnValues);
            increaseLineSumData(getViewLine(queryLineExpress), columnValues);
        } catch (Exception e) {
            //
            GAlerter.lab("ViewLineLogic processViewLineItemValidStatus error.", e);
        }
    }

    //the content is replied, view, favor.
    private void processViewLineItemInteract(String contentId, String contentUno, ViewLineSumDataField field, int delta) {
        List<ViewLineItem> items = null;

        //query all the category of the content.
        QueryExpress queryExpress = new QueryExpress();

        queryExpress.add(QueryCriterions.eq(ViewLineItemField.DIRECTID, contentId));
        queryExpress.add(QueryCriterions.eq(ViewLineItemField.DIRECTUNO, contentUno));
        queryExpress.add(QueryCriterions.eq(ViewLineItemField.VALIDSTATUS, ValidStatus.VALID.getCode()));

        try {
            items = readonlyViewLineHandlersPool.getHandler().queryLineItems(queryExpress);

            if (!CollectionUtil.isEmpty(items)) {
                for (ViewLineItem item : items) {
                    //update the category and thier parents
                    QueryExpress getExpress = new QueryExpress();
                    getExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, item.getCategoryId()));

                    Map<ViewLineSumDataField, Integer> columnValues = new HashMap<ViewLineSumDataField, Integer>();
                    columnValues.put(field, delta);

                    increaseCategorySumData(getCategory(getExpress), columnValues);
                    increaseLineSumData(getViewLine(new QueryExpress().add(QueryCriterions.eq(ViewLineField.LINEID, item.getLineId()))), columnValues);
                }
            }
        } catch (Exception e) {
            //
            GAlerter.lab("ViewLineLogic processViewLineItemInteract error.", e);
        }
    }

    public void increaseCategorySumData(ViewCategory category, Map<ViewLineSumDataField, Integer> columnValues) {
        //
        if (category == null || CollectionUtil.isEmpty(columnValues)) {
            return;
        }

        //update the current category sum data
        //all the sum
        try {
            QueryExpress queryExpress = new QueryExpress();

            queryExpress.add(QueryCriterions.eq(ViewLineSumDataField.SRCID, category.getCategoryId()));
            queryExpress.add(QueryCriterions.eq(ViewLineSumDataField.SUMDOMAIN, ViewLineSumDomain.CATEGORY.getCode()));
            queryExpress.add(QueryCriterions.eq(ViewLineSumDataField.SUMTYPECODE, ViewLineSumData.SUM_TYPE_ALL));

            //get the data
            ViewLineSumData allData = writeAbleViewLineHandler.getViewLineSumData(queryExpress);
            if (allData == null) {
                allData = new ViewLineSumData();

                allData.setSrcId(category.getCategoryId());
                allData.setSumDomain(ViewLineSumDomain.CATEGORY);
                allData.setSumTypeCode(ViewLineSumData.SUM_TYPE_ALL);

                allData = writeAbleViewLineHandler.insertViewLineSumData(allData);
            }

            //increase the sum data
            UpdateExpress updateExpress = new UpdateExpress();
            for (Map.Entry<ViewLineSumDataField, Integer> entry : columnValues.entrySet()) {
                updateExpress.increase(entry.getKey(), entry.getValue());
            }

            writeAbleViewLineHandler.updateViewLineSumData(updateExpress, queryExpress);
        } catch (Exception e) {
            //
            GAlerter.lab("ViewLineLogic increaseCategorySumData update all sum date error, category:" + category, e);
        }

        //daily sum
        try {
            String todaySumTypeCode = DateUtil.formatDateToString(new Date(), DateUtil.PATTERN_DATE_DAY);

            QueryExpress queryExpress = new QueryExpress();

            queryExpress.add(QueryCriterions.eq(ViewLineSumDataField.SRCID, category.getCategoryId()));
            queryExpress.add(QueryCriterions.eq(ViewLineSumDataField.SUMDOMAIN, ViewLineSumDomain.CATEGORY.getCode()));
            queryExpress.add(QueryCriterions.eq(ViewLineSumDataField.SUMTYPECODE, todaySumTypeCode));

            //
            ViewLineSumData todayData = writeAbleViewLineHandler.getViewLineSumData(queryExpress);
            if (todayData == null) {
                todayData = new ViewLineSumData();

                todayData.setSrcId(category.getCategoryId());
                todayData.setSumDomain(ViewLineSumDomain.CATEGORY);
                todayData.setSumTypeCode(todaySumTypeCode);

                todayData = writeAbleViewLineHandler.insertViewLineSumData(todayData);
            }

            //increase the sum data
            UpdateExpress updateExpress = new UpdateExpress();
            for (Map.Entry<ViewLineSumDataField, Integer> entry : columnValues.entrySet()) {
                updateExpress.increase(entry.getKey(), entry.getValue());
            }

            writeAbleViewLineHandler.updateViewLineSumData(updateExpress, queryExpress);
        } catch (Exception e) {
            //
            GAlerter.lab("ViewLineLogic increaseCategorySumData update today sum data error, category:" + category, e);
        }

        //update the parent category sum data.
        try {
            this.loadParentCategory(category);

            if (category.getParentCategory() != null) {
                increaseCategorySumData(category.getParentCategory(), columnValues);
            }
        } catch (Exception e) {
            //
            GAlerter.lab("ViewLineLogic increaseCategorySumData error, parentCategory:" + category, e);
        }
    }

    public void increaseLineSumData(ViewLine line, Map<ViewLineSumDataField, Integer> columnValues) {
        //
        if (line == null || CollectionUtil.isEmpty(columnValues)) {
            return;
        }

        //update the current category sum data
        //all the sum
        try {
            QueryExpress queryExpress = new QueryExpress();

            queryExpress.add(QueryCriterions.eq(ViewLineSumDataField.SRCID, line.getLineId()));
            queryExpress.add(QueryCriterions.eq(ViewLineSumDataField.SUMDOMAIN, ViewLineSumDomain.LINE.getCode()));
            queryExpress.add(QueryCriterions.eq(ViewLineSumDataField.SUMTYPECODE, ViewLineSumData.SUM_TYPE_ALL));

            //get the data
            ViewLineSumData allData = writeAbleViewLineHandler.getViewLineSumData(queryExpress);
            if (allData == null) {
                allData = new ViewLineSumData();

                allData.setSrcId(line.getLineId());
                allData.setSumDomain(ViewLineSumDomain.LINE);
                allData.setSumTypeCode(ViewLineSumData.SUM_TYPE_ALL);

                allData = writeAbleViewLineHandler.insertViewLineSumData(allData);
            }

            //increase the sum data
            UpdateExpress updateExpress = new UpdateExpress();
            for (Map.Entry<ViewLineSumDataField, Integer> entry : columnValues.entrySet()) {
                updateExpress.increase(entry.getKey(), entry.getValue());
            }

            writeAbleViewLineHandler.updateViewLineSumData(updateExpress, queryExpress);
        } catch (Exception e) {
            //
            GAlerter.lab("ViewLineLogic increaseLineSumData update all sum date error, line:" + line, e);
        }

        //daily sum
        try {
            String todaySumTypeCode = DateUtil.formatDateToString(new Date(), DateUtil.PATTERN_DATE_DAY);

            QueryExpress queryExpress = new QueryExpress();

            queryExpress.add(QueryCriterions.eq(ViewLineSumDataField.SRCID, line.getLineId()));
            queryExpress.add(QueryCriterions.eq(ViewLineSumDataField.SUMDOMAIN, ViewLineSumDomain.LINE.getCode()));
            queryExpress.add(QueryCriterions.eq(ViewLineSumDataField.SUMTYPECODE, todaySumTypeCode));

            //
            ViewLineSumData todayData = writeAbleViewLineHandler.getViewLineSumData(queryExpress);
            if (todayData == null) {
                todayData = new ViewLineSumData();

                todayData.setSrcId(line.getLineId());
                todayData.setSumDomain(ViewLineSumDomain.LINE);
                todayData.setSumTypeCode(todaySumTypeCode);

                todayData = writeAbleViewLineHandler.insertViewLineSumData(todayData);
            }

            //increase the sum data
            UpdateExpress updateExpress = new UpdateExpress();
            for (Map.Entry<ViewLineSumDataField, Integer> entry : columnValues.entrySet()) {
                updateExpress.increase(entry.getKey(), entry.getValue());
            }

            writeAbleViewLineHandler.updateViewLineSumData(updateExpress, queryExpress);
        } catch (Exception e) {
            //
            GAlerter.lab("ViewLineLogic increaseLineSumData update today sum data error, line:" + line, e);
        }
    }

    @Override
    public ViewCategory createCategory(ViewCategory category) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the ViewLineHandler to createCategory, category is " + category);
        }

        return writeAbleViewLineHandler.insertCategory(category);
    }

    @Override
    public ViewCategory getCategory(QueryExpress getExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the ViewLineHandler to createCategory, getExpress is " + getExpress);
        }

        ViewCategory returnValue = readonlyViewLineHandlersPool.getHandler().getCategory(getExpress);

        //load the category's parent category.
        if (returnValue != null) {
            loadParentCategory(returnValue);
        }

        return returnValue;
    }

    @Override
    public PageRows<ViewCategory> queryCategory(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the ViewLineHandler to queryCategory, queryExpress is " + queryExpress + " pagination:" + pagination);
        }

        PageRows<ViewCategory> returnValue = readonlyViewLineHandlersPool.getHandler().queryCategories(queryExpress, pagination);

        return returnValue;
    }

    public void loadParentCategory(ViewCategory category) throws ServiceException {
        if (category.getParentCategoryId() > 0) {
            QueryExpress parentGetExpress = new QueryExpress();
            parentGetExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, category.getParentCategoryId()));

            //
            ViewCategory patentCategory = readonlyViewLineHandlersPool.getHandler().getCategory(parentGetExpress);

            //
            loadParentCategory(patentCategory);

            //
            category.setParentCategory(patentCategory);
        }
    }

    @Override
    public List<ViewCategory> queryCategoryTreeByAspectParent(ViewCategoryAspect aspect, int parentCategoryId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the ViewLineHandler to queryCategoryTree, aspect is " + aspect + ", parentCategoryId is " + parentCategoryId);
        }

        //
        List<ViewCategory> returnValue = new ArrayList<ViewCategory>();

        //load the top level category.
        if (parentCategoryId == 0) {
            returnValue.addAll(queryChildrenCategories(aspect, parentCategoryId));
        } else {
            QueryExpress getExpress = new QueryExpress();
            getExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYASPECT, aspect.getCode()))
                    .add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, parentCategoryId))
                    .add(QuerySort.add(ViewCategoryField.DISPLAYORDER));

            returnValue.add(readonlyViewLineHandlersPool.getHandler().getCategory(getExpress));
        }

        //load the children.
        loadChildrenCategories(returnValue);

        return returnValue;
    }

    @Override
    public PageRows<ViewCategory> queryCategoryTreeByAspectParentPaging(ViewCategoryAspect aspect, int parentCategoryId, String categoryName, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the ViewLineHandler to queryCategoryTree, aspect is " + aspect + ", parentCategoryId is " + parentCategoryId + ", pagination is " + pagination + ", categoryName is " + categoryName);
        }

        //
        List<ViewCategory> returnValue = new ArrayList<ViewCategory>();
        PageRows<ViewCategory> returnPageRows = new PageRows<ViewCategory>();

        //load the top level category.
        PageRows<ViewCategory> pageRows = queryCategoriesByPaging(aspect, parentCategoryId, categoryName, pagination);
        if (parentCategoryId == 0) {
            returnValue.addAll(pageRows.getRows());
        } else {
            QueryExpress getExpress = new QueryExpress();
            getExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYASPECT, aspect.getCode()))
                    .add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, parentCategoryId))
                    .add(QuerySort.add(ViewCategoryField.DISPLAYORDER));

            if (!StringUtil.isEmpty(categoryName)) {
                getExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYNAME, categoryName));
            }

            returnValue.add(readonlyViewLineHandlersPool.getHandler().getCategory(getExpress));
        }

        //load the children.
        loadChildrenCategories(returnValue);

        returnPageRows.setRows(returnValue);
        returnPageRows.setPage(pageRows.getPage());

        return returnPageRows;
    }

    public void loadChildrenCategories(List<ViewCategory> categories) {
        //
        for (ViewCategory category : categories) {
            //
            List<ViewCategory> children = queryChildrenCategories(category.getCategoryAspect(), category.getCategoryId());

            //
            if (!children.isEmpty()) {
                //
                category.setChildrenCategories(children);

                //
                for (ViewCategory child : children) {
                    child.setParentCategory(category);
                }

                //loop
                loadChildrenCategories(children);
            }
        }
    }

    private PageRows<ViewCategory> queryCategoriesByPaging(ViewCategoryAspect aspect, int categoryId, String categoryName, Pagination pagination) throws ServiceException {
        PageRows<ViewCategory> returnValue = new PageRows<ViewCategory>();

        //
        QueryExpress queryExpress = new QueryExpress();

        queryExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYASPECT, aspect.getCode()));
        queryExpress.add(QueryCriterions.eq(ViewCategoryField.PARENTCATEGORYID, categoryId));

        queryExpress.add(QuerySort.add(ViewCategoryField.DISPLAYORDER));

        if (!StringUtil.isEmpty(categoryName)) {
            queryExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYNAME, categoryName));
        }

        //
        try {
            returnValue = readonlyViewLineHandlersPool.getHandler().queryCategories(queryExpress, pagination);

        } catch (DbException e) {

            GAlerter.lab("queryCategoriesByPaging caught an exception: ", e);
        }

        return returnValue;
    }

    private List<ViewCategory> queryChildrenCategories(ViewCategoryAspect aspect, int categoryId) {
        List<ViewCategory> returnValue = null;

        //
        QueryExpress queryExpress = new QueryExpress();

        queryExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYASPECT, aspect.getCode()));
        queryExpress.add(QueryCriterions.eq(ViewCategoryField.PARENTCATEGORYID, categoryId));

        queryExpress.add(QuerySort.add(ViewCategoryField.DISPLAYORDER));

        //
        try {
            returnValue = readonlyViewLineHandlersPool.getHandler().queryCategories(queryExpress);
        } catch (DbException e) {
            returnValue = new ArrayList<ViewCategory>();
        }

        return returnValue;
    }

    public List<ViewCategory> queryChildrenCategories(int categoryId) {
        List<ViewCategory> returnValue = null;

        //
        QueryExpress queryExpress = new QueryExpress();

        queryExpress.add(QueryCriterions.eq(ViewCategoryField.PARENTCATEGORYID, categoryId));

        queryExpress.add(QuerySort.add(ViewCategoryField.DISPLAYORDER));

        //
        try {
            returnValue = readonlyViewLineHandlersPool.getHandler().queryCategories(queryExpress);
        } catch (DbException e) {
            returnValue = new ArrayList<ViewCategory>();
        }

        return returnValue;
    }


    @Override
    public boolean modifyCategory(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleViewLineHandler to createCategory, updateExpress is " + updateExpress + ", queryExpress is " + queryExpress);
        }

        return writeAbleViewLineHandler.updateCategory(updateExpress, queryExpress) > 0;
    }

    @Override
    public ViewCategoryPrivacy createCategoryPrivacy(ViewCategoryPrivacy privacy) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleViewLineHandler to createCategoryPrivacy, privacy is " + privacy);
        }

        return writeAbleViewLineHandler.insertCategoryPrivacy(privacy);
    }

    @Override
    public ViewCategoryPrivacy getCategoryPrivacy(QueryExpress getExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the ViewLineHandler to getCategoryPrivacy, getExpress is " + getExpress);
        }

        return readonlyViewLineHandlersPool.getHandler().getCategoryPrivacy(getExpress);
    }

    @Override
    public List<ViewCategoryPrivacy> queryCategoryPrivacies(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the ViewLineHandler to queryCategoryPrivacies, queryExpress is " + queryExpress);
        }

        return readonlyViewLineHandlersPool.getHandler().queryCategoryPrivacies(queryExpress);
    }

    @Override
    public boolean modifyCategoryPrivacy(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleViewLineHandler to modifyCategoryPrivacy, updateExpress is " + updateExpress + ", queryExpress is " + queryExpress);
        }

        return writeAbleViewLineHandler.updateCategoryPrivacy(updateExpress, queryExpress) > 0;
    }

    @Override
    public boolean deleteCategoryPrivacy(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleViewLineHandler to deleteCategoryPrivacy, queryExpress is " + queryExpress + ", queryExpress is " + queryExpress);
        }

        return writeAbleViewLineHandler.deleteCategoryPrivacy(queryExpress) > 0;
    }

    @Override
    public boolean deleteCategory(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleViewLineHandler to deleteCategory, queryExpress is " + queryExpress);
        }

        return writeAbleViewLineHandler.deleteCategory(queryExpress) > 0;
    }

    @Override
    public ViewLine createViewLine(ViewLine line) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the ViewLineHandler to createViewLine, line is " + line);
        }

        return writeAbleViewLineHandler.insertLine(line);
    }

    @Override
    public ViewLine getViewLine(QueryExpress getExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the ViewLineHandler to getViewLine, getExpress is " + getExpress);
        }

        return readonlyViewLineHandlersPool.getHandler().getLine(getExpress);
    }

    @Override
    public Map<String, ViewLine> queryViewLines(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the ViewLineHandler to queryViewLines, queryExpress is " + queryExpress);
        }

        //要有序的
        Map<String, ViewLine> returnValue = new LinkedHashMap<String, ViewLine>();

        //query all the lines of the category.
        List<ViewLine> lines = readonlyViewLineHandlersPool.getHandler().queryLines(queryExpress);

        //put them inot map.
        for (ViewLine line : lines) {
            returnValue.put(line.getLocationCode(), line);
        }

        return returnValue;
    }

    @Override
    public PageRows<ViewLine> queryViewLinesByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the ViewLineHandler to queryViewLines, queryExpress is " + queryExpress + " pagination is " + pagination);
        }
        return readonlyViewLineHandlersPool.getHandler().queryLines(queryExpress, pagination);
    }

    @Override
    public RangeRows<ViewLine> queryViewLinesByRange(QueryExpress queryExpress, Rangination range) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the ViewLineHandler to queryViewLines, queryExpress is " + queryExpress + " range is " + range);
        }

        return readonlyViewLineHandlersPool.getHandler().queryLines(queryExpress, range);
    }

    @Override
    public boolean modifyViewLine(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleViewLineHandler to modifyViewLine, updateExpress is " + updateExpress + ", queryExpress is " + queryExpress);
        }

        return writeAbleViewLineHandler.updateLine(updateExpress, queryExpress) > 0;
    }

    @Override
    public boolean deleteViewLine(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleViewLineHandler to deleteViewLine, queryExpress is " + queryExpress);
        }

        return writeAbleViewLineHandler.deleteLine(queryExpress) > 0;
    }

    //####################################VIEWLINEITEM#######################################################
    @Override
    public ViewLineItem addLineItem(ViewLineItem item) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleViewLineHandler to addLineItem,ViewLineItem is " + item);
        }

        item = writeAbleViewLineHandler.insertLineItem(item);

        try {
            addViewLineInsertSumIncreaseEvent(item, false);

            addViewLineInsertEvent(item);

            addPostSystemMessage(item);
        } catch (Exception e) {
            GAlerter.lab("add line Item process error.e: ", e);
        }

        return item;
    }

    @Override
    public ViewLineItem getLineItem(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to getLineItem, queryExpress:" + queryExpress);
        }

        return readonlyViewLineHandlersPool.getHandler().getLineItem(queryExpress);
    }

    @Override
    public List<ViewLineItem> queryLineItems(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to getLineItem, queryExpress:" + queryExpress);
        }

        return readonlyViewLineHandlersPool.getHandler().queryLineItems(queryExpress);
    }

    @Override
    public PageRows<ViewLineItem> queryLineItems(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to queryLineItems, queryExpress:" + queryExpress + ", page:" + page);
        }

        return readonlyViewLineHandlersPool.getHandler().queryLineItems(queryExpress, page);
    }

    @Override
    public RangeRows<ViewLineItem> queryLineItems(QueryExpress queryExpress, Rangination range) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to queryLineItems, queryExpress:" + queryExpress + ", range:" + range);
        }

        return readonlyViewLineHandlersPool.getHandler().queryLineItems(queryExpress, range);
    }

    @Override
    public boolean modifyLineItem(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to modifyLineItem, updateExpress:" + updateExpress + ", queryExpress:" + queryExpress);
        }

        //先查询出来
        List<ViewLineItem> items = readonlyViewLineHandlersPool.getHandler().queryLineItems(queryExpress);

        boolean returnObj = writeAbleViewLineHandler.updateLineItem(updateExpress, queryExpress) > 0;

        if (returnObj) {
            processModifyStatus(items, updateExpress);
        }

        return returnObj;
    }


    @Override
    public boolean removeLineItem(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to removeLineItem, queryExpress:" + queryExpress);
        }

        //先查询出来
        List<ViewLineItem> items = readonlyViewLineHandlersPool.getHandler().queryLineItems(queryExpress);

        boolean returnObj = writeAbleViewLineHandler.deleteLineItem(queryExpress) > 0;

        if (returnObj) {
            processModifyStatus(items, new UpdateExpress().set(ViewLineItemField.VALIDSTATUS, ValidStatus.REMOVED));
        }

        return returnObj;
    }

    public ViewLineSumData createSumData(ViewLineSumData data) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleViewLineHandler to createSumData, data is " + data);
        }

        return writeAbleViewLineHandler.insertViewLineSumData(data);
    }

    public boolean modifySumData(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to modifySumData, updateExpress:" + updateExpress + ", queryExpress:" + queryExpress);
        }

        return writeAbleViewLineHandler.updateViewLineSumData(updateExpress, queryExpress) > 0;
    }

    @Override
    public ViewLineSumData getViewLineSumData(QueryExpress getExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to getViewLineSumData, getExpress:" + getExpress);
        }

        return readonlyViewLineHandlersPool.getHandler().getViewLineSumData(getExpress);
    }

    @Override
    public ViewLineLog createLog(ViewLineLog viewLineLog) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to createLog, queryExpress:" + viewLineLog);
        }

        return writeAbleViewLineHandler.insertViewLinLog(viewLineLog);
    }

    @Override
    public List<ViewLineLog> queryLineLogs(int srcId, ViewLineLogDomain logDomain) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to queryLineLogs, srcId:" + srcId + " logDomain:" + logDomain);
        }

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ViewLineLogField.SRCID, srcId))
                .add(QueryCriterions.eq(ViewLineLogField.SUBDOMAIN, logDomain.getCode()))
                .add(QuerySort.add(ViewLineLogField.CREATEDATE, QuerySortOrder.DESC));

        return readonlyViewLineHandlersPool.getHandler().queryViewLinLog(queryExpress);
    }

    public PageRows<ViewLineLog> queryLineLogs(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to getLogByExpress:" + queryExpress);
        }
        return readonlyViewLineHandlersPool.getHandler().queryViewLinLog(queryExpress, pagination);
    }

    @Override
    public ViewLineLog getLineLog(long logId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to queryLineLogs, logId:" + logId);
        }

        QueryExpress getExpress = new QueryExpress();
        getExpress.add(QueryCriterions.eq(ViewLineLogField.LOGID, logId));

        return readonlyViewLineHandlersPool.getHandler().getViewLinLog(getExpress);
    }

    @Override
    public List<ViewCategory> queryCategoryTreeByItem(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to queryCategoryTreeByItem, queryExpress:" + queryExpress);
        }

        //
        List<ViewCategory> returnValue = new ArrayList<ViewCategory>();

        //query the category ids by the item.
        List<ViewLineItem> viewLineItemList = readonlyViewLineHandlersPool.getHandler().queryLineItems(queryExpress);

        for (ViewLineItem lineItem : viewLineItemList) {
            //get the category object from the cache.
            ViewCategory category = categoryCache.getCategory(lineItem.getCategoryId());

            //
            if (category != null) {
                returnValue.add(category);
            }
        }

        return returnValue;
    }

    @Override
    public List<ViewCategory> queryCategoryTreeByPrivacy(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to queryCategoryTreeByPrivacy, queryExpress:" + queryExpress);
        }

        //
        List<ViewCategory> returnValue = new ArrayList<ViewCategory>();

        //query the category ids by the item.
        List<Integer> categoryIds = readonlyViewLineHandlersPool.getHandler().queryCategoryIdsByPrivacy(queryExpress);

        for (int categoryId : categoryIds) {
            //get the category object from the cache.
            ViewCategory category = categoryCache.getCategory(categoryId);

            //
            if (category != null) {
                returnValue.add(category);
            }
        }

        return returnValue;
    }

    @Override
    public boolean gatherLineItem(ViewItemType itemType, ViewLineItem lineItem,
                                  Set<Integer> selectedCategoryIds, Set<Integer> allCategoryIds) throws ServiceException {
        //
        //allCategoryIds.removeAll(selectedCategoryIds);

        //get all the selected lineid from the selectedCategoryIds
        Set<Integer> selectLineIds = queryCategoriesLineIds(itemType, selectedCategoryIds);

        //get all the lineid from the allCategoryIds
        Set<Integer> allLineIds = queryCategoriesLineIds(itemType, allCategoryIds);

        //delete viewlineItem.
        QueryExpress deleteExpress = new QueryExpress();

        //
        if (allLineIds.size() > 0) {
            QueryCriterions lineIdsCriterions = new QueryCriterions();

            deleteExpress.add(QueryCriterions.eq(ViewLineItemField.CATEGORYASPECT, ViewCategoryAspect.CONTENT_GAME));
            lineIdsCriterions.setLogic(QueryCriterionLogic.LOGIC_OR);
            for (int lineId : allLineIds) {
                lineIdsCriterions.getCriterionsList().add(QueryCriterions.eq(ViewLineItemField.LINEID, lineId));
            }

            deleteExpress.add(lineIdsCriterions);

            deleteExpress.add(QueryCriterions.eq(ViewLineItemField.DIRECTID, lineItem.getDirectId()));
            deleteExpress.add(QueryCriterions.eq(ViewLineItemField.DIRECTUNO, lineItem.getDirectUno()));

            //
            writeAbleViewLineHandler.deleteLineItem(deleteExpress);
//
//            for (int lineId : allLineIds) {
//                QueryExpress delExpress = new QueryExpress();
//                delExpress.add(QueryCriterions.eq(ViewLineItemField.LINEID, lineId));
//                deleteExpress.add(QueryCriterions.eq(ViewLineItemField.DIRECTID, lineItem.getDirectId()));
//                deleteExpress.add(QueryCriterions.eq(ViewLineItemField.DIRECTUNO, lineItem.getDirectUno()));
//                writeAbleViewLineHandler.deleteLineItem(deleteExpress);
//            }
        }

        //insert viewLine item.
        for (int lineId : selectLineIds) {
            //
            lineItem.setLineId(lineId);
            lineItem.setValidStatus(ValidStatus.VALID);
            //
            writeAbleViewLineHandler.insertLineItem(lineItem);
        }

        return true;
    }

    @Override
    public ViewCategory getCategoryByCategoryIdFromCache(int categoryId) throws ServiceException {
        if (categoryCache.getCategory(categoryId) != null) {
            return categoryCache.getCategory(categoryId);
        }

        return null;
    }

    //
    private Set<Integer> queryCategoriesLineIds(ViewItemType itemType, Set<Integer> categoryIds) throws DbException {
        Set<Integer> returnValue = new HashSet<Integer>();

        if (CollectionUtil.isEmpty(categoryIds) || categoryIds.size() == 0) {
            return returnValue;
        }

        //
        QueryExpress queryExpress = new QueryExpress();

        //
        QueryCriterions categoryIdsCriterions = new QueryCriterions();
        categoryIdsCriterions.setLogic(QueryCriterionLogic.LOGIC_OR);

        for (int categoryId : categoryIds) {
            categoryIdsCriterions.getCriterionsList().add(QueryCriterions.eq(ViewLineField.CATEGORYID, categoryId));
        }

        queryExpress.add(categoryIdsCriterions).add(QueryCriterions.eq(ViewLineField.ITEMTYPE, itemType.getCode()));

        //
        List<ViewLine> lines = readonlyViewLineHandlersPool.getHandler().queryLines(queryExpress);
        for (ViewLine line : lines) {
            returnValue.add(line.getLineId());
        }

        //
        return returnValue;
    }

    @Override
    public boolean feintContentFavorite(String contentId, FeintCache feintCache) throws ServiceException {
        synchronized (feintContentCacheMap) {
            feintContentCacheMap.put(contentId, feintCache);
        }
        return true;
    }

    @Override
    public Set<String> queryArticleIds(Set<Integer> categoryIds, Set<Integer> lineIds) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("ViewLineLogic queryArticleIds, categoryIds:" + categoryIds);
        }

        //query all the valid category ids
        Set<Integer> allCategoryIds = new HashSet<Integer>();
        for (int categoryId : categoryIds) {
            allCategoryIds.addAll(queryRecursionChildren(categoryId));
        }

        //query all the line ids.
        Set<Integer> contentLineIds = new HashSet<Integer>();
        Set<Integer> customLineIds = new HashSet<Integer>();

        //
        contentLineIds.addAll(queryCategoriesLineIds(ViewItemType.CONTENT, allCategoryIds));
        customLineIds.addAll(queryCategoriesLineIds(ViewItemType.CUSTOM, allCategoryIds));

        contentLineIds.addAll(lineIds);
        customLineIds.addAll(lineIds);

        //query all the content id.
        return queryContentIds(contentLineIds, customLineIds);
    }

    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("ViewLineLogic receiveEvent:" + event);
        }

        eventProcessQueueThreadN.add(event);

        return true;
    }

    private Set<Integer> queryRecursionChildren(int categoryId) throws ServiceException {
        Set<Integer> returnValue = new HashSet<Integer>();

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, categoryId));
        queryExpress.add(QueryCriterions.eq(ViewCategoryField.VALIDSTATUS, ValidStatus.VALID.getCode()));

        ViewCategory category = this.getCategory(queryExpress);

        if (category != null) {
            returnValue.add(category.getCategoryId());

            //loop children
            List<ViewCategory> children = this.queryChildrenCategories(category.getCategoryAspect(), category.getCategoryId());
            for (ViewCategory child : children) {
                if (child.getValidStatus().equals(ValidStatus.VALID)) {
                    returnValue.addAll(queryRecursionChildren(child.getCategoryId()));
                }
            }
        }

        return returnValue;
    }

    private Set<String> queryContentIds(Set<Integer> contentLineIds, Set<Integer> customLineIds) throws ServiceException {
        Set<String> returnValue = new HashSet<String>();

        if (!CollectionUtil.isEmpty(contentLineIds)) {
            //
            QueryExpress queryExpress = new QueryExpress();
            QueryCriterions lineIdsCriterions = new QueryCriterions();
            lineIdsCriterions.setLogic(QueryCriterionLogic.LOGIC_OR);
            for (int lineId : contentLineIds) {
                lineIdsCriterions.getCriterionsList().add(QueryCriterions.eq(ViewLineItemField.LINEID, lineId));
            }

            queryExpress.add(lineIdsCriterions).add(QueryCriterions.eq(ViewLineItemField.VALIDSTATUS, ValidStatus.VALID.getCode()));

            //
            List<ViewLineItem> items = queryLineItems(queryExpress);
            for (ViewLineItem item : items) {
                returnValue.add(item.getDirectId());
            }
        }

        if (!CollectionUtil.isEmpty(customLineIds)) {
            //
            QueryExpress queryExpress = new QueryExpress();
            QueryCriterions lineIdsCriterions = new QueryCriterions();
            lineIdsCriterions.setLogic(QueryCriterionLogic.LOGIC_OR);
            for (int lineId : customLineIds) {
                lineIdsCriterions.getCriterionsList().add(QueryCriterions.eq(ViewLineItemField.LINEID, lineId));
            }

            //
            queryExpress.add(lineIdsCriterions).add(QueryCriterions.eq(ViewLineItemField.VALIDSTATUS, ValidStatus.VALID.getCode()));

            //
            List<ViewLineItem> items = queryLineItems(queryExpress);
            for (ViewLineItem item : items) {
                String contentId = parseArticleId(item.getDisplayInfo().getLinkUrl());

                if (contentId != null) {
                    returnValue.add(contentId);
                }
            }
        }

        return returnValue;
    }

    public void reloadFeintProfileCache() {
        //reload the feintProfileMap cache.
        try {
            Map<Integer, String> map = new HashMap<Integer, String>();

            // category id
            Integer categoryId = null;
            List<ViewCategory> viewCategoryList = ViewLineServiceSngl.get().queryCategoryTreeByAspectParent(ViewCategoryAspect.CUSTOM_INDEX, 0);
            for (ViewCategory viewCategory : viewCategoryList) {
                if (feintCategoryCode.equals(viewCategory.getCategoryCode())) {
                    categoryId = viewCategory.getCategoryId();
                }
            }

            if (categoryId == null) {
                GAlerter.lan("feintProfileTask error : feint categoryId is null");
                return;
            }

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ViewLineItemField.CATEGORYID, categoryId));
            queryExpress.add(QueryCriterions.eq(ViewLineField.VALIDSTATUS, ValidStatus.VALID.getCode()));
            Pagination page = new Pagination(0, 1, 2000);

            PageRows<ViewLineItem> itemPageRows = queryLineItems(queryExpress, page);

            for (ViewLineItem lineItem : itemPageRows.getRows()) {
                map.put(map.size(), lineItem.getDirectUno());
            }

            page = itemPageRows.getPage();
            while (page.getMaxPage() > page.getCurPage()) {
                page.setCurPage(page.getCurPage() + 1);
                PageRows<ViewLineItem> pageRows = queryLineItems(queryExpress, page);

                for (ViewLineItem lineItem : pageRows.getRows()) {
                    map.put(map.size(), lineItem.getDirectUno());
                }

                page = pageRows.getPage();
            }

            synchronized (feintProfileCacheMap) {
                //
                feintProfileCacheMap = new HashMap<Integer, String>();
                feintProfileCacheMap.putAll(map);

                if (logger.isDebugEnabled()) {
                    logger.debug("feint Profile CacheMap size is " + feintProfileCacheMap.size());
                }
            }

        } catch (Exception e) {
            //
            GAlerter.lan("feintProfileTask occurred error.", e);
        }
    }


    private void processModifyStatus(List<ViewLineItem> beforeModifyItemList, UpdateExpress modifyExpress) {
        //get validstatus
        Object updateValidStatus = modifyExpress.getUpdateValueByField(ViewLineItemField.VALIDSTATUS);
        if (updateValidStatus == null) {
            return;
        }

        String updateStatusValue = String.valueOf(updateValidStatus);
        for (ViewLineItem item : beforeModifyItemList) {
            //修改的状态和修改前状态相等说明没做修改
            if (updateStatusValue.equals(item.getValidStatus().getCode())) {
                continue;
            } else if (!updateStatusValue.equals(ValidStatus.VALID.getCode())) {
                //不是修改成有效状态，删除
                addViewLineInsertSumIncreaseEvent(item, true);
                addViewLineRemoveEvent(item);
            } else {
                //是有效状态增加操作，添加
                addViewLineInsertSumIncreaseEvent(item, false);
                addViewLineInsertEvent(item);
                addPostSystemMessage(item);
            }
        }
    }

    private void addViewLineInsertSumIncreaseEvent(ViewLineItem item, boolean isDelete) {
        ViewLineItemSumIncreaseEvent insertEvent = new ViewLineItemSumIncreaseEvent();
        insertEvent.setViewCategoryAspect(item.getCategoryAspect());
        insertEvent.setCategoryId(item.getCategoryId());
        insertEvent.setLineId(item.getLineId());
        insertEvent.setDirectId(item.getDirectId());
        insertEvent.setDirectUno(item.getDirectUno());
        insertEvent.setItemCreateDate(new Date());
        insertEvent.setParentId(item.getParentId());
        insertEvent.setParentUno(item.getParentUno());
        insertEvent.setRelationId(item.getRelationId());
        insertEvent.setRelationUno(item.getRelationUno());
        insertEvent.setDelete(isDelete);

        eventProcessQueueThreadN.add(insertEvent);
    }

    private void addViewLineRemoveEvent(ViewLineItem item) {
        try {

            ViewLineItemRemoveEvent removeEvent = new ViewLineItemRemoveEvent();
            removeEvent.setCategoryId(item.getCategoryId());
            removeEvent.setViewCategoryAspect(item.getCategoryAspect());
            removeEvent.setLineId(item.getLineId());
            removeEvent.setDirectId(item.getDirectId());
            removeEvent.setDirectUno(item.getDirectUno());

            eventProcessQueueThreadN.add(removeEvent);
        } catch (Exception e) {
            GAlerter.lan("insert viewlineItem add eventProcessQueueThreadN error.", e);
        }
    }

    private void addViewLineInsertEvent(ViewLineItem item) {
        try {

            ViewLineItemInsertEvent insertEvent = new ViewLineItemInsertEvent();
            insertEvent.setCategoryId(item.getCategoryId());
            insertEvent.setViewCategoryAspect(item.getCategoryAspect());
            insertEvent.setLineId(item.getLineId());
            insertEvent.setDirectId(item.getDirectId());
            insertEvent.setDirectUno(item.getDirectUno());

            eventProcessQueueThreadN.add(insertEvent);
        } catch (Exception e) {
            GAlerter.lan("insert viewlineItem add eventProcessQueueThreadN error.", e);
        }
    }

    private void addPostSystemMessage(ViewLineItem item) {
        ViewLineItemPostSystemMessageEvent postSystemMessageEvent = new ViewLineItemPostSystemMessageEvent();
        postSystemMessageEvent.setCategoryId(item.getCategoryId());
        postSystemMessageEvent.setViewCategoryAspect(item.getCategoryAspect());
        postSystemMessageEvent.setDirectId(item.getDirectId());
        postSystemMessageEvent.setDirectUno(item.getDirectUno());
        postSystemMessageEvent.setCreateUno(item.getCreateUno());
        postSystemMessageEvent.setLineId(item.getLineId());

        //baike sync
        if (postSystemMessageEvent.getViewCategoryAspect().equals(ViewCategoryAspect.CUSTOM_BAIKE)) {
            postSystemMessage(postSystemMessageEvent);
        } else {
            eventProcessQueueThreadN.add(postSystemMessageEvent);
        }
    }

    private void postSystemMessage(ViewLineItemPostSystemMessageEvent postSystemMessageEvent) {
        //
        AbstractMessageProcessor abstractMessageProcessor = messageProcessorMap.get(postSystemMessageEvent.getViewCategoryAspect());
        if (abstractMessageProcessor != null) {
            abstractMessageProcessor.postSystemMessage(postSystemMessageEvent);
        } else {
            GAlerter.lan("post system mesage not suppport aspect:" + postSystemMessageEvent.getViewCategoryAspect());
        }
    }

    //
    private String parseArticleId(String articleUrl) {
        if (Strings.isNullOrEmpty(articleUrl)) {
            return null;
        }

        try {
            //
            Matcher matcher = articleUrlPattern.matcher(articleUrl);
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (Exception e) {
            logger.error("parseArticleId:" + articleUrl, e);
        }

        //
        return null;
    }
}
