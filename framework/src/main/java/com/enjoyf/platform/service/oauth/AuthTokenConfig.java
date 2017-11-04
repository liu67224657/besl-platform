/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.oauth;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-8 上午10:18
 * Description:
 */
public class AuthTokenConfig {
    //
    private boolean storeInTable = true;
    private long expireTimeSecs = 24l * 60 * 60;
    private int maxAccessTimes = 1;

    ///////////////////////
    public boolean isStoreInTable() {
        return storeInTable;
    }

    public void setStoreInTable(boolean storeInTable) {
        this.storeInTable = storeInTable;
    }

    public long getExpireTimeSecs() {
        return expireTimeSecs;
    }

    public void setExpireTimeSecs(long expireTimeSecs) {
        this.expireTimeSecs = expireTimeSecs;
    }

    public int getMaxAccessTimes() {
        return maxAccessTimes;
    }

    public void setMaxAccessTimes(int maxAccessTimes) {
        this.maxAccessTimes = maxAccessTimes;
    }

    //
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
