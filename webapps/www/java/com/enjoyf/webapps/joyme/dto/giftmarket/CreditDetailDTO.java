package com.enjoyf.webapps.joyme.dto.giftmarket;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-30
 * Time: 上午10:00
 * To change this template use File | Settings | File Templates.
 */
public class CreditDetailDTO {

    private String screenName;
    private String description;
    private Date date;
    private int point;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
