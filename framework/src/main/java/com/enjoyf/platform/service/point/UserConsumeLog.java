package com.enjoyf.platform.service.point;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.usercenter.Address;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-18
 * Time: 下午3:30
 * To change this template use File | Settings | File Templates.
 */
public class UserConsumeLog implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(UserConsumeLog.class);

    private long userConsumeLogId;
    private String userNo;
    private long goodsId;
    private String goodsName;
    private String goodsPic;
    private String goodsDesc;
    private long goodsItemId;
    private GoodsType goodsType;
    private int consumeAmount;
    private ConsumeType consumeType = ConsumeType.POINT;

    private Date consumeDate;
    private Date consumeTime;
    private String consumeIp;

    private String profileId;
    private GoodsActionType goodsActionType;
    private String appkey;
    private ValidStatus validStatus;
    private Address address;
    private long consumeOrder;//dingdanhao

    public long getConsumeOrder() {
        return consumeOrder;
    }

    public void setConsumeOrder(long consumeOrder) {
        this.consumeOrder = consumeOrder;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public long getUserConsumeLogId() {
        return userConsumeLogId;
    }

    public void setUserConsumeLogId(long userConsumeLogId) {
        this.userConsumeLogId = userConsumeLogId;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

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

    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public long getGoodsItemId() {
        return goodsItemId;
    }

    public void setGoodsItemId(long goodsItemId) {
        this.goodsItemId = goodsItemId;
    }

    public GoodsType getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(GoodsType goodsType) {
        this.goodsType = goodsType;
    }

    public Date getConsumeDate() {
        return consumeDate;
    }

    public void setConsumeDate(Date consumeDate) {
        this.consumeDate = consumeDate;
    }

    public Date getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(Date consumeTime) {
        this.consumeTime = consumeTime;
    }

    public String getConsumeIp() {
        return consumeIp;
    }

    public void setConsumeIp(String consumeIp) {
        this.consumeIp = consumeIp;
    }

    public int getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(int consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    public ConsumeType getConsumeType() {
        return consumeType;
    }

    public void setConsumeType(ConsumeType consumeType) {
        this.consumeType = consumeType;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public GoodsActionType getGoodsActionType() {
        return goodsActionType;
    }

    public void setGoodsActionType(GoodsActionType goodsActionType) {
        this.goodsActionType = goodsActionType;
    }

    public static Address parse(String jsonStr) {
        Address json = null;
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                json = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<Address>() {
                });

            } catch (IOException e) {
                logger.error("json parse error, jsonStr:" + jsonStr, e);
            }
        }
        return json;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
