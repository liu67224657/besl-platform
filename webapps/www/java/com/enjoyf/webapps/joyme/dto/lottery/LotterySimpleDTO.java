package com.enjoyf.webapps.joyme.dto.lottery;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-7-5
 * Time: 下午2:52
 * To change this template use File | Settings | File Templates.
 */
public class LotterySimpleDTO {

    private long lotteryId;
    private String lotteryName;
    private String lotteryDesc;
    private int lotteryTimesType;

    private long awardId;
    private String awardName;
    private String awardDesc;
    private String awardPic;
    private int awardLevel;
    private int awardType;

    private long awardItemId;
    private String itemName1;
    private String itemValue1;
    private String itemName2;
    private String itemValue2;

    public long getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(long lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getLotteryName() {
        return lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public String getLotteryDesc() {
        return lotteryDesc;
    }

    public void setLotteryDesc(String lotteryDesc) {
        this.lotteryDesc = lotteryDesc;
    }

    public int getLotteryTimesType() {
        return lotteryTimesType;
    }

    public void setLotteryTimesType(int lotteryTimesType) {
        this.lotteryTimesType = lotteryTimesType;
    }

    public long getAwardId() {
        return awardId;
    }

    public void setAwardId(long awardId) {
        this.awardId = awardId;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public String getAwardDesc() {
        return awardDesc;
    }

    public void setAwardDesc(String awardDesc) {
        this.awardDesc = awardDesc;
    }

    public String getAwardPic() {
        return awardPic;
    }

    public void setAwardPic(String awardPic) {
        this.awardPic = awardPic;
    }

    public int getAwardLevel() {
        return awardLevel;
    }

    public void setAwardLevel(int awardLevel) {
        this.awardLevel = awardLevel;
    }

    public int getAwardType() {
        return awardType;
    }

    public void setAwardType(int awardType) {
        this.awardType = awardType;
    }

    public long getAwardItemId() {
        return awardItemId;
    }

    public void setAwardItemId(long awardItemId) {
        this.awardItemId = awardItemId;
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
}
