package com.enjoyf.platform.tools.point;

import com.enjoyf.platform.db.content.ContentHandler;
import com.enjoyf.platform.db.point.PointHandler;
import com.enjoyf.platform.service.content.Activity;
import com.enjoyf.platform.service.content.ActivityRelation;
import com.enjoyf.platform.service.content.ActivityRelationField;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PinYinUtil;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.sql.QueryCriterion;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: liangtang
 * Date: 13-5-29
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
public class ImportActivityGoodsInfo {
    public static PointHandler pointHandler = null;
    public static ContentHandler contentHandler = null;

    public static void main(String[] args) {
//        try {

//            contentHandler = new ContentHandler("content", fiveProps);

        try {
            FiveProps fiveProps = Props.instance().getServProps();
            pointHandler = new PointHandler("point", fiveProps);
            List<ActivityGoods> list = pointHandler.queryActivityGoods(new QueryExpress());
            for (ActivityGoods activityGoods : list) {
                pointHandler.modifyActivityGoods(new UpdateExpress().set(ActivityGoodsField.FIRST_LETTER, PinYinUtil.getFirstWordLetter(activityGoods.getActivitySubject())),
                        new QueryExpress().add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_GOODS_ID, activityGoods.getActivityGoodsId())));
                if (activityGoods.getPlatform() == null) {
                    pointHandler.modifyActivityGoods(new UpdateExpress().set(ActivityGoodsField.PLATFORM, AppPlatform.CLIENT.getCode()), new QueryExpress().add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_GOODS_ID, activityGoods.getActivityGoodsId())));
                } else if (activityGoods.getPlatform().equals(AppPlatform.WEB)) {
                    pointHandler.modifyActivityGoods(new UpdateExpress().set(ActivityGoodsField.PLATFORM, AppPlatform.ALL.getCode()), new QueryExpress().add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_GOODS_ID, activityGoods.getActivityGoodsId())));
                }
            }
            System.out.println("success=================================================");
        } catch (ServiceException e) {
            System.out.println("error=================================================");

            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
//
//            List<Activity> list = contentHandler.listActivity(new QueryExpress());
//            for (Activity activity : list) {
//                ActivityGoods activityGoods = null;
//                if (activity.getActivityType().equals(ActivityType.EXCHANGE_GOODS)) {
//                    ActivityRelation activityRelation = contentHandler.getActivityRelation(new QueryExpress().add(QueryCriterions.eq(ActivityRelationField.ACTIVITY_ID, activity.getActivityId())));
//                    if (activityRelation != null) {
//                        long goodsId = activityRelation.getDirectId();
//                        ActivityGoods activityGoods2 = pointHandler.getActivityGoods(new QueryExpress().add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_GOODS_ID, goodsId)));
//                        if (activityGoods2 != null) {
//                            continue;
//                        }
//                        ExchangeGoods exchangeGoods = pointHandler.getExchangeGoodsById(goodsId);
//                        activityGoods = new ActivityGoods();
//                        activityGoods.setActivityDesc(activity.getActivityDesc());
//                        activityGoods.setActivityGoodsId(exchangeGoods.getGoodsId());
//                        activityGoods.setActivitygoodsType(exchangeGoods.getGoodsType());
//                        activityGoods.setSubDesc(activity.getSubDesc());
//                        activityGoods.setActivityPicUrl(activity.getActivityPicUrl());
//                        activityGoods.setActivitySubject(activity.getActivitySubject());
//                        activityGoods.setActivityType(activity.getActivityType());
//                        activityGoods.setChannelType(activity.getWeixinExclusive());
//                        activityGoods.setCreateIp(activity.getCreateIp());
//                        activityGoods.setCreateTime(activity.getCreateTime());
//                        activityGoods.setCreateUserId(activity.getCreateUserId());
//                        activityGoods.setDisplayOrder(activity.getDisplayOrder());
//                        // exchangegoods表new hot 合并为1个字段 0等于不是new也不是hot   new=1 hot=2  3=all；
//                        int i = 0;
//                        if (exchangeGoods.getIsNew()) {
//                            i = i + 1;
//                        } else if (exchangeGoods.getIsHot()) {
//                            i = i + 2;
//                        }
//                        activityGoods.setDisplayType(i);
//                        activityGoods.setEndTime(activity.getEndTime());
//                        activityGoods.setFirstLetter(activity.getFirstLetter());
//                        activityGoods.setGameDbId(activity.getGameDbId());
//                        activityGoods.setGoodsActionType(activity.getGoodsActionType());
//                        activityGoods.setGoodsAmount(exchangeGoods.getGoodsAmount());
//                        activityGoods.setGoodsPoint(exchangeGoods.getGoodsConsumePoint());
//                        activityGoods.setGoodsResetAmount(exchangeGoods.getGoodsResetAmount());
//                        activityGoods.setHotActivity(activity.getHotActivity());
//                        activityGoods.setPassiveShareId(exchangeGoods.getShareId());
//                        activityGoods.setReserveType(activity.getReserveType());
//                        activityGoods.setStartTime(activity.getStartTime());
//                        activityGoods.setTaoTimesType(exchangeGoods.getTaoTimesType() == null ? ConsumeTimesType.ONETIMESMANYDAY : exchangeGoods.getTaoTimesType());
//                        activityGoods.setTextJsonItemsList(activity.getTextJsonItemsList());
//                        activityGoods.setTimeType(exchangeGoods.getExchangeTimeType() == null ? ConsumeTimesType.ONETIMESMANYDAY : exchangeGoods.getExchangeTimeType());
//                        activityGoods.setActStatus(activity.getRemoveStatus());
//                        activityGoods.setBgPic(activity.getBgPic());
//                        if (activity.getActivityPlatform() != null) {
//                            int p = activity.getActivityPlatform().getValue();
//                            if (p > 2) {
//                                activityGoods.setPlatform(2);
//                            } else if (p == 1) {
//                                activityGoods.setPlatform(1);
//                            } else if (p == 2) {
//                                activityGoods.setPlatform(0);
//                            } else {
//                                activityGoods.setPlatform(-1);
//                            }
//                        } else {
//                            activityGoods.setPlatform(-1);
//                        }
//                        activityGoods.setSeckilltype(0);
//                        activityGoods.setSecKill(null);
//                        pointHandler.createActivity(activityGoods);
//                    }
//                } else {
//                    ActivityRelation activityRelation = contentHandler.getActivityRelation(new QueryExpress().add(QueryCriterions.eq(ActivityRelationField.ACTIVITY_ID, activity.getActivityId())));
//                    if (activityRelation != null) {
//                        long goodsId = activityRelation.getDirectId();
//                        ActivityGoods activityGoods2 = pointHandler.getActivityGoods(new QueryExpress().add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_GOODS_ID, goodsId)));
//                        if (activityGoods2 != null) {
//                            continue;
//                        }
//                        Goods goods = pointHandler.getGoodsById(goodsId);
//                        activityGoods = new ActivityGoods();
//                        activityGoods.setActivityDesc(activity.getActivityDesc());
//                        activityGoods.setActivityGoodsId(goods.getGoodsId());
//                        activityGoods.setActivitygoodsType(goods.getGoodsType());
//                        activityGoods.setSubDesc(activity.getSubDesc());
//                        activityGoods.setActivityPicUrl(activity.getActivityPicUrl());
//                        activityGoods.setActivitySubject(activity.getActivitySubject());
//                        activityGoods.setActivityType(activity.getActivityType());
//                        activityGoods.setChannelType(activity.getWeixinExclusive());
//                        activityGoods.setCreateIp(activity.getCreateIp());
//                        activityGoods.setCreateTime(activity.getCreateTime());
//                        activityGoods.setCreateUserId(activity.getCreateUserId());
//                        activityGoods.setDisplayOrder(activity.getDisplayOrder());
//                        // exchangegoods表new hot 合并为1个字段 0等于不是new也不是hot   new=1 hot=2  3=all；
//                        int i = 0;
//                        if (goods.getIsNew()) {
//                            i = i + 1;
//                        } else if (goods.getIsHot()) {
//                            i = i + 2;
//                        }
//                        activityGoods.setDisplayType(i);
//                        activityGoods.setEndTime(activity.getEndTime());
//                        activityGoods.setFirstLetter(activity.getFirstLetter());
//                        activityGoods.setGameDbId(activity.getGameDbId());
//                        activityGoods.setGoodsActionType(activity.getGoodsActionType());
//                        activityGoods.setGoodsAmount(goods.getGoodsAmount());
//                        activityGoods.setGoodsPoint(goods.getGoodsConsumePoint());
//                        activityGoods.setGoodsResetAmount(goods.getGoodsResetAmount());
//                        activityGoods.setHotActivity(activity.getHotActivity());
//                        activityGoods.setPassiveShareId(goods.getShareId());
//                        activityGoods.setReserveType(activity.getReserveType());
//                        activityGoods.setStartTime(activity.getStartTime());
//                        activityGoods.setTextJsonItemsList(activity.getTextJsonItemsList());
//                        activityGoods.setTimeType(goods.getConsumeTimesType() == null ? ConsumeTimesType.ONETIMESMANYDAY : goods.getConsumeTimesType());
//                        activityGoods.setActStatus(activity.getRemoveStatus());
//                        activityGoods.setTaoTimesType(goods.getConsumeTimesType() == null ? ConsumeTimesType.ONETIMESMANYDAY : goods.getConsumeTimesType());
//                        activityGoods.setPlatform(-1);
//                        activityGoods.setBgPic(activity.getBgPic());
//                        activityGoods.setSeckilltype(0);
//                        activityGoods.setSecKill(null);
//                        pointHandler.createActivity(activityGoods);
//
//                    }
//                }
//                System.out.println("-------success--------");
//            }
//            System.out.println("-------end--------");
//        } catch (Exception e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
    }

}
