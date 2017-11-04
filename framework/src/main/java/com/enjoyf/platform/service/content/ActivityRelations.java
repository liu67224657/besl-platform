package com.enjoyf.platform.service.content;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-12
 * Time: 下午2:51
 * To change this template use File | Settings | File Templates.
 */
public class ActivityRelations implements Serializable {

    private long activityId;

    private ActivityRelation exchangeGoodRelation;

    private ActivityRelation goodsRelation;


    public ActivityRelation getExchangeGoodRelation() {
        return exchangeGoodRelation;
    }

    public void setExchangeGoodRelation(ActivityRelation exchangeGoodRelation) {
        this.exchangeGoodRelation = exchangeGoodRelation;
    }

    public ActivityRelation getGoodsRelation() {
        return goodsRelation;
    }

    public void setGoodsRelation(ActivityRelation goodsRelation) {
        this.goodsRelation = goodsRelation;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
