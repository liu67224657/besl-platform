package com.enjoyf.platform.service.content;

import java.io.Serializable;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-10-28 下午6:42
 * Description:
 */
public class ActivityCooperation implements Serializable {

    //91
    public static final int COOP_91 = 1;

    //360
    public static final int COOP_360 = 2;

    //
    public static final int ALL = 3;

    private int value = 0;

    //
    public ActivityCooperation() {
    }

    private ActivityCooperation(int v) {
        this.value = v;
    }

    public ActivityCooperation has(int v) {
        value += v;

        return this;
    }

    public boolean hasJiuYao() {
        return (value & COOP_91) > 0;
    }

    public boolean hasSanLiuLing() {
        return (value & COOP_360) > 0;
    }

    public boolean hasAll() {
        return value == ALL;
    }

    public int getValue() {
        return value;
    }

    public static ActivityCooperation getByValue(int v){
        return new ActivityCooperation(v);
    }

    public static void main(String[] args) {
        System.out.println(2&3);
    }

}
