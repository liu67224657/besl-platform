package com.enjoyf.platform.service.point;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.text.TextJsonItem;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-12
 * Time: 下午2:51
 * To change this template use File | Settings | File Templates.
 */
public class ActivityHotRanks implements Serializable {
    private Long activityHotRanksId;
    private Long activityId;
    private String activityName;
    private Long goodsId;
    private String pic;
    private int exchange_num;
    private ActivityType activityType;
    private Date lastExchangeTime;
    private ActStatus removeStatus = ActStatus.UNACT;

    public Long getActivityHotRanksId() {
        return activityHotRanksId;
    }

    public void setActivityHotRanksId(Long activityHotRanksId) {
        this.activityHotRanksId = activityHotRanksId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public int getExchange_num() {
        return exchange_num;
    }

    public void setExchange_num(int exchange_num) {
        this.exchange_num = exchange_num;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public Date getLastExchangeTime() {
        return lastExchangeTime;
    }

    public void setLastExchangeTime(Date lastExchangeTime) {
        this.lastExchangeTime = lastExchangeTime;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
