package com.enjoyf.platform.service.billing;

import com.enjoyf.util.MD5Util;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/9/8
 * Description:
 */
public class BillingUtil {

    public static String getLogId(String orderId, DepositChannel channel) {
        return MD5Util.Md5(orderId + channel.getCode());
    }

    public static String getBalanceId(String profileId, String appKey, String zoneKey) {
        return MD5Util.Md5(profileId + appKey + zoneKey);
    }
}
