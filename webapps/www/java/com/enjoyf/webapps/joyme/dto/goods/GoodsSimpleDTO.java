package com.enjoyf.webapps.joyme.dto.goods;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-26
 * Time: 下午4:32
 * To change this template use File | Settings | File Templates.
 */
public class GoodsSimpleDTO {
    private long goodsId;
    private int goodsType;
    private String goodsName;
    private long goodsItemId;
    private String itemName1;
    private String itemValue1;
    private String itemName2;
    private String itemValue2;
    private long consumeOrder;


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

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
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
}
