package com.enjoyf.platform.service.billing;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-1-10
 * Time: 下午2:08
 * To change this template use File | Settings | File Templates.
 */
public class BillingConstants {
    public static final String SERVICE_SECTION = "billingservice";
    public static final String SERVICE_PREFIX = "billingserver";
    public static final String SERVICE_TYPE = "billingserver";

    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    public static final byte DEPOSIT_LOG_CREATE = 1;
    public static final byte DEPOSIT_SYNC_MODIFY = 2;

    public static final byte QUERY_DEPOSITLOG_LIST = 3;

    public static final byte QUERY_BY_SQL = 4;

    public static final byte CHECK_RECEIPT = 5;
    public static final byte SET_RECEIPT = 6;

    static {
        //
        transProfileContainer.put(new TransProfile(DEPOSIT_LOG_CREATE, "DEPOSIT_LOG_CREATE"));
        transProfileContainer.put(new TransProfile(DEPOSIT_SYNC_MODIFY, "DEPOSIT_LOG_MODIFY"));
        transProfileContainer.put(new TransProfile(QUERY_DEPOSITLOG_LIST, "QUERY_DEPOSITLOG_LIST"));
        transProfileContainer.put(new TransProfile(QUERY_BY_SQL, "QUERY_BY_SQL"));

        transProfileContainer.put(new TransProfile(CHECK_RECEIPT, "CHECK_RECEIPT"));
        transProfileContainer.put(new TransProfile(SET_RECEIPT, "SET_RECEIPT"));
    }


    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }

}
