package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by pengxu on 2016/12/1.
 */
public class UserLotteryLog implements Serializable {
    private String userLotteryLogId;//md5(PID+giftLotteryId);
    private long giftLotteryId;
    private String giftLotteryName;
    private String profileId;
    private Date lotteryDate;

    public String getUserLotteryLogId() {
        return userLotteryLogId;
    }

    public void setUserLotteryLogId(String userLotteryLogId) {
        this.userLotteryLogId = userLotteryLogId;
    }

    public long getGiftLotteryId() {
        return giftLotteryId;
    }

    public void setGiftLotteryId(long giftLotteryId) {
        this.giftLotteryId = giftLotteryId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public Date getLotteryDate() {
        return lotteryDate;
    }

    public void setLotteryDate(Date lotteryDate) {
        this.lotteryDate = lotteryDate;
    }

    public String getGiftLotteryName() {
        return giftLotteryName;
    }

    public void setGiftLotteryName(String giftLotteryName) {
        this.giftLotteryName = giftLotteryName;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
