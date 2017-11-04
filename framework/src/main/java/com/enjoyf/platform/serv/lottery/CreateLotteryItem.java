package com.enjoyf.platform.serv.lottery;/**
 * Created by ericliu on 16/6/18.
 */

import com.enjoyf.platform.service.lottery.LotteryAwardItem;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/6/18
 */
class CreateLotteryItem implements Serializable {
    private long awardId;
    private List<LotteryAwardItem> item;

    public CreateLotteryItem(long awardId, List<LotteryAwardItem> item) {
        this.awardId = awardId;
        this.item = item;
    }

    public long getAwardId() {
        return awardId;
    }

    public List<LotteryAwardItem> getItem() {
        return item;
    }
}