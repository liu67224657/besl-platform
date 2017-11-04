package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-4-23
 * Time: 下午6:27
 * To change this template use File | Settings | File Templates.
 */
public class HotActivityEvent extends SystemEvent {

    private long goodsId;

    private int activityType;


    public HotActivityEvent() {
        super(SystemEventType.HOT_ACTIVITY);
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
