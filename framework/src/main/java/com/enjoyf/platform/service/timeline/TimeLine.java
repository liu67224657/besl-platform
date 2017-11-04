/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.timeline;

import com.enjoyf.platform.util.Pagination;

import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午4:47
 * Description: this a composite object which includes many messages.
 */
public class TimeLine {
    //
    private String ownUno;

    //
    private TimeLineDomain domain;

    //
    private List<TimeLineItem> messages;

    //
    private Pagination page;

    //
    private Date lastUpdateDate;
}
