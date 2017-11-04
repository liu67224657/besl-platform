/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.userprops;

import com.enjoyf.platform.service.userprops.UserPropDomain;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class UserPropsConfig {
    //the value is the domain code.
    //so different domain can use the different database.
    private Map<UserPropDomain, String> dataSourceNameMap = new HashMap<UserPropDomain, String>();

    private static final String SEPARATOR = ":";

    //the props.
    private FiveProps props;

    //
    private int increaseMaxTryTimes = 3;

    //the keys
    private static final String KEY_DOMAIN_DSN_MAP = "userprops.domain.dsn.map";
    private static final String KEY_INCREASE_MAX_TRY_TIMES = "userprops.increase.max.trytimes";

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public UserPropsConfig(FiveProps servProps) {
        props = servProps;

        init();
    }

    private void init() {
        //db configure
        if (props == null) {
            GAlerter.lab("UserPropsConfig props is null.");
            return;
        }

        //read the database source names.
        List<String> domainDsnStrs = props.getList(KEY_DOMAIN_DSN_MAP);
        for (String m : domainDsnStrs) {
            StringTokenizer tokenizer = new StringTokenizer(m, SEPARATOR);

            if (tokenizer.countTokens() == 2) {
                String dc = tokenizer.nextToken();
                String dsn = tokenizer.nextToken();

                UserPropDomain domain = UserPropDomain.getByCode(dc);
                if (domain != null) {
                    dataSourceNameMap.put(domain, dsn);
                }
            }
        }

        //
        increaseMaxTryTimes = props.getInt(KEY_INCREASE_MAX_TRY_TIMES, increaseMaxTryTimes);
    }


    public Map<UserPropDomain, String> getDataSourceNameMap() {
        return dataSourceNameMap;
    }

    public FiveProps getProps() {
        return props;
    }

    public int getIncreaseMaxTryTimes() {
        return increaseMaxTryTimes;
    }

    /////////////////////////////////////////////////
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
