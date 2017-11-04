package com.enjoyf.platform.tools.point;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.point.PointHandler;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.serv.point.PointCache;
import com.enjoyf.platform.serv.point.PointRedis;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.point.ActivityGoods;
import com.enjoyf.platform.service.point.ActivityGoodsField;
import com.enjoyf.platform.service.point.UserExchangeLog;
import com.enjoyf.platform.service.point.UserExchangeLogField;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.util.CollectionUtil;

/**
 * Created by zhitaoshi on 2015/10/15.
 */
public class ImportGiftRedis {

    private static PointHandler pointHandler = null;
    private static PointRedis pointRedis = null;
    private static PointCache pointCache = null;

    public static void main(String[] args) {
        FiveProps servProps = Props.instance().getServProps();
        try {
            pointHandler = new PointHandler("point", servProps);
            pointRedis = new PointRedis(servProps);
            pointCache = new PointCache(new MemCachedConfig(servProps));
        } catch (DbException e) {
            e.printStackTrace();
        }
        //importGiftRedisCache();
        //importMyGiftCache();
        updateGiftCache();
    }

    private static void updateGiftCache() {
        try {
            pointHandler.modifyActivityGoods(new UpdateExpress().increase(ActivityGoodsField.GOODS_RESETAMOUNT, -1),
                    new QueryExpress().add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_GOODS_ID, 20963l)));
            pointCache.deleteActivityGoodsByCache(20963l);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private static void importMyGiftCache() {
        try {
            System.out.println("-------------------------begin-----------------------------");
            Pagination page = null;
            int cp = 0;
            do {
                cp += 1;
                page = new Pagination(2000 * cp, cp, 2000);
                QueryExpress queryExpress = new QueryExpress();
                //queryExpress.add(QueryCriterions.eq(UserExchangeLogField.PROFILEID, "71ec98e6a5cfe2923ddeaeecc39861e4"));
                queryExpress.add(QuerySort.add(UserExchangeLogField.EXCHANGE_TIME, QuerySortOrder.DESC));
                PageRows<UserExchangeLog> pageRows = pointHandler.queryUserExchangeLogByPageRows(queryExpress, page);
                if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                    for (UserExchangeLog log : pageRows.getRows()) {
                        System.out.println(log.getAppkey() + "  " + log.getProfileId() + "  "+log.getGoodsId());
                        pointRedis.putMyGiftCache(log.getProfileId(), log.getAppkey(), log);
                    }
                }
                page = pageRows.getPage();
            } while (!page.isLastPage());
            System.out.println("-------------------------end-----------------------------");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private static void importGiftRedisCache() {
        try {
            System.out.println("-------------------------begin-----------------------------");
            Pagination page = null;
            int cp = 0;
            do {
                cp += 1;
                page = new Pagination(2000 * cp, cp, 2000);

                QueryExpress queryExpress = new QueryExpress()
                        .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                        .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                        .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));

                PageRows<ActivityGoods> pageRows = pointHandler.queryActivityGoodsByPage(queryExpress, page);
                if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                    for (ActivityGoods gift : pageRows.getRows()) {
                        System.out.println(gift.getActivityGoodsId() + "    "+gift.getFirstLetter());
                        pointRedis.putGiftLetterCache(gift);
                    }
                }
                page = pageRows.getPage();
            } while (!page.isLastPage());
            System.out.println("-------------------------end-----------------------------");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
