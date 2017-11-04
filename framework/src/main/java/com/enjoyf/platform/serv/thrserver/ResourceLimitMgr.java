/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.util.HashMap;
import java.util.Map;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.util.RateLimiter;

/**
 * Class used to manage the rate limiting of transactions which
 * exceed the resource limits.
 */
public class ResourceLimitMgr {
    private final ResourceLimitCfg rlCfg;

    /**
     * A Map of TransProfile -> RateLimiter objects.
     */
    private final Map limitersMap = new HashMap();

    /**
     * @param cfg The cfg object specifying the rate limiting settings.
     */
    public ResourceLimitMgr(ResourceLimitCfg cfg) {
        if (cfg == null) {
            throw new IllegalArgumentException("ResourceLimitMgr.ctor: cfg is NULL!");
        }
        rlCfg = cfg;
    }

    /**
     * Note the fact that this transaction encountered an error.
     * It will increment the current number of failures so that we can
     * later ask if this transaction has been rate limited.
     * The error might not be a resource error, so that won't count.
     *
     * @param transProfile The transaction profile.
     * @param dbe          The DbException encountered.
     */
    public void gotError(TransProfile transProfile, DbException dbe) {
        if (!rlCfg.isEnabled()) {
            return;
        }

//        if (!dbe.equals(DbException.RESOURCE_LIMIT_EXCEEDED)) {
//            return;
//        }
        //todo

        RateLimiter rl = getRateLimiter(transProfile);
        rl.isLimited(1);
    }

    /**
     * Ask if the transaction type is currently rate limited even though
     * we have not run into any error at this point.
     */
    public boolean isLimited(TransProfile transProfile) {
        if (!rlCfg.isEnabled()) {
            return false;
        }

        RateLimiter rl = getRateLimiter(transProfile);
        return rl.isLimited();
    }

    private synchronized RateLimiter getRateLimiter(TransProfile transProfile) {
        RateLimiter rl = (RateLimiter) limitersMap.get(transProfile);
        if (rl == null) {
            rl = new RateLimiter(rlCfg.getPeriod(), rlCfg.getMaxFailedRequestsPerPeriod());
            limitersMap.put(transProfile, rl);
        }
        return rl;
    }
}
