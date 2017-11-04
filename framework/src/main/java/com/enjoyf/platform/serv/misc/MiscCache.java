package com.enjoyf.platform.serv.misc;

import com.enjoyf.platform.service.misc.MiscConstants;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-5-13
 * Time: 下午8:21
 * To change this template use File | Settings | File Templates.
 */
public class MiscCache {

    private static final int PHONE_LIMIT_OUT_SEC = 24 * 60 * 60;

    private static final int PHONE_LIMIT_OUT_MONTH_SEC = 24 * 60 * 60 *30;
    private MemCachedConfig config;

    private MemCachedManager manager;

    private static final String KEY_SMSSENDER_COUNTER = "_smssendercounter_";
    private static final String KEY_SMSSENDER_COUNTER_SUCCESS = "_smssendercounter_success_";
    private static final String PHONE_SMS_LIMIT = "p_sms_limit";


    MiscCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(this.config);
    }

    //////////////////////////////////////////////////////////
    public void saveMiscValue(String key, String value, long sec) {
        manager.put(MiscConstants.SERVICE_SECTION + key, value, sec);
    }

    public String getMiscValue(String key) {
        Object object = manager.get(MiscConstants.SERVICE_SECTION + key);
        if (object != null) {
            return String.valueOf(object);
        }
        return null;
    }

    public boolean removeMiscValue(String key) {
        return manager.remove(MiscConstants.SERVICE_SECTION + key);
    }

    public void addSMSSender(Date date) {
        manager.addOrIncr(MiscConstants.SERVICE_SECTION + KEY_SMSSENDER_COUNTER + DateUtil.formatDateToString(date, "yyyyMMddHH"), 1l, 60 * 60 * 2);
    }

    public void addSMSSenderSuccess(Date date) {
        manager.addOrIncr(MiscConstants.SERVICE_SECTION + KEY_SMSSENDER_COUNTER_SUCCESS + DateUtil.formatDateToString(date, "yyyyMMddHH"), 1l, 60 * 60 * 2);
    }

    public long incrSMSLimit(String phone, int times) {
        return manager.addOrIncr(PHONE_SMS_LIMIT + phone, times, PHONE_LIMIT_OUT_MONTH_SEC);
    }

    public int getSMSLimit(String phone) {
        Object obj = manager.get(PHONE_SMS_LIMIT + phone);

        if (obj != null) {
            return Integer.parseInt(String.valueOf(obj));
        }
        return 0;
    }
}
