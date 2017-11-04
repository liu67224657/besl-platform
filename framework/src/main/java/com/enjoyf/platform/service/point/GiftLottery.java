package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by pengxu on 2016/11/30.
 */
public class GiftLottery implements Serializable {

    private long giftLotteryId;
    private String giftLotteryName; //名称
    private double probability;    //概率   例如：10%=0.1
    private int returnPoint;  //返还积分
    private String picName; //已抽中显示图片
    private String disPicName;    //未抽中显示图片
    private String picKey;    //图片key
    private LotteryType lotteryType;
    private int starRating;//星级


    public GiftLottery() {

    }

    public GiftLottery(int giftLotteryId, String giftLotteryName, double probability) {
        this.giftLotteryId = giftLotteryId;
        this.giftLotteryName = giftLotteryName;
        this.probability = probability;
    }


    public long getGiftLotteryId() {
        return giftLotteryId;
    }

    public void setGiftLotteryId(long giftLotteryId) {
        this.giftLotteryId = giftLotteryId;
    }

    public String getGiftLotteryName() {
        return giftLotteryName;
    }

    public void setGiftLotteryName(String giftLotteryName) {
        this.giftLotteryName = giftLotteryName;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public int getReturnPoint() {
        return returnPoint;
    }

    public void setReturnPoint(int returnPoint) {
        this.returnPoint = returnPoint;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getDisPicName() {
        return disPicName;
    }

    public void setDisPicName(String disPicName) {
        this.disPicName = disPicName;
    }

    public String getPicKey() {
        return picKey;
    }

    public void setPicKey(String picKey) {
        this.picKey = picKey;
    }

    public LotteryType getLotteryType() {
        return lotteryType;
    }

    public void setLotteryType(LotteryType lotteryType) {
        this.lotteryType = lotteryType;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    public static GiftLottery getByJson(String json) {
        return new Gson().fromJson(json, GiftLottery.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
