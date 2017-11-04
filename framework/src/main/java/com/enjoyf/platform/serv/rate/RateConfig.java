/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.rate;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @author Yin Pengyi
 */
class RateConfig {
    private static String KEY_CLEAN_INTERVAL_SEC = "rate.cleaning.clean.sec";

    private int cleanIntervalSec = 900;

    public RateConfig(FiveProps props) {
        if (props == null) {
            return;
        }

        cleanIntervalSec = props.getInt(KEY_CLEAN_INTERVAL_SEC, cleanIntervalSec);
    }

    /**
     * Gets the cleaning cleanIntervalSec in seconds
     *
     * @return int - master table cleaning cleanIntervalSec
     */
    public int getIntervalSecs() {
        return cleanIntervalSec;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
