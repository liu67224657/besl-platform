/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.billing;

import com.enjoyf.platform.service.billing.BillingConstants;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
class DepositLogCache {
    private static final long TIME_OUT_SEC = 60l * 60l * 6l;

    private static final String PREFIX_DEPOSITLOGID = "_dplogid_";


    private MemCachedConfig config;

    private MemCachedManager manager;

    DepositLogCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(this.config);
    }

    //////////////////////////////////////////////////////////
    public void lockLog(String logId) {
        manager.put(BillingConstants.SERVICE_SECTION + PREFIX_DEPOSITLOGID + logId, 1, TIME_OUT_SEC);
    }

    public boolean isLockLog(String logId) {
        return manager.get(BillingConstants.SERVICE_SECTION + PREFIX_DEPOSITLOGID + logId) != null;
    }

    public boolean unlock(String logId) {
        return manager.remove(BillingConstants.SERVICE_SECTION + PREFIX_DEPOSITLOGID + logId);
    }

}
