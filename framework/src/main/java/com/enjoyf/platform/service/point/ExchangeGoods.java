package com.enjoyf.platform.service.point;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午12:52
 * To change this template use File | Settings | File Templates.
 */
public class ExchangeGoods implements Serializable {

    private long goodsId;
    private String goodsName;
    private String goodsDesc;
    private String goodsPic;
    private GoodsType goodsType;

    private Date startTime;
    private Date endTime;

    private int goodsAmount;
    private int goodsResetAmount;
    private int goodsConsumePoint;

    private ConsumeTimesType exchangeTimeType;
    private int exchangeIntrval;

    private ConsumeTimesType taoTimesType;
    private int taoIntrval;

    private int displayOrder;

    private boolean isNew;
    private boolean isHot;

    private ValidStatus validStatus = ValidStatus.INVALID;

    private Date createDate;
    private String createIp;
    private String createUserId;
    private long shareId;
    private String noticeBody;
    private String detailUrl;

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    public GoodsType getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(GoodsType goodsType) {
        this.goodsType = goodsType;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(int goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public int getGoodsResetAmount() {
        return goodsResetAmount;
    }

    public void setGoodsResetAmount(int goodsResetAmount) {
        this.goodsResetAmount = goodsResetAmount;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(boolean hot) {
        isHot = hot;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public long getShareId() {
        return shareId;
    }

    public void setShareId(long shareId) {
        this.shareId = shareId;
    }

    public String getNoticeBody() {
        return noticeBody;
    }

    public void setNoticeBody(String noticeBody) {
        this.noticeBody = noticeBody;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public ConsumeTimesType getExchangeTimeType() {
        return exchangeTimeType;
    }

    public void setExchangeTimeType(ConsumeTimesType exchangeTimeType) {
        this.exchangeTimeType = exchangeTimeType;
    }


    public ConsumeTimesType getTaoTimesType() {
        return taoTimesType;
    }

    public void setTaoTimesType(ConsumeTimesType taoTimesType) {
        this.taoTimesType = taoTimesType;
    }

    public int getExchangeIntrval() {
        return exchangeIntrval;
    }

    public void setExchangeIntrval(int exchangeIntrval) {
        this.exchangeIntrval = exchangeIntrval;
    }

    public int getTaoIntrval() {
        return taoIntrval;
    }

    public void setTaoIntrval(int taoIntrval) {
        this.taoIntrval = taoIntrval;
    }

    public int getGoodsConsumePoint() {
        return goodsConsumePoint;
    }

    public void setGoodsConsumePoint(int goodsConsumePoint) {
        this.goodsConsumePoint = goodsConsumePoint;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
