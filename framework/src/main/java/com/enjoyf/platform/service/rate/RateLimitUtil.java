package com.enjoyf.platform.service.rate;

import java.util.UUID;

import com.enjoyf.platform.service.rate.key.RateKey;
import com.enjoyf.platform.service.rate.key.RatingIPRateKey;
import com.enjoyf.platform.service.rate.key.RatingUUIDRateKey;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * This utility class lets you avoid calling RateServiceSngl and simplify your code
 * @author Daniel
 */
public class RateLimitUtil {

    /**
     *
     * @param userId
     * @return true if we can proceed
     */
    public static boolean checkAddGameRating(UUID userId) {
        RatingUUIDRateKey key = new RatingUUIDRateKey(RateKeyDomain.GAME_RATING_ADD_FREQUENCY, userId);
        key.getDomain().setLimit(new RateLimit(30, 1));
        return check(key);
    }

    /**
     *
     * @param ipaddress
     * @return true if we can proceed
     */
    public static boolean checkAddGameRating(String ipaddress) {
        RatingIPRateKey key = new RatingIPRateKey(RateKeyDomain.GAME_RATING_ADD_FREQUENCY, ipaddress);
        key.getDomain().setLimit(new RateLimit(300, 10));
        return check(key);
    }

    private static boolean check(RateKey key) {
        try {
            Rate rate = RateCheckServiceSngl.get().recordCount(key);
            if (rate != null) {
                return !rate.isExceeded();
            }
        } catch (ServiceException e) {
            GAlerter.lab("Rate Limit does not work for the key", key.toString(), e);
        }
        return true;//if rate limit is down, let it go
    }
}
