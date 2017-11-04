package com.enjoyf.platform.service.content;

import java.io.Serializable;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-10-28 下午6:42
 * Description:
 */
public class ActivityPlatform implements Serializable {
    public static final int PLATFORM_ANDORID = 1;
    public static final int PLATFORM_IOS = 2;
    public static final int PLATFORM_ALL = 3;

    private int value = 0;

    //
    public ActivityPlatform() {
    }

    private ActivityPlatform(int v) {
        this.value = v;
    }

    public ActivityPlatform has(int v) {
        value += v;

        return this;
    }

    public boolean hasAndroid() {
        return (value & PLATFORM_ANDORID) > 0;
    }

    public boolean hasIos() {
        return (value & PLATFORM_IOS) > 0;
    }

    public boolean hasAll() {
        return value == PLATFORM_ALL;
    }

    public int getValue() {
        return value;
    }

    public static ActivityPlatform getByValue(int v){
        return new ActivityPlatform(v);
    }
}
