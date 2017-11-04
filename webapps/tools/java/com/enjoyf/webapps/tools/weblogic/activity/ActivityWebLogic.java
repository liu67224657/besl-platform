package com.enjoyf.webapps.tools.weblogic.activity;

import com.enjoyf.platform.service.content.Activity;
import com.enjoyf.platform.service.content.ActivityField;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-9-23
 * Time: 上午10:33
 * To change this template use File | Settings | File Templates.
 */
//todo
public class ActivityWebLogic {
    public static Long sortExchangeActivity(String sort, Long activityId) throws ServiceException {
        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ActivityField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()));
        //step1 get goodsId,get display order
        Activity activity = ContentServiceSngl.get().getActivityById(activityId);
        if (activity == null) {
            return null;
        }
        if (sort.equals("up")) {
            queryExpress.add(QueryCriterions.lt(ActivityField.DISPLAY_ORDER, activity.getDisplayOrder()));
            queryExpress.add(QuerySort.add(ActivityField.DISPLAY_ORDER, QuerySortOrder.DESC));
        } else {
            queryExpress.add(QueryCriterions.gt(ActivityField.DISPLAY_ORDER, activity.getDisplayOrder()));
            queryExpress.add(QuerySort.add(ActivityField.DISPLAY_ORDER, QuerySortOrder.ASC));
        }

        PageRows<Activity> pageRows = ContentServiceSngl.get().queryActivity(queryExpress, new Pagination());
        List<Activity> list = pageRows.getRows();
        if (!CollectionUtil.isEmpty(list)) {
            updateExpress1.set(ActivityField.DISPLAY_ORDER, activity.getDisplayOrder());
            ContentServiceSngl.get().modifyActivity(updateExpress1, list.get(0).getActivityId());

            updateExpress2.set(ActivityField.DISPLAY_ORDER, list.get(0).getDisplayOrder());
            ContentServiceSngl.get().modifyActivity(updateExpress2, activity.getActivityId());
        }
//        ContentServiceSngl.get().queryActivity(new QueryExpress().add(QueryCriterions.eq(ActivityField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode())).add(QuerySort.add(ActivityField.DISPLAY_ORDER, QuerySortOrder.ASC)), new Pagination());
        return list.get(0).getActivityId();
    }

    public static void sortGoodsActivity(String sort, Long activityId, Integer goodsCategoryValue) throws ServiceException {
        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ActivityField.ACTIVITY_TYPE, ActivityType.GOODS.getCode()));
        if (goodsCategoryValue != null) {
            queryExpress.add(QueryCriterions.bitwiseAnd(ActivityField.GOODS_CATEGORY, QueryCriterionRelation.GT, goodsCategoryValue, 0));
        }
        //step1 get goodsId,get display order
        Activity activity = ContentServiceSngl.get().getActivityById(activityId);
        if (activity == null) {
            return;
        }
        if (sort.equals("up")) {
            queryExpress.add(QueryCriterions.lt(ActivityField.DISPLAY_ORDER, activity.getDisplayOrder()));
            queryExpress.add(QuerySort.add(ActivityField.DISPLAY_ORDER, QuerySortOrder.DESC));
        } else {
            queryExpress.add(QueryCriterions.gt(ActivityField.DISPLAY_ORDER, activity.getDisplayOrder()));
            queryExpress.add(QuerySort.add(ActivityField.DISPLAY_ORDER, QuerySortOrder.ASC));
        }

        PageRows<Activity> pageRows = ContentServiceSngl.get().queryActivity(queryExpress, new Pagination());
        List<Activity> list = pageRows.getRows();
        if (!CollectionUtil.isEmpty(list)) {
            updateExpress1.set(ActivityField.DISPLAY_ORDER, activity.getDisplayOrder());
            ContentServiceSngl.get().modifyActivity(updateExpress1, list.get(0).getActivityId());

            updateExpress2.set(ActivityField.DISPLAY_ORDER, list.get(0).getDisplayOrder());
            ContentServiceSngl.get().modifyActivity(updateExpress2, activity.getActivityId());
        }
        QueryExpress queryExpress1 = new QueryExpress();
        queryExpress1.add(QuerySort.add(ActivityField.DISPLAY_ORDER, QuerySortOrder.ASC));
        queryExpress1.add(QueryCriterions.eq(ActivityField.ACTIVITY_TYPE, ActivityType.GOODS.getCode()));
        if (goodsCategoryValue != null) {
            queryExpress1.add(QueryCriterions.bitwiseAnd(ActivityField.GOODS_CATEGORY, QueryCriterionRelation.GT, goodsCategoryValue, 0));
        }
        ContentServiceSngl.get().queryActivity(queryExpress1, new Pagination());
    }
}
