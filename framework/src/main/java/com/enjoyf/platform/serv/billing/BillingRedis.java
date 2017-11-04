/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.billing;


import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.service.billing.BillingConstants;
import com.enjoyf.platform.service.usercenter.UserCenterUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

/**
 * @author <a href=mailto:ericliu@staff.joyme.com>Eirc Liu</a>
 */

public class BillingRedis {
    //
    private static final Logger logger = LoggerFactory.getLogger(BillingRedis.class);

    private static final String KEY_PREFIX = BillingConstants.SERVICE_SECTION;

    private static final String KEY_FAILED_DEPOSITLOG_KEY = "_fdplog";

    private static final String KEY_CHECK_APPSTORE_RECEIPT = "_check_appstore_receipt_";

    private RedisManager manager;

    public BillingRedis(FiveProps p) {
        manager = new RedisManager(p);
    }


    public void pushFailedDepositLog(String... dplogIds) {
        manager.lpush(KEY_PREFIX + KEY_FAILED_DEPOSITLOG_KEY, dplogIds);
    }

    public void pushFailedDepositLog(String dplogId) {
        manager.lpush(KEY_PREFIX + KEY_FAILED_DEPOSITLOG_KEY, dplogId);
    }

    public String popFailedDepositLog() {
        return manager.rpop(KEY_PREFIX + KEY_FAILED_DEPOSITLOG_KEY);
    }

    public long getFailedDepositLogLength() {
        return manager.length(KEY_PREFIX + KEY_FAILED_DEPOSITLOG_KEY);
    }

    public boolean deleteLogs() {
        return manager.remove(KEY_PREFIX + KEY_FAILED_DEPOSITLOG_KEY) > 0;
    }
    ////////////////

    public boolean checkReceipt(String receipt) {
        return manager.keyExists(KEY_PREFIX + KEY_CHECK_APPSTORE_RECEIPT + MD5Util.Md5(receipt));
    }

    public void setReceipt(String receipt) {
        manager.set(KEY_PREFIX + KEY_CHECK_APPSTORE_RECEIPT + MD5Util.Md5(receipt), receipt);
    }
}


