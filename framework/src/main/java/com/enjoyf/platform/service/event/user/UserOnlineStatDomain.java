/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.user;

import com.enjoyf.platform.service.stats.AbstractStatDomain;
import com.enjoyf.platform.service.stats.StatDomainPrefix;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-17 下午8:17
 * Description:
 */
public class UserOnlineStatDomain extends AbstractStatDomain {
    //
    public static UserOnlineStatDomain ONLINE_USER = new UserOnlineStatDomain("online", false);

    protected UserOnlineStatDomain(String c, boolean multi) {
        super(StatDomainPrefix.DOMAIN_PREFIX_USER_EVENT.getCode() + StatDomainPrefix.KEY_DOMAIN_SEPARATOR + c, multi);
    }
}
