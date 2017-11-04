package com.enjoyf.platform.service.lottery;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-18
 * Time: 下午3:30
 * To change this template use File | Settings | File Templates.
 */
public class UserLotteryLog implements Serializable {
    private long userLotteryId;
    private String uno;
    private String screenName;

    private long lotteryId;

    //todo 抽中结果
    private long lotteryAwardId;
    private String lotteryAwardName;
    private String lotteryAwardDesc;
    private String lotteryAwardPic;
    private int lotteryAwardLevel;

    private long lotteryAwardItemId;
    private String name1;
    private String value1;
    private String name2;
    private String value2;

    private Date lotteryDate;
    private String lotteryIp;

    private String lottery_code;
    private String extension;


    public long getUserLotteryId() {
        return userLotteryId;
    }

    public void setUserLotteryId(long userLotteryId) {
        this.userLotteryId = userLotteryId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public long getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(long lotteryId) {
        this.lotteryId = lotteryId;
    }

    public long getLotteryAwardId() {
        return lotteryAwardId;
    }

    public void setLotteryAwardId(long lotteryAwardId) {
        this.lotteryAwardId = lotteryAwardId;
    }

    public String getLotteryAwardName() {
        return lotteryAwardName;
    }

    public void setLotteryAwardName(String lotteryAwardName) {
        this.lotteryAwardName = lotteryAwardName;
    }

    public String getLotteryAwardDesc() {
        return lotteryAwardDesc;
    }

    public void setLotteryAwardDesc(String lotteryAwardDesc) {
        this.lotteryAwardDesc = lotteryAwardDesc;
    }

    public String getLotteryAwardPic() {
        return lotteryAwardPic;
    }

    public void setLotteryAwardPic(String lotteryAwardPic) {
        this.lotteryAwardPic = lotteryAwardPic;
    }

    public int getLotteryAwardLevel() {
        return lotteryAwardLevel;
    }

    public void setLotteryAwardLevel(int lotteryAwardLevel) {
        this.lotteryAwardLevel = lotteryAwardLevel;
    }

    public long getLotteryAwardItemId() {
        return lotteryAwardItemId;
    }

    public void setLotteryAwardItemId(long lotteryAwardItemId) {
        this.lotteryAwardItemId = lotteryAwardItemId;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public Date getLotteryDate() {
        return lotteryDate;
    }

    public void setLotteryDate(Date lotteryDate) {
        this.lotteryDate = lotteryDate;
    }

    public String getLotteryIp() {
        return lotteryIp;
    }

    public void setLotteryIp(String lotteryIp) {
        this.lotteryIp = lotteryIp;
    }

    public String getLottery_code() {
        return lottery_code;
    }

    public void setLottery_code(String lottery_code) {
        this.lottery_code = lottery_code;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
