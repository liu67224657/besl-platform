/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.tools.stats;

import com.enjoyf.platform.service.stats.AbstractStatDomain;
import com.enjoyf.platform.service.stats.StatDomainPrefix;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-17 下午8:17
 * Description:
 */
public class StatDomainAdvertise extends AbstractStatDomain {

    public static StatDomainAdvertise ADVERTISE_STAT_PAGE_VIEW = new StatDomainAdvertise("pv", true);

    public static StatDomainAdvertise ADVERTISE_STAT_USER_VIEW = new StatDomainAdvertise("uv", true);

    public static StatDomainAdvertise ADVERTISE_STAT_VIEW_BOUNCE = new StatDomainAdvertise("vb", true);

    public static StatDomainAdvertise ADVERTISE_STAT_USER_RETAINED = new StatDomainAdvertise("ur", true);

    public static StatDomainAdvertise ADVERTISE_STAT_VIEW_TIME = new StatDomainAdvertise("vt", true);

    public static StatDomainAdvertise ADVERTISE_STAT_VIEW_DEPTH = new StatDomainAdvertise("vd", true);

    protected StatDomainAdvertise(String c, boolean multi) {
        super(StatDomainPrefix.DOMAIN_PREFIX_ADVERTISE.getCode() + StatDomainPrefix.KEY_DOMAIN_SEPARATOR + c, multi);
    }
}
