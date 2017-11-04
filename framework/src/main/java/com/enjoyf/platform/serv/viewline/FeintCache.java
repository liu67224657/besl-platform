package com.enjoyf.platform.serv.viewline;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-7-4
 * Time: 下午1:40
 * To change this template use File | Settings | File Templates.
 */
public class FeintCache implements Serializable {
    private int count = 20;
    private Date startDate;
    private int errorNum = 0;
    private String contentID;
    private String contentUNO;

    public FeintCache() {
    }

    public FeintCache(int count, Date startDate) {
        this.count = count;
        this.startDate = startDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(int errorNum) {
        this.errorNum = errorNum;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getContentUNO() {
        return contentUNO;
    }

    public void setContentUNO(String contentUNO) {
        this.contentUNO = contentUNO;
    }
}
