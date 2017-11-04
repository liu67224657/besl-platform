package com.enjoyf.webapps.joyme.dto.activity;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-6
 * Time: 上午10:41
 * To change this template use File | Settings | File Templates.
 */
public class EventActivityDTO {
    private long aid;
    private String name;
    private int count;
    private int restamount;

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRestamount() {
        return restamount;
    }

    public void setRestamount(int restamount) {
        this.restamount = restamount;
    }
}
