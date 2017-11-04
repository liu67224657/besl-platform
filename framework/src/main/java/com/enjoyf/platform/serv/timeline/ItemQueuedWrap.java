package com.enjoyf.platform.serv.timeline;

import com.enjoyf.platform.service.timeline.TimeLineItem;

import java.io.Serializable;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class ItemQueuedWrap implements Serializable {
    private TimeLineItem timeLineItem;
    private ItemQueuedProcessStrategyCode strategyCode;
    private String detailUno;

    ItemQueuedWrap(TimeLineItem timeLineItem, ItemQueuedProcessStrategyCode strategyCode) {
        this.timeLineItem = timeLineItem;
        this.strategyCode = strategyCode;
    }

    public TimeLineItem getTimeLineItem() {
        return timeLineItem;
    }

    public ItemQueuedProcessStrategyCode getStrategyCode() {
        return strategyCode;
    }

    public String getDetailUno() {
        return detailUno;
    }

    public void setDetailUno(String detailUno) {
        this.detailUno = detailUno;
    }
}
