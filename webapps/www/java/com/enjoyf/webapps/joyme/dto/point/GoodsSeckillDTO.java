package com.enjoyf.webapps.joyme.dto.point;

import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;

import java.util.Date;

/**
 * Created by zhitaoshi on 2015/7/30.
 */
public class GoodsSeckillDTO {
    private long seckillId;
    private String goodsId;                //秒杀商品ID

    private Date startTime;               //开始时间
    private Date endTime;                 //结束时间
    private String beforeTips;//开始前
    private String inTips; //进行中
    private String afterTips;  //结束后

    private int totals;
    private int restsum;

    private ActivityDTO activityDTO;

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getBeforeTips() {
        return beforeTips;
    }

    public void setBeforeTips(String beforeTips) {
        this.beforeTips = beforeTips;
    }

    public String getInTips() {
        return inTips;
    }

    public void setInTips(String inTips) {
        this.inTips = inTips;
    }

    public String getAfterTips() {
        return afterTips;
    }

    public void setAfterTips(String afterTips) {
        this.afterTips = afterTips;
    }

    public ActivityDTO getActivityDTO() {
        return activityDTO;
    }

    public void setActivityDTO(ActivityDTO activityDTO) {
        this.activityDTO = activityDTO;
    }

    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }

    public int getRestsum() {
        return restsum;
    }

    public void setRestsum(int restsum) {
        this.restsum = restsum;
    }
}
