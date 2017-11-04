package com.enjoyf.platform.service.point;

import java.io.Serializable;

/**
 * Created by zhitaoshi on 2015/7/27.
 */
public class ActivityGoodsDTO implements Serializable {
    //商品
    private long goodsId;
    private int goodsType;
    private String goodsName;
    private String goodsDesc;
    private String goodsPic;
    //库存
    private long goodsItemId;
    private String itemName1;
    private String itemValue1;
    private String itemName2;
    private String itemValue2;
    //订单号
    private long consumeOrder;
    //秒杀ID
    private long seckillId;

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
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

    public long getGoodsItemId() {
        return goodsItemId;
    }

    public void setGoodsItemId(long goodsItemId) {
        this.goodsItemId = goodsItemId;
    }

    public String getItemName1() {
        return itemName1;
    }

    public void setItemName1(String itemName1) {
        this.itemName1 = itemName1;
    }

    public String getItemValue1() {
        return itemValue1;
    }

    public void setItemValue1(String itemValue1) {
        this.itemValue1 = itemValue1;
    }

    public String getItemName2() {
        return itemName2;
    }

    public void setItemName2(String itemName2) {
        this.itemName2 = itemName2;
    }

    public String getItemValue2() {
        return itemValue2;
    }

    public void setItemValue2(String itemValue2) {
        this.itemValue2 = itemValue2;
    }

    public long getConsumeOrder() {
        return consumeOrder;
    }

    public void setConsumeOrder(long consumeOrder) {
        this.consumeOrder = consumeOrder;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }
}
