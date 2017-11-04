/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content.stats;

import com.enjoyf.platform.service.stats.AbstractStatDomain;
import com.enjoyf.platform.service.stats.StatDomainPrefix;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-17 下午8:17
 * Description:
 */
public class ContentStatDomain extends AbstractStatDomain {
    //
    public static ContentStatDomain CONTENT_TOP_INTERACTION_3DAY = new ContentStatDomain("top.act.3day", true);
    public static ContentStatDomain CONTENT_TOP_FAVOR_3DAY = new ContentStatDomain("top.fav.3day", true);
    public static ContentStatDomain CONTENT_TOP_REPLY_3DAY = new ContentStatDomain("top.rep.3day", true);

    protected ContentStatDomain(String c, boolean multi) {
        super(StatDomainPrefix.DOMAIN_PREFIX_CONTENT.getCode() + StatDomainPrefix.KEY_DOMAIN_SEPARATOR + c, multi);
    }
}
