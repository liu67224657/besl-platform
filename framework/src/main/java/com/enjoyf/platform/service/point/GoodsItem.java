package com.enjoyf.platform.service.point;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午2:30
 * To change this template use File | Settings | File Templates.
 */
public class GoodsItem implements Serializable {

    private long goodsItemId;
    private long goodsId;

    private String snName1;
    private String snValue1;
    private String snName2;
    private String snValue2;
    private String snName3;
    private String snValue3;

    private ActStatus exchangeStatus = ActStatus.UNACT;
    private String ownUserNo;
    private Date consumeDate;
    private Date createDate;
    private String profileId;

    public long getGoodsItemId() {
        return goodsItemId;
    }

    public void setGoodsItemId(long goodsItemId) {
        this.goodsItemId = goodsItemId;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getSnName1() {
        return snName1;
    }

    public void setSnName1(String snName1) {
        this.snName1 = snName1;
    }

    public String getSnValue1() {
        return snValue1;
    }

    public void setSnValue1(String snValue1) {
        this.snValue1 = snValue1;
    }

    public String getSnName2() {
        return snName2;
    }

    public void setSnName2(String snName2) {
        this.snName2 = snName2;
    }

    public String getSnValue2() {
        return snValue2;
    }

    public void setSnValue2(String snValue2) {
        this.snValue2 = snValue2;
    }

    public String getSnName3() {
        return snName3;
    }

    public void setSnName3(String snName3) {
        this.snName3 = snName3;
    }

    public String getSnValue3() {
        return snValue3;
    }

    public void setSnValue3(String snValue3) {
        this.snValue3 = snValue3;
    }

    public ActStatus getExchangeStatus() {
        return exchangeStatus;
    }

    public void setExchangeStatus(ActStatus exchangeStatus) {
        this.exchangeStatus = exchangeStatus;
    }

    public String getOwnUserNo() {
        return ownUserNo;
    }

    public void setOwnUserNo(String ownUserNo) {
        this.ownUserNo = ownUserNo;
    }

    public Date getConsumeDate() {
        return consumeDate;
    }

    public void setConsumeDate(Date consumeDate) {
        this.consumeDate = consumeDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
